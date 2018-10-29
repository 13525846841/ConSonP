package com.yksj.consultation.son.consultation.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import de.greenrobot.event.EventBus;

/**
 * Created by HEKL on 16/6/27.
 * Used for
 */

public class RecordMadeAty extends BaseFragmentActivity implements View.OnClickListener {
    private MovieRecorderView movieRV;//录制
    private ImageView startBtn;//开始
    private ImageView stopBtn;//结束
    private ImageView cancelBtn;
    private ImageView confirmBtn;
    private ImageView thumbnail;//缩略图
    private boolean isRecording = false;//是否在录制中

    private long startTime;
    private long endTime;

    int position;

    public RecordMadeAty() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_recordmade);
        initViews();
        initEvents();
        init();
        EventBus.getDefault().register(RecordMadeAty.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(RecordMadeAty.this);
    }

    private void init() {

    }

    private void initViews() {
        movieRV = (MovieRecorderView) findViewById(R.id.moive_rv);
        //录制
        startBtn = (ImageView) findViewById(R.id.start_btn);
        stopBtn = (ImageView) findViewById(R.id.stop_btn);
        cancelBtn = (ImageView) findViewById(R.id.cancel_btn);
        confirmBtn = (ImageView) findViewById(R.id.confirm_btn);
        thumbnail = (ImageView) findViewById(R.id.preshow_thumbnail);
        findViewById(R.id.playImageView).setOnClickListener(this);
        findViewById(R.id.preshow).setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        thumbnail.setOnClickListener(this);


    }

    private void initEvents() {
        //开始录制
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isRecording) {
                    movieRV.record(new MovieRecorderView.OnRecordFinishListener() {
                        @Override
                        public void onRecordFinish() {
                            movieRV.stop();
                            EventBus.getDefault().post("show");
                        }
                    }, new MovieRecorderView.OnRecordStartListener() {
                        @Override
                        public void onRecordStart() {
                            startTime = System.currentTimeMillis();
                            isRecording = true;
                            findViewById(R.id.preshow).setVisibility(View.GONE);//缩略图播放图标
                            findViewById(R.id.preshow_thumbnail).setVisibility(View.GONE);//缩略图
                            findViewById(R.id.cancel_btn).setVisibility(View.GONE);
                            findViewById(R.id.confirm_btn).setVisibility(View.GONE);
                            findViewById(R.id.start_btn).setVisibility(View.GONE);
                            findViewById(R.id.stop_btn).setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
        //停止录制
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTime = System.currentTimeMillis();
                if (endTime - startTime < 2000) {
                    return;
                }
                movieRV.stop();
                EventBus.getDefault().post("show");
            }
        });


    }


    @Override
    protected void onRestart() {
        super.onRestart();
        movieRV.stop();
        RecordMadeAty.this.finish();
        if (isRecording) {
            findViewById(R.id.start_btn).setVisibility(View.GONE);
            findViewById(R.id.stop_btn).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.start_btn).setVisibility(View.VISIBLE);
            findViewById(R.id.stop_btn).setVisibility(View.GONE);
        }

        findViewById(R.id.preshow).setVisibility(View.GONE);
        findViewById(R.id.preshow_thumbnail).setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
//        //先判断是否正在播放
//        if (player.isPlaying()) {
//            //如果正在播放我们就先保存这个播放位置
//            position=player.getCurrentPosition()
//            ;
//            player.stop();
//        }
        super.onPause();
        movieRV.stop();

    }

    @Override
    protected void onStop() {
        super.onStop();
        movieRV.stop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_btn://取消录制
                if (movieRV != null) {
                    movieRV.deleteRecordFile();
                }
                onBackPressed();
                break;
            case R.id.confirm_btn://确认录制
                if (movieRV.getmVecordFile() != null) {
                    //数据是使用Intent返回
                    Intent intent = new Intent();
                    //把返回数据存入Intent
                    intent.putExtra("filePath", movieRV.getmVecordFile().getAbsolutePath());
                    //设置返回数据
                    RecordMadeAty.this.setResult(RESULT_OK, intent);
                    //关闭Activity
                    RecordMadeAty.this.finish();
                }else {
                    ToastUtil.showShort("生成文件错误,请检查相关权限");
                }
                break;
            case R.id.preshow://预览
                String string=movieRV.getmVecordFile().getAbsolutePath();
                if (!HStringUtil.isEmpty(string)){
                    startActivityForResult(new Intent(this, PlayVideoActiviy.class).putExtra(PlayVideoActiviy.KEY_FILE_PATH, movieRV.getmVecordFile().getAbsolutePath()), 1);
                }else {
                    ToastUtil.showShort("生成文件错误,请检查相关权限");
                }
                break;
            case R.id.playImageView://小图预览
//                startActivity(new Intent(this, PlayVideoActiviy.class).putExtra(PlayVideoActiviy.KEY_FILE_PATH, movieRV.getmVecordFile().getAbsolutePath()));
                break;
        }
    }

    public void onEventMainThread(String str) {
        if ("show".equals(str)) {
            findViewById(R.id.start_btn).setVisibility(View.VISIBLE);
            findViewById(R.id.stop_btn).setVisibility(View.GONE);

            findViewById(R.id.cancel_btn).setVisibility(View.VISIBLE);
            findViewById(R.id.confirm_btn).setVisibility(View.VISIBLE);
            findViewById(R.id.preshow).setVisibility(View.VISIBLE);
            findViewById(R.id.preshow_thumbnail).setVisibility(View.VISIBLE);

            thumbnail.setImageBitmap(PlayVideoActiviy.getVideoThumbnail(movieRV.getmVecordFile().getAbsolutePath()));
            startActivity(new Intent(this, PlayVideoActiviy.class).putExtra(PlayVideoActiviy.KEY_FILE_PATH, movieRV.getmVecordFile().getAbsolutePath()));
            isRecording = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                Intent intent = new Intent();
                intent.putExtra("filePath", data.getStringExtra("filePath"));
                RecordMadeAty.this.setResult(RESULT_OK, intent);
                RecordMadeAty.this.finish();
            }

        }
    }


}