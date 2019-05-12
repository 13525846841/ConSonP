package com.yksj.consultation.son.friend;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.squareup.okhttp.Request;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;
import com.yksj.consultation.adapter.ServicePayMainUiAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog.OnDilaogClickListener;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.comm.WalletPayFragmentDialog.OnClickSureBtnListener;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.buyandsell.MyOrdersActivity;
import com.yksj.consultation.son.consultation.PAytSuccess;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.consultation.son.pay.PayResult;
import com.yksj.consultation.son.setting.SettingPhoneBound;
import com.yksj.consultation.son.simcpux.Constants;
import com.yksj.consultation.son.wallet.PwdSettingActivity;
import com.yksj.healthtalk.bean.BaseBean;
import com.yksj.healthtalk.bean.PayBean;
import com.yksj.healthtalk.entity.PaySuccessInfoEntity;
import com.yksj.healthtalk.entity.TickMesg;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.MD5Utils;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.ValidatorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 去购买跳转的界面
 *
 * @author lmk
 */
public class ServicePayMainUi extends BaseFragmentActivity implements OnClickListener,
        OnClickSureBtnListener, OnDilaogClickListener, AdapterView.OnItemClickListener {
    private String url = null;
    private String consultId = null;
    private EditText editName, editPhoneNum, editBeizhu;  //姓名,手机号,备注
    private TextView tvWalletBalance2, tvExtraMoney;//钱包余额,另外一个是钱包支付那个地方显示,还需支付
    private TextView zhifubaoPay, yinlianPay;//支付宝,银联 钱包支付

    //    private CustomerInfoEntity doctorInfoEntity;
    private TickMesg mesg;//传过来的TickMesg门票实体信息
    private static final int PLUGIN_NOT_INSTALLED = -1;
    private static final int PLUGIN_NEED_UPGRADE = 2;
    private String payName = null;
    private String payPhone = null;
    private String payRemark = null;
    private int type;///日历0

    /*****************************************************************
     * mMode参数解释：
     * "00" - 启动银联正式环境
     * "01" - 连接银联测试环境
     *****************************************************************/
    private String mMode = "00";
    //    private static final String TN_URL_01 = "http://222.66.233.198:8080/sim/gettn";
    private boolean isBindPhone, isSetPsw;//是否绑定手机,是否设置支付密码
    private String payId;//支付订单号
    private int balance, privice;//余额,价格
    private boolean isPaying = false;
    private String SERVICE_PRICE, SERVICE_ITEM_ID, SERVICE_TYPE_SUB_ID, SERVICE_TYPE_ID;
    private static final int ACTIVITY_FINISH = 401;//销毁本界面

    public static final String DOCTOR_ID = "doctor_id";
    public static final String DOCTOR_NAME = "doctor_name";
    private int doctorId ;
    private String doctorName = "";


    private ListView mPaySelect;
    private ServicePayMainUiAdapter mAdapter;
    private List<JSONObject> mList = null;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.service_pay_layout);
        initView();
        initData();
        initPayData();
    }

    @Override
    protected void onStart() {
        super.onStart();
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
//		HttpRestClient.doHttpGetQianBao(SmartFoxClient.getLoginUserId(), new AsyncHttpResponseHandler(){
//			@Override
//			public void onSuccess(int statusCode, String content) {
//				super.onSuccess(statusCode, content);
//				try {
//						JSONObject object =new JSONObject(content);
//						tvWalletBalance2.setText("余额:"+object.optString("Balance","0")+"元");
//						isBindPhone=("1".equals(object.optString("BindPhone")));//是否设置密码
//						isSetPsw=("1".equals(object.optString("isSetPsw")));//是否绑定手机
//				} catch (Exception e) {
//				}
//			}
//		});
    }

    //初始化数据
    private void initData() {
        Intent intent = getIntent();
        consultId = getIntent().getStringExtra("consultationId");
        type = getIntent().getIntExtra("Type", 0);

            doctorId = getIntent().getIntExtra(DOCTOR_ID,0);
            doctorName = getIntent().getStringExtra(DOCTOR_NAME);
        if (intent.hasExtra("mesg")) {
            mesg = (TickMesg) intent.getSerializableExtra("mesg");
            privice = Integer.parseInt(mesg.getSERVICE_PRICE());
            SERVICE_PRICE = mesg.getSERVICE_PRICE();
            SERVICE_ITEM_ID = mesg.getSERVICE_ITEM_ID();
            SERVICE_TYPE_SUB_ID = mesg.getSERVICE_TYPE_SUB_ID();
            SERVICE_TYPE_ID = mesg.getSERVICE_TYPE_ID();
            url = getParams();
            try {
                JSONObject object = new JSONObject(intent.getStringExtra("json"));
                balance = Integer.parseInt(object.optString("Balance"));//余额
                isBindPhone = ("1".equals(object.optString("BindPhone")));//是否设置密码
                isSetPsw = ("1".equals(object.optString("isSetPsw")));//是否绑定手机
                payId = object.optString("PAY_ID");//订单号
                tvWalletBalance2.setText(balance + "元");
                int need = (privice - balance > 0) ? privice - balance : balance - privice;
                tvExtraMoney.setText(need + "元");
            } catch (JSONException e) {
            }
        } else if (intent.hasExtra("json")) {
            try {
                JSONObject json = new JSONObject(intent.getStringExtra("json"));
                SERVICE_PRICE = json.optString("SERVICE_PRICE");
                SERVICE_ITEM_ID = json.optString("SERVICE_ITEM_ID");
                SERVICE_TYPE_SUB_ID = json.optString("SERVICE_TYPE_SUB_ID");
                SERVICE_TYPE_ID = json.optString("SERVICE_TYPE_ID");

            } catch (Exception e) {
            }
        }


        if ("3".equals(SERVICE_TYPE_ID)) {
            findViewById(R.id.service_pay_basic_info_layout).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.service_pay_basic_info_layout).setVisibility(View.GONE);
        }


        /**留言
         * Intent intent=new Intent(LeaveMesgConsultActivity.this,ServicePayMainUi.class);
         intent.putExtra("doctorInfoEntity", doctorInfoEntity);
         intent.putExtra("mesg",mesg);
         intent.putExtra("json", object.toString());
         startActivity(intent);
         //预约  门诊
         Intent intent = new Intent(this,ServicePayMainUi.class);
         intent.putExtra("isAddress", true);
         intent.putExtra("json", data);
         intent.putExtra("doctorInfoEntity", mCustomerInfoEntity);
         startActivity(intent);
         //继续支付
         Intent intent = new Intent(this,ServicePayMainUi.class);
         intent.putExtra("json", mData);
         intent.putExtra("doctorInfoEntity", mCustomerInfoEntity);
         startActivity(intent);
         *
         */
    }

    //初始化视图
    private void initView() {
        initTitle();
        titleTextV.setText(R.string.pay);//支付
        titleLeftBtn.setOnClickListener(this);

        mPaySelect = (ListView) findViewById(R.id.pay_list_select);
        mAdapter = new ServicePayMainUiAdapter(this);
        mPaySelect.setAdapter(mAdapter);
        mPaySelect.setOnItemClickListener(this);

//        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api = WXAPIFactory.createWXAPI(this, null);
// 将该app注册到微信
        api.registerApp(Constants.APP_ID);
        editName = (EditText) findViewById(R.id.service_pay_basic_info_name2);
        editPhoneNum = (EditText) findViewById(R.id.service_pay_basic_info_phone2);
        editBeizhu = (EditText) findViewById(R.id.service_pay_basic_info_remark2);//备注
//        zhifubaoPay = (TextView) findViewById(R.id.service_pay_method_zhifubao);
//        yinlianPay = (TextView) findViewById(R.id.service_pay_method_yinlian);
//        zhifubaoPay.setOnClickListener(this);
//        findViewById(R.id.service_pay_method_weixin).setOnClickListener(this);
//        yinlianPay.setOnClickListener(this);
//        tvWalletBalance2 = (TextView) findViewById(R.id.service_pay_method_wallet_money);
//        tvWalletBalance2.setOnClickListener(this);
        tvExtraMoney = (TextView) findViewById(R.id.service_pay_still_need2);
        intBasicData();
    }
    /**
     * 加载支付方式的方法
     */
    private void initPayData() {
        Map<String,String> map=new HashMap<>();
        map.put("customer_id", LoginServiceManeger.instance().getLoginUserId());
        HttpRestClient.OKHttpQueryYellowBoy(map,  new HResultCallback<String>(this)  {
            @Override
            public void onError(Request request, Exception e){
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
        },this);
    }
    /**
     * 获取支付储存信息
     */
    private void intBasicData() {
        HttpRestClient.doHttpPayinfo(LoginServiceManeger.instance().getLoginEntity().getId(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                if (!TextUtils.isEmpty(content)) {
                    try {
                        JSONObject object = new JSONObject(content);
                        JSONObject json;
                        if (object.optInt("code") == 1) {
                            payName = object.getJSONObject("result").optString("PATIENT_NAME");
                            payPhone = object.getJSONObject("result").optString("PATIENT_PHONE");
                            payRemark = object.getJSONObject("result").optString("ADVICE_CONTENT");
                            if (getIntent().hasExtra("json")) {
                                json = new JSONObject(getIntent().getStringExtra("json"));
                                if (!"".equals(json.optString("ORDER_ID"))) {
                                    editName.setText(json.optString("PATIENT_NAME"));
                                    editPhoneNum.setText(json.optString("PATIENT_PHONE"));
                                    editBeizhu.setText(json.optString("ADVICE_CONTENT"));
                                } else {
                                    if (!"".equals(payName)) {
                                        editName.setText(payName);
                                    }
                                    if (!"".equals(payPhone)) {
                                        editPhoneNum.setText(payPhone);
                                    }
                                    if (!"".equals(payRemark)) {
                                        editBeizhu.setText(payRemark);
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
//            case R.id.service_pay_method_zhifubao://支付宝
//                if ("3".equals(SERVICE_TYPE_ID) && (HStringUtil.isEmpty(editName.getText().toString()))) {
//                    ToastUtil.showShort("请输入姓名");
//                    return;
//                }
//
//                if ("3".equals(SERVICE_TYPE_ID) && !ValidatorUtil.checkMobile(editPhoneNum.getText().toString())) {
//                    ToastUtil.showShort("请输入正确的手机号码!");
//                    return;
//                }
//                String params = getPayService(0, "");
//                if (HStringUtil.isEmpty(params)) return;
//                HttpRestClient.doHttpGetAliPay(params, new AyncHander("Alipay"));
//                break;
//            case R.id.service_pay_method_weixin://微信
//                if ("3".equals(SERVICE_TYPE_ID) && (HStringUtil.isEmpty(editName.getText().toString()))) {
//                    ToastUtil.showShort("请输入姓名");
//                    return;
//                }
//
//                if ("3".equals(SERVICE_TYPE_ID) && !ValidatorUtil.checkMobile(editPhoneNum.getText().toString())) {
//                    ToastUtil.showShort("请输入正确的手机号码!");
//                    return;
//                }
//                String params2 = getPayService(1, "");
//                if (HStringUtil.isEmpty(params2)) return;
//                HttpRestClient.doHttpGetWeixinPay(params2, new AyncHander("Alipay"));
//                break;
//            case R.id.service_pay_method_yinlian://银联
//                if ("3".equals(SERVICE_TYPE_ID) && (HStringUtil.isEmpty(editName.getText().toString()))) {
//                    ToastUtil.showShort("请输入姓名");
//                    return;
//                }
//
//                if ("3".equals(SERVICE_TYPE_ID) && !ValidatorUtil.checkMobile(editPhoneNum.getText().toString())) {
//                    ToastUtil.showShort("请输入正确的手机号码!");
//                    return;
//                }
//                String para = getPayService(2, "");
//                if (HStringUtil.isEmpty(para)) return;
//                HttpRestClient.doHttpGetUnionPay(para, new AyncHander("Unionpay"));
//                break;
//            case R.id.service_pay_method_wallet_money://钱包支付
//                onClickWallet();
//			if("3".equals(SERVICE_TYPE_ID) && (HStringUtil.isEmpty(editName.getText().toString()))){
//				ToastUtil.showShort("请输入姓名");
//				return ;
//			}
//
//		if ("3".equals(SERVICE_TYPE_ID) && !ValidatorUtil.checkMobile(editPhoneNum.getText().toString())){
//			ToastUtil.showShort("请输入正确的手机号码!");
//			return ;
//		}
//

//			if (!isBindPhone) {
//				DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(),  "使用钱包支付，需绑定手机并设置支付密码，您目前未绑定手机。", "稍后再说", "现在绑定", this);
//			}else if(!isSetPsw){
//				DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(),  "使用钱包支付，需绑定手机并设置支付密码，您目前未设置支付密码。", "稍后再说", "现在设置", this);
//			}else {
//				WalletPayFragmentDialog.show(getSupportFragmentManager(), "请输入支付密码","" );
//			}
//                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (mAdapter.PayType(position)){
            case 3://支付宝
                if ("3".equals(SERVICE_TYPE_ID) && (HStringUtil.isEmpty(editName.getText().toString()))) {
                    ToastUtil.showShort("请输入姓名");
                    return;
                }

                if ("3".equals(SERVICE_TYPE_ID) && !ValidatorUtil.checkMobile(editPhoneNum.getText().toString())) {
                    ToastUtil.showShort("请输入正确的手机号码!");
                    return;
                }
                String params = getPayService(0, "");
                if (HStringUtil.isEmpty(params)) return;
                HttpRestClient.doHttpGetAliPay(params, new AyncHander("Alipay"));
                break;

            case 2://微信支付
                if ("3".equals(SERVICE_TYPE_ID) && (HStringUtil.isEmpty(editName.getText().toString()))) {
                    ToastUtil.showShort("请输入姓名");
                    return;
                }

                if ("3".equals(SERVICE_TYPE_ID) && !ValidatorUtil.checkMobile(editPhoneNum.getText().toString())) {
                    ToastUtil.showShort("请输入正确的手机号码!");
                    return;
                }
                String params2 = getPayService(1, "");
                if (HStringUtil.isEmpty(params2)) return;
                HttpRestClient.doHttpGetWeixinPay(params2, new AyncHander("Alipay"));
                break;
            case 4://银联
                if ("3".equals(SERVICE_TYPE_ID) && (HStringUtil.isEmpty(editName.getText().toString()))) {
                    ToastUtil.showShort("请输入姓名");
                    return;
                }

                if ("3".equals(SERVICE_TYPE_ID) && !ValidatorUtil.checkMobile(editPhoneNum.getText().toString())) {
                    ToastUtil.showShort("请输入正确的手机号码!");
                    return;
                }
                String para = getPayService(2, "");
                if (HStringUtil.isEmpty(para)) return;
                HttpRestClient.doHttpGetUnionPay(para, new AyncHander("Unionpay"));
                break;
            case 1://钱包

                onClickWallet();

                break;
        }
    }


    //支付操作类
    class AyncHander extends AsyncHttpResponseHandler {
        private String type;

        public AyncHander(String string) {
            super(ServicePayMainUi.this);
            this.type = string;
        }

        @Override
        public void onStart() {
            super.onStart();
            if (isPaying)//正在支付
                return;
            isPaying = true;
        }

        @Override
        public void onFinish() {
            super.onFinish();
            isPaying = false;
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);
            isPaying = false;
        }

        @Override
        public void onSuccess(int statusCode, String content) {
            if (content.contains("source")) {
                PayBean payBean = com.alibaba.fastjson.JSONObject.parseObject(content, PayBean.class);
                payZFB(payBean.source, payBean.sign);
            } else {
                try {
                    JSONObject response = new JSONObject(content);
                    if (response.optInt("code") != 1) {
                        SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), response.optString("message"));
                        return;
                    } else if (type.equals("Wallet")) {
                        if (!response.has("error_message")) {
                            ToastUtil.showShort("支付成功");
//                            FriendHttpUtil.chatFromPerson(ServicePayMainUi.this,doctorId,doctorName);
                            Intent intent = new Intent(ServicePayMainUi.this, MyOrdersActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    } else if ("tn".equals(response.optString("message"))) {
//					{"PAY_ID":"1140227007141885","tn":"201402271420460080192"}
                        /*************************************************
                         *
                         *  步骤2：通过银联工具类启动支付插件
                         *
                         ************************************************/
                        // mMode参数解释：
                        // 0 - 启动银联正式环境
                        // 1 - 连接银联测试环境
                        int ret = UPPayAssistEx.startPay(ServicePayMainUi.this, null, null, response.optString("result"), mMode);
                        if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
                            // 需要重新安装控件
                            AlertDialog.Builder builder = new AlertDialog.Builder(ServicePayMainUi.this);
                            builder.setTitle("六一健康");
                            builder.setMessage("完成购买需要安装银联支付控件，是否安装？");
                            builder.setNegativeButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.dismiss();
                                            UPPayAssistEx.installUPPayPlugin(ServicePayMainUi.this);
                                        }
                                    });

                            builder.setPositiveButton("取消",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            builder.create().show();
                        } else {
                            finish();
                        }
                    } else if (response.has("LING")) {//价格为0 跳转到 聊天界面
//                        FriendHttpUtil.chatFromPerson(ServicePayMainUi.this,doctorId,doctorName);
                    } else if (content.contains("WXPay")) {//微信支付
                        PaySuccessInfoEntity.getInstance().setPAYLOGO(2000);
                        sendWXPay(content);
                    }
                } catch (Exception e) {
//                if (content.contains("支付宝")) {
//                    Intent intent = new Intent(getApplicationContext(), PayActivity.class);
//                    intent.putExtra("summary", content);
//                    intent.putExtra("mCustomerInfoEntity", doctorInfoEntity);
//                    startActivityForResult(intent,ACTIVITY_FINISH);
//                }
                }
            }

            super.onSuccess(statusCode, content);
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
            setResult(RESULT_OK);
            finish();
        } else {
            /*************************************************
             *
             *  步骤3：处理银联手机支付控件返回的支付结果
             *
             ************************************************/
            if (data == null) {
                return;
            }
            String msg = "";
            /*
             * 支付控件返回字符串:success、fail、cancel
	         * 分别代表支付成功，支付失败，支付取消
	         */
            String str = data.getExtras().getString("pay_result");
            if (str.equalsIgnoreCase("success")) {
                msg = "支付成功！";
                Intent intent = new Intent(ServicePayMainUi.this, PAytSuccess.class);
                startActivity(intent);
                setResult(RESULT_OK);
                EventBus.getDefault().post(new MyEvent("refresh", 2));
                ServicePayMainUi.this.finish();

            } else if (str.equalsIgnoreCase("fail")) {
                msg = "支付失败！";
            } else if (str.equalsIgnoreCase("cancel")) {
                msg = "用户取消了支付";
            }

            SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), msg);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
     * 如果是门诊加号 或者 预约咨询 就装数据
     */
    public String getPayService(int type, String password) {
        String str = null;
        org.json.JSONObject json;
        if (url != null) return url;
        try {
            json = new org.json.JSONObject(getIntent().getStringExtra("json"));
        } catch (Exception e) {
            return null;
        }
        RequestParams params = new RequestParams();
        params.put("DOCTORID", doctorId+"");
        switch (type) {
            case 0://支付宝
                params.put("Type", "MedicallyRegistered330");
                break;
            case 1://微信
                params.put("Type", "wx");
                break;
            case 2://银联
                params.put("Type", "MedicallyRegistered");
                break;
            case 3://余额
                params.put("Type", "ye");
                params.put("PASS", MD5Utils.getMD5(password));
                break;
        }
        params.put("SERVICE_ITEM_ID", SERVICE_ITEM_ID);
        params.put("SERVICE_TYPE_ID", SERVICE_TYPE_ID);
        params.put("VALID_MARK", HTalkApplication.APP_VALID_MARK);
        if (type == 0) {
            params.put("ORDER_ID", json.optString("ORDER_ID"));
        }
        params.put("SERVICE_TYPE_SUB_ID", SERVICE_TYPE_SUB_ID);
        if (!json.has("SERVICE_TIME_BEGIN"))
            params.put("SELECTDATE", json.optString("SERVICE_START").substring(0, 8));
        else
            params.put("SELECTDATE", json.optString("SERVICE_TIME_BEGIN").substring(0, 8));

        if ("2".equals(SERVICE_TYPE_ID)) {
            params.put("ADVICE_CONTENT", editBeizhu.getText().toString());//咨询内容
        } else if ("3".equals(SERVICE_TYPE_ID)) {
            params.put("ADVICE_CONTENT", editBeizhu.getText().toString());//咨询内容
            params.put("PATIENT_NAME", editName.getText().toString());//名字
            params.put("PATIENT_PHONE", editPhoneNum.getText().toString());//手机
//				params.put("SERVICE_PLACE", json.optString("SERVICE_PLACE"));//地点
        }
        params.put("CUSTOMER_ID", LoginServiceManeger.instance().getLoginEntity().getId());
        params.put("VALID_MARK","60");
        return url = params.toString();

    }

    /**
     * 获取到查询钱包余额的字符串参数
     *
     * @return 字符串
     */
    public String getParams() {
        RequestParams params = new RequestParams();
        params.put("Type", "MedicallyRegistered");
        params.put("DOCTORID", doctorId+"");
//		params.put("SELECTDATE", null);
        params.put("CUSTOMER_ID", SmartFoxClient.getLoginUserId());
        params.put("SERVICE_ITEM_ID", SERVICE_ITEM_ID);
        params.put("SERVICE_TYPE_SUB_ID", SERVICE_TYPE_SUB_ID);
        params.put("SERVICE_TYPE_ID", SERVICE_TYPE_ID);
        return params.toString();
    }

    /**
     * 钱包支付调用
     */
    @Override
    public void onClickSureHander(String money) {
        if (TextUtils.isEmpty(money)) {
            ToastUtil.showShort(getApplicationContext(), "密码不能为空");
        } else {//ADVICE_CONTENT=wwfgg&DOCTORID=2607&CUSTOMER_ID=116305&SERVICE_ITEM_ID=24407&SERVICE_TYPE_ID=3&SERVICE_PLACE=嘻嘻嘻休息一&Type=MedicallyRegistered&ORDER_ID=&PATIENT_PHONE=15525252525&SELECTDATE=20150313&PATIENT_NAME=qqq
            String str[] = url.split("&");
            for (int i = 0; i < str.length; i++) {
                if (str[i].contains("Type")) {
                    str[i] = "Type=WalletPayment";
                }
            }
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < str.length; i++) {
                builder.append(str[i]);
                builder.append("&");
            }
            url = builder.toString().substring(0, builder.toString().length() - 1);
            HttpRestClient.doHttpWalletPay(url, MD5Utils.getMD5(money), new AyncHander("Wallet"));
        }
    }


    @Override
    public void onDismiss(DialogFragment fragment) {
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
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(ServicePayMainUi.this);
                // 调用支付接口，获取支付结果

//                String result = alipay.pay(payInfo, true);
                Map<String,String> result = alipay.payV2(payInfo, true);
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

    private static final int SDK_PAY_FLAG = 1;

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
//                    PayResult payResult = new PayResult((String) msg.obj);
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
                        Toast.makeText(ServicePayMainUi.this, "支付成功", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new MyEvent("refresh", 0));
                        finish();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(ServicePayMainUi.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(ServicePayMainUi.this, "支付失败", Toast.LENGTH_SHORT).show();
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


    private IWXAPI api;

    private void sendWXPay(String content) {
        BaseBean bean = com.alibaba.fastjson.JSONObject.parseObject(content, BaseBean.class);
        org.json.JSONObject json = null;
        try {
            json = new org.json.JSONObject(bean.result);
            PayReq req = new PayReq();
            req.appId = json.getString("appid");
            req.partnerId = json.getString("partnerid");
            req.prepayId = json.getString("prepayid");
            req.nonceStr = json.getString("noncestr");
            req.timeStamp = json.getString("timestamp");
            req.packageValue = json.getString("package");
            req.sign = json.getString("sign");
            req.extData = "app data"; // optional
//            Toast.makeText(ServicePayMainUi.this, "正常调起支付", Toast.LENGTH_SHORT).show();
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            api.sendReq(req);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
     * 钱包
     */

    public void onClickWallet() {
        if (!isBindPhone) {
            DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "使用钱包支付，需绑定手机并设置支付密码，您目前未绑定手机。", "稍后再说", "现在绑定", this);
        } else if (!isSetPsw) {//米有设置密码
            DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "使用钱包支付，需绑定手机并设置支付密码，您目前未设置支付密码。", "稍后再说", "现在设置", this);
        } else {
//            WalletPayFragmentDialog.show(getSupportFragmentManager(), "输入支付密码", "");
            DoubleBtnFragmentDialog.showDoubleBtnPay(ServicePayMainUi.this, "输入支付密码", new DoubleBtnFragmentDialog.OnFirstClickListener() {
                @Override
                public void onBtn1(final String str) {
                    if (!HStringUtil.isEmpty(str)) {
                        String para = getPayService(3, str);
                        HttpRestClient.doHttpGWalletPay(para, new AyncHander("Wallet"));
                    } else {
                        ToastUtil.showShort("密码不能为空");
                    }
                }
            }, null).show();
        }
    }
}
