package com.yksj.healthtalk.entity;

import java.util.List;

/**
 * Created by hekl on 18/4/24.
 */

public class HospitaFindTeamEntity {

    /**
     * code : 1
     * message : success
     * result : {"count":1,"list":[{"SITE_ID":300,"SITE_NAME":"666","OFFICE_ID":1024,"SITE_AREA":"340100","SITE_DESC":"88888","SITE_BIG_PIC":"/CusZiYuan/Voicechat/1/1//big1508139519094.png","SITE_SMALL_PIC":"/CusZiYuan/Voicechat/1/1//small1508139519094.png","SITE_CREATEOR":"124951","SITE_CR_TIME":"20171016153839","SITE_STATUS":20,"SITE_HOSPOTAL":null,"VISIT_TIME":14,"HOSPITAL_DESC":null,"SITE_CREATEOR_DESC":null,"ICON_DOCTOR_PICTURE":"/CusZiYuan/resources/124951/doctorPic/20170623131757.png","DOCTOR_NAME":"申昆玲","OFFICE_NAME":"儿科综合","MEMBER_NUM":2,"ORDER_NUM":46,"RN":1}]}
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
         * count : 1
         * list : [{"SITE_ID":300,"SITE_NAME":"666","OFFICE_ID":1024,"SITE_AREA":"340100","SITE_DESC":"88888","SITE_BIG_PIC":"/CusZiYuan/Voicechat/1/1//big1508139519094.png","SITE_SMALL_PIC":"/CusZiYuan/Voicechat/1/1//small1508139519094.png","SITE_CREATEOR":"124951","SITE_CR_TIME":"20171016153839","SITE_STATUS":20,"SITE_HOSPOTAL":null,"VISIT_TIME":14,"HOSPITAL_DESC":null,"SITE_CREATEOR_DESC":null,"ICON_DOCTOR_PICTURE":"/CusZiYuan/resources/124951/doctorPic/20170623131757.png","DOCTOR_NAME":"申昆玲","OFFICE_NAME":"儿科综合","MEMBER_NUM":2,"ORDER_NUM":46,"RN":1}]
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
             * SITE_ID : 300
             * SITE_NAME : 666
             * OFFICE_ID : 1024
             * SITE_AREA : 340100
             * SITE_DESC : 88888
             * SITE_BIG_PIC : /CusZiYuan/Voicechat/1/1//big1508139519094.png
             * SITE_SMALL_PIC : /CusZiYuan/Voicechat/1/1//small1508139519094.png
             * SITE_CREATEOR : 124951
             * SITE_CR_TIME : 20171016153839
             * SITE_STATUS : 20
             * SITE_HOSPOTAL : null
             * VISIT_TIME : 14
             * HOSPITAL_DESC : null
             * SITE_CREATEOR_DESC : null
             * ICON_DOCTOR_PICTURE : /CusZiYuan/resources/124951/doctorPic/20170623131757.png
             * DOCTOR_NAME : 申昆玲
             * OFFICE_NAME : 儿科综合
             * MEMBER_NUM : 2
             * ORDER_NUM : 46
             * RN : 1
             */

            private int SITE_ID;
            private String SITE_NAME;
            private int OFFICE_ID;
            private String SITE_AREA;
            private String SITE_DESC;
            private String SITE_BIG_PIC;
            private String SITE_SMALL_PIC;
            private String SITE_CREATEOR;
            private String SITE_CR_TIME;
            private int SITE_STATUS;
            private String SITE_HOSPOTAL;
            private int VISIT_TIME;
            private Object HOSPITAL_DESC;
            private Object SITE_CREATEOR_DESC;
            private String ICON_DOCTOR_PICTURE;
            private String DOCTOR_NAME;
            private String OFFICE_NAME;
            private int MEMBER_NUM;
            private int ORDER_NUM;
            private int RN;

            public int getSITE_ID() {
                return SITE_ID;
            }

            public void setSITE_ID(int SITE_ID) {
                this.SITE_ID = SITE_ID;
            }

            public String getSITE_NAME() {
                return SITE_NAME;
            }

