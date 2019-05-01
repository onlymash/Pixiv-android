package com.example.administrator.essim.fragments_re;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.activities_re.PixivApp;
import com.example.administrator.essim.activities_re.RankActivity;
import com.example.administrator.essim.adapters_re.IllustAdapter;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.network_re.Retro;
import com.example.administrator.essim.response_re.IllustListResponse;
import com.example.administrator.essim.response_re.IllustsBean;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.DensityUtil;
import com.example.administrator.essim.utils.GridItemDecoration;
import com.example.administrator.essim.utils.PixivOperate;
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

public class FragmentRank extends BaseFragment {

    private RecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;
    private ProgressBar mProgressBar;
    private GridLayoutManager mGridLayoutManager;
    private IllustAdapter mAdapter;
    private List<IllustsBean> allIllusts = new ArrayList<>();
    private String nextUrl = null, dataType = "", date = null;
    private static final String[] API_TITLES = new String[]{"day", "week", "month","day_male", "day_female", "week_original", "week_rookie",  "day_r18"};

    public static FragmentRank newInstance(int index) {
        Bundle args = new Bundle();
        args.putSerializable("index", index);
        FragmentRank fragment = new FragmentRank();
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
        mGridLayoutManager = new GridLayoutManager(mContext, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
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
        int index = (int) getArguments().getSerializable("index");
        dataType = API_TITLES[index];
        date = ((RankActivity) getActivity()).getDateString();
        getFirstData();
    }

    @Override
    void getFirstData() {
        Retro.initToken(() ->
                Retro.getAppApi().getRank(
                        LocalData.getToken(), "for_android", dataType, date)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<IllustListResponse>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(IllustListResponse illustListResponse) {
                                if (illustListResponse != null && illustListResponse.getIllusts() != null) {
                                    allIllusts.clear();
                                    allIllusts = illustListResponse.getIllusts();
                                    nextUrl = illustListResponse.getNext_url();
                                    mAdapter = new IllustAdapter(allIllusts, mContext);
                                    mAdapter.setOnItemClickListener(new OnItemClickListener() {
                                        @Override
                                        public void onItemClick(@NotNull View view, int position, int viewType) {
                                            if (viewType == 0){
                                                PixivApp.sIllustsBeans = allIllusts;
                                                Intent intent = new Intent(mContext, ViewPagerActivity.class);
                                                intent.putExtra("which one is selected", position);
                                                mContext.startActivity(intent);
                                            }
                                        }

                                        @Override
                                        public void onItemLongClick(@NotNull View view, int position) {
                                            if(!allIllusts.get(position).isIs_bookmarked()){
                                                allIllusts.get(position).setIs_bookmarked(true);
                                                ((ImageView) view).setImageResource(R.drawable.ic_favorite_white_24dp);
                                                PixivOperate.postStarIllust(allIllusts.get(position).getId(), mContext, "private");
                                            }
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
        } else {
        }
    }

    public List<IllustsBean> getAllIllusts() {
        return allIllusts;
    }

    public void setAllIllusts(List<IllustsBean> allIllusts) {
        this.allIllusts = allIllusts;
    }

    public int getScrollIndex(){
        return mGridLayoutManager.findFirstCompletelyVisibleItemPosition();
    }
}
