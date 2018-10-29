package com.yksj.consultation.son.consultation.bean;

/**
 * Created by HEKL on 15/9/26.
 * 初始会诊订单详情_
 */
public class ConsultDetailsFBean {
    private String message;//系统返回消息
    private ConDetalBean result;
    private int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ConDetalBean getResult() {
        return result;
    }

    public void setResult(ConDetalBean result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ConsultDetailsFBean{" +
                "message='" + message + '\'' +
                ", result=" + result +
                ", code=" + code +
                '}';
    }
}
