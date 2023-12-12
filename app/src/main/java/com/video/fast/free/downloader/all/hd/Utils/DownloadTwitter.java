package com.video.fast.free.downloader.all.hd.Utils;

import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.HideLoading;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.NetCheck_Dilog;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.ShowProgress;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.checkConnection;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.setLoper;
import static com.video.fast.free.downloader.all.hd.Activity.SplashScreenActivity.mRequestQueue;

import android.app.Activity;
import android.app.Dialog;

import android.view.View;
import android.webkit.URLUtil;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.video.fast.free.downloader.all.hd.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DownloadTwitter {


    public static native String getMainUrl();

    public static native String getParam();


    public static void getTwitterVideoData(String VideoUrl, Activity activity, String mPath) {


        if (!checkConnection(activity)) {
            NetCheck_Dilog(activity);
            return;
        }

        Dialog dialog = ShowProgress(activity);

        LinearLayout dhprogress = dialog.findViewById(R.id.dhprogress);
        ProgressBar dvprogress = dialog.findViewById(R.id.dvprogress);

        dhprogress.setVisibility(View.INVISIBLE);
        dvprogress.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getMainUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            String URL = response.toString();
                            if (URL.contains("url")) {
                                URL = URL.substring(URL.indexOf("url"));
                                URL = URL.substring(ordinalIndexOf(URL, "\"", 1) + 1, ordinalIndexOf(URL, "\"", 2));
                                if (URL.contains("\\")) {
                                    URL = URL.replace("\\", "");
                                }
                                if (URLUtil.isValidUrl(URL)) {

                                    try {
                                        new MyMethod.DownloadFileFromURL(activity, dialog, mPath).execute(URL);

                                    } catch (Exception e) {
                                        setLoper();

                                        HideLoading(dialog);
                                        Toast.makeText(activity, activity.getResources().getString(R.string.str_msg_try_agin), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    setLoper();

                                    HideLoading(dialog);
                                    Toast.makeText(activity, activity.getResources().getString(R.string.str_msg_vid_not_found), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                setLoper();

                                HideLoading(dialog);
                                Toast.makeText(activity, activity.getResources().getString(R.string.str_msg_vid_not_found), Toast.LENGTH_SHORT).show();
                            }
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
                        setLoper();

                        Toast.makeText(activity, activity.getResources().getString(R.string.str_msg_invalid_vid_url), Toast.LENGTH_SHORT).show();


                    }
                }) {
            @Nullable
            @org.jetbrains.annotations.Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(getParam(), getVideoId(VideoUrl));
                return params;
            }
        };
        mRequestQueue.add(stringRequest);
    }


    public static String getVideoId(String link) {
        if (link.contains("?")) {
            link = link.substring(link.indexOf("status"));
            link = link.substring(link.indexOf("/") + 1, link.indexOf("?"));
        } else {
            link = link.substring(link.indexOf("status"));
            link = link.substring(link.indexOf("/") + 1);
        }
        return link;
    }

    private static int ordinalIndexOf(String str, String substr, int n) {
        int pos = -1;
        do {
            pos = str.indexOf(substr, pos + 1);
        } while (n-- > 0 && pos != -1);
        return pos;
    }
}
