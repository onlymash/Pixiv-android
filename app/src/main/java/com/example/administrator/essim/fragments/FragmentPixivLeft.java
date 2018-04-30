package com.example.administrator.essim.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.adapters.PixivAdapter;
import com.example.administrator.essim.models.PixivRankItem;
import com.example.administrator.essim.models.Reference;
import com.example.administrator.essim.utils.Common;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;
import com.yalantis.phoenix.PullToRefreshView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/1/15 0015.
 */

public class FragmentPixivLeft extends BaseFragment {

    private static final int PER_PAGE_SIZE = 20;
    public int now_page = 1;
    public int currentDataType;
    public String now_link_address;
    private PixivAdapter mPixivAdapter;
    private String responseData = "";
    private RecyclerView mRecyclerView;
    private FloatingActionMenu mFloatingActionMenu;
    private Gson gson = new Gson();
    private PullToRefreshView mPullToRefreshView;
    private ProgressBar mProgressBar;
    private String url_rank_daily = "https://api.imjad.cn/pixiv/v1/?type=rank&content=illust&mode=daily&per_page=20&date=" + Common.getLastDay();
    private String url_rank_weekly = "https://api.imjad.cn/pixiv/v1/?type=rank&content=illust&mode=weekly&per_page=20&date=" + Common.getLastDay();
    private String url_rank_monthly = "https://api.imjad.cn/pixiv/v1/?type=rank&content=all&mode=monthly&per_page=20&date=" + Common.getLastDay();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pixiv_left, container, false);
        Reference.sFragmentPixivLeft = this;
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = view.findViewById(R.id.pixiv_recy);
        mFloatingActionMenu = getActivity().findViewById(R.id.menu_red);
        mProgressBar = view.findViewById(R.id.loading);
        mPullToRefreshView = view.findViewById(R.id.pull_wo_refresh);
        mPullToRefreshView.setOnRefreshListener(() -> {
            if (now_page <= PER_PAGE_SIZE) {
                getData(now_link_address + "&page=" + String.valueOf(now_page), true);
            } else {
                TastyToast.makeText(mContext, "没有更多数据啦~", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show();
                mPullToRefreshView.setRefreshing(false);
            }
        });

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    mFloatingActionMenu.hideMenuButton(true);
                } else {
                    mFloatingActionMenu.showMenuButton(true);
                }
            }
        });
        getData(url_rank_daily, false);
        currentDataType = 0;
        ((FragmentPixiv) getParentFragment()).setChangeDataSet(dataType -> {
            if (dataType == 0) {
                if (currentDataType != 0) {
                    now_page = 1;
                    getData(url_rank_daily, false);
                    currentDataType = 0;
                }
            } else if (dataType == 1) {
                if (currentDataType != 1) {
                    now_page = 1;
                    getData(url_rank_weekly, false);
                    currentDataType = 1;
                }
            } else if (dataType == 2) {
                if (currentDataType != 2) {
                    now_page = 1;
                    getData(url_rank_monthly, false);
                    currentDataType = 2;
                }
            }
        });
        return view;
    }

    public void getData(String address, boolean stopRefresh) {
        mProgressBar.setVisibility(View.VISIBLE);
        Common.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(() -> TastyToast.makeText(mContext, "数据加载失败", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseData = response.body().string();
                Reference.sPixivRankItem = gson.fromJson(responseData, PixivRankItem.class);
                mPixivAdapter = new PixivAdapter(Reference.sPixivRankItem, mContext);
                mPixivAdapter.setOnItemClickListener((view, position) -> {
                    if (position == -1) {
                        if (now_page <= PER_PAGE_SIZE) {
                            getData(now_link_address + "&page=" + String.valueOf(now_page), true);
                        } else {
                            TastyToast.makeText(mContext, "没有更多数据啦~", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show();
                        }
                    } else {
                        Intent intent = new Intent(mContext, ViewPagerActivity.class);
                        intent.putExtra("which_one_is_touched", position);
                        mContext.startActivity(intent);
                    }
                });
                getActivity().runOnUiThread(() -> {
                    mRecyclerView.setAdapter(mPixivAdapter);
                    mPixivAdapter.notifyDataSetChanged();
                    if (stopRefresh) {
                        mPullToRefreshView.setRefreshing(false);
                    }
                    mProgressBar.setVisibility(View.GONE);
                });
                now_link_address = address;
                ((FragmentPixiv) getParentFragment()).gotoPage = now_page;
                now_page++;
                Common.showLog(now_link_address);
            }
        });
    }
}