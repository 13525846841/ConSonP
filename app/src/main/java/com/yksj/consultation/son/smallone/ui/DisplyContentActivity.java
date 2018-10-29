package com.yksj.consultation.son.smallone.ui;

/**
 * Created by HEKL on 16/5/11.
 * Used for
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.JsonHttpResponseHandler;
import com.yksj.healthtalk.utils.SpeechUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jack_tang on 15/8/26.
 * 显示症状文字的页面
 */
public class DisplyContentActivity extends BaseFragmentActivity implements View.OnClickListener {

    private String path;//图片路径
    private SpeechUtils mSpeechUtils;
    private TextView mContent;
    private ImageView mPlay;

    class MJsonHttpResponseHandler extends JsonHttpResponseHandler {
        public MJsonHttpResponseHandler() {
            super(DisplyContentActivity.this);
        }

        public void onSuccess(int statusCode, JSONObject response) {//成功
            if (!isFinishing()) {
                if (statusCode == 200 && response != null) {
                    try {
                        JSONObject json = new JSONObject(response.optString("retlist"));
                        mContent.setText(parseToText(json.optString("cont")));
                        mContent.setMovementMethod(LinkMovementMethod.getInstance());
                        if (88 == response.optInt("functionid", 0)) {
//                            title_right_text.setVisibility(View.VISIBLE);
                            path = response.optString("functionname");
//                            title_right_text.setVisibility(View.VISIBLE);
                        } else {
//                            title_right_text.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    /**
     * 超链接
     *
     * @param content
     * @return
     */
    private CharSequence parseToText(String content) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        Pattern mPattern = Pattern.compile("<a>([^<]*)</a>");
        final Matcher matcher = mPattern.matcher(content);
        int endnume = 0;
        while (matcher.find()) {
            final CharSequence con = matcher.group(1);
            builder.append(content.subSequence(endnume, matcher.start()));
            builder.append(con);
            endnume = matcher.end();
            builder.setSpan(new ClickableSpan() {
                                @Override
                                public void onClick(View widget) {
                                    chatWithServiceFromDrugs(con.toString());
                                }

                                @Override
                                public void updateDrawState(TextPaint ds) {
                                    super.updateDrawState(ds);
                                    ds.setColor(Color.RED);       //设置文件颜色
                                    ds.setUnderlineText(true);      //设置下划线
                                }
                            }, builder.length() - matcher.group(1).length(), builder.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            builder.setSpan(new UnderlineSpan(), builder.length() - matcher.group(1).length(), builder.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        if (content.length() > endnume) {
            builder.append(content.subSequence(endnume, content.length()));
        }
        return builder;
    }


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_displty_content);
        initView();
    }

    private void initView() {
        initTitle();
        setTitle(getIntent().getStringExtra("title"));
//        setLeftBack(this);
        titleRightBtn2.setOnClickListener(this);
        titleRightBtn2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.descti_right, 0);
        findViewById(R.id.play).setOnClickListener(this);
        mContent = (TextView) findViewById(R.id.desc);
        initData();
        mSpeechUtils = new SpeechUtils(this);
        mPlay = (ImageView) findViewById(R.id.play);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right2:
                Intent intent = new Intent(this, DisplyContentPicActivity.class);
                intent.putExtra("path", path);
                startActivity(intent);
                break;
            case R.id.play:
                mPlay.setSelected(!mPlay.isSelected());
                mSpeechUtils.startPeed(mContent.getText().toString(), mPlay);
                break;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        mSpeechUtils.stopPeed();
        mPlay.setSelected(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSpeechUtils.destory();
    }

    private void initData() {
        try {
            JSONObject jsonObject = new JSONObject(getIntent().getStringExtra("data"));
            HttpRestClient.doHttpVirtualDoctor(jsonObject, new MJsonHttpResponseHandler());
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void chatWithServiceFromDrugs(final String txt) {
//        sendMsgToSelf(txt);
//        RequestParams params = new RequestParams();
//        params.put("key", HApplication.mKey);
//        params.put("appID", HApplication.getApplication().getPackageName());
//        params.put("Type", "XiaoYi_text_kefu_DaoRu");
//        params.put("LNG", HApplication.getApplication().location[0]);
//        params.put("LAT", HApplication.getApplication().location[1]);
//        params.put("smstext", txt);
//        params.put("CUSTOMER_ID", LoginServiceManeger.instance().getLoginId());
//        params.put("TERMINAL_ID", HApplication.getDeviceNumber());
//        HttpRestClient.doHttpcreateOrder(params, new JsonHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(JSONObject response) {
//                        super.onSuccess(response);
//                        if (response.optInt("code") == 1) {
//                            JSONObject jo = response.optJSONObject("result");
//                            HApplication.getApplication().mTalkServiceType = jo.optString("SERVICE_TYPE");
//                            HApplication.getApplication().setMTalkServiceId(jo.optString("retServiceId"));
//                            if (!HStringUtil.isEmpty(jo.optString("href"))) {//此处判断，如果有药品链接就跳转，没有就请求客服
//                                if (jo.optString("href").contains("jd.com")) {
//                                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//                                        Uri uri = Uri.parse(jo.optString("href"));
//                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                                        DisplyContentActivity.this.startActivity(intent);
//                                    } else {
//                                        CommonWebUIActivity.startWeb(DisplyContentActivity.this, jo.optString("href"), jo.optString("title"));
//                                    }
//                                } else {
//                                    CommonWebUIActivity.startWeb(DisplyContentActivity.this, jo.optString("href"), jo.optString("title"));
//                                }
//                            } else {
//                                try {
//                                    jo.put("JD_TEXT", txt);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                                Intent intent = getIntent();
//                                intent.putExtra("data", jo.toString());
//                                setResult(RESULT_OK, intent);
//                                finish();
//                            }
//                        }
//
//                    }
//
//                }
//        );
    }
}

