package com.example.administrator.essim.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.essim.R;
import com.example.administrator.essim.presenter.BasePresenter;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.views.BaseView;

public abstract class MvpBaseActivity<T extends BasePresenter> extends AppCompatActivity implements BaseView {

    private static final int DEFAULT_LAYOUT_ID = -1;

    protected Context mContext;
    protected Activity mActivity;
    protected T mPresenter;
    protected int mLayoutID = DEFAULT_LAYOUT_ID;

    abstract void initLayout();

    abstract void initView();

    abstract void initData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        mActivity = this;

        initLayout();
        if (mLayoutID != DEFAULT_LAYOUT_ID) {
            setContentView(mLayoutID);
        } else {
            Common.showToast(getString(R.string.wrong_layout_id));
        }
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        if(mPresenter != null){
            mPresenter.end();
            mPresenter = null;
        }
        super.onDestroy();
    }

    @Override
    public Context getSelfContext() {
        return mContext;
    }
}

