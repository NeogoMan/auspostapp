package com.conducthq.auspost.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.conducthq.auspost.R;
import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.model.request.RequestPin;
import com.conducthq.auspost.model.response.PrincipalResponse;
import com.conducthq.auspost.task.UpdateTokenTask;
import com.google.firebase.iid.FirebaseInstanceId;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;

/**
 * Created by conduct19 on 20/10/2016.
 */

public class PinActivity extends BaseActivity implements TextWatcher {

    private EditText mEditTxt;
    private TextView mStatusTxt;
    private int[] mDigitIds = {R.id.dig1, R.id.dig2, R.id.dig3, R.id.dig4, R.id.dig5};

    private SharedPreferences prefs;

    public AVLoadingIndicatorView mProgress;

    // data
    private String mPin = "";
    private boolean isRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        setTopActionBar(R.string.pin_title, Constants.BTN_HELP, Constants.BTN_X);

        mEditTxt = (EditText) findViewById(R.id.edit);
        mStatusTxt = (TextView) findViewById(R.id.status_pin_msg);
        mProgress = (AVLoadingIndicatorView) findViewById(R.id.avi_loader);
        mEditTxt.addTextChangedListener(this);

        prefs = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        mEditTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                if (mPin.length() == 5) {
                    return false;
                }
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        makeRequest();
                        break;
                }
                return false;
            }
        });

        if (actionBarClose != null) {
            actionBarClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onBackPressed();
                }
            });
        }

        if (actionHelp != null) {
            actionHelp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent i = new Intent(getApplication(), MoreContactActivity.class);
                    i.putExtra(Constants.CONTACT_HELP_TITLE, true);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                }
            });
        }

    }

    private void makeRequest() {

        String typedPin = mEditTxt.getText().toString();

        if (typedPin.matches("") || typedPin.length() != 5) {
            Toast.makeText(getApplicationContext(), R.string.pin_error_login, Toast.LENGTH_SHORT).show();
            showSoftKeyboard(mEditTxt);
            return;
        }

        mProgress.smoothToShow();
        final Call<PrincipalResponse> call = auspostAusPostApi.Login(new RequestPin(typedPin));

        call.enqueue(new Callback<PrincipalResponse>() {
            @Override
            public void onResponse(Call<PrincipalResponse> call, Response<PrincipalResponse> response) {
                mProgress.smoothToHide();
                final PrincipalResponse principalResponse = response.body();
                if (response.isSuccessful()) {
                    prefs.edit().putString(Constants.TOKEN, "Bearer " + principalResponse.getResponse().getToken()).apply();
                    mStatusTxt.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreenSuccess));
                    mStatusTxt.setText(R.string.pin_success);

                    String token = FirebaseInstanceId.getInstance().getToken();
                    new UpdateTokenTask(getApplicationContext(), token).execute();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(getApplication(), ProfileActivity.class);
                            i.putExtra(Constants.KEY_PROFILE_REGISTER, true);
                            EventBus.getDefault().postSticky(principalResponse);
                            startActivity(i);
                            overridePendingTransition(0, 0);
                            finish();
                        }
                    }, 500);

                } else if (response.code() == 401) {
                    mStatusTxt.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRedAusPost));
                    mStatusTxt.setText(R.string.pin_invalid);
                    showSoftKeyboard(mEditTxt);
                }
            }

            @Override
            public void onFailure(Call<PrincipalResponse> call, Throwable t) {
                mProgress.smoothToHide();
                String Response = t.getMessage();
            }
        });

    }

    private void update() {
        // update the 5 digit TextViews
        for (int i = 0; i < 5; i++) {
            char c = i < mPin.length() ? mPin.charAt(i) : ' ';
            ((TextView) findViewById(mDigitIds[i])).setText(String.valueOf(c));
        }

        if (mPin.length() == 5) {
            makeRequest();
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        mPin = mEditTxt.getText().toString();
        update();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_in_down);
    }
}