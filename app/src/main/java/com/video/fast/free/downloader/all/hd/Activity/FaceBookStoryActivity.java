package com.video.fast.free.downloader.all.hd.Activity;

import static com.video.fast.free.downloader.all.hd.Utils.Application.fbGetAL;
import static com.video.fast.free.downloader.all.hd.Utils.Application.fbGetALD;
import static com.video.fast.free.downloader.all.hd.Utils.Application.fbGetCK;
import static com.video.fast.free.downloader.all.hd.Utils.Application.fbGetDI;
import static com.video.fast.free.downloader.all.hd.Utils.Application.fbGetDID;
import static com.video.fast.free.downloader.all.hd.Utils.Application.fbGetKEY;
import static com.video.fast.free.downloader.all.hd.Utils.Application.fbGetUA;
import static com.video.fast.free.downloader.all.hd.Utils.Application.fbGetUAD;
import static com.video.fast.free.downloader.all.hd.Utils.Application.fbGetUCD;
import static com.video.fast.free.downloader.all.hd.Utils.Application.fbGetUCT;
import static com.video.fast.free.downloader.all.hd.Utils.Application.fbGetV;
import static com.video.fast.free.downloader.all.hd.Utils.Application.fbGetVD;
import static com.video.fast.free.downloader.all.hd.Utils.Application.fbStrUrl;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.HideLoading;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.NetCheck_Dilog;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.ShowHistory;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.ShowStatusDownloadProgress;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.checkConnection;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sdk.mynew.Interstitial_Ads_AdmobBack;
import com.video.fast.free.downloader.all.hd.Adpter.FaceBookStoryAdapter;
import com.video.fast.free.downloader.all.hd.Model.FbStoryData;
import com.video.fast.free.downloader.all.hd.R;
import com.video.fast.free.downloader.all.hd.Utils.MyPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FaceBookStoryActivity extends AppCompatActivity {

    private RecyclerView fbStory_recyclerView;
    private TextView fbStory_notFound_txt;
    private ProgressBar fbStory_progressBar;

    ArrayList<FbStoryData> fbStoryDataArrayList = new ArrayList<>();
    FaceBookStoryAdapter myFbStoryAdpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_story);

        idBinding();

        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fbStoryDataArrayList = new ArrayList<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        fbStory_recyclerView.setLayoutManager(gridLayoutManager);

        findViewById(R.id.fbStory_logoutImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialoglogout = ShowHistory(FaceBookStoryActivity.this, getResources().getString(R.string.str_logout), getResources().getString(R.string.str_msg_download_media), getResources().getString(R.string.str_cancel));
                AppCompatButton btnCopy = dialoglogout.findViewById(R.id.btnCopy);
                btnCopy.setText("Yes");
                btnCopy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HideLoading(dialoglogout);

                        Dialog dialog = ShowStatusDownloadProgress(FaceBookStoryActivity.this, getResources().getString(R.string.str_fb_logout), getResources().getString(R.string.str_process));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                HideLoading(dialog);

                                MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_FB_KEY, "");
                                MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_isFBLOGIN, false);
                                MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_FB_COOKIE, "");

                                CookieManager.getInstance().removeAllCookies((ValueCallback) null);
                                CookieManager.getInstance().flush();
                                Toast.makeText(FaceBookStoryActivity.this, getResources().getString(R.string.str_logoutSuccess), Toast.LENGTH_SHORT).show();

                                finish();
                            }
                        }, 3000);

                    }
                });
            }
        });

        if (!checkConnection(this)) {
            NetCheck_Dilog(this);
            return;
        }

        fbStory_progressBar.setVisibility(View.VISIBLE);
        fbStory_recyclerView.setVisibility(View.GONE);
        fbStory_notFound_txt.setVisibility(View.GONE);

        GetFbStories();
    }

    public void GetFbStories() {

        AndroidNetworking.post(fbStrUrl())
                .addHeaders(fbGetAL(), fbGetALD())
                .addHeaders(fbGetCK(), MyPreferences.getPrefsHelper().getPref(MyPreferences.PREF_FB_COOKIE))
                .addHeaders(fbGetUA(), fbGetUAD())
                .addHeaders(fbGetUCT(), fbGetUCD())
                .addBodyParameter(fbGetKEY(), MyPreferences.getPrefsHelper().getPref(MyPreferences.PREF_FB_KEY))
                .addBodyParameter(fbGetV(), fbGetVD())
                .addBodyParameter(fbGetDI(), fbGetDID())
                .setPriority(Priority.MEDIUM).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject me = response.getJSONObject("data").getJSONObject("me");
                    String uID = me.getString("id");
                    JSONArray edges = me.getJSONObject("unified_stories_buckets").getJSONArray("edges");
                    if (edges.length() > 0) {

                        fbStoryDataArrayList.clear();

                        for (int i = 0; i < edges.length(); i++) {
                            JSONObject jsonObject = edges.getJSONObject(i);
                            String uIDmatch = jsonObject.getJSONObject("node").getJSONObject("story_bucket_owner").getString("id");
                            if (!uID.equalsIgnoreCase(uIDmatch)) {
                                int storyLength = jsonObject.getJSONObject("node").getJSONObject("unified_stories").getJSONArray("edges").length();

                                if (storyLength > 0) {

                                    FbStoryData fbStoryData = new FbStoryData();
                                    fbStoryData.setuKeyID(jsonObject.getJSONObject("node").getString("id"));
                                    fbStoryData.setuName(jsonObject.getJSONObject("node").getJSONObject("story_bucket_owner").getString("name"));
                                    fbStoryData.setuScount(String.valueOf(storyLength));
                                    fbStoryData.setuThumb(jsonObject.getJSONObject("node").getJSONObject("owner").getJSONObject("profile_picture").getString("uri"));
                                    fbStoryDataArrayList.add(fbStoryData);
                                }
                            }
                        }

                        myFbStoryAdpter = new FaceBookStoryAdapter(FaceBookStoryActivity.this, fbStoryDataArrayList);
                        fbStory_recyclerView.setAdapter(myFbStoryAdpter);

                        myFbStoryAdpter.setOnItemClickListener(new FaceBookStoryAdapter.setOnItemClickListener() {
                            @Override
                            public void onItemClick(int i) {
                                Intent intent = new Intent(FaceBookStoryActivity.this, FbUserStoryActivity.class);
                                intent.putExtra("userKey", fbStoryDataArrayList.get(i).getuKeyID());
                                intent.putExtra("userName", fbStoryDataArrayList.get(i).getuName());
                                startActivity(intent);
                            }
                        });
                        fbStory_recyclerView.setVisibility(View.VISIBLE);
                        fbStory_notFound_txt.setVisibility(View.GONE);

                    } else {
                        fbStory_recyclerView.setVisibility(View.GONE);
                        fbStory_notFound_txt.setVisibility(View.VISIBLE);
                    }

                    fbStory_progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                fbStory_progressBar.setVisibility(View.GONE);
                fbStory_recyclerView.setVisibility(View.GONE);
                fbStory_notFound_txt.setVisibility(View.VISIBLE);

                Dialog dialoglogout = ShowHistory(FaceBookStoryActivity.this, getResources().getString(R.string.str_session_out), getResources().getString(R.string.str_msg_login_again_fb), getResources().getString(R.string.str_cancel));
                AppCompatButton btnCopy = dialoglogout.findViewById(R.id.btnCopy);
                btnCopy.setText("Yes");
                btnCopy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HideLoading(dialoglogout);

                        Dialog dialog = ShowStatusDownloadProgress(FaceBookStoryActivity.this, getResources().getString(R.string.str_fb_logout), getResources().getString(R.string.str_process));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                HideLoading(dialog);

                                MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_FB_KEY, "");
                                MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_isFBLOGIN, false);
                                MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_FB_COOKIE, "");

                                CookieManager.getInstance().removeAllCookies((ValueCallback) null);
                                CookieManager.getInstance().flush();
                                Toast.makeText(FaceBookStoryActivity.this, getResources().getString(R.string.str_logoutSuccess), Toast.LENGTH_SHORT).show();

                                finish();
                            }
                        }, 3000);

                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        Interstitial_Ads_AdmobBack.ShowAd_Full(this, () -> finish());
    }

    public void idBinding(){
        fbStory_recyclerView = (RecyclerView) findViewById(R.id.fbStory_recyclerView);
        fbStory_notFound_txt = (TextView) findViewById(R.id.fbStory_notFound_txt);
        fbStory_progressBar = (ProgressBar) findViewById(R.id.fbStory_progressBar);
    }
}

