package com.example.administrator.essim.activities;

import com.example.administrator.essim.network.RestClient;
import com.tencent.stat.StatService;

import org.litepal.LitePalApplication;


public class PixivApplication extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        StatService.registerActivityLifecycleCallbacks(PixivApplication.this);
        RestClient.init();
    }
}
