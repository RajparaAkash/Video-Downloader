package com.video.fast.free.downloader.all.hd.Utils;

import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.HideLoading;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.NetCheck_Dilog;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.ShowProgress;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.checkConnection;
import static com.video.fast.free.downloader.all.hd.Activity.SplashScreenActivity.mRequestQueue;

import android.app.Activity;
import android.app.Dialog;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.video.fast.free.downloader.all.hd.R;
import com.video.fast.free.downloader.all.hd.Model.VideosData;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DownloadVideos {


    public static native String getMainUrl();

    public static native String getLastUrl();

    public static ArrayList<VideosData> videosArrayList;

    public static void getVideoData(String VideoUrl, Activity activity, String mPath) {

        if (!checkConnection(activity)) {
            NetCheck_Dilog(activity);
            return;
        }
        videosArrayList = new ArrayList<>();
        Dialog dialog = ShowProgress(activity);

        LinearLayout dhprogress = dialog.findViewById(R.id.dhprogress);
        ProgressBar dvprogress = dialog.findViewById(R.id.dvprogress);

        dhprogress.setVisibility(View.INVISIBLE);
        dvprogress.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getMainUrl() + VideoUrl + getLastUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray videos = jsonObject.getJSONArray("videos");

                            if (videos.getJSONObject(0).getString("protocol").contains("http") && !videos.getJSONObject(0).getString("protocol").equals("http_dash_segments")) {
                                VideosData videosData = new VideosData();
                                videosData.setProtocol(videos.getJSONObject(0).getString("protocol"));
                                videosData.setResolution(videos.getJSONObject(0).getString("resolution"));
                                videosData.setUrl(videos.getJSONObject(0).getString("url"));
                                videosArrayList.add(videosData);

                            }

                            JSONArray formats = videos.getJSONObject(0).getJSONArray("formats");

                            for (int i = 0; i < formats.length(); i++) {
                                JSONObject formObj = formats.getJSONObject(i);
                                if (formObj.getString("protocol").contains("http") && !formObj.getString("protocol").equals("http_dash_segments")) {
                                    VideosData videosData = new VideosData();
                                    videosData.setProtocol(formObj.getString("protocol"));
                                    videosData.setResolution(formObj.getString("resolution"));
                                    videosData.setUrl(formObj.getString("url"));
                                    videosArrayList.add(videosData);
                                }
                            }
                            if (videosArrayList.size() > 0) {

                                new MyMethod.DownloadFileFromURL(activity, dialog, mPath).execute(videosArrayList.get(0).getUrl());

                            } else {
                                HideLoading(dialog);
                                Toast.makeText(activity, activity.getResources().getString(R.string.str_msg_url_not_found), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            HideLoading(dialog);
                            Toast.makeText(activity, activity.getResources().getString(R.string.str_msg_something_wrong), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            HideLoading(dialog);
                            Toast.makeText(activity, activity.getResources().getString(R.string.str_msg_something_wrong), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        HideLoading(dialog);
                        Toast.makeText(activity, activity.getResources().getString(R.string.str_msg_url_not_found), Toast.LENGTH_LONG).show();

                    }
                });
        mRequestQueue.add(stringRequest);
    }


}
