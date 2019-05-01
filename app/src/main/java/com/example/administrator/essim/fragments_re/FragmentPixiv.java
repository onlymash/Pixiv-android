package com.example.administrator.essim.fragments_re;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities_re.BatchDownloadActivity;
import com.example.administrator.essim.activities_re.MainActivity;
import com.example.administrator.essim.activities.SearchActivity;
import com.example.administrator.essim.activities_re.PixivApp;

import org.jetbrains.annotations.NotNull;

public class FragmentPixiv extends BaseFragment {

    private static final String[] TITLES = new String[]{"为你推荐", "热门标签"};

    private BaseFragment[] mFragments;

    @Override
    void initLayout() {
        mLayoutID = R.layout.fragment_pixiv;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    View initView(View v) {
        Toolbar toolbar = v.findViewById(R.id.toolbar);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v1 -> {
            ((MainActivity) getActivity()).getDrawer().openDrawer(GravityCompat.START, true);
        });
        mFragments = new BaseFragment[2];
        mFragments[0] = new FragmentRecmdIllust();
        mFragments[1] = new FragmentHotTag();
        ViewPager viewPager = v.findViewById(R.id.mViewPager);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return mFragments[i];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @NotNull
            @Override
            public CharSequence getPageTitle(int position) {
                return TITLES[position];
            }
        });
        TabLayout tabLayout = v.findViewById(R.id.mTabLayout);
        tabLayout.setupWithViewPager(viewPager);
        return v;
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_pixiv, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            Intent intent = new Intent(mContext, SearchActivity.class);
            startActivity(intent);
        }else if (item.getItemId() == R.id.action_download) {
            PixivApp.sIllustsBeans = ((FragmentRecmdIllust) mFragments[0]).getAllIllusts();
            Intent intent = new Intent(mContext, BatchDownloadActivity.class);
            intent.putExtra("scroll dist", ((FragmentRecmdIllust) mFragments[0]).getScrollIndex());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public BaseFragment[] getFragments() {
        return mFragments;
    }

    public void setFragments(BaseFragment[] fragments) {
        mFragments = fragments;
    }
}
