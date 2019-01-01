package com.example.administrator.essim.response;

import com.example.administrator.essim.response_re.UserBean;

import java.io.Serializable;
import java.util.List;

public class IllustCommentsResponse implements Serializable {
    private List<CommentsBean> comments;
    private String next_url;
    private int total_comments;

    public List<CommentsBean> getComments() {
        return this.comments;
    }

    public void setComments(List<CommentsBean> paramList) {
        this.comments = paramList;
    }

    public String getNext_url() {
        return this.next_url;
    }

    public void setNext_url(String paramString) {
        this.next_url = paramString;
    }

    public int getTotal_comments() {
        return this.total_comments;
    }

    public void setTotal_comments(int paramInt) {
        this.total_comments = paramInt;
    }

    public static class CommentsBean implements Serializable {
        private String comment;
        private String date;
        private int id;
        private ParentCommentBean parent_comment;
        private UserBean user;

        public String getComment() {
            return this.comment;
        }

        public void setComment(String paramString) {
            this.comment = paramString;
        }

        public String getDate() {
            return this.date;
        }

        public void setDate(String paramString) {
            this.date = paramString;
        }

        public int getId() {
            return this.id;
        }

        public void setId(int paramInt) {
            this.id = paramInt;
        }

        public ParentCommentBean getParent_comment() {
            return this.parent_comment;
        }

        public void setParent_comment(ParentCommentBean paramParentCommentBean) {
            this.parent_comment = paramParentCommentBean;
        }

        public UserBean getUser() {
            return this.user;
        }

        public void setUser(UserBean paramUserBean) {
            this.user = paramUserBean;
        }

        public static class ParentCommentBean implements Serializable {
            private String comment;
            private String date;
            private int id;
            private UserBean user;

            public String getComment() {
                return this.comment;
            }

            public void setComment(String paramString) {
                this.comment = paramString;
            }

            public String getDate() {
                return this.date;
            }

            public void setDate(String paramString) {
                this.date = paramString;
            }

            public int getId() {
                return this.id;
            }

            public void setId(int paramInt) {
                this.id = paramInt;
            }

            public UserBean getUser() {
                return this.user;
            }

            public void setUser(UserBean paramUserBeanX) {
                this.user = paramUserBeanX;
            }
        }
    }
}