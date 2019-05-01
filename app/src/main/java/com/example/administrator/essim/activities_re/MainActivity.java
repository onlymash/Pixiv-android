package com.example.administrator.essim.activities_re;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.SettingsActivity;
import com.example.administrator.essim.fragments.FragmentHitokoto;
import com.example.administrator.essim.fragments.FragmentMine;
import com.example.administrator.essim.fragments_re.FragmentNews;
import com.example.administrator.essim.fragments_re.FragmentPixiv;
import com.example.administrator.essim.interf.OnPrepared;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils_re.GlideUtil;
import com.example.administrator.essim.utils_re.LocalData;
import com.roughike.bottombar.BottomBar;
import com.tbruyelle.rxpermissions2.RxPermissions;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    private long mExitTime;
    private DrawerLayout drawer;
    private int lastShowFragment;
    private Fragment[] mFragments;
    private ImageView userHead;

    @Override
    void initLayout() {
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        mLayoutID = R.layout.activity_main;
    }

    @Override
    void initView() {
        checkPermission(this::loadView);
    }

    void loadView() {
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
            if (LocalData.getEmail().length() != 0) {
                textView2.setText(LocalData.getEmail());
            }
            userHead.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, UserDetailActivity.class);
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
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
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
        if(mFragments[index] == null){
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

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
        //注释掉上面那一行，就不会出现fragment界面重叠的问题，，我佛了
    }

    private void initFragments() {
        FragmentPixiv fragmentPixiv = new FragmentPixiv();
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
                intent.putExtra("dataType", "我的关注");
                intent.putExtra("user name", LocalData.getUserName());
                mContext.startActivity(intent);
                break;
            }
            case R.id.show_recommend_users: {
                Intent intent = new Intent(MainActivity.this, FollowActivity.class);
                intent.putExtra("user id", LocalData.getUserID());
                intent.putExtra("dataType", "推荐关注");
                intent.putExtra("user name", LocalData.getUserName());
                startActivity(intent);
                break;
            }
            case R.id.nav_gallery: {
                Intent intent = new Intent(mContext, ArticleActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.show_my_favorite: {
                Intent intent = new Intent(MainActivity.this, UserDetailActivity.class);
                intent.putExtra("user id", LocalData.getUserID());
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
}
