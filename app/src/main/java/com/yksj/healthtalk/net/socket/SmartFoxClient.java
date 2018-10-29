package com.yksj.healthtalk.net.socket;

import android.content.Context;

import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.entity.MessageEntity;
import com.yksj.healthtalk.utils.LogUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * SmartFoxClient用于发送所有的socket请求
 */
public class SmartFoxClient {
	private static final String TAG = "SmartFoxClient";
	public static final String EXTENTION_SMS_SYS_INITIALIZATION = "sms_sys_initialization";
	public static final String KEY_CODE = "server_code";
	public static final String KEY_PARAME = "server_parame";
	public static final String KEY_VALUE_GROUPID = "groupid";
	public static final String KEY_VALUE_INCEPTMESSAGE = "inceptMessage";
	public static final String KEYWORDS = "keyWords";
	public static final String KEY_VALUE_MESSAGE = "sms_req_content";
	public static final String KEY_CONTENT = "content";
	/**
	 * 多美助手id
	 */
	public static final String helperId = "100000";
	/**
	 * 多美医生
	 */
	public static final String doctorId = "-1";


	private static SmartControlClient smartFoxClient;
	private static SmartFoxClient mFoxClient;

	static {
		mFoxClient = new SmartFoxClient();
	}

	public SmartFoxClient() {
		smartFoxClient = SmartControlClient.getControlClient();

	}

	/**
	 * 设置smartfox基本信息
	 */
	private synchronized void initSmartFox() {
        /*mSmartFox = new SmartFox(LogUtil.DEBUG);
		addEventHandler(mEventListener);*/
	}

	public CustomerInfoEntity getCustomerInfoEntity() {
		return smartFoxClient.getInfoEntity();
	}

	public void setCustomerInfoEntity(CustomerInfoEntity customerInfoEntity) {
		smartFoxClient.setCustomerInfoEntity(customerInfoEntity);
//		this.customerInfoEntity = customerInfoEntity;
	}

	public String getUserMd5Id() {
		return smartFoxClient.getUserMd5Id();
	}

	public void setUserMd5Id(String userid) {
		//this.userMd5Id = MD5Utils.generateMD5Str(userid);
	}

	public String getUserId() {
		return smartFoxClient.getUserId();
	}

	public void setUserId(String userId) {
		//this.userId = userId;
	}


	public String getUserName() {
		if (smartFoxClient.getInfoEntity() != null) {
			return smartFoxClient.getInfoEntity().getUsername();
		} else {
			return "";
		}

	}


	public String getUserPas() {
		return smartFoxClient.getPassword();
	}


	public String getIp() {
		return "";
	}


	public void setIp(String ip) {
//		this.ip = ip;
	}

	public void sendJoinRoom() {
		//mSmartFox.send(new JoinRoomRequest(mSmartFox.getRoomByName(ROOM_NAME)));
	}

	public static SmartFoxClient getSmartFoxClient() {
		return mFoxClient;
	}


	/**
	 * 发送请求
	 *
	 * @param code 请求码
	 * @param str  json参数
	 */
	public static void sendStrRequest(int code, String str) {
		SocketParams SocketParams = new SocketParams();
		switch (code) {
			case SmartFoxClient.RequestCode.COLLECT_GROUP:
			case SmartFoxClient.RequestCode.COLLECT_GROUP_NOT:
				SocketParams.putUtfString("validMark", "40");
				break;
		}
		SocketParams.putInt(KEY_CODE, code);
		if (str != null) SocketParams.putUtfString(KEY_PARAME, str);
		smartFoxClient.doSendExtentionReq(SocketParams);
	}

	/**
	 * 发送请求
	 *
	 * @param code 请求码
	 * @param str  json参数
	 */
	public static void sendStrRequest(int code, String str, String content) {
		SocketParams SocketParams = new SocketParams();
		SocketParams.putInt(KEY_CODE, code);
		if (str != null) {
			SocketParams.putUtfString(KEY_PARAME, str);
			SocketParams.putUtfString("COMPLAINT_CONTENT", content);
		}
		smartFoxClient.doSendExtentionReq(SocketParams);
	}

	/**
	 * 发送保持连接请求
	 */
	public static void sendKeepConnect() {
		SocketParams Params = new SocketParams();
		Params.putInt(KEY_CODE, RequestCode.KEEP_CONECTION_REQ);
		smartFoxClient.doSendExtentionReq(Params);
	}

