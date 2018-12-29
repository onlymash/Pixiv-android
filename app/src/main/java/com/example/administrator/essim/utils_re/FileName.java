package com.example.administrator.essim.utils_re;

import com.example.administrator.essim.response_re.IllustsBean;

import java.io.File;

public class FileName {

    static final int NAME_STYLE_0 = 0;        //"作品id_p数.jpeg"
    private static final int NAME_STYLE_1 = 1;//"作品id_p数.png"
    private static final int NAME_STYLE_2 = 2;//"作品标题_作品id_p数.jpeg"
    private static final int NAME_STYLE_3 = 3;//"作品标题_作品id_p数.png"

    public static File getIllustFile(IllustsBean illustsBean, int index) {
        return new File(LocalData.getDownloadPath(), getFileName(illustsBean, index));
    }

    public static String getFileName(IllustsBean illustsBean, int index) {
        switch (LocalData.getFileNameStyle()) {
            case NAME_STYLE_0:
                return getFileType0(illustsBean, index);
            case NAME_STYLE_1:
                return getFileType1(illustsBean, index);
            case NAME_STYLE_2:
                return getFileType2(illustsBean, index);
            case NAME_STYLE_3:
                return getFileType3(illustsBean, index);
            default:
                return null;
        }
    }


    private static String getFileType0(IllustsBean illustsBean, int index) {
        String fileName;
        if (illustsBean.getPage_count() == 1) {
            fileName = String.valueOf(illustsBean.getId()) + "_p0.jpeg";
        } else {
            fileName = String.valueOf(illustsBean.getId()) + "_p" + String.valueOf(index) + ".jpeg";
        }
        return fileName;
    }

    private static String getFileType1(IllustsBean illustsBean, int index) {
        String fileName;
        if (illustsBean.getPage_count() == 1) {
            fileName = String.valueOf(illustsBean.getId()) + "_p0.png";
        } else {
            fileName = String.valueOf(illustsBean.getId()) + "_p" + String.valueOf(index) + ".png";
        }
        return fileName;
    }

    private static String getFileType2(IllustsBean illustsBean, int index) {
        String fileName;
        if (illustsBean.getPage_count() == 1) {
            fileName = illustsBean.getTitle() + "_" + String.valueOf(illustsBean.getId()) + "_p0.jpeg";
        } else {
            fileName = illustsBean.getTitle() + "_" + String.valueOf(illustsBean.getId()) + "_p" + String.valueOf(index) + ".jpeg";
        }
        return fileName;
    }

    private static String getFileType3(IllustsBean illustsBean, int index) {
        String fileName;
        if (illustsBean.getPage_count() == 1) {
            fileName = illustsBean.getTitle() + "_" + String.valueOf(illustsBean.getId()) + "_p0.png";
        } else {
            fileName = illustsBean.getTitle() + "_" + String.valueOf(illustsBean.getId()) + "_p" + String.valueOf(index) + ".png";
        }
        return fileName;
    }
}
