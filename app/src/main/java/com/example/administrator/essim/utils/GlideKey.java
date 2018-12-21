package com.example.administrator.essim.utils;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.example.administrator.essim.response_re.IllustsBean;

import java.util.HashMap;

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


    public static GlideUrl getLargeImg(IllustsBean illustsBean, int index){

        Headers header = () -> {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(MAP_KEY, MAP_VALUE_HEAD + illustsBean.getId());
            return hashMap;
        };
        if(illustsBean.getPage_count() > 1){
            return new GlideUrl(illustsBean.getMeta_pages().get(index).getImage_urls().getLarge(), header);
        }else {
            return new GlideUrl(illustsBean.getMeta_single_page().getOriginal_image_url(), header);
        }

    }
}
