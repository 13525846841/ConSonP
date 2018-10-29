package com.yksj.consultation.son.consultation.main;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.AppraiseAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.LogUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/***
 *   会诊  评价
 */
public class AppraiseActivity extends BaseFragmentActivity implements View.OnClickListener {

    public static final String ISCOMMENT = "iscomment";
    public EditText mET;
    public TextView textNum;
    private int textNumber = 0;
    private ListView appListView;
    private AppraiseAdapter adapter;
    private List<JSONObject> list = null;
    private View vHead;
    private String content;//提醒内容

    private String costumerId =  SmartFoxClient.getLoginUserId();
    private String orderId;
    private boolean isComment;//1，已评价，2 未评价
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appraise);
        initView();
    }

    private void initView() {
        initTitle();
        titleTextV.setText("评价");
        titleLeftBtn.setOnClickListener(this);

        isComment = getIntent().getBooleanExtra(ISCOMMENT,true);
//        if (!isComment){
            titleRightBtn2.setVisibility(View.VISIBLE);
            titleRightBtn2.setText("确定");
            titleRightBtn2.setOnClickListener(this);
//
//        }
        vHead= View.inflate(this, R.layout.head_appraise, null);
        mET= (EditText) vHead.findViewById(R.id.et_evaluate);
        textNum= (TextView) vHead.findViewById(R.id.textcount);
        orderId = getIntent().getStringExtra("ORDER_ID");


        appListView = (ListView) findViewById(R.id.lv_appraise);
        appListView.addHeaderView(vHead);
        adapter = new AppraiseAdapter(this);
        appListView.setAdapter(adapter);
        mET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if((start +count)<=200 && mET.getText().toString().length()<=200){
                    textNumber = mET.getText().toString().length();
                    textNum.setText(textNumber+"/200");
                }else{
                    mET.setText(s.subSequence(0, 200));
                    ToastUtil.showShort(AppraiseActivity.this, "最多可输入200个字");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mET.setHint("请输入内容(内容小于200字)");
        initData();

     //   initCommentContent();
    }


    /**
     * 加载数据
     */
    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("op", "findDoctorByConsultation");
        map.put("consultation_id", orderId);//229586
        HttpRestClient.OKHttpAppraiseList(map, new HResultCallback<String>(this)  {

            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if(HttpResult.SUCCESS.equals(obj.optString("code"))){
                        JSONObject jsonobject = obj.optJSONObject("result");
                        list = new ArrayList<>();
                        list= parseData(jsonobject);
                        adapter.onBoundData(list);
                    }else {
                        ToastUtil.showShort(obj.optString("message"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this);
    }

    /**
     *发送评价
     */
    private void sendData() {
        content = mET.getText().toString().trim();
//        if (TextUtils.isEmpty(content)) {
//            ToastUtil.showToastPanl("请填写评级内容");
//            return;
//        }


        for (int i = 0; i < adapter.list.size(); i++) {
            switch (adapter.list.get(i).optInt("type")){
                case 0:
                case 2:
                case 4:
                    adapter.list.remove(i);
                    break;
            }
        }
        JSONArray array=new JSONArray();
        for (int j = 0; j <adapter.list.size() ; j++) {
            JSONObject object = new JSONObject();
            LogUtil.d("OM+++++++=========",adapter.list.get(j).optString("DOCTOR_ID"));
            LogUtil.d("OMOM+++++==========",adapter.ratingNumber());

            try {
                object.put("customer_id",costumerId);
                object.put("doctor_id",adapter.list.get(j).optString("DOCTOR_ID"));
                object.put("evaluate_level",adapter.ratingNumber());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(object);
        }
        LogUtil.d("OMOMpppppppppppp+++++==========",array.toString());


        Map<String, String> map = new HashMap<>();
        map.put("op", "consultationEvaluate");
        map.put("consultation_id", orderId);
        map.put("content", content);
        map.put("type", "consultation");//评价类型
        map.put("json", array.toString());


        HttpRestClient.OKHttpAppraiseList(map, new HResultCallback<String>(this) {

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if ("0".equals(obj.optString("code"))) {
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
            case R.id.title_right2:
                DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "感谢您的评价，确定要提交吗？", "取消", "确定",
                        new DoubleBtnFragmentDialog.OnDilaogClickListener() {

                            @Override
                            public void onDismiss(DialogFragment fragment) {
                            }
                            @Override
                            public void onClick(DialogFragment fragment, View v) {
                                sendData();
                            }
                        });
                break;

        }
    }


    private List<JSONObject> parseData(JSONObject jsonobject) {
        try {
            JSONArray artsArray = jsonobject.optJSONArray("jiceng");
            JSONArray hospitalArray = jsonobject.optJSONArray("yaoqing");
            JSONArray doctorArray = jsonobject.optJSONArray("zhuanjia");

            if (null != artsArray && artsArray.length() > 0) {
                //活动
                JSONObject object1 = new JSONObject();
                object1.put("type", 0);
                object1.put("title", "评价此次服务的医生");
                list.add(object1);
                for (int i = 0; i < artsArray.length(); i++) {
                    JSONObject artsObject = artsArray.getJSONObject(i);
                    artsObject.put("type", 1);
                    list.add(artsObject);
                }
            }

            if (null != hospitalArray && hospitalArray.length() > 0) {
                //医院
                JSONObject object2 = new JSONObject();
                object2.put("type", 2);
                object2.put("title", "评价此次服务的专家");
                list.add(object2);
                for (int j = 0; j < hospitalArray.length(); j++) {
                    JSONObject hospitalObject = hospitalArray.getJSONObject(j);
                    hospitalObject.put("type", 3);
                    list.add(hospitalObject);
                }
            }
            if (null != doctorArray && doctorArray.length() > 0) {
                //医生
                JSONObject object3 = new JSONObject();
                object3.put("type", 4);
                object3.put("title", "评价此次服务的主治专家");
                list.add(object3);
                for (int m = 0; m < doctorArray.length(); m++) {
                    JSONObject doctorObject = doctorArray.getJSONObject(m);
                    doctorObject.put("type", 5);
                    list.add(doctorObject);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