            public void setSITE_NAME(String SITE_NAME) {
                this.SITE_NAME = SITE_NAME;
            }

            public int getOFFICE_ID() {
                return OFFICE_ID;
            }

            public void setOFFICE_ID(int OFFICE_ID) {
                this.OFFICE_ID = OFFICE_ID;
            }

            public String getSITE_AREA() {
                return SITE_AREA;
            }

            public void setSITE_AREA(String SITE_AREA) {
                this.SITE_AREA = SITE_AREA;
            }

            public String getSITE_DESC() {
                return SITE_DESC;
            }

            public void setSITE_DESC(String SITE_DESC) {
                this.SITE_DESC = SITE_DESC;
            }

            public String getSITE_BIG_PIC() {
                return SITE_BIG_PIC;
            }

            public void setSITE_BIG_PIC(String SITE_BIG_PIC) {
                this.SITE_BIG_PIC = SITE_BIG_PIC;
            }

            public String getSITE_SMALL_PIC() {
                return SITE_SMALL_PIC;
            }

            public void setSITE_SMALL_PIC(String SITE_SMALL_PIC) {
                this.SITE_SMALL_PIC = SITE_SMALL_PIC;
            }

            public String getSITE_CREATEOR() {
                return SITE_CREATEOR;
            }

            public void setSITE_CREATEOR(String SITE_CREATEOR) {
                this.SITE_CREATEOR = SITE_CREATEOR;
            }

            public String getSITE_CR_TIME() {
                return SITE_CR_TIME;
            }

            public void setSITE_CR_TIME(String SITE_CR_TIME) {
                this.SITE_CR_TIME = SITE_CR_TIME;
            }

            public int getSITE_STATUS() {
                return SITE_STATUS;
            }

            public void setSITE_STATUS(int SITE_STATUS) {
                this.SITE_STATUS = SITE_STATUS;
            }

            public String getSITE_HOSPOTAL() {
                return SITE_HOSPOTAL;
            }

            public void setSITE_HOSPOTAL(String SITE_HOSPOTAL) {
                this.SITE_HOSPOTAL = SITE_HOSPOTAL;
            }

            public int getVISIT_TIME() {
                return VISIT_TIME;
            }

            public void setVISIT_TIME(int VISIT_TIME) {
                this.VISIT_TIME = VISIT_TIME;
            }

            public Object getHOSPITAL_DESC() {
                return HOSPITAL_DESC;
            }

            public void setHOSPITAL_DESC(Object HOSPITAL_DESC) {
                this.HOSPITAL_DESC = HOSPITAL_DESC;
            }

            public Object getSITE_CREATEOR_DESC() {
                return SITE_CREATEOR_DESC;
            }

            public void setSITE_CREATEOR_DESC(Object SITE_CREATEOR_DESC) {
                this.SITE_CREATEOR_DESC = SITE_CREATEOR_DESC;
            }

            public String getICON_DOCTOR_PICTURE() {
                return ICON_DOCTOR_PICTURE;
            }

            public void setICON_DOCTOR_PICTURE(String ICON_DOCTOR_PICTURE) {
                this.ICON_DOCTOR_PICTURE = ICON_DOCTOR_PICTURE;
            }

            public String getDOCTOR_NAME() {
                return DOCTOR_NAME;
            }

            public void setDOCTOR_NAME(String DOCTOR_NAME) {
                this.DOCTOR_NAME = DOCTOR_NAME;
            }

            public String getOFFICE_NAME() {
                return OFFICE_NAME;
            }

            public void setOFFICE_NAME(String OFFICE_NAME) {
                this.OFFICE_NAME = OFFICE_NAME;
            }

            public int getMEMBER_NUM() {
                return MEMBER_NUM;
            }

            public void setMEMBER_NUM(int MEMBER_NUM) {
                this.MEMBER_NUM = MEMBER_NUM;
            }

            public int getORDER_NUM() {
                return ORDER_NUM;
            }

            public void setORDER_NUM(int ORDER_NUM) {
                this.ORDER_NUM = ORDER_NUM;
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
