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
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.SpecialCollectionActivity;
import com.example.administrator.essim.activities.UserDetailActivity;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.adapters.AuthorWorksAdapter;
import com.example.administrator.essim.adapters.SpecialCollecAdapter;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.network.AppApiPixivService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.PixivCollectionResponse;
import com.example.administrator.essim.response.RecommendResponse;
import com.example.administrator.essim.response.Reference;
import com.example.administrator.essim.response.SpecialArticalResponse;
import com.example.administrator.essim.response.SpecialCollectionResponse;
import com.example.administrator.essim.response.UserIllustsResponse;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.LocalData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.VISIBLE;

public class FragmentSpecialCollec extends BaseFragment {

    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private SpecialCollecAdapter specialCollecAdapter;
    private FragmentCollecDetail fragmentCollecDetail;
    private String next_url;
    private boolean isLoadingMore;
    private List<PixivCollectionResponse.SpotlightArticlesBean> spotlight_articles = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_special_collec, container, false);
        initView(view);
        getSpecialCollection();//默认加载第一页
        return view;
    }

    private void initView(View v) {
        mProgressBar = v.findViewById(R.id.try_login);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView = v.findViewById(R.id.pixiv_recy);
        Toolbar mToolbar = v.findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(view -> Objects.requireNonNull(getActivity()).finish());
        LinearLayoutManager linearLayout = new LinearLayoutManager(mContext);
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayout);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = linearLayout.findLastVisibleItemPosition();
                int totalItemCount = specialCollecAdapter.getItemCount();
                if (lastVisibleItem >= totalItemCount - 2 && dy > 0 && !isLoadingMore) {
                    getNextData();
                    isLoadingMore = true;
                }
            }
        });
    }

    private void getSpecialCollection() {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<PixivCollectionResponse> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .getSpecialCollection(LocalData.getToken());
        call.enqueue(new Callback<PixivCollectionResponse>() {
            @Override
            public void onResponse(Call<PixivCollectionResponse> call, retrofit2.Response<PixivCollectionResponse> response) {
                try {
                    next_url = response.body().getNext_url();
                    spotlight_articles.addAll(response.body().getSpotlight_articles());
                    specialCollecAdapter = new SpecialCollecAdapter(spotlight_articles, mContext);
                    specialCollecAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position, int viewType) {
                            fragmentCollecDetail = null;
                            //点了某一个想看的特辑，hide特辑列表，show特辑详情
                            fragmentCollecDetail = FragmentCollecDetail.newInstance(
                                    String.valueOf(spotlight_articles.get(position).getId()),
                                    spotlight_articles.get(position).getTitle());
                            assert getFragmentManager() != null;
                            getFragmentManager().beginTransaction()
                                    .add(R.id.special_collec_container, fragmentCollecDetail)
                                    .hide(((SpecialCollectionActivity) Objects.requireNonNull(getActivity())).getMFragmentSpecialCollec())
                                    .show(fragmentCollecDetail)
                                    .addToBackStack(null)
                                    .commit();
                        }

                        @Override
                        public void onItemLongClick(View view, int position) {

                        }
                    });
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mRecyclerView.setAdapter(specialCollecAdapter);
                } catch (Exception e) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Snackbar.make(mRecyclerView, R.string.no_proxy, Snackbar.LENGTH_SHORT).show();
                }
                isLoadingMore = false;
            }

            @Override
            public void onFailure(@NonNull Call<PixivCollectionResponse> call, @NonNull Throwable t) {
                Snackbar.make(mRecyclerView, R.string.no_proxy, Snackbar.LENGTH_SHORT).show();
                isLoadingMore = false;
            }
        });
    }

    private void getNextData() {
        if (next_url != null) {
            mProgressBar.setVisibility(View.VISIBLE);
            Call<PixivCollectionResponse> call = new RestClient()
                    .getRetrofit_AppAPI()
                    .create(AppApiPixivService.class)
                    .getNextSpecialCollection(LocalData.getToken(), next_url);
            call.enqueue(new Callback<PixivCollectionResponse>() {
                @Override
                public void onResponse(Call<PixivCollectionResponse> call, Response<PixivCollectionResponse> response) {
                    if (response.body() != null) {
                        next_url = response.body().getNext_url();
                        spotlight_articles.addAll(response.body().getSpotlight_articles());
                        specialCollecAdapter.notifyDataSetChanged();
                        mProgressBar.setVisibility(View.INVISIBLE);
                        isLoadingMore = false;
                    }
                }

                @Override
                public void onFailure(Call<PixivCollectionResponse> call, Throwable t) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Snackbar.make(mRecyclerView, R.string.no_proxy, Snackbar.LENGTH_SHORT).show();
                    isLoadingMore = false;
                }
            });
        } else {
            Snackbar.make(mProgressBar, getString(R.string.no_more_data), Snackbar.LENGTH_SHORT).show();
        }
    }
}
