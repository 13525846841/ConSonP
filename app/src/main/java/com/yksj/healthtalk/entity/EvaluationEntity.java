package com.yksj.healthtalk.entity;

import java.util.List;

/**
 * Created by hekl on 18/5/15.
 */

public class EvaluationEntity
{
    /**
     * code : 1
     * message : success
     * result : [{"EVALUATE_ID":512,"CONSULTATION_ID":15481,"DOCTOR_ID":3774,"CUSTOMER_ID":229490,"EVALUATE_LEVEL":4,"EVALUATE_TIME":"20170731085146","EVALUATE_CONTENT":"4.0","NOTE":"tuwen","EVALUATE_STATURS":0,"REPLY_CONTENT":null,"CHECK_STATUS":10,"CHECK_TIME":null,"CHECK_PERSON":null,"CHECK_NOTE":null,"SHOW_FLAG":1,"PRAISE_COUNT":0,"CUSTOMER_NAME":"小周","BIG_ICON_BACKGROUND":"/CusZiYuan/resources/229490/20170515141007.png","DOCTOR_NAME":"李安民","RN":1},{"EVALUATE_ID":445,"CONSULTATION_ID":281737,"DOCTOR_ID":3774,"CUSTOMER_ID":47324,"EVALUATE_LEVEL":1,"EVALUATE_TIME":"20170630105003","EVALUATE_CONTENT":"1","NOTE":"consultation","EVALUATE_STATURS":0,"REPLY_CONTENT":null,"CHECK_STATUS":10,"CHECK_TIME":null,"CHECK_PERSON":null,"CHECK_NOTE":null,"SHOW_FLAG":1,"PRAISE_COUNT":0,"CUSTOMER_NAME":"平川","BIG_ICON_BACKGROUND":"/CusZiYuan/resources/47324/20180419105215.png","DOCTOR_NAME":"李安民","RN":2},{"EVALUATE_ID":424,"CONSULTATION_ID":281737,"DOCTOR_ID":3774,"CUSTOMER_ID":47324,"EVALUATE_LEVEL":1,"EVALUATE_TIME":"20170630101346","EVALUATE_CONTENT":"1","NOTE":"consultation","EVALUATE_STATURS":0,"REPLY_CONTENT":null,"CHECK_STATUS":10,"CHECK_TIME":null,"CHECK_PERSON":null,"CHECK_NOTE":null,"SHOW_FLAG":1,"PRAISE_COUNT":0,"CUSTOMER_NAME":"平川","BIG_ICON_BACKGROUND":"/CusZiYuan/resources/47324/20180419105215.png","DOCTOR_NAME":"李安民","RN":3},{"EVALUATE_ID":421,"CONSULTATION_ID":281737,"DOCTOR_ID":3774,"CUSTOMER_ID":47324,"EVALUATE_LEVEL":1,"EVALUATE_TIME":"20170630101312","EVALUATE_CONTENT":"1","NOTE":"consultation","EVALUATE_STATURS":0,"REPLY_CONTENT":null,"CHECK_STATUS":10,"CHECK_TIME":null,"CHECK_PERSON":null,"CHECK_NOTE":null,"SHOW_FLAG":1,"PRAISE_COUNT":0,"CUSTOMER_NAME":"平川","BIG_ICON_BACKGROUND":"/CusZiYuan/resources/47324/20180419105215.png","DOCTOR_NAME":"李安民","RN":4},{"EVALUATE_ID":436,"CONSULTATION_ID":281737,"DOCTOR_ID":3774,"CUSTOMER_ID":47324,"EVALUATE_LEVEL":1,"EVALUATE_TIME":"20170630104049","EVALUATE_CONTENT":"1","NOTE":"consultation","EVALUATE_STATURS":0,"REPLY_CONTENT":null,"CHECK_STATUS":10,"CHECK_TIME":null,"CHECK_PERSON":null,"CHECK_NOTE":null,"SHOW_FLAG":1,"PRAISE_COUNT":0,"CUSTOMER_NAME":"平川","BIG_ICON_BACKGROUND":"/CusZiYuan/resources/47324/20180419105215.png","DOCTOR_NAME":"李安民","RN":5},{"EVALUATE_ID":430,"CONSULTATION_ID":281737,"DOCTOR_ID":3774,"CUSTOMER_ID":47324,"EVALUATE_LEVEL":1,"EVALUATE_TIME":"20170630103102","EVALUATE_CONTENT":"1","NOTE":"consultation","EVALUATE_STATURS":0,"REPLY_CONTENT":null,"CHECK_STATUS":10,"CHECK_TIME":null,"CHECK_PERSON":null,"CHECK_NOTE":null,"SHOW_FLAG":1,"PRAISE_COUNT":0,"CUSTOMER_NAME":"平川","BIG_ICON_BACKGROUND":"/CusZiYuan/resources/47324/20180419105215.png","DOCTOR_NAME":"李安民","RN":6},{"EVALUATE_ID":439,"CONSULTATION_ID":281737,"DOCTOR_ID":3774,"CUSTOMER_ID":47324,"EVALUATE_LEVEL":1,"EVALUATE_TIME":"20170630104051","EVALUATE_CONTENT":"1","NOTE":"consultation","EVALUATE_STATURS":0,"REPLY_CONTENT":null,"CHECK_STATUS":10,"CHECK_TIME":null,"CHECK_PERSON":null,"CHECK_NOTE":null,"SHOW_FLAG":1,"PRAISE_COUNT":0,"CUSTOMER_NAME":"平川","BIG_ICON_BACKGROUND":"/CusZiYuan/resources/47324/20180419105215.png","DOCTOR_NAME":"李安民","RN":7},{"EVALUATE_ID":442,"CONSULTATION_ID":281737,"DOCTOR_ID":3774,"CUSTOMER_ID":47324,"EVALUATE_LEVEL":1,"EVALUATE_TIME":"20170630104851","EVALUATE_CONTENT":"1","NOTE":"consultation","EVALUATE_STATURS":0,"REPLY_CONTENT":null,"CHECK_STATUS":10,"CHECK_TIME":null,"CHECK_PERSON":null,"CHECK_NOTE":null,"SHOW_FLAG":1,"PRAISE_COUNT":0,"CUSTOMER_NAME":"平川","BIG_ICON_BACKGROUND":"/CusZiYuan/resources/47324/20180419105215.png","DOCTOR_NAME":"李安民","RN":8},{"EVALUATE_ID":448,"CONSULTATION_ID":281737,"DOCTOR_ID":3774,"CUSTOMER_ID":47324,"EVALUATE_LEVEL":1,"EVALUATE_TIME":"20170630105008","EVALUATE_CONTENT":"1","NOTE":"consultation","EVALUATE_STATURS":0,"REPLY_CONTENT":null,"CHECK_STATUS":10,"CHECK_TIME":null,"CHECK_PERSON":null,"CHECK_NOTE":null,"SHOW_FLAG":1,"PRAISE_COUNT":0,"CUSTOMER_NAME":"平川","BIG_ICON_BACKGROUND":"/CusZiYuan/resources/47324/20180419105215.png","DOCTOR_NAME":"李安民","RN":9},{"EVALUATE_ID":433,"CONSULTATION_ID":281737,"DOCTOR_ID":3774,"CUSTOMER_ID":47324,"EVALUATE_LEVEL":1,"EVALUATE_TIME":"20170630103613","EVALUATE_CONTENT":"1","NOTE":"consultation","EVALUATE_STATURS":0,"REPLY_CONTENT":null,"CHECK_STATUS":10,"CHECK_TIME":null,"CHECK_PERSON":null,"CHECK_NOTE":null,"SHOW_FLAG":1,"PRAISE_COUNT":0,"CUSTOMER_NAME":"平川","BIG_ICON_BACKGROUND":"/CusZiYuan/resources/47324/20180419105215.png","DOCTOR_NAME":"李安民","RN":10}]
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
         * EVALUATE_ID : 512
         * CONSULTATION_ID : 15481
         * DOCTOR_ID : 3774
         * CUSTOMER_ID : 229490
         * EVALUATE_LEVEL : 4
         * EVALUATE_TIME : 20170731085146
         * EVALUATE_CONTENT : 4.0
         * NOTE : tuwen
         * EVALUATE_STATURS : 0
         * REPLY_CONTENT : null
         * CHECK_STATUS : 10
         * CHECK_TIME : null
         * CHECK_PERSON : null
         * CHECK_NOTE : null
         * SHOW_FLAG : 1
         * PRAISE_COUNT : 0
         * CUSTOMER_NAME : 小周
         * BIG_ICON_BACKGROUND : /CusZiYuan/resources/229490/20170515141007.png
         * DOCTOR_NAME : 李安民
         * RN : 1
         */

