package com.yksj.consultation.son.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.PAtyConsultStudioGoPaying;
import com.yksj.consultation.son.consultation.bean.ServiceType;
import com.yksj.healthtalk.entity.HealthLecturePayEntity;
import com.yksj.healthtalk.entity.HealthLectureWorksEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.ToastUtil;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class HealthLecturePayInfoActivity extends Activity implements View.OnClickListener {
    private String course_id,tuwenTime;
    private TextView tvTitle;
    private TextView docName;
    private TextView wenZJS;
    private TextView price;
    private TextView joinNum;
    private TextView starNum;
    private TextView evaluationNum;
    private RatingBar ratingBar;
    private HealthLectureWorksEntity.ResultBean info;
    private TextView pay;
    private HealthLecturePayEntity.ResultBean result;
    private TextView tipTv;
    private FrameLayout payLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_lecture_pay_info);
        initView();
    }

    private void initView() {
        course_id = getIntent().getStringExtra("course_ID");
        tuwenTime = getIntent().getStringExtra("tuwenTime");
        info = getIntent().getParcelableExtra("info");
        findViewById(R.id.title_back).setOnClickListener(this);
        ImageView share = (ImageView) findViewById(R.id.share);
        share.setOnClickListener(this);
        payLoad = (FrameLayout) findViewById(R.id.payLoad);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        docName = (TextView) findViewById(R.id.docName);
        wenZJS = (TextView) findViewById(R.id.wenZJS);
        price = (TextView) findViewById(R.id.price);
        joinNum = (TextView) findViewById(R.id.joinNum);
        starNum = (TextView) findViewById(R.id.starNum);
        evaluationNum = (TextView) findViewById(R.id.EvaluationNum);
        pay = (TextView) findViewById(R.id.pay);
        pay.setOnClickListener(this);
        FrameLayout goDocInfo = (FrameLayout) findViewById(R.id.goDocInfo);
        goDocInfo.setOnClickListener(this);
        LinearLayout goEvaluation = (LinearLayout) findViewById(R.id.goEvaluation);
        LinearLayout healthPingJia = (LinearLayout) findViewById(R.id.healthPingJia);
        healthPingJia.setOnClickListener(this);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        tipTv = (TextView) findViewById(R.id.tipTv);
        tipTv.setOnClickListener(this);
        tipTv.setEnabled(false);
        loadData();
    }

    private void loadData() {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("op", "queryBuyPageCourseInfo"));
        pairs.add(new BasicNameValuePair("course_ID", course_id));
//        pairs.add(new BasicNameValuePair("customer_id", "124783"));
        pairs.add(new BasicNameValuePair("customer_id", LoginServiceManeger.instance().getLoginUserId()));
        HttpRestClient.doGetPersonClassroom(pairs, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {
                tipTv.setText("加载失败，点击重试");
                tipTv.setEnabled(true);
            }


            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                HealthLecturePayEntity healthLecturePayEntity = gson.fromJson(response, HealthLecturePayEntity.class);
                result = healthLecturePayEntity.getResult();
                if (result.getCOURSE_NAME().length()>10){
                    String title=result.getCOURSE_NAME().substring(0,15);
                    tvTitle.setText(title+"...");
                }else
                tvTitle.setText(result.getCOURSE_NAME());
                docName.setText(result.getCOURSE_UP_NAME());
                wenZJS.setText(result.getCOURSE_DESC());
                price.setText("￥"+ result.getCOURSE_OUT_PRICE());
                joinNum.setText(result.getBuyerNum()+"人参与");
                starNum.setText(result.getAvgStar()+"");
                evaluationNum.setText(result.getEvaNum()+"条评价");
                ratingBar.setRating(result.getAvgStar());
                if (result.getPay_status()==20||result.getCOURSE_PAY().equals("0")) {
                    pay.setText("查看");
                } else {
                    pay.setText("付款");
                }
                tipTv.setVisibility(View.GONE);
                payLoad.setVisibility(View.GONE);
            }
        }, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.share:
                break;
            case R.id.tipTv:
                tipTv.setText("加载中...");
                tipTv.setEnabled(false);
                loadData();
                break;
            case R.id.goDocInfo:
                Intent intent = new Intent(this, DoctorInfoActivity.class);
                intent.putExtra("customer_id", Integer.valueOf(result.getCOURSE_UP_ID()));
                startActivity(intent);
                break;
            case R.id.healthPingJia:
                Intent intent1 = new Intent(this, EvaluationActivity.class);
//                intent = new Intent(this, SiteCommentListActivity.class);
                intent1.putExtra("type","heath");
                intent1.putExtra("course_ID",course_id);
                startActivity(intent1);
                break;
            case R.id.pay:
                if (result.getPay_status()==20||result.getCOURSE_PAY().equals("0")) {
                    if (result.getCOURSE_CLASS().equals("20")){
                    Intent intent0 = new Intent(this, HealthLectureTuwenActivity.class);
//                        intent0.putExtra("course_ID", course_id);
                        intent0.putExtra("tuwenTime",tuwenTime);
                        intent0.putExtra("pic",result.getSMALL_PIC());
                        intent0.putExtra("title",result.getCOURSE_NAME());
                        intent0.putExtra("content",result.getCOURSE_DESC());
                        startActivity(intent0);
                    }else if (result.getCOURSE_CLASS().equals("30")){
                        Intent intent0 = new Intent(this, HealthLectureHomeActivity.class);
                        intent0.putExtra("course_ID", course_id);
                        intent0.putExtra("course_up_ID", result.getCOURSE_UP_ID());
                        intent0.putExtra("info", info);
                        startActivity(intent0);
                    }
                    finish();
                } else {
                    Intent intent2 = new Intent(this, PAtyConsultStudioGoPaying.class);
                    intent2.putExtra("service_id", ServiceType.DL);
                    intent2.putExtra("course_id", course_id);
                    intent2.putExtra("price", result.getCOURSE_OUT_PRICE()+"");//价格
                    startActivityForResult(intent2,1000);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1000&&resultCode==9000&&data!=null){
            if (data.getStringExtra("alipayStatus").equals("9000")) {
                payLoad.setVisibility(View.VISIBLE);
                loadData();
            }
        }
    }
}
