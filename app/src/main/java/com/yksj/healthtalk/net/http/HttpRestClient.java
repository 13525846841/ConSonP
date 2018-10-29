package com.yksj.healthtalk.net.http;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.bean.ServiceType;
import com.yksj.consultation.son.consultation.member.PlayVideoActiviy;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.entity.GroupInfoEntity;
import com.yksj.healthtalk.entity.MessageEntity;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.net.socket.SmartControlClient;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.FileUtils;

import org.apache.http.message.BasicNameValuePair;
import org.universalimageloader.core.ImageLoader;
import org.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 所有执行http请求操作调用此类
 *
 * @author zhao
 */

public class HttpRestClient {
    private static HttpUrls mHttpUrls;// = new HttpUrls();
    public static final String BOUNDARY = "---------------------------7dd196f1b0c70";
    public static AsyncHttpClient mAsyncHttpClient = new AsyncHttpClient(true);

    public static Map<String, String> getDefaultHeaders() {
        return mAsyncHttpClient.getHeaders();
    }

    public static HttpUrls getmHttpUrls() {
        return mHttpUrls;
    }

    public static void setmHttpUrls(HttpUrls mHttpUrls) {
        HttpRestClient.mHttpUrls = mHttpUrls;
    }

    public static void addHttpHeader(String key, String value) {
        if (mAsyncHttpClient != null) {
            mAsyncHttpClient.addHeader(key, value);
        }
        OkHttpClientManager.addHeader(key, value);
    }

    /**
     * 检查版本更新
     */
    public static void doHttpCheckAppVersion(String version, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("version", version);
        requestParams.put("iostype", "1");
        mAsyncHttpClient.post(mHttpUrls.URL_APP_VERSIONCHECK, requestParams, handler);
    }

    /**
     * 绑定手机
     *
     * @param phone   电话
     * @param code    验证码
     * @param psw     密码
     * @param userId  用户id
     * @param handler 回调
     */
    public static void doHttpConsultationSetPhoneBound(String phone, String code, String psw, String userId, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("phone", phone);
        requestParams.put("code", code);
        requestParams.put("psw", psw);
        requestParams.put("customerid", userId);
        requestParams.put("consultation_center_id", HTalkApplication.APP_CONSULTATION_CENTERID);
        mAsyncHttpClient.post(mHttpUrls.HZCHANGEBINDINGPHONE210SERVLET, requestParams,
                handler);
    }

    /**
     * 解除手机绑定
     *
     * @param userId  用户id
     * @param handler 回调
     */
    public static void doHttpUnPhoneBind(String userId, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("customerid", userId);
        params.put("Type", "phone");
        params.put("CONSULTATION_CENTER_ID", HTalkApplication.APP_CONSULTATION_CENTERID);
        mAsyncHttpClient.post(mHttpUrls.URL_UNBIND_PHONE_EMAIL, params, handler);
    }

    public static String getGoogleMapUrl(String lt, String ln) {
        // http://www.h-tlk.mobi/DuoMeiHealth/small_location_mark.png
//		http://api.map.baidu.com/staticimage?markers=%D6%D0%B9%D8%B4%E5&zoom=19&center=116.403874,39.914889&
        //http://api.map.baidu.com/staticimage?width=400&height=400&center=116.379064,39.959871&markers=116.379064,39.959871&zoom=16&markerStyles=s,A,0xff0000
        StringBuffer stringBuffer = new StringBuffer(
                "http://api.map.baidu.com/staticimage?zoom=17&markerStyles=s,A,0xff0000");
        stringBuffer.append("&width=200&height=200");
        stringBuffer.append("&center=");
        stringBuffer.append(ln);
        stringBuffer.append(",");
        stringBuffer.append(lt);
        stringBuffer.append("&markers=");
        stringBuffer.append(ln);
        stringBuffer.append(",");
        stringBuffer.append(lt);
        /*stringBuffer
                .append("&markerStyles=-1,http://www.h-tlk.mobi/DuoMeiHealth/small_location_mark.png");*/
//		stringBuffer.append(String.valueOf(lt));
//		stringBuffer.append(",");
//		stringBuffer.append(ln);
//		stringBuffer.append("&zoom=15");
/*		stringBuffer.append("&sensor=false");
        stringBuffer.append("&maptype=roadmap");
		stringBuffer.append("&format=png");
		stringBuffer.append("&mobile=true");*/
        return stringBuffer.toString();
        // String urlStr=
        // "http://maps.google.com/maps/api/staticmap?language=zh-CN&center=39.9942100,116.3645000,13&zoom=14&size=300x200&maptype=roadmap%C2%A0%C2%A0%20&markers=color:blue|label:S|39.987889,116.350029&markers=color:green|label:G|39.9942100,116.3645000%C2%A0%C2%A0%20&markers=color:red|color:red|label:C|39.987889,116.350029&sensor=false";
    }

    /**
     * 程序异常日志报告
     */
    public static void doHttpReportAppException(String log, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("log", log);
        requestParams.put("OSType", "1");
        mAsyncHttpClient.post(mHttpUrls.URL_APPEXCEPTION_REPORT, requestParams,
                handler);
    }

