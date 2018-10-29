package com.yksj.consultation.son.consultation.main;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.db.DictionaryHelper;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.StringFormatUtils;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.WheelUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 添加新地址的界面
 */
public class AddNewAddressActivity extends BaseFragmentActivity implements View.OnClickListener {

    private String customerId = "";
    private EditText name,phone,area;
    private TextView province;
    private String text = "";
    private View wheelView;

    private PopupWindow pop;
    private List<Map<String, String>> proList = null;
    private Map<String, List<Map<String, String>>> cityMap = new LinkedHashMap<String, List<Map<String, String>>>();
    private View mainView;
    private String locationCode = "";//所在地编码

    private String areaCode = "";
    private String addressId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address);

        initView();
    }

    private void initView() {
        initTitle();
        titleLeftBtn.setOnClickListener(this);
        titleTextV.setText("收货地址");


        name = (EditText) findViewById(R.id.edit_name);
        phone = (EditText) findViewById(R.id.phone);
        province = (TextView) findViewById(R.id.pricity);
        area = (EditText) findViewById(R.id.address_text);
        province.clearFocus();
        findViewById(R.id.rl_price).setOnClickListener(this);
        findViewById(R.id.add_address).setOnClickListener(this);

        wheelView = getLayoutInflater().inflate(R.layout.wheel, null);
        wheelView.findViewById(R.id.wheel_cancel).setOnClickListener(this);
        wheelView.findViewById(R.id.wheel_sure).setOnClickListener(this);
        pop = new PopupWindow(wheelView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mainView = getLayoutInflater().inflate(R.layout.full_person_message, null);
        customerId = LoginServiceManeger.instance().getLoginEntity().getId();
        addressId = getIntent().getStringExtra("address_id");

        if (!HStringUtil.isEmpty(addressId)){
            initData();
        }
        queryData();//获取地区信息
    }

    /**
     * 加载编辑信息
     */
    private void initData() {
        Map<String,String> map=new HashMap<>();
        map.put("Type", "findAddressById");
        map.put("address_id", addressId);
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

                        name.setText(object.optString("CUSTOMER_NAME"));
                        phone.setText(object.optString("CUSTOMER_PHONE"));
                        area.setText(object.optString("CUSTOMER_REMARK"));

                        areaCode = object.optString("CUSTOMER_ADDRESS");

                        JSONArray array = object.optJSONArray("area");
                        JSONObject jsonObject = null;
                        List<JSONObject> list = new ArrayList<>();
                        StringBuilder  sb = new StringBuilder();
                        for (int i = 0; i <array.length() ; i++) {
                            jsonObject = array.getJSONObject(i);
                            list.add(jsonObject);
                            sb.append(list.get(i).optString("AREA_NAME"));
                        }
                        province.setText(sb);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        },this);
    }

    private void save() {

        if (TextUtils.isEmpty( name.getText().toString())) {
            ToastUtil.showToastPanl("请填写姓名");
            return;
        }
        if (TextUtils.isEmpty( province.getText().toString())) {
            ToastUtil.showToastPanl("请填写所属省市");
            return;
        }
        if (TextUtils.isEmpty( phone.getText().toString())) {
            ToastUtil.showToastPanl("请填写电话号码");
            return;
        }

        if(phone.getText().toString().trim().length()>0){

            if(!StringFormatUtils.isPhoneNum(phone.getText().toString().trim())){
                ToastUtil.showShort("请输入正确的手机号");
                return ;
            }
        }
        if (TextUtils.isEmpty(area.getText().toString())) {
            ToastUtil.showToastPanl("请填写详细地址");
            return;
        }

        Map<String,String> map=new HashMap<>();
        map.put("Type", "addAddress");
        if(!HStringUtil.isEmpty(addressId)){
            map.put("address_id", addressId);
        }
        map.put("customer_id", customerId);
        map.put("customer_address", areaCode);
        map.put("customer_name", name.getText().toString());
        map.put("customer_phone", phone.getText().toString());
        map.put("customer_remark", area.getText().toString());
        HttpRestClient.OKHttGoodsServlet(map, new HResultCallback<String>(this) {
            @Override
            public void onError(Request request, Exception e) {
            }
            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))){
                        ToastUtil.showShort(obj.optString("message"));
                        finish();
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
            case R.id.add_address:
                save();
                break;
            case R.id.rl_price:
                setCity();
                break;

            case R.id.wheel_cancel://滑轮取消
                if (pop != null)
                    pop.dismiss();
                break;
            case R.id.wheel_sure://滑轮确定
                if (pop != null)
                    pop.dismiss();
                if (WheelUtils.getCurrent() != null) {
                    province.setText(WheelUtils.getCurrent());
                    areaCode = WheelUtils.getCode();
                }
                break;
        }
    }

    /**
     * 所在地
     */
    private void setCity() {
        if (proList == null || cityMap == null) {
        } else {
            WheelUtils.setDoubleWheel(this, proList, cityMap, mainView, pop,
                    wheelView);
        }
    }

    /**
     * 获取地区
     */
    private void queryData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                proList = DictionaryHelper.getInstance(AddNewAddressActivity.this).setCityData(
                        AddNewAddressActivity.this, cityMap);
            }
        }).start();
    }
}
