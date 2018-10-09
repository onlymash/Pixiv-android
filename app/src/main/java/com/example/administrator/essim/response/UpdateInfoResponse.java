package com.example.administrator.essim.response;

public class UpdateInfoResponse {


    /**
     * body : {"is_succeed":false,"validation_errors":{"mail_address":"このメールアドレスはすでに登録されています"}}
     */

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



    public boolean error;
    public String message;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }


    public static class BodyBean {
        /**
         * is_succeed : false
         * validation_errors : {"mail_address":"このメールアドレスはすでに登録されています"}
         */

        private boolean is_succeed;
        private ValidationErrorsBean validation_errors;

        public boolean isIs_succeed() {
            return is_succeed;
        }

        public void setIs_succeed(boolean is_succeed) {
            this.is_succeed = is_succeed;
        }

        public ValidationErrorsBean getValidation_errors() {
            return validation_errors;
        }

        public void setValidation_errors(ValidationErrorsBean validation_errors) {
            this.validation_errors = validation_errors;
        }

        public static class ValidationErrorsBean {
            /**
             * mail_address : このメールアドレスはすでに登録されています
             */

            private String mail_address;

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            private String password;

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            private String message;

            public String getPixiv_id() {
                return pixiv_id;
            }

            public void setPixiv_id(String pixiv_id) {
                this.pixiv_id = pixiv_id;
            }

            private String pixiv_id;

            public String getMail_address() {
                return mail_address;
            }

            public void setMail_address(String mail_address) {
                this.mail_address = mail_address;
            }
        }
    }
}
