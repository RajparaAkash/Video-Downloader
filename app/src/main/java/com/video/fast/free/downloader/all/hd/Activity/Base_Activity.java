package com.video.fast.free.downloader.all.hd.Activity;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.sdk.mynew.AppPreference;
import com.sdk.mynew.CheckInstallActivity;

public class Base_Activity extends AppCompatActivity {

    String page = "0";

    public Class getNextIntent(Activity activity) {
        AppPreference preference = new AppPreference(activity);
        page = preference.getPage();
        if (Integer.parseInt(page) > 4) {
            page = "4";
        }
        if (CheckInstallActivity.checkScreenFlag(activity)) {
            switch (page) {
                case "0":
                    return StartActivity.class;

                case "1":
                    if (activity instanceof ExtraScreen1Activity) {
                        return ExtraScreen2Activity.class;
                    } else {
                        return StartActivity.class;
                    }

                case "2":
                    if (activity instanceof SplashScreenActivity) {
                        return ExtraScreen1Activity.class;
                    } else if (activity instanceof ExtraScreen1Activity) {
                        return ExtraScreen2Activity.class;
                    } else {
                        return StartActivity.class;
                    }
                case "3":
                    if (activity instanceof SplashScreenActivity) {
                        return ExtraScreen1Activity.class;
                    } else if (activity instanceof ExtraScreen1Activity) {
                        return ExtraScreen2Activity.class;
                    } else if (activity instanceof ExtraScreen2Activity) {
                        return ExtraScreen3Activity.class;
                    } else {
                        return StartActivity.class;
                    }
                case "4":
                    if (activity instanceof SplashScreenActivity) {
                        return ExtraScreen1Activity.class;
                    } else if (activity instanceof ExtraScreen1Activity) {
                        return ExtraScreen2Activity.class;
                    } else if (activity instanceof ExtraScreen2Activity) {
                        return ExtraScreen3Activity.class;
                    } else if (activity instanceof ExtraScreen3Activity) {
                        return ExtraScreen4Activity.class;
                    } else {
                        return StartActivity.class;
                    }
            }
        }
        return StartActivity.class;
    }
}
