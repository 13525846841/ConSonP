package com.yksj.healthtalk.entity;

import java.util.ArrayList;
import java.util.HashMap;

public class ApplyFormEntity {
    private String name;// 疾病名称
    private String condesc;// 疾病描述
    private int patientId;// 病人id
    private String phone;// 电话
    private String area;// 地区
    private int price;// 价钱
    private String time;// 时间
    private int stauts;// 状态
    private int assistantId;// 会诊医生Id
    private String assistant;// 会诊医生姓名
    private String aId;// 会诊医生Id
    private String doctor;// 专家姓名
    private String doctorIcon;// 专家头像
    private String assistantIcon;// 会诊医生头像
    private String patientPic;// 病人头像
    private String statusName;// 状态名称
    private String promoterType;// 发起人类型 患者：10;医生：20;
    private ArrayList<HashMap<String, String>> pics;// 状态名称


    public String getAssistantIcon() {
        return assistantIcon;
    }

    public void setAssistantIcon(String assistantIcon) {
        this.assistantIcon = assistantIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCondesc() {
        return condesc;
    }

    public void setCondesc(String condesc) {
        this.condesc = condesc;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStauts() {
        return stauts;
    }

    public void setStauts(int stauts) {
        this.stauts = stauts;
    }

    public int getAssistantId() {
        return assistantId;
    }

    public void setAssistantId(int assistantId) {
        this.assistantId = assistantId;
    }

    public String getAssistant() {
        return assistant;
    }

    public void setAssistant(String assistant) {
        this.assistant = assistant;
    }

    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getDoctorIcon() {
        return doctorIcon;
    }

    public void setDoctorIcon(String doctorIcon) {
        this.doctorIcon = doctorIcon;
    }

    public String getPatientPic() {
        return patientPic;
    }

    public void setPatientPic(String patientPic) {
        this.patientPic = patientPic;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getPromoterType() {
        return promoterType;
    }

    public void setPromoterType(String promoterType) {
        this.promoterType = promoterType;
    }

    public ArrayList<HashMap<String, String>> getPics() {
        return pics;
    }

    public void setPics(ArrayList<HashMap<String, String>> pics) {
        this.pics = pics;
    }

    @Override
    public String toString() {
        return "ApplyFormEntity{" +
                "name='" + name + '\'' +
                ", condesc='" + condesc + '\'' +
                ", patientId=" + patientId +
                ", phone='" + phone + '\'' +
                ", area='" + area + '\'' +
                ", price=" + price +
                ", time='" + time + '\'' +
                ", stauts=" + stauts +
                ", assistantId=" + assistantId +
                ", assistant='" + assistant + '\'' +
                ", aId='" + aId + '\'' +
                ", doctor='" + doctor + '\'' +
                ", doctorIcon='" + doctorIcon + '\'' +
                ", assistantIcon='" + assistantIcon + '\'' +
                ", patientPic='" + patientPic + '\'' +
                ", statusName='" + statusName + '\'' +
                ", promoterType='" + promoterType + '\'' +
                ", pics=" + pics +
                '}';
    }
}
