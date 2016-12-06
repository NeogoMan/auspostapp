package com.conducthq.auspost.view;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.MediaController;


import com.conducthq.auspost.R;

/**
 * Created by conduct19 on 20/10/2016.
 */

public class VideoPlayerActivity extends BaseActivity {

    private static final String TAG = "VideoPlayerActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        View skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), PinActivity.class);
                startActivity(myIntent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        final VideoView videoView = (VideoView) findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i(TAG, "Duration = " +
                        videoView.getDuration());
            }
        });
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.big_buck_bunny));
        videoView.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
