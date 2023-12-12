package com.video.fast.free.downloader.all.hd.Adpter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.video.fast.free.downloader.all.hd.R;
import com.video.fast.free.downloader.all.hd.Model.FbStoryData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FbUserStoryAdapter extends RecyclerView.Adapter {

    public Activity context;
    private ArrayList<FbStoryData> fbStoryDataArrayList;
    public setOnItemClickListener sClickListener;

    public interface setOnItemClickListener {
        void onItemClick(int i);
    }

    public FbUserStoryAdapter(Activity activity, ArrayList<FbStoryData> arrayList) {
        this.context = activity;
        this.fbStoryDataArrayList = arrayList;
    }

    public void setOnItemClickListener(setOnItemClickListener setonitemclicklistener) {
        this.sClickListener = setonitemclicklistener;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View layoutTwo
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_adapter_fb_user_story, parent,
                        false);
        return new MyViewHolder(layoutTwo);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

        FbStoryData fbStoryData = fbStoryDataArrayList.get(position);

        Glide.with(context)
                .load(fbStoryData.getUserImage())
                .apply((BaseRequestOptions<?>) ((RequestOptions) ((RequestOptions) ((RequestOptions) new RequestOptions()
                        .transform((Transformation<Bitmap>) new RoundedCorners(10)))
                        .error((int) R.mipmap.ic_launcher_round))
                        .skipMemoryCache(true))
                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(((MyViewHolder) holder).userImage);

        if (fbStoryData.isVideo()) {
            ((MyViewHolder) holder).userVid.setVisibility(View.VISIBLE);
        } else {
            ((MyViewHolder) holder).userVid.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return fbStoryDataArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public AppCompatImageView userImage;
        private AppCompatImageView userVid;


        public MyViewHolder(View view) {
            super(view);
            userImage = (AppCompatImageView) view.findViewById(R.id.userImage);
            userVid = view.findViewById(R.id.userVid);

            userImage.setOnClickListener(this);
        }

        public void onClick(View view) {
            sClickListener.onItemClick(getAdapterPosition());
        }
    }


}
