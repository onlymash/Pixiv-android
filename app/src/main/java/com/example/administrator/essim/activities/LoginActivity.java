package com.example.administrator.essim.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.network.OAuthSecureService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.PixivOAuthResponse;
import com.example.administrator.essim.utils.Common;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private EditText mEditText, mEditText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(R.layout.activity_try_to_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());
        mProgressBar = findViewById(R.id.try_login);
        mProgressBar.setVisibility(View.INVISIBLE);
        mEditText = findViewById(R.id.login_username);
        mEditText2 = findViewById(R.id.login_password);
        TextView textView = findViewById(R.id.new_user);
        textView.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, NewUserActivity.class);
            startActivity(intent);
            finish();
        });
        CardView cardView = findViewById(R.id.card_login);
        cardView.setOnClickListener(view -> {
            if (mEditText.getText().toString().trim().isEmpty()) {
                Snackbar.make(view, "用户名不能为空", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else if (!mEditText.getText().toString().trim().isEmpty() && mEditText2.getText().toString().trim().isEmpty()) {
                Snackbar.make(view, "请输入密码", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else if (!mEditText.getText().toString().trim().isEmpty() && !mEditText2.getText().toString().trim().isEmpty()) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                if (imm.isActive() && getCurrentFocus() != null) {
                    if (getCurrentFocus().getWindowToken() != null) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                tryToLogin();
                mProgressBar.setVisibility(View.VISIBLE);
            }
        });

        if (Common.getLocalDataSet().getString("username", "").length() != 0) {
            mEditText.setText(Common.getLocalDataSet().getString("useraccount", ""));
            mEditText2.setText(Common.getLocalDataSet().getString("password", ""));
        }
    }


    private void tryToLogin() {
        HashMap<String, String> localHashMap = new HashMap<>();
        localHashMap.put("client_id", "MOBrBDS8blbauoSck0ZfDbtuzpyT");
        localHashMap.put("client_secret", "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj");
        localHashMap.put("grant_type", "password");
        localHashMap.put("username", mEditText.getText().toString().trim());
        localHashMap.put("password", mEditText2.getText().toString().trim());
        Call<PixivOAuthResponse> call = new RestClient().getretrofit_OAuthSecure().create(OAuthSecureService.class).postAuthToken(localHashMap);
        call.enqueue(new Callback<PixivOAuthResponse>() {
            @Override
            public void onResponse(Call<PixivOAuthResponse> call, retrofit2.Response<PixivOAuthResponse> response) {
                PixivOAuthResponse pixivOAuthResponse = response.body();
                if (pixivOAuthResponse != null) {
                    //将登陆后的账号信息保存到本地
                    Common.saveLocalMessage(pixivOAuthResponse, mEditText2.getText().toString().trim());
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Snackbar.make(mProgressBar, "您的账户或密码有误", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PixivOAuthResponse> call, @NonNull Throwable throwable) {
            }
        });
    }
}
