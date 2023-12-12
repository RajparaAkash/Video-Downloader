package com.video.fast.free.downloader.all.hd.Utils;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

public class Application extends com.sdk.mynew.MyApplication {

    public static Context mActivity;
    public static Application mInstance;

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mActivity = this;
        AndroidNetworking.initialize(this);
        new MyPreferences(this);

        MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_INTER_COUNT, 2);


        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        String ONESIGNAL_APP_ID = "a1fa0b5d-54c5-4f76-b1d7-1ada01434035";
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        OneSignal.initWithContext(this);

        OneSignal.setNotificationOpenedHandler(result ->
                OneSignal.onesignalLog(OneSignal.LOG_LEVEL.VERBOSE, "OSNotificationOpenedResult result: " + result.toString()));

        OneSignal.setNotificationWillShowInForegroundHandler(notificationReceivedEvent -> {
            OneSignal.onesignalLog(OneSignal.LOG_LEVEL.VERBOSE, "NotificationWillShowInForegroundHandler fired!" +
                    " with notification event: " + notificationReceivedEvent.toString());

            OSNotification notification = notificationReceivedEvent.getNotification();

            notificationReceivedEvent.complete(notification);
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static synchronized Application getInstance() {
        return mInstance;
    }

    public static synchronized Context getContext() {
        return getInstance().getApplicationContext();
    }

    public static native String instaUserStoryStrUrl(String Ustr);

    public static native String instaStrUrl();

    public static native String instaCK();

    public static native String instaUA();

    public static native String instaUAD();

    public static native String instaParm(String Uid, String USid);

    public static native String fbStrUrl();

    public static native String fbGetAL();

    public static native String fbGetALD();

    public static native String fbGetCK();

    public static native String fbGetUA();

    public static native String fbGetUAD();

    public static native String fbGetUCT();

    public static native String fbGetUCD();

    public static native String fbGetKEY();

    public static native String fbGetV();

    public static native String fbGetVD();

    public static native String fbGetUserVD(String mKey);

    public static native String fbGetDI();

    public static native String fbGetDID();

    public static native String fbGetUSerDID();

}
