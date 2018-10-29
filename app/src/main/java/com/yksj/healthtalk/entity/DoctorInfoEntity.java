package com.yksj.healthtalk.entity;

import java.util.List;

/**
 * Created by hekl on 18/5/2.
 */

public class DoctorInfoEntity {
    /**
     * code : 1
     * message : 查询完成
     * result : {"CUSTOMER_ID":3774,"DOCTOR_REAL_NAME":"李安民","DOCTOR_SPECIALLY":"呼吸科疾病","DOCTOR_HOSPITAL":"北京中医医院","INTRODUCTION":"二十余年儿科经验","ICON_DOCTOR_PICTURE":"/CusZiYuan/resources/3774/doctorPic/20160712133251.png","WORK_LOCATION":"110101","WORK_LOCATION_DESC":"北京市 东城区","OFFICE_NAME":"儿科综合","TITLE_NAME":"主治医师","BIG_ICON_BACKGROUND":"/CusZiYuan/resources/3774/doctorPic/doc_100_20160712133251.png","MEMBER_TYPE":10,"isFollow":0,"siteDesc":[{"SITE_DESC":"The new version of a great game that is so fun I would love it more if you could see how it works with it to play with friends or play with me and play with me every time you get it I will send you my email and you will see what you have and I will get you some of my stuff and I\u2019ll get you a new phone number so you ","SITE_NAME":"测试卷","MEMBER_NUM":2,"ORDER_NUM":0}],"evaluate":{"EVALUATE_ID":512,"CONSULTATION_ID":15481,"DOCTOR_ID":3774,"CUSTOMER_ID":229490,"EVALUATE_LEVEL":4,"EVALUATE_TIME":"20170731085146","EVALUATE_CONTENT":"4.0","NOTE":"tuwen","EVALUATE_STATURS":0,"REPLY_CONTENT":null,"CHECK_STATUS":10,"CHECK_TIME":null,"CHECK_PERSON":null,"CHECK_NOTE":null,"SHOW_FLAG":1,"PRAISE_COUNT":0,"REAL_NAME":"小周","RN":1},"siteService":[{"SITE_ID":345,"SERVICE_TYPE_ID":3,"ORDER_ON_OFF":1,"SERVICE_PRICE":3,"SERVICE_TIME_LIMIT":null,"SERVICE_CREATE_TIME":"20180425173738","SERVICE_CREATOR":"3774","SERVICE_ITEM_ID":44006,"SITE_NAME":"测试卷","ICON_DOCTOR_PICTURE":"/CusZiYuan/resources/3774/doctorPic/20160712133251.png","ORDER_NUM":0,"DATA":{"ISBUZY":"0","MERCHANT_ID":null,"CUSTOMER_ID":3774,"SERVICE_ITEM_ID":44006,"SERVICE_ITEM_NAME":null,"SERVICE_TYPE_ID":3,"SERVICE_TYPE_SUB_ID":8,"ORDER_ON_OFF":1,"ORDER_ON_OFF_TIME":"20170607171643","SERVICE_MAX":55,"SERVICE_PRICE":"0.00","SERVICE_PLACE":null,"SERVICE_ITEM_DESC":null,"SERVICE_HOUR":7,"SERVICE_TIME_BEGIN":"20180502080000","SERVICE_TIME_END":"20180502150000","ORDER_CURRENT":0,"REPEAT_FLAG":2,"REPEAT_BATCH":7006,"WEEK":3,"REPEAT_START_DATE":"20170703","REPEAT_END_DATE":"99999999","REPEAT_START_TIME":"080000","REPEAT_END_TIME":"150000","USED_FLAG":1,"SERVICE_CREATE_TIME":"20180402000002","CYCLE_CODE":null,"FREE_MEDICAL_FLAG":0,"FREE_MEDICAL_PRICE":"0.00","FREE_MEDICAL_START_TIME":null,"FREE_MEDICAL_END_TIME":null}},{"SITE_ID":345,"SERVICE_TYPE_ID":5,"ORDER_ON_OFF":1,"SERVICE_PRICE":1,"SERVICE_TIME_LIMIT":null,"SERVICE_CREATE_TIME":"20180425173734","SERVICE_CREATOR":"3774","SERVICE_ITEM_ID":40331,"SITE_NAME":"测试卷","ICON_DOCTOR_PICTURE":"/CusZiYuan/resources/3774/doctorPic/20160712133251.png","ORDER_NUM":0}],"doctorService":[{"ORDER_ON_OFF":1,"SERVICE_ITEM_ID":40332,"SERVICE_PRICE":1,"SERVICE_TYPE_ID":6,"SERVICE_HOUR":10,"FREE_MEDICAL_FLAG":1,"FREE_MEDICAL_PRICE":5,"FREE_MEDICAL_START_TIME":"19170101000000","FREE_MEDICAL_END_TIME":"20181201000000","ORDER_NUM":13},{"ORDER_ON_OFF":1,"SERVICE_ITEM_ID":40334,"SERVICE_PRICE":0.01,"SERVICE_TYPE_ID":7,"SERVICE_HOUR":0,"FREE_MEDICAL_FLAG":0,"FREE_MEDICAL_PRICE":0,"FREE_MEDICAL_START_TIME":null,"FREE_MEDICAL_END_TIME":null,"ORDER_NUM":2},{"ORDER_ON_OFF":1,"SERVICE_ITEM_ID":40331,"SERVICE_PRICE":0.02,"SERVICE_TYPE_ID":5,"SERVICE_HOUR":0,"FREE_MEDICAL_FLAG":1,"FREE_MEDICAL_PRICE":0.01,"FREE_MEDICAL_START_TIME":"20180101000000","FREE_MEDICAL_END_TIME":"20180420000000","ORDER_NUM":12},{"ORDER_ON_OFF":1,"SERVICE_ITEM_ID":40335,"SERVICE_PRICE":0,"SERVICE_TYPE_ID":8,"SERVICE_HOUR":3,"FREE_MEDICAL_FLAG":0,"FREE_MEDICAL_PRICE":0,"FREE_MEDICAL_START_TIME":null,"FREE_MEDICAL_END_TIME":null,"ORDER_NUM":8}],"roleType":"0","tools":[{"CONSULTATION_CENTER_ID":5,"TOOL_CODE":10151,"TOOL_NAME":"百度","TOOL_DESC":null,"TOOL_URL":"http:www.baidu.com","USED_FLAG":1,"TOOL_SEQ":null,"NOTE":null,"DOCTOR_ID":3774}]}
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
         * CUSTOMER_ID : 3774
         * DOCTOR_REAL_NAME : 李安民
         * DOCTOR_SPECIALLY : 呼吸科疾病
         * DOCTOR_HOSPITAL : 北京中医医院
         * INTRODUCTION : 二十余年儿科经验
         * ICON_DOCTOR_PICTURE : /CusZiYuan/resources/3774/doctorPic/20160712133251.png
         * WORK_LOCATION : 110101
         * WORK_LOCATION_DESC : 北京市 东城区
         * OFFICE_NAME : 儿科综合
         * TITLE_NAME : 主治医师
         * BIG_ICON_BACKGROUND : /CusZiYuan/resources/3774/doctorPic/doc_100_20160712133251.png
         * MEMBER_TYPE : 10
         * isFollow : 0
         * siteDesc : [{"SITE_DESC":"The new version of a great game that is so fun I would love it more if you could see how it works with it to play with friends or play with me and play with me every time you get it I will send you my email and you will see what you have and I will get you some of my stuff and I\u2019ll get you a new phone number so you ","SITE_NAME":"测试卷","MEMBER_NUM":2,"ORDER_NUM":0}]
         * evaluate : {"EVALUATE_ID":512,"CONSULTATION_ID":15481,"DOCTOR_ID":3774,"CUSTOMER_ID":229490,"EVALUATE_LEVEL":4,"EVALUATE_TIME":"20170731085146","EVALUATE_CONTENT":"4.0","NOTE":"tuwen","EVALUATE_STATURS":0,"REPLY_CONTENT":null,"CHECK_STATUS":10,"CHECK_TIME":null,"CHECK_PERSON":null,"CHECK_NOTE":null,"SHOW_FLAG":1,"PRAISE_COUNT":0,"REAL_NAME":"小周","RN":1}
         * siteService : [{"SITE_ID":345,"SERVICE_TYPE_ID":3,"ORDER_ON_OFF":1,"SERVICE_PRICE":3,"SERVICE_TIME_LIMIT":null,"SERVICE_CREATE_TIME":"20180425173738","SERVICE_CREATOR":"3774","SERVICE_ITEM_ID":44006,"SITE_NAME":"测试卷","ICON_DOCTOR_PICTURE":"/CusZiYuan/resources/3774/doctorPic/20160712133251.png","ORDER_NUM":0,"DATA":{"ISBUZY":"0","MERCHANT_ID":null,"CUSTOMER_ID":3774,"SERVICE_ITEM_ID":44006,"SERVICE_ITEM_NAME":null,"SERVICE_TYPE_ID":3,"SERVICE_TYPE_SUB_ID":8,"ORDER_ON_OFF":1,"ORDER_ON_OFF_TIME":"20170607171643","SERVICE_MAX":55,"SERVICE_PRICE":"0.00","SERVICE_PLACE":null,"SERVICE_ITEM_DESC":null,"SERVICE_HOUR":7,"SERVICE_TIME_BEGIN":"20180502080000","SERVICE_TIME_END":"20180502150000","ORDER_CURRENT":0,"REPEAT_FLAG":2,"REPEAT_BATCH":7006,"WEEK":3,"REPEAT_START_DATE":"20170703","REPEAT_END_DATE":"99999999","REPEAT_START_TIME":"080000","REPEAT_END_TIME":"150000","USED_FLAG":1,"SERVICE_CREATE_TIME":"20180402000002","CYCLE_CODE":null,"FREE_MEDICAL_FLAG":0,"FREE_MEDICAL_PRICE":"0.00","FREE_MEDICAL_START_TIME":null,"FREE_MEDICAL_END_TIME":null}},{"SITE_ID":345,"SERVICE_TYPE_ID":5,"ORDER_ON_OFF":1,"SERVICE_PRICE":1,"SERVICE_TIME_LIMIT":null,"SERVICE_CREATE_TIME":"20180425173734","SERVICE_CREATOR":"3774","SERVICE_ITEM_ID":40331,"SITE_NAME":"测试卷","ICON_DOCTOR_PICTURE":"/CusZiYuan/resources/3774/doctorPic/20160712133251.png","ORDER_NUM":0}]
         * doctorService : [{"ORDER_ON_OFF":1,"SERVICE_ITEM_ID":40332,"SERVICE_PRICE":1,"SERVICE_TYPE_ID":6,"SERVICE_HOUR":10,"FREE_MEDICAL_FLAG":1,"FREE_MEDICAL_PRICE":5,"FREE_MEDICAL_START_TIME":"19170101000000","FREE_MEDICAL_END_TIME":"20181201000000","ORDER_NUM":13},{"ORDER_ON_OFF":1,"SERVICE_ITEM_ID":40334,"SERVICE_PRICE":0.01,"SERVICE_TYPE_ID":7,"SERVICE_HOUR":0,"FREE_MEDICAL_FLAG":0,"FREE_MEDICAL_PRICE":0,"FREE_MEDICAL_START_TIME":null,"FREE_MEDICAL_END_TIME":null,"ORDER_NUM":2},{"ORDER_ON_OFF":1,"SERVICE_ITEM_ID":40331,"SERVICE_PRICE":0.02,"SERVICE_TYPE_ID":5,"SERVICE_HOUR":0,"FREE_MEDICAL_FLAG":1,"FREE_MEDICAL_PRICE":0.01,"FREE_MEDICAL_START_TIME":"20180101000000","FREE_MEDICAL_END_TIME":"20180420000000","ORDER_NUM":12},{"ORDER_ON_OFF":1,"SERVICE_ITEM_ID":40335,"SERVICE_PRICE":0,"SERVICE_TYPE_ID":8,"SERVICE_HOUR":3,"FREE_MEDICAL_FLAG":0,"FREE_MEDICAL_PRICE":0,"FREE_MEDICAL_START_TIME":null,"FREE_MEDICAL_END_TIME":null,"ORDER_NUM":8}]
         * roleType : 0
         * tools : [{"CONSULTATION_CENTER_ID":5,"TOOL_CODE":10151,"TOOL_NAME":"百度","TOOL_DESC":null,"TOOL_URL":"http:www.baidu.com","USED_FLAG":1,"TOOL_SEQ":null,"NOTE":null,"DOCTOR_ID":3774}]
         */

