package com.yksj.healthtalk.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.yksj.consultation.son.dossier.CaseItemEntity;
import com.yksj.healthtalk.db.ChatUserHelper;
import com.yksj.healthtalk.db.Tables;
import com.yksj.healthtalk.entity.CommendEntity;
import com.yksj.healthtalk.entity.InterestImageUserReleaseEntity;
import com.yksj.healthtalk.entity.InterestWallEntity;
import com.yksj.healthtalk.entity.InterestWallImageEntity;
import com.yksj.healthtalk.entity.InterestWallImageEntity.Constant;
import com.yksj.healthtalk.entity.MyDoctorListEntity;
import com.yksj.healthtalk.entity.MyZoneGroupEntity;
import com.yksj.healthtalk.entity.MyZoneIconEntity;
import com.yksj.healthtalk.entity.SalonInComeEntity;
import com.yksj.healthtalk.entity.SalonIncomeDetailEntity;
import com.yksj.healthtalk.entity.ShopImageEntitiy;
import com.yksj.healthtalk.entity.ShopListItemEntity;
import com.yksj.consultation.son.app.AppData;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.healthtalk.sortlistview.CharacterParser;

public class JsonParseUtils {
	/**
	 * 解析 兴趣墙图片详细信息json
	 * 
	 * @param content
	 * @return
	 */
	public static InterestWallImageEntity parseInterestImageContentData(
			String content) {
		InterestWallImageEntity entity = new InterestWallImageEntity();
		try {
			JSONObject mJsonObject = new JSONObject(content);
			entity.setId(mJsonObject.getString(Constant.ID));
			entity.setClickCount(mJsonObject.getString(Constant.CLICKCOUNT));
			entity.setCollectionCount(mJsonObject.getString(Constant.COLLECTIONCOUNT));
			entity.setCommentCount(mJsonObject.getString(Constant.COMMENTCOUNT));
			entity.setCustomerid(mJsonObject.getString(Constant.CUSTOMERID));
			entity.setGroupClass(mJsonObject.getString(Constant.GROUPCLASS));
			entity.setGroupId(mJsonObject.getString(Constant.GROUPID));
			entity.setGroupLevel(mJsonObject.getString(Constant.GROUPLEVEL));
			entity.setImagepath(mJsonObject.getString(Constant.IMAGEPATH));
			entity.setPicDesc(mJsonObject.getString(Constant.PICDESC));
			entity.setPicName(mJsonObject.getString(Constant.PICNAME));
			entity.setPicUrl(mJsonObject.getString(Constant.PICURL));
			entity.setPublicCustomerInfo(mJsonObject
					.getString(Constant.PUBLICCUSTOMERINFO));
			entity.setRecordDesc(mJsonObject.getString(Constant.RECORDDESC));
			entity.setRecordName(mJsonObject.getString(Constant.RECORDNAME));
			entity.setReleaseSystemMessage(mJsonObject
					.getString(Constant.RELEASESYSTEMMESSAGE));
			entity.setShareCount(mJsonObject.getString(Constant.SHARECOUNT));
			entity.setStateTime(mJsonObject.getString(Constant.STATETIME));
			entity.setTransmitCount(mJsonObject
					.getString(Constant.TRANSMITCOUNT));
			entity.setUploadTime(mJsonObject.getString(Constant.UPLOADTIME));
			entity.setVerifyState(mJsonObject.getString(Constant.VERIFYSTATE));
			entity.setCustomerNickname(mJsonObject
					.getString(Constant.CUSTOMERNICKNAME));
			entity.setCustomerHeadPic(mJsonObject
					.getString("CLIENT_ICON_BACKGROUND"));
			entity.setShop_place(mJsonObject.getString("SHOP_PLACING"));
			entity.setClassId(mJsonObject.getString("CLASS_ID"));
			entity.setInfoLayId(mJsonObject.optString("Info_Lay_ID"));
			entity.setCUSTOMER_SEX(mJsonObject.getString("CUSTOMER_SEX"));
			entity.setINFORMATION_LAY(mJsonObject.optString("INFORMATION_LAY"));
			
			entity.setLIKENUM(mJsonObject.optString("LIKENUM"));
			entity.setISLIKE(mJsonObject.optString("ISLIKE"));
			entity.setDISLIKENUM(mJsonObject.optString("DISLIKENUM"));
			entity.setISDISLIKE(mJsonObject.optString("ISDISLIKE"));
			entity.setIS_COLLECTION(mJsonObject.optString("IS_COLLECTION"));
			return entity;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析我发布我收藏json
	 * 
	 * @param json
	 * @return
	 */
	public static List<InterestWallEntity> parseInterestUserEntity(String json,
			float mWidthSize) {
		List<InterestWallEntity> result = new ArrayList<InterestWallEntity>();
		try {
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {
				InterestWallEntity entity = new InterestWallEntity();
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				entity.setId(jsonObject.optString("pictureID"));
				entity.setImageState(jsonObject.optString("verifystate"));
				entity.setImageUrl(jsonObject.optString("iconPicturePath"));
				entity.setPictureUrl(jsonObject.optString("pictureUrl"));
//				float scale = mWidthSize / entity.getPicWidth();
//				entity.setPicWidth((int) (entity.getPicWidth() * scale));
//				entity.setPicHeight((int) (entity.getPicHeight() * scale));
				
				entity.setCustomerNickname(jsonObject.optString("customerNickname"));
				entity.setClientIconBackground(jsonObject.optString("clientIconBackground"));
				entity.setTime(jsonObject.optString("time"));
				entity.setPictureName(jsonObject.optString("pictureName"));
				entity.setBigPicturePath(jsonObject.optString("bigPicturePath"));
				entity.setBigPictureWidthPx(jsonObject.optString("bigPictureWidthPx"));
				entity.setBigPictureHeightPx(jsonObject.optString("bigPictureHeightPx"));
				entity.setCUSTOMER_SEX(jsonObject.optString("customerSex"));
				entity.setIsCollection(jsonObject.optInt("isCollection"));
				entity.setRoleId(jsonObject.optString("roleId"));
				result.add(entity);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 兴趣墙 用户发布图片信息
	 * 
	 * @param content
	 * @return
	 */
	public static List<Object> parseInterestImageUserReleaseEntity(
			String content) {
		List<Object> data = new ArrayList<Object>();
		try {
			JSONArray jsonArray = new JSONArray(content);
			for (int i = 0; i < jsonArray.length(); i++) {
				InterestImageUserReleaseEntity entity = new InterestImageUserReleaseEntity();
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				entity.setCollectionCount(jsonObject
						.getString(InterestImageUserReleaseEntity.Constant.COLLECTION_COUNT));
				entity.setCommentCount(jsonObject
						.getString(InterestImageUserReleaseEntity.Constant.COMMENT_COUNT));
				entity.setForwardCount(jsonObject
						.getString(InterestImageUserReleaseEntity.Constant.FORWARD_COUNT));
				entity.setGroupClass(jsonObject
						.getString(InterestImageUserReleaseEntity.Constant.GROUPCLASS));
				entity.setGroupDesc(jsonObject
						.getString(InterestImageUserReleaseEntity.Constant.GROUPDESC));
				entity.setGroupId(jsonObject
						.getString(InterestImageUserReleaseEntity.Constant.GROUP_ID));
				entity.setGroupLevel(jsonObject
						.getString(InterestImageUserReleaseEntity.Constant.GROUPLEVEL));
				entity.setGroupName(jsonObject
						.getString(InterestImageUserReleaseEntity.Constant.GROUPNAME));
				entity.setIconUrl(jsonObject
						.getString(InterestImageUserReleaseEntity.Constant.ICONPICTUREPATH));
				entity.setImageurl(jsonObject
						.getString(InterestImageUserReleaseEntity.Constant.BIGPICTUREPATH));
				entity.setInfolayId(jsonObject
						.getString(InterestImageUserReleaseEntity.Constant.INFOLAYID));
				entity.setPicDesc(jsonObject
						.getString(InterestImageUserReleaseEntity.Constant.PUCTUREDESC));
				entity.setPicId(jsonObject
						.getString(InterestImageUserReleaseEntity.Constant.PICTUREID));
				entity.setPicName(jsonObject
						.getString(InterestImageUserReleaseEntity.Constant.PUCTURENAME));
				entity.setPicSize(jsonObject
						.getString(InterestImageUserReleaseEntity.Constant.PUCTURESIZE));
				entity.setPicUrl(jsonObject
						.getString(InterestImageUserReleaseEntity.Constant.PICTURE_URL));
				entity.setShareCount(jsonObject
						.getString(InterestImageUserReleaseEntity.Constant.SHARE_COUNT));
				entity.setUploadTime(jsonObject
						.getString(InterestImageUserReleaseEntity.Constant.UPLOADTIME));
				entity.setVerifystate(jsonObject
						.getString(InterestImageUserReleaseEntity.Constant.VERIFYSTATE));

				data.add(entity);
			}
			return data;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 最近三条评论解析
	 * 
	 * @param content
	 * @return
	 */
	public static Map<String, Object> onCommendJsonParse(String content) {
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		try {
			JSONArray jsonArray = new JSONArray(content);
			JSONObject jsonObject = jsonArray
					.getJSONObject(jsonArray.length() - 1);
			data.put("count", jsonObject.getString("COUNT"));
			JSONArray array = jsonObject.getJSONArray("LIST");
			for (int i = 0; i < array.length(); i++) {
				CommendEntity entity = new CommendEntity();
				JSONObject temp = array.getJSONObject(i);
				entity.setUserid(temp.optString("CUSTOMER_ID", ""));
				entity.setCustomerNickname(temp
						.optString(CommendEntity.Constant.CUSTOMER_NICKNAME));
				if ("2".equals(temp.optString("MESSAGE_TYPE"))) {
					entity.setMessageContent("【图片】");
				} else if ("1".equals(temp.optString("MESSAGE_TYPE"))) {
					entity.setMessageContent("【语音】");
				} else {
					entity.setMessageContent(temp.optString(CommendEntity.Constant.MESSAGE_CONTENT));
				}
				entity.setSendTime(temp.optString(CommendEntity.Constant.SEND_TIME));
				entity.setSex(temp.optString("SEX"));
				entity.setIconUrl(temp.optString(CommendEntity.Constant.ICON_URL));
				data.put("item" + i, entity);
			}
			return data;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 家园图标更改的json数据格式
	 */
	public static List<MyZoneIconEntity> parseMyZoneIconData(String json) {
		List<MyZoneIconEntity> data = new ArrayList<MyZoneIconEntity>();
		try {
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {
				MyZoneIconEntity entity = new MyZoneIconEntity();
				JSONObject tempJson = jsonArray.getJSONObject(i);
				entity.setId(tempJson.getString("FUNCTION_ID"));
				entity.setName(tempJson.getString("FUNCTION_NAME"));
				entity.setSequence(tempJson.getString("FUNCTION_SEQ"));
				entity.setType(tempJson.getString("FUNCTION_TYPE"));
				data.add(entity);
			}
			Collections.sort(data);
			return data;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<MyZoneGroupEntity> parseGroupData(String json) {
		List<MyZoneGroupEntity> result = new ArrayList<MyZoneGroupEntity>();
		try {
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {

				MyZoneGroupEntity entity = new MyZoneGroupEntity();
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				entity.setClassId(jsonObject.getString("CLASS_ID"));
				entity.setClassName(jsonObject.getString("CLASS_NAME"));
				entity.setShopId(jsonObject.getString("SHOP_ID"));
				if (jsonObject.has("ALL_OPEN"))
					entity.setAppOpen(jsonObject.getInt("ALL_OPEN"));
				else
					entity.setAppOpen(2);
				result.add(entity);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 解析各种馆的数据 1 ：摄影馆 2：画廊 3：收藏
	 * 
	 * @param data
	 * @return
	 */
	public static List<ShopImageEntitiy> parseShopData(JSONArray jsonArray) {
		List<ShopImageEntitiy> result = new ArrayList<ShopImageEntitiy>();
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject tempJson = jsonArray.getJSONObject(i);
				result.add(parseShopSimpleData(tempJson));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static ShopImageEntitiy parseShopSimpleData(JSONObject jsonObject) {
		ShopImageEntitiy entitiy = new ShopImageEntitiy();
		try {
			entitiy.setShopId(jsonObject.getString("SHOP_ID"));
			entitiy.setAddInterest(jsonObject.getString("ADD_INTERESTS"));
			entitiy.setClassId(jsonObject.getString("CLASS_ID"));
			entitiy.setIconImageAddress(jsonObject
					.getString("ICON_PICTURE_ADDR"));
			entitiy.setImageAddress(jsonObject.getString("PICTURE_ADDR"));
			entitiy.setInfoLayId(jsonObject.getString("INFO_LAY_ID"));
			entitiy.setPicDesc(jsonObject.getString("PICTURE_DESC"));
			entitiy.setPicId(jsonObject.getString("PICTURE_ID"));
			entitiy.setPicTitle(jsonObject.getString("PICTURE_TITLE"));
			entitiy.setIconHeight(jsonObject.getInt("ICON_HEIGHT_PX"));
			entitiy.setIconWidth(jsonObject.getInt("ICON_WIDTH_PX"));
			entitiy.setPicHeight(jsonObject.getInt("PICTURE_HEIGHT_PX"));
			entitiy.setPicWidth(jsonObject.getInt("PICTURE_WIDTH_PX"));
			entitiy.setPicSize(jsonObject.getInt("PICTURE_SIZE"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return entitiy;
	}

	public static ShopImageEntitiy parseShopSimpleData(String jsonObject) {
		ShopImageEntitiy entitiy = new ShopImageEntitiy();
		try {
			JSONObject mJsonObject = new JSONObject(jsonObject);
			entitiy.setShopId(mJsonObject.getString("SHOP_ID"));
			entitiy.setAddInterest(mJsonObject.getString("ADD_INTERESTS"));
			entitiy.setClassId(mJsonObject.getString("CLASS_ID"));
			entitiy.setIconImageAddress(mJsonObject
					.getString("ICON_PICTURE_ADDR"));
			entitiy.setImageAddress(mJsonObject.getString("PICTURE_ADDR"));
			entitiy.setInfoLayId(mJsonObject.getString("INFO_LAY_ID"));
			entitiy.setPicDesc(mJsonObject.getString("PICTURE_DESC"));
			entitiy.setPicId(mJsonObject.getString("PICTURE_ID"));
			entitiy.setPicTitle(mJsonObject.getString("PICTURE_TITLE"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return entitiy;
	}

	public static ShopImageEntitiy parseShopData(String jsonObject) {
		ShopImageEntitiy entitiy = new ShopImageEntitiy();
		try {
			JSONArray mJsonArray = new JSONArray(jsonObject);
			JSONObject mJsonObject = mJsonArray.getJSONObject(0);
			entitiy.setShopId(mJsonObject.getString("SHOP_ID"));
			entitiy.setAddInterest(mJsonObject.getString("ADD_INTERESTS"));
			entitiy.setClassId(mJsonObject.getString("CLASS_ID"));
			entitiy.setIconImageAddress(mJsonObject
					.getString("ICON_PICTURE_ADDR"));
			entitiy.setImageAddress(mJsonObject.getString("PICTURE_ADDR"));
			entitiy.setInfoLayId(mJsonObject.getString("INFO_LAY_ID"));
			entitiy.setPicDesc(mJsonObject.getString("PICTURE_DESC"));
			entitiy.setPicId(mJsonObject.getString("PICTURE_ID"));
			entitiy.setPicTitle(mJsonObject.getString("PICTURE_TITLE"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return entitiy;
	}

	/**
	 * 解析我的沙龙和我的社交登陆初始化的时候
	 * 
	 * @param context
	 * @param str
	 * @param data
	 */
	@SuppressWarnings("unchecked")
	public static void loginInitSalon(Context context, String str) {
		try {
			com.alibaba.fastjson.JSONObject object = JSON.parseObject(str);
			AppData appData = HTalkApplication.getAppData();
			// 话题 解析并保存数据到数据库 
			SalonHttpUtil.jsonAnalysisSalonEntity(context, object
					.getString("myFocusGroup").toString());
			
			
			// 我关注的好友保存数据库
			FriendHttpUtil.jsonAnalysisFriendEntity(
					context,object.get("myFocus").toString(),
					Tables.TableFriend.TYPE_ATTENTION_FOR_ME);
			// 黑名单的数量
			appData.blackListSize = object.getIntValue("myBlacksCount");
			// 关注我的好友数量
			appData.attentionUserSize = object.getIntValue("focusMeCount");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 获取收入统计
	 * 
	 * @param str
	 * @return
	 */
	public static void getSalonInCome(String str,
			ArrayList<SalonInComeEntity> salonInComeEntities) {
		try {
			JSONObject o = new JSONObject(str);
			JSONArray array = (JSONArray) o.get("incomeList");
			for (int i = 0; i < array.length(); i++) {
				ArrayList<SalonInComeEntity> entities = new ArrayList<SalonInComeEntity>();
				JSONObject object = (JSONObject) array.get(i);
				SalonInComeEntity entity = new SalonInComeEntity();
				entity.setDate(object.getString("date"));
				if (object.has("timeStamp") || salonInComeEntities.size() == 0) {
					entity.setTimeStamp("1");
				} else {
					entity.setTimeStamp("");
				}
				JSONArray a2 = (JSONArray) object.get("income");
				for (int j = 0; j < a2.length(); j++) {
					SalonInComeEntity e = new SalonInComeEntity();
					JSONObject object1 = (JSONObject) a2.get(j);
					e.setChangeNum(object1.getString("changeNum"));
					e.setCustomerId(object1.getString("customerId"));
					e.setGroupId(object1.getString("groupId"));
					e.setInfo(object1.getString("info"));
					entities.add(e);
				}
				entity.setEntities(entities);
				salonInComeEntities.add(entity);
			}

		} catch (JSONException e) {
			System.err.println(e.toString());
		}
	}

	/**
	 * 获取沙龙详情
	 * 
	 * @param str
	 * @return
	 */
	public static ArrayList<SalonIncomeDetailEntity> getSalonIncomeDetail(
			String str) {
		ArrayList<SalonIncomeDetailEntity> entities = new ArrayList<SalonIncomeDetailEntity>();
		try {
			JSONArray array = new JSONArray(str);
			for (int i = 0; i < array.length(); i++) {
				SalonIncomeDetailEntity entity = new SalonIncomeDetailEntity();
				JSONObject object = (JSONObject) array.get(i);
				entity.setChangeNum(object.getString("changeNum"));
				entity.setChangeTime(object.getString("changeTime"));
				entity.setTypeDetail(object.getString("typeDetail"));
				entities.add(entity);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entities;
	}

	/**
	 * 解析离线消息
	 * @param content
	 */
	public static void parseOffMessage(String content) {
		try {
			JSONObject object = new JSONObject(content);
			JSONArray jsonArray = object.getJSONArray("messages");
			ChatUserHelper.getInstance().initMessage(jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 我的医生列表
	 * 
	 * @param str
	 * @param entity
	 * @return
	 */
	public static ArrayList<MyDoctorListEntity> getMyDoctorListData(String str) {
		ArrayList<MyDoctorListEntity> entities = new ArrayList<MyDoctorListEntity>();
		try {
			com.alibaba.fastjson.JSONArray jsonArr = JSON.parseArray(str);
			for (int i = 0; i < jsonArr.size(); i++) {
				MyDoctorListEntity entit = new MyDoctorListEntity();
				com.alibaba.fastjson.JSONObject object = jsonArr.getJSONObject(i);
				entit.setDoctorList(object.getJSONObject("doctorList"));
				entit.setIndex(object.getIntValue("index"));
				entit.setName(object.getString("name"));
				entit.setId(object.getIntValue("id"));
				entities.add(entit);
			}
		} catch (Exception e) {
		}
		Collections.sort(entities, new java.util.Comparator<MyDoctorListEntity>() {

			@Override
			public int compare(MyDoctorListEntity object1, MyDoctorListEntity object2) {
				if (object1.getIndex() > object2.getIndex()) {
					return 1;
				}else {
					return -1;
				}
				
			}
		});
		return entities;
	}

	/**
	 * 我的医生列表头像和数目
	 * 
	 * @param str
	 * @param entity
	 * @return
	 */
	public static ArrayList<HashMap<String, String>> getMyNineListData(
			String str) {
		ArrayList<HashMap<String, String>> entities = new ArrayList<HashMap<String, String>>();
		try {
			com.alibaba.fastjson.JSONArray jsonArr = JSON.parseArray(str);
			for (int i = 0; i < jsonArr.size(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				entities.add(reflect(jsonArr.getJSONObject(i)));
			}
		} catch (Exception e) {
		}
		return entities;
	}

	public static HashMap<String, String> reflect(com.alibaba.fastjson.JSONObject json) {
		HashMap<String, String> map = new HashMap<String, String>();
		Set<?> keys = json.keySet();
		for (Object key : keys) {
			String o = (String) json.get(key);
			map.put((String) key, o);
		}
		return map;
	}

	/**
	 * 解析购物车
	 * 
	 * @param str
	 * @param entity
	 * @return
	 */
	public static List<ShopListItemEntity> getGsonToTrolleyList(String str) {
		try {
			return JSON.parseArray(str, ShopListItemEntity.class);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * 过滤数据返回
	 * 
	 * @param content
	 * @return
	 */
	public static Object jsonfilter(String content) {
		Object object = null;
		try {
			object = JSON.parse(content);
			if(object instanceof com.alibaba.fastjson.JSONArray){
				return FriendHttpUtil.jsonAnalysisFriendEntity(content, false);
			}else if(object instanceof com.alibaba.fastjson.JSONObject){
				return ((JSONObject)object).optString("error_message", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 过滤数据返回
	 * 
	 * @param content
	 * @return
	 */
	public static String filterErrorMessage(String content) {
		try {
			com.alibaba.fastjson.JSONObject object = JSON.parseObject(content);
			return object.getString("message");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static String filterErrorMessage(com.alibaba.fastjson.JSONObject content) {
		try {
			return content.getString("error_message");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 用于动态模板的json数据解析
	 * @param srr  json字符串
	 * @return
	 */
	public static ArrayList<Map<String, String>> parseTemplateItemData(String srr) {
		ArrayList<Map<String, String>> list=new ArrayList<Map<String,String>>();
		try {
			JSONArray array=new JSONArray(srr);
			for(int i=0;i<array.length();i++){
				JSONObject object=array.getJSONObject(i);
				HashMap< String, String> map=new HashMap<String, String>();
				map.put("name", object.optString("OPTIONNAME"));
				map.put("code", ""+object.optInt("OPTIONID"));
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 用于动态模板的json数据解析
	 * 包含多选是否被选中
	 * @param srr  json字符串
	 * @return
	 */
	public static ArrayList<Map<String, Object>> parseMultipleChoiseData(String srr) {
		ArrayList<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		try {
			JSONArray array=new JSONArray(srr);
			for(int i=0;i<array.length();i++){
				JSONObject object=array.getJSONObject(i);
				HashMap< String, Object> map=new HashMap<String, Object>();
				if(object.has("OPTIONNAME")){
					map.put("name", object.optString("OPTIONNAME"));
					map.put("code", ""+object.optInt("OPTIONID"));
				}else{
					map.put("name", object.optString("NAME"));
					map.put("code", ""+object.optInt("CODE"));
				}
				map.put("isChecked", false);
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 用于动态模板的json数据解析封装到Post的JSONObject
	 * @param srr  json字符串
	 * @return
	 */
	public static JSONObject getPostTemplateObject(JSONObject object) {
		JSONObject obj=new JSONObject();
		try {
			obj.put("CLASSID", object.optInt("CLASSID"));
			obj.put("ITEMID", object.optInt("ITEMID"));
			obj.put("NEFILL", object.optInt("NEFILL"));
			obj.put("ITEMTYPE", object.optInt("ITEMTYPE"));
			obj.put("INFO", "");
			obj.put("INFO2", "");
			obj.put("SPIC", object.optInt("SPIC"));
			obj.put("SEQ", object.optInt("SEQ"));
			obj.put("SELECTION", "");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * JsonArray
	 * @return 获取病历项的数据list
	 */
	public static ArrayList<CaseItemEntity> getCaseItemDatas(CharacterParser characterParser,JSONArray array) {
		ArrayList<CaseItemEntity> data=new ArrayList<CaseItemEntity>();

		try {
			for (int i=0;i<array.length();i++){
				CaseItemEntity entity=new CaseItemEntity();
				JSONObject object=array.getJSONObject(i);
				entity.ITEMNAME=object.optString("ITEMNAME");
				entity.ITEMID=object.optInt("ITEMID");
				entity.ITEMTYPE=object.optInt("ITEMTYPE");
				entity.SPIC=object.optInt("SPIC");
				entity.CLASSID=object.optInt("CLASSID");
				entity.CLASSNAME=object.optString("CLASSNAME");
				entity.ITEMDESC=object.optString("ITEMDESC");
				entity.NEFILL=object.optInt("NEFILL");
				entity.PICNUM=object.optInt("PICNUM");
				entity.NUMSTART=object.optInt("NUMSTART");
				entity.NUMEND=object.optInt("NUMEND");
				entity.SEQ=object.optInt("SEQ");
				entity.OPTION=object.optString("OPTION");
				//汉字转换成拼音
				String pinyin = characterParser.getSelling(object.optString("ITEMNAME"));
				String sortString = pinyin.substring(0, 1).toUpperCase();
				// 正则表达式，判断首字母是否是英文字母
				if(sortString.matches("[A-Z]")){
					entity.setSortLetters(sortString.toUpperCase());
				}else{
					entity.setSortLetters("#");
				}
				data.add(entity);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return data;
	}
	

}
