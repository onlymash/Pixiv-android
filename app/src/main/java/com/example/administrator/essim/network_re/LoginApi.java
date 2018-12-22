package com.example.administrator.essim.network_re;

import com.example.administrator.essim.response_re.LoginResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginApi {


    /**
     * 登陆
     *
     * @return
     */
    @FormUrlEncoded
    @POST("/auth/token")
    Observable<LoginResponse> tryLogin(@Field("client_id") String client_id,
                                       @Field("client_secret") String client_secret,
                                       @Field("device_token") String device_token,
                                       @Field("get_secure_url") boolean get_secure_url,
                                       @Field("grant_type") String grant_type,
                                       @Field("password") String password,
                                       @Field("username") String username);


    /**
     * 刷新token
     * @param client_id
     * @param client_secret
     * @param grant_type
     * @param refresh_token
     * @param device_token
     * @param get_secure_url
     * @return
     */
    @FormUrlEncoded
    @POST("/auth/token")
    Observable<LoginResponse> refreshToken(@Field("client_id") String client_id,
                                           @Field("client_secret") String client_secret,
                                           @Field("grant_type") String grant_type,
                                           @Field("refresh_token") String refresh_token,
                                           @Field("device_token") String device_token,
                                           @Field("get_secure_url") boolean get_secure_url);
}