	/**
	 * 改变当前为登出状态
	 */
	public synchronized static void changeLogOut() {
		//isLogined = false;
	}

	private synchronized void doSendExtentionReq(SocketParams SocketParams) {
		/*SocketParams.putUtfString(SYSTEM_VERSION,osVersion);
		SocketParams.putUtfString(OS_TYPE,osType);
		doSend(new ExtensionRequest(EXTENTION_SMS_SYS_INITIALIZATION, SocketParams,mSmartFox.getLastJoinedRoom()));*/
	}


	/**
	 * 注销
	 *
	 * @param str
	 */
	public static void ifServerParame(String str) {
		sendStrRequest(RequestCode.SERVER_PARAME, str);
	}

	/**
	 * 获取在线成员
	 *
	 * @param str groupid
	 */
	public static void getChattingOnlineFriends(String str) {
		sendStrRequest(RequestCode.CHATTING_ONLINE_FRIEND, str);
	}


	/**
	 * 通讯录好友能找到我
	 *
	 * @param str
	 */
	public static void ifCanFindMe(String str) {
		sendStrRequest(RequestCode.CODE_FIND_ME, str);
	}


	/**
	 * 加入聊天室
	 */
	public static void sendJoinGroupChat(String id) {
		sendStrRequest(RequestCode.JOIN_GROUP_CHAT, id);
	}


	/**
	 * 获得登陆者的id
	 *
	 * @return
	 */
	public static String getLoginUserId() {
		if (LoginServiceManeger.instance().getLoginEntity() != null) {
			return LoginServiceManeger.instance().getLoginEntity().getId();
		} else {
			return "";
		}

	}

	/**
	 * 登陆用户信息
	 *
	 * @return
	 */
	public static CustomerInfoEntity getLoginUserInfo() {
		return smartFoxClient.getInfoEntity();
	}


	/**
	 * 举报
	 *
	 * @param str
	 */
	public static void report(String str, String content) {
		sendStrRequest(RequestCode.CODE_REPORT, str, content);
	}


	/**
	 * @param @param groupid
	 * @param @param inceptMessage
	 * @return void
	 * @throws
	 * @Title: sendIsInceptMessage
	 * @Description: 是否接受群消息
	 */
	public static void sendIsInceptMessage(String groupid, String inceptMessage) {
		sendIsInceptMessage(RequestCode.CODE_INCEPTMESSAGE, groupid, inceptMessage);
	}

	/**
	 * @param @param code
	 * @param @param groupid
	 * @param @param inceptMessage
	 * @return void
	 * @throws
	 * @Title: sendIsInceptMessage
	 * @Description: 是否接受群消息
	 */
	public static void sendIsInceptMessage(int code, String groupid, String inceptMessage) {
		SocketParams SocketParams = new SocketParams();
		SocketParams.putInt(KEY_CODE, code);
		if (groupid != null) SocketParams.putUtfString(KEY_VALUE_GROUPID, groupid);
		if (inceptMessage != null)
			SocketParams.putUtfString(KEY_VALUE_INCEPTMESSAGE, inceptMessage);
		smartFoxClient.doSendExtentionReq(SocketParams);
	}

	/**
	 * 发送保持连接请求
	 */
	public static void sendKeepConnectReq() {
		if (smartFoxClient != null) {
			SocketParams Params = new SocketParams();
			Params.putInt(KEY_CODE, RequestCode.KEEP_CONECTION_REQ);
			smartFoxClient.doSendExtentionReq(Params);
			LogUtil.d(TAG, "smartfox keepconnect sended");
		}
	}

	/**
	 * 发送聊天信息
	 * 同下
	 */
	public static void sendChatMessage(String consulId, String mesgId, String tagertId, boolean isGroupMessage, int type, float duration, String content, String voiceLength, String fileName, String dataHolder) {
		SocketParams SocketParams = new SocketParams();
		if (isGroupMessage) {
			SocketParams.putInt(KEY_CODE, 7045);
			SocketParams.putInt("isBL", 7045);
		} else {
			if (SmartFoxClient.helperId.equals(mesgId)) {
				SocketParams.putInt(KEY_CODE, SmartFoxClient.RequestCode.CHAT_HEALPER);
			} else {
				SocketParams.putInt(KEY_CODE, 7018);
			}
		}
		SocketParams.putUtfString("serverId", mesgId);//消息返回状态id,与前台id进行对应
		SocketParams.putUtfString("sms_req_content", content);
		SocketParams.putUtfString("customerId", smartFoxClient.getUserId());
		SocketParams.putInt("isGroupMessage", isGroupMessage ? 1 : 0);
		SocketParams.putUtfString("timeStamp", String.valueOf(System.currentTimeMillis()));
		SocketParams.putUtfString("sms_target_id", tagertId);
		if (consulId != null)
			SocketParams.putUtfString("consultation_id", consulId);//会诊id
		else
			SocketParams.putUtfString("consultation_id", "");//会诊id
		if (fileName != null) SocketParams.putUtfString("fileName", fileName);
		if (dataHolder != null) SocketParams.putUtfString("dataHolder", dataHolder);//地图坐标
		SocketParams.putInt("type", type);
		SocketParams.putUtfString("duration", duration + "");
		smartFoxClient.doSendExtentionReq(SocketParams);
	}


