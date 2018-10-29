package com.yksj.healthtalk.entity;

import java.util.List;

/**
 * Created by hekl on 18/8/21.
 */

public class HealthLectureRecEntity {
    /**
     * code : 1
     * message : 操作完成
     * result : [{"COURSE_UP_NAME":null,"COURSE_ID":"131","SITE_ID":"389","COURSE_UP_ID":"3774","COURSE_UP_TIME":"20180720092600","COURSE_NAME":"1","SMALL_PIC":"''","COURSE_CLASS":"20","RN":1},{"COURSE_UP_NAME":null,"COURSE_ID":"125","SITE_ID":"389","COURSE_UP_ID":"3774","COURSE_UP_TIME":"20180720085848","COURSE_NAME":"ç\u0089¹ä¹\u0090å\u0095\u008aå\u0098\u009eå\u0098\u009eé¥¿äº\u0086","SMALL_PIC":"''","COURSE_CLASS":"20","RN":2},{"COURSE_UP_NAME":"李晓铃","COURSE_ID":"117","SITE_ID":"366","COURSE_UP_ID":"3774","COURSE_UP_TIME":"20180712090943","COURSE_NAME":"test","SMALL_PIC":"''","COURSE_CLASS":"20","RN":3},{"COURSE_UP_NAME":"李晓铃","COURSE_ID":"116","SITE_ID":"366","COURSE_UP_ID":"3774","COURSE_UP_TIME":"20180712090623","COURSE_NAME":"test","SMALL_PIC":"''","COURSE_CLASS":"20","RN":4},{"COURSE_UP_NAME":"李庆怀","COURSE_ID":"115","SITE_ID":"366","COURSE_UP_ID":"3774","COURSE_UP_TIME":"20180712090433","COURSE_NAME":"test","SMALL_PIC":"''","COURSE_CLASS":"20","RN":5}]
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
         * COURSE_UP_NAME : null
         * COURSE_ID : 131
         * SITE_ID : 389
         * COURSE_UP_ID : 3774
         * COURSE_UP_TIME : 20180720092600
         * COURSE_NAME : 1
         * SMALL_PIC : ''
         * COURSE_CLASS : 20
         * RN : 1
         */

        private String COURSE_UP_NAME;
        private String COURSE_ID;
        private String SITE_ID;
        private String COURSE_UP_ID;
        private String COURSE_UP_TIME;
        private String COURSE_NAME;
        private String SMALL_PIC;
        private String COURSE_CLASS;
        private int RN;

        public String getCOURSE_UP_NAME() {
            return COURSE_UP_NAME;
        }

        public void setCOURSE_UP_NAME(String COURSE_UP_NAME) {
            this.COURSE_UP_NAME = COURSE_UP_NAME;
        }

        public String getCOURSE_ID() {
            return COURSE_ID;
        }

        public void setCOURSE_ID(String COURSE_ID) {
            this.COURSE_ID = COURSE_ID;
        }

        public String getSITE_ID() {
            return SITE_ID;
        }

        public void setSITE_ID(String SITE_ID) {
            this.SITE_ID = SITE_ID;
        }

        public String getCOURSE_UP_ID() {
            return COURSE_UP_ID;
        }

        public void setCOURSE_UP_ID(String COURSE_UP_ID) {
            this.COURSE_UP_ID = COURSE_UP_ID;
        }

        public String getCOURSE_UP_TIME() {
            return COURSE_UP_TIME;
        }

        public void setCOURSE_UP_TIME(String COURSE_UP_TIME) {
            this.COURSE_UP_TIME = COURSE_UP_TIME;
        }

        public String getCOURSE_NAME() {
            return COURSE_NAME;
        }

        public void setCOURSE_NAME(String COURSE_NAME) {
            this.COURSE_NAME = COURSE_NAME;
        }

        public String getSMALL_PIC() {
            return SMALL_PIC;
        }

        public void setSMALL_PIC(String SMALL_PIC) {
            this.SMALL_PIC = SMALL_PIC;
        }

        public String getCOURSE_CLASS() {
            return COURSE_CLASS;
        }

        public void setCOURSE_CLASS(String COURSE_CLASS) {
            this.COURSE_CLASS = COURSE_CLASS;
        }

        public int getRN() {
            return RN;
        }

        public void setRN(int RN) {
            this.RN = RN;
        }
    }
}
