package com.yksj.consultation.son.consultation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;
import com.yksj.consultation.adapter.PaySelectAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.consultation.son.consultation.bean.ServiceType;
import com.yksj.consultation.son.pay.PayResult;
import com.yksj.consultation.son.pay.SignUtils;
import com.yksj.consultation.son.setting.SettingPhoneBound;
import com.yksj.consultation.son.simcpux.Constants;
import com.yksj.consultation.son.simcpux.Util;
import com.yksj.consultation.son.wallet.PwdSettingActivity;
import com.yksj.healthtalk.bean.BaseBean;
import com.yksj.healthtalk.entity.GroupInfoEntity;
import com.yksj.healthtalk.entity.PaySuccessInfoEntity;
import com.yksj.healthtalk.entity.WeiXinPayResultJson;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.MyResultCallback;
import com.yksj.healthtalk.net.http.ObjectHttpResponseHandler;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.MD5Utils;
import com.yksj.healthtalk.utils.ToastUtil;

import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class PAtyConsultStudioGoPaying extends BaseFragmentActivity implements
        View.OnClickListener, DoubleBtnFragmentDialog.OnDilaogClickListener, AdapterView.OnItemClickListener, IWXAPIEventHandler {


    private ListView mPaySelect;
    private PaySelectAdapter mAdapter;
    private List<org.json.JSONObject> mList = null;

    private String serviceTypeId;//支付类型id
    private String doctor_id;//医生ID
    //private String consultationId;//会诊id
    private static final int PLUGIN_NOT_INSTALLED = -1;
    private static final int PLUGIN_NEED_UPGRADE = 2;
    private static final int ACTIVITY_FINISH = 401;//销毁本界面

    /*****************************************************************
     * mMode参数解释：
     * "00" - 启动银联正式环境
     * "01" - 连接银联测试环境
     *****************************************************************/
    private String mMode = "00";
    private String payId;
    private String type = "";
    private static final String TN_URL_01 = "http://222.66.233.198:8080/sim/gettn";
    private boolean isBindPhone, isSetPsw;//是否绑定手机,是否设置支付密码
    private boolean isPaying = false;
    private String price = "0", couponPrice = "0";//总价  优惠券价格
    private String couponId = "";//优惠券ID

    private TextView tvCoupon, tvPrice;
    private RelativeLayout couponLayout;
    private TextView tvUseCouponPrice;
//    private TextView number;
    private TextView reminds;
    private float balanceNumber;//余额钱数

    private TextView docName;//医生名称
    private TextView docStudio;//科室
    private TextView serviceType;


    private String str = "";
    private String name = "";
    private String id = "";
    private String order_id = "";
    private String pay_id = "";


    public static final String SITE_ID = "site_id";//医生集团id
    public static final String SERVICETYPEID = "service_type_id";//医生集团服务类型
    public static final String ORDER_ID = "order_id";//订单id
    private String site_id = "";//医生集团id
    private String service_type_id = "";//医生集团服务类型
    String service_type = "";//医生工作室服务类型
    private String order_id_new = "";
    private String course_id;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {

    }


    public interface OnBuyTicketHandlerListener {
        /**
         * @param state  1 成功,0多美币不足,-1失败
         * @param entity
         */
        void onTicketHandler(String state, GroupInfoEntity entity);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_paty_consult_studio_go_paying);
//        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
//        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(Constants.APP_ID);
        api.handleIntent(getIntent(), this);
        EventBus.getDefault().register(this);
        initWidget();
        initDate();
        initDate1();
        initPayData();

        if (LoginServiceManeger.instance().getLoginEntity() != null) {
            String phoneNum = LoginServiceManeger.instance().getLoginEntity().getPoneNumber();
            String setPsw = LoginServiceManeger.instance().getLoginEntity().getIsSetPwd();
            if (!HStringUtil.isEmpty(phoneNum)) {
                isBindPhone = true;
            }
            if (!HStringUtil.isEmpty(setPsw) && "1".equals(setPsw)) {
                isSetPsw = true;
            }
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    /**
     * 加载支付方式的方法
     */
    private void initPayData() {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", LoginServiceManeger.instance().getLoginUserId());
        HttpRestClient.OKHttpQueryYellowBoy(map, new HResultCallback<String>(this) {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String content) {
                try {
                    org.json.JSONObject obj = new org.json.JSONObject(content);
                    JSONArray array = obj.optJSONArray("info");
                    mList = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        org.json.JSONObject jsonobject = array.getJSONObject(i);
                        mList.add(jsonobject);
                    }
                    mAdapter.onBoundData(mList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, this);
    }

    /**********查询余额接口**************/
    private void initDate1() {
        HttpRestClient.OKHttpACCOUNTBALANCE(LoginServiceManeger.instance().getLoginUserId(), new ObjectHttpResponseHandler(this) {
            @Override
            public Object onParseResponse(String content) {
                if (content != null) {
                    return content;
                } else {
                    return null;
                }
            }

            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                try {
                    if (response != null) {
                        org.json.JSONObject object = new org.json.JSONObject(response.toString());
                        object.optString("balance");
                        balanceNumber = Float.parseFloat(object.optString("balance"));
//                        number.setText("当前余额" + String.valueOf(balanceNumber) + "元");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initDate() {
        order_id_new = getIntent().getStringExtra(ORDER_ID);
        serviceTypeId = getIntent().getStringExtra("service_item_id");
        doctor_id = getIntent().getStringExtra("doctor_id");
        price = getIntent().getStringExtra("price");
        site_id = getIntent().getStringExtra(SITE_ID);
        service_type_id = getIntent().getStringExtra(SERVICETYPEID);
        tvPrice.setText(price + "元");
//        docName.setText(getIntent().getStringExtra("expertName"));
//        docStudio.setText(getIntent().getStringExtra("officeName"));

        if (getIntent().hasExtra("type")) {//没有订单
            type = getIntent().getExtras().getString("type");
        } else {
            payId = getIntent().getStringExtra("payId");
//			send_code = getIntent().getStringExtra("send_code");
        }

        List<BasicNameValuePair> valuePairs = new ArrayList<>();
        valuePairs.add(new BasicNameValuePair("CUSTOMERID", LoginServiceManeger.instance().getLoginEntity().getId()));
        valuePairs.add(new BasicNameValuePair("service_item_id", serviceTypeId));
        HttpRestClient.doGetConsultationCouponsCount(valuePairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                BaseBean bb = JSONObject.parseObject(response, BaseBean.class);
                if ("1".equals(bb.code)) {
                    tvCoupon.setText("您有" + bb.result + "张优惠券可以使用");
                    couponLayout.setVisibility(View.VISIBLE);
//                    if (Integer.parseInt(bb.result) > 0) {
//                        tvCoupon.setText("您有" + bb.result + "张优惠券可以使用");
//                        couponLayout.setVisibility(View.VISIBLE);
//                    } else
//                        couponLayout.setVisibility(View.GONE);
//                } else {
//                    ToastUtil.showShort(PAtyConsultStudioGoPaying.this, bb.message);
//                    couponLayout.setVisibility(View.GONE);
                }
            }
        }, this);
    }


    private void initWidget() {
        initTitle();
        titleTextV.setText("选择支付方式");
        titleLeftBtn.setOnClickListener(this);

//        findViewById(R.id.rl_zfb).setOnClickListener(this);
//        findViewById(R.id.rl_wx).setOnClickListener(this);
//        findViewById(R.id.rl_bank).setOnClickListener(this);
//        findViewById(R.id.rl_purse).setOnClickListener(this);

        mPaySelect = (ListView) findViewById(R.id.pay_select);
        mAdapter = new PaySelectAdapter(this);
        mAdapter.setServiceType(service_type);
        mPaySelect.setAdapter(mAdapter);
        mPaySelect.setOnItemClickListener(this);

//      docName = (TextView) findViewById(R.id.doc_name);
//      docStudio = (TextView) findViewById(R.id.doc_studio);
        service_type = serviceTypeId = getIntent().getStringExtra("service_id");
        course_id = getIntent().getStringExtra("course_id");

        Log.i("rtrt", "service_type:       "+service_type);

        serviceType = (TextView) findViewById(R.id.service_type);
        reminds = (TextView) findViewById(R.id.reminds);
        if (ServiceType.TW.equals(service_type)) {
            serviceType.setText("图文咨询");
            reminds.setText("温馨提示说明 \n图文咨询:\n1、图文咨询单次有效期为24小时，可向医生发送20条消息。\n2、医生可能在门诊或手术中，如无法及时回复请谅解。若在有效期内未做回复，将全额退款。\n3、线上咨询为健康导航提供重要参考信息，若病情危急请直接前往医院就诊。\n4、若对本次服务有疑义，请在3日内联系平台客服");

        } else if (ServiceType.DH.equals(service_type)) {
            serviceType.setText("电话咨询");
            reminds.setText("温馨提示说明 \n" +
                    "电话咨询：\n" +
                    "1、购买服务后，平台立刻通知医生给您回电。\n" +
                    "2、通话时间以本页面医生设置时间为准。\n" +
                    "3、有时医生可能在门诊或手术中，若无法及时回电请谅解，请耐心等待。\n" +
                    "4、若3小时内医生依然未回电，您可选择继续等待或退款，如未选择，系统将默认为等待。\n" +
                    "5、若对本次服务有疑义，请在3日内联系平台客服。");

        } else if (ServiceType.BY.equals(service_type)) {
            serviceType.setText("包月咨询");
            reminds.setText("温馨提示说明 \n" +
                    "包月咨询：\n" +
                    "1、\t包月期间可以向医生发送1000条消息。\n" +
                    "2、包月咨询仅限图文形式，不包含其它咨询形式。\n" +
                    "3、包月咨询的问题回复实效期是24小时内，医生因故未及时回复请再次提交问题或寻求平台客服帮助。\n" +
                    "4、包月咨询服务尽量避开国家节假日，此期间回复可能滞后请谅解。\n" +
                    "5、包月期间若超过3次逾期不回复且经平台协调无果的情况，平台将为您全额退款。\n" +
                    "6、若对本次服务有疑义，请在3日内联系平台客服。");

        } else if (ServiceType.SP.equals(service_type)) {
            serviceType.setText("视频咨询");
            reminds.setText("温馨提示说明 \n" +
                    "视频咨询：\n" +
                    "1、购买服务后，平台立刻通知医生给您回复。\n" +
                    "2、通话时间以本页面医生设置时间为准。\n" +
                    "3、有时医生可能在门诊或手术中，若无法及时回电请谅解，请耐心等待。\n" +
                    "4、若3小时内医生依然未回电，您可选择继续等待或退款，如未选择，系统将默认为等待。\n" +
                    "5、若对本次服务有疑义，请在3日内联系平台客服。");
        }else if (ServiceType.DL.equals(service_type)) {
            serviceType.setText("患教文章");
        }else if (ServiceType.DS.equals(service_type)) {
            serviceType.setText("打赏");
        }


        tvCoupon = (TextView) findViewById(R.id.tv_coupon_price);
        tvUseCouponPrice = (TextView) findViewById(R.id.tv_coupon_price);
        tvPrice = (TextView) findViewById(R.id.pay_amount2);
        couponLayout = (RelativeLayout) findViewById(R.id.rl_discount_coupon);
        couponLayout.setOnClickListener(this);
//        number = (TextView) findViewById(R.id.pay_balance);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (mAdapter.PayType(position)) {
            case 3://支付宝
//                Double aa = Double.parseDouble(price);
//                Double bb = Double.parseDouble(couponPrice);
//
//                if (aa <= bb)
//                    onClickFreePay(view);
//                else
                if (service_type.equals(ServiceType.DL)||service_type.equals(ServiceType.DS)){
                    onClickAlipayDl(view);
                }else {
                    onClickAlipay(view);
                }

                break;

            case 2://微信支付
                if (service_type.equals(ServiceType.DL)||service_type.equals(ServiceType.DS)){
                    onClickWXPayDL();
                }else {
                    onClickWXPay();
                }
                break;
            case 4://银联
                if (service_type.equals(ServiceType.DL)||service_type.equals(ServiceType.DS)){
                    onClickUnionpayDl(view);
                }else {
                    onClickUnionpay(view);
                }
                break;
            case 1://钱包
                Float a = Float.parseFloat(price);
                Float b = Float.parseFloat(couponPrice);
                if (a<b){
                    onClickFreePay(view);
                }else {
                    if (a<balanceNumber){
                        onClickWallet(view);
                    }else {
                        ToastUtil.showShort("余额不足");
                    }
                }
                break;
        }
    }

    /**
     * 支付宝支付
     *
     * @param v
     */
    public void onClickAlipay(View v) {
        if (isPaying)//正在支付
            return;
        isPaying = true;
        List<BasicNameValuePair> valuePairs = new ArrayList<>();
        if (!HStringUtil.isEmptyAndZero(site_id)) {
            Log.i("ggg", "onClickAlipay: -----=-=-=-=--==");
            valuePairs.add(new BasicNameValuePair("site_id", site_id));
            valuePairs.add(new BasicNameValuePair("service_type_id", service_type_id));
        }
        if (!HStringUtil.isEmptyAndZero(order_id_new)) {
            valuePairs.add(new BasicNameValuePair("order_id", order_id_new));
        }
        valuePairs.add(new BasicNameValuePair("op", "buyDoctorService"));
        valuePairs.add(new BasicNameValuePair("doctor_id", doctor_id));
        valuePairs.add(new BasicNameValuePair("service_item_id", serviceTypeId));
        valuePairs.add(new BasicNameValuePair("pay_type", "1"));
        valuePairs.add(new BasicNameValuePair("customer_id", LoginServiceManeger.instance().getLoginUserId()));

        HttpRestClient.doGetConsultationBuyStudioServlet(valuePairs, new MyResultCallback<String>(this) {
            @Override
            public void onError(Request request, Exception e) {
                Log.i("hhh", "onError: "+e.getMessage());
            }

            @Override
            public void onResponse(String response) {

                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code==1){
                        ToastUtil.onShow(PAtyConsultStudioGoPaying.this,jsonObject.getString("message"),1000);
                        return;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                BaseBean baseBean = JSONObject.parseObject(response, BaseBean.class);
                BaseBean.Love baseLove = baseBean.love;
                isPaying = false;

                if ("0".equals(baseLove.code)) {//成功
//                    String response1 = "{\n" +
//                            "\"sign\": \"W95IiBiBB8LykQy4Xn9KKgj2xkOhav9vjW9BmHqpbZCY%2FdSStPA1XJ7myZstBH1Gd05lcWuX72KbxKj5w13VNnoDwAHOTU04UNh0xfk0RKsxUfKWAjHRzyRbx0UhxXFfHhMpKJgUzBRg%2FsKFed8R0fcibWtiF4Tmv%2BzZZDGeSbM%3D\",\n" +
//                            "\"message\": \"操作失败\",\n" +
//                            "\"source\": \"partner=\\\"2088121693621030\\\"&seller_id=\\\"61@61120.net\\\"&out_trade_no=\\\"1170502260092461\\\"&subject=\\\"医生咨询服务\\\"&body=\\\"余额充值\\\"&total_fee=\\\"100\\\"&notify_url=\\\"http://220.194.46.204/DuoMeiHealth/servlet/NotifyReceiver611\\\"&service=\\\"mobile.securitypay.pay\\\"&payment_type=\\\"1\\\"&_input_charset=\\\"utf-8\\\"&it_b_pay=\\\"30m\\\"&show_url=\\\"m.alipay.com\\\"\",\n" +
//                            "\"code\": 1\n" +
//                            "}";
//                    PayBean payBean = JSONObject.parseObject(response, PayBean.class);
//                  RSA_PRIVATE = payBean.sign;

                    name = baseLove.doctor_real_name;
                    id = baseLove.doctor_id;
                    order_id = baseLove.order_id;
                    pay_id = baseLove.pay_id;

                    payZFB(baseLove.source, baseLove.sign);
                } else if ("4".equals(baseLove.code)) {//0元免费

                    if ("888".equals(baseLove.role_id)) {
                        ToastUtil.showShort(PAtyConsultStudioGoPaying.this, baseLove.message);
                        Intent intent = new Intent(PAtyConsultStudioGoPaying.this, PAytSuccess.class);
                        if (!HStringUtil.isEmpty(service_type)) {
                            intent.putExtra(PAytSuccess.ORDER_TYPE, service_type);
                        }
                        intent.putExtra(PAytSuccess.NAME, baseLove.doctor_real_name);
                        intent.putExtra(PAytSuccess.ID, baseLove.doctor_id);
                        intent.putExtra(PAytSuccess.ORDER_ID, baseLove.order_id);
                        intent.putExtra(PAytSuccess.PAY_ID, baseLove.pay_id);
                        startActivity(intent);
                    } else {
                        ToastUtil.showShort(PAtyConsultStudioGoPaying.this, baseLove.message);
                    }
                } else {
                    ToastUtil.showShort(baseLove.message);
                }


                /**
                 * 注意这里
                 */
//                ToastUtil.showShort(PAtyConsultStudioGoPaying.this, payBean.message);
//				if (response.startsWith("<")) {//支付宝支付跳转
//					Intent intent = new Intent(getApplicationContext(), ConsultationAliPayActivity.class);
//					//intent.putExtra("conId", consultationId);
//					intent.putExtra("couponId", couponId);
//					startActivityForResult(intent, ACTIVITY_FINISH);
//					return;
//				} else {
//					isPaying = false;
//					BaseBean baseBean = JSONObject.parseObject(response, BaseBean.class);
//					ToastUtil.showShort(PAtyConsultStudioGoPaying.this, baseBean.message);
//				}
            }
        }, this);
    }

    /**
     * 支付宝支付
     *
     * @param v
     */
    public void onClickAlipayDl(View v) {
        if (isPaying)//正在支付
            return;
        isPaying = true;
        List<BasicNameValuePair> valuePairs = new ArrayList<>();
        if (service_type.equals(ServiceType.DL)){
            valuePairs.add(new BasicNameValuePair("op", "buyCourse"));
        }else {
            valuePairs.add(new BasicNameValuePair("op", "Reward"));
        }
        valuePairs.add(new BasicNameValuePair("course_id", course_id));
        valuePairs.add(new BasicNameValuePair("type", "1"));
        valuePairs.add(new BasicNameValuePair("course_price", price));
        valuePairs.add(new BasicNameValuePair("customer_id", LoginServiceManeger.instance().getLoginUserId()));

        HttpRestClient.doGetclassroomServlet(valuePairs, new MyResultCallback<String>(this) {
            @Override
            public void onError(Request request, Exception e) {
                Log.i("hhh", "onError: "+e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                Log.i("hhh", "onError: "+response);
                isPaying = false;
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(response);
                    if (jsonObject.getString("code").equals("1")) {
                        org.json.JSONObject result = jsonObject.getJSONObject("result");
                        String sign = result.getString("sign");
                        String source = result.getString("source");
                        payZFB(source, sign);
                    }else {
                        ToastUtil.onShow(PAtyConsultStudioGoPaying.this,"操作失败",1000);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this,service_type);
    }
    /**
     * 支付宝支付
     * <p>
     * 0元的情况
     *
     * @param v
     */
    public void onClickFreePay(View v) {
        if (isPaying)//正在支付
            return;
        isPaying = true;
        List<BasicNameValuePair> valuePairs = new ArrayList<>();
        if (!HStringUtil.isEmptyAndZero(order_id_new)) {
            valuePairs.add(new BasicNameValuePair("order_id", order_id_new));
        }
        valuePairs.add(new BasicNameValuePair("doctor_id", doctor_id));
        valuePairs.add(new BasicNameValuePair("op", "buyDoctorService"));
        valuePairs.add(new BasicNameValuePair("service_item_id", serviceTypeId));
        valuePairs.add(new BasicNameValuePair("pay_type", "1"));
        valuePairs.add(new BasicNameValuePair("customer_id", LoginServiceManeger.instance().getLoginUserId()));

        HttpRestClient.doGetConsultationBuyStudioServlet(valuePairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                BaseBean baseBean = JSONObject.parseObject(response, BaseBean.class);
                if ("1".equals(baseBean.code)) {//成功

                    /**
                     * 后添加的
                     */
//                    Intent intent = new Intent(getApplicationContext(), ConsultationAliPayActivity.class);
//                    //intent.putExtra("conId", consultationId);
//                    intent.putExtra("couponId", couponId);
//                    startActivityForResult(intent, ACTIVITY_FINISH);
//                    return;
                }
                ToastUtil.showShort(PAtyConsultStudioGoPaying.this, baseBean.message);
            }
        }, this);
    }

    /**
     * 银联支付
     *
     * @param v
     */
    public void onClickUnionpay(View v) {
        if (isPaying)//正在支付
            return;
        isPaying = true;

        List<BasicNameValuePair> valuePairs = new ArrayList<>();
        if (!HStringUtil.isEmptyAndZero(site_id)) {
            valuePairs.add(new BasicNameValuePair("site_id", site_id));
            valuePairs.add(new BasicNameValuePair("service_type_id", service_type_id));
        }
        if (!HStringUtil.isEmptyAndZero(order_id_new)) {
            valuePairs.add(new BasicNameValuePair("order_id", order_id_new));
        }
        valuePairs.add(new BasicNameValuePair("doctor_id", doctor_id));
        valuePairs.add(new BasicNameValuePair("op", "buyDoctorService"));
        valuePairs.add(new BasicNameValuePair("service_item_id", serviceTypeId));
        valuePairs.add(new BasicNameValuePair("pay_type", "3"));
        valuePairs.add(new BasicNameValuePair("customer_id", LoginServiceManeger.instance().getLoginUserId()));

        HttpRestClient.doGetConsultationBuyStudioServlet(valuePairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code==1){
                        ToastUtil.onShow(PAtyConsultStudioGoPaying.this,jsonObject.getString("message"),1000);
                        return;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                BaseBean baseBean = JSONObject.parseObject(response, BaseBean.class);
                BaseBean.Love baseLove = baseBean.love;
                isPaying = false;
                if ("0".equals(baseLove.code)) {

                    name = baseLove.doctor_real_name;
                    id = baseLove.doctor_id;
                    order_id = baseLove.order_id;
                    pay_id = baseLove.pay_id;
                    /*************************************************
                     *
                     *
                     *  步骤2：通过银联工具类启动支付插件
                     *
                     ************************************************/
                    // mMode参数解释：
                    // 0 - 启动银联正式环境
                    // 1 - 连接银联测试环境
                    int ret = UPPayAssistEx.startPay(PAtyConsultStudioGoPaying.this, null, null, baseLove.tn, mMode);
                    if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
                        // 需要重新安装控件
                        AlertDialog.Builder builder = new AlertDialog.Builder(PAtyConsultStudioGoPaying.this);
                        builder.setTitle("提示");
                        builder.setMessage("完成购买需要安装银联支付控件，是否安装？");

                        builder.setNegativeButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        UPPayAssistEx.installUPPayPlugin(PAtyConsultStudioGoPaying.this);
                                        isPaying = false;
                                    }
                                });

                        builder.setPositiveButton("取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        isPaying = false;
                                    }
                                });
                        builder.create().show();
                    }
                } else if ("4".equals(baseLove.code)) {//0元免费

                    if ("888".equals(baseLove.role_id)) {
                        ToastUtil.showShort(PAtyConsultStudioGoPaying.this, baseLove.message);
                        Intent intent = new Intent(PAtyConsultStudioGoPaying.this, PAytSuccess.class);
                        if (!HStringUtil.isEmpty(service_type)) {
                            intent.putExtra(PAytSuccess.ORDER_TYPE, service_type);
                        }
                        intent.putExtra(PAytSuccess.NAME, baseLove.doctor_real_name);
                        intent.putExtra(PAytSuccess.ID, baseLove.doctor_id);
                        intent.putExtra(PAytSuccess.ORDER_ID, baseLove.order_id);
                        intent.putExtra(PAytSuccess.PAY_ID, baseLove.pay_id);
                        startActivity(intent);
                    } else {
                        ToastUtil.showShort(PAtyConsultStudioGoPaying.this, baseLove.message);
                    }

                } else {
                    ToastUtil.showShort(PAtyConsultStudioGoPaying.this, baseLove.message);
                }
            }
        }, this);
    } /**
     * 银联支付
     *
     * @param v
     */
    public void onClickUnionpayDl(View v) {
        if (isPaying)//正在支付
            return;
        isPaying = true;
        List<BasicNameValuePair> valuePairs = new ArrayList<>();
        if (service_type.equals(ServiceType.DL)){
            valuePairs.add(new BasicNameValuePair("op", "buyCourse"));
        }else {
            valuePairs.add(new BasicNameValuePair("op", "Reward"));
        }        valuePairs.add(new BasicNameValuePair("course_id", course_id));
        valuePairs.add(new BasicNameValuePair("type", "3"));
        valuePairs.add(new BasicNameValuePair("course_price", price));
        valuePairs.add(new BasicNameValuePair("customer_id", LoginServiceManeger.instance().getLoginUserId()));

        HttpRestClient.doGetclassroomServlet(valuePairs, new MyResultCallback<String>(this) {
            @Override
            public void onError(Request request, Exception e) {
                Log.i("hhh", "onError: "+e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                Log.i("hhh", "onError: "+response);
                isPaying = false;
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(response);
                    if (jsonObject.getString("code").equals("1")) {
                        org.json.JSONObject result = jsonObject.getJSONObject("result");
                        String tn = result.getString("tn");
                        // mMode参数解释：
                        // 0 - 启动银联正式环境
                        // 1 - 连接银联测试环境
                        int ret = UPPayAssistEx.startPay(PAtyConsultStudioGoPaying.this, null, null, tn, mMode);
                        if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
                            // 需要重新安装控件
                            AlertDialog.Builder builder = new AlertDialog.Builder(PAtyConsultStudioGoPaying.this);
                            builder.setTitle("提示");
                            builder.setMessage("完成购买需要安装银联支付控件，是否安装？");

                            builder.setNegativeButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.dismiss();
                                            UPPayAssistEx.installUPPayPlugin(PAtyConsultStudioGoPaying.this);
                                            isPaying = false;
                                        }
                                    });

                            builder.setPositiveButton("取消",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.dismiss();
                                            isPaying = false;
                                        }
                                    });
                            builder.create().show();}

                    }else {
                        ToastUtil.onShow(PAtyConsultStudioGoPaying.this,"操作失败",1000);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this,service_type);
    }

    /**
     * 钱包
     *
     * @param v
     */

    public void onClickWallet(View v) {
        if (!isBindPhone) {
            DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "使用钱包支付，需绑定手机并设置支付密码，您目前未绑定手机。", "稍后再说", "现在绑定", this);
        } else if (!isSetPsw) {//米有设置密码
            DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "使用钱包支付，需绑定手机并设置支付密码，您目前未设置支付密码。", "稍后再说", "现在设置", this);
        } else {
//            WalletPayFragmentDialog.show(getSupportFragmentManager(), "输入支付密码", "");
            DoubleBtnFragmentDialog.showDoubleBtnPay(PAtyConsultStudioGoPaying.this, "输入支付密码", new DoubleBtnFragmentDialog.OnFirstClickListener() {
                @Override
                public void onBtn1(final String str) {
                    if (!HStringUtil.isEmpty(str)) {
                        onClickWalletPay(str);
                    } else {
                        ToastUtil.showShort("密码不能为空");
                    }
                }
            }, null).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {
            isSetPsw = true;
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            if (data.hasExtra("phone_num")) {
                isBindPhone = true;
            } else {
                isBindPhone = false;
            }
        } else if (requestCode == ACTIVITY_FINISH) {
            isPaying = false;
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                EventBus.getDefault().post(new MyEvent("refresh", 2));
                PAtyConsultStudioGoPaying.this.finish();
            }

        } else if (requestCode == 201 && resultCode == RESULT_OK) {//选择了优惠券
            couponPrice = data.getStringExtra("price");
            if ((Integer.parseInt(price) - data.getIntExtra("price", 0)) > 0) {
                tvPrice.setText((Integer.parseInt(price) - data.getIntExtra("price", 0)) + "元");
            } else {
                tvPrice.setText(0 + "元");
            }

            tvUseCouponPrice.setText(data.getIntExtra("price", 0) + "元");
            tvCoupon.setText("优惠券");
            couponId = data.getIntExtra("id", 0) + "";

        }else {
            /*************************************************
             *
             *  步骤3：处理银联手机支付控件返回的支付结果
             *
             ************************************************/
            if (data == null) {
                return;
            }

            String msg = "";
            String str = data.getExtras().getString("pay_result");

            /**
             * 支付控件返回字符串:success、fail、cancel
             *      分别代表支付成功，支付失败，支付取消
             */

            if (str.equalsIgnoreCase("success")) {
                if (ServiceType.DL.equals(service_type)){
                    Intent intent = new Intent();
                    intent.putExtra("alipayStatus","9000");
                    setResult(9000,intent);
                    finish();
                }else {
                    msg = "支付成功！";
                    Intent intent = new Intent(PAtyConsultStudioGoPaying.this, PAytSuccess.class);
                    if (!HStringUtil.isEmpty(service_type)) {
                        intent.putExtra(PAytSuccess.ORDER_TYPE, service_type);
                    }
                    intent.putExtra(PAytSuccess.NAME, name);
                    intent.putExtra(PAytSuccess.ID, id);
                    intent.putExtra(PAytSuccess.ORDER_ID, order_id);
                    intent.putExtra(PAytSuccess.PAY_ID, pay_id);
                    startActivity(intent);

                    setResult(RESULT_OK);
                    EventBus.getDefault().post(new MyEvent("refresh", 2));
                    PAtyConsultStudioGoPaying.this.finish();
                }
            } else if (str.equalsIgnoreCase("fail")) {
                msg = "支付失败！";
            } else if (str.equalsIgnoreCase("cancel")) {
                msg = "用户取消了支付";
            }
            ToastUtil.showShort(msg);
            isPaying = false;
//			SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(),  msg);
        }
    }

    @Override
    public void onClick(DialogFragment fragment, View v) {
        if (!isBindPhone) {
            Intent intent = new Intent(this, SettingPhoneBound.class);
            startActivityForResult(intent, 2);
        } else if (!isSetPsw) {
            Intent intent = new Intent(getApplicationContext(), PwdSettingActivity.class);
            intent.putExtra("isPayPwd", isSetPsw);
            intent.putExtra("isBDPhoneNum", SmartFoxClient.getLoginUserInfo().getPoneNumber());
            startActivityForResult(intent, 1);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.rl_zfb://支付宝
//                if (price <= couponPrice)
//                    onClickFreePay(v);
//                else
//                    onClickAlipay(v);
//                break;
            case R.id.title_back:
                onBackPressed();
                break;
//            case R.id.rl_wx://微信支付
//                onClickWXPay();
////              startActivity(new Intent(this, PayActivity.class));
////              wxPay();
//                break;
//            case R.id.rl_bank://银联
//                if (price <= couponPrice)
//                    onClickFreePay(v);
//                else
//                    onClickUnionpay(v);
//                break;
//            case R.id.rl_purse://钱包
//
//                if (price < balanceNumber) {
//                    if (price <= couponPrice)
//                        onClickFreePay(v);
//                    else
//                        onClickWallet(v);
//                } else {
//                    ToastUtil.showShort("余额不足");
//                }
//
//                break;
            case R.id.rl_discount_coupon:   //优惠券
                Intent intent = new Intent(PAtyConsultStudioGoPaying.this, PConsultCouponActivity.class);
                //   intent.putExtra("consultId", consultationId);
                startActivityForResult(intent, 201);

                break;
        }

    }

    @Override
    public void onDismiss(DialogFragment fragment) {

    }

    private static final int SDK_PAY_FLAG = 1;
    // 商户私钥，pkcs8格式
    public static String RSA_PRIVATE = "";

    private void payZFB(String orderInfo, String sign) {
        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
//        String sign2 = sign(orderInfo);
//        try {
//            /**
//             * 仅需对sign 做URL编码
//             */
//            sign = URLEncoder.encode(sign, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */

//        final String payInfo = "alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2016080400162728&biz_content=%7B%22body%22%3A%22%E8%B4%AD%E4%B9%B0%E5%8C%BB%E7%94%9F%E6%9C%8D%E5%8A%A1%22%2C%22out_trade_no%22%3A%221180614296095803%22%2C%22subject%22%3A%22%E8%B4%AD%E4%B9%B0%E5%8C%BB%E7%94%9F%E6%9C%8D%E5%8A%A1%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fyjk.h-tlk.com%2FDuoMeiHealth%2Fservlet%2FWorkSiteNotifyAlipayServlet&sign=gWFPVOePTMSuFOykaqi3t1lzq9RmRAuvLA%2Fmq54p7aDtxSegam8LVHn7cvGtRD5t9%2F%2Fu5AVO6bBoUBOvn%2B%2BL8GqmC9VMt%2FyZ86%2BAGB59XmJaHGI5pyBRA%2FE6dIhiATj85H6rSYzdbIQumsH6PsePNMt06XZr8G0hqnGC7s%2FhQYRWHu1xv8yXlqKc%2FU4tFeM6aXwJNTpq97uej8f6lrczqhMQzkXsZHVnQPssYS70CuGsrCO%2B9SUm23wN%2FYq7i9lPBLhKfvjQdc6dLcAKyq1ZtZQk2S4L68VUz2ITbuvTDTwMcjzoUSn7bcMZzXJvauzUdX1bqudXoSWJdX3vMii40A%3D%3D&sign_type=RSA2&timestamp=2018-06-14+15%3A23%3A14&version=1.0&sign=gWFPVOePTMSuFOykaqi3t1lzq9RmRAuvLA%2Fmq54p7aDtxSegam8LVHn7cvGtRD5t9%2F%2Fu5AVO6bBoUBOvn%2B%2BL8GqmC9VMt%2FyZ86%2BAGB59XmJaHGI5pyBRA%2FE6dIhiATj85H6rSYzdbIQumsH6PsePNMt06XZr8G0hqnGC7s%2FhQYRWHu1xv8yXlqKc%2FU4tFeM6aXwJNTpq97uej8f6lrczqhMQzkXsZHVnQPssYS70CuGsrCO%2B9SUm23wN%2FYq7i9lPBLhKfvjQdc6dLcAKyq1ZtZQk2S4L68VUz2ITbuvTDTwMcjzoUSn7bcMZzXJvauzUdX1bqudXoSWJdX3vMii40A%3D%3D";
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
//        final String payInfo = orderInfo;
//        final String payInfo = "app_id=2017022405848913&method=alipay.trade.app.pay&format=JSON&charset=UTF-8&sign_type=RSA&timestamp=2017-03-02 13:04:51&version=1.0&notify_url=http://notify_url&biz_content=%7B%22subject%22%3A%22order%22%2C%22out_trade_no%22%3A%22123123123131%22%2C%22total_amount%22%3A%22100%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%7D&sign=n4mZyQlQf+aK0GysIUXHMS+tNxaEtikdaKCBVo30HxFz5PhmKsJbr8FjR2T4/F1NZkzLEQwAJOTeJvmaDDiEO0WDL2AVxBbhallrPSQMDnpLuwNZD6dvbvzMjiLsc/HQAeHHnPWQHb2d/GKXTuyb0xvhYAJQK/iHoiCH24pfjVc=";

        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PAtyConsultStudioGoPaying.this);
                // 调用支付接口，获取支付结果
