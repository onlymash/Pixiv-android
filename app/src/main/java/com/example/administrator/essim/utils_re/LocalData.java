package com.example.administrator.essim.utils_re;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.administrator.essim.activities_re.PixivApp;
import com.example.administrator.essim.response_re.LoginResponse;


public class LocalData {

    private static SharedPreferences getLocalDataSet() {
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
        editor.putLong("last_token_time", System.currentTimeMillis());
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
        editor.putLong("last_token_time", System.currentTimeMillis());
        editor.apply();
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
}
