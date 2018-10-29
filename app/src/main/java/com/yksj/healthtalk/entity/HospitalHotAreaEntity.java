package com.yksj.healthtalk.entity;

import java.util.List;

/**
 * Created by hekl on 18/4/25.
 */

public class HospitalHotAreaEntity {
    /**
     * code : 1
     * message : success
     * result : [{"AREA_CODE":"110000","AREA_NAME":"北京市","SUB_AREA_CODE":"0","AREA_LEVEL":"直辖市","VALID_FLAG":"1","NOTES":"39.55,116.24","PIN_YIN":"bjs","AREA_NAME2":"北京"},{"AREA_CODE":"120000","AREA_NAME":"天津市","SUB_AREA_CODE":"0","AREA_LEVEL":"直辖市","VALID_FLAG":"0","NOTES":"39.02,117.12","PIN_YIN":"tjs","AREA_NAME2":"天津"},{"AREA_CODE":"310000","AREA_NAME":"上海市","SUB_AREA_CODE":"0","AREA_LEVEL":"直辖市","VALID_FLAG":"1","NOTES":"31.14,121.29","PIN_YIN":"shs","AREA_NAME2":"上海"},{"AREA_CODE":"330100","AREA_NAME":"杭州市","SUB_AREA_CODE":"330000","AREA_LEVEL":"地级市","VALID_FLAG":"0","NOTES":null,"PIN_YIN":"hzs","AREA_NAME2":"杭州"},{"AREA_CODE":"440100","AREA_NAME":"广州市","SUB_AREA_CODE":"440000","AREA_LEVEL":"地级市","VALID_FLAG":"1","NOTES":null,"PIN_YIN":"gzs","AREA_NAME2":"广州"},{"AREA_CODE":"440300","AREA_NAME":"深圳市","SUB_AREA_CODE":"440000","AREA_LEVEL":"地级市","VALID_FLAG":"1","NOTES":null,"PIN_YIN":"szs","AREA_NAME2":"深圳"}]
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
         * AREA_CODE : 110000
         * AREA_NAME : 北京市
         * SUB_AREA_CODE : 0
         * AREA_LEVEL : 直辖市
         * VALID_FLAG : 1
         * NOTES : 39.55,116.24
         * PIN_YIN : bjs
         * AREA_NAME2 : 北京
         */

        private String AREA_CODE;
        private String AREA_NAME;
        private String SUB_AREA_CODE;
        private String AREA_LEVEL;
        private String VALID_FLAG;
        private String NOTES;
        private String PIN_YIN;
        private String AREA_NAME2;

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

        public String getSUB_AREA_CODE() {
            return SUB_AREA_CODE;
        }

        public void setSUB_AREA_CODE(String SUB_AREA_CODE) {
            this.SUB_AREA_CODE = SUB_AREA_CODE;
        }

        public String getAREA_LEVEL() {
            return AREA_LEVEL;
        }

        public void setAREA_LEVEL(String AREA_LEVEL) {
            this.AREA_LEVEL = AREA_LEVEL;
        }

        public String getVALID_FLAG() {
            return VALID_FLAG;
        }

        public void setVALID_FLAG(String VALID_FLAG) {
            this.VALID_FLAG = VALID_FLAG;
        }

        public String getNOTES() {
            return NOTES;
        }

        public void setNOTES(String NOTES) {
            this.NOTES = NOTES;
        }

        public String getPIN_YIN() {
            return PIN_YIN;
        }

        public void setPIN_YIN(String PIN_YIN) {
            this.PIN_YIN = PIN_YIN;
        }

        public String getAREA_NAME2() {
            return AREA_NAME2;
        }

        public void setAREA_NAME2(String AREA_NAME2) {
            this.AREA_NAME2 = AREA_NAME2;
        }
    }
}
