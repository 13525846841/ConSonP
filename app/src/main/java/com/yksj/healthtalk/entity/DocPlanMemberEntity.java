package com.yksj.healthtalk.entity;

/**
 * Created by ${chen} on 2016/12/9.
 * 医教计划成员的 实体类
 */
public class DocPlanMemberEntity {
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public String getCustomer_remark() {
        return customer_remark;
    }

    public void setCustomer_remark(String customer_remark) {
        this.customer_remark = customer_remark;
    }

    public String getCREATOR_ID() {
        return CREATOR_ID;
    }

    public void setCREATOR_ID(String CREATOR_ID) {
        this.CREATOR_ID = CREATOR_ID;
    }


    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String sex;
    public String image;
    public String customer_id;
    public String customer_remark;
    public String CREATOR_ID;



}