	/**
	 * 同上
	 *
	 * @param entity
	 * @param type
	 */
	public static void sendChatMessage(MessageEntity entity, int type,String objectType, int duration) {
		String mesgId = entity.getId();
		String consulId = entity.getConsultationId();
		String tagertId = entity.getReceiverId();//接受者
//		boolean  isGroupMessage =entity.getGroupType()>0;
		String content;
		String dataHolder = null;
		if (type == MessageEntity.TYPE_LOCATION) {
			content = entity.getAddress();
			dataHolder = entity.getContent();
		} else {
			content = entity.getContent();
			dataHolder = null;
		}

		SocketParams params = new SocketParams();
		if (entity.getGroupType() == 1) {
			params.putInt(KEY_CODE, 7075);
			params.putUtfString("isBL", entity.getIsBL());
		} else if (entity.getGroupType() == 2) {
			params.putInt(KEY_CODE, 7075);
			params.putUtfString("allCustomerId", entity.getAllCustomerId());
			params.putUtfString("isBL", entity.getIsBL());
		} else if (entity.getGroupType() == 3) {//购买服务
			params.putInt(KEY_CODE, SmartControlClient.SERVICE_SINGLE_SEND_MSG);
		} else {
			if (SmartFoxClient.helperId.equals(tagertId)) {
				params.putInt(KEY_CODE, SmartFoxClient.RequestCode.CHAT_HEALPER);
			} else {
				params.putInt(KEY_CODE, 7018);
			}
		}
		params.putUtfString("serverId", mesgId);//消息返回状态id,与前台id进行对应
		params.putUtfString("sms_req_content", content);
		params.putUtfString("customerId", smartFoxClient.getUserId());
		params.putInt("isGroupMessage", entity.getGroupType());
		params.putUtfString("timeStamp", String.valueOf(System.currentTimeMillis()));
		params.putUtfString("sms_target_id", tagertId);
		params.putUtfString("Object_Type", objectType);//会诊标记
		params.put("client_type", HTalkApplication.CLIENT_TYPE);
		if (consulId != null)
			params.putUtfString("consultation_id", consulId);//会诊id
		else
			params.putUtfString("consultation_id", "0");//会诊id
//		if(fileName != null)SocketParams.putUtfString("fileName", fileName);
		if (dataHolder != null) params.putUtfString("dataHolder", dataHolder);//地图坐标
		params.putInt("type", type);
		params.putUtfString("duration", String.valueOf(duration));
		params.putUtfString("isDoctorMessage", entity.getIsDoctorMessage());
		params.putUtfString("order_id", entity.getOrderId());
		int code = 0;
		if (entity.getGroupType() == 1) {//六一班群聊
			code = SmartControlClient.SIX_ONE_SEND_MSG;
		} else if (entity.getGroupType() == 3) {//特殊服务单聊
			code = SmartControlClient.SERVICE_SINGLE_SEND_MSG;
		} else {
			code = SmartControlClient.SOCKET_SINGLE_CHAT;
		}
		SocketManager.sendSocketParams(params, code);
//		smartFoxClient.doSendExtentionReq(params);
	}

	/**
	 * 推送健康贴士
	 *
	 * @param str
	 */
	public static void ifPushAttention(String str) {
		sendStrRequest(RequestCode.CODE_PUSH_ATTENTION, str);
	}

