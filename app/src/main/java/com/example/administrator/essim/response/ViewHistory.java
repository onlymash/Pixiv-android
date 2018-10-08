package com.example.administrator.essim.response;

import org.litepal.crud.DataSupport;

public class ViewHistory extends DataSupport {

    private String id;
    private String author;
    private String title;
    private String view_time;
    private String illustSize;

    public String getIllustSize() {
        return illustSize;
    }

    public void setIllustSize(String illustSize) {
        this.illustSize = illustSize;
    }

    public ViewHistory(String id, String author, String title, String view_time, String illustSize) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.view_time = view_time;
        this.illustSize = illustSize;
    }

    public ViewHistory() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getView_time() {
        return view_time;
    }

    public void setView_time(String view_time) {
        this.view_time = view_time;
    }
}
