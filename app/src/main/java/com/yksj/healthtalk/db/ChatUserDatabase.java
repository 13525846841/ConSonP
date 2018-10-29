package com.yksj.healthtalk.db;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.universalimageloader.utils.StorageUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.yksj.healthtalk.db.Tables.TableChatMessage;
import com.yksj.healthtalk.entity.BaseInfoEntity;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.entity.GroupInfoEntity;
import com.yksj.healthtalk.entity.MessageEntity;
import com.yksj.healthtalk.entity.NewsEntity;
import com.yksj.healthtalk.entity.VirtualDoctorEntity;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.DataParseUtil;
import com.yksj.healthtalk.utils.HStringUtil;

/**
 * 用户数据库基本操作
 * 
 * @author zhao
 * @version 3.0
 */
public class ChatUserDatabase extends DataBase {

	ChatUserDatabase(Context context, String name) {
		super(context, name);
	}

	/**
	 * @param 好友聊天消息保存
	 * @return 返回自增消息id
	 */
	public synchronized long saveUserChatMessage(ContentValues contentValues) {
		return userDatabase.replace(
				Tables.TableChatMessage.TABLE_CHAT_MESSAGE,
				TableChatMessage.MESSAGE_ID, 
				contentValues);
	}

	/*
	 * public long save(String string, ContentValues values, String string2,
	 * String[] strings) { return userDatabase.update(string, values, "id = ?",
	 * strings); }
	 */

	/**
	 * 
	 * 改变消息的状态
	 * 
	 * @param id
	 *            消息id
	 * @param contentValues
	 */
	public synchronized void updateChatMessageState(String id, ContentValues contentValues) {
		userDatabase.update(TableChatMessage.TABLE_CHAT_MESSAGE, contentValues,
				"message_id=?", new String[] { id });
	}

	/**
	 * 
	 * 删除本地数据库消息
	 * 
	 * @param list
	 *            消息实体
	 * @return 失败成功状态
	 * 
	 */
	public boolean deleteChatMsg(Collection<MessageEntity> list) {
		userDatabase.beginTransaction();
		try {
			for (MessageEntity messageEntity : list) {
				if (messageEntity.getType() == MessageEntity.TYPE_VOICE) {
					StorageUtils.deleteVoiceFile(messageEntity.getContent());
				} else if (messageEntity.getType() == MessageEntity.TYPE_PICTURE) {
					StorageUtils.deleteImageFile(messageEntity.getContent());
				} else if (messageEntity.getType() == MessageEntity.TYPE_LOCATION) {
					StorageUtils.deleteMapFile(messageEntity.getContent());
				}
				deleteChatMessage(messageEntity.getId());
			}
			userDatabase.setTransactionSuccessful();
		} finally {
			userDatabase.endTransaction();
		}
		return true;
	}
	/**
	 * 
	 * 删除本地数据库消息
	 * 
	 * @param list
	 *            消息实体
	 * @return 失败成功状态
	 * 
	 */
	public boolean deleteChatMsgDoctor(Collection<MessageEntity> list) {
		userDatabase.beginTransaction();
		try {
			for (MessageEntity messageEntity : list) {
				if (messageEntity.getType() == MessageEntity.TYPE_VOICE) {
					StorageUtils.deleteVoiceFile(messageEntity.getContent());
				} else if (messageEntity.getType() == MessageEntity.TYPE_PICTURE) {
					StorageUtils.deleteImageFile(messageEntity.getContent());
				} else if (messageEntity.getType() == MessageEntity.TYPE_LOCATION) {
					StorageUtils.deleteMapFile(messageEntity.getContent());
				}
				deleteChatMessageDoctor(messageEntity.getId());
			}
			userDatabase.setTransactionSuccessful();
		} finally {
			userDatabase.endTransaction();
		}
		return true;
	}

	/**
	 * 
	 * 删除好友聊天消息
	 * 
	 * @param messageEntity
	 * @return
	 * 
	 */
	public boolean deleteChatMessage(String id) {
		if(!HStringUtil.isEmpty(id)){
			return userDatabase.delete(Tables.CHAT_MESSAGE_NAME,
					"message_id = ?", new String[] { id }) > 0;
		}
		return false;
	}
	public boolean deleteChatMessageDoctor(String id) {
		if(!HStringUtil.isEmpty(id)){
			return userDatabase.delete(Tables.TableChatMessage.TABLE_CHAT_MESSAGE,
					"message_id = ?", new String[] { id }) > 0;
		}
		return false;
	}

	/**
	 * 
	 * 根据聊天者id删除所有的消息
	 * 
	 * @param id1
	 *            发送者id
	 * @param id2
	 *            接受者id
	 * @return
	 */
	public boolean deleteAllChatMessageByChatId(String id1, String id2) {
//		int result = userDatabase
//				.delete(TableChatMessage.TABLE_CHAT_MESSAGE,
//						"(sender_id=? and receiver_id=?) or (receiver_id=? and sender_id=?)",
//						new String[] { id1, id2, id1, id2 });
		return true;
	}

