package com.yksj.healthtalk.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by hekl on 18/7/17.
 */

public class HealthLectureWorksEntity {
    /**
     * code : 1
     * message : 操作完成
     * result : [{"COURSE_ID":"131","SITE_ID":"389","COURSE_UP_ID":"3774","COURSE_UP_TIME":"20180720092600","COURSE_NAME":"ç\u0089¹ä¹\u0090å\u0095\u008aå\u0098\u009eå\u0098\u009eé¥¿äº\u0086","COURSE_DESC":"allç\u009a\u0084å\u0092³å\u0092³å\u0092³é¥¿æ\u0080¥äº\u0086","SMALL_PIC":"''","BIG_PIC":null,"COURSE_IN_PRICE":0,"COURSE_OUT_PRICE":0,"COURSE_CLASS":"20","COURSE_ADDRESS":"/classroomFile/1532049960042.jpeg","COURSE_LIST_TYPE":"20","COURSE_TIME1":null,"COURSE_TIME2":null,"COURSE_IN_LIST":"0","COURSE_OUT_LIST":"1","COURSE_STATUS":10,"COURSE_STATUS_TM":null,"COURSE_STATUS_PERSON":null,"NOTE":null,"SMALL_COURSE_ADDRESS":"''","COURSE_CATEGORY":null,"VISIT_TIME":0,"COURSE_UP_NAME":null,"COURSE_PAY":"0","COURSE_ISPAY":"0","COURSE_SCORE":5,"CUSTOMER_ID":null,"COURSE_COMMENTS":null,"RN":1},{"COURSE_ID":"125","SITE_ID":"389","COURSE_UP_ID":"3774","COURSE_UP_TIME":"20180720085848","COURSE_NAME":"ç\u0089¹ä¹\u0090å\u0095\u008aå\u0098\u009eå\u0098\u009eé¥¿äº\u0086","COURSE_DESC":"allç\u009a\u0084å\u0092³å\u0092³å\u0092³é¥¿æ\u0080¥äº\u0086","SMALL_PIC":"''","BIG_PIC":null,"COURSE_IN_PRICE":1,"COURSE_OUT_PRICE":1,"COURSE_CLASS":"20","COURSE_ADDRESS":"/classroomFile/1532048328160.jpeg","COURSE_LIST_TYPE":"20","COURSE_TIME1":null,"COURSE_TIME2":null,"COURSE_IN_LIST":"0","COURSE_OUT_LIST":"1","COURSE_STATUS":10,"COURSE_STATUS_TM":null,"COURSE_STATUS_PERSON":null,"NOTE":null,"SMALL_COURSE_ADDRESS":"''","COURSE_CATEGORY":null,"VISIT_TIME":0,"COURSE_UP_NAME":null,"COURSE_PAY":"0","COURSE_ISPAY":"0","COURSE_SCORE":5,"CUSTOMER_ID":null,"COURSE_COMMENTS":null,"RN":2},{"COURSE_ID":"117","SITE_ID":"366","COURSE_UP_ID":"3774","COURSE_UP_TIME":"20180712090943","COURSE_NAME":"test","COURSE_DESC":"999","SMALL_PIC":"''","BIG_PIC":null,"COURSE_IN_PRICE":999,"COURSE_OUT_PRICE":999,"COURSE_CLASS":"20","COURSE_ADDRESS":"/classroomFile/1531357783197.jpg","COURSE_LIST_TYPE":"10","COURSE_TIME1":null,"COURSE_TIME2":null,"COURSE_IN_LIST":"1","COURSE_OUT_LIST":"0","COURSE_STATUS":10,"COURSE_STATUS_TM":null,"COURSE_STATUS_PERSON":null,"NOTE":"gas的","SMALL_COURSE_ADDRESS":"''","COURSE_CATEGORY":"2","VISIT_TIME":0,"COURSE_UP_NAME":"李晓铃","COURSE_PAY":"0","COURSE_ISPAY":"0","COURSE_SCORE":5,"CUSTOMER_ID":null,"COURSE_COMMENTS":null,"RN":3},{"COURSE_ID":"116","SITE_ID":"366","COURSE_UP_ID":"3774","COURSE_UP_TIME":"20180712090623","COURSE_NAME":"test","COURSE_DESC":"555","SMALL_PIC":"''","BIG_PIC":null,"COURSE_IN_PRICE":555,"COURSE_OUT_PRICE":555,"COURSE_CLASS":"20","COURSE_ADDRESS":"/classroomFile/1531357583126.png","COURSE_LIST_TYPE":"10","COURSE_TIME1":null,"COURSE_TIME2":null,"COURSE_IN_LIST":"1","COURSE_OUT_LIST":"0","COURSE_STATUS":10,"COURSE_STATUS_TM":null,"COURSE_STATUS_PERSON":null,"NOTE":null,"SMALL_COURSE_ADDRESS":"''","COURSE_CATEGORY":"1","VISIT_TIME":0,"COURSE_UP_NAME":"李晓铃","COURSE_PAY":"0","COURSE_ISPAY":"0","COURSE_SCORE":5,"CUSTOMER_ID":null,"COURSE_COMMENTS":null,"RN":4},{"COURSE_ID":"115","SITE_ID":"366","COURSE_UP_ID":"3774","COURSE_UP_TIME":"20180712090433","COURSE_NAME":"test","COURSE_DESC":"222","SMALL_PIC":"''","BIG_PIC":null,"COURSE_IN_PRICE":222,"COURSE_OUT_PRICE":222,"COURSE_CLASS":"20","COURSE_ADDRESS":"/classroomFile/1531357473370.jpeg","COURSE_LIST_TYPE":"10","COURSE_TIME1":null,"COURSE_TIME2":null,"COURSE_IN_LIST":"1","COURSE_OUT_LIST":"0","COURSE_STATUS":10,"COURSE_STATUS_TM":null,"COURSE_STATUS_PERSON":null,"NOTE":"送到","SMALL_COURSE_ADDRESS":"''","COURSE_CATEGORY":null,"VISIT_TIME":0,"COURSE_UP_NAME":"李庆怀","COURSE_PAY":"0","COURSE_ISPAY":"0","COURSE_SCORE":5,"CUSTOMER_ID":null,"COURSE_COMMENTS":null,"RN":5}]
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

    public static class ResultBean implements Parcelable {
        /**
         * COURSE_ID : 131
         * SITE_ID : 389
         * COURSE_UP_ID : 3774
         * COURSE_UP_TIME : 20180720092600
         * COURSE_NAME : ç¹ä¹åååé¥¿äº
         * COURSE_DESC : allçå³å³å³é¥¿æ¥äº
         * SMALL_PIC : ''
         * BIG_PIC : null
         * COURSE_IN_PRICE : 0
         * COURSE_OUT_PRICE : 0
         * COURSE_CLASS : 20
         * COURSE_ADDRESS : /classroomFile/1532049960042.jpeg
         * COURSE_LIST_TYPE : 20
         * COURSE_TIME1 : null
         * COURSE_TIME2 : null
         * COURSE_IN_LIST : 0
         * COURSE_OUT_LIST : 1
         * COURSE_STATUS : 10
         * COURSE_STATUS_TM : null
         * COURSE_STATUS_PERSON : null
         * NOTE : null
         * SMALL_COURSE_ADDRESS : ''
         * COURSE_CATEGORY : null
         * VISIT_TIME : 0
         * COURSE_UP_NAME : null
         * COURSE_PAY : 0
         * COURSE_ISPAY : 0
         * COURSE_SCORE : 5
         * CUSTOMER_ID : null
         * COURSE_COMMENTS : null
         * RN : 1
         */

