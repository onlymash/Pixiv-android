package com.example.administrator.essim.utils_re;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.administrator.essim.activities_re.PixivApp;
import com.example.administrator.essim.response_re.IllustsBean;
import com.example.administrator.essim.utils.Common;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.listener.DownloadListener2;

import java.io.File;
import java.util.HashMap;

public class FileDownload {

    private static final String MAP_KEY = "Referer";
    //private static final String MAP_VALUE_HEAD = "https://www.pixiv.net/member_illust.php?mode=medium&illust_id=";
    private static final String IMAGE_REFERER = "https://app-api.pixiv.net/";


    public static void downloadIllust(IllustsBean illustsBean){
        if(illustsBean.getPage_count() == 1) {
            String fileName = illustsBean.getTitle() + "_" + String.valueOf(illustsBean.getId()) + ".png";
            File realFie = new File(LocalData.getDownloadPath(), fileName);
            if(realFie.exists()){
                Common.showToast("该文件已存在");
            }else {
                String url = illustsBean.getMeta_single_page().getOriginal_image_url();
                DownloadTask.Builder builder = new DownloadTask.Builder(url, new File(LocalData.getDownloadPath()))
                        .setFilename(fileName)
                        .setMinIntervalMillisCallbackProcess(30)
                        .setPassIfAlreadyCompleted(false);
                builder.addHeader(MAP_KEY, IMAGE_REFERER);
                DownloadTask task = builder.build();
                Common.showToast("开始下载");
                task.enqueue(new DownloadListener2() {
                    @Override
                    public void taskStart(@NonNull DownloadTask task) {
                        NotifiUtil.showStartNotification("作品名：" + illustsBean.getTitle(), "第1p， 共1p");
                    }

                    @Override
                    public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause) {
                        Common.showToast("下载完成");
                        NotifiUtil.showFinishNotification("作品名：" + illustsBean.getTitle(), "共1张");
                        Common.sendBroadcast(PixivApp.getContext(), realFie);
                    }
                });
            }
        }else {
            for (int i = 0; i < illustsBean.getMeta_pages().size(); i++) {
                final int position = i;
                String fileName = illustsBean.getTitle() + "_" + String.valueOf(illustsBean.getId()) + "_p" + String.valueOf(i) + ".png";
                File realFie = new File(LocalData.getDownloadPath(), fileName);
                if(realFie.exists()){
                    Common.showToast("该文件已存在");
                }else {
                    String url = illustsBean.getMeta_pages().get(i).getImage_urls().getOriginal();
                    DownloadTask.Builder builder = new DownloadTask.Builder(url, new File(LocalData.getDownloadPath()))
                            .setFilename(fileName)
                            .setMinIntervalMillisCallbackProcess(30)
                            .setPassIfAlreadyCompleted(false);
                    builder.addHeader(MAP_KEY, IMAGE_REFERER);
                    DownloadTask task = builder.build();
                    Common.showToast("开始下载");
                    task.enqueue(new DownloadListener2() {
                        @Override
                        public void taskStart(@NonNull DownloadTask task) {

                            NotifiUtil.showStartNotification("作品名：" + illustsBean.getTitle(),
                                    "第" + String.valueOf((position+1)) +"p， 共" + String.valueOf(illustsBean.getMeta_pages().size()) + "p");
                        }

                        @Override
                        public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause) {
                            Common.showToast("下载完成");
                            NotifiUtil.showFinishNotification("作品名：" + illustsBean.getTitle(), "共"+
                                            String.valueOf(illustsBean.getMeta_pages().size()) + "张");
                            Common.sendBroadcast(PixivApp.getContext(), realFie);
                        }
                    });
                }
            }
        }
    }
}
