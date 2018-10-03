package com.example.administrator.essim.activities;

import com.tencent.stat.StatService;

import org.litepal.LitePalApplication;


public class PixivApplication extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        StatService.registerActivityLifecycleCallbacks(PixivApplication.this);
    }
}
