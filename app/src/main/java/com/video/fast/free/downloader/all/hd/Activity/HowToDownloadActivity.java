package com.video.fast.free.downloader.all.hd.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.sdk.mynew.Interstitial_Ads;
import com.sdk.mynew.Interstitial_Ads_AdmobBack;
import com.video.fast.free.downloader.all.hd.R;

import java.util.Objects;

public class HowToDownloadActivity extends AppCompatActivity {

    int[] images = {R.drawable.how_to_download_1_image,
            R.drawable.how_to_download_2_image,
            R.drawable.how_to_download_3_image};

    ViewPager htd_viewPager;
    TextView htd_nextText;
    TextView htd_finishText;
    ViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_download);

        idBinding();

        mViewPagerAdapter = new ViewPagerAdapter(HowToDownloadActivity.this, images);
        htd_viewPager.setAdapter(mViewPagerAdapter);

        htd_nextText.setVisibility(View.VISIBLE);
        htd_finishText.setVisibility(View.GONE);

        htd_finishText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Interstitial_Ads().Show_Ads(HowToDownloadActivity.this, new Interstitial_Ads.AdCloseListener() {
                    @Override
                    public void onAdClosed() {
                        finish();
                    }
                });
            }
        });

        htd_nextText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                htd_viewPager.setCurrentItem(getItem(+1), true);
            }
        });

        htd_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == (images.length - 1)) {
                    htd_nextText.setVisibility(View.GONE);
                    htd_finishText.setVisibility(View.VISIBLE);
                } else {
                    htd_nextText.setVisibility(View.VISIBLE);
                    htd_finishText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private int getItem(int i) {
        return htd_viewPager.getCurrentItem() + i;
    }

    class ViewPagerAdapter extends PagerAdapter {

        // Context object
        Context context;

        // Array of images
        int[] images;

        // Layout Inflater
        LayoutInflater mLayoutInflater;


        // Viewpager Constructor
        public ViewPagerAdapter(Context context, int[] images) {
            this.context = context;
            this.images = images;
            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // return the number of images
            return images.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == (object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            // inflating the item.xml
            View itemView = mLayoutInflater.inflate(R.layout.item_adapter_how_to_download, container, false);

            // referencing the image view from the item.xml file
            AppCompatImageView imageView = itemView.findViewById(R.id.imgPgr);

            // setting the image in the imageView
            imageView.setImageResource(images[position]);

            // Adding the View
            Objects.requireNonNull(container).addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((LinearLayout) object);
        }
    }

    @Override
    public void onBackPressed() {
        Interstitial_Ads_AdmobBack.ShowAd_Full(this, () -> finish());
    }

    public void idBinding() {
        htd_viewPager = findViewById(R.id.htd_viewPager);
        htd_nextText = findViewById(R.id.htd_nextText);
        htd_finishText = findViewById(R.id.htd_finishText);
    }
}