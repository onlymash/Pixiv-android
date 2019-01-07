package com.example.administrator.essim.activities_re;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.essim.R;

public class AboutAppActivity extends BaseActivity {

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
        TextView textView = findViewById(R.id.be_free);
        textView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, FreeActivity.class);
            mContext.startActivity(intent);
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
}
