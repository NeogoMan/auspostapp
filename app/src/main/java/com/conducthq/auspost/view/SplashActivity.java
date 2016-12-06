package com.conducthq.auspost.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.model.response.ContentResponse;
import com.conducthq.auspost.task.AsyncContentResponse;
import com.conducthq.auspost.task.ContentTask;
import com.conducthq.auspost.view.EventRsvp.EventRsvpActivity;
import com.conducthq.auspost.view.EventRsvp.NoticeMessageActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by conduct19 on 20/10/2016.
 */

public class SplashActivity extends BaseActivity implements AsyncContentResponse {

    private SharedPreferences prefs;
    ContentResponse content;
    String token = null;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        token = prefs.getString(Constants.TOKEN, "");

        if(token.equals("")) {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        } else {
            final ContentTask asyncTask = new ContentTask(getApplicationContext());
            asyncTask.delegate = this;
            asyncTask.execute();
        }

    }

    @Override
    public void processFinish(ContentResponse contentResponse) {
        content = contentResponse;

        EventBus.getDefault().postSticky(content);

        switch (content.getResponse().getPrincipal().getEventStatus().getName()) {
            case Constants.EVENT_STATUS_UNDECIDED:
                intent = new Intent(this, EventRsvpActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            case Constants.EVENT_STATUS_ACCEPTED:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            case Constants.EVENT_STATUS_DECLINED:
                intent = new Intent(this, NoticeMessageActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            case Constants.EVENT_STATUS_ADMIN_ACCEPTED:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
        }

    }

}