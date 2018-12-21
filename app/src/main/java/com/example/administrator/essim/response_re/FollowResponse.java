package com.example.administrator.essim.response_re;

import java.util.List;

public class FollowResponse {

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

    public static class UserPreviewsBean {
        /**
         * user : {"id":9427,"name":"アマガイタロー","account":"10ten","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2013/02/26/10/51/13/5881325_62d6c9920851ae22e9de9822cd5f0b40_170.jpg"},"is_followed":true}
         * illusts : [{"id":66806885,"title":"2018年書き初め","type":"illust","image_urls":{"square_medium":"https://i.pximg.net/c/540x540_10_webp/img-master/img/2018/01/15/00/15/04/66806885_p0_square1200.jpg","medium":"https://i.pximg.net/c/540x540_70/img-master/img/2018/01/15/00/15/04/66806885_p0_master1200.jpg","large":"https://i.pximg.net/c/600x1200_90_webp/img-master/img/2018/01/15/00/15/04/66806885_p0_master1200.jpg"},"caption":"遅くなってしまいましたが、今年も一年よろしくお願いいたします！","restrict":0,"user":{"id":9427,"name":"アマガイタロー","account":"10ten","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2013/02/26/10/51/13/5881325_62d6c9920851ae22e9de9822cd5f0b40_170.jpg"},"is_followed":true},"tags":[{"name":"オリジナル"},{"name":"犬と女の子"},{"name":"ハイアングル"},{"name":"和服"},{"name":"はだけ"},{"name":"足袋"},{"name":"柴犬"},{"name":"かるた"},{"name":"オリジナル10000users入り"},{"name":"胸チラ"}],"tools":["SAI"],"create_date":"2018-01-15T00:15:04+09:00","page_count":1,"width":1000,"height":1414,"sanity_level":6,"x_restrict":0,"series":null,"meta_single_page":{"original_image_url":"https://i.pximg.net/img-original/img/2018/01/15/00/15/04/66806885_p0.png"},"meta_pages":[],"total_view":158685,"total_bookmarks":19589,"is_bookmarked":false,"visible":true,"is_muted":false},{"id":65979092,"title":"ジャージ女子","type":"illust","image_urls":{"square_medium":"https://i.pximg.net/c/540x540_10_webp/img-master/img/2017/11/21/00/03/18/65979092_p0_square1200.jpg","medium":"https://i.pximg.net/c/540x540_70/img-master/img/2017/11/21/00/03/18/65979092_p0_master1200.jpg","large":"https://i.pximg.net/c/600x1200_90_webp/img-master/img/2017/11/21/00/03/18/65979092_p0_master1200.jpg"},"caption":"黒ソックス","restrict":0,"user":{"id":9427,"name":"アマガイタロー","account":"10ten","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2013/02/26/10/51/13/5881325_62d6c9920851ae22e9de9822cd5f0b40_170.jpg"},"is_followed":true},"tags":[{"name":"オリジナル"},{"name":"ジャージ"},{"name":"女子高生"},{"name":"汗"},{"name":"裏腿"},{"name":"黒ハイソックス"},{"name":"ソックス足裏"},{"name":"赤ジャージ"},{"name":"ふともも"},{"name":"オリジナル10000users入り"}],"tools":["SAI"],"create_date":"2017-11-21T00:03:18+09:00","page_count":1,"width":1126,"height":1400,"sanity_level":4,"x_restrict":0,"series":null,"meta_single_page":{"original_image_url":"https://i.pximg.net/img-original/img/2017/11/21/00/03/18/65979092_p0.png"},"meta_pages":[],"total_view":178587,"total_bookmarks":19742,"is_bookmarked":false,"visible":true,"is_muted":false},{"id":65765210,"title":"黒ソックス","type":"illust","image_urls":{"square_medium":"https://i.pximg.net/c/540x540_10_webp/img-master/img/2017/11/05/20/31/39/65765210_p0_square1200.jpg","medium":"https://i.pximg.net/c/540x540_70/img-master/img/2017/11/05/20/31/39/65765210_p0_master1200.jpg","large":"https://i.pximg.net/c/600x1200_90_webp/img-master/img/2017/11/05/20/31/39/65765210_p0_master1200.jpg"},"caption":"女子高生","restrict":0,"user":{"id":9427,"name":"アマガイタロー","account":"10ten","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2013/02/26/10/51/13/5881325_62d6c9920851ae22e9de9822cd5f0b40_170.jpg"},"is_followed":true},"tags":[{"name":"オリジナル"},{"name":"下着"},{"name":"女子高生"},{"name":"黒パンツ"},{"name":"気になる胸元"},{"name":"黒ハイソックス"},{"name":"つけてない"},{"name":"オリジナル10000users入り"},{"name":"パンチラ"}],"tools":["SAI"],"create_date":"2017-11-05T20:31:39+09:00","page_count":1,"width":1118,"height":1400,"sanity_level":4,"x_restrict":0,"series":null,"meta_single_page":{"original_image_url":"https://i.pximg.net/img-original/img/2017/11/05/20/31/39/65765210_p0.png"},"meta_pages":[],"total_view":208961,"total_bookmarks":24102,"is_bookmarked":false,"visible":true,"is_muted":false}]
         * novels : []
         * is_muted : false
         */

        private UserBean user;
        private boolean is_muted;
        private List<IllustsBean> illusts;
        private List<?> novels;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public boolean isIs_muted() {
            return is_muted;
        }

        public void setIs_muted(boolean is_muted) {
            this.is_muted = is_muted;
        }

        public List<IllustsBean> getIllusts() {
            return illusts;
        }

        public void setIllusts(List<IllustsBean> illusts) {
            this.illusts = illusts;
        }

        public List<?> getNovels() {
            return novels;
        }

        public void setNovels(List<?> novels) {
            this.novels = novels;
        }
    }
}
