package com.video.fast.free.downloader.all.hd.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.video.fast.free.downloader.all.hd.R;
import com.video.fast.free.downloader.all.hd.Utils.WhatsAppUtil;
import com.video.fast.free.downloader.all.hd.Adpter.WaStatusAdapter;
import com.video.fast.free.downloader.all.hd.Model.StatusModel;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class WaStatus4VideosFragment extends Fragment implements WaStatusAdapter.OnCheckboxListener {

    LinearLayout was4_actionLay;
    LinearLayout was4_deleteIV;
    RecyclerView was4_gridView;
    CheckBox was4_selectAll;
    RelativeLayout was4_loaderLay;
    RelativeLayout was4_emptyLay;

    int save = 10;
    WaStatusAdapter adapter;
    ArrayList<StatusModel> fileList = null;
    ArrayList<StatusModel> filesToDelete = new ArrayList<>();
    ArrayList<StatusModel> temporary = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_wa_status_4_videos, viewGroup, false);

        idBinding(inflate);

        GridLayoutManager gridlayoutManager = new GridLayoutManager(getActivity(), 2);
        gridlayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // 5 is the sum of items in one repeated section
                if (fileList != null && fileList.size() > 0 && fileList.get(position) == null)
                    return 2;
                return 1;
            }
        });
        was4_gridView.setLayoutManager(gridlayoutManager);

        new loadDataAsync().execute(new Void[0]);

        was4_deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WaStatus4VideosFragment.this.filesToDelete.isEmpty()) {
                    return;
                }
                new AlertDialog.Builder(WaStatus4VideosFragment.this.getContext()).setMessage(WaStatus4VideosFragment.this.getResources().getString(R.string.delete_alert)).setCancelable(true).setNegativeButton(WaStatus4VideosFragment.this.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new deleteAll().execute(new Void[0]);
                    }
                }).setPositiveButton(WaStatus4VideosFragment.this.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
            }
        });

        was4_selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (compoundButton.isPressed()) {
                    WaStatus4VideosFragment.this.filesToDelete.clear();
                    int i = 0;
                    while (true) {
                        if (i >= WaStatus4VideosFragment.this.fileList.size()) {
                            break;
                        } else if (!WaStatus4VideosFragment.this.fileList.get(i).selected) {
                            z = true;
                            break;
                        } else {
                            i++;
                        }
                    }
                    if (z) {
                        for (int i2 = 0; i2 < WaStatus4VideosFragment.this.fileList.size(); i2++) {
                            WaStatus4VideosFragment.this.fileList.get(i2).selected = true;
                            WaStatus4VideosFragment.this.filesToDelete.add(WaStatus4VideosFragment.this.fileList.get(i2));
                        }
                        was4_selectAll.setChecked(true);
                    } else {
                        for (int i3 = 0; i3 < WaStatus4VideosFragment.this.fileList.size(); i3++) {
                            WaStatus4VideosFragment.this.fileList.get(i3).selected = false;
                        }
                        was4_actionLay.setVisibility(View.GONE);
                    }
                    WaStatus4VideosFragment.this.adapter.notifyDataSetChanged();
                }
            }
        });

        return inflate;
    }

    
    class deleteAll extends AsyncTask<Void, Void, Void> {
        AlertDialog alertDialog;
        int success = -1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AlertDialog loadingPopup = WhatsAppUtil.loadingPopup(WaStatus4VideosFragment.this.getActivity());
            this.alertDialog = loadingPopup;
            loadingPopup.show();
        }

        
        @Override
        public Void doInBackground(Void... voidArr) {
            ArrayList arrayList = new ArrayList();
            Iterator<StatusModel> it = WaStatus4VideosFragment.this.filesToDelete.iterator();
            while (it.hasNext()) {
                StatusModel next = it.next();
                File file = new File(next.getFilePath());
                if (file.exists()) {
                    if (file.delete()) {
                        arrayList.add(next);
                        if (this.success == 0) {
                            break;
                        }
                        this.success = 1;
                    } else {
                        this.success = 0;
                    }
                } else {
                    this.success = 0;
                }
            }
            WaStatus4VideosFragment.this.filesToDelete.clear();
            Iterator it2 = arrayList.iterator();
            while (it2.hasNext()) {
                WaStatus4VideosFragment.this.fileList.remove((StatusModel) it2.next());
            }
            return null;
        }

        
        @Override
        public void onPostExecute(Void r5) {
            super.onPostExecute(r5);
            adapter.notifyDataSetChanged();
            int i = this.success;
            if (i == 0) {
                Toast.makeText(WaStatus4VideosFragment.this.getContext(), WaStatus4VideosFragment.this.getResources().getString(R.string.delete_error), Toast.LENGTH_SHORT).show();
            } else if (i == 1) {
                Toast.makeText(WaStatus4VideosFragment.this.getActivity(), WaStatus4VideosFragment.this.getResources().getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
            }
            was4_actionLay.setVisibility(View.GONE);
            was4_selectAll.setChecked(false);
            alertDialog.dismiss();
        }
    }

    public class loadDataAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            was4_loaderLay.setVisibility(View.VISIBLE);
        }

        
        @Override
        public Void doInBackground(Void... voidArr) {
            WaStatus4VideosFragment.this.getFromSdcard();
            return null;
        }

        
        @Override
        public void onPostExecute(Void r4) {
            super.onPostExecute(r4);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (WaStatus4VideosFragment.this.fileList != null) {
                        addNativeAds();
                        adapter = new WaStatusAdapter(WaStatus4VideosFragment.this, fileList, WaStatus4VideosFragment.this);
                        was4_gridView.setAdapter(adapter);
                    }
                    was4_loaderLay.setVisibility(View.GONE);
                    if (WaStatus4VideosFragment.this.fileList == null || WaStatus4VideosFragment.this.fileList.size() == 0) {
                        was4_emptyLay.setVisibility(View.VISIBLE);
                    } else {
                        was4_emptyLay.setVisibility(View.GONE);
                    }
                }
            }, 1000L);
        }
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        this.adapter.onActivityResult(i, i2, intent);
        int i3 = this.save;
        if (i == i3 && i2 == i3) {
            this.adapter.notifyDataSetChanged();
            getFromSdcard();
            if (this.fileList != null) {
                WaStatusAdapter waStatusAdapter = new WaStatusAdapter(this, this.fileList, this);
                addNativeAds();
                this.adapter = waStatusAdapter;
                was4_gridView.setAdapter(waStatusAdapter);
            }
            was4_actionLay.setVisibility(View.GONE);
            was4_selectAll.setChecked(false);
        }
    }

    public void getFromSdcard() {
        File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "Download" + File.separator + getResources().getString(R.string.app_name) + "/WA_Videos");
        if (file.isDirectory()) {
            this.fileList = new ArrayList<>();
            if (file.isDirectory()) {
                File[] listFiles = file.listFiles();
                Arrays.sort(listFiles, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
                for (File file2 : listFiles) {
                    this.fileList.add(new StatusModel(file2.getAbsolutePath()));
                }
            }
        }
    }

    @Override
    public void onCheckboxListener(View view, List<StatusModel> list) {
        this.filesToDelete.clear();
        for (StatusModel statusModel : list) {
            if (statusModel.isSelected()) {
                this.filesToDelete.add(statusModel);
            }
        }
        if (this.filesToDelete.size() == this.fileList.size()) {
            was4_selectAll.setChecked(true);
        }
        if (!this.filesToDelete.isEmpty()) {
            was4_actionLay.setVisibility(View.VISIBLE);
            return;
        }
        was4_selectAll.setChecked(false);
        was4_actionLay.setVisibility(View.GONE);
    }

    private void addNativeAds() {
        int firstNativeAdPositon = 2;
        int interationCount = 6;
        temporary = new ArrayList<>();
        int startIndex = 0;
        for (int i = 0; i < fileList.size(); i++, startIndex++) {
            if (i == firstNativeAdPositon) {
                temporary.add(null);
                startIndex++;
                firstNativeAdPositon += interationCount;
            }
            StatusModel statusModel = fileList.get(i);
            temporary.add(statusModel);
        }

        fileList.clear();
        fileList.addAll(temporary);
    }

    public void idBinding(View myView){
        was4_gridView = (RecyclerView) myView.findViewById(R.id.was4_videoGrid);
        was4_actionLay = (LinearLayout) myView.findViewById(R.id.was4_actionLay);
        was4_deleteIV = (LinearLayout) myView.findViewById(R.id.was4_deleteIV);
        was4_selectAll = (CheckBox) myView.findViewById(R.id.was4_selectAll);
        was4_loaderLay = (RelativeLayout) myView.findViewById(R.id.loaderLay);
        was4_emptyLay = (RelativeLayout) myView.findViewById(R.id.emptyLay);
    }
}
