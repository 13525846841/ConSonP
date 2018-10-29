package com.netease.neliveplayer.playerkit.sdk;

import android.graphics.Bitmap;

import com.netease.neliveplayer.playerkit.core.view.IRenderView;
import com.netease.neliveplayer.playerkit.sdk.model.StateInfo;
import com.netease.neliveplayer.playerkit.sdk.model.VideoScaleMode;
import com.netease.neliveplayer.sdk.NECallback;
import com.netease.neliveplayer.sdk.NEDefinitionData;
import com.netease.neliveplayer.sdk.NELivePlayer;

/**
 * 网易云信直播拉流播放器接口
 * 基于播放器SDK封装的直播相关的接口
 * <p>
 * @author netease
 */

public abstract class LivePlayer {



    public enum STATE {

        /**
         * 播放器初始状态
         */
        IDLE,

        /**
         * 播放器准备中
         */
        PREPARING,

        /**
         * 播放器开始播放
         */
        PLAYING,

        /**
         * [点播专用]暂停
         */
        PAUSED,

        /**
         * 播放过程出错，播放结束
         */
        ERROR,

        /**
         * 播放器停止（已销毁）本质上与IDLE状态相同
         */
        STOPPED,
    }

    /**
     * 监听/取消监听播放器状态/事件回调
     *
     * @param observer 播放器观察者
     * @param register true表示注册; false表示注销
     */
    public abstract void registerPlayerObserver(LivePlayerObserver observer, boolean register);


    /**
     * 注册播放过程的实时真实时间戳回调
     * 在start接口后调用
     * @param interval 预期的回调的时间间隔(单位：毫秒 ms)
     * @param listener 回调监听器
     * @param register true表示注册; false表示注销
     */
    public abstract void registerPlayerCurrentRealTimestampListener(final long interval, NELivePlayer.OnCurrentRealTimeListener listener, boolean register);


    /**
     * 注册播放过程的实时同步时间戳回调
     * 在start接口后调用
     * @param interval 预期的回调的时间间隔(单位：毫秒 ms)
     * @param listener 回调监听器
     * @param register true表示注册; false表示注销
     */
    public abstract void registerPlayerCurrentSyncTimestampListener(final long interval, NELivePlayer.OnCurrentSyncTimestampListener listener, boolean register);

    /**
     * 注册播放过程的实时内容时间戳回调
     * 在start接口后调用
     * @param listener 回调监听器
     * @param register true表示注册; false表示注销
     */
    public abstract void registerPlayerCurrentSyncContentListener(NELivePlayer.OnCurrentSyncContentListener listener, boolean register);

    /**
     * 异步初始化并自动播放
     */
    public abstract void start();

    /**
     * 安装要渲染拉流画面的SurfaceView或者TextureView
     * 可以在播放器启动后按需安装，也可以在播放器启动前安装好。
     *
     * @param renderView SurfaceView/TextureView
     * @param videoScaleMode 视频缩放策略，默认是等比例拉伸。
     */
    public abstract void setupRenderView(IRenderView renderView, VideoScaleMode videoScaleMode);

    /**
     * Activity生命周期函数onStop()触发时请务必调用此接口
     * @param isLive 是否是直播
     */
    public abstract void onActivityStop(boolean isLive);

    /**
     * Activity生命周期函数onResume()触发时请务必调用此接口
     * @param isLive 是否是直播
     */
    public abstract void onActivityResume(boolean isLive);

    /**
     * 隐藏渲染View
     */
    public abstract void hideView();

    /**
     * 显示渲染View
     */
    public abstract void showView();

    /**
     * 检测是否正在播放
     * @return 是否正在播放 true: 正在播放 false: 不在播放
     */
    public abstract boolean isPlaying();

    /**
     * 设置静音
     *
     * @param mute true表示静音，false表示取消静音
     */
    public abstract void setMute(boolean mute);

    /**
     * 设置镜像
     * @param isMirror 是否镜像 true: 镜像，false: 不镜像
     */
    public abstract void setMirror(boolean isMirror);

    /**
     * 设置音量(0.0 ~ 1.0, 0.0为静音，1.0为最大)
     * @param volume
     */
    public abstract void setVolume(float volume);

    /**
     * 销毁播放器，这里不会清空观察者。
     * 销毁后，如果需要重新使用，请调用asyncInit方法重新初始化。
     */
    public abstract void stop();

    /**
     * 获取当前播放器的状态信息
     *
     * @return 播放器当前的状态信息：状态{@link STATE}、导致该状态的原因 {@link com.netease.neliveplayer.playerkit.sdk.constant.CauseCode}
     */
    public abstract StateInfo getCurrentState();

    /**
     * 获取当前播放位置的时间点 单位: ms, 需要在收到onPrepare的通知后调用
     * @return 当前播放位置的时间点 -1: 失败
     */
    public abstract long getCurrentPosition();

    /**
     * 获取当前直播拉流的实时同步的时间戳
     *
     * @return 当前相对直播推流开始的时间戳
     */
    public abstract long getCurrentSyncTimestamp();

    /**
     * 截图，仅在软件解码条件下支持，硬件解码不支持，需要在收到onPrepared的通知后调用
     *
     * @return 如果截图失败则返回 null，截图成功返回位图对象
     */
    public abstract Bitmap getSnapshot();


    /**
     * 播放过程中切换清晰度
     * @param definition 清晰度类型
     * @param callback 切换回调
     * @return
     */
    public abstract void switchDefinition(NEDefinitionData.DefinitionType definition, NECallback<Void> callback);

    /**
     * 切换清晰度
     *
     * @param definition 清晰度类型
     * @return 是否需要切换，如果类型相同或者切换的地址相同，那么这里返回 false
     */
    public abstract boolean switchDefinition(NEDefinitionData.DefinitionType definition);

    /**
     * 播放过程中切换播放地址，第一次播放不能调用该接口，仅支持当前播放结束切换到下一个视频，或者播放过程中切换下一个视频
     * 该接口通过调用播放器SDK的接口实现
     * @param url 播放地址
     */
    public abstract void switchContentUrl(String url);

    /**
     * 切换播放地址
     * 该接口通过调用播放器的重置和重新创建进行拉流实现
     *
     * @param path 待播放的地址
     */
    public abstract void switchContentPath(String path);
}