    /**
     * 查找话题
     *
     * @param mGroupInfoEntity
     * @param sourceType       (0---全部，1---最新，2---最热，3---推荐)
     * @param handler
     */
    public static void doHttpRequestSearchGroup(GroupInfoEntity mGroupInfoEntity, int sourceType, AsyncHttpResponseHandler handler) {
        // PAGESIZE int
        // PAGENUM int
        // CUSTOMERID int
        try {
            JSONObject object = new JSONObject();
            object.put("TYPE", mGroupInfoEntity.getType());
            // 页数
            object.put("PAGESIZE", mGroupInfoEntity.getPagesize());
            // 数量
            object.put("PAGENUM", mGroupInfoEntity.getPagenum());
            // userid
            if (mGroupInfoEntity.getCreateCustomerID().equals("")) {
                return;
            } else {
                object.put("CUSTOMERID",
                        Integer.valueOf(mGroupInfoEntity.getCreateCustomerID()));
            }

            switch (mGroupInfoEntity.getType()) {
                // 按照名称搜索话题
            /*
             * case 0: object.put("GROUPNAME", mGroupInfoEntity.getName());
			 * break;
			 */
                // 最新最热推荐
                case 1:
                    // UPPERID String
                    // INFOID String
                    // FLAG int
                    // SOURCETYPE int (0---全部，1---最新，2---最热，3---推荐)
                    String name = mGroupInfoEntity.getName();
                    object.put("GROUPNAME", name == null ? "" : name);
                    object.put(
                            "UPPERID",
                            mGroupInfoEntity.getUpperId() != null ? mGroupInfoEntity
                                    .getUpperId() : "0");
                    object.put("FLAG", mGroupInfoEntity.getFlag());
                    object.put("FLAGPLACING", mGroupInfoEntity.getFlagPlacing());
                    object.put("SOURCETYPE", sourceType);
                    object.put(
                            "INFOID",
                            mGroupInfoEntity.getInfoId() != null ? mGroupInfoEntity
                                    .getInfoId() : "0");
                    break;
                // 点击广告搜索话题
                case 2:
                    object.put("ADVERID", "");
                    object.put("FLAG", 2);
                    break;
                default:
                    break;
            }

            // 网络请求
            RequestParams params = new RequestParams();
            params.put("PARAMETER", object.toString());
            mAsyncHttpClient.post(mHttpUrls.URL_SEARCH_SALON, params, handler);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 按类型显示好友列表
     * sourceType 0--最新 1--活跃 2--推荐 3--附近 4--同城
     * TYPE   findFriendsByThreeCate之前的三种情况  附近  活跃  个性
     */
    public static void doHttpRequestSearchFriends1(CustomerInfoEntity c, int sourceType, AsyncHttpResponseHandler handler) {
        // SOURCETYPE 0--最新 1--活跃 2--推荐 3--附近 4--同城
        int sex = 0;
        String mSex;
        // 判断性别
        if ((mSex = c.getSex()) != null) {
            if (mSex.equalsIgnoreCase("2")) {
                sex = 2;
            } else if (mSex.equalsIgnoreCase("1")) {
                sex = 1;
            }
        }
        JSONObject object = new JSONObject();
        try {
            object.put("LATITUDE", c.getLatitude() != null ? c.getLatitude() : "");
            // 纬度
            object.put("LONGITUDE", c.getLongitude() != null ? c.getLongitude() : "");
            // 下一页的标记
            object.put("FLAG", c.getFlag());
            // TYPE 0--找朋友 1--找医生 2--相同经历
            object.put("TYPE", c.getType());
            object.put("CUSTOMERID", c.getId());
            // 0--最新 1--活跃 2--推荐 3--附近 4--同城
            object.put("SOURCETYPE", sourceType);
            object.put("COUNT", "");

            if (c.getType() == 0) {
                object.put("MINAGE", c.getMinAge());
                object.put("MAXAGE", c.getMaxAge() != 0 ? c.getMaxAge() : 100);
                // 个性标签
                object.put("INTEREST", c.getInterestCode() != null ? c.getInterestCode() : "");
                object.put("SEX", sex);
                // 地区编码
                object.put("AREA", c.getAreaCode());
                object.put("USERNAME", c.getUsername() != null ? c.getUsername() : "");
            } else if (c.getType() == 1) {
                // 医生姓名
                object.put("DOCTORNAME", c.getRealname() != null ? c.getRealname() : "");
                // 专长
                object.put("DOCTORSPECIALLY", c.getSpecial() != null ? c.getSpecial() : "");
                // 医生职称编码
                object.put(
                        "DOCTORTITLE",
                        c.getDoctorTitle() != null ? Integer.valueOf(c.getDoctorTitle()) : 0);
                // 科室编码
                object.put("DOCTOROFFICE", c.getOfficeCode1());
                object.put("DOCTORHOSPITAL",
                        c.getHospital() != null ? c.getHospital() : "");
                object.put("ORDERONOFF", c.getOrderOnOff());
                // 地区编码
                object.put("AREA", c.getAreaCode());
            } else {
                // 相同经历
                object.put("BODY", c.getSameExperienceCode());
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 网络请求
        RequestParams params = new RequestParams();
        //我的修改
//		LogUtil.d("DDD", object.toString()+"<<<<-");
//		params.put("VALID_MARK", "40");
        params.put("PARAMETER", object.toString());
        params.put("TYPE", "findFriendsByThreeCate");
        mAsyncHttpClient.post(mHttpUrls.URL_FIND_FRIENDS, params, handler);
    }

    /**
     * 按条件搜索好友    TYPE=findFriendsByParam 按条件查询
     *
     * @param c
     * @param pageSize
     * @param handler
     */
    public static void doHttpRequestSearchFriends2(CustomerInfoEntity c, int pageSize, AsyncHttpResponseHandler handler) {
        // SOURCETYPE 0--最新 1--活跃 2--推荐 3--附近 4--同城
        int sex = 0;
        String mSex;
        // 判断性别
        if ((mSex = c.getSex()) != null) {
            if (mSex.equalsIgnoreCase("2")) {
                sex = 2;
            } else if (mSex.equalsIgnoreCase("1")) {
                sex = 1;
            }
        }
        JSONObject object = new JSONObject();
        try {
            object.put("LATITUDE", c.getLatitude() != null ? c.getLatitude() : "");
            // 纬度
            object.put("LONGITUDE", c.getLongitude() != null ? c.getLongitude() : "");
            // 下一页的标记
            object.put("FLAG", c.getFlag());
            // TYPE 0--找朋友 1--找医生 2--相同经历
            object.put("TYPE", c.getType());
            object.put("CUSTOMERID", c.getId());
            // 0--最新 1--活跃 2--推荐 3--附近 4--同城
            object.put("SOURCETYPE", "");
            object.put("COUNT", "");
            object.put("PAGESIZE", pageSize);//第几页
            object.put("PAGENUM", 20);//每页显示20条
            if (c.getType() == 0) {
                object.put("MINAGE", c.getMinAge());
                object.put("MAXAGE", c.getMaxAge() != 0 ? c.getMaxAge() : 100);
                // 个性标签
                object.put("INTEREST", c.getInterestCode() != null ? c.getInterestCode() : "");
                object.put("SEX", sex);
                // 地区编码
                object.put("AREA", c.getAreaCode());
                object.put("USERNAME", c.getUsername() != null ? c.getUsername() : "");
            } else if (c.getType() == 1) {
                // 医生姓名
                object.put("DOCTORNAME", c.getRealname() != null ? c.getRealname() : "");
                // 专长
                object.put("DOCTORSPECIALLY", c.getSpecial() != null ? c.getSpecial() : "");
                // 医生职称编码
                object.put(
                        "DOCTORTITLE",
                        c.getDoctorTitle() != null ? Integer.valueOf(c.getDoctorTitle()) : 0);
                // 科室编码
                object.put("DOCTOROFFICE", c.getOfficeCode1());
                object.put("DOCTORHOSPITAL",
                        c.getHospital() != null ? c.getHospital() : "");
                object.put("ORDERONOFF", c.getOrderOnOff());
                // 地区编码
                object.put("AREA", c.getAreaCode());
            } else {
                // 相同经历
                object.put("BODY", c.getSameExperienceCode());
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 网络请求
        RequestParams params = new RequestParams();
        params.put("PARAMETER", object.toString());
        //我的修改
//		LogUtil.d("DDD", object.toString()+"<<<__");
//		params.put("VALID_MARK", "40");
        params.put("TYPE", "findFriendsByParam");
        mAsyncHttpClient.post(mHttpUrls.URL_FIND_FRIENDS, params, handler);
//		mAsyncHttpClient.post("http://192.168.16.157:8899/DuoMeiHealth/NewFindFriends420", params, handler);
    }

    /**
     * 更新广告点击次数
     */
    public static void doHttpUpdateAdverCount(String adverid, String userid) {
        RequestParams params = new RequestParams();
        params.put("ADVERID", adverid);
        params.put("CUSTOMERID", userid);
        mAsyncHttpClient.post(mHttpUrls.URL_ADDADVCLICKREQ, null);
    }

    /**
     * 虚拟医生
     */
    public static void doHttpVirtualDoctor(org.json.JSONObject jsonObject, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("server_parame", jsonObject.toString());
        mAsyncHttpClient.get(mHttpUrls.URL_MMS_SERVLET300, params, handler);
    }

    public static void doHttpSetAuthority(String type, String classId, String shopid, String cusids, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("TYPE", type);
        params.put("CLASSID", classId);
        params.put("SHOPID", shopid);
        if (type.equals("3")) {
            params.put("CUSTOMERIDS", cusids);
        }
        mAsyncHttpClient.get(mHttpUrls.PHOTO_OPEN_LEVEL, params, handler);
    }


    /**
     * 二维码
     *
     * @param callback
     * @param tag
     */
    public static void doGetBarCode(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.BARCODE, map, callback, tag);
    }


    /**
     * 人体图症状内容查询
     *
     * @param codeId 症状编码id
     */
    public static void doHttpQuerySymptomContent(String codeId,
                                                 AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("TYPE", "2");
        params.put("SITUATIONCODES", codeId);
        mAsyncHttpClient.get(mHttpUrls.URL_QUERYSITUATION, params, handler);
    }

    /**
     * 虚拟人体查询
     */
    public static void doHttpQuerySymptom(String verion,
                                          AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("TYPE", "1");
        requestParams.put("VERSION", verion);
        mAsyncHttpClient.get(mHttpUrls.URL_QUERYSITUATION, requestParams,
                handler);
    }


    /**
     * 发送语音文件
     */
    public static void doHttpSendChatVoiceMesg(MessageEntity entity,
                                               int groupType,String objectType, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        if (groupType == 2) {
            params.put("server_code", "7045");
            params.put("allCustomerId", entity.getAllCustomerId());
        } else if (groupType == 1) {
            params.put("server_code", SmartControlClient.SIX_ONE_SEND_MSG + "");
            params.put("allCustomerId", entity.getAllCustomerId());
        } else if (groupType == 3) {//特殊服务
            params.put("server_code", SmartControlClient.SERVICE_SINGLE_SEND_MSG + "");
        } else {
            params.put("server_code", "7018");
        }
        params.put("serverId", entity.getId());
        params.put("customerId", entity.getSenderId());
        params.put("isGroupMessage", groupType + "");
        params.put("timeStamp", String.valueOf(System.currentTimeMillis()));
        params.put("sms_target_id", entity.getReceiverId());
        params.put("type", String.valueOf(entity.getType()));
        params.put("Object_Type", objectType);
        params.put("consultation_id", entity.getConsultationId());
        params.put("duration", entity.getVoiceLength());
        params.put("isDoctorMessage", entity.getIsDoctorMessage());
        params.put("order_id", entity.getOrderId());
        try {
            ;
            File file = new File(StorageUtils.getVoicePath(),
                    entity.getContent());
            params.put("FILE", file);
            mAsyncHttpClient
                    .post(mHttpUrls.URL_SENDPICSERVLET, params, handler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            handler.onFailure(null, null);
        }
    }

    /**
     * 聊天发送图片
     *
     * @param entity
     * @param handler
     */
    public static void doHttpSendChatImageMesg(MessageEntity entity,
                                               int groupType, String objectType,AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        if (groupType == 2) {
            params.put("server_code", "7045");
            params.put("allCustomerId", entity.getAllCustomerId());
        } else if (groupType == 1) {
            params.put("server_code", SmartControlClient.SIX_ONE_SEND_MSG + "");
            params.put("allCustomerId", entity.getAllCustomerId());
        } else if (groupType == 3) {//特殊服务
            params.put("server_code", SmartControlClient.SERVICE_SINGLE_SEND_MSG + "");
        } else {
            params.put("server_code", "7018");
        }
        params.put("serverId", entity.getId());
        params.put("customerId", entity.getSenderId());
        params.put("isGroupMessage", groupType + "");
        params.put("timeStamp", String.valueOf(System.currentTimeMillis()));
        params.put("sms_target_id", entity.getReceiverId());
        params.put("type", String.valueOf(entity.getType()));
        params.put("Object_Type", objectType);
        params.put("consultation_id", entity.getConsultationId());
        params.put("duration", entity.getVoiceLength());
        params.put("sms_req_content", "您有一张新图片");
        params.put("isDoctorMessage", entity.getIsDoctorMessage());
        params.put("order_id", entity.getOrderId());

        try {
            String[] files = entity.getContent().split("&");
            ImageLoader loader = ImageLoader.getInstance();
            String rootPath = StorageUtils.getImagePath();
            File bigFile2 = loader.getOnDiscFileName(new File(rootPath), files[1]);
            File miniFile2 = loader.getOnDiscFileName(new File(rootPath), files[0]);
            String path = StorageUtils.getImagePath();
            params.put("icon", miniFile2);
            params.put("image", bigFile2);
            mAsyncHttpClient
                    .post(mHttpUrls.URL_SENDPICSERVLET, params, handler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            handler.onFailure(null, null);
        }

    }


    /**
     * 聊天发送视频
     *
     * @param entity
     * @param handler
     */
    public static void doHttpSendChatVideoMesg(MessageEntity entity,
                                               int groupType, String objectType,AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        if (groupType == 2) {
            params.put("server_code", "7045");
            params.put("allCustomerId", entity.getAllCustomerId());
        } else if (groupType == 1) {
            params.put("server_code", SmartControlClient.SIX_ONE_SEND_MSG + "");
            params.put("allCustomerId", entity.getAllCustomerId());
        } else if (groupType == 3) {//特殊服务
            params.put("server_code", SmartControlClient.SERVICE_SINGLE_SEND_MSG + "");
        } else {
            params.put("server_code", "7018");
        }
        params.put("serverId", entity.getId());
        params.put("customerId", entity.getSenderId());
        params.put("isGroupMessage", groupType + "");
        params.put("timeStamp", String.valueOf(System.currentTimeMillis()));
        params.put("sms_target_id", entity.getReceiverId());
        params.put("type", String.valueOf(entity.getType()));
        params.put("Object_Type", objectType);
        params.put("consultation_id", entity.getConsultationId());
        params.put("duration", entity.getVoiceLength());
        params.put("sms_req_content", "视频");
        params.put("isDoctorMessage", entity.getIsDoctorMessage());
        params.put("order_id", entity.getOrderId());
        try {
            File bigFile2 = new File(entity.getContent());
            File miniFile2 = FileUtils.saveChatPhotoBitmapToFile(PlayVideoActiviy.getVideoThumbnail(entity.getContent()));
            params.put("icon", miniFile2);
            params.put("image", bigFile2);
            mAsyncHttpClient
                    .post(mHttpUrls.URL_SENDPICSERVLET, params, handler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            handler.onFailure(null, null);
        }

    }


    /**
     * 根据经纬度查询位置
     *
     * @param latitude
     * @param longitude
     */
    public static void doHttpQueryMapAddress(String latitude, String longitude,
                                             AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("latlng", longitude + "," + latitude);
        params.put("sensor", "true");
        params.put("language", "zh-CN");
        mAsyncHttpClient.get(mHttpUrls.GOOLE_MAP_GECODE, params, handler);
    }

    /**
     * 语音文件下载
     *
     * @param path
     * @param handler
     */
    public static void doHttpDownChatFile(String path,
                                          AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrls.URL_QUERYHEADIMAGE + path, handler);
    }


    /**
     * 购买门票
     *
     * @param groupid
     * @param customerid
     * @param tickettype
     * @param handler
     */
    public static void doHttpBuyTicket(String groupid, String customerid,
                                       String tickettype, String type, AsyncHttpResponseHandler handler) {
        // String groupId = request.getParameter("GROUPID");
        // String customerId = request.getParameter("CUSTOMERID");
        // String ticType = request.getParameter("TICKETTYPE");
        // String payAccount = request.getParameter("PAYACCOUNT");//支付账户
        // String payType =
        // request.getParameter("PAYTYPE");//--10-支付宝--20-银行类型--30-财富通
        RequestParams params = new RequestParams();
        params.put("GROUPID", groupid);
        params.put("CUSTOMERID", customerid);
        params.put("TICKETTYPE", tickettype);
        params.put("PAYTYPE", type);
        params.put("PAYACCOUNT", "");
        mAsyncHttpClient.get(mHttpUrls.URL_BUYTICKET, params, handler);
    }

    /**
     * 购买门票
     *
     * @param groupid
     * @param customerid
     * @param tickettype
     * @param handler
     */
    public static void doHttpUnionBuyTicket(String groupid, String customerid,
                                            String tickettype, String type, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("GROUPID", groupid);
        params.put("CUSTOMERID", customerid);
        params.put("TICKETTYPE", tickettype);
        params.put("PAYTYPE", type);
        params.put("PAYACCOUNT", "");
        mAsyncHttpClient.get(mHttpUrls.SALONUNIONPAYPAYMENT, params, handler);
    }


    /**
     * 购买门票
     *
     * @param groupid
     * @param customerid
     * @param tickettype
     * @param handler
     */
    public static void doHttpWalletBuyTicket(String PAYMENT_PASSWORD, String groupid, String customerid,
                                             String tickettype, String type, AsyncHttpResponseHandler handler) {
        // String groupId = request.getParameter("GROUPID");
        // String customerId = request.getParameter("CUSTOMERID");
        // String ticType = request.getParameter("TICKETTYPE");
        // String payAccount = request.getParameter("PAYACCOUNT");//支付账户
        // String payType =
        // request.getParameter("PAYTYPE");//--10-支付宝--20-银行类型--30-财富通
        RequestParams params = new RequestParams();
        params.put("GROUPID", groupid);
        params.put("CUSTOMERID", customerid);
        params.put("TICKETTYPE", tickettype);
        params.put("PAYTYPE", type);
        params.put("PAYACCOUNT", "");
        params.put("PAYMENT_PASSWORD", PAYMENT_PASSWORD);
        params.put("validMark", "40");
        mAsyncHttpClient.get(mHttpUrls.SALONWALLETPAYMENT, params, handler);
    }

    public static void doHttpJoinGroupChating(String cusid, String groupID, String messge, String isBL, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("CUSTOMERID", cusid);
        params.put("GROUPID", groupID);
        params.put("GROUPMESSAGE", messge);
        params.put("isBL", isBL);
        mAsyncHttpClient.get(mHttpUrls.HZINGROUPSERVLET, params, handler);
    }

    public static void doHttpUpdateGroupInceptMsg(String customerId,
                                                  String groupId, Boolean inceptMessage,
                                                  Boolean releaseSystemMessage, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("CUSTOMERID", customerId);
        params.put("GROUPID", groupId);
        if (inceptMessage) {
            params.put("INCEPTMESSAGE", "Y");
        } else {
            params.put("INCEPTMESSAGE", "N");
        }
        if (releaseSystemMessage) {
            params.put("RELEASESYSTEMMESSAGE", "1");
        } else {
            params.put("RELEASESYSTEMMESSAGE", "0");
        }
        mAsyncHttpClient.get(mHttpUrls.URL_UPDATEGROUPINCEPTMSG, params,
                handler);
    }

    public static void doHttpServerMerchant(String width, String height,
                                            String customerid, String id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("customerid", customerid);
        params.put("merchantid", id);
        params.put("SYS_CLASS_ID", "Android");
        params.put("SCREEN_X", 480 + "");
        params.put("SCREEN_Y", 800 + "");
        params.put("PICTYPE", "1");
        params.put("Type", "queryCenterMerchantByDc");
        mAsyncHttpClient.get(mHttpUrls.URL_SERVER_BG_ALL33, params, handler);
    }


    /**
     * 挂号
     *
     * @param cusId
     * @param doctorId
     * @param handler
     * @param SERVICE_TYPE_ID 1为普通服务，2为预约时段服务,3为预约面访服务
     */
    public static void doHttpEngageTheDialogue(String cusId, String doctorId, String SERVICE_TYPE_ID,
                                               AsyncHttpResponseHandler handler) {
        // CUSTOMER_ID自己的客户id
        // DOCTORID 被点击人的客户id
        RequestParams params = new RequestParams();
        params.put("Type", "IsCanTalk");
        params.put("CUSTOMER_ID", cusId);
        params.put("DOCTORID", doctorId);
        params.put("SERVICE_TYPE_ID", SERVICE_TYPE_ID);
        mAsyncHttpClient.get(mHttpUrls.URL_SERVICESETSERVLET42, params, handler);
    }


    /**
     * 根据Id查询个人资料
     *
     * @param friendId
     * @param userId
     * @param handler
     */
    public static void doHttpFindCustomerInfoByCustId(String type,
                                                      String qrCode, String friendId, String userId,
                                                      AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(qrCode)) {
            params.put("TYPE", type);
            params.put("QRCODE", qrCode);
        } else {
            params.put("CUSTOMERID", friendId);
        }
        params.put("MYCUSTOMERID", SmartFoxClient.getLoginUserId());
        params.put("VALID_MARK", HTalkApplication.APP_VALID_MARK);
        mAsyncHttpClient.get(mHttpUrls.URL_FINDCUSTOMERINFOBYCUSTID, params, handler);
    }

    /**
     * 关于点赞的操作,
     *
     * @param context    上下文
     * @param praiseFlag 现在是否已经点过赞,点过赞则执行取消点赞操作,未点赞则执行点赞操作
     * @param entity     对方实体
     * @param handler
     */
    public static void doHttpOperatePraiseToFriend(Context context, boolean praiseFlag,
                                                   CustomerInfoEntity entity, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("CUSTOMERID", entity.getId());
        params.put("MYCUSTOMERID", SmartFoxClient.getLoginUserId());
        if (praiseFlag) {
            mAsyncHttpClient.post(mHttpUrls.FRIENDINFOCANCELLIKEDOC, params, handler);//取消点赞
        } else {
            mAsyncHttpClient.post(mHttpUrls.FRIENDINFOLIKEDOC, params, handler);//点赞
        }
    }


    /**
     * 删除群消息
     *
     * @param grouId  群id
     * @param type    1全部删除
     * @param ids     id字符串
     * @param handler
     */
    public static void deleteGroupMessages(String grouId, int type, String ids,
                                           AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        if (type == 1) {// 全部删除
            params.put("Type", "deleteGroupAllTalkmessage");
        } else {
            params.put("Type", "deleteGroupTalkmessage");
            params.put("offids", ids);
        }
        params.put("groupid", grouId);
        mAsyncHttpClient.get(
                mHttpUrls.URL_DeleteCustomerGroupChatLog, params,
                handler);
    }


    /**
     * 删除单聊
     *
     * @param type    1全部删除
     * @param ids     id字符串
     * @param handler "Type" ＝ ”deleteTalkHistory"
     *                "customerId" ＝ SelfID
     *                "offids" ＝ str;
     *                <p/>
     *                "Type" ＝ ”deleteAllTalkHistory"
     *                "customerId" ＝ SelfID
     *                "sms_target_id" ＝ self.customer.customerid //对方id
     */
    public static void deleteCustomPersonMessages(String chatUId, int type, String ids,
                                                  AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        if (type == 1) {// 全部删除
            params.put("Type", "deleteAllTalkHistory");
            params.put("sms_target_id", chatUId);
        } else {
            params.put("Type", "deleteTalkHistory");
            params.put("offids", ids);
        }
        params.put("customerId", SmartFoxClient.getLoginUserId());
        mAsyncHttpClient.get(mHttpUrls.DELETETALKHISTORYSERVLET, params,
                handler);
    }


    /**
     * 修改个人资料
     *
     * @param requestParams
     * @param handler
     */
    public static void doHttpUpdatePerson(RequestParams requestParams,
                                          AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.post(mHttpUrls.URL_UPDATE_CUSTOMINFO, requestParams,
                handler);
    }


    /**
     * 医生馆选择服务类型
     *
     * @param handler
     */
    public static void doHttpDoctorSelectService(AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        mAsyncHttpClient.get(mHttpUrls.FINDSERVICETYPE, params, handler);
    }


    /**
     * 科室查询
     *
     * @param handler
     */
    public static void doHttpFindkeshi(AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("flag", "2");
        mAsyncHttpClient.get(mHttpUrls.FindOfficeHasDoctor, params, handler);
    }


    /**
     * 支付储存信息
     *
     * @param customer_id
     * @param handler
     */
    public static void doHttpPayinfo(String customer_id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("Type", "getQianbao");
        params.put("CUSTOMER_ID", customer_id);
        params.put("SERVICE_TYPE_ID", "3");
        mAsyncHttpClient.get(mHttpUrls.SERVICEPATIENTSERVLET, params, handler);
//        mAsyncHttpClient.get("http://192.168.16.138:8080/DuoMeiHealth/ServicePatientServlet", params, handler);
    }

    /**
     * 删除订单
     *
     * @param customer_id
     * @param doctorid
     * @param handler
     */
    public static void doHttpDeleteOrder(String customer_id, String doctorid, String order_id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("Type", "deleteOrder");
        params.put("DOCTORID", doctorid);
        params.put("CUSTOMER_ID", customer_id);
        params.put("ORDER_ID", order_id);
//        mAsyncHttpClient.get(mHttpUrls.URL_SERVICESETSERVLET42, params, handler);
        mAsyncHttpClient.get(mHttpUrls.SERVICEPATIENTSERVLET, params, handler);
//        mAsyncHttpClient.get("http://192.168.16.138:8080/DuoMeiHealth/ServicePatientServlet", params, handler);
    }

    public static void doHttpFindmypatientdetails32(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrls.FINDMYSERVICELIST, params, handler);
    }


    /**
     * 患者端取消预约
     */
    public static void doHttpCancelOutpatient(String order_id, String customer_id, String doctorid, String reason, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("Type", "updateServiceOrder");
        params.put("ORDER_ID", order_id);
        params.put("CUSTOMER_ID", customer_id);
        params.put("DOCTORID", doctorid);
        params.put("CANCEL_REASON", reason);
//		addHttpHeader("username", SmartFoxClient.getLoginUserInfo().getUsername());
//		addHttpHeader("password", SmartFoxClient.getSmartFoxClient().getUserPas());
        mAsyncHttpClient.get(mHttpUrls.SERVICEPATIENTSERVLET, params, handler);
//        mAsyncHttpClient.get("http://192.168.16.138:8080/DuoMeiHealth/ServicePatientServlet", params, handler);
    }


    /**
     * 聊天初始化
     *
     * @param senderId
     * @param reciveId
     * @param handler
     */
    public static void doHttpInitChat(String senderId, String reciveId, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("CUSTOMER_ID", senderId);
        params.put("DOCTORID", reciveId);
        params.put("INTALK", "1");
        params.put("Type", "IsCanTalk");
        params.put("HZCHAT", "1");
        mAsyncHttpClient.get(mHttpUrls.HZSERVICESETSERVLET42, params, handler);
    }


    /**
     * 钱包设置
     *
     * @param params
     * @param handler
     */
    public static void doHttpWalletSetting(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrls.SETWALLETINFOSERVLET, params, handler);
    }


    /**
     * 钱包支付
     *
     * @param PAYMENT_PASSWORD
     */
    public static void doHttpWalletPay(String url, String PAYMENT_PASSWORD, AsyncHttpResponseHandler handler) {
        String[] str = url.split("&");
        RequestParams params = new RequestParams();
        for (int i = 0; i < str.length; i++) {
            String[] keys = str[i].split("=");
            if (keys.length == 1) {
                params.put(keys[0], "");
            } else {
                params.put(keys[0], keys[1]);
            }
        }
        params.put("PAYMENT_PASSWORD", PAYMENT_PASSWORD);
        params.put("VALID_MARK", HTalkApplication.APP_VALID_MARK);
        mAsyncHttpClient.post(mHttpUrls.HZSERVICESETSERVLET44, params, handler);
    }

    public static void doHttpWalletBalanceServlet(String SELECTDATE, String url, AsyncHttpResponseHandler handler) {
        String[] str = url.split("&");
        RequestParams params = new RequestParams();
        params.put("SELECTDATE", SELECTDATE);
        params.put("VALID_MARK", HTalkApplication.APP_VALID_MARK);
        for (int i = 0; i < str.length; i++) {
            String[] keys = str[i].split("=");
            if (keys[0].equalsIgnoreCase("type")) {
                params.put("Type", "getWalletBalance");
            } else {
                if (keys.length == 1) {
                    params.put(keys[0], "");
                } else {
                    params.put(keys[0], keys[1]);
                }
            }
        }
        mAsyncHttpClient.get(mHttpUrls.HZWalletBalanceServlet, params, handler);
    }

    public static void doHttpGetQianBao(String id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("CUSTOMER_ID", SmartFoxClient.getLoginUserId());
        params.put("Type", "getQianbao");
        params.put("VALID_MARK", HTalkApplication.APP_VALID_MARK);
        mAsyncHttpClient.get(mHttpUrls.HZWalletBalanceServlet, params, handler);
    }


    /**
     * 支付宝支付方式
     *
     * @param url
     * @param handler
     */
    public static void doHttpGetAliPay(String url, AsyncHttpResponseHandler handler) {
        String[] str = url.split("&");
        RequestParams params = new RequestParams();
        for (int i = 0; i < str.length; i++) {
            String[] keys = str[i].split("=");
            if (keys[0].equalsIgnoreCase("type")) {
                params.put("Type", "MedicallyRegistered330");
//                params.put("VALID_MARK", HTalkApplication.APP_VALID_MARK);
            } else {
                if (keys.length == 1)
                    params.put(keys[0], "");
                else
                    params.put(keys[0], keys[1]);
            }
        }
//        mAsyncHttpClient.post(mHttpUrls.HZSERVICESETSERVLET44, params, handler);
        mAsyncHttpClient.post(mHttpUrls.SERVICEPATIENTPAYSERVLET, params, handler);
//        mAsyncHttpClient.post("http://192.168.16.138:8080/DuoMeiHealth/SERVICEPATIENTPAYSERVLETayServlet", params, handler);
    }

    /**
     * 微信支付方式
     *
     * @param url
     * @param handler
     */
    public static void doHttpGetWeixinPay(String url, AsyncHttpResponseHandler handler) {
        String[] str = url.split("&");
        RequestParams params = new RequestParams();
        for (int i = 0; i < str.length; i++) {
            String[] keys = str[i].split("=");
            if (keys[0].equalsIgnoreCase("type")) {
                params.put("Type", "wx");
//                params.put("VALID_MARK", HTalkApplication.APP_VALID_MARK);
            } else {
                if (keys.length == 1)
                    params.put(keys[0], "");
                else
                    params.put(keys[0], keys[1]);
            }
        }
//        mAsyncHttpClient.post(mHttpUrls.HZSERVICESETSERVLET44, params, handler);
        mAsyncHttpClient.post(mHttpUrls.SERVICEPATIENTPAYSERVLET, params, handler);
//        mAsyncHttpClient.post("http://192.168.16.138:8080/DuoMeiHealth/SERVICEPATIENTPAYSERVLETayServlet", params, handler);
    }

    /**
     * 银联支付方式
     *
     * @param url
     * @param handler
     */
    public static void doHttpGetUnionPay(String url, AsyncHttpResponseHandler handler) {
        String[] str = url.split("&");
        RequestParams params = new RequestParams();
        for (int i = 0; i < str.length; i++) {
            String[] keys = str[i].split("=");
            if (keys[0].equalsIgnoreCase("type")) {
                params.put("Type", "MedicallyRegistered");
//                params.put("VALID_MARK", HTalkApplication.APP_VALID_MARK);
            } else {
                if (keys.length == 1)
                    params.put(keys[0], "");
                else
                    params.put(keys[0], keys[1]);
            }
        }
        mAsyncHttpClient.post(mHttpUrls.SERVICEPATIENTPAYSERVLET, params, handler);
//        mAsyncHttpClient.post("http://192.168.16.138:8080/DuoMeiHealth/ServicePatientPayServlet", params, handler);
    }

    /**
     * 余额支付方式
     *
     * @param url
     * @param handler
     */
    public static void doHttpGWalletPay(String url, AsyncHttpResponseHandler handler) {
        String[] str = url.split("&");
        RequestParams params = new RequestParams();
        for (int i = 0; i < str.length; i++) {
            String[] keys = str[i].split("=");
            if (keys[0].equalsIgnoreCase("type")) {
                params.put("Type", "ye");
//                params.put("VALID_MARK", HTalkApplication.APP_VALID_MARK);
            } else {
                if (keys.length == 1)
                    params.put(keys[0], "");
                else
                    params.put(keys[0], keys[1]);
            }
        }
        mAsyncHttpClient.post(mHttpUrls.SERVICEPATIENTPAYSERVLET, params, handler);
//        mAsyncHttpClient.post("http://192.168.16.138:8080/DuoMeiHealth/ServicePatientPayServlet", params, handler);
    }

    /**
     * 充值
     *
     * @param recharge_money
     * @param pay_type_id
     * @param handler        充值类型--10-支付宝 --20-银行类型
     */
    public static void doHttpTopUp(String recharge_money, String pay_type_id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("CUSTOMER_ID", SmartFoxClient.getLoginUserId());
        params.put("RECHARGE_MONEY", recharge_money);
        params.put("Type", "Recharge");
//		params.put("VALID_MARK", HTalkApplication.APP_VALID_MARK);
        params.put("PAY_TYPE_ID", pay_type_id);
//		mAsyncHttpClient.get(mHttpUrls.URL_SERVICESETSERVLET33, params, handler);
        mAsyncHttpClient.post(mHttpUrls.HZSERVICESETSERVLET44, params, handler);
    }


    /**
     * 编辑快速回复
     * Type=saveQuick
     * QUICK_REPLY_CONTENT 内容
     * CUSTOMER_ID 客户id
     * <p/>
     * 删除快速回复
     * Type=deleteQuick
     * QUICK_REPLY_ID 内容id
     * <p/>
     * 加载快速回复列表
     * Type=queryQuick
     * CUSTOMER_ID 客户id
     *
     * @param params
     * @param handler
     */
    public static void doHttpQUICKREPLYUPSAVSERVLET(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrls.QUICKREPLYUPSAVSERVLET, params, handler);
    }

    public static void doHttpQUICKREPLYSERVLET(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrls.QUICKREPLYSERVLET, params, handler);
    }


    /**
     * 查询已关注的人,医生已关注的是我的患者,患者已关注的是已关注的病友
     *
     * @param params
     * @param handler
     */
    public static void doHttpFINDMYFOCUSFRIENDS(RequestParams params,
                                                AsyncHttpResponseHandler handler) {
//			if(SmartFoxClient.getLoginUserInfo().isDoctor()){//是医生
//				mAsyncHttpClient.get(mHttpUrls.FINDMYPATIENTDETAILS32, params,handler);
//			}else{
////				mAsyncHttpClient.get(mHttpUrls.URL_FIND_FRIENDS, params,handler);
//				mAsyncHttpClient.get(mHttpUrls.GROUPCONSULTATIONLIST, params,handler);
//			}
        mAsyncHttpClient.get(mHttpUrls.GROUPCONSULTATIONLIST, params, handler);
    }

    /**
     * 医生端查询我的患者,查询到的数据是按照拼音排好序的
     *
     * @param params
     * @param handler
     */
    public static void doHttpFINDMYPatients(RequestParams params,
                                            AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrls.GROUPCONSULTATIONLIST200, params, handler);
//				mAsyncHttpClient.get(mHttpUrls.GROUPCONSULTATIONLIST, params,handler);
    }


    /**
     * 我的粉丝
     */
    public static void doHttpFINDMYFRIENDS32(RequestParams requestParams,
                                             AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrls.URL_FINDMYFRIENDS, requestParams, handler);
    }

