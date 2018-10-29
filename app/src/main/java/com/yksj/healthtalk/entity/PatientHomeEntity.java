package com.yksj.healthtalk.entity;

import java.util.List;

/**
 * Created by hekl on 18/10/26.
 */

public class PatientHomeEntity {
    /**
     * message : 查询成功
     * AllNews : [{"CONSULTATION_CENTER_ID":6,"INFO_ID":464,"INFO_NAME":"宝宝如果出现这些症状，可能是营养不良了！","INFO_CLASS_ID":1010202,"SHARE_COUNT":0,"COMMENT_COUNT":0,"FORWARD_COUNT":0,"PRAISE_COUNT":0,"PUBLISH_TIME":"20170225113357","USED_FLAG":1,"NOTE":null,"INFO_PICTURE":"/DynamicMsgPic/Center_6/100587.jpg","INFO_STATUS":20,"STATUS_TIME":"20170225113357","CUSTOMER_ID":null,"VISIBLE_FLAG":10,"CLIENT_TYPE":60,"RECOMMEND_FLAG":0,"ARTICLE_AUTHOR":null,"ROWNO":1},{"CONSULTATION_CENTER_ID":6,"INFO_ID":465,"INFO_NAME":"对小儿弱智有益的十大食物","INFO_CLASS_ID":1010202,"SHARE_COUNT":0,"COMMENT_COUNT":0,"FORWARD_COUNT":0,"PRAISE_COUNT":0,"PUBLISH_TIME":"20170225113357","USED_FLAG":1,"NOTE":null,"INFO_PICTURE":"/DynamicMsgPic/Center_6/100587.jpg","INFO_STATUS":20,"STATUS_TIME":"20170225113357","CUSTOMER_ID":null,"VISIBLE_FLAG":10,"CLIENT_TYPE":60,"RECOMMEND_FLAG":0,"ARTICLE_AUTHOR":null,"ROWNO":2},{"CONSULTATION_CENTER_ID":6,"INFO_ID":466,"INFO_NAME":"矮小宝宝的营养饮食原则","INFO_CLASS_ID":1010202,"SHARE_COUNT":0,"COMMENT_COUNT":0,"FORWARD_COUNT":0,"PRAISE_COUNT":0,"PUBLISH_TIME":"20170225113357","USED_FLAG":1,"NOTE":null,"INFO_PICTURE":"/DynamicMsgPic/Center_6/100587.jpg","INFO_STATUS":20,"STATUS_TIME":"20170225113357","CUSTOMER_ID":null,"VISIBLE_FLAG":10,"CLIENT_TYPE":60,"RECOMMEND_FLAG":0,"ARTICLE_AUTHOR":null,"ROWNO":3}]
     * code : 1
     * SowingList : [{"SowingMap1":"","url":""}]
     */

    private String message;
    private int code;
    private List<AllNewsBean> AllNews;
    private List<SowingListBean> SowingList;

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

    public List<AllNewsBean> getAllNews() {
        return AllNews;
    }

    public void setAllNews(List<AllNewsBean> AllNews) {
        this.AllNews = AllNews;
    }

    public List<SowingListBean> getSowingList() {
        return SowingList;
    }

    public void setSowingList(List<SowingListBean> SowingList) {
        this.SowingList = SowingList;
    }

    public static class AllNewsBean {
        /**
         * CONSULTATION_CENTER_ID : 6
         * INFO_ID : 464
         * INFO_NAME : 宝宝如果出现这些症状，可能是营养不良了！
         * INFO_CLASS_ID : 1010202
         * SHARE_COUNT : 0
         * COMMENT_COUNT : 0
         * FORWARD_COUNT : 0
         * PRAISE_COUNT : 0
         * PUBLISH_TIME : 20170225113357
         * USED_FLAG : 1
         * NOTE : null
         * INFO_PICTURE : /DynamicMsgPic/Center_6/100587.jpg
         * INFO_STATUS : 20
         * STATUS_TIME : 20170225113357
         * CUSTOMER_ID : null
         * VISIBLE_FLAG : 10
         * CLIENT_TYPE : 60
         * RECOMMEND_FLAG : 0
         * ARTICLE_AUTHOR : null
         * ROWNO : 1
         */

        private int CONSULTATION_CENTER_ID;
        private int INFO_ID;
        private String INFO_NAME;
        private int INFO_CLASS_ID;
        private int SHARE_COUNT;
        private int COMMENT_COUNT;
        private int FORWARD_COUNT;
        private int PRAISE_COUNT;
        private String PUBLISH_TIME;
        private int USED_FLAG;
        private Object NOTE;
        private String INFO_PICTURE;
        private int INFO_STATUS;
        private String STATUS_TIME;
        private Object CUSTOMER_ID;
        private int VISIBLE_FLAG;
        private int CLIENT_TYPE;
        private int RECOMMEND_FLAG;
        private Object ARTICLE_AUTHOR;
        private int ROWNO;

