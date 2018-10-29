package com.yksj.healthtalk.entity;

import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.net.socket.SmartFoxClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 最近联系人  实体
 * @author jack_tang
 *
 */
/**
 * 最近联系人  实体
 * @author jack_tang
 *
 */
public class MsgLaterEntity implements Serializable{
    private String headerPath;//头像路径
    private String nickName;//昵称
    private String content;//消息内容
    private String msgTime;//消息时间
    private String msgCount;//消息条数  -1表示已经看过
    private String sendId;//发送方id
    private String isGroup ;//0 多美助理  1是群消息   2 个人消息
    private String sex ;
    private String msgId;//消息id
    private String isBL;//病历讨论标记
	private String objectType;

	public String getConsultId() {
		return consultId;
	}

	public void setConsultId(String consultId) {
		this.consultId = consultId;
	}

	private String consultId;//会诊id
	private String groupId;//群聊id

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getIsBL() {
		return isBL;
	}

	public void setIsBL(String isBL) {
		this.isBL = isBL;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(String isGroup) {
		this.isGroup = isGroup;
	}

	public String getHeaderPath() {
		return headerPath;
	}
	
	public String getSendId() {
		return sendId;
	}
	

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setSendId(String sendId) {
		this.sendId = sendId;
	}

	public void setHeaderPath(String headerPath) {
		this.headerPath = headerPath;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMsgTime() {
		return msgTime;
	}
	public void setMsgTime(String msgTime) {
		this.msgTime = msgTime;
	}
	public String getMsgCount() {
		return msgCount;
	}
	public void setMsgCount(String msgCount) {
		this.msgCount = msgCount;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getObjectType() {
		return objectType;
	}

	public static List<MsgLaterEntity> parseToList(JSONArray array){
		List<MsgLaterEntity> entities =new ArrayList<MsgLaterEntity>();
		MsgLaterEntity entity = null;
		for (int i = 0; i < array.length(); i++) {
			JSONObject item = array.optJSONObject(i);
			entity =new MsgLaterEntity();
			if(LoginServiceManeger.instance().getLoginUserId().equals(item.optString("TARGET_ID")))
				entity.setSendId(item.optString("SEND_ID"));
			else
				entity.setSendId(item.optString("TARGET_ID"));
			
			if(SmartFoxClient.helperId.equals(item.optString("SEND_ID"))||SmartFoxClient.helperId.equals(item.optString("TARGET_ID"))){
				entity.setNickName("系统通知");
				entity.setIsGroup("0");
			}else if("1".equals(item.optString("ISGROUPMESSAGE"))){
				entity.setNickName(item.optString("TARGET_NAME"));
				entity.setIsGroup("1");
			}else{
				entity.setNickName(item.optString("TARGET_NAME"));
				entity.setIsGroup("2");
				entity.setSex(item.optString("TARGET_SEX"));
			}
			entity.setMsgTime(item.optString("SEND_TIME"));
			entity.setConsultId("" + item.optInt("OBJECT_ID"));
			entity.setGroupId("" + item.optString("GROUP_ID"));
			entity.setObjectType(item.optString("OBJECT_TYPE"));
			entity.setIsBL(item.optString("ISMEDICAL_REC"));
			entity.setMsgCount(item.optString("NUMS"));
//			entity.setHeaderPath(item.optString("NUMS"));
			switch (item.optInt("MESSAGE_TYPE")) {
			case 1:
				entity.setContent("[语音]");
				break;
			case 2:
				entity.setContent("[图片]");
				break;
			case 10:
				entity.setContent("[坐标]");
				break;
			default:
				entity.setContent(item.optString("MESSAGE_CONTENT"));
				break;
			}
			entity.setMsgId(item.optString("OFFLINE_ID"));
			entities.add(entity);
		}
		return entities;
	}
}
