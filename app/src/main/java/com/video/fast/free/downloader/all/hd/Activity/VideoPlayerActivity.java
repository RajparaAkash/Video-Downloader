package com.video.fast.free.downloader.all.hd.Activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.sdk.mynew.Interstitial_Ads_AdmobBack;
import com.video.fast.free.downloader.all.hd.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.FileDataSource;

public class VideoPlayerActivity extends AppCompatActivity {

    DataSpec dataSpec;
    private SimpleExoPlayer simpleExoPlayer;
    private SimpleExoPlayerView vp_simpleExoPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        idBinding();

        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initializePlayer();
    }

    private void initializePlayer() {
        String mVideoPath = getIntent().getStringExtra("mVideoPath");
        try {
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance((Context) this, (TrackSelector) new DefaultTrackSelector((TrackSelection.Factory) new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter())));
            dataSpec = new DataSpec(Uri.parse(mVideoPath));
            final FileDataSource fileDataSource = new FileDataSource();
            try {
                fileDataSource.open(this.dataSpec);
            } catch (FileDataSource.FileDataSourceException e) {
                e.printStackTrace();
            }
            simpleExoPlayer.prepare(new LoopingMediaSource(new ExtractorMediaSource(fileDataSource.getUri(), new DataSource.Factory() {
                public DataSource createDataSource() {
                    return fileDataSource;
                }
            }, new DefaultExtractorsFactory(), (Handler) null, (ExtractorMediaSource.EventListener) null)));
            simpleExoPlayer.setPlayWhenReady(true);
            vp_simpleExoPlayerView.setPlayer(simpleExoPlayer);
            vp_simpleExoPlayerView.setVisibility(View.VISIBLE);
            simpleExoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (simpleExoPlayer == null) {
            initializePlayer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    @Override
    public void onBackPressed() {
        Interstitial_Ads_AdmobBack.ShowAd_Full(this, () -> finish());
    }

    public void idBinding(){
        vp_simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.vp_simpleExoPlayerView);
    }
}