        private int CUSTOMER_ID;
        private String DOCTOR_REAL_NAME;
        private String DOCTOR_SPECIALLY;
        private String DOCTOR_HOSPITAL;
        private String INTRODUCTION;
        private String ICON_DOCTOR_PICTURE;
        private String WORK_LOCATION;
        private String WORK_LOCATION_DESC;
        private String OFFICE_NAME;
        private String TITLE_NAME;
        private String BIG_ICON_BACKGROUND;
        private String qrCodeUrl;
        private int MEMBER_TYPE;
        private int isFollow;
        private EvaluateBean evaluate;
        private String roleType;
        private List<SiteDescBean> siteDesc;
        private List<SiteServiceBean> siteService;
        private List<DoctorServiceBean> doctorService;
        private List<ToolsBean> tools;

        public String getQrCodeUrl() {
            return qrCodeUrl;
        }

        public void setQrCodeUrl(String qrCodeUrl) {
            this.qrCodeUrl = qrCodeUrl;
        }

        public int getCUSTOMER_ID() {
            return CUSTOMER_ID;
        }

        public void setCUSTOMER_ID(int CUSTOMER_ID) {
            this.CUSTOMER_ID = CUSTOMER_ID;
        }

        public String getDOCTOR_REAL_NAME() {
            return DOCTOR_REAL_NAME;
        }

