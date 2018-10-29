package com.yksj.healthtalk.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by hekl on 18/8/3.
 */

public class InsActEntity {
    /**
     * code : 1
     * message : 操作完成
     * result : [{"UNIT_PIC1":null,"ACTIV_TITLE":"毒素爱护","ACTIV_CODE":1,"ACTIV_DESC":"test","ACTIV_TIME_DESC":"谁都会吃爱仕达"}]
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
         * UNIT_PIC1 : null
         * ACTIV_TITLE : 毒素爱护
         * ACTIV_CODE : 1
         * ACTIV_DESC : test
         * ACTIV_TIME_DESC : 谁都会吃爱仕达
         */

        private String UNIT_PIC1;
        private String ACTIV_TITLE;
        private int ACTIV_CODE;
        private String ACTIV_DESC;
        private String ACTIV_TIME_DESC;

        public String getUNIT_PIC1() {
            return UNIT_PIC1;
        }

        public void setUNIT_PIC1(String UNIT_PIC1) {
            this.UNIT_PIC1 = UNIT_PIC1;
        }

        public String getACTIV_TITLE() {
            return ACTIV_TITLE;
        }

        public void setACTIV_TITLE(String ACTIV_TITLE) {
            this.ACTIV_TITLE = ACTIV_TITLE;
        }

        public int getACTIV_CODE() {
            return ACTIV_CODE;
        }

        public void setACTIV_CODE(int ACTIV_CODE) {
            this.ACTIV_CODE = ACTIV_CODE;
        }

        public String getACTIV_DESC() {
            return ACTIV_DESC;
        }

        public void setACTIV_DESC(String ACTIV_DESC) {
            this.ACTIV_DESC = ACTIV_DESC;
        }

        public String getACTIV_TIME_DESC() {
            return ACTIV_TIME_DESC;
        }

        public void setACTIV_TIME_DESC(String ACTIV_TIME_DESC) {
            this.ACTIV_TIME_DESC = ACTIV_TIME_DESC;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.UNIT_PIC1);
            dest.writeString(this.ACTIV_TITLE);
            dest.writeInt(this.ACTIV_CODE);
            dest.writeString(this.ACTIV_DESC);
            dest.writeString(this.ACTIV_TIME_DESC);
        }

        public ResultBean() {
        }

        protected ResultBean(Parcel in) {
            this.UNIT_PIC1 = in.readString();
            this.ACTIV_TITLE = in.readString();
            this.ACTIV_CODE = in.readInt();
            this.ACTIV_DESC = in.readString();
            this.ACTIV_TIME_DESC = in.readString();
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
