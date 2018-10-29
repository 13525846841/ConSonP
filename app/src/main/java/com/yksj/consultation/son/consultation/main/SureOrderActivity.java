package com.yksj.consultation.son.consultation.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.views.AmountView;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import static com.yksj.consultation.son.R.id.et_text;

public class SureOrderActivity extends BaseFragmentActivity implements View.OnClickListener {

    private static final int DEF_DIV_SCALE = 10;
    private String customerId = "";
    private TextView address;
    private TextView productName;
    private TextView tvUnitPrice;
    private TextView unitPrice;//单价
    private TextView freight;//运费
    private TextView totalPrice;//总价
    private ImageView imageView;
    private AmountView mAmountView;
    private int mAmount = 1;//购买商品数量
    private EditText etText;//买家留言

    private TextView tvName, tvPhone, tvAddress;

    private String unitprice, freightprice, totalprice;//第三个，总价
    private RelativeLayout morenAddress;

    private String customerRemark;
    private RelativeLayout rlCoupon;//优惠券
    private String couponPrice = "";//优惠券的价格
    private String total = "";//未使用优惠券前的价格
    private TextView tvCouponPrice;

    private String couponsType = "";//优惠券类型


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sure_order);
        initView();

    }

    private void initView() {
        initTitle();
        titleLeftBtn.setOnClickListener(this);
        titleTextV.setText("确认订单");
        unitPrice = (TextView) findViewById(R.id.unitprice);
        address = (TextView) findViewById(R.id.add_address);
        freight = (TextView) findViewById(R.id.freight);
        totalPrice = (TextView) findViewById(R.id.total_price);
        productName = (TextView) findViewById(R.id.product_name);
        tvUnitPrice = (TextView) findViewById(R.id.product_price);
        imageView = (ImageView) findViewById(R.id.image_head);
        etText = (EditText) findViewById(et_text);
        morenAddress = (RelativeLayout) findViewById(R.id.moren_address);
        unitprice = getIntent().getStringExtra("CURRENT_PRICE");
        freightprice = getIntent().getStringExtra("FREIGHT");
        customerId = LoginServiceManeger.instance().getLoginEntity().getId();

        rlCoupon = (RelativeLayout) findViewById(R.id.rl_coupon);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvPhone = (TextView) findViewById(R.id.tv_name_number);
        tvAddress = (TextView) findViewById(R.id.address_detail);

        totalprice = String.valueOf(changeType(Double.parseDouble(unitprice) + Double.parseDouble(freightprice)));
        goodId = getIntent().getStringExtra("GOODID");

        tvCouponPrice = (TextView) findViewById(R.id.coupon_price);
        productName.setText(getIntent().getStringExtra("GOODS_NAME"));
        tvUnitPrice.setText("￥ " + unitprice);
        unitPrice.setText("￥ " + unitprice);
        freight.setText("￥ " + freightprice);
        totalPrice.setText("￥ " + totalprice);

        String url = HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW + getIntent().getStringExtra("GOOD_BIG_PIC");
        Picasso.with(this).load(url).placeholder(R.drawable.waterfall_default).into(imageView);

        findViewById(R.id.buy_address).setOnClickListener(this);
        findViewById(R.id.add_order).setOnClickListener(this);
        findViewById(R.id.coupon).setOnClickListener(this);
        mAmountView = (AmountView) findViewById(R.id.amount_view);
        mAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                mAmount = amount;
                unitPrice.setText("￥ " + String.valueOf(changeType(Double.parseDouble(unitprice) * mAmount)));

                if (HStringUtil.isEmpty(couponsType)) {
                    totalprice = add(Double.parseDouble(unitprice) * mAmount, Double.parseDouble(freightprice));
                    totalPrice.setText("￥" + totalprice);
                } else {
                    total = add(Double.parseDouble(unitprice) * mAmount, Double.parseDouble(freightprice));//未使用优惠券前的价格
                    if ("20".equals(couponsType)) {
                        totalprice = mul(Double.valueOf(total), Double.parseDouble(mul(Double.valueOf(couponPrice), 0.10)));
                    } else {
                        totalprice = minus(Double.valueOf(total), Double.valueOf(couponPrice));
                    }
                    totalPrice.setText("￥" + totalprice);
                }


            }
        });

        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((start + count) <= 200 && etText.getText().toString().length() <= 200) {

                } else {
                    etText.setText(s.subSequence(0, 200));
                    etText.setSelection(etText.getText().toString().length() - 1);
                    ToastUtil.showShort(SureOrderActivity.this, "最多可输入200个字");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etText.setHint("请输入内容(内容小于200字)");
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    public static String changeType(double v3) {
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(v3);
    }

    //加
    public static String add(double v1, double v2) {
        DecimalFormat df = new DecimalFormat("######0.00");
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return df.format(b1.add(b2).doubleValue());
    }

    //减
    public static String minus(double v1, double v2) {
        DecimalFormat df = new DecimalFormat("######0.00");
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return df.format(b1.subtract(b2).doubleValue());
    }

    //乘
    public static String mul(Double v1, Double v2) {
        DecimalFormat df = new DecimalFormat("######0.00");
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return df.format(b1.multiply(b2).doubleValue());
    }

    /**
     * 请求默认送货地址
     */

    private boolean isNullAddress = false;//判断地址是否为空

    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("Type", "findDefaultAddress");
        map.put("customer_id", customerId);
        HttpRestClient.OKHttGoodsServlet(map, new HResultCallback<String>(this) {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))) {
                        JSONObject object = obj.optJSONObject("server_params");
                        if (object.length() == 0) {
                            isNullAddress = false;
                            address.setVisibility(View.VISIBLE);
                            morenAddress.setVisibility(View.GONE);
                        } else {
                            isNullAddress = true;
                            address.setVisibility(View.GONE);
                            morenAddress.setVisibility(View.VISIBLE);
                            shopName = object.optString("CUSTOMER_NAME");
                            shopPhone = object.optString("CUSTOMER_PHONE");
                            areaCode = object.optString("CUSTOMER_ADDRESS");
                            tvName.setText(shopName);
                            tvPhone.setText(shopPhone);
                            customerRemark = object.optString("CUSTOMER_REMARK");
                            tvAddress.setText(customerRemark);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this);
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.buy_address:
                intent = new Intent(this, AddressActivity.class);
                startActivity(intent);
                break;
            case R.id.add_order:
                addOrder();
                break;
            case R.id.coupon://优惠券
                intent = new Intent(SureOrderActivity.this, ShopCouponActivity.class);
                intent.putExtra("GOODID", goodId);
                intent.putExtra("TOTALPRICE", totalprice);
                intent.putExtra("COUPONID", couponId);
                startActivityForResult(intent, 201);
                break;
        }
    }

    private String areaCode = "";
    private String shopName = "";
    private String shopPhone = "";
    private String goodId = "";

    /**
     * 提交订单
     */
    private void addOrder() {
        if (!isNullAddress) {
            ToastUtil.showToastPanl("请填写地址");
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("Type", "createOrder");
        map.put("customer_id", customerId);
        map.put("customer_address", areaCode);
        map.put("customer_name", shopName);
        map.put("customer_phone", shopPhone);
        map.put("customer_remark", customerRemark);
        map.put("freight", freightprice);
        map.put("good_count", String.valueOf(mAmount));
        map.put("good_id", goodId);
        map.put("order_gold_total", totalprice);
        map.put("coupons_id", couponId);
        map.put("order_remark", etText.getText().toString());
        HttpRestClient.OKHttGoodsServlet(map, new HResultCallback<String>(this) {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if ("1".equals(obj.optString("code"))) {
                        Intent intent = new Intent(SureOrderActivity.this, ShopPayActivity.class);
                        intent.putExtra("ORDERID", obj.optJSONObject("server_params").optString("order_id"));
                        startActivity(intent);

                    } else {
                        ToastUtil.showShort(obj.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this);
    }


    private String couponId = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        total = add(Double.parseDouble(unitprice) * mAmount, Double.parseDouble(freightprice));
        if (requestCode == 201 && resultCode == RESULT_OK) {//选择了优惠券
            couponPrice = data.getStringExtra("price");
            couponId = data.getStringExtra("id");
            couponsType = data.getStringExtra("COUPONS_TYPE");
            rlCoupon.setVisibility(View.VISIBLE);

            if ("20".equals(couponsType)) {
                tvCouponPrice.setText(couponPrice + "折");
                totalprice = mul(Double.valueOf(total), Double.parseDouble(mul(Double.valueOf(couponPrice), 0.10)));
            } else {
                tvCouponPrice.setText("￥ " + couponPrice);
                totalprice = minus(Double.valueOf(total), Double.valueOf(couponPrice));
            }

            totalPrice.setText("￥" + totalprice);

        }
    }
}
