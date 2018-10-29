package com.yksj.healthtalk.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.yksj.consultation.son.chatting.ChatHelperUtils;
import com.yksj.healthtalk.db.ChatUserHelper;
import com.yksj.healthtalk.entity.GroupInfoEntity;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.consultation.comm.AdvertFragment;
import com.yksj.consultation.comm.AdvertFragment.AdvertEntity;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.salon.SalonSelectPaymentOptionActivity.OnBuyTicketHandlerListener;

/**
 * @ClassName: SalonHttpUtil
 * @Description: 话题网络请求的工具类
 * @date 2012-12-30 上午8:41:20
 */
public class SalonHttpUtil {
	/**
	 * 上传分隔符
	 */
	public static final String BOUNDARY = "---------------------------7dd196f1b0c70";
	/**
	 * 上传文件结束标记
	 */
	private static final String FILEUPLOAD_ENDTAG = "\r\n--" + BOUNDARY + "--"
			+ "\r\n";

	/**
	 * @Title: getSalonInfo
	 * @Description: 获取话题信息
	 * @param @param fristParameter
	 * @param @param secondParameter
	 * @param @param page
	 * @param @param size
	 * @param @param userId
	 * @param @param type
	 * @param @param sort 设定文件
	 * @return void 返回类型
	 * @throws
	 * 废弃
	 */
//	private static void getSalonInfo(GroupInfoEntity mGroupInfoEntity,
//			final int sourceType, final SalonHttpListener mSalonHttpListener) {
//		HttpRestClient.doHttpRequestSearchGroup(mGroupInfoEntity, sourceType,
//				new AsyncHttpResponseHandler() {
//					@Override
//					public void onSuccess(int statusCode, String content) {
//						if (!content.equals("N")) {
//							mSalonHttpListener.response(sourceType, content);
//						}else {
//							mSalonHttpListener.response(-1,content);
//						}
//						super.onSuccess(statusCode, content);
//					}
//				});
//	}
//	
//
//	public interface SalonHttpListener {
//		void response(int sourceType, String content);
//	}
	
	/**
	 * 这个方法和上面的方法一样  若有变动  两个一起变动
	 * @Title: getSalonInfo
	 * @Description: 获取话题信息
	 * @param @param sort 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void getSalonInfoData(GroupInfoEntity mGroupInfoEntity,
			final int sourceType,AsyncHttpResponseHandler handler) {
		HttpRestClient.doHttpRequestSearchGroup(mGroupInfoEntity, sourceType,handler);
	}


	/**
	 * @Title:(加关注 或者取消关注的时候  加载登录者话题的时候)发生关系的时候调用
	 * @Description: 解析Json SalonEntity实体
	 * @param @param content 数据
	 * @throws
	 */
	public static void jsonAnalysisSalonEntity(
			Context context, String content) {
		/**
		 * "biggb": "assets/group/m5_khmale.png", "chargingFlag": "0",
		 * "cilentbg": "assets/group/m_qjbing.png", "classID": "", "className":
		 * "", "createCustomerID": "2473", "createTime": "20120513025633",
		 * "customerid": "2601", "flag": "1115", "flagPlacing": "",
		 * "groupClass": "2", "groupId": "1115", "groupLevel": "1",
		 * "groupState": "", "groupVersion": "125207", "groupflag": "2",
		 * "hasExtensionInfomaiton": 0, "limitNnum": "1100", "merchantId": "",
		 * "onlineNum": "人", "openDate": "20130408151929", "personNum": "31人",
		 * "recordDesc": "", "recordName": "膀胱结石的治疗", "releaseSystemMessage":
		 * "1", "showPersonnum": true, "yesornoShow": false
		 */
		JSONArray mJsonArray = null;
		try {
			if (content.indexOf("[") == 0) {
				mJsonArray = new JSONArray(content);
			} else {
				JSONObject mJsonObject = new JSONObject(content);
				if (mJsonObject.has("group")) {
					mJsonArray = mJsonObject.getJSONArray("group");
				} else {
					StringBuilder builder = new StringBuilder();
					content = content.substring(0, content.length());
					builder.append("[");
					builder.append(content);
					builder.append("]");
					mJsonArray = new JSONArray(builder.toString());
				}
			}
			ChatUserHelper.getInstance().initGroupInfo(mJsonArray);
		} catch (JSONException e) {
		}
	}
	
