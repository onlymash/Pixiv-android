package com.example.administrator.essim.activities_re;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext;
    protected Activity mActivity;
    protected int mLayoutID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        setContentView(mLayoutID);

        mContext = this;
        mActivity = this;

        initView();
        initData();
    }

    abstract void initLayout();

    abstract void initView();

    abstract void initData();

    abstract void getFirstData();

    abstract void getNextData();
}