    /**
     * 我的黑名单
     */
    public static void doHttpFINDMYBLACK32(RequestParams requestParams,
                                           AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrls.URL_FINDMYBLACKS, requestParams, handler);
    }


    public static void doHttpSERVICESETSERVLETRJ420(RequestParams params,
                                                    AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrls.SERVICEPATIENTSERVLET, params, handler);
    }


    public static void doHttpSERVICESETSERVLET44(RequestParams params,
                                                 AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrls.SERVICEPATIENTPAYSERVLET, params, handler);
    }


    public static void doHttpFRIENDSINFOSET(RequestParams params,
                                            AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrls.FRIENDSINFOSET, params, handler);
    }


    public static void doHttpDELETELIXIAN42(RequestParams params,
                                            AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrls.DELETELIXIAN42, params, handler);
    }


    /**
     * 加载会诊病历模板接口
     * TempletClassMRTServlet?OPTION=6&CONSULTATIONID=1
     *
     * @param option       itemId   基层医生推荐模板给患者是需要传1,其他情况传递null
     * @param consultionId 会诊id
     * @param handler
     */
    public static void doHttpConsultionCaseTemplate(String option, String itemId, String consultionId,
                                                    AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        if (itemId != null) {
            params.put("TEMID", itemId);
        }
        params.put("OPTION", option);
        if ("4".equals(option))
            params.put("CUSTID", SmartFoxClient.getLoginUserId());
        params.put("CONSULTATIONID", consultionId);
        mAsyncHttpClient.get(mHttpUrls.TEMPLETCLASSMRTSERVLET, params, handler);
    }

    /**
     * 医生端共享六一健康
     */
    public static void doHttpConsultionCaseTemplateShare(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrls.TEMPLETCLASSMRTSERVLET, params, handler);
    }

    /**
     * 上传病历模板接口
     */
    public static void doHttpPostConsultionCaseTemplate(RequestParams params,
                                                        AsyncHttpResponseHandler handler) {
//			mAsyncHttpClient.post("http://192.168.16.118:8080/DuoMeiHealth/SaveOrEditMedicalRecordServlet2", params,handler);
        mAsyncHttpClient.post(mHttpUrls.SAVEOREDITMEDICALRECORDSERVLET2, params, handler);
    }


    public static void doHttpConsultationCenterDoctorList(int pagesize, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("TYPE", "consultationCenterDoctorList");
        params.put("CONSULTATION_CENTER_ID", HTalkApplication.APP_CONSULTATION_CENTERID);
        params.put("PAGESIZE", pagesize + "");
//		params.put("PAGENUM", "10");
        params.put("PAGENUM", "20");
        params.put("VALID_MARK", HTalkApplication.APP_VALID_MARK);
        mAsyncHttpClient.get(mHttpUrls.GROUPCONSULTATIONLIST, params, handler);
    }


    /**
     * 申请会诊
     */
    public static void doHttpApplyConsultation(RequestParams params, AsyncHttpResponseHandler handler) {

//			mAsyncHttpClient.post("http://192.168.16.45:8899/DuoMeiHealth/ApplyConsultation?PARAMETER="+json, params, handler);
//			mAsyncHttpClient.post("http://192.168.16.45:8899/DuoMeiHealth/ApplyConsultation", params, handler);
        mAsyncHttpClient.post(mHttpUrls.APPLYCONSULTATION, params, handler);
    }

    /**
     * 申请会诊验证码
     *
     * @param phone
     * @param handler
     */
    public static void doHttpSendVerificationCode(String phone, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("CONSULTATION_CENTER_ID", HTalkApplication.APP_CONSULTATION_CENTERID);
        params.put("TYPE", "sendVerificationCode");
        params.put("PATIENTTEL_PHONE", phone);
        mAsyncHttpClient.post(mHttpUrls.GROUPCONSULTATIONLIST, params, handler);
    }

    /**
     * 基层医生将病历提交给专家接口
     *
     * @param handler
     */
    public static void doHttpSubmitCaseTemplate(RequestParams params,
                                                AsyncHttpResponseHandler handler) {
//			mAsyncHttpClient.post("http://192.168.16.118:8080/DuoMeiHealth/SaveOrEditMedicalRecordServlet", params,handler);
        mAsyncHttpClient.post(mHttpUrls.SERVERDETAILSERVLET, params, handler);
    }

    /**
     * 资深医生会诊医生列表
     */
    public static void doHttpExpertAssistant(int pagesize, ObjectHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("TYPE", "consultationCenterDoctorAssiList");
        params.put("CONSULTATION_CENTER_ID", HTalkApplication.APP_CONSULTATION_CENTERID);
        params.put("PAGESIZE", pagesize + "");
        params.put("PAGENUM", "20");
        params.put("VALID_MARK", HTalkApplication.APP_VALID_MARK);
        params.put("CUSTOMERID", SmartFoxClient.getLoginUserId());
        mAsyncHttpClient.get(mHttpUrls.GROUPCONSULTATIONLIST, params, handler);
    }


    /**
     * 医生接诊
     *
     * @param
     * @param option
     * @param handler
     */
    public static void doHttpReceiveConsultation(int consultationId, int option, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("CONSULTATIONID", consultationId + "");
        params.put("OPTION", "" + option);
        params.put("DOCTORID", SmartFoxClient.getLoginUserId());
        mAsyncHttpClient.get(mHttpUrls.SERVERDETAILSERVLET, params, handler);
    }

    /**
     * 患者个人中心-我的服务-申请单
     *
     * @param consultationId
     * @param handler
     */
    public static void doHttpServerDetailServlet(String consultationId, ObjectHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("OPTION", "1");
        params.put("CUSTID", SmartFoxClient.getLoginUserId());
        params.put("CONSULTATIONID", consultationId);
        mAsyncHttpClient.get(mHttpUrls.SERVERDETAILSERVLET, params, handler);
    }


    /**
     * 六一健康退款
     * 退款到钱包       ConsultationBackServlet?OPTION=1&CONSULTATIONID=1
     * 退款到支付宝
     * ConsultationBackServlet?OPTION=2&CONSULTATIONID=1&ACCOUNT=517@qq.com&PHONE=18811170904
     * 退款到银行卡
     * ConsultationBackServlet?OPTION=3&CONSULTATIONID=1&ACCOUNT=6222...&PHONE=18811170904&NAME=陈琴&OPENADDR=开户行地址
     */
    public static void doHttpConsultationBackPay(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrls.CONSULTATIONBACKSERVLET, params, handler);
    }

    /**
     * 查看流程
     *
     * @param consultationId
     * @param handler
     */
    public static void doHttpfindConsuStatusList(String consultationId, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("TYPE", "findConsuStatusList");
        params.put("CONSULTATION_ID", consultationId);
        mAsyncHttpClient.post(mHttpUrls.GROUPCONSULTATIONLIST, params, handler);
    }


    /**
     * 获取删除服务/拒绝服务原因列表
     *
     * @param reasonType
     * @param handler
     */
    public static void doHttpGetCancelReason(String reasonType, ObjectHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("OPTION", "2");
        params.put("REASONTYPE", reasonType);
        mAsyncHttpClient.post(mHttpUrls.SERVERDETAILSERVLET, params, handler);
    }

    /**
     * 发送删除服务/拒绝服务原因列表
     */
    public static void doHttpPostCancelReason(String reason, String conID, String reasonstr, String customerId, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("OPTION", "3");
        params.put("REASON", reason);
        params.put("CONSULTATIONID", conID);
        params.put("CUSTID", customerId);
        params.put("REASONSTR", reasonstr);
//			mAsyncHttpClient.post(mHttpUrls.SERVERDETAILSERVLET,params,handler);
        mAsyncHttpClient.post(mHttpUrls.SERVERDETAILSERVLET, params, handler);
    }

    /**
     * 发送患者疑问
     *
     * @param params
     * @param handler
     */
    public static void doHttpPostConsultQuestion(RequestParams params,
                                                 AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.post(mHttpUrls.ASKQUESTIONSERVLET, params, handler);
    }

    /**
     * 查看会诊意见
     */
    public static void doHttpShowOpinion(String conID, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("OPTION", "9");
        params.put("CONSULTATIONID", conID);
        mAsyncHttpClient.post(mHttpUrls.SERVERDETAILSERVLET, params, handler);
    }

    /**
     * 基层医生服务细节
     *
     * @param handler
     */
    public static void doHttpDoctorService(RequestParams params,
                                           AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.post(mHttpUrls.SERVERDETAILSERVLET, params, handler);
    }

    /**
     * 六一健康动态消息接口
     * http://220.194.46.204:8080/DuoMeiHealth/GroupConsultationList?TYPE=findConsuInfo&CONSULTATION_CENTER_ID=1&INFOID=256&CUSTOMERID=2403
     *
     * @param params  参数
     * @param handler
     */
    public static void doHttpGroupConsultationList(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrls.GROUPCONSULTATIONLIST100, params, handler);
    }


    /**
     * 会诊服务列表弹出框接口
     *
     * @param consultationid
     * @param handler
     */
    public static void doHttpServerDetailServletChatMessage(String consultationid, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("OPTION", "13");
        params.put("CONSULTATIONID", consultationid);
        params.put("CUSTID", SmartFoxClient.getLoginUserId());
        mAsyncHttpClient.post(mHttpUrls.SERVERDETAILSERVLET, params, handler);
    }


    /**
     * 批量取消收藏动态消息
     */
    public static void doHttpCancelCollectedInfoBatch(String infoIds, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("TYPE", "cancelCollectedInfoBatch");
        params.put("CUSTOMERID", SmartFoxClient.getLoginUserId());
        params.put("INFOIDS", infoIds);
        mAsyncHttpClient.post(mHttpUrls.GROUPCONSULTATIONLIST, params, handler);
    }


    /**
     * 病历讨论-同行共享
     *
     * @param handler
     * @param pagesize
     */
    public static void doHttpMedicalCaseDiscussionShare(int pagesize, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("TYPE", "medicalCaseDiscussionShare");
        params.put("PAGESIZE", pagesize + "");
        params.put("PAGENUM", "20");
        params.put("CUSTOMERID", SmartFoxClient.getLoginUserId());
        params.put("CONSULTATION_CENTER_ID", HTalkApplication.APP_CONSULTATION_CENTERID);
        mAsyncHttpClient.post(mHttpUrls.GROUPCONSULTATIONLIST200, params, handler);
    }

    /**
     * 病历讨论-我关注的
     *
     * @param pagesize
     * @param handler
     */
    public static void doHttpMedicalCaseDiscussionFocus(int pagesize, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("TYPE", "medicalCaseDiscussionFocus");
        params.put("PAGESIZE", pagesize + "");
        params.put("PAGENUM", "20");
        params.put("CUSTOMERID", SmartFoxClient.getLoginUserId());
        params.put("CONSULTATION_CENTER_ID", HTalkApplication.APP_CONSULTATION_CENTERID);
        mAsyncHttpClient.post(mHttpUrls.GROUPCONSULTATIONLIST200, params, handler);
    }

    /**
     * 病历讨论-我共享的
     *
     * @param pagesize
     * @param handler
     */
    public static void doHttpMyMedicalCaseDiscussionShare(int pagesize, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("TYPE", "myMedicalCaseDiscussionShare");
        params.put("PAGESIZE", pagesize + "");
        params.put("PAGENUM", "20");
        params.put("CUSTOMERID", SmartFoxClient.getLoginUserId());
        params.put("CONSULTATION_CENTER_ID", HTalkApplication.APP_CONSULTATION_CENTERID);
        mAsyncHttpClient.post(mHttpUrls.GROUPCONSULTATIONLIST200, params, handler);
    }


    /**
     * 患者端会诊服务列表
     *
     * @param type
     * @param pagesize
     * @param handler
     */
    //192.168.16.44:8899/DuoMeiHealth/FindMyConsuServiceList?TERMINAL_TYPE=&TYPE=&PAGESIZE=&PAGENUM=&CUSTOMERID=
    public static void doHttpFindMyConsuServiceList1(int type, int pagesize, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("TERMINAL_TYPE", "findExpertByPatient");
        params.put("TYPE", type + "");
        params.put("PAGESIZE", pagesize + "");
        params.put("PAGENUM", "15");
        params.put("CUSTOMERID", SmartFoxClient.getLoginUserId());
        params.put("VALID_MARK", HTalkApplication.APP_VALID_MARK);
        mAsyncHttpClient.post(mHttpUrls.FINDMYCONSUSERVICELIST, params, handler);
    }


    /**
     * 病历成员
     */
    public static void doHttpCaseMembers(String consultation, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("CONSULTATIONID", consultation);
        params.put("CUSTID", SmartFoxClient.getLoginUserId());
        params.put("OPTION", "13");
        mAsyncHttpClient.get(mHttpUrls.SERVERDETAILSERVLET, params, handler);
    }


