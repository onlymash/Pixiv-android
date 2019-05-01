package com.example.administrator.essim.response;

import androidx.annotation.NonNull;

import org.litepal.crud.DataSupport;

public class ViewHistory extends DataSupport implements Comparable<ViewHistory>{

    private String illust_id;
    private String illust_author;
    private String illust_title;
    private long view_time;
    private String illust_size;

    public ViewHistory(String illust_id, String illust_author, String illust_title,
                       long view_time, String illust_size, String img_url) {
        this.illust_id = illust_id;
        this.illust_author = illust_author;
        this.illust_title = illust_title;
        this.view_time = view_time;
        this.illust_size = illust_size;
        this.img_url = img_url;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    private String img_url;

    public ViewHistory() {
    }

    public String getIllust_id() {
        return illust_id;
    }

    public void setIllust_id(String illust_id) {
        this.illust_id = illust_id;
    }

    public String getIllust_author() {
        return illust_author;
    }

    public void setIllust_author(String illust_author) {
        this.illust_author = illust_author;
    }

    public String getIllust_title() {
        return illust_title;
    }

    public void setIllust_title(String illust_title) {
        this.illust_title = illust_title;
    }

    public long getView_time() {
        return view_time;
    }

    public void setView_time(long view_time) {
        this.view_time = view_time;
    }

    public String getIllust_size() {
        return illust_size;
    }

    public void setIllust_size(String illust_size) {
        this.illust_size = illust_size;
    }



    @Override
    public int compareTo(@NonNull ViewHistory viewHistory) {
        if(this.view_time > viewHistory.getView_time()){
            return -1;
        }else {
            return 1;
        }
    }
}
