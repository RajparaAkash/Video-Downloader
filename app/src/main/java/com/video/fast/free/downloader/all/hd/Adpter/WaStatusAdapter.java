package com.video.fast.free.downloader.all.hd.Adpter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sdk.mynew.Interstitial_Ads;
import com.sdk.mynew.Native_Ads_Preload_1;
import com.video.fast.free.downloader.all.hd.Activity.WaStatusPreviewActivity;
import com.video.fast.free.downloader.all.hd.Model.StatusModel;
import com.video.fast.free.downloader.all.hd.R;

import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class WaStatusAdapter extends RecyclerView.Adapter<WaStatusAdapter.ItemViewHeader> {

    List<StatusModel> arrayList;
    Fragment context;
    LayoutInflater inflater;
    public OnCheckboxListener onCheckboxListener;
    int width;

    public interface OnCheckboxListener {
        void onCheckboxListener(View view, List<StatusModel> list);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public WaStatusAdapter(Fragment fragment, List<StatusModel> list, OnCheckboxListener onCheckboxListener) {
        this.context = fragment;
        this.arrayList = list;
        this.inflater = (LayoutInflater) fragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.width = fragment.getResources().getDisplayMetrics().widthPixels;
        this.onCheckboxListener = onCheckboxListener;
    }

    @NonNull
    @Override
    public ItemViewHeader onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHeader(LayoutInflater.from(context.getActivity()).inflate(R.layout.item_adapter_view_my_status, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHeader holder, int i) {
        int viewType = getItemViewType(i);
        holder.mainLayout.setVisibility(View.GONE);
        holder.native_big.setVisibility(View.GONE);
        if (viewType == 0) {
            holder.mainLayout.setVisibility(View.VISIBLE);
            if (isVideoFile(this.arrayList.get(i).getFilePath())) {
                holder.play.setVisibility(View.VISIBLE);
            } else {
                holder.play.setVisibility(View.GONE);
            }
            Glide.with(this.context.getActivity()).load(this.arrayList.get(i).getFilePath()).into(holder.gridImageVideo);

            holder.gridImageVideo.setOnClickListener(view2 -> {
                new Interstitial_Ads().Show_Ads(context.getActivity(), () -> {
                    Intent intent = new Intent(context.getActivity(), WaStatusPreviewActivity.class);
                    intent.putExtra("filepath", arrayList.get(i).getFilePath());
                    intent.putExtra("statusdownload", "download");
                    context.startActivity(intent);
                });
            });
        } else {
            holder.native_big.setVisibility(View.VISIBLE);

            // native_big
            Native_Ads_Preload_1.getInstance(context.getActivity()).addNativeAd(holder.native_big, true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position) != null)
            return 0;
        else return 1;
    }

    @Override
    public int getItemCount() {
        return this.arrayList.size();
    }

    public static class ItemViewHeader extends RecyclerView.ViewHolder {
        ImageView gridImageVideo;
        ImageView play;
        RelativeLayout mainLayout;
        FrameLayout native_big;

        public ItemViewHeader(View inflate) {
            super(inflate);
            gridImageVideo = inflate.findViewById(R.id.gridImageVideo);
            play = inflate.findViewById(R.id.play);
            mainLayout = inflate.findViewById(R.id.mainLayout);
            native_big = inflate.findViewById(R.id.native_big);
        }
    }

    public boolean isVideoFile(String str) {
        String guessContentTypeFromName = URLConnection.guessContentTypeFromName(str);
        return guessContentTypeFromName != null && guessContentTypeFromName.startsWith("video");
    }

    public void onActivityResult(int i, int i2, Intent intent) {

    }
}
