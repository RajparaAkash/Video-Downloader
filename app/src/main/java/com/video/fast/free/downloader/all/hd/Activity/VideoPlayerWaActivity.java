package com.video.fast.free.downloader.all.hd.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.sdk.mynew.Interstitial_Ads_AdmobBack;
import com.video.fast.free.downloader.all.hd.R;

public class VideoPlayerWaActivity extends AppCompatActivity {

    VideoView vpwa_videoView;
    String videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player_wa);

        idBinding();

        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        videoPath = getIntent().getStringExtra("filepath");
        vpwa_videoView.setVideoPath(videoPath);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(vpwa_videoView);
        vpwa_videoView.setMediaController(mediaController);
        vpwa_videoView.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (vpwa_videoView.canPause()) {
            vpwa_videoView.setVideoPath(videoPath);
            vpwa_videoView.start();
        }
    }

    @Override
    public void onBackPressed() {
        if (vpwa_videoView.isPlaying()){
            vpwa_videoView.pause();
        }
        Interstitial_Ads_AdmobBack.ShowAd_Full(this, () -> finish());
    }

    public void idBinding(){
        vpwa_videoView = (VideoView) findViewById(R.id.vpwa_videoView);
    }
}