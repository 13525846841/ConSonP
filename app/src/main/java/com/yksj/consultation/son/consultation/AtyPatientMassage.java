package com.yksj.consultation.son.consultation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.PatientConsuListAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.ZoomImgeDialogFragment;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.consultationorders.AtyOrderRecord;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.MyResultCallback;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 患者个人信息
 * Created by zheng on 2015/9/17.
 */
public class AtyPatientMassage extends BaseFragmentActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ImageView patientHeader, imgDutyIndex,imgDutyIndex2;
    private TextView nameEdit, ageEdit, sexEdit, phoneEdit, duoMeiHao, duty;
    private TextView allergy;//过敏史
    private CustomerInfoEntity mLoginUserInfo;
    private List<JSONObject> jsonLst;
    private PatientConsuListAdapter mAdapter;
    private ListView mListView;
    private boolean dutyExpanded = false;
    private boolean dutyExpanded2 = false;//过敏史
    private String shuoming;
    private String guomin;
    private JSONObject object;
    private ScrollView topSv;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_patient_massage);
        EventBus.getDefault().register(this);//注册
        initView();
    }

    private void initView() {
        initTitle();
        titleTextV.setText("我的信息");
        titleLeftBtn.setOnClickListener(this);
        if (!getIntent().hasExtra("ORDER")) {
            titleRightBtn2.setVisibility(View.VISIBLE);
            titleRightBtn2.setText("编辑");
            titleRightBtn2.setOnClickListener(this);
        }
        mLoginUserInfo = LoginServiceManeger.instance().getLoginEntity();
        patientHeader = ((ImageView) findViewById(R.id.patient_header));
        patientHeader.setOnClickListener(this);
        topSv = (ScrollView)findViewById(R.id.scrollview_top);
        topSv.fullScroll(ScrollView.FOCUS_UP);
        mListView = (ListView) findViewById(R.id.case_list);
        duoMeiHao = (TextView) findViewById(R.id.duomei_hao);
        nameEdit = ((TextView) findViewById(R.id.name2_edit));
        ageEdit = ((TextView) findViewById(R.id.age2_edit));
        sexEdit = ((TextView) findViewById(R.id.sex2_edit));
        phoneEdit = ((TextView) findViewById(R.id.phone2_edit));
        duoMeiHao.setText(mLoginUserInfo.getUsername());
        duty = (TextView) findViewById(R.id.clinic_specialty_content);
        allergy = (TextView) findViewById(R.id.clinic_specialty_content2);

        imgDutyIndex = (ImageView) findViewById(R.id.clinic_specialty_index);
        imgDutyIndex2 = (ImageView) findViewById(R.id.clinic_specialty_index2);
        initDate();
    }

    //获取资料
    private void initDate() {
        mAdapter = new PatientConsuListAdapter(this);
        mListView.setOnItemClickListener(this);
        mListView.setAdapter(mAdapter);
        onBandData();
    }

    //绑定资料
    private void onBandData() {
        HttpRestClient.OKHttpFindCustomerInfo(new MyResultCallback<JSONObject>(this) {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(JSONObject response) {
                super.onResponse(response);
                if (response != null) {
                    if ("1".equals(response.optString("code"))) {
                        object = response.optJSONObject("result");
                        ImageLoader instance = ImageLoader.getInstance();
                        instance.displayImage(object.optString("customerSex"), object.optString("clientIconBackground"), patientHeader);

                        nameEdit.setText(object.optString("realName"));
                        if ("M".equals(object.optString("customerSex"))) {
                            sexEdit.setText("男");
                        } else if ("W".equals(object.optString("customerSex"))) {
                            sexEdit.setText("女");
                        } else {
                            sexEdit.setText("");
                        }
                        ageEdit.setText(object.optString("age"));
                        phoneEdit.setText(object.optString("phone"));
                        shuoming = object.optString("diseaseDesc");
                        guomin = object.optString("allergy");
                        lineCount();
                        JSONArray array = object.optJSONArray("listconsultationRecord");
                        jsonLst = new ArrayList<JSONObject>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject item = array.optJSONObject(i);
                            jsonLst.add(item);
                        }
                        mAdapter.removeAll();
                        mAdapter.addAll(jsonLst);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                topSv.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }

                }
            }
        }, this);
    }

    private void lineCount() {
        if (!TextUtils.isEmpty(shuoming)) {
            StringBuilder b = new StringBuilder();
            b.append(shuoming);
            duty.setText(b);
            if (duty.getLineCount() < 2)//行数小于2,将展开按钮隐藏
                findViewById(R.id.clinic_specialty_index).setVisibility(View.INVISIBLE);
            else
                findViewById(R.id.clinic_layout_item1).setOnClickListener(this);
        }else {
            findViewById(R.id.clinic_specialty_index).setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(guomin)) {
            StringBuilder c = new StringBuilder();
            c.append(guomin);
            allergy.setText(c);
            if (allergy.getLineCount() < 2)//行数小于2,将展开按钮隐藏
                findViewById(R.id.clinic_specialty_index2).setVisibility(View.INVISIBLE);
            else
                findViewById(R.id.clinic_layout_item2).setOnClickListener(this);
        }else {
            findViewById(R.id.clinic_specialty_index2).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right2:
                Intent intent = new Intent(AtyPatientMassage.this, AtyEditorPatientMessage.class);
//              Intent intent = new Intent(AtyPatientMassage.this,PersonInfoEditActivity.class);
                startActivity(intent);
                break;
            case R.id.clinic_layout_item1://职务文本收缩展开
                if (dutyExpanded) {
                    dutyExpanded = false;
                    duty.setMaxLines(2);
                    imgDutyIndex.setImageResource(R.drawable.gengduos);
                } else {
                    dutyExpanded = true;
                    duty.setMaxLines(100);
                    imgDutyIndex.setImageResource(R.drawable.shouqis);
                }
                break;
            case R.id.clinic_layout_item2://过敏史收缩展开
                if (dutyExpanded2) {
                    dutyExpanded2 = false;
                    allergy.setMaxLines(2);
                    imgDutyIndex2.setImageResource(R.drawable.gengduos);
                } else {
                    dutyExpanded2 = true;
                    allergy.setMaxLines(100);
                    imgDutyIndex2.setImageResource(R.drawable.shouqis);
                }
                break;
            case R.id.patient_header:
                ZoomImgeDialogFragment.show(object.optString("bigIconBackground"),
                        getSupportFragmentManager());
                break;
        }
    }

    public void onEventMainThread(JSONObject event) {
        if ("1".equals(event.optString("code"))) {
//            ToastUtil.showToastPanl("谈谈谈");
            onBandData();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.post(new Runnable() {
            @Override
            public void run() {
                topSv.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(AtyPatientMassage.this, AtyOrderRecord.class);
        JSONObject object = mAdapter.datas.get(i);
        intent.putExtra("CONID", object.optInt("CONSULTATION_ID"));
        intent.putExtra("PERSONID", LoginServiceManeger.instance().getLoginEntity().getId());
        startActivity(intent);
    }
}
