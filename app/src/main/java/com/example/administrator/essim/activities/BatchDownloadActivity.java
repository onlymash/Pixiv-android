package com.example.administrator.essim.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.administrator.essim.R;
import com.example.administrator.essim.adapters.BatchSelectAdapter;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.response.Reference;
import com.example.administrator.essim.response_re.IllustsBean;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.DensityUtil;
import com.example.administrator.essim.utils.GridItemDecoration;
import com.example.administrator.essim.utils_re.LocalData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class BatchDownloadActivity extends BaseActivity {

    private Context mContext;
    private int startPosition;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_download);
        mContext = this;
        mToolbar = findViewById(R.id.mToolbar);
        int scrollLength = getIntent().getIntExtra("scroll length", 0);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(view -> finish());
        startPosition = getIntent().getIntExtra("scroll dist", 0);
        mToolbar.setTitle("选择了0个项目");
        Reference.downloadList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.select_recy);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new GridItemDecoration(
                2, DensityUtil.dip2px(mContext, 8.0f), true));
        BatchSelectAdapter adapter = new BatchSelectAdapter(Reference.sIllustsBeans, mContext);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NotNull View view, int position, int viewType) {
                if (viewType == 0) {
                    Reference.downloadList.remove(Reference.sIllustsBeans.get(position));
                } else {
                    Reference.downloadList.add(Reference.sIllustsBeans.get(position));
                }
                mToolbar.setTitle("选择了" + Reference.downloadList.size() + "个项目");
            }

            @Override
            public void onItemLongClick(@NotNull View view, int position) {

            }
        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.scrollToPosition(startPosition);
        Common.showLog(scrollLength);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (IllustsBean illustsBean : Reference.downloadList) {
            illustsBean.setSelected(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.batch_download, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_select_all:
                for (int i = 0; i < Reference.sIllustsBeans.size(); i++) {
                    if (!Reference.sIllustsBeans.get(i).isSelected()) {
                        Reference.sIllustsBeans.get(i).setSelected(true);
                        Reference.downloadList.add(Reference.sIllustsBeans.get(i));
                    }
                }
                mRecyclerView.getAdapter().notifyDataSetChanged();
                mToolbar.setTitle("选择了" + Reference.downloadList.size() + "个项目");
                break;
            case R.id.action_not_select:
                for (int i = 0; i < Reference.sIllustsBeans.size(); i++) {
                    if (Reference.sIllustsBeans.get(i).isSelected()) {
                        Reference.sIllustsBeans.get(i).setSelected(false);
                    }
                }
                mRecyclerView.getAdapter().notifyDataSetChanged();
                Reference.downloadList.clear();
                mToolbar.setTitle("选择了" + Reference.downloadList.size() + "个项目");
                break;
            case R.id.action_start:
                if (Reference.downloadList.size() == 0) {
                    Common.showToast("你要下个什么？！");
                } else {
                    if (LocalData.getDownloadPath().contains("emulated")) {
                        Common.showToast("开始下载了");
                        Intent intentService = new Intent(this, BatchDownloadService.class);
                        startService(intentService);
                    } else {
                        Snackbar.make(mRecyclerView, "暂时不支持下载到外置SD卡~", Snackbar.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
