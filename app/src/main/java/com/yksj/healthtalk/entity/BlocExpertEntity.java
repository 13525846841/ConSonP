package com.yksj.healthtalk.entity;

import java.util.List;

/**
 * Created by hekl on 18/7/4.
 */

public class BlocExpertEntity {
    /**
     * code : 1
     * message : success
     * result : {"count":11,"list":[{"SITE_ID":300,"SITE_NAME":"666","OFFICE_ID":1024,"SITE_AREA":"340100","SITE_DESC":"88888","SITE_BIG_PIC":"/CusZiYuan/Voicechat/1/1//big1508139519094.png","SITE_SMALL_PIC":"/CusZiYuan/Voicechat/1/1//small1508139519094.png","SITE_CREATEOR":"124951","SITE_CR_TIME":"20171016153839","SITE_STATUS":20,"SITE_HOSPOTAL":null,"VISIT_TIME":613,"HOSPITAL_DESC":null,"SITE_CREATEOR_DESC":null,"ICON_DOCTOR_PICTURE":"/CusZiYuan/resources/124951/doctorPic/20170623131757.png","DOCTOR_NAME":"申昆玲","OFFICE_NAME":"儿科综合","MEMBER_NUM":2,"ORDER_NUM":62,"FOLLOW_COUNT":0,"RN":1},{"SITE_ID":366,"SITE_NAME":"5","OFFICE_ID":1024,"SITE_AREA":"5","SITE_DESC":"北京医院位于北京市东城区东单大华路1号，始建于1905年，占地面积 55800多平方米，建筑面积 227788平方米，是一所以干部医疗保健为中心、老年医学研究为重点，向社会全面开放的医、教、研、防全面发展的现代化综合性医院，是直属国家卫生和计划生育委员会的三级甲等医院，是中央重要的干部保健基地，长期承担着中央领导干部的医疗保健任务及15000余名司局级以上干部医疗保健任务和80余万参保人员的医疗保","SITE_BIG_PIC":"/CusZiYuan/Voicechat/1/1/3774/big1529117593569.jpg","SITE_SMALL_PIC":null,"SITE_CREATEOR":"3774","SITE_CR_TIME":"20180523144556","SITE_STATUS":20,"SITE_HOSPOTAL":"5","VISIT_TIME":331,"HOSPITAL_DESC":"北京医院位于北京市东城区东单大华路1号，始建于1905年，占地面积 55800多平方米，建筑面积 227788平方米，是一所以干部医疗保健为中心、老年医学研究为重点，向社会全面开放的医、教、研、防全面发展的现代化综合性医院，是直属国家卫生和计划生育委员会的三级甲等医院，是中央重要的干部保健基地，长期承担着中央领导干部的医疗保健任务及15000余名司局级以上干部医疗保健任务和80余万参保人员的医疗保","SITE_CREATEOR_DESC":"北京医院位于北京市东城区东单大华路1号，始建于1905年，占地面积 55800多平方米，建筑面积 227788平方米，是一所以干部医疗保健为中心、老年医学研究为重点，向社会全面开放的医、教、研、防全面发展的现代化综合性医院，是直属国家卫生和计划生育委员会的三级甲等医院，是中央重要的干部保健基地，长期承担着中央领导干部的医疗保健任务及15000余名司局级以上干部医疗保健任务和80余万参保人员的医疗保","ICON_DOCTOR_PICTURE":"/CusZiYuan/resources/3774/doctorPic/20160712133251.png","DOCTOR_NAME":"李安民","OFFICE_NAME":"儿科综合","MEMBER_NUM":3,"ORDER_NUM":2,"FOLLOW_COUNT":0,"RN":2},{"SITE_ID":354,"SITE_NAME":"长长的","OFFICE_ID":1,"SITE_AREA":"0","SITE_DESC":"11111111111111111111111111111","SITE_BIG_PIC":"/CusZiYuan/Voicechat/1/1//big1524813410930.png","SITE_SMALL_PIC":null,"SITE_CREATEOR":"3779","SITE_CR_TIME":"20180427151649","SITE_STATUS":20,"SITE_HOSPOTAL":"丰富的","VISIT_TIME":249,"HOSPITAL_DESC":"高丰富","SITE_CREATEOR_DESC":"冯丰富","ICON_DOCTOR_PICTURE":"/FalseDoctorPic/20150701.jpg","DOCTOR_NAME":"赵琴","OFFICE_NAME":"呼吸内科","MEMBER_NUM":3,"ORDER_NUM":1,"FOLLOW_COUNT":0,"RN":3},{"SITE_ID":260,"SITE_NAME":"3443","OFFICE_ID":1024,"SITE_AREA":"110101","SITE_DESC":"绝交快撒不复读特大beat特特不把徒步额度答复大大ADC徒步不饿吧嘚吧嘚八大饿了了饿不饿不cuteCAD不服大大","SITE_BIG_PIC":"/CusZiYuan/Voicechat/1/1/124972/big1528276278765.jpg","SITE_SMALL_PIC":"/CusZiYuan/Voicechat/1/1//small1505895360356.png","SITE_CREATEOR":"124972","SITE_CR_TIME":"20170920161600","SITE_STATUS":20,"SITE_HOSPOTAL":null,"VISIT_TIME":242,"HOSPITAL_DESC":null,"SITE_CREATEOR_DESC":null,"ICON_DOCTOR_PICTURE":"/CusZiYuan/resources/124972/doctorPic/doc_100_20170515111444.png","DOCTOR_NAME":"古桂雄","OFFICE_NAME":"儿科综合","MEMBER_NUM":2,"ORDER_NUM":1,"FOLLOW_COUNT":0,"RN":4},{"SITE_ID":210,"SITE_NAME":"记得记得","OFFICE_ID":1024,"SITE_AREA":"0","SITE_DESC":"就觉得","SITE_BIG_PIC":"/CusZiYuan/Voicechat/1/1/124984/big1501070297480.jpg","SITE_SMALL_PIC":"/CusZiYuan/Voicechat/1/1/124984/small1501070297480.jpg","SITE_CREATEOR":"124984","SITE_CR_TIME":"20170726195817","SITE_STATUS":20,"SITE_HOSPOTAL":null,"VISIT_TIME":140,"HOSPITAL_DESC":null,"SITE_CREATEOR_DESC":null,"ICON_DOCTOR_PICTURE":"/CusZiYuan/resources/124984/doctorPic/doc_100_20170628143128.png","DOCTOR_NAME":"周冰","OFFICE_NAME":"儿科综合","MEMBER_NUM":0,"ORDER_NUM":2,"FOLLOW_COUNT":0,"RN":5},{"SITE_ID":389,"SITE_NAME":"1","OFFICE_ID":1024,"SITE_AREA":"1","SITE_DESC":"呢Ella路P我哦兔兔pull天咯啦杀菩提天咯啦咯啦咯啦咯啦拉拉吧KKKKKK啦的默默拉粑粑is是his确实会根据他累988885544411","SITE_BIG_PIC":"/CusZiYuan/Voicechat/1/1/124875/big1528178011946.jpg","SITE_SMALL_PIC":null,"SITE_CREATEOR":"124875","SITE_CR_TIME":"20180523170545","SITE_STATUS":20,"SITE_HOSPOTAL":"1","VISIT_TIME":190,"HOSPITAL_DESC":"啦额可口可乐楼饿急了阿鲁得了😠😔😜😣😏😰😞😜😔😏😏😣😔😜😔😜😜😖😉😜😜😔😔😏😔😏😏😏😏😔😏😣😏😏😷😏😔😏😔😜😔😜😔😜😜😔😝😂😝😣😌😖😌😣😌😣😌😣😜😣😜😣啦额可口可乐楼饿急了阿鲁得了😠😔😜😣😏😰😞😜😔😏😏😣😔😜😔😜😜😖😉😜😜😔😔😏","SITE_CREATEOR_DESC":"1","ICON_DOCTOR_PICTURE":"/CusZiYuan/resources/124875/doctorPic/doc_100_20170727232045.png","DOCTOR_NAME":"徐荣谦","OFFICE_NAME":"儿科综合","MEMBER_NUM":1,"ORDER_NUM":0,"FOLLOW_COUNT":0,"RN":6},{"SITE_ID":130,"SITE_NAME":"嘿嘿","OFFICE_ID":1024,"SITE_AREA":"0","SITE_DESC":"哼哼唧唧你好嘎嘎嘎","SITE_BIG_PIC":"/app_java/sms_web/CusZiYuan/Voicechat/1/1/125089/big1500035739638.jpg","SITE_SMALL_PIC":"/app_java/sms_web/CusZiYuan/Voicechat/1/1/125089/small1500035739638.jpg","SITE_CREATEOR":"125089","SITE_CR_TIME":"20170714203539","SITE_STATUS":20,"SITE_HOSPOTAL":null,"VISIT_TIME":63,"HOSPITAL_DESC":null,"SITE_CREATEOR_DESC":null,"ICON_DOCTOR_PICTURE":"/CusZiYuan/resources/125089/doctorPic/doc_100_20150723111154.png","DOCTOR_NAME":"周智勇4","OFFICE_NAME":"儿科综合","MEMBER_NUM":1,"ORDER_NUM":0,"FOLLOW_COUNT":0,"RN":7},{"SITE_ID":400,"SITE_NAME":"三喵军团","OFFICE_ID":1002,"SITE_AREA":"110101","SITE_DESC":"我很快乐","SITE_BIG_PIC":"/CusZiYuan/Voicechat/1/1//big1529373494109.png","SITE_SMALL_PIC":null,"SITE_CREATEOR":"314340","SITE_CR_TIME":"20180619095813","SITE_STATUS":20,"SITE_HOSPOTAL":"三喵军团","VISIT_TIME":189,"HOSPITAL_DESC":"快乐足球","SITE_CREATEOR_DESC":"没头脑与不高兴","ICON_DOCTOR_PICTURE":"/CusZiYuan/resources/314340/doctorPic/doc_100_20170913142601.png","DOCTOR_NAME":"张伯伦","OFFICE_NAME":"小儿五官科","MEMBER_NUM":1,"ORDER_NUM":0,"FOLLOW_COUNT":0,"RN":8},{"SITE_ID":351,"SITE_NAME":"保持多久","OFFICE_ID":1001,"SITE_AREA":"0","SITE_DESC":"内急第九集","SITE_BIG_PIC":"/CusZiYuan/Voicechat/1/1//big1523876719390.jpg","SITE_SMALL_PIC":null,"SITE_CREATEOR":"3766","SITE_CR_TIME":"20180416190519","SITE_STATUS":20,"SITE_HOSPOTAL":"你星级酒店","VISIT_TIME":53,"HOSPITAL_DESC":"男的女的","SITE_CREATEOR_DESC":"你爹地","ICON_DOCTOR_PICTURE":"/CusZiYuan/resources/3766/doctorPic/20150422170017.png","DOCTOR_NAME":"黄东海","OFFICE_NAME":"小儿呼吸科","MEMBER_NUM":1,"ORDER_NUM":0,"FOLLOW_COUNT":0,"RN":9},{"SITE_ID":198,"SITE_NAME":"音乐","OFFICE_ID":1012,"SITE_AREA":"0","SITE_DESC":"大型音乐会","SITE_BIG_PIC":"/CusZiYuan/Voicechat/1/1/281874/big1500692612460.jpg","SITE_SMALL_PIC":"/CusZiYuan/Voicechat/1/1/281874/small1500692612460.jpg","SITE_CREATEOR":"281874","SITE_CR_TIME":"20170722110332","SITE_STATUS":20,"SITE_HOSPOTAL":"音乐会","VISIT_TIME":95,"HOSPITAL_DESC":"音乐会ss","SITE_CREATEOR_DESC":"音乐会creae","ICON_DOCTOR_PICTURE":"/CusZiYuan/resources/281874/doctorPic/doc_100_20170714101410.png","DOCTOR_NAME":"王亚豪","OFFICE_NAME":"小儿内科","MEMBER_NUM":1,"ORDER_NUM":0,"FOLLOW_COUNT":0,"RN":10}]}
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
         * count : 11
         * list : [{"SITE_ID":300,"SITE_NAME":"666","OFFICE_ID":1024,"SITE_AREA":"340100","SITE_DESC":"88888","SITE_BIG_PIC":"/CusZiYuan/Voicechat/1/1//big1508139519094.png","SITE_SMALL_PIC":"/CusZiYuan/Voicechat/1/1//small1508139519094.png","SITE_CREATEOR":"124951","SITE_CR_TIME":"20171016153839","SITE_STATUS":20,"SITE_HOSPOTAL":null,"VISIT_TIME":613,"HOSPITAL_DESC":null,"SITE_CREATEOR_DESC":null,"ICON_DOCTOR_PICTURE":"/CusZiYuan/resources/124951/doctorPic/20170623131757.png","DOCTOR_NAME":"申昆玲","OFFICE_NAME":"儿科综合","MEMBER_NUM":2,"ORDER_NUM":62,"FOLLOW_COUNT":0,"RN":1},{"SITE_ID":366,"SITE_NAME":"5","OFFICE_ID":1024,"SITE_AREA":"5","SITE_DESC":"北京医院位于北京市东城区东单大华路1号，始建于1905年，占地面积 55800多平方米，建筑面积 227788平方米，是一所以干部医疗保健为中心、老年医学研究为重点，向社会全面开放的医、教、研、防全面发展的现代化综合性医院，是直属国家卫生和计划生育委员会的三级甲等医院，是中央重要的干部保健基地，长期承担着中央领导干部的医疗保健任务及15000余名司局级以上干部医疗保健任务和80余万参保人员的医疗保","SITE_BIG_PIC":"/CusZiYuan/Voicechat/1/1/3774/big1529117593569.jpg","SITE_SMALL_PIC":null,"SITE_CREATEOR":"3774","SITE_CR_TIME":"20180523144556","SITE_STATUS":20,"SITE_HOSPOTAL":"5","VISIT_TIME":331,"HOSPITAL_DESC":"北京医院位于北京市东城区东单大华路1号，始建于1905年，占地面积 55800多平方米，建筑面积 227788平方米，是一所以干部医疗保健为中心、老年医学研究为重点，向社会全面开放的医、教、研、防全面发展的现代化综合性医院，是直属国家卫生和计划生育委员会的三级甲等医院，是中央重要的干部保健基地，长期承担着中央领导干部的医疗保健任务及15000余名司局级以上干部医疗保健任务和80余万参保人员的医疗保","SITE_CREATEOR_DESC":"北京医院位于北京市东城区东单大华路1号，始建于1905年，占地面积 55800多平方米，建筑面积 227788平方米，是一所以干部医疗保健为中心、老年医学研究为重点，向社会全面开放的医、教、研、防全面发展的现代化综合性医院，是直属国家卫生和计划生育委员会的三级甲等医院，是中央重要的干部保健基地，长期承担着中央领导干部的医疗保健任务及15000余名司局级以上干部医疗保健任务和80余万参保人员的医疗保","ICON_DOCTOR_PICTURE":"/CusZiYuan/resources/3774/doctorPic/20160712133251.png","DOCTOR_NAME":"李安民","OFFICE_NAME":"儿科综合","MEMBER_NUM":3,"ORDER_NUM":2,"FOLLOW_COUNT":0,"RN":2},{"SITE_ID":354,"SITE_NAME":"长长的","OFFICE_ID":1,"SITE_AREA":"0","SITE_DESC":"11111111111111111111111111111","SITE_BIG_PIC":"/CusZiYuan/Voicechat/1/1//big1524813410930.png","SITE_SMALL_PIC":null,"SITE_CREATEOR":"3779","SITE_CR_TIME":"20180427151649","SITE_STATUS":20,"SITE_HOSPOTAL":"丰富的","VISIT_TIME":249,"HOSPITAL_DESC":"高丰富","SITE_CREATEOR_DESC":"冯丰富","ICON_DOCTOR_PICTURE":"/FalseDoctorPic/20150701.jpg","DOCTOR_NAME":"赵琴","OFFICE_NAME":"呼吸内科","MEMBER_NUM":3,"ORDER_NUM":1,"FOLLOW_COUNT":0,"RN":3},{"SITE_ID":260,"SITE_NAME":"3443","OFFICE_ID":1024,"SITE_AREA":"110101","SITE_DESC":"绝交快撒不复读特大beat特特不把徒步额度答复大大ADC徒步不饿吧嘚吧嘚八大饿了了饿不饿不cuteCAD不服大大","SITE_BIG_PIC":"/CusZiYuan/Voicechat/1/1/124972/big1528276278765.jpg","SITE_SMALL_PIC":"/CusZiYuan/Voicechat/1/1//small1505895360356.png","SITE_CREATEOR":"124972","SITE_CR_TIME":"20170920161600","SITE_STATUS":20,"SITE_HOSPOTAL":null,"VISIT_TIME":242,"HOSPITAL_DESC":null,"SITE_CREATEOR_DESC":null,"ICON_DOCTOR_PICTURE":"/CusZiYuan/resources/124972/doctorPic/doc_100_20170515111444.png","DOCTOR_NAME":"古桂雄","OFFICE_NAME":"儿科综合","MEMBER_NUM":2,"ORDER_NUM":1,"FOLLOW_COUNT":0,"RN":4},{"SITE_ID":210,"SITE_NAME":"记得记得","OFFICE_ID":1024,"SITE_AREA":"0","SITE_DESC":"就觉得","SITE_BIG_PIC":"/CusZiYuan/Voicechat/1/1/124984/big1501070297480.jpg","SITE_SMALL_PIC":"/CusZiYuan/Voicechat/1/1/124984/small1501070297480.jpg","SITE_CREATEOR":"124984","SITE_CR_TIME":"20170726195817","SITE_STATUS":20,"SITE_HOSPOTAL":null,"VISIT_TIME":140,"HOSPITAL_DESC":null,"SITE_CREATEOR_DESC":null,"ICON_DOCTOR_PICTURE":"/CusZiYuan/resources/124984/doctorPic/doc_100_20170628143128.png","DOCTOR_NAME":"周冰","OFFICE_NAME":"儿科综合","MEMBER_NUM":0,"ORDER_NUM":2,"FOLLOW_COUNT":0,"RN":5},{"SITE_ID":389,"SITE_NAME":"1","OFFICE_ID":1024,"SITE_AREA":"1","SITE_DESC":"呢Ella路P我哦兔兔pull天咯啦杀菩提天咯啦咯啦咯啦咯啦拉拉吧KKKKKK啦的默默拉粑粑is是his确实会根据他累988885544411","SITE_BIG_PIC":"/CusZiYuan/Voicechat/1/1/124875/big1528178011946.jpg","SITE_SMALL_PIC":null,"SITE_CREATEOR":"124875","SITE_CR_TIME":"20180523170545","SITE_STATUS":20,"SITE_HOSPOTAL":"1","VISIT_TIME":190,"HOSPITAL_DESC":"啦额可口可乐楼饿急了阿鲁得了😠😔😜😣😏😰😞😜😔😏😏😣😔😜😔😜😜😖😉😜😜😔😔😏😔😏😏😏😏😔😏😣😏😏😷😏😔😏😔😜😔😜😔😜😜😔😝😂😝😣😌😖😌😣😌😣😌😣😜😣😜😣啦额可口可乐楼饿急了阿鲁得了😠😔😜😣😏😰😞😜😔😏😏😣😔😜😔😜😜😖😉😜😜😔😔😏","SITE_CREATEOR_DESC":"1","ICON_DOCTOR_PICTURE":"/CusZiYuan/resources/124875/doctorPic/doc_100_20170727232045.png","DOCTOR_NAME":"徐荣谦","OFFICE_NAME":"儿科综合","MEMBER_NUM":1,"ORDER_NUM":0,"FOLLOW_COUNT":0,"RN":6},{"SITE_ID":130,"SITE_NAME":"嘿嘿","OFFICE_ID":1024,"SITE_AREA":"0","SITE_DESC":"哼哼唧唧你好嘎嘎嘎","SITE_BIG_PIC":"/app_java/sms_web/CusZiYuan/Voicechat/1/1/125089/big1500035739638.jpg","SITE_SMALL_PIC":"/app_java/sms_web/CusZiYuan/Voicechat/1/1/125089/small1500035739638.jpg","SITE_CREATEOR":"125089","SITE_CR_TIME":"20170714203539","SITE_STATUS":20,"SITE_HOSPOTAL":null,"VISIT_TIME":63,"HOSPITAL_DESC":null,"SITE_CREATEOR_DESC":null,"ICON_DOCTOR_PICTURE":"/CusZiYuan/resources/125089/doctorPic/doc_100_20150723111154.png","DOCTOR_NAME":"周智勇4","OFFICE_NAME":"儿科综合","MEMBER_NUM":1,"ORDER_NUM":0,"FOLLOW_COUNT":0,"RN":7},{"SITE_ID":400,"SITE_NAME":"三喵军团","OFFICE_ID":1002,"SITE_AREA":"110101","SITE_DESC":"我很快乐","SITE_BIG_PIC":"/CusZiYuan/Voicechat/1/1//big1529373494109.png","SITE_SMALL_PIC":null,"SITE_CREATEOR":"314340","SITE_CR_TIME":"20180619095813","SITE_STATUS":20,"SITE_HOSPOTAL":"三喵军团","VISIT_TIME":189,"HOSPITAL_DESC":"快乐足球","SITE_CREATEOR_DESC":"没头脑与不高兴","ICON_DOCTOR_PICTURE":"/CusZiYuan/resources/314340/doctorPic/doc_100_20170913142601.png","DOCTOR_NAME":"张伯伦","OFFICE_NAME":"小儿五官科","MEMBER_NUM":1,"ORDER_NUM":0,"FOLLOW_COUNT":0,"RN":8},{"SITE_ID":351,"SITE_NAME":"保持多久","OFFICE_ID":1001,"SITE_AREA":"0","SITE_DESC":"内急第九集","SITE_BIG_PIC":"/CusZiYuan/Voicechat/1/1//big1523876719390.jpg","SITE_SMALL_PIC":null,"SITE_CREATEOR":"3766","SITE_CR_TIME":"20180416190519","SITE_STATUS":20,"SITE_HOSPOTAL":"你星级酒店","VISIT_TIME":53,"HOSPITAL_DESC":"男的女的","SITE_CREATEOR_DESC":"你爹地","ICON_DOCTOR_PICTURE":"/CusZiYuan/resources/3766/doctorPic/20150422170017.png","DOCTOR_NAME":"黄东海","OFFICE_NAME":"小儿呼吸科","MEMBER_NUM":1,"ORDER_NUM":0,"FOLLOW_COUNT":0,"RN":9},{"SITE_ID":198,"SITE_NAME":"音乐","OFFICE_ID":1012,"SITE_AREA":"0","SITE_DESC":"大型音乐会","SITE_BIG_PIC":"/CusZiYuan/Voicechat/1/1/281874/big1500692612460.jpg","SITE_SMALL_PIC":"/CusZiYuan/Voicechat/1/1/281874/small1500692612460.jpg","SITE_CREATEOR":"281874","SITE_CR_TIME":"20170722110332","SITE_STATUS":20,"SITE_HOSPOTAL":"音乐会","VISIT_TIME":95,"HOSPITAL_DESC":"音乐会ss","SITE_CREATEOR_DESC":"音乐会creae","ICON_DOCTOR_PICTURE":"/CusZiYuan/resources/281874/doctorPic/doc_100_20170714101410.png","DOCTOR_NAME":"王亚豪","OFFICE_NAME":"小儿内科","MEMBER_NUM":1,"ORDER_NUM":0,"FOLLOW_COUNT":0,"RN":10}]
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
             * VISIT_TIME : 613
             * HOSPITAL_DESC : null
             * SITE_CREATEOR_DESC : null
             * ICON_DOCTOR_PICTURE : /CusZiYuan/resources/124951/doctorPic/20170623131757.png
             * DOCTOR_NAME : 申昆玲
             * OFFICE_NAME : 儿科综合
             * MEMBER_NUM : 2
             * ORDER_NUM : 62
             * FOLLOW_COUNT : 0
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
            private Object SITE_HOSPOTAL;
            private int VISIT_TIME;
            private Object HOSPITAL_DESC;
            private Object SITE_CREATEOR_DESC;
            private String ICON_DOCTOR_PICTURE;
            private String DOCTOR_NAME;
            private String OFFICE_NAME;
            private int MEMBER_NUM;
            private int ORDER_NUM;
            private int FOLLOW_COUNT;
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

            public Object getSITE_HOSPOTAL() {
                return SITE_HOSPOTAL;
            }

            public void setSITE_HOSPOTAL(Object SITE_HOSPOTAL) {
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

            public int getFOLLOW_COUNT() {
                return FOLLOW_COUNT;
            }

            public void setFOLLOW_COUNT(int FOLLOW_COUNT) {
                this.FOLLOW_COUNT = FOLLOW_COUNT;
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
