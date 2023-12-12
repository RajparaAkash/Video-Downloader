package com.video.fast.free.downloader.all.hd.Fragment;

import static android.app.Activity.RESULT_OK;
import static com.video.fast.free.downloader.all.hd.Utils.DownloadInstagram.startInstaDownload;
import static com.video.fast.free.downloader.all.hd.Utils.DownloadTwitter.getTwitterVideoData;
import static com.video.fast.free.downloader.all.hd.Utils.DownloadVideos.getVideoData;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.HideLoading;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.ShowHistory;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.ShowProgress;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.ShowStatusDownloadProgress;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.getCurrentDt;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.mValidUrl;
import static com.video.fast.free.downloader.all.hd.Utils.MyMethod.openApp;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.sdk.mynew.Interstitial_Ads;
import com.video.fast.free.downloader.all.hd.Activity.ExtraScreen1Activity;
import com.video.fast.free.downloader.all.hd.Activity.FaceBookStoryActivity;
import com.video.fast.free.downloader.all.hd.Activity.GallaryActivity;
import com.video.fast.free.downloader.all.hd.Activity.HistoryActivity;
import com.video.fast.free.downloader.all.hd.Activity.HowToDownloadActivity;
import com.video.fast.free.downloader.all.hd.Activity.InstagramStoryActivity;
import com.video.fast.free.downloader.all.hd.Activity.LoginWithFaceBookActivity;
import com.video.fast.free.downloader.all.hd.Activity.LoginWithInstagramActivity;
import com.video.fast.free.downloader.all.hd.DataBase.DB_History;
import com.video.fast.free.downloader.all.hd.R;
import com.video.fast.free.downloader.all.hd.Utils.DownloaderFacebook;
import com.video.fast.free.downloader.all.hd.Utils.DownloaderLinkedin;
import com.video.fast.free.downloader.all.hd.Utils.DownloaderRoposo;
import com.video.fast.free.downloader.all.hd.Utils.MyMethod;
import com.video.fast.free.downloader.all.hd.Utils.MyPreferences;
import com.sdk.mynew.Native_Ads_Preload_1;

public class Main1HomeFragment extends Fragment {

    DB_History db_history;

