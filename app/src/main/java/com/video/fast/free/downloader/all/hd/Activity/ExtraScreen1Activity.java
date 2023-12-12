package com.video.fast.free.downloader.all.hd.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.video.fast.free.downloader.all.hd.R;
import com.sdk.mynew.AppPreference;
import com.sdk.mynew.Interstitial_Ads;

public class ExtraScreen1Activity extends Base_Activity {

    private long BackPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_screen_1);

        findViewById(R.id.extra_screen_1_continue_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPermissionGranted()) {
                    new Interstitial_Ads().Show_Ads(ExtraScreen1Activity.this, new Interstitial_Ads.AdCloseListener() {
                        @Override
                        public void onAdClosed() {
                            startActivity(new Intent(ExtraScreen1Activity.this, getNextIntent(ExtraScreen1Activity.this)));
                        }
                    });
                } else {
                    takePermission();
                }
            }
        });
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            int post_notifications = ContextCompat.checkSelfPermission(this, "android.permission.POST_NOTIFICATIONS");
            return post_notifications == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private void takePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            "android.permission.POST_NOTIFICATIONS"}, 102);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 102) {
            boolean gotPermission1 = grantResults.length > 0;

            for (int result : grantResults) {
                gotPermission1 &= result == PackageManager.PERMISSION_GRANTED;
            }
            if (gotPermission1) {
                new Interstitial_Ads().Show_Ads(ExtraScreen1Activity.this, new Interstitial_Ads.AdCloseListener() {
                    @Override
                    public void onAdClosed() {
                        startActivity(new Intent(ExtraScreen1Activity.this, getNextIntent(ExtraScreen1Activity.this)));
                    }
                });
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (new AppPreference(this).getScreen().equalsIgnoreCase("off")) {
            try {
                finishAffinity();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (BackPressedTime + 2000 > System.currentTimeMillis()) {
            try {
                finishAffinity();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        BackPressedTime = System.currentTimeMillis();
    }
}