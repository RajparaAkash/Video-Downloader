package com.video.fast.free.downloader.all.hd.Adpter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.video.fast.free.downloader.all.hd.R;
import com.video.fast.free.downloader.all.hd.Utils.WhatsAppUtil;
import com.video.fast.free.downloader.all.hd.Activity.VideoPlayerWaActivity;
import com.video.fast.free.downloader.all.hd.Model.StatusModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class WaStatusPreviewAdapter extends PagerAdapter {
    Activity activity;
    ArrayList<StatusModel> imageList;

    public WaStatusPreviewAdapter(Activity activity, ArrayList<StatusModel> arrayList) {
        this.activity = activity;
        this.imageList = arrayList;
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, final int i) {
        View inflate = LayoutInflater.from(this.activity).inflate(R.layout.item_adapter_wa_status_preview, viewGroup, false);

        ImageView imageView = (ImageView) inflate.findViewById(R.id.imageView);
        ImageView imageView2 = (ImageView) inflate.findViewById(R.id.iconplayer);
        if (!WhatsAppUtil.getBack(this.imageList.get(i).getFilePath(), "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty()) {
            imageView2.setVisibility(View.VISIBLE);
        } else {
            imageView2.setVisibility(View.GONE);
        }
        Glide.with(this.activity).load(this.imageList.get(i).getFilePath()).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WhatsAppUtil.getBack(imageList.get(i).getFilePath(), "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty()) {
                    return;
                }
                WhatsAppUtil.mPath = imageList.get(i).getFilePath();
                activity.startActivity(new Intent(activity, VideoPlayerWaActivity.class));
            }
        });
        viewGroup.addView(inflate);
        return inflate;
    }

    @Override
    public int getCount() {
        return this.imageList.size();
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((RelativeLayout) obj);
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == ((RelativeLayout) obj);
    }
}
