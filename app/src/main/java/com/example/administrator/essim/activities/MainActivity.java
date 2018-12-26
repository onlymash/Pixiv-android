package com.example.administrator.essim.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.activities_re.ArticleActivity;
import com.example.administrator.essim.activities_re.FollowActivity;
import com.example.administrator.essim.activities_re.LoginActivity;
import com.example.administrator.essim.activities_re.UserDetailActivity;
import com.example.administrator.essim.fragments.FragmentHitokoto;
import com.example.administrator.essim.fragments.FragmentMine;
import com.example.administrator.essim.fragments_re.FragmentNews;
import com.example.administrator.essim.interf.OnPrepared;
import com.example.administrator.essim.presenter.MainPresenter;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils_re.GlideUtil;
import com.example.administrator.essim.utils_re.LocalData;
import com.example.administrator.essim.utils_re.NotifiUtil;
import com.example.administrator.essim.views.MainView;
import com.roughike.bottombar.BottomBar;
import com.tbruyelle.rxpermissions2.RxPermissions;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, MainView {

    private long mExitTime;
    private DrawerLayout drawer;
    private int lastShowFragment;
    private Fragment[] mFragments;
    private ImageView userHead;
    private MainPresenter mPresenter;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        checkPermission(this::initView);
    }

    private void initView() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        userHead = navigationView.getHeaderView(0).findViewById(R.id.imageView);
        if (LocalData.isLogin()) {
            drawer = findViewById(R.id.drawer_layout);
            TextView textView = navigationView.getHeaderView(0).findViewById(R.id.username);
            TextView textView2 = navigationView.getHeaderView(0).findViewById(R.id.useremail);
            textView.setText(LocalData.getUserName()
                    .equals(LocalData.getUserAccount()) ?
                    LocalData.getUserName() : String.format("%s (%s)",
                    LocalData.getUserName(),
                    LocalData.getUserAccount()));
            if (LocalData.getLocalDataSet().getString("email", "").length() != 0) {
                textView2.setText(LocalData.getLocalDataSet().getString("email", ""));
            }
            userHead.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, UserDetailActivity.class);
                intent.putExtra("user id", LocalData.getUserID());
                startActivity(intent);
            });
            BottomBar bottomBar = findViewById(R.id.bottomBar);
            bottomBar.setOnTabSelectListener(tabId -> {
                switch (tabId) {
                    case R.id.tab_pixiv:
                        switchFragment(0);
                        break;
                    case R.id.tab_rank:
                        switchFragment(1);
                        break;
                    case R.id.tab_hitokoto:
                        switchFragment(2);
                        break;
                    case R.id.tab_mine:
                        switchFragment(3);
                        break;
                    default:
                        break;
                }
            });
            initFragments();
            mTextView = findViewById(R.id.online_count);
            mPresenter = new MainPresenter(this);
            mPresenter.start();

        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void checkPermission(OnPrepared onPrepared) {
        final RxPermissions rxPermissions = new RxPermissions(this);

        rxPermissions
                .requestEachCombined(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE)
                .subscribe(permission  -> {
                    if (permission.granted ) {
                        onPrepared.doSomething();
                    } else {
                        // At least one permission is denied
                        Common.showToast("请给予足够的权限");
                    }
                });
    }

    /**
     * 切换Fragment
     *
     * @param index 需要显示的index
     */
    public void switchFragment(int index) {
        if (lastShowFragment == index) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(mFragments[lastShowFragment]);
        if (!mFragments[index].isAdded()) {
            transaction.add(R.id.fragment_container, mFragments[index]);
        }
        transaction.show(mFragments[index]).commitAllowingStateLoss();
        lastShowFragment = index;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
        //注释掉上面那一行，就不会出现fragment界面重叠的问题，，我佛了
    }

    private void initFragments() {
        com.example.administrator.essim.fragments_re.FragmentPixiv fragmentPixiv = new com.example.administrator.essim.fragments_re.FragmentPixiv();
        FragmentNews fragmentNews = new FragmentNews();
        FragmentHitokoto fragmentHitokoto = new FragmentHitokoto();
        FragmentMine fragmentMine = new FragmentMine();
        mFragments = new Fragment[]{fragmentPixiv, fragmentNews, fragmentHitokoto, fragmentMine};
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, fragmentPixiv)
                .show(fragmentPixiv)
                .commit();
        lastShowFragment = 0;
    }

    public DrawerLayout getDrawer() {
        return drawer;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.show_my_followed_users: {
                Intent intent = new Intent(mContext, FollowActivity.class);
                intent.putExtra("user id", LocalData.getUserID());
                intent.putExtra("user name", LocalData.getUserName());
                mContext.startActivity(intent);
                break;
            }
            case R.id.show_recommend_users: {
                Intent intent = new Intent(MainActivity.this, FollowActivity.class);
                intent.putExtra("user id", LocalData.getUserID());
                intent.putExtra("user name", LocalData.getUserName());
                startActivity(intent);
                break;
            }
            case R.id.nav_gallery: {
                //特辑走一波
                Intent intent = new Intent(mContext, ArticleActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.show_my_favorite: {
                Intent intent = new Intent(MainActivity.this, UserDetailActivity.class);
                intent.putExtra("user id", LocalData.getUserID());
                intent.putExtra("show favorite illust", true);
                startActivity(intent);
                break;
            }
            case R.id.nav_manage: {
                Intent intent = new Intent(mContext, SettingsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_send: {
                Intent intent = new Intent(mContext, AboutAppActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.thanks: {
                Intent intent = new Intent(mContext, ThanksActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.view_history: {
                Intent intent = new Intent(mContext, RecyclerViewActivity.class);
                intent.putExtra("dataType", "history");
                startActivity(intent);
                break;
            }
            default:
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LocalData.getHeadImage().length() != 0) {
            if (userHead.getDrawable() == null) {
                Glide.with(mContext)
                        .load(GlideUtil.getUserHead(LocalData.getHeadImage()))
                        .into(userHead);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                exit();
                return true;
            }
            return false;
        }
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(mContext, "再按一次退出~", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setNowPressure(String nowOnline) {
        mTextView.setText(nowOnline);
    }

    @Override
    public Context getSelfContext() {
        return mContext;
    }
}
