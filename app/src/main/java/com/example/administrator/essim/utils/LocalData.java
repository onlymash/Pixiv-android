package com.example.administrator.essim.utils;

import android.content.SharedPreferences;

import com.example.administrator.essim.response.IllustsBean;
import com.example.administrator.essim.response.PixivOAuthResponse;
import com.example.administrator.essim.response.ViewHistory;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class LocalData {

    private static final int HISTORY_SIZE = 5;
    /**
     * 登陆成功后将账户信息保存到本地
     *
     * @param pixivOAuthResponse pixivOAuthResponse
     * @param password           password
     */
    public static void saveLocalMessage(PixivOAuthResponse pixivOAuthResponse, String password) {
        String headerImage = "";
        int fileNameStyle = 0;
        if (Common.getLocalDataSet().getString("header_img_path", "").length() != 0) {
            //清空本地数据之前，先保存header图片的路径
            headerImage = Common.getLocalDataSet().getString("header_img_path", "");
        }
        if (Common.getLocalDataSet().getInt("file_name_style", 0) != 0) {
            fileNameStyle = Common.getLocalDataSet().getInt("file_name_style", 0);
        }

        SharedPreferences.Editor editor = Common.getLocalDataSet().edit();
        editor.clear();
        editor.apply();
        //清空本地数据之后，重新保存header图片的路径和文件命名类型
        editor.putString("header_img_path", headerImage);
        editor.putInt("file_name_style", fileNameStyle);
        editor.apply();

        String localStringBuilder = "Bearer " +
                pixivOAuthResponse.getResponse().getAccess_token();
        editor.putString("Authorization", localStringBuilder);
        editor.putInt("userid", pixivOAuthResponse.getResponse().getUser().getId());
        editor.putBoolean("islogin", true);
        editor.putBoolean("ispremium", pixivOAuthResponse.getResponse().getUser().isIs_premium());
        editor.putString("useraccount", pixivOAuthResponse.getResponse().getUser().getAccount());
        editor.putString("username", pixivOAuthResponse.getResponse().getUser().getName());
        editor.putString("password", password);
        editor.putString("email", pixivOAuthResponse.getResponse().getUser().getMail_address());
        editor.putString("useremail", pixivOAuthResponse.getResponse().getUser().getMail_address());
        editor.putString("hearurl", pixivOAuthResponse.getResponse().getUser().getProfile_image_urls().getPx_170x170());
        editor.putBoolean("is_origin_pic", true);
        editor.putString("download_path", "/storage/emulated/0/PixivPictures");
        editor.apply();
    }

    public static String getToken() {
        return Common.getLocalDataSet().getString("Authorization", "");
    }

    public static String getUserName() {
        return Common.getLocalDataSet().getString("username", "");
    }

    public static String getUserAccount() {
        return Common.getLocalDataSet().getString("useraccount", "");
    }

    public static String getUserPwd() {
        return Common.getLocalDataSet().getString("password", "");
    }

    public static void saveViewHistory(IllustsBean illustsBean) {
        List<ViewHistory> isSaved = DataSupport.where("illust_id=?",
                String.valueOf(illustsBean.getId())).find(ViewHistory.class);
        if (isSaved != null && isSaved.size() != 0) {
            /*isSaved.get(0).setView_time(Common.getTime(String.valueOf(System.currentTimeMillis())));
            isSaved.get(0).save();*/
            ViewHistory book = new ViewHistory();
            book.setView_time(System.currentTimeMillis());
            book.updateAll("illust_id=?", String.valueOf(illustsBean.getId()));
        } else {
            List<ViewHistory> list = new ArrayList<>();
            list.addAll(DataSupport.order("view_time desc").find(ViewHistory.class));
            if(list.size() >= HISTORY_SIZE){
                list.get(list.size() - 1).delete();
            }
            ViewHistory viewHistory = new ViewHistory(
                    String.valueOf(illustsBean.getId()),
                    illustsBean.getUser().getName(),
                    illustsBean.getTitle(),
                    System.currentTimeMillis(),
                    String.valueOf(illustsBean.getPage_count()),
                    illustsBean.getImage_urls().getMedium());
            viewHistory.save();
        }

    }
}
