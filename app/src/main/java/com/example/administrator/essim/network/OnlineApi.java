package com.example.administrator.essim.network;

import com.example.administrator.essim.response.AnnounceResponse;
import com.example.administrator.essim.response.OnlineResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface OnlineApi {

    @GET("/api/online.php")
    Call<OnlineResponse> getOnlineCount();

    @GET("/api/announcement.json")
    Call<AnnounceResponse> getPassword();
}
