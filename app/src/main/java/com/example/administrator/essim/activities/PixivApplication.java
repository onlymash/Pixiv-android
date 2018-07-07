package com.example.administrator.essim.activities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import org.litepal.LitePalApplication;

public class PixivApplication extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //获取读写本地文件的权限
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
}
