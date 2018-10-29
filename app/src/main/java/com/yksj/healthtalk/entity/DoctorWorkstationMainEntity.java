package com.yksj.healthtalk.entity;

import java.util.List;

/**
 * Created by hekl on 18/4/28.
 */

public class DoctorWorkstationMainEntity {
    /**
     * code : 1
     * message : success
     * result : {"siteService":[{"SITE_ID":354,"SERVICE_TYPE_ID":5,"ORDER_ON_OFF":0,"SERVICE_PRICE":0,"SERVICE_TIME_LIMIT":0,"SERVICE_CREATE_TIME":"20180427151649","SERVICE_CREATOR":"3779","ORDER_COUNT":1},{"SITE_ID":354,"SERVICE_TYPE_ID":3,"ORDER_ON_OFF":0,"SERVICE_PRICE":0,"SERVICE_TIME_LIMIT":0,"SERVICE_CREATE_TIME":"20180427151649","SERVICE_CREATOR":"3779","ORDER_COUNT":0}],"siteMember":[{"CUSTOMER_ID":3779,"MEMBER_TYPE":10,"DOCTOR_REAL_NAME":"赵琴","DOCTOR_PICTURE":"/FalseDoctorPic/20150701.jpg","ICON_DOCTOR_PICTURE":"/FalseDoctorPic/20150701.jpg","DOCTOR_TITLE":2,"TITLE_NAME":"副主任医师","DOCTOR_OFFICE2":1003,"OFFICE_NAME":"小儿感染科","WORK_LOCATION_DESC":"北京市 西城区","RN":1},{"CUSTOMER_ID":124783,"MEMBER_TYPE":30,"DOCTOR_REAL_NAME":"苏芮","DOCTOR_PICTURE":"/CusZiYuan/resources/124783/doctorPic/doc_100_20150717191416.png","ICON_DOCTOR_PICTURE":"/CusZiYuan/resources/124783/doctorPic/20150717191416.png","DOCTOR_TITLE":1,"TITLE_NAME":"主任医师","DOCTOR_OFFICE2":1003,"OFFICE_NAME":"小儿感染科","WORK_LOCATION_DESC":"北京市 东城区","ORDER_COUNT":0,"GOOD_EV":0,"ALL_EV":0,"RN":1}],"siteeValuate":{"EVALUATE_ID":625,"CONSULTATION_ID":18223,"DOCTOR_ID":124984,"CUSTOMER_ID":230536,"EVALUATE_LEVEL":5,"EVALUATE_TIME":"20171118172101","EVALUATE_CONTENT":"5.0","NOTE":"tuwen","EVALUATE_STATURS":1,"REPLY_CONTENT":"也一样","CHECK_STATUS":10,"CHECK_TIME":null,"CHECK_PERSON":null,"CHECK_NOTE":null,"SHOW_FLAG":1,"PRAISE_COUNT":0,"CUSTOMER_NAME":"我们的","RN":1},"siteInfo":{"SITE_ID":354,"SITE_NAME":"长长的","OFFICE_ID":1,"SITE_AREA":"0","SITE_DESC":"脱发人","SITE_BIG_PIC":"/CusZiYuan/Voicechat/1/1//big1524813410930.png","SITE_SMALL_PIC":null,"SITE_CREATEOR":"3779","SITE_CR_TIME":"20180427151649","SITE_STATUS":20,"SITE_HOSPOTAL":"丰富的","VISIT_TIME":0,"HOSPITAL_DESC":"高丰富","SITE_CREATEOR_DESC":"冯丰富","GROUP_ID":2607,"DOCTOR_NAME":"赵琴","OFFICE_NAME":"呼吸内科","MEMBER_NUM":2,"ORDER_NUM":1}}
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
         * siteService : [{"SITE_ID":354,"SERVICE_TYPE_ID":5,"ORDER_ON_OFF":0,"SERVICE_PRICE":0,"SERVICE_TIME_LIMIT":0,"SERVICE_CREATE_TIME":"20180427151649","SERVICE_CREATOR":"3779","ORDER_COUNT":1},{"SITE_ID":354,"SERVICE_TYPE_ID":3,"ORDER_ON_OFF":0,"SERVICE_PRICE":0,"SERVICE_TIME_LIMIT":0,"SERVICE_CREATE_TIME":"20180427151649","SERVICE_CREATOR":"3779","ORDER_COUNT":0}]
         * siteMember : [{"CUSTOMER_ID":3779,"MEMBER_TYPE":10,"DOCTOR_REAL_NAME":"赵琴","DOCTOR_PICTURE":"/FalseDoctorPic/20150701.jpg","ICON_DOCTOR_PICTURE":"/FalseDoctorPic/20150701.jpg","DOCTOR_TITLE":2,"TITLE_NAME":"副主任医师","DOCTOR_OFFICE2":1003,"OFFICE_NAME":"小儿感染科","WORK_LOCATION_DESC":"北京市 西城区","RN":1},{"CUSTOMER_ID":124783,"MEMBER_TYPE":30,"DOCTOR_REAL_NAME":"苏芮","DOCTOR_PICTURE":"/CusZiYuan/resources/124783/doctorPic/doc_100_20150717191416.png","ICON_DOCTOR_PICTURE":"/CusZiYuan/resources/124783/doctorPic/20150717191416.png","DOCTOR_TITLE":1,"TITLE_NAME":"主任医师","DOCTOR_OFFICE2":1003,"OFFICE_NAME":"小儿感染科","WORK_LOCATION_DESC":"北京市 东城区","ORDER_COUNT":0,"GOOD_EV":0,"ALL_EV":0,"RN":1}]
         * siteeValuate : {"EVALUATE_ID":625,"CONSULTATION_ID":18223,"DOCTOR_ID":124984,"CUSTOMER_ID":230536,"EVALUATE_LEVEL":5,"EVALUATE_TIME":"20171118172101","EVALUATE_CONTENT":"5.0","NOTE":"tuwen","EVALUATE_STATURS":1,"REPLY_CONTENT":"也一样","CHECK_STATUS":10,"CHECK_TIME":null,"CHECK_PERSON":null,"CHECK_NOTE":null,"SHOW_FLAG":1,"PRAISE_COUNT":0,"CUSTOMER_NAME":"我们的","RN":1}
         * siteInfo : {"SITE_ID":354,"SITE_NAME":"长长的","OFFICE_ID":1,"SITE_AREA":"0","SITE_DESC":"脱发人","SITE_BIG_PIC":"/CusZiYuan/Voicechat/1/1//big1524813410930.png","SITE_SMALL_PIC":null,"SITE_CREATEOR":"3779","SITE_CR_TIME":"20180427151649","SITE_STATUS":20,"SITE_HOSPOTAL":"丰富的","VISIT_TIME":0,"HOSPITAL_DESC":"高丰富","SITE_CREATEOR_DESC":"冯丰富","GROUP_ID":2607,"DOCTOR_NAME":"赵琴","OFFICE_NAME":"呼吸内科","MEMBER_NUM":2,"ORDER_NUM":1}
         */

