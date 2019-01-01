package com.example.administrator.essim.activities_re;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.SearchActivity;
import com.example.administrator.essim.fragments_re.BaseFragment;
import com.example.administrator.essim.fragments_re.FragmentUserCollect;
import com.example.administrator.essim.fragments_re.FragmentUserWorks;
import com.example.administrator.essim.network_re.Retro;
import com.example.administrator.essim.response_re.UserResponse;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.PixivOperate;
import com.example.administrator.essim.utils_re.GlideUtil;
import com.example.administrator.essim.utils_re.LocalData;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserDetailActivity extends BaseActivity {

    private static final String[] OTHER_USER = new String[]{"投稿", "公开收藏"};
    private static final String[] MYSELF = new String[]{"投稿", "公开收藏", "私人收藏"};
    private String[] dataType;
    private ViewPager mViewPager;
    private ProgressBar mProgressBar;
    private ConstraintLayout mConstraintLayout;
    private CircleImageView mCircleImageView;
    private TextView followMe, iFollow, clickFollow;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private int userID;
    private String userName;
    private BaseFragment[] baseFragments;

    @Override
    void initLayout() {
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        mLayoutID = R.layout.activity_user_detail;
    }

    @Override
    void initView() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(v -> finish());
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
        mProgressBar = findViewById(R.id.top_progress);
        mConstraintLayout = findViewById(R.id.constrain);
        mCircleImageView = findViewById(R.id.circleImageView);
        iFollow = findViewById(R.id.i_follow);
        iFollow.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, FollowActivity.class);
            intent.putExtra("user id", userID);
            intent.putExtra("dataType", "公开关注");
            intent.putExtra("user name", userName);
            startActivity(intent);
        });
        followMe = findViewById(R.id.follow_me);
        followMe.setOnClickListener(v -> Common.showToast("功能未开发"));
        clickFollow = findViewById(R.id.click_follow);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    void initData() {
        userID = getIntent().getIntExtra("user id", 0);
        getUserDetail(userID);
        if (userID != LocalData.getUserID()) {
            baseFragments = new BaseFragment[]{
                    FragmentUserWorks.newInstance(userID),
                    FragmentUserCollect.newInstance(userID, "public")};
            dataType = OTHER_USER;
        } else {
            baseFragments = new BaseFragment[]{
                    FragmentUserWorks.newInstance(userID),
                    FragmentUserCollect.newInstance(userID, "public"),
                    FragmentUserCollect.newInstance(userID, "private")};
            dataType = MYSELF;
        }
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return baseFragments[i];
            }

            @Override
            public int getCount() {
                return dataType.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return dataType[position];
            }
        });
    }

    @Override
    void getFirstData() {

    }

    @Override
    void getNextData() {

    }

    private void getUserDetail(int id) {
        Retro.initToken(() -> Retro.getAppApi().getUserDetail(
                LocalData.getToken(), "for_android", id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserResponse userResponse) {
                        if (userResponse != null && userResponse.getUser() != null) {
                            updateUI(userResponse);
                        } else {
                            Common.showToast(PixivApp.getContext().getString(R.string.load_error));
                        }
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        Common.showToast(PixivApp.getContext().getString(R.string.load_error));
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    /**
     * 获取了详细信息后更新页面
     * @param user
     */
    private void updateUI(UserResponse user){
        Glide.with(mContext).load(GlideUtil.getUserHead(user.getUser()))
                .into(mCircleImageView);
        mConstraintLayout.setVisibility(View.VISIBLE);
        mTabLayout.getTabAt(0).setText("投稿(" +user.getProfile().getTotal_illusts() +")");
        mTabLayout.getTabAt(1).setText("公开收藏(" +user.getProfile().getTotal_illust_bookmarks_public() +")");
        followMe.setText(String.format(getString(R.string.good_friend),
                user.getProfile().getTotal_mypixiv_users()));
        iFollow.setText(String.format(getString(R.string.i_follow),
                user.getProfile().getTotal_follow_users()));
        userName = user.getUser().getName();
        mToolbar.setTitle(userName);
        if (user.getUser().isIs_followed()) {
            clickFollow.setText("取消关注");
        } else {
            clickFollow.setText("点击关注");
        }
        clickFollow.setOnClickListener(v -> {
            if (user.getUser().getId() == LocalData.getUserID()) {
                Common.showToast("不能对自己操作");
            } else {
                if (user.getUser().isIs_followed()) {
                    PixivOperate.postUnFollowUser(user.getUser().getId());
                    clickFollow.setText("点击关注");
                    user.getUser().setIs_followed(false);
                } else {
                    PixivOperate.postFollowUser(user.getUser().getId(), "public");
                    clickFollow.setText("取消关注");
                    user.getUser().setIs_followed(true);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_pixiv, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            Intent intent = new Intent(mContext, SearchActivity.class);
            startActivity(intent);
        }else if (item.getItemId() == R.id.action_download) {
            Intent intent = new Intent(mContext, BatchDownloadActivity.class);
            if(mViewPager.getCurrentItem() == 0){
                PixivApp.sIllustsBeans = ((FragmentUserWorks) baseFragments[0]).getAllIllusts();
                intent.putExtra("scroll dist", ((FragmentUserWorks) baseFragments[0]).getScrollIndex());
            }else if(mViewPager.getCurrentItem() == 1){
                PixivApp.sIllustsBeans = ((FragmentUserCollect) baseFragments[1]).getAllIllusts();
                intent.putExtra("scroll dist", ((FragmentUserCollect) baseFragments[1]).getScrollIndex());
            }else if(mViewPager.getCurrentItem() == 2){
                PixivApp.sIllustsBeans = ((FragmentUserCollect) baseFragments[2]).getAllIllusts();
                intent.putExtra("scroll dist", ((FragmentUserCollect) baseFragments[2]).getScrollIndex());
            }
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
