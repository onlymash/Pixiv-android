package com.example.administrator.essim.response_re;

import java.io.Serializable;

public class UserBean implements Serializable {
    /**
     * id : 1922055
     * name : ぴっぴ
     * account : p3i2
     * profile_image_urls : {"medium":"https://i.pximg.net/user-profile/img/2013/04/30/20/28/41/6171621_55d3aca47cc056e3f3d63e7c1bfff0f1_170.jpg"}
     * comment : 点数、ブクマコメありがとうございます！！


     お仕事のご依頼などはこちらの連絡先からよろしくお願いいたします。

     連絡先 pippi913p3i2@gmail.com

     COMITIA122にサークル参加したときの新刊「Promonade」がアリスブックス様にて通販が開始されました。
     半分は風景イラスト集半分はケモノの漫画（32p、フルカラー）です。何卒宜しくお願い致します。
     http://alice-books.com/item/show/7389-1
     * is_followed : false
     */

    private int id;
    private String name;
    private String account;
    private ProfileImageUrlsBean profile_image_urls;
    private String comment;
    private boolean is_followed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public ProfileImageUrlsBean getProfile_image_urls() {
        return profile_image_urls;
    }

    public void setProfile_image_urls(ProfileImageUrlsBean profile_image_urls) {
        this.profile_image_urls = profile_image_urls;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isIs_followed() {
        return is_followed;
    }

    public void setIs_followed(boolean is_followed) {
        this.is_followed = is_followed;
    }

    public static class ProfileImageUrlsBean implements Serializable{
        /**
         * medium : https://i.pximg.net/user-profile/img/2013/04/30/20/28/41/6171621_55d3aca47cc056e3f3d63e7c1bfff0f1_170.jpg
         */

        private String medium;

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }
    }
}