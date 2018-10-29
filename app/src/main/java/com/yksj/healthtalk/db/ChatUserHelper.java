package com.yksj.healthtalk.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;

import com.yksj.healthtalk.db.Tables.ChatTable;
import com.yksj.healthtalk.db.Tables.TableChatMessage;
import com.yksj.healthtalk.db.Tables.TableFriend;
import com.yksj.healthtalk.entity.BaseInfoEntity;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.entity.GroupInfoEntity;
import com.yksj.healthtalk.entity.MessageEntity;
import com.yksj.healthtalk.entity.NewsEntity;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.AppData;
import com.yksj.consultation.son.app.HTalkApplication;

/**
 * 
 * 用户自身对应的数据库操作工具类
 * @author zhao yuan
 * @version 3.0
 */
public class ChatUserHelper {
	
	private static ChatUserHelper mChatUserHelper;
	private ChatUserDatabase mDatabase;
	
	private ChatUserHelper(Context context,String name){
		mDatabase = new ChatUserDatabase(context,name);
	}
	
	public static synchronized ChatUserHelper getInstance(){
		if(mChatUserHelper == null){
			mChatUserHelper = new ChatUserHelper(
					HTalkApplication.getApplication(),
					"1111");
		}
		return mChatUserHelper;
	}
	
	/**
	 * 关闭数据库
	 */
	public static synchronized void close(){
		if(mChatUserHelper != null){
			mChatUserHelper.mDatabase.closeUserDb();
		}
		mChatUserHelper = null;
	}
	
	/**
	 * 查询未读消息
	 * @param receiverId 接受者id
	 * @return
	 */
	public List<MessageEntity> queryNoReadStateMessages(String receiverId){
		
		/*ContentValues contentValues = new ContentValues();
		contentValues.put(Tables.TableChatMessage.READ_TAG,0);
		contentValues.put(Tables.TableChatMessage.RECEIVER_ID, receiverId);*/
		
		List<MessageEntity> list = new ArrayList<MessageEntity>();
		Cursor cursor = null;
		try{
			cursor = mDatabase.queryNoReadMesg(receiverId);
			while(cursor.moveToNext()){
				long messageId = cursor.getLong(0);
				int type = cursor.getInt(1);
				String senderId = cursor.getString(3);
				int read = cursor.getInt(4);
				String content = cursor.getString(5);
//				String contentPath = cursor.getString(6);
				String voiceLength = cursor.getString(7);
				int messageSendState = cursor.getInt(8);
				int messgeDownOrUpState = cursor.getInt(9);
				long time = cursor.getLong(10);
				int isSend = cursor.getInt(11);
				MessageEntity messageEntity = new MessageEntity();
				messageEntity.setId(String.valueOf(messageId));
				messageEntity.setType(type);
				messageEntity.setReceiverId(receiverId);
				messageEntity.setSenderId(senderId);
				messageEntity.setReadTag(read);
				messageEntity.setContent(content);
				messageEntity.setDownOrUpState(messgeDownOrUpState);
				messageEntity.setSendState(messageSendState);
				messageEntity.setVoiceLength(voiceLength);
				messageEntity.setDate(time);
				messageEntity.setSendFlag(isSend==1?true:false);
				list.add(messageEntity);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			if(cursor != null)cursor.close();
		}
		return list;
	} 
	
	/**
	 * 好友聊天消息保存
	 * @param messageEntity
	 * @return 消息id
	 */
	public void insertChatMessage(MessageEntity messageEntity,boolean isGroup) {
		mDatabase.saveUserChatMessage(messageEntity,isGroup);
	}
	
	/**
	 * 保存自己发送的消息 并且设置已读
	 * @param messageEntity
	 * @return 消息id
	 */
	public void insertChatMessageFromSelf(MessageEntity messageEntity,boolean isGroup) {
		mDatabase.saveUserChatMessageFromSelf(messageEntity,isGroup);
	}
	
	/**
	 * 改变消息的发送状态
	 * @param messageId
	 * @param downOrUpState
	 * @param sendState
	 */
	public void updateChatMessageState(MessageEntity entity){
		ContentValues contentValues = new ContentValues();
		contentValues.put(TableChatMessage.DOWNORUPSTATE, entity.getDownOrUpState());
		contentValues.put(TableChatMessage.SENDSTATE, entity.getSendState());
		mDatabase.updateChatMessageState(entity.getId(),contentValues);
	}
	
	/**
	 * 更新消息发送状态
	 * @param messageId
	 * @param sendState
	 */
	public void updateChatMessageSendState(MessageEntity entity){
		ContentValues contentValues = new ContentValues();
		contentValues.put(TableChatMessage.SENDSTATE, entity.getSendState());
		if(entity.getContent() != null){
			contentValues.put(Tables.TableChatMessage.MESSAGE_CONTENT, entity.getContent());
		}
		mDatabase.updateChatMessageState(entity.getId(),contentValues);
	}
	/**
	 * 消息删除状态
	 * @param collection
	 */
	public void updateChatMesageDeleteState(Collection<String> collection){
		mDatabase.updateMsgeDeletState(collection);
	}
	
	/**
	 * 更新消息发送状态
	 * @param messageId
	 * @param sendState
	 */
	public void updateChatMessageSendStateAndContent(MessageEntity entity){
		ContentValues contentValues = new ContentValues();
		contentValues.put(Tables.TableChatMessage.SENDSTATE, entity.getSendState());
		contentValues.put(Tables.TableChatMessage.MESSAGE_CONTENT, entity.getContent());
		mDatabase.updateChatMessageState(entity.getId(),contentValues);
	}
	
	/**
	 * 更新语音,图片消息下载状态
	 * @param messageId
	 * @param downOrUpState
	 * @param sendState
	 * @param content
	 */
/*	public void updateChatVoiceOrImageState(String messageId,int downOrUpState,int sendState,String content){
		ContentValues contentValues = new ContentValues();
		contentValues.put(Tables.TableChatMessage.DOWNORUPSTATE,downOrUpState);
		contentValues.put(Tables.TableChatMessage.SENDSTATE, sendState);
		contentValues.put(Tables.TableChatMessage.MESSAGE_CONTENT,content);
	}*/
	
	/**
	 * 更新聊天消息下载状态
	 */
	public void updateDownChatMessageState(String messageId,int downOrUpState,String content){
		ContentValues contentValues = new ContentValues();
		contentValues.put(Tables.TableChatMessage.DOWNORUPSTATE,downOrUpState);
		contentValues.put(Tables.TableChatMessage.MESSAGE_CONTENT,content);
		mDatabase.updateChatMessageState(messageId,contentValues);
	}
	
	/**
	 * 
	 * 更新消息上传状态(语音,图片)
	 * @param messageId 消息id
	 * @param downOrUpState 上传状态
	 * @param sendState 发送状态
	 */
	public void updateUploadChatMessageState(String messageId,int downOrUpState){
		ContentValues contentValues = new ContentValues();
		contentValues.put(Tables.TableChatMessage.DOWNORUPSTATE,downOrUpState);
		mDatabase.updateChatMessageState(messageId,contentValues);
	}
	
	/**
	 * 地图下载发送状态改变
	 * @param messageId
	 * @param downOrUpState
	 * @param sendState
	 * @param mapPath 地图保存地址
	 */
	public void upateChatMapMessageState(String messageId,int downOrUpState,int sendState,String mapPath){
		ContentValues contentValues = new ContentValues();
		contentValues.put(Tables.TableChatMessage.DOWNORUPSTATE,downOrUpState);
		contentValues.put(Tables.TableChatMessage.SENDSTATE, sendState);
		contentValues.put(Tables.TableChatMessage.MESSAGE_CONTENT,mapPath);
		mDatabase.updateChatMessageState(messageId,contentValues);
	}
	/**
	 * 更新消息
	 */
	public void updateChatMessageContentPath(MessageEntity messageEntity){
		ContentValues contentValues = new ContentValues();
		mDatabase.updateChatMessageState(messageEntity.getId(),contentValues);
	}
	
	/**
	 * 更改图片快捷图标
	 * @param messageEntity
	 */
/*	public void updateChatMessageContent(MessageEntity messageEntity){
		ContentValues contentValues = new ContentValues();
		mDatabase.updateChatMessageState(messageEntity,contentValues);
	} */
	
	/**
	 * 删除消息
	 * @param messageEntity
	 * @return
	 */
	public boolean deleteChatMessage(Collection<MessageEntity> list) {
		return mDatabase.deleteChatMsg(list);
	}
	/**
	 * 删除消息
	 * @param messageEntity
	 * @return
	 */
	public boolean deleteChatMessageDoctor(Collection<MessageEntity> list) {
		return mDatabase.deleteChatMsgDoctor(list);
	}
	
	public boolean deleteAllMessageByChatId(String id1,String id2){
		return mDatabase.deleteAllChatMessageByChatId(id1, id2);
	}
	
	/**
	 * 更新消息读取状态为已读状态
	 * 
	 * @param list
	 */
	private void updateChatMessageReadState(List<MessageEntity> list) {
		String[] arry = new String[list.size()];
		ContentValues contentValues = new ContentValues();
		contentValues.put(Tables.TableChatMessage.READ_TAG, 1);
		for (int i = 0; i < list.size(); i++) {
			arry[i] = String.valueOf(list.get(i).getId());
			mDatabase.updateChatMessageDuQu(list, i, contentValues);
		}
	}
	
	/**
	 * 更新消息下载状态
	 * 
	 * @param messageEntity
	 */
	public void updateVoiceChatMessageState(MessageEntity messageEntity) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(Tables.TableChatMessage.MESSAGE_STATUS, 1);
		mDatabase.updateChatXiaZaiState(messageEntity,contentValues);
	}
	
