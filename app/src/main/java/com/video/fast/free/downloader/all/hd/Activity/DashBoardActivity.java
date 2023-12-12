package com.video.fast.free.downloader.all.hd.Activity;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.video.fast.free.downloader.all.hd.Fragment.Main1HomeFragment;
import com.video.fast.free.downloader.all.hd.Fragment.Main2DownloadFragment;
import com.video.fast.free.downloader.all.hd.Fragment.Main3WAStatusFragment;
import com.video.fast.free.downloader.all.hd.R;
import com.google.android.material.tabs.TabLayout;
import com.sdk.mynew.Interstitial_Ads_AdmobBack;

import java.util.ArrayList;
import java.util.List;

public class DashBoardActivity extends AppCompatActivity {

    ViewPager dashBoard_viewpager;
    TabLayout dashBoard_tab_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        idBinding();
        setViewPagerMethod();
    }

    public void setViewPagerMethod() {
        setupViewPager(dashBoard_viewpager);
        dashBoard_tab_layout.setupWithViewPager(dashBoard_viewpager);

        dashBoard_tab_layout.getTabAt(0).setCustomView(getHeaderView());
        dashBoard_tab_layout.getTabAt(1).setCustomView(getHeaderView());
        dashBoard_tab_layout.getTabAt(2).setCustomView(getHeaderView());

        for (int i = 0; i < dashBoard_tab_layout.getTabCount(); i++) {
            TabLayout.Tab tab = dashBoard_tab_layout.getTabAt(i);
            AppCompatTextView appCompatTextView = tab.getCustomView().findViewById(R.id.tbTxt);
            AppCompatImageView imageView = tab.getCustomView().findViewById(R.id.tbImg);

            if (i == 0) {
                appCompatTextView.setVisibility(View.VISIBLE);
                appCompatTextView.setTextColor(getResources().getColor(R.color.tab_select));
                imageView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.tab_select)));
                imageView.setImageResource(R.drawable.home_image);
                appCompatTextView.setText("Home");

            } else {
                appCompatTextView.setVisibility(View.VISIBLE);
                appCompatTextView.setTextColor(getResources().getColor(R.color.tab_unselect));
                imageView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.tab_unselect)));

                if (i == 1) {
                    imageView.setImageResource(R.drawable.download_image);
                    appCompatTextView.setText("Download");
                } else {
                    imageView.setImageResource(R.drawable.status_image);
                    appCompatTextView.setText("WA Status");
                }
            }
        }

        dashBoard_tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                AppCompatTextView appCompatTextView = tab.getCustomView().findViewById(R.id.tbTxt);
                appCompatTextView.setTextColor(getResources().getColor(R.color.tab_select));
                appCompatTextView.setVisibility(View.VISIBLE);

                AppCompatImageView imageView = tab.getCustomView().findViewById(R.id.tbImg);
                imageView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.tab_select)));


                if (tab.getText().equals("ONE")) {
                    imageView.setImageResource(R.drawable.home_image);
                    appCompatTextView.setText("Home");
                } else if (tab.getText().equals("TWO")) {
                    imageView.setImageResource(R.drawable.download_image);
                    appCompatTextView.setText("Download");
                } else {
                    imageView.setImageResource(R.drawable.status_image);
                    appCompatTextView.setText("WA Status");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                AppCompatTextView appCompatTextView = tab.getCustomView().findViewById(R.id.tbTxt);
                appCompatTextView.setTextColor(getResources().getColor(R.color.tab_unselect));
                appCompatTextView.setVisibility(View.VISIBLE);

                AppCompatImageView imageView = tab.getCustomView().findViewById(R.id.tbImg);
                imageView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.tab_unselect)));

                if (tab.getText().equals("ONE")) {
                    imageView.setImageResource(R.drawable.home_image);
                    appCompatTextView.setText("Home");
                } else if (tab.getText().equals("TWO")) {
                    imageView.setImageResource(R.drawable.download_image);
                    appCompatTextView.setText("Download");
                } else {
                    imageView.setImageResource(R.drawable.status_image);
                    appCompatTextView.setText("WA Status");
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                
            }
        });
    }

    private View getHeaderView() {
        return ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.item_adapter_custom_tab, null, false);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Main1HomeFragment(), "ONE");
        adapter.addFragment(new Main2DownloadFragment(), "TWO");
        adapter.addFragment(new Main3WAStatusFragment(), "THREE");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }


        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Nullable
        @org.jetbrains.annotations.Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        Interstitial_Ads_AdmobBack.ShowAd_Full(this, () -> finish());
    }

    public void idBinding() {
        dashBoard_viewpager = (ViewPager) findViewById(R.id.dashBoard_viewpager);
        dashBoard_tab_layout = (TabLayout) findViewById(R.id.dashBoard_tab_layout);
    }
}