package com.yksj.healthtalk.net.socket;

import android.content.res.Resources;

import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.utils.MD5Utils;


public class SmartControlClient {

    private static final String TAG = "SmartControlClient";
    public static final String EXTENTION_SMS_SYS_INITIALIZATION = "sms_sys_initialization";
    public static final long CONNECTION_TIMEOUT = 1000 * 20;// 连接超时
    public static final long CONNECTION_PING = 1000 * 20;//检测连接
    public static final long INITIAL_RETRY_INTERVAL = 1000 * 10;//重连时间间隔
    public static final long COMMENT_TIME_AUTO = 1000 * 300;//好评弹出

    public static final String KEY_CODE = "server_code";
    public static final String KEY_PARAME = "server_parame";
    public static final String KEY_VALUE_GROUPID = "groupid";
    public static final String KEY_VALUE_INCEPTMESSAGE = "inceptMessage";
    public static final String ROOM_NAME = "minihome";
    public static final String SYSTEM_VERSION = "system_version";
    public static final String OS_TYPE = "os_type";
    public static final String KEYWORDS = "keyWords";

    /**
     * 多美助手id
     */
    public static final String helperId = "100000";
    private static SmartControlClient mSmartClient;
    private CustomerInfoEntity mLoginEntity;
    //	private boolean isLogined = false;//是否已经登录
    private LoginEvent loginState = LoginEvent.NONE;//登录状态-1未登陆,0登录中,1登录成功,2资料加载完成
    private long lastConnectTime;
    private String userName;
    private String password;
    private String userMd5Id = "";
    private final String osVersion = HTalkApplication.getAppVersionName();
    private final String osType = "1";
    private XsocketHanlder mListener;
    private final SocketManager mSocketManager;
    //	private String CONSULTATION_CENTER_ID=HTalkApplication.APP_CONSULTATION_CENTERID;

    public static synchronized SmartControlClient init(XsocketHanlder xsocketHanlder) {
        if (mSmartClient == null) {
            mSmartClient = new SmartControlClient(xsocketHanlder);
        }
        return mSmartClient;
    }

    public static synchronized SmartControlClient getControlClient() {
        return mSmartClient;
    }

    private SmartControlClient(XsocketHanlder listener) {
        this.mListener = listener;
//        System.setProperty("java.net.preferIPv6Addresses", "false");
//        Resources resources = HTalkApplication.getAppResources();
//        this.imei = SystemUtils.getImeiId();
//        mConfigData = new ConfigData();
//        mConfigData.setPort(Integer.valueOf(resources.getString((R.string.smart_port))));
//        mConfigData.setHost(resources.getString(R.string.smart_ip));
//        mConfigData.setZone(resources.getString(R.string.smart_zone));
        Resources resources = HTalkApplication.getAppResources();
        mSocketManager = SocketManager.getSocketManager(mListener);
        init();
    }

    private synchronized void init() {
//        if (mSmartFox != null) {
//            mSmartFox.removeAllEventListeners();
//            mSmartFox.disconnect();
//            mSmartFox = null;
//        }
//        mSmartFox = new SmartFox(LogUtil.DEBUG);
//        mSmartFox.addEventListener(SFSEvent.CONNECTION, mListener);
//        mSmartFox.addEventListener(SFSEvent.CONNECTION_LOST, mListener);
//        mSmartFox.addEventListener(SFSEvent.CONNECTION_RESUME, mListener);
//        mSmartFox.addEventListener(SFSEvent.CONNECTION_RETRY, mListener);
//        mSmartFox.addEventListener(SFSEvent.USER_ENTER_ROOM, mListener);
//        mSmartFox.addEventListener(SFSEvent.SOCKET_ERROR, mListener);
//        mSmartFox.addEventListener(SFSEvent.ROOM_JOIN, mListener);
//        mSmartFox.addEventListener(SFSEvent.ROOM_JOIN_ERROR, mListener);
//        mSmartFox.addEventListener(SFSEvent.EXTENSION_RESPONSE, mListener);
//        mSmartFox.addEventListener(SFSEvent.LOGOUT, mListener);
//        mSmartFox.addEventListener(SFSEvent.LOGIN, mListener);
//        mSmartFox.addEventListener(SFSEvent.LOGIN_ERROR, mListener);
//        mSmartFox.addEventListener(SFSEvent.HANDSHAKE, mListener);
    }

