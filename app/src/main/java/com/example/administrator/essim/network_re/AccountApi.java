package com.example.administrator.essim.network_re;

import com.example.administrator.essim.response_re.LoginResponse;
import com.example.administrator.essim.response_re.NewAccountResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AccountApi {

    @FormUrlEncoded
    @POST("/api/provisional-accounts/create")
    Observable<NewAccountResponse> createAccount(@Header("Authorization") String token,
                                                 @Field("user_name") String user_name,
                                                 @Field("ref") String ref);
}