        public void setDOCTOR_REAL_NAME(String DOCTOR_REAL_NAME) {
            this.DOCTOR_REAL_NAME = DOCTOR_REAL_NAME;
        }

        public String getDOCTOR_SPECIALLY() {
            return DOCTOR_SPECIALLY;
        }

        public void setDOCTOR_SPECIALLY(String DOCTOR_SPECIALLY) {
            this.DOCTOR_SPECIALLY = DOCTOR_SPECIALLY;
        }

        public String getDOCTOR_HOSPITAL() {
            return DOCTOR_HOSPITAL;
        }

        public void setDOCTOR_HOSPITAL(String DOCTOR_HOSPITAL) {
            this.DOCTOR_HOSPITAL = DOCTOR_HOSPITAL;
        }

        public String getINTRODUCTION() {
            return INTRODUCTION;
        }

        public void setINTRODUCTION(String INTRODUCTION) {
            this.INTRODUCTION = INTRODUCTION;
        }

        public String getICON_DOCTOR_PICTURE() {
            return ICON_DOCTOR_PICTURE;
        }

        public void setICON_DOCTOR_PICTURE(String ICON_DOCTOR_PICTURE) {
            this.ICON_DOCTOR_PICTURE = ICON_DOCTOR_PICTURE;
        }

