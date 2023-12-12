package com.video.fast.free.downloader.all.hd.Activity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.sdk.mynew.Interstitial_Ads_AdmobBack;
import com.video.fast.free.downloader.all.hd.R;

public class ImageShowActivity extends AppCompatActivity {

    private ImageView imageShow_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);

        idBinding();

        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Uri myUri = Uri.parse(getIntent().getStringExtra("mImagePath"));
        Glide.with(this)
                .load(myUri)
                .apply((BaseRequestOptions<?>) ((RequestOptions) ((RequestOptions) ((RequestOptions) new RequestOptions()
                        .transform((Transformation<Bitmap>) new RoundedCorners(10)))
                        .error((int) R.mipmap.ic_launcher_round))
                        .skipMemoryCache(true))
                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(imageShow_img);
    }

    @Override
    public void onBackPressed() {
        Interstitial_Ads_AdmobBack.ShowAd_Full(this, () -> finish());
    }

    public void idBinding(){
        imageShow_img = (ImageView) findViewById(R.id.imageShow_img);
    }
}