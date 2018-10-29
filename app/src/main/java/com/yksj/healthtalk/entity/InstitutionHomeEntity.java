package com.yksj.healthtalk.entity;

import java.util.List;

/**
 * Created by hekl on 18/8/2.
 */

public class InstitutionHomeEntity{

    /**
     * code : 1
     * message : 操作完成
     * result : [{"UNIT_CODE":103,"UNIT_NAME":"如粪土呀","UNIT_PIC1":"/institutionsFile/1533527158244.jpg","CLASS_TYPE":"3","UNIT_SPECIALTY_DESC":"让人头疼","UNIT_ADDRESS_DESC":"北京市东城区刚回家斤","ADDRESS":"北京市东城区","RN":1},{"UNIT_CODE":109,"UNIT_NAME":"2232","UNIT_PIC1":"/institutionsFile/1533534281075.jpg","CLASS_TYPE":"体验中心","UNIT_SPECIALTY_DESC":"测试","UNIT_ADDRESS_DESC":"北京市东城区","ADDRESS":null,"RN":2}]
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
         * UNIT_CODE : 103
         * UNIT_NAME : 如粪土呀
         * UNIT_PIC1 : /institutionsFile/1533527158244.jpg
         * CLASS_TYPE : 3
         * UNIT_SPECIALTY_DESC : 让人头疼
         * UNIT_ADDRESS_DESC : 北京市东城区刚回家斤
         * ADDRESS : 北京市东城区
         * RN : 1
         */

        private int UNIT_CODE;
        private String UNIT_NAME;
        private String UNIT_PIC1;
        private String CLASS_TYPE;
        private String UNIT_SPECIALTY_DESC;
        private String UNIT_ADDRESS_DESC;
        private String ADDRESS;
        private int RN;

        public int getUNIT_CODE() {
            return UNIT_CODE;
        }

        public void setUNIT_CODE(int UNIT_CODE) {
            this.UNIT_CODE = UNIT_CODE;
        }

        public String getUNIT_NAME() {
            return UNIT_NAME;
        }

        public void setUNIT_NAME(String UNIT_NAME) {
            this.UNIT_NAME = UNIT_NAME;
        }

        public String getUNIT_PIC1() {
            return UNIT_PIC1;
        }

        public void setUNIT_PIC1(String UNIT_PIC1) {
            this.UNIT_PIC1 = UNIT_PIC1;
        }

        public String getCLASS_TYPE() {
            return CLASS_TYPE;
        }

        public void setCLASS_TYPE(String CLASS_TYPE) {
            this.CLASS_TYPE = CLASS_TYPE;
        }

        public String getUNIT_SPECIALTY_DESC() {
            return UNIT_SPECIALTY_DESC;
        }

        public void setUNIT_SPECIALTY_DESC(String UNIT_SPECIALTY_DESC) {
            this.UNIT_SPECIALTY_DESC = UNIT_SPECIALTY_DESC;
        }

        public String getUNIT_ADDRESS_DESC() {
            return UNIT_ADDRESS_DESC;
        }

        public void setUNIT_ADDRESS_DESC(String UNIT_ADDRESS_DESC) {
            this.UNIT_ADDRESS_DESC = UNIT_ADDRESS_DESC;
        }

        public String getADDRESS() {
            return ADDRESS;
        }

        public void setADDRESS(String ADDRESS) {
            this.ADDRESS = ADDRESS;
        }

        public int getRN() {
            return RN;
        }

        public void setRN(int RN) {
            this.RN = RN;
        }
    }
}
