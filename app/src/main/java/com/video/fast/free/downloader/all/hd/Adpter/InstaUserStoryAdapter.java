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
import com.video.fast.free.downloader.all.hd.Model.InstaStoryData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class InstaUserStoryAdapter extends RecyclerView.Adapter {

    public Activity context;
    private ArrayList<InstaStoryData> instaStoryDataArrayList;

    public setOnItemClickListener sClickListener;

    public interface setOnItemClickListener {
        void onItemClick(int i);
    }

    public InstaUserStoryAdapter(Activity activity, ArrayList<InstaStoryData> arrayList) {
        this.context = activity;
        this.instaStoryDataArrayList = arrayList;
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
                .inflate(R.layout.item_adapter_insta_user_story, parent,
                        false);
        return new MyViewHolder(layoutTwo);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

        InstaStoryData instaStoryData = instaStoryDataArrayList.get(position);


        Glide.with(context)
                .load(instaStoryData.getImageVersionUrl())
                .apply((BaseRequestOptions<?>) ((RequestOptions) ((RequestOptions) ((RequestOptions) new RequestOptions()
                        .transform((Transformation<Bitmap>) new RoundedCorners(10)))
                        .error((int) R.mipmap.ic_launcher_round))
                        .skipMemoryCache(true))
                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(((MyViewHolder) holder).userImage);

        if (instaStoryData.getMedia_type() == 2) {
            ((MyViewHolder) holder).userVid.setVisibility(View.VISIBLE);
        } else {
            ((MyViewHolder) holder).userVid.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return instaStoryDataArrayList.size();
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
