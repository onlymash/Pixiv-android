package com.example.administrator.essim.utils_re;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities_re.PixivApp;

import static androidx.core.app.NotificationCompat.PRIORITY_DEFAULT;
import static androidx.core.app.NotificationCompat.VISIBILITY_SECRET;

public class NotifiUtil extends ContextWrapper {

    public static final String CHANNEL_ID = "default";
    private static final String CHANNEL_NAME = "Default Channel";
    private static final String CHANNEL_DESCRIPTION = "this is default channel!";
    private static NotificationManager mManager;

    public NotifiUtil(Context base) {
        super(base);
    }

    @TargetApi(Build.VERSION_CODES.O)
    public static void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.canBypassDnd();//是否绕过请勿打扰模式
            channel.setLockscreenVisibility(VISIBILITY_SECRET);//锁屏显示通知
            channel.canShowBadge();//桌面launcher的消息角标
            channel.getGroup();//获取通知取到组
            channel.setBypassDnd(true);//设置可绕过  请勿打扰模式
            getManager().createNotificationChannel(channel);
        }
    }

    private static NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) PixivApp.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public static void showStartNotification(int id, String title, String content) {
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(PixivApp.getContext(), CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(PixivApp.getContext());
            builder.setPriority(PRIORITY_DEFAULT);
        }
        builder.setSubText("正在下载");
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_file_download_black_24dp);
        //点击自动删除通知
        builder.setAutoCancel(true);
        getManager().notify(id, builder.build());
    }

    public static void showFinishNotification(int id, String title, String content) {
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(PixivApp.getContext(), CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(PixivApp.getContext());
            builder.setPriority(PRIORITY_DEFAULT);
        }
        builder.setSubText("已完成");
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_file_download_black_24dp);
        //点击自动删除通知
        builder.setAutoCancel(true);
        getManager().notify(id, builder.build());
    }
}
