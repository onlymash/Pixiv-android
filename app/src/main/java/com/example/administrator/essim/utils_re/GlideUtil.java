package com.example.administrator.essim.utils_re;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.example.administrator.essim.response_re.FollowResponse;
import com.example.administrator.essim.response_re.IllustsBean;
import com.example.administrator.essim.response_re.UserBean;
import com.example.administrator.essim.utils.Common;

import java.util.HashMap;


/**
 * 为每次图片请求添加Referer
 */
public class GlideUtil {

    private static final String MAP_KEY = "Referer";
    private static final String MAP_VALUE_HEAD = "https://www.pixiv.net/member_illust.php?mode=medium&illust_id=";
    private static final String USER_HEAD_HEAD = "https://app-api.pixiv.net/";

    public static GlideUrl getMediumImg(IllustsBean illustsBean){
        Headers header = () -> {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(MAP_KEY, MAP_VALUE_HEAD + illustsBean.getId());
            return hashMap;
        };
        return new GlideUrl(illustsBean.getImage_urls().getMedium(), header);
    }


    public static GlideUrl getUserHead(FollowResponse.UserPreviewsBean user){
        Headers header = () -> {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(MAP_KEY, USER_HEAD_HEAD);
            return hashMap;
        };
        Common.showLog(USER_HEAD_HEAD + String.valueOf(user.getUser().getId()));
        Common.showLog(user.getUser().getProfile_image_urls().getMedium());
        return new GlideUrl(user.getUser().getProfile_image_urls().getMedium(), header);
    }

    public static GlideUrl getUserHead(UserBean user){
        Headers header = () -> {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(MAP_KEY, USER_HEAD_HEAD);
            return hashMap;
        };
        return new GlideUrl(user.getProfile_image_urls().getMedium(), header);
    }


    public static GlideUrl getArticle(String url){
        Headers header = () -> {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(MAP_KEY, USER_HEAD_HEAD);
            return hashMap;
        };
        return new GlideUrl(url, header);
    }
}
