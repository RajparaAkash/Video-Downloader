package com.video.fast.free.downloader.all.hd.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.video.fast.free.downloader.all.hd.R;
import com.sdk.mynew.Interstitial_Ads;
import com.sdk.mynew.Interstitial_Ads_AdmobBack;

public class ExtraScreen3Activity extends Base_Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_screen_3);

        findViewById(R.id.extra_screen_3_continue_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Interstitial_Ads().Show_Ads(ExtraScreen3Activity.this, new Interstitial_Ads.AdCloseListener() {
                    @Override
                    public void onAdClosed() {
                        startActivity(new Intent(ExtraScreen3Activity.this, getNextIntent(ExtraScreen3Activity.this)));
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        Interstitial_Ads_AdmobBack.ShowAd_Full(this, () -> finish());
    }
}