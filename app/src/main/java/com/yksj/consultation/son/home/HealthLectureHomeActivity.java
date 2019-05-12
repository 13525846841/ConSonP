package com.yksj.consultation.son.home;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.netease.neliveplayer.playerkit.sdk.PlayerManager;
import com.netease.neliveplayer.playerkit.sdk.VodPlayer;
import com.netease.neliveplayer.playerkit.sdk.VodPlayerObserver;
import com.netease.neliveplayer.playerkit.sdk.constant.CauseCode;
import com.netease.neliveplayer.playerkit.sdk.model.MediaInfo;
import com.netease.neliveplayer.playerkit.sdk.model.StateInfo;
import com.netease.neliveplayer.playerkit.sdk.model.VideoBufferStrategy;
import com.netease.neliveplayer.playerkit.sdk.model.VideoOptions;
import com.netease.neliveplayer.playerkit.sdk.model.VideoScaleMode;
import com.netease.neliveplayer.playerkit.sdk.view.AdvanceSurfaceView;
import com.netease.neliveplayer.sdk.NEDefinitionData;
import com.netease.neliveplayer.sdk.NELivePlayer;
import com.squareup.okhttp.Request;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.PAtyConsultStudioGoPaying;
import com.yksj.consultation.son.consultation.bean.ServiceType;
import com.yksj.healthtalk.entity.HealthLectureRecEntity;
import com.yksj.healthtalk.entity.HealthLectureVideoEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.DensityUtils;
import com.yksj.healthtalk.utils.TimeUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.apache.http.message.BasicNameValuePair;
import org.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@TargetApi(Build.VERSION_CODES.M)
public class HealthLectureHomeActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, View.OnTouchListener {
    private int rewardPrice = 2;
    private ImageView imgReward;
    private TextView videoTime,starNum,EvaluationNum,tvrec;
    private SeekBar videoSeekBar;
    private AdvanceSurfaceView videoSur;
    private boolean isPlay;
    private ScrollView scrollView;
    private VodPlayer player;
    private MediaInfo mediaInfo;
    private boolean mHardware = true;
    private ImageView videoPlay, videoSound, videoSize;
    private boolean pressSeekBar;
    private boolean isScroll;//在切换到横屏状态的时候禁止滑动
    private boolean isSound;//视频是否静音
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (!pressSeekBar) videoSeekBar.setProgress((int) player.getCurrentPosition());
                videoTime.setText(stringForTime(player.getCurrentPosition()) + "/" + stringForTime(player.getDuration()));
            }
        }
    };
    private int widthPixels;
    private int heightPixels;
    private FrameLayout videoFrame;
    private int videoHeight;
    private LinearLayout lineBottom,lineParent;
    private FrameLayout topBar;
    private View videoClickView;
    private boolean isVideoStatusShow = true;//播放状态栏是否显示
    private LinearLayout videoStatusLine;
    private CountDownTimer countDownTimer;
    private String course_id,course_up_ID;
    private RatingBar ratingBar;
    private TextView tvTitle;
    private TextView docName;
    private TextView wenZJS;
    private HealthLectureVideoEntity.ResultBean result;
    private String lectureCId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_health_lecture_home);
        initView();
//        initPlayer();
        loadData();
        loadDataList();
    }

    private void loadData() {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("op", "queryBuyPageCourseInfo"));
//        pairs.add(new BasicNameValuePair("course_ID", "41"));
//        pairs.add(new BasicNameValuePair("customer_id", "124783"));
        pairs.add(new BasicNameValuePair("course_ID", course_id));
        pairs.add(new BasicNameValuePair("customer_id", LoginServiceManeger.instance().getLoginUserId()));
        HttpRestClient.doGetPersonClassroom(pairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                HealthLectureVideoEntity videoEntity = gson.fromJson(response, HealthLectureVideoEntity.class);
                result = videoEntity.getResult();
                if (result.getCOURSE_NAME().length()>10){
                    String title=result.getCOURSE_NAME().substring(0,15);
                    tvTitle.setText(title+"...");
                }else
                tvTitle.setText(result.getCOURSE_NAME());
                docName.setText(result.getCOURSE_UP_NAME());
                wenZJS.setText(result.getCOURSE_DESC());
                starNum.setText(result.getEvaNum()+"");
                ratingBar.setRating((float) result.getAvgStar());
                EvaluationNum.setText(result.getEvaNum()+"条评价");
                lectureCId = result.getCOURSE_ID();
                initPlayer(result.getCOURSE_ADDRESS());
//                Log.i("kkk", "onResponse: "+"http://jdvodocaoh4ht.vod.126.net/jdvodocaoh4ht"+result.getCOURSE_ADDRESS());
            }
        }, this);
    }

    private void loadDataList() {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("op", "queryLikenessClass"));
        pairs.add(new BasicNameValuePair("course_up_ID", course_up_ID));
        pairs.add(new BasicNameValuePair("pageNum", "1"));
        pairs.add(new BasicNameValuePair("course_id", course_id));
