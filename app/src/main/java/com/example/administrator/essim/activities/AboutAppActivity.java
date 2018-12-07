package com.example.administrator.essim.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.administrator.essim.R;

public class AboutAppActivity extends MvpBaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    @Override
    void initLayout() {
        mLayoutID = R.layout.activity_about;
    }

    @Override
    void initView() {

    }

    @Override
    void initData() {

    }
}