    public synchronized void doSendExtentionReq(SocketParams sfsObject) {
        sfsObject.putUtfString(SYSTEM_VERSION, osVersion);
        sfsObject.putUtfString(OS_TYPE, osType);
        sfsObject.putUtfString("CONSULTATION_CENTER_ID", HTalkApplication.APP_CONSULTATION_CENTERID);
//		sendReq(new ExtensionRequest(EXTENTION_SMS_SYS_INITIALIZATION,sfsObject,mSmartFox.getLastJoinedRoom()));
    }

//    public synchronized void sendReq(IRequest request) {
//
//        if (mSmartFox.isConnected()) {
//            mSmartFox.send(request);
//        } else {
//            if (lastConnectTime != 0 && (System.currentTimeMillis() - lastConnectTime > CONNECTION_TIMEOUT)) {
//                if (HTalkApplication.getApplication().isNetWork()) connect();
//            }
//        }
//    }

    public synchronized void connect() {
        mSocketManager.connect();
//        if (mSmartFox == null || mSmartFox.isConnecting()) {
//            init();
//            LogUtil.d(TAG, "================mSmartFox.isConnecting()");
//        }
//        if (!mSmartFox.isConnected()) {
//            mSmartFox.connect(mConfigData);
//            lastConnectTime = System.currentTimeMillis();
//        }
    }


    public synchronized void disconnect() {
        mSocketManager.disConnect();
    }

    public synchronized boolean isConnected() {
        return mSocketManager.isConnected();
    }

    public synchronized CustomerInfoEntity getInfoEntity() {
        return LoginServiceManeger.instance().getLoginEntity();
//        try {
//            if (mLoginEntity == null) {
//                String parame = SharePreUtils.getLoginUserInfo();
//                String[] str = SharePreUtils.fatchUserLoginCache();
//                mLoginEntity = DataParseUtil.jsonToCustomerInfo(parame);
//                SmartControlClient controlClient = SmartControlClient.getControlClient();
//                controlClient.setUserPassword(str[0], str[1]);
//                controlClient.setCustomerInfoEntity(mLoginEntity);
//                if (getLoginState() != LoginEvent.LOGIN_OK) {
//                    setLoginState(LoginEvent.LOGINING);
//                }
//                if (isConnected()) {
//                    login();
//                } else {
//                    connect();
//                }
//                mSmartClient.sendLoadUserInfo();
//            }
//            return mLoginEntity;
//        } catch (Exception e) {
//            return null;
//        }
    }

    public synchronized void setCustomerInfoEntity(CustomerInfoEntity mEntity) {
        LoginServiceManeger.instance().setLoginInfo(mEntity);
//        this.mLoginEntity = mEntity;
//        if (mLoginEntity != null) {
//            setLoginState(LoginEvent.LOGIN_OK);
//            setUserMd5Id(mEntity.getId());
//            //初始化数据库
//            ChatUserHelper.getInstance();
//        }
//        if (mEntity == null)
//            sendLoadUserInfo();
    }

    /**
     * 是否已经登录
     *
     * @return false
     */
    public synchronized boolean isLogined() {
        return loginState == LoginEvent.LOGIN_OK;
    }

    /**
     * 加载客户资料
     */
    public synchronized void sendLoadUserInfo() {
        SocketParams sfsObject = new SocketParams();
        sfsObject.putInt(KEY_CODE, RequestCode.CUSTOMER_INFO_REQ);
        doSendExtentionReq(sfsObject);
    }

    /**
     * 更改设备号码
     */
    public void sendChangeNumber() {
        SocketParams sfsObject = new SocketParams();
        sfsObject.putInt(KEY_CODE, RequestCode.CODE_CHANGE_NUMBER);
        doSendExtentionReq(sfsObject);
    }

/*	public synchronized void senPingPongRequest(){
        sendReq(new PingPongRequest());
	}*/

    /**
     * 发送保持连接请求
     */
    public synchronized void sendKeepConnect() {
        HeartServiceManager.instance().sendHeartMsg();
//        SocketParams isfsObject = new SocketParams();
//        isfsObject.putInt(KEY_CODE, RequestCode.KEEP_CONECTION_REQ);
//        doSendExtentionReq(isfsObject);
    }

