package com.yksj.consultation.son.smallone.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;

import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.smallone.event.LoginEventB;
import com.yksj.consultation.son.smallone.event.TalkEvent;
import com.yksj.consultation.son.smallone.manager.LoginServiceManegerB;
import com.yksj.consultation.son.smallone.manager.MessageServiceManagerB;
import com.yksj.consultation.son.smallone.socket.HeartServiceManagerB;
import com.yksj.consultation.son.smallone.socket.SocketCode;
import com.yksj.consultation.son.smallone.socket.XsocketHanlderB;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.SharePreUtils;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.WeakHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.xsocket.connection.INonBlockingConnection;

import de.greenrobot.event.EventBus;


/**
 * Created by jack_tang on 15/10/16.
 */
public class CoreServiceB extends Service implements XsocketHanlderB.XsocketHanlderListeren {

    public static final String PARAME_KEY = "parame"; // 广播传值key
    private Context mApplication;
    private LoginServiceManegerB mLoginCtrol;
    public static final String TAG = "CoreServiceB";
    public static final String XSOCKET_LOGIN_SUCCESS = "Xsocket.LoginSuccess";//成功登陆
    public static final String XSOCKET_LOGIN_ERROR = "Xsocket.Error";//错误
    public static final String XSOCKET_LOGIN_OUT = "Xsocket.Out";//错误
    private PowerManager.WakeLock wakeLock = null; //cpu 唤醒锁
    private int keepCountTimeOut = 0;//3  表示需要重连
    public static final String ACTION_RECONNECT = "CoreServiceB.Reconnect";//连接尝试
    public static final String ACTION_KEEPALIVE = "CoreServiceB.Keepalive";//保持连接
    private boolean iS_RETRY_INTERVAL = false;//是否正在重连操作  true 表示是
    public static final int RETRY_INTERVAL = 6000;//尝试重连
    public static final int CORESERVICE_KEEP_CONNECTION = 5000;//保持连接
    public static final int TOAST_CONTENT = 11000;//保持连接
    public static final String ACTION_TALK_WITH_SERVICE_TIMEOUT = "TALK_WITH_SERVICE_TIMEOUT";//action 标记
    public static final int TALK_WITH_SERVICE_TIMEOUT = 100000;//handler  what
    public static final String TALKTIMEOUT = "TALKTIMEOUT";
    public int TALKTIMEOUT_VALUE = 6000000;
    WeakHandler mHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case CORESERVICE_KEEP_CONNECTION://保持连接
                    startKeepConnection();
                    break;
                case RETRY_INTERVAL://尝试重连
                    scheduleReconnect();
                    break;
                case SocketCode.LOGIN_OTHERPLACE://异地登陆
                    showLoginOutDialog2(mApplication, "您的账号已在其它设备登录");
                    break;
                case TALK_WITH_SERVICE_TIMEOUT://超时
//                    showLoginOutDialog2(mApplication,"您的账号由于网络原因，已经断开连接");
                    HTalkApplication.getApplication().setMTalkServiceId("");
                    TalkEvent talkEvent = new TalkEvent();
                    talkEvent.setWhat(TalkEvent.LONGTIME);
                    EventBus.getDefault().post(talkEvent);
                    break;
                case TOAST_CONTENT://
                    // ToastUtil.showShort(msg.obj.toString());
                    break;
            }
            return false;
        }
    });
    private MessageServiceManagerB mMsgService;


    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = getApplicationContext();
        initService();
        registerNotificationReceiver();
    }

    /**
     * 挤下线对话框
     *
     * @param context
     * @return
     */
    public static void showLoginOutDialog2(final Context context, String str) {
        ToastUtil.showShort(context, "异地登陆");
//        Intent intent = new Intent(context, LoginOutDialogActivity.class);
//        intent.putExtra("text",str);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
    }

    /**
     * 注册通知
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            // 网络连接
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                // 重新连接
                if (HTalkApplication.getApplication().isNetWork()) {
                    reconnectIfNecessary();
                } else {
                    // 掉线处理
                    cancelReconnect();
                }
            }
        }

        ;
    };


    private synchronized void registerNotificationReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, filter);
    }

    private void unregisterNotificationReceiver() {
        try {
            unregisterReceiver(mReceiver);
        } catch (Exception e) {

        }

    }

    private synchronized void reconnectIfNecessary() {
        if (mLoginCtrol.getLoginState() == LoginEventB.Event.LOGINING || mLoginCtrol.getLoginState() == LoginEventB.Event.ACTION_LOGIN_OK) {
            //LogUtil.d(TAG, "CoreServiceB==尝试重新连接");
            scheduleReconnect();
        }
    }

    private void initService() {
        XsocketHanlderB.getInstance(this);
        mLoginCtrol = LoginServiceManegerB.instance();
        mLoginCtrol.onStartIMManager(mApplication);
        HeartServiceManagerB.instance().onStartIMManager(mApplication);
        mMsgService = MessageServiceManagerB.instance();
        mMsgService.onStartIMManager(mApplication);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        //  LogUtil.d(TAG, "=========CoreServiceB==onStart=intent=" + intent);

        if (intent == null || intent.getAction() == null)
            return;

        if (intent.getAction().equals(XSOCKET_LOGIN_SUCCESS)) {//登陆成功,并且资料加载完成
            LoginEventB event = new LoginEventB(LoginEventB.Event.ACTION_LOGIN_OK);
            EventBus.getDefault().post(event);
            cancelReconnect();//退出重连
            mHandler.sendEmptyMessageDelayed(CORESERVICE_KEEP_CONNECTION, SocketCode.CONNECTION_PING);
        } else if (intent.getAction().equals(XSOCKET_LOGIN_ERROR)) {//登陆错误,
            SharePreUtils.saveLoginStates(false);
//            SharePreUtils.clearAcctorPaswd();
            if (mLoginCtrol.getLoginState() == LoginEventB.Event.LOGIN_OK) {//异地登陆
                stopConnection();
                mHandler.sendEmptyMessage(SocketCode.LOGIN_OTHERPLACE);
            } else {//登陆错误
                try {
                    JSONObject jo = new JSONObject(intent.getStringExtra("content"));
                    LoginEventB event = new LoginEventB(jo.optString("message"), LoginEventB.Event.ACTION_LOGIN_FAILUES);
                    EventBus.getDefault().post(event);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mLoginCtrol.setLoginState(LoginEventB.Event.NONE);//改变状态
//            ThreadManager.getInstance().createLongPool().execute(new Runnable() {
//                @Override
//                public void run() {
//                    LoginServiceManegerB.instance().loginEmpty();
//                }
//            });

        } else if (intent.getAction().equals(ACTION_KEEPALIVE)) {
            sendtKeepAlive();//发送保持连接请求
        } else if (intent.getAction().equals(ACTION_RECONNECT) == true) {
            // 重连操作
            if (HTalkApplication.getApplication().isNetWork() && mLoginCtrol.getLoginState() != LoginEventB.Event.NONE) {
                mLoginCtrol.login();
                mHandler.sendEmptyMessageDelayed(RETRY_INTERVAL, SocketCode.INITIAL_RETRY_INTERVAL);
            }
            //登录出
        } else if (intent.getAction().equals(XSOCKET_LOGIN_OUT)) {
            mLoginCtrol.setLoginState(LoginEventB.Event.NONE);
            stopConnection();
        } else if (intent.getAction().equals(ACTION_TALK_WITH_SERVICE_TIMEOUT)) {
            TALKTIMEOUT_VALUE = Integer.valueOf(intent.getStringExtra(TALKTIMEOUT)) * 1000 * 60;
            changgeServiceTime();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }


    /**
     * 退出登录
     */
    private synchronized void stopConnection() {
        stopKeepConnection();
        cancelReconnect();
        unregisterNotificationReceiver();
        mLoginCtrol.loginOut();
    }

    /**
     * 登陆失败
     *
     * @param ctx
     * @param jo
     */
    public static void actionLoginError(Context ctx, JSONObject jo) {
        Intent i = new Intent(ctx, CoreServiceB.class);
        i.putExtra("content", jo.toString());
        i.setAction(XSOCKET_LOGIN_ERROR);
        ctx.startService(i);
    }

    /**
     * 登陆成功
     *
     * @param ctx
     */
    public static void actionLoginSuccess(Context ctx) {
        Intent i = new Intent(ctx, CoreServiceB.class);
        i.setAction(XSOCKET_LOGIN_SUCCESS);
        ctx.startService(i);
    }

    /**
     * 登陆成功
     *
     * @param ctx
     */
    public static void actionTalkTimeOut(Context ctx, String time) {
        Intent i = new Intent(ctx, CoreServiceB.class);
        i.putExtra(TALKTIMEOUT, time);
        i.setAction(ACTION_TALK_WITH_SERVICE_TIMEOUT);
        ctx.startService(i);
    }

    /**
     * 退出
     *
     * @param ctx
     */
    public static void actionLogOut(Context ctx) {
        Intent i = new Intent(ctx, CoreServiceB.class);
        i.setAction(XSOCKET_LOGIN_OUT);
        ctx.startService(i);
    }


    /**
     * 发送广播
     *
     * @param action
     * @param parame
     */
    private void onSendBroadcast(String action, Object parame) {
        Intent intent = new Intent(action);
        if (parame != null) {
            if (parame instanceof Boolean) {
                intent.putExtra(PARAME_KEY, (Boolean) parame);
            } else if (parame instanceof String) {
                intent.putExtra(PARAME_KEY, (String) parame);
            } else if (parame instanceof Integer) {
                intent.putExtra(PARAME_KEY, (Integer) parame);
            }
        }
        sendBroadcast(intent);
    }

    @Override
    public void XsocketonConnect(INonBlockingConnection arg0) {
        // 连接成功，自动登录
        if (mLoginCtrol.getLoginState() != LoginEventB.Event.NONE) {
            mLoginCtrol.login();
        }
        System.out.println("-------------onConnect---");
    }

    @Override
    public void XsocketonDisconnect(INonBlockingConnection arg0) {
        // KLog.d(TAG, "=========断开Socket");
        keepCountTimeOut = 3;
        LoginEventB event = new LoginEventB(LoginEventB.Event.LOGIN_OUT_SUCCESS);
        EventBus.getDefault().post(event);

        if (LoginServiceManegerB.instance().getLoginState() == LoginEventB.Event.LOGINING) {
            LoginServiceManegerB.instance().login();
        }
//        EventBus.getDefault().post(new MyEvent("", SocketCode.LOGINACTITION));
    }

    @Override
    public void XsocketonData(INonBlockingConnection arg0) {
        try {
            keepCountTimeOut = 0;
            //为保持长连接使用
            String data = arg0.readStringByDelimiter(SocketCode.BYDELIMITER);
            JSONObject jo = new JSONObject(data);
//           if(data.contains("message")&&0 == jo.optInt("code")){
//               stopConnection();
//                CoreServiceB.showLoginOutDialog2(this,jo.optString("message"));
//               return;
//            }
            if (jo.optInt("response_type") == 1) return;

            if (!jo.has("response_type")) {
                int code = jo.getInt("server_code");
                switch (code) {//登陆
                    case SocketCode.LOGIN_CODE:
                        mLoginCtrol.setLoginInfo(jo);
                        break;
                    case SocketCode.SERVICE_SWITCH_RESPONSE://服务发起将内容转发给小壹
                        mHandler.removeMessages(TALK_WITH_SERVICE_TIMEOUT);
                        HTalkApplication.getApplication().setMTalkServiceId("");
                        mMsgService.receiverMsg(jo, jo.getInt("server_code"));
                        break;
                    case SocketCode.RECEIVE_MSG_CODE://接受对方消息
                    case SocketCode.SEND_MSG_CODE://接受自己发送的消息
                        changgeServiceTime();
                        mMsgService.receiverMsg(jo, jo.getInt("server_code"));
                        break;
                }
            }
        } catch (Exception e) {
        }
    }


    public static void actionSart(Context ctx) {
        ctx.startService(new Intent(ctx, CoreServiceB.class));
    }

    public class CoreServiceBinder extends Binder {
        public CoreServiceB getService() {
            return CoreServiceB.this;
        }
    }


    /**
     * 尝试重连
     */
    private synchronized void scheduleReconnect() {
        acquireWakeLock();//禁止cpu挂掉
        if (HTalkApplication.getApplication().isNetWork()) {
            Intent intent = new Intent();
            intent.setClass(this, CoreServiceB.class);
            intent.setAction(ACTION_RECONNECT);
            startService(intent);
            iS_RETRY_INTERVAL = true;
        }

    }

    /**
     * 尝试重连
     *
     * @param ctx
     */
    public static void login(Context ctx) {
        Intent intent = new Intent();
        intent.setClass(ctx, CoreServiceB.class);
        intent.setAction(ACTION_RECONNECT);
        ctx.startService(intent);
    }

    /**
     * 退出重连
     * 意思是 当自动掉线后,会每隔6s进行一次连接,连接成功,就不用重连了
     * 用户成功登陆  退出 都会退出重连
     */
    public synchronized void cancelReconnect() {
        //LogUtil.d(TAG, "CoreServiceB==退出重连");
        mHandler.removeMessages(RETRY_INTERVAL);
        iS_RETRY_INTERVAL = false;
    }

    /**
     * 保持活动
     */
    private synchronized void startKeepConnection() {
        acquireWakeLock();//禁止cpu挂掉
        if (!iS_RETRY_INTERVAL) {//如果没有在重连状态 会继续重连
            Intent i = new Intent();
            i.setClass(this, CoreServiceB.class);
            i.setAction(ACTION_KEEPALIVE);
            startService(i);
            keepCountTimeOut++;
        }
    }


    /**
     * 断开保存连接
     */
    private synchronized void stopKeepConnection() {
        //LogUtil.d(TAG, "===========断开保持连接");
        releaseWakeLock();
        mHandler.removeMessages(CORESERVICE_KEEP_CONNECTION);
    }

    /**
     * 保持活动
     * 备注  :  首先判断,是否已经三次没有收到消息,意味着已经掉线,需要重新登录
     * 否则 相反
     */
    private synchronized void sendtKeepAlive() {

        if (keepCountTimeOut < 3) {
            HeartServiceManagerB.instance().sendHeartMsg();
            //LogUtil.d(TAG, "=========CoreServiceB==onStart=intent=发送保持连接请求ing----->");
            mHandler.sendEmptyMessageDelayed(CORESERVICE_KEEP_CONNECTION, SocketCode.CONNECTION_PING);
        } else {
            keepCountTimeOut = 0;
            // 关闭保持连接
            stopKeepConnection();
            scheduleReconnect();
        }
    }


    /**
     * 获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
     */
    private void acquireWakeLock() {
        if (null == wakeLock) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK
                    | PowerManager.ON_AFTER_RELEASE, getClass()
                    .getCanonicalName());
            if (null != wakeLock) {
                wakeLock.acquire();
            }
        } else if (!wakeLock.isHeld()) {
            wakeLock.acquire();
        }
    }


    private void releaseWakeLock() {
        if (null != wakeLock && wakeLock.isHeld()) {
            wakeLock.release();
            wakeLock = null;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopConnection();

    }

    /**
     * 更新超时时间
     */
    public void changgeServiceTime() {
        if (!HStringUtil.isEmpty(HTalkApplication.getApplication().getmTalkServiceId())) {
            mHandler.removeMessages(TALK_WITH_SERVICE_TIMEOUT);
            mHandler.sendEmptyMessageDelayed(TALK_WITH_SERVICE_TIMEOUT, TALKTIMEOUT_VALUE);
        }
    }

    public void sendToastContent(String content) {
        Message msg = Message.obtain();
        msg.what = TOAST_CONTENT;
        msg.obj = content;
        mHandler.sendMessage(msg);
    }
}
