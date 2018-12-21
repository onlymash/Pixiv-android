package com.example.administrator.essim.fragments_re;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.utils_re.CustPagerTransformer;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class FragmentNews extends BaseFragment {

    private ViewPager mViewPager;
    private TextView mTextView;
    private static final String[] TITLES = new String[]{"榜单", "动态", "特辑"};


    @Override
    void initLayout() {
        mLayoutID = R.layout.fragment_news;
    }

    @Override
    View initView(View v) {
        mTextView = v.findViewById(R.id.title_text);
        mViewPager = v.findViewById(R.id.view_pager);
        return v;
    }

    @Override
    void initData() {
        mViewPager.setPageTransformer(false, new CustPagerTransformer(mContext));
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return FragmentCard.newInstance(position);
            }

            @Override
            public int getCount() {
                return TITLES.length;
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mTextView.setText(TITLES[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    void getFirstData() {

    }

    @Override
    void getNextData() {

    }
}
