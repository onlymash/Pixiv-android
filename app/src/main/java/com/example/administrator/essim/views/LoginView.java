package com.example.administrator.essim.views;

public interface LoginView extends BaseView{

    void showProgress();

    void hideProgress();

    String getUserName();

    String getUserPwd();

    void setUserName(String name);

    void setPwd(String pwd);

    void loginSuccess();
}
