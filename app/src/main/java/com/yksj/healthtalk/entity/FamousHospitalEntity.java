package com.yksj.healthtalk.entity;

import java.util.List;

/**
 * Created by hekl on 18/5/14.
 */

public class FamousHospitalEntity {
    /**
     * code : 1
     * message : success
     * result : [{"HOSPITAL_ID":2,"HOSPITAL_NAME":"北京六一儿童医院","HOSPITAL_NAME_PINYIN":"beijingliuyiertongyiyuan","EXPLAIN":null,"HOSPITAL_DESC":null,"CREATE_TIME":null,"VALID_FLAG":1,"AREA_CODE":"110000","AREA_NAME":"北京","FIRST_LETTER":"b"},{"HOSPITAL_ID":1,"HOSPITAL_NAME":"上海六一儿童医院","HOSPITAL_NAME_PINYIN":"shanghailiuyiertongyiyuan","EXPLAIN":null,"HOSPITAL_DESC":null,"CREATE_TIME":null,"VALID_FLAG":1,"AREA_CODE":"310000","AREA_NAME":"上海","FIRST_LETTER":"s"}]
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
         * HOSPITAL_ID : 2
         * HOSPITAL_NAME : 北京六一儿童医院
         * HOSPITAL_NAME_PINYIN : beijingliuyiertongyiyuan
         * EXPLAIN : null
         * HOSPITAL_DESC : null
         * CREATE_TIME : null
         * VALID_FLAG : 1
         * AREA_CODE : 110000
         * AREA_NAME : 北京
         * FIRST_LETTER : b
         */

        private int HOSPITAL_ID;
        private String HOSPITAL_NAME;
        private String HOSPITAL_NAME_PINYIN;
        private Object EXPLAIN;
        private Object HOSPITAL_DESC;
        private Object CREATE_TIME;
        private int VALID_FLAG;
        private String AREA_CODE;
        private String AREA_NAME;
        private String FIRST_LETTER;

        public int getHOSPITAL_ID() {
            return HOSPITAL_ID;
        }

        public void setHOSPITAL_ID(int HOSPITAL_ID) {
            this.HOSPITAL_ID = HOSPITAL_ID;
        }

        public String getHOSPITAL_NAME() {
            return HOSPITAL_NAME;
        }

        public void setHOSPITAL_NAME(String HOSPITAL_NAME) {
            this.HOSPITAL_NAME = HOSPITAL_NAME;
        }

        public String getHOSPITAL_NAME_PINYIN() {
            return HOSPITAL_NAME_PINYIN;
        }

        public void setHOSPITAL_NAME_PINYIN(String HOSPITAL_NAME_PINYIN) {
            this.HOSPITAL_NAME_PINYIN = HOSPITAL_NAME_PINYIN;
        }

        public Object getEXPLAIN() {
            return EXPLAIN;
        }

        public void setEXPLAIN(Object EXPLAIN) {
            this.EXPLAIN = EXPLAIN;
        }

        public Object getHOSPITAL_DESC() {
            return HOSPITAL_DESC;
        }

        public void setHOSPITAL_DESC(Object HOSPITAL_DESC) {
            this.HOSPITAL_DESC = HOSPITAL_DESC;
        }

        public Object getCREATE_TIME() {
            return CREATE_TIME;
        }

        public void setCREATE_TIME(Object CREATE_TIME) {
            this.CREATE_TIME = CREATE_TIME;
        }

        public int getVALID_FLAG() {
            return VALID_FLAG;
        }

        public void setVALID_FLAG(int VALID_FLAG) {
            this.VALID_FLAG = VALID_FLAG;
        }

        public String getAREA_CODE() {
            return AREA_CODE;
        }

        public void setAREA_CODE(String AREA_CODE) {
            this.AREA_CODE = AREA_CODE;
        }

        public String getAREA_NAME() {
            return AREA_NAME;
        }

        public void setAREA_NAME(String AREA_NAME) {
            this.AREA_NAME = AREA_NAME;
        }

        public String getFIRST_LETTER() {
            return FIRST_LETTER;
        }

        public void setFIRST_LETTER(String FIRST_LETTER) {
            this.FIRST_LETTER = FIRST_LETTER;
        }
    }
}
