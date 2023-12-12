package com.video.fast.free.downloader.all.hd.Model;

public class FbStoryData {

    public String uKeyID;
    public String uName;
    public String uThumb;
    public String uScount;

    public String userImage;
    public String userVideo;
    public boolean isVideo;
    public long StoryAddTime;

    public String getuKeyID() {
        return uKeyID;
    }

    public void setuKeyID(String uKeyID) {
        this.uKeyID = uKeyID;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuThumb() {
        return uThumb;
    }

    public void setuThumb(String uThumb) {
        this.uThumb = uThumb;
    }

    public String getuScount() {
        return uScount;
    }

    public void setuScount(String uScount) {
        this.uScount = uScount;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserVideo() {
        return userVideo;
    }

    public void setUserVideo(String userVideo) {
        this.userVideo = userVideo;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public long getStoryAddTime() {
        return StoryAddTime;
    }

    public void setStoryAddTime(long storyAddTime) {
        StoryAddTime = storyAddTime;
    }
}
