package com.example.administrator.essim.activities_re;

import android.content.Intent;
import android.graphics.Color;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.SettingsActivity;
import com.example.administrator.essim.network_re.Retro;
import com.example.administrator.essim.response_re.LoginResponse;
import com.example.administrator.essim.response_re.NewAccountResponse;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils_re.LocalData;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewUserActivity extends BaseActivity {

    private EditText mEditText;
    private ProgressBar mProgressBar;
    private static final String TOKEN = "Bearer l-f9qZ0ZyqSwRyZs8-MymbtWBbSxmCu1pmbOlyisou8";
    private static final String REF = "pixiv_android_app_provisional_account";

    private static final String LOGIN_PARAM_1 = "MOBrBDS8blbauoSck0ZfDbtuzpyT";
    private static final String LOGIN_PARAM_2 = "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj";
    private static final String LOGIN_PARAM_3 = "pixiv";
    private static final boolean LOGIN_PARAM_4 = true;
    private static final String LOGIN_PARAM_5 = "password";

    @Override
    void initLayout() {
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        mLayoutID = R.layout.activity_new_user;
    }

    @Override
    void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());
        mProgressBar = findViewById(R.id.progress);
        mProgressBar.setVisibility(View.INVISIBLE);
        mEditText = findViewById(R.id.login_username);
        CardView cardView = findViewById(R.id.now_login);
        cardView.setOnClickListener(view -> {
            if (mEditText.getText().toString().trim().isEmpty()) {
                Common.showToast(getString(R.string.username_nonnull));
            } else if (!mEditText.getText().toString().trim().isEmpty()) {
                Common.hideKeyboard(mActivity);
                createAccount();
            }
        });
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

    private void createAccount(){
        mProgressBar.setVisibility(View.VISIBLE);
        Retro.getAccountApi().createAccount(TOKEN, mEditText.getText().toString().trim(), REF)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NewAccountResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(NewAccountResponse newAccountResponse) {
                        if (newAccountResponse != null && !newAccountResponse.isError()){
                            Common.showToast(getString(R.string.create_success));
                            LocalData.setDeviceToken(newAccountResponse.getBody().getDevice_token());
                            login(newAccountResponse);
                        }else {
                            Common.showToast(getString(R.string.create_error));
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Common.showToast(getString(R.string.create_error));
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void login(NewAccountResponse newAccountResponse) {
            Retro.getLoginApi().tryLogin(
                    LOGIN_PARAM_1,
                    LOGIN_PARAM_2,
                    newAccountResponse.getBody().getDevice_token(),
                    LOGIN_PARAM_4,
                    LOGIN_PARAM_5,
                    newAccountResponse.getBody().getPassword(),
                    newAccountResponse.getBody().getUser_account())
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
                                LocalData.saveLocalMessage(loginResponse, newAccountResponse.getBody().getPassword());
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setting:
                Intent intent = new Intent(mContext, SettingsActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
