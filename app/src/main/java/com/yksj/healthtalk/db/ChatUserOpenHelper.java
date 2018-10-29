package com.yksj.healthtalk.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yksj.healthtalk.net.socket.SmartFoxClient;

/**
 * 客户对应数据库,实现对数据库创建,更新
 * 
 * @author zhao yuan
 * @version 3.0
 */
public class ChatUserOpenHelper extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = 13;// 数据库版本
	private static String database_name = "talk_db";// 数据库名称
	
	public ChatUserOpenHelper(Context context, String name) {

		super(context, SmartFoxClient.getLoginUserId()+database_name, null, DATABASE_VERSION);

		if (name == null || "".equals(name))
			throw new IllegalStateException("数据库名称错误" + name + database_name+"当前用户的名字__"+SmartFoxClient.getLoginUserInfo().getUsername());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		try {
//			db.execSQL("alter table friend_list add column blacklist integer default 0");
//			db.execSQL("alter table group_list add column recordDesc String ");
//			db.execSQL("alter table group_list add column groupLevel String ");
//			db.execSQL("alter table group_list add column groupflag Boolean ");
//			db.execSQL("alter table group_list add column charge Boolean ");
//			db.execSQL("alter table group_list add column isReleaseSystemMessage Boolean ");
//			db.execSQL("alter table group_list add column inceptMessage Boolean ");
//			db.execSQL("alter table friend_list add column relationType INTEGER");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	/**
	 * 创建表
	 * @param db
	 */
	private void createTable(SQLiteDatabase db) {
		// 朋友聊天历史
//		final StringBuffer sqlBuffer = new StringBuffer(
//				Tables.TableDB.CREATE_TABLE_IF_NOT_EXISTS);
//		sqlBuffer.append(Tables.TableChatMessage.TABLE_CHAT_MESSAGE);
//		sqlBuffer.append(" (");
//		sqlBuffer.append(Tables.TableChatMessage.MESSAGE_ID
//				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
//				+ Tables.TableChatMessage.MSG_SERVER_ID + " TEXT UNIQUE ON CONFLICT IGNORE,"
//				+ Tables.TableChatMessage.MSG_SERVER_DELET_STATE + " TEXT,"
//				+ Tables.TableChatMessage.MESSAGE_TYPE + " INT,"
//				+ Tables.TableChatMessage.RECEIVER_ID + " INTEGER,"
//				+ Tables.TableChatMessage.SENDER_ID + " INTEGER,"
//				+ Tables.TableChatMessage.READ_TAG + " INT,"
//				+ Tables.TableChatMessage.ISSEND + " INT,"
//				+ Tables.TableChatMessage.MESSAGE_CONTENT + " TEXT,"
//				+ Tables.TableChatMessage.MESSAGE_JSONCOTENT + " TEXT,"
//				+ Tables.TableChatMessage.MESSAGE_PATH + " TEXT,"
//				+ Tables.TableChatMessage.SENDSTATE + " INT,"
//				+ Tables.TableChatMessage.DOWNORUPSTATE + " INT,"
//				+ Tables.TableChatMessage.VOICE_LENGTH + " FLOAT,"
//				+ Tables.TableChatMessage.SIZE + " INT,"
//				+ Tables.ChatTable.TIME + " TEXT" + ")");
//		db.execSQL(sqlBuffer.toString());
//		// 收藏
//		sqlBuffer.setLength(0);
//		sqlBuffer.append(Tables.TableDB.CREATE_TABLE_IF_NOT_EXISTS
//				+ Tables.TableNewsCollection.TABLE_NEWS_COllECTION + " ("
//				+ Tables.TableNewsCollection.NEWS_CLLECTION_ID
//				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
//				+ Tables.TableNewsCollection.NEWS_ID + " TEXT,"
//				+ Tables.TableNewsCollection.NEWS_TYPE + " INT,"
//				+ Tables.TableNewsCollection.NEWS_TITLE + " TEXT,"
//				+ Tables.TableNewsCollection.NEWS_CONTENT + " TEXT,"
//				+ Tables.TableNewsCollection.NEWS_TIME + " INTEGER,"
//				+ Tables.TableNewsCollection.TYPE_TITLE + " TEXT,"
//				+ Tables.TableNewsCollection.NEWS_CLLECTION_TIME + " INTEGER"
//				+ ")");
//		db.execSQL(sqlBuffer.toString());
//		
//		
//		// 虚拟医生聊天历史
//		sqlBuffer.setLength(0);
//		sqlBuffer.append("CREATE TABLE IF NOT EXISTS ");
//		sqlBuffer.append(Tables.TableDoctor.TABLE_VIRTUAL_DOCTOR_MESSAGE);
//		sqlBuffer.append("(");
//		sqlBuffer.append("message_id INTEGER PRIMARY KEY AUTOINCREMENT,");
//		sqlBuffer.append("message_type INT,");
//		sqlBuffer.append("sendstate INT,");
//		sqlBuffer.append("message_content TEXT,");
//		sqlBuffer.append("receiver_id INTEGER,");
//		sqlBuffer.append("sender_id INTEGER,");
//		sqlBuffer.append("read_tag INT,");
//		sqlBuffer.append("msg_content TEXT,");
//		sqlBuffer.append("isSend INT,");
//		sqlBuffer.append("messageCode TEXT,");
//		sqlBuffer.append("messageClass TEXT,");
//		sqlBuffer.append("retFeatur int,");
//		sqlBuffer.append("functionId TEXT,");
//		sqlBuffer.append("linkDialog TEXT,");
//		sqlBuffer.append("ltalkName TEXT,");
//		sqlBuffer.append("serviceLinkId TEXT,");
//		sqlBuffer.append("serviceLinkName TEXT,");
//		sqlBuffer.append("largeinfoId TEXT,");
//		sqlBuffer.append("talkname TEXT,");
//		sqlBuffer.append("time TEXT");
//		sqlBuffer.append(")");
//		db.execSQL(sqlBuffer.toString());
//
//		// 朋友列表
//		sqlBuffer.setLength(0);
//		sqlBuffer.append("CREATE TABLE IF NOT EXISTS ["
//				+ Tables.TableFriend.TABLE_FRIEND + "] (" + "[id] CHAR, "
//				+ "[customerId] INTEGER, " + "[recent] INTEGER, "
//				+ "[iscollect] BOOL, " + "[attention_to] INTEGER, "
//				+ "[attention_for] INTEGER, " + "[blacklist] INTEGER, "
//				+ "[name] TEXT, " + "[realname] TEXT, " + "[account] TEXT, "
//				+ "[description] TEXT, " + "[cusmesssage] TEXT, "
//				+ "[locus] TEXT, " + "[sex] TEXT, " + "[age] TEXT, "
//				+ "[doctorTitle] TEXT, " + "[doctorunitAdd] TEXT, "
//				+ "[doctorunit] TEXT, " + "[dwellingplace] TEXT, "
//				+ "[face_head_norml] CHAR, " + "[face_head_big] CHAR, "
//				+ "[groupName] TEXT, " + "[not_readsmg] INT,"
//				+ "time DATETIME," + "[relationType] INTEGER,"
//				+ "PRIMARY KEY(id,customerId))");
//		db.execSQL(sqlBuffer.toString());
//
//		// 组列表
//		sqlBuffer.setLength(0);
//		sqlBuffer.append("CREATE TABLE IF NOT EXISTS ["
//				+ Tables.TableFriend.TABLE_GROUP + "] (" + "[id] CHAR, "
//				+ "[customerId] CHAR, " + "[recent] INTEGER, "
//				+ "[iscollect] BOOL, " + "[name] TEXT, " + "[note] TEXT, "
//				+ "[personNumber] INTEGER," + "[createTime] TEXT, "
//				+ "[infoLayid] TEXT, " + "[groupClass] INTEGER, "
//				+ "[createCustomerID] TEXT, " + "[limitNnum] TEXT, "
//				+ "[face_head_norml] CHAR, " + "[face_head_big] CHAR, "
//				+ "[infoLayName] CHAR, " + "time DATETIME,"
//				+ "[recordDesc] TEXT," + "[groupLevel] CHAR,"
//				+ "[groupflag] BOOL," + "[charge] BOOL,"
//				+ "[isReleaseSystemMessage] BOOL," + "[inceptMessage] BOOL,"
//				+ "[not_readsmg] INT," + "PRIMARY KEY(id,customerId))");
//		db.execSQL(sqlBuffer.toString());
//		sqlBuffer.setLength(0);
//		//
//		sqlBuffer
//				.append("CREATE TABLE IF NOT EXISTS ["
//						+ Tables.TableFriend.ALL_FRIEND_GROUP
//						+ "] ([id] INTEGER , [customerId] CHAR, [name] CHAR PRIMARY KEY)");
//		db.execSQL(sqlBuffer.toString());
		
		//创建个人表
		final StringBuffer sqlBuffer =new StringBuffer();
		sqlBuffer.setLength(0);
		sqlBuffer.append("CREATE TABLE IF NOT EXISTS "+Tables.PERSONINFO_NAME+" (c_id INT" +//用户id
				",rel_type  INT," +//关系 0 陌生 1是关注的 2是黑名单 3是客户 4医生 5 我的合作者 6 助理 7相互关注 8 小壹 9我关注的话题  10 我创建的话题 
				" is_lately INT default 0," +//是否是最近联系人 0不是  1 是
				" is_doctor INT," +//是否是医生(医师话题) 0不是  1 是
				"is_group  INT)");//是否是话题  0不是 1是
		db.execSQL(sqlBuffer.toString());
		
		//创建消息表
		sqlBuffer.setLength(0);
		sqlBuffer.append("CREATE TABLE IF NOT EXISTS "+Tables.CHAT_MESSAGE_NAME+" ( sender_id  INT," +//发送者 id
				" message_id   INT, " +//消息id
				"content   TEXT," +//消息内容
				"is_read  INT, " +//是否已读 0未读  1已读
				"msg_type   INT," +//消息type
				"msg_time  TEXT," +//消息时间
				" msg_path TEXT," +//消息附件路径
				" is_group  INT," +//是否是话题 0 不是 1是
				" c_id INT )");//针对话题 新消息发送者
		db.execSQL(sqlBuffer.toString());
		
		// 朋友聊天历史
		final StringBuffer sql = new StringBuffer(
				Tables.TableDB.CREATE_TABLE_IF_NOT_EXISTS);
		sql.append(Tables.TableChatMessage.TABLE_CHAT_MESSAGE);
		sql.append(" (");
		sql.append(Tables.TableChatMessage.MESSAGE_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ Tables.TableChatMessage.MSG_SERVER_ID + " TEXT UNIQUE ON CONFLICT IGNORE,"
				+ Tables.TableChatMessage.MSG_SERVER_DELET_STATE + " TEXT,"
				+ Tables.TableChatMessage.MESSAGE_TYPE + " INT,"
				+ Tables.TableChatMessage.RECEIVER_ID + " INTEGER,"
				+ Tables.TableChatMessage.SENDER_ID + " INTEGER,"
				+ Tables.TableChatMessage.READ_TAG + " INT,"
				+ Tables.TableChatMessage.ISSEND + " INT,"
				+ Tables.TableChatMessage.MESSAGE_CONTENT + " TEXT,"
				+ Tables.TableChatMessage.MESSAGE_JSONCOTENT + " TEXT,"
				+ Tables.TableChatMessage.MESSAGE_PATH + " TEXT,"
				+ Tables.TableChatMessage.SENDSTATE + " INT,"
				+ Tables.TableChatMessage.DOWNORUPSTATE + " INT,"
				+ Tables.TableChatMessage.VOICE_LENGTH + " FLOAT,"
				+ Tables.TableChatMessage.SIZE + " INT,"
				+ Tables.ChatTable.TIME + " TEXT" + ")");
		db.execSQL(sql.toString());
	}

}