/*--------------------------------------以下为新版六一健康接口----------------------------------------------*/


    /**
     * 患者端选择科室列表
     *
     * @param name
     * @param handler
     */
    public static void doHttpFindOfficePatient(String customerid, String name, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("TYPE", "findOfficePatient");
        params.put("CONSULTATION_CENTER_ID", HTalkApplication.APP_CONSULTATION_CENTERID);
        params.put("OFFICENAME", name);
        params.put("CUSTOMERID", customerid);
        mAsyncHttpClient.get(mHttpUrls.DUOMEIHEALTH, params, handler);
    }

    /**
     * 患者端查询可服务的专家人数
     *
     * @param upperOfficeId
     * @param handler
     */
    public static void doHttpfindDocNum(String customerid, String upperOfficeId, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("TYPE", "findDocNum");
        params.put("CONSULTATION_CENTER_ID", HTalkApplication.APP_CONSULTATION_CENTERID);
        params.put("UPPER_OFFICE_ID", upperOfficeId);
        params.put("CUSTOMERID", customerid);
        mAsyncHttpClient.get(mHttpUrls.DUOMEIHEALTH, params, handler);
    }

    /**
     * 医生专家资料获取
     *
     * @param type
     * @param handler
     */
    public static void doHttpFindInfo(String customerid, String customerId, String type, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("TYPE", type);
        params.put("CONSULTATION_CENTER_ID", HTalkApplication.APP_CONSULTATION_CENTERID);
        params.put("MYCUSTOMERID", customerid);
        params.put("CUSTOMERID", customerId);
        params.put("FLAG", "0");
        mAsyncHttpClient.get(mHttpUrls.DUOMEIHEALTH, params, handler);
    }

    /**
     * 申请会诊验证码
     *
     * @param phoneNum
     * @param handler
     */
    public static void doHttpSendApplyConsuCode(String isVisitor, String userId, String phoneNum, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("TYPE", "sendApplyConsuCode");
        params.put("PHONENUM", phoneNum);
//		params.put("CONSULTATION_CENTER_ID", HTalkApplication.APP_CONSULTATION_CENTERID);
        params.put("LOGINMK", isVisitor);
        params.put("CUSTOMERID", userId);
        mAsyncHttpClient.get(mHttpUrls.DUOMEIHEALTH, params, handler);
    }

    /**
     * 修改申请会诊验证码
     *
     * @param phoneNum
     * @param handler
     */
    public static void doHttpConsultationInfoSet(String phoneNum, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("TYPE", "sendUpdateConsuCode");
        params.put("PHONENUM", phoneNum);
//		params.put("CONSULTATION_CENTER_ID", HTalkApplication.APP_CONSULTATION_CENTERID);
//        params.put("CUSTOMERID", SmartFoxClient.getLoginUserId());
        mAsyncHttpClient.get(mHttpUrls.DUOMEIHEALTH, params, handler);
    }

    /**
     * ConsultationInfoSet接口
     * 获取专家列表
     *
     * @param params
     * @param handler
     */
    public static void doGetConsultationInfoSet(RequestParams params, AsyncHttpResponseHandler handler) {

        mAsyncHttpClient.get(mHttpUrls.DUOMEIHEALTH, params, handler);
    }

    /**
     * ConsultationInfoSet接口
     * 获取专家列表
     */
    public static void doGetConsultationInfoSet(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.DUOMEIHEALTH, params, callback, tag);
    }

    /**
     * wss接口
     * 获取专家列表
     */
    public static void doGetWss(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.DUOMEIHEALTH2, params, callback, tag);
    }

    /**
     * wss接口
     * 获取专家列表
     */
    public static void doGetWorks(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.COMMONURL2, params, callback, tag);
    }

    /**
     * wss接口
     * 工作站咨询
     */
    public static void doGetWorksZixun(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.COMMONURL3, params, callback, tag);
    }

    /**
     * 工作站健康讲堂
     */
    public static void doGetHuanZheWorkSiteClass(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.HuanZheWorkSiteClass, params, callback, tag);
    }
    /**
     * 医生个人健康讲堂
     */
    public static void doGetPersonClassroom(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.PersonClassroom, params, callback, tag);
    }

   /**
     * 找机构
     */
    public static void doGetInstitutionsServlet(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.InstitutionsServlet, params, callback, tag);
    }

    /**
     * 找机构
     */
    public static void doGetPatientHome(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.PATIENT_HOME, params, callback, tag);
    }

    /**
     * see接口
     * 获取城市列表
     */
    public static void doGetProvinceSee(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.GET_PROVINCE_DATA, params, callback, tag);
    }

    /**
     * ServicePatientServlet接口
     * 患者端门诊详情
     */
    public static void doGetServicePatientServlet(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.SERVICEPATIENTSERVLET, params, callback, tag);
