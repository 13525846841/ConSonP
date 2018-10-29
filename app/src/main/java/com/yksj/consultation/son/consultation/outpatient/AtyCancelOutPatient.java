package com.yksj.consultation.son.consultation.outpatient;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.yksj.consultation.adapter.AdtRadio;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog.OnDilaogClickListener;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.ObjectHttpResponseHandler;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * @author HEKL
 *         <p/>
 *         取消预约原因_
 */
public class AtyCancelOutPatient extends BaseFragmentActivity implements OnClickListener {
    ArrayList<HashMap<String, String>> list = new ArrayList<>();
    private AdtRadio mAdtRadio;
    private ListView radioList;
    private int docId;
    String orderId;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_cancel_consult);
        initView();
    }

    private void initView() {
        initTitle();
        titleRightBtn2.setOnClickListener(this);
        titleRightBtn2.setVisibility(View.VISIBLE);
        titleRightBtn2.setText("提交");
        titleLeftBtn.setOnClickListener(this);
        titleTextV.setText("取消预约");
        findViewById(R.id.view_line).setVisibility(View.GONE);
        findViewById(R.id.et_ortherreasons).setVisibility(View.GONE);
        orderId = getIntent().getStringExtra("orderId");
        docId = getIntent().getIntExtra("docId", 0);
        radioList = (ListView) findViewById(R.id.lv_choice);
        getSensonsData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right2:
                sendCancelReasons();
                break;
        }
    }

    /**
     * 获取取消服务原因
     */
    private void getSensonsData() {
        HttpRestClient.doHttpGetCancelReason("Order_Patient", new ObjectHttpResponseHandler(AtyCancelOutPatient.this) {
            JSONObject obj = null;

            @Override
            public Object onParseResponse(String content) {
                try {
                    obj = new JSONObject(content);
                    if (obj.optInt("code") != 1) {
                        ToastUtil.showShort(obj.optString("message"));
                    } else {
                        JSONArray array = obj.getJSONArray("result");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("ID", "" + object.optInt("ID"));
                            map.put("NAME", object.optString("NAME"));
                            list.add(map);
                        }
                    }
                } catch (JSONException e) {
                    return null;
                }
                return list;
            }

            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                mAdtRadio = new AdtRadio(AtyCancelOutPatient.this, list);
                radioList.setAdapter(mAdtRadio);
            }
        });
    }

    /**
     * 发送患者取消服务
     */
    private void sendSeasonsData(String reasons) {
        HttpRestClient.doHttpCancelOutpatient(orderId, LoginServiceManeger.instance().getLoginEntity().getId(), docId + "", reasons,
                new AsyncHttpResponseHandler(this) {
                    JSONObject obj = null;

                    @Override
                    public void onSuccess(int statusCode, String content) {
                        if (!TextUtils.isEmpty(content)) {
                            try {
                                obj = new JSONObject(content);
                                ToastUtil.showShort(obj.optString("message"));
                                if (obj.optInt("code") == 1) {
                                    EventBus.getDefault().post(new MyEvent("outPatientRefresh",2));
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        super.onSuccess(statusCode, content);
                    }
                });
    }

    /**
     * 点击提交
     */
    public void sendCancelReasons() {
        long[] authorsId = radioList.getCheckedItemIds();
        String code = "";
        final String reasons;
        if (authorsId.length > 0) {
            // 用户至少选择一个
            for (int i = 0; i < authorsId.length; i++) {
                code += "," + list.get((int) authorsId[i]).get("ID");
            }
            // 将第一个前面的“，”去掉
            reasons = code.substring(1);
            DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "您确认提交吗?", "取消", "确定",
                    new OnDilaogClickListener() {

                        @Override
                        public void onDismiss(DialogFragment fragment) {

                        }

                        @Override
                        public void onClick(DialogFragment fragment, View v) {
                            sendSeasonsData(reasons);
                        }
                    });
        } else {
            ToastUtil.showShort("请选择或填写取消预约的原因!");
        }

    }
}
