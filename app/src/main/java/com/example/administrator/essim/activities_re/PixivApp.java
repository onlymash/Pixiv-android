package com.example.administrator.essim.activities_re;

import android.app.Application;
import android.content.Context;

import com.example.administrator.essim.activities.PixivApplication;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.tencent.stat.StatService;

import org.litepal.LitePalApplication;

public class PixivApp extends LitePalApplication {


    static {
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) ->
                new ClassicsFooter(context).setDrawableSize(13));
    }


    public PixivApp(){

    }

    @Override
    public void onCreate() {
        super.onCreate();
        StatService.registerActivityLifecycleCallbacks(PixivApp.this);
    }
}