//		OkHttpClientManager.getAsyn("http://192.168.16.138:8080/DuoMeiHealth/ServicePatientServlet", params, callback, tag);
    }

    /**
     * SERVICEPATIENTPAYSERVLET
     * 患者端门诊详情
     */
    public static void doGetServicePatientPayServlet(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.SERVICEPATIENTPAYSERVLET, params, callback, tag);
//		OkHttpClientManager.getAsyn("http://192.168.16.138:8080/DuoMeiHealth/ServicePatientPayServlet", params, callback, tag);
    }

    /**
     * ConsultationCouponsCount
     * 支付获取可用优惠券数量
     */
    public static void doGetConsultationCouponsCount(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.CONSULTATIONCOUPONSCOUNT, params, callback, tag);
    }

    /**
     * ConsultationCouponsListServlet
     * 支付获取可用优惠券列表
     */
    public static void doGetConsultationCouponsList(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.CONSULTATIONCOUPONSLIST, params, callback, tag);
    }


    /**
     * TalkHistoryServlet接口
     * 聊天历史列表
     */
    public static void doGetTalkHistoryServlet(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.TALKHISTORYSERVLET, params, callback, tag);
    }

    /**
     * ConsultationBuyServlet接口
     * 支付
     */
    public static void doGetConsultationBuyServlet(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
//		OkHttpClientManager.getAsyn("http://192.168.16.138:8080/DuoMeiHealth/ConsultationBuyServlet",params,callback,tag);
        OkHttpClientManager.getAsyn(mHttpUrls.CONSULTATIONbUYSERVLET, params, callback, tag);
    }

    /**
     * 购买具体服务支付接口
     *
     * @param params
     * @param callback
     * @param tag
     */
    public static void doGetConsultationBuyStudioServlet(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
//		OkHttpClientManager.getAsyn("http://192.168.16.138:8080/DuoMeiHealth/ConsultationBuyServlet",params,callback,tag);
        OkHttpClientManager.getAsyn(mHttpUrls.BUYDOCTORSERVICE, params, callback, tag);
    }
