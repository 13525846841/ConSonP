package com.yksj.consultation.son.smallone.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.smallone.manager.LoginServiceManegerB;
import com.yksj.consultation.son.views.WheelView;
import com.yksj.healthtalk.net.http.HttpRestClientB;
import com.yksj.healthtalk.net.http.JsonHttpResponseHandler;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.WheelUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by jack_tang on 15/11/15.
 */
public class OrderUserInfoActivity extends BaseFragmentActivity implements View.OnClickListener {

    private EditText mName;
    private TextView mAge;
    private EditText mSfz;
    private EditText mPhone;
    private TextView mSex;
    private String ORDER_ID;
    private String SERVICE_ID;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.order_user_info_layout);
        initView();
    }

    private void initView() {
        initTitle();
//        setLeftBack(this);
        setTitle("个人信息");
        titleLeftBtn.setVisibility(View.VISIBLE);
        titleLeftBtn.setOnClickListener(this);
        findViewById(R.id.sure).setOnClickListener(this);
        JSONObject jsonObject = null;
        mName = ((EditText) findViewById(R.id.name));
        mAge = ((TextView) findViewById(R.id.age));
        mSfz = ((EditText) findViewById(R.id.sfz));
        mPhone = ((EditText) findViewById(R.id.number));
        mSex = ((TextView) findViewById(R.id.sex));
        try {
            jsonObject = new JSONObject(getIntent().getStringExtra("data"));
            ORDER_ID = jsonObject.optString("ORDER_ID");
            SERVICE_ID = getIntent().getStringExtra("TalkId");
            mName.setText(jsonObject.optString("REGISTER_NAME"));
            mAge.setText(jsonObject.optString("REGISTER_AGE"));
            mSfz.setText(jsonObject.optString("REGISTER_NUMBER"));
            mPhone.setText(jsonObject.optString("REGISTER_PHONE"));
            mSex.setText(jsonObject.optString("REGISTER_SEX"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mList = new ArrayList<Map<String, String>>();


        mAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop(1);
            }
        });
        mSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop(2);
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.sure:
                onSumit();
                break;
        }
    }

    /**
     * 提示判断
     */
    private void onSumit() {
        if (TextUtils.isEmpty(mName.getText().toString())) {
            ToastUtil.showShort("请填写姓名");
            return;
        }
        if (TextUtils.isEmpty(mAge.getText().toString())) {
            ToastUtil.showShort("请选择年龄");
            return;
        }
        if (TextUtils.isEmpty(mSfz.getText().toString()) || mSfz.getText().length() != 18) {
            ToastUtil.showShort("请填写请填写正确的身份号码");
            return;
        }
        if (TextUtils.isEmpty(mPhone.getText().toString()) || mPhone.getText().length() != 11) {
            ToastUtil.showShort("请填写正确的手机号码");
            return;
        }
        if (TextUtils.isEmpty(mSex.getText().toString())) {
            ToastUtil.showShort("请选择性别");
            return;
        }

        DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "个人信息提交后不能修改，请确认无误后再提交", "取消", "确认", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
            @Override
            public void onDismiss(DialogFragment fragment) {
            }

            @Override
            public void onClick(DialogFragment fragment, View v) {
                onsendHttp();
            }
        });
    }


    private void onsendHttp() {
        RequestParams params = new RequestParams();
        params.put("Type", "CusConfirmationOrder");
        params.put("REGISTER_NAME", mName.getText().toString());
        params.put("REGISTER_SEX", mSex.getText().toString());
        params.put("REGISTER_AGE", mAge.getText().toString());
        params.put("REGISTER_NUMBER", mSfz.getText().toString());
        params.put("REGISTER_PHONE", mPhone.getText().toString());
        params.put("ORDER_ID", ORDER_ID);
        params.put("CUSTOMER_ID", LoginServiceManegerB.instance().getLoginId());
        params.put("SERVICE_ID", HTalkApplication.getApplication().getmTalkServiceId());
        final StringBuffer buffer = new StringBuffer();
        buffer.append("您填写的信息如下:\n");
        buffer.append(mName.getText().toString() + ", " + mSex.getText().toString() + ", " + mAge.getText().toString() + "岁\n");
        buffer.append("身份证号: " + mSfz.getText().toString() + "\n");
        buffer.append("手机号码: " + mPhone.getText().toString());
        HttpRestClientB.doHttpcreateOrder(params, new JsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                ToastUtil.showShort(response.optString("message"));
                if (1 != response.optInt("code")) return;
                Intent intent = getIntent();
                intent.putExtra("message", buffer.toString());
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }


    private List<Map<String, String>> mList;// 周期/年龄

    private void showPop(int choose) {// 1是年龄,2是性别
        SystemUtils.hideSoftBord(this, mPhone);
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
        mPopupWindow = null;
        mList.clear();
        if (1 == choose) {
            for (int i = 0; i < 120; i++) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", String.valueOf(i + 1));
                mList.add(map);
            }
            mPopupWindow = WheelUtils.showSingleWheel(this, mList, mAge,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int index1 = (Integer) v.getTag(R.id.wheel_one);
                            Map<String, String> map = mList.get(index1);
                            String age = map.get("name");
                            mAge.setText(age);
                            mAge.setTag(age);
                        }
                    });
            if (mAge.getTag() != null) {
                WheelView wheelOne = (WheelView) mPopupWindow.getContentView().findViewById(R.id.wheel_one);
                wheelOne.setCurrentItem(Integer.valueOf(mAge.getTag().toString()) - 1);
            }
        } else {
            Map<String, String> map = new HashMap<String, String>();
            map.put("name", "男");
            mList.add(map);
            Map<String, String> map1 = new HashMap<String, String>();
            map1.put("name", "女");
            mList.add(map1);
            mPopupWindow = WheelUtils.showSingleWheel(this, mList,
                    mSex, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int index1 = (Integer) v.getTag(R.id.wheel_one);
                            Map<String, String> map = mList.get(index1);
                            String age = map.get("name");
                            mSex.setText(age);
                            mSex.setTag(age);
                            if ("男".equals(mSex.getTag().toString())) {
                                mSex.setText("男");
                            } else {
                                mSex.setText("女");
                            }
                        }
                    });

            if (mSex.getTag() != null) {
                WheelView wheelOne = (WheelView) mPopupWindow.getContentView().findViewById(R.id.wheel_one);
                if ("男".equals(mSex.getTag().toString())) {
                    wheelOne.setCurrentItem(0);
                } else {
                    wheelOne.setCurrentItem(1);
                }
            }
        }
    }
}
