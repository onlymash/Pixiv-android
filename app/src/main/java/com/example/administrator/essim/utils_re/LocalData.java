package com.example.administrator.essim.utils_re;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.administrator.essim.activities_re.PixivApp;
import com.example.administrator.essim.response.ViewHistory;
import com.example.administrator.essim.response_re.IllustsBean;
import com.example.administrator.essim.response_re.LoginResponse;

import org.litepal.crud.DataSupport;

import java.util.List;


public class LocalData {

    private static final int HISTORY_SIZE = 100;



    public static SharedPreferences getLocalDataSet() {
        return PreferenceManager.getDefaultSharedPreferences(PixivApp.getContext());
    }

    public static boolean isLogin(){
        return getLocalDataSet().getBoolean("islogin", false);
    }

    public static String getToken() {

        return getLocalDataSet().getString("Authorization", "");
    }

    public static void saveLocalMessage(LoginResponse pixivOAuthResponse, String password) {
        SharedPreferences sharedPreferences = getLocalDataSet();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        String localStringBuilder = "Bearer " +
                pixivOAuthResponse.getResponse().getAccess_token();
        editor.putString("Authorization", localStringBuilder);
        editor.putString("device_token", pixivOAuthResponse.getResponse().getDevice_token());
        editor.putString("refresh_token", pixivOAuthResponse.getResponse().getRefresh_token());
        editor.putInt("userid", pixivOAuthResponse.getResponse().getUser().getId());
        editor.putBoolean("islogin", true);
        editor.putBoolean("ispremium", pixivOAuthResponse.getResponse().getUser().isIs_premium());
        editor.putString("useraccount", pixivOAuthResponse.getResponse().getUser().getAccount());
        editor.putString("username", pixivOAuthResponse.getResponse().getUser().getName());
        editor.putString("password", password);
        editor.putString("email", pixivOAuthResponse.getResponse().getUser().getMail_address());
        editor.putString("headurl", pixivOAuthResponse.getResponse().getUser().getProfile_image_urls().getPx_170x170());
        editor.putBoolean("is_origin_pic", true);
        editor.putString("download_path", "/storage/emulated/0/PixivPictures");
        editor.putInt("file_name_style", FileUtil.NAME_STYLE_0);
        editor.putLong("last_token_time", System.currentTimeMillis());
        editor.apply();
    }

    public static void setFileNameStyle(int i){
        SharedPreferences sharedPreferences = getLocalDataSet();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("file_name_style", i);
        editor.apply();
    }

    public static int getFileNameStyle(){
        return getLocalDataSet().getInt("file_name_style", 0);
    }

    public static void setDeviceToken(String deviceToken){
        SharedPreferences sharedPreferences = getLocalDataSet();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("device_token", deviceToken);
        editor.apply();
    }

    public static void saveLocalMessage(LoginResponse pixivOAuthResponse) {
        SharedPreferences sharedPreferences = getLocalDataSet();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        String localStringBuilder = "Bearer " +
                pixivOAuthResponse.getResponse().getAccess_token();
        editor.putString("Authorization", localStringBuilder);
        editor.putString("device_token", pixivOAuthResponse.getResponse().getDevice_token());
        editor.putString("refresh_token", pixivOAuthResponse.getResponse().getRefresh_token());
        editor.putInt("userid", pixivOAuthResponse.getResponse().getUser().getId());
        editor.putBoolean("islogin", true);
        editor.putBoolean("ispremium", pixivOAuthResponse.getResponse().getUser().isIs_premium());
        editor.putString("useraccount", pixivOAuthResponse.getResponse().getUser().getAccount());
        editor.putString("username", pixivOAuthResponse.getResponse().getUser().getName());
        editor.putString("email", pixivOAuthResponse.getResponse().getUser().getMail_address());
        editor.putString("headurl", pixivOAuthResponse.getResponse().getUser().getProfile_image_urls().getPx_170x170());
        editor.putBoolean("is_origin_pic", true);
        editor.putString("download_path", "/storage/emulated/0/PixivPictures");
        editor.putInt("file_name_style", FileUtil.NAME_STYLE_0);
        editor.putLong("last_token_time", System.currentTimeMillis());
        editor.apply();
    }

    public static String getHeadImage(){
        return getLocalDataSet().getString("headurl", "");
    }

    public static long getLastTokenTime(){
        return getLocalDataSet().getLong("last_token_time", 0);
    }

    public static int getUserID(){
        return getLocalDataSet().getInt("userid", 0);
    }

    public static String getDeviceToken(){
        return getLocalDataSet().getString("device_token", "");
    }

    public static String getRefreshToken(){
        return getLocalDataSet().getString("refresh_token", "");
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

    public static String getUserName() {
        return getLocalDataSet().getString("username", "");
    }

    public static String getDownloadPath() {
        return getLocalDataSet().getString("download_path", "/storage/emulated/0/PixivPictures");
    }

    public static String getUserAccount() {
        return getLocalDataSet().getString("useraccount", "");
    }

    public static String getEmail() {
        return getLocalDataSet().getString("email", "");
    }

    public static String getUserPwd() {
        return getLocalDataSet().getString("password", "");
    }

    public static boolean getIsVIP() {
        return getLocalDataSet().getBoolean("ispremium", false);
    }
}
