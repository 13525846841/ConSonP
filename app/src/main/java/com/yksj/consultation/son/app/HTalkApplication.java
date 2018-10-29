package com.yksj.consultation.son.app;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatControlCommand;
import com.netease.nimlib.sdk.avchat.model.AVChatAttachment;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.model.IMMessageFilter;
import com.netease.nimlib.sdk.team.model.UpdateTeamAttachment;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.yksj.consultation.comm.SOConfigs;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.avchat.AVChatActivity;
import com.yksj.consultation.son.consultation.avchat.AVChatProfile;
import com.yksj.consultation.son.consultation.avchat.SystemUtil;
import com.yksj.consultation.son.consultation.avchat.cache.DemoCache;
import com.yksj.consultation.son.consultation.avchat.cache.UserPreferences;
import com.yksj.consultation.son.consultation.avchat.common.DefaultUserInfoProvider;
import com.yksj.consultation.son.consultation.avchat.common.NimUIKit;
import com.yksj.consultation.son.consultation.avchat.receiver.PhoneCallStateObserver;
import com.yksj.consultation.son.consultation.avchat.session.SessionHelper;
import com.yksj.consultation.son.consultation.avchat.team.TeamAVChatHelper;
import com.yksj.consultation.son.login.WelcomeActivity;
import com.yksj.consultation.son.message.MessageNotifyActivity;
import com.yksj.consultation.son.smallone.bean.Configs;
import com.yksj.healthtalk.entity.MessageEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpRestClientB;
import com.yksj.healthtalk.net.http.HttpUrls;
import com.yksj.healthtalk.net.http.HttpUrlsB;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.AppCashHandler;
import com.yksj.healthtalk.utils.ImageItem;
import com.yksj.healthtalk.utils.MD5Utils;
import com.yksj.healthtalk.utils.SharePreUtils;
import com.yksj.healthtalk.utils.SystemUtils;

import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.sharesdk.framework.ShareSDK;
import okio.Buffer;

public class HTalkApplication extends Application {
    public static final int NOTIFY_ID = 0x0;
    //	public static final String strKey = "c0K7ypRRjn2MjhKRK8t6Cvsu";
    public static int haveShoppingCar = 0;
    public static final String APP_CONSULTATION_CENTERID = "6";//六一健康id
    public static final String APP_VALID_MARK = "60";
    public static final String CLIENT_TYPE = "60";
    private static HTalkApplication mApplication;
    private static Resources mResources;

    private ArrayList<ImageItem> mImages = new ArrayList<>();
    public static String mKey = "58781ed63f807a8f5d157f2180d95e82";
    public String[] location = new String[2];

    private String CER_61_TEST =
            "-----BEGIN CERTIFICATE-----\n" +
                    "MIIDXDCCAkQCCQCmgd8JH66dYzANBgkqhkiG9w0BAQsFADBvMQswCQYDVQQGEwJD\n" +
                    "TjEQMA4GA1UECAwHQmVpamluZzEQMA4GA1UEBwwHQmVpamluZzEQMA4GA1UECgwH\n" +
                    "SVQgdGVzdDEQMA4GA1UECwwHSVQgRGVwdDEYMBYGA1UEAwwPNjEuamlhbnNpb24u\n" +
                    "Y29tMCAXDTE4MDIyNzA2MDA1M1oYDzIxMTgwMjAzMDYwMDUzWjBvMQswCQYDVQQG\n" +
                    "EwJDTjEQMA4GA1UECAwHQmVpamluZzEQMA4GA1UEBwwHQmVpamluZzEQMA4GA1UE\n" +
                    "CgwHSVQgdGVzdDEQMA4GA1UECwwHSVQgRGVwdDEYMBYGA1UEAwwPNjEuamlhbnNp\n" +
                    "b24uY29tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzgzNAMvWSzM+\n" +
                    "iqvuef5wGXv9Nz2BZl8swRJq/KHH+PB55lpg6LQmKF98XsIM+YFjuEod/2VG+QLu\n" +
                    "8Qgbt/1PRfiAKy2ErBHVuOFHq9sbcT/BCKxFasUfTDPbfOuGBxrEjTrBTlXu1rwA\n" +
                    "UUkgxyAVVIAB3k8B20r7F2Mk/HvxX3xxDLbC9MMNr1Kp/OKe/hVZfTyF2TbaLCJ6\n" +
                    "+kbBZjT80im27Aa3JCWSsF808K95KDoxV95Q0GfOzty7U/kCwJA4BnoylnIMOvN4\n" +
                    "krCPWjtivJUUbL6ADup3GeCYUhr61/gKrsqLgltHPe982jSapDp4wx7099x7W2N1\n" +
                    "R8B8SX2AjwIDAQABMA0GCSqGSIb3DQEBCwUAA4IBAQBPjCESOwYSAjwhRe3QLqLT\n" +
                    "rSHCywmHsoeFOnYI8ZlQDM/hR4AmjZlwRMPV2SKoxkBAUQ6zCIsdBvAjPqfzNxGg\n" +
                    "FSmjN7kluBhLGp8/tTAQNxIkj3/ueKFVSjkTkmApwV28XINsGlXBQ1kuiUMDLp1Y\n" +
                    "3IqGhC4Dvsxj1NChalMvzBN5dfFdYkukKBt/BVLr0RqOyEYrKZ5SrZom2d5jildP\n" +
                    "YShTQRPwDbV8Qf0QAAJfVRHcEVOw5OspXv3tWMb4DKEZoNTpTLzNOMmaznSt1Rwe\n" +
                    "NK1LDgrSIfI0iTT4Q08MOUdvnSHcb/SUuvxDXE6yvBsxlZQ5YmAR9Dfl4U3OIY3j\n" +
                    "-----END CERTIFICATE-----\n";//六一测试服务器认证证书


    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    public String getmTalkServiceId() {
        return mTalkServiceId;
    }

