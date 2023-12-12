package com.video.fast.free.downloader.all.hd.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sdk.mynew.Interstitial_Ads_AdmobBack;
import com.video.fast.free.downloader.all.hd.Adpter.HistoryAdapter;
import com.video.fast.free.downloader.all.hd.DataBase.DB_History;
import com.video.fast.free.downloader.all.hd.Model.HistoryData;
import com.video.fast.free.downloader.all.hd.R;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView history_recyclerView;
    private TextView history_noData_txt;

    DB_History db_history;
    ArrayList<HistoryData> historyDataArrayList = new ArrayList<HistoryData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        idBinding();

        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        historyDataArrayList = new ArrayList<HistoryData>();
        historyDataArrayList.clear();

        db_history = new DB_History(this);
        historyDataArrayList = db_history.getAllHistory();

        if (historyDataArrayList.size() > 0) {
            history_recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            history_recyclerView.setAdapter(new HistoryAdapter(HistoryActivity.this, historyDataArrayList));

            history_noData_txt.setVisibility(View.GONE);
            history_recyclerView.setVisibility(View.VISIBLE);
        } else {
            history_noData_txt.setVisibility(View.VISIBLE);
            history_recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        Interstitial_Ads_AdmobBack.ShowAd_Full(this, () -> finish());
    }

    public void idBinding(){
        history_recyclerView = (RecyclerView) findViewById(R.id.history_recyclerView);
        history_noData_txt = (TextView) findViewById(R.id.history_noData_txt);
    }
}