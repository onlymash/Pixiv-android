package com.example.administrator.essim.response;

import com.example.administrator.essim.response_re.IllustsBean;

import java.io.Serializable;

public class IllustDetailResponse{
    private IllustsBean illust;

    public IllustsBean getIllust() {
        return this.illust;
    }

    public void setIllust(IllustsBean paramIllustBean) {
        this.illust = paramIllustBean;
    }
}