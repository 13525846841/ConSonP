package com.yksj.consultation.son.consultation.main;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 图文    评价
 */
public class AppraiseTuAty extends BaseFragmentActivity implements View.OnClickListener {
    public static final String ISCOMMENT = "iscomment";
    public static final String SITE_ID = "site_id";
    public static final String EVALUATE_ID = "evaluate_id";
    private String costumerId = SmartFoxClient.getLoginUserId();
    private String orderId, site_id;
    private boolean isComment;//1，已评价，2 未评价
    private String content;//提醒内容
    private String evaluate_id = "";//评论id
    public EditText mET;
    public RatingBar ratingBar;
    public TextView name;
    public TextView header;//职称

    private String number = "5.0";
    private String doctorId;
    private int textNumber = 0;
    public TextView textNum;
    private String image;
    private ImageView imageView;

    private LinearLayout linearLayout;//医生回复
    public TextView comments;//医生回复

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appraise_tu_aty);

        initView();
    }

    private void initView() {
        initTitle();
        titleTextV.setText("评价");
        titleLeftBtn.setOnClickListener(this);
        mET = (EditText) findViewById(R.id.et_evaluate);
        ratingBar = (RatingBar) findViewById(R.id.rb_speed);
        imageView = (ImageView) findViewById(R.id.assistant_head);
        isComment = getIntent().getBooleanExtra(ISCOMMENT, true);
        image = getIntent().getStringExtra("IMAGE");

        orderId = getIntent().getStringExtra("ORDER_ID");
        site_id = getIntent().getStringExtra(SITE_ID);
        evaluate_id = getIntent().getStringExtra(EVALUATE_ID);
        name = (TextView) findViewById(R.id.tv_assisName);
        header = (TextView) findViewById(R.id.tv_type);
        comments = (TextView) findViewById(R.id.assess_text);
        linearLayout = (LinearLayout) findViewById(R.id.ll_comments);
        textNum = (TextView) findViewById(R.id.textcount);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                number = String.valueOf(rating);
            }
        });
        if (getIntent().hasExtra("NAME")) ;
        name.setText(getIntent().getStringExtra("NAME"));
        if (getIntent().hasExtra("TITLE")) ;
        header.setText(getIntent().getStringExtra("TITLE"));
        if (getIntent().hasExtra("DOCTORID")) ;
        doctorId = getIntent().getStringExtra("DOCTORID");
        String url = HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW + image;
        Picasso.with(this).load(url).placeholder(R.drawable.default_head_doctor).into(imageView);


        mET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((start + count) <= 200 && mET.getText().toString().length() <= 200) {
                    textNumber = mET.getText().toString().length();
                    textNum.setText(textNumber + "/200");
                } else {
                    mET.setText(s.subSequence(0, 200));
                    ToastUtil.showShort(AppraiseTuAty.this, "最多可输入200个字");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mET.setHint("请输入内容(内容小于200字)");
        titleRightBtn2.setOnClickListener(this);
        titleRightBtn2.setVisibility(View.VISIBLE);
        if (!isComment) {
            titleRightBtn2.setText("确定");
        } else {
            titleRightBtn2.setText("删除");
            initCommentContent();
        }
    }

    /**
     * 评论后查看评论
     * {
     * "message": "查询成功",
     * "result": {
     * "comment_info": "呃呃",
     * "Evaluate_Staturs": 1,
     * "evaluate_level": 5,
     * "Reply_Content": "也一样"
     * },
     * "code": 0
     * }
     */
    private void initCommentContent() {
        Map<String, String> map = new HashMap<>();
        map.put("op", "findCommentByOrder");
        map.put("order_id", orderId);//229586
//        map.put("order_id", "18223");//229586
        HttpRestClient.OKHttpAppraiseList(map, new HResultCallback<String>(this) {

            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))) {
                        JSONObject jsonobject = obj.optJSONObject("result");
                        mET.setText(jsonobject.optString("comment_info"));
                        if (jsonobject.has("evaluate_level")) {
                            ratingBar.setRating(Float.valueOf(jsonobject.optString("evaluate_level")));
                        } else {
                            ratingBar.setRating(5.0f);
                        }
                        if (jsonobject.has("evaluate_id")) {
                            evaluate_id = jsonobject.optString("evaluate_id");
                        }
                        if (!HStringUtil.isEmpty(jsonobject.optString("Reply_Content"))) {
                            linearLayout.setVisibility(View.VISIBLE);
                            comments.setText(jsonobject.optString("Reply_Content"));
                        }
                    } else {
                        ToastUtil.showShort(obj.optString("message"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right2:

                if (!isComment) {
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
                } else {
                    DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "确定要删除这条评论吗？", "取消", "确定",
                            new DoubleBtnFragmentDialog.OnDilaogClickListener() {

                                @Override
                                public void onDismiss(DialogFragment fragment) {
                                }

                                @Override
                                public void onClick(DialogFragment fragment, View v) {
                                    sendDelteData();
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
        content = mET.getText().toString().trim();
//        if (TextUtils.isEmpty(content)) {
//            ToastUtil.showToastPanl("请填写评级内容");
//            return;
//        }

        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        try {
            object.put("customer_id", costumerId);
            object.put("doctor_id", doctorId);
            object.put("evaluate_level", number);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        array.put(object);

        Map<String, String> map = new HashMap<>();
        map.put("op", "consultationEvaluate");
        map.put("content", content);
        map.put("consultation_id", orderId);
        if (!HStringUtil.isEmpty(site_id)) {
            map.put("type", "site_" + site_id);//工作站
        } else {
            map.put("type", "tuwen");//评价类型
        }
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
        }, this);
    }

    /**
     * 删除评价
     */
    private void sendDelteData() {
        if (HStringUtil.isEmpty(evaluate_id))
            return;
        Map<String, String> map = new HashMap<>();
        map.put("op", "deleteComment");
        map.put("evaluate_id", evaluate_id);
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
        }, this);
    }
}