	/**
	 * 更新消息读取状态为已读状态
	 * @param list
	 * @param i
	 * @param contentValues
	 */
	public void updateChatMessageDuQu(List<MessageEntity> list, int i,
			ContentValues contentValues) {
//		userDatabase.update(Tables.TableChatMessage.TABLE_CHAT_MESSAGE,
//				contentValues, "message_id = ?",
//				new String[] { String.valueOf(list.get(i).getId()) });
	}

	/**
	 * 更新消息下载状态
	 */
	public void updateChatXiaZaiState(
			MessageEntity messageEntity,
			ContentValues contentValues) {
		userDatabase.update(
				Tables.TableChatMessage.TABLE_CHAT_MESSAGE,
				contentValues, "message_id = ?",
				new String[] { String.valueOf(messageEntity.getId()) });
	}

	/**
	 * 查询消息id之后的消息
	 * @param sendId
	 * @param receiveId
	 * @param isGroupChat
	 * @param mesgeId
	 * @param number
	 * @param noSendList
	 * @return
	 */
	public Cursor queryChatMesgeByIdAfter(String sendId,
			String receiveId, boolean isGroupChat, String mesgeId,
			String number) {
		return userDatabase
				.query(
						Tables.TableChatMessage.TABLE_CHAT_MESSAGE,
						new String[] { "message_id", "message_type",
								"receiver_id", "sender_id", "read_tag",
								"message_content", "message_path",
								"voice_length", "sendstate", "downorupstate",
								"time", "isSend",
								Tables.TableChatMessage.MESSAGE_JSONCOTENT,
								Tables.TableChatMessage.MSG_SERVER_ID,
								TableChatMessage.MSG_SERVER_DELET_STATE
								},
						"message_id in ( select message_id from "
								+ Tables.TableChatMessage.TABLE_CHAT_MESSAGE
								+ " where message_id < ?  and ((sender_id=? and receiver_id=?) or (receiver_id=? and sender_id=?)) ORDER BY message_id desc LIMIT ?)",
						new String[] { mesgeId, sendId, receiveId, sendId,
								receiveId, number }, null, null, null, null);
	}

	/**
	 * 查询历史消息
	 * 
	 */
	public  Cursor chatLiShiMessage(String sendId, String receiveId,
			String number) {
		return userDatabase.query(Tables.TableChatMessage.TABLE_CHAT_MESSAGE,
				new String[] { "message_id", "message_type", "receiver_id",
						"sender_id", "read_tag", "message_content",
						"message_path", "voice_length", "sendstate",
						"downorupstate", "time", "isSend" },
				"sender_id=? and receiver_id=?", new String[] { sendId,
						receiveId }, null, null, "message_id asc", number);
	}

	/**
	 * 查询查询新闻收藏列表
	 * 
	 */
	public  Cursor queryNewShouCangList() {
		return userDatabase.query(
				Tables.TableNewsCollection.TABLE_NEWS_COllECTION, new String[] {
						"news_cllection_id", "news_id", "news_type",
						"news_title", "news_time", "type_title" }, null, null,
				null, null, "news_cllection_id asc");
	}

	/**
	 * 根据新闻id和类型查询新闻是否存在
	 * 
	 */
	public  Cursor queryNewsIdIsExis(Cursor cursor, String newsType,
			String newsId, boolean isExit) {
		return userDatabase.query(
				Tables.TableNewsCollection.TABLE_NEWS_COllECTION,
				new String[] { "news_id" }, "news_type=? and news_id=?",
				new String[] { newsType, newsId }, null, null, null);
	}
	
	

	/**
	 * 添加新闻到收藏
	 * 
	 * @param contentValues
	 * @return
	 */
	public  boolean inserNewToCollection(ContentValues contentValues) {

		return userDatabase.insert(
				Tables.TableNewsCollection.TABLE_NEWS_COllECTION,
				"news_cllection_id", contentValues) != -1;
	}

	/**
	 * 根据收藏id查询收藏内容
	 * 
	 * @param collectionId
	 * @return
	 */
	public  Cursor QueryNewsQueryContent(String collectionId) {
		return userDatabase.query(
				Tables.TableNewsCollection.TABLE_NEWS_COllECTION,
				new String[] { "news_content" }, "news_cllection_id = ?",
				new String[] { collectionId }, null, null, null);
	}

	/**
	 * 根据收藏id删除收藏新闻
	 * 
	 * @param collectionId
	 * @return
	 */
	public  int deleteCollectionIdNews(String collectionId) {
		return userDatabase.delete(
				Tables.TableNewsCollection.TABLE_NEWS_COllECTION,
				"news_cllection_id = ?", new String[] { collectionId });

	}

	/**
	 * 服务器删除状态
	 * @param collection
	 * @return
	 */
	public boolean updateMsgeDeletState(Collection<String> collection){
		
		try {
			userDatabase.beginTransaction();
			for (String id : collection) {
				ContentValues contentValues = new ContentValues();
				contentValues.put("is_read","1");
				userDatabase.update(Tables.CHAT_MESSAGE_NAME, contentValues,"message_id=?",new String[] { id });
			}
			userDatabase.setTransactionSuccessful();
		} finally {
			userDatabase.endTransaction();
		}
		return true;
	}
	
