package com.example.administrator.essim.response_re;

import java.io.Serializable;
import java.util.List;

public class IllustsBean implements Serializable {
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    /**
     * id : 53496095
     * title : 光
     * type : illust
     * image_urls : {"square_medium":"https://i.pximg.net/c/540x540_10_webp/img-master/img/2015/11/11/15/28/20/53496095_p0_square1200.jpg","medium":"https://i.pximg.net/c/540x540_70/img-master/img/2015/11/11/15/28/20/53496095_p0_master1200.jpg","large":"https://i.pximg.net/c/600x1200_90_webp/img-master/img/2015/11/11/15/28/20/53496095_p0_master1200.jpg"}
     * caption : 画了两晚上....眼花了_(:з」∠)_
     * restrict : 0
     * user : {"id":3999381,"name":"萃","account":"jidanhaidaitang","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2018/07/25/18/32/41/14534685_583322ae8911f8ab8897baab4d196bd8_170.png"},"is_followed":false}
     * tags : [{"name":"魔理沙"},{"name":"東方"},{"name":"東方project"},{"name":"霧雨魔理沙"},{"name":"東方Project5000users入り"}]
     * tools : ["SAI"]
     * create_date : 2015-11-11T15:28:20+09:00
     * page_count : 1
     * width : 2298
     * height : 2016
     * sanity_level : 2
     * x_restrict : 0
     * series : null
     * meta_single_page : {"original_image_url":"https://i.pximg.net/img-original/img/2015/11/11/15/28/20/53496095_p0.png"}
     * meta_pages : []
     * total_view : 90582
     * total_bookmarks : 6571
     * is_bookmarked : false
     * visible : true
     * is_muted : false
     */

    private boolean isSelected;
    private int id;
    private String title;
    private String type;
    private ImageUrlsBean image_urls;
    private String caption;
    private int restrict;
    private UserBean user;
    private String create_date;
    private int page_count;
    private int width;
    private int height;
    private int sanity_level;
    private int x_restrict;
    private Object series;
    private MetaSinglePageBean meta_single_page;
    private int total_view;
    private int total_bookmarks;
    private boolean is_bookmarked;
    private boolean visible;
    private boolean is_muted;
    private List<TagsBean> tags;
    private List<String> tools;
    private List<MetaPagesBean> meta_pages;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ImageUrlsBean getImage_urls() {
        return image_urls;
    }

    public void setImage_urls(ImageUrlsBean image_urls) {
        this.image_urls = image_urls;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getRestrict() {
        return restrict;
    }

    public void setRestrict(int restrict) {
        this.restrict = restrict;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public int getPage_count() {
        return page_count;
    }

    public void setPage_count(int page_count) {
        this.page_count = page_count;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSanity_level() {
        return sanity_level;
    }

    public void setSanity_level(int sanity_level) {
        this.sanity_level = sanity_level;
    }

    public int getX_restrict() {
        return x_restrict;
    }

    public void setX_restrict(int x_restrict) {
        this.x_restrict = x_restrict;
    }

    public Object getSeries() {
        return series;
    }

    public void setSeries(Object series) {
        this.series = series;
    }

    public MetaSinglePageBean getMeta_single_page() {
        return meta_single_page;
    }

    public void setMeta_single_page(MetaSinglePageBean meta_single_page) {
        this.meta_single_page = meta_single_page;
    }

    public int getTotal_view() {
        return total_view;
    }

    public void setTotal_view(int total_view) {
        this.total_view = total_view;
    }

    public int getTotal_bookmarks() {
        return total_bookmarks;
    }

    public void setTotal_bookmarks(int total_bookmarks) {
        this.total_bookmarks = total_bookmarks;
    }

    public boolean isIs_bookmarked() {
        return is_bookmarked;
    }

    public void setIs_bookmarked(boolean is_bookmarked) {
        this.is_bookmarked = is_bookmarked;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isIs_muted() {
        return is_muted;
    }

    public void setIs_muted(boolean is_muted) {
        this.is_muted = is_muted;
    }

    public List<TagsBean> getTags() {
        return tags;
    }

    public void setTags(List<TagsBean> tags) {
        this.tags = tags;
    }

    public List<String> getTools() {
        return tools;
    }

    public void setTools(List<String> tools) {
        this.tools = tools;
    }

    public List<MetaPagesBean> getMeta_pages() {
        return meta_pages;
    }

    public void setMeta_pages(List<MetaPagesBean> meta_pages) {
        this.meta_pages = meta_pages;
    }

    public static class ImageUrlsBean implements Serializable{
        /**
         * square_medium : https://i.pximg.net/c/540x540_10_webp/img-master/img/2015/11/11/15/28/20/53496095_p0_square1200.jpg
         * medium : https://i.pximg.net/c/540x540_70/img-master/img/2015/11/11/15/28/20/53496095_p0_master1200.jpg
         * large : https://i.pximg.net/c/600x1200_90_webp/img-master/img/2015/11/11/15/28/20/53496095_p0_master1200.jpg
         */

        private String square_medium;
        private String medium;
        private String large;

        public String getSquare_medium() {
            return square_medium;
        }

        public void setSquare_medium(String square_medium) {
            this.square_medium = square_medium;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }
    }


    public static class MetaSinglePageBean implements Serializable{
        /**
         * original_image_url : https://i.pximg.net/img-original/img/2015/11/11/15/28/20/53496095_p0.png
         */

        private String original_image_url;

        public String getOriginal_image_url() {
            return original_image_url;
        }

        public void setOriginal_image_url(String original_image_url) {
            this.original_image_url = original_image_url;
        }
    }

    public static class TagsBean implements Serializable{
        /**
         * name : 魔理沙
         */

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
