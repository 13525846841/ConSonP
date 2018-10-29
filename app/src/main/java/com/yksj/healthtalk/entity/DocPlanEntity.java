package com.yksj.healthtalk.entity;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by ${chen} on 2016/12/8.
 * 医教计划实体类
 */
public class DocPlanEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    private String CUSTOMER_ID;
    private String RELATIONSHIP;
    private String CHILDREN_HIGHT;
    private String CHILDREN_WEIGHT;
    private String CHILDREN_SCHOOL;
    private String CHILDREN_YEAR;
    private String HEAD_PORTRAIT_ICON;
    private String HEAD_PORTRAIT;
    private String CHILDREN_REMARK;
    private String CHILDREN_NAME;



    private String CHILDREN_ID;
    private String CHILDREN_SEX;





    public String getCHILDREN_ID() {
        return CHILDREN_ID;
    }

    public void setCHILDREN_ID(String CHILDREN_ID) {
        this.CHILDREN_ID = CHILDREN_ID;
    }



    public String getCHILDREN_NAME() {
        return CHILDREN_NAME;
    }

    public void setCHILDREN_NAME(String CHILDREN_NAME) {
        this.CHILDREN_NAME = CHILDREN_NAME;
    }

    public String getHEAD_PORTRAIT() {
        return HEAD_PORTRAIT;
    }

    public void setHEAD_PORTRAIT(String HEAD_PORTRAIT) {
        this.HEAD_PORTRAIT = HEAD_PORTRAIT;
    }

    private String CHILDREN_BIRTHDAY;

    public String getHEAD_PORTRAIT_ICON() {
        return HEAD_PORTRAIT_ICON;
    }

    public void setHEAD_PORTRAIT_ICON(String HEAD_PORTRAIT_ICON) {
        this.HEAD_PORTRAIT_ICON = HEAD_PORTRAIT_ICON;
    }

    public String getCHILDREN_BIRTHDAY() {
        return CHILDREN_BIRTHDAY;
    }

    public void setCHILDREN_BIRTHDAY(String CHILDREN_BIRTHDAY) {
        this.CHILDREN_BIRTHDAY = CHILDREN_BIRTHDAY;
    }



    public String getCHILDREN_YEAR() {
        return CHILDREN_YEAR;
    }

    public void setCHILDREN_YEAR(String CHILDREN_YEAR) {
        this.CHILDREN_YEAR = CHILDREN_YEAR;
    }

    public String getCHILDREN_SCHOOL() {
        return CHILDREN_SCHOOL;

    }

    public void setCHILDREN_SCHOOL(String CHILDREN_SCHOOL) {
        this.CHILDREN_SCHOOL = CHILDREN_SCHOOL;
    }


    public String getCUSTOMER_ID() {
        return CUSTOMER_ID;
    }

    public void setCUSTOMER_ID(String CUSTOMER_ID) {
        this.CUSTOMER_ID = CUSTOMER_ID;
    }

    public String getRELATIONSHIP() {
        return RELATIONSHIP;
    }

    public String getCHILDREN_WEIGHT() {
        return CHILDREN_WEIGHT;
    }

    public void setCHILDREN_WEIGHT(String CHILDREN_WEIGHT) {
        this.CHILDREN_WEIGHT = CHILDREN_WEIGHT;
    }

    public String getCHILDREN_HIGHT() {
        return CHILDREN_HIGHT;
    }

    public void setCHILDREN_HIGHT(String CHILDREN_HIGHT) {
        this.CHILDREN_HIGHT = CHILDREN_HIGHT;
    }

    public void setRELATIONSHIP(String RELATIONSHIP) {
        this.RELATIONSHIP = RELATIONSHIP;
    }

    public String getCHILDREN_SEX() {
        return CHILDREN_SEX;
    }

    public void setCHILDREN_SEX(String CHILDREN_SEX) {
        this.CHILDREN_SEX = CHILDREN_SEX;
    }

    public String getCHILDREN_REMARK() {
        return CHILDREN_REMARK;
    }

    public void setCHILDREN_REMARK(String CHILDREN_REMARK) {
        this.CHILDREN_REMARK = CHILDREN_REMARK;
    }
}
