package com.example.administrator.essim.response;

import java.util.ArrayList;
import java.util.List;

public class AllBookmarkTagResponse {

    public ArrayList<SingleTag> getBookmark_tags() {
        return bookmark_tags;
    }

    public void setBookmark_tags(ArrayList<SingleTag> bookmark_tags) {
        this.bookmark_tags = bookmark_tags;
    }

    public ArrayList<SingleTag> bookmark_tags;

    public String getNext_url() {
        return next_url;
    }

    public void setNext_url(String next_url) {
        this.next_url = next_url;
    }

    public String next_url;
}