    public String setMTalkServiceId(String mTalkServiceId) {
        return this.mTalkServiceId = mTalkServiceId;
    }
    /**
     * 系统所有http请求地址集合
     */
    private static HttpUrls httpUrls;
    private static HttpUrlsB bHttpUrls;

    /**
     * 全局数据
     */
    private static AppData appData;
    public static String userID;

    private NotificationManager mNotificationManager;
    private ConnectivityManager mConnectivityManager;

    public void onCreate() {
        super.onCreate();
        try {

//            HttpsUtils.SSLParams sslParams =
//                    HttpsUtils.getSslSocketFactory(new InputStream[]{getAssets().open("srca.cer")}, null, null);


//            OkHttpClientManager.getInstance()
//                    .setCertificates(new Buffer()
//                            .writeUtf8(CER_61_TEST)
//                            .inputStream());
//
//            Picasso.setSingletonInstance(new Picasso.Builder(this).
//                    downloader(new OkHttpDownloader(getUnsafeOkHttpClient()))
//                    .build());


//            OkHttpClientManager.getInstance()
//                    .setCertificates(getAssets().open("www61120cn.cer"));
//            OkHttpClientManager.getInstance()
//                    .setCertificates(getAssets().open("www61120der.cer"));
//                    .setCertificates(getAssets().open("www61120cn2.cer"));
//                    .setCertificates(getAssets().open("srca.cer"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mApplication = this;
        mResources = getResources();
        init();
    }

    private void init() {

//        SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));
        SpeechUtility.createUtility(mApplication, SpeechConstant.APPID + "=" + "59ae3cbc");
        AppCashHandler appExceptionCashHandler = AppCashHandler.getInstance();
        appExceptionCashHandler.init(this);
        ShareSDK.initSDK(this);


        NIMClient.init(this, null, getOptions());

        initUIKitS();
        //上传异常日志
        if (isWifi()) {
            appExceptionCashHandler.sendLogToServer();
        }
//		SDKInitializer.initialize(getApplication());//
//		initLocationManager(this);
//		ContextMgr.setContext(this);//微博

        if (httpUrls == null) httpUrls = new HttpUrls(Configs.WEB_IP);
        if (appData == null) appData = new AppData();
        HttpRestClient.setmHttpUrls(httpUrls);
        HttpRestClient.addHttpHeader("client_type", HTalkApplication.CLIENT_TYPE);


        try {

//            OkHttpClientManager.getInstance()
//                    .setCertificates(new Buffer()
//                            .writeUtf8(CER_61)
//                            .inputStream());

//            OkHttpClientManager.getInstance()
//                    .setCertificates(getAssets().open("www61120cn.cer"));
//            OkHttpClientManager.getInstance()
//                    .setCertificates(getAssets().open("www61120der.cer"));
//                    .setCertificates(getAssets().open("www61120cn2.cer"));
//                    .setCertificates(getAssets().open("srca.cer"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bHttpUrls == null) bHttpUrls = new HttpUrlsB(Configs.WEB_IP);
        HttpRestClientB.setmHttpUrlsB(bHttpUrls);
        try {
            HttpRestClientB.addHttpHeader("ProjectVersion", SOConfigs.VERSION_SYS);//??
            HttpRestClientB.addHttpHeader("OSName", "Android");
            HttpRestClientB.addHttpHeader("OSSYstemVersion", android.os.Build.VERSION.RELEASE);
            HttpRestClientB.addHttpHeader("OSDeviceName", android.os.Build.MODEL);
            HttpRestClientB.addHttpHeader("OSLocale", getResources().getConfiguration().locale.toString());
        } catch (Exception e) {

        }
//		CrashHandler crashHandler = CrashHandler.getInstance();
//		crashHandler.init(getApplicationContext());
    }


    public void showNotify(String content) {
        Intent intent;
        if (!SmartFoxClient.getLoginUserInfo().isDoctor()) {
            intent = new Intent(mApplication, MessageNotifyActivity.class);
        } else {
            intent = new Intent(mApplication, MessageNotifyActivity.class);
        }

//		Notification notification = new Notification(R.drawable.launcher_logo,content,System.currentTimeMillis());
//		notification.contentView = null;


        PendingIntent pendingIntent = PendingIntent.getActivity(mApplication, 0, intent, 0);
        Notification notification = new Notification.Builder(mApplication)
                .setAutoCancel(true)
                .setContentTitle("六一健康")
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.launcher_logo)
                .setWhen(System.currentTimeMillis())
                .build();
        SettingManager manager = SettingManager.getInstance();
        //需要服务提醒
        if (manager.isServiceNote()) {
            final Calendar calendar = Calendar.getInstance();
            final int hour = calendar.get(Calendar.HOUR_OF_DAY);
//			Time time = new Time("GMT+8");
//			time.set(System.currentTimeMillis());

            if (hour > 6 && hour < 22) {//提醒
                if (manager.isSoundOpen()) notification.defaults |= Notification.DEFAULT_SOUND;
                if (manager.isVibrateOpen()) notification.defaults |= Notification.DEFAULT_VIBRATE;
            } else {

            }
        } else {
            if (manager.isSoundOpen()) notification.defaults |= Notification.DEFAULT_SOUND;
            if (manager.isVibrateOpen()) notification.defaults |= Notification.DEFAULT_VIBRATE;
        }
//		notification.defaults |= Notification.DEFAULT_SOUND;
//		notification.defaults |= Notification.DEFAULT_VIBRATE;
//		notification.setLatestEventInfo(mApplication, "六一健康", content,PendingIntent.getActivity(mApplication, 0,intent, 0));
        getNotificationManager().notify(HTalkApplication.NOTIFY_ID, notification);
    }


    public void cancelNotify() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        mNotificationManager.cancel(HTalkApplication.NOTIFY_ID);
    }

