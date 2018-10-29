package com.yksj.healthtalk.entity;

import java.util.List;

/**
 * Created by hekl on 18/4/26.
 */

public class SelectProvinceEntity
{
    /**
     * message : 查询成功
     * area : [{"AREA_CODE":"340000","AREA_NAME":"安徽省","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"1","NOTES":"31.52,117.17","PIN_YIN":"ahs","AREA_NAME2":"安徽"},{"AREA_CODE":"350000","AREA_NAME":"福建省","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"0","NOTES":"26.05,119.18","PIN_YIN":"fjs","AREA_NAME2":"福建"},{"AREA_CODE":"360000","AREA_NAME":"江西省","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"0","NOTES":"28.40,115.55","PIN_YIN":"jxs","AREA_NAME2":"江西"},{"AREA_CODE":"370000","AREA_NAME":"山东省","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"0","NOTES":"36.40,117.00","PIN_YIN":"sds","AREA_NAME2":"山东"},{"AREA_CODE":"410000","AREA_NAME":"河南省","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"0","NOTES":"34.46,113.40","PIN_YIN":"hns","AREA_NAME2":"河南"},{"AREA_CODE":"420000","AREA_NAME":"湖北省","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"0","NOTES":"30.35,114.17","PIN_YIN":"hbs","AREA_NAME2":"湖北"},{"AREA_CODE":"150000","AREA_NAME":"内蒙古自治区","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"0","NOTES":"40.48,111.41","PIN_YIN":"nmgzzq","AREA_NAME2":"内蒙古"},{"AREA_CODE":"210000","AREA_NAME":"辽宁省","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"0","NOTES":"41.48,123.25","PIN_YIN":"lns","AREA_NAME2":"辽宁"},{"AREA_CODE":"310000","AREA_NAME":"上海市","SUB_AREA_CODE":"0","AREA_LEVEL":"直辖市","VALID_FLAG":"1","NOTES":"31.14,121.29","PIN_YIN":"shs","AREA_NAME2":"上海"},{"AREA_CODE":"320000","AREA_NAME":"江苏省","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"1","NOTES":"32.03,118.46","PIN_YIN":"jss","AREA_NAME2":"江苏"},{"AREA_CODE":"330000","AREA_NAME":"浙江省","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"0","NOTES":"30.16,120.10","PIN_YIN":"zjs","AREA_NAME2":"浙江"},{"AREA_CODE":"220000","AREA_NAME":"吉林省","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"0","NOTES":"43.54,125.19","PIN_YIN":"jls","AREA_NAME2":"吉林"},{"AREA_CODE":"230000","AREA_NAME":"黑龙江省","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"0","NOTES":"45.44,126.36","PIN_YIN":"hljs","AREA_NAME2":"黑龙江"},{"AREA_CODE":"430000","AREA_NAME":"湖南省","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"0","NOTES":"28.12,112.59","PIN_YIN":"hns","AREA_NAME2":"湖南"},{"AREA_CODE":"110000","AREA_NAME":"北京市","SUB_AREA_CODE":"0","AREA_LEVEL":"直辖市","VALID_FLAG":"1","NOTES":"39.55,116.24","PIN_YIN":"bjs","AREA_NAME2":"北京"},{"AREA_CODE":"120000","AREA_NAME":"天津市","SUB_AREA_CODE":"0","AREA_LEVEL":"直辖市","VALID_FLAG":"0","NOTES":"39.02,117.12","PIN_YIN":"tjs","AREA_NAME2":"天津"},{"AREA_CODE":"130000","AREA_NAME":"河北省","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"0","NOTES":"38.02,114.30","PIN_YIN":"hbs","AREA_NAME2":"河北"},{"AREA_CODE":"140000","AREA_NAME":"山西省","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"0","NOTES":"37.54,112.33","PIN_YIN":"sxs","AREA_NAME2":"山西"},{"AREA_CODE":"440000","AREA_NAME":"广东省","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"1","NOTES":"23.08,113.14","PIN_YIN":"gds","AREA_NAME2":"广东"},{"AREA_CODE":"530000","AREA_NAME":"云南省","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"0","NOTES":"25.04,102.42","PIN_YIN":"yns","AREA_NAME2":"云南"},{"AREA_CODE":"610000","AREA_NAME":"陕西省","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"0","NOTES":"34.17,108.57","PIN_YIN":"sxs","AREA_NAME2":"陕西"},{"AREA_CODE":"540000","AREA_NAME":"西藏自治区","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"0","NOTES":"29.39,91.08","PIN_YIN":"xczzq","AREA_NAME2":"西藏"},{"AREA_CODE":"620000","AREA_NAME":"甘肃省","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"0","NOTES":"36.04,103.51","PIN_YIN":"gss","AREA_NAME2":"甘肃"},{"AREA_CODE":"630000","AREA_NAME":"青海省","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"0","NOTES":"36.38,101.48","PIN_YIN":"qhs","AREA_NAME2":"青海"},{"AREA_CODE":"640000","AREA_NAME":"宁夏回族自治区","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"0","NOTES":"38.27,106.16","PIN_YIN":"nxhzzzq","AREA_NAME2":"宁夏"},{"AREA_CODE":"650000","AREA_NAME":"新疆维吾尔自治区","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"0","NOTES":"43.45,87.36","PIN_YIN":"xjwwezzq","AREA_NAME2":"新疆"},{"AREA_CODE":"660000","AREA_NAME":"香港特别行政区","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"0","NOTES":"21.23,115.12","PIN_YIN":"xgtbxzq","AREA_NAME2":"香港"},{"AREA_CODE":"670000","AREA_NAME":"澳门特别行政区","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"0","NOTES":"21.33,115.07","PIN_YIN":"amtbxzq","AREA_NAME2":"澳门"},{"AREA_CODE":"680000","AREA_NAME":"台湾省","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"0","NOTES":"25.03,121.30","PIN_YIN":"tws","AREA_NAME2":"台湾"},{"AREA_CODE":"450000","AREA_NAME":"广西壮族自治区","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"0","NOTES":"22.48,108.19","PIN_YIN":"gxzzzzq","AREA_NAME2":"广西"},{"AREA_CODE":"500000","AREA_NAME":"重庆市","SUB_AREA_CODE":"0","AREA_LEVEL":"直辖市","VALID_FLAG":"0","NOTES":"29.35,106.33","PIN_YIN":"zqs","AREA_NAME2":"重庆"},{"AREA_CODE":"460000","AREA_NAME":"海南省","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"0","NOTES":"20.02,110.20","PIN_YIN":"hns","AREA_NAME2":"海南"},{"AREA_CODE":"510000","AREA_NAME":"四川省","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"0","NOTES":"30.40,104.04","PIN_YIN":"scs","AREA_NAME2":"四川"},{"AREA_CODE":"520000","AREA_NAME":"贵州省","SUB_AREA_CODE":"0","AREA_LEVEL":"省","VALID_FLAG":"0","NOTES":"26.35,106.42","PIN_YIN":"gzs","AREA_NAME2":"贵州"}]
     * code : 0
     */