	/**
	 * 根据收藏id删除收藏新闻
	 * @param collectionId
	 * @return
	 */
	public  boolean deleteCollectionToId(List<NewsEntity> list) {
		userDatabase.beginTransaction();
		try {
			for (NewsEntity newsEntity : list) {
				deleteCollectionNews(newsEntity.getCollectionId());
			}
			userDatabase.setTransactionSuccessful();
		} finally {
			userDatabase.endTransaction();
		}
		// ChatUserDatabase.deleteCollectionToId(list);
		return true;
	}

	/**
	 * 根据收藏id删除收藏新闻
	 * 
	 * @param collectionId
	 * @return
	 */
	public  boolean deleteCollectionNews(String collectionId) {
		int value = deleteCollectionIdNews(collectionId);
		return value > 0;
	}

	/**
	 * 删除朋友聊天信息
	 * 
	 * @param id
	 * @return
	 */
	public  boolean deleteFriendChatMessage(String id) {
		return userDatabase.delete(Tables.TableChatMessage.TABLE_CHAT_MESSAGE,
				"message_id = ?", new String[] { id }) > 0;

	}

	/**
	 * 删除所有聊天的记录
	 */
	public  void deleteAllChatMessage() {
		userDatabase.execSQL("delete from "
				+ Tables.TableDoctor.TABLE_VIRTUAL_DOCTOR_MESSAGE);
		userDatabase.execSQL("delete from "
				+ Tables.TableChatMessage.TABLE_CHAT_MESSAGE);
		// sequence 置为0
		userDatabase.execSQL("update sqlite_sequence set seq=0 where name='"
				+ Tables.TableDoctor.TABLE_VIRTUAL_DOCTOR_MESSAGE + "'");
		userDatabase.execSQL("update sqlite_sequence set seq=0 where name='"
				+ Tables.TableChatMessage.TABLE_CHAT_MESSAGE + "'");
		// 释放空间
		userDatabase.execSQL("VACUUM");

	}
	
	public synchronized boolean queryHaveMessageServerId(String serverid){
		Cursor cursor = null;
		try{
			 cursor = userDatabase.query(
						TableChatMessage.TABLE_CHAT_MESSAGE,
						new String[]{TableChatMessage.MSG_SERVER_ID}, 
						"msg_server_id = ?",
						new String[]{serverid},
						null,
						null,
						null,
						null);
				if(cursor != null){
					return cursor.moveToNext();
				}
		}finally{
			if(cursor != null){
				cursor.close();
			}
		}
		return false;
	}

	/**
	 * 查询未读消息 read_tag = 0
	 * @param receiverId
	 * @return
	 */
	public synchronized Cursor queryNoReadMesg(String receiverId) {
		return userDatabase.query(
				TableChatMessage.TABLE_CHAT_MESSAGE,
				new String[] { "message_id", "message_type", "receiver_id",
						"sender_id", "read_tag", "message_content",
						"message_path", "voice_length", "sendstate",
						"downorupstate", "time", "isSend" },
				"read_tag = 0 and receiver_id=?",
				new String[] { receiverId },
				null, null, null, null);

	}

	public  Cursor queryVirtualDoctorChatMessageById(String mesgeId,
			String sendId, String receiveId, String sendId2, String receiveId2,
			String number) {
		return userDatabase
				.query(Tables.TableDoctor.TABLE_VIRTUAL_DOCTOR_MESSAGE,
						new String[] { "message_id", "message_type",
								"receiver_id", "sender_id", "read_tag",
								"message_content", "isSend", "messageCode",
								"messageClass", "retFeatur", "functionId",
								"linkDialog", "ltalkName", "serviceLinkId",
								"serviceLinkName", "largeinfoId", "talkname",
								"time", "sendstate" },
						"message_id in (select message_id from "
								+ Tables.TableDoctor.TABLE_VIRTUAL_DOCTOR_MESSAGE
								+ " where message_id < ?  and ((sender_id=? and receiver_id=?) or (receiver_id=? and sender_id=?)) ORDER BY message_id desc LIMIT ?)",
						new String[] { mesgeId, sendId, receiveId, sendId,
								receiveId, number }, null, null, null, null);

	}

	/**
	 * 查询虚拟医生对话历史消息
	 * 
	 * @param sendId
	 * @param receiveId
	 * @param sendId2
	 * @param receiveId2
	 * @return
	 */
	public  Cursor queryVirtualDoctorChatMesg(String sendId,
			String receiveId, String sendId2, String receiveId2) {
		return userDatabase
				.query(Tables.TableDoctor.TABLE_VIRTUAL_DOCTOR_MESSAGE,
						new String[] { "message_id", "message_type",
								"receiver_id", "sender_id", "read_tag",
								"message_content", "isSend", "messageCode",
								"messageClass", "retFeatur", "functionId",
								"linkDialog", "ltalkName", "serviceLinkId",
								"serviceLinkName", "largeinfoId", "talkname",
								"time" },
						"sender_id=? and receiver_id=? or receiver_id=? and sender_id=?",
						new String[] { sendId, receiveId, sendId, receiveId },
						null, null, null, null);

	}

