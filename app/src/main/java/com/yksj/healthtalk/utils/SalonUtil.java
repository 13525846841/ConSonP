package com.yksj.healthtalk.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.widget.ToggleButton;

import com.yksj.healthtalk.entity.GroupInfoEntity;
import com.yksj.healthtalk.net.socket.SmartFoxClient;

/**
* @ClassName: SalonUtil 
* @Description: 
* @author wt
* @date 2013-1-16 上午03:34:11
 */
public class SalonUtil {
	
	/**
	* @Title: sendIsInceptMessage
	* @Description: 发送是否推送消息
	* @param     
	* @return boolean   返回现在推送消息按钮的状态
	* @throws
	 */
	public static boolean  sendIsInceptMessage(String groupId ,ToggleButton btn){
		//判断是否没创建就退出该话题
//		if (groupId != null && !"".equals(groupId)) {
//			if (is_message_setting_toggleBtn_state_change == message_setting_toggleBtn.isChecked()) {
//				group.setInceptMessage(message_setting_toggleBtn.isChecked());
//			}else {
					SmartFoxClient.sendIsInceptMessage(groupId,btn.isChecked()? "Y" : "N");
//					group.setInceptMessage(message_setting_toggleBtn.isChecked());
					//状态码重新赋值
				return btn.isChecked();
				}
//			}
//	}
	
	/**
	 * 改变关注的状态
	 */
	public static void changeAttentionState(){
		
	}
	
//	{"CODE":"-1","MESSAGE":"需要购买门票","TICKET":[{"GROUP_ID":32528,"TICKET_TYPE":1,
//"CHARGING_STANDARDS":100,"CREATE_TIME":"20130509182124","NOTE":null,
//	"DEFINE_TYPE":2},{"GROUP_ID":32528,"TICKET_TYPE":2,"CHARGING_STANDARDS":500,"CREATE_TIME":"20130509182124","NOTE":null,"DEFINE_TYPE":2}],"GROUPMESSAGE":{"biggb":"assets/group/groupIcon.png","chargingFlag":"0","cilentbg":"assets/group/groupIcon.png","classID":"","className":"","createCustomerID":"27446","createTime":"20130509182106","customerid":"26285","flag":"","flagPlacing":"","groupClass":"2","groupHeadPortraitID":"9","groupHeadPortraitName":"3.0版本使用统一默认头像","groupId":"32528","groupLevel":"1","groupState":"","groupVersion":"1","groupflag":"2","inceptMessage":"N","infoLayName":"失恋之痛","infoLayid":"10000406","larglayid":"","limitNnum":"","merchantId":"","note":"","onlineNum":"人","openDate":"20130509182106","personNum":"1人","publicCustInfo":"Y","recordDesc":"xx","recordName":"xx","releaseSystemMessage":"1","showPersonnum":true,
//	"sourceId":"","upperLayName":"","upperLayid":"","yesornoShow":false}}
	
	/**
	 * 需要购买门票返回群资料信息
	 * @param str
	 * @return
	 */
	public static GroupInfoEntity jsonToObject(String str){
		 try {
			JSONObject jsonObject = new JSONObject(str);
			 String r = jsonObject.getString("GROUPMESSAGE");
			 GroupInfoEntity entity =  SalonHttpUtil.jsonAnalysisSalonEntity(r).get(0);
			 return entity;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
//	/**
//	 * 购买成功后获取价格+
//	 * @param str json数据
//	 * {"code":10003,"time":"20130510111431-20130511111431","groupName":"Sdk","price":1}
//	 * @throws Exception 
//	 */
//	public static  int  getTicketPrice(String str) throws Exception{
//		JSONObject object =  new JSONObject(str);
//		int price = object.getInt("price");
//		return price;
//	}
	
}
