package com.yksj.healthtalk.entity;

import java.util.List;

/**
 * Created by hekl on 18/7/4.
 */

public class BlocDaShiJEntity {
    /**
     * code : 1
     * message : success
     * result : {"count":4,"list":[{"UNION_ID":420,"EVENT_ID":4,"EVENT_TITLE":"劳动节","EVENT_CONTENT":null,"EVENT_IMAGE":null,"EVENT_TIME":"20180501000000","RECORD_TIME":null,"RN":1},{"UNION_ID":420,"EVENT_ID":3,"EVENT_TITLE":"妇女节","EVENT_CONTENT":null,"EVENT_IMAGE":null,"EVENT_TIME":"20180308000000","RECORD_TIME":null,"RN":2},{"UNION_ID":420,"EVENT_ID":2,"EVENT_TITLE":"情人节","EVENT_CONTENT":null,"EVENT_IMAGE":null,"EVENT_TIME":"20180214000000","RECORD_TIME":null,"RN":3},{"UNION_ID":420,"EVENT_ID":1,"EVENT_TITLE":"元旦","EVENT_CONTENT":null,"EVENT_IMAGE":null,"EVENT_TIME":"20180101000000","RECORD_TIME":null,"RN":4}]}
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
         * count : 4
         * list : [{"UNION_ID":420,"EVENT_ID":4,"EVENT_TITLE":"劳动节","EVENT_CONTENT":null,"EVENT_IMAGE":null,"EVENT_TIME":"20180501000000","RECORD_TIME":null,"RN":1},{"UNION_ID":420,"EVENT_ID":3,"EVENT_TITLE":"妇女节","EVENT_CONTENT":null,"EVENT_IMAGE":null,"EVENT_TIME":"20180308000000","RECORD_TIME":null,"RN":2},{"UNION_ID":420,"EVENT_ID":2,"EVENT_TITLE":"情人节","EVENT_CONTENT":null,"EVENT_IMAGE":null,"EVENT_TIME":"20180214000000","RECORD_TIME":null,"RN":3},{"UNION_ID":420,"EVENT_ID":1,"EVENT_TITLE":"元旦","EVENT_CONTENT":null,"EVENT_IMAGE":null,"EVENT_TIME":"20180101000000","RECORD_TIME":null,"RN":4}]
         */

        private int count;
        private List<ListBean> list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * UNION_ID : 420
             * EVENT_ID : 4
             * EVENT_TITLE : 劳动节
             * EVENT_CONTENT : null
             * EVENT_IMAGE : null
             * EVENT_TIME : 20180501000000
             * RECORD_TIME : null
             * RN : 1
             */

            private int UNION_ID;
            private int EVENT_ID;
            private String EVENT_TITLE;
            private Object EVENT_CONTENT;
            private Object EVENT_IMAGE;
            private String EVENT_TIME;
            private Object RECORD_TIME;
            private int RN;

            public int getUNION_ID() {
                return UNION_ID;
            }

            public void setUNION_ID(int UNION_ID) {
                this.UNION_ID = UNION_ID;
            }

            public int getEVENT_ID() {
                return EVENT_ID;
            }

            public void setEVENT_ID(int EVENT_ID) {
                this.EVENT_ID = EVENT_ID;
            }

            public String getEVENT_TITLE() {
                return EVENT_TITLE;
            }

            public void setEVENT_TITLE(String EVENT_TITLE) {
                this.EVENT_TITLE = EVENT_TITLE;
            }

            public Object getEVENT_CONTENT() {
                return EVENT_CONTENT;
            }

            public void setEVENT_CONTENT(Object EVENT_CONTENT) {
                this.EVENT_CONTENT = EVENT_CONTENT;
            }

            public Object getEVENT_IMAGE() {
                return EVENT_IMAGE;
            }

            public void setEVENT_IMAGE(Object EVENT_IMAGE) {
                this.EVENT_IMAGE = EVENT_IMAGE;
            }

            public String getEVENT_TIME() {
                return EVENT_TIME;
            }

            public void setEVENT_TIME(String EVENT_TIME) {
                this.EVENT_TIME = EVENT_TIME;
            }

            public Object getRECORD_TIME() {
                return RECORD_TIME;
            }

            public void setRECORD_TIME(Object RECORD_TIME) {
                this.RECORD_TIME = RECORD_TIME;
            }

            public int getRN() {
                return RN;
            }

            public void setRN(int RN) {
                this.RN = RN;
            }
        }
    }
}
