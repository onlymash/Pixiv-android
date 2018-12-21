package com.example.administrator.essim.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.administrator.essim.activities.PixivApplication;
import com.example.administrator.essim.response.PixivOAuthResponse;
import com.example.administrator.essim.response.ViewHistory;
import com.example.administrator.essim.response_re.IllustsBean;

import org.litepal.crud.DataSupport;

import java.util.List;

public class LocalData {

    private static final int HISTORY_SIZE = 100;

    /**
     * 登陆成功后将账户信息保存到本地
     *
     * @param pixivOAuthResponse pixivOAuthResponse
     * @param password           password
     */
    public static void saveLocalMessage(PixivOAuthResponse pixivOAuthResponse, String password) {
        SharedPreferences sharedPreferences = getLocalDataSet();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        String headerImage = "";
        int fileNameStyle = 0;
        if (sharedPreferences.getString("header_img_path", "").length() != 0) {
            //清空本地数据之前，先保存header图片的路径
            headerImage = sharedPreferences.getString("header_img_path", "");
        }
        if (sharedPreferences.getInt("file_name_style", 0) != 0) {
            fileNameStyle = sharedPreferences.getInt("file_name_style", 0);
        }


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

    public static SharedPreferences getLocalDataSet() {
        return PreferenceManager.getDefaultSharedPreferences(PixivApplication.getContext());
    }

    public static String getToken() {
        return getLocalDataSet().getString("Authorization", "");
    }

    public static String getR18Pwd() {
        return getLocalDataSet().getString("r18_password", "");
    }

    public static void setNoProxy(boolean isProxy){
        SharedPreferences sharedPreferences = getLocalDataSet();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isProxy", isProxy);
        editor.apply();
    }

    public static Boolean getNoProxy() {
        return getLocalDataSet().getBoolean("isProxy", true);
    }

    public static void setR18Pwd(String pwd){
        SharedPreferences sharedPreferences = getLocalDataSet();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("r18_password", pwd);
        editor.apply();
    }

    public static String getRealR18Pwd() {
        return getLocalDataSet().getString("real_r18_password", "");
    }

    public static void setRealR18Pwd(String pwd){
        SharedPreferences sharedPreferences = getLocalDataSet();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("real_r18_password", pwd);
        editor.apply();
    }

    public static String getUserName() {
        return getLocalDataSet().getString("username", "");
    }

    public static String getUserAccount() {
        return getLocalDataSet().getString("useraccount", "");
    }

    public static String getUserPwd() {
        return getLocalDataSet().getString("password", "");
    }

    public static boolean getIsVIP() {
        return getLocalDataSet().getBoolean("ispremium", false);
    }

    public static int getUserID() {
        return getLocalDataSet().getInt("userid", 0);
    }

    public static int getFileNameStyle() {
        return getLocalDataSet().getInt("file_name_style", 0);
    }

    public static int getNetworkStyle() {
        return getLocalDataSet().getInt("network_style", 0);
    }


    public static String getDownloadPath() {
        return getLocalDataSet().getString("download_path", "/storage/emulated/0/PixivPictures");
    }

    public static String getEmail() {
        return getLocalDataSet().getString("email", "");
    }

    public static void saveViewHistory(IllustsBean illustsBean) {
        List<ViewHistory> isSaved = DataSupport.where("illust_id=?",
                String.valueOf(illustsBean.getId())).find(ViewHistory.class);
        if (isSaved != null && isSaved.size() != 0) {
            ViewHistory book = new ViewHistory();
            book.setView_time(System.currentTimeMillis());
            book.updateAll("illust_id=?", String.valueOf(illustsBean.getId()));
        } else {
            List<ViewHistory> list = DataSupport.order("view_time desc").find(ViewHistory.class);
            if(list != null && list.size() >= HISTORY_SIZE){
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
