package com.example.administrator.essim.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.MainActivity;
import com.example.administrator.essim.interf.LoadListener;
import com.example.administrator.essim.models.LoginModel;
import com.example.administrator.essim.network.OAuthSecureService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.PixivOAuthResponse;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.Constant;
import com.example.administrator.essim.utils.LocalData;
import com.example.administrator.essim.views.LoginView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginPresenter implements BasePresenter {

    private LoginView mView;
    private Context mContext;
    private LoginModel mModel;

    public LoginPresenter(LoginView view) {
        mView = view;
        mContext = mView.getSelfContext();
        mModel = new LoginModel();
    }

    @Override
    public void start() {

    }

    @Override
    public void end() {
        mContext = null;
        mView = null;
    }

    public void login() {
        if (checkInput()) {
            mView.showProgress();
            mModel.setUserName(mView.getUserName());
            mModel.setPassword(mView.getUserPwd());
            mModel.getData(new LoadListener<PixivOAuthResponse>() {
                @Override
                public void onSuccess(PixivOAuthResponse response) {
                    mView.hideProgress();
                    mView.loginSuccess();
                }

                @Override
                public void onFailed() {
                    mView.hideProgress();
                    Common.showToast(mContext, "登陆失败");
                }
            });
        }
    }

    private boolean checkInput() {
        if (mView.getUserName().length() == 0) {
            Common.showToast(mContext.getString(R.string.username_no_null));
            return false;
        } else if (mView.getUserPwd().length() == 0) {
            Common.showToast(mContext.getString(R.string.input_pwd));
            return false;
        } else {
            return true;
        }
    }

    /**
     * 填写已经保存的信息
     */
    public void setDefaultData(){
        if (LocalData.getUserName().length() != 0 && LocalData.getUserPwd().length() != 0) {
            mView.setUserName(LocalData.getUserAccount());
            mView.setPwd(LocalData.getUserPwd());
        }
    }
}
