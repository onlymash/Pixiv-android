package com.example.administrator.essim.response;

import java.util.List;

public class PixivCollectionResponse {


    /**
     * spotlight_articles : [{"id":3714,"title":"拨动琴弦，奏响旋律！吉他少女特辑","pure_title":"吉他少女特辑","thumbnail":"https://i.pximg.net/c/540x540_70/img-master/img/2018/08/18/00/30/00/70253866_p0_master1200.jpg","article_url":"https://www.pixivision.net/zh/a/3714","publish_date":"2018-09-22T17:00:00+09:00","category":"spotlight","subcategory_label":"插画"},{"id":3740,"title":"带我去那个地方。巴士站特辑","pure_title":"巴士站特辑","thumbnail":"https://i.pximg.net/c/540x540_70/img-master/img/2016/04/12/00/21/52/56316785_p0_master1200.jpg","article_url":"https://www.pixivision.net/zh/a/3740","publish_date":"2018-09-21T17:00:00+09:00","category":"spotlight","subcategory_label":"插画"},{"id":3598,"title":"你准备好了吗！格斗姿势的少女特辑","pure_title":"格斗姿势的少女特辑","thumbnail":"https://i.pximg.net/c/540x540_70/img-master/img/2017/10/08/17/06/47/65339515_p0_master1200.jpg","article_url":"https://www.pixivision.net/zh/a/3598","publish_date":"2018-09-20T18:00:00+09:00","category":"spotlight","subcategory_label":"插画"},{"id":3711,"title":"秋日长夜里，你指尖轻轻划过文字。书与男子特辑","pure_title":"书与男子特辑","thumbnail":"https://i.pximg.net/c/540x540_70/img-master/img/2014/09/10/19/40/58/45899770_p0_master1200.jpg","article_url":"https://www.pixivision.net/zh/a/3711","publish_date":"2018-09-20T17:00:00+09:00","category":"spotlight","subcategory_label":"插画"},{"id":3738,"title":"焦点在你身上！运用了\u201c景深\u201d的插画作品特辑","pure_title":"运用了\u201c景深\u201d的插画作品特辑","thumbnail":"https://i.pximg.net/c/540x540_70/img-master/img/2018/08/13/00/00/05/70165419_p0_master1200.jpg","article_url":"https://www.pixivision.net/zh/a/3738","publish_date":"2018-09-19T18:00:00+09:00","category":"spotlight","subcategory_label":"插画"},{"id":3721,"title":"猫的日常，我的非日常。有猫的风景特辑","pure_title":"有猫的风景特辑","thumbnail":"https://i.pximg.net/c/540x540_70/img-master/img/2018/08/10/00/27/45/70115747_p0_master1200.jpg","article_url":"https://www.pixivision.net/zh/a/3721","publish_date":"2018-09-19T17:00:00+09:00","category":"spotlight","subcategory_label":"插画"},{"id":3739,"title":"轻轻提起裙摆♡提着裙摆的少女特辑","pure_title":"提着裙摆的少女特辑","thumbnail":"https://i.pximg.net/c/540x540_70/img-master/img/2017/11/14/00/01/06/65884837_p0_master1200.jpg","article_url":"https://www.pixivision.net/zh/a/3739","publish_date":"2018-09-18T18:00:00+09:00","category":"spotlight","subcategory_label":"插画"},{"id":3727,"title":"看到另一个世界。美丽的映像特辑","pure_title":"美丽的映像特辑","thumbnail":"https://i.pximg.net/c/540x540_70/img-master/img/2018/05/31/00/00/03/69000196_p0_master1200.jpg","article_url":"https://www.pixivision.net/zh/a/3727","publish_date":"2018-09-18T17:00:00+09:00","category":"spotlight","subcategory_label":"插画"},{"id":3743,"title":"希望有一天能够超越你！亲子特辑","pure_title":"亲子特辑","thumbnail":"https://i.pximg.net/c/540x540_70/img-master/img/2014/12/11/16/40/20/47504860_p0_master1200.jpg","article_url":"https://www.pixivision.net/zh/a/3743","publish_date":"2018-09-17T17:00:00+09:00","category":"spotlight","subcategory_label":"插画"}]
     * next_url : https://app-api.pixiv.net/v1/spotlight/articles?filter=for_android&category=all&offset=10
     */
    private String next_url;
    private List<SpotlightArticlesBean> spotlight_articles;

    public String getNext_url() {
        return next_url;
    }

    public void setNext_url(String next_url) {
        this.next_url = next_url;
    }

    public List<SpotlightArticlesBean> getSpotlight_articles() {
        return spotlight_articles;
    }

    public void setSpotlight_articles(List<SpotlightArticlesBean> spotlight_articles) {
        this.spotlight_articles = spotlight_articles;
    }

    public static class SpotlightArticlesBean {
        /**
         * id : 3714
         * title : 拨动琴弦，奏响旋律！吉他少女特辑
         * pure_title : 吉他少女特辑
         * thumbnail : https://i.pximg.net/c/540x540_70/img-master/img/2018/08/18/00/30/00/70253866_p0_master1200.jpg
         * article_url : https://www.pixivision.net/zh/a/3714
         * publish_date : 2018-09-22T17:00:00+09:00
         * category : spotlight
         * subcategory_label : 插画
         */

        private int id;
        private String title;
        private String pure_title;
        private String thumbnail;
        private String article_url;
        private String publish_date;
        private String category;
        private String subcategory_label;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPure_title() {
            return pure_title;
        }

        public void setPure_title(String pure_title) {
            this.pure_title = pure_title;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getArticle_url() {
            return article_url;
        }

        public void setArticle_url(String article_url) {
            this.article_url = article_url;
        }

        public String getPublish_date() {
            return publish_date;
        }

        public void setPublish_date(String publish_date) {
            this.publish_date = publish_date;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getSubcategory_label() {
            return subcategory_label;
        }

        public void setSubcategory_label(String subcategory_label) {
            this.subcategory_label = subcategory_label;
        }
    }
}