	/**
	 * 
	 * 查询消息id之后的消息
	 * @param sendId
	 * @param receiveId
	 * @param isGroupChat
	 * @param mesgeId 消息id
	 * @param number
	 * @return
	 */
	public List<MessageEntity> queryChatMessageByAfterId(String sendId,String receiveId,boolean isGroupChat,String mesgeId,String number){
		
		List<MessageEntity> list = new ArrayList<MessageEntity>();
		List<MessageEntity> deleteMesgList = new ArrayList<MessageEntity>();//需要删除服务器上的消息id
		
		Cursor cursor = null;
		try{
			cursor = mDatabase.queryChatMesgeByIdAfter( sendId, receiveId, isGroupChat, mesgeId, number);
			ConcurrentHashMap<String, MessageEntity> map = HTalkApplication.getAppData().getSendMesgeCache();
			while(cursor.moveToNext()){
				String messageId = cursor.getString(0);
				int type = cursor.getInt(1);
				String receiverId = cursor.getString(2);
				String senderId = cursor.getString(3);
				int read = cursor.getInt(4);
				String content = cursor.getString(5);
				String contentPath = cursor.getString(6);
				String voiceLength = cursor.getString(7);
				int messageSendState = cursor.getInt(8);
				int messgeDownOrUpState = cursor.getInt(9);
				long time = cursor.getLong(10);
				int isSend = cursor.getInt(11);
				String jsonStr = cursor.getString(12);
				String serverId = cursor.getString(13);
				String serverDelState = cursor.getString(14);//服务器删除状态
				
				MessageEntity messageEntity = null;
				//正在发送并且消息在缓存中
				if(messageSendState == MessageEntity.STATE_PROCESING && map.containsKey(messageId)){
					messageEntity = map.get(messageId);
					list.add(messageEntity);
					continue;
				}else{
					messageEntity = new MessageEntity();
					messageEntity.setServerId(serverId);
					messageEntity.setId(messageId);
					messageEntity.setType(type);
					messageEntity.setReceiverId(receiverId);
					messageEntity.setSenderId(senderId);
					messageEntity.setReadTag(read);
					messageEntity.setContent(content);
					messageEntity.setDownOrUpState(messgeDownOrUpState);
					messageEntity.setSendState(messageSendState);
					messageEntity.setVoiceLength(voiceLength);
					messageEntity.setDate(time);
					messageEntity.setAddress(contentPath);
					messageEntity.setSendFlag(isSend==1?true:false);
					if(jsonStr != null){
						try {
							JSONArray array = new JSONArray(jsonStr);
							messageEntity.setContentJsonArray(array);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					if(messageSendState == MessageEntity.STATE_PROCESING){
						map.put(messageId,messageEntity);
					}
					list.add(messageEntity);
					//需要删除离线消息
					if("-1".equals(serverDelState)  &&  serverId != null){
						deleteMesgList.add(messageEntity);
					}
				}
			}
			SmartFoxClient.deleteHistroyMessage(deleteMesgList);
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			if(cursor != null)cursor.close();
		}
		return list;
	}
	
	public boolean isHaveMeessageByServerId(String serverid){
		return mDatabase.queryHaveMessageServerId(serverid);
	}
	
	/**
	 * 查询历史消息
	 * @param userId
	 * @param tagertId
	 * @param isGroup
	 * @param number
	 * @return
	 */
	public List<MessageEntity> queryChatMessage(String sendId,String receiveId,
			boolean isGroupChat, String number) {
		List<MessageEntity> list = new ArrayList<MessageEntity>();
		
		Cursor cursor=mDatabase.chatLiShiMessage(sendId,receiveId,number);
		while(cursor.moveToNext()){
			long messageId = cursor.getLong(0);
			int type = cursor.getInt(1);
			String receiverId = cursor.getString(2);
			String senderId = cursor.getString(3);
			int read = cursor.getInt(4);
			String content = cursor.getString(5);
//			String contentPath = cursor.getString(6);
			String voiceLength = cursor.getString(7);
			int messageSendState = cursor.getInt(8);
			int messgeDownOrUpState = cursor.getInt(9);
			long time = cursor.getLong(10);
			int isSend = cursor.getInt(11);
			
			MessageEntity messageEntity = new MessageEntity();
			messageEntity.setId(String.valueOf(messageId));
			messageEntity.setType(type);
			messageEntity.setReceiverId(receiverId);
			messageEntity.setSenderId(senderId);
			messageEntity.setReadTag(read);
			messageEntity.setContent(content);
			messageEntity.setDownOrUpState(messgeDownOrUpState);
			messageEntity.setSendState(messageSendState);
			messageEntity.setVoiceLength(voiceLength);
			messageEntity.setDate(time);
			messageEntity.setSendFlag(isSend==1?true:false);
			list.add(messageEntity);
		}
		return list;
	}
	/**
	 * 查询新闻收藏列表
	 * @return
	 */
	public List<NewsEntity> queryNewsCollectionList(){
		List<NewsEntity> newsEntities = new ArrayList<NewsEntity>();
		Cursor cursor = null;
		try{
			cursor = mDatabase.queryNewShouCangList();
			while(cursor.moveToNext()){
				long collectionId = cursor.getLong(0);
				String newsId = cursor.getString(1);
				int newType = cursor.getInt(2);
				String newsTitle = cursor.getString(3);
				String newsTime = cursor.getString(4);
				String typeTitle = cursor.getString(5);
				
				NewsEntity newsEntity = new NewsEntity();
				newsEntity.setCollectionId(String.valueOf(collectionId));
				newsEntity.setNewsId(newsId);
				newsEntity.setType(String.valueOf(newType));
				newsEntity.setTitle(newsTitle);
				newsEntity.setTime(newsTime);
				newsEntity.setTypeTitle(typeTitle);
				newsEntities.add(newsEntity);
			}
		}catch(SQLiteException e){
			e.printStackTrace();
		}finally{
			if(cursor!=null)cursor.close();
		}
		
		return newsEntities;
	}	
	
	/**
	 * 根据新闻id和类型查询新闻是否存在
	 * @param newsId
	 * @param newsType
	 * @return
	 */
	public boolean queryNewsIdExist(String newsId,String newsType){
		boolean isExit = false;
		Cursor cursor = null;
		try{
			cursor =mDatabase.queryNewsIdIsExis(cursor,newsType,newsId,isExit);
			isExit = cursor.moveToNext();
		}catch(SQLiteException e){
			e.printStackTrace();
		}finally{
			if(cursor != null)cursor.close();
		}
		
		return isExit;
	}
	
	/**
	 * 添加收藏
	 * @param newsEntity
	 * @return
	 */
	public boolean  addNewsToCollection(NewsEntity newsEntity){
		ContentValues contentValues = new ContentValues();
		contentValues.put(Tables.TableNewsCollection.NEWS_ID, newsEntity.getNewsId());
		contentValues.put(Tables.TableNewsCollection.NEWS_TYPE, newsEntity.getType());
		contentValues.put(Tables.TableNewsCollection.NEWS_TITLE, newsEntity.getTitle());
		contentValues.put(Tables.TableNewsCollection.NEWS_CONTENT, newsEntity.getContent());
		contentValues.put(Tables.TableNewsCollection.NEWS_TIME, newsEntity.getTime());
		contentValues.put(Tables.TableNewsCollection.NEWS_CLLECTION_TIME, System.currentTimeMillis());
		contentValues.put(Tables.TableNewsCollection.TYPE_TITLE,newsEntity.getTypeTitle());
		return mDatabase.inserNewToCollection(contentValues);
	}
	
	/**
	 * 根据收藏id查询收藏内容
	 * @param collectionId
	 * @return
	 */
	public String queryNewsContent(String collectionId){
		Cursor cursor = null;
		try{        
			cursor = mDatabase.QueryNewsQueryContent(collectionId);
				while(cursor.moveToNext()){
					String content = cursor.getString(0);
					return content;
				}
		}catch(SQLiteException e){
			e.printStackTrace();
		}finally{
			if(cursor != null)cursor.close();
		}
		return null;
	}
	/**
	 * 根据收藏id删除收藏新闻
	 * @param collectionId
	 * @return
	 */
	public boolean deleteCollectionNews(String collectionId){
		int value = mDatabase.deleteCollectionIdNews(collectionId);
		return value>0;
	}
	
	/**
	 * 根据收藏id删除收藏新闻
	 * @param collectionId
	 * @return
	 */
	public boolean deleteCollectionNews(List<NewsEntity> list){
		return mDatabase.deleteCollectionToId(list);
	}
	/**
	 * 删除好友聊天消息
	 * @param messageEntity
	 * @return
	 */
	public boolean deleteChatMessage(String id) {
		return 	mDatabase.deleteFriendChatMessage(id);
	}

	/**
	 * 清除所有聊天记录
	 * 
	 * @return
	 */
	public boolean clearAllChatMessage() {
		try {
			mDatabase.deleteAllChatMessage();
			return true;
		} catch (SQLiteException e) {
			return false;
		}
	}
	private  void saveGroupInfo(Context context, GroupInfoEntity group,
			String customerId) {
		ContentValues values = new ContentValues();
		values.put(Tables.TableChatMessage.ID, group.getId());
		values.put(Tables.TableChatMessage.CUSTOMERID, customerId);
		values.put(Tables.TableChatMessage.NAME, group.getName());
		values.put(Tables.TableChatMessage.NOTE, group.getNote());
		values.put(Tables.TableChatMessage.CREATETIME, group.getCreateTime());
		values.put(Tables.TableChatMessage.INFOLAYID, group.getInfoId());
		values.put(Tables.TableChatMessage.CREATECUSTOMERID, group.getCreateCustomerID());
		values.put(Tables.TableChatMessage.LIMITNNUM, group.getLimitNumber());
		values.put(Tables.TableChatMessage.FACE_HEAD_NORML, group.getNormalHeadIcon());
		values.put(Tables.TableChatMessage.FACE_HEAD_BIG, group.getBigHeadIcon());
		values.put(Tables.TableChatMessage.NOT_READSMG, "");
		values.put(Tables.TableChatMessage.INFOLAYNAME, group.getInfoLayName());
		values.put(Tables.TableChatMessage.PERSONNUMBER, group.getPersonNumber());
		values.put(Tables.TableChatMessage.GROUPCLASS, group.isSalon() ? 1 : 2);
		
		values.put(Tables.TableChatMessage.RECORDDESC, group.getRecordDesc());
		values.put(Tables.TableChatMessage.GROUPLEVEL, group.getGroupLevel());
		values.put(Tables.TableChatMessage.GROUPFLAG, group.isSalonAttention());
		values.put(Tables.TableChatMessage.CHARGE, group.isCharge());
		values.put(Tables.TableChatMessage.ISRELEASESYSTEMMESSAGE, group.getIsReleaseSystemMessage());
		values.put(Tables.TableChatMessage.INCEPTMESSAGE, group.isInceptMessage());
		long result=mDatabase.saveGroupMesgInfos(Tables.TableFriend.TABLE_GROUP,values,Tables.ChatTable.ID+" = ?",new String[] { values.getAsString(Tables.ChatTable.ID) });
		if (result == 0)
		mDatabase.insertData(Tables.TableFriend.TABLE_GROUP, null, values);
	}
	/**
	 * 查询好友分组信息
	 */
	public  ArrayList<String> queryFriendGroupInfo(Context context,
			String customerId) {
		Cursor cursor = null;
		ArrayList<String> list = null;
		try {
			String sql = "select name from all_friend_group where customerId = ?";
			cursor = mDatabase.reQuery(sql,
					new String[] { customerId });
			list = new ArrayList<String>();
			while (cursor.moveToNext()) {
				list.add(cursor.getString(0));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(cursor!=null&& cursor.isClosed()){
				cursor.close();
			}
		}
		return list;
	}
	/**
	 * 保存好友信息
	 * 
	 * @param context
	 * @param cus
	 *            保存好友的资料对象
	 * @param customerId
	 *            登陆客户ID
	 * @param group
	 *            所在分组
	 * @param type
	 */
	public  void saveFriendInfo(Context context, CustomerInfoEntity cus,
			String customerId, String groupName, int type) {
		if (customerId == null) {
			return;
		}
		if (cus == null)
			return;
		ContentValues values = new ContentValues();
		values.put(Tables.ChatTable.ID, cus.getId());
		values.put(Tables.TableChatMessage.CUSTOMERID, customerId);
		values.put(Tables.ChatTable.NAME, cus.getName());
		values.put(Tables.TableFriend.REALNAME, "");
		values.put(Tables.TableFriend.DESCRIPTION, cus.getDescription());
		values.put(Tables.TableChatMessage.CUSMESSSAGE, cus.getCusMessag());
		values.put(Tables.TableLogin.ACCOUNT, cus.getUsername());
		values.put(Tables.Information.LOCUS, cus.getCustomerlocus());
		values.put(Tables.Information.SEX, cus.getSex());
		values.put(Tables.Information.AGE, cus.getAge());
		values.put(Tables.Information.DOCTORTITLE, cus.getDoctorTitle());
		values.put(Tables.Information.DOCTORUNITADD, cus.getDoctorunitAdd());
		values.put(Tables.Information.DOCTORUNIT, cus.getDoctorunit());
		values.put(Tables.Information.DWELLINGPLACE, cus.getDwellingplace());
		values.put(Tables.TableChatMessage.FACE_HEAD_NORML, cus.getNormalHeadIcon());
		values.put(Tables.TableChatMessage.FACE_HEAD_BIG, cus.getBigHeadIcon());
		values.put(Tables.TableChatMessage.NOT_READSMG, "");
		values.put(Tables.TableChatMessage.RELATIONTYPE, cus.getIsAttentionFriend());
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");// 可以方便地修改日期格式
		String time = dateFormat.format(now);
		values.put(Tables.ChatTable.TIME, time);
//		if (cus.getIsAttentionFriend() != 2 && cus.getIsAttentionFriend() != 0 ) {
//			values.put(Tables.TableChatMessage.ISCOLLECT, true);
//		}else {
//			values.put(Tables.TableChatMessage.ISCOLLECT, false);
//		}
		switch (type) {
//		case Tables.TableFriend.TYPE_ATTENTION_FOR_ME:
//			values.put(Tables.TableFriend.ATTENTION_FOR, "1");
//			break;
//		case Tables.TableFriend.TYPE_ATTENTION_TO_ME:
//			values.put(Tables.TableFriend.ATTENTION_TO, "1");
//			break;
		case Tables.TableFriend.TYPE_RECENT:
			values.put(Tables.TableFriend.RECENT, "1");
			break;
//		case Tables.TableFriend.TYPE_BLACKLIST:
//			values.put(Tables.TableFriend.BLACKLIST , "1");
//			break;
//		default:
//			break;
		}
		if (groupName != null)
			values.put(Tables.TableFriend.GROUPNAME, groupName);
		
		long result=mDatabase.updateData(Tables.TableFriend.TABLE_FRIEND, values,Tables.ChatTable.ID +"= ?",
				new String[] { values.getAsString(Tables.ChatTable.ID) });
		if (result == 0)
		mDatabase.insertData(Tables.TableFriend.TABLE_FRIEND, null, values);
	}
	
	/**
	 * 更新数据库内好友收藏属性
	 * 
	 * @param context
	 * @param id
	 *            好友Id
	 * @param isCollect
	 *            是否收藏
	 */
	public  Long updateFriendInfo(Context context, String table,
			String whereArgs, String key, String value) {
		ContentValues values = new ContentValues();
		values.put(key, value);

		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");// 可以方便地修改日期格式
		String time = dateFormat.format(now);
		values.put(Tables.ChatTable.TIME, time);
		
		return mDatabase.updateData(table, values, Tables.ChatTable.ID+" = ?", new String[] { whereArgs });
	}
	
	
	/**
	* @Title: saveOrUpdateRecentGroupInfo 
	* @Description: 
	* @param @param context
	* @param @param group
	* @param @param customerId    
	* @return void    
	* @throws
	 */
		
		public  synchronized void saveOrUpdateRecentGroupInfo(GroupInfoEntity group,
				String customerId) {
			/*SQLiteDatabase sqliteData = getSqLiteDatabase(context, "history.db",
					false);*/
			ContentValues values = new ContentValues();
			values.put("id", group.getId());
			values.put("customerId", customerId);
			values.put("name", group.getName());
			values.put("note", group.getDescription());
			values.put("createTime", group.getCreateTime());
			values.put("infoLayid", group.getInfoId());
			values.put("createCustomerID", group.getCreateCustomerID());
			values.put("limitNnum", group.getLimitNumber());
			values.put("face_head_norml", group.getNormalHeadIcon());
			values.put("face_head_big", group.getBigHeadIcon());
			values.put("face_head_big", group.getBigHeadIcon());
			values.put("not_readsmg", "");
			values.put("infoLayName", group.getInfoLayName());
			values.put("personNumber", group.getPersonNumber());
			values.put("groupClass", group.isSalon() ? 1 : 2);
			values.put("iscollect", group.isSalonAttention());
			values.put("groupflag", group.isSalonAttention());
			values.put("charge", group.isCharge());
			values.put("recordDesc", group.getRecordDesc());
			values.put("isReleaseSystemMessage", group.getIsReleaseSystemMessage());
			values.put("inceptMessage", group.isInceptMessage());
			values.put("recent", 1);
			// sqliteData.replace("group_list", null, values);
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");// 可以方便地修改日期格式
			String time = dateFormat.format(now);
			values.put("time", time);

			long result = mDatabase.saveGroupMesgInfos("group_list", values, "id = ?",
					new String[] { values.getAsString("id") });
			if (result == 0)
				mDatabase.insertData("group_list", null, values);
//			sqliteData.close();
		}
	/**
	 * 删除新闻收藏数据
	 * 
	 * @param userId
	 *            客户id
	 * @param newsId
	 *            被删除新闻id
	 */
	public  boolean deleteNewsCollect(Context context, String userId,
			String newsId) {
		int result=mDatabase.deleteIdNews(Tables.TableNewsCollection.TABL_NAME_NEWS_CONNECTION,Tables.TableNewsCollection.CONNECTION_USERID+"= ? and "+Tables.TableNewsCollection.NEWS_ID+" = ?", new String[] { userId,
				newsId });
		if (result > 0) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 删除聊天室信息
	 * 
	 * @param context
	 * @param id
	 *            好友ID
	 */
	public  void deleGroupInfo(Context context, String id) {
		mDatabase.deleteIdNews(Tables.TableFriend.TABLE_GROUP, Tables.ChatTable.ID+" = ?", new String[] { id });
	}
	
	/**
	 * 查询好友信息
	 * 
	 * @param context
	 * @param memInfo
	 * @param arrID
	 * @param friendList
	 *            健康友集合 HashMap<所在分组,分组内成员Id集合>
	 * @param customerId
	 *            登录客户ID
	 */
	public   void queryFriendInfo(Context context,
			Map<String, Object> memInfo,
			LinkedHashMap<String, List<String>> friendList, String customerId) {
		ArrayList<String> fri_group = new ArrayList<String>();
		ArrayList<String> strangerList = new ArrayList<String>();
		ArrayList<String> blacklistList = new ArrayList<String>();
		String recent = context.getResources()
				.getString(R.string.friend_recentContact);
		String friend = context.getResources().getString(R.string.friend_friendList);
		String stranger = context.getResources().getString(R.string.friend_stranger);
		String blacklist = context.getResources().getString(R.string.friend_blacklist);
		fri_group.add(recent);
		fri_group.add(friend);
		fri_group.add(blacklist);
		List<String> recentList = null;
		if (!friendList.containsKey(recent)) {
			recentList = new ArrayList<String>();
			friendList.put(recent, recentList);
		} else
			recentList = friendList.get(recent);

		if (!friendList.containsKey(friend)) {
			ArrayList<String> temp = new ArrayList<String>();
			friendList.put(friend, temp);
		} else
			friendList.get(friend).clear();

		if (!friendList.containsKey(stranger)) {
			friendList.put(stranger, strangerList);
		} else
			friendList.get(stranger).clear();
		
		if (!friendList.containsKey(blacklist)) {
			friendList.put(blacklist, blacklistList);
		} else
			friendList.get(blacklist).clear();


		if (customerId == null) {
			return;
		}
		String sql = "select "+Tables.TableFriend.ID+","+Tables.TableFriend.RECENT+",name ,realname,description,cusmesssage,locus,sex,"
				+ "age,doctorTitle ,doctorunitAdd,doctorunit,dwellingplace,face_head_norml,face_head_big,"
				+ "groupName,not_readsmg,iscollect,account,attention_for,attention_to,blacklist,time from "+Tables.TableFriend.TABLE_FRIEND+" where customerId = ? order by time desc";
		 Cursor cursor=mDatabase.reQuery(sql, new String[] { customerId });
		while (cursor.moveToNext()) {
			CustomerInfoEntity cus = new CustomerInfoEntity();
			String id = cursor.getString(0);
			cus.setAge(cursor.getString(8));
			cus.setBigHeadIcon(cursor.getString(14));
			cus.setNormalHeadIcon(cursor.getString(13));
			cus.setId(id);
			cus.setSex(cursor.getString(7));
			String customerlocus = cursor.getString(6);
			cus.setCustomerlocus(customerlocus);
			cus.setDoctorTitle(cursor.getString(9));
			cus.setDoctorunit(cursor.getString(11));
			cus.setDoctorunitAdd(cursor.getString(10));
			cus.setDwellingplace(cursor.getString(12));
			cus.setUsername(cursor.getString(18));
			cus.setName(cursor.getString(2));
			cus.setCusMessag(cursor.getString(5) == null ? "":cursor.getString(5));
			cus.setDescription(cursor.getString(4));

			if (!memInfo.containsKey(id)) {
				memInfo.put(id, cus);
			}

			if (cursor.getInt(1) == 1 && !recentList.contains(id)) {
				if (recentList.size() <= 20) {
					recentList.add(id);
				}
			}

			if (cursor.getInt(20) == 1 && !strangerList.contains(id)) {
				strangerList.add(id);
			}
			//添加黑名单
			if (cursor.getInt(21) == 1 && !blacklistList.contains(id)) {
				blacklistList.add(id);
			}

			if (cursor.getInt(19) == 1) {
				String group = cursor.getString(15);
				if (group == null) {
					group = friend;
				}
				if (!fri_group.contains(group)) {
					fri_group.add(group);
				}
				if (friendList.containsKey(group)) {
					if (!friendList.get(group).contains(id))
						friendList.get(group).add(id);
				} else {
					ArrayList<String> temp = new ArrayList<String>();
					temp.add(id);
					friendList.put(group, temp);
				}
			}
		}
			if(cursor!=null&& cursor.isClosed()){
				cursor.close();
			}
	}
	/**
	 * 删除好友信息
	 * 
	 * @param context
	 * @param id
	 *            好友ID
	 */
	public  void deleFriendInfo(Context context, String id) {
		mDatabase.deleteIdNews(Tables.TableFriend.TABLE_FRIEND,Tables.ChatTable.ID +"= ?", new String[] { id });
	}
	
	/**
	 * 查询最近联系人
	 * 
	 * @param context
	 * @param List
	 *            最近联系人集合
	 * @param customerId
	 *            登录客户ID
	 */
	public  synchronized void queryFriendRecentInfo(Context context,List<String> recentList,
			String customerId) {
		AppData appData = HTalkApplication.getAppData();
		if (customerId == null) {
			return;
		}
		String sql = "select id,recent,name ,realname,description,cusmesssage,locus,sex,"
				+ "age,doctorTitle ,doctorunitAdd,doctorunit,dwellingplace,face_head_norml,face_head_big,"
				+ "groupName,not_readsmg,iscollect,account,attention_for,attention_to,time,relationType from "+Tables.TableFriend.TABLE_FRIEND+" where customerId = ? and recent = 1 order by time desc";
		Cursor cursor = mDatabase.reQuery(sql, new String[] { customerId });
		while (cursor.moveToNext()) {
			CustomerInfoEntity cus = new CustomerInfoEntity();
			String id = cursor.getString(0);
			cus.setAge(cursor.getString(8));
			cus.setBigHeadIcon(cursor.getString(14));
			cus.setNormalHeadIcon(cursor.getString(13));
			cus.setId(id);
			cus.setSex(cursor.getString(7));
			String customerlocus = cursor.getString(6);
			cus.setCustomerlocus(customerlocus);
			cus.setDoctorTitle(cursor.getString(9));
			cus.setDoctorunit(cursor.getString(11));
			cus.setDoctorunitAdd(cursor.getString(10));
			cus.setDwellingplace(cursor.getString(12));
			cus.setUsername(cursor.getString(18));
			cus.setName(cursor.getString(2));
			cus.setCusMessag(cursor.getString(5) == null ? "":cursor.getString(5));
			cus.setIsAttentionFriend(cursor.getInt(22));
			cus.setDescription(cursor.getString(4));
			if (cursor.getInt(1) == 1) {
			switch (cursor.getInt(22) ) {
			case 4:
				appData.getLatelyDoctordList().add(id);
				break;
			case 3:
				appData.getLatelyCustomList().add(id);
				break;
			case 1:
				recentList.add(id);
				break;
			default:
				appData.getLatelyFriendIdList().add(id);
				break;
				}
				appData.updateCacheInfomation(cus);
			}
		}
			if(cursor!=null&& cursor.isClosed()){
				cursor.close();
		}
	}

	/**
	 * 查询好友
	 * 
	 * @return
	 */
	public  ArrayList<Object> queryFriend(Context context, String value) {
		ArrayList<Object> list = null;
		Cursor cursor = null;
		try {
			list = new ArrayList<Object>();
			String sql = "select "+Tables.TableFriend.ID+" from "+Tables.TableFriend.TABLE_FRIEND+" where "+Tables.TableFriend.NAME+" = ? or "+Tables.TableFriend.ACCOUNT+" = ?";
			 cursor = mDatabase.reQuery(sql, new String[] { value, value });
			while (cursor.moveToNext()) {
				list.add(cursor.getString(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(cursor!=null&& cursor.isClosed()){
				cursor.close();
			}
		}
		return list;
	}
	
	/**
	 * 查询最近参与群
	 * 
	 * @param context
	 * @param memInfo
	 *            所有资料
	 * @param arrID
	 *            所有ID
	 * @param groupList
	 *            聊天室列表集合
	 * @param customerId
	 *            登录客户ID
	 */
	public  synchronized void queryRecentGroup(List<String> recentList,
			String customerId) {
		// ArrayList<String> recentList = new ArrayList<String>();
		if (customerId == null) {
			return;
		}
		Cursor cursor = null;
		try {
			cursor = mDatabase.queryData(Tables.TableFriend.TABLE_GROUP, new String[] { Tables.ChatTable.ID,
					Tables.TableChatMessage.ISCOLLECT,Tables.ChatTable.NAME ,Tables.TableChatMessage.NOTE, Tables.TableChatMessage.CREATETIME,Tables.TableChatMessage.INFOLAYID,
					Tables.TableChatMessage.LIMITNNUM,Tables.TableChatMessage.FACE_HEAD_NORML , Tables.TableChatMessage.FACE_HEAD_BIG, Tables.TableChatMessage.INFOLAYNAME,
					Tables.TableFriend.RECENT,Tables.TableFriend.TABLE_GROUP_CLASS , Tables.TableChatMessage.CREATECUSTOMERID, Tables.TableChatMessage.PERSONNUMBER,Tables.TableChatMessage.RECORDDESC,Tables.TableChatMessage.GROUPLEVEL,Tables.TableChatMessage.GROUPFLAG,Tables.TableChatMessage.CHARGE,Tables.TableChatMessage.ISRELEASESYSTEMMESSAGE,Tables.TableChatMessage.INCEPTMESSAGE,
					Tables.ChatTable.TIME }, Tables.TableChatMessage.CUSTOMERID+"= ? and "+Tables.TableFriend.RECENT+" = 1",
					new String[] { customerId }, null, null, "time desc");
			while (cursor.moveToNext()) {
				GroupInfoEntity group = new GroupInfoEntity();
				String id = cursor.getString(0);
				group.setBigHeadIcon(cursor.getString(8));
				group.setNormalHeadIcon(cursor.getString(7));
				group.setId(id);
				group.setLimitNumber(cursor.getString(6));
				group.setInfoLayName(cursor.getString(9));
				group.setIsConnection(cursor.getInt(1));
				group.setName(cursor.getString(2));
				group.setNote(cursor.getString(3));
				group.setInfoId(cursor.getString(5));
				group.setCreateTime(cursor.getString(4));
				group.setSalon(cursor.getInt(11) == 1);
				group.setCreateCustomerID(cursor.getString(12));
				group.setPersonNumber(cursor.getString(13));
				group.setRecordDesc(cursor.getString(14));
				group.setGroupLevel(cursor.getString(15));
				group.setSalonAttention(cursor.getInt(16)==1?true:false);
				group.setCharge(cursor.getInt(17)==1?true:false);
				group.setIsReleaseSystemMessage(cursor.getInt(18)==1?true:false);
				group.setInceptMessage(cursor.getInt(19)==1?true:false);
				if (cursor.getInt(10) == 1 && !recentList.contains(id)) {
					recentList.add(id);
				}
				HTalkApplication.getAppData().updateCacheInfomation(group);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(cursor!=null&& cursor.isClosed()){
				cursor.close();
			}
		}
	}
	
	/**
	 * 查询话题
	 * 
	 * @return
	 */
	public  ArrayList<Object> queryGroup(Context context, String value) {
		ArrayList<Object> list = null;
		Cursor cursor = null;
		try {
			list = new ArrayList<Object>();
			String sql = "select "+Tables.ChatTable.ID+" from "+Tables.TableFriend.TABLE_GROUP+" where "+Tables.ChatTable.NAME+" = ? or "+Tables.TableChatMessage.INFOLAYNAME+" = ?";
			cursor = mDatabase.reQuery(sql, new String[] { value, value });
			while (cursor.moveToNext()) {
				list.add(cursor.getString(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(cursor!=null&& cursor.isClosed()){
				cursor.close();
			}
		}
		return list;
	}

	
	/**
	 * 删除沙龙信息
	 */
	public void deleteSalon(String id){
		mDatabase.deleteSalon(TableFriend.TABLE_GROUP,ChatTable.ID+" = ?", new String[] { id });
	}
	
	/**
	 * 好友列表
	 * @param id 
	 */
	public void deleteFriend(String id) {
		mDatabase.deleteSalon(TableFriend.TABLE_FRIEND,ChatTable.ID+" = ?", new String[] { id });
	}
	
	/**
	 * 初始化的时候 加载话题资料
	 * @param array
	 */
	public void initGroupInfo(JSONArray array){
		mDatabase.insertInitGroupList(array);
	}

	/**
	 * 初始化的时候 加载人资料
	 * @param array
	 */
	public void initPersonInfo(JSONArray mJsonArray) {
		mDatabase.insertInitPersonInfo(mJsonArray);
	}

	/**
	 * 存储离线消息
	 * @param jsonArray
	 */
	public void initMessage(JSONArray jsonArray) {
		mDatabase.insertLeaveMessage(jsonArray);
	}
	
	/**
	 * 加入最近联系人中
	 * @param id
	 * @param isJoin true 添加到表中并添加到最近联系人  false 仅仅添加到表中
	 */
	public void joinLatelyObject(BaseInfoEntity entity,boolean isJoin){
		if(entity ==null)return;
		if(entity instanceof GroupInfoEntity){
			mDatabase.joinLatelyGroup((GroupInfoEntity)entity,isJoin);
		}else if(entity instanceof CustomerInfoEntity){
			mDatabase.joinLatelyPerson((CustomerInfoEntity)entity,isJoin);
		}
	}
	
	//查询所有最近联系人
	public List<BaseInfoEntity> queryAllLater(){
		return mDatabase.queryAllLater();
	}
	
	/**
	 * 根据id 查询当前登录者  与 用户之间的 关系
	 * @param id
	 * @return -1 或者 0  表示没有任何关系
	 */
	public synchronized int queryRelType(String id){
		int type;
		Cursor c = mDatabase.reQuery("select rel_type from "+Tables.PERSONINFO_NAME+" where c_id = ?", new String[]{id});
		if(c.moveToNext()){
			type = c.getInt(c.getColumnIndex("rel_type"));
			c.close();
			return type;
		}else{
			c.close();
			return -1;
		}
	} 
	
	
	/**
	 * 改变 与 用户之间的 关系
	 * @param id
	 * @return -1 或者 0  表示没有任何关系
	 */
	public synchronized void changeRelType(BaseInfoEntity entity){
		Cursor c = mDatabase.reQuery("select rel_type from "+Tables.PERSONINFO_NAME+" where c_id = ?", new String[]{entity.getId()});
		if(c.moveToNext()){
			mDatabase.updateRelType(entity);
		}else{
			joinLatelyObject(entity, false);
		}
		c.close();
	}

	public long insertChatMessageDoctor(MessageEntity messageEntity) {

		ContentValues contentValues = new ContentValues();
		//服务器消息删除状态
		contentValues.put(TableChatMessage.MSG_SERVER_DELET_STATE, messageEntity.getServerId() != null ? "-1":"1");
		contentValues.put(TableChatMessage.SENDER_ID, messageEntity.getSenderId());
		contentValues.put(TableChatMessage.RECEIVER_ID, messageEntity.getReceiverId());
		contentValues.put(TableChatMessage.MESSAGE_TYPE, messageEntity.getType());
		contentValues.put(TableChatMessage.MESSAGE_CONTENT, messageEntity.getContent());
		
		//虚拟医生消息内容
		if(messageEntity.getContentJsonArray() != null)
			contentValues.put(TableChatMessage.MESSAGE_JSONCOTENT, messageEntity.getContentJsonArray().toString());
		contentValues.put(TableChatMessage.VOICE_LENGTH, messageEntity.getVoiceLength());
		contentValues.put(TableChatMessage.DOWNORUPSTATE,messageEntity.getDownOrUpState());
		contentValues.put(TableChatMessage.SENDSTATE, messageEntity.getSendState());
		contentValues.put(TableChatMessage.READ_TAG, messageEntity.getReadTag());
		contentValues.put(TableChatMessage.TIME, messageEntity.getDate());
		if(messageEntity.getServerId() != null){
			contentValues.put(Tables.TableChatMessage.MSG_SERVER_ID, messageEntity.getServerId());
		}
		//是否是发送标记
		contentValues.put(TableChatMessage.ISSEND,messageEntity.isSendFlag()?1:0);
		//地址消息
		if(messageEntity.getType() == MessageEntity.TYPE_LOCATION 
				&& messageEntity.getAddress() != null){
			contentValues.put(TableChatMessage.MESSAGE_PATH,messageEntity.getAddress());
		}
		long id = mDatabase.saveUserChatMessage(contentValues);
		messageEntity.setId(String.valueOf(id));
		return id;
	
		
	}
	
	
}
