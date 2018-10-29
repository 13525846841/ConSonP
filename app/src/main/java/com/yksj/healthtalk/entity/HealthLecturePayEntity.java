package com.yksj.healthtalk.entity;

/**
 * Created by hekl on 18/7/20.
 */

public class HealthLecturePayEntity {
    /**
     * code : 1
     * message : 操作完成
     * result : {"EvaNum":0,"COURSE_OUT_PRICE":0.02,"SMALL_PIC":"/classroomFile/1506076410614.mp4","COURSE_DESC":"庙西街建筑群","COURSE_NAME":"你哪位","COURSE_UP_NAME":null,"BIG_PIC":null,"COURSE_IN_PRICE":0.01,"avgStar":0,"pay_id":"1170926290105226","BuyerNum":99,"course_order_id":"36","pay_status":10}
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
         * EvaNum : 0
         * COURSE_OUT_PRICE : 0.02
         * SMALL_PIC : /classroomFile/1506076410614.mp4
         * COURSE_DESC : 庙西街建筑群
         * COURSE_NAME : 你哪位
         * COURSE_UP_NAME : null
         * BIG_PIC : null
         * COURSE_IN_PRICE : 0.01
         * avgStar : 0
         * pay_id : 1170926290105226
         * BuyerNum : 99
         * course_order_id : 36
         * pay_status : 10
         */

        private int EvaNum;
        private double COURSE_OUT_PRICE;
        private String SMALL_PIC;
        private String COURSE_DESC;
        private String COURSE_NAME;
        private String COURSE_CLASS;
        private String COURSE_PAY;
        private String COURSE_UP_NAME;
        private String COURSE_UP_ID;
        private Object BIG_PIC;
        private double COURSE_IN_PRICE;
        private float avgStar;
        private String pay_id;
        private int BuyerNum;
        private String course_order_id;
        private int pay_status;

        public String getCOURSE_UP_ID() {
            return COURSE_UP_ID;
        }

        public void setCOURSE_UP_ID(String COURSE_UP_ID) {
            this.COURSE_UP_ID = COURSE_UP_ID;
        }

        public String getCOURSE_PAY() {
            return COURSE_PAY;
        }

        public void setCOURSE_PAY(String COURSE_PAY) {
            this.COURSE_PAY = COURSE_PAY;
        }

        public String getCOURSE_CLASS() {
            return COURSE_CLASS;
        }

        public void setCOURSE_CLASS(String COURSE_CLASS) {
            this.COURSE_CLASS = COURSE_CLASS;
        }

        public int getEvaNum() {
            return EvaNum;
        }

        public void setEvaNum(int EvaNum) {
            this.EvaNum = EvaNum;
        }

        public double getCOURSE_OUT_PRICE() {
            return COURSE_OUT_PRICE;
        }

        public void setCOURSE_OUT_PRICE(double COURSE_OUT_PRICE) {
            this.COURSE_OUT_PRICE = COURSE_OUT_PRICE;
        }

        public String getSMALL_PIC() {
            return SMALL_PIC;
        }

        public void setSMALL_PIC(String SMALL_PIC) {
            this.SMALL_PIC = SMALL_PIC;
        }

        public String getCOURSE_DESC() {
            return COURSE_DESC;
        }

        public void setCOURSE_DESC(String COURSE_DESC) {
            this.COURSE_DESC = COURSE_DESC;
        }

        public String getCOURSE_NAME() {
            return COURSE_NAME;
        }

        public void setCOURSE_NAME(String COURSE_NAME) {
            this.COURSE_NAME = COURSE_NAME;
        }

        public String getCOURSE_UP_NAME() {
            return COURSE_UP_NAME;
        }

        public void setCOURSE_UP_NAME(String COURSE_UP_NAME) {
            this.COURSE_UP_NAME = COURSE_UP_NAME;
        }

        public Object getBIG_PIC() {
            return BIG_PIC;
        }

        public void setBIG_PIC(Object BIG_PIC) {
            this.BIG_PIC = BIG_PIC;
        }

        public double getCOURSE_IN_PRICE() {
            return COURSE_IN_PRICE;
        }

        public void setCOURSE_IN_PRICE(double COURSE_IN_PRICE) {
            this.COURSE_IN_PRICE = COURSE_IN_PRICE;
        }

        public float getAvgStar() {
            return avgStar;
        }

        public void setAvgStar(float avgStar) {
            this.avgStar = avgStar;
        }

        public String getPay_id() {
            return pay_id;
        }

        public void setPay_id(String pay_id) {
            this.pay_id = pay_id;
        }

        public int getBuyerNum() {
            return BuyerNum;
        }

        public void setBuyerNum(int BuyerNum) {
            this.BuyerNum = BuyerNum;
        }

        public String getCourse_order_id() {
            return course_order_id;
        }

        public void setCourse_order_id(String course_order_id) {
            this.course_order_id = course_order_id;
        }

        public int getPay_status() {
            return pay_status;
        }

        public void setPay_status(int pay_status) {
            this.pay_status = pay_status;
        }
    }
}
