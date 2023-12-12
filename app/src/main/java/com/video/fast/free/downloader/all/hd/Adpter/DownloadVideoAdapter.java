package com.video.fast.free.downloader.all.hd.Adpter;

import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.isImageFile;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.isVideoFile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.sdk.mynew.Interstitial_Ads;
import com.video.fast.free.downloader.all.hd.R;
import com.video.fast.free.downloader.all.hd.Model.StatusData;
import com.video.fast.free.downloader.all.hd.Activity.ImageShowActivity;
import com.video.fast.free.downloader.all.hd.Activity.VideoPlayerActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class DownloadVideoAdapter extends RecyclerView.Adapter {

    public Activity context;
    private ArrayList<StatusData> statusDataArrayList;
    public setOnItemClickListener sClickListener;

    public interface setOnItemClickListener {
        void onItemClick(int i);
    }

    public DownloadVideoAdapter(Activity activity, ArrayList<StatusData> arrayList) {
        this.context = activity;
        this.statusDataArrayList = arrayList;
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
                .inflate(R.layout.item_adapter_main_download, parent,
                        false);
        return new MyViewHolder(layoutTwo);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

        StatusData statusData = statusDataArrayList.get(position);

        Glide.with(context)
                .load(statusData.getUri())
                .apply((BaseRequestOptions<?>) ((RequestOptions) ((RequestOptions) ((RequestOptions) new RequestOptions()
                        .transform((Transformation<Bitmap>) new RoundedCorners(10)))
                        .error((int) R.mipmap.ic_launcher_round))
                        .skipMemoryCache(true))
                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(((MyViewHolder) holder).vidThumb);

        if (isVideoFile(statusData.getFilepath())) {
            ((MyViewHolder) holder).imgVidbtn.setVisibility(View.VISIBLE);
        } else {
            ((MyViewHolder) holder).imgVidbtn.setVisibility(View.INVISIBLE);
        }

        ((MyViewHolder) holder).vidThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isImageFile(statusData.getFilepath())) {
                    new Interstitial_Ads().Show_Ads(context, new Interstitial_Ads.AdCloseListener() {
                        @Override
                        public void onAdClosed() {
                            Intent intent = new Intent(context, ImageShowActivity.class);
                            intent.putExtra("mImagePath", statusData.getUri().toString());
                            context.startActivity(intent);
                        }
                    });
                    return;
                }
                if (isVideoFile(statusData.getFilepath())) {
                    new Interstitial_Ads().Show_Ads(context, new Interstitial_Ads.AdCloseListener() {
                        @Override
                        public void onAdClosed() {
                            Intent intent = new Intent(context, VideoPlayerActivity.class);
                            intent.putExtra("mVideoPath", statusData.getFilepath());
                            context.startActivity(intent);
                        }
                    });
                    return;
                }
                Toast.makeText(context, "File Not Supported", Toast.LENGTH_LONG).show();
            }
        });

        ((MyViewHolder) holder).postShare.setOnClickListener(v -> {

            Uri fromFile = FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".provider", //(use your app signature + ".provider" )
                    new File(statusData.getFilepath()));
            StringBuilder sb = new StringBuilder();
            sb.append(context.getResources().getString(R.string.str_download) + " ");
            sb.append(context.getString(R.string.app_name));
            sb.append(" : ");
            sb.append(Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName()));
            String sb2 = sb.toString();
            if (isImageFile(statusData.getFilepath())) {
                Intent videoshare = new Intent(Intent.ACTION_SEND);
                videoshare.setType("image/*");
                videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                videoshare.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                videoshare.putExtra(Intent.EXTRA_TEXT, sb2);
                videoshare.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
                videoshare.putExtra(Intent.EXTRA_STREAM, fromFile);
                context.startActivity(Intent.createChooser(videoshare, "Share Image"));
                return;
            }
            if (isVideoFile(statusData.getFilepath())) {
                Intent videoshare = new Intent(Intent.ACTION_SEND);
                videoshare.setType("video/*");
                videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                videoshare.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                videoshare.putExtra(Intent.EXTRA_TEXT, sb2);
                videoshare.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
                videoshare.putExtra(Intent.EXTRA_STREAM, fromFile);
                context.startActivity(Intent.createChooser(videoshare, "Share Video"));
                return;
            }
            Toast.makeText(context, "File Not Supported", Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public int getItemCount() {
        return statusDataArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public AppCompatImageView vidThumb;
        public AppCompatTextView postShare;
        public AppCompatTextView postDelete;
        public AppCompatImageView imgVidbtn;

        public MyViewHolder(View view) {
            super(view);
            postDelete = view.findViewById(R.id.postDelete);
            vidThumb = (AppCompatImageView) view.findViewById(R.id.vidThumb);
            postShare = view.findViewById(R.id.postShare);
            imgVidbtn = (AppCompatImageView) view.findViewById(R.id.imgVidbtn);
            postDelete.setOnClickListener(this);
        }

        public void onClick(View view) {
            sClickListener.onItemClick(getAdapterPosition());
        }
    }
}