        public String getWORK_LOCATION() {
            return WORK_LOCATION;
        }

        public void setWORK_LOCATION(String WORK_LOCATION) {
            this.WORK_LOCATION = WORK_LOCATION;
        }

        public String getWORK_LOCATION_DESC() {
            return WORK_LOCATION_DESC;
        }

        public void setWORK_LOCATION_DESC(String WORK_LOCATION_DESC) {
            this.WORK_LOCATION_DESC = WORK_LOCATION_DESC;
        }

        public String getOFFICE_NAME() {
            return OFFICE_NAME;
        }

        public void setOFFICE_NAME(String OFFICE_NAME) {
            this.OFFICE_NAME = OFFICE_NAME;
        }

        public String getTITLE_NAME() {
            return TITLE_NAME;
        }

        public void setTITLE_NAME(String TITLE_NAME) {
            this.TITLE_NAME = TITLE_NAME;
        }

        public String getBIG_ICON_BACKGROUND() {
            return BIG_ICON_BACKGROUND;
        }

        public void setBIG_ICON_BACKGROUND(String BIG_ICON_BACKGROUND) {
            this.BIG_ICON_BACKGROUND = BIG_ICON_BACKGROUND;
        }

        public int getMEMBER_TYPE() {
            return MEMBER_TYPE;
        }

        public void setMEMBER_TYPE(int MEMBER_TYPE) {
            this.MEMBER_TYPE = MEMBER_TYPE;
        }

        public int getIsFollow() {
            return isFollow;
        }

        public void setIsFollow(int isFollow) {
            this.isFollow = isFollow;
        }

        public EvaluateBean getEvaluate() {
            return evaluate;
        }

        public void setEvaluate(EvaluateBean evaluate) {
            this.evaluate = evaluate;
        }

        public String getRoleType() {
            return roleType;
        }

        public void setRoleType(String roleType) {
            this.roleType = roleType;
        }

        public List<SiteDescBean> getSiteDesc() {
            return siteDesc;
        }

        public void setSiteDesc(List<SiteDescBean> siteDesc) {
            this.siteDesc = siteDesc;
        }

        public List<SiteServiceBean> getSiteService() {
            return siteService;
        }

        public void setSiteService(List<SiteServiceBean> siteService) {
            this.siteService = siteService;
        }

        public List<DoctorServiceBean> getDoctorService() {
            return doctorService;
        }

        public void setDoctorService(List<DoctorServiceBean> doctorService) {
            this.doctorService = doctorService;
        }

        public List<ToolsBean> getTools() {
            return tools;
        }

        public void setTools(List<ToolsBean> tools) {
            this.tools = tools;
        }

        public static class EvaluateBean {
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
             * REAL_NAME : 小周
             * RN : 1
             */

            private int EVALUATE_ID;
            private int CONSULTATION_ID;
            private int DOCTOR_ID;
            private int CUSTOMER_ID;
            private float EVALUATE_LEVEL;
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
            private String REAL_NAME;
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

            public float getEVALUATE_LEVEL() {
                return EVALUATE_LEVEL;
            }

            public void setEVALUATE_LEVEL(float EVALUATE_LEVEL) {
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

            public String getREAL_NAME() {
                return REAL_NAME;
            }

            public void setREAL_NAME(String REAL_NAME) {
                this.REAL_NAME = REAL_NAME;
            }

            public int getRN() {
                return RN;
            }

            public void setRN(int RN) {
                this.RN = RN;
            }
        }

        public static class SiteDescBean {
            /**
             * SITE_DESC : The new version of a great game that is so fun I would love it more if you could see how it works with it to play with friends or play with me and play with me every time you get it I will send you my email and you will see what you have and I will get you some of my stuff and I’ll get you a new phone number so you
             * SITE_NAME : 测试卷
             * MEMBER_NUM : 2
             * ORDER_NUM : 0
             */

            private String SITE_DESC;
            private String SITE_NAME;
            private int MEMBER_NUM;
            private int ORDER_NUM;

            public String getSITE_DESC() {
                return SITE_DESC;
            }

            public void setSITE_DESC(String SITE_DESC) {
                this.SITE_DESC = SITE_DESC;
            }

            public String getSITE_NAME() {
                return SITE_NAME;
            }