    public synchronized void login() {
//            SocketParams params=new SocketParams();
//            params.put("ACCOUNT", userName);
//            params.put("PASSWORD", password);
//            params.put("IS_AFTER_MINIMIZATION_LOGIN", loginState == LoginEvent.LOGIN_OK ? "1" : "0");
//            params.put("CLIENTONLY", SystemUtils.getImeiId());
//            SocketManager.sendSocketParams(params, SmartControlClient.LOGIN_CODE);


//        SocketParams sfsObject = new SocketParams();
//        sfsObject.putUtfString("user_identifier", imei);
//        sfsObject.putUtfString("serial_code", imei);
//        sfsObject.putUtfString("iscreate", false ? "1" : "0");
//        //是否是重新连接,正常登录0
//        sfsObject.putUtfString("loginflag", loginState == 2 ? "1" : "0");
//        sfsObject.putUtfString(SYSTEM_VERSION, osVersion);//
//        sfsObject.putUtfString("user_password", password);
//        sfsObject.putUtfString(OS_TYPE, osType);//1
//        sfsObject.putUtfString("CONSULTATION_CENTER_ID", HTalkApplication.APP_CONSULTATION_CENTERID);//1
//        sfsObject.putUtfString("VALID_MARK", HTalkApplication.APP_VALID_MARK);
//		LoginRequest request = new LoginRequest(userName,password,mConfigData.getZone(),sfsObject);
//		sendReq(request);
    }


    public synchronized String getUserMd5Id() {
        return userMd5Id;
    }

    public synchronized String getPassword() {
        return password;
    }

    public synchronized String getUserId() {
        if (getInfoEntity() != null) {
            return getInfoEntity().getId();
        } else {
            return "";
        }

    }

    public synchronized void setUserMd5Id(String userid) {
        this.userMd5Id = MD5Utils.generateMD5Str(userid);
    }

    public synchronized void setUserPassword(String userName, String password) {
        this.userName = userName;
        if (password.length() <= 12) {
            this.password = MD5Utils.getMD5(password);
        } else {
            this.password = password;
        }
        HttpRestClient.addHttpHeader("username", this.userName);
        HttpRestClient.addHttpHeader("password", this.password);
        HttpRestClient.addHttpHeader("client_type", HTalkApplication.CLIENT_TYPE);
    }

    /**
     * 修改密码
     *
     * @param password
     */
    public synchronized void updateUserPassword(String password) {
        if (password.length() <= 12) {
            this.password = MD5Utils.getMD5(password);
        } else {
            this.password = password;
        }
        HttpRestClient.addHttpHeader("password", password);
    }

    public LoginEvent getLoginState() {
        return LoginServiceManeger.instance().getLoginState();
    }

    /**
     * 服务编码
     *
     * @author zhao yuan
     */
    public static class RequestCode {
        public static final int LOGIN_REQ = 1024;
        public static final int KEEP_CONECTION_REQ = 100;// 保持连接定时检测
        // 小贴士
        public static final int CODE_NEWS = 9111;
        public static final int CODE_REPORT = 9100; // 举报
        public static final int CODE_REPEAT_MESSAGE = 9133;//转发
        /**
         * 聊天
         */
        public static final int CHATTING_MESSAGE_REQ = 9900;
        public static final int CHATTING_INVITE_RECEIVE = 9088; // 接收群邀请消息
        public static final int CHATTING_SINGLE_MESSAGE_RECEIVE = 9019;// 接受单聊消息
        public static final int CHATTING_MESSAGE_SENDSTATE = 9018;// 消息发送状态返回
        public static final int CHATTING_GROUP_MESSAGE_RECEIVE = 9045;// 群聊接收
        public static final int CHATTING_GROUP_MESSAGE_SEND = 9045;// 群聊发送,接收
        public static final int DIALOG_CHECK_VALIDATOR_REQ = 9099;// 虚拟医生聊天关联检查话题是否存在
        public static final int DIALOG_CREATE_REQ = 9098;// 虚拟医生聊天关联话题创建话题
        public static final int CHATTING_ONLINE_FRIEND = 9142;//在线成员
        /**
         * news
         */
        public static final int CODE_ATTENTION = 9056; // 加载我的关注
        public static final int CODE_MY_ATTENTION_DELE = 9058;// 取消关注
        public static final int CODE_MY_ATTENTION_ADD = 9057; // 添加关注

        /**
         * 健康友
         */
        public static final int CUSTOMER_INFO_REQ = 9220; // 请求客户基本信息
        public static final int LOAD_FRIEND_LIST = 9001; // 加载健康友列表
        public static final int CODE_SEARCH_FRIEND = 9021; // 按条件搜索健康友
        public static final int CODE_COLLECT_FRIEND = 9024; // 1收藏或0删除好友,2加入黑名单,3移除黑名单
        public static final int CODE_PUBLIC_LOCATION = 9106; // 是否公开自己位置
        public static final int CODE_CHANGE_LOCATION = 9062; // 更改自己位置9062
        public static final int CODE_PUSH_ATTENTION = 9118;// 推送健康贴士
        public static final int SERVER_PARAME = 9125;// 注销
        public static final int CODE_INCEPTMESSAGE = 9120; // 是否接受群消息
        // 9025要改
        public static final int CODE_FIND_ME = 9025; // 通讯录好友能否找到我

