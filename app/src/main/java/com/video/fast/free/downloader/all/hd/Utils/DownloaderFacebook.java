package com.video.fast.free.downloader.all.hd.Utils;

import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.HideLoading;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.ShowProgress;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.setLoper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.video.fast.free.downloader.all.hd.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloaderFacebook {


    public static String VideoTitle;


    @SuppressLint("StaticFieldLeak")
    public static class Data extends AsyncTask<String, String, String> {
        Activity context;
        Dialog dialog;

        public Data(Activity context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ShowProgress(context);
            LinearLayout dhprogress = dialog.findViewById(R.id.dhprogress);
            ProgressBar dvprogress = dialog.findViewById(R.id.dvprogress);

            dhprogress.setVisibility(View.INVISIBLE);
            dvprogress.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection;
            BufferedReader reader;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                String buffer = "No URL";
                String Line;
                while ((Line = reader.readLine()) != null) {

                    if (Line.contains("og:video:url")) {
                        Line = Line.substring(Line.indexOf("og:video:url"));
                        if (Line.contains("og:title")) {
                            VideoTitle = Line.substring(Line.indexOf("og:title"));
                            VideoTitle = VideoTitle.substring(ordinalIndexOf(VideoTitle, "\"", 1) + 1, ordinalIndexOf(VideoTitle, "\"", 2));
                        }
                        Line = Line.substring(ordinalIndexOf(Line, "\"", 1) + 1, ordinalIndexOf(Line, "\"", 2));
                        if (Line.contains("amp;")) {
                            Line = Line.replace("amp;", "");
                        }
                        if (!Line.contains("https")) {
                            Line = Line.replace("http", "https");
                        }
                        buffer = Line;
                        break;
                    } else {
                        buffer = "No URL";
                    }
                }
                return buffer;
            } catch (IOException e) {
                return "No URL";
            }
        }

        @Override
        protected void onPostExecute(String s) {

            if (!s.contains("No URL")) {
                try {
                    new MyMethod.DownloadFileFromURL(context, dialog, "Facebook").execute(s);

                } catch (Exception e) {
                    HideLoading(dialog);
                    setLoper();

                    Toast.makeText(context, context.getResources().getString(R.string.str_msg_try_agin), Toast.LENGTH_SHORT).show();
                }

            } else {
                HideLoading(dialog);

                setLoper();


                Toast.makeText(context, context.getResources().getString(R.string.str_msg_invalid_vid_url), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            HideLoading(dialog);
            setLoper();

            Toast.makeText(context, context.getResources().getString(R.string.str_msg_try_agin), Toast.LENGTH_SHORT).show();

        }
    }

    private static int ordinalIndexOf(String str, String substr, int n) {
        int pos = -1;
        do {
            pos = str.indexOf(substr, pos + 1);
        } while (n-- > 0 && pos != -1);
        return pos;
    }


}
