package com.video.fast.free.downloader.all.hd.Fragment;

import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.HideLoading;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.delete;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.getMimeType;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.mainFolder;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.subFolder;

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


public class GalleryDownloadFragment extends Fragment {

    private RecyclerView gdfm_rvDownload;
    private TextView gdfm_vidNotFound;

    DownloadVideoAdapter videoSaveAdp;
    ArrayList<StatusData> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery_download, container, false);

        idBinding(view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gdfm_rvDownload.setHasFixedSize(true);
        gdfm_rvDownload.setLayoutManager(gridLayoutManager);

        GetCreateVieo();

        return view;
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

                    for (int i = 0; i < arrayList.size(); i++) {
                        if (i % 7 == 6) {
                        }
                    }

                    videoSaveAdp = new DownloadVideoAdapter(getActivity(), arrayList);
                    gdfm_rvDownload.setAdapter(videoSaveAdp);
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
                                    gdfm_rvDownload.smoothScrollToPosition(i);

                                    if (arrayList.size() > 0) {
                                        gdfm_vidNotFound.setVisibility(View.GONE);
                                        gdfm_rvDownload.setVisibility(View.VISIBLE);

                                    } else {
                                        gdfm_vidNotFound.setVisibility(View.VISIBLE);
                                        gdfm_rvDownload.setVisibility(View.GONE);
                                    }
                                    Main2DownloadFragment.deleteData(getActivity(), i);
                                }
                            }
                        });
                    });

                    gdfm_vidNotFound.setVisibility(View.GONE);
                    gdfm_rvDownload.setVisibility(View.VISIBLE);
                } else {
                    gdfm_vidNotFound.setVisibility(View.VISIBLE);
                    gdfm_rvDownload.setVisibility(View.GONE);
                }
            } else {
                gdfm_vidNotFound.setVisibility(View.VISIBLE);
                gdfm_rvDownload.setVisibility(View.GONE);
            }

        } else {
            gdfm_vidNotFound.setVisibility(View.VISIBLE);
            gdfm_rvDownload.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void idBinding(View myView) {
        gdfm_rvDownload = (RecyclerView) myView.findViewById(R.id.gdfm_rvDownload);
        gdfm_vidNotFound = (TextView) myView.findViewById(R.id.gdfm_vidNotFound);
    }
}