//                String result = String.valueOf(alipay.pay(payInfo,true));
//                String result = String.valueOf(alipay.payV2(payInfo, true));
                Map<String, String> result = alipay.payV2(payInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);

            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(PAtyConsultStudioGoPaying.this, "支付成功", Toast.LENGTH_SHORT).show();
                        if (ServiceType.DL.equals(service_type)){
                            Intent intent = new Intent();
                            intent.putExtra("alipayStatus","9000");
                            setResult(9000,intent);
                            finish();
                        }else {
                            Intent intent = new Intent(PAtyConsultStudioGoPaying.this, PAytSuccess.class);
                            if (!HStringUtil.isEmpty(service_type)) {
                                intent.putExtra(PAytSuccess.ORDER_TYPE, service_type);
                            }
                            intent.putExtra(PAytSuccess.NAME, name);
                            intent.putExtra(PAytSuccess.ID, id);
                            intent.putExtra(PAytSuccess.ORDER_ID, order_id);
                            intent.putExtra(PAytSuccess.PAY_ID, pay_id);
                            startActivity(intent);

                            EventBus.getDefault().post(new MyEvent("refresh", 0));
                            finish();
                        }

                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PAtyConsultStudioGoPaying.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(PAtyConsultStudioGoPaying.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };


    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo() {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + "2088121693621030" + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + "61@61120.net" + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + "1160513250164862" + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + "会诊服务" + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + "会诊服务" + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + "1" + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://220.194.46.204/DuoMeiHealth/servlet/NotifyReceiver61" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";
//        orderInfo += "&show_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }


    private IWXAPI api;

    private void wxPay() {
        String url = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";
//        Button payBtn = (Button) findViewById(R.id.appay_btn);
//        payBtn.setEnabled(false);
        Toast.makeText(PAtyConsultStudioGoPaying.this, "获取订单中...", Toast.LENGTH_SHORT).show();
        try {
            byte[] buf = Util.httpGet(url);
            if (buf != null && buf.length > 0) {
                String content = new String(buf);
                org.json.JSONObject json = new org.json.JSONObject(content);
                if (null != json && !json.has("retcode")) {
                    PayReq req = new PayReq();
                    req.appId = json.getString("appid");
                    req.partnerId = json.getString("partnerid");
                    req.prepayId = json.getString("prepayid");
                    req.nonceStr = json.getString("noncestr");
                    req.timeStamp = json.getString("timestamp");
                    req.packageValue = json.getString("package");
                    req.sign = json.getString("sign");
                    req.extData = "app data"; // optional
                    Toast.makeText(PAtyConsultStudioGoPaying.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    api.sendReq(req);
                } else {
                    Toast.makeText(PAtyConsultStudioGoPaying.this, "支付失败,请稍后重试", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(PAtyConsultStudioGoPaying.this, "服务器请求错误", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(PAtyConsultStudioGoPaying.this, "异常:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
//        payBtn.setEnabled(true);
    }

    /**
     * 微信支付
     *
     * @param
     */
    public void onClickWXPay() {
        if (isPaying)//正在支付
            return;
        isPaying = true;
        List<BasicNameValuePair> valuePairs = new ArrayList<>();
        if (!HStringUtil.isEmptyAndZero(site_id)) {
            valuePairs.add(new BasicNameValuePair("site_id", site_id));
            valuePairs.add(new BasicNameValuePair("service_type_id", service_type_id));
        }
        if (!HStringUtil.isEmptyAndZero(order_id_new)) {
            valuePairs.add(new BasicNameValuePair("order_id", order_id_new));
        }
        valuePairs.add(new BasicNameValuePair("op", "buyDoctorService"));
        valuePairs.add(new BasicNameValuePair("doctor_id", doctor_id));
        valuePairs.add(new BasicNameValuePair("service_item_id", serviceTypeId));
        valuePairs.add(new BasicNameValuePair("pay_type", "2"));
        valuePairs.add(new BasicNameValuePair("customer_id", LoginServiceManeger.instance().getLoginUserId()));

        HttpRestClient.doGetConsultationBuyStudioServlet(valuePairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onAfter() {
                super.onAfter();
                isPaying = false;
            }

            @Override
            public void onResponse(String response) {
//                try {
//                    org.json.JSONObject jsonObject = new org.json.JSONObject(response);
//                    int code = jsonObject.getInt("code");
//                    if (code==1){
//                        ToastUtil.onShow(PAtyConsultStudioGoPaying.this,jsonObject.getString("message"),1000);
//                        return;
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                BaseBean baseBean = JSONObject.parseObject(response, BaseBean.class);
                BaseBean.Love baseLove = baseBean.love;
                isPaying = false;

                if ("0".equals(baseLove.code)) {//成功
                    PaySuccessInfoEntity.getInstance().setPAYLOGO(1000);
                    PaySuccessInfoEntity.getInstance().setId(baseLove.doctor_id);
                    PaySuccessInfoEntity.getInstance().setName(baseLove.doctor_real_name);
                    PaySuccessInfoEntity.getInstance().setOrder_id(baseLove.order_id);
                    PaySuccessInfoEntity.getInstance().setPay_id(baseLove.pay_id);
                    sendWXPay(baseLove);
                } else if ("4".equals(baseLove.code)) {//0元免费

                    if ("888".equals(baseLove.role_id)) {
                        ToastUtil.showShort(PAtyConsultStudioGoPaying.this, baseLove.message);
                        Intent intent = new Intent(PAtyConsultStudioGoPaying.this, PAytSuccess.class);
                        if (!HStringUtil.isEmpty(service_type)) {
                            intent.putExtra(PAytSuccess.ORDER_TYPE, service_type);
                        }
                        intent.putExtra(PAytSuccess.NAME, baseLove.doctor_real_name);
                        intent.putExtra(PAytSuccess.ID, baseLove.doctor_id);
                        intent.putExtra(PAytSuccess.ORDER_ID, baseLove.order_id);
                        intent.putExtra(PAytSuccess.PAY_ID, baseLove.pay_id);
                        startActivity(intent);
                    } else {
                        ToastUtil.showShort(PAtyConsultStudioGoPaying.this, baseLove.message);
                    }
                } else {
                    ToastUtil.showShort(baseLove.message);
                }
            }
        }, this);

    }
 /**
     * 微信支付
     *
     * @param
     */
    public void onClickWXPayDL() {if (isPaying)//正在支付
     return;
     isPaying = true;
     List<BasicNameValuePair> valuePairs = new ArrayList<>();
        if (service_type.equals(ServiceType.DL)){
            valuePairs.add(new BasicNameValuePair("op", "buyCourse"));
        }else {
            valuePairs.add(new BasicNameValuePair("op", "Reward"));
        }     valuePairs.add(new BasicNameValuePair("course_id", course_id));
     valuePairs.add(new BasicNameValuePair("type", "2"));
     valuePairs.add(new BasicNameValuePair("course_price", price));
     valuePairs.add(new BasicNameValuePair("customer_id", LoginServiceManeger.instance().getLoginUserId()));

     HttpRestClient.doGetclassroomServlet(valuePairs, new MyResultCallback<String>(this) {
         @Override
         public void onError(Request request, Exception e) {
             Log.i("hhh", "onError: "+e.getMessage());
         }

         @Override
         public void onResponse(String response) {
             Log.i("hhh", "onError: " + response);
             isPaying = false;
             Gson gson = new Gson();
             WeiXinPayResultJson weiXinPayResultJson = gson.fromJson(response, WeiXinPayResultJson.class);
             WeiXinPayResultJson.ResultBean.RepmapBean repmap = weiXinPayResultJson.getResult().getRepmap();
             if (weiXinPayResultJson.getCode().equals("1")) {
                 PaySuccessInfoEntity.getInstance().setPAYLOGO(3000);
                 PayReq req = new PayReq();
                 req.appId = repmap.getAppid();
                 req.partnerId = repmap.getPartnerid();
                 req.prepayId = repmap.getPrepayid();
                 req.nonceStr = repmap.getNoncestr();
                 req.timeStamp = repmap.getTimestamp();
                 req.packageValue = repmap.getPackageX();
                 req.sign = repmap.getSign();
                 req.extData = "app data"; // optional
//            Toast.makeText(PAtyConsultStudioGoPaying.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                 // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                 api.sendReq(req);
             } else {
                 ToastUtil.onShow(PAtyConsultStudioGoPaying.this, "操作失败", 1000);
             }
         }
     }, this,service_type);

 }

    private void sendWXPay(BaseBean.Love bean) {
        org.json.JSONObject json = null;
        try {
            json = new org.json.JSONObject(bean.repmap);
            PayReq req = new PayReq();
            req.appId = json.getString("appid");
            req.partnerId = json.getString("partnerid");
            req.prepayId = json.getString("prepayid");
            req.nonceStr = json.getString("noncestr");
            req.timeStamp = json.getString("timestamp");
            req.packageValue = json.getString("package");
            req.sign = json.getString("sign");
            req.extData = "app data"; // optional
//            Toast.makeText(PAtyConsultStudioGoPaying.this, "正常调起支付", Toast.LENGTH_SHORT).show();
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            api.sendReq(req);

//            setResult(RESULT_OK);
//            EventBus.getDefault().post(new MyEvent("refresh", 2));
//            PAtyConsultStudioGoPaying.this.finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 钱包支付
     *
     * @param
     */
    public void onClickWalletPay(String password) {
        if (isPaying)//正在支付
            return;
        isPaying = true;
        List<BasicNameValuePair> valuePairs = new ArrayList<>();
        if (!HStringUtil.isEmptyAndZero(site_id)) {
            valuePairs.add(new BasicNameValuePair("site_id", site_id));
            valuePairs.add(new BasicNameValuePair("service_type_id", service_type_id));
        }
        if (!HStringUtil.isEmptyAndZero(order_id_new)) {
            valuePairs.add(new BasicNameValuePair("order_id", order_id_new));
        }
        valuePairs.add(new BasicNameValuePair("op", "buyDoctorService"));
        valuePairs.add(new BasicNameValuePair("doctor_id", doctor_id));
        valuePairs.add(new BasicNameValuePair("service_item_id", serviceTypeId));
        valuePairs.add(new BasicNameValuePair("pay_type", "4"));
        valuePairs.add(new BasicNameValuePair("pass", MD5Utils.getMD5(password)));
        valuePairs.add(new BasicNameValuePair("customer_id", LoginServiceManeger.instance().getLoginUserId()));

        HttpRestClient.doGetConsultationBuyStudioServlet(valuePairs, new HResultCallback<org.json.JSONObject>(this) {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onAfter() {
                super.onAfter();
                isPaying = false;
            }

            @Override
            public void onResponse(org.json.JSONObject response) {
                if (response.has("love")) {
                    org.json.JSONObject message = response.optJSONObject("love");
                    if (0 == message.optInt("code")) {
                        Intent intent = new Intent(PAtyConsultStudioGoPaying.this, PAytSuccess.class);
                        if (!HStringUtil.isEmpty(service_type)) {
                            intent.putExtra(PAytSuccess.ORDER_TYPE, service_type);
                        }
                        intent.putExtra(PAytSuccess.NAME, message.optString("doctor_real_name"));
                        intent.putExtra(PAytSuccess.ID, message.optString("doctor_id"));
                        intent.putExtra(PAytSuccess.ORDER_ID, message.optString("order_id"));
                        intent.putExtra(PAytSuccess.PAY_ID, message.optString("pay_id"));
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtil.showShort(message.optString("message"));
                    }
                } else {
                    ToastUtil.showShort(response.optString("message"));
                }

            }
        }, this);

    }

    public void onEvent(MyEvent event) {
        if (event.code==Constants.WXSUCCESS) {
            Intent intent = new Intent();
            intent.putExtra("alipayStatus","9000");
            setResult(9000,intent);
            finish();
        }
    }
    @Subscribe
    public void WeixinPayOkResult(int event) {
        if (event==3000){
            Intent intent = new Intent();
            intent.putExtra("alipayStatus","9000");
            setResult(9000,intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
