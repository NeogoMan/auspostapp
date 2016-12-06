package com.conducthq.auspost.view;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.conducthq.auspost.R;
import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.model.response.ContentResponse;
import com.conducthq.auspost.service.AusPostApi;
import com.conducthq.auspost.service.RestAdapter;
import com.conducthq.auspost.view.EventRsvp.EventInfoFragment;
import com.roughike.bottombar.OnTabSelectListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    public AVLoadingIndicatorView mProgress;
    protected TextView mHeaderTitle;
    protected AusPostApi auspostAusPostApi;

    public ContentResponse content;

    public ImageButton actionBarClose;
    public ImageButton actionBarBack;
    public View actionHelp;
    public View actionEdit;
    public ImageButton actionBarInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auspostAusPostApi = RestAdapter.retrofit.create(AusPostApi.class);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/APLetter-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void showSoftKeyboard(final EditText edit) {
        edit.postDelayed(new Runnable()
        {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(edit, 0);
            }
        }, 50);
    }

    public void hideSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    //To hide Keyboard when clicking outside the EditText
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight()
                    || y < w.getTop() || y > w.getBottom()) ) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

    public void setTopActionBar(Integer title, int optionLeft, int optionRight) {

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        View v = getSupportActionBar().getCustomView();
        TextView titleTxtView = (TextView) v.findViewById(R.id.action_bar_title);
        titleTxtView.setText(title);

        actionBarClose = (ImageButton) v.findViewById(R.id.action_bar_close);
        actionBarBack = (ImageButton) v.findViewById(R.id.action_bar_back);
        actionHelp = v.findViewById(R.id.btnHelp);
        actionEdit = v.findViewById(R.id.btnEdit);
        actionBarInfo = (ImageButton) v.findViewById(R.id.action_bar_info);

        switch (optionLeft) {
            case Constants.ARROW:
                actionBarBack.setVisibility(View.VISIBLE);
                actionHelp.setVisibility(View.GONE);
                break;
            case Constants.BTN_HELP:
                actionHelp.setVisibility(View.VISIBLE);
                actionBarBack.setVisibility(View.GONE);
                break;
            default:
                actionBarBack.setVisibility(View.GONE);
                actionHelp.setVisibility(View.GONE);
                break;
        }

        switch (optionRight) {
            case Constants.BTN_INFO:
                actionBarInfo.setVisibility(View.VISIBLE);
                actionBarClose.setVisibility(View.GONE);
                actionEdit.setVisibility(View.GONE);
                break;
            case Constants.BTN_EDIT:
                actionBarInfo.setVisibility(View.GONE);
                actionEdit.setVisibility(View.VISIBLE);
                actionBarClose.setVisibility(View.GONE);
                break;
            case Constants.BTN_X:
                actionBarInfo.setVisibility(View.GONE);
                actionBarClose.setVisibility(View.VISIBLE);
                actionEdit.setVisibility(View.GONE);
                break;
            default:
                actionBarInfo.setVisibility(View.GONE);
                actionBarClose.setVisibility(View.GONE);
                actionEdit.setVisibility(View.GONE);
                break;
        }
    }

    public File createTempFile() {

        String mTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        try {
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File tempFile = File.createTempFile(
                    "IMG_" + mTime,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            return tempFile;
        } catch (IOException e) {
            Log.e(TAG, "createTempFile: " + e.getMessage());
            return null;
        }
    }

    protected void addFragment(@IdRes int containerViewId,
                               @NonNull Fragment fragment,
                               @NonNull String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .disallowAddToBackStack()
                .commit();
    }

    protected void  replaceFragment(@IdRes int containerViewId,
                                   @NonNull Fragment fragment,
                                   @NonNull String fragmentTag,
                                   @Nullable String backStackStateName) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .addToBackStack(backStackStateName)
                .commit();
    }

    public void intentVideoLocal(String path) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
            intent.setDataAndType(Uri.parse(path), "video/*");
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "intentVideoLocal: " + e.getMessage());
        }
    }

    public void intentVideoLink(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public void lockedInfo() {
        View view = getLayoutInflater().inflate(R.layout.custom_dialog_unlock, null);
        final Button close = (Button) view.findViewById(R.id.btn_close);

        AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        mDialog.setView(view);
        mDialog.setCancelable(true);
        final AlertDialog dialog = mDialog.create();
        dialog.show();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
    }

    public void showError(Exception ex) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage())
                .setNeutralButton(android.R.string.ok, null)
                .show();
    }

    public Bitmap fixOrientation(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(-90);
        return Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public File convertBitmapToFile(Bitmap bitmap) {
        String mTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        try {
            File tempFile = File.createTempFile(
                    "IMG_" + mTime,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            //Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapData = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(bitmapData);
            fos.flush();
            fos.close();

            return tempFile;

        } catch (IOException e) {
            Log.e(TAG, "convertBitmapToFile: " + e.getMessage());
        }
        return null;
    }

    public static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }
}
