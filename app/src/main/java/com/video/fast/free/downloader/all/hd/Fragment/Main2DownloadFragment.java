package com.video.fast.free.downloader.all.hd.Fragment;

import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.HideLoading;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.delete;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.getMimeType;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.mainFolder;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.subFolder;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.video.fast.free.downloader.all.hd.Adpter.DownloadVideoAdapter;
import com.video.fast.free.downloader.all.hd.Model.StatusData;
import com.video.fast.free.downloader.all.hd.R;
import com.video.fast.free.downloader.all.hd.Utils.MyMethod;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;


public class Main2DownloadFragment extends Fragment {

    public static DownloadVideoAdapter videoSaveAdp;
    public static ArrayList<StatusData> arrayList;

    public static RecyclerView downfg_rvDownload;
    public static TextView downfg_vidNotFound;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_2_download, container, false);

        idBinding(view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        downfg_rvDownload.setHasFixedSize(true);
        downfg_rvDownload.setLayoutManager(gridLayoutManager);

        GetCreateVieo();

        return view;
    }

    public static void setData(Activity activity, File absolutePath) {
        if (arrayList != null) {

            if (arrayList.size() > 0) {
                StatusData statusData = new StatusData();
                statusData.setFilename(absolutePath.getName());
                statusData.setFilepath(absolutePath.getAbsolutePath());
                statusData.setUri(Uri.fromFile(absolutePath));
                arrayList.add(0, statusData);

                videoSaveAdp.notifyDataSetChanged();
                return;
            }

            arrayList = new ArrayList<StatusData>();
            StatusData statusData = new StatusData();
            statusData.setFilename(absolutePath.getName());
            statusData.setFilepath(absolutePath.getAbsolutePath());
            statusData.setUri(Uri.fromFile(absolutePath));
            arrayList.add(statusData);

            videoSaveAdp = new DownloadVideoAdapter(activity, arrayList);
            downfg_rvDownload.setAdapter(videoSaveAdp);
            videoSaveAdp.setOnItemClickListener(i -> {
                Dialog dialog = MyMethod.ShowDeleteFile(activity);
                AppCompatButton btnYes = dialog.findViewById(R.id.btnYes);
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HideLoading(dialog);

                        File file = new File(arrayList.get(i).getFilepath());
                        if (delete(activity, file)) {
                            arrayList.remove(i);
                            videoSaveAdp.notifyItemRemoved(i);
                            downfg_rvDownload.smoothScrollToPosition(i);
                            if (arrayList.size() > 0) {
                                downfg_vidNotFound.setVisibility(View.GONE);
                                downfg_rvDownload.setVisibility(View.VISIBLE);
                                return;
                            }
                            downfg_vidNotFound.setVisibility(View.VISIBLE);
                            downfg_rvDownload.setVisibility(View.GONE);
                        }

                    }
                });
            });


            downfg_vidNotFound.setVisibility(View.GONE);
            downfg_rvDownload.setVisibility(View.VISIBLE);

        } else {
            arrayList = new ArrayList<StatusData>();
            StatusData statusData = new StatusData();
            statusData.setFilename(absolutePath.getName());
            statusData.setFilepath(absolutePath.getAbsolutePath());
            statusData.setUri(Uri.fromFile(absolutePath));
            arrayList.add(statusData);


            videoSaveAdp = new DownloadVideoAdapter(activity, arrayList);
            downfg_rvDownload.setAdapter(videoSaveAdp);
            videoSaveAdp.setOnItemClickListener(i -> {
                Dialog dialog = MyMethod.ShowDeleteFile(activity);
                AppCompatButton btnYes = dialog.findViewById(R.id.btnYes);
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HideLoading(dialog);

                        File file = new File(arrayList.get(i).getFilepath());
                        if (delete(activity, file)) {
                            arrayList.remove(i);
                            videoSaveAdp.notifyItemRemoved(i);
                            downfg_rvDownload.smoothScrollToPosition(i);
                            if (arrayList.size() > 0) {
                                downfg_vidNotFound.setVisibility(View.GONE);
                                downfg_rvDownload.setVisibility(View.VISIBLE);
                                return;
                            }
                            downfg_vidNotFound.setVisibility(View.VISIBLE);
                            downfg_rvDownload.setVisibility(View.GONE);
                        }

                    }
                });
            });


            downfg_vidNotFound.setVisibility(View.GONE);
            downfg_rvDownload.setVisibility(View.VISIBLE);
        }


    }

    public static void deleteData(Activity activity, int pos) {

        if (arrayList != null) {
            arrayList.remove(pos);
            videoSaveAdp.notifyItemRemoved(pos);
            downfg_rvDownload.smoothScrollToPosition(pos);
            if (arrayList.size() > 0) {
                downfg_vidNotFound.setVisibility(View.GONE);
                downfg_rvDownload.setVisibility(View.VISIBLE);

            } else {
                downfg_vidNotFound.setVisibility(View.VISIBLE);
                downfg_rvDownload.setVisibility(View.GONE);
            }
        }


    }

    private void GetCreateVieo() {

        if (mainFolder.exists()) {
            if (subFolder.exists()) {
                File[] listFiles = subFolder.listFiles();
                if (listFiles.length > 0) {
                    Arrays.sort(listFiles, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
                    arrayList = new ArrayList<StatusData>();
                    for (File absolutePath : listFiles) {

                        String mimetype = getMimeType(absolutePath.getAbsolutePath());
                        if (mimetype != null && !TextUtils.isEmpty(mimetype)) {
                            StatusData statusData = new StatusData();
                            statusData.setFilename(absolutePath.getName());
                            statusData.setFilepath(absolutePath.getAbsolutePath());
                            statusData.setUri(Uri.fromFile(absolutePath));
                            arrayList.add(statusData);
                        }

                    }

                    videoSaveAdp = new DownloadVideoAdapter(getActivity(), arrayList);
                    downfg_rvDownload.setAdapter(videoSaveAdp);
                    videoSaveAdp.setOnItemClickListener(i -> {
                        Dialog dialog = MyMethod.ShowDeleteFile(getActivity());
                        AppCompatButton btnYes = dialog.findViewById(R.id.btnYes);
                        btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                HideLoading(dialog);

                                File file = new File(arrayList.get(i).getFilepath());
                                if (delete(getActivity(), file)) {
                                    arrayList.remove(i);
                                    videoSaveAdp.notifyItemRemoved(i);
                                    downfg_rvDownload.smoothScrollToPosition(i);
                                    if (arrayList.size() > 0) {
                                        downfg_vidNotFound.setVisibility(View.GONE);
                                        downfg_rvDownload.setVisibility(View.VISIBLE);
                                        return;
                                    }
                                    downfg_vidNotFound.setVisibility(View.VISIBLE);
                                    downfg_rvDownload.setVisibility(View.GONE);
                                }

                            }
                        });
                    });

                    downfg_vidNotFound.setVisibility(View.GONE);
                    downfg_rvDownload.setVisibility(View.VISIBLE);
                } else {
                    downfg_vidNotFound.setVisibility(View.VISIBLE);
                    downfg_rvDownload.setVisibility(View.GONE);
                }
            } else {
                downfg_vidNotFound.setVisibility(View.VISIBLE);
                downfg_rvDownload.setVisibility(View.GONE);
            }

        } else {
            downfg_vidNotFound.setVisibility(View.VISIBLE);
            downfg_rvDownload.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void idBinding(View myView) {
        downfg_rvDownload = (RecyclerView) myView.findViewById(R.id.downfg_rvDownload);
        downfg_vidNotFound = (TextView) myView.findViewById(R.id.downfg_vidNotFound);
    }
}