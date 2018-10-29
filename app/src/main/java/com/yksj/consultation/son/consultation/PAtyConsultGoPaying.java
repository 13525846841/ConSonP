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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.squareup.okhttp.Request;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog.OnDilaogClickListener;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.consultation.son.pay.PayResult;
import com.yksj.consultation.son.pay.SignUtils;
import com.yksj.consultation.son.setting.SettingPhoneBound;
import com.yksj.consultation.son.simcpux.Constants;
import com.yksj.consultation.son.simcpux.Util;
import com.yksj.consultation.son.wallet.PwdSettingActivity;
import com.yksj.healthtalk.bean.BaseBean;
import com.yksj.healthtalk.bean.PayBean;
import com.yksj.healthtalk.entity.GroupInfoEntity;
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
import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * @author HEKL
 *         会诊选择支付页面
 *         支付引导
 */
public class PAtyConsultGoPaying extends BaseFragmentActivity implements
        OnClickListener, OnDilaogClickListener, IWXAPIEventHandler {

    private String serviceTypeId;//支付类型id
    private String consultationId;//会诊id
    private static final int PLUGIN_NOT_INSTALLED = -1;
    private static final int PLUGIN_NEED_UPGRADE = 2;
    public static final int ACTIVITY_FINISH = 401;//销毁本界面

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
    private String price = "", couponPrice = "";//总价  优惠券价格
    //    private  double price2 = 0.0d , couponPrice2 = 0.0d ;//总价  优惠券价格
    private String couponId = "";//优惠券ID

    private TextView tvCoupon, tvPrice;
    private RelativeLayout couponLayout;
    private TextView tvUseCouponPrice;
    private TextView number;
    private TextView reminds;

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
        setContentView(R.layout.aty_guidefor_paying);
        EventBus.getDefault().register(this);

        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(Constants.APP_ID);
        api.handleIntent(getIntent(), this);
        initWidget();
        initDate();
//        initDate1();
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

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
                        number.setText("当前余额" + object.optString("balance") + "元");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static Double sub(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.subtract(b2).doubleValue();
    }

    private void initDate() {
        consultationId = getIntent().getStringExtra("consultId");
        serviceTypeId = getIntent().getStringExtra("service_item_id");
        price = getIntent().getStringExtra("price");
        tvPrice.setText(price + "元");
        if (getIntent().hasExtra("type")) {//没有订单
            type = getIntent().getExtras().getString("type");
        } else {
            payId = getIntent().getStringExtra("payId");
//				send_code = getIntent().getStringExtra("send_code");
        }

        List<BasicNameValuePair> valuePairs = new ArrayList<>();
        valuePairs.add(new BasicNameValuePair("CUSTOMERID", LoginServiceManeger.instance().getLoginEntity().getId()));
        valuePairs.add(new BasicNameValuePair("CONSULTATIONID", consultationId));
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
//                    ToastUtil.showShort(PAtyConsultGoPaying.this, bb.message);
//                    couponLayout.setVisibility(View.GONE);
                }
            }
        }, this);
    }

    private void initWidget() {
        initTitle();
        titleTextV.setText("选择支付方式");
        titleLeftBtn.setOnClickListener(this);
        findViewById(R.id.rl_zfb).setOnClickListener(this);
        findViewById(R.id.rl_wx).setOnClickListener(this);
        findViewById(R.id.rl_bank).setOnClickListener(this);
        findViewById(R.id.rl_purse).setOnClickListener(this);
//        reminds = (TextView) findViewById(R.id.reminds);
//        reminds.setText("温馨提示：\n 1.购买服务后，可向医生最多提问20条问题,图片不计数。\n 2.医生在繁忙期间，可能不能及时回复，请耐心等待。\n 3.一定时间内，医生没有回答您的内容，系统自动全额退款。");

        tvCoupon = (TextView) findViewById(R.id.tv_coupon_price);
        tvUseCouponPrice = (TextView) findViewById(R.id.tv_coupon_price);
        tvPrice = (TextView) findViewById(R.id.pay_amount2);
        couponLayout = (RelativeLayout) findViewById(R.id.rl_discount_coupon);
        couponLayout.setOnClickListener(this);
        number = (TextView) findViewById(R.id.pay_balance);
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
        valuePairs.add(new BasicNameValuePair("COUPONSID", couponId));
        valuePairs.add(new BasicNameValuePair("CONSULTATIONID", consultationId));
        valuePairs.add(new BasicNameValuePair("OPTION", "2"));
        valuePairs.add(new BasicNameValuePair("CUSTID", LoginServiceManeger.instance().getLoginEntity().getId()));

        HttpRestClient.doGetConsultationBuyServlet(valuePairs, new MyResultCallback<String>(this) {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                isPaying = false;
                PayBean payBean = JSONObject.parseObject(response, PayBean.class);
//              RSA_PRIVATE = payBean.sign;
                payZFB(payBean.source, payBean.sign);
//				ToastUtil.showShort(PAtyConsultGoPaying.this, payBean.message);
//				if (response.startsWith("<")) {//支付宝支付跳转
//					Intent intent = new Intent(getApplicationContext(), ConsultationAliPayActivity.class);
//					intent.putExtra("conId", consultationId);
//					intent.putExtra("couponId", couponId);
//					startActivityForResult(intent, ACTIVITY_FINISH);
//					return;
//				} else {
//					isPaying = false;
//					BaseBean baseBean = JSONObject.parseObject(response, BaseBean.class);
//					ToastUtil.showShort(PAtyConsultGoPaying.this, baseBean.message);
//				}
            }
        }, this);
    }

    /**
     * 支付宝支付
     *
     * @param v
     */
    public void onClickFreePay(View v) {
        if (isPaying)//正在支付
            return;
        isPaying = true;
        List<BasicNameValuePair> valuePairs = new ArrayList<>();
        valuePairs.add(new BasicNameValuePair("COUPONSID", couponId));
        valuePairs.add(new BasicNameValuePair("CONSULTATIONID", consultationId));
        valuePairs.add(new BasicNameValuePair("OPTION", "3"));
        valuePairs.add(new BasicNameValuePair("CUSTID", LoginServiceManeger.instance().getLoginEntity().getId()));

        HttpRestClient.doGetConsultationBuyServlet(valuePairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                BaseBean baseBean = JSONObject.parseObject(response, BaseBean.class);
                if ("1".equals(baseBean.code)) {//成功

                }
                ToastUtil.showShort(PAtyConsultGoPaying.this, baseBean.message);
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
        valuePairs.add(new BasicNameValuePair("COUPONSID", couponId));
        valuePairs.add(new BasicNameValuePair("CONSULTATIONID", consultationId));
        valuePairs.add(new BasicNameValuePair("OPTION", "1"));
        valuePairs.add(new BasicNameValuePair("CUSTID", LoginServiceManeger.instance().getLoginEntity().getId()));

        HttpRestClient.doGetConsultationBuyServlet(valuePairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {

                BaseBean baseBean = JSONObject.parseObject(response, BaseBean.class);
                if ("1".equals(baseBean.code)) {

                    /*************************************************
                     *
                     *  步骤2：通过银联工具类启动支付插件
                     *
                     ************************************************/
                    // mMode参数解释：
                    // 0 - 启动银联正式环境
                    // 1 - 连接银联测试环境
                    int ret = UPPayAssistEx.startPay(PAtyConsultGoPaying.this, null, null, baseBean.result, mMode);
                    if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
                        // 需要重新安装控件
                        AlertDialog.Builder builder = new AlertDialog.Builder(PAtyConsultGoPaying.this);
                        builder.setTitle("提示");
                        builder.setMessage("完成购买需要安装银联支付控件，是否安装？");

                        builder.setNegativeButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        UPPayAssistEx.installUPPayPlugin(PAtyConsultGoPaying.this);
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
                    }
                } else {
                    ToastUtil.showShort(PAtyConsultGoPaying.this, baseBean.message);
                }
            }
        }, this);
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
            DoubleBtnFragmentDialog.showDoubleBtnPay(PAtyConsultGoPaying.this, "输入支付密码", new DoubleBtnFragmentDialog.OnFirstClickListener() {
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
                PAtyConsultGoPaying.this.finish();
            }

        } else if (requestCode == 201 && resultCode == RESULT_OK) {//选择了优惠券
            couponPrice = data.getStringExtra("price");

            double num = sub(Double.valueOf(price), Double.valueOf(couponPrice));
            if (num > 0) {
                tvPrice.setText(num + "元");
            } else {
                tvPrice.setText(0 + "元");
            }

            tvUseCouponPrice.setText(data.getIntExtra("price", 0) + "元");
            tvCoupon.setText("优惠券");
            couponId = data.getIntExtra("id", 0) + "";

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
			 *      分别代表支付成功，支付失败，支付取消
			 */
            String str = data.getExtras().getString("pay_result");
            if (str.equalsIgnoreCase("success")) {
                msg = "支付成功！";
//				Intent intent=new Intent(PAtyConsultGoPaying.this,AtyConsultServer.class);
//				startActivity(intent);
                setResult(RESULT_OK);
                EventBus.getDefault().post(new MyEvent("refresh", 2));
                PAtyConsultGoPaying.this.finish();
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
        if (HStringUtil.isEmpty(price)) {
            price = "0";
        }
        if (HStringUtil.isEmpty(couponPrice)) {
            couponPrice = "0";
        }
        double nums = sub(Double.valueOf(price), Double.valueOf(couponPrice));
        switch (v.getId()) {
            case R.id.rl_zfb://支付宝
                if (nums <= 0)
                    onClickFreePay(v);
                else
                    onClickAlipay(v);
                break;
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.rl_wx://微信支付
                onClickWXPay();
//              startActivity(new Intent(this, PayActivity.class));
//              wxPay();
                break;
            case R.id.rl_bank://银联
                if (nums <= 0)
                    onClickFreePay(v);
                else
                    onClickUnionpay(v);
                break;
            case R.id.rl_purse://钱包
                if (nums <= 0)
                    onClickFreePay(v);
                else
                    onClickWallet(v);
                break;
            case R.id.rl_discount_coupon:
                Intent intent = new Intent(PAtyConsultGoPaying.this, PConsultCouponActivity.class);
                intent.putExtra("consultId", consultationId);
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
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
//        final String payInfo = "app_id=2017022405848913&method=alipay.trade.app.pay&format=JSON&charset=UTF-8&sign_type=RSA&timestamp=2017-03-02 13:04:51&version=1.0&notify_url=http://notify_url&biz_content=%7B%22subject%22%3A%22order%22%2C%22out_trade_no%22%3A%22123123123131%22%2C%22total_amount%22%3A%22100%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%7D&sign=n4mZyQlQf+aK0GysIUXHMS+tNxaEtikdaKCBVo30HxFz5PhmKsJbr8FjR2T4/F1NZkzLEQwAJOTeJvmaDDiEO0WDL2AVxBbhallrPSQMDnpLuwNZD6dvbvzMjiLsc/HQAeHHnPWQHb2d/GKXTuyb0xvhYAJQK/iHoiCH24pfjVc=";

        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PAtyConsultGoPaying.this);
                // 调用支付接口，获取支付结果
//                String result = String.valueOf(alipay.pay(payInfo,true));
                String result = String.valueOf(alipay.payV2(payInfo, true));
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
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(PAtyConsultGoPaying.this, "支付成功", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new MyEvent("refresh", 0));
                        finish();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PAtyConsultGoPaying.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(PAtyConsultGoPaying.this, "支付失败", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(PAtyConsultGoPaying.this, "获取订单中...", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(PAtyConsultGoPaying.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    api.sendReq(req);
                } else {
                    Toast.makeText(PAtyConsultGoPaying.this, "支付失败,请稍后重试", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(PAtyConsultGoPaying.this, "服务器请求错误", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(PAtyConsultGoPaying.this, "异常:" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        valuePairs.add(new BasicNameValuePair("COUPONSID", couponId));
        valuePairs.add(new BasicNameValuePair("CONSULTATIONID", consultationId));
        valuePairs.add(new BasicNameValuePair("OPTION", "4"));
        valuePairs.add(new BasicNameValuePair("CUSTID", LoginServiceManeger.instance().getLoginEntity().getId()));

        HttpRestClient.doGetConsultationBuyServlet(valuePairs, new OkHttpClientManager.ResultCallback<String>() {
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
                BaseBean baseBean = JSONObject.parseObject(response, BaseBean.class);
                if ("1".equals(baseBean.code)) {//成功
                    sendWXPay(baseBean);
                } else {
                    ToastUtil.showShort(baseBean.message);
                }
            }
        }, this);

    }

    private void sendWXPay(BaseBean bean) {
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
//            Toast.makeText(PAtyConsultGoPaying.this, "正常调起支付", Toast.LENGTH_SHORT).show();
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            api.sendReq(req);
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
        valuePairs.add(new BasicNameValuePair("COUPONSID", couponId));
        valuePairs.add(new BasicNameValuePair("CONSULTATIONID", consultationId));
        valuePairs.add(new BasicNameValuePair("OPTION", "5"));
        valuePairs.add(new BasicNameValuePair("PASS", MD5Utils.getMD5(password)));
        valuePairs.add(new BasicNameValuePair("CUSTID", LoginServiceManeger.instance().getLoginEntity().getId()));

        HttpRestClient.doGetConsultationBuyServlet(valuePairs, new OkHttpClientManager.ResultCallback<String>() {
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
                try {
                    org.json.JSONObject object = new org.json.JSONObject(response);
                    ToastUtil.showShort(object.optString("message"));
                    if (1 == object.optInt("code")) {
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this);

    }


    public void onEventMainThread(MyEvent event) {
        if (event.code == Constants.WXSUCCESS) {
            EventBus.getDefault().post(new MyEvent("refresh", 2));
            PAtyConsultGoPaying.this.finish();
        }
    }
}
