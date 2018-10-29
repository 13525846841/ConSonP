package com.yksj.healthtalk.net.http;

/**
 * 所有的http请求地址
 * 
 * @author zhao
 */
/**
 * http请求地址
 *
 * @author zhao
 */
public class HttpUrlsB {
	public static String FORGETPSW;
	public final String SYSSETTING;
	public final String CUSSUGGESTION;
	public final String KEEPCONNECTION;
	public final String URL_MMS_SERVLET300;
	public final String URL_MMS_SERVLET61;
	public final String URL_QUERYSITUATION;
	public final String URL_XIEYI;//
	public final String UPDATEPHONENUM;//
	public final String UPDATEPSW;//
	public final String CUSEVALUATIONDISEASE;//
	public final String XYWENZHENHIS;//
	public final String CREATEORDER;//
	public final String FAMILYLIST;//
	public final String PRODUCTS;//
	/**
	 * 根路径
	 */
	public final String WEB_ROOT;
	public String URL_DOWN_LOAD_URL;//下载更新
	public String JUMPRECORD;//
	public String SENDCODE;
	public String REGIST;
	public String LOGIN;
	public String UPDATECUSMESG;
	public String MESGHISTORY;
	public String INTELLIGENCEBALL;
	public String NEARBYHOSPITAL2;
	public String SHARE_CONTENT_LINT;//分享
	public String LOAD_PIC_DISPLAY;//下载
	public String DISCONNECTSERVER;//断开连接
	public String INICHOUJIANG;//
	public String SHARECHOUJIANGREC;//
	public String LOADEDOFFLINEMESSAGE;//
	public String PAYMENTMESG;//
	public String RETURNHTML;//
	public String CONTACT_WEB;//
	public String ABOUTDXY_WEB;//
	public String XYMENU;//
	public String XYVIP;//
	public String BINDCARD;//绑定VIP卡号
	public String xyMenu;//
	public String UPDATEINFO;//更新个人信息
	public String UPBITMAP;//发送图片
	public String DOWNBITMAP;//发送图片
	public String CHANGETALKER;//发送图片


	/**
	 * 检测更新 聊天 协议 一样
	 * <p/>
	 * 人体图
	 *
	 * @param webRoot
	 */

	public HttpUrlsB(String webRoot) {
		WEB_ROOT = webRoot;
		URL_MMS_SERVLET300 = WEB_ROOT + "/dmys/xyService.do";
		URL_MMS_SERVLET61 = WEB_ROOT + "/DuoMeiHealth/Robot";
		URL_QUERYSITUATION = "http://220.194.46.205:8080" + "/sms_web/QuerySituation";
		URL_DOWN_LOAD_URL = WEB_ROOT + "/dmys/versionControl";//
		JUMPRECORD = WEB_ROOT + "/dmys/jumpRecord";
		URL_XIEYI = WEB_ROOT + "/dmys/Robot_Agreement_210.html";//http://220.194.46.204/dmys/Robot_Agreement_100.html
		KEEPCONNECTION = WEB_ROOT + "/dmys/keepConnect";//http://220.194.46.204/dmys/Robot_Agreement_100.html


		CUSSUGGESTION = WEB_ROOT + "/dmys/cusSuggestion";
		SENDCODE = WEB_ROOT + "/dmys/sendCode";
		REGIST = WEB_ROOT + "/dmys/regist";
		LOGIN = WEB_ROOT + "/dmys/login";
		UPDATECUSMESG = WEB_ROOT + "/dmys/updateCusMesg";
		SYSSETTING = WEB_ROOT + "/dmys/sysSetting";
		UPDATEPHONENUM = WEB_ROOT + "/dmys/updatePhoneNum";
		UPDATEPSW = WEB_ROOT + "/dmys/updatePsw";
		CUSEVALUATIONDISEASE = WEB_ROOT + "/dmys/cusEvaluationDisease";
		XYWENZHENHIS = WEB_ROOT + "/dmys/xyWenzhenHis";
		MESGHISTORY = WEB_ROOT + "/dmys/mesgHistory";
		INTELLIGENCEBALL = WEB_ROOT + "/dmys/intelligenceBall";
		FORGETPSW = WEB_ROOT + "/dmys/fogetPsw";
		NEARBYHOSPITAL2 = WEB_ROOT + "/dmys/nearbyHospital2";
		SHARE_CONTENT_LINT = WEB_ROOT + "/XiaoYiShare/XiaoYiShare?";
		LOAD_PIC_DISPLAY = WEB_ROOT + "/dmys/DownLoadfile?path=";
		DISCONNECTSERVER = WEB_ROOT + "/dmys/DisconnectServer";
		INICHOUJIANG = WEB_ROOT + "/dmys/IniChoujiang";
		SHARECHOUJIANGREC = WEB_ROOT + "/dmys/shareChoujiangRec";
		LOADEDOFFLINEMESSAGE = WEB_ROOT + "/dmys/LoadedOfflineMessage";
		CREATEORDER = WEB_ROOT + "/dmys/createOrder";
		PAYMENTMESG = WEB_ROOT + "/dmys/paymentMesg";
		RETURNHTML = WEB_ROOT + "/dmys/ReturnHtml";
		CONTACT_WEB = WEB_ROOT + "/dmys/Contact.html";
		ABOUTDXY_WEB = WEB_ROOT + "/dmys/AboutDXY.html";
		XYMENU = WEB_ROOT + "/dmys/xyMenu";
		XYVIP = WEB_ROOT + "/dmys/xyvip";
		FAMILYLIST = WEB_ROOT + "/dmys/xyProductsManage";
		PRODUCTS = WEB_ROOT + "/dmys/xyProducts";
		BINDCARD = WEB_ROOT + "/dmys/baseDateSet";
		UPDATEINFO = WEB_ROOT + "/dmys/baseDateSet";
		UPBITMAP=WEB_ROOT + "/dmys/sendFile";
		DOWNBITMAP=WEB_ROOT + "/dmys/DownLoadfile?path=";
		CHANGETALKER=WEB_ROOT + "/dmys/baseDateSet";
		xyMenu=WEB_ROOT + "/dmys/xyMenu";
	}
}
