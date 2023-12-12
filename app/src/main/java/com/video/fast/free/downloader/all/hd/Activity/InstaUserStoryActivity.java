package com.video.fast.free.downloader.all.hd.Activity;

import static com.video.fast.free.downloader.all.hd.Utils.Application.instaCK;
import static com.video.fast.free.downloader.all.hd.Utils.Application.instaParm;
import static com.video.fast.free.downloader.all.hd.Utils.Application.instaUA;
import static com.video.fast.free.downloader.all.hd.Utils.Application.instaUAD;
import static com.video.fast.free.downloader.all.hd.Utils.Application.instaUserStoryStrUrl;
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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sdk.mynew.Interstitial_Ads_AdmobBack;
import com.video.fast.free.downloader.all.hd.Adpter.InstaUserStoryAdapter;
import com.video.fast.free.downloader.all.hd.Model.InstaStoryData;
import com.video.fast.free.downloader.all.hd.R;
import com.video.fast.free.downloader.all.hd.Utils.MyPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InstaUserStoryActivity extends AppCompatActivity {

    private TextView instaus_header_txt;
    private RecyclerView instaus_recyclerView;
    private TextView instaus_noData_txt;
    private ProgressBar instaus_progressBar;
    
    ArrayList<InstaStoryData> instaStoryDataArrayList = new ArrayList<>();
    InstaUserStoryAdapter myInstaUserStoryAdpter;

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

        instaus_header_txt.setText(getIntent().getStringExtra("userName"));
        
        instaStoryDataArrayList = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        instaus_recyclerView.setLayoutManager(gridLayoutManager);
        
        if (!checkConnection(this)) {
            NetCheck_Dilog(this);
            return;
        }

        instaus_progressBar.setVisibility(View.VISIBLE);
        instaus_recyclerView.setVisibility(View.GONE);
        instaus_noData_txt.setVisibility(View.GONE);
        
        GetUserInstaStories();
    }

    public void GetUserInstaStories() {

        AndroidNetworking.get(instaUserStoryStrUrl(getIntent().getStringExtra("userPk")))
                .addHeaders(instaCK(), instaParm(MyPreferences.getPrefsHelper().getPref(MyPreferences.PREF_INSTA_DS_USERID), MyPreferences.getPrefsHelper().getPref(MyPreferences.PREF_INSTA_SESSIONID)))
                .addHeaders(instaUA(), instaUAD())
                .setPriority(Priority.MEDIUM).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray items = response.getJSONObject("reel_feed").getJSONArray("items");

                    if (items.length() > 0) {
                        instaStoryDataArrayList.clear();
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject jsonObject = items.getJSONObject(i);

                            InstaStoryData instaStoryData = new InstaStoryData();
                            instaStoryData.setMedia_type(jsonObject.getInt("media_type"));
                            instaStoryData.setImageVersionUrl(jsonObject.getJSONObject("image_versions2").getJSONArray("candidates").getJSONObject(0).getString("url"));

                            if (jsonObject.getInt("media_type") == 2) {
                                instaStoryData.setVideoVersionUrl(jsonObject.getJSONArray("video_versions").getJSONObject(0).getString("url"));
                            } else {
                                instaStoryData.setVideoVersionUrl("");
                            }

                            instaStoryDataArrayList.add(instaStoryData);
                        }

                        myInstaUserStoryAdpter = new InstaUserStoryAdapter(InstaUserStoryActivity.this, instaStoryDataArrayList);
                        instaus_recyclerView.setAdapter(myInstaUserStoryAdpter);

                        myInstaUserStoryAdpter.setOnItemClickListener(new InstaUserStoryAdapter.setOnItemClickListener() {
                            @Override
                            public void onItemClick(int i) {
                                Intent intent = new Intent(InstaUserStoryActivity.this, FbInstaStoryViewerActivity.class);
                                if (instaStoryDataArrayList.get(i).getMedia_type() == 2) {
                                    intent.putExtra("mediaURl", instaStoryDataArrayList.get(i).getVideoVersionUrl());
                                    intent.putExtra("isVid", true);
                                } else {
                                    intent.putExtra("mediaURl", instaStoryDataArrayList.get(i).getImageVersionUrl());
                                    intent.putExtra("isVid", false);
                                }
                                startActivity(intent);
                            }
                        });

                        instaus_recyclerView.setVisibility(View.VISIBLE);
                        instaus_noData_txt.setVisibility(View.GONE);

                    } else {
                        instaus_recyclerView.setVisibility(View.GONE);
                        instaus_noData_txt.setVisibility(View.VISIBLE);
                    }

                    instaus_progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                instaus_progressBar.setVisibility(View.GONE);
                instaus_recyclerView.setVisibility(View.GONE);
                instaus_noData_txt.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Interstitial_Ads_AdmobBack.ShowAd_Full(this, () -> finish());
    }

    public void idBinding(){
        instaus_header_txt = (TextView) findViewById(R.id.instaus_header_txt);
        instaus_recyclerView = (RecyclerView) findViewById(R.id.instaus_recyclerView);
        instaus_noData_txt = (TextView) findViewById(R.id.instaus_noData_txt);
        instaus_progressBar = (ProgressBar) findViewById(R.id.instaus_progressBar);
    }
}