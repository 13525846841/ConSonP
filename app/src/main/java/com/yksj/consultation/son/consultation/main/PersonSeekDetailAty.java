package com.yksj.consultation.son.consultation.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.PersonSeekDetailAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.utils.HStringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonSeekDetailAty extends BaseFragmentActivity implements View.OnClickListener {

    public static final String PERSON_ID = "person_id";

    private ListView mListView;
    private PersonSeekDetailAdapter adapter;
    private List<JSONObject> list = null;
    private String person_id;
    private String name,sex,age,phone,idCard,allergy;

    private TextView nameTv,sexTv,ageTv,phoneTv,idCardTv,allergyTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_seek_detail_aty);
        initView();
    }

    private void initView() {
        initTitle();
        titleLeftBtn.setOnClickListener(this);
        titleTextV.setText("就诊人详情");

        findViewById(R.id.modify).setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.personseekdetail);
        adapter = new PersonSeekDetailAdapter(this);
        mListView.setAdapter(adapter);


        nameTv = (TextView) findViewById(R.id.name);
        sexTv = (TextView) findViewById(R.id.sex);
        ageTv = (TextView) findViewById(R.id.age);
        phoneTv = (TextView) findViewById(R.id.phone);
        idCardTv = (TextView) findViewById(R.id.id_card);
        allergyTv = (TextView) findViewById(R.id.allergy);


        if (getIntent().hasExtra(PERSON_ID)){
            person_id = getIntent().getStringExtra(PERSON_ID);
        }
        if (getIntent().hasExtra("name")){
            name = getIntent().getStringExtra("name");
            nameTv.setText("姓名: "+name);
        }
        if (getIntent().hasExtra("sex")){
            sex = getIntent().getStringExtra("sex");
            if ("M".equals(sex)){
                sexTv.setText("性别: 男");
            }else if ("W".equals(sex)){
                sexTv.setText("性别: 女");
            }
        }
        if (getIntent().hasExtra("age")){
            age = getIntent().getStringExtra("age");
            ageTv.setText("年龄: "+age);
        }
        if (getIntent().hasExtra("phone")){
            phone = getIntent().getStringExtra("phone");
            phoneTv.setText("手机: "+phone);
        }
        if (getIntent().hasExtra("idCard")){
            idCard = getIntent().getStringExtra("idCard");
            idCardTv.setText("身份证号:"+ idCard);
        }
        if (getIntent().hasExtra("allergy")){
            allergy = getIntent().getStringExtra("allergy");
            allergyTv.setText("过敏史:"+ allergy);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        Map<String,String> map=new HashMap<>();
        map.put("op", "findCasesByPerson");
        map.put("person_id", person_id);
        HttpRestClient.OKHttPersonSeek(map, new HResultCallback<String>(this) {
            @Override
            public void onError(Request request, Exception e) {
            }
            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    list = new ArrayList<>();
                    if ("1".equals(obj.optString("code"))){

                        if (!HStringUtil.isEmpty(obj.optString("result"))){
                            JSONArray array = obj.optJSONArray("result");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonobject = array.getJSONObject(i);
                                list.add(jsonobject);
                            }
                            adapter.onBoundData(list);
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
            case R.id.modify:
                Intent intent = new Intent(this, AddTextActivity.class);
                intent.putExtra(AddTextActivity.TYPE, 2);
                intent.putExtra("person_id", person_id);
                startActivity(intent);
                break;
        }
    }
}
