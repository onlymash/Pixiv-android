package com.example.administrator.essim.activities_re;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.MainActivity;
import com.example.administrator.essim.adapters_re.LoginBgAdapter;
import com.example.administrator.essim.network_re.Retro;
import com.example.administrator.essim.response_re.IllustListResponse;
import com.example.administrator.essim.response_re.LoginResponse;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.DensityUtil;
import com.example.administrator.essim.utils.GridItemDecoration;
import com.example.administrator.essim.utils_re.AutoScrollRecyclerView;
import com.example.administrator.essim.utils_re.LocalData;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {

    private static final String LOGIN_PARAM_1 = "MOBrBDS8blbauoSck0ZfDbtuzpyT";
    private static final String LOGIN_PARAM_2 = "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj";
    private static final String LOGIN_PARAM_3 = "pixiv";
    private static final boolean LOGIN_PARAM_4 = true;
    private static final String LOGIN_PARAM_5 = "password";

    private AutoScrollRecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private EditText userName, password;

    @Override
    void initLayout() {
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        mLayoutID = R.layout.activity_login_re;
    }

    @Override
    void initView() {
        mProgressBar = findViewById(R.id.progress);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView = findViewById(R.id.recy_list);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new GridItemDecoration(
                2, DensityUtil.dip2px(mContext, 4.0f), false));
        mRecyclerView.setHasFixedSize(true);
        userName = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        CardView loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(v -> login());
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    void initData() {
        getLoginBg();
    }

    @Override
    void getFirstData() {

    }

    @Override
    void getNextData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mRecyclerView.getAdapter() != null) {
            /**
             * 继续滚动背景图
             */
            mRecyclerView.setPointTouch(false);
            mRecyclerView.setLoopEnabled(true);
            mRecyclerView.openAutoScroll();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mRecyclerView.getAdapter() != null) {
            /**
             * 停止滚动背景图
             */
            mRecyclerView.setPointTouch(true);
        }
    }

    private void getLoginBg() {
        Retro.getAppApi().getLoginBg()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<IllustListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(IllustListResponse loginBgResponse) {
                        if (loginBgResponse != null && loginBgResponse.getIllusts() != null) {
                            LoginBgAdapter adapter = new LoginBgAdapter(loginBgResponse.getIllusts(), mContext);
                            mRecyclerView.setAdapter(adapter);
                            mRecyclerView.setLoopEnabled(true);
                            mRecyclerView.openAutoScroll();
                        } else {
                            Common.showToast(getString(R.string.load_error));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Common.showToast(getString(R.string.load_error));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void login() {
        if (checkInput()) {
            mProgressBar.setVisibility(View.VISIBLE);
            Retro.getLoginApi().tryLogin(
                    LOGIN_PARAM_1,
                    LOGIN_PARAM_2,
                    LOGIN_PARAM_3,
                    LOGIN_PARAM_4,
                    LOGIN_PARAM_5,
                    password.getText().toString(),
                    userName.getText().toString())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<LoginResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(LoginResponse loginResponse) {
                            if (loginResponse != null && loginResponse.getResponse() != null) {
                                mProgressBar.setVisibility(View.INVISIBLE);
                                LocalData.saveLocalMessage(loginResponse, password.getText().toString());
                                Intent intent = new Intent(mContext, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                mProgressBar.setVisibility(View.INVISIBLE);
                                Common.showToast(mContext.getString(R.string.login_error));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            mProgressBar.setVisibility(View.INVISIBLE);
                            Common.showToast(mContext.getString(R.string.login_error));
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    private boolean checkInput() {
        if (userName.getText().toString().length() == 0) {
            Common.showToast(mContext.getString(R.string.username_no_null));
            return false;
        } else if (password.getText().toString().length() == 0) {
            Common.showToast(mContext.getString(R.string.input_pwd));
            return false;
        } else {
            return true;
        }
    }
}
