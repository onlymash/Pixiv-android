package com.example.administrator.essim.activities_re;

import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.fragments_re.FragmentFollow;
import com.example.administrator.essim.fragments_re.FragmentRecmdFollow;


public class FollowActivity extends BaseActivity {

    private int userID;
    private String userName, dataType;
    //自己的公开，非公开关注
    private static final String[] CHINESE_TITLES = new String[]{"公开","私人"};
    //他人的公开关注
    private static final String[] CHINESE_TITLES_2 = new String[]{"公开"};
    //自己的推荐关注
    private static final String[] CHINESE_TITLES_3 = new String[]{"推荐"};

    @Override
    void initLayout() {
        mLayoutID = R.layout.activity_follow;
    }

    @Override
    void initView() {
        userID = getIntent().getIntExtra("user id", 0);
        userName = getIntent().getStringExtra("user name");
        dataType = getIntent().getStringExtra("dataType");
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle(userName + "的关注");
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                if(dataType.equals("推荐关注")){
                    return new FragmentRecmdFollow();
                }else if(dataType.equals("公开关注")){
                    return FragmentFollow.newInstance(0, userID);
                }else {
                    return FragmentFollow.newInstance(i, userID);
                }
            }

            @Override
            public int getCount() {
                if ("我的关注".equals(dataType)) {
                    return CHINESE_TITLES.length;
                } else if ("公开关注".equals(dataType)) {
                    return CHINESE_TITLES_2.length;
                } else if ("推荐关注".equals(dataType)) {
                    return CHINESE_TITLES_3.length;
                } else {
                    return 0;
                }
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                if(dataType.equals("推荐关注")){
                    return CHINESE_TITLES_3[0];
                }else if(dataType.equals("公开关注")){
                    return CHINESE_TITLES_2[0];
                }else {
                    return CHINESE_TITLES[position];
                }
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
