package com.video.fast.free.downloader.all.hd.Activity;

import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.ShowProgress;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.sdk.mynew.Interstitial_Ads_AdmobBack;
import com.video.fast.free.downloader.all.hd.R;
import com.video.fast.free.downloader.all.hd.Utils.MyMethod;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class FbInstaStoryViewerActivity extends AppCompatActivity {

    private SimpleExoPlayerView storyViewer_simpleExoPlayerView;
    private ImageView storyViewer_image;
    private ProgressBar storyViewer_progressBar;

    String mediaURl;
    boolean isVid;
    private SimpleExoPlayer simpleExoPlayer;
    private AdaptiveTrackSelection.Factory factory;
    private TrackSelector trackSelector;
    private LoadControl loadControl;
    private DefaultBandwidthMeter defaultBandwidthMeter;
    private LoopingMediaSource loopingMediaSource;
    private int lastSongIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_insta_story_viewer);

        idBinding();

        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mediaURl = getIntent().getStringExtra("mediaURl");
        isVid = getIntent().getBooleanExtra("isVid", false);
        
        if (isVid) {
            storyViewer_simpleExoPlayerView.setVisibility(View.VISIBLE);
            storyViewer_image.setVisibility(View.GONE);
        } else {
            storyViewer_image.setVisibility(View.VISIBLE);
            storyViewer_simpleExoPlayerView.setVisibility(View.GONE);

            Glide.with(this)
                    .load(mediaURl)
                    .apply(new RequestOptions()
                            .transform(new RoundedCorners(10))
                            .error(R.mipmap.ic_launcher_round)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            storyViewer_progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                            storyViewer_progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(storyViewer_image);
        }

        findViewById(R.id.storyViewer_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVid) {
                    Dialog dialog = ShowProgress(FbInstaStoryViewerActivity.this);

                    LinearLayout dhprogress = dialog.findViewById(R.id.dhprogress);
                    ProgressBar dvprogress = dialog.findViewById(R.id.dvprogress);

                    dhprogress.setVisibility(View.INVISIBLE);
                    dvprogress.setVisibility(View.VISIBLE);
                    new MyMethod.DownloadFileFromURL(FbInstaStoryViewerActivity.this, dialog, "FB_Story").execute(mediaURl);

                } else {
                    Dialog dialog = ShowProgress(FbInstaStoryViewerActivity.this);

                    LinearLayout dhprogress = dialog.findViewById(R.id.dhprogress);
                    ProgressBar dvprogress = dialog.findViewById(R.id.dvprogress);

                    dhprogress.setVisibility(View.INVISIBLE);
                    dvprogress.setVisibility(View.VISIBLE);
                    new MyMethod.SaveImageAsync(FbInstaStoryViewerActivity.this, dialog, "FB_Story").execute(mediaURl);

                }
            }
        });
    }

    private void playMedia() {
        if (!isVid) {
            return;
        }
        defaultBandwidthMeter = new DefaultBandwidthMeter();
        factory = new AdaptiveTrackSelection.Factory(defaultBandwidthMeter);
        trackSelector = new DefaultTrackSelector(factory);
        loadControl = new DefaultLoadControl();
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
        storyViewer_simpleExoPlayerView.setPlayer(simpleExoPlayer);
        DefaultBandwidthMeter dBandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "com.exoplayerdemo"), dBandwidthMeter);
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // below line you can pass video url
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mediaURl),
                dataSourceFactory,
                extractorsFactory,
                null,
                null);

        loopingMediaSource = new LoopingMediaSource(mediaSource);
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.addListener(new ExoPlayer.EventListener() {

            @Override
            public void onTimelineChanged(Timeline timeline, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == ExoPlayer.STATE_BUFFERING) {
                    storyViewer_progressBar.setVisibility(View.VISIBLE);
                } else {
                    storyViewer_progressBar.setVisibility(View.GONE);
                }
                if (playbackState == ExoPlayer.STATE_ENDED) {
                    lastSongIndex++;
                    playMedia();
                }
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                if (!TextUtils.isEmpty(error.getMessage())) {
                    Toast.makeText(getApplicationContext(), "Error::" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onPositionDiscontinuity(int reason) {
                int latestSongIndex = simpleExoPlayer.getCurrentWindowIndex();
                if (latestSongIndex != lastSongIndex) {
                    lastSongIndex = latestSongIndex;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        playMedia();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopMedia();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopMedia();
    }

    private void stopMedia() {
        if (!isVid) {
            return;
        }
        simpleExoPlayer.stop();
        simpleExoPlayer.release();
    }

    @Override
    public void onBackPressed() {
        Interstitial_Ads_AdmobBack.ShowAd_Full(this, () -> finish());
    }

    public void idBinding(){
        storyViewer_simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.storyViewer_simpleExoPlayerView);
        storyViewer_image = (ImageView) findViewById(R.id.storyViewer_image);
        storyViewer_progressBar = (ProgressBar) findViewById(R.id.storyViewer_progressBar);
    }
}