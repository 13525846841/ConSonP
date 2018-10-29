package com.yksj.healthtalk.bean;

import java.io.Serializable;

/**
 * 医生实体---简单数据
 * Created by lmk on 15/9/18.
 *
 * DOCTOR_REAL_NAME  	姓名
 DOCTOR_SPECIALLY  	专长
 ICON_DOCTOR_PICTURE 	小头像
 DOCTOR_PICTURE		大头像
 INTRODUCTION		简介
 SERVICE_PRICE		价格
 UNIT_NAME		医院名称
 OFFICE_NAME		科室名称
 TITLE_NAME		职称名称
 NUMS			剩余名额
 isrecommnd     是否推荐
 *
 */
public class DoctorSimpleBean implements Serializable{

    public int R;
    public String DOCTOR_REAL_NAME;
    public String CUSTOMER_ID;
    public String DOCTOR_SPECIALLY;
    public String ICON_DOCTOR_PICTURE;
    public String DOCTOR_PICTURE;

    public String getNUMS() {
        return NUMS;
    }

    public int getR() {
        return R;
    }

    public void setR(int r) {
        R = r;
    }

    public void setNUMS(String NUMS) {

        this.NUMS = NUMS;
    }

    public String INTRODUCTION;
    public String SERVICE_PRICE;
    public String UNIT_NAME;

    public String ISRECOMMND;

    public String getISRECOMMNED(){
        return ISRECOMMND;
    }
    public void setISRECOMMNED(String ISRECOMMND){
        this.ISRECOMMND = ISRECOMMND;
    }

    public String getUNIT_NAME() {
        return UNIT_NAME;
    }

    public void setUNIT_NAME(String UNIT_NAME) {
        this.UNIT_NAME = UNIT_NAME;
    }

    public String getSERVICE_PRICE() {
        return SERVICE_PRICE;
    }

    public void setSERVICE_PRICE(String SERVICE_PRICE) {
        this.SERVICE_PRICE = SERVICE_PRICE;
    }

    public String getCUSTOMER_ID() {
        return CUSTOMER_ID;

    }

    public String getINTRODUCTION() {
        return INTRODUCTION;
    }

    public void setINTRODUCTION(String INTRODUCTION) {
        this.INTRODUCTION = INTRODUCTION;
    }

    public void setCUSTOMER_ID(String CUSTOMER_ID) {
        this.CUSTOMER_ID = CUSTOMER_ID;
    }

    public String getICON_DOCTOR_PICTURE() {
        return ICON_DOCTOR_PICTURE;
    }

    public void setICON_DOCTOR_PICTURE(String ICON_DOCTOR_PICTURE) {
        this.ICON_DOCTOR_PICTURE = ICON_DOCTOR_PICTURE;
    }

    public String OFFICE_NAME;

    public String getDOCTOR_SPECIALLY() {
        return DOCTOR_SPECIALLY;
    }

    public void setDOCTOR_SPECIALLY(String DOCTOR_SPECIALLY) {
        this.DOCTOR_SPECIALLY = DOCTOR_SPECIALLY;
    }

    public String getOFFICE_NAME() {
        return OFFICE_NAME;

    }

    public String getDOCTOR_PICTURE() {
        return DOCTOR_PICTURE;
    }

    public void setDOCTOR_PICTURE(String DOCTOR_PICTURE) {
        this.DOCTOR_PICTURE = DOCTOR_PICTURE;
    }

    public String getTITLE_NAME() {
        return TITLE_NAME;
    }

    public void setTITLE_NAME(String TITLE_NAME) {
        this.TITLE_NAME = TITLE_NAME;
    }

    public void setOFFICE_NAME(String OFFICE_NAME) {
        this.OFFICE_NAME = OFFICE_NAME;

    }

    public String TITLE_NAME;

    public String getDOCTOR_HOSPITAL() {
        return DOCTOR_HOSPITAL;
    }

    public void setDOCTOR_HOSPITAL(String DOCTOR_HOSPITAL) {
        this.DOCTOR_HOSPITAL = DOCTOR_HOSPITAL;
    }

    public String NUMS;
    public String DOCTOR_HOSPITAL;//医院名称
    public String DOCTOR_SITE_ID;//医生所在医生集团id

    public String getDOCTOR_REAL_NAME() {
        return DOCTOR_REAL_NAME;
    }

    public void setDOCTOR_REAL_NAME(String DOCTOR_REAL_NAME) {
        this.DOCTOR_REAL_NAME = DOCTOR_REAL_NAME;
    }

    public String getDOCTOR_SITE_ID() {
        return DOCTOR_SITE_ID;
    }

    public void setDOCTOR_SITE_ID(String DOCTOR_SITE_ID) {
        this.DOCTOR_SITE_ID = DOCTOR_SITE_ID;
    }
}
