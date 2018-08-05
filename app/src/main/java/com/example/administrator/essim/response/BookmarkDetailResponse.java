package com.example.administrator.essim.response;

import java.util.ArrayList;

public class BookmarkDetailResponse {
    public BookMark bookmark_detail;

    public BookMark getBookmark_detail() {
        return bookmark_detail;
    }

    public void setBookmark_detail(BookMark bookmark_detail) {
        this.bookmark_detail = bookmark_detail;
    }

    public class BookMark {
        public boolean is_bookmarked;
        public boolean restrict;
        public ArrayList<BookmarkTagResponse> tags;
    }
}
