package com.yksj.consultation.son.app;
import com.yksj.healthtalk.entity.BaseInfoEntity;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.entity.GroupInfoEntity;
import com.yksj.healthtalk.entity.MessageEntity;
import com.yksj.healthtalk.net.socket.SmartFoxClient;

import org.json.JSONObject;
import org.universalimageloader.core.download.ImageDownloader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
/**
 * 全局变量缓存
 * @author zhao
 */
public class AppData {
	
	public static final String DYHSID="3745";//导医护士的id,用来做全局判断    正式129718   测试3745
	public static final String DYHSID_NAME="导医护士";//导医护士的id,用来做全局判断    正式129718   测试3745
	//下面三个分别是1-全局搜索历史的key,2-话题搜索历史的key,3-医生搜索历史的key,4-搜索好友的key
	public static final String SEARCH_QUANJU_HISTORY="search_quanju_history";//全局搜索历史
	public static final String SEARCH_TOPIC_HISTORY="search_topic_history";//话题搜索历史
	public static final String SEARCH_INTERESTWALL_HISTORY="search_interestwall_history";//个人分享搜索历史
	public static final String SEARCH_FRIEND_HISTORY="search_friend_history";//找病友搜索历史
	public static final String SEARCH_MESSAGE_HISTORY="search_message_history";//消息直播搜索历史
	public static final String SEARCH_DRUG_HISTORY="search_drug_history";//药品按名称搜索历史
	public static final String SEARCH_MERCHANT_DOCTOR_HISTORY="search_merchant_doctor_history";//医疗机构按名称搜医生搜索历史
	public static final String KNOWLEDGEAD = "http://second-vision.mobi/JumpPage/LiuYi_QR_Code/LiuYi_QR_Code.jpg";
	public static final String PIC_TYPE = "10";//图片类型
	public static final String VIDEO_TYPE = "20";//视频类型
	public static final String THUMBNAIL_TYPE = "30";//视频缩略图类型
	public static final String SERVER_CALL = "400-0061120";//六一健康客服电话
	public static final String CERT_PASS = "111111";//https安全证书密码

	public static String ShareContent = "我在六一健康看到一条值得分享的内容:%1$s...更多内容查看：http://yijiankangv.mobi/SJ/download.html";
	/**
	 * 未读消息
	 */
	public final ConcurrentHashMap<String,List<MessageEntity>> messageCllection = new ConcurrentHashMap<String, List<MessageEntity>>();
	
	public final ConcurrentHashMap<String,MessageEntity> messageMap = new ConcurrentHashMap<String, MessageEntity>();//消息缓存
	public final ConcurrentHashMap<String,Object> cacheInformation = new ConcurrentHashMap<String,Object>();//用户或者群的资料信息
	private final ConcurrentHashMap<String,List<String>> typeListMap = new ConcurrentHashMap<String,List<String>>();//分类列表
	private final ConcurrentHashMap<String,MessageEntity> sendMesgeCache = new ConcurrentHashMap<String, MessageEntity>();//未发,发送失败消息缓存
	public void clearAll(){
		messageCllection.clear();
		cacheInformation.clear();
		typeListMap.clear();
		messageMap.clear();
		sendMesgeCache.clear();
		init();
	}
	
	public ConcurrentHashMap<String, MessageEntity> getSendMesgeCache() {
		return sendMesgeCache;
	}

	public void removeSendMesgeCache(MessageEntity entity){
		sendMesgeCache.put(entity.getId(),entity);
	}

	public MessageEntity getSendCacheMessageEntity(String id){
		return sendMesgeCache.get(id);
	}
	

	public int attentionUserSize;
	public int blackListSize;
	//底部消息切换时间
	public String messageShowTime ="5000";
	public AppData(){
		init();
	}
	
	private void init(){

		List interestGroupList = Collections.synchronizedList(new ArrayList());
		List createGroupList = Collections.synchronizedList(new ArrayList());//自己创建的话题
		List latelyFriendList = Collections.synchronizedList(new ArrayList());//最近联系
		List interestFriendList = Collections.synchronizedList(new ArrayList());//关注好友
		List customList = Collections.synchronizedList(new ArrayList());//客户
		List doctorList = Collections.synchronizedList(new ArrayList());//医生
//		List strangerLis1t = Collections.synchronizedList(new ArrayList());//陌生人
		List doctorLatelyList = Collections.synchronizedList(new ArrayList());//最近医生 
		List customLatelyList = Collections.synchronizedList(new ArrayList());//最近客户
		List myBoughtGroupIdList = Collections.synchronizedList(new ArrayList());//我购买的话题

		
		typeListMap.put("2",interestGroupList);//关注的群
		typeListMap.put("3",createGroupList);//自己创建的群
		typeListMap.put("4",latelyFriendList);//最近好友列表
		typeListMap.put("5",interestFriendList);//关注的好友
		typeListMap.put("6",customLatelyList);//最近联系的客户
		typeListMap.put("7",doctorLatelyList);//最近医生
		typeListMap.put("9", customList);
		typeListMap.put("10", doctorList);
		typeListMap.put("12", myBoughtGroupIdList);
		CustomerInfoEntity entity = new CustomerInfoEntity();
		entity.setId(SmartFoxClient.helperId);
		entity.setName("系统通知");
		entity.setSex("2");
		entity.setNormalHeadIcon(ImageDownloader.PROTOCOL_FILE_ASSETS + ":///customerIcons/launcher_logo.png");
		entity.setDescription("我是系统通知");
		cacheInformation.put(entity.getId(),entity);
	}
	
