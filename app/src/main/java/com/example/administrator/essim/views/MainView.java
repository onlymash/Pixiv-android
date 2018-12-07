package com.example.administrator.essim.views;

import android.app.Activity;

public interface MainView extends BaseView{

    void showProgress();

    void hideProgress();

    void setNowPressure(String nowOnline);
}
