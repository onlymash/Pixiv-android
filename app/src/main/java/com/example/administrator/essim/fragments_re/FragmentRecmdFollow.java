package com.example.administrator.essim.fragments_re;

import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities_re.PixivApp;
import com.example.administrator.essim.activities_re.UserDetailActivity;
import com.example.administrator.essim.adapters_re.UserFollowAdapter;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.network_re.Retro;
import com.example.administrator.essim.response_re.ListUserResponse;
import com.example.administrator.essim.response_re.UserPreviewsBean;
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

/**
 * 推荐关注
 * 2018年12月29日18:36:19
 */
public class FragmentRecmdFollow extends BaseFragment {

    private RecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;
    private ProgressBar mProgressBar;
    private UserFollowAdapter mAdapter;
    private List<UserPreviewsBean> allIllusts = new ArrayList<>();
    private String nextUrl = null;

    @Override
    void initLayout() {
        mLayoutID = R.layout.fragment_illust_list;
    }

    @Override
    View initView(View v) {
        mRecyclerView = v.findViewById(R.id.recy_list);
        mProgressBar = v.findViewById(R.id.progress);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
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
        getFirstData();
    }

    @Override
    void getFirstData() {
        Retro.initToken(() ->
                Retro.getAppApi().getRecmdUser(
                        LocalData.getToken(), "for_android")
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<ListUserResponse>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(ListUserResponse listUserResponse) {
                                if (listUserResponse != null && listUserResponse.getUser_previews() != null) {
                                    allIllusts.clear();
                                    allIllusts = listUserResponse.getUser_previews();
                                    nextUrl = listUserResponse.getNextUrl();
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
                    .subscribe(new Observer<ListUserResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ListUserResponse listUserResponse) {
                            if (listUserResponse != null && listUserResponse.getUser_previews() != null) {
                                mRefreshLayout.finishLoadMore(true);
                                allIllusts.addAll(listUserResponse.getUser_previews());
                                nextUrl = listUserResponse.getNextUrl();
                                mAdapter.notifyItemRangeChanged(allIllusts.size() -
                                        listUserResponse.getUser_previews().size(),
                                        listUserResponse.getUser_previews().size());
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
