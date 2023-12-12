package com.video.fast.free.downloader.all.hd.Adpter;

import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.HideLoading;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.ShowHistory;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.date2DayTime;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.video.fast.free.downloader.all.hd.R;
import com.video.fast.free.downloader.all.hd.Model.HistoryData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter {

    ArrayList<HistoryData> historyDataArrayList = new ArrayList<HistoryData>();
    public static final int LAYOUT_TITLE = 1;
    public static final int LAYOUT_MAIN = 2;
    Activity mActivity;

    public HistoryAdapter(Activity context, ArrayList<HistoryData> itemList) {
        this.mActivity = context;
        this.historyDataArrayList = itemList;
    }

    @Override
    public int getItemViewType(int position) {
        if (historyDataArrayList.get(position).isNxtDay()) {
            return LAYOUT_TITLE;
        } else {
            return LAYOUT_MAIN;
        }
    }

    class LayoutTitleViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView txtTitleDate;

        public LayoutTitleViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitleDate = itemView.findViewById(R.id.txtTitleDate);
        }

        private void setView(String text) {
            txtTitleDate.setText(text);
        }
    }

    class LayoutMainViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView txtTitle, txtLink;
        private AppCompatImageView imgLink;

        public LayoutMainViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            imgLink = itemView.findViewById(R.id.imgLink);
            txtLink = itemView.findViewById(R.id.txtLink);
        }

        private void setViews(String textTwo) {
            txtLink.setText(textTwo);
        }
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case LAYOUT_TITLE:
                View layoutOne
                        = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_adapter_history_first, parent,
                                false);
                return new LayoutTitleViewHolder(layoutOne);
            case LAYOUT_MAIN:
                View layoutTwo
                        = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_adapter_history_second, parent,
                                false);
                return new LayoutMainViewHolder(layoutTwo);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

        HistoryData data = historyDataArrayList.get(position);
        int viewType = getItemViewType(position);
        switch (viewType) {
            case LAYOUT_TITLE:
                String DTformate = date2DayTime(data.getmDate()) + " - " + data.getmDate();
                ((LayoutTitleViewHolder) holder).setView(DTformate);
                break;
            case LAYOUT_MAIN:
                String mUrl = data.getmLink();
                ((LayoutMainViewHolder) holder).setViews(mUrl);
                ((LayoutMainViewHolder) holder).itemView.setOnClickListener(v -> {
                    Dialog dialog = ShowHistory(mActivity, ((LayoutMainViewHolder) holder).txtTitle.getText().toString(), mUrl, mActivity.getResources().getString(R.string.str_close));
                    AppCompatButton btnCopy = dialog.findViewById(R.id.btnCopy);
                    btnCopy.setOnClickListener(v1 -> {
                        HideLoading(dialog);
                        ClipboardManager clipMan = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Copied String", ((LayoutMainViewHolder) holder).txtLink.getText().toString());
                        clipMan.setPrimaryClip(clip);
                        Toast.makeText(mActivity, "Text Copied", Toast.LENGTH_SHORT).show();
                    });
                });

                if (mUrl.contains("instagram.com")) {
                    ((LayoutMainViewHolder) holder).txtTitle.setText("www.instagram.com");
                    ((LayoutMainViewHolder) holder).imgLink.setImageResource(R.drawable.instagram_image);
                } else if (mUrl.contains("facebook.com")) {
                    ((LayoutMainViewHolder) holder).txtTitle.setText("www.facebook.com");
                    ((LayoutMainViewHolder) holder).imgLink.setImageResource(R.drawable.facebook_image_);
                } else if (mUrl.contains("fb.watch")) {
                    ((LayoutMainViewHolder) holder).txtTitle.setText("fb.watch");
                    ((LayoutMainViewHolder) holder).imgLink.setImageResource(R.drawable.facebook_image_);
                } else if (mUrl.contains("vimeo.com")) {
                    ((LayoutMainViewHolder) holder).txtTitle.setText("vimeo.com");
                    ((LayoutMainViewHolder) holder).imgLink.setImageResource(R.drawable.vimeo_image);
                } else if (mUrl.contains("pinterest.com")) {
                    ((LayoutMainViewHolder) holder).txtTitle.setText("www.pinterest.com");
                    ((LayoutMainViewHolder) holder).imgLink.setImageResource(R.drawable.pinterest_image);
                } else if (mUrl.contains("likee")) {
                    ((LayoutMainViewHolder) holder).txtTitle.setText("likee.video");
                    ((LayoutMainViewHolder) holder).imgLink.setImageResource(R.drawable.likee_image);
                } else if (mUrl.contains("ted.com")) {
                    ((LayoutMainViewHolder) holder).txtTitle.setText("www.ted.com");
                    ((LayoutMainViewHolder) holder).imgLink.setImageResource(R.drawable.ted_image);
                } else if (mUrl.contains("sharechat.com")) {
                    ((LayoutMainViewHolder) holder).txtTitle.setText("sharechat.com");
                    ((LayoutMainViewHolder) holder).imgLink.setImageResource(R.drawable.sharechat_image);
                } else if (mUrl.contains("twitter.com")) {
                    ((LayoutMainViewHolder) holder).txtTitle.setText("twitter.com");
                    ((LayoutMainViewHolder) holder).imgLink.setImageResource(R.drawable.twitter_image);
                } else if (mUrl.contains("roposo.com")) {
                    ((LayoutMainViewHolder) holder).txtTitle.setText("www.roposo.com");
                    ((LayoutMainViewHolder) holder).imgLink.setImageResource(R.drawable.roposo_image);
                } else if (mUrl.contains("linkedin")) {
                    ((LayoutMainViewHolder) holder).txtTitle.setText("www.linkedin.com");
                    ((LayoutMainViewHolder) holder).imgLink.setImageResource(R.drawable.linkdin_image);
                } else {
                    ((LayoutMainViewHolder) holder).txtTitle.setText("------");
                    ((LayoutMainViewHolder) holder).imgLink.setImageResource(R.mipmap.ic_launcher_round);
                }

                break;
        }
    }


    @Override
    public int getItemCount() {
        return historyDataArrayList.size();
    }
}