        private SiteeValuateBean siteeValuate;
        private SiteInfoBean siteInfo;
        private List<SiteServiceBean> siteService;
        private List<SiteMemberBean> siteMember;
        private String qrCodeUrl;

        public String getQrCodeUrl() {
            return qrCodeUrl;
        }

        public void setQrCodeUrl(String qrCodeUrl) {
            this.qrCodeUrl = qrCodeUrl;
        }

        public SiteeValuateBean getSiteeValuate() {
            return siteeValuate;
        }

        public void setSiteeValuate(SiteeValuateBean siteeValuate) {
            this.siteeValuate = siteeValuate;
        }

        public SiteInfoBean getSiteInfo() {
            return siteInfo;
        }

        public void setSiteInfo(SiteInfoBean siteInfo) {
            this.siteInfo = siteInfo;
        }

        public List<SiteServiceBean> getSiteService() {
            return siteService;
        }

        public void setSiteService(List<SiteServiceBean> siteService) {
            this.siteService = siteService;
        }

        public List<SiteMemberBean> getSiteMember() {
            return siteMember;
        }

        public void setSiteMember(List<SiteMemberBean> siteMember) {
            this.siteMember = siteMember;
        }

        public static class SiteeValuateBean {
            /**
             * EVALUATE_ID : 625
             * CONSULTATION_ID : 18223
             * DOCTOR_ID : 124984
             * CUSTOMER_ID : 230536
             * EVALUATE_LEVEL : 5
             * EVALUATE_TIME : 20171118172101
             * EVALUATE_CONTENT : 5.0
             * NOTE : tuwen
             * EVALUATE_STATURS : 1
             * REPLY_CONTENT : 也一样
             * CHECK_STATUS : 10
             * CHECK_TIME : null
             * CHECK_PERSON : null
             * CHECK_NOTE : null
             * SHOW_FLAG : 1
             * PRAISE_COUNT : 0
             * CUSTOMER_NAME : 我们的
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
            private String REPLY_CONTENT;
            private int CHECK_STATUS;
            private Object CHECK_TIME;
            private Object CHECK_PERSON;
            private Object CHECK_NOTE;
            private int SHOW_FLAG;
            private int PRAISE_COUNT;
            private String CUSTOMER_NAME;
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

            public String getREPLY_CONTENT() {
                return REPLY_CONTENT;
            }

            public void setREPLY_CONTENT(String REPLY_CONTENT) {
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

            public int getRN() {
                return RN;
            }

            public void setRN(int RN) {
                this.RN = RN;
            }
        }

        public static class SiteInfoBean {
            /**
             * SITE_ID : 354
             * SITE_NAME : 长长的
             * OFFICE_ID : 1
             * SITE_AREA : 0
             * SITE_DESC : 脱发人
             * SITE_BIG_PIC : /CusZiYuan/Voicechat/1/1//big1524813410930.png
             * SITE_SMALL_PIC : null
             * SITE_CREATEOR : 3779
             * SITE_CR_TIME : 20180427151649
             * SITE_STATUS : 20
             * SITE_HOSPOTAL : 丰富的
             * VISIT_TIME : 0
             * HOSPITAL_DESC : 高丰富
             * SITE_CREATEOR_DESC : 冯丰富
             * GROUP_ID : 2607
             * DOCTOR_NAME : 赵琴
             * OFFICE_NAME : 呼吸内科
             * MEMBER_NUM : 2
             * ORDER_NUM : 1
             */

