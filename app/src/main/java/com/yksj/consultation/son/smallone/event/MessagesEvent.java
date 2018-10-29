package com.yksj.consultation.son.smallone.event;


import com.yksj.healthtalk.entity.MessageEntity;

import org.json.JSONObject;


/**
 * Created by jack_tang on 15/11/30.
 */
public class MessagesEvent {
    public MessageEntity getMsg() {
        return msg;
    }

    public void setMsg(MessageEntity msg) {
        this.msg = msg;
    }

    public JSONObject jsonObject;
    public MessageEntity msg;
    public int code;
    public Event event;
    public String string;

    public MessagesEvent(JSONObject jsonObject, Event event, int code) {
        this.jsonObject = jsonObject;
        this.event = event;
        this.code = code;
    }

    public MessagesEvent(JSONObject jsonObject, Event event) {
        this.jsonObject = jsonObject;
        this.event = event;
    }

    public MessagesEvent(MessageEntity msg, Event event) {
        this.msg = msg;
        this.event = event;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public MessagesEvent(Event event) {
        this.event = event;
    }

    public MessagesEvent(String string, Event event) {
        this.event = event;
        this.string = string;
    }

    public enum Event {
        SERVICE_TO_AUTO_CONTENT,//客服要求(将内容)切换机器人
        RECEIVE_PUSH_SHOP_MESG,
        SERVICE_SWITCH_RESPONSE
    }

}
