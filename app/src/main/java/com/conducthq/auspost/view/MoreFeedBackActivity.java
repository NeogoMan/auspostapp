package com.conducthq.auspost.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.conducthq.auspost.R;
import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.model.bus.DashboardResume;
import com.conducthq.auspost.model.data.Feedback;
import com.conducthq.auspost.task.FeedbackTask;
import com.conducthq.auspost.task.LikeMessageTask;

import org.greenrobot.eventbus.EventBus;

public class MoreFeedBackActivity extends BaseActivity implements View.OnClickListener {

    Button btnSubmit;
    EditText comment;
    RatingBar rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_feedback);
        setTopActionBar(R.string.feedback_title, Constants.ARROW, -1);

        btnSubmit = (Button) findViewById(R.id.button);
        btnSubmit.setOnClickListener(this);
        btnSubmit.setText(R.string.feedback_button);
        comment = (EditText) findViewById(R.id.comment);
        rating = (RatingBar) findViewById(R.id.rating);

        if(actionBarBack != null) {
            actionBarBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onBackPressed();
                }
            });
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button: {
                if (!comment.getText().toString().isEmpty() && rating.getNumStars() != 0) {
                    Feedback feedback = new Feedback();
                    feedback.setRating((int) rating.getRating());
                    feedback.setComments(comment.getText().toString());
                    new FeedbackTask(this, 1, feedback).execute();
                }
                break;
            }

        }
    }

    @Override
    public void onBackPressed() {
        DashboardResume event = new DashboardResume(true);
        EventBus.getDefault().post(event);

        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
    }
}
