package com.yksj.healthtalk.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hekl on 18/7/26.
 */

public class WeiXinPayResultJson {
    /**
     * code : 1
     * message : 操作成功
     * result : {"message":"操作成功","repmap":{"sign":"98D3B38040200759319B9E8D8D792F37","timestamp":"1532602888","noncestr":"svcywerhluc2lcfdvnqs3wy52rpi5o6z","partnerid":"1481493372","prepayid":"wx26190131038460fa7e6f397a2650711693","package":"Sign=WXPay","appid":"wx00b1b581f6ae7623"},"COURSE_CLASS":"20","SITE_ID":"366","code":1,"pay_id":"1180726300190104"}
     */

    private String code;
    private String message;
    private ResultBean result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * message : 操作成功
         * repmap : {"sign":"98D3B38040200759319B9E8D8D792F37","timestamp":"1532602888","noncestr":"svcywerhluc2lcfdvnqs3wy52rpi5o6z","partnerid":"1481493372","prepayid":"wx26190131038460fa7e6f397a2650711693","package":"Sign=WXPay","appid":"wx00b1b581f6ae7623"}
         * COURSE_CLASS : 20
         * SITE_ID : 366
         * code : 1
         * pay_id : 1180726300190104
         */

        private String message;
        private RepmapBean repmap;
        private String COURSE_CLASS;
        private String SITE_ID;
        private int code;
        private String pay_id;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public RepmapBean getRepmap() {
            return repmap;
        }

        public void setRepmap(RepmapBean repmap) {
            this.repmap = repmap;
        }

        public String getCOURSE_CLASS() {
            return COURSE_CLASS;
        }

        public void setCOURSE_CLASS(String COURSE_CLASS) {
            this.COURSE_CLASS = COURSE_CLASS;
        }

        public String getSITE_ID() {
            return SITE_ID;
        }

        public void setSITE_ID(String SITE_ID) {
            this.SITE_ID = SITE_ID;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getPay_id() {
            return pay_id;
        }

        public void setPay_id(String pay_id) {
            this.pay_id = pay_id;
        }

        public static class RepmapBean {
            /**
             * sign : 98D3B38040200759319B9E8D8D792F37
             * timestamp : 1532602888
             * noncestr : svcywerhluc2lcfdvnqs3wy52rpi5o6z
             * partnerid : 1481493372
             * prepayid : wx26190131038460fa7e6f397a2650711693
             * package : Sign=WXPay
             * appid : wx00b1b581f6ae7623
             */

            private String sign;
            private String timestamp;
            private String noncestr;
            private String partnerid;
            private String prepayid;
            @SerializedName("package")
            private String packageX;
            private String appid;

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            public String getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(String timestamp) {
                this.timestamp = timestamp;
            }

            public String getNoncestr() {
                return noncestr;
            }

            public void setNoncestr(String noncestr) {
                this.noncestr = noncestr;
            }

            public String getPartnerid() {
                return partnerid;
            }

            public void setPartnerid(String partnerid) {
                this.partnerid = partnerid;
            }

            public String getPrepayid() {
                return prepayid;
            }

            public void setPrepayid(String prepayid) {
                this.prepayid = prepayid;
            }

            public String getPackageX() {
                return packageX;
            }

            public void setPackageX(String packageX) {
                this.packageX = packageX;
            }

            public String getAppid() {
                return appid;
            }

            public void setAppid(String appid) {
                this.appid = appid;
            }
        }
    }
}
