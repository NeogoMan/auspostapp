package com.conducthq.auspost.view.EventRsvp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.conducthq.auspost.R;
import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.model.request.RequestPrincipalEvent;
import com.conducthq.auspost.model.response.ContentResponse;
import com.conducthq.auspost.model.response.StandardResponse;
import com.conducthq.auspost.view.BaseActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by conduct19 on 27/10/2016.
 */

public class NoticeActivity extends BaseActivity {

    private Button mSubmitButton;
    private EditText edit_text;
    public AVLoadingIndicatorView mProgress;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        setTopActionBar(R.string.notice_title, -1, Constants.BTN_X);
        mSubmitButton = (Button) findViewById(R.id.button);
        edit_text = (EditText) findViewById(R.id.edit_text);

        content = EventBus.getDefault().getStickyEvent(ContentResponse.class);

        mSubmitButton.setText(R.string.notice_button);

        prefs = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        mProgress = (AVLoadingIndicatorView) findViewById(R.id.avi_loader);

        if (actionBarClose != null) {
            actionBarClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onBackPressed();
                }
            });
        }

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                String ed_text = edit_text.getText().toString().trim();

                if (ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.notice_empty_field, Toast.LENGTH_SHORT).show();
                    return;
                }

                String token = prefs.getString(Constants.TOKEN, "");
                mProgress.smoothToShow();
                final Call<StandardResponse> call = auspostAusPostApi.UpdatePrincipalEvent(new RequestPrincipalEvent(false, ed_text), content.getResponse().getPrincipal().getId(), content.getResponse().getEvent().getId(), token);

                call.enqueue(new Callback<StandardResponse>() {
                    @Override
                    public void onResponse(Call<StandardResponse> call, Response<StandardResponse> response) {
                        mProgress.smoothToHide();
                        if (response.isSuccessful()) {
                            Intent i = new Intent(getApplication(), NoticeMessageActivity.class);
                            overridePendingTransition(0, 0);
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onFailure(Call<StandardResponse> call, Throwable t) {
                        mProgress.smoothToHide();
                        String Response = t.getMessage();
                    }
                });
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_in_down);
    }
}
