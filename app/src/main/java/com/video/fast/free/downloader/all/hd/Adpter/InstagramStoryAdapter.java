package com.video.fast.free.downloader.all.hd.Adpter;


import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class InstagramStoryAdapter extends RecyclerView.Adapter {

    public Activity context;
    private ArrayList<InstaStoryData> instaStoryDataArrayList;
    public setOnItemClickListener sClickListener;

    public interface setOnItemClickListener {
        void onItemClick(int i);
    }

    public InstagramStoryAdapter(Activity activity, ArrayList<InstaStoryData> arrayList) {
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
                .inflate(R.layout.item_adapter_instagram_story, parent,
                        false);
        return new MyViewHolder(layoutTwo);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);
        InstaStoryData instaStoryData = instaStoryDataArrayList.get(position);

        Glide.with(context)
                .load(instaStoryData.getProfile_pic_url())
                .apply((BaseRequestOptions<?>) ((RequestOptions) ((RequestOptions) ((RequestOptions) new RequestOptions()
                        .transform((Transformation<Bitmap>) new RoundedCorners(10)))
                        .error((int) R.mipmap.ic_launcher_round))
                        .skipMemoryCache(true))
                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(((MyViewHolder) holder).userinstaImage);

        ((MyViewHolder) holder).usrInstaName.setText(instaStoryData.getFull_name());
        ((MyViewHolder) holder).usrFullName.setText(instaStoryData.getUsername());
    }

    @Override
    public int getItemCount() {
        return instaStoryDataArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AppCompatTextView usrFullName;
        private CircleImageView userinstaImage;
        private AppCompatTextView usrInstaName;


        public MyViewHolder(View view) {
            super(view);
            usrFullName = view.findViewById(R.id.usrFullName);
            userinstaImage = view.findViewById(R.id.userinstaImage);
            usrInstaName = view.findViewById(R.id.usrInstaName);

            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            sClickListener.onItemClick(getAdapterPosition());
        }
    }


}
