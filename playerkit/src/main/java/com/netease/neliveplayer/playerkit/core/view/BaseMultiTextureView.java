package com.netease.neliveplayer.playerkit.core.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;

import com.netease.neliveplayer.playerkit.common.log.LogUtil;
import com.netease.neliveplayer.playerkit.sdk.model.VideoScaleMode;

/**
 * @author netease
 * 多个TextureView
 * 适用于播放页面有多个TextureView切换播放时
 *
 */

public class BaseMultiTextureView extends TextureView implements IRenderView, TextureView.SurfaceTextureListener {

    private String TAG = "NoneSavedBaseTextureView";

    /// callback
    private IRenderView.SurfaceCallback mCallback;

    /// surface holder state
    private Surface mSurface;
    private boolean mSizeChanged;
    private int mWidth;
    private int mHeight;

    /// measure
    private MeasureHelper mMeasureHelper;

    /**
     * ******************************** 构造器 ****************************
     */

    public BaseMultiTextureView(Context context) {
        super(context);
        init();
    }

    public BaseMultiTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseMultiTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mMeasureHelper = new MeasureHelper(this);
        setSurfaceTextureListener(this);
    }

    /**
     * *********************************** IRenderView **********************************
     */

    @Override
    public void onSetupRenderView() {

    }

    @Override
    public void setCallback(SurfaceCallback callback) {
        if (mCallback != null || callback == null) {
            return; // 已经注册过的或者null注册的，直接返回
        }

        mCallback = callback;
        if (mSurface != null) {
            mCallback.onSurfaceCreated(getSurface());
        }

        if (mSizeChanged) {
            mCallback.onSurfaceSizeChanged(getSurface(), 0, mWidth, mHeight);
        }
    }

    @Override
    public void removeCallback() {
        mCallback = null;
    }

    @Override
    public void showView(boolean show) {
        setVisibility(show ? VISIBLE : GONE);
    }

    @Override
    public void setVideoSize(int videoWidth, int videoHeight, int videoSarNum, int videoSarDen, VideoScaleMode scaleMode) {
        boolean changed = false;

        if (videoWidth > 0 && videoHeight > 0 && mMeasureHelper.setVideoSize(videoWidth, videoHeight)) {
            changed = true;
        }

        if (videoSarNum > 0 && videoSarDen > 0 && mMeasureHelper.setVideoSampleAspectRatio(videoSarNum, videoSarDen)) {
            changed = true;
        }

        if (scaleMode != null && mMeasureHelper.setVideoScaleMode(scaleMode)) {
            changed = true;
        }

        if (changed) {
            LogUtil.i(TAG,"set video size to render view done, request layout...");
            requestLayout();
        }
    }

    @Override
    public Surface getSurface() {
        return mSurface;
    }

    /**
     * *********************************** SurfaceTextureListener **********************************
     */

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mSurface = new Surface(surface);
        mSizeChanged = false;
        mWidth = 0;
        mHeight = 0;

        if (mCallback != null) {
            mCallback.onSurfaceCreated(mSurface);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        mSizeChanged = true;
        mWidth = width;
        mHeight = height;

        if (mCallback != null) {
            mCallback.onSurfaceSizeChanged(null, 0, width, height);
        }
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mSurface = null;
        mSizeChanged = false;
        mWidth = 0;
        mHeight = 0;

        if (mCallback != null) {
            mCallback.onSurfaceDestroyed(null);
        }

        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        boolean valid = mMeasureHelper.doMeasure(widthMeasureSpec, heightMeasureSpec);
        if (valid) {
            setMeasuredDimension(mMeasureHelper.getMeasuredWidth(), mMeasureHelper.getMeasuredHeight());
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
