package com.example.administrator.essim.utils;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.administrator.essim.response.IllustsBean;

import java.util.HashMap;
import java.util.Map;

/**
 * 为每次图片请求添加Referer
 */
public class GlideKey {

    private static final String MAP_KEY = "Referer";
    private static final String MAP_VALUE_HEAD = "https://www.pixiv.net/member_illust.php?mode=medium&illust_id=";

    public static GlideUrl getMediumImg(IllustsBean illustsBean){
        Headers header = () -> {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(MAP_KEY, MAP_VALUE_HEAD + illustsBean.getId());
            return hashMap;
        };
        return new GlideUrl(illustsBean.getImage_urls().getMedium(), header);
    }
}