    //用户协议和隐私条款
    public String getUserAgentPath() {
//		return HttpRestClient.getmHttpUrls().WEB_ROOT+
//				"/DuoMeiHealth/UserAgreementServlet300?Type=queryAgreement&customerid="
//				+SmartControlClient.getControlClient().getUserId()+
//				"&agreementType=Registered&system_version="+
//				getAppVersionName();
        return "http://www.yijiankangv.com/JumpPage/ETYY_Customer_Agreement_100.html";
    }

    //用户条款和隐私协议http://220.194.46.204/JumpPage/Consultation_Agreement_Cus_1_430.html
    public String getDoctorUserAgentPath() {
//		return HttpRestClient.getmHttpUrls().WEB_ROOT+"/DuoMeiHealth/UserAgreementServlet300?Type=queryAgreement&customerid="+SmartFoxClient.getLoginUserId()+"&agreementType=Registered&system_version="+
//				getAppVersionName();
        return "http://www.yijiankangv.com/JumpPage/ETYY_Customer_Agreement_100.html";
    }

    //医生注册条款和隐私协议http://220.194.46.204/JumpPage/Consultation_Agreement_Doc_1_430.html
    public String getDoctorAgentPath() {
//		return HttpRestClient.getmHttpUrls().WEB_ROOT+"/DuoMeiHealth/UserAgreementServlet300?Type=queryAgreement&customerid="
//				+SmartControlClient.getControlClient().getUserId()+
//				"&agreementType=Doctor_Apply&system_version="+HTalkApplication.getAppVersionName();
        return "http://yijiankangv.com/JumpPage/Consultation_Agreement_Doc_1_100.html";
    }

