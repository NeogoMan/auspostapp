package com.conducthq.auspost.view;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.conducthq.auspost.R;
import com.conducthq.auspost.helper.CameraPreview;
import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.helper.InternalStorageContentProvider;

import com.conducthq.auspost.model.response.ImageUploadResponse;
import com.conducthq.auspost.model.response.StandardResponse;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import eu.janmuller.android.simplecropimage.CropImage;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilePictureActivity extends BaseActivity implements Camera.PictureCallback {

    public static final String TAG = "ProfilePictureActivity";
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    public static final String DATA_IMAGE_URL = "IMAGE_URL";

    private Camera camera2;
    private int cameraId = 0;

    private CircularImageView picProfile;
    private FrameLayout previewCamera;
    private File mFileTemp;
    private Uri mUri;
    private Bitmap mBitmap;
    private View gallery;
    private View camera;
    private CameraPreview mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);
        setTopActionBar(R.string.profile_pic_title, Constants.BTN_HELP, Constants.BTN_X);

        picProfile = (CircularImageView) findViewById(R.id.profile_pic);
        previewCamera = (FrameLayout) findViewById(R.id.camera_preview);
        gallery = findViewById(R.id.gallery);
        camera = findViewById(R.id.camera);

        // do we have a camera?
        if (!getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, R.string.message_error_camera, Toast.LENGTH_LONG)
                    .show();
        } else {
            cameraPermission();
        }


        actionBarClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //openCamera();
                clickCamera();
            }
        });
    }


    public void clickCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            if (camera2 != null) {
                camera2.takePicture(null, null,
                        new PhotoHandler(getApplicationContext()));
            } else {
                restartCamera();
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.INTENT_CAMERA);
        }
    }

    public void restartCamera() {
        picProfile.setImageBitmap(null);
        camera2 = getCameraInstance();
        if (camera2 == null) {
            // TODO: show error
        } else {
            camera2.setPreviewCallback(null);
            mPreview.getHolder().removeCallback(mPreview);
            mPreview = new CameraPreview(getApplicationContext(), camera2);
            previewCamera.removeAllViews();
            previewCamera.addView(mPreview);
        }
    }

    public void cameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
               PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            cameraId = findFrontFacingCamera();
            if (cameraId < 0) {
                Toast.makeText(this, R.string.message_error_camera_front,
                        Toast.LENGTH_LONG).show();
            } else {
                camera2 = getCameraInstance();
            }

            mPreview = new CameraPreview(this, camera2);
            previewCamera.addView(mPreview);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.INTENT_CAMERA);
        }
    }

    public void cameraResume() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            camera2 = getCameraInstance();
            mPreview = new CameraPreview(this, camera2);
            previewCamera.addView(mPreview);
        }
    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /** A safe way to get an instance of the Camera object. */
    public Camera getCameraInstance(){
        Camera c = null;
        try {
            c = openFrontFacingCamera();
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Log.d(TAG, "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    private Camera openFrontFacingCamera() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for ( int camIdx = 0; camIdx < cameraCount; camIdx++ ) {
            Camera.getCameraInfo( camIdx, cameraInfo );
            if ( cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT ) {
                try {
                    cam = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }

        return cam;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (camera2 == null) {
            //cameraResume();
        }
    }

    @Override
    protected void onPause() {
        closeCamera();
        super.onPause();
    }

    @Override
    public void onStop() {
        closeCamera();
        super.onStop();
    }

    public void closeCamera() {
        if (camera2 != null) {
            camera2.setPreviewCallback(null);
            mPreview.getHolder().removeCallback(mPreview);
            camera2.release();
            camera2 = null;
        }
    }

    public void openCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (intentCamera.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                mFileTemp = createTempFile();

                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    //mUri = Uri.fromFile(mFileTemp);
                    mUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", mFileTemp);
                } else {
                    mUri = InternalStorageContentProvider.CONTENT_URI;
                }
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                intentCamera.putExtra("return-data", true);
                startActivityForResult(intentCamera, Constants.INTENT_CAMERA);
            } else {
                Toast.makeText(this, R.string.message_error_camera, Toast.LENGTH_SHORT).show();
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.INTENT_CAMERA);
        }
    }

    private void openGallery() {
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) /*&&
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.MANAGE_DOCUMENTS) == PackageManager.PERMISSION_GRANTED)*/) {
            //Intent intent = new Intent(Intent.ACTION_PICK);
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //intent.setType("image/*");
            //intent.setAction(Intent.ACTION_GET_CONTENT);
            try {
                mFileTemp = createTempFile();
                mUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", mFileTemp);
                startActivityForResult(intent, Constants.INTENT_GALLERY);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, R.string.message_error_gallery, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "openGallery: " + e.getMessage());
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.INTENT_GALLERY);
        }
    }

    private void doCrop() {
        Intent intent = new Intent(this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, mUri);
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 3);
        intent.putExtra(CropImage.ASPECT_Y, 3);

        startActivityForResult(intent, Constants.INTENT_CROP);
        overridePendingTransition(0, 0);
    }

    private void doCropFile() {
        Intent intent = new Intent(this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_FILE, mFileTemp.getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 3);
        intent.putExtra(CropImage.ASPECT_Y, 3);

        startActivityForResult(intent, Constants.INTENT_CROP);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.INTENT_CAMERA:
                if (resultCode == RESULT_OK) {
                    try {
                        doCrop();
                    } catch (ActivityNotFoundException | NullPointerException e) {
                        Log.e(TAG, "onActivityResult:takeCamera " + e.getMessage());
                        Toast.makeText(this, R.string.message_error_crop, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case Constants.INTENT_GALLERY:
                if (resultCode == RESULT_OK) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(data.getData());
                        FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                        copyStream(inputStream, fileOutputStream);

                        doCropFile();
                    } catch (ActivityNotFoundException | SecurityException | NullPointerException | IOException e) {
                        Log.e(TAG, "onActivityResult:takeGallery " + e.getMessage());
                        Toast.makeText(this, R.string.message_error_crop, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case Constants.INTENT_CROP:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Uri uri = data.getParcelableExtra(CropImage.IMAGE_PATH);
                        if (uri == null) {
                            mBitmap = null;
                            return;
                        }

                        try {
                            uploadImage(new File(uri.getPath()));
                            mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                            picProfile.setImageBitmap(mBitmap);
                            camera2 = null;
                        } catch (IOException e) {
                            Log.e(TAG, "onActivityResult:cropImage: " + e.getMessage());
                            mBitmap = null;
                        }
                    }
                }
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Constants.INTENT_CAMERA:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        cameraPermission();
                    }
                }
                break;
            case Constants.INTENT_GALLERY:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED/* && grantResults[1] == PackageManager.PERMISSION_GRANTED*/) {
                        openGallery();
                    }
                }
                break;
        }
    }

    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {

    }


    class PhotoHandler implements Camera.PictureCallback {

        private final Context context;

        public PhotoHandler(Context context) {
            this.context = context;
        }

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFileDir = getDir();

            if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

                Toast.makeText(context, R.string.message_error_write_storage,
                        Toast.LENGTH_LONG).show();
                return;

            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
            String date = dateFormat.format(new Date());
            String photoFile = "Picture_" + date + ".jpg";

            String filename = pictureFileDir.getPath() + File.separator + photoFile;

            File pictureFile = new File(filename);

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();

                Bitmap bitmap = BitmapFactory.decodeFile(pictureFile.getPath());
                if (bitmap.getWidth() > bitmap.getHeight()) {
                    bitmap = fixOrientation(bitmap);
                }

                uploadImage(convertBitmapToFile(bitmap));

            } catch (Exception error) {
                Log.d(ProfilePictureActivity.TAG, "File" + filename + "not saved: "
                        + error.getMessage());
                Toast.makeText(context, "Image could not be saved.",
                        Toast.LENGTH_LONG).show();
            }

            camera2 = null;
        }

        private File getDir() {
            File sdDir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            return new File(sdDir, "CameraAPIDemo");
        }
    }

    private void uploadImage(File photo) {
        Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show();
        SharedPreferences prefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String token = prefs.getString(Constants.TOKEN, "");

        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", photo.getName(),
                RequestBody.create(MediaType.parse("image/*"), photo));

        Call<ImageUploadResponse> call = auspostAusPostApi.UploadImage(filePart, token);
        call.enqueue(new Callback<ImageUploadResponse>()
        {
            @Override
            public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {
                if (response.isSuccessful()) {
                    Intent data = new Intent();
                    Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                    data.putExtra(DATA_IMAGE_URL, response.body().getImageUrl());  // pass ImageUrl back to caller
                    setResult(RESULT_OK, data);
                    finish();
                } else if (response.errorBody() != null) {
                    try {
                        showError(new IOException(response.errorBody().string()));
                    } catch (IOException e) {
                        // ignore errors in errors
                    }
                }
            }

            @Override
            public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
                if (t instanceof Exception) {
                    showError((Exception) t);
                } else {
                    showError(new RuntimeException("Unknown error"));
                }
            }
        });
    }
}
