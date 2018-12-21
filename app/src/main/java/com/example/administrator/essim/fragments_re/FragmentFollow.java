package com.example.administrator.essim.fragments_re;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities_re.PixivApp;
import com.example.administrator.essim.activities_re.UserDetailActivity;
import com.example.administrator.essim.adapters_re.UserFollowAdapter;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.network_re.Retro;
import com.example.administrator.essim.response_re.FollowResponse;
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

public class FragmentFollow extends BaseFragment {

    private RecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;
    private ProgressBar mProgressBar;
    private UserFollowAdapter mAdapter;
    private List<FollowResponse.UserPreviewsBean> allIllusts = new ArrayList<>();
    private String nextUrl = null, dataType = "";
    private static final String[] API_TITLES = new String[]{"public", "private"};
    private int index, userID;

    public static FragmentFollow newInstance(int index, int id) {
        Bundle args = new Bundle();
        args.putSerializable("index", index);
        args.putSerializable("user id", id);
        FragmentFollow fragment = new FragmentFollow();
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new LinearItemDecoration(
                DensityUtil.dip2px(mContext, 12.0f)));
        mRecyclerView.setHasFixedSize(true);
        mRefreshLayout = v.findViewById(R.id.refreshLayout);
        mRefreshLayout.setRefreshHeader(new DeliveryHeader(mContext));
        mRefreshLayout.setOnLoadMoreListener(layout -> getNextData());
        mRefreshLayout.setOnRefreshListener(layout -> getFirstData());
        return v;
    }

    @Override
    void initData() {
        index = (int) getArguments().getSerializable("index");
        userID = (int) getArguments().getSerializable("user id");
        dataType = API_TITLES[index];
        getFirstData();
    }

    @Override
    void getFirstData() {
        Retro.initToken(() ->
                Retro.getAppApi().getFollowUser(
                        LocalData.getToken(), "for_android", userID, dataType)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<FollowResponse>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(FollowResponse followResponse) {
                                if (followResponse != null && followResponse.getUser_previews() != null) {
                                    allIllusts.clear();
                                    allIllusts = followResponse.getUser_previews();
                                    nextUrl = followResponse.getNextUrl();
                                    mAdapter = new UserFollowAdapter(allIllusts, mContext);
                                    mAdapter.setOnItemClickListener(new OnItemClickListener() {
                                        @Override
                                        public void onItemClick(@NotNull View view, int position, int viewType) {
                                            if(viewType == 0){
                                                Intent intent = new Intent(mContext, UserDetailActivity.class);
                                                intent.putExtra("user id", allIllusts.get(position).getUser().getId());
                                                startActivity(intent);
                                            }
                                        }

                                        @Override
                                        public void onItemLongClick(@NotNull View view, int position) {

                                        }
                                    });
                                    mRecyclerView.setAdapter(mAdapter);
                                    mRefreshLayout.finishRefresh(true);
                                } else {
                                    Common.showToast(PixivApp.getContext().getString(R.string.load_error));
                                    mRefreshLayout.finishRefresh(false);
                                }
                                mProgressBar.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onError(Throwable e) {
                                mRefreshLayout.finishRefresh(false);
                                mProgressBar.setVisibility(View.INVISIBLE);
                                Common.showToast(PixivApp.getContext().getString(R.string.load_error));
                            }

                            @Override
                            public void onComplete() {

                            }
                        }));
    }

    @Override
    void getNextData() {
        if (nextUrl != null) {
            Retro.initToken(() -> Retro.getAppApi().getNextFollowUser(LocalData.getToken(), nextUrl)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<FollowResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(FollowResponse followResponse) {
                            if (followResponse != null && followResponse.getUser_previews() != null) {
                                mRefreshLayout.finishLoadMore(true);
                                allIllusts.addAll(followResponse.getUser_previews());
                                nextUrl = followResponse.getNextUrl();
                                mAdapter.notifyItemRangeChanged(allIllusts.size() - 30, 30);
                            } else {
                                mRefreshLayout.finishLoadMore(false);
                                Common.showToast(PixivApp.getContext().getString(R.string.load_error));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            mRefreshLayout.finishLoadMore(false);
                            Common.showToast(PixivApp.getContext().getString(R.string.load_error));
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