	/**
	 * 解析加载出来的各个标签及其对应id
	 * @param content  数据字符串
	 * @return  ArrayList
	 */
	public static ArrayList<HashMap<String, Object>> jsonAnalysisInfolys(String content){
		ArrayList<HashMap<String, Object>> infos=new ArrayList<HashMap<String,Object>>();
		JSONArray array = null;
		try {
			JSONObject object = new JSONObject(content);
			if(object.has("groupInfoLay"))//生活话题
				array=object.getJSONArray("groupInfoLay");
			else if(object.has("interestInfoLay"))
				array=object.getJSONArray("interestInfoLay");
			else if(object.has("findConsuInfoLay"))
				array=object.getJSONArray("findConsuInfoLay");
			for(int i=0;i<array.length();i++){
				HashMap<String, Object> map=new HashMap<String, Object>();
				JSONObject obj = array.getJSONObject(i);
				map.put("name", obj.optString("INFO_LAY_NAME"));
				map.put("id", obj.optString("INFO_LAY_ID"));
				infos.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return infos;
	}
	
	/**
	 * 解析加载到的话题数据
	 * @param context
	 * @param content
	 * @return
	 */
	public static ArrayList<GroupInfoEntity> jsonAnalysisTopicEntitys(
			Context context, String content) {
		ArrayList<GroupInfoEntity> salonEntities = new ArrayList<GroupInfoEntity>();
		JSONArray array = null;
		try {
			JSONObject mJsonObject = new JSONObject(content);
			if(mJsonObject.has("findNotDocGroupList"))//生活话题
				array=mJsonObject.getJSONArray("findNotDocGroupList");
			else if(mJsonObject.has("findDocGroupList"))
				array=mJsonObject.getJSONArray("findDocGroupList");//医生话题
			else if(mJsonObject.has("findGroupListByInfoLayId"))
				array=mJsonObject.getJSONArray("findGroupListByInfoLayId");//根据标签id搜索的话题
			else if(mJsonObject.has("findGroupListByName"))
				array=mJsonObject.getJSONArray("findGroupListByName");//根据搜索话题名称
			else if(mJsonObject.has("findMyFocusGroupList"))
				array=mJsonObject.getJSONArray("findMyFocusGroupList");//我关注的
			else if(mJsonObject.has("findMyBoughtGroupList"))
				array=mJsonObject.getJSONArray("findMyBoughtGroupList");//我购买的
			else if(mJsonObject.has("findGroupListByParam"))
				array=mJsonObject.getJSONArray("findGroupListByParam");//bannar条跳转按参数
			else if(mJsonObject.has("finGroupListByClass"))
				array=mJsonObject.getJSONArray("finGroupListByClass");//bannar条跳转按参数
			else if(mJsonObject.has("findMyList"))
				array=mJsonObject.getJSONArray("findMyList");//bannar条跳转按参数
			else
				array=mJsonObject.getJSONArray("findMyCreGroupList");//医生话题
			for(int i=0;i<array.length();i++){
				GroupInfoEntity mSalonEntity = new GroupInfoEntity();
				JSONObject object = array.getJSONObject(i);
				mSalonEntity.setNormalHeadIcon(object.optString("CLIENT_ICON_BACKGROUND"));//头像
				mSalonEntity.setBigHeadIcon(object.optString("BIG_ICON_BACKGROUND"));//大头像
				mSalonEntity.setName(object.optString("RECORD_NAME"));//沙龙名称
				mSalonEntity.setRecordDesc(object.optString("RECORD_DESC"));//沙龙描述
				mSalonEntity.setId(object.optString("GROUP_ID"));//沙龙Id
				mSalonEntity.setFlagPlacing(object.optString("FLAGPLACING"));//
				mSalonEntity.setPersonNumber(object.optString("PERSONNUM"));//关注人数
				mSalonEntity.setCreateCustomerID(object.optString("CUSTOMER_ID"));
				//关注标记（已关注101700，空或者其他是未关注）
				mSalonEntity.setSalonAttention("101700".equals(object.optString("RELATION_TYPE")));//关注标记
				mSalonEntity.setTicketMsg(object.optString("TICKETMSG"));
				salonEntities.add(mSalonEntity);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return salonEntities;
	}
	

	/**
	 * @Title: jsonAnalysisSalonEntity
	 * @Description: 解析Json SalonEntity实体
	 * @param @param content 数据
	 * @throws
	 */
	public static ArrayList<GroupInfoEntity> jsonAnalysisSalonEntity(
			String content) {
		ArrayList<GroupInfoEntity> salonEntities = new ArrayList<GroupInfoEntity>();
		JSONArray mJsonArray = null;
		try {
			if (content.indexOf("[") == 0) {
				mJsonArray = new JSONArray(content);
			} else {
				JSONObject mJsonObject = new JSONObject(content);
				if (mJsonObject.has("group")) {
					mJsonArray = mJsonObject.getJSONArray("group");
				} else {
					StringBuilder builder = new StringBuilder();
					content = content.substring(0, content.length());
					builder.append("[");
					builder.append(content);
					builder.append("]");
					mJsonArray = new JSONArray(builder.toString());
				}
			}

			for (int i = 0; i < mJsonArray.length(); i++) {
				JSONObject object = mJsonArray.getJSONObject(i);
				GroupInfoEntity mSalonEntity = new GroupInfoEntity();
				// 头像
				mSalonEntity.setBigHeadIcon(object.getString("biggb"));
				mSalonEntity.setCharge(object.getString("chargingFlag").equals(
						"1") ? true : false);// 是否收费
				mSalonEntity.setNormalHeadIcon(object.getString("cilentbg"));
				object.getString("classID");
				object.getString("className");
				mSalonEntity.setCreateCustomerID(object.getString("createCustomerID"));
				mSalonEntity.setCreateTime(object.getString("createTime"));
				mSalonEntity.setFlag(object.getString("flag"));
				mSalonEntity.setFlagPlacing(object.getString("flagPlacing"));
				mSalonEntity.setGroupClass(object.optString("groupClass"));
				// 是否是医师话题
				mSalonEntity.setSalon(object.getString("groupClass").equals("1") ? true : false);
				mSalonEntity.setId(object.getString("groupId"));// 群组ID
				mSalonEntity.setGroupLevel(object.getString("groupLevel"));// 话题等级(主治医师、副主任医师、主任医师
																			// 创建的话题默认为2级，其他默认为0级)
				object.getString("groupState");
				object.getString("groupVersion");
				mSalonEntity.setSalonAttention((object.getString("groupflag").equals("1")) ? true : false);// 标记（1为收藏，0为未收藏）
				mSalonEntity.setLimitNumber(object.getString("limitNnum"));// 上限人数
				mSalonEntity.setMerchantId(object.getString("merchantId"));
				mSalonEntity.setOnLineNumber(object.getString("onlineNum"));// 在线人数
				object.getString("openDate");
				mSalonEntity.setPersonNumber(object.getString("personNum"));// 此聊天室内当前有多少人
				mSalonEntity.setRecordDesc(object.getString("recordDesc"));// 描述
				mSalonEntity.setName(object.getString("recordName"));// 名称
				mSalonEntity.setIsReleaseSystemMessage(object.getString("releaseSystemMessage").equals("1") ? true : false);// 是否将消息发布到消息厅(1-发布，0-不发布)
				mSalonEntity.setShowPersonNumber(object.getBoolean("showPersonnum"));// 是否显示此聊天室内当前有多少人
				mSalonEntity.setYesornoShow(object.getBoolean("yesornoShow"));// 是否显示在线人数
				mSalonEntity.setOrderInfo(object.optString("orderInfo"));// 是否需要再次支付
				mSalonEntity.setTicketMsg(object.optString("ticketMsg"));//医生开通的门票信息

//				mSalonEntity.setGroupHeadPortraitName(groupHeadPortraitName)
//				mSalonEntity.setName();
				
				if (object.getInt("hasExtensionInfomaiton") != 0) {
					mSalonEntity.setGroupHeadPortraitID(object.getString("groupHeadPortraitID"));// 群组头像ID
					mSalonEntity.setGroupHeadPortraitName(object.getString("groupHeadPortraitName"));// 群组头像名称
					mSalonEntity.setInceptMessage(object.getString("inceptMessage").equals("Y") ? true : false);
					mSalonEntity.setInfoId(object.getString("infoLayid"));// 小类信息层面id
					mSalonEntity.setInfoLayName(object.getString("infoLayName"));// 信息层面名称
					mSalonEntity.setUpperId(object.getString("larglayid"));// 大类id
					mSalonEntity.setCusMessag("");
					mSalonEntity.setPublicCustInfo(object.getString("publicCustInfo").equals("Y") ? true : false);// 是否公开创建者信息(Y/N,默认为Y)
					mSalonEntity.setName(object.getString("recordName"));// 群组名称
					mSalonEntity.setNote(object.getString("note"));// 签名
				}
				salonEntities.add(mSalonEntity);
			}
		} catch (JSONException e) {
		}
		return salonEntities;
	}

	
	/**
	 * @Title: jsonAnalysisSalonEntity
	 * @Description: 解析出我的话题和我创建的话题以外的其他话题
	 * @param @param content 数据
	 * @throws
	 */
	public static ArrayList<GroupInfoEntity> jsonAnalysisSalonEntity(
			String content, List<String> list) {
		ArrayList<GroupInfoEntity> salonEntities = new ArrayList<GroupInfoEntity>();
		JSONArray mJsonArray = null;
		try {
			if (content.indexOf("[") == 0) {
				mJsonArray = new JSONArray(content);
			} else {
				JSONObject mJsonObject = new JSONObject(content);
				if (mJsonObject.has("group")) {
					mJsonArray = mJsonObject.getJSONArray("group");
				} else {
					StringBuilder builder = new StringBuilder();
					content = content.substring(0, content.length());
					builder.append("[");
					builder.append(content);
					builder.append("]");
					mJsonArray = new JSONArray(builder.toString());
				}
			}
			for (int i = 0; i < mJsonArray.length(); i++) {
				JSONObject object = mJsonArray.getJSONObject(i);
				GroupInfoEntity mSalonEntity = new GroupInfoEntity();
				// 头像
				mSalonEntity.setBigHeadIcon(object.getString("biggb"));
				mSalonEntity.setCharge(object.getString("chargingFlag").equals(
						"1") ? true : false);// 是否收费
				mSalonEntity.setNormalHeadIcon(object.getString("cilentbg"));
				object.getString("classID");
				object.getString("className");
				mSalonEntity.setCreateCustomerID(object.getString("createCustomerID"));
				mSalonEntity.setCreateTime(object.getString("createTime"));
				mSalonEntity.setFlag(object.getString("flag"));
				mSalonEntity.setFlagPlacing(object.getString("flagPlacing"));
				// 是否是医师话题
				mSalonEntity.setSalon(object.getString("groupClass").equals("1") ? true : false);
				mSalonEntity.setId(object.getString("groupId"));// 群组ID
				mSalonEntity.setGroupLevel(object.getString("groupLevel"));// 话题等级(主治医师、副主任医师、主任医师
																			// 创建的话题默认为2级，其他默认为0级)
				object.getString("groupState");
				object.getString("groupVersion");
				mSalonEntity.setSalonAttention((object.getString("groupflag").equals("1")) ? true : false);// 标记（1为收藏，0为未收藏）
				mSalonEntity.setLimitNumber(object.getString("limitNnum"));// 上限人数
				mSalonEntity.setMerchantId(object.getString("merchantId"));
				mSalonEntity.setOnLineNumber(object.getString("onlineNum"));// 在线人数
				object.getString("openDate");
				mSalonEntity.setPersonNumber(object.getString("personNum"));// 此聊天室内当前有多少人
				mSalonEntity.setRecordDesc(object.getString("recordDesc"));// 群组主题
				mSalonEntity.setName(object.getString("recordName"));// 名称
				mSalonEntity.setIsReleaseSystemMessage(object.getString("releaseSystemMessage").equals("1") ? true : false);// 是否将消息发布到消息厅(1-发布，0-不发布)
				mSalonEntity.setShowPersonNumber(object.getBoolean("showPersonnum"));// 是否显示此聊天室内当前有多少人
				mSalonEntity.setYesornoShow(object.getBoolean("yesornoShow"));// 是否显示在线人数
				mSalonEntity.setOrderInfo(object.optString("orderInfo"));// 是否需要再次支付
				if (object.getInt("hasExtensionInfomaiton") != 0) {
					mSalonEntity.setGroupHeadPortraitID(object.getString("groupHeadPortraitID"));// 群组头像ID
					mSalonEntity.setGroupHeadPortraitName(object.getString("groupHeadPortraitName"));// 群组头像名称
					mSalonEntity.setInceptMessage(object.getString("inceptMessage").equals("Y") ? true : false);
					mSalonEntity.setInfoId(object.getString("infoLayid"));// 小类信息层面id
					mSalonEntity.setInfoLayName(object.getString("infoLayName"));// 信息层面名称
					mSalonEntity.setUpperId(object.getString("larglayid"));// 大类id
					mSalonEntity.setCusMessag("");
					mSalonEntity.setPublicCustInfo(object.getString("publicCustInfo").equals("Y") ? true : false);// 是否公开创建者信息(Y/N,默认为Y)
					mSalonEntity.setName(object.getString("recordName"));// 群组名称
					mSalonEntity.setNote(object.getString("note"));// 签名
				}
				HTalkApplication.getAppData().cacheInformation.put(mSalonEntity.getId(), mSalonEntity);
				if (!list.contains(mSalonEntity.getId())) {
					list.add(mSalonEntity.getId());
				}
			}
		} catch (JSONException e) {
		}
		return salonEntities;
	}

	/**
	 * @Title: jsonAnalysisAdvertEntity
	 * @Description: 解析广告实体
	 * @param @param JSONObject
	 * @return void
	 * @throws
	 */
	public static ArrayList<AdvertEntity> jsonAnalysisAdvertEntity(
			String content) {
		ArrayList<AdvertEntity> advertEntities = new ArrayList<AdvertEntity>();
		try {
			JSONObject mJsonObject = new JSONObject(content);
			JSONArray mJsonArray;
			if (mJsonObject.has("adver")) {
				mJsonArray = mJsonObject.getJSONArray("adver");
			} else {
				return null;
			}
			for (int i = 0; i < mJsonArray.length(); i++) {
				JSONObject object = mJsonArray.getJSONObject(i);
				String adver_Content_Path = object.getString("adver_Content_Path");// 广告地址
				String adver_Icon_Path = HttpRestClient.getmHttpUrls().URL_QUERYHEADIMAGE+ object.getString("adver_Icon_Path");// 头像地址
				String adver_id = object.getString("adver_id");
				String begin_time = object.getString("begin_time");
				String end_time = object.getString("end_time");
				AdvertFragment.AdvertEntity entity = (new AdvertFragment()).new AdvertEntity(
						adver_id, 0, adver_Content_Path, adver_Icon_Path);
				advertEntities.add(entity);
			}
		} catch (JSONException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			return null;
		}
		return advertEntities;
	}

	/**
	 * 创建话题
	 * 
	 * @param path
	 * @param groupId
	 * @param value
	 * @return
	 */
	public static String createSalon(String path, String groupId, String value) {
		JSONObject json;
		HttpURLConnection httpURLConnection = null;
		DataOutputStream outPut = null;
		FileInputStream fileInput = null;
		File file = null;
		long fileLength = 0;
		if (path != null && !path.equals("")) {
			file = new File(path);
			fileLength = file.length();
		}
		// //Log.i(TAG, "上传文件大小:" + fileLength+":");
		try {
			if (path == null || path.equals("")) {
				try {
					httpURLConnection = post(HttpRestClient.getmHttpUrls().URL_CREATE_SALON,value, "UTF-8");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				// 初始化发送参数
				// //Log.i("test", value);
				// 上传头文件准备
				String strHeader = createFileUploadBeginTag(value, value);
				fileLength = fileLength + strHeader.getBytes().length;
				URL url = new URL(HttpRestClient.getmHttpUrls().URL_CREATE_SALON);
				httpURLConnection = (HttpURLConnection) url.openConnection();
				// http参数设置
				setHttpURLConnectionProperty(httpURLConnection, fileLength);
				httpURLConnection.connect();
				outPut = new DataOutputStream(httpURLConnection.getOutputStream());
				// //Log.i(TAG, "：" + strHeader);
				// 结尾
				outPut.writeUTF(strHeader);
				// outPut.writeBytes(strHeader);
				if (path != null && !path.equals("")) {
					fileInput = new FileInputStream(file);
					byte[] buffer = new byte[1024 * 4];
					int length;
					while ((length = fileInput.read(buffer)) > 0) {
						outPut.write(buffer, 0, length);
					}
				}
				outPut.writeBytes(FILEUPLOAD_ENDTAG);
				outPut.flush();
			}
			// 返回上传状态
			int stateCode = httpURLConnection.getResponseCode();
			// 响应请求失败
			// //Log.i(TAG, "上传返回状态" + stateCode);

			if (stateCode != 200) {
				return null;
			}
			// InputStream inputStream = httpURLConnection.getInputStream();
			// int first = inputStream.read();
			// int lengths = inputStream.available();
			// byte[] bytes = new byte[lengths+1];
			// bytes[0] = (byte)first;
			// inputStream.read(bytes,1,lengths);
			// 返回上传的文件名
			// String strResut = new String(bytes, 0, lengths+1);
			// //Log.i(TAG, "文件上传成功" + strResut);
			String line = "";
			String result = "";
			BufferedReader br = new BufferedReader(new InputStreamReader(
					httpURLConnection.getInputStream(), HTTP.UTF_8));
			while (null != (line = br.readLine())) {
				result += line;
			}
			return result;
		} catch (IOException e) {
			// Log.e(TAG, "文件上传io异常"+e.getMessage());
		} finally {
			try {
				if (httpURLConnection != null)
					httpURLConnection.disconnect();
				if (outPut != null)
					outPut.close();
				if (fileInput != null)
					fileInput.close();
			} catch (IOException e) {
			}
		}
		return null;
	}

	/**
	 * 发送请求
	 * 
	 * @param path
	 *            请求路径
	 * @param params
	 *            请求参数 key为参数名称 value为参数值
	 * @param encode
	 *            请求参数的编码
	 */
	public static HttpURLConnection post(String path, String params,
			String encode) throws Exception {
		HttpURLConnection conn = null;
		JSONObject object = new JSONObject(params);
		StringBuilder parambuilder = new StringBuilder("");
		if (params != null && params.trim().length() != 0) {
			StringBuilder textEntity = new StringBuilder();
			Iterator<String> itr = object.keys();
			while (itr.hasNext()) {
				String str = itr.next();
				textEntity.append("--");
				textEntity.append(BOUNDARY);
				textEntity.append("\r\n");
				textEntity.append("Content-Disposition: form-data; name=\""
						+ str + "\"\r\n\r\n");
				textEntity.append(URLEncoder.encode(object.getString(str),
						"UTF-8"));
				textEntity.append("\r\n");
			}
			int dataLength = textEntity.toString().getBytes().length
					+ FILEUPLOAD_ENDTAG.getBytes().length;
			byte[] data = parambuilder.toString().getBytes();
			URL url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);// 允许对外发送请求参数
			conn.setUseCaches(false);// 不进行缓存
			conn.setConnectTimeout(5 * 1000);
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
			// 发送参数
			DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
			outStream.write(textEntity.toString().getBytes());
			// 下面发送数据结束标志，表示数据已经结束
			outStream.write(FILEUPLOAD_ENDTAG.getBytes());
			outStream.flush();
		}
		return conn;
	}

	/**
	 * 文件上传头
	 * 
	 * @param jsonObj
	 * @param fileName
	 * @return
	 */
	private static final String createFileUploadBeginTag(String filename,
			String name) {
		StringBuilder textEntity = new StringBuilder();
		try {
			JSONObject object = new JSONObject(name);
			Iterator<String> itr = object.keys();
			while (itr.hasNext()) {
				String str = itr.next();
				textEntity.append("--");
				textEntity.append(BOUNDARY);
				textEntity.append("\r\n");
				textEntity.append("Content-Disposition: form-data; name=\""+ str + "\"\r\n\r\n");
				textEntity.append(object.getString(str));
				textEntity.append("\r\n");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data; ");
		sb.append("name=\"");
		sb.append(name);
		sb.append("\"; ");
		sb.append("filename=\"");
		sb.append(filename);
		sb.append("\"");
		sb.append("\r\n");
		sb.append("Content-Type: binary/data;charset=UTF-8");
		sb.append("\r\n");
		sb.append("\r\n");
		return textEntity.toString() + sb.toString() + FILEUPLOAD_ENDTAG;
	}

	/**
	 * 设置http连接属性
	 * 
	 * @param httpURLConnection
	 */
	private static void setHttpURLConnectionProperty(
			HttpURLConnection httpURLConnection, long fileLength) {
		// 缓存的最长时间
		// httpURLConnection.setReadTimeout(5 * 1000);
		httpURLConnection.setConnectTimeout(1000 * 60);
		// 允许输入
		httpURLConnection.setDoInput(true);
		// 允许输出
		httpURLConnection.setDoOutput(true);
		// 不允许缓存
		httpURLConnection.setUseCaches(false);
		// 请求方式
		try {
			httpURLConnection.setRequestMethod("POST");
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
		httpURLConnection.setRequestProperty(HTTP.CONN_DIRECTIVE,
				HTTP.CONN_KEEP_ALIVE);
		httpURLConnection.setRequestProperty(HTTP.CHARSET_PARAM, HTTP.UTF_8);
		// 伪造请求头
		httpURLConnection.setRequestProperty("Content-Length",
				String.valueOf(fileLength));
		httpURLConnection.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + BOUNDARY);
		// 设置每次传输的流大小，可以有效防止手机因为内存不足崩溃
		// 此方法用于在预先不知道内容长度时启用没有进行内部缓冲的 HTTP 请求正文的流。
		httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
	}

	/**
	 * 收藏和取消话题
	 * 
	 * @param context
	 * @param userid
	 * @param entity
	 * @return
	 */
	public static GroupInfoEntity requestAttOrUnfollowToSalon(Context context, GroupInfoEntity entity) {
		if (entity.isSalonAttention()) {
			SmartFoxClient.sendStrRequest(SmartFoxClient.RequestCode.COLLECT_GROUP_NOT,
					entity.getId());
		} else {
			
			SmartFoxClient.sendStrRequest(SmartFoxClient.RequestCode.COLLECT_GROUP, entity.getId());
		}
		return entity;
	}

	/**
	 * 处理 收藏 成功后的数据
	 *  话题
	 * @param context
	 * @param cacheCustomerInfoEntity
	 * @param mAppData
	 */
	public static void requestAttentionToSalonResult(Context context,
			GroupInfoEntity cacheGroupInfoEntity) {
		if(cacheGroupInfoEntity==null)return;
		cacheGroupInfoEntity.setSalonAttention(true);
		cacheGroupInfoEntity.setPersonNumber(Integer.parseInt(cacheGroupInfoEntity.getPersonNumber())+1+"");
		ToastUtil.showShort(context, "关注成功");
		ChatUserHelper.getInstance().changeRelType(cacheGroupInfoEntity);
	}

	/**
	 * 处理 取消收藏 成功后的数据
	 * 	话题
	 * @param context
	 * @param cacheGroupInfoEntity
	 * @param mAppData
	 * @param chatUserHelper
	 */
	public static void requestUnfollowToSalonResult(Context context,
			GroupInfoEntity cacheGroupInfoEntity) {
		if (cacheGroupInfoEntity == null) {
			return;
		}
		cacheGroupInfoEntity.setSalonAttention(false);
		cacheGroupInfoEntity.setPersonNumber(Integer.parseInt(cacheGroupInfoEntity.getPersonNumber())-1+"");
		ToastUtil.showShort(context, "取消关注成功");
		ChatUserHelper.getInstance().changeRelType(cacheGroupInfoEntity);
//
//		// 判断最近联系话题里面是否有该对象
//		if (mAppData.getLatelyGroupIdList().contains(
//				cacheGroupInfoEntity.getId())) {
//			CollectUtils.updateGroupRecent(chatUserHelper,
//					cacheGroupInfoEntity, SmartFoxClient.getLoginUserId());
//			// mAppData.updateLatelyGroup(cacheGroupInfoEntity);
//			mAppData.updateCacheInfomation(cacheGroupInfoEntity);
//		} else {
//			// 联系人不存在该联系人 则从数据库中删除该群
//			mAppData.removeCacheInfomation(cacheGroupInfoEntity);
//			CollectUtils.deleteSalon(chatUserHelper,cacheGroupInfoEntity.getId());
//		}

		// }else {
		// 自己创建的
//		if (cacheGroupInfoEntity.getCreateCustomerID().equals(
//				SmartFoxClient.getSmartFoxClient().getUserId())) {
//			mAppData.getCreatedGroupIdList().remove(
//					cacheGroupInfoEntity.getId());
//		} else {
//			mAppData.getInterestGroupIdList().remove(
//					cacheGroupInfoEntity.getId());
//		}
	}

	/**
	 * 修改话题资料
	 * @param context
	 * @param customerId
	 * @param groupId
	 * @param inceptMessage
	 * @param releaseSystemMessage
	 */
	public static void updateGroupInceptMsg(final FragmentManager manager,
			final Context context, String customerId, String groupId,
			Boolean inceptMessage, Boolean releaseSystemMessage) {
		HttpRestClient.doHttpUpdateGroupInceptMsg(customerId, groupId,
				inceptMessage, releaseSystemMessage,
				new AsyncHttpResponseHandler((FragmentActivity)context) {
					@Override
					public void onSuccess(int statusCode, String content) {
						if ("Y".equalsIgnoreCase(content)) {
						} else if ("N".equalsIgnoreCase(content)) {
							ToastUtil.showShort(context, "failure");
						}
						super.onSuccess(statusCode, content);
					}
				});
	}

	/**
	 * 话题点击没列
	 * 
	 * @param context
	 * @param listener
	 * @param manager
	 * @param entity
	 * @param isReturnInfo  是否返回资料(重新加载资料返回)
	 * @return
	 */
	public static void onItemClick(final Context context,final OnBuyTicketHandlerListener listener,
			final FragmentManager manager,final GroupInfoEntity entity, final boolean isReturnInfo) {
		ChatHelperUtils.chatFromGroup(context, listener, manager, entity, isReturnInfo);

	}

}
