package com.example.administrator.essim.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.UserDetailActivity;
import com.example.administrator.essim.adapters.CollecDetailAdapter;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.network.AppApiPixivService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.CollectionResponse;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.DensityUtil;
import com.example.administrator.essim.utils.LinearItemDecoration;
import com.example.administrator.essim.utils.PixivOperate;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class FragmentCollecDetail extends BaseFragment {

    private String id, title;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;

    public static FragmentCollecDetail newInstance(String id, String title) {
        Bundle args = new Bundle();
        args.putSerializable("id", id);
        args.putSerializable("title", title);
        FragmentCollecDetail fragment = new FragmentCollecDetail();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collec_detail, container, false);
        id = (String) getArguments().getSerializable("id");
        title = (String) getArguments().getSerializable("title");
        initView(view);
        getSpecialCollection();
        return view;
    }

    private void initView(View v) {
        mProgressBar = v.findViewById(R.id.try_login);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView = v.findViewById(R.id.pixiv_recy);
        Toolbar mToolbar = v.findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(view -> Objects.requireNonNull(getActivity()).finish());
        mToolbar.setTitle(title);
        LinearLayoutManager linearLayout = new LinearLayoutManager(mContext);
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayout);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new LinearItemDecoration(DensityUtil.dip2px(mContext, 16.0f)));
        Glide.get(mContext).clearMemory();
    }

    private void getSpecialCollection() {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<CollectionResponse> call = new RestClient()
                .getRetrofit_SpecialCollection()
                .create(AppApiPixivService.class)
                .getCollecDetail(id);
        call.enqueue(new Callback<CollectionResponse>() {
            @Override
            public void onResponse(Call<CollectionResponse> call, retrofit2.Response<CollectionResponse> response) {
                CollectionResponse specialCollectionResponse = response.body();
                if(specialCollectionResponse.body != null && specialCollectionResponse.body.size() != 0) {
                    try {
                        CollecDetailAdapter specialCollecAdapter = new CollecDetailAdapter(specialCollectionResponse, mContext);
                        specialCollecAdapter.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(@NonNull View view, int position, int viewType) {
                                if (viewType == 0) {
                                    PixivOperate.getSingleIllust(mProgressBar, mContext,
                                            Long.parseLong(specialCollectionResponse.body.get(0).illusts.get(position).illust_id));
                                } else if (viewType == 1) {
                                    Intent intent = new Intent(mContext, UserDetailActivity.class);
                                    intent.putExtra("user id", Integer.valueOf(specialCollectionResponse.body
                                            .get(0).illusts.get(position).illust_user_id));
                                    startActivity(intent);
                                } else if (viewType == 2) {
                                    TastyToast.makeText(mContext, "收藏功能还没填坑，以后更新哦",
                                            TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show();
                                }
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {

                            }
                        });
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mRecyclerView.setAdapter(specialCollecAdapter);
                    } catch (Exception e) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        Snackbar.make(mRecyclerView, "服务器不稳定，出了点小问题", Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Snackbar.make(mRecyclerView, "服务器不稳定，出了点小问题", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CollectionResponse> call, Throwable t) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Snackbar.make(mRecyclerView, "服务器不稳定，出了点小问题", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
