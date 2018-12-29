package com.example.administrator.essim.network_re;

import android.util.Log;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities_re.PixivApp;
import com.example.administrator.essim.interf.OnPrepared;
import com.example.administrator.essim.response_re.LoginResponse;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils_re.LocalData;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retro {

    private static Retrofit sRetrofitApi;

    private static final String API_BASE_URL = "https://app-api.pixiv.net";
    private static final String LOGIN_BASE_URL = "https://oauth.secure.pixiv.net";
    private static final String ACCOUNT_BASE_URL = "https://accounts.pixiv.net";
    //private static final String LOGIN_BASE_URL = "https://144.34.229.245";

    private static final String HEADER_NAME = "User-Agent";
    private static final String HEADER_VALUE = "PixivAndroidApp/5.0.119 (Android 6.0.1; D6653)";
    private static final String HEADER_NAME2 = "Accept-Language";
    private static final String HEADER_VALUE2 = "zh_CN";

    private static final long ONE_HOUR = 3500 * 1000;
    private static final String LOGIN_PARAM_1 = "MOBrBDS8blbauoSck0ZfDbtuzpyT";
    private static final String LOGIN_PARAM_2 = "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj";
    private static final String LOGIN_PARAM_3 = "pixiv";
    private static final boolean LOGIN_PARAM_4 = true;
    private static final String LOGIN_PARAM_5 = "password";
    private static final String LOGIN_PARAM_6 = "refresh_token";

    public Retro() {

    }

    public static Retrofit getRetrofitApi() {
        if(sRetrofitApi == null){
            sRetrofitApi = new Retrofit.Builder()
                    .client(getOkhttp())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(API_BASE_URL)
                    .build();
        }
        return sRetrofitApi;
    }

    public static AppApi getAppApi() {
        return getRetrofitApi().create(AppApi.class);
    }

    public static LoginApi getLoginApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(getOkhttp())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(LOGIN_BASE_URL)
                .build();
        return retrofit.create(LoginApi.class);
    }

    public static AccountApi getAccountApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(getOkhttp())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ACCOUNT_BASE_URL)
                .build();
        return retrofit.create(AccountApi.class);
    }

    private static OkHttpClient getOkhttp() {
        return new OkHttpClient
                .Builder()
                .addInterceptor(getLogGenerator())
                .addInterceptor(chain -> {
                    Request localRequest = chain.request().newBuilder()
                            .addHeader(HEADER_NAME, HEADER_VALUE)
                            .addHeader(HEADER_NAME2, HEADER_VALUE2)
                            .build();
                    return chain.proceed(localRequest);
                })
                .build();
    }

    private static HttpLoggingInterceptor getLogGenerator() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                message -> Log.i("RetrofitLog", "retrofitBack = " + message));
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    public static void initToken(OnPrepared onPrepared) {
        if (System.currentTimeMillis() - LocalData.getLastTokenTime() >= ONE_HOUR) {
            Common.showToast(PixivApp.getContext().getString(R.string.refresh_token));
            Retro.getLoginApi().refreshToken(
                    LOGIN_PARAM_1,
                    LOGIN_PARAM_2,
                    LOGIN_PARAM_6,
                    LocalData.getRefreshToken(),
                    LocalData.getDeviceToken(),
                    LOGIN_PARAM_4)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<LoginResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(LoginResponse loginResponse) {
                            if (loginResponse != null && loginResponse.getResponse() != null) {
                                LocalData.saveLocalMessage(loginResponse);
                            } else {
                                Common.showToast(PixivApp.getContext().getString(R.string.login_error));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Common.showToast(PixivApp.getContext().getString(R.string.login_error));
                        }

                        @Override
                        public void onComplete() {
                            onPrepared.doSomething();
                        }
                    });
        } else {
            onPrepared.doSomething();
        }
    }
}
