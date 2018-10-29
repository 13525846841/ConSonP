package com.yksj.consultation.son.chatting;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dmsj.newask.Views.CircleImageView;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.friend.DoctorClinicMainActivity;
import com.yksj.consultation.son.home.PersonInfoActivity;
import com.yksj.consultation.son.home.PersonInfoEditActivity;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.FriendHttpUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;

/**
 * 聊天UI
 *
 * @author HEKL
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class AtyChatMembers extends BaseFragmentActivity implements View.OnClickListener, View.OnLongClickListener {
    String consultationId = "";
    private CircleImageView mExpert, mDoctor, mPatient;
    private TextView mTextExpert, mTextDoctor, mTextPatient;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private boolean isDoctor = SmartFoxClient.getLoginUserInfo().isDoctor();// 是否为医生
    private String expertId, doctorId, patientId;
    private CustomerInfoEntity entityE, entityD, entityP;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.chat_member_layout);
        initView();
    }

    private void initView() {
        entityE = new CustomerInfoEntity();
        entityD = new CustomerInfoEntity();
        entityP = new CustomerInfoEntity();
        consultationId = getIntent().getStringExtra("consultationId");
        initTitle();
        titleLeftBtn.setOnClickListener(this);
        titleTextV.setText("成员");
        mImageLoader = ImageLoader.getInstance();
        mOptions = DefaultConfigurationFactory.createHeadDisplayImageOptions(this);
        mExpert = (CircleImageView) findViewById(R.id.iv_expert);
        mDoctor = (CircleImageView) findViewById(R.id.iv_doctor);
        mPatient = (CircleImageView) findViewById(R.id.iv_patient);
        mTextExpert = (TextView) findViewById(R.id.tv_expert);
        mTextDoctor = (TextView) findViewById(R.id.tv_doctor);
        mTextPatient = (TextView) findViewById(R.id.tv_patient);
        initData();
        mExpert.setOnClickListener(this);
        mDoctor.setOnClickListener(this);
        mPatient.setOnClickListener(this);
        mExpert.setOnLongClickListener(this);
        mDoctor.setOnLongClickListener(this);
        mPatient.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.iv_expert:
                if (doctorId != SmartFoxClient.getLoginUserInfo().getId()) {
                    intent = new Intent(this, DoctorClinicMainActivity.class);
                    intent.putExtra("id", entityE.getId());
                } else {
//                    intent = new Intent(this, DoctorRegisteUI.class);
                }
                startActivity(intent);
                break;
            case R.id.iv_doctor:
                if (doctorId != SmartFoxClient.getLoginUserInfo().getId()){
                    intent = new Intent(this, DoctorClinicMainActivity.class);
                    intent.putExtra("id", entityD.getId());
                } else {
//                    intent = new Intent(this, DoctorRegisteUI.class);
                }
                startActivity(intent);
                break;
            case R.id.iv_patient:
                if (doctorId != SmartFoxClient.getLoginUserInfo().getId()) {
                    intent = new Intent(this,PersonInfoActivity.class);
                    intent.putExtra("id", entityP.getId());
                } else {
                    intent = new Intent(this, PersonInfoEditActivity.class);
                }
                startActivity(intent);
                break;
        }
    }

    private void initData() {
        HttpRestClient.doHttpCaseMembers(consultationId, new AsyncHttpResponseHandler(this) {
            JSONObject object;

            @Override
            public void onSuccess(String content) {
                try {
                    object = new JSONObject(content);
                    if (object.has("errormessage")) {
                        ToastUtil.showShort(object.optString("errormessage"));
                    } else {
                        JSONArray array = object.getJSONArray("EXPERTINFO");
                        doctorId = object.optString("DOCTORID");
                        patientId = object.optString("PATIENTID");
                        expertId = array.getJSONObject(0).optString("EXPERTID");
                        mTextExpert.setText(array.getJSONObject(0).optString("EXPERTNAME"));
                        mTextDoctor.setText(object.optString("DOCTORNAME"));
                        mTextPatient.setText(object.optString("PATIENTNAME"));
                        mImageLoader.displayImage(object.optString("DOCTORICON"), mDoctor, mOptions);
                        mImageLoader.displayImage(object.optString("PATIENTICON"), mPatient, mOptions);
                        mImageLoader.displayImage(array.getJSONObject(0).optString("EXPERTICON"), mExpert, mOptions);
                        entityE.setId(array.getJSONObject(0).optString("EXPERTID"));
                        entityE.setName(array.getJSONObject(0).optString("EXPERTNAME"));
                        entityE.setDoctorClientPicture(array.getJSONObject(0).optString("EXPERTICON"));
                        entityD.setId(object.optString("DOCTORID"));
                        entityD.setName(object.optString("DOCTORNAME"));
                        entityD.setDoctorClientPicture(object.optString("DOCTORICON"));
                        entityP.setId(object.optString("PATIENTID"));
                        entityP.setName(object.optString("PATIENTNAME"));
                        entityP.setDoctorClientPicture(object.optString("PATIENTICON"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onSuccess(content);
            }
        });
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.iv_expert:
                if (expertId != SmartFoxClient.getLoginUserInfo().getId()) {
                    FriendHttpUtil.chatFromPerson(this, entityE);
                } else {
                    return false;
                }
                break;
            case R.id.iv_doctor:
                if (doctorId != SmartFoxClient.getLoginUserInfo().getId()) {
                    FriendHttpUtil.chatFromPerson(this, entityD);
                } else {
                    return false;
                }
                break;
            case R.id.iv_patient:
                if (patientId != SmartFoxClient.getLoginUserInfo().getId()) {
                    FriendHttpUtil.chatFromPerson(this, entityP);
                } else {
                    return false;
                }
                break;
        }
        return true;
    }
}
