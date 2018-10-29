package com.yksj.consultation.son.smallone.socket;

/**
 * Created by jack_tang on 15/10/16.
 */
public class SocketCode {

    public static final int LOGIN_CODE = 10001;//登陆
    public static final int LOGIN_TIMEOUT = 30*1000;//超时时间
    public static final String BYDELIMITER = "-@#$@#%0___4@$!!$#";
    public static final int SOCKET_HEART_CODE =100 ;//心跳
    public static final long CONNECTION_PING = 15*1000;//心跳链接时间
    public static final int LOGIN_OTHERPLACE = 9105;//异地登陆
    public static final long INITIAL_RETRY_INTERVAL = 1000 * 15;//重连时间间隔

    public static final int ACTIVE_FAULT = 10002;
    public static final int ACTIVE_FAULT_2 = 10003;
    public static final int RECEIVE_MSG_CODE = 9019;//接受消息
    public static final int SEND_MSG_CODE = 9018;//消息发送状态
    public static final int SURE_ORDER = 10005;//确认订单
    public static final int LOGIN_PAUSE = 10009;//暂停服务/上线
    public static final int SERVICE_SWITCH = 10011;//服务发起切换小壹 10011

    public static final int SERVICE_PUSH_ORDER = 10004;//客服推送订单
    public static final int SERVICE_SWITCH_RESPONSE = 9017;//服务发起切换小壹 10011
    public static final int LOGINED_SEND = 10007;//登陆之后发送


    //LoginActivity
    public static final int LOGINACTITION = 11;
    public static final int LOGINOUTACTITION = 12;





    /************************************************************************************
     * 用于广播
     */
    //登陆成功
    public static final String ACTION_LOGIN_SUCCESS ="action_login_success";
    //登录失败
    public static final String ACTION_LOGIN_FAILUES ="action_login_failues";
    //异地登陆
    public static final String ACTION_LOGIN_OTHER_PLACE ="action_login_other_place";

    /**
     * 推送的订单
     */
    public static final int ORDER_PUSH_SHOP_DISPLAY = 10021;//推送商品展示
    public static final int ORDER_PUSH_DOCTOR_DISPLAY_1 = 10025;//推送趣医院医生
    public static final int ORDER_PUSH_BIAOQIAN_CODE = 10023;//推送标签

}
