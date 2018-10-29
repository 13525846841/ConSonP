package com.yksj.healthtalk.services;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yksj.consultation.adapter.ChatAdapter.ViewHolder;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.AppData;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.avchat.login.LogoutHelper;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.healthtalk.bean.VideoEvent;
import com.yksj.healthtalk.db.ChatUserHelper;
import com.yksj.healthtalk.entity.MessageEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.ObjectHttpResponseHandler;
import com.yksj.healthtalk.net.socket.HeartServiceManager;
import com.yksj.healthtalk.net.socket.LoginEvent;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.net.socket.SmartControlClient;
import com.yksj.healthtalk.net.socket.SmartControlClient.RequestCode;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.net.socket.XsocketHanlder;
import com.yksj.healthtalk.utils.DialogUtils;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.LogUtil;
import com.yksj.healthtalk.utils.ThreadManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.ImageLoader;
import org.universalimageloader.utils.StorageUtils;
import org.xsocket.connection.INonBlockingConnection;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * 系统核心服务
 *
 * @author origin
 */
@SuppressLint("HandlerLeak")
public class CoreService extends Service implements XsocketHanlder.XsocketHanlderListeren {


    public static final String TAG = "CoreService";
    public static final String PREF_STARTED = "isStarted";
    public static final String ACTION = "com.yksj.healthtalk.services.CoreService";
    public static final String ACTION_STOP = "CoreService.Stop";//停止
    public static final String ACTION_EXCEPTION_STOP = "CoreService.Exception.Stop";//异常停止
    public static final String ACTION_START = "CoreService.Start";//启动
    public static final String ACTION_RECONNECT = "CoreService.Reconnect";//连接尝试
    public static final String ACTION_KEEPALIVE = "CoreService.Keepalive";//保持连接
    public static final String ACTION_LOGOUT = "CoreService.Logout";//登出
    public static final String ACTION_LOGING = "CoreService.Loging";//登录
    public static final String ACTION_VIDEO = "CoreService.video";//视频提醒
    public static final String ACTION_VIDEO_REMOVE = "CoreService.video.remove";//视频提醒清除
    public static final String ACTION_LOAD_OFFLINE_MSG = "ACTION_LOAD_OFFLINE_MSG";//加载离线消息

    public static final String ACTION_REPEAT_STATE = "com.yksj.consultation.ui.ACTION_REPEAT_STATE";// 兴趣墙转发状态返回
    public static final String ACTION_DOCTOR_CHARGE_STATE = "com.yksj.consultation.ui.ACTION_DOCTOR_CHARGE_STATE";// 聊天中医生收费请求状态返回

    // public static final String ACTION_DOCTOR_CHARGE =
    // "com.yksj.consultation.ui.ACTION_DOCTOR_CHARGE";//聊天中医生收费
    public static final String ACTION_FORBIDDENLIST = "com.yksj.healthtalk.services.ForbiddenlistAction";// 禁言列表
    public static final String ACTION_USERINFO_LOADCOMPLETE = "com.yksj.ui.ACTION_USERINFO_LOADCOMPLETE";// 加载用户个人资料完成
    public static final String ACTION_GETPERSONAL_INFO = "com.yksj.ui.ACTION_GETPERSONAL_INFO";// 加载客户信息完成
    public static final String ACTION_OFFLINE_MESSAGE = "com.yksj.ui.ACTION_OFFLINE_MESSAGE";// 离线消息加载完成通知
    public static final String ACTION_STARTMUSIC_BACKGROUND = "com.yksj.ui.ACTION_STARTMUSIC_BACKGROUND";
    public static final String ACTION_COLLECT_FRIEND = "com.yksj.ui.FriendInfo";
    public static final String ACTION_LOGIN = "com.yksj.healthtalk.services.LoginAction";// 登陆
    public static final String ACTION_COLLECT_GROUP = "com.yksj.ui.ACTION_COLLECT_GROUP";
    public static final String ACTION_JOIN_GROUP = "com.yksj.healthtalk.ACTION_JOIN_GROUP";// 加入聊天室通知
    public static final String ACTION_DOCTOR_INFO_AUDIT = "com.yksj.ui.ACTION_DOCTOR_INFO_AUDIT"; // 医师资料审核
    public static final String ACTION_CONNECTION_LOST = "com.yksj.ui.ACTION_CONNECTION_LOST"; // 连接丢失
    public static final String ACTION_COLLECT_GROUP_NOT = "com.yksj.ui.ACTION_COLLECT_GROUP_NOT";
    public static final String ACTION_FRIENDLIST = "com.yksj.ui.friendList";
    public static final String ACTION_MESSAGE_JOINROOM = "com.yksj.healthtalk.services.MessageaJoinRoomAction";// 加入房间通知
    public static final String ACTION_GROUP_ONLINE = "com.yksj.ui.ACTION_GROUP_ONLINE"; // 群在线成员
    public static final String ACTION_SEARCH_FRIEND_CON = "com.yksj.ui.FriendSearchCon";
    public static final String ACTION_GROUP_INVITE = "com.yksj.ui.ACTION_GROUP_INVITE"; // 群邀请
    public static final String ACTION_EXIT = "com.yksj.ui.EXIT";// 注销
    public static final String ACTION_GROUPLIST = "com.yksj.ui.groupList";
    public static final String ACTION_MODIFY_PERSONIF = "com.yksj.Health.PersonIn";// 修改个人资料
    public static final String ACTION_MESSAGE = "com.yksj.healthtalk.services.MessageaAction";// 新消息通知
    public static final String ACTION_CONTENT_MESSAGE = "com.yksj.healthtalk.services.MessageContentAction";// 主页消息提示
    public static final String ACTION_XSOCKET_HANDLER = "action_xsocket_handler";// Xsocket 回调

    public static final String ACTION_PAY_MESSAGE = "com.yksj.healthtalk.services.PayMessageaAction";// 支付
    public static final String ACTION_LEAVE_WORD = "com.yksj.healthtalk.services.leaveWordsAction";// 离线留言
    public static final String ACTION_CHATTINGONLINEFRIEND = "com.yksj.ui.online.friend";// 话题在线成员
    public static final String ACTION_COMMONT_CONTENT = "com.yksj.healthtalk.services.commont_content";//好评
    public static final String ACTION_VIDEO_TIP = "com.yksj.healthtalk.services.videotip";
    public static final String ACTION_VIDEO_TIP_END = "com.yksj.healthtalk.services.videotipend";
    private static final int WHAT_OTHERPLACE_LOGIN = -1;// 异地登陆
    private static final int WHAT_FORCELOGIN = -2;// 强制重新登录
    public static final String PARAME_KEY = "parame"; // 广播传值key
    public static final String BROAD_KEY = "result"; // 广播传值key
    //	public static final String ACTION_FRAME_VIEW_VISIABLE = "frame_view_visiable";//悬浮框显示
//	public static final String ACTION_FRAME_VIEW_GONE = "frame_view_gone";//悬浮框隐藏
    public static final int CORESERVICE_KEEP_CONNECTION = 5000;//保持连接
    public static final int RETRY_INTERVAL = 6000;//尝试重连
    public static final int VIDEOTIMETIP = 7000;//视频咨询倒计时提醒
    public static final int VIDEOTIMEEND = 7001;//视频咨询倒计时视频
    public static final int VIDEOTIMEREMOVE = 7002;//视频咨询倒计时清除
    public static final String MESSAGE_STATUS = "chat.message.status";//聊天消息的发送状态
    private boolean mStarted;// 记录状态
    private final DecimalFormat mFormat = new DecimalFormat("0.0");
    protected final CoreServiceBinder mServiceBinder = new CoreServiceBinder();
    private SmartControlClient mControlClient;
    private HTalkApplication mApplication;
    private SharedPreferences mServicePre;// 记录service状态
    private AppData mAppData;
    private WakeLock wakeLock = null; //cpu 唤醒锁
    private int keepCountTimeOut = 0;//3  表示需要重连


