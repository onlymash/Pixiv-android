package com.example.administrator.essim.network

import com.example.administrator.essim.response.PixivAccountsResponse
import com.example.administrator.essim.response.UpdateInfoResponse

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface AccountPixivService {

    @FormUrlEncoded
    @POST("/api/provisional-accounts/create")
    fun createProvisionalAccount(@Field("user_name") paramString1: String,
                                 @Field("ref") paramString2: String,
                                 @Header("Authorization") paramString3: String): Call<PixivAccountsResponse>

    @FormUrlEncoded
    @POST("/api/account/edit")
    fun updateUserInfo(@Header("Authorization") paramString: String,
                       @Field("new_mail_address") new_mail_address: String,
                       @Field("new_user_account") new_user_account: String,
                       @Field("current_password") current_password: String,
                       @Field("new_password") new_password: String): Call<UpdateInfoResponse>
}