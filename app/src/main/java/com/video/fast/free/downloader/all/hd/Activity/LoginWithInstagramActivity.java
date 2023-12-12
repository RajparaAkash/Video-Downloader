package com.video.fast.free.downloader.all.hd.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sdk.mynew.Interstitial_Ads_AdmobBack;
import com.video.fast.free.downloader.all.hd.R;
import com.video.fast.free.downloader.all.hd.Utils.MyPreferences;

import java.util.Random;

public class LoginWithInstagramActivity extends AppCompatActivity {

    private WebView loginInsta_webView;
    private ProgressBar loginInsta_progressBar;
    
    public static String[] auX = {"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.63 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.63 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.63 Safari/537.36"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_instagram);

        idBinding();

        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loginInsta_webView.getSettings().setJavaScriptEnabled(true);
        loginInsta_webView.clearCache(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(loginInsta_webView, true);
        loginInsta_webView.setWebViewClient(new WebClient());
        loginInsta_webView.setWebChromeClient(new WebChrom());
        CookieSyncManager.createInstance(this);
        CookieManager.getInstance().removeAllCookie();
        loginInsta_webView.loadUrl("https://www.instagram.com/accounts/login/");
        try {
            Random random = new Random();
            int nextInt = random.nextInt(auX.length);
            loginInsta_webView.getSettings().setUserAgentString(auX[nextInt] + "");
        } catch (Exception e) {
            loginInsta_webView.getSettings().setUserAgentString("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36");
        }
    }

    public class WebChrom extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress < 100) {
                loginInsta_progressBar.setVisibility(View.VISIBLE);
                loginInsta_webView.setVisibility(View.GONE);
                return;
            }
            loginInsta_progressBar.setVisibility(View.GONE);
            loginInsta_webView.setVisibility(View.VISIBLE);
        }

    }

    public class WebClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            String cookie = CookieManager.getInstance().getCookie(url);
            try {
                String sessionid = getCookie(cookie, "sessionid");
                String csrftoken = getCookie(cookie, "csrftoken");
                String ds_user_id = getCookie(cookie, "ds_user_id");

                if (sessionid != null && csrftoken != null && ds_user_id != null) {

                    MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_INSTA_COOKIE, cookie);
                    MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_INSTA_SESSIONID, sessionid);
                    MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_INSTA_CRFTOKEN, csrftoken);
                    MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_INSTA_DS_USERID, ds_user_id);
                    MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_isINSTALOGIN, true);

                    view.destroy();
                    Intent intent = new Intent();
                    intent.putExtra("result", "result");
                    setResult(RESULT_OK, intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

        }
    }


    public String getCookie(String cookie, String str2) {
        if (cookie != null && !cookie.isEmpty()) {
            for (String str3 : cookie.split(";")) {
                if (str3.contains(str2)) {
                    return str3.split("=")[1];
                }
            }
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        ((ViewGroup) loginInsta_webView.getParent()).removeView(loginInsta_webView);
        loginInsta_webView.destroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Interstitial_Ads_AdmobBack.ShowAd_Full(this, () -> finish());
    }

    public void idBinding() {
        loginInsta_webView = (WebView) findViewById(R.id.loginInsta_webView);
        loginInsta_progressBar = (ProgressBar) findViewById(R.id.loginInsta_progressBar);
    }
}