        private String COURSE_ID;
        private String SITE_ID;
        private String COURSE_UP_ID;
        private String COURSE_UP_TIME;
        private String COURSE_NAME;
        private String COURSE_DESC;
        private String SMALL_PIC;
        private Object BIG_PIC;
        private float COURSE_IN_PRICE;
        private float COURSE_OUT_PRICE;
        private String COURSE_CLASS;
        private String COURSE_ADDRESS;
        private String COURSE_LIST_TYPE;
        private Object COURSE_TIME1;
        private Object COURSE_TIME2;
        private String COURSE_IN_LIST;
        private String COURSE_OUT_LIST;
        private int COURSE_STATUS;
        private Object COURSE_STATUS_TM;
        private Object COURSE_STATUS_PERSON;
        private Object NOTE;
        private String SMALL_COURSE_ADDRESS;
        private Object COURSE_CATEGORY;
        private int VISIT_TIME;
        private String COURSE_UP_NAME;
        private String COURSE_PAY;
        private String COURSE_ISPAY;
        private int COURSE_SCORE;
        private Object CUSTOMER_ID;
        private Object COURSE_COMMENTS;
        private int RN;

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

        public String getCOURSE_DESC() {
            return COURSE_DESC;
        }

        public void setCOURSE_DESC(String COURSE_DESC) {
            this.COURSE_DESC = COURSE_DESC;
        }

        public String getSMALL_PIC() {
            return SMALL_PIC;
        }