            public void setSITE_NAME(String SITE_NAME) {
                this.SITE_NAME = SITE_NAME;
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
             * SITE_ID : 345
             * SERVICE_TYPE_ID : 3
             * ORDER_ON_OFF : 1
             * SERVICE_PRICE : 3
             * SERVICE_TIME_LIMIT : null
             * SERVICE_CREATE_TIME : 20180425173738
             * SERVICE_CREATOR : 3774
             * SERVICE_ITEM_ID : 44006
             * SITE_NAME : 测试卷
             * ICON_DOCTOR_PICTURE : /CusZiYuan/resources/3774/doctorPic/20160712133251.png
             * ORDER_NUM : 0
             * DATA : {"ISBUZY":"0","MERCHANT_ID":null,"CUSTOMER_ID":3774,"SERVICE_ITEM_ID":44006,"SERVICE_ITEM_NAME":null,"SERVICE_TYPE_ID":3,"SERVICE_TYPE_SUB_ID":8,"ORDER_ON_OFF":1,"ORDER_ON_OFF_TIME":"20170607171643","SERVICE_MAX":55,"SERVICE_PRICE":"0.00","SERVICE_PLACE":null,"SERVICE_ITEM_DESC":null,"SERVICE_HOUR":7,"SERVICE_TIME_BEGIN":"20180502080000","SERVICE_TIME_END":"20180502150000","ORDER_CURRENT":0,"REPEAT_FLAG":2,"REPEAT_BATCH":7006,"WEEK":3,"REPEAT_START_DATE":"20170703","REPEAT_END_DATE":"99999999","REPEAT_START_TIME":"080000","REPEAT_END_TIME":"150000","USED_FLAG":1,"SERVICE_CREATE_TIME":"20180402000002","CYCLE_CODE":null,"FREE_MEDICAL_FLAG":0,"FREE_MEDICAL_PRICE":"0.00","FREE_MEDICAL_START_TIME":null,"FREE_MEDICAL_END_TIME":null}
             */

            private int SITE_ID;
            private int SERVICE_TYPE_ID;
            private int ORDER_ON_OFF;
            private float SERVICE_PRICE;
            private Object SERVICE_TIME_LIMIT;
            private String SERVICE_CREATE_TIME;
            private String SERVICE_CREATOR;
            private int SERVICE_ITEM_ID;
            private String SITE_NAME;
            private String ICON_DOCTOR_PICTURE;
            private int ORDER_NUM;
            private DATABean DATA;

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

            public Object getSERVICE_TIME_LIMIT() {
                return SERVICE_TIME_LIMIT;
            }

            public void setSERVICE_TIME_LIMIT(Object SERVICE_TIME_LIMIT) {
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

            public int getSERVICE_ITEM_ID() {
                return SERVICE_ITEM_ID;
            }

            public void setSERVICE_ITEM_ID(int SERVICE_ITEM_ID) {
                this.SERVICE_ITEM_ID = SERVICE_ITEM_ID;
            }

            public String getSITE_NAME() {
                return SITE_NAME;
            }

            public void setSITE_NAME(String SITE_NAME) {
                this.SITE_NAME = SITE_NAME;
            }

            public String getICON_DOCTOR_PICTURE() {
                return ICON_DOCTOR_PICTURE;
            }

            public void setICON_DOCTOR_PICTURE(String ICON_DOCTOR_PICTURE) {
                this.ICON_DOCTOR_PICTURE = ICON_DOCTOR_PICTURE;
            }

            public int getORDER_NUM() {
                return ORDER_NUM;
            }

            public void setORDER_NUM(int ORDER_NUM) {
                this.ORDER_NUM = ORDER_NUM;
            }

            public DATABean getDATA() {
                return DATA;
            }

            public void setDATA(DATABean DATA) {
                this.DATA = DATA;
            }

            public static class DATABean {
                /**
                 * ISBUZY : 0
                 * MERCHANT_ID : null
                 * CUSTOMER_ID : 3774
                 * SERVICE_ITEM_ID : 44006
                 * SERVICE_ITEM_NAME : null
                 * SERVICE_TYPE_ID : 3
                 * SERVICE_TYPE_SUB_ID : 8
                 * ORDER_ON_OFF : 1
                 * ORDER_ON_OFF_TIME : 20170607171643
                 * SERVICE_MAX : 55
                 * SERVICE_PRICE : 0.00
                 * SERVICE_PLACE : null
                 * SERVICE_ITEM_DESC : null
                 * SERVICE_HOUR : 7
                 * SERVICE_TIME_BEGIN : 20180502080000
                 * SERVICE_TIME_END : 20180502150000
                 * ORDER_CURRENT : 0
                 * REPEAT_FLAG : 2
                 * REPEAT_BATCH : 7006
                 * WEEK : 3
                 * REPEAT_START_DATE : 20170703
                 * REPEAT_END_DATE : 99999999
                 * REPEAT_START_TIME : 080000
                 * REPEAT_END_TIME : 150000
                 * USED_FLAG : 1
                 * SERVICE_CREATE_TIME : 20180402000002
                 * CYCLE_CODE : null
                 * FREE_MEDICAL_FLAG : 0
                 * FREE_MEDICAL_PRICE : 0.00
                 * FREE_MEDICAL_START_TIME : null
                 * FREE_MEDICAL_END_TIME : null
                 */

                private String ISBUZY;
                private Object MERCHANT_ID;
                private int CUSTOMER_ID;
                private int SERVICE_ITEM_ID;
                private Object SERVICE_ITEM_NAME;
                private int SERVICE_TYPE_ID;
                private int SERVICE_TYPE_SUB_ID;
                private int ORDER_ON_OFF;
                private String ORDER_ON_OFF_TIME;
                private int SERVICE_MAX;
                private String SERVICE_PRICE;
                private Object SERVICE_PLACE;
                private Object SERVICE_ITEM_DESC;
                private int SERVICE_HOUR;
                private String SERVICE_TIME_BEGIN;
                private String SERVICE_TIME_END;
                private int ORDER_CURRENT;
                private int REPEAT_FLAG;
                private int REPEAT_BATCH;
                private int WEEK;
                private String REPEAT_START_DATE;
                private String REPEAT_END_DATE;
                private String REPEAT_START_TIME;
                private String REPEAT_END_TIME;
                private int USED_FLAG;
                private String SERVICE_CREATE_TIME;
                private Object CYCLE_CODE;
                private int FREE_MEDICAL_FLAG;
                private String FREE_MEDICAL_PRICE;
                private Object FREE_MEDICAL_START_TIME;
                private Object FREE_MEDICAL_END_TIME;

                public String getISBUZY() {
                    return ISBUZY;
                }

                public void setISBUZY(String ISBUZY) {
                    this.ISBUZY = ISBUZY;
                }

                public Object getMERCHANT_ID() {
                    return MERCHANT_ID;
                }

                public void setMERCHANT_ID(Object MERCHANT_ID) {
                    this.MERCHANT_ID = MERCHANT_ID;
                }

                public int getCUSTOMER_ID() {
                    return CUSTOMER_ID;
                }

                public void setCUSTOMER_ID(int CUSTOMER_ID) {
                    this.CUSTOMER_ID = CUSTOMER_ID;
                }

                public int getSERVICE_ITEM_ID() {
                    return SERVICE_ITEM_ID;
                }

                public void setSERVICE_ITEM_ID(int SERVICE_ITEM_ID) {
                    this.SERVICE_ITEM_ID = SERVICE_ITEM_ID;
                }

                public Object getSERVICE_ITEM_NAME() {
                    return SERVICE_ITEM_NAME;
                }

                public void setSERVICE_ITEM_NAME(Object SERVICE_ITEM_NAME) {
                    this.SERVICE_ITEM_NAME = SERVICE_ITEM_NAME;
                }

                public int getSERVICE_TYPE_ID() {
                    return SERVICE_TYPE_ID;
                }

                public void setSERVICE_TYPE_ID(int SERVICE_TYPE_ID) {
                    this.SERVICE_TYPE_ID = SERVICE_TYPE_ID;
                }

                public int getSERVICE_TYPE_SUB_ID() {
                    return SERVICE_TYPE_SUB_ID;
                }

                public void setSERVICE_TYPE_SUB_ID(int SERVICE_TYPE_SUB_ID) {
                    this.SERVICE_TYPE_SUB_ID = SERVICE_TYPE_SUB_ID;
                }

                public int getORDER_ON_OFF() {
                    return ORDER_ON_OFF;
                }

                public void setORDER_ON_OFF(int ORDER_ON_OFF) {
                    this.ORDER_ON_OFF = ORDER_ON_OFF;
                }

                public String getORDER_ON_OFF_TIME() {
                    return ORDER_ON_OFF_TIME;
                }

                public void setORDER_ON_OFF_TIME(String ORDER_ON_OFF_TIME) {
                    this.ORDER_ON_OFF_TIME = ORDER_ON_OFF_TIME;
                }

                public int getSERVICE_MAX() {
                    return SERVICE_MAX;
                }

                public void setSERVICE_MAX(int SERVICE_MAX) {
                    this.SERVICE_MAX = SERVICE_MAX;
                }

                public String getSERVICE_PRICE() {
                    return SERVICE_PRICE;
                }

                public void setSERVICE_PRICE(String SERVICE_PRICE) {
                    this.SERVICE_PRICE = SERVICE_PRICE;
                }

                public Object getSERVICE_PLACE() {
                    return SERVICE_PLACE;
                }

                public void setSERVICE_PLACE(Object SERVICE_PLACE) {
                    this.SERVICE_PLACE = SERVICE_PLACE;
                }

                public Object getSERVICE_ITEM_DESC() {
                    return SERVICE_ITEM_DESC;
                }

                public void setSERVICE_ITEM_DESC(Object SERVICE_ITEM_DESC) {
                    this.SERVICE_ITEM_DESC = SERVICE_ITEM_DESC;
                }

                public int getSERVICE_HOUR() {
                    return SERVICE_HOUR;
                }

                public void setSERVICE_HOUR(int SERVICE_HOUR) {
                    this.SERVICE_HOUR = SERVICE_HOUR;
                }

                public String getSERVICE_TIME_BEGIN() {
                    return SERVICE_TIME_BEGIN;
                }

                public void setSERVICE_TIME_BEGIN(String SERVICE_TIME_BEGIN) {
                    this.SERVICE_TIME_BEGIN = SERVICE_TIME_BEGIN;
                }

                public String getSERVICE_TIME_END() {
                    return SERVICE_TIME_END;
                }

                public void setSERVICE_TIME_END(String SERVICE_TIME_END) {
                    this.SERVICE_TIME_END = SERVICE_TIME_END;
                }

                public int getORDER_CURRENT() {
                    return ORDER_CURRENT;
                }

                public void setORDER_CURRENT(int ORDER_CURRENT) {
                    this.ORDER_CURRENT = ORDER_CURRENT;
                }

                public int getREPEAT_FLAG() {
                    return REPEAT_FLAG;
                }

                public void setREPEAT_FLAG(int REPEAT_FLAG) {
                    this.REPEAT_FLAG = REPEAT_FLAG;
                }

                public int getREPEAT_BATCH() {
                    return REPEAT_BATCH;
                }

                public void setREPEAT_BATCH(int REPEAT_BATCH) {
                    this.REPEAT_BATCH = REPEAT_BATCH;
                }

                public int getWEEK() {
                    return WEEK;
                }

                public void setWEEK(int WEEK) {
                    this.WEEK = WEEK;
                }

                public String getREPEAT_START_DATE() {
                    return REPEAT_START_DATE;
                }

                public void setREPEAT_START_DATE(String REPEAT_START_DATE) {
                    this.REPEAT_START_DATE = REPEAT_START_DATE;
                }

                public String getREPEAT_END_DATE() {
                    return REPEAT_END_DATE;
                }

                public void setREPEAT_END_DATE(String REPEAT_END_DATE) {
                    this.REPEAT_END_DATE = REPEAT_END_DATE;
                }

                public String getREPEAT_START_TIME() {
                    return REPEAT_START_TIME;
                }

                public void setREPEAT_START_TIME(String REPEAT_START_TIME) {
                    this.REPEAT_START_TIME = REPEAT_START_TIME;
                }

                public String getREPEAT_END_TIME() {
                    return REPEAT_END_TIME;
                }

                public void setREPEAT_END_TIME(String REPEAT_END_TIME) {
                    this.REPEAT_END_TIME = REPEAT_END_TIME;
                }

                public int getUSED_FLAG() {
                    return USED_FLAG;
                }

                public void setUSED_FLAG(int USED_FLAG) {
                    this.USED_FLAG = USED_FLAG;
                }

                public String getSERVICE_CREATE_TIME() {
                    return SERVICE_CREATE_TIME;
                }

                public void setSERVICE_CREATE_TIME(String SERVICE_CREATE_TIME) {
                    this.SERVICE_CREATE_TIME = SERVICE_CREATE_TIME;
                }

                public Object getCYCLE_CODE() {
                    return CYCLE_CODE;
                }

                public void setCYCLE_CODE(Object CYCLE_CODE) {
                    this.CYCLE_CODE = CYCLE_CODE;
                }

                public int getFREE_MEDICAL_FLAG() {
                    return FREE_MEDICAL_FLAG;
                }

                public void setFREE_MEDICAL_FLAG(int FREE_MEDICAL_FLAG) {
                    this.FREE_MEDICAL_FLAG = FREE_MEDICAL_FLAG;
                }

                public String getFREE_MEDICAL_PRICE() {
                    return FREE_MEDICAL_PRICE;
                }

                public void setFREE_MEDICAL_PRICE(String FREE_MEDICAL_PRICE) {
                    this.FREE_MEDICAL_PRICE = FREE_MEDICAL_PRICE;
                }

                public Object getFREE_MEDICAL_START_TIME() {
                    return FREE_MEDICAL_START_TIME;
                }

                public void setFREE_MEDICAL_START_TIME(Object FREE_MEDICAL_START_TIME) {
                    this.FREE_MEDICAL_START_TIME = FREE_MEDICAL_START_TIME;
                }

                public Object getFREE_MEDICAL_END_TIME() {
                    return FREE_MEDICAL_END_TIME;
                }

                public void setFREE_MEDICAL_END_TIME(Object FREE_MEDICAL_END_TIME) {
                    this.FREE_MEDICAL_END_TIME = FREE_MEDICAL_END_TIME;
                }
            }
        }

        public static class DoctorServiceBean {
            /**
             * ORDER_ON_OFF : 1
             * SERVICE_ITEM_ID : 40332
             * SERVICE_PRICE : 1
             * SERVICE_TYPE_ID : 6
             * SERVICE_HOUR : 10
             * FREE_MEDICAL_FLAG : 1
             * FREE_MEDICAL_PRICE : 5
             * FREE_MEDICAL_START_TIME : 19170101000000
             * FREE_MEDICAL_END_TIME : 20181201000000
             * ORDER_NUM : 13
             */

            private int ORDER_ON_OFF;
            private int SERVICE_ITEM_ID;
            private float SERVICE_PRICE;
            private int SERVICE_TYPE_ID;
            private int SERVICE_HOUR;
            private int FREE_MEDICAL_FLAG;
            private float FREE_MEDICAL_PRICE;
            private String FREE_MEDICAL_START_TIME;
            private String FREE_MEDICAL_END_TIME;
            private int ORDER_NUM;

            public int getORDER_ON_OFF() {
                return ORDER_ON_OFF;
            }

            public void setORDER_ON_OFF(int ORDER_ON_OFF) {
                this.ORDER_ON_OFF = ORDER_ON_OFF;
            }

            public int getSERVICE_ITEM_ID() {
                return SERVICE_ITEM_ID;
            }

            public void setSERVICE_ITEM_ID(int SERVICE_ITEM_ID) {
                this.SERVICE_ITEM_ID = SERVICE_ITEM_ID;
            }

            public float getSERVICE_PRICE() {
                return SERVICE_PRICE;
            }

            public void setSERVICE_PRICE(int SERVICE_PRICE) {
                this.SERVICE_PRICE = SERVICE_PRICE;
            }

            public int getSERVICE_TYPE_ID() {
                return SERVICE_TYPE_ID;
            }

            public void setSERVICE_TYPE_ID(int SERVICE_TYPE_ID) {
                this.SERVICE_TYPE_ID = SERVICE_TYPE_ID;
            }

            public int getSERVICE_HOUR() {
                return SERVICE_HOUR;
            }

            public void setSERVICE_HOUR(int SERVICE_HOUR) {
                this.SERVICE_HOUR = SERVICE_HOUR;
            }

            public int getFREE_MEDICAL_FLAG() {
                return FREE_MEDICAL_FLAG;
            }

            public void setFREE_MEDICAL_FLAG(int FREE_MEDICAL_FLAG) {
                this.FREE_MEDICAL_FLAG = FREE_MEDICAL_FLAG;
            }

            public float getFREE_MEDICAL_PRICE() {
                return FREE_MEDICAL_PRICE;
            }

            public void setFREE_MEDICAL_PRICE(float FREE_MEDICAL_PRICE) {
                this.FREE_MEDICAL_PRICE = FREE_MEDICAL_PRICE;
            }

            public String getFREE_MEDICAL_START_TIME() {
                return FREE_MEDICAL_START_TIME;
            }

            public void setFREE_MEDICAL_START_TIME(String FREE_MEDICAL_START_TIME) {
                this.FREE_MEDICAL_START_TIME = FREE_MEDICAL_START_TIME;
            }

            public String getFREE_MEDICAL_END_TIME() {
                return FREE_MEDICAL_END_TIME;
            }

            public void setFREE_MEDICAL_END_TIME(String FREE_MEDICAL_END_TIME) {
                this.FREE_MEDICAL_END_TIME = FREE_MEDICAL_END_TIME;
            }

            public int getORDER_NUM() {
                return ORDER_NUM;
            }

            public void setORDER_NUM(int ORDER_NUM) {
                this.ORDER_NUM = ORDER_NUM;
            }
        }

        public static class ToolsBean {
            /**
             * CONSULTATION_CENTER_ID : 5
             * TOOL_CODE : 10151
             * TOOL_NAME : 百度
             * TOOL_DESC : null
             * TOOL_URL : http:www.baidu.com
             * USED_FLAG : 1
             * TOOL_SEQ : null
             * NOTE : null
             * DOCTOR_ID : 3774
             */

            private int CONSULTATION_CENTER_ID;
            private int TOOL_CODE;
            private String TOOL_NAME;
            private Object TOOL_DESC;
            private String TOOL_URL;
            private int USED_FLAG;
            private Object TOOL_SEQ;
            private Object NOTE;
            private int DOCTOR_ID;

            public int getCONSULTATION_CENTER_ID() {
                return CONSULTATION_CENTER_ID;
            }

            public void setCONSULTATION_CENTER_ID(int CONSULTATION_CENTER_ID) {
                this.CONSULTATION_CENTER_ID = CONSULTATION_CENTER_ID;
            }

            public int getTOOL_CODE() {
                return TOOL_CODE;
            }

            public void setTOOL_CODE(int TOOL_CODE) {
                this.TOOL_CODE = TOOL_CODE;
            }

            public String getTOOL_NAME() {
                return TOOL_NAME;
            }

            public void setTOOL_NAME(String TOOL_NAME) {
                this.TOOL_NAME = TOOL_NAME;
            }

            public Object getTOOL_DESC() {
                return TOOL_DESC;
            }

            public void setTOOL_DESC(Object TOOL_DESC) {
                this.TOOL_DESC = TOOL_DESC;
            }

            public String getTOOL_URL() {
                return TOOL_URL;
            }

            public void setTOOL_URL(String TOOL_URL) {
                this.TOOL_URL = TOOL_URL;
            }

            public int getUSED_FLAG() {
                return USED_FLAG;
            }

            public void setUSED_FLAG(int USED_FLAG) {
                this.USED_FLAG = USED_FLAG;
            }

            public Object getTOOL_SEQ() {
                return TOOL_SEQ;
            }

            public void setTOOL_SEQ(Object TOOL_SEQ) {
                this.TOOL_SEQ = TOOL_SEQ;
            }

            public Object getNOTE() {
                return NOTE;
            }

            public void setNOTE(Object NOTE) {
                this.NOTE = NOTE;
            }

            public int getDOCTOR_ID() {
                return DOCTOR_ID;
            }

            public void setDOCTOR_ID(int DOCTOR_ID) {
                this.DOCTOR_ID = DOCTOR_ID;
            }
        }
    }
}
