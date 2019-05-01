package com.example.administrator.essim.activities_re;

import android.content.Intent;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.SearchActivity;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.adapters_re.IllustAdapter;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.response_re.IllustListResponse;
import com.example.administrator.essim.utils.DensityUtil;
import com.example.administrator.essim.utils.GridItemDecoration;

import org.jetbrains.annotations.NotNull;

public class RelatedActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private IllustListResponse mRelatedIllust;
    private GridLayoutManager mGridLayoutManager;
    private Toolbar mToolbar;

    @Override
    void initLayout() {
        mLayoutID = R.layout.activity_related;
    }

    @Override
    void initView() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(v -> finish());
        mRecyclerView = findViewById(R.id.recy_list);
        mGridLayoutManager = new GridLayoutManager(mContext, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.addItemDecoration(new GridItemDecoration(
                2, DensityUtil.dip2px(mContext, 4.0f), false));
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    void initData() {
        Intent intent = getIntent();
        mRelatedIllust = (IllustListResponse) intent.getSerializableExtra("illust set");
        String illustTitle = intent.getStringExtra("illust title");
        mToolbar.setTitle(illustTitle + "的相关作品");
        IllustAdapter adapter = new IllustAdapter(mRelatedIllust.getIllusts(), mContext);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NotNull View view, int position, int viewType) {
                if (viewType == 0){
                    PixivApp.sIllustsBeans = mRelatedIllust.getIllusts();
                    Intent intent = new Intent(mContext, ViewPagerActivity.class);
                    intent.putExtra("which one is selected", position);
                    mContext.startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(@NotNull View view, int position) {

            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    void getFirstData() {

    }

    @Override
    void getNextData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_pixiv, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            Intent intent = new Intent(mContext, SearchActivity.class);
            startActivity(intent);
        }else if (item.getItemId() == R.id.action_download) {
            PixivApp.sIllustsBeans = mRelatedIllust.getIllusts();
            Intent intent = new Intent(mContext, BatchDownloadActivity.class);
            intent.putExtra("scroll dist", mGridLayoutManager.findFirstCompletelyVisibleItemPosition());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
