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

public class FileDownload {

    private static final String MAP_KEY = "Referer";
    //private static final String MAP_VALUE_HEAD = "https://www.pixiv.net/member_illust.php?mode=medium&illust_id=";
    private static final String IMAGE_REFERER = "https://app-api.pixiv.net/";


    /**
     * 下载某个作品的全部图片（1p就下载一张图，10p就下载10张图)
     *
     * @param illustsBean
     */
    public static void downloadIllust(IllustsBean illustsBean) {
        if (illustsBean.getPage_count() == 1) {
            downloadSingle(illustsBean);
        } else {
            downloadMulti(illustsBean);
        }
    }

    private static void downloadSingle(IllustsBean illustsBean) {
        File realFie = FileName.getIllustFile(illustsBean, 0);
        if (realFie.exists()) {
            Common.showToast("该文件已存在");
        } else {
            String url = illustsBean.getMeta_single_page().getOriginal_image_url();
            DownloadTask.Builder builder = new DownloadTask.Builder(url, new File(LocalData.getDownloadPath()))
                    .setFilename(FileName.getFileName(illustsBean, 0))
                    .setMinIntervalMillisCallbackProcess(30)
                    .setPassIfAlreadyCompleted(false);
            builder.addHeader(MAP_KEY, IMAGE_REFERER);
            DownloadTask task = builder.build();
            Common.showToast("开始下载");
            task.enqueue(new DownloadListener2() {
                @Override
                public void taskStart(@NonNull DownloadTask task) {
                    NotifiUtil.showStartNotification(illustsBean.getId(), "作品名：" + illustsBean.getTitle(), "第1张， 共1张");
                }

                @Override
                public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause) {
                    Common.showToast("下载完成");
                    NotifiUtil.showFinishNotification(illustsBean.getId(), "作品名：" + illustsBean.getTitle(), "第1张， 共1张");
                    Common.sendBroadcast(PixivApp.getContext(), realFie);
                }
            });
        }
    }


    private static void downloadMulti(IllustsBean illustsBean) {
        Common.showToast("开始下载");
        for (int i = 0; i < illustsBean.getMeta_pages().size(); i++) {
            final int position = i;
            File realFie = FileName.getIllustFile(illustsBean, position);
            if (realFie.exists()) {
                Common.showToast("该文件已存在");
            } else {
                String url = illustsBean.getMeta_pages().get(i).getImage_urls().getOriginal();
                DownloadTask.Builder builder = new DownloadTask.Builder(url, new File(LocalData.getDownloadPath()))
                        .setFilename(FileName.getFileName(illustsBean, position))
                        .setMinIntervalMillisCallbackProcess(30)
                        .setPassIfAlreadyCompleted(false);
                builder.addHeader(MAP_KEY, IMAGE_REFERER);
                DownloadTask task = builder.build();
                task.enqueue(new DownloadListener2() {
                    @Override
                    public void taskStart(@NonNull DownloadTask task) {
                        NotifiUtil.showStartNotification(illustsBean.getId() + position, "作品名：" + illustsBean.getTitle(),
                                "第" + String.valueOf((position + 1)) + "张， 共" + String.valueOf(illustsBean.getMeta_pages().size()) + "张");
                    }

                    @Override
                    public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause) {
                        if (position == illustsBean.getMeta_pages().size() - 1) {
                            Common.showToast("下载完成");
                        }
                        NotifiUtil.showFinishNotification(illustsBean.getId() + position, "作品名：" + illustsBean.getTitle(),
                                "第" +
                                        String.valueOf((position + 1)) + "张" + "/共" +
                                        String.valueOf(illustsBean.getMeta_pages().size()) + "张");
                        Common.sendBroadcast(PixivApp.getContext(), realFie);
                    }
                });
            }
        }
    }


    public static void downloadSingleOfMulti(IllustsBean illustsBean, int index) {
        File realFie = FileName.getIllustFile(illustsBean, index);
        if (realFie.exists()) {
            Common.showToast("该文件已存在");
        } else {
            Common.showToast("开始下载");
            String url = illustsBean.getMeta_pages().get(index).getImage_urls().getOriginal();
            DownloadTask.Builder builder = new DownloadTask.Builder(url, new File(LocalData.getDownloadPath()))
                    .setFilename(FileName.getFileName(illustsBean, index))
                    .setMinIntervalMillisCallbackProcess(30)
                    .setPassIfAlreadyCompleted(false);
            builder.addHeader(MAP_KEY, IMAGE_REFERER);
            DownloadTask task = builder.build();
            task.enqueue(new DownloadListener2() {
                @Override
                public void taskStart(@NonNull DownloadTask task) {
                }

                @Override
                public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause) {
                    Common.showToast("下载完成");
                    Common.sendBroadcast(PixivApp.getContext(), realFie);
                }
            });
        }
    }
}
