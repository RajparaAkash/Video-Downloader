package com.video.fast.free.downloader.all.hd.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sdk.mynew.Interstitial_Ads_AdmobBack;
import com.video.fast.free.downloader.all.hd.R;
import com.video.fast.free.downloader.all.hd.Utils.MyPreferences;

import java.util.Random;

public class LoginWithFaceBookActivity extends AppCompatActivity {

    private WebView loginFb_webView;
    private ProgressBar loginFb_progressBar;
    
    public static String[] auX = {"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.63 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.63 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.63 Safari/537.36"};
    
    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_facebook);

        idBinding();

        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loginFb_webView.getSettings().setJavaScriptEnabled(true);
        loginFb_webView.getSettings().setDomStorageEnabled(true);
        loginFb_webView.getSettings().setBuiltInZoomControls(true);
        loginFb_webView.getSettings().setDisplayZoomControls(true);
        loginFb_webView.getSettings().setUseWideViewPort(true);
        loginFb_webView.getSettings().setLoadWithOverviewMode(true);
        loginFb_webView.addJavascriptInterface(this, "FB");
        loginFb_webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        CookieManager.getInstance().setAcceptThirdPartyCookies(loginFb_webView, true);
        loginFb_webView.getSettings().setMixedContentMode(2);
        loginFb_webView.setWebViewClient(new WebClient());
        loginFb_webView.setWebChromeClient(new WebChrom());
        CookieSyncManager.createInstance(this);
        CookieManager.getInstance().setAcceptCookie(true);
        CookieSyncManager.getInstance().startSync();
        loginFb_webView.loadUrl("https://www.facebook.com/");
        try {
            Random random = new Random();
            int nextInt = random.nextInt(auX.length);
            loginFb_webView.getSettings().setUserAgentString(auX[nextInt] + "");
        } catch (Exception e) {
            loginFb_webView.getSettings().setUserAgentString("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.63 Safari/537.36");
        }
    }

    public class WebChrom extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress < 100) {
                loginFb_progressBar.setVisibility(View.VISIBLE);
                loginFb_webView.setVisibility(View.GONE);
                return;
            }
            loginFb_progressBar.setVisibility(View.GONE);
            loginFb_webView.setVisibility(View.VISIBLE);
        }
    }

    public class WebClient extends WebViewClient {

        public final boolean isValidData(WebView webView, String str) {

            webView.loadUrl(str);
            String cookie = CookieManager.getInstance().getCookie(str);
            if (!isStringExits(cookie)) {
                return true;
            }
            MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_FB_COOKIE, cookie);

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            view.loadUrl("javascript:FB.keyFound();");
            view.loadUrl("javascript:var el = document.querySelector('body');FB.keyFound(el.innerHTML);");
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            String uri = request.getUrl().toString();
            if (uri.contains("https://www.facebook.com/api/graphqlbatch")) {
                StringBuilder sb = new StringBuilder();
                sb.append("found graph url ");
                sb.append(uri);
                sb.append("   ");
                sb.append(request.getRequestHeaders().get("cookie"));
            }
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return isValidData(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return isValidData(view, request.getUrl().toString());
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

    public static boolean isStringExits(String str) {
        return !TextUtils.isEmpty(str) && str.contains("c_user");
    }

    @JavascriptInterface
    public void keyFound(String str) {
        String substringBetween = substringBetween(substringBetween(str, "token", "async_get_token"), "\":\"", "\"},");
        if (substringBetween.length() < 15) {
            return;
        }
        if (isStringExits(CookieManager.getInstance().getCookie("https://www.facebook.com"))) {

            MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_FB_KEY, substringBetween);
            MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_isFBLOGIN, true);

            Intent intent = new Intent();
            intent.putExtra("resultfb", "result");
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public static String substringBetween(String str, String str2, String str3) {
        int indexOf;
        int indexOf2;
        if (str == null || str2 == null || str3 == null || (indexOf = str.indexOf(str2)) == -1 || (indexOf2 = str.indexOf(str3, str2.length() + indexOf)) == -1) {
            return null;
        }
        return str.substring(str2.length() + indexOf, indexOf2);
    }
    
    @Override
    protected void onDestroy() {
        ((ViewGroup) loginFb_webView.getParent()).removeView(loginFb_webView);
        loginFb_webView.destroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Interstitial_Ads_AdmobBack.ShowAd_Full(this, () -> finish());
    }
    
    public void idBinding(){
        loginFb_webView = (WebView) findViewById(R.id.loginFb_webView);
        loginFb_progressBar = (ProgressBar) findViewById(R.id.loginFb_progressBar);
    }
}