/**
     * 健康讲堂购买具体服务支付接口
     *
     * @param params
     * @param callback
     * @param tag
     * @param payType 判断是打赏还是健康讲堂的购买
     */
public static void doGetclassroomServlet(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag, String payType) {
//		OkHttpClientManager.getAsyn("http://192.168.16.138:8080/DuoMeiHealth/ConsultationBuyServlet",params,callback,tag);
    if (ServiceType.DL.equals(payType))
        OkHttpClientManager.getAsyn(mHttpUrls.classroomServlet, params, callback, tag);
    else if (ServiceType.DS.equals(payType))
        OkHttpClientManager.getAsyn(mHttpUrls.PersonClassroom, params, callback, tag);
}

    /**
     * ConsultationInfoSet接口
     * 获取专家列表
     */
    public static void doFindbeforeConsuPatientInfo(String customerid, OkHttpClientManager.ResultCallback callback, Object tag) {
        BasicNameValuePair param = new BasicNameValuePair("CUSTOMERID", customerid);
        BasicNameValuePair param1 = new BasicNameValuePair("TYPE", "findbeforeConsuPatientInfo");
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(param);
        params.add(param1);
        OkHttpClientManager.getAsyn(mHttpUrls.DUOMEIHEALTH, params, callback, tag);
    }


    /**
     * 患者申请会诊
     *
     * @param params
     * @param handler
     */
    public static void doHttpApplyConsult(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.post(mHttpUrls.APPLYCONSULT, params, handler);
//      mAsyncHttpClient.post("http://192.168.16.45:8899/DuoMeiHealth/ApplyConsuByPatientFindExpert", params, handler);
    }   /**
     * 患者创建会诊
     *
     * @param params
     * @param handler
     */
    public static void doHttpSaveApplyConsult(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.post(mHttpUrls.SAVEPATIENTCASESERVLEt, params, handler);
//      mAsyncHttpClient.post("http://192.168.16.45:8899/DuoMeiHealth/ApplyConsuByPatientFindExpert", params, handler);
    }

    /**
     * 患者修改会诊单信息
     *
     * @param params
     * @param handler
     */
    public static void doHttpUpdateConsultationById(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.post(mHttpUrls.UPDATACONSULTATIONBYID, params, handler);
//        mAsyncHttpClient.post("http://192.168.16.45:8899/DuoMeiHealth/UpdateConsultationById", params, handler);
    }  /**
     * 患者修改会诊单信息
     *
     * @param params
     * @param handler
     */
    public static void doHttpCaseUpdateConsultationById(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.post(mHttpUrls.UPDATEPATIENTCASESERVLET, params, handler);
//        mAsyncHttpClient.post("http://192.168.16.45:8899/DuoMeiHealth/UpdateConsultationById", params, handler);
    }
    /**
     * 患者会诊列表
     */
    public static void OKHttpConsultationList(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.FINDMYCONSUSERVICELIST, params, callback, tag);
    }

    /**
     * 患者会诊订单详情
     */
    public static void OKHttpConsultInfo(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.CONSULTATIONDETAILSSERVLET, params, callback, tag);
    }   /**
     * 病历详情
     */
    public static void OKHttpCaseInfo(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.TALKHISTORYSERVLETS, params, callback, tag);
    }

    /**
     * 查询个人资料
     */
    public static void OKHttpFindCustomerInfo(OkHttpClientManager.ResultCallback callback, Object tag) {
        List<BasicNameValuePair> params = new ArrayList<>();
        BasicNameValuePair param = new BasicNameValuePair("TYPE", "findCustomerInfo");
        BasicNameValuePair param1 = new BasicNameValuePair("CUSTOMERID", LoginServiceManeger.instance().getLoginEntity().getId());
        params.add(param);
        params.add(param1);
        OkHttpClientManager.getAsyn(mHttpUrls.DUOMEIHEALTH, params, callback, tag);
    }

    /**
     * 患者订单提示
     */
    public static void OKHttpOrderTip(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.DUOMEIHEALTH, params, callback, tag);
    }

    /**
     * 患者修改个人资料
     *
     * @param fileKey
     * @param file
     * @param params
     * @param callback
     * @param tag
     */
    public static void doPostUpdatePatientInfo(String fileKey, File file, OkHttpClientManager.Param[] params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getUploadDelegate().postAsyn(mHttpUrls.UPDATAPATIENTINFO, fileKey, file, params, callback, tag);
//		OkHttpClientManager.getUploadDelegate().postAsyn("http://192.168.16.45:8899/DuoMeiHealth/UpdatePatientInfo",fileKey,file,params,callback,tag);

    }

    /**
     * 患者端修改个人资料发送验证码
     *
     * @param phonenum
     * @param callback
     * @param tag
     */
    //192.168.16.45:8899/DuoMeiHealth/ConsultationInfoSet?TYPE=sendUpdatePatientInfoCode&PHONENUM=
    public static void OKHttpSendUpdatePatientInfoCode(String phonenum, OkHttpClientManager.ResultCallback callback, Object tag) {
        List<BasicNameValuePair> params = new ArrayList<>();
        BasicNameValuePair param = new BasicNameValuePair("PHONENUM", phonenum);
        BasicNameValuePair param1 = new BasicNameValuePair("TYPE", "sendUpdatePatientInfoCode");
        params.add(param);
        params.add(param1);
//        OkHttpClientManager.getAsyn("http://192.168.16.45:8899/DuoMeiHealth/ConsultationInfoSet", params, callback, tag);
        OkHttpClientManager.getAsyn(mHttpUrls.DUOMEIHEALTH, params, callback, tag);
    }

    /**
     * 患者确认信息
     */
    public static void OKHttpConfirm(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.SERVERDETAILSERVLET, params, callback, tag);
    }

    /**
     * 患者查看会诊意见
     */
    public static void OKHttpgetOpinion(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.SERVERDETAILSERVLET, params, callback, tag);