    public static final String XSOCKET_LOGIN_SUCCESS = "Xsocket.LoginSuccess";//成功登陆
    public static final String XSOCKET_LOGIN_ERROR = "Xsocket.Error";//错误
    private long videoTime = 0L;
    private long videoTipTime = 0L;
    /**
     * 异步更新ui线程
     */
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RequestCode.CHATTING_MESSAGE_SENDSTATE:
                    final MessageEntity entity = (MessageEntity) msg.obj;
                    onUpdateMesgeState(entity);
                    break;
                //弹出框
                case RequestCode.CODE_PRODUCT_CHANGE:

//				onShowProductStateChangeDialog(str);
                    break;
                //异地登录
                case RequestCode.LOGIN_OTHERPLACE:
                    LogUtil.d(TAG, "=====异地登录====");
                    actionLogout(mApplication);
                    DialogUtils.showLoginOutDialog2(mApplication, mApplication.getResources().getString(R.string.login_agin));
                    break;
                //发送超时
                case 3000:
                    final MessageEntity entity1 = (MessageEntity) msg.obj;
                    if (entity1.getSendState() == MessageEntity.STATE_PROCESING) {
                        entity1.setSendState(MessageEntity.STATE_FAIL);
                        onUpdateMesgeState(entity1);
                    }
                    break;
                case 4000://好评弹出
                    Intent intent = new Intent(ACTION_COMMONT_CONTENT);
                    sendBroadcast(intent);
                    break;
                case CORESERVICE_KEEP_CONNECTION://保持连接
                    startKeepConnection();
                    break;
                case RETRY_INTERVAL://尝试重连
                    scheduleReconnect();
                    break;
                case VIDEOTIMETIP://视频咨询倒计时提醒
                    mHandler.sendEmptyMessageDelayed(VIDEOTIMEEND, videoTipTime);
                    EventBus.getDefault().post(new VideoEvent(videoTipTime + "", VideoEvent.TIP));
                    break;
                case VIDEOTIMEEND://视频咨询关闭
                    EventBus.getDefault().post(new VideoEvent("", VideoEvent.END));
                    break;
                case VIDEOTIMEREMOVE://视频咨询倒计时清除
                    mHandler.removeMessages(VIDEOTIMETIP);
                    mHandler.removeMessages(VIDEOTIMEEND);
                    break;
            }
        }
    };

    /**
     * 全局提示框
     */
    Dialog mDialog;
    private LoginServiceManeger mLoginCtrol;
    private boolean mIsLogining;

    private void onShowProductStateChangeDialog(String content) {
        if (mDialog == null) {
            final Dialog dialog = new Dialog(this, R.style.dialog);
            dialog.setCanceledOnTouchOutside(false);
            View view = LayoutInflater.from(this).inflate(R.layout.dialog, null);
            Button button = (Button) view.findViewById(R.id.dialog_btn);
            button.setText(R.string.sure);
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            DisplayMetrics display = getResources().getDisplayMetrics();
            int width = display.widthPixels - 50;
            int height = LayoutParams.WRAP_CONTENT;
            dialog.setContentView(view, new LayoutParams(width, height));
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            mDialog = dialog;
        }
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
        ((TextView) mDialog.findViewById(R.id.dialog_text)).setText(content);
        mDialog.show();
    }


