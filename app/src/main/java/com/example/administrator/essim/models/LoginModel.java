package com.example.administrator.essim.models;

import android.support.annotation.NonNull;

import com.example.administrator.essim.interf.LoadListener;
import com.example.administrator.essim.network.OAuthSecureService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.PixivOAuthResponse;
import com.example.administrator.essim.utils.LocalData;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginModel implements BaseModel<PixivOAuthResponse> {

    /**
     * 用作登陆的几个固定参数
     */
    private static final String CLIENT_ID = "MOBrBDS8blbauoSck0ZfDbtuzpyT";
    private static final String CLIENT_SECRET = "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj";
    private static final String GRANT_TYPE = "password";
    private static final String CLIENT_ID_KEY = "client_id";
    private static final String CLIENT_SECRET_KEY = "client_secret";
    private static final String GRANT_TYPE_KEY = "grant_type";
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";

    private String userName;
    private String password;

    public LoginModel() {
    }

    @Override
    public void getData(LoadListener<PixivOAuthResponse> listener) {
        HashMap<String, String> localHashMap = new HashMap<>();
        localHashMap.put(CLIENT_ID_KEY, CLIENT_ID);
        localHashMap.put(CLIENT_SECRET_KEY, CLIENT_SECRET);
        localHashMap.put(GRANT_TYPE_KEY, GRANT_TYPE);
        localHashMap.put(USERNAME_KEY, userName);
        localHashMap.put(PASSWORD_KEY, password);
        Call<PixivOAuthResponse> call = RestClient.retrofit_OAuthSecure
                .create(OAuthSecureService.class).postAuthToken(localHashMap);
        call.enqueue(new Callback<PixivOAuthResponse>() {
            @Override
            public void onResponse(@NotNull Call<PixivOAuthResponse> call,
                                   @NotNull retrofit2.Response<PixivOAuthResponse> response) {
                PixivOAuthResponse pixivOAuthResponse = response.body();
                if (pixivOAuthResponse != null) {
                    if (pixivOAuthResponse.getResponse() != null && pixivOAuthResponse.getResponse().getAccess_token() != null) {
                        //将登陆后的账号信息保存到本地
                        LocalData.saveLocalMessage(pixivOAuthResponse, password);
                        listener.onSuccess(pixivOAuthResponse);
                    } else {
                        listener.onFailed();
                    }
                } else {
                    listener.onFailed();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PixivOAuthResponse> call, @NonNull Throwable throwable) {
                listener.onFailed();
            }
        });
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
