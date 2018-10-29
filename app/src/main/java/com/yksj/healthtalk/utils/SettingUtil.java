package com.yksj.healthtalk.utils;


public class SettingUtil {

	/*public static final String SYSTEM_SETTING_PREFER = "system_setting_prefer";
	
	*//**
	 * 铃声
	 *//*
	public static final String KEY_SETTING_RING = "setting_IsSound"+SmartFoxClient.getSmartFoxClient().getUserId();
	*//**
	 * 震动
	 *//*
	public static final String KEY_SETTING_VIBRATE = "setting_IsVibrate"+SmartFoxClient.getSmartFoxClient().getUserId();
	*//**
	 * 公开地理位置
	 *//*
	public static final String KEY_LOCATION_SHARE = "setting_location_isShare"+SmartFoxClient.getSmartFoxClient().getUserId();
	*//**
	 * 背景音乐开关
	 *//*
	public static final String KEY_BACKGROUND_MUSIC = "setting_background_music_isOpen"+SmartFoxClient.getSmartFoxClient().getUserId(); 
	*//**
	 * 通知不显示详情
	 *//*
	public static final String KEY_NOTIFICATION_SHOW = "setting_notification_isShow"+SmartFoxClient.getSmartFoxClient().getUserId();
	*//**
	 * 通知是否接受推送消息
	 *//*
	public static final String KEY_PUSH_SHOW = "setting_push_isShow"+SmartFoxClient.getSmartFoxClient().getUserId();
	*//**
	 * 小太阳闪烁
	 *//*
	public static final String KEY_FLASH = "setting_flash"+SmartFoxClient.getSmartFoxClient().getUserId();
	*//**
	 * 通讯录好友能找到我
	 *//*
	public static final String KEY_FINDME_SHOW = "setting_findme_isShow"+SmartFoxClient.getSmartFoxClient().getUserId();
	*//**
	 * 背景音乐位置保存
	 *//*
	public static final String KEY_BACKGROUND_MUSIC_INDEX = "setting_music_index";
	*//**
	 * 背景音乐路径保存
	 *//*
	public String KEY_BACKGROUND_MUSIC_PATH = SmartFoxClient.getSmartFoxClient().getUserId();
	
	
	*//**
	 * 获得系统铃声设置
	 * @param contexts
	 * @return
	 *//*
	public static boolean getSettingRing(Context context){
		SharedPreferences sharedPreferences = getSettingSharedPreferences(context);
		boolean isRing = sharedPreferences.getBoolean(KEY_SETTING_RING, true);
		return isRing;
	}
	
	*//**
	 * 获得系统震动设置
	 * @param context
	 * @return
	 *//*
	public static boolean getSettingVibrate(Context context){
		SharedPreferences sharedPreferences = getSettingSharedPreferences(context);
		boolean isVibrate = sharedPreferences.getBoolean(KEY_SETTING_VIBRATE, true);
		return isVibrate;
	}
	*//**
	 * 获得位置共享
	 * @param context
	 * @return
	 *//*
	public static boolean getSettingLocation(Context context){
		return getSettingSharedPreferences(context).getBoolean(KEY_LOCATION_SHARE,true);
	}
	

	*//**
	 * 获得通知是否显示详情
	 * @param context
	 * @return
	 *//*
	public static boolean getNotification(Context context){
		return getSettingSharedPreferences(context).getBoolean(KEY_NOTIFICATION_SHOW,true);
	}
	

	*//**
	 * 获得通知是否显示推送贴士
	 * @param context
	 * @return
	 *//*
	public static boolean getPushOpen(Context context){
		return getSettingSharedPreferences(context).getBoolean(KEY_PUSH_SHOW,true);
	}

	*//**
	 * 创建一个sharedPreferences
	 * @param context
	 * @return
	 *//*
	private static SharedPreferences getSettingSharedPreferences(Context context){
		SharedPreferences preferences=context.getSharedPreferences(SYSTEM_SETTING_PREFER, Context.MODE_PRIVATE);
		return preferences;
	}
	*//**
	 * 地理位置共享
	 * @param isShare
	 *//*
	public static void saveSettingLocation(Context context,boolean isShare){
//		AppTools.getSettingManager(context).setLocationShare(isShare);
		SettingManager.getSettingManager(context).setLocationShare(isShare);
		Editor editor = createEditor(context,SYSTEM_SETTING_PREFER);
		editor.putBoolean(KEY_LOCATION_SHARE, isShare);
		editor.commit();
	}
	
	*//**
	 * 获得编辑器
	 * @param context
	 * @param name
	 * @return
	 *//*
	private static Editor createEditor(Context context,String name){
		SharedPreferences sharedPreferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		return editor;
	}

	*//**
	 * 按键声音
	 * @param isShare
	 *//*
	public static void saveSettingSound(Context context,boolean isShare){
		//SettingManager.isSoundOpen = isShare;
		SettingManager.getSettingManager(context).setSoundOpen(isShare);
		Editor editor = createEditor(context,SYSTEM_SETTING_PREFER);
		editor.putBoolean(KEY_SETTING_RING, isShare);
		editor.commit();
	}
	
	*//**
	 * 推送健康贴士
	 * @param isShare
	 *//*
	public static void saveSettingPush(Context context,boolean isShare){
		SettingManager.getSettingManager(context).setSendIos(isShare);
		Editor editor = createEditor(context,SYSTEM_SETTING_PREFER);
		editor.putBoolean(KEY_PUSH_SHOW, isShare);
		editor.commit();
	}
	
	*//**
	 * 震动提示
	 * @param isShare
	 *//*
	public static void saveSettingShack(Context context,boolean isShare){
		SettingManager.getSettingManager(context).setVibrateOpen(isShare);
		Editor editor = createEditor(context,SYSTEM_SETTING_PREFER);
		editor.putBoolean(KEY_SETTING_VIBRATE, isShare);
		editor.commit();
	}
	
	*//**
	 * 通知不显示详情
	 * @param isShare
	 *//*
	public static void saveSettingNotification(Context context,boolean isShare){
		//SettingManager.isShowNotificationDetail = isShare;
		SettingManager.getSettingManager(context).setShowNotificationDetail(isShare);
		Editor editor = createEditor(context,SYSTEM_SETTING_PREFER);
		editor.putBoolean(KEY_NOTIFICATION_SHOW, isShare);
		editor.commit();
	}
	
	*//**
	 * 判断邮件email是否正确格式 
	 * @param email
	 * @return
	 *//*
	  public boolean  checkEmail(String email) {
	        Pattern pattern = Pattern.compile("^\\w+([-.]\\w+)*@\\w+([-]\\w+)*\\.(\\w+([-]\\w+)*\\.)*[a-z]{2,3}$");
	        Matcher matcher = pattern.matcher(email);
	        if (matcher.matches()) {
	            return true;
	        }
	        return false;
	    }
	 
	  // 判断手机号phone是否正确格式 
	     public   boolean  checkPhone(String phone) {
	    	  
	    	String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";  
	    	  
	    	Pattern p = Pattern.compile(regExp);  
	    	  
	    	Matcher m = p.matcher(phone);  
	    	  
	    	return m.find();//boolean 
	    } */
}