//	private final IEventListener mEventListener = new IEventListener() {
//		@Override
//		public void dispatch(BaseEvent event) throws SFSException {
//			final String type = event.getType();
//			LogUtil.d(TAG, "=====dispatch====" + event.getType());
//			if (SFSEvent.EXTENSION_RESPONSE.equalsIgnoreCase(type)) {
//				SocketParams isfsObject = (SocketParams) event.getArguments().get(
//						"params");
//				String cmd = event.getArguments().get("cmd").toString();
//				if (SmartFoxClient.EXTENTION_SMS_SYS_INITIALIZATION.equals(cmd)) {
//					onExtensionEventHandler(isfsObject);
//				}
//			} else if (SFSEvent.CONNECTION.equalsIgnoreCase(type) || SFSEvent.CONNECTION_RESUME.equalsIgnoreCase(type)) {
//				setStarted(true);
//				boolean isSuccess  = false;
//				if(SFSEvent.CONNECTION.equalsIgnoreCase(type)){
//					 isSuccess = (Boolean) event.getArguments().get("success");
//					LogUtil.d(TAG, "=====dispatch====连接成功event.getArguments().get(success)" +isSuccess);
//				}else{
//					isSuccess = true;
//					LogUtil.d(TAG, "=====dispatch====连接成功" +isSuccess);
//				}
//
//				// 连接成功操作
//				Intent intent = new Intent(SFSEvent.LOGIN);
//				intent.putExtra("state", isSuccess ? 1 : -1);
//				sendBroadcast(intent);
//				// 连接成功，自动登录
//				if (isSuccess && mControlClient.getLoginState() != -1) {
//					mControlClient.login();
//					LogUtil.d(TAG, "=====dispatch====连接成功,开始登陆");
//				}
//			} else if (SFSEvent.CONNECTION_LOST.equalsIgnoreCase(type)) {////接收连接服务器失败事件
//				// 关闭保持连接
//				stopKeepConnection();
//				if (mApplication.isNetWork() && mControlClient.isLogined()) {
//					//尝试重新连接
//					mHandler.sendEmptyMessageDelayed(RETRY_INTERVAL,SmartControlClient.INITIAL_RETRY_INTERVAL);
//				}
//			} else if (SFSEvent.CONNECTION_RETRY.equalsIgnoreCase(type)) {//接收重试服务器连接事件
//				ISFSObject isfsObject = (ISFSObject) event.getArguments().get("params");
//			onExtensionEventHandler(isfsObject);
//
//			} else if (SFSEvent.LOGIN.equalsIgnoreCase(type)) {
//				Intent intent = new Intent(SFSEvent.LOGIN);
//				intent.putExtra("state", 2);
//				sendBroadcast(intent);
//				mControlClient.setLoginState(LoginEvent.LOGIN_OK);
//				mControlClient.sendJoinRoom();
//			} else if (SFSEvent.LOGIN_ERROR.equalsIgnoreCase(type)) {
//				if(mControlClient.getLoginState() == LoginEvent.LOGIN_OK && event.getArguments().containsKey("errorCode") && "6".equalsIgnoreCase(event.getArguments().get("errorCode").toString())){
//					//异地登录
//					mHandler.sendEmptyMessage(RequestCode.LOGIN_OTHERPLACE);
//					mControlClient.setLoginState(LoginEvent.NONE);
//				}else{
//					LogUtil.d(TAG, "=====SFSEvent.LOGIN_ERROR====");
//					mControlClient.setLoginState(LoginEvent.NONE);
//					Intent intent = new Intent(SFSEvent.LOGIN);
//					Map<String, Object> map = event.getArguments();
//					String code = String.valueOf(map.get("errorCode"));
//					String errorMessage = (String) (map.get("errorMessage"));
//					if ("3".equals(code)) {// 用户名或者密码错误
//						intent.putExtra("state", 3);
//						intent.putExtra("errormsg", "账号或者密码错误");
//					}else if(errorMessage.startsWith("User")){//异地登录
//
//					} else if ("6".equals(code)) {// 需要重新登录
//						intent.putExtra("state", 6);
//						intent.putExtra("errormsg", errorMessage);
//					} else {
//						intent.putExtra("state", 7);
//						intent.putExtra("errormsg", errorMessage);
//					}
//					try {
//						sendBroadcast(intent);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					LogUtil.d(TAG,intent.toString());
//				}
//			} else if (SFSEvent.LOGOUT.equalsIgnoreCase(type)) {
//				mControlClient.disconnect();
//			} else if (SFSEvent.ROOM_JOIN_ERROR.equalsIgnoreCase(type)) {
//
//			} else if (SFSEvent.ROOM_JOIN.equalsIgnoreCase(type)) {
//				if (mControlClient.isLogined()) {
//					cancelReconnect();//退出重连
//					keepCountTimeOut=0;
//					mHandler.sendEmptyMessageDelayed(CORESERVICE_KEEP_CONNECTION, SmartControlClient.CONNECTION_PING);
//					mControlClient.sendChangeNumber();
//					mControlClient.sendLoadUserInfo();
//				}else{
//				}
//			} else if (SFSEvent.HANDSHAKE.equalsIgnoreCase(type)) {
//			}
//		}
//	};
    /**
     * 注册通知
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            // 网络连接
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                // 重新连接
                if (mApplication.isNetWork()) {
                    reconnectIfNecessary();
                } else {
                    // 掉线处理
                    cancelReconnect();
                }
            }
        }

        ;
    };

    private boolean iS_RETRY_INTERVAL = false;//是否正在重连操作  true 表示是

    @Override
    public void XsocketonConnect(INonBlockingConnection arg0) {
        setStarted(true);
        mIsLogining = true;
        LogUtil.d(TAG, "=====dispatch====连接成功");
        // 连接成功，自动登录
        if (mIsLogining && mLoginCtrol.getLoginState() != LoginEvent.NONE) {
            login();
            LogUtil.d(TAG, "=====dispatch====连接成功,开始登陆");
            mIsLogining = false;
        }
    }

    @Override
    public void XsocketonDisconnect(INonBlockingConnection arg0) {
        EventBus.getDefault().post(new MyEvent("服务器登陆超时", 20));
        mIsLogining = false;
    }

    //返回数据
    @Override
    public void XsocketonData(INonBlockingConnection arg0) {
        try {
            //为保持长连接使用
            LogUtil.d(TAG, "===CoreService===收到第" + keepCountTimeOut + "次响应");
            keepCountTimeOut = 0;
            String data = arg0.readStringByDelimiter(SmartControlClient.BYDELIMITER);
            JSONObject jo = new JSONObject(data);
            if (jo.optInt("response_type") == 1) {
                LogUtil.d(TAG, "===CoreService===心跳");
                return;
            } else {
                Logger.json(jo.toString());
            }

            switch (jo.getInt("server_code")) {//登陆
                case SmartControlClient.LOGIN_CODE:
                    LoginServiceManeger.instance().setLoginInfo(jo);
                    break;
                case SmartControlClient.LOGIN_OUT://登出
                    if (LoginServiceManeger.instance().loginState == LoginEvent.LOGINING) ;
                    LoginServiceManeger.instance().login();
                    break;
                case SmartControlClient.CHATTING_MESSAGE_SENDSTATE://发送消息返回
                    onHandlerMesgeSendState(jo);
                    //
                    String customerId = jo.optString("customerId");//发送者id
                    String orderId = jo.optString("order_id");//发送者id
                    Intent intent = new Intent(MessagePushService.ACTION_MESSAGE);
                    intent.putExtra("senderId", customerId);//发送者id
                    intent.putExtra("order_id", orderId);//发送者id
                    sendBroadcast(intent);
                    break;
                case SmartControlClient.CHATTING_SINGLE_MESSAGE_RECEIVE:
                case SmartControlClient.SERVICE_SINGLE_RECIEVE_MSG://服务单聊
                    EventBus.getDefault().post(new MyEvent(data, 10));
                    onReceiveChatMesg(jo);
                    showMessageTip(data);
                    break;
                case SmartControlClient.CHATTING_GROUP_MESSAGE_RECEIVE:
                case SmartControlClient.SIX_ONE_RECIEVE_MSG://群聊
                    EventBus.getDefault().post(new MyEvent(data, 10));
                    onReceiveChatMesg(jo);
                    showMessageTip(data);
                    break;
                case SmartControlClient.ORDER_CHANGE_STATE://订单状态变化
                    EventBus.getDefault().post(new MyEvent(data, 11));
                    showMessageTip(data);
                    break;
                case SmartControlClient.SERVICE_SINGLE_SYS_MSG://特殊服务提示
                    EventBus.getDefault().post(new MyEvent(data, SmartControlClient.SERVICE_SINGLE_SYS_MSG));
                    showMessageTip(data);
                    break;
                case SmartControlClient.CON_EXPERT_ACCEPT:
                    showMessageTip(data);
                    break;
                case SmartControlClient.SERVICE_SINGLE_SYS_MSG_TIP://特殊服务比例提示
                    EventBus.getDefault().post(new MyEvent(data, SmartControlClient.SERVICE_SINGLE_SYS_MSG_TIP));
                    showMessageTip(data);
                    break;
                case SmartControlClient.CHAT_ORDER_CHANGE_STATE://订单状态变化
                    EventBus.getDefault().post(new MyEvent(data, 10));
                    String customer_Id = jo.optString("customerId");//发送者id
                    Intent intents = new Intent(MessagePushService.ACTION_MESSAGE);
                    intents.putExtra("senderId", customer_Id);//发送者id
                    sendBroadcast(intents);
                    showMessageTip(data);
                    break;
            }

        } catch (Exception e) {
            System.out.println("---------" + e.toString());
        }


    }


    public class CoreServiceBinder extends Binder {
        public CoreService getService() {
            return CoreService.this;
        }
    }

    public static void actionStart(Context ctx) {
        Intent i = new Intent(ctx, CoreService.class);
        i.setAction(ACTION_START);
        ctx.startService(i);
    }

    public static void actionStop(Context ctx) {
        Intent i = new Intent(ctx, CoreService.class);
        i.setAction(ACTION_STOP);
        ctx.startService(i);
    }

    public static void actionExceptionStop(Context ctx) {
        Intent i = new Intent(ctx, CoreService.class);
        i.setAction(ACTION_EXCEPTION_STOP);
        ctx.startService(i);
    }

    /**
     * 登出
     *
     * @param context
     */
    public static void actionLogout(Context context) {
        LogoutHelper.logout();
        Intent i = new Intent(context, CoreService.class);
        i.setAction(ACTION_LOGOUT);
        context.startService(i);

    }


    /**
     * 登陆
     *
     * @param context
     */
    public static void actionLogin(Context context) {
        Intent i = new Intent(context, CoreService.class);
        i.setAction(ACTION_LOGING);
        context.startService(i);
    }


    /**
     * 登陆成功 并且加载完资料
     *
     * @param context
     */
    public static void actionLoginSuccess(Context context) {
        Intent i = new Intent(context, CoreService.class);
        i.setAction(XSOCKET_LOGIN_SUCCESS);
        context.startService(i);
    }

    /**
     * 登陆错误
     *
     * @param context 1 表示异地登陆 2,表示登陆密码错误
     */
    public static void actionLoginError(Context context, JSONObject statues) {
        Intent i = new Intent(context, CoreService.class);
        i.putExtra("content", statues.toString());
        i.setAction(XSOCKET_LOGIN_ERROR);
        context.startService(i);
    }


    @Override
    public void onCreate() {
        super.onCreate();

        LogUtil.d(TAG, "===CoreService==onCreate");

		/*startForeground(Notification.FLAG_FOREGROUND_SERVICE,
                new Notification());*/

        mApplication = HTalkApplication.getApplication();
        mAppData = mApplication.getAppData();
        XsocketHanlder mXsocketHanlder = new XsocketHanlder();
        mControlClient = SmartControlClient.init(mXsocketHanlder);
        mXsocketHanlder.setXsocketHanlderListeren(this);
        mServicePre = getSharedPreferences(TAG, MODE_PRIVATE);
        initService();
    }


    private void initService() {
        mLoginCtrol = LoginServiceManeger.instance();
        mLoginCtrol.onStartIMManager(mApplication);
        HeartServiceManager.instance();


        //上一次异常退出
        handleCrashedService();
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.d(TAG, "===CoreService===onBind");
        return mServiceBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            LogUtil.d(TAG,
                    "===CoreService===onStartCommand action=" + intent.getAction());
        } else {
            LogUtil.d(TAG,
                    "===CoreService===onStartCommand intent===为空");
        }
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        LogUtil.d(TAG, "===CoreService===onStart=intent=" + intent);

        if (intent == null || intent.getAction() == null)
            return;

        if (intent.getAction().equals(XSOCKET_LOGIN_SUCCESS)) {//登陆成功,并且资料加载完成
            cancelReconnect();//退出重连
            mLoginCtrol.setLoginState(LoginEvent.LOGIN_OK);//改变状态
            EventBus.getDefault().post(new MyEvent("成功", 1));
            mHandler.sendEmptyMessageDelayed(CORESERVICE_KEEP_CONNECTION, SmartControlClient.CONNECTION_PING);
        } else if (intent.getAction().equals(XSOCKET_LOGIN_ERROR)) {//登陆错误,
            if (mLoginCtrol.getLoginState() == LoginEvent.LOGIN_OK) {//异地登陆
                mHandler.sendEmptyMessage(RequestCode.LOGIN_OTHERPLACE);
            } else {//登陆错误
                try {
                    JSONObject jo = new JSONObject(intent.getStringExtra("content"));
                    EventBus.getDefault().post(new MyEvent(jo.optString("message"), Integer.valueOf(jo.optString("code"))));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mLoginCtrol.setLoginState(LoginEvent.NONE);//改变状态
        } else if (intent.getAction().equals(ACTION_STOP)) {
            LogUtil.d(TAG, "===CoreService==onStart=intent=" + intent);
            stop();
        } else if (intent.getAction().equals(ACTION_EXCEPTION_STOP)) {
            stop();
            stopSelf();

        } else if (intent.getAction().equals(ACTION_START) == true) {
            start();
        } else if (intent.getAction().equals(ACTION_KEEPALIVE) == true) {
            sendtKeepAlive();//发送保持连接请求
        } else if (intent.getAction().equals(ACTION_RECONNECT) == true) {
            // 重连操作
            if (mApplication.isNetWork() && mControlClient.getLoginState() != LoginEvent.NONE) {
                login();
                mHandler.sendEmptyMessageDelayed(RETRY_INTERVAL, SmartControlClient.INITIAL_RETRY_INTERVAL);
            }
            //登录出
        } else if (intent.getAction().equals(ACTION_LOGOUT)) {
            logout();
            //登录
        } else if (intent.getAction().equals(ACTION_LOGING)) {
            LogUtil.d(TAG, "===CoreService==onStart=intent=" + intent);
            login();
        } else if (intent.getAction().equals(ACTION_VIDEO)) {//视频计时开始
//            videoTime = intent.getLongExtra(ACTION_VIDEO_TIP, 600000L);
//            videoTipTime = intent.getLongExtra(ACTION_VIDEO_TIP_END, 60000L);
            videoTime = intent.getLongExtra(ACTION_VIDEO_TIP, 30000L);
            videoTipTime = intent.getLongExtra(ACTION_VIDEO_TIP_END, 5000L);
            mHandler.sendEmptyMessageDelayed(VIDEOTIMETIP, videoTime);
        } else if (intent.getAction().equals(ACTION_VIDEO_REMOVE)) {//视频计时清除
            mHandler.sendEmptyMessage(VIDEOTIMEREMOVE);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isStarted()) {
            stop();
        }
        LogUtil.d(TAG, "===CoreService==onDestroy");
//
//		try {
//			WindowManager wm = (WindowManager)getApplicationContext().getSystemService("window");
//			if(mFrameView!=null)
//				wm.removeView(mFrameView);
//		} catch (Exception e) {
//		}
    }

    /**
     * 异常重启处理
     */
    private synchronized void handleCrashedService() {
        if (isStarted()) {
            stopKeepConnection();
            start();
        }
    }

    private synchronized boolean isStarted() {
        return mServicePre.getBoolean(PREF_STARTED, false);
    }

    private synchronized void setStarted(boolean started) {
        mServicePre.edit().putBoolean(PREF_STARTED, started).commit();
        mStarted = started;
    }

    private synchronized void reconnectIfNecessary() {
        if (mStarted && mControlClient.isLogined()) {
            LogUtil.d(TAG, "CoreService==尝试重新连接");
            mHandler.sendEmptyMessage(RETRY_INTERVAL);
        }
    }

    private synchronized void start() {
        registerNotificationReceiver();
    }

    private synchronized void stop() {
        setStarted(false);
        stopKeepConnection();
        cancelReconnect();
        unregisterNotificationReceiver();
        mControlClient.disconnect();
    }

    private synchronized void logout() {
        mIsLogining = false;
        mLoginCtrol.loginOut();
        stopKeepConnection();
        cancelReconnect();
    }

    private void login() {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                mLoginCtrol.login();
            }
        });
    }

    /**
     * 尝试重连
     */
    private synchronized void scheduleReconnect() {
        acquireWakeLock();//禁止cpu挂掉
        LogUtil.d(TAG, "CoreService== 尝试重连");
        Intent intent = new Intent();
        intent.setClass(this, CoreService.class);
        intent.setAction(ACTION_RECONNECT);
        startService(intent);
        iS_RETRY_INTERVAL = true;
//		PendingIntent pi = PendingIntent.getService(this, 0, intent, 0);
//		long triggerAtTime = SystemClock.elapsedRealtime()  + SmartControlClient.INITIAL_RETRY_INTERVAL;
//		AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
//		alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
    }

    /**
     * 退出重连
     * 意思是 当自动掉线后,会每隔6s进行一次连接,连接成功,就不用重连了
     * 用户成功登陆  退出 都会退出重连
     */
    public synchronized void cancelReconnect() {
        LogUtil.d(TAG, "CoreService==退出重连");
        mHandler.removeMessages(RETRY_INTERVAL);
        iS_RETRY_INTERVAL = false;
        //		Intent i = new Intent();
//		i.setClass(this, CoreService.class);
//		i.setAction(ACTION_RECONNECT);
//		PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
//		AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
//		alarmMgr.cancel(pi);
    }

    /**
     * 保持活动
     */
