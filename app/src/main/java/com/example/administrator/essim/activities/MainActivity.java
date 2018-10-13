package com.example.administrator.essim.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.fragments.FragmentHitokoto;
import com.example.administrator.essim.fragments.FragmentMine;
import com.example.administrator.essim.fragments.FragmentPixiv;
import com.example.administrator.essim.fragments.FragmentRank;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.GlideUtil;
import com.example.administrator.essim.utils.LocalData;
import com.roughike.bottombar.BottomBar;
import com.sdsmdg.tastytoast.TastyToast;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private long mExitTime;
    private DrawerLayout drawer;
    private int lastShowFragment;
    private Fragment[] mFragments;
    private ImageView mImageView, userHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(R.layout.activity_main);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //获取读写本地文件的权限
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        //判断是否有登录记录，没登录就去LoginActivity，登录了就加载视图
        if (LocalData.getLocalDataSet().getBoolean("islogin", false)) {
            TextView textView = navigationView.getHeaderView(0).findViewById(R.id.username);
            TextView textView2 = navigationView.getHeaderView(0).findViewById(R.id.useremail);
            textView.setText(LocalData.getUserName()
                    .equals(LocalData.getUserAccount()) ?
                    LocalData.getUserName() : String.format("%s (%s)",
                    LocalData.getUserName() ,
                    LocalData.getUserAccount()));
            if (LocalData.getLocalDataSet().getString("email", "").length() != 0) {
                textView2.setText(LocalData.getLocalDataSet().getString("email", ""));
            }
            userHead = navigationView.getHeaderView(0).findViewById(R.id.imageView);
            Glide.with(mContext).load(new GlideUtil().getHead(LocalData.getLocalDataSet().getInt("userid", 0),
                    LocalData.getLocalDataSet().getString("hearurl", ""))).into(userHead);
            userHead.setOnClickListener(view -> {
                if (LocalData.getLocalDataSet().getBoolean("islogin", false)) {
                    Intent intent = new Intent(MainActivity.this, UserDetailActivity.class);
                    intent.putExtra("user id", LocalData.getLocalDataSet().getInt("userid", 0));
                    startActivity(intent);
                }
            });
            mImageView = navigationView.getHeaderView(0).findViewById(R.id.header_img);
            BottomBar bottomBar = findViewById(R.id.bottomBar);
            bottomBar.setOnTabSelectListener(tabId -> {
                switch (tabId) {
                    case R.id.tab_pixiv:
                        switchFrament(0);
                        break;
                    case R.id.tab_rank:
                        switchFrament(1);
                        break;
                    case R.id.tab_hitokoto:
                        switchFrament(2);
                        break;
                    case R.id.tab_mine:
                        switchFrament(3);
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

    /**
     * 切换Fragment
     *
     * @param index 需要显示的index
     */
    public void switchFrament(int index) {
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
        FragmentPixiv fragmentPixiv = new FragmentPixiv();
        FragmentRank fragmentRank = new FragmentRank();
        FragmentHitokoto fragmentHitokoto = new FragmentHitokoto();
        FragmentMine fragmentMine = new FragmentMine();
        mFragments = new Fragment[]{fragmentPixiv, fragmentRank, fragmentHitokoto, fragmentMine};
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

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("店长推荐：");
        builder.setMessage("请确认你的账号已开启R-18");
        builder.setCancelable(true);
        builder.setPositiveButton("我已经开好了", (dialogInterface, i) -> {
            Intent intent = new Intent(mContext, SearchResultActivity.class);
            intent.putExtra("what is the keyword", "R-18");
            mContext.startActivity(intent);
        });
        builder.setNegativeButton("没开", (dialogInterface, i) -> runOnUiThread(() ->
                TastyToast.makeText(MainActivity.this, "你是个好人",
                        TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show()));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.show_my_followed_users: {
                Intent intent = new Intent(mContext, FollowShowActivity.class);
                intent.putExtra("user id", LocalData.getUserID());
                intent.putExtra("user name", LocalData.getUserName());
                mContext.startActivity(intent);
                break;
            }
            case R.id.show_recommend_users: {
                Intent intent = new Intent(MainActivity.this, FollowShowActivity.class);
                intent.putExtra("user id", LocalData.getUserID());
                intent.putExtra("user name", LocalData.getUserName());
                intent.putExtra("show recommend user", true);
                startActivity(intent);
                break;
            }
            case R.id.nav_gallery: {
                //特辑走一波
                Intent intent = new Intent(mContext, SpecialCollectionActivity.class);
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
            case R.id.nav_share:
                createDialog();
                break;
            case R.id.nav_send: {
                Intent intent = new Intent(mContext, AboutActivity.class);
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
        if (LocalData.getLocalDataSet().getString("header_img_path", "").length() != 0 &&
                mImageView.getDrawable() == null) {
            Glide.with(mContext)
                    .load(LocalData.getLocalDataSet().getString("header_img_path", ""))
                    .into(mImageView);
        }
        if (userHead.getDrawable() == null) {
            Glide.with(mContext)
                    .load(new GlideUtil().getHead(LocalData.getLocalDataSet().getString("hearurl", "")))
                    .into(userHead);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Common.showLog("点击了一次");
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
            TastyToast.makeText(MainActivity.this, "再按一次退出~", Toast.LENGTH_SHORT, TastyToast.INFO).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
