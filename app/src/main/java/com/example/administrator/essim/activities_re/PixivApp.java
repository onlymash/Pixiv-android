package com.example.administrator.essim.activities_re;

import com.example.administrator.essim.response_re.IllustsBean;
import com.example.administrator.essim.utils_re.NotifiUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.tencent.stat.StatService;

import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.List;

public class PixivApp extends LitePalApplication {

    public static List<IllustsBean> sIllustsBeans = new ArrayList<>();
    public static List<IllustsBean> downloadList = new ArrayList<>();

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


        NotifiUtil.createNotificationChannel();
    }
}
