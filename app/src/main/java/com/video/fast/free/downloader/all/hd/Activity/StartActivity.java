package com.video.fast.free.downloader.all.hd.Activity;

import static android.Manifest.permission.POST_NOTIFICATIONS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.READ_MEDIA_VIDEO;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.sdk.mynew.Native_Ads_Preload_1;
import com.video.fast.free.downloader.all.hd.R;
import com.sdk.mynew.AppPreference;
import com.sdk.mynew.CheckInstallActivity;
import com.sdk.mynew.Interstitial_Ads;
import com.sdk.mynew.Interstitial_Ads_AdmobBack;

public class StartActivity extends AppCompatActivity {

    private long BackPressedTime = 0;
    private final int BELOW_ANDROID_13 = 101;
    private final int ABOVE_ANDROID_13 = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // native_banner_small
        Native_Ads_Preload_1.getInstance(this).Native_Banner_Ads(this, findViewById(R.id.native_small));

        findViewById(R.id.start_screen_start_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (givePermissionGranted()) {
                    moveOnScreen();
                } else {
                    take_Permission();
                }
            }
        });

        findViewById(R.id.start_screen_rate_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.start_screen_share_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + getPackageName() + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean givePermissionGranted() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {

            int read_external_storage = ContextCompat.checkSelfPermission(StartActivity.this, READ_EXTERNAL_STORAGE);

            return read_external_storage == PackageManager.PERMISSION_GRANTED;

        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {

            int read_media_images = ContextCompat.checkSelfPermission(StartActivity.this, READ_MEDIA_IMAGES);
            int read_media_video = ContextCompat.checkSelfPermission(StartActivity.this, READ_MEDIA_VIDEO);
            int post_notifications = ContextCompat.checkSelfPermission(StartActivity.this, POST_NOTIFICATIONS);

            return read_media_images == PackageManager.PERMISSION_GRANTED
                    && read_media_video == PackageManager.PERMISSION_GRANTED
                    && post_notifications == PackageManager.PERMISSION_GRANTED;

        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case BELOW_ANDROID_13:
                boolean gotPermission1 = grantResults.length > 0;

                for (int result : grantResults) {
                    gotPermission1 &= result == PackageManager.PERMISSION_GRANTED;
                }

                if (gotPermission1) {
                    moveOnScreen();
                } else {
                    Toast.makeText(StartActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;

            case ABOVE_ANDROID_13:
                boolean gotPermission2 = grantResults.length > 0;

                for (int result : grantResults) {
                    gotPermission2 &= result == PackageManager.PERMISSION_GRANTED;
                }

                if (gotPermission2) {
                    moveOnScreen();
                } else {
                    Toast.makeText(StartActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void moveOnScreen() {
        new Interstitial_Ads().Show_Ads(StartActivity.this, new Interstitial_Ads.AdCloseListener() {
            @Override
            public void onAdClosed() {
                startActivity(new Intent(StartActivity.this, DashBoardActivity.class));
            }
        });
    }

    private void take_Permission() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {

            ActivityCompat.requestPermissions(StartActivity.this,
                    new String[]{READ_MEDIA_IMAGES, READ_MEDIA_VIDEO, POST_NOTIFICATIONS}, ABOVE_ANDROID_13);

        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {

            ActivityCompat.requestPermissions(StartActivity.this,
                    new String[]{READ_EXTERNAL_STORAGE}, BELOW_ANDROID_13);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (new AppPreference(StartActivity.this).getScreen().equalsIgnoreCase("off")) {
            try {
                finishAffinity();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!CheckInstallActivity.checkScreenFlag(this)) {
            if (BackPressedTime + 2000 > System.currentTimeMillis()) {
                finishAffinity();
            } else {
                Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
            }
            BackPressedTime = System.currentTimeMillis();
        } else {
            Interstitial_Ads_AdmobBack.ShowAd_Full(StartActivity.this, () -> finish());
        }
    }
}