        /**
         * 个人资料
         */
        public static final int CODE_CHANGE_PERSONALINFO = 9026; // 修改个人信息
        public static final int CODE_GET_ATTENTION = 9077; // 查询关注信息
        public static final int CODE_GET_WANTKNOW = 9081; // 查询我想了解
        public static final int CODE_CHANGE_WANTKNOW = 9080; // 更改我想了解
        public static final int CODE_CHANGE_PASSWORD = 9043; // 更改密码
        public static final int CODE_SEND_FACEBACK = 9079; // 意见反馈

        /**
         * 聊天室
         */
        public static final int LOAD_GROUP_LIST = 9003; // 加载聊天室列表
        public static final int SEARCH_GROUP_ATT = 9006; // 按关注搜索聊天室
        public static final int SEARCH_GROUP_NAME = 9008; // 按话题搜索聊天室
        public static final int COLLECT_GROUP = 9073; // 收藏聊天室
        public static final int COLLECT_GROUP_NOT = 9074; // 取消收藏聊天室
        public static final int GROUP_HALL = 9071; // 加载消息厅
        public static final int JOIN_GROUP_CHAT = 9090;// 加入聊天室
        public static final int EXIT_GROUP_CHAT = 9091;// 退出聊天室
        public static final int GROUP_INVITE = 9149; // 群邀请
        public static final int GROUP_ONLINE = 9047;// 当前群的在线用户

        public static final int DOCTOR_INFO_AUDIT = 9108; // 医师资格审核
        public static final int OFFLINE_DELE = 9104; // 删除离线消息
        /**
         * 账号异地登录
         */
        public static final int LOGIN_OTHERPLACE = 9105; // 账号异地登录
        public static final int CHAT_HEALPER = 9112;// 多美妹子聊天
        public static final int NOTIFY_MESSAGE = 9114;// 通知
        public static final int CODE_NOTIFY_FILTER = 9127;// 发布厅筛选
        public static final int CODE_UPDATE_FORBIDWORDS_LIST = 9126;// 禁言列表更新
        public static final int CODE_FORBIDWORDS_LIST = 9129;// 禁言列表查询


        public static final int CODE_CHARGE_STATE = 9145;//医生收费请求发送状态返回
        public static final int CODE_CHARGE = 9146;//医生收费请求

        public static final int CODE_PRODUCT_CHANGE = 10008;//订单变化通知
        public static final int CODE_LEAVE_WORDS = 120;//留言
        public static final int CODE_CHANGE_NUMBER = 9990;//更改设备号
    }


    //		新版-------------------------
//消息分隔符
    public static final String BYDELIMITER = "-@#$@#%0___4@$!!$#";
    public static final int LOGIN_CODE = 1001;//登陆
    public static final int LOGIN_OUT = 1002;//登出
    public static final int LOGIN_TIMEOUT = 1000 * 30;//超时时间
    public static final int SOCKET_HEART_CODE = 100;//心跳
    public static final int SOCKET_SINGLE_CHAT = 7018;//单聊
    public static final int CON_EXPERT_ACCEPT = 7019;//专家接受邀请
    public static final int SOCKET_GROUP_CHAT = 7045;//群聊
    public static final int CHATTING_MESSAGE_SENDSTATE = 9018;// 消息发送状态返回
    public static final int CHATTING_GROUP_MESSAGE_RECEIVE = 9019;// 接受群聊消息
    public static final int CHATTING_SINGLE_MESSAGE_RECEIVE = 9045;//  接受单聊消息

    public static final int ORDER_CHANGE_STATE = 7010;// 订单状态变化
    public static final int CHAT_ORDER_CHANGE_STATE = 7011;// 特殊订单状态变化

    public static final int SIX_ONE_SEND_MSG = 7075;// 六一班群聊发送消息
    public static final int SIX_ONE_RECIEVE_MSG = 7076;// 六一班群聊发送消息

    public static final int SERVICE_SINGLE_SEND_MSG = 7055;// 服务购买单聊发送
    public static final int SERVICE_SINGLE_RECIEVE_MSG = 7056;// 服务购买单聊接收
    public static final int SERVICE_SINGLE_SYS_MSG = 7057;// 服务购买单聊系统提示
    public static final int SERVICE_SINGLE_SYS_MSG_TIP = 7058;// 服务购买单聊系统比例提示

}