    private String message;
    private int code;
    private List<AreaBean> area;

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

    public List<AreaBean> getArea() {
        return area;
    }

    public void setArea(List<AreaBean> area) {
        this.area = area;
    }

    public static class AreaBean {
        /**
         * AREA_CODE : 340000
         * AREA_NAME : 安徽省
         * SUB_AREA_CODE : 0
         * AREA_LEVEL : 省
         * VALID_FLAG : 1
         * NOTES : 31.52,117.17
         * PIN_YIN : ahs
         * AREA_NAME2 : 安徽
         */

        private String AREA_CODE;
        private String AREA_NAME;
        private String SUB_AREA_CODE;
        private String AREA_LEVEL;
        private String VALID_FLAG;
        private String NOTES;
        private String PIN_YIN;
        private String AREA_NAME2;

        public String getAREA_CODE() {
            return AREA_CODE;
        }

        public void setAREA_CODE(String AREA_CODE) {
            this.AREA_CODE = AREA_CODE;
        }

        public String getAREA_NAME() {
            return AREA_NAME;
        }

        public void setAREA_NAME(String AREA_NAME) {
            this.AREA_NAME = AREA_NAME;
        }

        public String getSUB_AREA_CODE() {
            return SUB_AREA_CODE;
        }

        public void setSUB_AREA_CODE(String SUB_AREA_CODE) {
            this.SUB_AREA_CODE = SUB_AREA_CODE;
        }

        public String getAREA_LEVEL() {
            return AREA_LEVEL;
        }

        public void setAREA_LEVEL(String AREA_LEVEL) {
            this.AREA_LEVEL = AREA_LEVEL;
        }

        public String getVALID_FLAG() {
            return VALID_FLAG;
        }

        public void setVALID_FLAG(String VALID_FLAG) {
            this.VALID_FLAG = VALID_FLAG;
        }

        public String getNOTES() {
            return NOTES;
        }

        public void setNOTES(String NOTES) {
            this.NOTES = NOTES;
        }

        public String getPIN_YIN() {
            return PIN_YIN;
        }

        public void setPIN_YIN(String PIN_YIN) {
            this.PIN_YIN = PIN_YIN;
        }

        public String getAREA_NAME2() {
            return AREA_NAME2;
        }

        public void setAREA_NAME2(String AREA_NAME2) {
            this.AREA_NAME2 = AREA_NAME2;
        }
    }
}
