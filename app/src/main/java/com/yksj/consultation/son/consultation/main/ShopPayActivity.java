package com.yksj.consultation.son.consultation.main;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
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
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.consultation.son.pay.PayResult;
import com.yksj.consultation.son.simcpux.Constants;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.MyResultCallback;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.ToastUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;


/**
 * 商城支付界面
 */
public class ShopPayActivity extends BaseFragmentActivity implements View.OnClickListener, AdapterView.OnItemClickListener,IWXAPIEventHandler {

    private ListView mPaySelect;
    private PaySelectAdapter mAdapter;
    private List<org.json.JSONObject> mList = null;

    private boolean isPaying = false;
    private static final int PLUGIN_NOT_INSTALLED = -1;
    private static final int PLUGIN_NEED_UPGRADE = 2;
    private static final int ACTIVITY_FINISH = 401;//销毁本界面

    private String mMode = "00";
    private IWXAPI api;
    private String orderId = "";//订单ID

    private TextView orderNumber;//订单号
    private TextView orderGoodsName;
    private TextView orderAddress;
    private TextView orderPrice;
    private TextView orderNum;//订单数量
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_pay);
        initView();
    }

    private void initView() {
        initTitle();
        titleLeftBtn.setOnClickListener(this);
        titleTextV.setText("确定支付");

        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(Constants.APP_ID);
        api.handleIntent(getIntent(), this);

        orderNumber = (TextView) findViewById(R.id.order_number);
        orderGoodsName = (TextView) findViewById(R.id.product_name);
        orderAddress = (TextView) findViewById(R.id.address_name);
        orderPrice = (TextView) findViewById(R.id.product_price);
        orderNum = (TextView) findViewById(R.id.product_number);


        mPaySelect = (ListView) findViewById(R.id.pay_select);
        mAdapter = new PaySelectAdapter(this);
        mPaySelect.setAdapter(mAdapter);
        mPaySelect.setOnItemClickListener(this);

        orderId = getIntent().getStringExtra("ORDERID");
        initData();
        initPayData();
    }

    /**
     * 订单详情数据
     */
    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("Type", "findOrderById");
        map.put("order_id", orderId);
        HttpRestClient.OKHttGoodsServlet(map, new HResultCallback<String>(this) {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String content) {
                try {
                    org.json.JSONObject obj = new org.json.JSONObject(content);
                    org.json.JSONObject object = obj.optJSONObject("server_params");
                    orderNumber.setText(object.optString("PAY_ID"));
                    orderGoodsName.setText(object.optString("GOODS_NAME"));
                    orderAddress.setText(object.optString("CUSTOMER_REMARK"));
                    orderPrice.setText("￥ " + object.optString("ORDER_GOLD_TOTAL"));
                    orderNum.setText("×"+object.optString("GOOD_COUNT"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;

        }
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
                        Toast.makeText(ShopPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(ShopPayActivity.this, PAytSuccess.class);
//                        intent.putExtra(PAytSuccess.NAME, name);
//                        intent.putExtra(PAytSuccess.ID, id);
//                        intent.putExtra(PAytSuccess.ORDER_ID, order_id);
//                        intent.putExtra(PAytSuccess.PAY_ID, pay_id);
//                        startActivity(intent);

                        EventBus.getDefault().post(new MyEvent("refresh", 0));
                        finish();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(ShopPayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(ShopPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
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



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (mAdapter.PayType(position)) {
            case 3://支付宝
                onClickAlipay(view);
                break;

            case 2://微信支付
                onClickWXPay();
                break;
            case 4://银联
                onClickUnionpay(view);
                break;

        }
    }
    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
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
        valuePairs.add(new BasicNameValuePair("Type", "payOrder"));
        valuePairs.add(new BasicNameValuePair("pay_type", "20"));
        valuePairs.add(new BasicNameValuePair("order_id", orderId));

        HttpRestClient.OKHttGoodsPayServlet(valuePairs, new MyResultCallback<String>(this) {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {

                try {
                    org.json.JSONObject object = new org.json.JSONObject(response);
                    isPaying = false;
                    if ("0".equals(object.optString("code"))){//成功
                        org.json.JSONObject params = object.optJSONObject("server_params");
                        payZFB(params.optString("source"), params.optString("sign"));
                    }else {
                        ToastUtil.showShort(object.optString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this);
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

        valuePairs.add(new BasicNameValuePair("Type", "payOrder"));
        valuePairs.add(new BasicNameValuePair("pay_type", "30"));
        valuePairs.add(new BasicNameValuePair("order_id", orderId));

        HttpRestClient.OKHttGoodsPayServlet(valuePairs, new OkHttpClientManager.ResultCallback<String>() {
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
                    if ("0".equals(object.optString("code"))){//成功
                        org.json.JSONObject params = object.optJSONObject("server_params");
                        sendWXPay(params);
                    }
                    isPaying = false;
                } catch (JSONException e) {
                    e.printStackTrace();
                }



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
        valuePairs.add(new BasicNameValuePair("Type", "payOrder"));
        valuePairs.add(new BasicNameValuePair("pay_type", "10"));
        valuePairs.add(new BasicNameValuePair("order_id", orderId));

        HttpRestClient.OKHttGoodsPayServlet(valuePairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {

                try {
                    org.json.JSONObject object = new org.json.JSONObject(response);
                    isPaying = false;
                    if ("0".equals(object.optString("code"))) {

                        org.json.JSONObject params = object.optJSONObject("server_params");
                        /*************************************************
                         *
                         *  步骤2：通过银联工具类启动支付插件
                         *
                         ************************************************/
                        // mMode参数解释：
                        // 0 - 启动银联正式环境
                        // 1 - 连接银联测试环境
                        int ret = UPPayAssistEx.startPay(ShopPayActivity.this, null, null, params.optString("tn"), mMode);
                        if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
                            // 需要重新安装控件
                            AlertDialog.Builder builder = new AlertDialog.Builder(ShopPayActivity.this);
                            builder.setTitle("提示");
                            builder.setMessage("完成购买需要安装银联支付控件，是否安装？");

                            builder.setNegativeButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.dismiss();
                                            UPPayAssistEx.installUPPayPlugin(ShopPayActivity.this);
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
                    }else {
                        ToastUtil.showShort(ShopPayActivity.this,object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, this);
    }
    private void sendWXPay(org.json.JSONObject object) {
        org.json.JSONObject json = null;
        try {
            json = object.optJSONObject("repmap");

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
                PayTask alipay = new PayTask(ShopPayActivity.this);
                // 调用支付接口，获取支付结果
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
    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {

    }
}
