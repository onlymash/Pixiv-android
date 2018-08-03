package com.example.administrator.essim.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private OkHttpClient okHttpClient;
    private Retrofit retrofit_AppAPI;
    private Retrofit retrofit_OAuthSecure;

    /*著作权归作者所有。
        商业转载请联系作者获得授权，非商业转载请注明出处。
        Mashiro
        链接：https://2heng.xin/2017/09/19/pixiv/
        来源：樱花庄的白猫

        210.129.120.49  pixiv.net
        210.129.120.49  www.pixiv.net
        210.140.92.134  i.pximg.net
        210.140.131.146 source.pixiv.net
        210.129.120.56  accounts.pixiv.net
        210.129.120.56  touch.pixiv.net
        210.140.131.147 imgaz.pixiv.net
        210.129.120.44  app-api.pixiv.net
        210.129.120.48  oauth.secure.pixiv.net
        210.129.120.41  dic.pixiv.net
        210.140.131.153 comic.pixiv.net
        210.129.120.43  factory.pixiv.net
        74.120.148.207  g-client-proxy.pixiv.net
        210.140.174.37  sketch.pixiv.net
        210.129.120.43  payment.pixiv.net
        210.129.120.41  sensei.pixiv.net
        210.140.131.144 novel.pixiv.net
        210.129.120.44  en-dic.pixiv.net
        210.140.131.145 i1.pixiv.net
        210.140.131.145 i2.pixiv.net
        210.140.131.145 i3.pixiv.net
        210.140.131.145 i4.pixiv.net
        210.140.131.159 d.pixiv.org
        210.140.92.141  s.pximg.net
        210.140.92.135  pixiv.pximg.net
        210.129.120.56  fanbox.pixiv.net
        #Pixiv End*/

    public OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor localHttpLoggingInterceptor = new HttpLoggingInterceptor(paramString -> {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("message====");
            localStringBuilder.append(paramString);
            Log.i("aaa", localStringBuilder.toString());
        });
        localHttpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.dns(new HttpDns());
        builder.addInterceptor(paramChain -> {//User-Agent: PixivAndroidApp/5.0.104 (Android 6.0.1; D6653)
            Request localRequest = paramChain.request().newBuilder().addHeader("User-Agent", "PixivAndroidApp/5.0.104 (Android 6.0.1; D6653)").build();
            if (localRequest.header("Authorization") != null)
                Log.i("header", localRequest.header("Authorization"));
            return paramChain.proceed(localRequest);
        }).addInterceptor(localHttpLoggingInterceptor);
        okHttpClient = builder.build();
        return okHttpClient;
    }

    public Retrofit getretrofit_OAuthSecure() {
        Gson localGson = new GsonBuilder().create();
        retrofit_OAuthSecure = new Retrofit.Builder().baseUrl("https://oauth.secure.pixiv.net/")
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(localGson))
                .build();
        return retrofit_OAuthSecure;
    }

    public Retrofit getRetrofit_AppAPI() {
        Gson localGson = new GsonBuilder().create();
        retrofit_AppAPI = new Retrofit.Builder().baseUrl("https://app-api.pixiv.net/")
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(localGson))
                .build();
        return retrofit_AppAPI;
    }

    public Retrofit getRetrofit_Account() {
        Gson localGson = new GsonBuilder().create();
        retrofit_AppAPI = new Retrofit.Builder().baseUrl("https://accounts.pixiv.net/")
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(localGson))
                .build();
        return this.retrofit_AppAPI;
    }

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
        return this.retrofit_AppAPI;
    }
}
