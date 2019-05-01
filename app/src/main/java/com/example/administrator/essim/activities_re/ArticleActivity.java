package com.example.administrator.essim.activities_re;

import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.adapters_re.ArticleAdapter;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.network_re.Retro;
import com.example.administrator.essim.response_re.ArticleResponse;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.DensityUtil;
import com.example.administrator.essim.utils.LinearItemDecoration;
import com.example.administrator.essim.utils_re.LocalData;
import com.scwang.smartrefresh.header.DeliveryHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ArticleActivity extends BaseActivity{

    private RecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;
    private ProgressBar mProgressBar;
    private ArticleAdapter mAdapter;
    private List<ArticleResponse.SpotlightArticlesBean> allIllusts = new ArrayList<>();
    private String nextUrl = null;

    @Override
    void initLayout() {
        mLayoutID = R.layout.activity_article;
    }

    @Override
    void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        mRecyclerView = findViewById(R.id.recy_list);
        mProgressBar = findViewById(R.id.progress);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new LinearItemDecoration(DensityUtil.dip2px(mContext, 16.0f)));
        mRecyclerView.setHasFixedSize(true);
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRefreshLayout.setRefreshHeader(new DeliveryHeader(mContext));
        mRefreshLayout.setOnLoadMoreListener(layout -> getNextData());
        mRefreshLayout.setOnRefreshListener(layout -> getFirstData());
    }

    @Override
    void initData() {
        getFirstData();
    }

    @Override
    void getFirstData() {
        Retro.initToken(() -> {
            Retro.getAppApi().getArticles(
                    LocalData.getToken(), "for_android", "all")
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ArticleResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ArticleResponse articleResponse) {
                            if (articleResponse != null && articleResponse.getSpotlight_articles() != null) {
                                allIllusts.clear();
                                allIllusts = articleResponse.getSpotlight_articles();
                                nextUrl = articleResponse.getNext_url();
                                mAdapter = new ArticleAdapter(allIllusts, mContext);
                                mAdapter.setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(@NotNull View view, int position, int viewType) {
                                        Intent intent = new Intent(mContext, WebViewActivity.class);
                                        intent.putExtra("article url", allIllusts.get(position).getArticle_url());
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onItemLongClick(@NotNull View view, int position) {

                                    }
                                });
                                mRecyclerView.setAdapter(mAdapter);
                                mRefreshLayout.finishRefresh(true);
                            } else {
                                Common.showToast(getString(R.string.load_error));
                                mRefreshLayout.finishRefresh(false);
                            }
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mRefreshLayout.finishRefresh(false);
                            mProgressBar.setVisibility(View.INVISIBLE);
                            Common.showToast(getString(R.string.load_error));
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        });
    }

    @Override
    void getNextData() {
        if (nextUrl != null) {
            Retro.initToken(() -> Retro.getAppApi().getNextArticle(LocalData.getToken(), nextUrl)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ArticleResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ArticleResponse articleResponse) {
                            if (articleResponse != null && articleResponse.getSpotlight_articles() != null) {
                                mRefreshLayout.finishLoadMore(true);
                                allIllusts.addAll(articleResponse.getSpotlight_articles());
                                nextUrl = articleResponse.getNext_url();
                                mAdapter.notifyItemRangeChanged(allIllusts.size() - 10, 10);
                            } else {
                                mRefreshLayout.finishLoadMore(false);
                                Common.showToast(getString(R.string.load_error));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            mRefreshLayout.finishLoadMore(false);
                            Common.showToast(getString(R.string.load_error));
                        }

                        @Override
                        public void onComplete() {

                        }
                    }));
        } else {
            mRefreshLayout.finishLoadMore(true);
            Common.showToast(getString(R.string.no_more_data));
        }
    }
}
