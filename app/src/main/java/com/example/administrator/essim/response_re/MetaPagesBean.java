package com.example.administrator.essim.response_re;

import java.io.Serializable;

public class MetaPagesBean implements Serializable {
    /**
     * image_urls : {"square_medium":"https://i.pximg.net/c/360x360_10_webp/img-master/img/2018/12/15/00/00/10/72114149_p0_square1200.jpg","medium":"https://i.pximg.net/c/540x540_70/img-master/img/2018/12/15/00/00/10/72114149_p0_master1200.jpg","large":"https://i.pximg.net/c/600x1200_90_webp/img-master/img/2018/12/15/00/00/10/72114149_p0_master1200.jpg","original":"https://i.pximg.net/img-original/img/2018/12/15/00/00/10/72114149_p0.png"}
     */

    private ImageUrlsBeanXX image_urls;

    public ImageUrlsBeanXX getImage_urls() {
        return image_urls;
    }

    public void setImage_urls(ImageUrlsBeanXX image_urls) {
        this.image_urls = image_urls;
    }

    public static class ImageUrlsBeanXX implements Serializable{
        /**
         * square_medium : https://i.pximg.net/c/360x360_10_webp/img-master/img/2018/12/15/00/00/10/72114149_p0_square1200.jpg
         * medium : https://i.pximg.net/c/540x540_70/img-master/img/2018/12/15/00/00/10/72114149_p0_master1200.jpg
         * large : https://i.pximg.net/c/600x1200_90_webp/img-master/img/2018/12/15/00/00/10/72114149_p0_master1200.jpg
         * original : https://i.pximg.net/img-original/img/2018/12/15/00/00/10/72114149_p0.png
         */

        private String square_medium;
        private String medium;
        private String large;
        private String original;

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

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }
    }
}