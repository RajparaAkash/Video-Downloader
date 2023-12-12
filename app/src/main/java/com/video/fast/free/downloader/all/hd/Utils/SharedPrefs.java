package com.video.fast.free.downloader.all.hd.Utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPrefs {
    public static final String PREF_NIGHT_MODE = "night_mode";
    public static final String WA_TREE_URI = "wa_tree_uri";
    public static final String WB_TREE_URI = "wb_tree_uri";
    private static SharedPreferences mPreferences;

    private static SharedPreferences getInstance(Context context) {
        if (mPreferences == null) {
            mPreferences = context.getApplicationContext().getSharedPreferences("wa_data", 0);
        }
        return mPreferences;
    }

    public static int getInt(Context context, String str, int i) {
        return getInstance(context).getInt(str, i);
    }

    public static void setInt(Context context, String str, int i) {
        getInstance(context).edit().putInt(str, i).apply();
    }

    public static void clearPrefs(Context context) {
        getInstance(context).edit().clear().apply();
    }

    public static int getAppNightDayMode(Context context) {
        return getInt(context, "night_mode", 1);
    }

    public static void setLang(Context context, String str) {
        getInstance(context).edit().putString("language", str).apply();
    }

    public static String getLang(Context context) {
        return getInstance(context).getString("language", "en");
    }

    public static void setWATree(Context context, String str) {
        getInstance(context).edit().putString("wa_tree_uri", str).apply();
    }

    public static String getWATree(Context context) {
        return getInstance(context).getString("wa_tree_uri", "");
    }

    public static void setWBTree(Context context, String str) {
        getInstance(context).edit().putString("wb_tree_uri", str).apply();
    }

    public static String getWBTree(Context context) {
        return getInstance(context).getString("wb_tree_uri", "");
    }
}
