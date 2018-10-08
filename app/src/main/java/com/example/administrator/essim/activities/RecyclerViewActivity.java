package com.example.administrator.essim.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.adapters.DonaterAdapter;
import com.example.administrator.essim.adapters.ViewHistoryAdapter;
import com.example.administrator.essim.response.ViewHistory;
import com.example.administrator.essim.utils.Constant;
import com.example.administrator.essim.utils.DensityUtil;
import com.example.administrator.essim.utils.LinearItemDecoration;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_donation);
        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar_pixiv);
        toolbar.setOnClickListener(view -> finish());
        RecyclerView recyclerView = findViewById(R.id.donate_recy);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new LinearItemDecoration(DensityUtil.dip2px(mContext, 16.0f)));
        String dataType = getIntent().getStringExtra("dataType");
        DonaterAdapter adapter = null;
        if (dataType.equals("donation")) {
            adapter = new DonaterAdapter(Constant.donaterList, mContext);
            toolbar.setTitle("神豪列表");
            recyclerView.setAdapter(adapter);
        } else if (dataType.equals("libraries")) {
            adapter = new DonaterAdapter(Constant.libraries, mContext);
            toolbar.setTitle("开源库列表");
            recyclerView.setAdapter(adapter);
        }else if (dataType.equals("history")) {
            List<ViewHistory> list = new ArrayList<>();
            list.addAll(DataSupport.findAll(ViewHistory.class));
            ViewHistoryAdapter viewHistoryAdapter = new ViewHistoryAdapter(list, mContext);
            toolbar.setTitle("浏览历史");
            recyclerView.setAdapter(viewHistoryAdapter);
        }

    }
}
