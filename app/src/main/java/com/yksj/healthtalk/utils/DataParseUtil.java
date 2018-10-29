package com.yksj.healthtalk.utils;

import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.entity.GroupInfoEntity;
import com.yksj.healthtalk.entity.InterestWallEntity;
import com.yksj.healthtalk.entity.MessageEntity;
import com.yksj.healthtalk.entity.TagEntity;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.sortlistview.CharacterParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于数据的解析封装
 *
 * @author zhao
 */
public class DataParseUtil {
    final static DecimalFormat mDecimalFormat = new DecimalFormat("0.0");

    /**
     * @param
     */
    public static List<MessageEntity> parseGroupMessage(String value,
                                                        String userId) {
        List<MessageEntity> list = new ArrayList<MessageEntity>();
        try {
            if (value == null || "".equals(value)) return list;
            JSONTokener jsonTokener = new JSONTokener(value);
            Object object = jsonTokener.nextValue();
            if (object == null) return list;
            JSONArray jsonArray = (JSONArray) (object);
            int i = jsonArray.length();
            if (i == 0) return list;
            for (; i > 0; ) {
                --i;
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                MessageEntity messageEntity = jsonObjToMessgeEntity(jsonObject, userId);
                list.add(messageEntity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * @param
     */
    public static List<MessageEntity> parseToGroupMessage(String value,
                                                          String userId) {
        List<MessageEntity> list = new ArrayList<MessageEntity>();
        try {
            if (value == null || "".equals(value)) return list;
            JSONTokener jsonTokener = new JSONTokener(value);
            Object object = jsonTokener.nextValue();
            if (object == null) return list;
            JSONArray jsonArray = (JSONArray) (object);
            int i = jsonArray.length();
            if (i == 0) return list;
            for (int j = 0; j < i; j++) {
                JSONObject jsonObject = jsonArray.getJSONObject(j);
                MessageEntity msg = jsonToListMesg(jsonObject, userId);
                list.add(msg);
            }
//			for (; i > 0;) {
//				--i;
//				JSONObject jsonObject = jsonArray.getJSONObject(i);
//				MessageEntity messageEntity = jsonToListMesg(jsonObject,userId);
//				list.add(messageEntity);
//			}
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * jsonobject解析为MessageEntity实体
     *
     * @param jsonObject
     * @param userid     登陆者id
     * @return
     */
    public static MessageEntity jsonObjToMessgeEntity(JSONObject jsonObject,
                                                      String userid) {
        NumberFormat format = NumberFormat.getInstance();
        MessageEntity messageEntity = new MessageEntity();
        try {
            int type = jsonObject.optInt("type");
            String customerId = jsonObject.optString("customerId");
            String serverId = jsonObject.optString("serverId");
//			String timeStamp = jsonObject.optString("timeStamp");
            String targetId = jsonObject.optString("targetCustomerId");
            boolean isSendFlag = userid.equals(customerId);// jsonObject.getBoolean("self");
            String str = jsonObject.optString("timeStamp");
            String isWeChat = "";
            if (jsonObject.has("isWechat")) {
                isWeChat = jsonObject.optString("isWechat");
            }
            try {
                long time = format.parse(str).longValue();
                messageEntity.setDate(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
//			messageEntity.setDate(TimeUtil.getChatTime(jsonObject.optString("timeStamp")));
            messageEntity.setSenderId(customerId);
            messageEntity.setReceiverId(targetId);
            messageEntity.setId(serverId);
            messageEntity.setServerId(serverId);
            messageEntity.setType(type);
            messageEntity.setSendFlag(isSendFlag);
            messageEntity.setSendState(MessageEntity.STATE_OK);
            messageEntity.setIsWeChat(isWeChat);
            if (MessageEntity.TYPE_VOICE == type) {
                double duration = jsonObject.getDouble("duration");
                String content = jsonObject.optString("content");
                messageEntity.setVoiceLength(mDecimalFormat.format(duration));
                messageEntity.setContent(content);
            } else if (MessageEntity.TYPE_PICTURE == type) {
                String imagePath = jsonObject.optString("dataHolder");
                messageEntity.setContent(imagePath);
            } else if (MessageEntity.TYPE_VIDEO == type) {
                String imagePath = jsonObject.optString("dataHolder");
                messageEntity.setContent(imagePath);
            } else if (MessageEntity.TYPE_LOCATION == type) {
                String address = jsonObject.optString("content");
                String location = jsonObject.optString("dataHolder");
                messageEntity.setContent(location);
                messageEntity.setAddress(address);
                //文本
            } else {
                JSONArray array = jsonObject.optJSONArray("keyWords");
                String content = jsonObject.optString("content");
                messageEntity.setType(MessageEntity.TYPE_TEXT);
                messageEntity.setContentJsonArray(array);
                messageEntity.setContent(content);
            }
            return messageEntity;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return messageEntity;
    }

    /**
     * 求医问药中的内容解析
     *
     * @return
     */
    private static MessageEntity jsonToListMesg(JSONObject jsonObject,
                                                String userid) {
        NumberFormat format = NumberFormat.getInstance();
        MessageEntity messageEntity = new MessageEntity();
        try {
            int type = jsonObject.optInt("TYPE");
            String customerId = jsonObject.optString("CUSTOMERID");
            String serverId = jsonObject.optString("SERVERID");
            //			String timeStamp = jsonObject.optString("timeStamp");
            String targetId = jsonObject.optString("TARGETCUSTOMERID");
            boolean isSendFlag = userid.equals(customerId);// jsonObject.getBoolean("self");
            String str = jsonObject.optString("TIMESTAMP");
            try {
                long time = format.parse(str).longValue();
                messageEntity.setDate(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //			messageEntity.setDate(TimeUtil.getChatTime(jsonObject.optString("timeStamp")));
//				messageEntity.setGroup("888".equals(jsonObject.optString("IS_SECURITY")) || "777".equals(jsonObject.optString("IS_SECURITY")));
            messageEntity.setSenderId(customerId);
            messageEntity.setReceiverId(targetId);
            messageEntity.setId(serverId);
            messageEntity.setType(type);
            messageEntity.setSendFlag(isSendFlag);
            messageEntity.setSendState(MessageEntity.STATE_OK);
            if (MessageEntity.TYPE_VOICE == type) {
                double duration = jsonObject.getDouble("DURATION");
                String content = jsonObject.optString("CONTENT");
                messageEntity.setVoiceLength(mDecimalFormat.format(duration));
                messageEntity.setContent(content);
            } else if (MessageEntity.TYPE_PICTURE == type) {
                String imagePath = jsonObject.optString("DATAHOLDER");
                messageEntity.setContent(imagePath);
            } else if (MessageEntity.TYPE_LOCATION == type) {
                String address = jsonObject.optString("CONTENT");
                String location = jsonObject.optString("DATAHOLDER");
                messageEntity.setContent(location);
                messageEntity.setAddress(address);
                //文本
            } else {
                JSONArray array = jsonObject.optJSONArray("KEYWORDS");
                String content = jsonObject.optString("CONTENT");
                messageEntity.setType(MessageEntity.TYPE_TEXT);
                messageEntity.setContentJsonArray(array);
                messageEntity.setContent(content);
            }
            return messageEntity;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return messageEntity;


    }

    /**
     * 群资料
     *
     * @param object
     * @return
     */
    public static GroupInfoEntity jsonToGroupInfoEntity(JSONObject object) {
        GroupInfoEntity mSalonEntity = new GroupInfoEntity();
        // 头像
        mSalonEntity.setBigHeadIcon(object.optString("biggb"));
        mSalonEntity
                .setCharge(object.optString("chargingFlag").equals("1") ? true
                        : false);// 是否收费
        mSalonEntity.setNormalHeadIcon(object.optString("cilentbg"));
        object.optString("classID");
        object.optString("className");
        mSalonEntity.setCreateCustomerID(object
                .optString("createCustomerID"));
        mSalonEntity.setCreateTime(object.optString("createTime"));
        mSalonEntity.setFlag(object.optString("flag"));
        // 是否是医师沙龙
        mSalonEntity
                .setSalon(object.optString("groupClass").equals("1") ? true
                        : false);
        mSalonEntity.setId(object.optString("groupId"));// 群组ID
        mSalonEntity.setGroupLevel(object.optString("groupLevel", "0"));// 沙龙等级(主治医师、副主任医师、主任医师
        // 创建的话题默认为2级，其他默认为0级)
        object.optString("groupState");
        object.optString("groupVersion");
        mSalonEntity.setSalonAttention((object.optString("groupflag")
                .equals("1")) ? true : false);// 标记（1为收藏，0为未收藏）
        mSalonEntity.setLimitNumber(object.optString("limitNnum"));// 上限人数
        mSalonEntity.setMerchantId(object.optString("merchantId"));
        mSalonEntity.setOnLineNumber(object.optString("onlineNum"));// 在线人数
        object.optString("openDate");
        mSalonEntity.setPersonNumber(object.optString("personNum"));// 此聊天室内当前有多少人
        mSalonEntity.setRecordDesc(object.optString("recordDesc"));// 群组主题
        mSalonEntity.setName(object.optString("recordName"));// 名称
        mSalonEntity.setIsReleaseSystemMessage(object.optString(
                "releaseSystemMessage").equals("1") ? true : false);// 是否将消息发布到消息厅(1-发布，0-不发布)
        mSalonEntity
                .setShowPersonNumber(object.optBoolean("showPersonnum", false));// 是否显示此聊天室内当前有多少人
        mSalonEntity.setYesornoShow(object.optBoolean("yesornoShow", false));// 是否显示在线人数
        if (object.optInt("hasExtensionInfomaiton") != 0) {
            mSalonEntity.setGroupHeadPortraitID(object
                    .optString("groupHeadPortraitID"));// 群组头像ID
            mSalonEntity.setGroupHeadPortraitName(object
                    .optString("groupHeadPortraitName"));// 群组头像名称
            mSalonEntity.setInceptMessage(object.optString("inceptMessage")
                    .equals("Y") ? true : false);
            mSalonEntity.setInfoId(object.optString("infoLayid"));// 小类信息层面id
            mSalonEntity.setInfoLayName(object.optString("infoLayName"));// 信息层面名称
            mSalonEntity.setUpperId(object.optString("larglayid"));// 大类id
            mSalonEntity.setCusMessag("");
            mSalonEntity.setPublicCustInfo(object.optString(
                    "publicCustInfo").equals("Y") ? true : false);// 是否公开创建者信息(Y/N,默认为Y)
            mSalonEntity.setName(object.optString("recordName"));// 群组名称
            mSalonEntity.setNote(object.optString("note"));// 签名
        }
        return mSalonEntity;
    }

    public static CustomerInfoEntity JsonTocuStomUpdate(JSONObject object,
                                                        CustomerInfoEntity mCustomerInfoEntity) {
        if (mCustomerInfoEntity == null)
            return mCustomerInfoEntity;
        try {
            mCustomerInfoEntity.setBackGold(object.optInt("backGold"));
            mCustomerInfoEntity.setBigHeadIcon(object
                    .optString("bigIconBackground"));
            mCustomerInfoEntity.setNormalHeadIcon(object
                    .optString("clientIconBackground"));
            mCustomerInfoEntity.setUsername(object
                    .optString("customerAccounts"));
            mCustomerInfoEntity.setRemarkName(object
                    .optString("remarksName", ""));
            mCustomerInfoEntity.setMoney(Integer.valueOf(object.optString(
                    "customerGold").equals("") ? "0" : object
                    .optString("customerGold")));
            mCustomerInfoEntity.setCustomerGroupRel(object
                    .optInt("customerGroupRel"));
            mCustomerInfoEntity.setId(object.optString("customerId"));
            mCustomerInfoEntity.setName(object.optString("customerNickname"));
            mCustomerInfoEntity.setSex(object.optString("customerSex"));
            mCustomerInfoEntity.setDistance(object.getDouble("distance"));
            mCustomerInfoEntity.setFlag(object.optString("flag"));
            mCustomerInfoEntity.setInfoVersion(object.optString("infoVersion"));
            mCustomerInfoEntity.setDescription(object
                    .optString("personalNarrate"));
            mCustomerInfoEntity.setIsAttentionFriend(object
                    .optInt("relationType"));// 关系 0 无关系 1 我关注的 2 黑名单 3是客户 4医生 5合作者
            mCustomerInfoEntity.setRoldid(object.optInt("roleId"));
            mCustomerInfoEntity.setServicePrice(object
                    .optString("servicePrice"));
            mCustomerInfoEntity.setServiceStatus(object
                    .optString("serviceStatus"));
            mCustomerInfoEntity
                    .setServiceTime(object.optString("serviceStart"));
            mCustomerInfoEntity.setServiceStatusCode(object
                    .optString("serviceStatusCode"));
            mCustomerInfoEntity.setDwellingplace(object
                    .optString("dwellingPlace"));
            mCustomerInfoEntity.setTicketFlag(object.optString("ticketFlag"));
            mCustomerInfoEntity.setDoctorWorkaddress(object.optString("workLocationName"));
            mCustomerInfoEntity.setDoctorWorkaddressCode(object.optString("workLocation"));
            if (object.optInt("hasExtensionInfomaiton") != 0) {
                mCustomerInfoEntity.setJson(object.optString("albums"));
                mCustomerInfoEntity.setBirthday(object.optString("birthday"));
                mCustomerInfoEntity.setSameExperience(object
                        .optString("bodyList"));
                mCustomerInfoEntity.setCultureLevel(object
                        .optString("cultureLevel"));
                mCustomerInfoEntity.setCustomerlocus(object
                        .optString("customerLocus"));
                mCustomerInfoEntity.setDoctorEmail(object
                        .optString("doctorEmail"));
                mCustomerInfoEntity.setHospital(object
                        .optString("doctorHospital"));
                mCustomerInfoEntity.setMobilePhone(object
                        .optString("doctorMobilePhone"));

                mCustomerInfoEntity.setOfficeCode1(object.optString("doctorOffice"));
                mCustomerInfoEntity.setRealname(object
                        .optString("doctorRealName"));
                mCustomerInfoEntity.setDoctorClientPicture(object
                        .optString("doctorClientBackground"));
                mCustomerInfoEntity.setDoctorBigPicture(object
                        .optString("doctorBigIconbackground"));
                mCustomerInfoEntity.setSpecial(object
                        .optString("doctorSpecially"));
                mCustomerInfoEntity.setTelePhone(object
                        .optString("doctorTelephone"));
                mCustomerInfoEntity.setDoctorTitle(object
                        .optString("doctorTitle"));
                mCustomerInfoEntity.setErrorCode(object.optInt("errorCode"));
                mCustomerInfoEntity.setGameLevel(object.optString("gameLevel"));
                mCustomerInfoEntity.setLableJson(object
                        .optString("interestsList"));
                mCustomerInfoEntity.setLatitude(object.optString("latitude"));
                mCustomerInfoEntity.setLongitude(object.optString("longitude"));
                mCustomerInfoEntity.setHunyin(object.optString("marryFlag"));
                mCustomerInfoEntity.setMetier(object.optString("metier"));
                mCustomerInfoEntity.setOnlineState(object
                        .optString("onlineState"));// 502770-在线，502780-离开
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mCustomerInfoEntity;
    }

    /**
     * 将json转换为个人资料对象
     *
     * @param object
     * @return
     * @throws JSONException
     */
    public static CustomerInfoEntity jsonToCustmerInfo(JSONObject object) {
        CustomerInfoEntity mCustomerInfoEntity = new CustomerInfoEntity();
        mCustomerInfoEntity.setBackGold(object.optInt("backGold"));
        if (object.has("phoneNum")) {
            mCustomerInfoEntity.setPoneNumber(object.optString("phoneNum"));
        } else {
            mCustomerInfoEntity.setPoneNumber(object.optString("phone"));
        }
        mCustomerInfoEntity.setPhonefriendName(object.optString("phonefriendName"));
        mCustomerInfoEntity.setBigHeadIcon(object
                .optString("bigIconBackground"));
        mCustomerInfoEntity.setNormalHeadIcon(object
                .optString("clientIconBackground"));
        mCustomerInfoEntity.setUsername(object
                .optString("customerAccounts"));
        mCustomerInfoEntity.setMoney(Integer.valueOf(object.optString("customerGold").equals("") ? "0" : object.optString("customerGold")));
        mCustomerInfoEntity.setCustomerGroupRel(object.optInt("customerGroupRel"));
        mCustomerInfoEntity.setId(object.optString("customerId"));
        mCustomerInfoEntity.setName(object.optString("customerNickname"));

        //汉字转换成拼音
        CharacterParser characterParser = CharacterParser.getInstance();
        String pinyin = characterParser.getSelling(object.optString("customerNickname"));
        String sortString = pinyin.substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            mCustomerInfoEntity.setSortLetters(sortString.toUpperCase());
        } else {
            mCustomerInfoEntity.setSortLetters("#");
        }
        // 性别 0--全部 1--男 2--女
        mCustomerInfoEntity.setSex(object.optString("customerSex", "0"));
        mCustomerInfoEntity.setDistance(object.optDouble("distance", 0.0));
        mCustomerInfoEntity.setFlag(object.optString("flag"));
        mCustomerInfoEntity.setInfoVersion(object.optString("infoVersion"));
        mCustomerInfoEntity.setDescription(object.optString("personalNarrate"));
        mCustomerInfoEntity.setIsAttentionFriend(object.optInt("relationType"));// 关系 0 无关系 1 我关注的 2 黑名单 3是客户 4医生 5合作者
        mCustomerInfoEntity.setRoldid(object.optInt("roleId"));
        mCustomerInfoEntity.setServicePrice(object
                .optString("servicePrice"));
        mCustomerInfoEntity.setServiceStatus(object
                .optString("serviceStatus"));
        mCustomerInfoEntity
                .setServiceTime(object.optString("serviceStart"));
        mCustomerInfoEntity.setServiceStatusCode(object
                .optString("serviceStatusCode"));
        mCustomerInfoEntity.setServiceTypes(object.optString("serviceTypes"));
        mCustomerInfoEntity.setOrderServiceTypes(object.optString("CUSTOM_SERVICE_TYPE"));//医生订制服务
        mCustomerInfoEntity.setTicketFlag(object.optString("ticketFlag"));
        mCustomerInfoEntity.setRemarkName(object
                .optString("remarksName", ""));
        // 医生
        mCustomerInfoEntity.setSpecial(object.optString("doctorSpecially"));

        mCustomerInfoEntity.setOfficeCode1(object.optString("doctorOfficeCode"));
        mCustomerInfoEntity.setOfficeCode2(object.optString("doctorOffice2Code"));
        mCustomerInfoEntity.setOfficeName1(object.optString("doctorOffice"));
        mCustomerInfoEntity.setOfficeName2(object.optString("doctorOffice2"));

        mCustomerInfoEntity.setHospital(object.optString("doctorHospital"));
        mCustomerInfoEntity.setDoctorTitle(object.optString("doctorTitle"));
        mCustomerInfoEntity.setDwellingplace(object
                .optString("dwellingPlace"));
        mCustomerInfoEntity.setRegisterCount(object.optString("reqCount"));
        mCustomerInfoEntity.setOrderId(object.optString("orderId"));
        mCustomerInfoEntity.setServiceTypeId(object.optString("serviceTypeId"));
        mCustomerInfoEntity.setServiceTypeSubId(object.optString("serviceTypeSubId"));
        mCustomerInfoEntity.setServiceItemId(object.optString("serviceItemId"));
        mCustomerInfoEntity.setServiceTypeName(object.optString("serviceTypeName"));
        mCustomerInfoEntity.setDoctorWorkaddress(object.optString("workLocationName"));
        mCustomerInfoEntity.setDoctorWorkaddressCode(object.optString("workLocation"));
        mCustomerInfoEntity.setServiceEnd(object.optString("serviceEnd"));
        mCustomerInfoEntity.setCustomerLevel(object.optInt("customerLevel"));
        mCustomerInfoEntity.setDoctorLevel(object.optInt("doctorLevel"));
        mCustomerInfoEntity.setDoctorMessage(object.optString("MESSAGE_CONTENT"));
        mCustomerInfoEntity.setMessageTime(object.optString("MESSAGE_TIME"));
        if (object.optInt("hasExtensionInfomaiton") != 0) {
            mCustomerInfoEntity.setJson(object.optString("albums"));
            mCustomerInfoEntity.setBirthday(object.optString("birthday"));
            mCustomerInfoEntity.setSameExperience(object
                    .optString("bodyList"));
            mCustomerInfoEntity.setCultureLevel(object
                    .optString("cultureLevel"));
            mCustomerInfoEntity.setCustomerlocus(object
                    .optString("customerLocus"));
            mCustomerInfoEntity.setDoctorEmail(object
                    .optString("doctorEmail"));
            mCustomerInfoEntity.setMobilePhone(object
                    .optString("doctorMobilePhone"));
            mCustomerInfoEntity.setRealname(object.optString("doctorRealName"));
            mCustomerInfoEntity.setDoctorClientPicture(object.optString("doctorClientBackground"));
            mCustomerInfoEntity.setDoctorBigPicture(object.optString("doctorBigIconbackground"));
            mCustomerInfoEntity.setTelePhone(object.optString("doctorTelephone"));
            mCustomerInfoEntity.setErrorCode(object.optInt("errorCode"));
            mCustomerInfoEntity.setGameLevel(object.optString("gameLevel"));
            mCustomerInfoEntity.setLableJson(object.optString("interestsList"));
            mCustomerInfoEntity.setLatitude(object.optString("latitude"));
            mCustomerInfoEntity.setLongitude(object.optString("longitude"));
            mCustomerInfoEntity.setHunyin(object.optString("marryFlag"));
            mCustomerInfoEntity.setMetier(object.optString("metier"));
            mCustomerInfoEntity.setOnlineState(object
                    .optString("onlineState"));// 502770-在线，502780-离开
            mCustomerInfoEntity.setServiceEnd(object.optString("serviceEnd"));
            mCustomerInfoEntity.setPraiseCount(object.optString("likedNum"));
            mCustomerInfoEntity.setPraiseFlag(object.optString("likedflag"));
            if (object.toString().contains("ACADEMIC_JOB")) {
                mCustomerInfoEntity.setAcademicJob(object.optString("ACADEMIC_JOB"));
            }
            if (object.toString().contains("RESUME_CONTENT")) {
                mCustomerInfoEntity.setResumeContent(object.optString("RESUME_CONTENT"));
            }

        }
        return mCustomerInfoEntity;
    }


    /**
     * jsonobj 解析当前登录用户资料
     *
     * @param
     * @return
     */
    public static CustomerInfoEntity jsonToCustomerInfo(String josn) {
        CustomerInfoEntity mCustomerInfoEntity = new CustomerInfoEntity();
        try {
            JSONObject object = new JSONObject(josn);
            mCustomerInfoEntity.setBigHeadIcon(object
                    .optString("bigIconBackground"));
            mCustomerInfoEntity.setEmail(object.optString("email"));
            mCustomerInfoEntity.setPoneNumber(object.optString("phone"));
            mCustomerInfoEntity.setPhonefriendName(object.optString("phonefriendName"));
            mCustomerInfoEntity.setBirthday(object.optString("birthday"));
            mCustomerInfoEntity.setSameExperience(object.optJSONArray("bodyList").toString());
            mCustomerInfoEntity.setNormalHeadIcon(object
                    .getString("clientIconBackground"));
            mCustomerInfoEntity.setCultureLevel(object
                    .getString("cultureLevel"));
            mCustomerInfoEntity.setUsername(object
                    .getString("customerAccounts"));
            mCustomerInfoEntity.setMoney(Integer.valueOf(object.optString(
                    "customerGold").equals("") ? "0" : object
                    .getString("customerGold")));
            mCustomerInfoEntity.setId(object.optString("customerId"));
            mCustomerInfoEntity.setCustomerlocus(object
                    .getString("customerLocus"));
            mCustomerInfoEntity.setName(object.optString("customerNickname"));
            //汉字转换成拼音
            CharacterParser characterParser = CharacterParser.getInstance();
            String pinyin = characterParser.getSelling(object.optString("customerNickname"));
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                mCustomerInfoEntity.setSortLetters(sortString.toUpperCase());
            } else {
                mCustomerInfoEntity.setSortLetters("#");
            }

            mCustomerInfoEntity.setSex(object.optString("customerSex"));
            mCustomerInfoEntity.setDoctorEmail(object.optString("doctorEmail"));
            mCustomerInfoEntity.setHospital(object.optString("doctorHospital"));
            mCustomerInfoEntity.setMobilePhone(object
                    .getString("doctorMobilePhone"));

            mCustomerInfoEntity.setOfficeCode1(object.optString("doctorOfficeCode"));
            mCustomerInfoEntity.setOfficeCode2(object.optString("doctorOffice2Code"));
            mCustomerInfoEntity.setOfficeName1(object.optString("doctorOffice"));
            mCustomerInfoEntity.setOfficeName2(object.optString("doctorOffice2"));
            mCustomerInfoEntity.setDoctorLevel(object.optInt("doctorLevel"));
            mCustomerInfoEntity.setRealname(object.optString("doctorRealName"));
            mCustomerInfoEntity.setDoctorClientPicture(object
                    .getString("doctorClientBackground"));
            mCustomerInfoEntity.setDoctorBigPicture(object
                    .getString("doctorBigIconbackground"));
            mCustomerInfoEntity.setSpecial(object.optString("doctorSpecially"));
            mCustomerInfoEntity.setTelePhone(object
                    .getString("doctorTelephone"));
            mCustomerInfoEntity.setDoctorTitle(object.optString("doctorTitleCode"));
            mCustomerInfoEntity.setDoctorTitleName(object.optString("doctorTitle"));
            mCustomerInfoEntity.setDwellingplace(object
                    .getString("dwellingPlace"));
            mCustomerInfoEntity.setLableJson(object.optString("interestsList"));
            mCustomerInfoEntity.setLatitude(object.optString("latitude"));
            mCustomerInfoEntity.setLongitude(object.optString("longitude"));
            mCustomerInfoEntity.setHunyin(object.optString("marryFlag"));
            mCustomerInfoEntity.setMetier(object.optString("metier"));
            mCustomerInfoEntity.setDescription(object
                    .getString("personalNarrate"));
            mCustomerInfoEntity.setRoldid(object.optInt("roleId"));
            mCustomerInfoEntity.setMicroBlogUID(object
                    .getString("microBlogUID"));
            mCustomerInfoEntity.setSetBindSessionCod(object
                    .getString("sessionCode"));
            mCustomerInfoEntity.setSetBindSinaState(object
                    .getString("bandingState"));
            mCustomerInfoEntity.setMicroBlogCode(object
                    .getString("microBlogAccount"));
            mCustomerInfoEntity.setRemarkName(object
                    .getString("remarksName"));
            mCustomerInfoEntity.setDoctorCertificate(object
                    .getString("doctorCertificate"));
            mCustomerInfoEntity.setTransferName(object
                    .getString("transferName"));
            mCustomerInfoEntity.setTransferCode(object
                    .getString("transferCode"));
            mCustomerInfoEntity.setTransferGetName(object
                    .getString("transferGetName"));
            mCustomerInfoEntity.setTransferGetTele(object
                    .getString("transferGetTele"));
            mCustomerInfoEntity.setTransferAddr((object
                    .getString("transferAddr")));
            mCustomerInfoEntity.setQrCode(object.optString("qrCode"));
            mCustomerInfoEntity.setQrCodeIcon(object.optString("qrCodeIcon"));
            mCustomerInfoEntity.setDoctorWorkaddress(object.optString("workLocationName"));
            mCustomerInfoEntity.setDoctorWorkaddressCode(object.optString("workLocation"));
            mCustomerInfoEntity.setDoctorPosition(object.optString("position"));
            mCustomerInfoEntity.setCenterName(object.optString("CENTER_NAME"));
            mCustomerInfoEntity.setTalkOnOff(object.optString("TALK_ON_OFF"));
        } catch (Exception e) {
//			System.out.println(e.toString());
        }
        return mCustomerInfoEntity;
    }

    /**
     * jsonobj 新版六一健康解析当前登录用户资料
     *
     * @param
     * @return
     */
    public static CustomerInfoEntity jsonToCustomerInfo2(String josn) {
        CustomerInfoEntity mCustomerInfoEntity = new CustomerInfoEntity();
        try {
            JSONObject object = new JSONObject(josn);
            mCustomerInfoEntity.setDoctorPosition(object.optString("position"));
            mCustomerInfoEntity.setVerifyFlag(object.optString("verify_Flag"));
            mCustomerInfoEntity.setDoctorPosition(object.optString("banding_state"));
            mCustomerInfoEntity.setPoneNumber(object.optString("phone"));
            mCustomerInfoEntity.setRealname(object.optString("realName"));
            mCustomerInfoEntity.setIsSetPwd(object.optString("isSetPsw"));
            mCustomerInfoEntity.setDwellingplace(object.getString("dwellingPlace"));
            mCustomerInfoEntity.setUsername(object.getString("customerAccounts"));
            mCustomerInfoEntity.setSixOneAccoutn(object.getString("customerAccounts"));
            mCustomerInfoEntity.setBigHeadIcon(object.optString("bigIconBackground"));
            mCustomerInfoEntity.setSex(object.optString("customerSex"));
            mCustomerInfoEntity.setDiseaseDesc(object.optString("diseaseDesc"));
            mCustomerInfoEntity.setInfoVersion(object.optString("infoVersion"));
            mCustomerInfoEntity.setDoctorTitleName(object.optString("doctorTitleName"));
            mCustomerInfoEntity.setId(object.optString("customerId"));
            mCustomerInfoEntity.setDoctorWorkaddress(object.optString("workLocationName"));
            mCustomerInfoEntity.setOfficeCode2(object.optString("doctorOffice2"));
            mCustomerInfoEntity.setAge(object.optString("age"));
            mCustomerInfoEntity.setDoctorWorkaddressCode(object.optString("workLocation"));
            mCustomerInfoEntity.setOfficeName2(object.optString("doctorOffice2Name"));
            mCustomerInfoEntity.setName(object.optString("realName"));
            mCustomerInfoEntity.setDoctorTitle(object.optString("doctorTitle"));
            mCustomerInfoEntity.setRoldid(object.optInt("roleId"));
            mCustomerInfoEntity.setNormalHeadIcon(object.getString("clientIconBackground"));
            mCustomerInfoEntity.setSpecial(object.optString("doctorSpecially"));
            mCustomerInfoEntity.setQrCode(object.optString("qrCode"));
            mCustomerInfoEntity.setCustomerlocus(object.getString("customerLocus"));
//            mCustomerInfoEntity.setDoctorCertificate(object.getString("doctorCertificate"));
            mCustomerInfoEntity.setEmail(object.optString("email"));
            mCustomerInfoEntity.setQrCodeIcon(object.optString("qrCodeIcon"));
//            mCustomerInfoEntity.setDoctorClientPicture(object.getString("doctorClientBackground"));
            mCustomerInfoEntity.setAvToken(object.getString("token"));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return mCustomerInfoEntity;
    }

    /**
     * 解析个人资料  医生编辑个人信息
     *
     * @param object
     * @return
     * @throws JSONException
     */
    public static CustomerInfoEntity JsonToDocCustmerInfo(JSONObject object) {
        CustomerInfoEntity mCustomerInfoEntity = new CustomerInfoEntity();

        mCustomerInfoEntity.setDoctorPosition(object.optString("position"));
        mCustomerInfoEntity.setVerifyFlag(object.optString("verify_Flag"));
        mCustomerInfoEntity.setIntroduction(object.optString("introduction"));//简介
        mCustomerInfoEntity.setHospital(object.optString("unitName"));
        mCustomerInfoEntity.setHospitalCode(object.optString("unitCode"));
        mCustomerInfoEntity.setBandingState(object.optString("banding_state"));
        mCustomerInfoEntity.setPoneNumber(object.optString("phone"));
        mCustomerInfoEntity.setRealname(object.optString("doctorRealName"));
        mCustomerInfoEntity.setDwellingplace(object.optString("dwellingPlace"));
        mCustomerInfoEntity.setUsername(object.optString("customerAccounts"));
        mCustomerInfoEntity.setBigHeadIcon(object.optString("bigIconbackground"));
        mCustomerInfoEntity.setSex(object.optString("customerSex"));
        mCustomerInfoEntity.setDiseaseDesc(object.optString("diseaseDesc"));
        mCustomerInfoEntity.setInfoVersion(object.optString("infoVersion"));
        mCustomerInfoEntity.setDoctorTitleName(object.optString("doctorTitleName"));
        mCustomerInfoEntity.setId(object.optString("customerId"));
        mCustomerInfoEntity.setDoctorWorkaddress(object.optString("workLocationName"));
        mCustomerInfoEntity.setDoctorBigPicture(object.optString("doctorBigIconbackground"));
        mCustomerInfoEntity.setOfficeCode2(object.optString("doctorOffice2"));
        mCustomerInfoEntity.setAge(object.optString("age"));
        mCustomerInfoEntity.setDoctorWorkaddressCode(object.optString("workLocation"));
        mCustomerInfoEntity.setOfficeName2(object.optString("doctorOffice2Name"));
        mCustomerInfoEntity.setName(object.optString("customerNickname"));
        mCustomerInfoEntity.setDoctorTitle(object.optString("doctorTitle"));
        mCustomerInfoEntity.setRoldid(Integer.parseInt(object.optString("roleId")));
        mCustomerInfoEntity.setSpecial(object.optString("doctorSpecially"));
        mCustomerInfoEntity.setDoctorClientPicture(object.optString("doctorClientBackground"));
        mCustomerInfoEntity.setQrCode(object.optString("qrCode"));
        mCustomerInfoEntity.setQrCodeIcon(object.optString("qrCodeIcon"));
        mCustomerInfoEntity.setCustomerlocus(object.optString("customerLocus"));
        mCustomerInfoEntity.setDoctorCertificate(object.optString("doctorCertificate"));
        mCustomerInfoEntity.setEmail(object.optString("email"));
        mCustomerInfoEntity.setNormalHeadIcon(object.optString("clientIconBackground"));

        return mCustomerInfoEntity;
    }

    /**
     * 解析个人资料  解析患者资料
     *
     * @param object
     * @return
     * @throws JSONException
     */
    public static CustomerInfoEntity JsonToCustmerInfo(JSONObject object) {
        CustomerInfoEntity mCustomerInfoEntity = new CustomerInfoEntity();

        mCustomerInfoEntity.setId("" + object.optInt("CUSTOMER_ID"));
        mCustomerInfoEntity.setInfoVersion("" + object.optInt("INFO_VERSION"));
        mCustomerInfoEntity.setRealname(object.optString("REAL_NAME"));
        mCustomerInfoEntity.setSex(object.optString("CUSTOMER_SEX"));
        mCustomerInfoEntity.setUsername(object.optString("CUSTOMER_ACCOUNTS"));
        mCustomerInfoEntity.setCustomerlocus(object.optString("CUSTOMER_LOCUS"));
        mCustomerInfoEntity.setAge(object.optString("AGE"));
        mCustomerInfoEntity.setDiseaseDesc(object.optString("DISEASE_DESC"));
        mCustomerInfoEntity.setNormalHeadIcon(object.optString("CLIENT_ICON_BACKGROUND"));
        mCustomerInfoEntity.setBigHeadIcon(object.optString("BIG_ICON_BACKGROUND"));
        mCustomerInfoEntity.setPoneNumber(object.optString("PHONE_NUMBER"));
        return mCustomerInfoEntity;
    }

    public static List<CustomerInfoEntity> jsonArrayToCustomerInfo(
            String content) {
        List<CustomerInfoEntity> list = new ArrayList<CustomerInfoEntity>();
        try {
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                list.add(jsonToCustmerInfo(jsonObject));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 将json转化为的标签
     *
     * @param
     */
    public static List<TagEntity> JsonToListEntity(String jsonObject, int type) {
        List<TagEntity> list = new ArrayList<TagEntity>();
        String CODE = "focusCode";
        String NAME = "focusName";
        try {
            if (jsonObject == null || jsonObject.toString().equals(""))
                return list;
            JSONArray json = new JSONArray(jsonObject);
            list = new ArrayList<TagEntity>();
            for (int i = 0; i < json.length(); i++) {
                JSONObject object = (JSONObject) json.get(i);
                TagEntity entity = new TagEntity(object.optString(CODE),
                        object.optString(NAME), true);
                list.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 将Tag转化为jsonArray
     *
     * @param list
     * @return
     */
    public static JSONArray TagsListEntityToJson(List<TagEntity> list) {
        JSONArray object = new JSONArray();
        try {
            for (int i = 0; i < list.size(); i++) {
                JSONObject json = new JSONObject();
                TagEntity entity = list.get(i);
                json.put("focusCode", entity.getId());
                json.put("focusName", entity.getName());
                object.put(json);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static InterestWallEntity parseWallEntity(JSONObject jsonObject) {
        InterestWallEntity entity = new InterestWallEntity();
        String imageurl = jsonObject.optString("iconPicturePath");
        String id = jsonObject.optString("pictureID");
        entity.setImageUrl(imageurl);
        entity.setId(id);
        entity.setPictureUrl(jsonObject.optString("pictureUrl"));
        entity.setPicHeight(jsonObject.optInt("pictureHeightPx"));
        entity.setPicWidth(jsonObject.optInt("pictureWidthPx"));
        entity.setCustomerId(jsonObject.optString("customerId"));
        entity.setCustomerNickname(jsonObject.optString("customerNickname"));
        entity.setClientIconBackground(jsonObject.optString("clientIconBackground"));
        entity.setTime(jsonObject.optString("time"));
        entity.setCUSTOMER_SEX(jsonObject.optString("customerSex"));
        entity.setIsCollection(jsonObject.optInt("isCollection"));
        entity.setPictureName(jsonObject.optString("pictureName"));
        entity.setBigPicturePath(jsonObject.optString("bigPicturePath"));
        entity.setBigPictureWidthPx(jsonObject.optString("bigPictureWidthPx"));
        entity.setBigPictureHeightPx(jsonObject.optString("bigPictureHeightPx"));
        return entity;
    }

    public static Object parseInviteResultData(String content) {
        final List<CustomerInfoEntity> list = new ArrayList<CustomerInfoEntity>();
        try {
            JSONTokener tokener = new JSONTokener(content);
            Object obj = tokener.nextValue();
            JSONArray jsonArray;
            if (obj instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) obj;
                if (jsonObject.has("error_message"))
                    return jsonObject.optString("error_message");
                jsonArray = jsonObject.getJSONArray("CUSLIST");
            } else {
                jsonArray = (JSONArray) obj;
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                list.add(jsonToCustmerInfo(jsonObject));
            }
        } catch (JSONException e) {
        }
        return list;
    }

    /**
     * 更新信息
     *
     * @param jsonObj
     */
    public static void jsonUpDataCustomerInfo(JSONObject jsonObj) {
        CustomerInfoEntity mCustomerInfoEntity = LoginServiceManeger.instance().getLoginEntity();
        mCustomerInfoEntity.setRealname(jsonObj.optString("PATIENT_NAME"));
        mCustomerInfoEntity.setAge(jsonObj.optString("PATIENT_AGE"));
        mCustomerInfoEntity.setSex(jsonObj.optString("PATIENT_SEX"));
        mCustomerInfoEntity.setName(jsonObj.optString("PATIENT_NAME"));
        mCustomerInfoEntity.setBigHeadIcon(jsonObj.optString("BIG_ICON_BACKGROUND"));
        mCustomerInfoEntity.setNormalHeadIcon(jsonObj.optString("CLIENT_ICON_BACKGROUND"));
        mCustomerInfoEntity.setPoneNumber(jsonObj.optString("PATIENTTEL_PHONE"));

        String userId = LoginServiceManeger.instance().getLoginEntity().getId();
        if (!HStringUtil.isEmpty(userId)) {
            CustomerInfoEntity baseInfoEntity = (CustomerInfoEntity) HTalkApplication.getAppData().cacheInformation.get(userId);
            if (baseInfoEntity != null) {
                baseInfoEntity.setNormalHeadIcon(jsonObj.optString("CLIENT_ICON_BACKGROUND"));
                HTalkApplication.getAppData().updateCacheInfomation(baseInfoEntity);
            }
        }


    }

}
