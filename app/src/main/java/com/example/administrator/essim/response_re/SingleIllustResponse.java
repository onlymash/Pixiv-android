package com.example.administrator.essim.response_re;

import java.util.List;

public class SingleIllustResponse {


    /**
     * illust : {"id":71658270,"title":"ワタシモヨウ","type":"illust","image_urls":{"square_medium":"https://i.pximg.net/c/540x540_10_webp/img-master/img/2018/11/15/00/00/01/71658270_p0_square1200.jpg","medium":"https://i.pximg.net/c/540x540_70/img-master/img/2018/11/15/00/00/01/71658270_p0_master1200.jpg","large":"https://i.pximg.net/c/600x1200_90_webp/img-master/img/2018/11/15/00/00/01/71658270_p0_master1200.jpg"},"caption":"明日はどんな模様になるのかな","restrict":0,"user":{"id":211515,"name":"防人","account":"sakimori30","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2018/10/04/12/19/34/14855902_890d45995ba84f45d1c9c08da1d86ab8_170.jpg"},"is_followed":false},"tags":[{"name":"オリジナル"},{"name":"女の子"},{"name":"琥珀"},{"name":"オリジナル5000users入り"}],"tools":["SAI"],"create_date":"2018-11-15T00:00:01+09:00","page_count":1,"width":900,"height":900,"sanity_level":2,"x_restrict":0,"series":null,"meta_single_page":{"original_image_url":"https://i.pximg.net/img-original/img/2018/11/15/00/00/01/71658270_p0.jpg"},"meta_pages":[],"total_view":42918,"total_bookmarks":5234,"is_bookmarked":false,"visible":true,"is_muted":false,"total_comments":16}
     */

    private IllustsBean illust;

    public IllustsBean getIllust() {
        return illust;
    }

    public void setIllust(IllustsBean illust) {
        this.illust = illust;
    }
}
