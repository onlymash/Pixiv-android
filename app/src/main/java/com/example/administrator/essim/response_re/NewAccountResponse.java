package com.example.administrator.essim.response_re;

public class NewAccountResponse {


    /**
     * error : false
     * message :
     * body : {"user_account":"user_djvx5583","password":"kcuvDmY9HC","device_token":"5bfc61d2686eb0755cc2d6cec4875f70"}
     */

    private boolean error;
    private String message;
    private BodyBean body;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class BodyBean {
        /**
         * user_account : user_djvx5583
         * password : kcuvDmY9HC
         * device_token : 5bfc61d2686eb0755cc2d6cec4875f70
         */

        private String user_account;
        private String password;
        private String device_token;

        public String getUser_account() {
            return user_account;
        }

        public void setUser_account(String user_account) {
            this.user_account = user_account;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDevice_token() {
            return device_token;
        }

        public void setDevice_token(String device_token) {
            this.device_token = device_token;
        }
    }
}
