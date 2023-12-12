package com.video.fast.free.downloader.all.hd.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.video.fast.free.downloader.all.hd.R;
import com.sdk.mynew.Interstitial_Ads;
import com.sdk.mynew.Interstitial_Ads_AdmobBack;

public class ExtraScreen2Activity extends Base_Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_screen_2);

        findViewById(R.id.extra_screen_2_continue_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Interstitial_Ads().Show_Ads(ExtraScreen2Activity.this, new Interstitial_Ads.AdCloseListener() {
                    @Override
                    public void onAdClosed() {
                        startActivity(new Intent(ExtraScreen2Activity.this, getNextIntent(ExtraScreen2Activity.this)));
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