package com.yksj.consultation.son.consultation.consultationorders;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.ImageGalleryActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.MyResultCallback;
import com.yksj.healthtalk.utils.TimeUtil;

import org.apache.http.message.BasicNameValuePair;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshScrollView;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by HEKL on 15/10/13.
 * Used for 会诊意见_
 */
public class AtyConsultOpinion extends BaseFragmentActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener<ScrollView> {
    private PullToRefreshScrollView mPullToRefreshScrollView;//整体滑动布局
    private ArrayList<HashMap<String, String>> list = null;//储存图片
    private DisplayImageOptions mOptions;//画廊异步读取操作
    private HorizontalScrollView mView2;//图片横滑布局
    private LayoutInflater mInflater;//图片布局
    private ImageLoader mImageLoader;//异步加载图片
    private LinearLayout llQuestion;//提问和专家解答布局
    private LinearLayout mGallery;//图片画廊
    private String[] array = null;//病历图片
    private TextView textOpinion;//会诊意见
    private int supplyAdd;//专家给出意见是否在N天之内,1:N天内,0:N天外
    private int questionFlag;//患者是否可以有提出疑问,0可以,1不可以
    private int answerFlag;//专家是否给出解答,0没有
    private int conId;//会诊id
    private int isRecord;
    public static final int CAMERA_REQUESTCODE = 3;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_consult_opinion);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        initTitle();
        conId = getIntent().getIntExtra("conId", 0);
        if (getIntent().hasExtra("record")) {
            isRecord = 1;
        }
        titleTextV.setText("会诊意见");
        textOpinion = (TextView) findViewById(R.id.tv_opinion);
        llQuestion = (LinearLayout) findViewById(R.id.ll_question);
        mPullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
        mPullToRefreshScrollView.setOnRefreshListener(this);
        titleLeftBtn.setOnClickListener(this);
        mImageLoader = ImageLoader.getInstance();
        mOptions = DefaultConfigurationFactory.createApplyPicDisplayImageOptions(this);
        mInflater = LayoutInflater.from(this);
        loadOpinion();
    }

    public void onEvent(MyEvent event) {
        if ("opinion".equals(event.what)) {
            loadOpinion();
        }
    }


    private void loadOpinion() {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("CONSULTATIONID", conId + ""));
        pairs.add(new BasicNameValuePair("OPTION", "9"));
        HttpRestClient.OKHttpgetOpinion(pairs, new MyResultCallback<String>(this) {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onAfter() {
                super.onAfter();
                mPullToRefreshScrollView.onRefreshComplete();
            }

            @Override
            public void onResponse(String response) {

                if (!TextUtils.isEmpty(response)) {
                    try {
                        JSONObject object = new JSONObject(response);
                        View view = getLayoutInflater().inflate(R.layout.view_question, null);
                        View viewSec = getLayoutInflater().inflate(R.layout.view_question, null);
                        mView2 = (HorizontalScrollView) view.findViewById(R.id.hs_gallery);
                        TextView textFrom = (TextView) view.findViewById(R.id.tv_fromwhere);
                        TextView textTime = (TextView) view.findViewById(R.id.tv_time);
                        TextView textQuestion = (TextView) view.findViewById(R.id.tv_question);
                        TextView textFrom2 = (TextView) viewSec.findViewById(R.id.tv_fromwhere);
                        TextView textTime2 = (TextView) viewSec.findViewById(R.id.tv_time);
                        TextView textQuestion2 = (TextView) viewSec.findViewById(R.id.tv_question);
                        String time = TimeUtil.formatTime(object.getJSONObject("result").optString("QUESTIONTIME"));
                        String time2 = TimeUtil.formatTime(object.getJSONObject("result").optString("ANSWERTIME"));

                        mGallery = (LinearLayout) view.findViewById(R.id.ll_gallery);
                        mGallery.setOnClickListener(AtyConsultOpinion.this);
                        list = new ArrayList<>();
                        if (object.optInt("code") == 1) {
                            textOpinion.setText(object.getJSONObject("result").optString("CONTENT"));
                            questionFlag = object.getJSONObject("result").optInt("QUESTIONFLAG");
                            answerFlag = object.getJSONObject("result").optInt("ANSWERFLAG");
                            findViewById(R.id.btn_question).setVisibility(View.GONE);
                            findViewById(R.id.tv_tip).setVisibility(View.GONE);
                            llQuestion.setVisibility(View.GONE);
                            findViewById(R.id.view1).setVisibility(View.GONE);
                            findViewById(R.id.tv_tip2).setVisibility(View.GONE);
                            llQuestion.removeAllViews();
                            supplyAdd = object.getJSONObject("result").optInt("SUPPLYADD");
                            if (supplyAdd == 1) {
                                if (isRecord == 0) {
                                    if (questionFlag == 0) {
                                        findViewById(R.id.btn_question).setOnClickListener(AtyConsultOpinion.this);
                                        findViewById(R.id.btn_question).setVisibility(View.VISIBLE);
                                        findViewById(R.id.tv_tip).setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                            if (questionFlag == 1) {
                                llQuestion.setVisibility(View.VISIBLE);
                                findViewById(R.id.tv_tip2).setVisibility(View.VISIBLE);
                                findViewById(R.id.view1).setVisibility(View.VISIBLE);
                                textFrom.setText("我的疑问:");
                                textQuestion.setText(object.getJSONObject("result").optString("QUESTION"));
                                textTime.setText(time);
                                llQuestion.addView(view);
                                if (answerFlag == 1) {
                                    findViewById(R.id.tv_tip2).setVisibility(View.GONE);
                                    textFrom2.setText("专家解答:");
                                    textQuestion2.setText(object.getJSONObject("result").optString("ANSWER"));
                                    textTime2.setText(time2);
                                    llQuestion.addView(viewSec);
                                }
                                if (object.getJSONObject("result").getJSONArray("QUESTIONFILE").length() != 0) {
                                    int count = object.getJSONObject("result").getJSONArray("QUESTIONFILE").length();//图片数量
                                    //图片的适配
                                    mView2.setVisibility(View.VISIBLE);
                                    for (int t = 0; t < count; t++) {
                                        JSONObject ob = object.getJSONObject("result").getJSONArray("QUESTIONFILE").getJSONObject(t);
                                        HashMap<String, String> map = new HashMap<String, String>();
                                        map.put("ID", "" + ob.optInt("PIC_ID"));
                                        map.put("SMALL", ob.optString("SMALL_PICTURE"));
                                        map.put("BIG", ob.optString("BIG_PICTURE"));
                                        map.put("SEQ", "" + ob.optInt("PICTURE_SEQ"));
                                        list.add(map);
                                    }
                                    array = new String[count];
                                    for (int t = 0; t < count; t++) {
                                        array[t] = list.get(t).get("BIG");
                                    }
                                    mGallery.removeAllViews();
                                    for (int i = 0; i < count; i++) {
                                        final int index = i;
//                                            View view2 = getLayoutInflater().inflate(R.layout.aty_applyform_gallery, null);
                                        View view2 = mInflater.inflate(R.layout.view_gallery, mGallery, false);
                                        ImageView img = (ImageView) view2.findViewById(R.id.image_illpic);
                                        mImageLoader.displayImage(list.get(i).get("SMALL"), img, mOptions);
                                        img.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(AtyConsultOpinion.this, ImageGalleryActivity.class);
                                                intent.putExtra(ImageGalleryActivity.URLS_KEY, array);
                                                intent.putExtra(ImageGalleryActivity.TYPE_KEY, 1);
                                                intent.putExtra("type", 1);// 0,1单个,多个
                                                intent.putExtra("position", index);
                                                startActivity(intent);
                                            }
                                        });
                                        mGallery.addView(view2);
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.btn_question:
                intent = new Intent(AtyConsultOpinion.this, AtyQuestion.class);
                intent.putExtra("conId", conId);
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
        loadOpinion();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
