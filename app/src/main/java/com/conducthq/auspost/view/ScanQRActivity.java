package com.conducthq.auspost.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.conducthq.auspost.R;
import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.helper.QRCodeReaderView;
import com.conducthq.auspost.model.data.Achievement;
import com.conducthq.auspost.model.data.AchievementLock;
import com.conducthq.auspost.model.data.Event;
import com.conducthq.auspost.model.data.Principal;
import com.conducthq.auspost.model.response.ContentResponse;
import com.conducthq.auspost.task.AsyncUnlockAchievementResponse;
import com.conducthq.auspost.task.ContentTask;
import com.conducthq.auspost.task.UnlockAchievementTask;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class ScanQRActivity extends BaseActivity implements QRCodeReaderView.OnQRCodeReadListener, AsyncUnlockAchievementResponse {

    public static final String TAG = "ScanQRActivity";
    private QRCodeReaderView qrPreview;
    private View inputCode;
    private View qrScan;

    private ArrayList<Achievement> achievements;
    private Event event;
    private Principal principal;

    public AVLoadingIndicatorView mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        setTopActionBar(R.string.scan_title, -1, Constants.BTN_X);

        inputCode = findViewById(R.id.inputCode);
        qrScan = findViewById(R.id.qrScan);

        mProgress= (AVLoadingIndicatorView) findViewById(R.id.avi_loader);

        content = EventBus.getDefault().getStickyEvent(ContentResponse.class);
        achievements = content.getResponse().getEvent().getAchievements();
        event = content.getResponse().getEvent();
        principal = content.getResponse().getPrincipal();
        achievements = content.getResponse().getEvent().getAchievements();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            initQRCodeReaderView();
        } else {
            requestCameraPermission();
        }

        actionBarClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });

        inputCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(), ScanCodeActivity.class), 9002);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        qrScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCamera();
            }
        });
    }


    public void clickCamera() {
        if (qrPreview != null) {
            qrPreview.setQRDecodingEnabled(true);
        } else {
            requestCameraPermission();
        }
    }

    @Override
    protected void onResume() {
         if (qrPreview != null) {
                qrPreview.startCamera();
         }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (qrPreview != null) {
            qrPreview.stopCamera();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        if (qrPreview != null) {
            qrPreview.stopCamera();
        }
        super.onStop();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 9002:
                if (resultCode == RESULT_OK) {
                    startActivity(new Intent(getApplicationContext(), AchievementActivity.class));
                    overridePendingTransition(0, 0);
                    onBackPressed();
                }
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Constants.INTENT_CAMERA:
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initQRCodeReaderView();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_in_down);
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
//        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 50);
//        toast.show();

        makeRequest(text);

        if (qrPreview != null) {
            qrPreview.setQRDecodingEnabled(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    qrPreview.setQRDecodingEnabled(true);
                }
            }, 5000);
        }
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(ScanQRActivity.this,
                    new String[] { Manifest.permission.CAMERA }, Constants.INTENT_CAMERA);
        } else {
            Toast.makeText(this, "Permission is not available. Requesting camera permission.", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA },
                    Constants.INTENT_CAMERA);
        }
    }

    private void initQRCodeReaderView() {
        FrameLayout wrapperQR = (FrameLayout) findViewById(R.id.wrapperQR);
        qrPreview = (QRCodeReaderView) getLayoutInflater().inflate(R.layout.scan_qr, null);
        qrPreview.setAutofocusInterval(5000L);
        qrPreview.setOnQRCodeReadListener(this);
        qrPreview.setBackCamera();
        qrPreview.startCamera();
        qrPreview.setQRDecodingEnabled(true);

        wrapperQR.addView(qrPreview);
    }

    private void makeRequest(String scannedCode) {

        mProgress.smoothToShow();
        if (achievements != null) {
            for (int i = 0; i < achievements.size(); i++) {
                if (achievements.get(i).getQrCode().equals(scannedCode)) {
                    AchievementLock lock = new AchievementLock();
                    lock.setEventId(event.getId());
                    lock.setQrCode(scannedCode);
                    final UnlockAchievementTask asyncTask = new UnlockAchievementTask(this, principal.getId(), achievements.get(i), mProgress, lock);
                    asyncTask.delegate = this;
                    asyncTask.execute();
                    return;
                }
            }
        }
        mProgress.smoothToHide();

        Toast.makeText(this, "Invalid QR Code!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void processFinish() {
        onBackPressed();
    }
}
