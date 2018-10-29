package com.yksj.healthtalk.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.yksj.consultation.son.app.AppManager;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.services.CoreService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 异常日志捕获上传
 * 
 * @author zhao
 * 
 */
public class AppCashHandler implements UncaughtExceptionHandler {

	// 是否开启日志输出,debug状态下开启
	public static final boolean DEBUG = LogUtil.DEBUG;

	// 用于格式化日期,作为日志文件名的一部分

	// 自定义处理异常
	private static AppCashHandler mAppExceptionCashHandler;

	// 异常由系统处理
	private Thread.UncaughtExceptionHandler defaultExceptionHandler;

	// 用来存储设备信息和异常信息
	private final Map<String, String> infos = new HashMap<String, String>();

	private Context mContext;

	private AppCashHandler() {
	}

	public static AppCashHandler getInstance() {
		if (mAppExceptionCashHandler == null) {
			mAppExceptionCashHandler = new AppCashHandler();
		}
		return mAppExceptionCashHandler;
	}

	/**
	 * 设置异常由系统进行处理
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
		defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	
	/**
	 * 异常发生转入该函数
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		HTalkApplication.getApplication().cancelNotify();
		ex.printStackTrace();

		if (!handleException(ex) && defaultExceptionHandler != null) {
			defaultExceptionHandler.uncaughtException(thread, ex);
		} else {
			// 自己处理异常
			mContext.stopService(new Intent(mContext, CoreService.class));
						
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
			AppManager.getInstance().finishAllActivity();
			try{
				Thread.sleep(800l);
			}catch(Exception e){
			}
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

	private boolean handleException(final Throwable ex) {
		
		saveDeviceInfo(mContext);

		saveCrashInforFile(ex);

		return true;
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	public void saveDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				DateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss",Locale.getDefault());
				infos.put("date", mFormat.format(new Date()));
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
				infos.put("model", android.os.Build.MODEL);
				infos.put("releaseV", android.os.Build.VERSION.RELEASE);
				infos.put("sdkV", android.os.Build.VERSION.SDK);
			}
		} catch (NameNotFoundException e) {
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 保存错误信息到文件中
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private void saveCrashInforFile(Throwable ex) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try {
			saveLogFile(sb);
		} catch (Exception e) {
		}
	}
	
	/**
	 * 保存日志文件
	 * @throws IOException 
	 */
	private void saveLogFile(StringBuffer sb) throws IOException{
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String fileName = "crash.log";
			String root = Environment.getExternalStorageDirectory().toString()+"/consultation/log/";
			File dir = new File(root);
			if(!dir.exists()){
				dir.mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(dir.getAbsolutePath() + fileName);
			fos.write(sb.toString().getBytes());
			fos.flush();
			fos.close();
		}
	}
	
	/**
	 * 发送错误日志到服务器
	 */
	public void sendLogToServer(){
		StringBuffer buffer = new StringBuffer();
		String root = Environment.getExternalStorageDirectory().toString()+"/consultation/log/";
		final File file = new File(root,"crash.log");
		if(file.exists()){
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String str = reader.readLine();
				while(str != null){
					//数据不能太大，最大1M上传
					buffer.append(str);
					str = reader.readLine();
					if(buffer.length() == 1048576) break;
				}
				reader.close();
				file.deleteOnExit();
				//提交异常
				HttpRestClient.doHttpReportAppException(buffer.toString(),null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void reportExceptionToServer(String exception) {
		String sdk = SystemUtils.getSdkVersion();
		String versionRelease = SystemUtils.getVersionRelease();
		String model = SystemUtils.getModel();
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("clientVcode", SystemUtils
					.getAppVersionCode(HTalkApplication.getHTalkApplication()));
			jsonObject.put("clientVname", SystemUtils
					.getAppVersionName(HTalkApplication.getHTalkApplication()));
			jsonObject.put("releaseV", versionRelease);
			jsonObject.put("sdkV", sdk);
			jsonObject.put("model", model);
			jsonObject.put("exception", exception);
			/*
			 * HttpRestClient.doHttpReportAppException(jsonObject.toString(),
			 * new AsyncHttpResponseHandler(){
			 * 
			 * @Override public void onFinish() {
			 * android.os.Process.killProcess(android.os.Process.myPid());
			 * System.exit(0); } });
			 */
		} catch (JSONException e) {
		}
	}

}
