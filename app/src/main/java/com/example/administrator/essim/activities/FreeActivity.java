package com.example.administrator.essim.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.utils.Common;

public class FreeActivity extends BaseActivity {

    private Context mContext;
    private TextView mTextView, mTextView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free);

        mContext = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());
        mTextView = findViewById(R.id.key);

        mTextView.setOnLongClickListener(view -> {
            Common.copyMessage(mContext, mTextView.getText().toString());
            return true;
        });

        mTextView2 = findViewById(R.id.help);

        mTextView2.setOnLongClickListener(view -> {
            Common.copyMessage(mContext, mTextView2.getText().toString());
            return true;
        });
    }
}
