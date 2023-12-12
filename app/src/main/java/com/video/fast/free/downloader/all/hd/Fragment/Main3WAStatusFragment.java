package com.video.fast.free.downloader.all.hd.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.video.fast.free.downloader.all.hd.R;
import com.video.fast.free.downloader.all.hd.Utils.SharedPrefs;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class Main3WAStatusFragment extends Fragment {

    TabLayout waStatus_tabLayout;
    ViewPager waStatus_viewPager;

    ViewPagerAdapter viewPagerAdapter;
    boolean isOpenWapp = false;
    boolean isOpenWbApp = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_3_wa_status, container, false);

        idBinding(view);

        setupViewPager(waStatus_viewPager);

        waStatus_tabLayout.setupWithViewPager(waStatus_viewPager);
        waStatus_tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                waStatus_viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0 && Main3WAStatusFragment.this.isOpenWapp) {
                    Main3WAStatusFragment.this.isOpenWapp = false;
                    if (!SharedPrefs.getWATree(getActivity()).equals("")) {
                        FragmentManager supportFragmentManager = getChildFragmentManager();
                        ((WaStatus1WhatsAppFragment) supportFragmentManager.findFragmentByTag("android:switcher:" + waStatus_viewPager.getId() + ":" + tab.getPosition())).populateGrid();
                    }
                }
                if (tab.getPosition() == 1 && Main3WAStatusFragment.this.isOpenWbApp) {
                    Main3WAStatusFragment.this.isOpenWbApp = false;
                    if (SharedPrefs.getWBTree(getActivity()).equals("")) {
                        return;
                    }
                    FragmentManager supportFragmentManager2 = getChildFragmentManager();
                    ((WaStatus2WABusinessFragment) supportFragmentManager2.findFragmentByTag("android:switcher:" + waStatus_viewPager.getId() + ":" + tab.getPosition())).populateGrid();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }


    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(new WaStatus1WhatsAppFragment(), "Whatsapp");
        viewPagerAdapter.addFragment(new WaStatus2WABusinessFragment(), "WA Business");
        viewPagerAdapter.addFragment(new WaStatus3PhotosFragment(), "Photos");
        viewPagerAdapter.addFragment(new WaStatus4VideosFragment(), "Videos");

        viewPager.setAdapter(viewPagerAdapter);
    }


    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList;
        private final List<String> mFragmentTitleList;

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            this.mFragmentList = new ArrayList();
            this.mFragmentTitleList = new ArrayList();
        }

        @Override
        public Fragment getItem(int i) {
            return this.mFragmentList.get(i);
        }

        @Override
        public int getCount() {
            return this.mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String str) {
            this.mFragmentList.add(fragment);
            this.mFragmentTitleList.add(str);
        }

        @Override
        public CharSequence getPageTitle(int i) {
            return this.mFragmentTitleList.get(i);
        }
    }

    public void idBinding(View myView){
        waStatus_viewPager = (ViewPager) myView.findViewById(R.id.waStatus_viewpager);
        waStatus_tabLayout = (TabLayout) myView.findViewById(R.id.waStatus_tablayout);
    }
}