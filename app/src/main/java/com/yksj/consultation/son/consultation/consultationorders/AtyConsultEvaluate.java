package com.yksj.consultation.son.consultation.consultationorders;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog.OnDilaogClickListener;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;

/**
 * @author HEKL
 *         <p/>
 *         评价界面_
 */
public class AtyConsultEvaluate extends BaseFragmentActivity implements OnClickListener {
    private EditText mEditText;//填写评价
    private RatingBar mBar;// 评价星级
    private int conId;//会诊id

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_consult_evaluate);
        initView();
    }

    public void initView() {
        conId = getIntent().getIntExtra("conId", 0);
        String doctorName = getIntent().getStringExtra("DoctorName");
        String doctorIcon = getIntent().getStringExtra("DoctorIcon");
        String doctorTitle = getIntent().getStringExtra("DoctorTitle");
        String doctorHospital = getIntent().getStringExtra("DoctorHospital");
        initTitle();
        titleTextV.setText("评价");
        titleLeftBtn.setOnClickListener(this);
        titleRightBtn2.setVisibility(View.VISIBLE);
        titleRightBtn2.setText("确定");
        titleRightBtn2.setOnClickListener(this);
        ImageLoader mImageLoader = ImageLoader.getInstance();
        DisplayImageOptions mOptions = DefaultConfigurationFactory.createHeadDisplayImageOptions(this);
        TextView mTextHospital = (TextView) findViewById(R.id.tv_hospital);
        TextView mTextName = (TextView) findViewById(R.id.tv_assisName);
        TextView mTextType = (TextView) findViewById(R.id.tv_type);
        mEditText = (EditText) findViewById(R.id.et_evaluate);
        ImageView mHeadView = (ImageView) findViewById(R.id.assistant_head);
        mBar = (RatingBar) findViewById(R.id.rb_speed);
        mBar.setStepSize(1f);
        mTextName.setText(doctorName);
        mTextType.setText(doctorTitle);
        mTextHospital.setText(doctorHospital);
        mImageLoader.displayImage(doctorIcon, mHeadView, mOptions);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() >= 1000) {
                    ToastUtil.showShort("您输入的字数已经超过了1000限制！");
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right2:
                if ((int) mBar.getRating() == 0) {
                    ToastUtil.showShort("你还没有评价，不要偷懒哦，亲！");
                } else {
                    DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "感谢您的评价，确定要提交吗？", "取消", "确定",
                            new OnDilaogClickListener() {

                                @Override
                                public void onDismiss(DialogFragment fragment) {
                                }

                                @Override
                                public void onClick(DialogFragment fragment, View v) {
                                    sendData();
                                }
                            });
                }

                break;

        }
    }

    /**
     * 发送评价
     */
    private void sendData() {
        String str = mEditText.getText().toString();
        RequestParams params = new RequestParams();
        params.put("OPTION", "11");
        params.put("CONSULTATIONID", conId + "");
        params.put("SERVICELEVEL", (int) mBar.getRating() + "");// 星级
        params.put("CONTENT", str);
        HttpRestClient.doHttpDoctorService(params, new AsyncHttpResponseHandler(this) {
            JSONObject obj = null;

            @Override
            public void onSuccess(int statusCode, String content) {
                try {
                    obj = new JSONObject(content);
                    ToastUtil.showShort(obj.getString("message"));
                    if ((obj.optInt("code") == 1)) {
                        EventBus.getDefault().post(new MyEvent("refresh", 2));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onSuccess(statusCode, content);
            }
        });
    }
}
