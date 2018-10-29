package com.yksj.healthtalk.entity;

import java.util.List;

/**
 * Created by hekl on 18/5/2.
 */

public class DoctorworksTeamEntity {
    /**
     * code : 1
     * message : 查询完成
     * result : [{"SITE_ID":354,"CUSTOMER_ID":3779,"MEMBER_TYPE":10,"MEMBER_STATUS":10,"MEMBER_STATUS_TIME":20180427151649,"CLIENT_ICON_BACKGROUND":"/FalseDoctorPic/20150701.jpg","BIG_ICON_BACKGROUND":"/FalseDoctorPic/20150701.jpg","DOCTOR_REAL_NAME":"赵琴","INTRODUCTION":null,"WORK_LOCATION_DESC":"北京市 西城区","DOCTOR_OFFICE2":1003,"OFFICE_NAME":"小儿感染科","DOCTOR_TITLE":2,"TITLE_NAME":"副主任医师"},{"SITE_ID":354,"CUSTOMER_ID":124783,"MEMBER_TYPE":30,"MEMBER_STATUS":10,"MEMBER_STATUS_TIME":20180427164942,"CLIENT_ICON_BACKGROUND":"/CusZiYuan/resources/124783/doctorPic/20150717191416.png","BIG_ICON_BACKGROUND":"/CusZiYuan/resources/124783/doctorPic/doc_100_20150717191416.png","DOCTOR_REAL_NAME":"苏芮","INTRODUCTION":"嘎嘎嘎，小公举","WORK_LOCATION_DESC":"北京市 东城区","DOCTOR_OFFICE2":1003,"OFFICE_NAME":"小儿感染科","DOCTOR_TITLE":1,"TITLE_NAME":"主任医师"}]
     */

    private String code;
    private String message;
    private List<ResultBean> result;

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

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * SITE_ID : 354
         * CUSTOMER_ID : 3779
         * MEMBER_TYPE : 10
         * MEMBER_STATUS : 10
         * MEMBER_STATUS_TIME : 20180427151649
         * CLIENT_ICON_BACKGROUND : /FalseDoctorPic/20150701.jpg
         * BIG_ICON_BACKGROUND : /FalseDoctorPic/20150701.jpg
         * DOCTOR_REAL_NAME : 赵琴
         * INTRODUCTION : null
         * WORK_LOCATION_DESC : 北京市 西城区
         * DOCTOR_OFFICE2 : 1003
         * OFFICE_NAME : 小儿感染科
         * DOCTOR_TITLE : 2
         * TITLE_NAME : 副主任医师
         */

        private int SITE_ID;
        private int CUSTOMER_ID;
        private int MEMBER_TYPE;
        private int MEMBER_STATUS;
        private long MEMBER_STATUS_TIME;
        private String CLIENT_ICON_BACKGROUND;
        private String BIG_ICON_BACKGROUND;
        private String DOCTOR_REAL_NAME;
        private Object INTRODUCTION;
        private String WORK_LOCATION_DESC;
        private int DOCTOR_OFFICE2;
        private String OFFICE_NAME;
        private int DOCTOR_TITLE;
        private String TITLE_NAME;

        public int getSITE_ID() {
            return SITE_ID;
        }

        public void setSITE_ID(int SITE_ID) {
            this.SITE_ID = SITE_ID;
        }

        public int getCUSTOMER_ID() {
            return CUSTOMER_ID;
        }

        public void setCUSTOMER_ID(int CUSTOMER_ID) {
            this.CUSTOMER_ID = CUSTOMER_ID;
        }

        public int getMEMBER_TYPE() {
            return MEMBER_TYPE;
        }

        public void setMEMBER_TYPE(int MEMBER_TYPE) {
            this.MEMBER_TYPE = MEMBER_TYPE;
        }

        public int getMEMBER_STATUS() {
            return MEMBER_STATUS;
        }

        public void setMEMBER_STATUS(int MEMBER_STATUS) {
            this.MEMBER_STATUS = MEMBER_STATUS;
        }

        public long getMEMBER_STATUS_TIME() {
            return MEMBER_STATUS_TIME;
        }

        public void setMEMBER_STATUS_TIME(long MEMBER_STATUS_TIME) {
            this.MEMBER_STATUS_TIME = MEMBER_STATUS_TIME;
        }

        public String getCLIENT_ICON_BACKGROUND() {
            return CLIENT_ICON_BACKGROUND;
        }

        public void setCLIENT_ICON_BACKGROUND(String CLIENT_ICON_BACKGROUND) {
            this.CLIENT_ICON_BACKGROUND = CLIENT_ICON_BACKGROUND;
        }

        public String getBIG_ICON_BACKGROUND() {
            return BIG_ICON_BACKGROUND;
        }

        public void setBIG_ICON_BACKGROUND(String BIG_ICON_BACKGROUND) {
            this.BIG_ICON_BACKGROUND = BIG_ICON_BACKGROUND;
        }

        public String getDOCTOR_REAL_NAME() {
            return DOCTOR_REAL_NAME;
        }

        public void setDOCTOR_REAL_NAME(String DOCTOR_REAL_NAME) {
            this.DOCTOR_REAL_NAME = DOCTOR_REAL_NAME;
        }

        public Object getINTRODUCTION() {
            return INTRODUCTION;
        }

        public void setINTRODUCTION(Object INTRODUCTION) {
            this.INTRODUCTION = INTRODUCTION;
        }

        public String getWORK_LOCATION_DESC() {
            return WORK_LOCATION_DESC;
        }

        public void setWORK_LOCATION_DESC(String WORK_LOCATION_DESC) {
            this.WORK_LOCATION_DESC = WORK_LOCATION_DESC;
        }

        public int getDOCTOR_OFFICE2() {
            return DOCTOR_OFFICE2;
        }

        public void setDOCTOR_OFFICE2(int DOCTOR_OFFICE2) {
            this.DOCTOR_OFFICE2 = DOCTOR_OFFICE2;
        }

        public String getOFFICE_NAME() {
            return OFFICE_NAME;
        }

        public void setOFFICE_NAME(String OFFICE_NAME) {
            this.OFFICE_NAME = OFFICE_NAME;
        }

        public int getDOCTOR_TITLE() {
            return DOCTOR_TITLE;
        }

        public void setDOCTOR_TITLE(int DOCTOR_TITLE) {
            this.DOCTOR_TITLE = DOCTOR_TITLE;
        }

        public String getTITLE_NAME() {
            return TITLE_NAME;
        }

        public void setTITLE_NAME(String TITLE_NAME) {
            this.TITLE_NAME = TITLE_NAME;
        }
    }
}
