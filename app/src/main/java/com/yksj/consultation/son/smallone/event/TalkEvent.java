package com.yksj.consultation.son.smallone.event;

/**
 * Created by HEKL on 16/5/11.
 * Used for
 */

public class TalkEvent {
    public  static final  int LONGTIME=100;
    public  static final  int UPTALK=101;
    public  static final  int STOPAN=102;
    String  message;
    Object object;
    int arg1;
    int arg2;
    int what;

    public void setMessage(String message) {
        this.message = message;
    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    public String getMessage() {
        return message;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public void setArg1(int arg1) {
        this.arg1 = arg1;
    }

    public int getArg1() {
        return arg1;
    }

    public void setArg2(int arg2) {
        this.arg2 = arg2;
    }

    public int getArg2() {
        return arg2;
    }
}
