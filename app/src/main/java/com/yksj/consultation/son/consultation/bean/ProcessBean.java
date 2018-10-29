package com.yksj.consultation.son.consultation.bean;

/**
 * Created by HEKL on 15/9/26.
 * 会诊订单进度_
 */
public class ProcessBean {
    private String color;
    private String name;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ProcessBean{" +
                "color='" + color + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
