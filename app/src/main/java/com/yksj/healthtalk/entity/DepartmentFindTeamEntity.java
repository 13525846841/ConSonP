package com.yksj.healthtalk.entity;

import java.util.List;

/**
 * Created by hekl on 18/4/24.
 */

public class DepartmentFindTeamEntity

{
    /**
     * code : 1
     * message : success
     * result : [{"OFFICE_ID":1024,"OFFICE_NAME":"儿科综合","SITE_COUNT":6,"DOCTOR_COUNT":6,"ORDER_COUNT":54},{"OFFICE_ID":1012,"OFFICE_NAME":"小儿内科","SITE_COUNT":1,"DOCTOR_COUNT":1,"ORDER_COUNT":0},{"OFFICE_ID":1001,"OFFICE_NAME":"小儿呼吸科","SITE_COUNT":1,"DOCTOR_COUNT":1,"ORDER_COUNT":0},{"OFFICE_ID":1,"OFFICE_NAME":"呼吸内科","SITE_COUNT":1,"DOCTOR_COUNT":1,"ORDER_COUNT":0},{"OFFICE_ID":1017,"OFFICE_NAME":"小儿外科","SITE_COUNT":1,"DOCTOR_COUNT":1,"ORDER_COUNT":0}]
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
         * OFFICE_ID : 1024
         * OFFICE_NAME : 儿科综合
         * SITE_COUNT : 6
         * DOCTOR_COUNT : 6
         * ORDER_COUNT : 54
         */

        private int OFFICE_ID;
        private String OFFICE_NAME;
        private int SITE_COUNT;
        private int DOCTOR_COUNT;
        private int ORDER_COUNT;

        public int getOFFICE_ID() {
            return OFFICE_ID;
        }

        public void setOFFICE_ID(int OFFICE_ID) {
            this.OFFICE_ID = OFFICE_ID;
        }

        public String getOFFICE_NAME() {
            return OFFICE_NAME;
        }

        public void setOFFICE_NAME(String OFFICE_NAME) {
            this.OFFICE_NAME = OFFICE_NAME;
        }

        public int getSITE_COUNT() {
            return SITE_COUNT;
        }

        public void setSITE_COUNT(int SITE_COUNT) {
            this.SITE_COUNT = SITE_COUNT;
        }

        public int getDOCTOR_COUNT() {
            return DOCTOR_COUNT;
        }

        public void setDOCTOR_COUNT(int DOCTOR_COUNT) {
            this.DOCTOR_COUNT = DOCTOR_COUNT;
        }

        public int getORDER_COUNT() {
            return ORDER_COUNT;
        }

        public void setORDER_COUNT(int ORDER_COUNT) {
            this.ORDER_COUNT = ORDER_COUNT;
        }
    }
}
