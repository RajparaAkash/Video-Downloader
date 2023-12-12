#include <jni.h>
#include <string>
#include <unistd.h>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_video_fast_free_downloader_all_hd_Utils_DownloadVideos_getMainUrl(JNIEnv *env, jclass clazz) {
    // TODO: implement getMainUrl()
    std::string hello = "https://dlphpapis.herokuapp.com/api/info?url=";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_video_fast_free_downloader_all_hd_Utils_DownloadVideos_getLastUrl(JNIEnv *env, jclass clazz) {
    // TODO: implement getLastUrl()
    std::string hello = "&flatten=True";
    return env->NewStringUTF(hello.c_str());

}extern "C"
JNIEXPORT jstring JNICALL
Java_com_video_fast_free_downloader_all_hd_Utils_DownloadTwitter_getMainUrl(JNIEnv *env, jclass clazz) {
    // TODO: implement getMainUrl()
    std::string hello = "https://twittervideodownloaderpro.com/twittervideodownloadv2/index.php";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_video_fast_free_downloader_all_hd_Utils_DownloadTwitter_getParam(JNIEnv *env, jclass clazz) {
    // TODO: implement getParam()
    std::string hello = "id";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_video_fast_free_downloader_all_hd_Utils_DownloadInstagram_getUrl(JNIEnv *env, jclass clazz, jstring url) {
    // TODO: implement getUrl()

    const char *murl = env->GetStringUTFChars(url, 0);

    std::string hello = "";

    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");
    jobjectArray ret = nullptr;
    if (cmdline) {
        char application_id[64] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);
        const char *package = "com.allvideo.downloaderpro";

        if (strcmp(package, application_id) == 0) {
            hello.append(murl);
            hello.append("?__a=1&__d=dis");
        }
    }

    env->ReleaseStringUTFChars(url, murl);

    return env->NewStringUTF(hello.c_str());


}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_video_fast_free_downloader_all_hd_Utils_Application_fbGetAL(JNIEnv *env, jclass clazz) {
    // TODO: implement fbGetAL()
    std::string hello = "accept-language";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_video_fast_free_downloader_all_hd_Utils_Application_fbGetALD(JNIEnv *env, jclass clazz) {
    // TODO: implement fbGetALD()
    std::string hello = "en,en-US;q=0.9,fr;q=0.8,ar;q=0.7";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_video_fast_free_downloader_all_hd_Utils_Application_fbGetCK(JNIEnv *env, jclass clazz) {
    // TODO: implement fbGetCK()
    std::string hello = "cookie";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_video_fast_free_downloader_all_hd_Utils_Application_fbGetUA(JNIEnv *env, jclass clazz) {
    // TODO: implement fbGetUA()
    std::string hello = "user-agent";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_video_fast_free_downloader_all_hd_Utils_Application_fbGetUAD(JNIEnv *env, jclass clazz) {
    // TODO: implement fbGetUAD()
    std::string hello = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_video_fast_free_downloader_all_hd_Utils_Application_fbGetUCT(JNIEnv *env, jclass clazz) {
    // TODO: implement fbGetUCT()
    std::string hello = "Content-Type";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_video_fast_free_downloader_all_hd_Utils_Application_fbGetUCD(JNIEnv *env, jclass clazz) {
    // TODO: implement fbGetUCD()
    std::string hello = "application/json";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_video_fast_free_downloader_all_hd_Utils_Application_fbGetKEY(JNIEnv *env, jclass clazz) {
    // TODO: implement fbGetKEY()
    std::string hello = "fb_dtsg";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_video_fast_free_downloader_all_hd_Utils_Application_fbGetV(JNIEnv *env, jclass clazz) {
    // TODO: implement fbGetV()
    std::string hello = "variables";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_video_fast_free_downloader_all_hd_Utils_Application_fbGetVD(JNIEnv *env, jclass clazz) {
    // TODO: implement fbGetVD()
    std::string hello = "{\"bucketsCount\":200,\"initialBucketID\":null,\"pinnedIDs\":[\"\"],\"scale\":3}";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_video_fast_free_downloader_all_hd_Utils_Application_fbGetDI(JNIEnv *env, jclass clazz) {
    // TODO: implement fbGetDI()
    std::string hello = "doc_id";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_video_fast_free_downloader_all_hd_Utils_Application_fbGetDID(JNIEnv *env, jclass clazz) {
    // TODO: implement fbGetDID()
    std::string hello = "2893638314007950";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_video_fast_free_downloader_all_hd_Utils_Application_fbStrUrl(JNIEnv *env, jclass clazz) {
    // TODO: implement fbStrUrl()
    std::string hello = "https://www.facebook.com/api/graphql/";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_video_fast_free_downloader_all_hd_Utils_Application_fbGetUSerDID(JNIEnv *env, jclass clazz) {
    // TODO: implement fbGetUSerDID()
    std::string hello = "2558148157622405";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_video_fast_free_downloader_all_hd_Utils_Application_fbGetUserVD(JNIEnv *env, jclass clazz,
                                                              jstring m_key) {
    // TODO: implement fbGetUserVD()


    const char *murl = env->GetStringUTFChars(m_key, 0);

    std::string hello = "";

    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");
    jobjectArray ret = nullptr;
    if (cmdline) {
        char application_id[64] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);
        const char *package = "com.allvideo.downloaderpro";

        // {\"bucketID\":\"" + str + "\",\"initialBucketID\":\"" + str + "\",\"initialLoad\":false,\"scale\":5}
        if (strcmp(package, application_id) == 0) {

            hello = "{";
            hello.append("\"bucketID\":");
            hello.append("\"");
            hello.append(murl);
            hello.append("\"");

            hello.append(",\"initialBucketID\":");
            hello.append("\"");
            hello.append(murl);

            hello.append("\"");
            hello.append(",\"initialLoad\":");
            hello.append("false");

            hello.append(",\"scale\":");
            hello.append("5");

            hello.append("}");

        }
    }

    env->ReleaseStringUTFChars(m_key, murl);

    return env->NewStringUTF(hello.c_str());

}extern "C"
JNIEXPORT jstring JNICALL
Java_com_video_fast_free_downloader_all_hd_Utils_Application_instaStrUrl(JNIEnv *env, jclass clazz) {
    // TODO: implement instaStrUrl()
    std::string hello = "https://i.instagram.com/api/v1/feed/reels_tray/";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_video_fast_free_downloader_all_hd_Utils_Application_instaCK(JNIEnv *env, jclass clazz) {
    // TODO: implement instaCK()
    std::string hello = "Cookie";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_video_fast_free_downloader_all_hd_Utils_Application_instaUA(JNIEnv *env, jclass clazz) {
    // TODO: implement instaUA()
    std::string hello = "User-Agent";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_video_fast_free_downloader_all_hd_Utils_Application_instaUAD(JNIEnv *env, jclass clazz) {
    // TODO: implement instaUAD()
    std::string hello = "\"Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+\"";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_video_fast_free_downloader_all_hd_Utils_Application_instaParm(JNIEnv *env, jclass clazz, jstring uid,
                                                            jstring usid) {
    // TODO: implement instaParm()

    const char *mid = env->GetStringUTFChars(uid, 0);
    const char *msid = env->GetStringUTFChars(usid, 0);

    std::string hello = "";

    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");
    jobjectArray ret = nullptr;
    if (cmdline) {
        char application_id[64] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);
        const char *package = "com.allvideo.downloaderpro";

        // {\"bucketID\":\"" + str + "\",\"initialBucketID\":\"" + str + "\",\"initialLoad\":false,\"scale\":5}
        if (strcmp(package, application_id) == 0) {

            hello.append("ds_user_id=");
            hello.append(mid);
            hello.append("; sessionid=");
            hello.append(msid);


        }
    }

    env->ReleaseStringUTFChars(uid, mid);
    env->ReleaseStringUTFChars(usid, msid);

    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_video_fast_free_downloader_all_hd_Utils_Application_instaUserStoryStrUrl(JNIEnv *env, jclass clazz,
                                                                       jstring ustr) {
    // TODO: implement instaUserStoryStrUrl()

    const char *mid = env->GetStringUTFChars(ustr, 0);

    std::string hello = "";

    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");
    jobjectArray ret = nullptr;
    if (cmdline) {
        char application_id[64] = {0};
        fread(application_id, sizeof(application_id), 1, cmdline);
        const char *package = "com.allvideo.downloaderpro";

        // {\"bucketID\":\"" + str + "\",\"initialBucketID\":\"" + str + "\",\"initialLoad\":false,\"scale\":5}
        if (strcmp(package, application_id) == 0) {

            hello.append("https://i.instagram.com/api/v1/users/");
            hello.append(mid);
            hello.append("/full_detail_info?max_id=");



        }
    }

    env->ReleaseStringUTFChars(ustr, mid);

    return env->NewStringUTF(hello.c_str());
}