package com.example.administrator.essim.activities;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;

import com.example.administrator.essim.R;
import com.example.administrator.essim.response.Reference;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils_re.LocalData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BatchDownloadService extends IntentService {

    private Context mContext;

    public BatchDownloadService() {
        super("BatchDownloadService");
        mContext = this;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Notification.Builder myBuilder = new Notification.Builder(mContext);
        myBuilder.setSubText("批量下载通知")
                .setTicker("开始下载了")
                //设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示
                .setSmallIcon(R.mipmap.logo)
                //设置默认声音和震动
                .setAutoCancel(true)//点击后取消
                .setWhen(System.currentTimeMillis())//设置通知时间
                .setPriority(Notification.PRIORITY_LOW)//高优先级
                .setVisibility(Notification.VISIBILITY_PUBLIC);
        NotificationManager myManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification myNotification;
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel mChannel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_LOW);
            myManager.createNotificationChannel(mChannel);
            myBuilder.setChannelId("channel_id");
        }
        //android5.0加入了一种新的模式Notification的显示等级，共有三种：
        //VISIBILITY_PUBLIC  只有在没有锁屏时会显示通知
        //VISIBILITY_PRIVATE 任何情况都会显示通知
        //VISIBILITY_SECRET  在安全锁和没有锁屏的情况下显示通知
        for (int i = 0; i < Reference.downloadList.size(); i++) {
            if (Reference.downloadList.get(i).getPage_count() == 1) {
                myBuilder.setContentText("单个进度1/1");
                myBuilder.setProgress(Reference.downloadList.size(), i + 1, false);
                myBuilder.setContentTitle("总进度: " + String.valueOf(i + 1) + "/" + String.valueOf(Reference.downloadList.size()));
                myNotification = myBuilder.build();
                myManager.notify(100001, myNotification);
                File file = Common.generatePictureFile(mContext, Reference.downloadList.get(i), 0,
                        LocalData.getFileNameStyle(),0);
                if (!file.exists()) {
                    downloadToLocalSD(Reference.downloadList.get(i).getMeta_single_page().getOriginal_image_url(), file,
                            String.valueOf(Reference.downloadList.get(i).getId()));
                }
            } else {
                for (int j = 0; j < Reference.downloadList.get(i).getPage_count(); j++) {
                    myBuilder.setContentText("单个进度" + String.valueOf(j + 1) + "/" + Reference.downloadList.get(i).getPage_count());
                    myBuilder.setProgress(Reference.downloadList.size(), i + 1, false);
                    myBuilder.setContentTitle("总进度: " + String.valueOf(i + 1) + "/" + String.valueOf(Reference.downloadList.size()));
                    myNotification = myBuilder.build();
                    myManager.notify(100001, myNotification);
                    File file = Common.generatePictureFile(mContext, Reference.downloadList.get(i), j,
                            LocalData.getFileNameStyle(), 0);
                    if (!file.exists()) {
                        downloadToLocalSD(Reference.downloadList.get(i).getMeta_pages().get(j).getImage_urls().getOriginal(), file,
                                String.valueOf(Reference.downloadList.get(i).getId()));
                    }
                }
            }

        }
        myBuilder.setContentText("下载完成！");
        myNotification = myBuilder.build();
        myManager.notify(100001, myNotification);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Common.showLog("开始了任务");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Common.showLog("结束了任务");
    }

    private void downloadToLocalSD(String pixicUrl, File realFile, String id) {
        InputStream inputStream = null;
        try {
            FileOutputStream outputStream = new FileOutputStream(realFile);
            URL url = new URL(pixicUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Referer", "https://www.pixiv.net/member_illust.php?mode=medium&illust_id=" + id);
            connection.connect();
            inputStream = connection.getInputStream();
            int len;
            byte[] data = new byte[1024];
            while ((len = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, len);
            }
            outputStream.close();
            Common.sendBroadcast(mContext, realFile);   //通知相册更新最新下载的图片
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
