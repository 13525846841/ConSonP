package com.netease.neliveplayer.playerkit.core.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;

import com.netease.neliveplayer.playerkit.common.log.LogUtil;
import com.netease.neliveplayer.playerkit.sdk.model.VideoScaleMode;


/**
 * @author netease
 * 单一TextureView
 * 适用于播放页面只有一个TextureView时，支持后台播放
 *
 */

public class BaseSingleTextureView extends TextureView implements IRenderView, TextureView.SurfaceTextureListener {
    /// callback
    private IRenderView.SurfaceCallback mCallback;

    //Surface Texture
    private static SurfaceTexture savedSurfaceTexture;

    /// surface holder state
    private static Surface mSurface;
    private boolean mSizeChanged;
    private int mWidth;
    private int mHeight;

    /// measure
    private MeasureHelper mMeasureHelper;

    private boolean isFirstAvailable = false;

    /**
     * ******************************** 构造器 ****************************
     */

    public BaseSingleTextureView(Context context) {
        super(context);
        init();
    }

    public BaseSingleTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseSingleTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        isFirstAvailable = true;
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
            LogUtil.ui("set video size to render view done, request layout...");
            requestLayout();
        }
    }

    @Override
    public Surface getSurface() {
        return mSurface;
    }

    public void releaseSurface() {
        if (savedSurfaceTexture != null) {
            savedSurfaceTexture.release();
        }
        savedSurfaceTexture = null;

        if (mSurface != null) {
            mSurface.release();
        }
        mSurface = null;

    }

    @Override
    protected void onDetachedFromWindow(){
        super.onDetachedFromWindow();
        releaseSurface();
    }

    /**
     * *********************************** SurfaceTextureListener **********************************
     */

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        //api 16 以上才能支持 setSurfaceTexture 接口，才能支持后台播放
        boolean hasSetsavedSurfaceTexture  = false;
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) && savedSurfaceTexture != null && !isFirstAvailable){
            try {
                setSurfaceTexture(savedSurfaceTexture);
                hasSetsavedSurfaceTexture = true;
            }catch (IllegalArgumentException e) {
                LogUtil.error("onSurfaceTextureAvailable,setSurfaceTexture ",e);
            }
        }
        if(!hasSetsavedSurfaceTexture){
            isFirstAvailable = false;
            releaseSurface();
            mSurface = new Surface(surfaceTexture);
            if (mCallback != null) {
                mCallback.onSurfaceCreated(mSurface);
            }
        }
        mSizeChanged = false;
        mWidth = 0;
        mHeight = 0;
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
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        mSizeChanged = false;
        mWidth = 0;
        mHeight = 0;
        if (savedSurfaceTexture == null ) {
            if (mCallback != null) {
                mCallback.onSurfaceDestroyed(null);
            }
        }
        savedSurfaceTexture = surfaceTexture;

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