	/**
	 * 兴趣墙图片转发
	 * <p>
	 * flag 放入smartfox内的标志位 话题2,好友1
	 *
	 * @param ids
	 */
	public static void sendRepeatToFriend(String flag, String ids, String picId) {
		SocketParams params = new SocketParams();
		params.putInt(KEY_CODE, RequestCode.CODE_REPEAT_MESSAGE);
		params.putUtfString("sms_verifyFlag", flag);
		params.putUtfString("session", "");
		params.putUtfString("sms_stringid", ids);
		params.putUtfString("wall_pictureid", picId);
		smartFoxClient.doSendExtentionReq(params);
	}

	/**
	 * 兴趣墙 转发 (我的所有(沙龙 朋友))
	 * MYATTENTION 我关注的人
	 * ATTENTIONME 关注我的人
	 * MYCREATE 我创建的群
	 * MYATTENTIONGROUP 我关注的群
	 */
	public static void sendRepeatToAll(String flag, String picId) {
		SocketParams params = new SocketParams();
		params.putUtfString("flag", flag);
		params.putInt(KEY_CODE, RequestCode.CODE_REPEAT_PIC);
		params.putUtfString("session", "");
		params.putUtfString("wall_pictureid", picId);
		smartFoxClient.doSendExtentionReq(params);
	}


	/**
	 * 查询禁言列表
	 */
	public static void queryForbiddenWordsList(String chatId) {
		sendStrRequest(SmartFoxClient.RequestCode.CODE_FORBIDWORDS_LIST, chatId);
	}

	/**
	 * 更新禁言列表
	 */
	public static void updateForbiddenWordsList(String groupId, Collection<String> collection) {
		SocketParams params = new SocketParams();
		params.putUtfString("groupid", groupId);
		JSONArray array = new JSONArray();
		params.putUtfStringArray(KEY_PARAME, collection);
		params.putInt(KEY_CODE, SmartFoxClient.RequestCode.CODE_UPDATE_FORBIDWORDS_LIST);
		smartFoxClient.doSendExtentionReq(params);
	}

	/**
	 * 服务编码
	 *
	 * @author zhao yuan
	 */
	public static class RequestCode {
		public static final int KEEP_CONECTION_REQ = 100;// 保持连接定时检测
		// 小贴士
		public static final int CODE_REPORT = 9100; // 举报

		public static final int CODE_REPEAT_MESSAGE = 9133;//转发
		/**
		 * 聊天
		 */
		public static final int CHATTING_INVITE_RECEIVE = 9088; // 接收群邀请消息
		public static final int CHATTING_SINGLE_MESSAGE_RECEIVE = 9019;// 接受单聊消息
		public static final int CHATTING_GROUP_MESSAGE_RECEIVE = 9045;// 群聊接收
		public static final int DIALOG_CREATE_REQ = 9098;// 虚拟医生聊天关联话题创建话题
		public static final int CHATTING_ONLINE_FRIEND = 9142;//在线成员

		/**
		 * 健康友
		 */

		public static final int CODE_COLLECT_FRIEND = 9024; // 1收藏或0删除好友,2加入黑名单,3移除黑名单
		public static final int CODE_PUSH_ATTENTION = 9118;// 推送健康贴士
		public static final int SERVER_PARAME = 9125;// 注销
		public static final int CODE_INCEPTMESSAGE = 9120; // 是否接受群消息
		// 9025要改
		public static final int CODE_FIND_ME = 9025; // 通讯录好友能否找到我

		/**
		 * 个人资料
		 */
		public static final int CODE_CHANGE_PERSONALINFO = 9026; // 修改个人信息
		public static final int CODE_SEND_FACEBACK = 9079; // 意见反馈

		/**
		 * 聊天室
		 */
		public static final int COLLECT_GROUP = 9073; // 收藏聊天室
		public static final int COLLECT_GROUP_NOT = 9074; // 取消收藏聊天室
		public static final int JOIN_GROUP_CHAT = 9090;// 加入聊天室
		public static final int EXIT_GROUP_CHAT = 9091;// 退出聊天室
		public static final int GROUP_INVITE = 9149; // 群邀请
		public static final int GROUP_ONLINE = 9047;// 当前群的在线用户
		public static final int DOCTOR_SEND_PERSON_MESSAGE = 9137;//医生向用户群发
		public static final int DOCTOR_INFO_AUDIT = 9108; // 医师资格审核
		public static final int OFFLINE_DELE = 9104; // 删除离线消息

		/**
		 * 账号异地登录
		 */
		public static final int CHAT_HEALPER = 9112;// 多美妹子聊天
		public static final int NOTIFY_MESSAGE = 9114;// 通知
		public static final int CODE_UPDATE_FORBIDWORDS_LIST = 9126;// 禁言列表更新
		public static final int CODE_FORBIDWORDS_LIST = 9129;// 禁言列表查询


