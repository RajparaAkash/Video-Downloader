package com.video.fast.free.downloader.all.hd.Activity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.video.fast.free.downloader.all.hd.R;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.sdk.mynew.AppOpenManager;
import com.sdk.mynew.AppPreference;
import com.sdk.mynew.CheckInstallActivity;
import com.sdk.mynew.CryptoUtil;
import com.sdk.mynew.ReferrerListener;

import org.json.JSONException;

public class SplashScreenActivity extends AppCompatActivity {

    public static RequestQueue mRequestQueue;

    AppPreference appPreference;
    String decryptedString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mRequestQueue = Volley.newRequestQueue(this);

        appPreference = new AppPreference(this);
        if (isConnected()) {
            CallData("1382");
        } else {
            try {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SplashScreenActivity.this);
                builder.setTitle("Network Unavailable!");
                builder.setMessage("Internet is not available, Cross check your internet connectivity and try again");
                builder.setPositiveButton("OK", (dialog, which) -> finishAffinity());
                builder.setCancelable(false);
                builder.show();
            } catch (Exception e) {

            }
        }
    }

    public boolean isConnected() {
        boolean connected;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
        return connected;
    }

    private void CallData(String str) {
        try {
            decryptedString = new CryptoUtil().decrypt(getResources().getString(R.string.secret_api_client_id), getResources().getString(R.string.google_api_client_id) + getResources().getString(R.string.firebase_api_client_id));
            appPreference.set_Base_Url(decryptedString + str);
            Get_Server_Data();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Get_Server_Data() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, appPreference.get_Base_Url(), response -> {

            try {
                appPreference.StoreAllDataFromJSON(response);
                FinalCallHome();
            } catch (JSONException e) {
                e.printStackTrace();
                FinalCallHome();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
            FinalCallHome();
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private Intent toGetIntent() {
        AppOpenManager.isSplash = false;

        Intent intent;
        if (CheckInstallActivity.checkScreenFlag(this)) {
            intent = new Intent(this, ExtraScreen1Activity.class);
        } else {
            intent = new Intent(this, StartActivity.class);
        }
        return intent;
    }

    public void FinalCallHome() {

        if (appPreference.get_Ad_Status().equalsIgnoreCase("on")) {
            CheckInstallActivity.checkinstallreferre(SplashScreenActivity.this, new ReferrerListener() {
                @Override
                public void referrerDone() {
                    CheckInstallActivity.startSDKActivity(SplashScreenActivity.this, toGetIntent());
                }

                @Override
                public void referrerCancel() {
                    startActivity(toGetIntent());
                    finish();
                }
            });
        } else {
            startActivity(toGetIntent());
            finish();
        }
    }

    @Override
    public void onBackPressed() {

    }
}