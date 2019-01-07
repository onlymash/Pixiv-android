package com.example.administrator.essim.activities_re;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.administrator.essim.R;

public class ThanksActivity extends BaseActivity {

    @Override
    void initLayout() {
        mLayoutID = R.layout.activity_thanks;
    }

    @Override
    void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar_pixiv);
        toolbar.setOnClickListener(view -> finish());

        TextView textView = findViewById(R.id.getDonaterList);
        textView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, RecyclerViewActivity.class);
            intent.putExtra("dataType", "donation");
            mContext.startActivity(intent);
        });
        TextView textView2 = findViewById(R.id.getUsedLibraries);
        textView2.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, RecyclerViewActivity.class);
            intent.putExtra("dataType", "libraries");
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