	public List<CustomerInfoEntity> getChatList(String tag){
		List<CustomerInfoEntity> infoEntities = new ArrayList<CustomerInfoEntity>();
		List<String> list = typeListMap.get(tag);
		for (String id : list) {
			if(cacheInformation.containsKey(id))infoEntities.add((CustomerInfoEntity)cacheInformation.get(id));
		}
		return infoEntities;
	}
	
	public List<String> getListByType(String type){
		synchronized (typeListMap) {
			return typeListMap.get(type);
		}
	}
	
	//我购买的话题
	public List<String> getMyBoughtGroupIdList(){
		return getListByType("12");
	}
	
	//我关注的salon
	public List<String> getInterestGroupIdList(){
		return getListByType("2");
	}
	
	//创建的salon
	public List<String> getCreatedGroupIdList(){
		return getListByType("3");
	}
	
	//最近联系人
	public List<String> getLatelyFriendIdList(){
		return getListByType("4");
	}
	
	//最近联系医生
	public List<String> getLatelyDoctordList(){
		return getListByType("7");
	}
	//最近联系的客户
	public List<String> getLatelyCustomList(){
			return getListByType("6");
	}
	
	//我关注的好友
	public List<String> getInterestFriendIdList(){
		return getListByType("5");
	}

	
	/**
	 * 获得客户id列表
	 * @return
	 */
	public List<String> getCustomerIdList(){
		return getListByType("9");
	}
	
	/**
	 * 获得客户id列表
	 * @return
	 */
	public List<String> getDoctorIdList(){
		synchronized (typeListMap) {
			return typeListMap.get("10");
		}
	}
	
	/**
	 * 关注好友列表
	 * @return
	 */
	public List<CustomerInfoEntity> getInterestFriendList(){
		final List<CustomerInfoEntity> listEntities = new ArrayList<CustomerInfoEntity>();
		List<String> list = getInterestFriendIdList();
		for (int i = 0; i < list.size(); i++) {
			String id = list.get(i);
			if(cacheInformation.containsKey(id)){
				listEntities.add((CustomerInfoEntity)cacheInformation.get(id));
			}
		}
		return listEntities;
	}
	


	
	/**
	 * 获取群资料
	 * @param id
	 * @return 没有资料返回null
	 */
	public GroupInfoEntity getGroupInfoEntity(String id){
		Object object = cacheInformation.get(id);
		if(object != null && object instanceof GroupInfoEntity){
			return (GroupInfoEntity)object;
		}
		return null;
	}
	
	/**
	 *好友资料 
	 * @param id
	 * @return
	 */
	public CustomerInfoEntity getCustomerInfoEntity(String id){
		Object object = cacheInformation.get(id);
		if(object instanceof CustomerInfoEntity){
			return (CustomerInfoEntity)object;
		}
		return null;
	}
	


	
	/**
	 * 获得客户列表
	 * @return
	 */
	public List<CustomerInfoEntity> getCustomerInfoList(){
		List<CustomerInfoEntity> infoEntities = new ArrayList<CustomerInfoEntity>();
		List<String> list = getCustomerIdList();
		for (String id : list) {
			if(cacheInformation.containsKey(id))infoEntities.add((CustomerInfoEntity)cacheInformation.get(id));
		}
		return infoEntities;
	}
	
	/**
	 * 获得关注的群
	 * @return
	 */
	public List<GroupInfoEntity> getInterestGroupInfoList(){
		List<GroupInfoEntity> infoEntities = new ArrayList<GroupInfoEntity>();
		List<String> list = getInterestGroupIdList();
		for (String id : list) {
			if(cacheInformation.containsKey(id))infoEntities.add((GroupInfoEntity)cacheInformation.get(id));
		}
		return infoEntities;
	}
	
	/**
	 * 更新资料
	 */
	public void updateCacheInfomation(BaseInfoEntity infoEntity){
		synchronized (cacheInformation) {
			cacheInformation.put(infoEntity.getId(),infoEntity);
		}
	}
	


	
	/**
	 * 
	 * 未读消息数量
	 * @param id
	 * @return
	 */
	public int getNoReadMesgSize(String id){
		int size = 0;
		if(messageCllection.containsKey(id)){
			List<MessageEntity> list = messageCllection.get(id);
			size = list.size();
		}
		return size;
	}


	
	
	/**
	 * 获得指定集合消息数量
	 * @param listMesgCount
	 * @return
	 */
	private int getMesgCount(List<String> listMesgCount) {
		int count = 0;
		for (String id : listMesgCount) {
			count += getNoReadMesgSize(id);
		}
		return count;
	}
	private final List<JSONObject> contents = new ArrayList<>();//服务端发起切换小壹  将内容发送

	public synchronized void saveToAutoContent(JSONObject content) {
		contents.add(content);
	}

	public synchronized String getToAutoContent() {
		if (contents.size() != 0) {
			return contents.get(0).optString("sms_req_content");
		} else {
			return "";
		}
	}
	public void clearToAutoContent() {
		contents.clear();
	}
	/**
	 * 未读消息数量
	 */
	public int getNoReadMesgSize2() {
		return messageNoRead.size();
	}
	public final List<MessageEntity> messageNoRead = new ArrayList<>();
}
