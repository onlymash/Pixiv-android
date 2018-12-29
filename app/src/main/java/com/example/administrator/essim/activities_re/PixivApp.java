package com.example.administrator.essim.activities_re;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.tencent.stat.StatService;

import org.litepal.LitePalApplication;

public class PixivApp extends LitePalApplication {


    static {
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) ->
                new ClassicsFooter(context).setDrawableSize(16));
    }


    public PixivApp(){

    }

    @Override
    public void onCreate() {
        super.onCreate();
        StatService.registerActivityLifecycleCallbacks(PixivApp.this);
    }
}
