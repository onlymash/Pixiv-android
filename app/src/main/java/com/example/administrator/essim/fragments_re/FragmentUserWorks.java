package com.example.administrator.essim.fragments_re;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.response.Reference;
import com.scwang.smartrefresh.header.DeliveryHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;
import com.example.administrator.essim.R;
import com.example.administrator.essim.adapters_re.IllustAdapter;
import com.example.administrator.essim.network_re.Retro;
import com.example.administrator.essim.response_re.IllustListResponse;
import com.example.administrator.essim.response_re.IllustsBean;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.DensityUtil;
import com.example.administrator.essim.utils.GridItemDecoration;
import com.example.administrator.essim.utils_re.LocalData;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentUserWorks extends BaseFragment {

    private int userId;
    private ImageView noData;
    private RecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;
    private ProgressBar mProgressBar;
    private IllustAdapter mAdapter;
    private List<IllustsBean> allIllusts = new ArrayList<>();
    private String nextUrl = null;

    public static FragmentUserWorks newInstance(int userID) {
        Bundle args = new Bundle();
        args.putSerializable("user id", userID);
        FragmentUserWorks fragment = new FragmentUserWorks();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    void initLayout() {
        mLayoutID = R.layout.fragment_illust_list;
    }

    @Override
    View initView(View v) {
        mRecyclerView = v.findViewById(R.id.recy_list);
        mProgressBar = v.findViewById(R.id.progress);
        noData = v.findViewById(R.id.no_data);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new GridItemDecoration(
                2, DensityUtil.dip2px(mContext, 4.0f), false));
        mRecyclerView.setHasFixedSize(true);
        mRefreshLayout = v.findViewById(R.id.refreshLayout);
        mRefreshLayout.setRefreshHeader(new DeliveryHeader(mContext));
        mRefreshLayout.setOnLoadMoreListener(layout -> getNextData());
        mRefreshLayout.setOnRefreshListener(layout -> getFirstData());
        return v;
    }

    @Override
    void initData() {
        userId = (int) getArguments().getSerializable("user id");
        getFirstData();
    }

    @Override
    void getFirstData() {
        Retro.initToken(() ->
                Retro.getAppApi().getUserWorks(
                        LocalData.getToken(), "for_android", userId, null)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<IllustListResponse>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(IllustListResponse userWorkResponse) {
                                if (userWorkResponse != null && userWorkResponse.getIllusts() != null) {
                                    if(userWorkResponse.getIllusts().size() != 0){
                                        allIllusts.clear();
                                        allIllusts = userWorkResponse.getIllusts();
                                        nextUrl = userWorkResponse.getNext_url();
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
                                    }else {
                                        noData.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    Common.showToast(getString(R.string.load_error));
                                    mRefreshLayout.finishRefresh(false);
                                    noData.setVisibility(View.VISIBLE);
                                }
                                mProgressBar.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onError(Throwable e) {
                                mRefreshLayout.finishRefresh(false);
                                mProgressBar.setVisibility(View.INVISIBLE);
                                noData.setVisibility(View.VISIBLE);
                                Common.showToast(getString(R.string.load_error));
                            }

                            @Override
                            public void onComplete() {

                            }
                        }));
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
