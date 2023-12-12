package com.video.fast.free.downloader.all.hd.Utils;

import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.HideLoading;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.NetCheck_Dilog;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.ShowProgress;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.checkConnection;
import static com.video.fast.free.downloader.all.hd.Activity.SplashScreenActivity.mRequestQueue;

import android.app.Activity;
import android.app.Dialog;

import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.video.fast.free.downloader.all.hd.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class DownloadInstagram {

    public static void startInstaDownload(Activity activity, String str) {


        if (!checkConnection(activity)) {
            NetCheck_Dilog(activity);
            return;
        }

        Dialog dialog = ShowProgress(activity);
        try {

            LinearLayout dhprogress = dialog.findViewById(R.id.dhprogress);
            ProgressBar dvprogress = dialog.findViewById(R.id.dvprogress);

            dhprogress.setVisibility(View.INVISIBLE);
            dvprogress.setVisibility(View.VISIBLE);

            URI uri = new URI(str);
            String uri2 = new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), (String) null, uri.getFragment()).toString();
            String element = uri2;
            if (element.contains("/reel/")) {
                element = element.replace("/reel/", "/p/");
            }
            if (element.contains("/tv/")) {
                element = element.replace("/tv/", "/p/");
            }
            String instaurl = getUrl(element);

            StringRequest
                    stringRequest
                    = new StringRequest(
                    Request.Method.GET,
                    instaurl,
                    (Response.Listener) response -> {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            JSONObject graphql = jsonObject.getJSONObject("graphql");
                            JSONObject shortcode_media = graphql.getJSONObject("shortcode_media");
                            boolean is_video = shortcode_media.getBoolean("is_video");
                            if (is_video) {
                                String video_url = shortcode_media.getString("video_url");
                                new MyMethod.DownloadFileFromURL(activity, dialog, "Insta").execute(video_url);
                            } else {
                                HideLoading(dialog);
                                Toast.makeText(activity, activity.getResources().getString(R.string.str_msg_vid_not_found), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            HideLoading(dialog);
                            Toast.makeText(activity, activity.getResources().getString(R.string.str_msg_something_wrong), Toast.LENGTH_LONG).show();
                        }
                    },
                    error -> {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                HideLoading(dialog);
                                Toast.makeText(activity, activity.getResources().getString(R.string.str_msg_url_not_found), Toast.LENGTH_LONG).show();

                            }
                        },2500);
                    });
            mRequestQueue.add(stringRequest);

        } catch (URISyntaxException e) {
            e.printStackTrace();
            HideLoading(dialog);

        }

    }

    public static native String getUrl(String url);

}
