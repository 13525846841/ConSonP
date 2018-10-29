package com.yksj.consultation.son.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.healthtalk.entity.InsActEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.utils.ToastUtil;

import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class InstitutionDoingActivity extends Activity implements View.OnClickListener {

    private String unit_code;
    private EditText edtActName;
    private EditText edtActTime;
    private EditText edtActIntroduction;
    private FrameLayout tipPro;
    private InsActEntity.ResultBean doing;
    private String activ_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institution_doing);
        initVeiw();
    }

    private void initVeiw() {
        unit_code = getIntent().getStringExtra("Unit_Code");
        activ_code = getIntent().getStringExtra("activ_Code");
        doing = getIntent().getParcelableExtra("doing");
        findViewById(R.id.title_back).setOnClickListener(this);
        TextView title= (TextView) findViewById(R.id.title_lable);
        title.setText("活动添加");
        edtActName = (EditText) findViewById(R.id.edtActName);
        edtActTime = (EditText) findViewById(R.id.edtActTime);
        edtActIntroduction = (EditText) findViewById(R.id.edtActIntroduction);
        Button btnActSubmit= (Button) findViewById(R.id.btnActSubmit);
        btnActSubmit.setOnClickListener(this);
        tipPro = (FrameLayout) findViewById(R.id.tipPro);
        if (doing !=null){
            edtActName.setText(doing.getACTIV_TITLE());
            edtActTime.setText(doing.getACTIV_TIME_DESC());
            edtActIntroduction.setText(doing.getACTIV_DESC());
        }
    }

   private void uploadAct(){

       List<BasicNameValuePair> pairs = new ArrayList<>();
       try {
           if (doing!=null){
               pairs.add(new BasicNameValuePair("activ_Code", activ_code));
               pairs.add(new BasicNameValuePair("op", "updateActive"));
           }else {
               pairs.add(new BasicNameValuePair("op", "createActive"));
           }
           pairs.add(new BasicNameValuePair("time", URLEncoder.encode(edtActTime.getText().toString(),"utf-8")));
           pairs.add(new BasicNameValuePair("Unit_Code", unit_code));
           pairs.add(new BasicNameValuePair("activ_title", URLEncoder.encode(edtActName.getText().toString(),"utf-8")));
           pairs.add(new BasicNameValuePair("activ_desc", URLEncoder.encode(edtActIntroduction.getText().toString(),"utf-8")));
       } catch (UnsupportedEncodingException e) {
           e.printStackTrace();
       }

       HttpRestClient.doGetInstitutionsServlet(pairs, new OkHttpClientManager.ResultCallback<String>() {
           @Override
           public void onError(Request request, Exception e) {
               tipPro.setVisibility(View.GONE);
               if (doing!=null){
                   ToastUtil.onShow(InstitutionDoingActivity.this,"修改失败",1000);
               }else {
                   ToastUtil.onShow(InstitutionDoingActivity.this,"上传失败",1000);
               }

           }

           @Override
           public void onResponse(String response) {
               tipPro.setVisibility(View.GONE);
               Log.i("lll", "onResponse: "+response);
               if (doing!=null){
                   ToastUtil.onShow(InstitutionDoingActivity.this,"修改成功",1000);
               }else {
                   ToastUtil.onShow(InstitutionDoingActivity.this,"上传成功",1000);
               }
               EventBus.getDefault().post(new MyEvent("",12333));
               finish();
           }
       }, this);
   }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:finish();break;
            case R.id.btnActSubmit:
                tipPro.setVisibility(View.VISIBLE);
                uploadAct();
                break;
        }
    }
}
