package com.netease.neliveplayer.playerkit.sdk;


import com.netease.neliveplayer.playerkit.sdk.model.MediaInfo;
import com.netease.neliveplayer.playerkit.sdk.model.StateInfo;
import com.netease.neliveplayer.sdk.NEDefinitionData;
import com.netease.neliveplayer.sdk.NELivePlayer;

import java.util.List;

/**
 * 直播拉流播放器状态/事件回调函数观察者
 * 基于播放器SDK封装的直播相关的状态/事件回调
 * <p>
 * @author netease
 */

public interface LivePlayerObserver {

    /**
     * [重要]正在初始化准备播放
     * 开发者可以在此时做加载、等待动画
     */
    void onPreparing();

    /**
     * [重要]初始化完成并开始播放
     * 开发者可以在此取消加载、等待动画
     */
    void onPrepared(MediaInfo mediaInfo);

    /**
     * [重要]视频播放器出现错误
     * 开发者可以在此结束播放、给予用户出错提示、开启重新播放按钮等
     *
     * @param code  错误码 {@link com.netease.neliveplayer.sdk.constant.NEErrorType}
     *              如果 code=-9999 表示视频码流解析失败，此时音频播放正常，视频可能无画面，开发者可以针对此错误码添加处理逻辑，例如退出、重新播放。
     * @param extra 错误附加信息
     */
    void onError(int code, int extra);

    /**
     * [重要]视频第一帧显示，标志着视频正在播放
     */
    void onFirstVideoRendered();

    /**
     * 音频第一帧显示
     */
    void onFirstAudioRendered();

    /**
     * 视频开始缓冲
     */
    void onBufferingStart();

    /**
     * 视频缓冲结束
     */
    void onBufferingEnd();

    /**
     * 视频缓冲进度
     */
    void onBuffering(int percent);

    /**
     * 硬件解码开启
     */
    void onHardwareDecoderOpen();

    /**
     * 播放器状态回调
     *
     * @param stateInfo 播放器当前状态及导致状态的原因
     */
    void onStateChanged(StateInfo stateInfo);

    /**
     * 解析到不同的清晰度信息
     *
     * @param data 不同清晰度信息：包括清晰度类型、当前是否正在使用。
     */
    void onParseDefinition(List<NEDefinitionData> data);

    /**
     * 自动切换清晰度的结果
     * 如果使用的是高清晰度的地址播放，而网络状态比较差时，那么会自动切换到清晰度低的地址，切换成功后触发此回调
     *
     * @param definitionType 已切换到的清晰度类型
     */
    void onAutoSwitchDefinition(NEDefinitionData.DefinitionType definitionType);

    /**
     * 视频数据回调
     * @param videoRawData 视频数据结构,包含视频数据以及宽高等参数
     */
    void onVideoFrameFilter(NELivePlayer.NEVideoRawData videoRawData);

    /**
     * 音频数据回调
     * @param audioRawData 音频数据结构,包含音频数据以及采样率、通道数等参数
     */
    void onAudioFrameFilter(NELivePlayer.NEAudioRawData audioRawData);


    /**
     * 拉流http状态信息
     * @param code 状态码
     * @param header 头信息
     *
     */
    void onHttpResponseInfo(int code, String header);

}
