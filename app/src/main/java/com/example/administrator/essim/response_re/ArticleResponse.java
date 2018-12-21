package com.example.administrator.essim.response_re;

import java.util.List;

public class ArticleResponse {


    /**
     * spotlight_articles : [{"id":3985,"title":"摇摇晃晃♡吊床特辑","pure_title":"吊床特辑","thumbnail":"https://i.pximg.net/c/540x540_70/img-master/img/2018/05/23/22/25/50/68886862_p0_master1200.jpg","article_url":"https://www.pixivision.net/zh/a/3985","publish_date":"2018-12-21T18:00:00+09:00","category":"spotlight","subcategory_label":"插画"},{"id":3917,"title":"想让你诊疗我的心♡医生特辑","pure_title":"医生特辑","thumbnail":"https://i.pximg.net/c/540x540_70/img-master/img/2015/05/18/01/14/50/50435499_p0_master1200.jpg","article_url":"https://www.pixivision.net/zh/a/3917","publish_date":"2018-12-21T17:00:00+09:00","category":"spotlight","subcategory_label":"插画"},{"id":4133,"title":"秘诀就在设置页面。专业人士倾情解说在家打印出美丽图片的技巧","pure_title":"专业人士倾情解说在家打印出美丽图片的技巧","thumbnail":"https://i.pximg.net/imgaz/upload/20181211/219191075.jpg","article_url":"https://www.pixivision.net/zh/a/4133","publish_date":"2018-12-20T18:00:00+09:00","category":"inspiration","subcategory_label":"推荐"},{"id":4045,"title":"和煦温柔的颜色。充满了橙色温柔的插画特辑","pure_title":"充满了橙色温柔的插画特辑","thumbnail":"https://i.pximg.net/c/540x540_70/img-master/img/2018/11/02/01/28/24/71463831_p0_master1200.jpg","article_url":"https://www.pixivision.net/zh/a/4045","publish_date":"2018-12-20T18:00:00+09:00","category":"spotlight","subcategory_label":"插画"},{"id":4033,"title":"棋盘上的智力游戏。国际象棋特辑","pure_title":"国际象棋特辑","thumbnail":"https://i.pximg.net/c/540x540_70/img-master/img/2010/07/07/06/31/07/11728679_p0_master1200.jpg","article_url":"https://www.pixivision.net/zh/a/4033","publish_date":"2018-12-20T17:00:00+09:00","category":"spotlight","subcategory_label":"插画"},{"id":3962,"title":"邀请你来到一千零一夜。阿拉伯特辑","pure_title":"阿拉伯特辑","thumbnail":"https://i.pximg.net/c/540x540_70/img-master/img/2018/06/05/20/30/01/69089942_p0_master1200.jpg","article_url":"https://www.pixivision.net/zh/a/3962","publish_date":"2018-12-19T18:00:00+09:00","category":"spotlight","subcategory_label":"插画"},{"id":3938,"title":"凝望纸张的小宇宙。书架特辑","pure_title":"书架特辑","thumbnail":"https://i.pximg.net/c/540x540_70/img-master/img/2018/04/18/06/23/10/68284720_p0_master1200.jpg","article_url":"https://www.pixivision.net/zh/a/3938","publish_date":"2018-12-19T17:00:00+09:00","category":"spotlight","subcategory_label":"插画"},{"id":3947,"title":"保留生前的状态。标本特辑","pure_title":"标本特辑","thumbnail":"https://i.pximg.net/c/540x540_70/img-master/img/2015/05/18/19/56/35/50443628_p0_master1200.jpg","article_url":"https://www.pixivision.net/zh/a/3947","publish_date":"2018-12-18T18:00:00+09:00","category":"spotlight","subcategory_label":"插画"},{"id":4021,"title":"点缀平安京的夜晚。《阴阳师》特辑","pure_title":"《阴阳师》特辑","thumbnail":"https://i.pximg.net/c/540x540_70/img-master/img/2017/01/12/01/39/28/60897024_p0_master1200.jpg","article_url":"https://www.pixivision.net/zh/a/4021","publish_date":"2018-12-18T17:00:00+09:00","category":"spotlight","subcategory_label":"插画"},{"id":4013,"title":"神圣的独角兽。独角兽特辑","pure_title":"独角兽特辑","thumbnail":"https://i.pximg.net/c/540x540_70/img-master/img/2017/11/18/00/19/44/65934948_p0_master1200.jpg","article_url":"https://www.pixivision.net/zh/a/4013","publish_date":"2018-12-17T18:00:00+09:00","category":"spotlight","subcategory_label":"插画"}]
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
         * id : 3985
         * title : 摇摇晃晃♡吊床特辑
         * pure_title : 吊床特辑
         * thumbnail : https://i.pximg.net/c/540x540_70/img-master/img/2018/05/23/22/25/50/68886862_p0_master1200.jpg
         * article_url : https://www.pixivision.net/zh/a/3985
         * publish_date : 2018-12-21T18:00:00+09:00
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
