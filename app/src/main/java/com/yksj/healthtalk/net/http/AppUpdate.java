package com.yksj.healthtalk.net.http;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;

import org.apache.http.protocol.HTTP;
import org.universalimageloader.utils.StorageUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class AppUpdate {
	private static boolean isDownloading = false;
	public  final String DOWNLOAD_DIR = StorageUtils.getDownCachePath();
	
	public  final int NOTIFICATION_ID = 20010;
	public  final int MALFORMED_URL_EXCEPTION = -100;
	
	private HTalkApplication mContext;
	private NotificationManager mNotificationManager;
	private Notification mNotification;
	File mFile;
	
	String healthDown;//六一健康下载
	String healthUpdate;//六一健康更新
	String downProgeress;
	String downloaded;//下载完成
	private final String FILENAME_APK= "HZCenter.apk";


	public interface AppUpdateListener{
		/**
		 * @param isUpdate 是否更新标记
		 * @param path 下载路径
		 * @param updateNote 更新内容说明
		 */
		public void onUpdate(boolean isUpdate,String path,String updateNote);
		/**
		 * 错误检测
		 * @param errorCode
		 */
		public void onError(int errorCode);
	}
	
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			notification(msg.arg1);
		}
	};
	
	public AppUpdate(){
		mContext = HTalkApplication.getApplication();
		healthDown = mContext.getString(R.string.setting_health_down);
		healthUpdate = mContext.getString(R.string.setting_health_update);
		downProgeress = mContext.getString(R.string.setting_health_downed_progres);
		downloaded =mContext.getString(R.string.setting_health_downloaded);
	}

	private void notification(int progress){
		if(mNotificationManager == null){
			mNotificationManager = mContext.getNotificationManager();
			mNotification = new Notification(R.drawable.launcher_logo,healthDown,System.currentTimeMillis());
		}
//		if(progress < 100){
//			mNotification.flags = Notification.FLAG_NO_CLEAR;
//			mNotification.flags |= Notification.FLAG_ONGOING_EVENT;
//			Intent intent = new Intent();
//			mNotification.contentIntent = PendingIntent.getActivity(mContext,0,intent,0);
//			mNotification.setLatestEventInfo(mContext,healthUpdate,downProgeress+progress+"%", mNotification.contentIntent);
//		}else{
//			mNotification.defaults = Notification.DEFAULT_SOUND;
//			mNotification.flags = Notification.FLAG_AUTO_CANCEL;
//			Intent intent = new Intent(Intent.ACTION_VIEW);
//			intent.setDataAndType(Uri.fromFile(new File(mFile.getAbsolutePath())), "application/vnd.android.package-archive");
//			mNotification.contentIntent = PendingIntent.getActivity(mContext,0,intent,0);
//			mNotification.setLatestEventInfo(mContext,healthUpdate,downloaded, mNotification.contentIntent);
//		}
		mNotificationManager.notify(NOTIFICATION_ID,mNotification);
	}
	
	private void cancelNotification(){
		Toast.makeText(mContext,mContext.getString(R.string.setting_health_down_fail),Toast.LENGTH_SHORT).show();
		mNotificationManager.cancel(NOTIFICATION_ID);
	}
	
	public void donwloadApp(String webPath){
		if(isDownloading)return;
		isDownloading = true;
		File dir = new File(DOWNLOAD_DIR);
		if(!dir.exists())dir.mkdirs();
		mFile = new File(dir,FILENAME_APK);
		try {
			if(mFile.exists())mFile.delete();
			mFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mHandler.sendMessage(Message.obtain());
		new AppDownload(webPath,mFile).execute(mFile.length());
	}
	
	class AppDownload extends AsyncTask<Long,Integer,Integer>{
		String mWebPath;
		File mFile;
		
		public AppDownload(String webPath,File file){
			mWebPath = webPath;
			mFile = file;
		}
		
		@Override
		protected Integer doInBackground(Long... params) {
			HttpURLConnection httpURLConnection = null;
			BufferedInputStream bufferedInputStream = null;
			BufferedOutputStream bufferedOutputStream = null;
			URL url;
			try {
				url = new URL(mWebPath);
				httpURLConnection = (HttpURLConnection)url.openConnection();
				// 读取超时最长时间
				httpURLConnection.setReadTimeout(10 * 1000);
				//连接时间
				httpURLConnection.setConnectTimeout(1000 * 10);
				// 允许输入输出
				httpURLConnection.setDoInput(true);
				httpURLConnection.setDoOutput(true);
				// 不允许缓存
				httpURLConnection.setUseCaches(false);
				httpURLConnection.setRequestMethod("POST");
//				httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
				httpURLConnection.setRequestProperty(HTTP.CHARSET_PARAM, HTTP.UTF_8);
				httpURLConnection.setRequestProperty(HTTP.CONN_DIRECTIVE,HTTP.CONN_KEEP_ALIVE);

				//文件长度
				int fileLength = httpURLConnection.getContentLength();
				
				int readLength = 0;//当前读取长度
				long completeLength = 0;//完成度
				byte[] buffer = new byte[1024*2];
				
				bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
				bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(mFile));
				long lastTime = System.currentTimeMillis();
				
				while((readLength = bufferedInputStream.read(buffer))>0){
					bufferedOutputStream.write(buffer,0,readLength);
					completeLength += readLength;
					long currentTime = System.currentTimeMillis();
					int progress = (int)(((double)completeLength/fileLength)*100);
					if(progress==100 || currentTime-lastTime >= 1000){
						Message message = mHandler.obtainMessage();
						message.arg1 = progress;
						mHandler.sendMessage(message);
						lastTime = currentTime;
					}
				}
				bufferedOutputStream.flush();
			}catch(MalformedURLException e){
				e.printStackTrace();
				return -1;
			}catch(IOException e){
				e.printStackTrace();
				return -1;
			}finally{
				try {
					if(bufferedInputStream != null)bufferedInputStream.close();
					if(bufferedOutputStream != null)bufferedOutputStream.close();
					if(httpURLConnection != null)httpURLConnection.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
					return 1;
				}
			}
			return 1;
		}
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if(result == -1)cancelNotification();
			isDownloading = false;
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.fromFile(mFile), "application/vnd.android.package-archive");
			mContext.startActivity(intent);
		}
	}
}

