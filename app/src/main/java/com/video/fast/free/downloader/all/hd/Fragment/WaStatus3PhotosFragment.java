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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.video.fast.free.downloader.all.hd.Adpter.WaStatusAdapter;
import com.video.fast.free.downloader.all.hd.Model.StatusModel;
import com.video.fast.free.downloader.all.hd.R;
import com.video.fast.free.downloader.all.hd.Utils.WhatsAppUtil;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class WaStatus3PhotosFragment extends Fragment implements WaStatusAdapter.OnCheckboxListener {

    LinearLayout was3_actionLay;
    LinearLayout was3_deleteIV;
    RecyclerView was3_imageGrid;
    RelativeLayout was3_loaderLay;
    RelativeLayout was3_emptyLay;
    CheckBox was3_selectAll;

    WaStatusAdapter myAdapter;
    int save = 10;
    ArrayList<StatusModel> fileList = new ArrayList<>();
    ArrayList<StatusModel> filesToDelete = new ArrayList<>();
    ArrayList<StatusModel> temporary = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_wa_status_3_photos, viewGroup, false);

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
        was3_imageGrid.setLayoutManager(gridlayoutManager);

        new loadDataAsync().execute(new Void[0]);

        was3_deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WaStatus3PhotosFragment.this.filesToDelete.isEmpty()) {
                    return;
                }
                new AlertDialog.Builder(WaStatus3PhotosFragment.this.getContext()).setMessage(WaStatus3PhotosFragment.this.getResources().getString(R.string.delete_alert)).setCancelable(true).setNegativeButton(WaStatus3PhotosFragment.this.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new deleteAll().execute(new Void[0]);
                    }
                }).setPositiveButton(WaStatus3PhotosFragment.this.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
            }
        });


        was3_selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (compoundButton.isPressed()) {
                    WaStatus3PhotosFragment.this.filesToDelete.clear();
                    int i = 0;
                    while (true) {
                        if (i >= WaStatus3PhotosFragment.this.fileList.size()) {
                            break;
                        } else if (!WaStatus3PhotosFragment.this.fileList.get(i).selected) {
                            z = true;
                            break;
                        } else {
                            i++;
                        }
                    }
                    if (z) {
                        for (int i2 = 0; i2 < WaStatus3PhotosFragment.this.fileList.size(); i2++) {
                            WaStatus3PhotosFragment.this.fileList.get(i2).selected = true;
                            WaStatus3PhotosFragment.this.filesToDelete.add(WaStatus3PhotosFragment.this.fileList.get(i2));
                        }
                        was3_selectAll.setChecked(true);
                    } else {
                        for (int i3 = 0; i3 < WaStatus3PhotosFragment.this.fileList.size(); i3++) {
                            WaStatus3PhotosFragment.this.fileList.get(i3).selected = false;
                        }
                        was3_actionLay.setVisibility(View.GONE);
                    }
                    WaStatus3PhotosFragment.this.myAdapter.notifyDataSetChanged();
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
            AlertDialog loadingPopup = WhatsAppUtil.loadingPopup(WaStatus3PhotosFragment.this.getActivity());
            this.alertDialog = loadingPopup;
            loadingPopup.show();
        }


        @Override
        public Void doInBackground(Void... voidArr) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < WaStatus3PhotosFragment.this.filesToDelete.size(); i++) {
                StatusModel statusModel = WaStatus3PhotosFragment.this.filesToDelete.get(i);
                File file = new File(statusModel.getFilePath());
                if (file.exists()) {
                    if (file.delete()) {
                        arrayList.add(statusModel);
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
            WaStatus3PhotosFragment.this.filesToDelete.clear();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                WaStatus3PhotosFragment.this.fileList.remove((StatusModel) it.next());
            }
            return null;
        }


        @Override
        public void onPostExecute(Void r5) {
            super.onPostExecute(r5);
            WaStatus3PhotosFragment.this.myAdapter.notifyDataSetChanged();
            int i = this.success;
            if (i == 0) {
                Toast.makeText(WaStatus3PhotosFragment.this.getContext(), WaStatus3PhotosFragment.this.getResources().getString(R.string.delete_error), Toast.LENGTH_SHORT).show();
            } else if (i == 1) {
                Toast.makeText(WaStatus3PhotosFragment.this.getActivity(), WaStatus3PhotosFragment.this.getResources().getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
            }
            was3_actionLay.setVisibility(View.GONE);
            was3_selectAll.setChecked(false);
            this.alertDialog.dismiss();
        }
    }

    public class loadDataAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            was3_loaderLay.setVisibility(View.VISIBLE);
        }


        @Override
        public Void doInBackground(Void... voidArr) {
            WaStatus3PhotosFragment.this.getFromSdcard();
            return null;
        }


        @Override
        public void onPostExecute(Void r4) {
            super.onPostExecute(r4);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    addNativeAds();
                    myAdapter = new WaStatusAdapter(WaStatus3PhotosFragment.this, fileList, WaStatus3PhotosFragment.this);
                    was3_imageGrid.setAdapter(myAdapter);
                    was3_loaderLay.setVisibility(View.GONE);
                    if (WaStatus3PhotosFragment.this.fileList == null || WaStatus3PhotosFragment.this.fileList.size() == 0) {
                        was3_emptyLay.setVisibility(View.VISIBLE);
                    } else {
                        was3_emptyLay.setVisibility(View.GONE);
                    }
                }
            }, 1000L);
        }
    }

    public void getFromSdcard() {
        File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "Download" + File.separator + getResources().getString(R.string.app_name) + "/WA_Images");
        this.fileList = new ArrayList<>();
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            Arrays.sort(listFiles, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            for (File file2 : listFiles) {
                this.fileList.add(new StatusModel(file2.getAbsolutePath()));
            }
        }
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        this.myAdapter.onActivityResult(i, i2, intent);
        int i3 = this.save;
        if (i == i3 && i2 == i3) {
            this.myAdapter.notifyDataSetChanged();
            getFromSdcard();
            WaStatusAdapter waStatusAdapter = new WaStatusAdapter(this, this.fileList, this);
            addNativeAds();
            this.myAdapter = waStatusAdapter;
            was3_imageGrid.setAdapter(waStatusAdapter);
            was3_actionLay.setVisibility(View.GONE);
            was3_selectAll.setChecked(false);
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
            was3_selectAll.setChecked(true);
        }
        if (!this.filesToDelete.isEmpty()) {
            was3_actionLay.setVisibility(View.VISIBLE);
            return;
        }
        was3_selectAll.setChecked(false);
        was3_actionLay.setVisibility(View.GONE);
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

    public void idBinding(View myView) {
        was3_imageGrid = (RecyclerView) myView.findViewById(R.id.was3_videoGrid);
        was3_actionLay = (LinearLayout) myView.findViewById(R.id.was3_actionLay);
        was3_deleteIV = (LinearLayout) myView.findViewById(R.id.was3_deleteIV);
        was3_selectAll = (CheckBox) myView.findViewById(R.id.was3_selectAll);
        was3_loaderLay = (RelativeLayout) myView.findViewById(R.id.loaderLay);
        was3_emptyLay = (RelativeLayout) myView.findViewById(R.id.emptyLay);
    }
}
