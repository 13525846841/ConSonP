package com.yksj.healthtalk.services;
/**
 * 
 * 后台服务处理请求
 * @author zhao
 * 
 */
public class MessagePushService{
	public static final String ACTION_REPEAT_STATE = "com.yksj.consultation.ui.ACTION_REPEAT_STATE";//兴趣墙转发状态返回
	public static final String ACTION_DOCTOR_CHARGE_STATE = "com.yksj.consultation.ui.ACTION_DOCTOR_CHARGE_STATE";//聊天中医生收费请求状态返回
	public static final String ACTION_FORBIDDENLIST = "com.yksj.healthtalk.services.ForbiddenlistAction";// 禁言列表
	public static final String ACTION_USERINFO_LOADCOMPLETE = "com.yksj.ui.ACTION_USERINFO_LOADCOMPLETE";// 加载用户个人资料完成
	public static final String ACTION_GETPERSONAL_INFO = "com.yksj.ui.ACTION_GETPERSONAL_INFO";// 加载客户信息完成
	public static final String ACTION_OFFLINE_MESSAGE = "com.yksj.ui.ACTION_OFFLINE_MESSAGE";//离线消息加载完成通知
	public static final String ACTION_STARTMUSIC_BACKGROUND = "com.yksj.ui.ACTION_STARTMUSIC_BACKGROUND";
	public static final String ACTION_COLLECT_FRIEND = "com.yksj.ui.FriendInfo";//处理与客户之间的关系
	public static final String ACTION_LOGIN = "com.yksj.healthtalk.services.LoginAction";// 登陆
	public static final String ACTION_COLLECT_GROUP = "com.yksj.ui.ACTION_COLLECT_GROUP";
	public static final String ACTION_JOIN_GROUP = "com.yksj.healthtalk.ACTION_JOIN_GROUP";// 加入聊天室通知
	public static final String ACTION_DOCTOR_INFO_AUDIT = "com.yksj.ui.ACTION_DOCTOR_INFO_AUDIT"; // 医师资料审核
	public static final String ACTION_CONNECTION_LOST = "com.yksj.ui.ACTION_CONNECTION_LOST"; // 连接丢失
	public static final String ACTION_COLLECT_GROUP_NOT = "com.yksj.ui.ACTION_COLLECT_GROUP_NOT";
	public static final String ACTION_FRIENDLIST = "com.yksj.ui.friendList";
	public static final String ACTION_MESSAGE_JOINROOM = "com.yksj.healthtalk.services.MessageaJoinRoomAction";// 加入房间通知
	public static final String ACTION_GROUP_ONLINE = "com.yksj.ui.ACTION_GROUP_ONLINE"; // 群在线成员
	public static final String ACTION_SEARCH_FRIEND_CON = "com.yksj.ui.FriendSearchCon";
	public static final String ACTION_GROUP_INVITE = "com.yksj.ui.ACTION_GROUP_INVITE"; // 群邀请
	public static final String ACTION_EXIT = "com.yksj.ui.EXIT";// 注销
	public static final String ACTION_GROUPLIST = "com.yksj.ui.groupList";
	public static final String ACTION_MODIFY_PERSONIF = "com.yksj.Health.PersonIn";// 修改个人资料
	public static final String ACTION_MESSAGE = "com.yksj.healthtalk.services.MessageaAction";// 新消息通知
	public static final String ACTION_PAY_MESSAGE = "com.yksj.healthtalk.services.PayMessageaAction";// 支付
	public static final String ACTION_LEAVE_WORD = "com.yksj.healthtalk.services.leaveWordsAction";//离线留言
	public static  final String ACTION_CHATTINGONLINEFRIEND = "com.yksj.ui.online.friend";//沙龙在线成员
	
	public static final String PARAME_KEY = "parame"; // 广播传值key
	public static final String BROAD_KEY = "result"; // 广播传值key

}	