package com.yksj.healthtalk.bean;

/**
 * 视频提醒
 */
public class VideoEvent {
    public static final int TIP = 100;
    public static final int END = 101;
    /**
     * what: refresh 订单详情的更新
     */
    public String what;
    public int code;//100视频结束  101 视频提醒

    public VideoEvent(String what, int code) {
        super();
        this.what = what;
        this.code = code;
    }
}
