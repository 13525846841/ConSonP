package com.yksj.consultation.son.consultation.member;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.yksj.consultation.son.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;

/**
 * 播放视频页面
 *
 * @author Martin
 */
public class PlayVideoActiviy extends Activity implements View.OnClickListener {

    public static final String TAG = "PlayVideoActiviy";

    public static final String KEY_FILE_PATH = "file_path";

    private String filePath;

    private ScalableVideoView mScalableVideoView;
    private ImageView mPlayImageView;
    private ImageView mThumbnailImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filePath = getIntent().getStringExtra(KEY_FILE_PATH);
//        Log.d(TAG, filePath);
        if (TextUtils.isEmpty(filePath)) {
            Toast.makeText(this, "视频路径错误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setContentView(R.layout.activity_play_video);
        mScalableVideoView = (ScalableVideoView) findViewById(R.id.video_view);
        try {
            // 这个调用是为了初始化mediaplayer并让它能及时和surface绑定
            mScalableVideoView.setDataSource("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        mPlayImageView = (ImageView) findViewById(R.id.playImageView);

        mThumbnailImageView = (ImageView) findViewById(R.id.thumbnailImageView);
        mThumbnailImageView.setImageBitmap(getVideoThumbnail(filePath));

        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.confirm).setOnClickListener(this);

    }

    /**
     * 获取视频缩略图（这里获取第一帧）
     *
     * @param filePath
     * @return
     */
    public static Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime(TimeUnit.MILLISECONDS.toMicros(1));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_view:
                mScalableVideoView.stop();
                mPlayImageView.setVisibility(View.VISIBLE);
                mThumbnailImageView.setVisibility(View.VISIBLE);
                break;
            case R.id.playImageView:
                try {
                    mScalableVideoView.setDataSource(filePath);
                    mScalableVideoView.setLooping(true);
                    mScalableVideoView.prepare();
                    mScalableVideoView.start();
                    mPlayImageView.setVisibility(View.GONE);
                    mThumbnailImageView.setVisibility(View.GONE);
                } catch (IOException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                    Toast.makeText(this, "播放视频异常", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cancel://取消
                onBackPressed();
                break;
            case R.id.confirm://确认
                Intent intent = new Intent();
                intent.putExtra("filePath", filePath);
                PlayVideoActiviy.this.setResult(RESULT_OK, intent);
                EventBus.getDefault().post("RecordMadeAty");
                PlayVideoActiviy.this.finish();
                break;
        }
    }

}
