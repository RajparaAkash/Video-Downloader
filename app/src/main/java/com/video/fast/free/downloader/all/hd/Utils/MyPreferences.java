package com.video.fast.free.downloader.all.hd.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferences {

    public static final String PREF_FB_COOKIE = "PREF_FB_COOKIE";
    public static final String PREF_FB_KEY = "PREF_FB_KEY";
    public static final String PREF_isFBLOGIN = "PREF_isFBLOGIN";

    public static final String PREF_isAUTO_DOWNLOAD = "PREF_isAUTO_DOWNLOAD";

    public static final String PREF_isINSTALOGIN = "PREF_isINSTALOGIN";
    public static final String PREF_INSTA_SESSIONID = "PREF_INSTA_SESSIONID";
    public static final String PREF_INSTA_CRFTOKEN = "PREF_INSTA_CRFTOKEN";
    public static final String PREF_INSTA_DS_USERID = "PREF_INSTA_DS_USERID";
    public static final String PREF_INSTA_COOKIE = "PREF_INSTA_COOKIE";

    public static final String PREF_INTER_COUNT = "PREF_INTER_COUNT";
    public static final String PREF_INTER_COMPLETE_COUNT = "PREF_INTER_COMPLETE_COUNT";


    // Sharedpref file name
    public static final String PREF_APP_NAME = "ALLVIDEODOWNLOADER";


    private static MyPreferences instance;
    // Shared Preferences
    public SharedPreferences sharedPreferences;
    // Editor for Shared preferences
    private SharedPreferences.Editor editor;


    public MyPreferences(Context context) {
        instance = this;
        sharedPreferences = context.getSharedPreferences(PREF_APP_NAME, Context.MODE_PRIVATE);
    }

    public static MyPreferences getPrefsHelper() {
        return instance;
    }


    public void delete(String key) {
        if (sharedPreferences.contains(key)) {
            editor.remove(key).commit();
        }
    }


    public void setData(String key, Object value) {

        editor = sharedPreferences.edit();
        delete(key);
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Enum) {
            editor.putString(key, value.toString());
        } else if (value != null) {
            throw new RuntimeException("Attempting to save non-primitive preference");
        }
        editor.commit();
    }

    @SuppressWarnings("unchecked")
    public <T> T getPref(String key) {
        return (T) sharedPreferences.getAll().get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getPref(String key, T defValue) {
        T returnValue = (T) sharedPreferences.getAll().get(key);
        return returnValue == null ? defValue : returnValue;
    }
}
