/**
 *
 */
package com.yksj.consultation.son.consultation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.R.color;
import com.yksj.healthtalk.net.http.ObjectHttpResponseHandler;
import com.yksj.healthtalk.utils.LogUtil;

/**
 * 申请流程Activity
 * @author zheng
 *
 */
public class PConsultApplyActivity extends BaseFragmentActivity implements OnClickListener{
    private Button applyBtn;
    private LinearLayout applyFlow;
    private ImageView gray1,gray2,gray3,gray4,gray5,gray6;
    private ImageView gray11,gray21,gray31,gray41,gray51,gray61;
    private TextView time1,time2,time3,time4,time5,time6,text61,text62,text63,text64,text65,text66;
    private AnimationDrawable animationDrawable;
    private String CUSTOMER_ID;
    private Intent intent;
    private LinearLayout showApply;
    private TextView expense;//费用
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.consultation_apply_activity);
        initView();
    }
    private void initView() {
        initTitle();
        titleTextV.setText("会诊进度");
        titleLeftBtn.setOnClickListener(this);
        showApply=(LinearLayout) findViewById(R.id.show_apply);
        applyFlow=(LinearLayout) findViewById(R.id.apply_flow1);
        applyBtn=(Button) findViewById(R.id.consultation_apply);
        expense = (TextView) findViewById(R.id.expense_flow);//费用
        gray11=(ImageView) findViewById(R.id.gray11);
        gray21=(ImageView) findViewById(R.id.gray21);
        gray31=(ImageView) findViewById(R.id.gray31);
        gray41=(ImageView) findViewById(R.id.gray41);
        gray51=(ImageView) findViewById(R.id.gray51);
        gray61=(ImageView) findViewById(R.id.gray61);
        gray1=(ImageView) findViewById(R.id.gray1);
        gray2=(ImageView) findViewById(R.id.gray2);
        gray3=(ImageView) findViewById(R.id.gray3);
        gray4=(ImageView) findViewById(R.id.gray4);
        gray5=(ImageView) findViewById(R.id.gray5);
        gray6=(ImageView) findViewById(R.id.gray6);
        time1=(TextView) findViewById(R.id.time71);
        time2=(TextView) findViewById(R.id.time72);
        time3=(TextView) findViewById(R.id.time73);
        time4=(TextView) findViewById(R.id.time74);
        time5=(TextView) findViewById(R.id.time75);
        time6=(TextView) findViewById(R.id.time76);
        text61=(TextView) findViewById(R.id.text61);
        text62=(TextView) findViewById(R.id.text62);
        text63=(TextView) findViewById(R.id.text63);
        text64=(TextView) findViewById(R.id.text64);
        text65=(TextView) findViewById(R.id.text65);
        text66=(TextView) findViewById(R.id.text66);
        intent = getIntent();
        CUSTOMER_ID=intent.getStringExtra("CUSTOMER_ID");
        if(intent.getIntExtra("APPLY", 0)!=3){
            initStageData(intent.getIntExtra("conId", 0)+"");
        }else {
            staged(intent.getIntExtra("APPLY", 0));
        }
    }
    private void initStageData(String consultationId){
        HttpRestClient.doHttpfindConsuStatusList(consultationId,new ObjectHttpResponseHandler(PConsultApplyActivity.this) {
            @Override
            public Object onParseResponse(String content) {
                JSONObject obj;
                try {
                    obj = new JSONObject(content);
                    JSONArray array=obj.getJSONArray("findConsuStatusList");
                    return array;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                try {
                    JSONArray array= new JSONArray(response.toString());
                    initStated(array);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void initStated(JSONArray array){
        try {
            JSONObject maxItme=(JSONObject) array.get(array.length()-1);
            String last=maxItme.getString("AFTER_ORDER_STATUS");
            for(int i=0;i<array.length();i++){
                JSONObject object = array.getJSONObject(i);
                int stated = Integer.parseInt(object.optString("AFTER_ORDER_STATUS"));
                JSONObject obj=null;
                switch (stated){
                    case 1:
                        break;
                    case 10://提前申请
                        text61.setTextColor(color.bianse);
//                        text61.setText(object.optString("AFTER_ORDER_STATUS"));
                        gray11.setBackgroundResource(R.drawable.consultation_flow_green);
                        time1.setVisibility(View.VISIBLE);
                        obj=(JSONObject) array.get(i);
                        time1.setText(obj.getString("AFTER_STATUS_TIME"));
                        break;
                    case 20://医生助理接诊
                        text62.setTextColor(color.bianse);
                        gray21.setBackgroundResource(R.drawable.consultation_flow_green);
                        time2.setVisibility(View.VISIBLE);
                        obj=(JSONObject) array.get(i);
                        time2.setText(obj.getString("AFTER_STATUS_TIME"));
                        break;
                    case 50://填写病历
                        text63.setTextColor(color.bianse);
                        gray31.setBackgroundResource(R.drawable.consultation_flow_green);
                        time3.setVisibility(View.VISIBLE);
                        obj=(JSONObject) array.get(i);
                        LogUtil.d("DDD",obj.getString("AFTER_STATUS_TIME"));
                        time3.setText(obj.getString("AFTER_STATUS_TIME"));
                        break;
                    case 70://等待专家接诊
                        text64.setTextColor(color.bianse);
                        gray41.setBackgroundResource(R.drawable.consultation_flow_green);
                        time4.setVisibility(View.VISIBLE);
                        obj=(JSONObject) array.get(i);
                        time4.setText(obj.getString("AFTER_STATUS_TIME"));
                        break;
                    case 80://支付
                        text65.setTextColor(color.bianse);
                        gray51.setBackgroundResource(R.drawable.consultation_flow_green);
                        time5.setVisibility(View.VISIBLE);
                        obj=(JSONObject) array.get(i);
                        time5.setText(obj.getString("AFTER_STATUS_TIME"));
                        break;
                    case 99://专家给出建议
                        text66.setTextColor(color.bianse);
                        gray61.setBackgroundResource(R.drawable.consultation_flow_green);
                        time6.setVisibility(View.VISIBLE);
                        obj=(JSONObject) array.get(i);
                        time6.setText(obj.getString("AFTER_STATUS_TIME"));
                        break;
                }
            }
            staged(Integer.parseInt(last));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void staged(int stage){
        switch (stage) {
            case 3://申请前
                getIntent().getStringExtra("PAYMENT");//会诊价格
                applyBtn.setOnClickListener(this);
                showApply.setVisibility(View.VISIBLE);
                applyFlow.setVisibility(View.GONE);
                expense.setText(getIntent().getStringExtra("PAYMENT"));
                break;
            case 1://提交申请
                gray11.setBackgroundResource(R.drawable.consultation_flow_orange);
                gray1.setVisibility(View.VISIBLE);
                animationDrawable=(AnimationDrawable) gray1.getBackground();
                animationDrawable.start();
                animationDrawable.setOneShot(false);
                break;
            case 10://医生助理接诊
                gray21.setBackgroundResource(R.drawable.consultation_flow_orange);
                gray2.setVisibility(View.VISIBLE);
                animationDrawable=(AnimationDrawable) gray2.getBackground();
                animationDrawable.start();
                animationDrawable.setOneShot(false);
                break;
            case 20://填写病历
                gray31.setBackgroundResource(R.drawable.consultation_flow_orange);
                gray3.setVisibility(View.VISIBLE);
                animationDrawable=(AnimationDrawable) gray3.getBackground();
                animationDrawable.start();
                animationDrawable.setOneShot(false);
                break;
            case 50://等待专家接诊
                gray41.setBackgroundResource(R.drawable.consultation_flow_orange);
                gray4.setVisibility(View.VISIBLE);
                animationDrawable=(AnimationDrawable) gray4.getBackground();
                animationDrawable.start();
                animationDrawable.setOneShot(false);
                break;
            case 70://付款
                gray51.setBackgroundResource(R.drawable.consultation_flow_orange);
                gray5.setVisibility(View.VISIBLE);
                animationDrawable=(AnimationDrawable) gray5.getBackground();
                animationDrawable.start();
                animationDrawable.setOneShot(false);
                break;
            case 80://专家给出意见
                gray61.setBackgroundResource(R.drawable.consultation_flow_orange);
                gray6.setVisibility(View.VISIBLE);
                animationDrawable=(AnimationDrawable) gray6.getBackground();
                animationDrawable.start();
                animationDrawable.setOneShot(false);
                break;
            case 99:
                break;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.consultation_apply:
                Intent intent=new Intent(this,PConsultApplyFlowActivity.class);
                intent.putExtra("CUSTOMER_ID", CUSTOMER_ID);
                intent.putExtra("PAYMENT", getIntent().getStringExtra("PAYMENT"));
                startActivityForResult(intent, 99);
                break;
            case R.id.title_back:
                onBackPressed();
                break;
        }
    }
    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if(arg0==99&&arg1==33){
            int key=arg2.getIntExtra("FINISH", 32);
            switch (key) {
                case 55:
                    finish();
                    break;
            }
        }
    }
}
