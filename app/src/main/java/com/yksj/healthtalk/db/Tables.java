package com.yksj.healthtalk.db;

/**
 * 所有的库表字段
 * @author zhao yuan
 * @version 3.0
 */
public class Tables {
	
	public static class ChatTable {
		public static final String TABLE_NAME = "chat";
		public static final String ID = "id";
		public static final String NAME = "name";
		public static final String TIME = "time";
	}
	
	/**
	 *医生 
	 */
	public static class TableDoctor{
		public static final String TABLE_NAME = "virtual_doctor_message";
		public static final String TABLE_VIRTUAL_DOCTOR_MESSAGE = "virtual_doctor_message";// 虚拟医生消息
	}
	
	/*
	 * 聊天信息
	 */
	public static class TableChatMessage{
		public static final String TABLE_NAME = "chat_message";
		public static final String ID = "id";
		public static final String DB_HISTORY_NAME = "history.db";//聊天历史,好友收藏库
		
		public static final String MSG_SERVER_DELET_STATE = "msg_server_delet_state";//服务器上消息删除状态 -1 表示没有删除
		public static final String TABLE_CHAT_MESSAGE = "chat_message";
		public static final String CUSTOMERID="customerId";
		public static final String NAME="name";
		public static final String FACE_HEAD_BIG="face_head_big";
		public static final String NOTE="note";
		public static final String ISCOLLECT="iscollect";
		public static final String INFOLAYNAME="infoLayName";
		public static final String GROUPCLASS="groupClass";
		public static final String NOT_READSMG="not_readsmg";
		public static final String PERSONNUMBER="personNumber";
		public static final String FACE_HEAD_NORML="face_head_norml";
		public static final String CREATETIME="createTime";
		public static final String CREATECUSTOMERID="createCustomerID";
		public static final String LIMITNNUM="limitNnum";
		public static final String INFOLAYID="infoLayid";
		
		public static final String MESSAGE_ID="message_id";
		// 好友聊天消息
		public static final String MSG_SERVER_ID = "msg_server_id";//服务器上的消息id
		public static final String MESSAGE_TYPE="message_type";
		public static final String SENDER_ID = "sender_id";
		public static final String READ_TAG = "read_tag";
		public static final String ISSEND = "isSend";
		public static final String MESSAGE_CONTENT = "message_content";
		public static final String MESSAGE_PATH = "message_path";
		public static final String SENDSTATE = "sendstate";//发送状态
		public static final String DOWNORUPSTATE = "downorupstate";//下载/上传状态
		public static final String VOICE_LENGTH = "voice_length";
		public static final String SIZE = "size";
		public static final String MESSAGE_JSONCOTENT = "message_jsoncontent";//消息json内容
		public static final String MSG_CONTENT="msg_content";
		public static final String MESSAGECLASS = "messageClass";
		public static final String MESSAGECODE="messageCode";
		public static final String RETFEATUR = "retFeatur";
		public static final String FUNCTIONID="functionId";
		public static final String LINKDIALOG = "linkDialog";
		public static final String LTALKNAME = "ltalkName";
		public static final String SERVICELINKID = "serviceLinkId";
		public static final String SERVICELINKNAME="serviceLinkName";
		public static final String LARGEINFOID = "largeinfoId";
		public static final String TALKNAME = "talkname";
		public static final String CUSMESSSAGE = "cusmesssage";
		public static final String RECEIVER_ID = "receiver_id";
		public static final String MESSAGE_STATUS = "message_status"; 
		public static final String TIME = "time";
		//话题新增
		public static final String RECORDDESC = "recordDesc";//描述
		public static final String  GROUPLEVEL = "groupLevel";//话题等级
		public static final String GROUPFLAG = "groupflag";//关注状态
		public static final String CHARGE ="charge";//是否付费
		public static final String ISRELEASESYSTEMMESSAGE ="isReleaseSystemMessage";//是否对消息厅开放
		public static final String INCEPTMESSAGE ="inceptMessage";//是否接受消息推送
		//朋友圈新增
		public static final String RELATIONTYPE = "relationType";//关系 0 无关系 1 我关注的 2 黑名单 3 客户 4医生
		
	}
	
	/**
	 *新闻
	 */
	public static class TableNewsCollection{
		public static final String TABL_NAME_NEWS_CONNECTION = "news_connection";// 新闻收藏
		public static final String TABLE_NEWS_COllECTION = "news_collection";
		public static final String NEWS_CLLECTION_ID = "news_cllection_id";
		public static final String NEWS_ID = "news_id";// 远程数据库消息id
		public static final String NEWS_TYPE = "news_type";
		public static final String NEWS_TITLE = "news_title";
		public static final String NEWS_CONTENT = "news_content";
		public static final String NEWS_TIME = "news_time";
		public static final String TYPE_TITLE = "type_title";
		public static final String NEWS_CLLECTION_TIME = "news_cllection_time";// 收藏时间
		public static final String TYPE_ID = "type_id";
		public static final String TYPE_CODE = "type_code";
		public static final String CONNECTION_USERID = "connection_userid";
		public static final String CONTENT_ID = "content_id";
		public static final String NEWSTITLE = "newsTitle";
		public static final String NEWSTIME = "newsTime";
		public static final String NEWSID = "newsid";
		
	}
	
