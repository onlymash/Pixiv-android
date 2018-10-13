package com.example.administrator.essim.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ProgressBar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.network.AppApiPixivService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.BookmarkAddResponse;
import com.example.administrator.essim.response.IllustDetailResponse;
import com.example.administrator.essim.response.IllustsBean;
import com.example.administrator.essim.response.Reference;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class PixivOperate {

    /**
     * 收藏一个画作
     *
     * @param id       画作id
     * @param context  context
     * @param starType 公开/非公开收藏
     */
    public static void postStarIllust(int id, Context context, String starType) {
        Call<BookmarkAddResponse> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .postLikeIllust(LocalData.getToken(), id, starType, null);
        call.enqueue(new Callback<BookmarkAddResponse>() {
            @Override
            public void onResponse(Call<BookmarkAddResponse> call, retrofit2.Response<BookmarkAddResponse> response) {
                if (starType.equals("private")) {
                    Common.showToast(context, context.getString(R.string.private_star));
                } else {
                    Common.showToast(context, context.getString(R.string.public_star));
                }
            }

            @Override
            public void onFailure(Call<BookmarkAddResponse> call, Throwable throwable) {
                Common.showToast(context, context.getString(R.string.no_proxy));
            }
        });
    }

    /**
     * 取消收藏
     *
     * @param id      画作id
     * @param context context
     */
    public static void postUnstarIllust(int id, Context context) {
        Call<ResponseBody> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .postUnlikeIllust(LocalData.getToken(), id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Common.showToast(context, context.getString(R.string.remove_star));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Common.showToast(context, context.getString(R.string.no_proxy));
            }
        });
    }

    /**
     * 收藏一个画作到某个标签
     *
     * @param illustsBean illustsBean
     * @param tagList     收藏到标签
     * @param context     context
     * @param starType    公开/非公开收藏
     */
    public static void postStarIllust(IllustsBean illustsBean, List<String> tagList, Context context, String starType) {
        Call<BookmarkAddResponse> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .postLikeIllust(LocalData.getToken(), illustsBean.getId(), starType, tagList);
        call.enqueue(new Callback<BookmarkAddResponse>() {
            @Override
            public void onResponse(Call<BookmarkAddResponse> call, retrofit2.Response<BookmarkAddResponse> response) {
                illustsBean.setIs_bookmarked(true);
                if (starType.equals("private")) {
                    Common.showToast(context, context.getString(R.string.private_star));
                } else {
                    Common.showToast(context, context.getString(R.string.public_star));
                }
            }

            @Override
            public void onFailure(Call<BookmarkAddResponse> call, Throwable throwable) {
                Common.showToast(context, context.getString(R.string.no_proxy));
            }
        });
    }


    /**
     *
     * @param progressBar
     * @param context
     * @param illustID
     */
    public static void getSingleIllust(ProgressBar progressBar, Context context, Long illustID) {
        progressBar.setVisibility(View.VISIBLE);
        Call<IllustDetailResponse> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .getIllust(LocalData.getToken(), illustID);
        call.enqueue(new Callback<IllustDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<IllustDetailResponse> call,
                                   @NonNull retrofit2.Response<IllustDetailResponse> response) {
                IllustDetailResponse illustDetailResponse = response.body();
                List<IllustsBean> singleIllust = new ArrayList<>();
                try {
                    singleIllust.add(illustDetailResponse.getIllust());
                    Reference.sIllustsBeans = singleIllust;
                    Intent intent = new Intent(context, ViewPagerActivity.class);
                    intent.putExtra("which one is selected", 0);
                    context.startActivity(intent);
                } catch (Exception e) {
                    Snackbar.make(progressBar, "没有这个作品", Snackbar.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(@NonNull Call<IllustDetailResponse> call, @NonNull Throwable throwable) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
