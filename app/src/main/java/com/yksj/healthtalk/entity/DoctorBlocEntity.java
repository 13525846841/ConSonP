package com.yksj.healthtalk.entity;

import java.util.List;

/**
 * Created by hekl on 18/7/4.
 */

public class DoctorBlocEntity {
    /**
     * code : 1
     * message : success
     * result : {"count":4,"list":[{"UNION_ID":420,"UNION_NAME":"北京联盟","BE_GOOD":"北京联盟擅长治疗各种疑难杂症....","UNION_DESC":"北京联盟介绍","BACKGROUND":"CusZiYuan/resources/union_background/beijing.png","VISIT_TIME":45,"CREATE_ID":null,"CREATE_TIME":null,"SITE_COUNT":0,"RN":1},{"UNION_ID":421,"UNION_NAME":"上海联盟","BE_GOOD":"上海联盟擅长治疗各种疑难杂症....","UNION_DESC":"上海联盟介绍.....","BACKGROUND":"CusZiYuan/resources/union_background/shanghai.png","VISIT_TIME":4,"CREATE_ID":null,"CREATE_TIME":null,"SITE_COUNT":0,"RN":2},{"UNION_ID":422,"UNION_NAME":"广州联盟","BE_GOOD":"广州联盟擅长治疗各种疑难杂症....","UNION_DESC":"广州联盟介绍.....","BACKGROUND":"CusZiYuan/resources/union_background/guangzhou.png","VISIT_TIME":4,"CREATE_ID":null,"CREATE_TIME":null,"SITE_COUNT":0,"RN":3},{"UNION_ID":423,"UNION_NAME":"深圳联盟","BE_GOOD":"深圳联盟擅长治疗各种疑难杂症....","UNION_DESC":"深圳联盟介绍.....","BACKGROUND":"CusZiYuan/resources/union_background/shenzhen.png","VISIT_TIME":6,"CREATE_ID":null,"CREATE_TIME":null,"SITE_COUNT":0,"RN":4}]}
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
         * list : [{"UNION_ID":420,"UNION_NAME":"北京联盟","BE_GOOD":"北京联盟擅长治疗各种疑难杂症....","UNION_DESC":"北京联盟介绍","BACKGROUND":"CusZiYuan/resources/union_background/beijing.png","VISIT_TIME":45,"CREATE_ID":null,"CREATE_TIME":null,"SITE_COUNT":0,"RN":1},{"UNION_ID":421,"UNION_NAME":"上海联盟","BE_GOOD":"上海联盟擅长治疗各种疑难杂症....","UNION_DESC":"上海联盟介绍.....","BACKGROUND":"CusZiYuan/resources/union_background/shanghai.png","VISIT_TIME":4,"CREATE_ID":null,"CREATE_TIME":null,"SITE_COUNT":0,"RN":2},{"UNION_ID":422,"UNION_NAME":"广州联盟","BE_GOOD":"广州联盟擅长治疗各种疑难杂症....","UNION_DESC":"广州联盟介绍.....","BACKGROUND":"CusZiYuan/resources/union_background/guangzhou.png","VISIT_TIME":4,"CREATE_ID":null,"CREATE_TIME":null,"SITE_COUNT":0,"RN":3},{"UNION_ID":423,"UNION_NAME":"深圳联盟","BE_GOOD":"深圳联盟擅长治疗各种疑难杂症....","UNION_DESC":"深圳联盟介绍.....","BACKGROUND":"CusZiYuan/resources/union_background/shenzhen.png","VISIT_TIME":6,"CREATE_ID":null,"CREATE_TIME":null,"SITE_COUNT":0,"RN":4}]
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
             * UNION_NAME : 北京联盟
             * BE_GOOD : 北京联盟擅长治疗各种疑难杂症....
             * UNION_DESC : 北京联盟介绍
             * BACKGROUND : CusZiYuan/resources/union_background/beijing.png
             * VISIT_TIME : 45
             * CREATE_ID : null
             * CREATE_TIME : null
             * SITE_COUNT : 0
             * RN : 1
             */

            private int UNION_ID;
            private String UNION_NAME;
            private String BE_GOOD;
            private String UNION_DESC;
            private String BACKGROUND;
            private int VISIT_TIME;
            private Object CREATE_ID;
            private Object CREATE_TIME;
            private int SITE_COUNT;
            private int RN;

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

            public int getSITE_COUNT() {
                return SITE_COUNT;
            }

            public void setSITE_COUNT(int SITE_COUNT) {
                this.SITE_COUNT = SITE_COUNT;
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
