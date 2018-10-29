package com.yksj.consultation.son.consultation.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Request;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GmNexeActivity extends BaseFragmentActivity implements View.OnClickListener {

    private LinearLayout ll_chuxu;//储蓄卡
    private LinearLayout ll_credit;//信用卡
    private ImageView iv_credit;
    private ImageView iv_chuxu;
    private Button getmon_next;
    private String number;
    private EditText edit_bank_number;
    private EditText edit_name;
    private EditText edit_bank_name;
    private EditText edit_bank_pro;
    private String bank;
    private String name;
    private String bank_name;
    private String bank_detail;
    private String accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gm_nexe);
        initView();
    }

    private void initView() {
        initTitle();
        titleTextV.setText("余额提现");
        titleLeftBtn.setOnClickListener(this);
        number = getIntent().getStringExtra("NUMBER");
        getmon_next = (Button) findViewById(R.id.getmon_next);
        getmon_next.setOnClickListener(this);
        ll_chuxu = (LinearLayout) findViewById(R.id.ll_chuxu);
        ll_credit = (LinearLayout) findViewById(R.id.ll_credit);
        iv_chuxu = (ImageView) findViewById(R.id.iv_chuxu);
        iv_credit = (ImageView) findViewById(R.id.iv_credit);
        ll_chuxu.setOnClickListener(this);
        ll_credit.setOnClickListener(this);
        edit_bank_number = (EditText) findViewById(R.id.edit_bank_number);
        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_bank_name = (EditText) findViewById(R.id.edit_bank_name);
        edit_bank_pro = (EditText) findViewById(R.id.edit_bank_pro);
        iv_credit.setSelected(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.getmon_next:
                Embody();
                break;
            case R.id.ll_chuxu:
                iv_credit.setSelected(false);
                iv_chuxu.setSelected(true);
                break;
            case R.id.ll_credit:
                iv_credit.setSelected(true);
                iv_chuxu.setSelected(false);
                break;
        }
    }

    /**
     * 体现
     */
    private void Embody() {
        if (iv_credit.isSelected()){
            accountType ="1";
        } else if (iv_chuxu.isSelected()) {
            accountType ="0";
        }
        bank = edit_bank_number.getText().toString();
        name = edit_name.getText().toString();
        bank_name = edit_bank_name.getText().toString();
        bank_detail = edit_bank_pro.getText().toString();


        if (TextUtils.isEmpty(bank)) {
            ToastUtil.showToastPanl("请填写银行卡号");
            return;
        }
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showToastPanl("请填写姓名");
            return;
        }
        if (TextUtils.isEmpty(bank_name)) {
            ToastUtil.showToastPanl("请填写银行名称");
            return;
        }
        if (TextUtils.isEmpty(bank_detail)) {
            ToastUtil.showToastPanl("请填写开户行");
            return;
        }

        if (!SystemUtils.isNetWorkValid(this)) {
            ToastUtil.showShort(this, R.string.getway_error_note);
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", LoginServiceManeger.instance().getLoginUserId());
        map.put("withdrawal_money", number);
        map.put("bank_account", bank);
        map.put("payee", name);
        map.put("bank_full_name", bank_name);
        map.put("subbranch_name", bank_detail);
        map.put("bank_account_type", accountType);

        HttpRestClient.OKHttpEmbodyMoney(map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                ToastUtil.showShort("提现失败");
            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if ("1".equals(obj.optString("code"))) {
                        Intent intent = new Intent(GmNexeActivity.this,WarnActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        ToastUtil.showShort(obj.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },this);
    }
}
