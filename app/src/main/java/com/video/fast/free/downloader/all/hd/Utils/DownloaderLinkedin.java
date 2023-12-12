package com.video.fast.free.downloader.all.hd.Utils;

import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.HideLoading;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.ShowProgress;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.setLoper;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.text.TextUtils;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.video.fast.free.downloader.all.hd.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class DownloaderLinkedin {

    public static class LinkedinAsync extends AsyncTask<String, String, String> {

        Activity context;
        Dialog dialog;

        public LinkedinAsync(Activity context) {
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
            String dsfdf = "";
            Document doc = null;
            try {
                doc = Jsoup.connect(strings[0]).get();
                Elements meta = doc.select("video");
                dsfdf = meta.attr("data-sources");

            } catch (IOException e) {
                e.printStackTrace();
            }
            return dsfdf;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.isEmpty(s)) {
                HideLoading(dialog);
                setLoper();

                Toast.makeText(context, context.getResources().getString(R.string.str_msg_something_wrong), Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONArray jSONArray = new JSONArray(s);
                    new MyMethod.DownloadFileFromURL(context, dialog, "linkedin").execute(jSONArray.getJSONObject(1).getString("src"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    HideLoading(dialog);
                }

            }
        }
    }
}