	/**
	 * 删除单条
	 * 
	 * @param id
	 * @return
	 */
	public  boolean delete(long id) {
		return userDatabase.delete(
				Tables.TableDoctor.TABLE_VIRTUAL_DOCTOR_MESSAGE,
				"message_id = ?", new String[] { String.valueOf(id) }) > 0;

	}

	public  boolean deleteVirtualDoctorChatMesg(
			List<VirtualDoctorEntity> list) {
		userDatabase.beginTransaction();
		try {
			for (VirtualDoctorEntity VirtualDoctorEntity : list) {
				delete(VirtualDoctorEntity.getId());
			}
			userDatabase.setTransactionSuccessful();
		} finally {
			userDatabase.endTransaction();
		}
		return true;
	}

	/**
	 * 更新虚拟医生消息读取状态
	 * 
	 * @param list
	 */
	public  void update(ContentValues contentValues, StringBuffer sql,
			String[] arry) {
		userDatabase.update(Tables.TableDoctor.TABLE_VIRTUAL_DOCTOR_MESSAGE,
				contentValues, sql.toString(), arry);

	}

	/**
	 * 保存虚拟医生对话信息
	 */
	public  void saveVirtualDoctorChatMesg(List<VirtualDoctorEntity> list) {
		userDatabase.beginTransaction();
		try {
			for (VirtualDoctorEntity virtualDoctorEntity : list) {
				insertVirtualDoctorChatMessage(virtualDoctorEntity);
			}
			userDatabase.setTransactionSuccessful();
		} finally {
			userDatabase.endTransaction();
		}
	}

