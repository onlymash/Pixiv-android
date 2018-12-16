package com.example.administrator.essim.network;

import android.util.Log;

import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.LocalData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.InetSocketAddress;
import java.net.Proxy;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    public static OkHttpClient okHttpClient;
    public static Retrofit retrofit_AppAPI;
    public static Retrofit retrofit_OAuthSecure;
    public static Retrofit retrofit_Account;

    static {
        Gson localGson = new GsonBuilder().create();
        /*if(nowLine == 0){*/
            retrofit_OAuthSecure = new Retrofit.Builder().baseUrl("https://oauth.secure.pixiv.net/")
                    //retrofit_OAuthSecure = new Retrofit.Builder().baseUrl("https://oauth.secure.pixiv.net/")
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(localGson))
                    .build();
            retrofit_AppAPI = new Retrofit.Builder().baseUrl("https://app-api.pixiv.net/")
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(localGson))
                    .build();
            retrofit_Account = new Retrofit.Builder().baseUrl("https://accounts.pixiv.net/")
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(localGson))
                    .build();
        /*}else if(nowLine == 1){
            retrofit_OAuthSecure = new Retrofit.Builder().baseUrl("https://oauth.ceuilisa.com/")
                    //retrofit_OAuthSecure = new Retrofit.Builder().baseUrl("https://oauth.secure.pixiv.net/")
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(localGson))
                    .build();
            retrofit_AppAPI = new Retrofit.Builder().baseUrl("https://app-api.ceuilisa.net/")
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(localGson))
                    .build();
            retrofit_Account = new Retrofit.Builder().baseUrl("https://accounts.ceuilisa.net/")
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(localGson))
                    .build();
        }else if(nowLine == 2){
            retrofit_OAuthSecure = new Retrofit.Builder().baseUrl("https://oauth2.ceuilisa.com/")
                    //retrofit_OAuthSecure = new Retrofit.Builder().baseUrl("https://oauth.secure.pixiv.net/")
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(localGson))
                    .build();
            retrofit_AppAPI = new Retrofit.Builder().baseUrl("https://app-api2.ceuilisa.net/")
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(localGson))
                    .build();
            retrofit_Account = new Retrofit.Builder().baseUrl("https://accounts2.ceuilisa.net/")
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(localGson))
                    .build();
        }*/
    }

    private static OkHttpClient getOkHttpClient() {
        if(okHttpClient == null) {
            HttpLoggingInterceptor localHttpLoggingInterceptor = new HttpLoggingInterceptor(paramString -> {
                StringBuilder localStringBuilder = new StringBuilder();
                localStringBuilder.append("message====");
                localStringBuilder.append(paramString);
                Log.i("aaa", localStringBuilder.toString());
            });
            localHttpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            //builder.dns(HttpDns.getInstance());
            builder.addInterceptor(paramChain -> {//User-Agent: PixivAndroidApp/5.0.104 (Android 6.0.1; D6653)
                Request localRequest = paramChain.request().newBuilder().addHeader("User-Agent",
                        "PixivAndroidApp/5.0.104 (Android 6.0.1; D6653)").build();
                if (localRequest.header("Authorization") != null)
                    Log.i("header", localRequest.header("Authorization"));
                return paramChain.proceed(localRequest);
            }).addInterceptor(localHttpLoggingInterceptor);
            okHttpClient = builder.build();
        }
        return okHttpClient;
    }

    public RestClient() {
    }

    /*public Retrofit getretrofit_OAuthSecure() {
        Gson localGson = new GsonBuilder().create();
        if(nowLine == 0){
            retrofit_OAuthSecure = new Retrofit.Builder().baseUrl("https://oauth.secure.pixiv.net/")
                    //retrofit_OAuthSecure = new Retrofit.Builder().baseUrl("https://oauth.secure.pixiv.net/")
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(localGson))
                    .build();
        }else if(nowLine == 1){
            retrofit_OAuthSecure = new Retrofit.Builder().baseUrl("https://oauth.ceuilisa.com/")
                    //retrofit_OAuthSecure = new Retrofit.Builder().baseUrl("https://oauth.secure.pixiv.net/")
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(localGson))
                    .build();
        }else if(nowLine == 2){
            retrofit_OAuthSecure = new Retrofit.Builder().baseUrl("https://oauth2.ceuilisa.com/")
                    //retrofit_OAuthSecure = new Retrofit.Builder().baseUrl("https://oauth.secure.pixiv.net/")
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(localGson))
                    .build();
        }
        return retrofit_OAuthSecure;
    }


    public Retrofit getRetrofit_AppAPI() {
        Gson localGson = new GsonBuilder().create();
        if(nowLine == 0){
            retrofit_AppAPI = new Retrofit.Builder().baseUrl("https://app-api.pixiv.net/")
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(localGson))
                    .build();
        }else if(nowLine == 1){
            retrofit_AppAPI = new Retrofit.Builder().baseUrl("https://app-api.ceuilisa.com/")
                    //retrofit_AppAPI = new Retrofit.Builder().baseUrl("https://app-api.pixiv.net/")
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(localGson))
                    .build();
        }else if(nowLine == 2){
            retrofit_AppAPI = new Retrofit.Builder().baseUrl("https://app-api2.ceuilisa.com/")
                    //retrofit_AppAPI = new Retrofit.Builder().baseUrl("https://app-api.pixiv.net/")
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(localGson))
                    .build();
        }
        return retrofit_AppAPI;
    }

    public Retrofit getRetrofit_Account() {
        Gson localGson = new GsonBuilder().create();
        if(nowLine == 0){
            retrofit_Account = new Retrofit.Builder().baseUrl("https://accounts.pixiv.net/")
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(localGson))
                    .build();
        }else if(nowLine == 1){
            retrofit_Account = new Retrofit.Builder().baseUrl("https://accounts.ceuilisa.com/")
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(localGson))
                    .build();
        }else if(nowLine == 2){
            retrofit_Account = new Retrofit.Builder().baseUrl("https://accounts2.ceuilisa.com/")
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(localGson))
                    .build();
        }
        return retrofit_Account;
    }*/

    public Retrofit getRetrofit_SpecialCollection() {
        Gson localGson = new GsonBuilder().create();
        HttpLoggingInterceptor localHttpLoggingInterceptor = new HttpLoggingInterceptor(paramString -> {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("message====");
            localStringBuilder.append(paramString);
            Log.i("aaa", localStringBuilder.toString());
        });
        localHttpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(localHttpLoggingInterceptor);
        okHttpClient = builder.build();

        retrofit_AppAPI = new Retrofit.Builder().baseUrl("https://api.imjad.cn/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(localGson))
                .build();
        return retrofit_AppAPI;
    }


    public OnlineApi getOnlineApi() {
        Gson localGson = new GsonBuilder().create();
        HttpLoggingInterceptor localHttpLoggingInterceptor = new HttpLoggingInterceptor(paramString -> {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("message====");
            localStringBuilder.append(paramString);
            Log.i("aaa", localStringBuilder.toString());
        });
        localHttpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(localHttpLoggingInterceptor);
        okHttpClient = builder.build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://pixiv-meapi.ceuilisa.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(localGson))
                .build();
        return retrofit.create(OnlineApi.class);
    }
}
