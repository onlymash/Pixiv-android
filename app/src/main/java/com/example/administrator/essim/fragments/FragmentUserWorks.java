package com.example.administrator.essim.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.UserDetailActivity;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.adapters.AuthorWorksAdapter;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.network.AppApiPixivService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.IllustsBean;
import com.example.administrator.essim.response.Reference;
import com.example.administrator.essim.response.UserIllustsResponse;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.DensityUtil;
import com.example.administrator.essim.utils.PixivOperate;
import com.example.administrator.essim.utils.WorksItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

import static android.view.View.VISIBLE;


public class FragmentUserWorks extends ScrollObservableFragment {

    public GridLayoutManager gridLayoutManager;
    public List<IllustsBean> mIllustsBeanList = new ArrayList<>();
    private String next_url;
    private Context mContext;
    private ImageView mImageView;
    private boolean isLoadingMore;
    private RecyclerView rcvGoodsList;
    private AuthorWorksAdapter mPixivAdapterGrid;
    private SharedPreferences mSharedPreferences;
    private int scrolledY = 0;

    public static FragmentUserWorks newInstance() {
        FragmentUserWorks fragment = new FragmentUserWorks();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        mSharedPreferences = Common.getLocalDataSet();
        View v = inflater.inflate(R.layout.fragment_home_list, container, false);
        initView(v);
        getUserIllust();
        return v;
    }

    private void initView(View v) {
        gridLayoutManager = new GridLayoutManager(mContext, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mPixivAdapterGrid.getItemViewType(position) == 3) {
                    return gridLayoutManager.getSpanCount();
                } else {
                    return 1;
                }
            }
        });
        rcvGoodsList = v.findViewById(R.id.rcvGoodsList);
        rcvGoodsList.setLayoutManager(gridLayoutManager);
        rcvGoodsList.setHasFixedSize(true);
        rcvGoodsList.addItemDecoration(new WorksItemDecoration(
                2, DensityUtil.dip2px(mContext, 8.0f), true));
        rcvGoodsList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, final int dx, final int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrolledY += dy;
                if (FragmentUserWorks.this.isResumed()) {
                    doOnScrollChanged(0, scrolledY, dx, dy);
                }
                int lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = mPixivAdapterGrid.getItemCount();
                if (lastVisibleItem >= totalItemCount - 1 && dy > 0 && !isLoadingMore) {
                    getNextUserIllust();
                    isLoadingMore = true;
                }
            }
        });
        mImageView = v.findViewById(R.id.no_data);
    }

    private void getUserIllust() {
        FragmentUserDetail.mShowProgress.showProgress(true);
        Call<UserIllustsResponse> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .getUserIllusts(mSharedPreferences.getString("Authorization", ""),
                        ((UserDetailActivity) Objects.requireNonNull(getActivity())).getUserID(), null);
        call.enqueue(new retrofit2.Callback<UserIllustsResponse>() {
            @Override
            public void onResponse(Call<UserIllustsResponse> call, retrofit2.Response<UserIllustsResponse> response) {
                if (getView() != null) {
                    if (response.body().getIllusts().size() == 0) {
                        rcvGoodsList.setVisibility(View.INVISIBLE);
                        mImageView.setVisibility(VISIBLE);
                    } else {
                        mIllustsBeanList.clear();
                        mIllustsBeanList.addAll(response.body().getIllusts());
                        mPixivAdapterGrid = new AuthorWorksAdapter(mIllustsBeanList, mContext);
                        next_url = response.body().getNext_url();
                        mPixivAdapterGrid.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position, int viewType) {
                                if (viewType == 0) {
                                    Reference.sIllustsBeans = mIllustsBeanList;
                                    Intent intent = new Intent(mContext, ViewPagerActivity.class);
                                    intent.putExtra("which one is selected", position);
                                    mContext.startActivity(intent);
                                } else if (viewType == 1) {
                                    if (!mIllustsBeanList.get(position).isIs_bookmarked()) {
                                        ((ImageView) view).setImageResource(R.drawable.ic_favorite_white_24dp);
                                        view.startAnimation(Common.getAnimation());
                                        mIllustsBeanList.get(position).setIs_bookmarked(true);
                                        PixivOperate.postStarIllust(mIllustsBeanList.get(position).getId(), mContext, "public");
                                    } else {
                                        ((ImageView) view).setImageResource(R.drawable.no_favor);
                                        view.startAnimation(Common.getAnimation());
                                        mIllustsBeanList.get(position).setIs_bookmarked(false);
                                        PixivOperate.postUnstarIllust(mIllustsBeanList.get(position).getId(), mContext);
                                    }
                                }
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                new FragmentDialog(mContext, view, mIllustsBeanList.get(position)).showDialog();
                            }
                        });
                        // 有数据，textview不显示，显示recyclerview
                        rcvGoodsList.setVisibility(VISIBLE);
                        mImageView.setVisibility(View.INVISIBLE);
                        rcvGoodsList.setAdapter(mPixivAdapterGrid);
                        scrolledY = 0;
                        rcvGoodsList.scrollBy(0, FragmentUserDetail.scrollYset);
                    }
                    FragmentUserDetail.mShowProgress.showProgress(false);
                }
            }

            @Override
            public void onFailure(Call<UserIllustsResponse> call, Throwable throwable) {
                rcvGoodsList.setVisibility(View.INVISIBLE);
                mImageView.setVisibility(VISIBLE);
                FragmentUserDetail.mShowProgress.showProgress(false);
                Common.showToast(mContext, getString(R.string.no_proxy));
            }
        });
    }

    private void getNextUserIllust() {
        if (next_url != null) {
            FragmentUserDetail.mShowProgress.showProgress(true);
            Call<UserIllustsResponse> call = new RestClient()
                    .getRetrofit_AppAPI()
                    .create(AppApiPixivService.class)
                    .getNextUserIllusts(mSharedPreferences.getString("Authorization", ""), next_url);
            call.enqueue(new retrofit2.Callback<UserIllustsResponse>() {
                @Override
                public void onResponse(Call<UserIllustsResponse> call, retrofit2.Response<UserIllustsResponse> response) {
                    next_url = response.body().getNext_url();
                    mIllustsBeanList.addAll(response.body().getIllusts());
                    mPixivAdapterGrid.notifyDataSetChanged();
                    isLoadingMore = false;
                    FragmentUserDetail.mShowProgress.showProgress(false);

                }

                @Override
                public void onFailure(Call<UserIllustsResponse> call, Throwable throwable) {

                }
            });
        } else {
            Snackbar.make(rcvGoodsList, "再怎么找也找不到了~", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setScrolledY(int scrolledY) {
        if (rcvGoodsList != null) {
            if (this.scrolledY >= scrolledY) {
                int scrollDistance = (this.scrolledY - scrolledY) * -1;
                rcvGoodsList.scrollBy(0, scrollDistance);
            } else {
                rcvGoodsList.scrollBy(0, scrolledY);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPixivAdapterGrid != null) {
            mPixivAdapterGrid.notifyDataSetChanged();
        }
    }
}