		public static final int CODE_CHARGE_STATE = 9145;//医生收费请求发送状态返回

		public static final int CODE_PRODUCT_CHANGE = 10008;//订单变化通知
		public static final int CODE_LEAVE_WORDS = 120;//留言

		public static final int CODE_REPEAT_PIC = 9134;  //转发 (兴趣墙 全选的)

	}


	/**
	 * 加载当前群在线的成员列表
	 *
	 * @param groupId 群id
	 */
	public static boolean loadChatGroupOnlineMenbers(String groupId) {
		SocketParams SocketParams = new SocketParams();
		SocketParams.putInt(KEY_CODE, SmartFoxClient.RequestCode.GROUP_ONLINE);
		SocketParams.putUtfString(KEY_PARAME, groupId);
		smartFoxClient.doSendExtentionReq(SocketParams);
		return true;
	}

	/**
	 * 医生向患者群发消息
	 *
	 * @return
	 */
	public static void doctorSendMessageToPersons(String message, String id) {
		SocketParams SocketParams = new SocketParams();
		SocketParams.putUtfString("PATIENT_GROUP_ID", id);
		SocketParams.putUtfString("VALID_MARK", "40");
		SocketParams.putUtfString("client_type", HTalkApplication.CLIENT_TYPE);
		SocketParams.putInt(KEY_CODE, SmartFoxClient.RequestCode.DOCTOR_SEND_PERSON_MESSAGE);
		SocketParams.putUtfString(KEY_VALUE_MESSAGE, message);
		SocketParams.putUtfString("session", "");
		smartFoxClient.doSendExtentionReq(SocketParams);
	}

	/**
	 * 群邀请
	 *
	 * @param groupId 群ID
	 */
	public static boolean sendGroupInvite(Context context, String groupId, List<String> inviteId) {
		SocketParams params = new SocketParams();
		params.putInt(KEY_CODE, SmartFoxClient.RequestCode.GROUP_INVITE);
		String content = "";
		for (int i = 0; i < inviteId.size(); i++) {
			content = content + inviteId.get(i) + "&";
		}
		content = content.trim().substring(0, content.trim().length() - 1);
		params.putUtfString("sms_req_content", content);
		params.putUtfString("sms_target_id", groupId);
		params.putUtfString("customerId", SmartFoxClient.getLoginUserId());
		params.putUtfString("groupid", groupId);
		params.putInt("type", MessageEntity.TYPE_INVITATION);
		smartFoxClient.doSendExtentionReq(params);
		return true;
	}

	/**
	 * 发送参数为数组类型请求
	 */
	public static void sendArrayRequest(int code, Collection<String> codes) {
		SocketParams params = new SocketParams();
		params.putInt(KEY_CODE, code);
		params.putUtfStringArray(KEY_PARAME, codes);
		smartFoxClient.doSendExtentionReq(params);
	}

	/**
	 * 发送聊天收费
	 *
	 * @param time
	 * @param price
	 * @param chatId
	 * @param type   医生发0    客户发1
	 */
	public static void sendDoctorCharge(String time, String price, String chatId, String orid, int type) {
		SocketParams params = new SocketParams();
		params.putInt(KEY_CODE, RequestCode.CODE_CHARGE_STATE);
		params.putUtfString("money", price);
		params.putUtfString("time", time);
		params.putInt("type", type); //医生发0    客户发1
		if (orid != null) params.putUtfString("orid", orid);
		params.putUtfString("sms_target_id", chatId);
		smartFoxClient.doSendExtentionReq(params);
	}

	// 删除离线消息
	public static void deleteHistroyMessage(List<MessageEntity> list) {
		if (list == null || list.size() == 0) return;
		List<String> idList = new ArrayList<String>();
		for (MessageEntity messageEntity : list) {
			final String serverid = messageEntity.getServerId();
			if (serverid != null) idList.add(serverid);
		}
		sendArrayRequest(SmartFoxClient.RequestCode.OFFLINE_DELE, idList);
	}

	public static void sendLogoutGroup(String grouId) {
		SocketParams params = new SocketParams();
		params.putInt(KEY_CODE, RequestCode.EXIT_GROUP_CHAT);
		params.putUtfString(KEY_PARAME, grouId);
		smartFoxClient.doSendExtentionReq(params);
	}
}
