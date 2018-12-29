package com.example.administrator.essim.response_re;

import java.util.List;

public class ListUserResponse {

    private String next_url;
    private List<UserPreviewsBean> user_previews;

    public String getNextUrl() {
        return next_url;
    }

    public void setNextUrl(String url) {
        this.next_url = url;
    }

    public List<UserPreviewsBean> getUser_previews() {
        return user_previews;
    }

    public void setUser_previews(List<UserPreviewsBean> user_previews) {
        this.user_previews = user_previews;
    }
}
