package com.example.administrator.essim.utils;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.activities_re.PixivApp;
import com.example.administrator.essim.network.AppApiPixivService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.network_re.Retro;
import com.example.administrator.essim.response.BookmarkAddResponse;
import com.example.administrator.essim.response_re.IllustsBean;
import com.example.administrator.essim.response_re.SingleIllustResponse;
import com.example.administrator.essim.utils_re.LocalData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
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
        Call<BookmarkAddResponse> call = RestClient.retrofit_AppAPI
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
        Call<ResponseBody> call = RestClient.retrofit_AppAPI
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
        Call<BookmarkAddResponse> call = RestClient.retrofit_AppAPI
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


    public static void postFollowUser(int userID, String followType) {
        Retro.initToken(() ->
                Retro.getAppApi().postFollow(
                        com.example.administrator.essim.utils_re.LocalData.getToken(), userID, followType)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<BookmarkAddResponse>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(BookmarkAddResponse bookmarkAddResponse) {
                                if (followType.equals("public")) {
                                    Common.showToast("关注成功~(公开的)");
                                } else {
                                    Common.showToast("关注成功~(非公开的)");
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Common.showToast(PixivApp.getContext().getString(R.string.operate_error));
                            }

                            @Override
                            public void onComplete() {

                            }
                        }));
    }

    public static void postUnFollowUser(int userID) {
        Retro.initToken(() ->
                Retro.getAppApi().postUnFollow(
                        com.example.administrator.essim.utils_re.LocalData.getToken(), userID)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<BookmarkAddResponse>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(BookmarkAddResponse bookmarkAddResponse) {
                                Common.showToast("取消关注~");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Common.showToast(PixivApp.getContext().getString(R.string.operate_error));
                            }

                            @Override
                            public void onComplete() {

                            }
                        }));
    }


    public static void getSingleIllust(Context context, int illustID) {
        Retro.initToken(() -> {
            Retro.getAppApi().getSingleIllust(
                    com.example.administrator.essim.utils_re.LocalData.getToken(), "for_android", illustID)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<SingleIllustResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(SingleIllustResponse singleIllustResponse) {
                            if (singleIllustResponse != null && singleIllustResponse.getIllust() != null) {
                                PixivApp.sIllustsBeans = new ArrayList<>();
                                PixivApp.sIllustsBeans.add(singleIllustResponse.getIllust());
                                Intent intent = new Intent(context, ViewPagerActivity.class);
                                context.startActivity(intent);
                            } else {
                                Common.showToast(context.getString(R.string.load_error));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Common.showToast(context.getString(R.string.load_error));
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        });
    }

    public static void getSingleIllust(Context context, int illustID, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        Retro.initToken(() -> Retro.getAppApi().getSingleIllust(
                LocalData.getToken(), "for_android", illustID)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SingleIllustResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SingleIllustResponse singleIllustResponse) {
                        if (singleIllustResponse != null && singleIllustResponse.getIllust() != null) {
                            PixivApp.sIllustsBeans = new ArrayList<>();
                            PixivApp.sIllustsBeans.add(singleIllustResponse.getIllust());
                            Intent intent = new Intent(context, ViewPagerActivity.class);
                            context.startActivity(intent);
                        } else {
                            Common.showToast(context.getString(R.string.load_error));
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Common.showToast(context.getString(R.string.load_error));
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }
}
