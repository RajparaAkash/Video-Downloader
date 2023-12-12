package com.video.fast.free.downloader.all.hd.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;

import com.video.fast.free.downloader.all.hd.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WhatsAppUtil {

    static AlertDialog alertDialog = null;
    public static String mPath = null;

    public static boolean appInstalledOrNot(Context context, String str) {
        try {
            context.getPackageManager().getPackageInfo(str, 1);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    public static AlertDialog loadingPopup(Context context) {
        alertDialog = null;
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_loader, (ViewGroup) null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(inflate);
        AlertDialog create = builder.create();
        alertDialog = create;
        create.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.setCancelable(false);
        return alertDialog;
    }

    public static boolean download(Context context, String str) {
        return copyFileInSavedDir(context, str);
    }

    public static boolean copyFileInSavedDir(Context context, String str) {

        String absolutePath;
        if (isVideoFile(context, str)) {
            absolutePath = getDir(context, "WA_Videos").getAbsolutePath();
        } else {
            absolutePath = getDir(context, "WA_Images").getAbsolutePath();
        }
        String str2 = absolutePath + File.separator + new File(Uri.parse(str).getPath()).getName();
        Uri fromFile = Uri.fromFile(new File(str2));


        if (new File(str2).exists()) {
            Toast.makeText(context, "Already Downloaded", Toast.LENGTH_SHORT).show();
        } else {
            try {
                InputStream openInputStream = context.getContentResolver().openInputStream(Uri.parse(str));
                OutputStream openOutputStream = context.getContentResolver().openOutputStream(fromFile, "w");
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = openInputStream.read(bArr);
                    if (read > 0) {
                        openOutputStream.write(bArr, 0, read);
                    } else {
                        openInputStream.close();
                        openOutputStream.flush();
                        openOutputStream.close();
                        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                        intent.setData(fromFile);
                        context.sendBroadcast(intent);
                        Toast.makeText(context, "Saved Successfully", Toast.LENGTH_SHORT).show();

                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Saved UnSuccessfully", Toast.LENGTH_SHORT).show();

                return false;
            }
        }
        return false;
    }

    public static boolean download1(Context context, String str) {
        return copyFileInSavedDir1(context, str);
    }

    public static boolean copyFileInSavedDir1(Context context, String str) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        final View customLayout = LayoutInflater.from(context).inflate(R.layout.dialog_loader, null);
        alertDialog.setView(customLayout);

        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();

        String absolutePath;
        if (isVideoFile(context, str)) {
            absolutePath = getDir(context, "WA_Videos").getAbsolutePath();
        } else {
            absolutePath = getDir(context, "WA_Images").getAbsolutePath();
        }
        String str2 = absolutePath + File.separator + new File(Uri.parse(str).getPath()).getName();
        Uri fromFile = Uri.fromFile(new File(str2));
        try {
            InputStream openInputStream = context.getContentResolver().openInputStream(Uri.parse(str));
            OutputStream openOutputStream = context.getContentResolver().openOutputStream(fromFile, "w");
            byte[] bArr = new byte[1024];
            while (true) {
                int read = openInputStream.read(bArr);
                if (read > 0) {
                    openOutputStream.write(bArr, 0, read);
                } else {
                    openInputStream.close();
                    openOutputStream.flush();
                    openOutputStream.close();
                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    intent.setData(fromFile);
                    context.sendBroadcast(intent);
                    alert.dismiss();
                    Toast.makeText(context, "Saved Successfully", Toast.LENGTH_SHORT).show();

                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            alert.dismiss();
            Toast.makeText(context, "Saved UnSuccessfully", Toast.LENGTH_SHORT).show();

            return false;
        }
    }


    public static boolean isVideoFile(Context context, String str) {
        if (str.startsWith("content")) {
            String type = DocumentFile.fromSingleUri(context, Uri.parse(str)).getType();
            return type != null && type.startsWith("video");
        }
        String guessContentTypeFromName = URLConnection.guessContentTypeFromName(str);
        return guessContentTypeFromName != null && guessContentTypeFromName.startsWith("video");
    }

    static File getDir(Context context, String str) {
        File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "Download" + File.separator + context.getResources().getString(R.string.app_name) + File.separator + str);
        file.mkdirs();
        return file;
    }

    public static String getBack(String str, String str2) {
        Matcher matcher = Pattern.compile(str2).matcher(str);
        return matcher.find() ? matcher.group(1) : "";
    }

    public static void repostWhatsApp(Context context, boolean z, String str) {
        Uri uriForFile;
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        if (z) {
            intent.setType("Video/*");
        } else {
            intent.setType("image/*");
        }
        if (str.startsWith("content")) {
            uriForFile = Uri.parse(str);
        } else {
            uriForFile = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", new File(str));
        }
        intent.putExtra("android.intent.extra.STREAM", uriForFile);
        intent.setPackage("com.whatsapp");
        context.startActivity(intent);
    }

    public static void shareFile(Context context, boolean z, String str) {
        Uri uriForFile;
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        if (z) {
            intent.setType("Video/*");
        } else {
            intent.setType("image/*");
        }
        if (str.startsWith("content")) {
            uriForFile = Uri.parse(str);
        } else {
            uriForFile = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", new File(str));
        }
        intent.putExtra("android.intent.extra.STREAM", uriForFile);
        context.startActivity(intent);
    }
}

