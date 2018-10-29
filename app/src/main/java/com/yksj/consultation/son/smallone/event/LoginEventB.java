package com.yksj.consultation.son.smallone.event;


public class LoginEventB {

    private String mesge;
    private Object object;
    private Event event;

    public String getMesge() {
        return mesge;
    }

    public void setMesge(String mesge) {
        this.mesge = mesge;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public LoginEventB(String mesge, Event event) {
        this.mesge = mesge;
        this.event = event;
    }
    public LoginEventB(Event event) {
        this.event = event;
    }

    public enum Event {
        //空闲,未登录
        NONE,
        //登陆中
        LOGINING,
        // 登陆成功
        LOGIN_OK,
        LOGIN_OUT_SUCCESS,//退出


        ACTION_LOGIN,//登陆
        ACTION_LOGIN_OK,//登陆成功
        ACTION_LOGIN_FAILUES,//登陆失败
        ACTION_LOGIN_OTHER_PLACE//异地登陆
    }

}