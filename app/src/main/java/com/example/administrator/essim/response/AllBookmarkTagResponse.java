package com.example.administrator.essim.response;

import java.util.ArrayList;

public class AllBookmarkTagResponse {

    public ArrayList<SingleTag> bookmark_tags;
    public String next_url;

    public ArrayList<SingleTag> getBookmark_tags() {
        return bookmark_tags;
    }

    public void setBookmark_tags(ArrayList<SingleTag> bookmark_tags) {
        this.bookmark_tags = bookmark_tags;
    }

    public String getNext_url() {
        return next_url;
    }

    public void setNext_url(String next_url) {
        this.next_url = next_url;
    }
}