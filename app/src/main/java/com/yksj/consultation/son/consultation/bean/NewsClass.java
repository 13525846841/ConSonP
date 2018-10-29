package com.yksj.consultation.son.consultation.bean;

import com.yksj.healthtalk.utils.HStringUtil;

import org.json.JSONObject;

/**
 * Created by HEKL on 16/5/9.
 * Used for
 */

public class NewsClass  {
    private String INFO_CLASS_ID;
    private String INFO_CLASS_NAME;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getINFO_CLASS_ID() {
        return INFO_CLASS_ID;
    }

    public void setINFO_CLASS_ID(String INFO_CLASS_ID) {
        this.INFO_CLASS_ID = INFO_CLASS_ID;
    }

    public String getINFO_CLASS_NAME() {
        return INFO_CLASS_NAME;
    }

    public void setINFO_CLASS_NAME(String INFO_CLASS_NAME) {
        this.INFO_CLASS_NAME = INFO_CLASS_NAME;
    }

    public static NewsClass parseFormat(JSONObject json) {
        NewsClass entity = new NewsClass();
        try {
            entity.INFO_CLASS_ID = HStringUtil.stringFormat(json.optString("INFO_CLASS_ID"));
            entity.INFO_CLASS_NAME = HStringUtil.stringFormat(json.optString("INFO_CLASS_NAME"));
            entity.content = HStringUtil.stringFormat(json.optString("artList"));

        } catch (Exception e) {
            return null;
        }
        return entity;
    }
}
