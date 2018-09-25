package com.example.administrator.essim.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.adapters.DonaterAdapter;
import com.example.administrator.essim.utils.Constant;
import com.example.administrator.essim.utils.DensityUtil;
import com.example.administrator.essim.utils.LinearItemDecoration;

public class ThanksActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks);
        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar_pixiv);
        toolbar.setOnClickListener(view -> finish());

        TextView textView = findViewById(R.id.getDonaterList);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AboutDonationActivity.class);
                intent.putExtra("dataType", "donation");
                mContext.startActivity(intent);
            }
        });
        TextView textView2 = findViewById(R.id.getUsedLibraries);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AboutDonationActivity.class);
                intent.putExtra("dataType", "libraries");
                mContext.startActivity(intent);
            }
        });
    }
}
