package com.yksj.healthtalk.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.yksj.consultation.son.doctor.AtyDoctorMassage;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.consultation.son.app.AppData;
import com.yksj.consultation.son.home.PersonInfoActivity;

/**
 * 好友上传资料和更新资料
 * 
 * @author Administrator
 * 
 */
public class PersonInfoUtil {
	private static String BOUNDARY = "---------------------------7dd29224170ac8";
	private static final String FILEUPLOAD_ENDTAG = "\r\n--" + BOUNDARY + "--"
			+ "\r\n";
	private static long fileLength;
	private static File file;

	public static String updatePersonInfo(String imagepath, String infoJson,
			String url) {
		HttpURLConnection conn = null;
		long dataLength = 0;
		StringBuilder textEntity = new StringBuilder();
		textEntity.append("--");
		textEntity.append(BOUNDARY);
		textEntity.append("\r\n");
		textEntity.append("Content-Disposition: form-data; name=\"" + "xxxx"
				+ "\"\r\n\r\n");
		textEntity.append(infoJson);
		textEntity.append("\r\n");

		textEntity.append("--");
		textEntity.append(BOUNDARY);
		textEntity.append("\r\n");
		
		
		if (imagepath == null) {
			imagepath = "";
		}
		textEntity.append("Content-Disposition: form-data; name=\"" + "xxx1"
				+ "\"; filename=\"" + imagepath + "\"");
		textEntity.append("\r\n");
		
		if (imagepath == null || imagepath.equals("")) {
			textEntity.append("Content-Type: application/octet-stream"
					+ "\r\n\r\n");
			dataLength = textEntity.toString().getBytes().length
					+ FILEUPLOAD_ENDTAG.getBytes().length;
		} else {
			textEntity.append("Content-Type: image/x-png" + "\r\n\r\n");
			file = new File(imagepath);
			fileLength = file.length();
			dataLength = textEntity.toString().getBytes().length + fileLength
					+ FILEUPLOAD_ENDTAG.getBytes().length;
		}
		try {
			URL u = new URL(url);
			conn = (HttpURLConnection) u.openConnection();
			conn.setDoOutput(true);// 允许对外发送请求参数
			conn.setUseCaches(false);// 不进行缓存
			conn.setConnectTimeout(15 * 1000);
			conn.setRequestMethod("POST");
			// 下面设置http请求头
			conn.setRequestProperty(
					"Accept",
					"application/x-shockwave-flash, image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/msword, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/xaml+xml, application/x-ms-xbap, application/x-ms-application, */*");
			conn.setRequestProperty("Accept-Language", "zh-CN");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);
			conn.setRequestProperty("Content-Length",
					String.valueOf(dataLength));
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setChunkedStreamingMode(128 * 1024);

			DataOutputStream outStream = new DataOutputStream(
					conn.getOutputStream());
			outStream.write(textEntity.toString().getBytes());
			if (imagepath != null && !"".equals(imagepath)) {
				FileInputStream fileInput = new FileInputStream(file);
				byte[] buffer = new byte[1024 * 4];
				int length;
				while ((length = fileInput.read(buffer)) > 0) {
					outStream.write(buffer, 0, length);
				}
			}
			outStream.writeBytes(FILEUPLOAD_ENDTAG);
			outStream.flush();

			InputStream inputStream = conn.getInputStream();
			byte[] bytes = new byte[1024];
			String strResult = "";
			int length;
			while ((length = inputStream.read(bytes)) != -1) {
				strResult = strResult + new String(bytes, 0, length);
			}
			return strResult;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/**
	 * 判断个人资料跳转
	 * @param id  对方id
	 * @param context 
	 * @param ROLE_ID 对方的
	 */
	public static void choiceActivity(String id,Context context,String roleId){
		if(id.equals(AppData.DYHSID)){//是导医护士直接聊天
			CustomerInfoEntity daoyi=new CustomerInfoEntity();
			daoyi.setId(id);
			FriendHttpUtil.chatFromPerson((FragmentActivity)context,daoyi);
		}else  if ("777".equals(roleId)||"888".equals(roleId)){//改新版六一健康
			Intent intent = new Intent(context, AtyDoctorMassage.class);
			intent.putExtra("id", id);
			intent.putExtra("type", "1");
			context.startActivity(intent);
//			Intent intent = new Intent(context, DoctorClinicMainActivity.class);
//			intent.putExtra("id", id);
//			intent.putExtra("type", "1");
//			context.startActivity(intent);
		}else if (SmartFoxClient.helperId.equals(id)) {//多美助手
//			Intent intent = new Intent(context,SystemUserInforActivity.class);
//			intent.putExtra("id", id);
//			context.startActivity(intent);
		}else{
			Intent intent = new Intent();
			intent.setClass(context, PersonInfoActivity.class);
			intent.putExtra("id",id);
			context.startActivity(intent);
		}
	}
	
	/**
	 * 
	 * @param isDoctor 对方是否是医生
	 * @return
	 */
	public static String getTalkName(boolean isDoctor){
		if(SmartFoxClient.getLoginUserInfo().isDoctor() && isDoctor){
			return "对话";
		}else if(SmartFoxClient.getLoginUserInfo().isDoctor() && !isDoctor){
			return "看病人";
		}else if(!SmartFoxClient.getLoginUserInfo().isDoctor() && isDoctor){
			return "咨询医生";
		}else {
			return "对话";
		}
		
	}

}
