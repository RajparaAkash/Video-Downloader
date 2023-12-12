package com.video.fast.free.downloader.all.hd.Adpter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sdk.mynew.Interstitial_Ads;
import com.sdk.mynew.Native_Ads_Preload_1;
import com.video.fast.free.downloader.all.hd.Activity.WaStatusPreviewActivity;
import com.video.fast.free.downloader.all.hd.Model.StatusModel;
import com.video.fast.free.downloader.all.hd.R;
import com.video.fast.free.downloader.all.hd.Utils.WhatsAppUtil;

import java.util.ArrayList;
import java.util.List;


public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.ItemViewHeader> {

    List<StatusModel> arrayList;
    Fragment context;
    LayoutInflater inflater;
    public OnCheckboxListener onCheckboxListener;
    int width;

    public RecentAdapter(Fragment fragment, List<StatusModel> list, OnCheckboxListener onCheckboxListener) {
        this.context = fragment;
        this.arrayList = list;
        this.inflater = (LayoutInflater) fragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.width = fragment.getResources().getDisplayMetrics().widthPixels;
        this.onCheckboxListener = onCheckboxListener;
    }

    @NonNull
    @Override
    public ItemViewHeader onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHeader(LayoutInflater.from(context.getActivity()).inflate(R.layout.item_adapter_view_recent, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHeader holder, int position) {
        int viewType = getItemViewType(position);
        holder.mainLayout.setVisibility(View.GONE);
        holder.native_big.setVisibility(View.GONE);
        if (viewType == 0) {
            holder.mainLayout.setVisibility(View.VISIBLE);
            if (!WhatsAppUtil.getBack(this.arrayList.get(position).getFilePath(), "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty()) {
                holder.play.setVisibility(View.VISIBLE);
            } else {
                holder.play.setVisibility(View.GONE);
            }
            Glide.with(this.context).load(this.arrayList.get(position).getFilePath()).into(holder.gridImage);

            holder.download_txt.setOnClickListener(v -> {
                WhatsAppUtil.download(context.getActivity(), arrayList.get(position).getFilePath());
            });

            holder.gridImage.setOnClickListener(view2 -> {
                new Interstitial_Ads().Show_Ads(context.getActivity(), () -> {
                    Intent intent = new Intent(context.getActivity(), WaStatusPreviewActivity.class);
                    intent.putExtra("filepath", arrayList.get(position).getFilePath());
                    intent.putExtra("statusdownload", "");
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

    public interface OnCheckboxListener {
        void onCheckboxListener(View view, List<StatusModel> list);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void onActivityResult(int i, int i2, Intent intent) {

    }

    public static class ItemViewHeader extends RecyclerView.ViewHolder {
        ImageView gridImage;
        ImageView play;
        TextView download_txt;
        RelativeLayout mainLayout;
        FrameLayout native_big;

        public ItemViewHeader(View inflate) {
            super(inflate);
            gridImage = inflate.findViewById(R.id.gridImage);
            play = inflate.findViewById(R.id.play);
            download_txt = inflate.findViewById(R.id.download_txt);
            mainLayout = inflate.findViewById(R.id.mainLayout);
            native_big = inflate.findViewById(R.id.native_big);
        }
    }
}
