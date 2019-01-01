package com.example.administrator.essim.network


import com.example.administrator.essim.response.*
import com.example.administrator.essim.response_re.IllustListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface AppApiPixivService {

    //GET /v1/search/illust?filter=for_android&word=VOCALOID&sort=date_desc&search_target=partial_match_for_tags HTTP/1.1
    @GET("/v1/search/illust?filter=for_android")
    fun getSearchIllust(@Query("word") paramString1: String,
                        @Query("sort") paramString2: String,
                        @Query("search_target") paramString3: String,
                        @Query("bookmark_num") paramInteger: Int?,
                        @Query("duration") paramString4: String,
                        @Header("Authorization") paramString5: String): Call<SearchIllustResponse>

    @FormUrlEncoded
    @POST("/v2/illust/bookmark/add")
    fun postLikeIllust(@Header("Authorization") paramString1: String,
                       @Field("illust_id") paramLong: Long,
                       @Field("restrict") paramString2: String,
                       @Field("tags[]") paramList: List<String>): Call<BookmarkAddResponse>

    //v1/spotlight/articles?filter=for_android&category=all HTTP/1.1
    @GET("/v1/spotlight/articles?filter=for_android&category=all")
    fun getSpecialArtical(@Header("Authorization") paramString1: String,
                       @Query("offset") paramInt: Int): Call<SpecialArticalResponse>


    // https://app-api.pixiv.net/v1/user/bookmarks/illust?user_id=31655571&restrict=public
    @GET("/v1/user/bookmarks/illust")
    fun getLikeIllust(@Header("Authorization") paramString1: String,
                      @Query("user_id") paramLong: Long,
                      @Query("restrict") paramString2: String,
                      @Query("tag") paramString3: String): Call<UserIllustsResponse>

    // GET /v1/user/bookmark-tags/illust?user_id=32483920&restrict=public
    // GET /v1/user/bookmark-tags/illust?user_id=32483920&restrict=private
    // 获取自己搜集的标签
    @GET("/v1/user/bookmark-tags/illust")
    fun getBookedTags(@Header("Authorization") paramString1: String,
                      @Query("user_id") paramLong: Long,
                      @Query("restrict") paramString2: String): Call<AllBookmarkTagResponse>

    @FormUrlEncoded
    @POST("v1/illust/comment/add")
    fun postIllustComment(@Header("Authorization") paramString1: String,
                          @Field("illust_id") paramLong: Long,
                          @Field("comment") paramString2: String,
                          @Field("parent_comment_id") paramInteger: Int?): Call<ResponseBody>

    @GET("/v1/user/following?filter=for_android")
    fun getUserFollowing(@Header("Authorization") paramString1: String,
                         @Query("user_id") paramLong: Long,
                         @Query("restrict") paramString2: String): Call<SearchUserResponse>


    //GET /v1/user/recommended?filter=for_android HTTP/1.1
    @GET("/v1/user/recommended?filter=for_android")
    fun getRecommendUser(@Header("Authorization") paramString1: String): Call<SearchUserResponse>

    @GET("v1/ugoira/metadata")
    fun getUgoiraMetadata(@Header("Authorization") paramString: String,
                          @Query("illust_id") paramLong: Long): Call<UgoiraMetadataResponse>

    @GET("/v1/user/illusts?filter=for_ios")
    fun getUserIllusts(@Header("Authorization") paramString1: String,
                       @Query("user_id") paramLong: Long,
                       @Query("type") paramString2: String): Call<UserIllustsResponse>

    @GET("/v1/illust/ranking?filter=for_ios")
    fun getIllustRanking(@Header("Authorization") paramString1: String,
                         @Query("mode") paramString2: String,
                         @Query("date") paramString3: String?): Call<IllustRankingResponse>

    @FormUrlEncoded
    @POST("/v1/user/follow/add")
    fun postFollowUser(@Header("Authorization") paramString1: String,
                       @Field("user_id") paramLong: Long,
                       @Field("restrict") paramString2: String): Call<BookmarkAddResponse>

    @GET("/v2/illust/follow")
    fun getFollowIllusts(@Header("Authorization") paramString1: String, @Query("restrict") paramString2: String): Call<IllustfollowResponse>

    @FormUrlEncoded
    @POST("/v1/user/follow/delete")
    fun postUnfollowUser(@Header("Authorization") paramString: String,
                         @Field("user_id") paramLong: Long): Call<ResponseBody>

    //POST /v1/user/profile/edit HTTP/1.1
    @Multipart
    @POST("/v1/user/profile/edit")
    fun changeHeadImg(@Header("Authorization") paramString: String,
                      @Part photo: MultipartBody.Part, @Part("description") description: RequestBody): Call<ResponseBody>

    // v1/user/related?filter=for_android&seed_user_id=1589657
    @GET("/v1/user/related?filter=for_android")
    fun getRelatedAuthor(@Header("Authorization") paramString: String,
                         @Field("seed_user_id") paramLong: Long): Call<SearchUserResponse>

    // GET /v2/illust/bookmark/detail?illust_id=69350247 HTTP/1.1
    @GET("/v2/illust/bookmark/detail")
    fun getBookmarkDetail(@Header("Authorization") paramString: String,
                          @Query("illust_id") paramLong: Long): Call<BookmarkDetailResponse>

    @GET
    fun getNextComment(@Header("Authorization") paramString1: String,
                       @Url paramString2: String): Call<IllustCommentsResponse>

    @GET
    fun getNextTags(@Header("Authorization") paramString1: String,
                    @Url paramString2: String): Call<AllBookmarkTagResponse>

    @GET("/v1/search/user?filter=for_ios")
    fun getSearchUser(@Header("Authorization") paramString1: String,
                      @Query("word") paramString2: String): Call<SearchUserResponse>

    @GET("/v1/illust/detail?filter=for_ios")
    fun getIllust(@Header("Authorization") paramString: String, @Query("illust_id") paramLong: Long): Call<IllustDetailResponse>


    @GET("/v1/search/autocomplete")
    fun getSearchAutoCompleteKeywords(@Header("Authorization") paramString1: String,
                                      @Query("word") paramString2: String): Call<PixivResponse>

    @GET
    fun getNextUser(@Header("Authorization") paramString1: String,
                    @Url paramString2: String): Call<SearchUserResponse>

    @FormUrlEncoded
    @POST("/v1/illust/bookmark/delete")
    fun postUnlikeIllust(@Header("Authorization") paramString: String,
                         @Field("illust_id") paramLong: Long): Call<ResponseBody>

    @GET("/v1/illust/comments")
    fun getIllustComments(@Header("Authorization") paramString: String,
                          @Query("illust_id") paramLong: Long): Call<IllustCommentsResponse>

    //https://app-api.pixiv.net/v1/user/detail?filter=for_android&user_id=31655571
    @GET("/v1/user/detail?filter=for_android")
    fun getUserDetail(@Header("Authorization") paramString: String,
                      @Query("user_id") paramLong: Long): Call<UserDetailResponse>

    ///v1/illust/recommended?filter=for_android&include_ranking_illusts=true&include_privacy_policy=true
    @GET("/v1/illust/recommended?content_type=illust&filter=for_ios&include_ranking_label=true")
    fun getRecommend(@Header("Authorization") paramString: String): Call<RecommendResponse>

    @GET
    fun getNext(@Header("Authorization") paramString1: String,
                @Url paramString2: String): Call<RecommendResponse>

    //GET /v1/trending-tags/illust?filter=for_android HTTP/1.1
    @GET("/v1/trending-tags/illust?filter=for_android")
    fun getIllustTrendTags(@Header("Authorization") paramString: String): Call<TrendingtagResponse>

    @GET
    fun getNextUserIllusts(@Header("Authorization") paramString1: String,
                           @Url paramString2: String): Call<UserIllustsResponse>



    @GET("/pixiv/v1/")
    fun getSpecialCollection(@Query("type") cate: String,
                             @Query("page") page: Int): Call<SpecialCollectionResponse>

    @GET("/pixiv/v1/?type=showcase")
    fun getCollecDetail(@Query("id") cate: String): Call<CollectionResponse>


    // /v1/spotlight/articles?filter=for_android&category=all


    @GET("/v1/spotlight/articles?filter=for_android&category=all")
    fun getSpecialCollection(@Header("Authorization") paramString: String): Call<PixivCollectionResponse>

    @GET
    fun getNextSpecialCollection(@Header("Authorization") paramString: String, @Url paramString2: String):
            Call<PixivCollectionResponse>

    /*@GET("/pixiv/v1/?type=showcase")
    fun getCollecDetail(@Query("id") cate: String): Call<CollectionResponse>*/

    @GET("/v1/user/me/state")
    fun getMyAccountState(): Call<AccountStateResponse>
}