//		OkHttpClientManager.postAsyn(mHttpUrls.SERVERDETAILSERVLET, map, callback, tag);
    }

    /**
     * 患者提醒专家
     */
    public static void OKHttpmindExpert(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
//		OkHttpClientManager.getAsyn(mHttpUrls.SERVERDETAILSERVLET,params,callback,tag);
        OkHttpClientManager.getAsyn(mHttpUrls.SERVERDETAILSERVLET, params, callback, tag);
    }

    /**
     * 患者收藏医生
     */
    public static void OKHttpCollectedPerson(String TYPE, String doctorid, String customerid, OkHttpClientManager.ResultCallback callback, Object tag) {
        BasicNameValuePair param = new BasicNameValuePair("TYPE", TYPE);
        BasicNameValuePair param1 = new BasicNameValuePair("CUSTOMERID", customerid);
        BasicNameValuePair param2 = new BasicNameValuePair("DOCTORID", doctorid);
        BasicNameValuePair param3 = new BasicNameValuePair("RELTYPE", "D");
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(param);
        params.add(param1);
        params.add(param2);
        params.add(param3);
        OkHttpClientManager.getAsyn(mHttpUrls.DUOMEIHEALTH, params, callback, tag);
    }

    /**
     * 找回修改密码
     *
     * @param phone
     * @param password
     * @param code
     * @param callback
     * @param tag
     */
    public static void OKHttpUpdateFindPassword(String phone, String password, String code, OkHttpClientManager.ResultCallback callback, Object tag) {
        List<BasicNameValuePair> params = new ArrayList<>();
        BasicNameValuePair param = new BasicNameValuePair("PHONENUM", phone);
        BasicNameValuePair param1 = new BasicNameValuePair("TYPE", "updateFindPassword");
        BasicNameValuePair param2 = new BasicNameValuePair("PASSWORD", password);
        BasicNameValuePair param3 = new BasicNameValuePair("VERIFICATION_CODE", code);
        params.add(param);
        params.add(param1);
        params.add(param2);
        params.add(param3);
        OkHttpClientManager.getAsyn(mHttpUrls.DUOMEIHEALTH, params, callback, tag);
    }


    /**
     * 修改密码发送验证码
     *
     * @param customerid
     * @param callback
     * @param tag
     */
    public static void OKHttpSendUpdatePasswordCode(String customerid, OkHttpClientManager.ResultCallback callback, Object tag) {
        List<BasicNameValuePair> params = new ArrayList<>();
        BasicNameValuePair param = new BasicNameValuePair("CUSTOMERID", customerid);
        BasicNameValuePair param1 = new BasicNameValuePair("TYPE", "sendUpdatePasswordCode");
        params.add(param);
        params.add(param1);
        OkHttpClientManager.getAsyn(mHttpUrls.DUOMEIHEALTH, params, callback, tag);
    }

    /**
     * 修改密码
     *
     * @param customerid
     * @param password
     * @param code
     * @param callback
     * @param tag
     */
    public static void OKHttpUpdatePassword(String customerid, String password, String code, OkHttpClientManager.ResultCallback callback, Object tag) {
        List<BasicNameValuePair> params = new ArrayList<>();
        BasicNameValuePair param = new BasicNameValuePair("CUSTOMERID", customerid);
        BasicNameValuePair param1 = new BasicNameValuePair("TYPE", "updatePassword");
        BasicNameValuePair param2 = new BasicNameValuePair("PASSWORD", password);
        BasicNameValuePair param3 = new BasicNameValuePair("VERIFICATION_CODE", code);
        params.add(param);
        params.add(param1);
        params.add(param2);
        params.add(param3);
        OkHttpClientManager.getAsyn(mHttpUrls.DUOMEIHEALTH, params, callback, tag);
    }

    /**
     * 发送已读
     */
    public static void OKHttpSendRead(String conId, OkHttpClientManager.ResultCallback callback, Object tag) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("TYPE", "updateNewChange"));
        params.add(new BasicNameValuePair("FLAG", "0"));
        params.add(new BasicNameValuePair("CONSULTATIONID", conId));
        OkHttpClientManager.getAsyn(mHttpUrls.DUOMEIHEALTH, params, callback, tag);
    }

    /**
     * HZPushManagementServlet接口
     * 群聊消息
     */
    public static void doGetHZPushManagementServlet(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.URL_HZPUSHMANGAGER, params, callback, tag);
    }

    /**
     * 根据Id查询个人资料(新版六一健康)
     * 192.168.16.45:8899/DuoMeiHealth/ConsultationInfoSet?TYPE=findPatientInfo&CUSTOMERID=
     *
     * @param friendId
     * @param friendId
     * @param callback
     */
    public static void doGetCustomerInfoByCustId(String friendId, OkHttpClientManager.ResultCallback callback, Object tag) {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("TYPE", "findPatientInfo"));
        pairs.add(new BasicNameValuePair("CUSTOMERID", friendId));
        pairs.add(new BasicNameValuePair("FLAG", "0"));
        OkHttpClientManager.getAsyn(mHttpUrls.DUOMEIHEALTH, pairs, callback, tag);
    }

    /**
     * 绑定手机
     *
     * @param customerid
     * @param phone
     * @param code
     * @param callback
     * @param tag
     */
    public static void OKHttpBindPhoneNum(String customerid, String phone, String code, OkHttpClientManager.ResultCallback callback, Object tag) {
        List<BasicNameValuePair> params = new ArrayList<>();
        BasicNameValuePair param = new BasicNameValuePair("CUSTOMERID", customerid);
        BasicNameValuePair param1 = new BasicNameValuePair("TYPE", "bindPhoneNum");
        BasicNameValuePair param2 = new BasicNameValuePair("PHONENUM", phone);
        BasicNameValuePair param3 = new BasicNameValuePair("VERIFICATION_CODE", code);
        params.add(param);
        params.add(param1);
        params.add(param2);
        params.add(param3);
//        OkHttpClientManager.getAsyn("http://192.168.16.45:8899/DuoMeiHealth/ConsultationInfoSet", params, callback, tag);
        OkHttpClientManager.getAsyn(mHttpUrls.DUOMEIHEALTH, params, callback, tag);
    }

    /**
     * 绑定手机发送验证码
     *
     * @param phone
     * @param callback
     * @param tag
     */
    public static void OKHttpSendBindPhoneNumCode(String phone, OkHttpClientManager.ResultCallback callback, Object tag) {
        List<BasicNameValuePair> params = new ArrayList<>();
        BasicNameValuePair param = new BasicNameValuePair("PHONENUM", phone);
        BasicNameValuePair param1 = new BasicNameValuePair("TYPE", "sendBindPhoneNumCode");
        params.add(param);
        params.add(param1);
//        OkHttpClientManager.getAsyn("http://192.168.16.45:8899/DuoMeiHealth/ConsultationInfoSet", params, callback, tag);
        OkHttpClientManager.getAsyn(mHttpUrls.DUOMEIHEALTH, params, callback, tag);
    }

    /**
     * 根据Id查询个人资料
     *
     * @param qrCode
     */
    public static void doHttpFindCustomerByQrCode(String qrCode, String clientType, MyResultCallback callback, Object tag) {
        List<BasicNameValuePair> params = new ArrayList<>();
        BasicNameValuePair param = new BasicNameValuePair("QRCODE", qrCode);
        BasicNameValuePair param1 = new BasicNameValuePair("TYPE", "findCustomerByQrCode");
        BasicNameValuePair param4 = new BasicNameValuePair("client_type", clientType);
        BasicNameValuePair param2 = new BasicNameValuePair("MYCUSTOMERID", LoginServiceManeger.instance().getLoginEntity().getId());
        BasicNameValuePair param3 = new BasicNameValuePair("CONSULTATION_CENTER_ID", HTalkApplication.APP_CONSULTATION_CENTERID);
        params.add(param);
        params.add(param1);
        params.add(param2);
        params.add(param3);
        params.add(param4);
        OkHttpClientManager.getAsyn(mHttpUrls.DUOMEIHEALTH, params, callback, tag);
    }

    /**
     * 意见反馈
     *
     * @param conent
     * @param callback
     * @param tag
     */
    public static void OKHttpSaveFeedBackHZ(String conent, OkHttpClientManager.ResultCallback callback, Object tag) {
        List<BasicNameValuePair> params = new ArrayList<>();
        BasicNameValuePair param = new BasicNameValuePair("CUSTOMERID", LoginServiceManeger.instance().getLoginUserId());
        BasicNameValuePair param1 = new BasicNameValuePair("TYPE", "saveFeedBackHZ");
        BasicNameValuePair param2 = new BasicNameValuePair("CONSULTATION_CENTER_ID", HTalkApplication.APP_CONSULTATION_CENTERID);
        BasicNameValuePair param3 = new BasicNameValuePair("CONTENT", conent);
        params.add(param);
        params.add(param1);
        params.add(param2);
        params.add(param3);
        OkHttpClientManager.getAsyn(mHttpUrls.DUOMEIHEALTH, params, callback, tag);
    }

    /**
     * 关联手机
     */
    public static void OKHttpConPhone(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.DUOMEIHEALTH, params, callback, tag);
    }

    /**
     * 患者加载账户信息
     */
    public static void OKHttpgetAccountInfo(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.SERVERDETAILSERVLET, params, callback, tag);
//		OkHttpClientManager.postAsyn(mHttpUrls.SERVERDETAILSERVLET, map, callback, tag);
    }

    /**
     * 新闻
     */
    public static void OKHttpGetTitle(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.FRIENDSINFOSET, params, callback, tag);
    }

    /**
     * 公益活动
     */
    public static void OKHttpGetEntry(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.HZPUSHMANAGEMENTSERVLET, params, callback, tag);
    }

   /*----------------------2016.12.8 新版本六一接口----------------------------*/

   /*-------医教计划------------*/

    /**
     * 添加宝贝
     */
    public static void OKHttpSaveInformation(String fileKey, File file, OkHttpClientManager.Param[] params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getUploadDelegate().postAsyn(mHttpUrls.DOCTORADDBABY, fileKey, file, params, callback, tag);
    }

    /**
     * 修改宝贝
     *
     * @param fileKey
     * @param file
     * @param params
     * @param callback
     * @param tag
     */
    public static void OKHttpModityInformation(String fileKey, File file, OkHttpClientManager.Param[] params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getUploadDelegate().postAsyn(mHttpUrls.DOCTORMODIFYBABY, fileKey, file, params, callback, tag);
    }

    /**
     * 医教联盟首页数据
     */
    public static void OKHttpGetPlanList(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORTEACH, map, callback, tag);
    }

    /**
     * 删除宝贝
     *
     * @param map
     * @param callback
     * @param tag
     */
    public static void OKHttpDelectBaby(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORSEARCH, map, callback, tag);
    }

    /**
     * 医教计划成员
     *
     * @param
     */
    public static void OKHttpGetMemberList(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORTEACHMEMBER, map, callback, tag);
    }

    /**
     * 查询单个宝贝详细信息
     *
     * @param
     */
//    public static void OKHttpGetBabyInfo(String children_id, AsyncHttpResponseHandler handler) {
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("children_id", children_id);
//        mAsyncHttpClient.post(mHttpUrls.DOCTORBABYINFO, requestParams, handler);
//    }
    public static void OKHttpGetBabyInfo(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORBABYINFO, map, callback, tag);
    }

    /**
     * 根据电话号码查询客户
     *
     * @param phone   手机号码
     * @param handler
     */
    public static void OKHttpGetPhoneCustom(String phone, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("phone", phone);
        mAsyncHttpClient.post(mHttpUrls.DOCTORPHONECUSTOM, requestParams, handler);
    }

    /**
     * 添加成员
     */
    public static void OKHttpAddMember(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORADDMEMBER, map, callback, tag);
    }

    /**
     * 添加计划
     */

    public static void OKHttpAddPlan(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORADDPLAN, map, callback, tag);
    }

    /**
     * 填写关爱记录
     */
    public static void OKHttpAddCare(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORADDCARE, map, callback, tag);
    }

    /**
     * 删除关爱计划
     *
     * @param map
     * @param callback
     * @param tag
     */
    public static void OKHttpDelectPlan(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORSELECTBABY, map, callback, tag);
    }

    /**
     * 修改成员备注
     *
     * @param children_id
     * @param customer_id
     * @param customer_remark 成员备注
     * @param handler
     */
    public static void OKHttpUpdateMemberRemark(String children_id, String customer_id,
                                                String customer_remark, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("children_id", children_id);
        params.put("customer_id", customer_id);
        params.put("customer_remark", customer_remark);
        mAsyncHttpClient.post(mHttpUrls.DOCTORUPDATEMEMBERREMARK, params, handler);
    }

    /**
     * 根据宝贝计划是否正在执行进行查询
     */
    public static void OKHttpISRun(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DCOTORISRUN, map, callback, tag);
    }

    /**
     * 计划详情
     */
    public static void OKHttpPlanDetail(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORPLANDETAIL, map, callback, tag);
    }

    /**
     * 计划变更状态
     */
    public static void OKHttpPlanChange(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORPLANCHANGE, map, callback, tag);
    }
    /***********专家会诊**************/
    /**
     * 找专家查找类型
     *
     * @param handler
     */
    public static void OKHttpFindExpert(AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.post(mHttpUrls.DOCTORFINDEXPERT, handler);
    }

    /**
     * 根据科室查询医生
     */
