package com.yksj.healthtalk.entity;

/**
 * Created by hekl on 18/7/4.
 */

public class DoctorBlocHomeEntity {
    /**
     * code : 1
     * message : success
     * result : {"UNION_ID":420,"UNION_NAME":"北京联盟","BE_GOOD":"北京联盟擅长治疗各种疑难杂症....","UNION_DESC":"北京联盟介绍","BACKGROUND":"CusZiYuan/resources/union_background/beijing.png","VISIT_TIME":144,"CREATE_ID":null,"CREATE_TIME":null,"FOLLOW_COUNT":1,"FOLLOW_FLAG":0,"EXPERT_COUNT":3,"qrCodeUrl":"http://www.baidu.com","JOIN_FLAG":3,"JOIN_DESC":"底部不显示任何按钮"}
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
         * UNION_ID : 420
         * UNION_NAME : 北京联盟
         * BE_GOOD : 北京联盟擅长治疗各种疑难杂症....
         * UNION_DESC : 北京联盟介绍
         * BACKGROUND : CusZiYuan/resources/union_background/beijing.png
         * VISIT_TIME : 144
         * CREATE_ID : null
         * CREATE_TIME : null
         * FOLLOW_COUNT : 1
         * FOLLOW_FLAG : 0
         * EXPERT_COUNT : 3
         * qrCodeUrl : http://www.baidu.com
         * JOIN_FLAG : 3
         * JOIN_DESC : 底部不显示任何按钮
         */

        private int UNION_ID;
        private String UNION_NAME;
        private String BE_GOOD;
        private String UNION_DESC;
        private String BACKGROUND;
        private int VISIT_TIME;
        private Object CREATE_ID;
        private Object CREATE_TIME;
        private int FOLLOW_COUNT;
        private int FOLLOW_FLAG;
        private int EXPERT_COUNT;
        private String qrCodeUrl;
        private int JOIN_FLAG;
        private String JOIN_DESC;

        public int getUNION_ID() {
            return UNION_ID;
        }

        public void setUNION_ID(int UNION_ID) {
            this.UNION_ID = UNION_ID;
        }

        public String getUNION_NAME() {
            return UNION_NAME;
        }

        public void setUNION_NAME(String UNION_NAME) {
            this.UNION_NAME = UNION_NAME;
        }

        public String getBE_GOOD() {
            return BE_GOOD;
        }

        public void setBE_GOOD(String BE_GOOD) {
            this.BE_GOOD = BE_GOOD;
        }

        public String getUNION_DESC() {
            return UNION_DESC;
        }

        public void setUNION_DESC(String UNION_DESC) {
            this.UNION_DESC = UNION_DESC;
        }

        public String getBACKGROUND() {
            return BACKGROUND;
        }

        public void setBACKGROUND(String BACKGROUND) {
            this.BACKGROUND = BACKGROUND;
        }

        public int getVISIT_TIME() {
            return VISIT_TIME;
        }

        public void setVISIT_TIME(int VISIT_TIME) {
            this.VISIT_TIME = VISIT_TIME;
        }

        public Object getCREATE_ID() {
            return CREATE_ID;
        }

        public void setCREATE_ID(Object CREATE_ID) {
            this.CREATE_ID = CREATE_ID;
        }

        public Object getCREATE_TIME() {
            return CREATE_TIME;
        }

        public void setCREATE_TIME(Object CREATE_TIME) {
            this.CREATE_TIME = CREATE_TIME;
        }

        public int getFOLLOW_COUNT() {
            return FOLLOW_COUNT;
        }

        public void setFOLLOW_COUNT(int FOLLOW_COUNT) {
            this.FOLLOW_COUNT = FOLLOW_COUNT;
        }

        public int getFOLLOW_FLAG() {
            return FOLLOW_FLAG;
        }

        public void setFOLLOW_FLAG(int FOLLOW_FLAG) {
            this.FOLLOW_FLAG = FOLLOW_FLAG;
        }

        public int getEXPERT_COUNT() {
            return EXPERT_COUNT;
        }

        public void setEXPERT_COUNT(int EXPERT_COUNT) {
            this.EXPERT_COUNT = EXPERT_COUNT;
        }

        public String getQrCodeUrl() {
            return qrCodeUrl;
        }

        public void setQrCodeUrl(String qrCodeUrl) {
            this.qrCodeUrl = qrCodeUrl;
        }

        public int getJOIN_FLAG() {
            return JOIN_FLAG;
        }

        public void setJOIN_FLAG(int JOIN_FLAG) {
            this.JOIN_FLAG = JOIN_FLAG;
        }

        public String getJOIN_DESC() {
            return JOIN_DESC;
        }

        public void setJOIN_DESC(String JOIN_DESC) {
            this.JOIN_DESC = JOIN_DESC;
        }
    }
}
