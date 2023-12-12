package com.video.fast.free.downloader.all.hd.Adpter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class FaceBookStoryAdapter extends RecyclerView.Adapter {

    public Activity context;
    private ArrayList<FbStoryData> fbStoryDataArrayList;
    public setOnItemClickListener sClickListener;

    public interface setOnItemClickListener {
        void onItemClick(int i);
    }

    public FaceBookStoryAdapter(Activity activity, ArrayList<FbStoryData> arrayList) {
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
                .inflate(R.layout.item_adapter_facebook_story, parent,
                        false);
        return new MyViewHolder(layoutTwo);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

        FbStoryData fbStoryData = fbStoryDataArrayList.get(position);

        Glide.with(context)
                .load(fbStoryData.getuThumb())
                .apply((BaseRequestOptions<?>) ((RequestOptions) ((RequestOptions) ((RequestOptions) new RequestOptions()
                        .transform((Transformation<Bitmap>) new RoundedCorners(10)))
                        .error((int) R.mipmap.ic_launcher_round))
                        .skipMemoryCache(true))
                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(((MyViewHolder) holder).userImage);

        Glide.with(context)
                .load(fbStoryData.getuThumb())
                .apply((BaseRequestOptions<?>) ((RequestOptions) ((RequestOptions) ((RequestOptions) new RequestOptions()
                        .transform((Transformation<Bitmap>) new RoundedCorners(10)))
                        .error((int) R.mipmap.ic_launcher_round))
                        .skipMemoryCache(true))
                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(((MyViewHolder) holder).userThumb);

        ((MyViewHolder) holder).usName.setText(fbStoryData.getuName());
        ((MyViewHolder) holder).usrStryCount.setText(fbStoryData.getuScount());
        ((MyViewHolder) holder).usName.setSelected(true);
        ((MyViewHolder) holder).usName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        ((MyViewHolder) holder).usName.setSingleLine(true);
        ((MyViewHolder) holder).usName.setMarqueeRepeatLimit(-1);
        ((MyViewHolder) holder).usName.setLines(1);
        ((MyViewHolder) holder).usName.setHorizontallyScrolling(true);
    }

    @Override
    public int getItemCount() {
        return fbStoryDataArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public AppCompatImageView userImage;
        private AppCompatTextView usrStryCount;
        private CircleImageView userThumb;
        private AppCompatTextView usName;


        public MyViewHolder(View view) {
            super(view);
            userImage = (AppCompatImageView) view.findViewById(R.id.userImage);
            usrStryCount = view.findViewById(R.id.usrStryCount);
            userThumb = view.findViewById(R.id.userThumb);
            usName = view.findViewById(R.id.usName);

            userImage.setOnClickListener(this);
        }

        public void onClick(View view) {
            sClickListener.onItemClick(getAdapterPosition());
        }
    }


}
