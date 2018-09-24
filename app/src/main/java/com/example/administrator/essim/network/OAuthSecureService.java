package com.example.administrator.essim.network;

import com.example.administrator.essim.response.PixivOAuthResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OAuthSecureService {

    @FormUrlEncoded
    @POST("/auth/token")
    Call<PixivOAuthResponse> postAuthToken(@FieldMap Map<String, String> paramMap);

    @FormUrlEncoded
    @POST("/auth/token")
    Call<PixivOAuthResponse> postRefreshAuthToken(@Field("client_id") String client_id,
                                                  @Field("client_secret") String client_secret,
                                                  @Field("grant_type") String grant_type,
                                                  @Field("refresh_token") String refresh_token,
                                                  @Field("device_token") String device_token,
                                                  @Field("get_secure_url") boolean get_secure_url,
                                                  @Field("include_policy") boolean include_policy);

}