//    public static void OKHttpFindDocByRoom(String office_id, String pageNum, AsyncHttpResponseHandler handler) {
//        RequestParams params = new RequestParams();
//        params.put("office_id", office_id);
//        params.put("pageNum", pageNum);
//        mAsyncHttpClient.post(mHttpUrls.DOCTORFINDDOCBYROOM, params, handler);
//    }
    public static void OKHttpFindDocByRoom(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORFINDDOCBYROOM, map, callback, tag);
    }

    /**
     * 根据科室查询医生
     *
     * @param map
     * @param callback
     * @param tag
     */
    public static void OKHttpFindDocByDis(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORFINDDOCBYDIS, map, callback, tag);
    }

    /**
     * 搜索科室和热门疾病
     *
     * @param map
     * @param callback
     * @param tag
     */
    public static void OKHttpSearchRoom(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORSEARCHROOM, map, callback, tag);
    }

    /**
     * 热门疾病查询医生 疾病推荐医生
     *
     * @param map
     * @param callback
     * @param tag
     */
    public static void OKHttpFindExpertByDisease(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORFINDDOCBYDISEASE, map, callback, tag);
    }

    /**
     * 会诊专家之查找地区
     *
     * @param handler
     */
    public static void OKHttpFindArea(AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.post(mHttpUrls.DOCTORAREA, handler);
    }

    /**
     * 优惠卷
     *
     * @param customer_id
     * @param status
     * @param handler
     */
    public static void OKHttpCoupon(String customer_id, String status, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("customer_id", customer_id);
        params.put("status", status);
        mAsyncHttpClient.post(mHttpUrls.DOCTORCOUPON, params, handler);
    }

    /**
     * 账户管理之账户余额
     *
     * @param customer_id
     * @param handler
     */
    public static void OKHttpACCOUNTBALANCE(String customer_id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("customer_id", customer_id);
        mAsyncHttpClient.post(mHttpUrls.DOCTORMANAGERBALANCE, params, handler);
    }

    /**
     * 账户管理之查询账户变更记录
     */
    public static void OKHttpACCOUNTCHANGE(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORACCOUNTCHANGE, map, callback, tag);
    }

    /**
     * 热门活动
     *
     * @param params
     * @param handler
     */
    public static void doHttpHotMessageList(RequestParams params, ObjectHttpResponseHandler handler) {
        mAsyncHttpClient.post(mHttpUrls.DOCTORMESSAGE, params, handler);
    }
    /*************公益援助******************/

    /**
     * 查询义诊医生
     */
    public static void OKHttpFamDoc(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORFAMDOCTOR, map, callback, tag);
    }

    /**
     * 查找科室
     *
     * @param handler
     */
    public static void OKHttpFindOffice(AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.post(mHttpUrls.DOCTORFINDOFFICE, handler);
    }

    /**
     * 查询项目发布类型
     *
     * @param handler
     */
    public static void doHttpItemType(AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.post(mHttpUrls.DOCTORITEMTYPE, handler);
    }

    /**
     * 根据类型查询项目发布
     */
//    public static void OKHttpItemDetail(String class_id, AsyncHttpResponseHandler handler) {
//        RequestParams params = new RequestParams();
//        params.put("class_id", class_id);
//        mAsyncHttpClient.post(mHttpUrls.DOCTORITEMDETAIL, params, handler);
//    }
    public static void OKHttpItemDetail(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORITEMDETAIL, map, callback, tag);
    }

    /**
     * 查询发布项目
     *
     * @param project_id
     * @param handler
     */
    public static void OKHttpItemDetailContext(String project_id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("project_id", project_id);
        mAsyncHttpClient.post(mHttpUrls.DOCTORITEMDETAILCONTEXT, params, handler);
    }

    /**
     * 查询公益基金类型
     *
     * @param handler
     */
    public static void doHttpProFundType(AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.post(mHttpUrls.DOCTORITEMFUND, handler);
    }

    /**
     * 根据类型查询公益基金
     *
     * @param class_id
     * @param handler
     */
    public static void OKHttpFindFund(String class_id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("class_id", class_id);
        mAsyncHttpClient.post(mHttpUrls.DOCTORFINDFUND, params, handler);
    }

    /**
     * 查询公益详细
     */
    public static void OKHttpFindFundContent(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORITEMFUNDCONTENT, map, callback, tag);
    }
    /*************六一百科******************/

    /**
     * 常见疾病 分类
     *
     * @param handler
     */
    public static void doHttpCommonIll(AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.post(mHttpUrls.DOCTORCOMMONILL, handler);
    }

    /**
     * 根据类型查询常见疾病
     */
    public static void OKHttpCommonIllById(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORCOMMONILLBYID, map, callback, tag);
    }

    /**
     * 常见病详情
     */
    public static void OKHttpCommonIllDetail(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORCOMMONDETAIL, map, callback, tag);
    }

    /**
     * 常见病推荐医生
     */
    public static void OKHttpDoctorRecommend(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORRECOMMEND, map, callback, tag);
    }

    /**
     * 查询医学前沿   的列表
     *
     * @param handler
     */
    public static void doHttpFroMedicine(AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.post(mHttpUrls.DOCTORFROMEDICINE, handler);
    }

    /**
     * 医学前沿内容
     */
    public static void OKHttpFroMedicineDetail(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORFROMEDICINEDETAIL, map, callback, tag);
    }

    /**
     * 名医名院 分类
     *
     * @param handler
     */
    public static void doHttpFamousHospital(AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.post(mHttpUrls.DOCTORFAMOUSHOSPITAL, handler);
    }

    /**
     * 类型下名医名院
     */
    public static void OKHttpFamHosDetail(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORFAMHOSDETAIL, map, callback, tag);
    }

    /**
     * 名医名院详情
     *
     * @param hospital_id
     * @param handler
     */
    public static void OKHttpFamHosDetailContent(String hospital_id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("hospital_id", hospital_id);
        mAsyncHttpClient.post(mHttpUrls.DOCTORFAMHOSDETAILCONTENT, params, handler);
    }

    /**
     * 名医讲堂 分类
     *
     * @param handler
     */
    public static void doHttpFamDocClassRoom(AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.post(mHttpUrls.DOCTORFAMDOCCLASSROOM, handler);
    }

    /**
     * 类型下名医讲堂
     *
     * @param map
     * @param callback
     * @param tag
     */
    public static void OKHttpFamDocClassRoomDetail(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORFAMDOCCLASSROOMDETAIL, map, callback, tag);
    }

    /**
     * 增加名义讲堂的评论
     *
     * @param map
     * @param callback
     * @param tag
     */
    public static void OKHttpAddFamDocComment(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORADDFAMDOCCOMMENT, map, callback, tag);
    }

    /**
     * 查询名医讲堂评论
     *
     * @param map
     * @param callback
     * @param tag
     */
    public static void OKHttpSeeFamDocComment(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORSEEDOCFAMCOMMENT, map, callback, tag);
    }

    /**
     * 名医讲堂推荐医生
     *
     * @param map
     * @param callback
     * @param tag
     */
    public static void OKHttpSCommentDoctor(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORCOMMENTDOCTOR, map, callback, tag);
    }

    /**
     * 查询名医讲堂详细内容
     *
     * @param map
     * @param callback
     * @param tag
     */
    public static void OKHttpFamDocDetail(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORDOCFAMDETAIL, map, callback, tag);
    }
    /**************医生工作室**************/
    /**
     * 首页信息
     *
     * @param map
     * @param callback
     * @param tag
     */
    public static void OKHttpDoctorStudio(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORSTUDIO, map, callback, tag);
    }


    /**
     * 医生工作室的工具
     *
     * @param map
     * @param callback
     * @param tag
     */
    public static void OKHttpDoctorStudioUtils(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.INFCENTERSERVLET, map, callback, tag);
    }

    /**
     * 添加关心
     *
     * @param map
     * @param callback
     * @param tag
     */
    public static void OKHttpDoctorStudioCare(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORSTUDIOCARE, map, callback, tag);
    }

    /***
     * 取消关心
     * @param map
     * @param callback
     * @param tag
     */
    public static void OKHttpDoctorStudioCancelCare(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORCANCELCARE, map, callback, tag);
    }

    /*************新闻中心******************/
    public static void OKHttpNewsCenter(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORNEWSCENTER, map, callback, tag);
    }

    /************新首页接口******************/
    public static void OKHttpNewsPatientHome(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.PATIENT_HOME, map, callback, tag);
    }

    /**
     * 添加评论
     *
     * @param map
     * @param callback
     * @param tag
     */
    public static void OKHttpAddComment(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORADDCOMMENT, map, callback, tag);
    }

    /**
     * 查询详细内容，评论信息
     */
    public static void OKHttpQueryComment(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORQUERYCOMMENT, map, callback, tag);
    }
    /*************首页******************/
    /**
     * 首页热门搜索词
     *
     * @param handler
     */
    public static void doHttppHotSearchWord(AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.post(mHttpUrls.DOCTORHOTSEARCHWORD, handler);
    }

    /**
     * 全局搜索
     */
    public static void OKHttpSearch(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.DOCTORSEARCH, map, callback, tag);
    }

    /**
     * 充值
     */
    public static void OKHttpFillMoney(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.ADDBALANCE, map, callback, tag);
    }

    /**
     * 体现
     *
     * @param map
     * @param callback
     * @param tag
     */

    public static void OKHttpEmbodyMoney(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.EMBODY, map, callback, tag);
    }

    /**
     * 填写关爱记录
     *
     * @param params
     * @param handler
     */
    public static void doHttpFillCareRecord(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.post(mHttpUrls.DOCTORADDCARE, params, handler);
    }

    /**
     * 查询随访计划
     *
     * @param map
     * @param callback
     * @param tag
     */
    public static void OKHttpFindTemplate(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.FINDTEMPLATE, map, callback, tag);
    }

    /**
     * 查询随访计划详情
     *
     * @param map
     * @param callback
     * @param tag
     */
    public static void OKHttpFindFollowSubListById(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.FINDFOLLOWSUBLISTBYID, map, callback, tag);
    }

    /**
     * 我的订单
     *
     * @param map
     * @param callback
     * @param tag
     */
    public static void OKHttpFindOrderByCustomer(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.FINDORDERBYCUSTOMER, map, callback, tag);
    }

    /**
     * 退单
     */
    public static void OKHttpBackOrderUploadServlet(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.post(mHttpUrls.BACKORDERUPLOADSERVLET, params, handler);
    }


    /**
     * 绑定微信
     */
    public static void OKHttpBound(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.DUOMEIHEALTHS, params, callback, tag);
    }

    /**
     * TalkHistoryServlet接口
     * 特殊服务聊天历史列表
     */
    public static void doGetTalkHistoryServletS(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.TALKHISTORYSERVLETS, params, callback, tag);
    }


    /**
     * 查询支付方式
     */
    public static void OKHttpQueryYellowBoy(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.QUERYYELLOWBOY, map, callback, tag);
    }

    /**
     * 会诊订单 评价列表
     *
     * @param map
     * @param callback
     * @param tag
     */
    public static void OKHttpAppraiseList(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.FINDFOLLOWSUBLISTBYID, map, callback, tag);
    }


    /**
     * 医师集团 统一的连网方法
     */
    public static void OKHttpStationCommonUrl(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.COMMONURL, map, callback, tag);
    }

    /**
     * 医师集团 统一的连网方法2
     */
    public static void OKHttpStationUrl(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.FINDSITECOMMENT, map, callback, tag);
    }

    /**
     * 就诊人 统一的连网方法
     */
    public static void OKHttPersonSeek(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.PERSONSEEKCOMMONURL, map, callback, tag);
    }
    /**
     * 病历管理 统一的连网方法
     */
    public static void OKHttpCaseList(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.TALKHISTORYSERVLETS, map, callback, tag);
    }


    /**
     * 商城 统一联网接口
     * @param map
     * @param callback
     * @param tag
     */
    public static void OKHttGoodsServlet(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.GOODSSERVLET, map, callback, tag);
    }

    /**
     * 购买商城接口
     *
     * @param params
     * @param callback
     * @param tag
     */
    public static void OKHttGoodsPayServlet(List<BasicNameValuePair> params, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.getAsyn(mHttpUrls.GOODSSERVLET, params, callback, tag);
    }

    /**
     * 商城优惠券
     * @param
     * @param callback
     * @param tag
     */
    public static void OKHttGoodsCouponServlet(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.COUPONSLIST, map, callback, tag);
    }
    /**
     * 六一班查看医生好友(创建群聊)
     */
    public static void OKHttpGetFriends(Map<String, String> map, OkHttpClientManager.ResultCallback callback, Object tag) {
        OkHttpClientManager.postAsyn(mHttpUrls.TALKHISTORYSERVLETS, map, callback, tag);
    }

}




