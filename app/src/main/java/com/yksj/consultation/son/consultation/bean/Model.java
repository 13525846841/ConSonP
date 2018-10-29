package com.yksj.consultation.son.consultation.bean;

/**
 * 找专家的功能界面的按科室找专家的grid view的实体类
 */
public class Model {
    public String name;
    //public String iconRes;
    public String iconRes;
    public String office_id;

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

    public void setIconRes(String iconRes) {
        this.iconRes = iconRes;
    }

    public Model(){}

    //    public Model(String name, String iconRes) {
//        this.name = name;
//        this.iconRes = iconRes;
//    }
    public Model(String name,String iconRes){
        this.name = name;
        this.iconRes = iconRes;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getIconRes() {
//        return iconRes;
//    }
//
//    public void setIconRes(String iconRes) {
//        this.iconRes = iconRes;
//    }
}