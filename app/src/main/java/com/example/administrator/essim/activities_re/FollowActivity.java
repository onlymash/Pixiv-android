package com.example.administrator.essim.activities_re;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.administrator.essim.R;
import com.example.administrator.essim.fragments_re.FragmentFollow;
import com.example.administrator.essim.utils_re.LocalData;


public class FollowActivity extends BaseActivity {

    private int userID;
    private String userName;
    private static final String[] CHINESE_TITLES = new String[]{"公开","私人"};
    private static final String[] CHINESE_TITLES_2 = new String[]{"公开"};

    @Override
    void initLayout() {
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        mLayoutID = R.layout.activity_follow;
    }

    @Override
    void initView() {
        userID = getIntent().getIntExtra("user id", 0);
        userName = getIntent().getStringExtra("user name");
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle(userName + "的关注");
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return FragmentFollow.newInstance(i, userID);
            }

            @Override
            public int getCount() {
                if(userID == LocalData.getUserID()) {
                    return CHINESE_TITLES.length;
                }else {
                    return CHINESE_TITLES_2.length;
                }
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
