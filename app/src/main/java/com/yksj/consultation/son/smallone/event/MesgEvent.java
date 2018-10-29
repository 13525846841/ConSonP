package com.yksj.consultation.son.smallone.event;

import com.yksj.healthtalk.entity.MessageEntity;

import org.json.JSONObject;

/**
 * Created by jack_tang on 15/9/26.
 * 自己随便写的一个对象  方便简单的传递
 * Event使用
 */
public class MesgEvent {
        public JSONObject what;
        public MessageEntity msg;
        public int code;

        public MesgEvent(JSONObject what, int code) {
            super();
            this.what = what;
            this.code=code;
        }
        public MesgEvent(MessageEntity msg, int code) {
            super();
            this.msg = msg;
            this.code=code;
        }
}
