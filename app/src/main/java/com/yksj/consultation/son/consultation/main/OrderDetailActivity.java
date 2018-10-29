package com.yksj.consultation.son.consultation.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.SystemUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrderDetailActivity extends BaseFragmentActivity implements View.OnClickListener, DoubleBtnFragmentDialog.OnFristClickListener , DoubleBtnFragmentDialog.OnSecondClickListener{
    private String orderId = "";//订单ID
    private TextView callService;
    private TextView goPay;
    private TextView orderNumber;
    private TextView tvName,tvPhone,tvAddress;
    private TextView unitPrice;//单价
    private TextView freight;//运费
    private TextView totalPrice;//总价

    private TextView goodName;
    private TextView goodNumber;
    private TextView etText;//留言
    private ImageView imageView;
    private RelativeLayout rlCoupon;
    private TextView tvCoupon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initView();
    }

    private void initView() {
        initTitle();
        titleLeftBtn.setOnClickListener(this);
        titleTextV.setText("订单详情");
        orderId = getIntent().getStringExtra("ORDERID");

        imageView = (ImageView) findViewById(R.id.image_head);
        orderNumber = (TextView) findViewById(R.id.order_number);
        freight = (TextView) findViewById(R.id.freight);
        totalPrice = (TextView) findViewById(R.id.total_price);
        unitPrice = (TextView) findViewById(R.id.unitprice);

        tvCoupon = (TextView) findViewById(R.id.coupon_price);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvPhone = (TextView) findViewById(R.id.tv_name_number);
        tvAddress = (TextView) findViewById(R.id.address_detail);
        etText = (TextView) findViewById(R.id.et_text);
        goodName = (TextView) findViewById(R.id.product_name);
        goodNumber = (TextView) findViewById(R.id.product_number);
        callService = (TextView) findViewById(R.id.callservice);
        callService.setOnClickListener(this);
        goPay = (TextView) findViewById(R.id.gopay);
        goPay.setOnClickListener(this);

        rlCoupon = (RelativeLayout) findViewById(R.id.rl_coupon);
        initData();
    }

    private void initData() {
        Map<String,String> map=new HashMap<>();
        map.put("Type", "findOrderByIdExt");
        map.put("order_id", orderId);
        HttpRestClient.OKHttGoodsServlet(map, new HResultCallback<String>(this) {
            @Override
            public void onError(Request request, Exception e) {
            }
            @Override
            public void onResponse(String content) {
                try {
                     JSONObject obj = new JSONObject(content);
                     if (HttpResult.SUCCESS.equals(obj.optString("code"))){
                        JSONObject object = obj.optJSONObject("server_params");

                        orderNumber.setText("订单号: "+ object.optString("PAY_ID"));
                        tvName.setText( object.optString("CUSTOMER_NAME"));
                        tvPhone.setText( object.optString("CUSTOMER_PHONE"));
                        tvAddress.setText(object.optString("CUSTOMER_REMARK"));
                        goodName.setText(object.optString("GOODS_NAME"));
                        goodNumber.setText("数量:" + object.optString("GOOD_COUNT"));

                        freight.setText("￥"+object.optString("FREIGHT"));
                        unitPrice.setText("￥"+object.optString("CURRENT_PRICE"));
                        totalPrice.setText("￥"+object.optString("ORDER_GOLD_TOTAL"));

                        etText.setText(object.optString("ORDER_REMARK"));

                        String url = HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW + object.optString("GOOD_BIG_PIC");
                        Picasso.with(OrderDetailActivity.this).load(url).placeholder(R.drawable.waterfall_default).into(imageView);


                         JSONObject jsonObject = object.optJSONObject("COUPONS");
                         if (!HStringUtil.isEmpty(object.optString("COUPONS_ID"))){
                             rlCoupon.setVisibility(View.VISIBLE);
                             if ("10".equals(jsonObject.optString("COUPONS_TYPE"))){
                                 tvCoupon.setText("￥"+jsonObject.optString("COUPONS_VALUE"));
                             }else if ("20".equals(jsonObject.optString("COUPONS_TYPE"))){
                                 tvCoupon.setText(jsonObject.optString("COUPONS_VALUE") + "折");
                             }
                         }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        },this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.callservice:
                DoubleBtnFragmentDialog.showDoubleBtn4(OrderDetailActivity.this, "客服电话  10086", "取消", "呼叫", this, this).show();
                break;
            case R.id.gopay:
                Intent intent  = new Intent(this,ShopPayActivity.class);
                intent.putExtra("ORDERID",orderId);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBtn1() {

    }

    @Override
    public void onBtn2() {
        SystemUtils.callPhone(this,"10086");
    }
}
