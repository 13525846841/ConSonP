package com.yksj.healthtalk.entity;

import java.util.List;

/**
 * Created by hekl on 18/4/26.
 */

public class SelectChildCityEntity
{
    /**
     * message : 查询成功
     * area : [{"AREA_CODE":"340100","AREA_NAME":"合肥市","SUB_AREA_CODE":"340000","AREA_LEVEL":"地级市","VALID_FLAG":"1","NOTES":null,"PIN_YIN":"hfs","AREA_NAME2":"合肥"},{"AREA_CODE":"340200","AREA_NAME":"芜湖市","SUB_AREA_CODE":"340000","AREA_LEVEL":"地级市","VALID_FLAG":"1","NOTES":null,"PIN_YIN":"whs","AREA_NAME2":"芜湖"},{"AREA_CODE":"340300","AREA_NAME":"蚌埠市","SUB_AREA_CODE":"340000","AREA_LEVEL":"地级市","VALID_FLAG":"1","NOTES":null,"PIN_YIN":"bbs","AREA_NAME2":"蚌埠"},{"AREA_CODE":"340400","AREA_NAME":"淮南市","SUB_AREA_CODE":"340000","AREA_LEVEL":"地级市","VALID_FLAG":"1","NOTES":null,"PIN_YIN":"hns","AREA_NAME2":"淮南"},{"AREA_CODE":"340500","AREA_NAME":"马鞍山市","SUB_AREA_CODE":"340000","AREA_LEVEL":"地级市","VALID_FLAG":"1","NOTES":null,"PIN_YIN":"mass","AREA_NAME2":"马鞍山"},{"AREA_CODE":"340600","AREA_NAME":"淮北市","SUB_AREA_CODE":"340000","AREA_LEVEL":"地级市","VALID_FLAG":"1","NOTES":null,"PIN_YIN":"hbs","AREA_NAME2":"淮北"},{"AREA_CODE":"340700","AREA_NAME":"铜陵市","SUB_AREA_CODE":"340000","AREA_LEVEL":"地级市","VALID_FLAG":"1","NOTES":null,"PIN_YIN":"tls","AREA_NAME2":"铜陵"},{"AREA_CODE":"340800","AREA_NAME":"安庆市","SUB_AREA_CODE":"340000","AREA_LEVEL":"地级市","VALID_FLAG":"1","NOTES":null,"PIN_YIN":"aqs","AREA_NAME2":"安庆"},{"AREA_CODE":"341000","AREA_NAME":"黄山市","SUB_AREA_CODE":"340000","AREA_LEVEL":"地级市","VALID_FLAG":"1","NOTES":null,"PIN_YIN":"hss","AREA_NAME2":"黄山"},{"AREA_CODE":"341100","AREA_NAME":"滁州市","SUB_AREA_CODE":"340000","AREA_LEVEL":"地级市","VALID_FLAG":"1","NOTES":null,"PIN_YIN":"czs","AREA_NAME2":"滁州"},{"AREA_CODE":"341200","AREA_NAME":"阜阳市","SUB_AREA_CODE":"340000","AREA_LEVEL":"地级市","VALID_FLAG":"1","NOTES":null,"PIN_YIN":"fys","AREA_NAME2":"阜阳"},{"AREA_CODE":"341300","AREA_NAME":"宿州市","SUB_AREA_CODE":"340000","AREA_LEVEL":"地级市","VALID_FLAG":"1","NOTES":null,"PIN_YIN":"szs","AREA_NAME2":"宿州"},{"AREA_CODE":"341500","AREA_NAME":"六安市","SUB_AREA_CODE":"340000","AREA_LEVEL":"地级市","VALID_FLAG":"1","NOTES":null,"PIN_YIN":"las","AREA_NAME2":"六安"},{"AREA_CODE":"341600","AREA_NAME":"亳州市","SUB_AREA_CODE":"340000","AREA_LEVEL":"地级市","VALID_FLAG":"1","NOTES":null,"PIN_YIN":"bzs","AREA_NAME2":"亳州"},{"AREA_CODE":"341700","AREA_NAME":"池州市","SUB_AREA_CODE":"340000","AREA_LEVEL":"地级市","VALID_FLAG":"1","NOTES":null,"PIN_YIN":"czs","AREA_NAME2":"池州"},{"AREA_CODE":"341800","AREA_NAME":"宣城市","SUB_AREA_CODE":"340000","AREA_LEVEL":"地级市","VALID_FLAG":"1","NOTES":null,"PIN_YIN":"xcs","AREA_NAME2":"宣城"}]
     * code : 0
     */

    private String message;
    private int code;
    private List<AreaBean> area;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<AreaBean> getArea() {
        return area;
    }

    public void setArea(List<AreaBean> area) {
        this.area = area;
    }

    public static class AreaBean {
        /**
         * AREA_CODE : 340100
         * AREA_NAME : 合肥市
         * SUB_AREA_CODE : 340000
         * AREA_LEVEL : 地级市
         * VALID_FLAG : 1
         * NOTES : null
         * PIN_YIN : hfs
         * AREA_NAME2 : 合肥
         */

        private String AREA_CODE;
        private String AREA_NAME;
        private String SUB_AREA_CODE;
        private String AREA_LEVEL;
        private String VALID_FLAG;
        private Object NOTES;
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

        public Object getNOTES() {
            return NOTES;
        }

        public void setNOTES(Object NOTES) {
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
