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
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.adapters.PixivAdapterGrid;
import com.example.administrator.essim.fragments.FragmentDialog;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.response.Reference;
import com.example.administrator.essim.response.RelatedIllust;
import com.example.administrator.essim.response_re.IllustsBean;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.DensityUtil;
import com.example.administrator.essim.utils.GridItemDecoration;
import com.example.administrator.essim.utils.PixivOperate;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RelatedActivity extends BaseActivity {

    private Context mContext;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private PixivAdapterGrid mPixivAdapter;
    private RelatedIllust mRelatedIllust;
    private GridLayoutManager mGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_related);

        mContext = this;
        Intent intent = getIntent();
        mRelatedIllust = (RelatedIllust) intent.getSerializableExtra("illust set");
        String illustTitle = intent.getStringExtra("illust title");

        initView(illustTitle);
        initAdapter(mRelatedIllust.illusts);
    }

    private void initView(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(title + "的相关作品");
        mProgressBar = findViewById(R.id.try_login);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView = findViewById(R.id.pixiv_recy);
        toolbar.setNavigationOnClickListener(view -> finish());
        mGridLayoutManager = new GridLayoutManager(mContext, 2);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mPixivAdapter.getItemViewType(position) == 2) {
                    return mGridLayoutManager.getSpanCount();
                } else {
                    return 1;
                }
            }
        });
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new GridItemDecoration(
                2, DensityUtil.dip2px(mContext, 8.0f), true));
    }

    private void initAdapter(List<IllustsBean> illustsBeans) {
        mPixivAdapter = new PixivAdapterGrid(illustsBeans, mContext);
        mPixivAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NotNull View view, int position, int viewType) {
                if (position == -1) {
                    Snackbar.make(mProgressBar, "再怎么找也找不到了~", Snackbar.LENGTH_SHORT).show();
                } else if (viewType == 0) {
                    Reference.sIllustsBeans = illustsBeans;
                    Intent intent = new Intent(mContext, ViewPagerActivity.class);
                    intent.putExtra("which one is selected", position);
                    mContext.startActivity(intent);
                } else if (viewType == 1) {
                    if (!illustsBeans.get(position).isIs_bookmarked()) {
                        ((ImageView) view).setImageResource(R.drawable.ic_favorite_white_24dp);
                        view.startAnimation(Common.getAnimation());
                        illustsBeans.get(position).setIs_bookmarked(true);
                        PixivOperate.postStarIllust(illustsBeans.get(position).getId(), mContext, "public");
                    } else {
                        ((ImageView) view).setImageResource(R.drawable.no_favor);
                        view.startAnimation(Common.getAnimation());
                        illustsBeans.get(position).setIs_bookmarked(false);
                        PixivOperate.postUnstarIllust(illustsBeans.get(position).getId(), mContext);
                    }
                }
            }

            @Override
            public void onItemLongClick(@NotNull View view, int position) {
                new FragmentDialog(mContext, view, illustsBeans.get(position)).showDialog();
            }
        });
        mRecyclerView.setAdapter(mPixivAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPixivAdapter != null) {
            mPixivAdapter.notifyDataSetChanged();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.download, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_download) {
            Reference.sIllustsBeans = mRelatedIllust.illusts;
            Intent intent = new Intent(mContext, BatchDownloadActivity.class);
            intent.putExtra("scroll dist", mGridLayoutManager.findFirstCompletelyVisibleItemPosition());
            mContext.startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
