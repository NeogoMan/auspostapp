package com.conducthq.auspost.view.EventRsvp;

import android.os.Bundle;
import android.view.View;

import com.conducthq.auspost.R;
import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.view.BaseActivity;

/**
 * Created by conduct19 on 28/10/2016.
 */

public class NoticeMessageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_message);

        setTopActionBar(R.string.notice_title, -1, Constants.BTN_X);

        if(actionBarClose != null) {
            actionBarClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    finishAffinity();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
