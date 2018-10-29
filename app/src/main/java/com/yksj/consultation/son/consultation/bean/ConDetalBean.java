package com.yksj.consultation.son.consultation.bean;

import java.util.ArrayList;

/**
 * Created by HEKL on 15/9/26.
 * 初始订单详情内容_
 */
public class ConDetalBean {
    private String CUSTNAME;
    private int OFFICE_ID;
    private String SEX;
    private String AGE;
    private String PHONE;
    private String AREA;
    private String CONDESC;
    private String CON_NAME;
    private int PATIENTID;
    private int PROMOTER_TYPE;
    private String TIME;
    private String ASSISTANTID;
    private String PRICE;
    private int STATUS;
    private int INVITATION_FLAG;
    private int OPENREMIND;
    private int OPENEXPERTORDER;
    private int OPENCOMMENT;
    private String STATUSNAME;
    private ArrayList<ProcessBean> PROCESS;
    private Patient PATIENT;
    private String RECORDSUPPLY;

    public String getRECORDSUPPLY() {
        return RECORDSUPPLY;
    }

    public void setRECORDSUPPLY(String RECORDSUPPLY) {
        this.RECORDSUPPLY = RECORDSUPPLY;
    }

    public int getOPENEXPERTORDER() {
        return OPENEXPERTORDER;
    }

    public void setOPENEXPERTORDER(int OPENEXPERTORDER) {
        this.OPENEXPERTORDER = OPENEXPERTORDER;
    }

    public int getOPENCOMMENT() {
        return OPENCOMMENT;
    }

    public void setOPENCOMMENT(int OPENCOMMENT) {
        this.OPENCOMMENT = OPENCOMMENT;
    }

    public String getCON_NAME() {
        return CON_NAME;
    }

    public void setCON_NAME(String CON_NAME) {
        this.CON_NAME = CON_NAME;
    }

    public int getOPENREMIND() {
        return OPENREMIND;
    }

    public void setOPENREMIND(int OPENREMIND) {
        this.OPENREMIND = OPENREMIND;
    }

    public String getCUSTNAME() {
        return CUSTNAME;
    }

    public void setCUSTNAME(String CUSTNAME) {
        this.CUSTNAME = CUSTNAME;
    }

    public int getOFFICE_ID() {
        return OFFICE_ID;
    }

    public void setOFFICE_ID(int OFFICE_ID) {
        this.OFFICE_ID = OFFICE_ID;
    }

    public String getSEX() {
        return SEX;
    }

    public void setSEX(String SEX) {
        this.SEX = SEX;
    }

    public String getAGE() {
        return AGE;
    }

    public void setAGE(String AGE) {
        this.AGE = AGE;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getAREA() {
        return AREA;
    }

    public void setAREA(String AREA) {
        this.AREA = AREA;
    }

    public String getCONDESC() {
        return CONDESC;
    }

    public void setCONDESC(String CONDESC) {
        this.CONDESC = CONDESC;
    }

    public int getPATIENTID() {
        return PATIENTID;
    }

    public void setPATIENTID(int PATIENTID) {
        this.PATIENTID = PATIENTID;
    }

    public int getPROMOTER_TYPE() {
        return PROMOTER_TYPE;
    }

    public void setPROMOTER_TYPE(int PROMOTER_TYPE) {
        this.PROMOTER_TYPE = PROMOTER_TYPE;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public String getASSISTANTID() {
        return ASSISTANTID;
    }

    public void setASSISTANTID(String ASSISTANTID) {
        this.ASSISTANTID = ASSISTANTID;
    }

    public String getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
    }

    public int getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(int STATUS) {
        this.STATUS = STATUS;
    }

    public int getINVITATION_FLAG() {
        return INVITATION_FLAG;
    }

    public void setINVITATION_FLAG(int INVITATION_FLAG) {
        this.INVITATION_FLAG = INVITATION_FLAG;
    }

    public String getSTATUSNAME() {
        return STATUSNAME;
    }

    public void setSTATUSNAME(String STATUSNAME) {
        this.STATUSNAME = STATUSNAME;
    }

    public ArrayList<ProcessBean> getPROCESS() {
        return PROCESS;
    }

    public void setPROCESS(ArrayList<ProcessBean> PROCESS) {
        this.PROCESS = PROCESS;
    }

    public Patient getPATIENT() {
        return PATIENT;
    }

    public void setPATIENT(Patient PATIENT) {
        this.PATIENT = PATIENT;
    }

    @Override
    public String toString() {
        return "ConDetalBean{" +
                "CUSTNAME='" + CUSTNAME + '\'' +
                ", OFFICE_ID=" + OFFICE_ID +
                ", SEX='" + SEX + '\'' +
                ", AGE='" + AGE + '\'' +
                ", PHONE='" + PHONE + '\'' +
                ", AREA='" + AREA + '\'' +
                ", CONDESC='" + CONDESC + '\'' +
                ", CON_NAME='" + CON_NAME + '\'' +
                ", PATIENTID=" + PATIENTID +
                ", PROMOTER_TYPE=" + PROMOTER_TYPE +
                ", TIME='" + TIME + '\'' +
                ", ASSISTANTID='" + ASSISTANTID + '\'' +
                ", PRICE='" + PRICE + '\'' +
                ", STATUS=" + STATUS +
                ", INVITATION_FLAG=" + INVITATION_FLAG +
                ", OPENREMIND=" + OPENREMIND +
                ", OPENEXPERTORDER=" + OPENEXPERTORDER +
                ", OPENCOMMENT=" + OPENCOMMENT +
                ", STATUSNAME='" + STATUSNAME + '\'' +
                ", PROCESS=" + PROCESS +
                ", PATIENT=" + PATIENT +
                ", RECORDSUPPLY='" + RECORDSUPPLY + '\'' +
                '}';
    }
}