//	private synchronized void startKeepAlives() {
//		LogUtil.d(TAG, "CoreService==保持活动");
//		Intent i = new Intent();
//		i.setClass(this, CoreService.class);
//		i.setAction(ACTION_KEEPALIVE);
//		PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
//		AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
//		long triggerAtTime = SystemClock.elapsedRealtime()+ SmartControlClient.CONNECTION_PING;
//		alarmMgr.setInexactRepeating(
//				AlarmManager.ELAPSED_REALTIME_WAKEUP,
//				triggerAtTime, 
//				SmartControlClient.CONNECTION_PING,
//				pi);
//	}

    /**
     * 保持活动
     */
    private synchronized void startKeepConnection() {
        acquireWakeLock();//禁止cpu挂掉
        if (!iS_RETRY_INTERVAL) {//如果没有在重连状态 会继续重连
            Intent i = new Intent();
            i.setClass(this, CoreService.class);
            i.setAction(ACTION_KEEPALIVE);
            startService(i);
            keepCountTimeOut++;
            LogUtil.d(TAG, "===CoreService===startKeepConnection-----" + keepCountTimeOut + "次发送保持连接->>");
        }
    }


    /**
     * 断开保存连接
     */
    private synchronized void stopKeepConnection() {
        LogUtil.d(TAG, "CoreService==断开保持连接");
        releaseWakeLock();
        mHandler.removeMessages(CORESERVICE_KEEP_CONNECTION);
        mHandler.removeMessages(4000);//移除好评弹出框
    }


    /**
     * 保持活动
     * 备注  :  首先判断,是否已经三次没有收到消息,意味着已经掉线,需要重新登录
     * 否则 相反
     */
    private synchronized void sendtKeepAlive() {
        if (keepCountTimeOut < 3) {
            mControlClient.sendKeepConnect();
            LogUtil.d(TAG, "===CoreService===sendtKeepAlive");
            mHandler.sendEmptyMessageDelayed(CORESERVICE_KEEP_CONNECTION, SmartControlClient.CONNECTION_PING);
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

    private synchronized void registerNotificationReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, filter);
    }

    private void unregisterNotificationReceiver() {
        unregisterReceiver(mReceiver);
    }

    /**
     * 解析登录用户
     * @param isfsObject
     * @throws Exception
     */
//	private void onParseLoginUserInfo(SocketParams isfsObject) throws Exception {
//		boolean isShowProtocol = false;// 是否显示协议
//		String parame = isfsObject.getUtfString(SmartFoxClient.KEY_PARAME);
//		//消息切换时间
//		if(isfsObject.containsKey("showTime"))
//		HTalkApplication.getAppData().messageShowTime= isfsObject.getUtfString("showTime");
//		LogUtil.d(TAG, "=====dispatch====更新资料");
//		com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(parame);
//		String protocol = jsonObject.getString("toShowCustomerProtocol");
//		isShowProtocol = "1".equals(protocol);
//		CustomerInfoEntity entity = DataParseUtil
//				.jsonToCustomerInfo(jsonObject.toString());
//		mControlClient.setCustomerInfoEntity(entity);
//		// 发送登录通知
//		Intent intent = new Intent(SFSEvent.LOGIN);
//		//登录跳转到指定商户
//		String merchantId = jsonObject.getString("merchantId");
//		if(merchantId != null && merchantId.trim().length() != 0)
//			intent.putExtra("merchantId",merchantId);
//
//		intent.putExtra("state", 0);
//		intent.putExtra("isShowProtocol", isShowProtocol);
//		sendBroadcast(intent);
//
//		//保存登录用户信息
//		SharePreUtils.saveLoginUserInfo(parame);
//		loadingSalonOrGroupInfo();
//
//		if(RandomUtils.nextBoolean() && !SharePreUtils.feachCommentGood())//随机 50% 的记录
//			mHandler.sendEmptyMessageDelayed(4000,SmartControlClient.COMMENT_TIME_AUTO);
//	}

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

    /**
     * 加载离线消息
     */
