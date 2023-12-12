package com.video.fast.free.downloader.all.hd.Model;

public class InstaStoryData {

    private String username;
    private String full_name;
    private String profile_pic_url;
    private long usrPk;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getProfile_pic_url() {
        return profile_pic_url;
    }

    public void setProfile_pic_url(String profile_pic_url) {
        this.profile_pic_url = profile_pic_url;
    }

    public long getUsrPk() {
        return usrPk;
    }

    public void setUsrPk(long usrPk) {
        this.usrPk = usrPk;
    }

    private int media_type;
    private String ImageVersionUrl;
    private String VideoVersionUrl;

    public int getMedia_type() {
        return media_type;
    }

    public void setMedia_type(int media_type) {
        this.media_type = media_type;
    }

    public String getImageVersionUrl() {
        return ImageVersionUrl;
    }

    public void setImageVersionUrl(String imageVersionUrl) {
        ImageVersionUrl = imageVersionUrl;
    }

    public String getVideoVersionUrl() {
        return VideoVersionUrl;
    }

    public void setVideoVersionUrl(String videoVersionUrl) {
        VideoVersionUrl = videoVersionUrl;
    }
}
