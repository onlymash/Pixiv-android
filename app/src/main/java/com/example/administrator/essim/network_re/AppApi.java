package com.example.administrator.essim.network_re;

import com.example.administrator.essim.response.BookmarkAddResponse;
import com.example.administrator.essim.response.TrendingtagResponse;
import com.example.administrator.essim.response_re.ArticleResponse;
import com.example.administrator.essim.response_re.FollowResponse;
import com.example.administrator.essim.response_re.IllustListResponse;
import com.example.administrator.essim.response_re.SingleIllustResponse;
import com.example.administrator.essim.response_re.UserResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface AppApi {

    /**
     * 获取登陆页面的背景图列表
     *
     * @return
     */
    @GET("/v1/walkthrough/illusts")
    Observable<IllustListResponse> getLoginBg();


    /**
     * 推荐榜单
     *
     * @param token
     * @param filter
     * @param include_ranking_illusts
     * @return
     */
    @GET("/v1/illust/recommended")
    Observable<IllustListResponse> getRecmdIllust(@Header("Authorization") String token,
                                                  @Query("filter") String filter,
                                                  @Query("include_ranking_illusts") boolean include_ranking_illusts);

    /**
     * 获取用户投稿列表
     *
     * @param token
     * @param filter
     * @param user_id
     * @param type
     * @return
     */
    @GET("/v1/user/illusts")
    Observable<IllustListResponse> getUserWorks(@Header("Authorization") String token,
                                                @Query("filter") String filter,
                                                @Query("user_id") int user_id,
                                                @Query("type") String type);

    /**
     * 获取用户收藏列表
     *
     * @param token
     * @param user_id
     * @param restrict
     * @return
     */
    @GET("/v1/user/bookmarks/illust")
    Observable<IllustListResponse> getUserCollections(@Header("Authorization") String token,
                                                      @Query("user_id") int user_id,
                                                      @Query("restrict") String restrict);


    @GET
    Observable<IllustListResponse> getNext(@Header("Authorization") String token,
                                           @Url String nextUrl);

    /**
     * 获取下一批文章
     * @param token
     * @param nextUrl
     * @return
     */
    @GET
    Observable<ArticleResponse> getNextArticle(@Header("Authorization") String token,
                                           @Url String nextUrl);

    @GET
    Observable<FollowResponse> getNextFollowUser(@Header("Authorization") String token,
                                                 @Url String nextUrl);


    /**
     * 排行榜
     *
     * @param token
     * @param filter
     * @param mode
     * @return
     */
    @GET("/v1/illust/ranking")
    Observable<IllustListResponse> getRank(@Header("Authorization") String token,
                                           @Query("filter") String filter,
                                           @Query("mode") String mode);

    @GET("/v1/user/detail")
    Observable<UserResponse> getUserDetail(@Header("Authorization") String token,
                                           @Query("filter") String filter,
                                           @Query("user_id") int user_id);


    /**
     * 获取我关注的人
     *
     * @param token
     * @param filter
     * @param user_id
     * @param restrict
     * @return
     */
    @GET("/v1/user/following")
    Observable<FollowResponse> getFollowUser(@Header("Authorization") String token,
                                             @Query("filter") String filter,
                                             @Query("user_id") int user_id,
                                             @Query("restrict") String restrict);


    @GET("/v1/trending-tags/illust")
    Observable<TrendingtagResponse> getHotTags(@Header("Authorization") String token,
                                               @Query("filter") String filter);

    @FormUrlEncoded
    @POST("/v1/user/follow/add")
    Observable<BookmarkAddResponse> postFollow(@Header("Authorization") String token,
                                               @Field("user_id") int user_id,
                                               @Field("restrict") String restrict);



    @FormUrlEncoded
    @POST("/v1/user/follow/delete")
    Observable<BookmarkAddResponse> postUnFollow(@Header("Authorization") String token,
                                               @Field("user_id") int user_id);


    /**
     * 获取自己关注用户的最新投稿
     * @param token
     * @param restrict
     * @return
     */
    @GET("/v2/illust/follow")
    Observable<IllustListResponse> getNewWorks(@Header("Authorization") String token,
                                             @Query("restrict") String restrict);

    @GET("/v1/spotlight/articles")
    Observable<ArticleResponse> getArticles(@Header("Authorization") String token,
                                            @Query("filter") String filter,
                                            @Query("category") String category);

    @GET("/v1/illust/detail")
    Observable<SingleIllustResponse> getSingleIllust(@Header("Authorization") String token,
                                                 @Query("filter") String filter,
                                                 @Query("illust_id") int illust_id);
}