	private  void insertVirtualDoctorChatMessage(
			VirtualDoctorEntity doctorEntity) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(Tables.TableChatMessage.SENDER_ID,
				doctorEntity.getSenderId());
		contentValues.put(Tables.TableChatMessage.RECEIVER_ID,
				doctorEntity.getReceiverId());
		contentValues.put(Tables.TableChatMessage.MESSAGE_TYPE,
				doctorEntity.getType());
		contentValues.put(Tables.TableChatMessage.MESSAGE_CONTENT,
				doctorEntity.getContent());
		contentValues.put(Tables.TableChatMessage.ISSEND,
				doctorEntity.isSendFlag() ? 1 : 0);
		contentValues.put(Tables.TableChatMessage.READ_TAG,
				doctorEntity.getReadTag());
		contentValues.put(Tables.TableChatMessage.MESSAGECODE,
				doctorEntity.getMessageCode());
		contentValues.put(Tables.TableChatMessage.MESSAGECLASS,
				doctorEntity.getMessageClass());
		contentValues.put(Tables.TableChatMessage.RETFEATUR,
				doctorEntity.getRetFeatur());
		contentValues.put(Tables.TableChatMessage.FUNCTIONID,
				doctorEntity.getFunctionId());
		contentValues.put(Tables.TableChatMessage.LINKDIALOG,
				doctorEntity.getLinkDialog());
		contentValues.put(Tables.TableChatMessage.LTALKNAME,
				doctorEntity.getTalkname());
		contentValues.put(Tables.TableChatMessage.SERVICELINKID,
				doctorEntity.getServiceLinkId());
		contentValues.put(Tables.TableChatMessage.SERVICELINKNAME,
				doctorEntity.getServiceLinkName());
		contentValues.put(Tables.TableChatMessage.LARGEINFOID,
				doctorEntity.getLargeinfoId());
		contentValues.put(Tables.TableChatMessage.TALKNAME,
				doctorEntity.getTalkname());
		contentValues.put(Tables.ChatTable.TIME, doctorEntity.getDate());
		contentValues.put(Tables.TableChatMessage.SENDSTATE,
				doctorEntity.getSendState());
		long id = insert(contentValues);
		doctorEntity.setId(id);

	}

	/**
	 * 保存虚拟医生对话信息
	 */
	public  long insert(ContentValues contentValues) {
		long id = userDatabase.insert(
				Tables.TableDoctor.TABLE_VIRTUAL_DOCTOR_MESSAGE, "message_id",
				contentValues);
		return id;
	}

	public  long updateData(String string, ContentValues values,
			String string2, String[] strings) {
		return userDatabase.update(Tables.TableFriend.TABLE_FRIEND, values,
				"id = ?", strings);

	}

	/**
	 * 保存聊天室信息
	 * 
	 * @param string
	 * @param values
	 * @param string2
	 * @param strings
	 * @return
	 */
	public  long saveGroupMesgInfos(String string, ContentValues values,
			String string2, String[] strings) {
		return userDatabase.update(string, values, "id = ?", strings);
	}

	public  Cursor reQuery(String sql, String[] strings) {
		return userDatabase.rawQuery(sql, strings);
	}

	/**
	 * 删除新闻收藏数据
	 * 
	 * @param tablNameNewsConnection
	 * @param string
	 * @param strings
	 * @return
	 */
	public  int deleteIdNews(String tablNameNewsConnection,
			String string, String[] strings) {
		return userDatabase.delete(tablNameNewsConnection,
				Tables.TableNewsCollection.CONNECTION_USERID + " = ? and "
						+ Tables.TableNewsCollection.NEWS_ID + " = ?", strings);
	}

	/**
	 * 收藏新闻
	 * 
	 * @param title
	 *            标题
	 * @param time
	 *            时间
	 * @param content
	 *            新闻内容
	 * @param connectionUserId
	 *            用户id
	 * @param typeCode
	 *            内容类型id
	 * @param contentId
	 *            内容id
	 * @return
	 */
	public synchronized long insertData(String tablNameNewsConnection, String object,
			ContentValues contentValues) {
		return userDatabase.insert(tablNameNewsConnection, null, contentValues);
	}

	public synchronized Cursor queryData(String string, String[] strings,
			String string2, String[] strings2, Object object, Object object2,
			String string3) {
		return userDatabase.query(string, strings, string2, strings2, null,
				null, string3);

	}

	/**
	 * 删除沙龙
	 */
	public synchronized void deleteSalon(String tablNameNewsConnection,
			String string, String[] strings) {
		userDatabase.delete(tablNameNewsConnection,string, strings);
	}

	
	/**
	 * 插入初始化时  加载的群组资料
	 * @param mJsonArray
	 */
	public synchronized void insertInitGroupList(JSONArray mJsonArray) {
		userDatabase.beginTransaction();
		try {
			int size =mJsonArray.length();
			GroupInfoEntity mSalonEntity =null;
			ContentValues values=new ContentValues();
			for (int i = 0; i <size; i++) {
				mSalonEntity =new GroupInfoEntity();
				JSONObject object = mJsonArray.optJSONObject(i);
				// 头像
				mSalonEntity.setBigHeadIcon(object.optString("biggb"));
				mSalonEntity.setCharge(object.optString("chargingFlag").equals("1") ? true : false);// 是否收费
				mSalonEntity.setNormalHeadIcon(object.optString("cilentbg"));
				mSalonEntity.setCreateCustomerID(object.optString("createCustomerID"));
				mSalonEntity.setCreateTime(object.optString("createTime"));
				mSalonEntity.setFlag(object.optString("flag"));
				mSalonEntity.setFlagPlacing(object.optString("flagPlacing"));
				// 是否是医师话题
				mSalonEntity.setSalon(object.optString("groupClass").equals("1") ? true : false);
				mSalonEntity.setId(object.optString("groupId"));// 群组ID
				mSalonEntity.setGroupLevel(object.optString("groupLevel"));// 话题等级(主治医师、副主任医师、主任医师
				mSalonEntity.setSalonAttention( "1".equals(object.optString("groupflag")));// 标记（1为收藏，0为未收藏）
				mSalonEntity.setLimitNumber(object.optString("limitNnum"));// 上限人数
				mSalonEntity.setMerchantId(object.optString("merchantId"));
				mSalonEntity.setOnLineNumber(object.optString("onlineNum"));// 在线人数
				mSalonEntity.setPersonNumber(object.optString("personNum"));// 此聊天室内当前有多少人
				mSalonEntity.setRecordDesc(object.optString("recordDesc"));// 群组主题
				mSalonEntity.setName(object.optString("recordName"));// 名称
				mSalonEntity.setIsReleaseSystemMessage(object.optString("releaseSystemMessage").equals("1"));// 是否将消息发布到消息厅(1-发布，0-不发布)
				mSalonEntity.setShowPersonNumber(object.optBoolean("showPersonnum"));// 是否显示此聊天室内当前有多少人
				mSalonEntity.setYesornoShow(object.optBoolean("yesornoShow"));// 是否显示在线人数
				mSalonEntity.setOrderInfo(object.optString("orderInfo"));// 是否需要再次支付
				if (object.optInt("hasExtensionInfomaiton") != 0) {
					mSalonEntity.setGroupHeadPortraitID(object.optString("groupHeadPortraitID"));// 群组头像ID
					mSalonEntity.setGroupHeadPortraitName(object.optString("groupHeadPortraitName"));// 群组头像名称
					mSalonEntity.setInceptMessage(object.optString("inceptMessage").equals("Y"));
					mSalonEntity.setInfoId(object.optString("infoLayid"));// 小类信息层面id
					mSalonEntity.setInfoLayName(object.optString("infoLayName"));// 信息层面名称
					mSalonEntity.setUpperId(object.optString("larglayid"));// 大类id
					mSalonEntity.setCusMessag("");
					// mSalonEntity.setNote(object.optString("note"));//备注
					mSalonEntity.setPublicCustInfo(object.optString("publicCustInfo").equals("Y"));// 是否公开创建者信息(Y/N,默认为Y)
					mSalonEntity.setName(object.optString("recordName"));// 群组名称
					mSalonEntity.setNote(object.optString("note"));// 签名
					// mSalonEntity.setSourceId(object.getString("sourceId"));//话题来源id
				}
				if (mSalonEntity.isSalonAttention()
						&& !mSalonEntity.getCreateCustomerID().equals(SmartFoxClient.getLoginUserId())) {
					// 我关注的
					values.put("rel_type", "9");
				} else if (mSalonEntity.isSalonAttention()) {
					// 我创建的
					values.put("rel_type", "10");
				}else{//陌生的
					values.put("rel_type", "0");
				}
				values.put("c_id", mSalonEntity.getId());
				values.put("is_group", "1");//话题
				values.put("is_doctor",mSalonEntity.isSalon()?"1":"0");//话题
				long len = userDatabase.insert(Tables.PERSONINFO_NAME,null, values);
				// System.out.println("数据库操作"+len);
				values.clear();
			}
			userDatabase.setTransactionSuccessful();
		}finally{
			userDatabase.endTransaction();
		}
	}

	public void insertInitPersonInfo(JSONArray mJsonArray) {
		userDatabase.beginTransaction();
		try {
			int size = mJsonArray.length();
			CustomerInfoEntity mCustomerInfo;
			ContentValues values = new ContentValues();
			for (int i = 0; i < size; i++) {
				mCustomerInfo = DataParseUtil.jsonToCustmerInfo(mJsonArray.getJSONObject(i));
				if(mCustomerInfo == null) continue;
				values.put("c_id", mCustomerInfo.getId());
				values.put("is_group", "0");
				values.put("is_doctor", mCustomerInfo.isDoctor()?"1":"0");
				values.put("rel_type", mCustomerInfo.getIsAttentionFriend());
				long len =userDatabase.insert(Tables.PERSONINFO_NAME,null, values);
				// System.out.println("数据库操作"+len);
				values.clear();
			}
			userDatabase.setTransactionSuccessful();
		}catch(Exception e){
		}finally{
			userDatabase.endTransaction();
		}
	}

	public synchronized void insertLeaveMessage(JSONArray jsonArray) {
		userDatabase.beginTransaction();
		NumberFormat format = NumberFormat.getInstance();
		try {
			ContentValues values=new ContentValues();
			MessageEntity messageEntity ;
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = jsonArray.optJSONObject(i);
				messageEntity = new MessageEntity();
				int type = json.optInt("type");
				String id = json.optString("serverId");
					String str = json.optString("timeStamp");
					try {
						long time = format.parse(str).longValue();
						messageEntity.setDate(time);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					messageEntity.setServerId(id);
					String targetId = json.optString("targetCustomerId");
					boolean isGroupMesge = (json.getInt("isGroupMessage") == 1);
					// 图片
					if (type == MessageEntity.TYPE_PICTURE) {
						String pictureName = json.optString("dataHolder");
						messageEntity.setSendFlag(false);
						messageEntity.setType(MessageEntity.TYPE_PICTURE);
						messageEntity.setSenderId(json.optString("customerId"));
						messageEntity.setDownOrUpState(MessageEntity.STATE_NORMAL);
						messageEntity.setContent(pictureName);
						// 语音
					} else if (type == MessageEntity.TYPE_VOICE) {
						messageEntity.setContent(json.optString("content"));
						messageEntity.setSendFlag(false);
						messageEntity.setType(MessageEntity.TYPE_VOICE);
						messageEntity.setSenderId(json.optString("customerId"));
						messageEntity.setDownOrUpState(MessageEntity.STATE_NORMAL);
						DecimalFormat mFormat = new DecimalFormat("0.00");
						messageEntity.setVoiceLength(mFormat.format(json.optDouble("duration", 0.0d)));
						// 文字
					} else if (type == MessageEntity.TYPE_TEXT || type == 0
							|| type == 3 || type == 12) {
						messageEntity.setContent(json.optString("content"));
						messageEntity.setSendFlag(false);
						messageEntity.setContentJsonArray(json.optJSONArray("keyWords"));
						messageEntity.setType(MessageEntity.TYPE_TEXT);
						messageEntity.setSenderId(json.optString("customerId"));
						messageEntity.setDownOrUpState(MessageEntity.STATE_OK);
						// 坐标
					} else if (type == MessageEntity.TYPE_LOCATION) {
						String dataHolder = json.optString("dataHolder");
						messageEntity.setContent(dataHolder);
						messageEntity.setSendFlag(false);
						messageEntity.setType(MessageEntity.TYPE_LOCATION);
						messageEntity.setSenderId(json.optString("customerId"));
						messageEntity
								.setDownOrUpState(MessageEntity.STATE_NORMAL);
						// 关注 邀请
					} else if (type == MessageEntity.TYPE_INVITATION) {
						messageEntity.setContentJsonArray(json.optJSONArray("keyWords"));
						messageEntity.setContent(json.optString("content"));
						messageEntity.setSendFlag(false);
						messageEntity.setType(MessageEntity.TYPE_TEXT);
						messageEntity.setSenderId(SmartFoxClient.helperId);
					} else {
						continue;
					}
					messageEntity.setReceiverId(targetId);
					// 如果是群离线消息无需插入数据库
					if (isGroupMesge) {
						// 群聊
						messageEntity.setId(id);
						messageEntity.setGroupId(targetId);
						values.put("is_group", "1");
						values.put("sender_id", targetId);
						GroupInfoEntity entity=new GroupInfoEntity();
						entity.setId(targetId);
						joinLatelyGroup(entity, true);
					} else {
						// 单聊
						values.put("sender_id", messageEntity.getSenderId());
						values.put("is_group", "0");
						CustomerInfoEntity entity=new CustomerInfoEntity();
						entity.setId(targetId);
						joinLatelyPerson(entity, true);
					}
					values.put("message_id", messageEntity.getServerId());
					values.put("content", messageEntity.getContent());
					values.put("msg_time", messageEntity.getDate());
					values.put("msg_type", messageEntity.getType());//消息类型
					values.put("is_read", "0");//未读
					long len =userDatabase.insert(Tables.CHAT_MESSAGE_NAME, null, values);
//					System.out.println("数据库操作"+len);
					values.clear();
			}
			userDatabase.setTransactionSuccessful();
		} catch (Exception e) {
		}finally{
			userDatabase.endTransaction();
		}
	}
	
	/**
	 * 改变消息状态
	 * @param msg
	 * @param isRead
	 * @param isGroup
	 */
	public synchronized void changeMessageStatus(MessageEntity msg,boolean isRead,boolean isGroup){
		try {
			//先查出id 是否存在 如果没有存在直接插入  如果存在 改变
			String read = isRead? "1":"0";
			Cursor c = userDatabase.rawQuery("select * from "+Tables.CHAT_MESSAGE_NAME+" where message_id = ?",new String[]{ msg.getId()});
			if(c.moveToNext()){
				userDatabase.execSQL("update "+Tables.CHAT_MESSAGE_NAME+" set is_read = "+read+" where message_id = "+msg.getId());
				c.close();
			}else{
				c.close();
				ContentValues values=new ContentValues();
				if (isGroup) {
					// 群聊
					values.put("is_group", "1");
					values.put("sender_id", msg.getReceiverId());
				} else {
					// 单聊
					values.put("sender_id", msg.getSenderId());
					values.put("is_group", "0");
				}
				 	values.put("message_id", msg.getServerId());
					values.put("content", msg.getContent());
					values.put("msg_time", msg.getDate());
					values.put("msg_type", msg.getType());//消息类型
					values.put("is_read", read);//未读
				long len =userDatabase.insert(Tables.CHAT_MESSAGE_NAME, null, values);
//				System.out.println("数据库操作"+len);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 人 
	 * @param mCustomerInfo
	 * @param isJoin  true 表示聊天最近联系人列表  false 表示添加好友,
	 */
	public synchronized void joinLatelyPerson(CustomerInfoEntity mCustomerInfo, boolean isJoin) {
		if(mCustomerInfo==null || mCustomerInfo.getId().equals(SmartFoxClient.getLoginUserId()))return;
		
		if(queryIdIsContinue(mCustomerInfo.getId()))return;
		ContentValues values=new ContentValues();
		values.put("c_id", mCustomerInfo.getId());
		values.put("is_group", "0");
		values.put("is_lately", isJoin?"1":"0");
		values.put("is_doctor", mCustomerInfo.isDoctor()?"1":"0");
		values.put("rel_type", mCustomerInfo.getIsAttentionFriend());
		long len =userDatabase.insert(Tables.PERSONINFO_NAME,null, values);
//		System.out.println("数据库操作"+len);
	}

	/**
	 * 话题加入
	 * @param mSalonEntity
	 * @param isJoin  true 表示聊天最近联系人列表  false 表示添加好友,
	 */
	public synchronized void joinLatelyGroup(GroupInfoEntity mSalonEntity, boolean isJoin) {
		if(queryIdIsContinue(mSalonEntity.getId()))return;
		ContentValues values=new ContentValues();
		if (mSalonEntity.isSalonAttention()&& !mSalonEntity.getCreateCustomerID().equals(SmartFoxClient.getLoginUserId())) {
			// 我关注的
			values.put("rel_type", "9");
		} else if (mSalonEntity.isSalonAttention()) {
			// 我创建的
			values.put("rel_type", "10");
		}else{//陌生的
			values.put("rel_type", "0");
		}
		values.put("is_lately", isJoin?"1":"0");
		values.put("c_id", mSalonEntity.getId());
		values.put("is_group", "1");//话题
		long len = userDatabase.insert(Tables.PERSONINFO_NAME,null, values);
		// System.out.println("数据库操作"+len);
	}

	/**
	 * 通过id 查询当前用户是否在库中  如果在直接改变为时最近联系人
	 * 如果不再返回false;
	 * @param id
	 */
	private  synchronized boolean queryIdIsContinue(String id){
		Cursor c = userDatabase.rawQuery("select * from "+Tables.PERSONINFO_NAME+" where c_id = ? ", new String[]{id});
		if(c.moveToNext()){
			c.close();
			String sql = "update "+Tables.PERSONINFO_NAME+" set is_lately = '1' where c_id ="+id;
			userDatabase.execSQL(sql);
			return true;
		}else{
			c.close();
			return false;
		}
	}
	
	
	/**
	 * 查询所有最近联系人
	 * @return
	 */
	public List<BaseInfoEntity> queryAllLater() {
		String sql="select ac.c_Id," +
				"ac.rel_type," +
				"ac.is_lately," +
				"ac.is_group," +
				"ac.is_doctor," +
				"ac.mid," +
				"acm.content," +
				"acm.msg_type," +
				" acm.msg_time," +
				" acm.msg_path," +
				"ac.is_read_count "+
				"from "+Tables.CHAT_MESSAGE_NAME+" acm, "+
				"(select aci.c_Id, "+
				"aci.rel_type, "+
				" aci.is_lately, "+
				"aci.is_group, "+
				"aci.is_doctor, "+
				"mmsg.mid, "+
				"mmsg.sender_id, "+
				"(select count(*) "+
				"from "+Tables.CHAT_MESSAGE_NAME+" "+
				" where is_read = 0 "+
				" and sender_id = aci.c_id) as is_read_count "+
				"from "+Tables.PERSONINFO_NAME+" aci, "+
				"(select acm.sender_id, max(acm.message_id) as mid"+
				" from "+Tables.CHAT_MESSAGE_NAME+" acm"+
				"group by acm.sender_id) mmsg"+
				" where aci.c_id = mmsg.sender_id)  ac"+
				"where acm.sender_Id = ac.sender_id"+///ac.rel_type ==   指定查询
				"and acm.message_Id = ac.midorder by acm.msg_time desc";
		List<BaseInfoEntity> entities =new ArrayList<BaseInfoEntity>();
		Cursor c = userDatabase.rawQuery(sql, null);
		GroupInfoEntity goup = null;
		CustomerInfoEntity cus = null;
		while (c.moveToNext()) {
			String string = c.getString(c.getColumnIndex("ac.is_group"));
			if("1".equals(string)){//话题
				goup=new GroupInfoEntity();
				goup.setId(c.getString(c.getColumnIndex("ac.c_Id")));
				goup.setLastMsg(c.getString(c.getColumnIndex("acm.content")));
				goup.setLastMsgTime(c.getString(c.getColumnIndex("acm.msg_time")));
				goup.setLastMsgType(c.getString(c.getColumnIndex("ac.rel_type")));
				entities.add(goup);
			}else if("0".equals(string)){//人
				cus=new CustomerInfoEntity();
				cus.setId(c.getString(c.getColumnIndex("ac.c_Id")));
				cus.setLastMessage(c.getString(c.getColumnIndex("acm.content")));
				cus.setLastMessageTime(c.getString(c.getColumnIndex("acm.msg_time")));
				cus.setLastMessageType(c.getString(c.getColumnIndex("ac.rel_type")));
				entities.add(cus);
			}
		}
		c.close();
		return entities;
	}

	/**
	 * 保存聊天消息 默认是未读的
	 * @param messageEntity
	 * @param isGroup true 是话题  不是话题
	 */
	public synchronized void saveUserChatMessage(MessageEntity messageEntity, boolean isGroup) {
		//更新 资料表
			if(isGroup){
				GroupInfoEntity entity =new GroupInfoEntity();
				entity.setId(messageEntity.getGroupId());
				joinLatelyGroup(entity, true);
			}else{
				CustomerInfoEntity cus =new CustomerInfoEntity();
				cus.setId(messageEntity.getSenderId());
				joinLatelyPerson(cus, true);
			}
		//插入消息  都是未读的
		changeMessageStatus(messageEntity, false, isGroup);
	}

	/**
	 * 保存自己发送的消息 默认已读
	 * @param messageEntity
	 * @param isGroup
	 */
	public void saveUserChatMessageFromSelf(MessageEntity messageEntity,
			boolean isGroup) {
		//更新 资料表
		if(isGroup){
			GroupInfoEntity entity =new GroupInfoEntity();
			entity.setId(messageEntity.getReceiverId());
			joinLatelyGroup(entity, true);
		}else{
			CustomerInfoEntity cus =new CustomerInfoEntity();
			cus.setId(messageEntity.getReceiverId());
			joinLatelyPerson(cus, true);
		}
	//插入消息
		changeMessageStatus(messageEntity, true, isGroup);
	}

	/**
	 * 更新实体
	 * @param entity
	 */
	public void updateRelType(BaseInfoEntity entity) {
		if(entity ==null) return;
		if(entity instanceof CustomerInfoEntity){
			CustomerInfoEntity cus= (CustomerInfoEntity) entity;
			userDatabase.execSQL("update "+Tables.PERSONINFO_NAME+" set rel_type = "+cus.getIsAttentionFriend()+" where c_id = "+cus.getId());
		}else if(entity instanceof GroupInfoEntity){
			GroupInfoEntity groupInfoEntity = (GroupInfoEntity) entity;
			String rel_type;
			if (groupInfoEntity.isSalonAttention()&& !groupInfoEntity.getCreateCustomerID().equals(SmartFoxClient.getLoginUserId())) {
				// 我关注的
				rel_type = "9";
			} else if (groupInfoEntity.isSalonAttention()) {
				// 我创建的
				rel_type = "10";
			}else{//陌生的
				rel_type = "0";
			}
			userDatabase.execSQL("update "+Tables.PERSONINFO_NAME+" set rel_type = "+rel_type+" where c_id = "+groupInfoEntity.getId());
		}
	}

	/**
	 * 
	 */
//	public void deleteTab() {
//		userDatabase.execSQL("delete from "+Tables.PERSONINFO_NAME+";");
//		userDatabase.execSQL("delete from "+Tables.CHAT_MESSAGE_NAME+";");
//	}
	
}




















