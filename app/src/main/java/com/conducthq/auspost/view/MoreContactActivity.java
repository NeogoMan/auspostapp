package com.conducthq.auspost.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.conducthq.auspost.R;
import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.model.response.ContentResponse;
import com.conducthq.auspost.task.AsyncContentResponse;
import com.conducthq.auspost.task.ContentTask;

import org.greenrobot.eventbus.EventBus;

public class MoreContactActivity extends BaseActivity implements AsyncContentResponse {

    private static final String TAG = "MoreContactActivity";

    ContentResponse content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_contact);

        Intent intent = getIntent();
        Boolean isHelp = intent.getExtras().getBoolean(Constants.CONTACT_HELP_TITLE);

        if(isHelp) {
            setTopActionBar(R.string.contact_team_contact_help, Constants.ARROW, -1);
        } else {
            setTopActionBar(R.string.contact_information_title, Constants.ARROW, -1);
        }


        content = EventBus.getDefault().getStickyEvent(ContentResponse.class);
        if (content == null) {
            ContentTask asyncTask = new ContentTask(getApplicationContext());
            asyncTask.delegate = this;
            asyncTask.execute();
        }

        if(actionBarBack != null) {
            actionBarBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onBackPressed();
                }
            });
        }

        View call = findViewById(R.id.btn_call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call();
            }
        });

        View email = findViewById(R.id.btn_email);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeEmail();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
    }


    public void writeEmail() {
        try {
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO,
                    Uri.fromParts("mailto", content.getResponse().getContactDetails().getEMail(), null));
            startActivity(Intent.createChooser(sendIntent,
                    content.getResponse().getContactDetails().getEMail()));
        } catch (Exception e) {
            Toast.makeText(this, R.string.message_error_email, Toast.LENGTH_SHORT).show();
        }
    }

    public void call() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            try {
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + content.getResponse().getContactDetails().getPhone())));
            } catch (Exception e) {
                Toast.makeText(this, R.string.message_error_phone, Toast.LENGTH_SHORT).show();
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{ Manifest.permission.CALL_PHONE },
                    Constants.CALL_PHONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {

                        call();
                    }

                }
            }
        }
    }

    @Override
    public void processFinish(ContentResponse contentResponse) {
        try {
            EventBus.getDefault().postSticky(contentResponse);
            content = contentResponse;
        } catch (NullPointerException e) {
            Log.e(TAG, "proccessFinish: " + e.getMessage());
        }
    }
}
