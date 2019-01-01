package com.example.administrator.essim.activities_re;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.administrator.essim.R;
import com.example.administrator.essim.fragments_re.FragmentRank;
import com.example.administrator.essim.utils.Common;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;


public class RankActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    private static final String[] CHINESE_TITLES = new String[]{"日榜", "每周", "每月", "男性向", "女性向", "原创", "新人", "R"};
    private FragmentRank[] allPages = new FragmentRank[]{null, null, null, null, null, null, null, null};
    private ViewPager mViewPager;

    private String dateString = "";

    @Override
    void initLayout() {
        mLayoutID = R.layout.activity_rank;
    }

    @Override
    void initView() {
        dateString = getIntent().getStringExtra("date");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        if(dateString != null){
            toolbar.setTitle(dateString + "排行榜");
        }
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                if (allPages[i] == null) {
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
        tabLayout.setupWithViewPager(mViewPager);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.rank_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_download) {
            PixivApp.sIllustsBeans = allPages[mViewPager.getCurrentItem()].getAllIllusts();
            Intent intent = new Intent(mContext, BatchDownloadActivity.class);
            intent.putExtra("scroll dist", allPages[mViewPager.getCurrentItem()].getScrollIndex());
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_day) {
            Calendar now = Calendar.getInstance();
            now.add(Calendar.DAY_OF_MONTH, -1);
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    RankActivity.this,
                    now.get(Calendar.YEAR), // Initial year selection
                    now.get(Calendar.MONTH), // Initial month selection
                    now.get(Calendar.DAY_OF_MONTH) // Inital day selection
            );
            Calendar start = Calendar.getInstance();
            start.set(2008, 1, 1);
            dpd.setMinDate(start);
            dpd.setMaxDate(now);
            dpd.show(getFragmentManager(), "Datepickerdialog");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        Common.showLog(date);
        Intent intent = new Intent(mContext, RankActivity.class);
        intent.putExtra("date", String.valueOf(date));
        startActivity(intent);
        finish();
    }

    public String getDateString() {
        return dateString;
    }
}