        public void setSMALL_PIC(String SMALL_PIC) {
            this.SMALL_PIC = SMALL_PIC;
        }

        public Object getBIG_PIC() {
            return BIG_PIC;
        }

        public void setBIG_PIC(Object BIG_PIC) {
            this.BIG_PIC = BIG_PIC;
        }

        public float getCOURSE_IN_PRICE() {
            return COURSE_IN_PRICE;
        }

        public void setCOURSE_IN_PRICE(float COURSE_IN_PRICE) {
            this.COURSE_IN_PRICE = COURSE_IN_PRICE;
        }

        public float getCOURSE_OUT_PRICE() {
            return COURSE_OUT_PRICE;
        }

        public void setCOURSE_OUT_PRICE(float COURSE_OUT_PRICE) {
            this.COURSE_OUT_PRICE = COURSE_OUT_PRICE;
        }

        public String getCOURSE_CLASS() {
            return COURSE_CLASS;
        }

        public void setCOURSE_CLASS(String COURSE_CLASS) {
            this.COURSE_CLASS = COURSE_CLASS;
        }

        public String getCOURSE_ADDRESS() {
            return COURSE_ADDRESS;
        }

        public void setCOURSE_ADDRESS(String COURSE_ADDRESS) {
            this.COURSE_ADDRESS = COURSE_ADDRESS;
        }

        public String getCOURSE_LIST_TYPE() {
            return COURSE_LIST_TYPE;
        }

        public void setCOURSE_LIST_TYPE(String COURSE_LIST_TYPE) {
            this.COURSE_LIST_TYPE = COURSE_LIST_TYPE;
        }

        public Object getCOURSE_TIME1() {
            return COURSE_TIME1;
        }

        public void setCOURSE_TIME1(Object COURSE_TIME1) {
            this.COURSE_TIME1 = COURSE_TIME1;
        }

        public Object getCOURSE_TIME2() {
            return COURSE_TIME2;
        }

        public void setCOURSE_TIME2(Object COURSE_TIME2) {
            this.COURSE_TIME2 = COURSE_TIME2;
        }

        public String getCOURSE_IN_LIST() {
            return COURSE_IN_LIST;
        }

        public void setCOURSE_IN_LIST(String COURSE_IN_LIST) {
            this.COURSE_IN_LIST = COURSE_IN_LIST;
        }

        public String getCOURSE_OUT_LIST() {
            return COURSE_OUT_LIST;
        }

        public void setCOURSE_OUT_LIST(String COURSE_OUT_LIST) {
            this.COURSE_OUT_LIST = COURSE_OUT_LIST;
        }

        public int getCOURSE_STATUS() {
            return COURSE_STATUS;
        }

        public void setCOURSE_STATUS(int COURSE_STATUS) {
            this.COURSE_STATUS = COURSE_STATUS;
        }

        public Object getCOURSE_STATUS_TM() {
            return COURSE_STATUS_TM;
        }

        public void setCOURSE_STATUS_TM(Object COURSE_STATUS_TM) {
            this.COURSE_STATUS_TM = COURSE_STATUS_TM;
        }

        public Object getCOURSE_STATUS_PERSON() {
            return COURSE_STATUS_PERSON;
        }

        public void setCOURSE_STATUS_PERSON(Object COURSE_STATUS_PERSON) {
            this.COURSE_STATUS_PERSON = COURSE_STATUS_PERSON;
        }

        public Object getNOTE() {
            return NOTE;
        }

        public void setNOTE(Object NOTE) {
            this.NOTE = NOTE;
        }

        public String getSMALL_COURSE_ADDRESS() {
            return SMALL_COURSE_ADDRESS;
        }

        public void setSMALL_COURSE_ADDRESS(String SMALL_COURSE_ADDRESS) {
            this.SMALL_COURSE_ADDRESS = SMALL_COURSE_ADDRESS;
        }

        public Object getCOURSE_CATEGORY() {
            return COURSE_CATEGORY;
        }

        public void setCOURSE_CATEGORY(Object COURSE_CATEGORY) {
            this.COURSE_CATEGORY = COURSE_CATEGORY;
        }

        public int getVISIT_TIME() {
            return VISIT_TIME;
        }

        public void setVISIT_TIME(int VISIT_TIME) {
            this.VISIT_TIME = VISIT_TIME;
        }

        public String getCOURSE_UP_NAME() {
            return COURSE_UP_NAME;
        }

        public void setCOURSE_UP_NAME(String COURSE_UP_NAME) {
            this.COURSE_UP_NAME = COURSE_UP_NAME;
        }

        public String getCOURSE_PAY() {
            return COURSE_PAY;
        }