        private int EVALUATE_ID;
        private int CONSULTATION_ID;
        private int DOCTOR_ID;
        private int CUSTOMER_ID;
        private int EVALUATE_LEVEL;
        private String EVALUATE_TIME;
        private String EVALUATE_CONTENT;
        private String NOTE;
        private int EVALUATE_STATURS;
        private Object REPLY_CONTENT;
        private int CHECK_STATUS;
        private Object CHECK_TIME;
        private Object CHECK_PERSON;
        private Object CHECK_NOTE;
        private int SHOW_FLAG;
        private int PRAISE_COUNT;
        private String CUSTOMER_NAME;
        private String BIG_ICON_BACKGROUND;
        private String DOCTOR_NAME;
        private int RN;

        public int getEVALUATE_ID() {
            return EVALUATE_ID;
        }

        public void setEVALUATE_ID(int EVALUATE_ID) {
            this.EVALUATE_ID = EVALUATE_ID;
        }

        public int getCONSULTATION_ID() {
            return CONSULTATION_ID;
        }

        public void setCONSULTATION_ID(int CONSULTATION_ID) {
            this.CONSULTATION_ID = CONSULTATION_ID;
        }

        public int getDOCTOR_ID() {
            return DOCTOR_ID;
        }

        public void setDOCTOR_ID(int DOCTOR_ID) {
            this.DOCTOR_ID = DOCTOR_ID;
        }

