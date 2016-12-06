package com.conducthq.auspost.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.conducthq.auspost.R;
import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.model.data.Achievement;
import com.conducthq.auspost.model.bus.StatusAchievement;
import com.conducthq.auspost.model.data.AchievementLock;
import com.conducthq.auspost.model.data.Event;
import com.conducthq.auspost.model.data.Principal;
import com.conducthq.auspost.model.response.ContentResponse;
import com.conducthq.auspost.task.AsyncContentResponse;
import com.conducthq.auspost.task.AsyncUnlockAchievementResponse;
import com.conducthq.auspost.task.ContentTask;
import com.conducthq.auspost.task.UnlockAchievementTask;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by conduct19 on 20/10/2016.
 */

public class ScanCodeActivity extends BaseActivity implements TextWatcher, AsyncContentResponse, AsyncUnlockAchievementResponse {

    private static final String TAG = "ScanCodeActivity";
    private EditText mEditTxt;
    private TextView mStatusTxt;
    private int[] mDigitIds = {R.id.dig1, R.id.dig2, R.id.dig3, R.id.dig4, R.id.dig5};

    private SharedPreferences prefs;

    public AVLoadingIndicatorView mProgress;

    // data
    private String mPin = "";
    private boolean matchingCode = false;
    private ArrayList<Achievement> achievements;
    private Event event;
    private Principal principal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);

        setTopActionBar(R.string.scan_input_title, Constants.ARROW, -1);

        mEditTxt = (EditText) findViewById(R.id.edit);
        mStatusTxt = (TextView) findViewById(R.id.status_pin_msg);
        mProgress= (AVLoadingIndicatorView) findViewById(R.id.avi_loader);
        mEditTxt.addTextChangedListener(this);

        prefs = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        mEditTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch(result) {
                    case EditorInfo.IME_ACTION_DONE:
                        makeRequest();
                        break;
                }
                return false;
            }
        });

        if(actionBarBack != null) {
            actionBarBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onBackPressed();
                }
            });
        }

        content = EventBus.getDefault().getStickyEvent(ContentResponse.class);
        if (content != null) {
            populateAchievements(content);
        } else {
            ContentTask asyncTask = new ContentTask(this);
            asyncTask.delegate = this;
            asyncTask.execute();
        }

    }

    public void populateAchievements(ContentResponse content) {
        principal = content.getResponse().getPrincipal();
        event = content.getResponse().getEvent();
        achievements = content.getResponse().getEvent().getAchievements();

    }

    @Override
    public void processFinish(ContentResponse contentResponse) {
        EventBus.getDefault().postSticky(contentResponse);
        populateAchievements(contentResponse);
        content = contentResponse;
    }

    private void makeRequest() {
        String typedPin = mEditTxt.getText().toString();

        if(typedPin.matches("") || typedPin.length() != 5) {
            Toast.makeText(getApplicationContext(), R.string.scan_qr_error, Toast.LENGTH_SHORT).show();
            showSoftKeyboard(mEditTxt);
            return;
        }

        mProgress.smoothToShow();
        if (achievements != null) {
            for (int i = 0; i < achievements.size(); i++) {
                if (achievements.get(i).getQrCode().equals(typedPin)) {
                    matchingCode = true;
                    AchievementLock lock = new AchievementLock();
                    lock.setEventId(event.getId());
                    lock.setQrCode(typedPin);
                    final UnlockAchievementTask asyncTask = new UnlockAchievementTask(this, principal.getId(), achievements.get(i), mProgress, lock);
                    asyncTask.delegate = this;
                    asyncTask.execute();
                    return;
                }
            }
        }

        Toast.makeText(this, "Invalid QR Code!", Toast.LENGTH_SHORT).show();
        mProgress.smoothToHide();
    }

    private void update() {
        // update the 5 digit TextViews
        for (int i = 0; i < 5; i++) {
            char c = i < mPin.length() ? mPin.charAt(i) : ' ';
            ((TextView) findViewById(mDigitIds[i])).setText(String.valueOf(c));
        }

        if(mPin.length() == 5) {
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
        if (matchingCode) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }

        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
    }

    @Override
    public void processFinish() {
        onBackPressed();
    }
}