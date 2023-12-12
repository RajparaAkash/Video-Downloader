package com.video.fast.free.downloader.all.hd.Utils;

import static com.video.fast.free.downloader.all.hd.Fragment.Main2DownloadFragment.setData;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;

import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.video.fast.free.downloader.all.hd.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class MyMethod {

    public static boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo != null) {

            // connected to the mobile provider's data plan
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        }
        return false;
    }


    public static void NetCheck_Dilog(final Activity context) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Please Check Your Network Connection And Try Again");
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "OK",
                (dialog, id) -> context.finish());


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    // valid url
    public static boolean mValidUrl(CharSequence input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        Pattern URL_PATTERN = Patterns.WEB_URL;
        boolean isURL = URL_PATTERN.matcher(input).matches();
        if (!isURL) {
            String urlString = input + "";
            if (URLUtil.isNetworkUrl(urlString)) {
                try {
                    new URL(urlString);
                    isURL = true;
                } catch (Exception e) {
                }
            }
        }
        return isURL;
    }

    // open other app
    public static void openApp(Activity activity, String packageName) {
        if (isAppInstalled(activity, packageName)) {
            activity.startActivity(activity.getPackageManager().getLaunchIntentForPackage(packageName));
        } else {
            Dialog dialoglogout = ShowHistory(activity, activity.getResources().getString(R.string.str_installnow), activity.getResources().getString(R.string.str_install_and_download), activity.getResources().getString(R.string.str_cancel));
            AppCompatButton btnCopy = dialoglogout.findViewById(R.id.btnCopy);
            btnCopy.setText("Install");
            btnCopy.setOnClickListener(v1 -> {
                HideLoading(dialoglogout);
                rateApp(activity, packageName);
            });
        }
    }

    public static boolean isAppInstalled(Activity activity, String packageName) {
        PackageManager pm = activity.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return false;
    }

    // rate app
    public static void rateApp(Activity activity, String pckgname) {
        try {
            Intent rateIntent = rateIntentForUrl("market://details", pckgname);
            activity.startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details", pckgname);
            activity.startActivity(rateIntent);
        }
    }

    private static Intent rateIntentForUrl(String url, String pckgname) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, pckgname)));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }

    // Progress dialog
    public static Dialog ShowProgress(Activity activity) {
        Dialog dialog = new Dialog(activity);
        try {
            dialog.setContentView(R.layout.dialog_progress);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.setCancelable(false);
            dialog.getWindow().setLayout(-1, -2);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            ProgressBar mProgressBar = dialog.findViewById(R.id.ProgressBar);
            mProgressBar.setProgress(0);
            mProgressBar.setMax(100);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }

    // Progress dialog
    public static Dialog ShowStatusDownloadProgress(Activity activity, String mTitle, String mStatus) {
        Dialog dialog = new Dialog(activity);
        try {
            dialog.setContentView(R.layout.dialog_progress);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.setCancelable(false);
            dialog.getWindow().setLayout(-1, -2);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            AppCompatTextView tvProgressdesc = dialog.findViewById(R.id.tvProgressdesc);
            AppCompatTextView tvTitle = dialog.findViewById(R.id.tvTitle);
            tvTitle.setText(mTitle);
            ProgressBar dvprogress = dialog.findViewById(R.id.dvprogress);
            dvprogress.setVisibility(View.VISIBLE);
            LinearLayout dhprogress = dialog.findViewById(R.id.dhprogress);
            dhprogress.setVisibility(View.INVISIBLE);
            tvProgressdesc.setText(mStatus);

            ProgressBar mProgressBar = dialog.findViewById(R.id.ProgressBar);
            mProgressBar.setProgress(0);
            mProgressBar.setMax(100);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }

    // DeleteFile dialog
    public static Dialog ShowDeleteFile(Activity activity) {
        Dialog dialog = new Dialog(activity);
        try {
            dialog.setContentView(R.layout.dialog_delete_file);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.setCancelable(false);
            dialog.getWindow().setLayout(-1, -2);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            AppCompatButton btnNo = dialog.findViewById(R.id.btnNo);
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HideLoading(dialog);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }

    // History dialog
    public static Dialog ShowHistory(Activity activity, String title, String link, String btnname) {
        Dialog dialog = new Dialog(activity);
        try {
            dialog.setContentView(R.layout.dialog_history_browser);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.setCancelable(false);
            dialog.getWindow().setLayout(-1, -2);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            AppCompatTextView tvTitle = dialog.findViewById(R.id.tvTitle);
            AppCompatTextView tvLink = dialog.findViewById(R.id.tvLink);
            AppCompatButton btnClose = dialog.findViewById(R.id.btnClose);
            btnClose.setOnClickListener(v -> {
                HideLoading(dialog);
            });

            btnClose.setText(btnname);
            tvTitle.setText(title);
            tvLink.setText(link);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }

    // Hide Dialog
    public static void HideLoading(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    // DownloadVideo
    private static String VideoTitle;
    public static File mainFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Video Downloader");
    public static File subFolder = new File(mainFolder.getAbsoluteFile(), "Videos");
    public static File WAFolder = new File(mainFolder.getAbsoluteFile(), "WA_Status");

    public static String createDirectory() {
        if (!mainFolder.exists()) {
            mainFolder.mkdirs();
        }
        if (!subFolder.exists()) {
            subFolder.mkdirs();
        }
        return subFolder.getPath();
    }

    public static String createWADirectory() {
        if (!mainFolder.exists()) {
            mainFolder.mkdirs();
        }
        if (!WAFolder.exists()) {
            WAFolder.mkdirs();
        }
        return WAFolder.getPath();
    }

    public static class DownloadFileFromURL extends AsyncTask<String, String, String> {

        Activity context;
        Dialog mProgressDialog;
        String mPath;

        public DownloadFileFromURL(Activity context, Dialog dialog, String videoPath) {
            this.context = context;
            this.mProgressDialog = dialog;
            this.mPath = videoPath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            LinearLayout dhprogress = mProgressDialog.findViewById(R.id.dhprogress);
            ProgressBar dvprogress = mProgressDialog.findViewById(R.id.dvprogress);
            AppCompatTextView tvProgressdesc = mProgressDialog.findViewById(R.id.tvProgressdesc);

            tvProgressdesc.setText(context.getResources().getString(R.string.str_process));
            dhprogress.setVisibility(View.VISIBLE);
            dvprogress.setVisibility(View.INVISIBLE);

        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {

                String path = createDirectory();
                VideoTitle = mPath + "_" + System.currentTimeMillis() + ".mp4";

                File newFile = new File(path, VideoTitle);

                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                OutputStream output = new FileOutputStream(newFile.getAbsoluteFile());

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

                return newFile.getAbsolutePath();
            } catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            ProgressBar mProgress = mProgressDialog.findViewById(R.id.ProgressBar);
            AppCompatTextView tvProgress = mProgressDialog.findViewById(R.id.tvProgress);
            mProgress.setProgress(Integer.parseInt(values[0]));
            tvProgress.setText(values[0] + " %");

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            File newFile = new File(file_url);
            if (newFile.exists()) {
                addVideo(newFile, context);
                setData(context, newFile);

                Toast.makeText(context, context.getResources().getString(R.string.str_msg_succ_vid_down), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.str_msg_try_agin), Toast.LENGTH_SHORT).show();

            }

            HideLoading(mProgressDialog);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            HideLoading(mProgressDialog);
            setLoper();

            Toast.makeText(context, context.getResources().getString(R.string.str_msg_try_agin), Toast.LENGTH_SHORT).show();

        }
    }

    public static class SaveImageAsync extends AsyncTask<String, String, String> {
        private Activity context;
        URL ImageUrl;
        Bitmap bmImg = null;

        Dialog mProgressDialog;
        String mPath;

        public SaveImageAsync(Activity contextt, Dialog dialog, String imgPath) {
            this.context = contextt;
            this.mProgressDialog = dialog;
            this.mPath = imgPath;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            LinearLayout dhprogress = mProgressDialog.findViewById(R.id.dhprogress);
            ProgressBar dvprogress = mProgressDialog.findViewById(R.id.dvprogress);
            AppCompatTextView tvProgressdesc = mProgressDialog.findViewById(R.id.tvProgressdesc);

            tvProgressdesc.setText(context.getResources().getString(R.string.str_process));
            dhprogress.setVisibility(View.VISIBLE);
            dvprogress.setVisibility(View.INVISIBLE);

        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub

            InputStream is = null;

            try {

                ImageUrl = new URL(args[0]);
                // myFileUrl1 = args[0];

                HttpURLConnection conn = (HttpURLConnection) ImageUrl
                        .openConnection();
                conn.setDoInput(true);
                conn.connect();
                is = conn.getInputStream();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                bmImg = BitmapFactory.decodeStream(is, null, options);

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {

                String path = createDirectory();
                String fileName = mPath + "_" + System.currentTimeMillis() + ".jpeg";

                File file = new File(path, fileName);


                FileOutputStream fos = new FileOutputStream(file);
                bmImg.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();

                File imageFile = file;
                MediaScannerConnection.scanFile(context,
                        new String[]{imageFile.getPath()},
                        new String[]{"image/jpeg"}, null);

                return imageFile.getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception e) {
                    }
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);


            ProgressBar mProgress = mProgressDialog.findViewById(R.id.ProgressBar);
            AppCompatTextView tvProgress = mProgressDialog.findViewById(R.id.tvProgress);
            mProgress.setProgress(Integer.parseInt(values[0]));
            tvProgress.setText(values[0] + " %");
        }

        @Override
        protected void onPostExecute(String args) {
            // TODO Auto-generated method stub

            File newFile = new File(args);
            if (newFile.exists()) {
                addImageToGallery(newFile.getAbsolutePath(), context);
                setData(context, newFile);

                Toast.makeText(context, context.getResources().getString(R.string.str_msg_succ_img_down), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.str_msg_img_try_agin), Toast.LENGTH_SHORT).show();

            }

            HideLoading(mProgressDialog);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            HideLoading(mProgressDialog);
            setLoper();

            Toast.makeText(context, context.getResources().getString(R.string.str_msg_try_agin), Toast.LENGTH_SHORT).show();

        }

    }


    //copyStatus File
    public static void copyFile(Activity activity, File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }

        if (isImageFile(destFile.getAbsolutePath())) {
            addImageToGallery(destFile.getAbsolutePath(), activity);
        }
        if (isVideoFile(destFile.getAbsolutePath())) {
            addVideo(destFile, activity);
        }
    }

    // isvideo or isimage
    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    public static boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }

    public static String getMimeType(String fileUrl) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(fileUrl);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    public static String getCurrentDt() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MMM dd,yyyy");
        String formattedDate = df.format(date);
        return formattedDate;
    }

    private static Date getStrToDt(String strDate) {
        SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy");
        Date date = null;
        try {
            date = format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private static String getStrToDay(String strDate) {
        String dayOfTheWeekk = "";
        try {

            SimpleDateFormat sdff = new SimpleDateFormat("EEE");
            dayOfTheWeekk = sdff.format(getStrToDt(strDate));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dayOfTheWeekk;
    }

    public static String date2DayTime(String oldTimestr) {
        Date oldTime = getStrToDt(oldTimestr);
        Date newTime = new Date();
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(newTime);
            Calendar oldCal = Calendar.getInstance();
            oldCal.setTime(oldTime);

            int oldYear = oldCal.get(Calendar.YEAR);
            int year = cal.get(Calendar.YEAR);
            int oldDay = oldCal.get(Calendar.DAY_OF_YEAR);
            int day = cal.get(Calendar.DAY_OF_YEAR);

            if (oldYear == year) {
                int value = oldDay - day;
                if (value == -1) {
                    return "Yesterday";
                } else if (value == 0) {
                    return "Today";
                } else if (value == 1) {
                    return "Tomorrow";
                } else {
                    return getStrToDay(oldTimestr);
                }
            }
        } catch (Exception e) {

        }
        return getStrToDay(oldTimestr);
    }

    public static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    public static void addVideo(File videoFile, final Context context) {
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.Video.Media.TITLE, videoFile.getName());
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.DATA, videoFile.getAbsolutePath());
        context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
    }

    public static boolean delete(final Context context, final File file) {
        final String where = MediaStore.MediaColumns.DATA + "=?";
        final String[] selectionArgs = new String[]{
                file.getAbsolutePath()
        };
        final ContentResolver contentResolver = context.getContentResolver();
        final Uri filesUri = MediaStore.Files.getContentUri("external");

        contentResolver.delete(filesUri, where, selectionArgs);

        if (file.exists()) {

            contentResolver.delete(filesUri, where, selectionArgs);
        }
        return !file.exists();
    }

    public static void setLoper() {
        new Thread() {
            @Override
            public void run() {
                if (Looper.myLooper() == null)
                    Looper.prepare();
                Looper.loop();
            }
        }.start();
    }
}
