package com.yksj.healthtalk.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HEKL on 15/9/24.
 * 会诊列表实体类
 */
public class ConsultListBean {
    private String code;
    private String message;
    private ArrayList<ListDetails> result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<ListDetails> getResult() {
        return result;
    }

    public void setResult(ArrayList<ListDetails> result) {
        this.result = result;
    }


    @Override
    public String toString() {
        return "ConsultListBean{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
