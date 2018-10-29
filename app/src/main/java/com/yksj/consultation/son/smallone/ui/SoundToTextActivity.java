package com.yksj.consultation.son.smallone.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.smallone.common.ApkInstaller;
import com.yksj.healthtalk.utils.JsonParser;
import com.yksj.healthtalk.utils.ThreadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;


/**
 * Created by jack_tang on 15/8/28.
 * 忘记密码
 */
public class SoundToTextActivity extends BaseFragmentActivity implements View.OnClickListener {

    TextView textView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.sound_to_text);

        initView();
    }

    private void initView() {
        initTitle();
//        setLeftBack(this);
        textView = (TextView) findViewById(R.id.textview_sound_to_text);
        initVoiceData();
        initDate();
    }

    private void initDate() {
        mIatResults.clear();
        mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
        if (mIat.startListening(mRecognizerListener) == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
            //未安装则跳转到提示安装页面
            new ApkInstaller(SoundToTextActivity.this).install();
        } else {

            ThreadManager.getInstance().createShortPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                /*
                 * 通过URL取得HttpURLConnection
                 * 要网络连接成功，需在AndroidMainfest.xml中进行权限配置
                 * <uses-permission android:name="android.permission.INTERNET" />
                 */
                        URL url = new URL(getIntent().getStringExtra("url"));
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        //取得inputStream，并进行读取
                        InputStream input = conn.getInputStream();
                        byte[] data =InputStreamTOByte(input);

                        input.close();
                        mIat.writeAudio(data, 0, data.length);
                        mIat.stopListening();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;

        }
    }

    // 语音听写对象
    private SpeechRecognizer mIat;
    private SharedPreferences mSharedPreferences;
    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
//                ToastUtil.showToastPanl("初始化失败，错误码：" + code);
//                mTalkRipView.setVisibility(View.GONE);
//                ToastUtil.showShort(ChatActivityB.this, "初始化失败，错误码：" + code);
//				showTip("初始化失败，错误码：" + code);

            }
        }
    };

    /**
     * 参数设置
     *
     * @param
     * @return
     */

    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        String lag = mSharedPreferences.getString("iat_language_preference",
                "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "4000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.FORCE_LOGIN, "true");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");

    }

    RecognizerListener mRecognizerListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {

        }

        @Override
        public void onBeginOfSpeech() {

        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            if (b) {
            } else {
                printResult(recognizerResult);
            }

        }

        @Override
        public void onError(SpeechError speechError) {
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
        }
    };


    private void initVoiceData() {
        SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
        mSharedPreferences = this.getSharedPreferences(PREFER_NAME, Activity.MODE_PRIVATE);
        // 设置参数
        setParam();
    }

    private final String PREFER_NAME = "com.iflytek.setting";
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private void printResult(RecognizerResult results) {
//        RingPlayer.playPressSound(mActivity);
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mIatResults.put(sn, text);
        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        textView.setText(resultBuffer.toString());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时释放连接
        mIat.cancel();
        mIat.destroy();
    }


    public  byte[] InputStreamTOByte(InputStream in) throws IOException{

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int count = -1;
        while((count = in.read(data,0,1024)) != -1)
            outStream.write(data, 0, count);
        byte[] dataOut= outStream.toByteArray();
        outStream.close();
        return dataOut;
    }


}
