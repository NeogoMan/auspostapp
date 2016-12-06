package com.conducthq.auspost.view;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.conducthq.auspost.R;
import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.model.data.Achievement;

import org.greenrobot.eventbus.EventBus;

public class AchievementInfoKeyActivity extends BaseActivity {

    private Achievement achievement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_info_material);
        setTopActionBar(R.string.achievement_information_info_key_title, Constants.ARROW, -1);

        achievement = EventBus.getDefault().getStickyEvent(Achievement.class);
        TextView wrapperInfoMaterial = (TextView) findViewById(R.id.wrapperInfoMaterial);
        wrapperInfoMaterial.setText(achievement.getOutcomes());

        if(actionBarBack != null) {
            actionBarBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onBackPressed();
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
    }

}
