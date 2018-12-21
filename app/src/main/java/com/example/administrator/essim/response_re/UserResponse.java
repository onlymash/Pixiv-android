package com.example.administrator.essim.response_re;

public class UserResponse {


    /**
     * user : {"id":1922055,"name":"ぴっぴ","account":"p3i2","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2013/04/30/20/28/41/6171621_55d3aca47cc056e3f3d63e7c1bfff0f1_170.jpg"},"comment":"点数、ブクマコメありがとうございます！！\r\n\r\n\r\nお仕事のご依頼などはこちらの連絡先からよろしくお願いいたします。\r\n\r\n連絡先 pippi913p3i2@gmail.com\r\n\r\nCOMITIA122にサークル参加したときの新刊「Promonade」がアリスブックス様にて通販が開始されました。\r\n半分は風景イラスト集半分はケモノの漫画（32p、フルカラー）です。何卒宜しくお願い致します。\r\nhttp://alice-books.com/item/show/7389-1","is_followed":false}
     * profile : {"webpage":null,"gender":"female","birth":"","birth_day":"","birth_year":0,"region":"日本 栃木县","address_id":9,"country_code":"","job":"大学生/研究生","job_id":19,"total_follow_users":28,"total_mypixiv_users":11,"total_illusts":85,"total_manga":3,"total_novels":0,"total_illust_bookmarks_public":163,"total_illust_series":0,"background_image_url":null,"twitter_account":"pippi913","twitter_url":"https://twitter.com/pippi913","pawoo_url":null,"is_premium":true,"is_using_custom_profile_image":true}
     * profile_publicity : {"gender":"public","region":"public","birth_day":"public","birth_year":"public","job":"public","pawoo":true}
     * workspace : {"pc":"","monitor":"","tool":"","scanner":"","tablet":"","mouse":"","printer":"","desktop":"","music":"","desk":"","chair":"","comment":"","workspace_image_url":null}
     */

    private UserBean user;
    private ProfileBean profile;
    private ProfilePublicityBean profile_publicity;
    private WorkspaceBean workspace;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public ProfileBean getProfile() {
        return profile;
    }

    public void setProfile(ProfileBean profile) {
        this.profile = profile;
    }

    public ProfilePublicityBean getProfile_publicity() {
        return profile_publicity;
    }

    public void setProfile_publicity(ProfilePublicityBean profile_publicity) {
        this.profile_publicity = profile_publicity;
    }

    public WorkspaceBean getWorkspace() {
        return workspace;
    }

    public void setWorkspace(WorkspaceBean workspace) {
        this.workspace = workspace;
    }

    public static class ProfileBean {
        /**
         * webpage : null
         * gender : female
         * birth :
         * birth_day :
         * birth_year : 0
         * region : 日本 栃木县
         * address_id : 9
         * country_code :
         * job : 大学生/研究生
         * job_id : 19
         * total_follow_users : 28
         * total_mypixiv_users : 11
         * total_illusts : 85
         * total_manga : 3
         * total_novels : 0
         * total_illust_bookmarks_public : 163
         * total_illust_series : 0
         * background_image_url : null
         * twitter_account : pippi913
         * twitter_url : https://twitter.com/pippi913
         * pawoo_url : null
         * is_premium : true
         * is_using_custom_profile_image : true
         */

        private Object webpage;
        private String gender;
        private String birth;
        private String birth_day;
        private int birth_year;
        private String region;
        private int address_id;
        private String country_code;
        private String job;
        private int job_id;
        private int total_follow_users;
        private int total_mypixiv_users;
        private int total_illusts;
        private int total_manga;
        private int total_novels;
        private int total_illust_bookmarks_public;
        private int total_illust_series;
        private Object background_image_url;
        private String twitter_account;
        private String twitter_url;
        private Object pawoo_url;
        private boolean is_premium;
        private boolean is_using_custom_profile_image;

        public Object getWebpage() {
            return webpage;
        }

