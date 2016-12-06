package com.conducthq.auspost.view.Fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.conducthq.auspost.R;
import com.conducthq.auspost.helper.AchievementAdapter;
import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.model.data.Achievement;
import com.conducthq.auspost.model.bus.StatusAchievement;
import com.conducthq.auspost.model.response.ContentResponse;
import com.conducthq.auspost.task.AsyncContentResponse;
import com.conducthq.auspost.task.ContentTask;
import com.conducthq.auspost.view.ScanQRActivity;
import com.dgreenhalgh.android.simpleitemdecoration.grid.GridDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by conduct19 on 8/11/2016.
 */

public class BadgesFragment extends Fragment implements AsyncContentResponse {

    private static final String TAG = "BadgesFragment";
    private RecyclerView recyclerView;
    private FloatingActionButton scanButton;

    public static final String FRAGMENT_TAG = Constants.BADGES_FRAGMENT_TAG;

    ContentResponse content;
    private EventBus eventBus = EventBus.getDefault();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_badges, container, false);

        eventBus.register(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.achievements);
        scanButton = (FloatingActionButton) view.findViewById(R.id.button_new_qr);

        content = EventBus.getDefault().getStickyEvent(ContentResponse.class);
        if (content != null) {
            populateAchievements(content);
        } else {
            ContentTask asyncTask = new ContentTask(getContext());
            asyncTask.delegate = this;
            asyncTask.execute();
        }


        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ScanQRActivity.class));
                getActivity().overridePendingTransition( R.anim.slide_in_up, R.anim.stay);
            }
        });

        return view;
    }

    public void populateAchievements(ContentResponse content) {
        AchievementAdapter adapter = new AchievementAdapter(content.getResponse().getEvent().getAchievements(), getContext());

        Drawable dividerDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.divider_grid);
        GridDividerItemDecoration itemDecoration = new GridDividerItemDecoration(dividerDrawable, dividerDrawable, 2);

        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    public void refreshAchievements(ContentResponse content, int id, int status) {

        ArrayList<Achievement> achievements = content.getResponse().getEvent().getAchievements();

        for (int i = 0; i < achievements.size(); i++) {
            if (achievements.get(i).getId() == id) {
                if (status == Constants.ACHIEVEMENT_COMPLETED) {
                    achievements.get(i).setStatus("completed");
                } else {
                    achievements.get(i).setStatus("unlocked");
                }
                break;
            }
        }

        AchievementAdapter adapter = new AchievementAdapter(achievements, getContext());

        Drawable dividerDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.divider_grid);
        GridDividerItemDecoration itemDecoration = new GridDividerItemDecoration(dividerDrawable, dividerDrawable, 2);

        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }


    @Override
    public void processFinish(ContentResponse contentResponse) {
        try {
            EventBus.getDefault().postSticky(contentResponse);
            populateAchievements(contentResponse);
            content = contentResponse;
        } catch (NullPointerException e) {
            Log.e(TAG, "processFinish: " + e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
    }


    @Subscribe()
    public void onStatusAchievementEvent(StatusAchievement event) {
        if (content != null) {
            refreshAchievements(content, event.getId(), event.getStatus());
        }
    }
}
