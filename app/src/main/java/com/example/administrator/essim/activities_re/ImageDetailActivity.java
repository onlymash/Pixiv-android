package com.example.administrator.essim.activities_re;

import android.content.Intent;
import android.graphics.Color;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.fragments_re.FragmentImageDetail;
import com.example.administrator.essim.response_re.IllustsBean;
import com.example.administrator.essim.utils_re.FileDownload;

public class ImageDetailActivity extends BaseActivity {

    public ViewPager mViewPager;
    private IllustsBean mIllustsBean;
    private TextView mTextView;

    @Override
    void initLayout() {
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        mLayoutID = R.layout.activity_image_detail;
    }

    @Override
    void initView() {
        mTextView = findViewById(R.id.image_order);
        findViewById(R.id.download_origin).setOnClickListener(view ->
                FileDownload.downloadSingleOfMulti(mIllustsBean, mViewPager.getCurrentItem()));
        mViewPager = findViewById(R.id.view_pager);
    }

    @Override
    void initData() {
        Intent intent = getIntent();
        mIllustsBean = (IllustsBean) intent.getSerializableExtra("illust");
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return FragmentImageDetail.newInstance(position);
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
                mTextView.setText(String.format("%s/%s", String.valueOf(position + 1),
                        mIllustsBean.getPage_count()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTextView.setText(String.format("%s/%s", String.valueOf(mViewPager.getCurrentItem() + 1),
                mIllustsBean.getPage_count()));
    }

    @Override
    void getFirstData() {

    }

    @Override
    void getNextData() {

    }


    public IllustsBean getIllustsBean() {
        return mIllustsBean;
    }
}