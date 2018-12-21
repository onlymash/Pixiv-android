package com.example.administrator.essim.activities_re;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.fragments_re.FragmentRank;


public class RankActivity extends BaseActivity {

    private static final String[] CHINESE_TITLES = new String[]{"日榜","每周", "每月", "男性向", "女性向", "原创", "新人",  "R"};
    private FragmentRank[] allPages = new FragmentRank[]{null, null, null, null, null, null, null, null};

    @Override
    void initLayout() {
        mLayoutID = R.layout.activity_rank;
    }

    @Override
    void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                if(allPages[i] == null){
                    allPages[i] = FragmentRank.newInstance(i);
                }
                return allPages[i];
            }

            @Override
            public int getCount() {
                return CHINESE_TITLES.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return CHINESE_TITLES[position];
            }


        });
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    void initData() {

    }

    @Override
    void getFirstData() {

    }

    @Override
    void getNextData() {

    }
}
