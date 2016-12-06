package com.conducthq.auspost.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;
import android.view.View.OnClickListener;


import com.conducthq.auspost.R;

/**
 * Created by conduct19 on 20/10/2016.
 */

public class IntroVideoActivity extends BaseActivity implements OnClickListener {

    VideoView preview;
    ImageButton btnPlayVideo;
    Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_video);

//        preview = (VideoView) findViewById(R.id.video_preview);
//        preview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.big_buck_bunny));
//        preview.seekTo(10000);

        btnPlayVideo = (ImageButton) findViewById(R.id.play_btn);
        btnPlayVideo.setOnClickListener(this);

        btnContinue = (Button) findViewById(R.id.button);
        btnContinue.setOnClickListener(this);
        btnContinue.setText(R.string.intro_button);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.play_btn: {
                startActivity(new Intent(getApplicationContext(), VideoPlayerActivity.class));
                overridePendingTransition(0, 0);
                break;
            }

            case R.id.button: {
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(myIntent);
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up);
                break;
            }

        }
    }
}
