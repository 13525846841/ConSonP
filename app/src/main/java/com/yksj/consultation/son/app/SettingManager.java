package com.yksj.consultation.son.app;

import android.content.SharedPreferences;

import com.yksj.healthtalk.net.socket.SmartControlClient;
import com.yksj.healthtalk.utils.SharePreUtils;

/**
 * 
 * 设置管理类
 * 
 * @author zhao
 */
public class SettingManager {
	/**
	 * 音效
	 */
	public static final String KEY_SETTING_RING = "SETTING_ISSOUND";
	/**
	 * 震动
	 */
	public static final String KEY_SETTING_VIBRATE = "SETTING_ISVIBRATE";
	/**
	 * 公开地理位置
	 */
	public static final String KEY_LOCATION_SHARE = "SETTING_LOCATION_ISSHARE";
	/**
	 * 背景音乐开关
	 */
	public static final String KEY_BACKGROUND_MUSIC = "SETTING_BG_MUSIC_ISOPEN";
	/**
	 * 通知不显示详情
	 */
	public static final String KEY_NOTIFICATION_SHOW = "SETTING_NOTIFICATION_ISDETAILS";
	/**
	 * 推送健康贴士
	 */
	public static final String KEY_SEND_IOS = "SETTING_SEND_IOS";
	/**
	 * 通讯录好友能否找到我
	 */
	public static final String KEY_FIND_ME = "SETTING_FIND_ME";
	/**
	 * 音乐开关
	 */
	public static final String KEY_MUSIC_ON = "SETTING_MUSIC_ON";

	/**
	 * 服务开关
	 */
	public static final String KEY_SERVICE_NOTE = "SERVICE_NOTE";

	/**
	 * 私聊开关
	 */
	public static final String KEY_PRIVATE_CHAT = "PRIVATE_CHAT";


	private SharedPreferences sharedPreferences;
	private boolean isSoundOpen = true;// 按钮声音
	private boolean isVibrateOpen = true;// 系统震动
	private boolean isLocationShare = true;// 位置共享
	private boolean isSendIos = true;// 推送健康贴士
	private boolean canFindMe = true;// 通讯录好友能否找到我
	private boolean isServiceNote = true;// 服务是否提醒
	private boolean isPrivateChat = true;// 私聊

	/**
	 * 1 新闻 3 新生成客户 0 话题发言 2 创建话题
	 */
	private boolean[] filterType = new boolean[] { false, false, false, false };// 筛选类型
	private String settingConfig = "setting_config_";// xml名称
	private String userid;

	private static SettingManager mSettingManager;

	// public static boolean isNetWorkAvailab = true;//网络是否连接
	// public static boolean isSDCardMount = true;//内存卡装载状态
	// private boolean isAtMainUI = true;//是否在主界面
	// private boolean isSunFlicker = true;//闪小太阳
	// private boolean isMainAnimOpen = false;//主页动画是否开启
	// private boolean isShowNotificationDetail = true;//通知是否显示详情
	// 当前背景音乐地址
	// private String currentBgSoundPath;

	private SettingManager() {
		init();
	}

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	/*
	 * public static SettingManager getSettingManager(Context context){
	 * //if(settingManager != null)destorySetting(); if(settingManager == null){
	 * settingManager = new SettingManager();
	 * settingManager.initSetConfig(context); } return settingManager; }
	 */

	public static void destory() {
		mSettingManager = null;
	}

	public static SettingManager getInstance() {
		if (mSettingManager == null) {
			mSettingManager = new SettingManager();
		}
		return mSettingManager;
	}

	/**
	 * 初始化配置
	 */
	private void init() {
		userid = SmartControlClient.getControlClient().getUserId();
		settingConfig = settingConfig + userid;
		sharedPreferences = SharePreUtils.getSharePreFernces(
				HTalkApplication.getApplication(), settingConfig);
		isSoundOpen = sharedPreferences.getBoolean(KEY_BACKGROUND_MUSIC,
				isSoundOpen);
		isVibrateOpen = sharedPreferences.getBoolean(KEY_SETTING_VIBRATE,
				isVibrateOpen);
		isLocationShare = sharedPreferences.getBoolean(KEY_LOCATION_SHARE,
				isLocationShare);
		isPrivateChat = sharedPreferences.getBoolean(KEY_PRIVATE_CHAT,
				isPrivateChat);
		isSendIos = sharedPreferences.getBoolean(KEY_SEND_IOS, isSendIos);
		canFindMe = sharedPreferences.getBoolean(KEY_FIND_ME, false);
		isServiceNote = sharedPreferences.getBoolean(KEY_SERVICE_NOTE, false);
		// settingConfig =
		// settingConfig+SmartFoxClient.getSmartFoxClient().getUserName();
		// sharedPreferences =
		// SharePreUtils.getSharePreFernces(context,settingConfig);
		// isShowNotificationDetail =
		// sharedPreferences.getBoolean(KEY_NOTIFICATION_SHOW,
		// isShowNotificationDetail);
		// isMusicOpen = sharedPreferences.getBoolean(KEY_MUSIC_ON,
		// isMusicOpen);
//		 isNetWorkAvailab = SystemUtils.isNetworkAvailable(context);
		// isSDCardMount = SystemUtils.getScdExit();
	}

	public boolean[] getFilterType() {
		return filterType;
	}

	public void setFilterType(boolean[] filterType) {
		this.filterType = filterType;
	}

	private void onUpdatePrefer(String key, boolean value) {
		SharePreUtils.saveBoolean(this.sharedPreferences, key, value);
	}

	public boolean isServiceNote() {
		return isServiceNote;
	}

	/**
	 * 服务提醒
	 * 
	 * @param isServiceNote
	 */
	public void setServiceNote(boolean isServiceNote) {
		this.isServiceNote = isServiceNote;
		onUpdatePrefer(KEY_SERVICE_NOTE, isSoundOpen);
	}

	public boolean isSoundOpen() {
		return isSoundOpen;
	}

	public void setSoundOpen(boolean isSoundOpen) {
		this.isSoundOpen = isSoundOpen;
		onUpdatePrefer(KEY_BACKGROUND_MUSIC, isSoundOpen);
	}

	public boolean isVibrateOpen() {
		return isVibrateOpen;
	}

	public void setVibrateOpen(boolean isVibrateOpen) {
		this.isVibrateOpen = isVibrateOpen;
		onUpdatePrefer(KEY_SETTING_VIBRATE, isVibrateOpen);
	}

	public boolean isSendIos() {
		return isSendIos;
	}

	public void setSendIos(boolean isSendIos) {
		this.isSendIos = isSendIos;
		onUpdatePrefer(KEY_SEND_IOS, isSendIos);
	}

	public boolean canFindMe() {
		return canFindMe;
	}

	public void setFindMe(boolean canFindMe) {
		this.canFindMe = canFindMe;
		onUpdatePrefer(KEY_FIND_ME, canFindMe);
	}

	public boolean isLocationShare() {
		return isLocationShare;
	}

	public void setLocationShare(boolean isLocationShare) {
		this.isLocationShare = isLocationShare;
		onUpdatePrefer(KEY_LOCATION_SHARE, isLocationShare);
	}
	public boolean isPrivateChat() {
		return isPrivateChat;
	}

	public void setPrivateChat(boolean isPrivateChat) {
		this.isPrivateChat = isPrivateChat;
		onUpdatePrefer(KEY_PRIVATE_CHAT, isPrivateChat);
	}


}
