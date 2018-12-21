package com.example.administrator.essim.activities_re;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.adapters_re.IllustAdapter;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.network_re.Retro;
import com.example.administrator.essim.response.Reference;
import com.example.administrator.essim.response_re.IllustListResponse;
import com.example.administrator.essim.response_re.IllustsBean;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.DensityUtil;
import com.example.administrator.essim.utils.GridItemDecoration;
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

public class IllustListActivity extends BaseActivity{

    private RecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;
    private ProgressBar mProgressBar;
    private IllustAdapter mAdapter;
    private List<IllustsBean> allIllusts = new ArrayList<>();
    private String nextUrl = null;

    @Override
    void initLayout() {
        mLayoutID = R.layout.activity_illust_list;
    }

    @Override
    void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        mRecyclerView = findViewById(R.id.recy_list);
        mProgressBar = findViewById(R.id.progress);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new GridItemDecoration(
                2, DensityUtil.dip2px(mContext, 4.0f), false));
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
            Retro.getAppApi().getNewWorks(
                    LocalData.getToken(), "all")
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<IllustListResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(IllustListResponse followNewResponse) {
                            if (followNewResponse != null && followNewResponse.getIllusts() != null) {
                                allIllusts.clear();
                                allIllusts = followNewResponse.getIllusts();
                                nextUrl = followNewResponse.getNext_url();
                                mAdapter = new IllustAdapter(allIllusts, mContext);
                                mAdapter.setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(@NotNull View view, int position, int viewType) {
                                        if (viewType == 0){
                                            Reference.sIllustsBeans = allIllusts;
                                            Intent intent = new Intent(mContext, ViewPagerActivity.class);
                                            intent.putExtra("which one is selected", position);
                                            mContext.startActivity(intent);
                                        }
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
            Retro.initToken(() -> Retro.getAppApi().getNext(LocalData.getToken(), nextUrl)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<IllustListResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(IllustListResponse recmdIllustResponse) {
                            if (recmdIllustResponse != null && recmdIllustResponse.getIllusts() != null) {
                                mRefreshLayout.finishLoadMore(true);
                                allIllusts.addAll(recmdIllustResponse.getIllusts());
                                nextUrl = recmdIllustResponse.getNext_url();
                                mAdapter.notifyItemRangeChanged(allIllusts.size() - 30, 30);
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