    private ImageView dashboard_imgGallery;
    private EditText dashboard_edtUrlSerch;
    private ImageView dashboard_imgMenu;
    private CheckBox dashboard_chkAutoDownload;
    private CheckBox dashboard_chkFbstories;
    private CheckBox dashboard_chkInstastories;
    private LinearLayout dashboard_llFblgn;
    private LinearLayout dashboard_llFbviewStory;
    private LinearLayout dashboard_llInstalgn;
    private LinearLayout dashboard_llinstaviewStory;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_1_home, container, false);

        // native_big
        Native_Ads_Preload_1.getInstance(getActivity()).addNativeAd(view.findViewById(R.id.native_big), false);

        idBinding(view);
        db_history = new DB_History(getActivity());

        fbloginDn();
        instaloginDn();

        boolean isAutoDown = MyPreferences.getPrefsHelper().getPref(MyPreferences.PREF_isAUTO_DOWNLOAD, false);
        dashboard_chkAutoDownload.setChecked(isAutoDown);

        dashboard_imgGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Interstitial_Ads().Show_Ads(getActivity(), new Interstitial_Ads.AdCloseListener() {
                    @Override
                    public void onAdClosed() {
                        startActivity(new Intent(getActivity(), GallaryActivity.class));
                    }
                });
            }
        });

        view.findViewById(R.id.dashboard_imgRemove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboard_edtUrlSerch.setText("");
            }
        });

        dashboard_imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), dashboard_imgMenu);
                popupMenu.getMenuInflater().inflate(R.menu.dashboard_main_menu, popupMenu.getMenu());
                popupMenu.setForceShowIcon(true);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.dashboard_main_history:
                                new Interstitial_Ads().Show_Ads(getActivity(), new Interstitial_Ads.AdCloseListener() {
                                    @Override
                                    public void onAdClosed() {
                                        startActivity(new Intent(getActivity(), HistoryActivity.class));
                                    }
                                });
                                return true;
                            case R.id.dashboard_main_search_engine:
                                try {
                                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                                    startActivity(myIntent);
                                } catch (ActivityNotFoundException e) {
                                    Toast.makeText(getActivity(), "No application can handle this request. Please install a web browser", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                                return true;
                            case R.id.dashboard_main_share:
                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                String shareBody = "Hey Try this amazing application \n\n Download Any Social Media Videos Short Videos Without being Watermark Free & Unlimited Times App Link :- \n\n https://play.google.com/store/apps/details?id=" + getActivity().getPackageName();
                                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                                return true;
                            case R.id.dashboard_main_rate_us:
                                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                try {
                                    startActivity(myAppLinkToMarket);
                                } catch (ActivityNotFoundException e) {
                                    e.printStackTrace();
                                }
                                return true;
                            default:
                                return Main1HomeFragment.super.onOptionsItemSelected(menuItem);
                        }

                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });

        view.findViewById(R.id.dashboard_llPaste).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipMan = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                dashboard_edtUrlSerch.setText(clipMan.getText());

                if (MyPreferences.getPrefsHelper().getPref(MyPreferences.PREF_isAUTO_DOWNLOAD, false)) {
                    downloadData();
                }
            }
        });

        view.findViewById(R.id.dashboard_llDownload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadData();
            }
        });

        dashboard_chkAutoDownload.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_isAUTO_DOWNLOAD, isChecked);

            }
        });

        dashboard_chkFbstories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MyPreferences.getPrefsHelper().getPref(MyPreferences.PREF_isFBLOGIN, false)) {
                    startActivityForResult(new Intent(getActivity(), LoginWithFaceBookActivity.class), 201);
                    return;
                }

                Dialog dialoglogout = ShowHistory(getActivity(), getResources().getString(R.string.str_logout), getResources().getString(R.string.str_msg_download_media), getResources().getString(R.string.str_cancel));
                AppCompatButton btnCopy = dialoglogout.findViewById(R.id.btnCopy);
                btnCopy.setText("Yes");
                btnCopy.setOnClickListener(v1 -> {
                    HideLoading(dialoglogout);

                    Dialog dialog = ShowStatusDownloadProgress(getActivity(), getResources().getString(R.string.str_fb_logout), getResources().getString(R.string.str_process));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            HideLoading(dialog);

                            MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_FB_KEY, "");
                            MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_isFBLOGIN, false);
                            MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_FB_COOKIE, "");

                            CookieManager.getInstance().removeAllCookies((ValueCallback) null);
                            CookieManager.getInstance().flush();
                            fbloginDn();
                            Toast.makeText(getActivity(), getResources().getString(R.string.str_logoutSuccess), Toast.LENGTH_SHORT).show();
                        }
                    }, 3000);

                });
            }
        });

        dashboard_chkInstastories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MyPreferences.getPrefsHelper().getPref(MyPreferences.PREF_isINSTALOGIN, false)) {
                    startActivityForResult(new Intent(getActivity(), LoginWithInstagramActivity.class), 200);
                    return;
                }

                Dialog dialoglogoutt = ShowHistory(getActivity(), getResources().getString(R.string.str_logout), getResources().getString(R.string.str_msg_download_media), getResources().getString(R.string.str_cancel));
                AppCompatButton btnCopyy = dialoglogoutt.findViewById(R.id.btnCopy);
                btnCopyy.setText("Yes");
                btnCopyy.setOnClickListener(v1 -> {
                    HideLoading(dialoglogoutt);

                    Dialog dialog = ShowStatusDownloadProgress(getActivity(), getResources().getString(R.string.str_insta_logout), getResources().getString(R.string.str_process));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            HideLoading(dialog);

                            MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_INSTA_COOKIE, "");
                            MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_INSTA_SESSIONID, "");
                            MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_INSTA_CRFTOKEN, "");
                            MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_INSTA_DS_USERID, "");
                            MyPreferences.getPrefsHelper().setData(MyPreferences.PREF_isINSTALOGIN, false);

                            instaloginDn();
                            Toast.makeText(getActivity(), getResources().getString(R.string.str_logoutSuccess), Toast.LENGTH_SHORT).show();
                        }
                    }, 3000);

                });
            }
        });

        dashboard_llFbviewStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Interstitial_Ads().Show_Ads(getActivity(), new Interstitial_Ads.AdCloseListener() {
                    @Override
                    public void onAdClosed() {
                        if (!MyPreferences.getPrefsHelper().getPref(MyPreferences.PREF_isFBLOGIN, false)) {
                            startActivityForResult(new Intent(getActivity(), LoginWithFaceBookActivity.class), 201);
                        } else {
                            startActivity(new Intent(getActivity(), FaceBookStoryActivity.class));
                        }
                    }
                });
            }
        });

        dashboard_llinstaviewStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Interstitial_Ads().Show_Ads(getActivity(), new Interstitial_Ads.AdCloseListener() {
                    @Override
                    public void onAdClosed() {
                        if (!MyPreferences.getPrefsHelper().getPref(MyPreferences.PREF_isINSTALOGIN, false)) {
                            startActivityForResult(new Intent(getActivity(), LoginWithInstagramActivity.class), 200);
                        } else {
                            startActivity(new Intent(getActivity(), InstagramStoryActivity.class));
                        }
                    }
                });
            }
        });

        view.findViewById(R.id.dashboard_howToDownloadText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Interstitial_Ads().Show_Ads(getActivity(), new Interstitial_Ads.AdCloseListener() {
                    @Override
                    public void onAdClosed() {
                        startActivity(new Intent(getActivity(), HowToDownloadActivity.class));
                    }
                });
            }
        });

        view.findViewById(R.id.dashboard_imgFB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApp(getActivity(), "com.facebook.katana");
            }
        });

        view.findViewById(R.id.dashboard_imgWP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApp(getActivity(), "com.whatsapp");
            }
        });

        view.findViewById(R.id.dashboard_imgInsta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApp(getActivity(), "com.instagram.android");
            }
        });

        view.findViewById(R.id.dashboard_imgVimeo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApp(getActivity(), "com.vimeo.android.videoapp");
            }
        });

        view.findViewById(R.id.dashboard_imgPinterest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApp(getActivity(), "com.pinterest");
            }
        });

        view.findViewById(R.id.dashboard_imgLikee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApp(getActivity(), "video.like");
            }
        });

        view.findViewById(R.id.dashboard_imgLinkdin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApp(getActivity(), "com.linkedin.android");
            }
        });

        view.findViewById(R.id.dashboard_imgRoposo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApp(getActivity(), "com.roposo.android");
            }
        });

        view.findViewById(R.id.dashboard_imgShareChat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApp(getActivity(), "in.mohalla.sharechat");
            }
        });

        view.findViewById(R.id.dashboard_imgTed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApp(getActivity(), "com.ted.android");
            }
        });

        view.findViewById(R.id.dashboard_imgTwitter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApp(getActivity(), "com.twitter.android");
            }
        });

        return view;

    }

    private void downloadData() {
        String mUrl = dashboard_edtUrlSerch.getText().toString();
        if (TextUtils.isEmpty(mUrl)) {
            Toast.makeText(getActivity(), "Please Enter a Valid Url", Toast.LENGTH_SHORT).show();
        } else {

            if (mValidUrl(mUrl)) {

                if (mUrl.contains("mp4")) {
                    Dialog dialog = ShowProgress(getActivity());

                    LinearLayout dhprogress = dialog.findViewById(R.id.dhprogress);
                    ProgressBar dvprogress = dialog.findViewById(R.id.dvprogress);

                    dhprogress.setVisibility(View.INVISIBLE);
                    dvprogress.setVisibility(View.VISIBLE);
                    new MyMethod.DownloadFileFromURL(getActivity(), dialog, "All_Video").execute(mUrl);

                } else if (mUrl.contains("instagram.com")) {
                    startInstaDownload(getActivity(), mUrl);
                } else if (mUrl.contains("fb.watch")) {
                    new DownloaderFacebook.Data(getActivity()).execute(mUrl);
                } else if (mUrl.contains("facebook.com")) {
                    new DownloaderFacebook.Data(getActivity()).execute(mUrl);
                } else if (mUrl.contains("vimeo.com")) {
                    getVideoData(mUrl, getActivity(), "Vimeo");
                } else if (mUrl.contains("pinterest.com")) {
                    getVideoData(mUrl, getActivity(), "Pinterest");
                } else if (mUrl.contains("likee")) {
                    getVideoData(mUrl, getActivity(), "Likee");
                } else if (mUrl.contains("ted.com")) {
                    getVideoData(mUrl, getActivity(), "Ted");
                } else if (mUrl.contains("sharechat.com")) {
                    getVideoData(mUrl, getActivity(), "ShareChat");
                } else if (mUrl.contains("twitter.com")) {
                    getTwitterVideoData(mUrl, getActivity(), "Twitter");
                } else if (mUrl.contains("roposo.com")) {
                    new DownloaderRoposo.RoposoExecute(getActivity()).execute(mUrl);
                } else if (mUrl.contains("linkedin")) {
                    new DownloaderLinkedin.LinkedinAsync(getActivity()).execute(mUrl);
                } else {
                    Toast.makeText(getActivity(), "Url Not Support", Toast.LENGTH_SHORT).show();
                    return;
                }
                db_history.insertHis(getCurrentDt(), mUrl);

            } else {
                Toast.makeText(getActivity(), "Please Enter Valid Video Url", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fbloginDn();
        instaloginDn();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 201 && resultCode == RESULT_OK) {
            fbloginDn();
        } else if (requestCode == 200 && resultCode == RESULT_OK) {
            instaloginDn();
        }
    }

    private void fbloginDn() {
        if (!MyPreferences.getPrefsHelper().getPref(MyPreferences.PREF_isFBLOGIN, false)) {
            dashboard_llFblgn.setVisibility(View.VISIBLE);
            dashboard_chkFbstories.setChecked(false);
            dashboard_llFbviewStory.setVisibility(View.GONE);
        } else {
            dashboard_llFblgn.setVisibility(View.GONE);
            dashboard_chkFbstories.setChecked(true);
            dashboard_llFbviewStory.setVisibility(View.VISIBLE);
        }
    }

    private void instaloginDn() {
        if (!MyPreferences.getPrefsHelper().getPref(MyPreferences.PREF_isINSTALOGIN, false)) {
            dashboard_llInstalgn.setVisibility(View.VISIBLE);
            dashboard_chkInstastories.setChecked(false);
            dashboard_llinstaviewStory.setVisibility(View.GONE);
        } else {
            dashboard_llInstalgn.setVisibility(View.GONE);
            dashboard_chkInstastories.setChecked(true);
            dashboard_llinstaviewStory.setVisibility(View.VISIBLE);
        }
    }

    public void idBinding(View myView) {
        dashboard_imgGallery = (ImageView) myView.findViewById(R.id.dashboard_imgGallery);
        dashboard_edtUrlSerch = (EditText) myView.findViewById(R.id.dashboard_edtUrlSerch);
        dashboard_imgMenu = (ImageView) myView.findViewById(R.id.dashboard_imgMenu);
        dashboard_chkAutoDownload = (CheckBox) myView.findViewById(R.id.dashboard_chkAutoDownload);
        dashboard_chkFbstories = (CheckBox) myView.findViewById(R.id.dashboard_chkFbstories);
        dashboard_chkInstastories = (CheckBox) myView.findViewById(R.id.dashboard_chkInstastories);
        dashboard_llFblgn = (LinearLayout) myView.findViewById(R.id.dashboard_llFblgn);
        dashboard_llFbviewStory = (LinearLayout) myView.findViewById(R.id.dashboard_llFbviewStory);
        dashboard_llInstalgn = (LinearLayout) myView.findViewById(R.id.dashboard_llInstalgn);
        dashboard_llinstaviewStory = (LinearLayout) myView.findViewById(R.id.dashboard_llinstaviewStory);
    }
}