        public void setCOURSE_PAY(String COURSE_PAY) {
            this.COURSE_PAY = COURSE_PAY;
        }

        public String getCOURSE_ISPAY() {
            return COURSE_ISPAY;
        }

        public void setCOURSE_ISPAY(String COURSE_ISPAY) {
            this.COURSE_ISPAY = COURSE_ISPAY;
        }

        public int getCOURSE_SCORE() {
            return COURSE_SCORE;
        }

        public void setCOURSE_SCORE(int COURSE_SCORE) {
            this.COURSE_SCORE = COURSE_SCORE;
        }

        public Object getCUSTOMER_ID() {
            return CUSTOMER_ID;
        }

        public void setCUSTOMER_ID(Object CUSTOMER_ID) {
            this.CUSTOMER_ID = CUSTOMER_ID;
        }

        public Object getCOURSE_COMMENTS() {
            return COURSE_COMMENTS;
        }

        public void setCOURSE_COMMENTS(Object COURSE_COMMENTS) {
            this.COURSE_COMMENTS = COURSE_COMMENTS;
        }

        public int getRN() {
            return RN;
        }

        public void setRN(int RN) {
            this.RN = RN;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.COURSE_ID);
            dest.writeString(this.SITE_ID);
            dest.writeString(this.COURSE_UP_ID);
            dest.writeString(this.COURSE_UP_TIME);
            dest.writeString(this.COURSE_NAME);
            dest.writeString(this.COURSE_DESC);
            dest.writeString(this.SMALL_PIC);
//            dest.writeParcelable(this.BIG_PIC, flags);
            dest.writeFloat(this.COURSE_IN_PRICE);
            dest.writeFloat(this.COURSE_OUT_PRICE);
            dest.writeString(this.COURSE_CLASS);
            dest.writeString(this.COURSE_ADDRESS);
            dest.writeString(this.COURSE_LIST_TYPE);
//            dest.writeParcelable(this.COURSE_TIME1, flags);
//            dest.writeParcelable(this.COURSE_TIME2, flags);
            dest.writeString(this.COURSE_IN_LIST);
            dest.writeString(this.COURSE_OUT_LIST);
            dest.writeInt(this.COURSE_STATUS);
//            dest.writeParcelable(this.COURSE_STATUS_TM, flags);
//            dest.writeParcelable(this.COURSE_STATUS_PERSON, flags);
//            dest.writeParcelable(this.NOTE, flags);
            dest.writeString(this.SMALL_COURSE_ADDRESS);
//            dest.writeParcelable(this.COURSE_CATEGORY, flags);
            dest.writeInt(this.VISIT_TIME);
            dest.writeString(this.COURSE_UP_NAME);
            dest.writeString(this.COURSE_PAY);
            dest.writeString(this.COURSE_ISPAY);
            dest.writeInt(this.COURSE_SCORE);
//            dest.writeParcelable(this.CUSTOMER_ID, flags);
//            dest.writeParcelable(this.COURSE_COMMENTS, flags);
            dest.writeInt(this.RN);
        }

        public ResultBean() {
        }

        protected ResultBean(Parcel in) {
            this.COURSE_ID = in.readString();
            this.SITE_ID = in.readString();
            this.COURSE_UP_ID = in.readString();
            this.COURSE_UP_TIME = in.readString();
            this.COURSE_NAME = in.readString();
            this.COURSE_DESC = in.readString();
            this.SMALL_PIC = in.readString();
            this.COURSE_IN_PRICE = in.readFloat();
            this.COURSE_OUT_PRICE = in.readFloat();
            this.COURSE_CLASS = in.readString();
            this.COURSE_ADDRESS = in.readString();
            this.COURSE_LIST_TYPE = in.readString();
            this.COURSE_IN_LIST = in.readString();
            this.COURSE_OUT_LIST = in.readString();
            this.COURSE_STATUS = in.readInt();
            this.SMALL_COURSE_ADDRESS = in.readString();
            this.VISIT_TIME = in.readInt();
            this.COURSE_UP_NAME = in.readString();
            this.COURSE_PAY = in.readString();
            this.COURSE_ISPAY = in.readString();
            this.COURSE_SCORE = in.readInt();
            this.RN = in.readInt();
        }

        public static final Parcelable.Creator<ResultBean> CREATOR = new Parcelable.Creator<ResultBean>() {
            @Override
            public ResultBean createFromParcel(Parcel source) {
                return new ResultBean(source);
            }

            @Override
            public ResultBean[] newArray(int size) {
                return new ResultBean[size];
            }
        };
    }
}
