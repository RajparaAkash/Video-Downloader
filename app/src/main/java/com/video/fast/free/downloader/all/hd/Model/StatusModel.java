package com.video.fast.free.downloader.all.hd.Model;

import android.os.Parcel;
import android.os.Parcelable;


public class StatusModel implements Parcelable {
    public static final Creator<StatusModel> CREATOR = new Creator<StatusModel>() {

        @Override
        public StatusModel createFromParcel(Parcel parcel) {
            return new StatusModel(parcel);
        }

        
        @Override
        public StatusModel[] newArray(int i) {
            return new StatusModel[i];
        }
    };
    private String filepath;
    public boolean selected = false;
//    private int listPosition = -1;
//
//    public int getListPosition() {
//        return listPosition;
//    }
//
//    public void setListPosition(int listPositon) {
//        this.listPosition = listPositon;
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean z) {
        this.selected = z;
    }

    public StatusModel(String str) {
        this.filepath = str;
    }

    protected StatusModel(Parcel parcel) {
        this.filepath = parcel.readString();
    }

    public String getFilePath() {
        return this.filepath;
    }

    public void setFilePath(String str) {
        this.filepath = str;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.filepath);
    }
}