        public int getCONSULTATION_CENTER_ID() {
            return CONSULTATION_CENTER_ID;
        }

        public void setCONSULTATION_CENTER_ID(int CONSULTATION_CENTER_ID) {
            this.CONSULTATION_CENTER_ID = CONSULTATION_CENTER_ID;
        }

        public int getINFO_ID() {
            return INFO_ID;
        }

        public void setINFO_ID(int INFO_ID) {
            this.INFO_ID = INFO_ID;
        }

        public String getINFO_NAME() {
            return INFO_NAME;
        }

        public void setINFO_NAME(String INFO_NAME) {
            this.INFO_NAME = INFO_NAME;
        }

        public int getINFO_CLASS_ID() {
            return INFO_CLASS_ID;
        }

        public void setINFO_CLASS_ID(int INFO_CLASS_ID) {
            this.INFO_CLASS_ID = INFO_CLASS_ID;
        }

        public int getSHARE_COUNT() {
            return SHARE_COUNT;
        }

        public void setSHARE_COUNT(int SHARE_COUNT) {
            this.SHARE_COUNT = SHARE_COUNT;
        }

        public int getCOMMENT_COUNT() {
            return COMMENT_COUNT;
        }

        public void setCOMMENT_COUNT(int COMMENT_COUNT) {
            this.COMMENT_COUNT = COMMENT_COUNT;
        }

        public int getFORWARD_COUNT() {
            return FORWARD_COUNT;
        }

        public void setFORWARD_COUNT(int FORWARD_COUNT) {
            this.FORWARD_COUNT = FORWARD_COUNT;
        }

        public int getPRAISE_COUNT() {
            return PRAISE_COUNT;
        }

        public void setPRAISE_COUNT(int PRAISE_COUNT) {
            this.PRAISE_COUNT = PRAISE_COUNT;
        }

        public String getPUBLISH_TIME() {
            return PUBLISH_TIME;
        }

        public void setPUBLISH_TIME(String PUBLISH_TIME) {
            this.PUBLISH_TIME = PUBLISH_TIME;
        }

        public int getUSED_FLAG() {
            return USED_FLAG;
        }

        public void setUSED_FLAG(int USED_FLAG) {
            this.USED_FLAG = USED_FLAG;
        }

        public Object getNOTE() {
            return NOTE;
        }

        public void setNOTE(Object NOTE) {
            this.NOTE = NOTE;
        }

        public String getINFO_PICTURE() {
            return INFO_PICTURE;
        }

        public void setINFO_PICTURE(String INFO_PICTURE) {
            this.INFO_PICTURE = INFO_PICTURE;
        }

        public int getINFO_STATUS() {
            return INFO_STATUS;
        }

        public void setINFO_STATUS(int INFO_STATUS) {
            this.INFO_STATUS = INFO_STATUS;
        }

        public String getSTATUS_TIME() {
            return STATUS_TIME;
        }

        public void setSTATUS_TIME(String STATUS_TIME) {
            this.STATUS_TIME = STATUS_TIME;
        }

        public Object getCUSTOMER_ID() {
            return CUSTOMER_ID;
        }

        public void setCUSTOMER_ID(Object CUSTOMER_ID) {
            this.CUSTOMER_ID = CUSTOMER_ID;
        }

        public int getVISIBLE_FLAG() {
            return VISIBLE_FLAG;
        }

        public void setVISIBLE_FLAG(int VISIBLE_FLAG) {
            this.VISIBLE_FLAG = VISIBLE_FLAG;
        }

        public int getCLIENT_TYPE() {
            return CLIENT_TYPE;
        }

        public void setCLIENT_TYPE(int CLIENT_TYPE) {
            this.CLIENT_TYPE = CLIENT_TYPE;
        }

        public int getRECOMMEND_FLAG() {
            return RECOMMEND_FLAG;
        }

        public void setRECOMMEND_FLAG(int RECOMMEND_FLAG) {
            this.RECOMMEND_FLAG = RECOMMEND_FLAG;
        }

        public Object getARTICLE_AUTHOR() {
            return ARTICLE_AUTHOR;
        }

        public void setARTICLE_AUTHOR(Object ARTICLE_AUTHOR) {
            this.ARTICLE_AUTHOR = ARTICLE_AUTHOR;
        }

        public int getROWNO() {
            return ROWNO;
        }

        public void setROWNO(int ROWNO) {
            this.ROWNO = ROWNO;
        }
    }

    public static class SowingListBean {
        /**
         * SowingMap1 :
         * url :
         */

        private String SowingMap;
        private String url;

        public String getSowingMap() {
            return SowingMap;
        }

        public void setSowingMap(String SowingMap) {
            this.SowingMap = SowingMap;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
