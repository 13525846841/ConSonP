package com.yksj.consultation.son.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.AddMemberAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.bean.NewsClass;
import com.yksj.healthtalk.entity.AddMemberEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.ObjectHttpResponseHandler;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.ToastUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 添加成员
 */
public class AddMemberActivity extends BaseFragmentActivity implements View.OnClickListener, TextWatcher{

    private EditText phone_search;
    private AddMemberAdapter adapter;
    private ListView mLv;
    private List<AddMemberEntity> data;
    private AddMemberEntity addMemberEntity;
    private String childrenId = "";
    private String children_id;//宝贝ID
    private String customerId;//成员ID;
    private List<String> mCustomerId = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        initView();
    }

    private void initView() {
        initTitle();
        Intent intent = getIntent();
        children_id = intent.getStringExtra("CHILDREN_ID");
        customerId = intent.getStringExtra("customer_id");
        mCustomerId = Arrays.asList(customerId.split(","));

        titleTextV.setText("添加成员");
        titleRightBtn2.setVisibility(View.VISIBLE);
        titleRightBtn2.setText("添加");
        titleLeftBtn.setOnClickListener(this);
        titleRightBtn2.setOnClickListener(this);
        phone_search = (EditText) findViewById(R.id.phone_search);
        data = new ArrayList<AddMemberEntity>();
        phone_search.setHint("通过手机号码搜索");
        phone_search.addTextChangedListener(this);
    //  phone_search.setOnEditorActionListener(this);
        mLv = (ListView) findViewById(R.id.phone_lv_member);
        adapter = new AddMemberAdapter(this);
        mLv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right2:
//                if (childrenId.equals("11")){
//                    ToastUtil.showShort("请选择成员");
//                    return;
//                }else{
//                    addMember();
//                }
                addMember();
                break;
        }
    }
    private String customer_id = "";
    private void addMember() {
        customer_id = adapter.getChildrenId();
        if (TextUtils.isEmpty(customer_id)) {
            ToastUtil.showShort("请选择");
            return;
        }

        for (String customer:mCustomerId){
            if (customer.equals(customer_id)){
                ToastUtil.showShort("已经是成员了");
                return;
            }
        }


        Map<String,String> map=new HashMap<>();
        map.put("customer_id",customer_id);
        map.put("children_id",children_id);
        HttpRestClient.OKHttpAddMember(map, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {
                ToastUtil.showShort("添加失败");
            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if ("0".equals(obj.optString("code"))){
                        ToastUtil.showShort("添加成功");
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },this);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
    @Override
    public void afterTextChanged(Editable s) {
        initData();
    }

    private void initData() {
        String number = phone_search.getText().toString().trim();
        HttpRestClient.OKHttpGetPhoneCustom(number, new ObjectHttpResponseHandler() {
            @Override
            public Object onParseResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    JSONObject jsonObject = obj.getJSONObject("plan");
                    addMemberEntity = new AddMemberEntity();
                    addMemberEntity.setName(jsonObject.optString("CUSTOMER_NICKNAME"));
                    addMemberEntity.setImage(jsonObject.optString("CLIENT_ICON_BACKGROUND"));
                    addMemberEntity.setSex(jsonObject.optString("CUSTOMER_SEX"));
                    addMemberEntity.setCustomer_id(jsonObject.optString("CUSTOMER_ID"));
                    data.add(addMemberEntity);
                    return data;
                } catch (JSONException e) {
                    return null;
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                if (response != null) {
                    adapter.addAll((List<AddMemberEntity>) response);
                }
            }
        });

    }

}
