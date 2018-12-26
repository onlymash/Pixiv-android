package com.example.administrator.essim.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.download.DownloadTask;
import com.example.administrator.essim.download.SDDownloadTask;
import com.example.administrator.essim.fragments.FragmentImageDetail;
import com.example.administrator.essim.response_re.IllustsBean;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils_re.FileDownload;
import com.example.administrator.essim.utils_re.LocalData;

import java.io.File;

public class ImageDetailActivity extends BaseActivity {

    public ViewPager mViewPager;
    public IllustsBean mIllustsBean;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(R.layout.activity_image_detail);

        mContext = this;
        Intent intent = getIntent();
        mIllustsBean = (IllustsBean) intent.getSerializableExtra("illust");
        mTextView = findViewById(R.id.image_order);
        findViewById(R.id.download_origin).setOnClickListener(view -> {
            FileDownload.downloadIllust(mIllustsBean);
        });
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return FragmentImageDetail.Companion.newInstance(position);
            }

            @Override
            public int getCount() {
                return mIllustsBean.getPage_count();
            }
        });
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 2) {
                    mTextView.setText(String.format("%s/%s", String.valueOf(mViewPager.getCurrentItem() + 1),
                            mIllustsBean.getPage_count()));
                }
            }
        });
        mTextView.setText(String.format("%s/%s", String.valueOf(mViewPager.getCurrentItem() + 1),
                mIllustsBean.getPage_count()));
    }
}