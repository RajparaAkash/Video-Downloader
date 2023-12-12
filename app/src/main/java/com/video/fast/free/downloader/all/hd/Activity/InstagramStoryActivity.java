package com.video.fast.free.downloader.all.hd.Activity;

import static com.video.fast.free.downloader.all.hd.Utils.Application.instaCK;
import static com.video.fast.free.downloader.all.hd.Utils.Application.instaParm;
import static com.video.fast.free.downloader.all.hd.Utils.Application.instaStrUrl;
import static com.video.fast.free.downloader.all.hd.Utils.Application.instaUA;
import static com.video.fast.free.downloader.all.hd.Utils.Application.instaUAD;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sdk.mynew.Interstitial_Ads_AdmobBack;
import com.video.fast.free.downloader.all.hd.R;
import com.video.fast.free.downloader.all.hd.Utils.MyPreferences;
import com.video.fast.free.downloader.all.hd.Adpter.InstagramStoryAdapter;
import com.video.fast.free.downloader.all.hd.Model.InstaStoryData;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InstagramStoryActivity extends AppCompatActivity {

    private RecyclerView instaStory_recyclerView;
    private TextView instaStory_notFound_txt;
    private ProgressBar instaStory_progressBar;
    
    ArrayList<InstaStoryData> instaStoryDataArrayList = new ArrayList<>();
    InstagramStoryAdapter myInstaStoryAdpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram_story);

        idBinding();

        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        instaStory_recyclerView.setLayoutManager(gridLayoutManager);

        findViewById(R.id.instaStory_logoutImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialoglogout = ShowHistory(InstagramStoryActivity.this, getResources().getString(R.string.str_logout), getResources().getString(R.string.str_msg_download_media), getResources().getString(R.string.str_cancel));
                AppCompatButton btnCopy = dialoglogout.findViewById(R.id.btnCopy);
                btnCopy.setText("Yes");
                btnCopy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HideLoading(dialoglogout);


                        Dialog dialog = ShowStatusDownloadProgress(InstagramStoryActivity.this, getResources().getString(R.string.str_insta_logout), getResources().getString(R.string.str_process));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                HideLoading(dialog);

                                MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_INSTA_COOKIE, "");
                                MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_INSTA_SESSIONID, "");
                                MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_INSTA_CRFTOKEN, "");
                                MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_INSTA_DS_USERID, "");
                                MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_isINSTALOGIN, false);

                                Toast.makeText(InstagramStoryActivity.this, getResources().getString(R.string.str_logoutSuccess), Toast.LENGTH_SHORT).show();
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

        instaStory_progressBar.setVisibility(View.VISIBLE);
        instaStory_recyclerView.setVisibility(View.GONE);
        instaStory_notFound_txt.setVisibility(View.GONE);

        GetInstaStories();
    }

    public void GetInstaStories() {

        AndroidNetworking.get(instaStrUrl())
                .addHeaders(instaCK(), instaParm(MyPreferences.getPrefsHelper().getPref(MyPreferences.PREF_INSTA_DS_USERID), MyPreferences.getPrefsHelper().getPref(MyPreferences.PREF_INSTA_SESSIONID)))
                .addHeaders(instaUA(), instaUAD())
                .setPriority(Priority.MEDIUM).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String status = response.getString("status");
                    if (status.contains("ok")) {

                        JSONArray tray = response.getJSONArray("tray");

                        if (tray.length() > 0) {

                            for (int i = 0; i < tray.length(); i++) {

                                JSONObject jsonObject = tray.getJSONObject(i);
                                JSONObject user = jsonObject.getJSONObject("user");

                                InstaStoryData instaStoryData = new InstaStoryData();
                                instaStoryData.setUsrPk(user.getLong("pk"));
                                instaStoryData.setFull_name(user.getString("full_name"));
                                instaStoryData.setUsername(user.getString("username"));
                                instaStoryData.setProfile_pic_url(user.getString("profile_pic_url"));
                                instaStoryDataArrayList.add(instaStoryData);
                            }

                            myInstaStoryAdpter = new InstagramStoryAdapter(InstagramStoryActivity.this, instaStoryDataArrayList);
                            instaStory_recyclerView.setAdapter(myInstaStoryAdpter);

                            myInstaStoryAdpter.setOnItemClickListener(new InstagramStoryAdapter.setOnItemClickListener() {
                                @Override
                                public void onItemClick(int i) {
                                    Intent intent = new Intent(InstagramStoryActivity.this, InstaUserStoryActivity.class);
                                    intent.putExtra("userPk", String.valueOf(instaStoryDataArrayList.get(i).getUsrPk()));
                                    intent.putExtra("userName", instaStoryDataArrayList.get(i).getUsername());
                                    startActivity(intent);
                                }
                            });

                            instaStory_recyclerView.setVisibility(View.VISIBLE);
                            instaStory_notFound_txt.setVisibility(View.GONE);

                        } else {
                            instaStory_recyclerView.setVisibility(View.GONE);
                            instaStory_notFound_txt.setVisibility(View.VISIBLE);
                        }

                    } else {
                        instaStory_recyclerView.setVisibility(View.GONE);
                        instaStory_notFound_txt.setVisibility(View.VISIBLE);
                    }


                    instaStory_progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                instaStory_progressBar.setVisibility(View.GONE);
                instaStory_recyclerView.setVisibility(View.GONE);
                instaStory_notFound_txt.setVisibility(View.VISIBLE);

                Dialog dialoglogout = ShowHistory(InstagramStoryActivity.this, getResources().getString(R.string.str_session_out), getResources().getString(R.string.str_msg_login_again_insta), getResources().getString(R.string.str_cancel));
                AppCompatButton btnCopy = dialoglogout.findViewById(R.id.btnCopy);
                btnCopy.setText("Yes");
                btnCopy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HideLoading(dialoglogout);

                        Dialog dialog = ShowStatusDownloadProgress(InstagramStoryActivity.this, getResources().getString(R.string.str_insta_logout), getResources().getString(R.string.str_process));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                HideLoading(dialog);

                                MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_INSTA_COOKIE, "");
                                MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_INSTA_SESSIONID, "");
                                MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_INSTA_CRFTOKEN, "");
                                MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_INSTA_DS_USERID, "");
                                MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_isINSTALOGIN, false);

                                Toast.makeText(InstagramStoryActivity.this, getResources().getString(R.string.str_logoutSuccess), Toast.LENGTH_SHORT).show();
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
        instaStory_recyclerView = (RecyclerView) findViewById(R.id.instaStory_recyclerView);
        instaStory_notFound_txt = (TextView) findViewById(R.id.instaStory_notFound_txt);
        instaStory_progressBar = (ProgressBar) findViewById(R.id.instaStory_progressBar);
    }
}