        public int getCUSTOMER_ID() {
            return CUSTOMER_ID;
        }

        public void setCUSTOMER_ID(int CUSTOMER_ID) {
            this.CUSTOMER_ID = CUSTOMER_ID;
        }

        public int getEVALUATE_LEVEL() {
            return EVALUATE_LEVEL;
        }

        public void setEVALUATE_LEVEL(int EVALUATE_LEVEL) {
            this.EVALUATE_LEVEL = EVALUATE_LEVEL;
        }

        public String getEVALUATE_TIME() {
            return EVALUATE_TIME;
        }

        public void setEVALUATE_TIME(String EVALUATE_TIME) {
            this.EVALUATE_TIME = EVALUATE_TIME;
        }

        public String getEVALUATE_CONTENT() {
            return EVALUATE_CONTENT;
        }

        public void setEVALUATE_CONTENT(String EVALUATE_CONTENT) {
            this.EVALUATE_CONTENT = EVALUATE_CONTENT;
        }

        public String getNOTE() {
            return NOTE;
        }

        public void setNOTE(String NOTE) {
            this.NOTE = NOTE;
        }

        public int getEVALUATE_STATURS() {
            return EVALUATE_STATURS;
        }

        public void setEVALUATE_STATURS(int EVALUATE_STATURS) {
            this.EVALUATE_STATURS = EVALUATE_STATURS;
        }

        public Object getREPLY_CONTENT() {
            return REPLY_CONTENT;
        }

        public void setREPLY_CONTENT(Object REPLY_CONTENT) {
            this.REPLY_CONTENT = REPLY_CONTENT;
        }

        public int getCHECK_STATUS() {
            return CHECK_STATUS;
        }

        public void setCHECK_STATUS(int CHECK_STATUS) {
            this.CHECK_STATUS = CHECK_STATUS;
        }

        public Object getCHECK_TIME() {
            return CHECK_TIME;
        }

        public void setCHECK_TIME(Object CHECK_TIME) {
            this.CHECK_TIME = CHECK_TIME;
        }

        public Object getCHECK_PERSON() {
            return CHECK_PERSON;
        }

        public void setCHECK_PERSON(Object CHECK_PERSON) {
            this.CHECK_PERSON = CHECK_PERSON;
        }

        public Object getCHECK_NOTE() {
            return CHECK_NOTE;
        }

        public void setCHECK_NOTE(Object CHECK_NOTE) {
            this.CHECK_NOTE = CHECK_NOTE;
        }

        public int getSHOW_FLAG() {
            return SHOW_FLAG;
        }

        public void setSHOW_FLAG(int SHOW_FLAG) {
            this.SHOW_FLAG = SHOW_FLAG;
        }

        public int getPRAISE_COUNT() {
            return PRAISE_COUNT;
        }

        public void setPRAISE_COUNT(int PRAISE_COUNT) {
            this.PRAISE_COUNT = PRAISE_COUNT;
        }

        public String getCUSTOMER_NAME() {
            return CUSTOMER_NAME;
        }

        public void setCUSTOMER_NAME(String CUSTOMER_NAME) {
            this.CUSTOMER_NAME = CUSTOMER_NAME;
        }

        public String getBIG_ICON_BACKGROUND() {
            return BIG_ICON_BACKGROUND;
        }

        public void setBIG_ICON_BACKGROUND(String BIG_ICON_BACKGROUND) {
            this.BIG_ICON_BACKGROUND = BIG_ICON_BACKGROUND;
        }

        public String getDOCTOR_NAME() {
            return DOCTOR_NAME;
        }

        public void setDOCTOR_NAME(String DOCTOR_NAME) {
            this.DOCTOR_NAME = DOCTOR_NAME;
        }

        public int getRN() {
            return RN;
        }

        public void setRN(int RN) {
            this.RN = RN;
        }
    }
}
