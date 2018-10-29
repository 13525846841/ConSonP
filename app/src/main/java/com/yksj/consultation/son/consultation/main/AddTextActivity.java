package com.yksj.consultation.son.consultation.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddTextActivity extends BaseFragmentActivity implements View.OnClickListener {
    public static final String TYPE = "type";
    public static final String CONTENTS = "contents";
    public int type = 0;//1,输入过敏史  2.就诊人详情内容
    public EditText mET;
    public TextView textNum;
    private int textNumber = 0;
    private String personId;
    private TextView save;
    private String contents = "";//过敏史

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_text);

        initView();
    }

    private void initView() {
        initTitle();
        titleLeftBtn.setOnClickListener(this);
        save = (TextView) findViewById(R.id.modify);

        mET = (EditText) findViewById(R.id.et_text);
        if (getIntent().hasExtra(TYPE))
            type = getIntent().getIntExtra(TYPE, 0);
        if (getIntent().hasExtra("person_id"))
            personId = getIntent().getStringExtra("person_id");
        if (getIntent().hasExtra(CONTENTS))
            contents = getIntent().getStringExtra(CONTENTS);
        save.setOnClickListener(this);
        switch (type) {
            case 1:
                titleRightBtn2.setVisibility(View.VISIBLE);
                titleTextV.setText("输入过敏史");
                mET.setHint("请输入内容");
                titleRightBtn2.setVisibility(View.VISIBLE);
                titleRightBtn2.setOnClickListener(this);
                titleRightBtn2.setText("确定");
//                textNum = (TextView)findViewById(R.id.textcount);
//                textNum.setVisibility(View.VISIBLE);
//                textNum.setText("0/20");
//                mET.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        if((start +count)<=20 && mET.getText().toString().length()<=20){
//                            textNumber = mET.getText().toString().length();
//                            textNum.setText(textNumber+"/20");
//                        }else{
//                            mET.setText(s.subSequence(0, 20));
//                            ToastUtil.showShort(AddTextActivity.this, "最多可输入20个字");
//                        }
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//
//                    }
//                });

                break;
            case 2:
                titleRightBtn2.setVisibility(View.VISIBLE);
                titleTextV.setText("就诊人详情");
                mET.setHint("请输入内容");
                save.setVisibility(View.VISIBLE);

                break;

        }
        if (!HStringUtil.isEmpty(contents)) {
            mET.setText(contents);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.modify:
                String content = mET.getText().toString();
                PersonSeekDetail(content);
                break;
            case R.id.title_right2://确定
                switch (type) {
                    case 1:
                        String remingContent = mET.getText().toString();
                        Intent intent = new Intent();
                        intent.putExtra("content", remingContent);
                        setResult(RESULT_OK, intent);
                        finish();
//                        if (remingContent.length()>20){
//                            ToastUtil.showShort("内容不能多于20个字");
//                        }else{
//                            Intent intent = new Intent();
//                            intent.putExtra("content", remingContent);
//                            setResult(RESULT_OK, intent);
//                            finish();
//                        }
                        break;
                }
                break;
        }
    }

    /**
     * 就诊人详情内容填写
     */
    private void PersonSeekDetail(String content) {
        if (TextUtils.isEmpty(content)) {
            ToastUtil.showToastPanl("请填写内容");
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("op", "addCase");
        map.put("person_id", personId);
        map.put("disease_Desc", content);
        HttpRestClient.OKHttPersonSeek(map, new HResultCallback<String>(this) {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if ("1".equals(obj.optString("code"))) {
                        ToastUtil.showShort(obj.optString("message"));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this);
    }
}
