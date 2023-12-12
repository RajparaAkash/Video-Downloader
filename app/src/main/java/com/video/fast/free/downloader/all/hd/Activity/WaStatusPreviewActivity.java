package com.video.fast.free.downloader.all.hd.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.sdk.mynew.Interstitial_Ads;
import com.sdk.mynew.Interstitial_Ads_AdmobBack;
import com.video.fast.free.downloader.all.hd.R;
import com.video.fast.free.downloader.all.hd.Utils.WhatsAppUtil;

public class WaStatusPreviewActivity extends AppCompatActivity {

    ImageView waPreview_image;
    ImageView waPreview_videoplay_img;

    LinearLayout waPreview_save_ly;
    LinearLayout waPreview_share_ly;
    LinearLayout waPreview_repost_ly;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_wa_status_preview);

        idBinding();

        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String statusdownload = getIntent().getStringExtra("statusdownload");
        String filepath = getIntent().getStringExtra("filepath");

        if (statusdownload.equals("download")) {
            waPreview_save_ly.setVisibility(View.GONE);
        } else {
            waPreview_save_ly.setVisibility(View.VISIBLE);
        }

        if (WhatsAppUtil.isVideoFile(WaStatusPreviewActivity.this, filepath)) {
            waPreview_videoplay_img.setVisibility(View.VISIBLE);
        } else {
            waPreview_videoplay_img.setVisibility(View.INVISIBLE);
        }

        Glide.with(this).load(filepath).into(waPreview_image);

        waPreview_save_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    WhatsAppUtil.download(WaStatusPreviewActivity.this, filepath);
                    finish();
                    return;
                } catch (Exception unused) {
                    Toast.makeText(WaStatusPreviewActivity.this, "Sorry we can't move file.try with other file.", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
            }
        });

        waPreview_share_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WhatsAppUtil.shareFile(WaStatusPreviewActivity.this,
                        WhatsAppUtil.isVideoFile(WaStatusPreviewActivity.this, filepath), filepath);
            }
        });

        waPreview_repost_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WhatsAppUtil.repostWhatsApp(WaStatusPreviewActivity.this,
                        WhatsAppUtil.isVideoFile(WaStatusPreviewActivity.this, filepath), filepath);
            }
        });

        waPreview_videoplay_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Interstitial_Ads().Show_Ads(WaStatusPreviewActivity.this, () -> {
                    Intent intent = new Intent(WaStatusPreviewActivity.this, VideoPlayerWaActivity.class);
                    intent.putExtra("filepath", filepath);
                    startActivity(intent);
                });
            }
        });
    }


    @Override
    public void onBackPressed() {
        Interstitial_Ads_AdmobBack.ShowAd_Full(this, () -> finish());
    }

    public void idBinding() {
        waPreview_save_ly = (LinearLayout) findViewById(R.id.waPreview_save_ly);
        waPreview_share_ly = (LinearLayout) findViewById(R.id.waPreview_share_ly);
        waPreview_repost_ly = (LinearLayout) findViewById(R.id.waPreview_repost_ly);
        waPreview_image = (ImageView) findViewById(R.id.waPreview_image);
        waPreview_videoplay_img = (ImageView) findViewById(R.id.waPreview_videoplay_img);
    }
}