            private int SITE_ID;
            private String SITE_NAME;
            private int OFFICE_ID;
            private String SITE_AREA;
            private String SITE_DESC;
            private String SITE_BIG_PIC;
            private Object SITE_SMALL_PIC;
            private String SITE_CREATEOR;
            private String SITE_CR_TIME;
            private int SITE_STATUS;
            private String SITE_HOSPOTAL;
            private int VISIT_TIME;
            private String HOSPITAL_DESC;
            private String SITE_CREATEOR_DESC;
            private int GROUP_ID;
            private String DOCTOR_NAME;
            private String OFFICE_NAME;
            private int MEMBER_NUM;
            private int ORDER_NUM;
            private int IS_FOLLOW;

            public int getIS_FOLLOW() {
                return IS_FOLLOW;
            }

            public void setIS_FOLLOW(int IS_FOLLOW) {
                this.IS_FOLLOW = IS_FOLLOW;
            }

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

            public Object getSITE_SMALL_PIC() {
                return SITE_SMALL_PIC;
            }

            public void setSITE_SMALL_PIC(Object SITE_SMALL_PIC) {
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

            public String getHOSPITAL_DESC() {
                return HOSPITAL_DESC;
            }

            public void setHOSPITAL_DESC(String HOSPITAL_DESC) {
                this.HOSPITAL_DESC = HOSPITAL_DESC;
            }

            public String getSITE_CREATEOR_DESC() {
                return SITE_CREATEOR_DESC;
            }

            public void setSITE_CREATEOR_DESC(String SITE_CREATEOR_DESC) {
                this.SITE_CREATEOR_DESC = SITE_CREATEOR_DESC;
            }

            public int getGROUP_ID() {
                return GROUP_ID;
            }

            public void setGROUP_ID(int GROUP_ID) {
                this.GROUP_ID = GROUP_ID;
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
        }

        public static class SiteServiceBean {
            /**
             * SITE_ID : 354
             * SERVICE_TYPE_ID : 5
             * ORDER_ON_OFF : 0
             * SERVICE_PRICE : 0
             * SERVICE_TIME_LIMIT : 0
             * SERVICE_CREATE_TIME : 20180427151649
             * SERVICE_CREATOR : 3779
             * ORDER_COUNT : 1
             */

            private int SITE_ID;
            private int SERVICE_TYPE_ID;
            private int ORDER_ON_OFF;
            private float SERVICE_PRICE;
            private int SERVICE_TIME_LIMIT;
            private String SERVICE_CREATE_TIME;
            private String SERVICE_CREATOR;
            private int ORDER_COUNT;

            public int getSITE_ID() {
                return SITE_ID;
            }

            public void setSITE_ID(int SITE_ID) {
                this.SITE_ID = SITE_ID;
            }

            public int getSERVICE_TYPE_ID() {
                return SERVICE_TYPE_ID;
            }

            public void setSERVICE_TYPE_ID(int SERVICE_TYPE_ID) {
                this.SERVICE_TYPE_ID = SERVICE_TYPE_ID;
            }

            public int getORDER_ON_OFF() {
                return ORDER_ON_OFF;
            }

            public void setORDER_ON_OFF(int ORDER_ON_OFF) {
                this.ORDER_ON_OFF = ORDER_ON_OFF;
            }

            public float getSERVICE_PRICE() {
                return SERVICE_PRICE;
            }

            public void setSERVICE_PRICE(float SERVICE_PRICE) {
                this.SERVICE_PRICE = SERVICE_PRICE;
            }

            public int getSERVICE_TIME_LIMIT() {
                return SERVICE_TIME_LIMIT;
            }

            public void setSERVICE_TIME_LIMIT(int SERVICE_TIME_LIMIT) {
                this.SERVICE_TIME_LIMIT = SERVICE_TIME_LIMIT;
            }

            public String getSERVICE_CREATE_TIME() {
                return SERVICE_CREATE_TIME;
            }

            public void setSERVICE_CREATE_TIME(String SERVICE_CREATE_TIME) {
                this.SERVICE_CREATE_TIME = SERVICE_CREATE_TIME;
            }

            public String getSERVICE_CREATOR() {
                return SERVICE_CREATOR;
            }

            public void setSERVICE_CREATOR(String SERVICE_CREATOR) {
                this.SERVICE_CREATOR = SERVICE_CREATOR;
            }

            public int getORDER_COUNT() {
                return ORDER_COUNT;
            }

            public void setORDER_COUNT(int ORDER_COUNT) {
                this.ORDER_COUNT = ORDER_COUNT;
            }
        }

        public static class SiteMemberBean {
            /**
             * CUSTOMER_ID : 3779
             * MEMBER_TYPE : 10
             * DOCTOR_REAL_NAME : 赵琴
             * DOCTOR_PICTURE : /FalseDoctorPic/20150701.jpg
             * ICON_DOCTOR_PICTURE : /FalseDoctorPic/20150701.jpg
             * DOCTOR_TITLE : 2
             * TITLE_NAME : 副主任医师
             * DOCTOR_OFFICE2 : 1003
             * OFFICE_NAME : 小儿感染科
             * WORK_LOCATION_DESC : 北京市 西城区
             * RN : 1
             * ORDER_COUNT : 0
             * GOOD_EV : 0
             * ALL_EV : 0
             */

            private int CUSTOMER_ID;
            private int MEMBER_TYPE;
            private String DOCTOR_REAL_NAME;
            private String DOCTOR_PICTURE;
            private String ICON_DOCTOR_PICTURE;
            private int DOCTOR_TITLE;
            private String TITLE_NAME;
            private int DOCTOR_OFFICE2;
            private String OFFICE_NAME;
            private String WORK_LOCATION_DESC;
            private int RN;
            private int ORDER_COUNT;
            private int GOOD_EV;
            private int ALL_EV;

            public int getCUSTOMER_ID() {
                return CUSTOMER_ID;
            }

            public void setCUSTOMER_ID(int CUSTOMER_ID) {
                this.CUSTOMER_ID = CUSTOMER_ID;
            }

            public int getMEMBER_TYPE() {
                return MEMBER_TYPE;
            }

            public void setMEMBER_TYPE(int MEMBER_TYPE) {
                this.MEMBER_TYPE = MEMBER_TYPE;
            }

            public String getDOCTOR_REAL_NAME() {
                return DOCTOR_REAL_NAME;
            }

            public void setDOCTOR_REAL_NAME(String DOCTOR_REAL_NAME) {
                this.DOCTOR_REAL_NAME = DOCTOR_REAL_NAME;
            }

            public String getDOCTOR_PICTURE() {
                return DOCTOR_PICTURE;
            }

            public void setDOCTOR_PICTURE(String DOCTOR_PICTURE) {
                this.DOCTOR_PICTURE = DOCTOR_PICTURE;
            }

            public String getICON_DOCTOR_PICTURE() {
                return ICON_DOCTOR_PICTURE;
            }

            public void setICON_DOCTOR_PICTURE(String ICON_DOCTOR_PICTURE) {
                this.ICON_DOCTOR_PICTURE = ICON_DOCTOR_PICTURE;
            }

            public int getDOCTOR_TITLE() {
                return DOCTOR_TITLE;
            }

            public void setDOCTOR_TITLE(int DOCTOR_TITLE) {
                this.DOCTOR_TITLE = DOCTOR_TITLE;
            }

            public String getTITLE_NAME() {
                return TITLE_NAME;
            }

            public void setTITLE_NAME(String TITLE_NAME) {
                this.TITLE_NAME = TITLE_NAME;
            }

            public int getDOCTOR_OFFICE2() {
                return DOCTOR_OFFICE2;
            }

            public void setDOCTOR_OFFICE2(int DOCTOR_OFFICE2) {
                this.DOCTOR_OFFICE2 = DOCTOR_OFFICE2;
            }

            public String getOFFICE_NAME() {
                return OFFICE_NAME;
            }

            public void setOFFICE_NAME(String OFFICE_NAME) {
                this.OFFICE_NAME = OFFICE_NAME;
            }

            public String getWORK_LOCATION_DESC() {
                return WORK_LOCATION_DESC;
            }

            public void setWORK_LOCATION_DESC(String WORK_LOCATION_DESC) {
                this.WORK_LOCATION_DESC = WORK_LOCATION_DESC;
            }

            public int getRN() {
                return RN;
            }

            public void setRN(int RN) {
                this.RN = RN;
            }

            public int getORDER_COUNT() {
                return ORDER_COUNT;
            }

            public void setORDER_COUNT(int ORDER_COUNT) {
                this.ORDER_COUNT = ORDER_COUNT;
            }

            public int getGOOD_EV() {
                return GOOD_EV;
            }

            public void setGOOD_EV(int GOOD_EV) {
                this.GOOD_EV = GOOD_EV;
            }

            public int getALL_EV() {
                return ALL_EV;
            }

            public void setALL_EV(int ALL_EV) {
                this.ALL_EV = ALL_EV;
            }
        }
    }
}
