package com.video.fast.free.downloader.all.hd.Activity;

import static com.video.fast.free.downloader.all.hd.Utils.Application.fbGetAL;
import static com.video.fast.free.downloader.all.hd.Utils.Application.fbGetALD;
import static com.video.fast.free.downloader.all.hd.Utils.Application.fbGetCK;
import static com.video.fast.free.downloader.all.hd.Utils.Application.fbGetDI;
import static com.video.fast.free.downloader.all.hd.Utils.Application.fbGetKEY;
import static com.video.fast.free.downloader.all.hd.Utils.Application.fbGetUA;
import static com.video.fast.free.downloader.all.hd.Utils.Application.fbGetUAD;
import static com.video.fast.free.downloader.all.hd.Utils.Application.fbGetUCD;
import static com.video.fast.free.downloader.all.hd.Utils.Application.fbGetUCT;
import static com.video.fast.free.downloader.all.hd.Utils.Application.fbGetUSerDID;
import static com.video.fast.free.downloader.all.hd.Utils.Application.fbGetUserVD;
import static com.video.fast.free.downloader.all.hd.Utils.Application.fbGetV;
import static com.video.fast.free.downloader.all.hd.Utils.Application.fbStrUrl;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.NetCheck_Dilog;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.checkConnection;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sdk.mynew.Interstitial_Ads_AdmobBack;
import com.video.fast.free.downloader.all.hd.R;
import com.video.fast.free.downloader.all.hd.Utils.MyPreferences;
import com.video.fast.free.downloader.all.hd.Adpter.FbUserStoryAdapter;
import com.video.fast.free.downloader.all.hd.Model.FbStoryData;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FbUserStoryActivity extends AppCompatActivity {
    
    private TextView fbus_header_txt;
    private RecyclerView fbus_recyclerView;
    private TextView fbus_noData_txt;
    private ProgressBar fbus_progressBar;

    FbUserStoryAdapter fbStoryAdpter;
    ArrayList<FbStoryData> fbStoryDataArrayList = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_user_story);

        idBinding();

        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fbus_header_txt.setText(getIntent().getStringExtra("userName"));

        fbStoryDataArrayList = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        fbus_recyclerView.setLayoutManager(gridLayoutManager);

        if (!checkConnection(this)) {
            NetCheck_Dilog(this);
            return;
        }

        fbus_progressBar.setVisibility(View.VISIBLE);
        fbus_recyclerView.setVisibility(View.GONE);
        fbus_noData_txt.setVisibility(View.GONE);
        
        GetUserFbStories();
    }

    public void GetUserFbStories() {

        AndroidNetworking.post(fbStrUrl())
                .addHeaders(fbGetAL(), fbGetALD())
                .addHeaders(fbGetCK(), MyPreferences.getPrefsHelper().getPref(MyPreferences.PREF_FB_COOKIE))
                .addHeaders(fbGetUA(), fbGetUAD())
                .addHeaders(fbGetUCT(), fbGetUCD())
                .addBodyParameter(fbGetKEY(), MyPreferences.getPrefsHelper().getPref(MyPreferences.PREF_FB_KEY))
                .addBodyParameter(fbGetV(), fbGetUserVD(getIntent().getStringExtra("userKey")))
                .addBodyParameter(fbGetDI(), fbGetUSerDID())
                .setPriority(Priority.MEDIUM).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray edges = response.getJSONObject("data").getJSONObject("bucket").getJSONObject("unified_stories").getJSONArray("edges");

                    if (edges.length() > 0) {
                        fbStoryDataArrayList.clear();
                        for (int i = 0; i < edges.length(); i++) {
                            JSONObject jsonObject = edges.getJSONObject(i);

                            long j = jsonObject.getJSONObject("node").getLong("creation_time");

                            JSONArray attachments = jsonObject.getJSONObject("node").getJSONArray("attachments");
                            if (attachments.length() > 0) {
                                String str;
                                JSONObject jSONObject2 = attachments.getJSONObject(0).getJSONObject("media");
                                String typename = jSONObject2.getString("__typename");
                                String string2 = jSONObject2.getJSONObject("previewImage").getString("uri");

                                if (!typename.equalsIgnoreCase("Photo")) {
                                    if (typename.equalsIgnoreCase("Video")) {
                                        if (jSONObject2.has("playable_url_quality_hd")) {
                                            str = jSONObject2.getString("playable_url_quality_hd");
                                        } else {
                                            str = "";
                                        }
                                        if (str == null || str.length() < 10) {
                                            String string3 = jSONObject2.getString("playable_url");

                                            FbStoryData fbStoryData = new FbStoryData();
                                            fbStoryData.setUserImage(string2);
                                            fbStoryData.setUserVideo(string3);
                                            fbStoryData.setStoryAddTime(j);
                                            fbStoryData.setVideo(true);

                                            fbStoryDataArrayList.add(fbStoryData);

                                        } else {
                                            FbStoryData fbStoryData = new FbStoryData();
                                            fbStoryData.setUserImage(string2);
                                            fbStoryData.setUserVideo(str);
                                            fbStoryData.setStoryAddTime(j);
                                            fbStoryData.setVideo(true);

                                            fbStoryDataArrayList.add(fbStoryData);
                                        }
                                    }
                                } else {
                                    String string5 = jSONObject2.getJSONObject("image").getString("uri");

                                    FbStoryData fbStoryData = new FbStoryData();
                                    fbStoryData.setUserImage(string2);
                                    fbStoryData.setUserVideo(string5);
                                    fbStoryData.setStoryAddTime(j);
                                    fbStoryData.setVideo(false);

                                    fbStoryDataArrayList.add(fbStoryData);
                                }
                            }
                        }

                        fbStoryAdpter = new FbUserStoryAdapter(FbUserStoryActivity.this, fbStoryDataArrayList);
                        fbus_recyclerView.setAdapter(fbStoryAdpter);

                        fbStoryAdpter.setOnItemClickListener(new FbUserStoryAdapter.setOnItemClickListener() {
                            @Override
                            public void onItemClick(int i) {
                                Intent intent = new Intent(FbUserStoryActivity.this, FbInstaStoryViewerActivity.class);
                                intent.putExtra("mediaURl", fbStoryDataArrayList.get(i).getUserVideo());
                                intent.putExtra("isVid", fbStoryDataArrayList.get(i).isVideo());
                                startActivity(intent);
                            }
                        });

                        fbus_recyclerView.setVisibility(View.VISIBLE);
                        fbus_noData_txt.setVisibility(View.GONE);

                    } else {
                        fbus_recyclerView.setVisibility(View.GONE);
                        fbus_noData_txt.setVisibility(View.VISIBLE);
                    }

                    fbus_progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                fbus_progressBar.setVisibility(View.GONE);
                fbus_recyclerView.setVisibility(View.GONE);
                fbus_noData_txt.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Interstitial_Ads_AdmobBack.ShowAd_Full(this, () -> finish());
    }

    public void idBinding(){
        fbus_header_txt = (TextView) findViewById(R.id.fbus_header_txt);
        fbus_recyclerView = (RecyclerView) findViewById(R.id.fbus_recyclerView);
        fbus_noData_txt = (TextView) findViewById(R.id.fbus_noData_txt);
        fbus_progressBar = (ProgressBar) findViewById(R.id.fbus_progressBar);
    }
}