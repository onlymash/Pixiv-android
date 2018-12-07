package com.example.administrator.essim.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.network.OAuthSecureService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.presenter.LoginPresenter;
import com.example.administrator.essim.response.PixivOAuthResponse;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.Constant;
import com.example.administrator.essim.utils.LocalData;
import com.example.administrator.essim.views.LoginView;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends MvpBaseActivity<LoginPresenter> implements LoginView {

    private EditText userName, password;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    @Override
    void initLayout() {
        mLayoutID = R.layout.activity_try_to_login;
    }

    @Override
    void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());
        mProgressBar = findViewById(R.id.try_login);
        mProgressBar.setVisibility(View.INVISIBLE);
        userName = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        TextView newUser = findViewById(R.id.new_user);
        newUser.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, NewUserActivity.class);
            startActivity(intent);
            finish();
        });
        CardView nowLogin = findViewById(R.id.login);
        nowLogin.setOnClickListener(view -> mPresenter.login());
    }


    @Override
    void initData() {
        mPresenter = new LoginPresenter(this);
        mPresenter.setDefaultData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    /**
     * 让未登录的人也可以去设置页面
     *
     * @param item 设置走一波
     * @return super
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_setting) {
            Intent intent = new Intent(mContext, SettingsActivity.class);
            mContext.startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public String getUserName() {
        return userName.getText().toString().trim();
    }

    @Override
    public String getUserPwd() {
        return password.getText().toString().trim();
    }

    @Override
    public void setUserName(String name) {
        userName.setText(name);
    }

    @Override
    public void setPwd(String pwd) {
        password.setText(pwd);
    }

    @Override
    public void loginSuccess() {
        Intent intent = new Intent(mContext, MainActivity.class);
        mContext.startActivity(intent);
        finish();
    }
}