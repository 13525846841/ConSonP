package com.yksj.healthtalk.bean;

/**
 * Created by jack_tang on 15/10/28.
 */
public class AlertEntity {
    public String name;
    public String code;
    public Object o;

    public AlertEntity() {
    }

    public AlertEntity(String name, String code) {
        this.name = name;
        this.code = code;
    }
    public AlertEntity(String name, String code, Object o) {
        this.name = name;
        this.code = code;
        this.o=o;
    }
}
