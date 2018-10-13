package com.example.administrator.essim.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.adapters.PixivAdapterGrid;
import com.example.administrator.essim.fragments.FragmentDialog;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.network.AppApiPixivService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.IllustsBean;
import com.example.administrator.essim.response.RecommendResponse;
import com.example.administrator.essim.response.Reference;
import com.example.administrator.essim.response.SearchIllustResponse;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.DensityUtil;
import com.example.administrator.essim.utils.GridItemDecoration;
import com.example.administrator.essim.utils.LocalData;
import com.example.administrator.essim.utils.PixivOperate;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class SearchResultActivity extends BaseActivity {

    private static final String[] sort = {"popular_desc", "date_desc"};
    private static final String[] arrayOfSearchType = {" 500users入り", " 1000users入り",
            " 2000users入り"," 5000users入り"," 7500users入り", " 10000users入り"," 50000users入り", "不筛选"};
    public String ketWords;
    private String temp;
    private String next_url;
    private Context mContext;
    private boolean isBestSort;
    private boolean isLoadingMore;
    private ProgressBar mProgressBar;
    private ImageView mImageView;
    private RecyclerView mRecyclerView;
    private PixivAdapterGrid mPixivAdapter;
    private int nowSearchType = 7, togo = 7;
    private List<IllustsBean> mIllustsBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tag);

        mContext = this;
        Intent intent = getIntent();
        ketWords = intent.getStringExtra("what is the keyword");

        initView();
        getData(sort[1], "");
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar_pixiv);
        toolbar.setTitle(ketWords);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());
        mProgressBar = findViewById(R.id.try_login);
        mProgressBar.setVisibility(View.INVISIBLE);
        mImageView = findViewById(R.id.no_data);
        mRecyclerView = findViewById(R.id.pixiv_recy);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = mPixivAdapter.getItemCount();
                if (lastVisibleItem >= totalItemCount - 4 && dy > 0 && !isLoadingMore) {
                    getNextData();
                    isLoadingMore = true;
                }
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new GridItemDecoration(
                2, DensityUtil.dip2px(mContext, 8.0f), true));
        AlphaAnimation alphaAnimationShowIcon = new AlphaAnimation(0.2f, 1.0f);
        alphaAnimationShowIcon.setDuration(500);
    }

    private void getData(String rankType, String usersyori) {
        isBestSort = rankType.equals(sort[0]);
        mProgressBar.setVisibility(View.VISIBLE);
        mImageView.setVisibility(View.INVISIBLE);
        Call<SearchIllustResponse> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .getSearchIllust(ketWords + usersyori,
                        rankType, "partial_match_for_tags", null, null,
                        LocalData.getToken());
        call.enqueue(new Callback<SearchIllustResponse>() {
            @Override
            public void onResponse(Call<SearchIllustResponse> call, retrofit2.Response<SearchIllustResponse> response) {
                if (response.body() != null) {
                    if(response.body().getIllusts().size() > 0) {
                        next_url = response.body().getNext_url();
                        mIllustsBeanList.clear();
                        mIllustsBeanList.addAll(response.body().getIllusts());
                        mPixivAdapter = new PixivAdapterGrid(mIllustsBeanList, mContext);
                        mPixivAdapter.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(@NotNull View view, int position, int viewType) {
                                if (viewType == 0) {
                                    Reference.sIllustsBeans = mIllustsBeanList;
                                    Intent intent = new Intent(mContext, ViewPagerActivity.class);
                                    intent.putExtra("which one is selected", position);
                                    mContext.startActivity(intent);
                                } else if (viewType == 1) {
                                    if (!mIllustsBeanList.get(position).isIs_bookmarked()) {
                                        ((ImageView) view).setImageResource(R.drawable.ic_favorite_white_24dp);
                                        view.startAnimation(Common.getAnimation());
                                        mIllustsBeanList.get(position).setIs_bookmarked(true);
                                        PixivOperate.postStarIllust(mIllustsBeanList.get(position).getId(), mContext, "public");
                                    } else {
                                        ((ImageView) view).setImageResource(R.drawable.no_favor);
                                        view.startAnimation(Common.getAnimation());
                                        mIllustsBeanList.get(position).setIs_bookmarked(false);
                                        PixivOperate.postUnstarIllust(mIllustsBeanList.get(position).getId(), mContext);
                                    }
                                }
                            }

                            @Override
                            public void onItemLongClick(@NotNull View view, int position) {
                                new FragmentDialog(mContext, view, mIllustsBeanList.get(position)).showDialog();
                            }
                        });
                        mRecyclerView.setAdapter(mPixivAdapter);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mImageView.setVisibility(View.INVISIBLE);
                    }
                    else {
                        mRecyclerView.setVisibility(View.INVISIBLE);
                        mImageView.setVisibility(View.VISIBLE);
                    }
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<SearchIllustResponse> call, Throwable throwable) {
                mRecyclerView.setVisibility(View.INVISIBLE);
                mImageView.setVisibility(View.VISIBLE);
                Common.showToast(mContext, getString(R.string.no_proxy));
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void getNextData() {
        if (next_url != null) {
            mProgressBar.setVisibility(View.VISIBLE);
            Call<RecommendResponse> call = new RestClient()
                    .getRetrofit_AppAPI()
                    .create(AppApiPixivService.class)
                    .getNext(LocalData.getToken(), next_url);
            call.enqueue(new Callback<RecommendResponse>() {
                @Override
                public void onResponse(Call<RecommendResponse> call, retrofit2.Response<RecommendResponse> response) {
                    if (response.body() != null) {
                        next_url = response.body().getNext_url();
                        mIllustsBeanList.addAll(response.body().getIllusts());
                        mPixivAdapter.notifyDataSetChanged();
                        isLoadingMore = false;
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<RecommendResponse> call, Throwable throwable) {
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    mImageView.setVisibility(View.VISIBLE);
                    Common.showToast(mContext, getString(R.string.no_proxy));
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            });
        } else {
            Snackbar.make(mProgressBar, getString(R.string.no_more_data), Snackbar.LENGTH_SHORT).show();
        }
    }

    private void createSearchTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setIcon(R.mipmap.logo);
        builder.setTitle("筛选结果：");
        builder.setCancelable(true);
        builder.setSingleChoiceItems(arrayOfSearchType, nowSearchType,
                (dialogInterface, i) -> {
                    if (i != 4) {
                        temp = arrayOfSearchType[i];
                    }
                    togo = i;
                });
        builder.setPositiveButton("确定", (dialogInterface, i) -> {
            if (nowSearchType != togo) {
                nowSearchType = togo;
                if (togo != 4) {
                    getData(sort[1], arrayOfSearchType[nowSearchType]);
                } else {
                    getData(sort[1], "");
                }
            }
        })
                .setNegativeButton("取消", (dialogInterface, i) -> {
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tag_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_change_search) {
            createSearchTypeDialog();
            return true;
        } else if (id == R.id.action_get_hot) {
            if (LocalData.getIsVIP()) {
                if (!isBestSort) {
                    getData(sort[0], "");
                }
            } else {
                Snackbar.make(mRecyclerView, "只有会员才能按热度排序", Snackbar.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPixivAdapter != null) {
            mPixivAdapter.notifyDataSetChanged();
        }
    }
}
