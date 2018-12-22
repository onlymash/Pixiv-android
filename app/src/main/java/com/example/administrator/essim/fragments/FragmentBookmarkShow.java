package com.example.administrator.essim.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.adapters.BookedTagAdapter;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.network.AppApiPixivService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.AllBookmarkTagResponse;
import com.example.administrator.essim.response.SingleTag;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils_re.LocalData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class FragmentBookmarkShow {

    private int dataKind;
    private int userId;
    private Context mContext;
    private Dialog mDialog;
    private boolean isLoadingMore = false;
    private BookedTagAdapter mIllustTagAdapter;
    private AllBookmarkTagResponse allBookmarkTagResponse;
    private List<SingleTag> singleTags = new ArrayList<>();

    public FragmentBookmarkShow(Context context, int paramId) {
        mContext = context;
        userId = paramId;
    }

    private void getBookedTag(RecyclerView recyclerView, ProgressBar progressBar, String dataType) {
        progressBar.setVisibility(View.VISIBLE);
        Call<AllBookmarkTagResponse> call = RestClient.retrofit_AppAPI
                .create(AppApiPixivService.class)
                .getBookedTags(LocalData.getToken(), (long) userId, dataType);
        call.enqueue(new Callback<AllBookmarkTagResponse>() {
            @Override
            public void onResponse(Call<AllBookmarkTagResponse> call,
                                   retrofit2.Response<AllBookmarkTagResponse> response) {
                if (response.body() != null && response.body().bookmark_tags != null) {
                    allBookmarkTagResponse = response.body();
                    singleTags.clear();
                    singleTags.add(new SingleTag("全部的"));
                    singleTags.add(new SingleTag("未分类的"));
                    singleTags.addAll(allBookmarkTagResponse.bookmark_tags);
                    initAdapter();
                    progressBar.setVisibility(View.INVISIBLE);
                    recyclerView.setAdapter(mIllustTagAdapter);
                }
            }

            @Override
            public void onFailure(Call<AllBookmarkTagResponse> call, Throwable throwable) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void initAdapter() {
        mIllustTagAdapter = new BookedTagAdapter(singleTags, mContext);
        mIllustTagAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NotNull View view, int position, int viewType) {
                /*if (dataKind == 0) {
                    if (position == 0) {
                        FragmentUserLikes.sRefreshLayout.refreData("public", null);
                        mDialog.dismiss();
                    } else if (position == 1) {
                        FragmentUserLikes.sRefreshLayout.refreData("public", "未分類");
                        mDialog.dismiss();
                    } else {
                        FragmentUserLikes.sRefreshLayout.refreData("public", singleTags.get(position).name);
                        mDialog.dismiss();
                    }
                } else if (dataKind == 1) {
                    if (position == 0) {
                        FragmentUserLikes.sRefreshLayout.refreData("private", null);
                        mDialog.dismiss();
                    } else if (position == 1) {
                        FragmentUserLikes.sRefreshLayout.refreData("private", "未分類");
                        mDialog.dismiss();
                    } else {
                        FragmentUserLikes.sRefreshLayout.refreData("private", singleTags.get(position).name);
                        mDialog.dismiss();
                    }
                }*/
            }

            @Override
            public void onItemLongClick(@NotNull View view, int position) {

            }
        });
    }

    private void getNextData(RecyclerView recyclerView, ProgressBar progressBar, String url) {
        if (allBookmarkTagResponse.next_url != null) {
            progressBar.setVisibility(View.VISIBLE);
            Call<AllBookmarkTagResponse> call = RestClient.retrofit_AppAPI
                    .create(AppApiPixivService.class)
                    .getNextTags(LocalData.getToken(), url);
            call.enqueue(new Callback<AllBookmarkTagResponse>() {
                @Override
                public void onResponse(Call<AllBookmarkTagResponse> call, retrofit2.Response<AllBookmarkTagResponse> response) {
                    if (response.body() != null && response.body().bookmark_tags != null) {
                        allBookmarkTagResponse = response.body();
                        singleTags.addAll(allBookmarkTagResponse.bookmark_tags);
                        progressBar.setVisibility(View.INVISIBLE);
                        mIllustTagAdapter.notifyDataSetChanged();
                        isLoadingMore = false;
                    }
                }

                @Override
                public void onFailure(Call<AllBookmarkTagResponse> call, Throwable throwable) {

                }
            });
        } else {

        }
    }

    public void showDialog() {
        Common.showLog("开始显示了");
        mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View dialogView = inflater.inflate(R.layout.view_dialog_bookmark, null);

        RecyclerView recyclerView = dialogView.findViewById(R.id.bookmark_recy);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = singleTags.size();
                if (lastVisibleItem >= totalItemCount - 1 && dy > 0 && !isLoadingMore) {
                    getNextData(recyclerView, dialogView.findViewById(R.id.mProgressbar), allBookmarkTagResponse.next_url);
                    isLoadingMore = true;
                }
            }
        });
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        TextView textPublic = dialogView.findViewById(R.id.text_public);
        TextView textPrivate = dialogView.findViewById(R.id.text_private);
        textPublic.setOnClickListener(v -> {
            if (dataKind != 0) {
                dataKind = 0;
                textPublic.setBackground(mContext.getDrawable(R.drawable.text_bg));
                textPublic.setTextColor(Color.WHITE);
                textPrivate.setBackground(null);
                textPrivate.setTextColor(Color.BLACK);
                getBookedTag(recyclerView, dialogView.findViewById(R.id.mProgressbar), "public");
            }
        });
        textPrivate.setOnClickListener(v -> {
            if (dataKind != 1) {
                dataKind = 1;
                textPrivate.setBackground(mContext.getDrawable(R.drawable.text_bg));
                textPrivate.setTextColor(Color.WHITE);
                textPublic.setBackground(null);
                textPublic.setTextColor(Color.BLACK);
                getBookedTag(recyclerView, dialogView.findViewById(R.id.mProgressbar), "private");
            }
        });
        ImageView close = dialogView.findViewById(R.id.close);
        close.setOnClickListener(v -> mDialog.dismiss());
        getBookedTag(recyclerView, dialogView.findViewById(R.id.mProgressbar), "public");
        mDialog.setContentView(dialogView);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mDialog.show();
        window.setAttributes(lp);
        mDialog.setCancelable(true);
    }
}
