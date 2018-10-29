package com.yksj.consultation.son.consultation.bean;

/**
 * Created by jack_tang on 15/9/26.
 * 自己随便写的一个对象  方便简单的传递
 * Event使用
 */
public class MyEvent {
    public String what;
    public int code;//7057系统提示 2001微信制服成功

    public MyEvent(String what, int code) {
        super();
        this.what = what;
        this.code = code;
    }
}