//	private void onLoadOffMessge() {
//		HttpRestClient.doHttpLoadOffMessage(SmartFoxClient.getLoginUserId(),
//				new ObjectHttpResponseHandler() {
//					@Override
//					public Object onParseResponse(String content) {
//						if(!mControlClient.isLogined()) return null;
//						synchronized (mAppData) {
//							JsonParseUtils.parseOffMessage(content);
//							int size = mApplication.getNoReadMesgSize();
//							if(size > 0){
//								mApplication.showNotify("未读消息("+size+")条");
//								sendContentMesgBroad("未读消息("+size+")条");
//							}
//						}
//						return null;
//					}
//					public void onSuccess(Object response) {
//						LogUtil.d(TAG, "=====加载离线消息 成功====");
//					};
//					public void onFinish() {
//						LogUtil.d(TAG, "=====加载离线消息 结束====");
//					};
//					public void onFailure(Throwable error) {
//						if(mControlClient.getLoginState() ==LoginEvent.LOGIN_OK)
//						onLoadOffMessge();
//						LogUtil.d(TAG, "=====加载离线消息 失败====");
//						if(error!=null)
//						LogUtil.d(TAG, "=====加载离线消息 失败====原因"+error.toString());
//					};
//				});
//	}

    /**
     * 加入到聊天室通知
     */
    private void sendChattingJoinGroupBroad(String value) {
        Intent intent = new Intent(MessagePushService.ACTION_JOIN_GROUP);
        intent.putExtra("value", value);
        getApplicationContext().sendBroadcast(intent);
    }


    /**
     * 聊天语音发送
     *
     * @author zhao
     */
    class ChatVoiceUploadHttpHandler extends ObjectHttpResponseHandler {
        final MessageEntity mEntity;
        final boolean mIsGroupChat;
        int sendState = 0;//发送状态

        public ChatVoiceUploadHttpHandler(MessageEntity entity, boolean isGroupChat) {
            mEntity = entity;
            mIsGroupChat = isGroupChat;
        }

        @Override
        public Object onParseResponse(String cotent) {
            try {
                if ("0".equals(cotent)) {
                    sendState = MessageEntity.STATE_FAIL;
                    mEntity.setSendState(sendState);
                } else {
                    JSONObject response = new JSONObject(cotent);
//					String serverId = response.getString("serverId");
                    String sid = response.getString("sid");

                    String dataHolder = response.getString("dataHolder");
                    File file = new File(StorageUtils.getVoicePath(), mEntity.getContent());
                    String filePath = file.getParent();
                    String name = StorageUtils.getFileName(dataHolder);
                    file.renameTo(new File(filePath, name));
                    mEntity.setContent(name);
                    sendState = MessageEntity.STATE_OK;
                    mEntity.setSendState(sendState);
                    mAppData.removeSendMesgeCache(mEntity);
                    if (!HStringUtil.isEmpty(sid)) mEntity.setId(sid);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                sendState = MessageEntity.STATE_FAIL;
            }
            mEntity.setSendState(sendState);
            return null;
        }

        @Override
        public void onSuccess(Object response) {
            super.onSuccess(response);
            ChatUserHelper.getInstance().insertChatMessageFromSelf(mEntity, mIsGroupChat);
        }

        @Override
        public void onFinish() {
            mAppData.removeSendMesgeCache(mEntity);
            onUpdateMesgeState(mEntity);
        }

        @Override
        public void onFailure(Throwable error, String content) {
            mEntity.setSendState(sendState);
        }
    }

    /**
     * 聊天图片发送
     *
     * @author zhao
     */
    class ChatImageUploadHttpHandler extends ObjectHttpResponseHandler {
        final MessageEntity mEntity;
        final boolean mIsGroupChat;
        int sendState = 0;//发送状态

        public ChatImageUploadHttpHandler(MessageEntity entity, boolean isGroupChat) {
            this.mEntity = entity;
            this.mIsGroupChat = isGroupChat;
        }

        @Override
        public Object onParseResponse(String cotent) {
            try {
                if ("0".equals(cotent)) {
                    sendState = MessageEntity.STATE_FAIL;
                    mEntity.setSendState(MessageEntity.STATE_FAIL);
                } else {
                    JSONObject response = new JSONObject(cotent);
                    String serverId = response.getString("serverId");
                    String sid = response.getString("sid");
                    String dataHolder = response.getString("dataHolder");
                    String sendStatus = response.getString("sendStatus");
                    //上传完成之后需要重新命名
                    ImageLoader loader = ImageLoader.getInstance();
                    String rootPath = StorageUtils.getImagePath();
                    String oldPaths[] = mEntity.getContent().split("&");
                    String newPaths[] = dataHolder.split("&");
                    File rootFilePath = new File(rootPath);
                    //小文件重新命名
                    File oldFile = loader.getOnDiscFileName(rootFilePath, oldPaths[0]);
                    File newFile = loader.getOnDiscFileName(rootFilePath, newPaths[0]);
                    oldFile.renameTo(newFile);
                    //大文件重新命名
                    oldFile = loader.getOnDiscFileName(rootFilePath, oldPaths[1]);
                    newFile = loader.getOnDiscFileName(rootFilePath, newPaths[1]);
                    oldFile.renameTo(newFile);
                    mEntity.setContent(dataHolder);
                    sendState = "0".equals(sendStatus) ? MessageEntity.STATE_FAIL : MessageEntity.STATE_OK;
                    mAppData.removeSendMesgeCache(mEntity);
                    if (!HStringUtil.isEmpty(sid)) mEntity.setId(sid);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                sendState = MessageEntity.STATE_FAIL;
            }
            mEntity.setSendState(sendState);
            return null;
        }

        @Override
        public void onSuccess(int statusCode, Object response) {
            super.onSuccess(statusCode, response);
            ChatUserHelper.getInstance().insertChatMessageFromSelf(mEntity, mIsGroupChat);
        }

        @Override
        public void onFinish() {
            mAppData.removeSendMesgeCache(mEntity);
            onUpdateMesgeState(mEntity);
        }
    }


    /**
     * 聊天视频发送
     *
     * @author zhao
     */
    class ChatVideoUploadHttpHandler extends ObjectHttpResponseHandler {
        final MessageEntity mEntity;
        final boolean mIsGroupChat;
        int sendState = 0;//发送状态

        public ChatVideoUploadHttpHandler(MessageEntity entity, boolean isGroupChat) {
            this.mEntity = entity;
            this.mIsGroupChat = isGroupChat;
        }

        @Override
        public Object onParseResponse(String cotent) {
            try {
                if ("0".equals(cotent)) {
                    sendState = MessageEntity.STATE_FAIL;
                    mEntity.setSendState(MessageEntity.STATE_FAIL);
                } else {
                    JSONObject response = new JSONObject(cotent);
//                    ToastUtil.showShort(cotent);
                    String serverId = response.getString("serverId");
                    String sid = response.getString("sid");
                    String dataHolder = response.getString("dataHolder");
                    String sendStatus = response.getString("sendStatus");
//                    //上传完成之后需要重新命名
//                    ImageLoader loader = ImageLoader.getInstance();
//                    String rootPath = StorageUtils.getImagePath();
//                    String oldPaths[] = mEntity.getContent().split("&");
//                    String newPaths[] = dataHolder.split("&");
//                    File rootFilePath = new File(rootPath);
//                    //小文件重新命名
//                    File oldFile = loader.getOnDiscFileName(rootFilePath, oldPaths[0]);
//                    File newFile = loader.getOnDiscFileName(rootFilePath, newPaths[0]);
//                    oldFile.renameTo(newFile);
//                    //大文件重新命名
//                    oldFile = loader.getOnDiscFileName(rootFilePath, oldPaths[1]);
//                    newFile = loader.getOnDiscFileName(rootFilePath, newPaths[1]);
//                    oldFile.renameTo(newFile);
                    mEntity.setContent(dataHolder);
                    sendState = "0".equals(sendStatus) ? MessageEntity.STATE_FAIL : MessageEntity.STATE_OK;
                    mAppData.removeSendMesgeCache(mEntity);
                    if (!HStringUtil.isEmpty(sid)) mEntity.setId(sid);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                sendState = MessageEntity.STATE_FAIL;
            }
            mEntity.setSendState(sendState);
            return null;
        }

        @Override
        public void onSuccess(int statusCode, Object response) {
            super.onSuccess(statusCode, response);
            ChatUserHelper.getInstance().insertChatMessageFromSelf(mEntity, mIsGroupChat);
        }

        @Override
        public void onFinish() {
            mAppData.removeSendMesgeCache(mEntity);
            onUpdateMesgeState(mEntity);
        }
    }

    /**
     * 更新消息发送状态
     */
    private void onUpdateMesgeState(MessageEntity entity) {
        final int state = entity.getSendState();
        if (entity.viewHolder != null) {
            ViewHolder holder = entity.viewHolder.get();
            if (holder == null) return;
            CheckBox checkBox = holder.stateCheckbV;
            if (checkBox == null) return;
            if (state == MessageEntity.STATE_FAIL) {
                checkBox.setChecked(false);
                checkBox.setText("发送失败");
                checkBox.setVisibility(View.VISIBLE);
//				onSendBroadcast(MESSAGE_STATUS, entity.getId());
            } else if (state == MessageEntity.STATE_OK) {
                checkBox.setText("发送成功");
                checkBox.setChecked(true);
                checkBox.setVisibility(View.GONE);
                ChatUserHelper.getInstance().insertChatMessageFromSelf(entity, entity.getGroupType() > 0);
            } else if (state == MessageEntity.STATE_PROCESING) {
                checkBox.setText("发送中");
                checkBox.setChecked(false);
                checkBox.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 保存消息到缓存
     *
     * @param entity
     * @param isGroupChat
     */
    private void onSaveMesage(MessageEntity entity, boolean isGroupChat) {
//		if (isGroupChat)
        entity.setId(String.valueOf(System.currentTimeMillis()));
        mAppData.getSendMesgeCache().put(entity.getId(), entity);
    }

    /**
     * 发送聊天消息
     *
     * @param entity
     */
    public void onSendChatMessage(MessageEntity entity, String objType, int groupType,
                                  int type) {
        boolean isGroupChat = groupType > 0;
        onSaveMesage(entity, groupType == 1);
        entity.setGroupType(groupType);
        entity.setIsDoctorMessage("0");
        switch (type) {
            case MessageEntity.TYPE_TEXT:// 文本发送
                SmartFoxClient.sendChatMessage(entity, type, objType, 0);

                if (groupType > 0) {
                    Message msg = mHandler.obtainMessage();
                    msg.obj = entity;
                    msg.what = 3000;
                    mHandler.sendMessageDelayed(msg, 1000 * 60);
                }
                break;
            case MessageEntity.TYPE_VOICE:
                HttpRestClient.doHttpSendChatVoiceMesg(entity, groupType, objType,
                        new ChatVoiceUploadHttpHandler(entity, isGroupChat));
                break;
            case MessageEntity.TYPE_PICTURE:// 图片发送
                HttpRestClient.doHttpSendChatImageMesg(entity, groupType, objType,
                        new ChatImageUploadHttpHandler(entity, isGroupChat));
                break;
            case MessageEntity.TYPE_VIDEO:// 视频发送
                HttpRestClient.doHttpSendChatVideoMesg(entity, groupType, objType,
                        new ChatVideoUploadHttpHandler(entity, groupType > 0));
                break;
            case MessageEntity.TYPE_LOCATION:// 地图发送
                SmartFoxClient.sendChatMessage(entity, type, objType, 0);
                if (!isGroupChat) {
                    Message msg = mHandler.obtainMessage();
                    msg.obj = entity;
                    msg.what = 3000;
                    mHandler.sendMessageDelayed(msg, 1000 * 60);
                }
                break;
        }
    }

    /**
     * 发送广播
     * @param action
     * @param value
     */
//	private void sendBroad(String action, String value) {
//		Intent intent = new Intent(action);
//		intent.putExtra(BROAD_KEY, value);
//		sendBroadcast(intent);
//	}


    /**
     * 消息发送状态返回处理
     * @param isfsObject
     */
//	private void onHandlerMesgeSendState(SocketParams isfsObject) {
//		// 1,0,群,单
//		String serverId = isfsObject.getUtfString("serverId");
//		String sid = isfsObject.getUtfString("sid");
//		String ISBUYTICK = isfsObject.getUtfString("ISBUYTICK");
//		String tickNote = isfsObject.getUtfString("message");
//		String customerId = isfsObject.getUtfString("sms_target_id");
//		boolean isGroupMessage = isfsObject.getInt("isGroupMessage") == 1 ? true:false;
//		int sendStatus = "0".equals(isfsObject.getUtfString("sendStatus")) ? 0 : 1;
//		//int type = isfsObject.getInt("type");//消息类型
//		//群聊
//		if(isGroupMessage){//更新本地数据库消息状态
//
//			final MessageEntity entity = mAppData.getSendCacheMessageEntity(serverId);
//			if(entity != null){
//				mAppData.getSendMesgeCache().remove(serverId);
//				entity.setId(sid);
//				entity.setSendState(sendStatus);
//				Message msg = mHandler.obtainMessage();
//				msg.what = RequestCode.CHATTING_MESSAGE_SENDSTATE;
//				msg.obj = entity;
//				mHandler.sendMessage(msg);
//			}
//		}else{
//			MessageEntity entity = mAppData.getSendCacheMessageEntity(serverId);
//			if(entity == null){
//				entity = new MessageEntity();
//			}
//			entity.setSendState(sendStatus);
//			if(!HStringUtil.isEmpty(sid))entity.setId(sid);
////			ChatUserHelper.getInstance().updateChatMessageSendState(entity);
//			Message msg = mHandler.obtainMessage();
//			msg.what = RequestCode.CHATTING_MESSAGE_SENDSTATE;
//			msg.obj = entity;
//			mHandler.sendMessage(msg);
//
//			//提示购买服务
//			if("1".equals(ISBUYTICK)){
//				Intent intent = new Intent(MessagePushService.ACTION_PAY_MESSAGE);
//				intent.putExtra("senderId", customerId);
//				intent.putExtra("tickNote", tickNote);
//				sendBroadcast(intent);
//			}
//		}
//	}

    /**
     * 消息发送状态返回处理
     */
    private void onHandlerMesgeSendState(JSONObject server_params) {
        // 1,0,群,单
        String serverId = server_params.optString("serverId");
        String sid = server_params.optString("sid");
        boolean isGroupMessage = server_params.optInt("isGroupMessage") == 1 ? true : false;
        int sendStatus = "0".equals(server_params.optString("sendStatus")) ? 0 : 1;
        //int type = isfsObject.getInt("type");//消息类型
        //群聊
        if (isGroupMessage) {//更新本地数据库消息状态
            final MessageEntity entity = mAppData.getSendCacheMessageEntity(serverId);
            if (entity != null) {
                mAppData.getSendMesgeCache().remove(serverId);
                entity.setId(sid);
                entity.setSendState(sendStatus);
                Message msg = mHandler.obtainMessage();
                msg.what = RequestCode.CHATTING_MESSAGE_SENDSTATE;
                msg.obj = entity;
                mHandler.sendMessage(msg);
            }
        } else {
            MessageEntity entity = mAppData.getSendCacheMessageEntity(serverId);
            if (entity == null) {
                entity = new MessageEntity();
            }
            entity.setSendState(sendStatus);
            if (!HStringUtil.isEmpty(sid)) entity.setId(sid);
            Message msg = mHandler.obtainMessage();
            msg.what = RequestCode.CHATTING_MESSAGE_SENDSTATE;
            msg.obj = entity;
            mHandler.sendMessage(msg);

        }
    }

    /**
     * 接收聊天消息
     */
    private void onReceiveChatMesg(JSONObject jo) {
        int type = jo.optInt("type");
//        boolean isGroupMesg = jo.optInt("isGroupMessage") == 0 ? false : true;
        String isGroupMessage = jo.optString("isGroupMessage");
        boolean isGroupMesg = "1".equals(isGroupMessage);
        MessageEntity messageEntity = new MessageEntity();
        String id = jo.optString("serverId");//消息id
        //String time = isfsObject.getUtfString("timeStamp");//时间
        messageEntity.setServerId(id);
        messageEntity.setId(id);
        messageEntity.setIsWeChat(jo.optString("isWeChat"));
        // 图片
        if (type == MessageEntity.TYPE_PICTURE) {
            String pictureName = jo.optString("dataHolder");
            messageEntity.setSendFlag(false);
            messageEntity.setType(MessageEntity.TYPE_PICTURE);
            messageEntity.setSenderId(jo.optString("customerId"));
            messageEntity.setDownOrUpState(MessageEntity.STATE_NORMAL);
            messageEntity.setContent(pictureName);
            sendContentMesgBroad("图片消息");

        }// 视频
        if (type == MessageEntity.TYPE_VIDEO) {
            String name = jo.optString("dataHolder");
            messageEntity.setSendFlag(false);
            messageEntity.setType(MessageEntity.TYPE_VIDEO);
            messageEntity.setSenderId(jo.optString("customerId"));
            messageEntity.setDownOrUpState(MessageEntity.STATE_NORMAL);
            messageEntity.setContent(name);
            sendContentMesgBroad("视频消息");
            // 语音
        } else if (type == MessageEntity.TYPE_VOICE) {
            messageEntity.setContent(jo.optString("dataHolder"));
            messageEntity.setSendFlag(false);
            messageEntity.setType(MessageEntity.TYPE_VOICE);
            messageEntity.setSenderId(jo.optString("customerId"));
            messageEntity.setDownOrUpState(MessageEntity.STATE_NORMAL);
            messageEntity.setVoiceLength(mFormat.format(Float.valueOf(jo.optString("duration"))));
            sendContentMesgBroad("语音消息");
            // 文字
        } else if (type == MessageEntity.TYPE_TEXT || type == MessageEntity.TYPE_TIME) {
            messageEntity.setSenderId(jo.optString("customerId"));
            messageEntity.setSendFlag(false);
            messageEntity.setDownOrUpState(MessageEntity.STATE_OK);
            messageEntity.setType(MessageEntity.TYPE_TEXT);
            String content = jo.optString("sms_req_content");
            messageEntity.setContent(content);
            if (jo.has(SmartFoxClient.KEYWORDS)) {
                JSONArray array;
//					Collection<String> collection  = jo.oStringArray(SmartFoxClient.KEYWORDS);
//					array = new JSONArray(collection.toString());
                messageEntity.setContentJsonArray(jo.optJSONArray(SmartFoxClient.KEYWORDS));

            }

            //医生审核
            if (SmartControlClient.helperId.equals(jo.optString("customerId"))) {
                String dataHolder = jo.optString("dataHolder");
                //审核通过
                if (dataHolder != null) {
                    mControlClient.sendLoadUserInfo();
//					if(NumberUtils.isNumber(dataHolder)){
//						mControlClient.getInfoEntity().setRoldid(NumberUtils.toInt(dataHolder));
//					}
                }
            }
            sendContentMesgBroad(messageEntity.getContent());
            // 坐标
        } else if (type == MessageEntity.TYPE_LOCATION) {
            String dataHolder = jo.optString("dataHolder");
            String address = jo.optString("sms_req_content");
            messageEntity.setAddress(address);
            messageEntity.setContent(dataHolder);
            messageEntity.setSendFlag(false);
            messageEntity.setType(MessageEntity.TYPE_LOCATION);
            messageEntity.setSenderId(jo.optString("customerId"));
            messageEntity.setDownOrUpState(MessageEntity.STATE_NORMAL);
            sendContentMesgBroad("位置消息");
            //好友关注
        } else if (type == 11) {
            //String userId = isfsObject.getUtfString("customerId");// 关注者id
            return;
        } else if (type == MessageEntity.TYPE_INVITATION) {//邀请,关注
            String sms_req_content = jo.optString("sms_req_content");
//			Collection<String> collection  = jo.gStringArray(SmartFoxClient.KEYWORDS);
            messageEntity.setSenderId(SmartControlClient.helperId);
            messageEntity.setReceiverId(SmartFoxClient.getLoginUserId());
            messageEntity.setSendFlag(false);
            messageEntity.setContent(sms_req_content);
//			if(collection == null){
//				messageEntity.setContent(sms_req_content);
//			}else{
//				JSONArray array;
//				try {
//					array = new JSONArray(collection.toString());
//					messageEntity.setContentJsonArray(array);
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
            messageEntity.setType(MessageEntity.TYPE_TEXT);
            ChatUserHelper.getInstance().insertChatMessage(messageEntity, false);
            onUpdateMesgCllection(messageEntity, SmartControlClient.helperId);
            Intent intent1 = new Intent(MessagePushService.ACTION_MESSAGE);
            intent1.putExtra("senderId", SmartControlClient.helperId);//发送者id
            sendBroadcast(intent1);
            sendContentMesgBroad(sms_req_content);
            return;
            //医生资质审核
        } else if (type == 12) {
            String sms_req_content = jo.optString("sms_req_content");
            mControlClient.sendLoadUserInfo();
            messageEntity.setSenderId(SmartControlClient.helperId);
            messageEntity.setReceiverId(SmartFoxClient.getLoginUserId());
            messageEntity.setSendFlag(false);
            messageEntity.setContent(sms_req_content);
            messageEntity.setType(MessageEntity.TYPE_TEXT);
            ChatUserHelper.getInstance().insertChatMessage(messageEntity, false);
            Intent intent1 = new Intent(MessagePushService.ACTION_MESSAGE);
            intent1.putExtra("senderId", SmartControlClient.helperId);//发送者id
            sendBroadcast(intent1);
            return;
        }
        String targetId = jo.optString("sms_target_id");
        String customerId = jo.optString("customerId");
        messageEntity.setSenderId(customerId);
        messageEntity.setReceiverId(targetId);

        if (isGroupMesg) {//群聊消息
            onUpdateMesgCllection(messageEntity, targetId);
            messageEntity.setGroupId(targetId);
            Intent intent = new Intent(MessagePushService.ACTION_MESSAGE);
            intent.putExtra("senderId", targetId);//发送者id
            sendBroadcast(intent);
        } else {//单聊消息
            onUpdateMesgCllection(messageEntity, customerId);
            Intent intent = new Intent(MessagePushService.ACTION_MESSAGE);
            intent.putExtra("senderId", customerId);//发送者id
            sendBroadcast(intent);
        }
        ChatUserHelper.getInstance().insertChatMessage(messageEntity, isGroupMesg);
    }


    private void onUpdateMesgCllection(MessageEntity entity, String userid) {
        List<MessageEntity> list = mAppData.messageCllection.get(userid);
        if (list == null) {
            list = new ArrayList<MessageEntity>();
        }
        list.add(entity);
        mAppData.messageCllection.put(userid, list);
        mApplication.showNotify("未读消息(" + mApplication.getNoReadMesgSize() + ")条");
    }

    /**
     * 接收聊天消息
     */
//	private void onReceiveChatMesg(SocketParams isfsObject,boolean isGroupMesg){
//		int type = isfsObject.getInt("type");
//		MessageEntity messageEntity = new MessageEntity();
//		String id = isfsObject.getUtfString("serverId");//消息id
//		//String time = isfsObject.getUtfString("timeStamp");//时间
//		messageEntity.setServerId(id);
//		messageEntity.setId(id);
//		// 图片
//		if (type == MessageEntity.TYPE_PICTURE) {
//			String pictureName = isfsObject.getUtfString("dataHolder");
//			messageEntity.setSendFlag(false);
//			messageEntity.setType(MessageEntity.TYPE_PICTURE);
//			messageEntity.setSenderId(isfsObject.getUtfString("customerId"));
//			messageEntity.setDownOrUpState(MessageEntity.STATE_NORMAL);
//			messageEntity.setContent(pictureName);
//			sendContentMesgBroad("图片消息");
//			// 语音
//		} else if (type == MessageEntity.TYPE_VOICE) {
//			messageEntity.setContent(isfsObject.getUtfString("dataHolder"));
//			messageEntity.setSendFlag(false);
//			messageEntity.setType(MessageEntity.TYPE_VOICE);
//			messageEntity.setSenderId(isfsObject.getUtfString("customerId"));
//			messageEntity.setDownOrUpState(MessageEntity.STATE_NORMAL);
//			messageEntity.setVoiceLength(mFormat.format(Float.valueOf(isfsObject.getUtfString("duration"))));
//			sendContentMesgBroad("语音消息");
//			// 文字
//		} else if (type == MessageEntity.TYPE_TEXT || type == MessageEntity.TYPE_TIME) {
//			String customerId = isfsObject.getUtfString("customerId");
//			messageEntity.setSendFlag(false);
//			messageEntity.setSenderId(customerId);
//			messageEntity.setDownOrUpState(MessageEntity.STATE_OK);
//			messageEntity.setType(MessageEntity.TYPE_TEXT);
//			messageEntity.setContent(isfsObject.getUtfString("sms_req_content"));
//
//			if(isfsObject.containsKey(SmartFoxClient.KEYWORDS)){
//				JSONArray array;
//				try {
//					Collection<String> collection  = isfsObject.getUtfStringArray(SmartFoxClient.KEYWORDS);
//					array = new JSONArray(collection.toString());
//					messageEntity.setContentJsonArray(array);
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//
//			//医生审核
//			if(SmartControlClient.helperId.equals(customerId)){
//				String dataHolder = isfsObject.getUtfString("dataHolder");
//				//审核通过
//				if(dataHolder != null){
//					mControlClient.sendLoadUserInfo();
////					if(NumberUtils.isNumber(dataHolder)){
////						mControlClient.getInfoEntity().setRoldid(NumberUtils.toInt(dataHolder));
////					}
//				}
//			}
//			sendContentMesgBroad(messageEntity.getContent());
//			// 坐标
//		} else if (type == MessageEntity.TYPE_LOCATION) {
//			String dataHolder = isfsObject.getUtfString("dataHolder");
//			String address = isfsObject.getUtfString("sms_req_content");
//			messageEntity.setAddress(address);
//			messageEntity.setContent(dataHolder);
//			messageEntity.setSendFlag(false);
//			messageEntity.setType(MessageEntity.TYPE_LOCATION);
//			messageEntity.setSenderId(isfsObject.getUtfString("customerId"));
//			messageEntity.setDownOrUpState(MessageEntity.STATE_NORMAL);
//			sendContentMesgBroad("位置消息");
//			//好友关注
//		} else if (type == 11) {
//			//String userId = isfsObject.getUtfString("customerId");// 关注者id
//			return;
//		}else if(type == MessageEntity.TYPE_INVITATION){//邀请,关注
//			String sms_req_content = isfsObject.getUtfString("sms_req_content");
//			Collection<String> collection  = isfsObject.getUtfStringArray(SmartFoxClient.KEYWORDS);
//			messageEntity.setSenderId(SmartControlClient.helperId);
//			messageEntity.setReceiverId(SmartFoxClient.getLoginUserId());
//			messageEntity.setSendFlag(false);
//			messageEntity.setContent(collection.toString());
//			if(collection == null){
//				messageEntity.setContent(sms_req_content);
//			}else{
//				JSONArray array;
//				try {
//					array = new JSONArray(collection.toString());
//					messageEntity.setContentJsonArray(array);
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//			messageEntity.setType(MessageEntity.TYPE_TEXT);
//			ChatUserHelper.getInstance().insertChatMessage(messageEntity,false);
//			onUpdateMesgCllection(messageEntity,SmartControlClient.helperId);
//			Intent intent1 = new Intent(MessagePushService.ACTION_MESSAGE);
//			intent1.putExtra("senderId",SmartControlClient.helperId);//发送者id
//			sendBroadcast(intent1);
//			sendContentMesgBroad(sms_req_content);
//			return;
//		//医生资质审核
//		}else if(type == 12){
//			String sms_req_content = isfsObject.getUtfString("sms_req_content");
//			mControlClient.sendLoadUserInfo();
//			messageEntity.setSenderId(SmartControlClient.helperId);
//			messageEntity.setReceiverId(SmartFoxClient.getLoginUserId());
//			messageEntity.setSendFlag(false);
//			messageEntity.setContent(sms_req_content);
//			messageEntity.setType(MessageEntity.TYPE_TEXT);
//			ChatUserHelper.getInstance().insertChatMessage(messageEntity,false);
//			Intent intent1 = new Intent(MessagePushService.ACTION_MESSAGE);
//			intent1.putExtra("senderId",SmartControlClient.helperId);//发送者id
//			sendBroadcast(intent1);
//			return;
//		}
//		String targetId = isfsObject.getUtfString("sms_target_id");
//		String customerId = isfsObject.getUtfString("customerId");
//		messageEntity.setSenderId(customerId);
//		messageEntity.setReceiverId(targetId);
//
//		if (isGroupMesg) {//群聊消息
//			onUpdateMesgCllection(messageEntity,targetId);
//			messageEntity.setGroupId(targetId);
//			Intent intent = new Intent(MessagePushService.ACTION_MESSAGE);
//			intent.putExtra("senderId",targetId);//发送者id
//			sendBroadcast(intent);
//		} else {//单聊消息
//			onUpdateMesgCllection(messageEntity,customerId);
//			Intent intent = new Intent(MessagePushService.ACTION_MESSAGE);
//			intent.putExtra("senderId",customerId);//发送者id
//			sendBroadcast(intent);
//		}
//		ChatUserHelper.getInstance().insertChatMessage(messageEntity,isGroupMesg);
//	}

//	private void onUpdateMesgCllection(MessageEntity entity,String userid){
//		List<MessageEntity> list = mAppData.messageCllection.get(userid);
//		if(list == null){
//			list = new ArrayList<MessageEntity>();
//		}
//		list.add(entity);
//		mAppData.messageCllection.put(userid,list);
//		mApplication.showNotify("未读消息("+mApplication.getNoReadMesgSize()+")条");
//	}

    /**
     * 主页消息通知
     *
     * @param content
     */
    private void sendContentMesgBroad(String content) {
        Intent intent = new Intent(ACTION_CONTENT_MESSAGE);
        intent.putExtra("content", content);
        sendBroadcast(intent);
    }

    /**
     * 我的话题和我的社交的信息
     */
//	private void loadingSalonOrGroupInfo() {
//		HttpRestClient.doHttpLoginAboutMe(
//				mControlClient.getUserId(),
//				new ObjectHttpResponseHandler() {
//			@Override
//			public Object onParseResponse(String cotent) {
//				if(!mControlClient.isLogined()) return null;
//				synchronized (mAppData) {
//					JsonParseUtils.loginInitSalon(mApplication,cotent);
//				}
//				return null;
//			}
//			@Override
//					public void onSuccess(Object response) {
//						super.onSuccess(response);
//						onLoadOffMessge();
//			}
//		});
//	}

//	private void onExtensionEventHandler(SocketParams isfsObject) {
//		int code = isfsObject.getInt(SmartFoxClient.KEY_CODE);
//		Intent intent=null;
//		String value;
//		switch (code) {
//		//异地登录
//		case RequestCode.LOGIN_OTHERPLACE:
//			mControlClient.setLoginState(-1);
//			mHandler.sendEmptyMessage(code);
//			break;
//		// 登录者客户资料
//		case RequestCode.CUSTOMER_INFO_REQ:
//			try {
//				onParseLoginUserInfo(isfsObject);
//			} catch (Exception e) {
//				e.printStackTrace();
//				intent = new Intent(SFSEvent.LOGIN);
//				intent.putExtra("state", 7);
//				intent.putExtra("errormsg", "登录异常");
//				sendBroadcast(intent);
//				LogUtil.d(TAG, "登陆客户基本资料加载异常", e);
//			}
//			break;
//		// 收藏or删除健康友
//		case SmartFoxClient.RequestCode.CODE_COLLECT_FRIEND:
//			value = isfsObject.getUtfString(SmartFoxClient.KEY_PARAME);
//			sendBroad(ACTION_COLLECT_FRIEND, value);
//			break;
//
//		case SmartFoxClient.RequestCode.CODE_SEND_FACEBACK:
//			value = isfsObject.getUtfString(SmartFoxClient.KEY_PARAME);
//			sendBroad("com.yksj.ui.ACTION_SEND_FACEBACK", value);
//			break;
//
//		// 话题取消关注
//		case SmartFoxClient.RequestCode.COLLECT_GROUP_NOT:
//			value = isfsObject.getUtfString(SmartFoxClient.KEY_PARAME);
//			sendBroad(ACTION_COLLECT_GROUP_NOT, value);
//			break;
//
//		// 话题关注
//		case SmartFoxClient.RequestCode.COLLECT_GROUP:
//			value = isfsObject.getUtfString(SmartFoxClient.KEY_PARAME);
//			sendBroad(ACTION_COLLECT_GROUP, value);
//			break;
//		case SmartFoxClient.RequestCode.JOIN_GROUP_CHAT:// 加入群聊返回
//			Collection<String> collection = isfsObject
//					.getUtfStringArray(SmartFoxClient.KEY_PARAME);
//			sendChattingJoinGroupBroad(collection.toString());
//			break;
//		// 注销
//		case SmartFoxClient.RequestCode.SERVER_PARAME:
//			int result = isfsObject.getInt(SmartFoxClient.KEY_PARAME);
//			intent = new Intent(ACTION_EXIT);
//			intent.putExtra(SmartFoxClient.KEY_PARAME, result);
//			sendBroadcast(intent);
//			break;
//		// 转发成功状态
//		case SmartFoxClient.RequestCode.CODE_REPEAT_MESSAGE:
//		case SmartFoxClient.RequestCode.CODE_REPEAT_PIC:
//		case SmartFoxClient.RequestCode.DOCTOR_SEND_PERSON_MESSAGE:
////			intent = new Intent(ACTION_REPEAT_STATE);
////			String flag = isfsObject.getUtfString("FLAG");
////			intent.putExtra("FLAG", flag);
////			// 生成消息对象
////			if ("Y".equals(flag)) {
////				if (isfsObject.containsKey("picture")) {
////					SocketParams isfsObject1 = isfsObject.getSFSObject("picture");
////					if (isfsObject1.containsKey("isGroupMessage")
////							&& isfsObject1.getInt("isGroupMessage") != 1) {
////						String pictureName = isfsObject1
////								.getUtfString("dataHolder");
////						MessageEntity messageEntity = new MessageEntity();
////						messageEntity.setSenderId(SmartFoxClient
////								.getLoginUserId());
////						messageEntity.setType(MessageEntity.TYPE_PICTURE);
////						messageEntity.setSendFlag(true);
////						messageEntity.setContent(pictureName);
////						messageEntity.setSendState(MessageEntity.STATE_OK);
////						intent.putExtra("picture", messageEntity);
////					}
////				}
////				if (isfsObject.containsKey("text")) {
////					SocketParams isfsObject1 = isfsObject.getSFSObject("text");
////					if (isfsObject1.containsKey("isGroupMessage")&& isfsObject1.getInt("isGroupMessage") != 1) {
////						String content = isfsObject1
////								.getUtfString("sms_req_content");
////						MessageEntity messageEntity = new MessageEntity();
////						messageEntity.setSenderId(SmartFoxClient
////								.getLoginUserId());
////						messageEntity.setType(MessageEntity.TYPE_TEXT);
////						messageEntity.setSendFlag(true);
////						messageEntity.setContent(content);
////						messageEntity.setSendState(MessageEntity.STATE_OK);
////						intent.putExtra("text", messageEntity);
////					}
////				}
////			}
////			sendBroadcast(intent);
//			break;
//		case SmartFoxClient.RequestCode.CHATTING_GROUP_MESSAGE_RECEIVE:// 群消息
//			onReceiveChatMesg(isfsObject, true);
//			break;
//		case SmartFoxClient.RequestCode.CHATTING_SINGLE_MESSAGE_RECEIVE:// 单聊消息
//			onReceiveChatMesg(isfsObject, false);
//			break;
//		case SmartFoxClient.RequestCode.GROUP_INVITE:// 群邀请状态返回
//		{
//			// String sms_req_content =
//			// isfsObject.getUtfString("sms_req_content");//
//			// String name = isfsObject.getUtfString(SmartFoxClient.KEY_PARAME);
//			intent = new Intent(ACTION_GROUP_INVITE);
//			sendBroadcast(intent);
//		}
//			break;
//		case SmartFoxClient.RequestCode.CHATTING_INVITE_RECEIVE:// 接收群邀请消息
//			onReceiveChatMesg(isfsObject, false);
//			break;
//		case SmartFoxClient.RequestCode.OFFLINE_DELE://删除离线消息状态返回
////			{
////				Collection<String> collection2 = isfsObject.getUtfStringArray("msgid");
////				if(collection2.size() == 0) return;
////				for (String string : collection2) {
////					mAppData.messageMap.remove(string);
////				}
////				ChatUserHelper.getInstance().updateChatMesageDeleteState(collection2);
////			}
//			break;
//		case RequestCode.CHATTING_MESSAGE_SENDSTATE:// 消息发送状态返回
//			onHandlerMesgeSendState(isfsObject);
//			break;
//		case SmartFoxClient.RequestCode.CODE_FORBIDWORDS_LIST:// 禁言列表返回
//			Collection<String> collection2 = isfsObject
//					.getUtfStringArray(SmartFoxClient.KEY_PARAME);
//			String[] resultArray = new String[collection2.size()];
//			collection2.toArray(resultArray);
//			intent = new Intent(ACTION_FORBIDDENLIST);
//			Bundle bundle = new Bundle();
//			bundle.putStringArray(BROAD_KEY, resultArray);
//			intent.putExtras(bundle);
//			sendBroadcast(intent);
//			break;
//		case SmartFoxClient.RequestCode.CHATTING_ONLINE_FRIEND: {
//			String value1 = isfsObject.getUtfString(SmartFoxClient.KEY_PARAME);
//			List<CustomerInfoEntity> entities = FriendHttpUtil
//					.jsonAnalysisFriendEntity(value1, true);
//			intent = new Intent(ACTION_CHATTINGONLINEFRIEND);
//			intent.putExtra(BROAD_KEY, (ArrayList) entities);
//			sendBroadcast(intent);
//		}// 获取在线成员
//			break;
//		case SmartFoxClient.RequestCode.DOCTOR_INFO_AUDIT:
//			value = isfsObject.getUtfString(SmartFoxClient.KEY_PARAME);
//			break;
//		// 订单变化通知
//		case SmartFoxClient.RequestCode.CODE_PRODUCT_CHANGE:
//			String content = isfsObject.getUtfString("content");
//			Message message = mHandler.obtainMessage();
//			message.obj = content;
//			message.what = RequestCode.CODE_PRODUCT_CHANGE;
//			mHandler.sendMessage(message);
//			break;
//		case SmartFoxClient.RequestCode.CODE_LEAVE_WORDS:// 留言条数
//			String sizeStr = isfsObject.getUtfString(SmartFoxClient.KEY_PARAME);
//			sendContentMesgBroad(sizeStr);;
//			intent = new Intent(ACTION_LEAVE_WORD);
//			intent.putExtra(BROAD_KEY, sizeStr);
//			sendBroadcast(intent);
//			break;
//		case SmartFoxClient.RequestCode.NOTIFY_MESSAGE:// 通知
//		{
//			String sizeCount = isfsObject.getUtfString(SmartFoxClient.KEY_CONTENT);
//			intent = new Intent(ACTION_CONTENT_MESSAGE);
//			intent.putExtra("content", sizeCount);
//			intent.putExtra("type", 1);
//			sendBroadcast(intent);
//		}
//			break;
//		case RequestCode.KEEP_CONECTION_REQ://保持长连接
//			LogUtil.d(TAG, "CoreService==收到第"+keepCountTimeOut+"次响应");
//			keepCountTimeOut=0;
//			break;
//		}
//	}

    /**
     * 消息提醒
     *
     * @param content
     */
    private void showMessageTip(String content) {
        try {
            JSONObject obj = new JSONObject(content);
            String tip = obj.optString("sms_req_content");
            if (!HStringUtil.isEmpty(tip))
                mApplication.showNotify(tip);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 视频倒计时
     *
     * @param context
     */
    public static void setActionVideoTip(Context context, long time, long timeTip) {
        Intent i = new Intent(context, CoreService.class);
//        i.putExtra(ACTION_VIDEO_TIP, time);
//        i.putExtra(ACTION_VIDEO_TIP_END, timeTip);
        i.setAction(ACTION_VIDEO);
        context.startService(i);
    }

    /**
     * 视频倒计时清除
     *
     * @param context
     */
    public static void removeActionVideo(Context context) {
        Intent i = new Intent(context, CoreService.class);
        i.setAction(ACTION_VIDEO_REMOVE);
        context.startService(i);
    }
}
