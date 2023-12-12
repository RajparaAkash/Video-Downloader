package com.video.fast.free.downloader.all.hd.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.storage.StorageManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.view.PointerIconCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.video.fast.free.downloader.all.hd.R;
import com.video.fast.free.downloader.all.hd.Utils.SharedPrefs;
import com.video.fast.free.downloader.all.hd.Utils.WhatsAppUtil;
import com.video.fast.free.downloader.all.hd.Adpter.RecentAdapter;
import com.video.fast.free.downloader.all.hd.Model.StatusModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WaStatus2WABusinessFragment extends Fragment implements RecentAdapter.OnCheckboxListener {

    SwipeRefreshLayout was2_swipeToRefresh;
    RecyclerView was2_imageGrid;
    LinearLayout was2_actionLay;
    LinearLayout was2_deleteIV;
    LinearLayout was2_downloadIV;
    LinearLayout was2_sAccessBtn;
    CheckBox was2_selectAll;
    RelativeLayout was2_loaderLay;
    RelativeLayout was2_emptyLay;

    loadDataAsync async;
    RecentAdapter myAdapter;
    ArrayList<StatusModel> fileList = new ArrayList<>();
    ArrayList<StatusModel> filesToDelete = new ArrayList<>();
    int REQUEST_ACTION_OPEN_DOCUMENT_TREE = PointerIconCompat.TYPE_CONTEXT_MENU;
    ArrayList<StatusModel> temporary = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_wa_status_2_wa_business, viewGroup, false);

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
        was2_imageGrid.setLayoutManager(gridlayoutManager);

        was2_swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RecentWappBus1();
            }
        });

        was2_deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecentWappBus2(view);
            }
        });

        was2_downloadIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new downloadAll().execute(new Void[0]);
            }
        });

        was2_selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                RecentWappBus3(compoundButton, z);
            }
        });

        was2_sAccessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecentWappBus4(view);
            }
        });

        if (!SharedPrefs.getWBTree(getActivity()).equals("")) {
            populateGrid();
        }
        return inflate;
    }

    public void RecentWappBus1() {
        if (!SharedPrefs.getWBTree(getActivity()).equals("")) {
            Iterator<StatusModel> it = this.filesToDelete.iterator();
            while (it.hasNext()) {
                ArrayList<StatusModel> arrayList = this.fileList;
                it.next().selected = false;
                arrayList.contains(false);
            }
            RecentAdapter myRecentAdapter = this.myAdapter;
            if (myRecentAdapter != null) {
                myRecentAdapter.notifyDataSetChanged();
            }
            this.filesToDelete.clear();
            was2_selectAll.setChecked(false);
            was2_actionLay.setVisibility(View.GONE);
            populateGrid();
        }
        was2_swipeToRefresh.setRefreshing(false);
    }

    public void RecentWappBus2(View view) {
        if (this.filesToDelete.isEmpty()) {
            return;
        }
        new AlertDialog.Builder(getContext()).setMessage(getResources().getString(R.string.delete_alert)).setCancelable(true).setNegativeButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public final void onClick(DialogInterface dialogInterface, int i) {
                new deleteAll().execute(new Void[0]);
            }
        }).setPositiveButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    public void RecentWappBus3(CompoundButton compoundButton, boolean z) {
        if (compoundButton.isPressed()) {
            this.filesToDelete.clear();
            int i = 0;
            while (true) {
                if (i >= this.fileList.size()) {
                    break;
                } else if (!this.fileList.get(i).selected) {
                    z = true;
                    break;
                } else {
                    i++;
                }
            }
            if (z) {
                for (int i2 = 0; i2 < this.fileList.size(); i2++) {
                    this.fileList.get(i2).selected = true;
                    this.filesToDelete.add(this.fileList.get(i2));
                }
                was2_selectAll.setChecked(true);
            } else {
                for (int i3 = 0; i3 < this.fileList.size(); i3++) {
                    this.fileList.get(i3).selected = false;
                }
                was2_actionLay.setVisibility(View.GONE);
            }
            this.myAdapter.notifyDataSetChanged();
        }
    }

    public void RecentWappBus4(View view) {
        Intent intent;
        if (WhatsAppUtil.appInstalledOrNot(getActivity(), "com.whatsapp.w4b")) {
            StorageManager storageManager = (StorageManager) getActivity().getSystemService(Context.STORAGE_SERVICE);
            String whatsupFolder = getWhatsupFolder();
            if (Build.VERSION.SDK_INT >= 29) {
                intent = storageManager.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
                String replace = ((Uri) intent.getParcelableExtra("android.provider.extra.INITIAL_URI")).toString().replace("/root/", "/document/");
                intent.putExtra("android.provider.extra.INITIAL_URI", Uri.parse(replace + "%3A" + whatsupFolder));
            } else {
                intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
                intent.putExtra("android.provider.extra.INITIAL_URI", Uri.parse("content://com.android.externalstorage.documents/document/primary%3A" + whatsupFolder));
            }
            intent.addFlags(2);
            intent.addFlags(1);
            intent.addFlags(128);
            intent.addFlags(64);
            startActivityForResult(intent, this.REQUEST_ACTION_OPEN_DOCUMENT_TREE);
            return;
        }
        Toast.makeText(getActivity(), "Please Install WhatsApp Business For Download Status!!!!!", Toast.LENGTH_SHORT).show();
    }


    class deleteAll extends AsyncTask<Void, Void, Void> {
        AlertDialog alertDialog;
        int success = -1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AlertDialog loadingPopup = WhatsAppUtil.loadingPopup(WaStatus2WABusinessFragment.this.getActivity());
            this.alertDialog = loadingPopup;
            loadingPopup.show();
        }

        
        @Override
        public Void doInBackground(Void... voidArr) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < WaStatus2WABusinessFragment.this.filesToDelete.size(); i++) {
                StatusModel statusModel = WaStatus2WABusinessFragment.this.filesToDelete.get(i);
                DocumentFile fromSingleUri = DocumentFile.fromSingleUri(WaStatus2WABusinessFragment.this.getActivity(), Uri.parse(statusModel.getFilePath()));
                if (fromSingleUri.exists()) {
                    if (fromSingleUri.delete()) {
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
            WaStatus2WABusinessFragment.this.filesToDelete.clear();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                WaStatus2WABusinessFragment.this.fileList.remove((StatusModel) it.next());
            }
            return null;
        }

        
        @Override
        public void onPostExecute(Void r4) {
            super.onPostExecute(r4);
            WaStatus2WABusinessFragment.this.myAdapter.notifyDataSetChanged();
            int i = this.success;
            if (i == 0) {
                Toast.makeText(WaStatus2WABusinessFragment.this.getContext(), WaStatus2WABusinessFragment.this.getResources().getString(R.string.delete_error), Toast.LENGTH_SHORT).show();
            } else if (i == 1) {
                Toast.makeText(WaStatus2WABusinessFragment.this.getActivity(), WaStatus2WABusinessFragment.this.getResources().getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
            }
            was2_actionLay.setVisibility(View.GONE);
            was2_selectAll.setChecked(false);
            alertDialog.dismiss();
        }
    }


    class downloadAll extends AsyncTask<Void, Void, Void> {
        AlertDialog alertDialog;
        int success = -1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AlertDialog loadingPopup = WhatsAppUtil.loadingPopup(WaStatus2WABusinessFragment.this.getActivity());
            this.alertDialog = loadingPopup;
            loadingPopup.show();
        }

        
        @Override
        public Void doInBackground(Void... voidArr) {
            if (WaStatus2WABusinessFragment.this.filesToDelete.isEmpty()) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < WaStatus2WABusinessFragment.this.filesToDelete.size(); i++) {
                StatusModel statusModel = WaStatus2WABusinessFragment.this.filesToDelete.get(i);
                if (DocumentFile.fromSingleUri(WaStatus2WABusinessFragment.this.getActivity(), Uri.parse(statusModel.getFilePath())).exists()) {
                    if (WhatsAppUtil.download(WaStatus2WABusinessFragment.this.getActivity(), statusModel.getFilePath())) {
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
            WaStatus2WABusinessFragment.this.filesToDelete.clear();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ArrayList<StatusModel> arrayList2 = WaStatus2WABusinessFragment.this.fileList;
                ((StatusModel) it.next()).selected = false;
                arrayList2.contains(false);
            }
            return null;
        }

        
        @Override
        public void onPostExecute(Void r5) {
            super.onPostExecute(r5);
            WaStatus2WABusinessFragment.this.myAdapter.notifyDataSetChanged();
            int i = this.success;
            if (i == 0) {
                Toast.makeText(WaStatus2WABusinessFragment.this.getContext(), WaStatus2WABusinessFragment.this.getResources().getString(R.string.save_error), Toast.LENGTH_SHORT).show();
            } else if (i == 1) {
                Toast.makeText(WaStatus2WABusinessFragment.this.getActivity(), WaStatus2WABusinessFragment.this.getResources().getString(R.string.save_success), Toast.LENGTH_SHORT).show();
            }
            was2_actionLay.setVisibility(View.GONE);
            was2_selectAll.setChecked(false);
            alertDialog.dismiss();
        }
    }

    public void populateGrid() {
        loadDataAsync loaddataasync = new loadDataAsync();
        this.async = loaddataasync;
        loaddataasync.execute(new Void[0]);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loadDataAsync loaddataasync = this.async;
        if (loaddataasync != null) {
            loaddataasync.cancel(true);
        }
    }

    public class loadDataAsync extends AsyncTask<Void, Void, Void> {
        DocumentFile[] allFiles;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            was2_loaderLay.setVisibility(View.VISIBLE);
            was2_imageGrid.setVisibility(View.GONE);
            was2_sAccessBtn.setVisibility(View.GONE);
            was2_emptyLay.setVisibility(View.GONE);
        }

        
        @Override
        public Void doInBackground(Void... voidArr) {
            this.allFiles = null;
            WaStatus2WABusinessFragment.this.fileList = new ArrayList<>();
            this.allFiles = WaStatus2WABusinessFragment.this.getFromSdcard();
            int i = 0;
            while (true) {
                DocumentFile[] documentFileArr = this.allFiles;
                if (i >= documentFileArr.length) {
                    return null;
                }
                if (!documentFileArr[i].getUri().toString().contains(".nomedia")) {
                    WaStatus2WABusinessFragment.this.fileList.add(new StatusModel(this.allFiles[i].getUri().toString()));
                }
                i++;
            }
        }

        
        @Override
        public void onPostExecute(Void r4) {
            super.onPostExecute(r4);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadDataAsync.this.onPostExecuteRecentWappBusloadDataAsync();
                }
            }, 1000L);
        }

        public void onPostExecuteRecentWappBusloadDataAsync() {
            if (WaStatus2WABusinessFragment.this.getActivity() != null) {
                if (WaStatus2WABusinessFragment.this.fileList != null && WaStatus2WABusinessFragment.this.fileList.size() != 0) {
                    WaStatus2WABusinessFragment myFragmentWhatsAppBusiness = WaStatus2WABusinessFragment.this;
                    WaStatus2WABusinessFragment myFragmentWhatsAppBusiness2 = WaStatus2WABusinessFragment.this;
                    addNativeAds();
                    myFragmentWhatsAppBusiness.myAdapter = new RecentAdapter(myFragmentWhatsAppBusiness2, myFragmentWhatsAppBusiness2.fileList, WaStatus2WABusinessFragment.this);
                    was2_imageGrid.setAdapter(myAdapter);
                    was2_imageGrid.setVisibility(View.VISIBLE);
                }
                was2_loaderLay.setVisibility(View.GONE);
            }
            if (WaStatus2WABusinessFragment.this.fileList == null || WaStatus2WABusinessFragment.this.fileList.size() == 0) {
                was2_emptyLay.setVisibility(View.VISIBLE);
            } else {
                was2_emptyLay.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        RecentAdapter myRecentAdapter = this.myAdapter;
        if (myRecentAdapter != null) {
            myRecentAdapter.onActivityResult(i, i2, intent);
        }
        if (i == 10 && i2 == 10) {
            this.fileList = new ArrayList<>();
            DocumentFile[] fromSdcard = getFromSdcard();
            for (int i3 = 0; i3 < fromSdcard.length; i3++) {
                if (!fromSdcard[i3].getUri().toString().contains(".nomedia")) {
                    this.fileList.add(new StatusModel(fromSdcard[i3].getUri().toString()));
                }
            }
            RecentAdapter myRecentAdapter2 = new RecentAdapter(this, this.fileList, this);
            addNativeAds();
            this.myAdapter = myRecentAdapter2;
            was2_imageGrid.setAdapter(myRecentAdapter2);
            was2_actionLay.setVisibility(View.GONE);
            was2_selectAll.setChecked(false);
        }
        if (i == this.REQUEST_ACTION_OPEN_DOCUMENT_TREE && i2 == -1) {
            Uri data = intent.getData();
            try {
                if (Build.VERSION.SDK_INT >= 19) {
                    requireContext().getContentResolver().takePersistableUriPermission(data, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            SharedPrefs.setWBTree(getActivity(), data.toString());
            populateGrid();
        }
    }

    
    public DocumentFile[] getFromSdcard() {
        DocumentFile fromTreeUri = DocumentFile.fromTreeUri(requireContext().getApplicationContext(), Uri.parse(SharedPrefs.getWBTree(getActivity())));
        if (fromTreeUri != null && fromTreeUri.exists() && fromTreeUri.isDirectory() && fromTreeUri.canRead() && fromTreeUri.canWrite()) {
            return fromTreeUri.listFiles();
        }
        return null;
    }

    public String getWhatsupFolder() {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory());
        sb.append(File.separator);
        sb.append("Android/media/com.whatsapp.w4b/WhatsApp Business");
        sb.append(File.separator);
        sb.append("Media");
        sb.append(File.separator);
        sb.append(".Statuses");
        return new File(sb.toString()).isDirectory() ? "Android%2Fmedia%2Fcom.whatsapp.w4b%2FWhatsApp Business%2FMedia%2F.Statuses" : "WhatsApp Business%2FMedia%2F.Statuses";
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
            was2_selectAll.setChecked(true);
        }
        if (!this.filesToDelete.isEmpty()) {
            was2_actionLay.setVisibility(View.VISIBLE);
            return;
        }
        was2_selectAll.setChecked(false);
        was2_actionLay.setVisibility(View.GONE);
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
        was2_swipeToRefresh = (SwipeRefreshLayout) myView.findViewById(R.id.was2_swipeToRefresh);
        was2_imageGrid = (RecyclerView) myView.findViewById(R.id.was2_WorkImageGrid);
        was2_actionLay = (LinearLayout) myView.findViewById(R.id.was2_actionLay);
        was2_deleteIV = (LinearLayout) myView.findViewById(R.id.was2_deleteIV);
        was2_downloadIV = (LinearLayout) myView.findViewById(R.id.was2_downloadIV);
        was2_selectAll = (CheckBox) myView.findViewById(R.id.was2_selectAll);
        was2_sAccessBtn = (LinearLayout) myView.findViewById(R.id.was2_sAccessBtn);
        was2_loaderLay = (RelativeLayout) myView.findViewById(R.id.loaderLay);
        was2_emptyLay = (RelativeLayout) myView.findViewById(R.id.emptyLay);
    }
}