    //功能介绍
    public String getFunctionIntroduction() {
//		return "http://www.h-tlk.com/JumpPage/Health_Introduce_420.html";
        return "http://yijiankangv.com/JumpPage/ETYY_Health_Introduce_Cus_100.html";
    }

    public synchronized NotificationManager getNotificationManager() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    public synchronized ConnectivityManager getConnectivityManager() {
        if (mConnectivityManager == null) {
            mConnectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        }
        return mConnectivityManager;
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public static String getAppVersionName() {
        try {
            PackageInfo packageInfo = mApplication.getPackageManager()
                    .getPackageInfo(mApplication.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "1.1.5";
    }

    /**
     * 获取指南版本号
     *
     * @return
     */
    public static String getHelpAppVersionName() {
//		return "4.0.2";
        return getAppVersionName();
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public static int getAppVersionCode() {
        try {
            PackageInfo packageInfo = mApplication.getPackageManager()
                    .getPackageInfo(mApplication.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 网络是否有效
     *
     * @return false ,true
     */
    public synchronized boolean isNetWork() {
        getConnectivityManager();

        NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        return info.isConnected();
    }

    /**
     * 是否是wifi网络
     *
     * @return
     */
    public synchronized boolean isWifi() {
        getConnectivityManager();
        NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        return info.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static HTalkApplication getApplication() {
        return mApplication;
    }

    public static Resources getAppResources() {
        return mResources;
    }

    private void initLocationManager(Context context) {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现  

//		try {
//			if (mBMapManager == null) {
//				mBMapManager = new BMapManager(this);
//			}
//			mBMapManager.init(new MKGeneralListener() {
//				@Override
//				public void onGetPermissionState(int arg0) {//授权问题
//				}
//				@Override
//				public void onGetNetworkState(int arg0) {//您的网络出错啦
//				}
//			});
//		} catch (Exception e) {
//			LogUtil.d("application", e.toString());
//		}
//		
//		mBMapManager.init(strKey,null);
    }

    /**
     * 登陆清除
     */
    public static void clearAll() {
        if (appData != null) {
            appData.clearAll();
        }
    }

    /**
     * 清楚全局数据
     */
    public void destoryAll() {
        /*setAppData(null);
        setHttpUrls(null);
		setSettingManager(null);*/
    }

    public void setHttpUrls(HttpUrls httpUrls) {
        this.httpUrls = httpUrls;
    }

	/*public SettingManager getSettingManager() {
        return settingManager;
	}

	public void setSettingManager(SettingManager settingManager) {
		this.settingManager = settingManager;
	}*/

    public void setAppData(AppData appData) {
        this.appData = appData;
    }

    public static HTalkApplication getHTalkApplication() {
        return mApplication;
    }

    public static AppData getAppData() {
        return appData;
    }

    public static HttpUrls getHttpUrls() {
        return mApplication.httpUrls;
    }

    public static String getMd5UserId() {
        return MD5Utils.getMD5(LoginServiceManeger.instance().getLoginUserId());
    }

    /**
     * 未读消息数量
     *
     * @return
     */
    public static int getNoReadMesgSize() {
        Collection<List<MessageEntity>> lists = appData.messageCllection.values();
        int i = 0;
        for (List<MessageEntity> list : lists) {
            i += list.size();
        }
        return i;
    }

    public ArrayList<ImageItem> getmImages() {
        return mImages;
    }

    public void setmImages(ArrayList<ImageItem> mImages) {
        this.mImages = mImages;
    }

    public String mTalkServiceType;//9999 挂号   10000未命中
    private String mTalkServiceId;//客服ID

    /**
     * 获取唯一id
     *
     * @return
     */
    public static String getDeviceNumber() {
        return SystemUtils.getDeviceId(getApplication());
    }

    private void initUIKitS() {
        DemoCache.setContext(this);
//        NIMClient.init(this, getLoginInfo(), getOptions());
//		NIMClient.init(this, null, getOptions());
        if (inMainProcess()) {
            // 初始化红包模块，在初始化UIKit模块之前执行
//			NIMRedPacketClient.init(this);

            // 初始化UIKit模块
            NimUIKit.init(this);


            // 会话窗口的定制初始化。
            SessionHelper.init();

            TeamAVChatHelper.sharedInstance().registerObserver(true);

            // 注册通知消息过滤器
//			registerIMMessageFilter();

            // 初始化消息提醒
//			NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

            // 注册白板会话
//			registerRTSIncomingObserver(true);

//            // 注册语言变化监听
//            registerLocaleReceiver(true);
            // 注册网络通话来电
            registerAVChatIncomingCallObserver(true);

//			OnlineStateEventManager.init();
        }
    }

//	private LoginInfo getLoginInfo() {
//		String account = Preferences.getUserAccount();
//		String token = Preferences.getUserToken();
//
//		if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
//			DemoCache.setAccount(account.toLowerCase());
//			return new LoginInfo(account, token);
//		} else {
//			return null;
//		}
//	}

    private SDKOptions getOptions() {
        SDKOptions options = new SDKOptions();

//		 如果将新消息通知提醒托管给SDK完成，需要添加以下配置。
//        initStatusBarNotificationConfig(options);

        // 配置保存图片，文件，log等数据的目录
//        options.sdkStorageRootPath = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/nim";

        // 配置数据库加密秘钥
        options.databaseEncryptKey = "NETEASE";

        // 配置是否需要预下载附件缩略图
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小，
//        options.thumbnailSize = MsgViewHolderThumbBase.getImageMaxEdge();

        // 用户信息提供者
        options.userInfoProvider = new DefaultUserInfoProvider(this);

        // 定制通知栏提醒文案（可选，如果不定制将采用SDK默认文案）
//        options.messageNotifierCustomization = messageNotifierCustomization;

        // 在线多端同步未读数
        options.sessionReadAck = true;

        // 云信私有化配置项
//        configServerAddress(options);

        return options;
    }

    private void configServerAddress(final SDKOptions options) {
//		String appKey = PrivatizationConfig.getAppKey();
//		if (!TextUtils.isEmpty(appKey)) {
//			options.appKey = appKey;
//		}
//
//		ServerAddresses serverConfig = PrivatizationConfig.getServerAddresses();
//		if (serverConfig != null) {
//			options.serverConfig = serverConfig;
//		}
    }

    private void initStatusBarNotificationConfig(SDKOptions options) {
        // load 应用的状态栏配置
        StatusBarNotificationConfig config = loadStatusBarNotificationConfig();

        // load 用户的 StatusBarNotificationConfig 设置项
        StatusBarNotificationConfig userConfig = UserPreferences.getStatusConfig();
        if (userConfig == null) {
            userConfig = config;
        } else {
            // 新增的 UserPreferences 存储项更新，兼容 3.4 及以前版本
            // 新增 notificationColor 存储，兼容3.6以前版本
            // APP默认 StatusBarNotificationConfig 配置修改后，使其生效
            userConfig.notificationEntrance = config.notificationEntrance;
            userConfig.notificationFolded = config.notificationFolded;
            userConfig.notificationColor = config.notificationColor;
        }
        // 持久化生效
        UserPreferences.setStatusConfig(userConfig);
        // SDK statusBarNotificationConfig 生效
        options.statusBarNotificationConfig = userConfig;
    }

    // 这里开发者可以自定义该应用初始的 StatusBarNotificationConfig
    private StatusBarNotificationConfig loadStatusBarNotificationConfig() {
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        // 点击通知需要跳转到的界面
        config.notificationEntrance = WelcomeActivity.class;
        config.notificationSmallIconId = R.drawable.launcher_logo;
        config.notificationColor = getResources().getColor(R.color.color_blue_3a9efb);
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";

        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;

        // save cache，留做切换账号备用
        DemoCache.setNotificationConfig(config);
        return config;
    }

    public boolean inMainProcess() {
        String packageName = getPackageName();
        String processName = SystemUtil.getProcessName(this);
        return packageName.equals(processName);
    }

    /**
     * 通知消息过滤器（如果过滤则该消息不存储不上报）
     */
    private void registerIMMessageFilter() {
        NIMClient.getService(MsgService.class).registerIMMessageFilter(new IMMessageFilter() {
            @Override
            public boolean shouldIgnore(IMMessage message) {
                if (UserPreferences.getMsgIgnore() && message.getAttachment() != null) {
                    if (message.getAttachment() instanceof UpdateTeamAttachment) {
                        UpdateTeamAttachment attachment = (UpdateTeamAttachment) message.getAttachment();
                        for (Map.Entry<TeamFieldEnum, Object> field : attachment.getUpdatedFields().entrySet()) {
                            if (field.getKey() == TeamFieldEnum.ICON) {
                                return true;
                            }
                        }
                    } else if (message.getAttachment() instanceof AVChatAttachment) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void registerAVChatIncomingCallObserver(boolean register) {
        AVChatManager.getInstance().observeIncomingCall(new Observer<AVChatData>() {
            @Override
            public void onEvent(AVChatData data) {
                String extra = data.getExtra();
                Log.e("Extra", "Extra Message->" + extra);
                if (PhoneCallStateObserver.getInstance().getPhoneCallState() != PhoneCallStateObserver.PhoneCallStateEnum.IDLE
                        || AVChatProfile.getInstance().isAVChatting()
                        || TeamAVChatHelper.sharedInstance().isTeamAVChatting()
                        || AVChatManager.getInstance().getCurrentChatId() != 0) {
//					LogUtil.i(TAG, "reject incoming call data =" + data.toString() + " as local phone is not idle");
                    AVChatManager.getInstance().sendControlCommand(data.getChatId(), AVChatControlCommand.BUSY, null);
                    return;
                }
                // 有网络来电打开AVChatActivity
                AVChatProfile.getInstance().setAVChatting(true);
                AVChatProfile.getInstance().launchActivity(data, AVChatActivity.FROM_BROADCASTRECEIVER);
                if (LoginServiceManeger.instance().getLoginEntity() != null) {
                    LoginServiceManeger.instance().getLoginEntity().setAvData(data);
                    SharePreUtils.updateAvChateState(true);
                }
            }
        }, register);
    }

    private void registerRTSIncomingObserver(boolean register) {
//		RTSManager.getInstance().observeIncomingSession(new Observer<RTSData>() {
//			@Override
//			public void onEvent(RTSData rtsData) {
////                RTSActivity.incomingSession(DemoCache.getContext(), rtsData, RTSActivity.FROM_BROADCAST_RECEIVER);
//			}
//		}, register);
    }

    public void showN() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentInfo("补充内容");
        builder.setContentText("主内容区");
        builder.setContentTitle("通知标题");
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setTicker("新消息");
        builder.setAutoCancel(true);

        builder.setWhen(System.currentTimeMillis());//设置时间，设置为系统当前的时间
        Intent intent = new Intent(this, MessageNotifyActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        /**
         * vibrate属性是一个长整型的数组，用于设置手机静止和振动的时长，以毫秒为单位。
         * 参数中下标为0的值表示手机静止的时长，下标为1的值表示手机振动的时长， 下标为2的值又表示手机静止的时长，以此类推。
         */
//		long[] vibrates = { 0, 1000, 1000, 1000 };
//		notification.vibrate = vibrates;

        /**
         * 手机处于锁屏状态时， LED灯就会不停地闪烁， 提醒用户去查看手机,下面是绿色的灯光一 闪一闪的效果
         */
        notification.ledARGB = Color.GREEN;// 控制 LED 灯的颜色，一般有红绿蓝三种颜色可选
        notification.ledOnMS = 1000;// 指定 LED 灯亮起的时长，以毫秒为单位
        notification.ledOffMS = 1000;// 指定 LED 灯暗去的时长，也是以毫秒为单位
        notification.flags = Notification.VISIBILITY_PUBLIC;// 指定通知的一些行为，其中就包括显示
        // LED 灯这一选项


//                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//                notification.sound = uri;
//                notification.defaults = Notification.DEFAULT_ALL;
        manager.notify(1, notification);


    }

    public static OkHttpClient getUnsafeOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }};

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts,
                    new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext
                    .getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setSslSocketFactory(sslSocketFactory);
            okHttpClient.setHostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;

                }
            });

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