//        pairs.add(new BasicNameValuePair("course_id", "41"));
        HttpRestClient.doGetPersonClassroom(pairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                HealthLectureRecEntity healthLectureRecEntity = gson.fromJson(response, HealthLectureRecEntity.class);
                List<HealthLectureRecEntity.ResultBean> result = healthLectureRecEntity.getResult();
                if (result.size()==0)tvrec.setVisibility(View.GONE);
                for (int i = 0; i < result.size(); i++) {
                    if (isDestroyed())return;
                    View inflate = View.inflate(HealthLectureHomeActivity.this, R.layout.healthlecture_recommend_item, null);
                    ImageView img= (ImageView) inflate.findViewById(R.id.imgLift);
                    TextView title = (TextView) inflate.findViewById(R.id.title);
                    TextView name = (TextView) inflate.findViewById(R.id.name);
                    TextView tvDate = (TextView) inflate.findViewById(R.id.tvDate);
                    final HealthLectureRecEntity.ResultBean resultBean = result.get(i);
                    Glide.with(HealthLectureHomeActivity.this).load(ImageLoader.getInstance().getDownPathUri(resultBean.getSMALL_PIC()))
                            .error(R.drawable.waterfall_default).placeholder(R.drawable.waterfall_default).dontAnimate().into(img);
                    title.setText(resultBean.getCOURSE_NAME());
                    name.setText(resultBean.getCOURSE_UP_NAME());
                    tvDate.setText(TimeUtil.getTimeStr8(resultBean.getCOURSE_UP_TIME()));
                    inflate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //if (resultBean.getCOURSE_PAY().equals("1")&&resultBean.getCOURSE_ISPAY().equals("0")) {
                            Intent intent = new Intent(HealthLectureHomeActivity.this, HealthLecturePayInfoActivity.class);
                            intent.putExtra("course_ID",resultBean.getCOURSE_ID());
                            intent.putExtra("tuwenTime",resultBean.getCOURSE_UP_TIME());
                            startActivity(intent);
//        }else {
//            if (resultBean.getCOURSE_CLASS().equals("20")){
//                Intent intent = new Intent(this, HealthLectureTuwenActivity.class);
//                intent.putExtra("course_ID",resultBean.getCOURSE_ID());
//                intent.putExtra("info",resultBean);
//                startActivity(intent);
//            }else if (resultBean.getCOURSE_CLASS().equals("30")){
//                Intent intent = new Intent(this, HealthLectureHomeActivity.class);
//                intent.putExtra("course_ID",resultBean.getCOURSE_ID());
//                intent.putExtra("info",resultBean);
//                startActivity(intent);
//            }
//
//        }
                        }
                    });
                    lineParent.addView(inflate);
                }
            }
        }, this);
    }


    private void initView() {
        course_id = getIntent().getStringExtra("course_ID");
        course_up_ID = getIntent().getStringExtra("course_up_ID");
        widthPixels = getResources().getDisplayMetrics().widthPixels;
        heightPixels = getResources().getDisplayMetrics().heightPixels;
        videoHeight = DensityUtils.dip2px(this, 200f);
        videoClickView = findViewById(R.id.videoClickView);//videoView表面的点击时间用来显示video的控制栏
        videoClickView.setOnClickListener(this);
        findViewById(R.id.title_back).setOnClickListener(this);
        ImageView share = (ImageView) findViewById(R.id.share);
        imgReward = (ImageView) findViewById(R.id.imgReward);
        share.setOnClickListener(this);
        imgReward.setOnClickListener(this);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        starNum = (TextView) findViewById(R.id.starNum);
        EvaluationNum = (TextView) findViewById(R.id.EvaluationNum);
        tvrec = (TextView) findViewById(R.id.tvrec);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        docName = (TextView) findViewById(R.id.docName);
        wenZJS = (TextView) findViewById(R.id.wenZJS);
        TextView tvCollection = (TextView) findViewById(R.id.tvCollection);
        TextView yuyueMZ = (TextView) findViewById(R.id.yuyueMZ);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.setOnTouchListener(this);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M)
            yuyueMZ.setOnClickListener(this);
        topBar = (FrameLayout) findViewById(R.id.topBar);
        FrameLayout goDocInfo = (FrameLayout) findViewById(R.id.goDocInfo);
        goDocInfo.setOnClickListener(this);
        videoFrame = (FrameLayout) findViewById(R.id.videoFrame);
        lineBottom = (LinearLayout) findViewById(R.id.lineBottom);
        lineParent = (LinearLayout) findViewById(R.id.lineParent);
        videoStatusLine = (LinearLayout) findViewById(R.id.videoStatusLine);
        LinearLayout goEvaluation = (LinearLayout) findViewById(R.id.goEvaluation);
        goEvaluation.setOnClickListener(this);
        tvCollection.setOnClickListener(this);
        videoSur = (AdvanceSurfaceView) findViewById(R.id.videoSur);
        videoPlay = (ImageView) findViewById(R.id.videoPlay);
        videoPlay.setOnClickListener(this);
        videoSound = (ImageView) findViewById(R.id.videoSound);
        videoSound.setOnClickListener(this);
        videoSize = (ImageView) findViewById(R.id.videoSize);
        videoSize.setOnClickListener(this);
        videoSeekBar = (SeekBar) findViewById(R.id.videoSeekBar);
        videoSeekBar.setOnSeekBarChangeListener(this);
        videoTime = (TextView) findViewById(R.id.videoTime);
        countDownTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                videoStatusLine.setVisibility(View.GONE);
            }
        };

    }

    private void initPlayer(String url) {
        VideoOptions options = new VideoOptions();
        options.autoSwitchDefinition = false;
        options.hardwareDecode = mHardware;
        /**
         * isPlayLongTimeBackground 控制退到后台或者锁屏时是否继续播放，开发者可根据实际情况灵活开发,我们的示例逻辑如下：
         * 使用软件解码：
         * isPlayLongTimeBackground 为 false 时，直播进入后台停止播放，进入前台重新拉流播放
         * isPlayLongTimeBackground 为 true 时，直播进入后台不做处理，继续播放,
         *
         * 使用硬件解码：
         * 直播进入后台停止播放，进入前台重新拉流播放
         */

        options.bufferStrategy = VideoBufferStrategy.ANTI_JITTER;
        player = PlayerManager.buildVodPlayer(this, url, options);

//        intentToStartBackgroundPlay();
        start();
        player.setupRenderView(videoSur, VideoScaleMode.FIT);

    }

    private VodPlayerObserver playerObserver = new VodPlayerObserver() {
        @Override
        public void onCurrentPlayProgress(long currentPosition, long duration, float percent, long cachedPosition) {
            mHandler.sendEmptyMessageDelayed(0, 1000);
        }

        @Override
        public void onSeekCompleted() {

        }

        @Override
        public void onCompletion() {
            isPlay = false;
            videoStatusLine.setVisibility(View.VISIBLE);
            videoPlay.setImageResource(R.drawable.nemediacontroller_pause);
        }

        @Override
        public void onNetStateBad() {

        }

        @Override
        public void onDecryption(int ret) {

        }

        @Override
        public void onPreparing() {

        }

        @Override
        public void onPrepared(MediaInfo info) {
            mediaInfo = info;
        }

        @Override
        public void onError(int code, int extra) {
//            mBuffer.setVisibility(View.INVISIBLE);
            if (code == CauseCode.CODE_VIDEO_PARSER_ERROR) {
                showToast("视频解析出错");
            } else {
                AlertDialog.Builder build = new AlertDialog.Builder(HealthLectureHomeActivity.this);
                build.setTitle("播放错误").setMessage("错误码：" + code)
                        .setPositiveButton("确定", null)
                        .setCancelable(false)
                        .show();
            }

        }

        @Override
        public void onFirstVideoRendered() {
            videoSeekBar.setMax((int) player.getDuration());
            videoTime.setText("00:00:00/" + stringForTime(player.getDuration()));
//            showToast("视频第一帧已解析");
        }

        @Override
        public void onFirstAudioRendered() {
//            showToast("音频第一帧已解析");
        }

        @Override
        public void onBufferingStart() {
//            mBuffer.setVisibility(View.VISIBLE);
        }

        @Override
        public void onBufferingEnd() {
//            mBuffer.setVisibility(View.GONE);
        }

        @Override
        public void onBuffering(int percent) {
//            LogUtil.d(TAG, "缓冲中..." + percent + "%");
//            mProgressBar.setSecondaryProgress(percent);
        }

        @Override
        public void onHardwareDecoderOpen() {

        }

        @Override
        public void onStateChanged(StateInfo stateInfo) {

        }

        @Override
        public void onParseDefinition(List<NEDefinitionData> data) {
            showToast("解析到多个清晰度");

        }

        @Override
        public void onAutoSwitchDefinition(NEDefinitionData.DefinitionType definitionType) {
            showToast("自动切换到清晰度" + definitionType);

        }

        @Override
        public void onVideoFrameFilter(NELivePlayer.NEVideoRawData videoRawData) {

        }

        @Override
        public void onAudioFrameFilter(NELivePlayer.NEAudioRawData audioRawData) {

        }

        @Override
        public void onHttpResponseInfo(int code, String header) {
//            Log.i(TAG, "onHttpResponseInfo,code:" + code + " header:" + header);
        }
    };


    private void showToast(String msg) {
        try {
            Toast.makeText(HealthLectureHomeActivity.this, msg, Toast.LENGTH_SHORT).show();
        } catch (Throwable th) {
            th.printStackTrace(); // fuck oppo
        }
    }

    private void start() {
        player.registerPlayerObserver(playerObserver, true);
//        player.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.share:
                break;
            case R.id.tvCollection:
                ToastUtil.onShow(this,"收藏成功",1000);
                break;
            case R.id.imgReward:
                createRewardPop();
                break;
            case R.id.goDocInfo:
                Intent intent = new Intent(this, DoctorInfoActivity.class);
                intent.putExtra("customer_id", Integer.valueOf(result.getCOURSE_UP_ID()));
                startActivity(intent);
                break;
            case R.id.goEvaluation:
                break;
            case R.id.yuyueMZ:

                Intent intent1 = new Intent(this, DoctorInfoActivity.class);
                intent1.putExtra("customer_id", Integer.valueOf(result.getCOURSE_UP_ID()));
                startActivity(intent1);
//                intent = new Intent(this, BuyServiceListFromPatientActivity.class);
//                intent.putExtra("consultId", "");
////                intent.putExtra(BuyServiceListFromPatientActivity.DOCTOR_NAME, expertName);//医生姓名
////                intent.putExtra(BuyServiceListFromPatientActivity.DOCTOR_ID, doctor_id);//医生ID
//                intent.putExtra("type", 3);
//                startActivity(intent);
                break;
            case R.id.videoPlay:
                if (!isPlay) {
                    player.start();
                    videoPlay.setImageResource(R.drawable.nemediacontroller_play);
                    isPlay = true;
                    countDownTimer.start();
                } else {
                    player.pause();
                    videoPlay.setImageResource(R.drawable.nemediacontroller_pause);
                    isPlay = false;
                    countDownTimer.cancel();
                }
                break;
            case R.id.videoSound:
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (isSound) {
                    player.setVolume(streamVolume);
                    videoSound.setImageResource(R.drawable.nemediacontroller_mute02);
                    isSound = false;
                } else {
                    player.setVolume(0f);
                    videoSound.setImageResource(R.drawable.nemediacontroller_mute01);
                    isSound = true;
                }
                break;
            case R.id.videoSize:
                int screenNum = getResources().getConfiguration().orientation;
                if (screenNum == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    videoSize.setImageResource(R.drawable.nemediacontroller_scale02);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    videoSize.setImageResource(R.drawable.nemediacontroller_scale01);
                }
                break;
            case R.id.videoClickView:
                int visibility = videoStatusLine.getVisibility();
                if (isPlay) {
                    if (visibility == View.GONE) {
                        countDownTimer.start();
                        videoStatusLine.setVisibility(View.VISIBLE);
                    } else {
                        videoStatusLine.setVisibility(View.GONE);
                        countDownTimer.cancel();
                    }
                }
                break;
        }
    }

    private static String stringForTime(long position) {
        int totalSeconds = (int) ((position / 1000.0) + 0.5);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds).toString();
    }

    private void createRewardPop() {
        final PopupWindow popupWindow = new PopupWindow();
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        View view = View.inflate(this, R.layout.popupwindow_reward, null);
        final Button button1 = (Button) view.findViewById(R.id.button1);
        final Button button2 = (Button) view.findViewById(R.id.button2);
        final Button button3 = (Button) view.findViewById(R.id.button3);
        final Button button4 = (Button) view.findViewById(R.id.button4);
        Button button5 = (Button) view.findViewById(R.id.button5);
        view.findViewById(R.id.framePop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rewardPrice = 2;
                selectBtn(button1, button2, button3, button4, button1);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rewardPrice = 5;
                selectBtn(button1, button2, button3, button4, button2);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rewardPrice = 10;
                selectBtn(button1, button2, button3, button4, button3);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rewardPrice = 20;
                selectBtn(button1, button2, button3, button4, button4);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(HealthLectureHomeActivity.this, PAtyConsultStudioGoPaying.class);
                intent2.putExtra("service_id", ServiceType.DS);
                intent2.putExtra("course_id", result.getCOURSE_ID());
                intent2.putExtra("price", rewardPrice+"");//价格
                startActivityForResult(intent2,1000);
                popupWindow.dismiss();
            }
        });
        popupWindow.setContentView(view);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#30000000")));
        popupWindow.showAtLocation(imgReward, Gravity.CENTER, 0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1000&&resultCode==9000&&data!=null){
            if (data.getStringExtra("alipayStatus").equals("9000")) {
                ToastUtil.onShow(HealthLectureHomeActivity.this,"打赏成功",1000);
            }
        }
    }

    private void selectBtn(Button button1, Button button2, Button button3, Button button4, Button button) {
        button1.setBackgroundResource(R.drawable.shape_msgbg);
        button1.setTextColor(Color.parseColor("#000000"));
        button2.setBackgroundResource(R.drawable.shape_msgbg);
        button2.setTextColor(Color.parseColor("#000000"));
        button3.setBackgroundResource(R.drawable.shape_msgbg);
        button3.setTextColor(Color.parseColor("#000000"));
        button4.setBackgroundResource(R.drawable.shape_msgbg);
        button4.setTextColor(Color.parseColor("#000000"));
        button.setBackgroundResource(R.drawable.shape_msgbg_yellow);
        button.setTextColor(Color.parseColor("#ffffff"));
    }

    private void releasePlayer() {
        if (player == null) {
            return;
        }
        player.registerPlayerObserver(playerObserver, false);
        player.setupRenderView(null, VideoScaleMode.NONE);
        player.stop();
        player = null;
        mHandler.removeCallbacksAndMessages(null);
    }

    public String getVideoPath() {
        Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
        try {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)); // id
                String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM)); // 专辑
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST)); // 艺术家
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)); // 显示名称
                String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)); // 路径
                long duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)); // 时长
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)); // 大小
                String resolution = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION));
                Log.i("hjh", "getVideoPath: " + duration + "  " + size);
                return path;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private void videoConfigurationChanged(int width, int height) {
        ViewGroup.LayoutParams lineParams = videoFrame.getLayoutParams();
        lineParams.height = height;
        lineParams.width = width;
        videoFrame.setLayoutParams(lineParams);

        ViewGroup.LayoutParams flParams = videoSur.getLayoutParams();
        flParams.height = height;
        flParams.width = width;
        videoSur.setLayoutParams(flParams);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("hjh", "onConfigurationChanged: " + newConfig.orientation);
        //横屏
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            videoConfigurationChanged(ViewGroup.LayoutParams.MATCH_PARENT, widthPixels);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);//移除半屏状态
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏状态
            lineBottom.setVisibility(View.GONE);
            topBar.setVisibility(View.GONE);
            isScroll = true;
            scrollView.scrollTo(0, 0);
            videoSize.setImageResource(R.drawable.nemediacontroller_scale02);
        } else {//竖屏
            videoConfigurationChanged(ViewGroup.LayoutParams.MATCH_PARENT, videoHeight);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            lineBottom.setVisibility(View.VISIBLE);
            topBar.setVisibility(View.VISIBLE);
            isScroll = false;
            videoSize.setImageResource(R.drawable.nemediacontroller_scale01);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        videoTime.setText(stringForTime(progress) + "/" + stringForTime(player.getDuration()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        pressSeekBar = true;
        mHandler.removeMessages(0);
        if (isPlay) countDownTimer.cancel();

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        pressSeekBar = false;
        player.seekTo(seekBar.getProgress());
        if (isPlay) countDownTimer.start();
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (isSound) {
            player.setVolume(0f);
        } else {
            player.setVolume(streamVolume);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.onActivityStop(false);
        }
        if (isPlay && player != null) {
            player.pause();
            videoPlay.setImageResource(R.drawable.nemediacontroller_pause);
            isPlay = false;
            countDownTimer.cancel();
            videoStatusLine.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }


    //scrollView的onTouch事件
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return isScroll;

    }
}