	/*
	 * 健康树
	 */
	public static class TableHealthTree{
		// 2 挂号 3 体检 4 购药
		public static final String FLAG_GUAHAO = "2";
		public static final String FLAG_TIJIAN = "3";
		public static final String FLAG_GOUYAO = "4";
	}
	
	/**
	 * 关于登陆的处理
	 * @author Administrator
	 *
	 */
	public static class TableLogin{
		public static final int UPDATE_FACE = 1; // 更新头像
		public static final int UPDATE_PAS = 0; // 更新密码
		public static final int UPDATE_ISDEF = 2; // 更新默认账号
		public static final String ACCOUNT = "account";
		public  static final String TABLE_ACCOUNTS = "accounts";//多账户登陆
	}
	
	/**
	 * 关于好友的处理
	 * @author Administrator
	 *
	 */
	public static class TableFriend{
		public static final String TABLE_FRIEND = "friend_list";//好友
		public static final String TABLE_GROUP = "group_list";//话题
		public static final String TABLE_GROUP_CLASS = "groupClass";
		public static final String ALL_FRIEND_GROUP="all_friend_group";
		public static final int TYPE_RECENT = 1; // 最近联系人
		public static final String RECENT="recent";
		
		public static final int TYPE_ATTENTION_TO_ME = 2; // 关注我的
		public static final int TYPE_ATTENTION_FOR_ME = 3; // 我关注的
		public static final int TYPE_BLACKLIST = 4; // 黑名单
		public static final String REALNAME = "realname";
		public static final String DESCRIPTION = "description";
		public static final String ATTENTION_FOR = "attention_for";
		public static final String ATTENTION_TO = "attention_to";
		public static final String BLACKLIST = "blacklist";
		public static final String GROUPNAME = "groupName";
		public static final String ACCOUNT = "account";
		public static final String ID = "id";
		public static final String NAME = "name";
		
	}
	/***
	 * 数据库的操作
	 */
	public static class TableDB{
		public static final String CREATE_TABLE_IF_NOT_EXISTS="CREATE TABLE IF NOT EXISTS ";
	}
	
	/**
	 * 个人资料
	 * @author wt
	 */
	public static class Information{
		public static final String LOCUS = "locus";
		public static final String SEX = "sex";
		public static final String AGE = "age";
		public static final String DOCTORTITLE = "doctorTitle";
		public static final String DOCTORUNITADD = "doctorunitAdd";
		public static final String DOCTORUNIT = "doctorunit";
		public static final String DWELLINGPLACE = "dwellingplace";
	}
	
	
	public static class TableCity{
		public static final String NAME = "name";//城市名字
		public static final String CODE = "code";//城市编码
		
	}
	/**
	 * 4.2
	 */
	public static final String PERSONINFO_NAME ="ADR_CUS_INFO";
//	public static final String INFO_ID ="id";
//	public static final String INFO_ATTENTIONTYPE ="attentionType";
	//关系 0 陌生 1是关注的 2是黑名单 3是客户 4医生 5 我的合作者
	// 6 助理 7相互关注 8 小壹 9我关注的话题  10 我创建的话题 
	
//	public static final String INFO_LATELY ="lately";//是否是最近联系人  0 表示不是  1是
//	public static final String INFO_IN_GROUP ="is_group";//话题 1  人2 
//	public static final String INFO_HEADPATH ="headpath";//小头像路径
//	public static final String INFO_NAME ="name";//昵称
//	public static final String INFO_SEX ="sex";//性别
//	public static final String INFO_MESSAGECOUNT ="messageCount";//未读消息条数
//	public static final String INFO_ISDOCTOR ="is_doctor";//是否是医生(医生话题)
//	public static final String INFO_BIGPATH ="bigPath";//大头像路径
//	
//	
//	
//	
	public static String CHAT_MESSAGE_NAME ="ADR_CUS_MESSAGE";
//	public static String MESSAGE_ID ="message_id";
//	public static String MESSAGE_SENDER_ID ="sender_id";//发送者
//	public static String MESSAGE_RECEIVER_ID ="receiver_id";//接受者
//	public static String MESSAGE_CONTENT ="content";//消息内容
//	public static String MESSAGE_MESSAGE_STATUS ="message_status";//消息状态  1已读  未读0
//	public static String MESSAGE_CHATER_TYPE ="chater_type";//聊天着的类型 1人  0话题
//	public static String MESSAGE_MESSAGE_TIME ="message_time";//消息时间
//	public static String MESSAGE_MESSAGE_TYPE ="message_type";//消息类型
//	public static String MESSAGE_MESSAGEPATH ="messagePath";//消息附件路径(图片,语音)	
	
}