        public void setWebpage(Object webpage) {
            this.webpage = webpage;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getBirth() {
            return birth;
        }

        public void setBirth(String birth) {
            this.birth = birth;
        }

        public String getBirth_day() {
            return birth_day;
        }

        public void setBirth_day(String birth_day) {
            this.birth_day = birth_day;
        }

        public int getBirth_year() {
            return birth_year;
        }

        public void setBirth_year(int birth_year) {
            this.birth_year = birth_year;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public int getAddress_id() {
            return address_id;
        }

        public void setAddress_id(int address_id) {
            this.address_id = address_id;
        }

        public String getCountry_code() {
            return country_code;
        }

        public void setCountry_code(String country_code) {
            this.country_code = country_code;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public int getJob_id() {
            return job_id;
        }

        public void setJob_id(int job_id) {
            this.job_id = job_id;
        }

        public int getTotal_follow_users() {
            return total_follow_users;
        }

        public void setTotal_follow_users(int total_follow_users) {
            this.total_follow_users = total_follow_users;
        }

        public int getTotal_mypixiv_users() {
            return total_mypixiv_users;
        }

        public void setTotal_mypixiv_users(int total_mypixiv_users) {
            this.total_mypixiv_users = total_mypixiv_users;
        }

        public int getTotal_illusts() {
            return total_illusts;
        }

        public void setTotal_illusts(int total_illusts) {
            this.total_illusts = total_illusts;
        }

        public int getTotal_manga() {
            return total_manga;
        }

        public void setTotal_manga(int total_manga) {
            this.total_manga = total_manga;
        }

        public int getTotal_novels() {
            return total_novels;
        }

        public void setTotal_novels(int total_novels) {
            this.total_novels = total_novels;
        }

        public int getTotal_illust_bookmarks_public() {
            return total_illust_bookmarks_public;
        }

        public void setTotal_illust_bookmarks_public(int total_illust_bookmarks_public) {
            this.total_illust_bookmarks_public = total_illust_bookmarks_public;
        }

        public int getTotal_illust_series() {
            return total_illust_series;
        }

        public void setTotal_illust_series(int total_illust_series) {
            this.total_illust_series = total_illust_series;
        }

        public Object getBackground_image_url() {
            return background_image_url;
        }

        public void setBackground_image_url(Object background_image_url) {
            this.background_image_url = background_image_url;
        }

        public String getTwitter_account() {
            return twitter_account;
        }

        public void setTwitter_account(String twitter_account) {
            this.twitter_account = twitter_account;
        }

        public String getTwitter_url() {
            return twitter_url;
        }

        public void setTwitter_url(String twitter_url) {
            this.twitter_url = twitter_url;
        }

        public Object getPawoo_url() {
            return pawoo_url;
        }

        public void setPawoo_url(Object pawoo_url) {
            this.pawoo_url = pawoo_url;
        }

        public boolean isIs_premium() {
            return is_premium;
        }

        public void setIs_premium(boolean is_premium) {
            this.is_premium = is_premium;
        }

        public boolean isIs_using_custom_profile_image() {
            return is_using_custom_profile_image;
        }

        public void setIs_using_custom_profile_image(boolean is_using_custom_profile_image) {
            this.is_using_custom_profile_image = is_using_custom_profile_image;
        }
    }

    public static class ProfilePublicityBean {
        /**
         * gender : public
         * region : public
         * birth_day : public
         * birth_year : public
         * job : public
         * pawoo : true
         */

        private String gender;
        private String region;
        private String birth_day;
        private String birth_year;
        private String job;
        private boolean pawoo;

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getBirth_day() {
            return birth_day;
        }

        public void setBirth_day(String birth_day) {
            this.birth_day = birth_day;
        }

        public String getBirth_year() {
            return birth_year;
        }

        public void setBirth_year(String birth_year) {
            this.birth_year = birth_year;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public boolean isPawoo() {
            return pawoo;
        }

        public void setPawoo(boolean pawoo) {
            this.pawoo = pawoo;
        }
    }

    public static class WorkspaceBean {
        /**
         * pc :
         * monitor :
         * tool :
         * scanner :
         * tablet :
         * mouse :
         * printer :
         * desktop :
         * music :
         * desk :
         * chair :
         * comment :
         * workspace_image_url : null
         */

        private String pc;
        private String monitor;
        private String tool;
        private String scanner;
        private String tablet;
        private String mouse;
        private String printer;
        private String desktop;
        private String music;
        private String desk;
        private String chair;
        private String comment;
        private Object workspace_image_url;

        public String getPc() {
            return pc;
        }

        public void setPc(String pc) {
            this.pc = pc;
        }

        public String getMonitor() {
            return monitor;
        }

        public void setMonitor(String monitor) {
            this.monitor = monitor;
        }

        public String getTool() {
            return tool;
        }

        public void setTool(String tool) {
            this.tool = tool;
        }

        public String getScanner() {
            return scanner;
        }

        public void setScanner(String scanner) {
            this.scanner = scanner;
        }

        public String getTablet() {
            return tablet;
        }

        public void setTablet(String tablet) {
            this.tablet = tablet;
        }

        public String getMouse() {
            return mouse;
        }

        public void setMouse(String mouse) {
            this.mouse = mouse;
        }

        public String getPrinter() {
            return printer;
        }

        public void setPrinter(String printer) {
            this.printer = printer;
        }

        public String getDesktop() {
            return desktop;
        }

        public void setDesktop(String desktop) {
            this.desktop = desktop;
        }

        public String getMusic() {
            return music;
        }

        public void setMusic(String music) {
            this.music = music;
        }

        public String getDesk() {
            return desk;
        }

        public void setDesk(String desk) {
            this.desk = desk;
        }

        public String getChair() {
            return chair;
        }

        public void setChair(String chair) {
            this.chair = chair;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public Object getWorkspace_image_url() {
            return workspace_image_url;
        }

        public void setWorkspace_image_url(Object workspace_image_url) {
            this.workspace_image_url = workspace_image_url;
        }
    }
}
