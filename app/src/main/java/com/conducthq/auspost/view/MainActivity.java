package com.conducthq.auspost.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.conducthq.auspost.R;
import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.model.bus.DashboardResume;
import com.conducthq.auspost.view.EventRsvp.EventInfoFragment;
import com.conducthq.auspost.view.Fragments.BadgesFragment;
import com.conducthq.auspost.view.Fragments.DashboardFragment;
import com.conducthq.auspost.view.Fragments.MoreFragment;
import com.conducthq.auspost.view.Fragments.ProfileViewFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by conduct19 on 28/10/2016.
 */

public class MainActivity extends BaseActivity {

    SwipeRefreshLayout swipeContainer;
    RecyclerView recyclerView;
    private SharedPreferences prefs;
    FloatingActionButton buttonNewMessage;

    boolean doubleBackToExitPressedOnce = false;
    private BottomBar bottomBar;
    private EventBus eventBus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTopActionBar(R.string.dashboard_title, -1, -1);

        eventBus.register(this);
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new BottomTabListener(getApplicationContext()));

        replaceFragment(android.R.id.content,
                new DashboardFragment(),
                DashboardFragment.FRAGMENT_TAG,
                null);

    }

    public class BottomTabListener implements OnTabSelectListener {
        private Context context;

        public BottomTabListener(Context context) {
            this.context = context;
        }

        @Override
        public void onTabSelected(@IdRes int tabId) {
            switch (tabId)  {
                case R.id.tab_dash:
                    setTopActionBar(R.string.dashboard_title, -1, -1);
                    replaceFragment(android.R.id.content,
                            new DashboardFragment(),
                            DashboardFragment.FRAGMENT_TAG,
                            null);
                    break;
                case R.id.tab_badges:
                    setTopActionBar(R.string.badges_title, -1, Constants.BTN_INFO);
                    replaceFragment(android.R.id.content,
                            new BadgesFragment(),
                            BadgesFragment.FRAGMENT_TAG,
                            null);

                    if (actionBarInfo != null) {
                        actionBarInfo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                lockedInfo();
                            }
                        });
                    }
                    return;
                case R.id.tab_event:
                    setTopActionBar(R.string.event_info_title, -1, -1);
                    replaceFragment(android.R.id.content,
                            new EventInfoFragment(),
                            EventInfoFragment.FRAGMENT_TAG,
                            null);
                    break;
                case R.id.tab_profile:
                    setTopActionBar(R.string.profile_view_title, -1, Constants.BTN_EDIT);
                    replaceFragment(android.R.id.content,
                            new ProfileViewFragment(),
                            ProfileViewFragment.FRAGMENT_TAG,
                            null);

                    if (actionEdit != null) {
                        actionEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
                                overridePendingTransition( R.anim.slide_in_up, R.anim.stay);
                            }
                        });
                    }

                    break;
                case R.id.tab_more:
                    setTopActionBar(R.string.more_title, -1, -1);
                    replaceFragment(android.R.id.content,
                            new MoreFragment(),
                            MoreFragment.FRAGMENT_TAG,
                            null);
                    break;
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
    }

    @Subscribe()
    public void onDashboardResumeEvent(DashboardResume event) {
        if (event.isStatus()) {
            if (bottomBar != null) {
                bottomBar.selectTabAtPosition(0);
            }
        }
    }

    /**
     * Back button listener.
     * Will close the application if the back button pressed twice.
     */
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.press_back_home, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
