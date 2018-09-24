package com.example.administrator.essim.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.network.OAuthSecureService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.PixivOAuthResponse;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.Constant;
import com.example.administrator.essim.utils.LocalData;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends BaseActivity {

    private EditText mEditText, mEditText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(R.layout.activity_try_to_login);
        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());
        mProgressBar = findViewById(R.id.try_login);
        mProgressBar.setVisibility(View.INVISIBLE);
        mEditText = findViewById(R.id.login_username);
        mEditText2 = findViewById(R.id.login_password);
        TextView textView = findViewById(R.id.new_user);
        TextView textView2 = findViewById(R.id.study_now);
        textView2.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, FreeActivity.class);
            mContext.startActivity(intent);
        });
        textView.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, NewUserActivity.class);
            startActivity(intent);
            finish();
        });
        CardView cardView = findViewById(R.id.card_login);
        cardView.setOnClickListener(view -> {
            if (mEditText.getText().toString().trim().isEmpty()) {
                Snackbar.make(view, R.string.username_no_null, Snackbar.LENGTH_LONG).show();
            } else if (!mEditText.getText().toString().trim().isEmpty() && mEditText2.getText().toString().trim().isEmpty()) {
                Snackbar.make(view, R.string.input_pwd, Snackbar.LENGTH_LONG).show();
            } else if (!mEditText.getText().toString().trim().isEmpty() && !mEditText2.getText().toString().trim().isEmpty()) {
                Common.hideKeyboard(mActivity);
                tryToLogin();
            }
        });

        if (LocalData.getUserName().length() != 0) {
            mEditText.setText(LocalData.getUserAccount());
            mEditText2.setText(LocalData.getUserPwd());
        }
    }


    /**
     * 登陆一下呗
     */
    private void tryToLogin() {
        mProgressBar.setVisibility(View.VISIBLE);
        HashMap<String, String> localHashMap = new HashMap<>();
        localHashMap.put(getString(R.string.client_id), Constant.CLIENT_ID);
        localHashMap.put(getString(R.string.client_secret), Constant.CLIENT_SECRET);
        localHashMap.put(getString(R.string.grant_type), Constant.GRANT_TYPE);
        localHashMap.put(getString(R.string.user_name), mEditText.getText().toString().trim());
        localHashMap.put(getString(R.string.pass_word), mEditText2.getText().toString().trim());
        Call<PixivOAuthResponse> call = new RestClient().getretrofit_OAuthSecure().create(OAuthSecureService.class).postAuthToken(localHashMap);
        call.enqueue(new Callback<PixivOAuthResponse>() {
            @Override
            public void onResponse(Call<PixivOAuthResponse> call, retrofit2.Response<PixivOAuthResponse> response) {
                PixivOAuthResponse pixivOAuthResponse = response.body();
                if (pixivOAuthResponse != null) {
                    //将登陆后的账号信息保存到本地
                    LocalData.saveLocalMessage(pixivOAuthResponse, mEditText2.getText().toString().trim());
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Snackbar.make(mProgressBar, R.string.account_error, Snackbar.LENGTH_SHORT).show();
                }
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(@NonNull Call<PixivOAuthResponse> call, @NonNull Throwable throwable) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Common.showToast(mContext, getString(R.string.no_proxy));
            }
        });
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
        int id = item.getItemId();
        if (id == R.id.action_setting) {
            Intent intent = new Intent(mContext, SettingsActivity.class);
            mContext.startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
