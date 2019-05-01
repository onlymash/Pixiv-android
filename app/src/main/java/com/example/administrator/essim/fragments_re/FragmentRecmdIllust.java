package com.example.administrator.essim.fragments_re;

import android.content.Intent;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.activities_re.PixivApp;
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

public class FragmentRecmdIllust extends BaseFragment {

    private RecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;
    private ProgressBar mProgressBar;
    private IllustAdapter mAdapter;
    private GridLayoutManager mGridLayoutManager;
    private List<IllustsBean> allIllusts = new ArrayList<>();
    private ImageView loadError;
    private String nextUrl = null;

    @Override
    void initLayout() {
        mLayoutID = R.layout.fragment_recommend;
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
        loadError = v.findViewById(R.id.load_error);
        loadError.setOnClickListener(v1 -> getFirstData());
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
        mProgressBar.setVisibility(View.VISIBLE);
        loadError.setVisibility(View.INVISIBLE);
        Retro.initToken(() -> {
            Retro.getAppApi().getRecmdIllust(
                    LocalData.getToken(), "for_android", false)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<IllustListResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(IllustListResponse recmdIllustResponse) {
                            if (recmdIllustResponse != null && recmdIllustResponse.getIllusts() != null) {
                                allIllusts.clear();
                                allIllusts = recmdIllustResponse.getIllusts();
                                Common.showLog(allIllusts.size());
                                Common.showLog("这里有多少个");
                                nextUrl = recmdIllustResponse.getNext_url();
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
                                mRefreshLayout.getLayout().setVisibility(View.VISIBLE);
                                loadError.setVisibility(View.INVISIBLE);
                                mRefreshLayout.finishRefresh(true);
                                ((FragmentPixiv) getParentFragment()).getFragments()[1].getFirstData();
                            } else {
                                Common.showToast(getString(R.string.load_error));
                                mRefreshLayout.finishRefresh(false);
                                mRefreshLayout.getLayout().setVisibility(View.INVISIBLE);
                                loadError.setVisibility(View.VISIBLE);
                                ((FragmentHotTag)((FragmentPixiv) getParentFragment()).getFragments()[1]).noNetwork();
                            }
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mRefreshLayout.finishRefresh(false);
                            mRefreshLayout.getLayout().setVisibility(View.INVISIBLE);
                            loadError.setVisibility(View.VISIBLE);
                            mProgressBar.setVisibility(View.INVISIBLE);
                            ((FragmentHotTag)((FragmentPixiv) getParentFragment()).getFragments()[1]).noNetwork();
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
                                mAdapter.notifyItemRangeChanged(allIllusts.size() -
                                        recmdIllustResponse.getIllusts().size(),
                                        recmdIllustResponse.getIllusts().size());
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
