package com.netease.neliveplayer.playerkit.sdk;

import com.netease.neliveplayer.sdk.NELivePlayer;


/**
 * 网易云信视频点播播放器接口
 * 基于播放器SDK封装的点播相关的接口
 * <p>
 * @author netease
 */

public abstract class VodPlayer extends LivePlayer {


    /**
     * [点播专用] 获取当前视频的总长度，需要在收到onPrepared的回调后调用有效。
     *
     * @return 点播视频的总长度(单位ms)
     */
    public abstract long getDuration();

    /**
     * [点播专用] 获取当前播放位置的时间点，需要在收到onPrepared的回调后调用有效。
     *
     * @return 当前播放时间(单位ms)
     */
    public abstract long getCurrentPosition();

    /**
     * [点播专用] 获取当前播放进度，需要在收到onPrepared的回调后调用有效。
     *
     * @return 当前播放进度
     */
    public abstract float getCurrentPositionPercent();

    /**
     * [点播专用] 获取当前已经缓存的视频位置，需要在收到onPrepared的回调后调用有效。
     *
     * @return 当前已经缓存的可以直接播放的视频播放位置(单位ms)
     */
    public abstract long getCachedPosition();

    /**
     * [点播专用] 设置到指定时间点播放，需要在收到onPrepared的回调后调用有效。
     *
     * @param position 指定的播放时间位置
     */
    public abstract void seekTo(long position);

    /**
     * [点播专用] 设置播放速度，默认是1.0，范围是[0.5, 2.0]
     *
     * @param speed 速度，常用 0.5/1.0/1.3/1.5/2.0
     */
    public abstract void setPlaybackSpeed(float speed);

    /**
     * [点播专用] 暂停当前播放，调用 start 恢复播放
     */
    public abstract void pause();

    /**
     * [点播专用] 设置循环播放
     * @param loopCount 0，不循环；-1无限循环；1循环一次，2循环两次，以此类推
     */
    public abstract void setLooping(int loopCount);

    /**
     * [点播专用] 获取是否循环播放
     * @return 是否是循环
     */
    public abstract boolean isLooping();

    /**
     * [点播专用] 设置点播时本地外挂字幕文件
     * 目前只支持SRT格式字幕，SRT文件中的编码只支持UTF-8编码。
     * 设置为 null 关闭字幕。
     * 字幕中的特殊格式需要应用层处理转换为UTF-8编码的SRT格式字幕。
     * @param path 本地外挂字幕文件路径
     */
    public abstract void setSubtitleFile(String path);

    /**
     * [点播专用] 注册一个回调函数，在是否显示外挂字幕时调用
     * 设置外挂字幕路径后才能有回调，参考 {@link VodPlayer#setSubtitleFile}
     * @param listener 是否显示字幕的监听器
     * @param register true表示注册; false表示注销
     */
    public abstract void registerPlayerSubtitleListener(NELivePlayer.OnSubtitleListener listener, boolean register);


    /**
     * 播放过程中使用解密信息切换加密播放地址，第一次播放不能调用该接口，仅支持当前播放结束切换到下一个视频，或者播放过程中切换下一个视频
     * 获取密钥并对密钥做相关的校验,如果校验正确那么自动进行prepareAsync拉流操作，校验结果OnDecryptionListener中进行回调
     * 该接口不能与switchContentUrlWithDecryptionKey、switchContentUrl同时使用
     * @param url 播放地址
     * @param transferToken 获取密钥所需的令牌
     * @param accid 视频云用户创建的其子用户id
     * @param token 视频云用户子用户的token
     * @param appKey 开发者平台分配的AppKey
     */
    public abstract void switchContentUrlWithDecryptionToken(String url, String transferToken, String accid, String token, String appKey);


    /**
     * [点播专用] 播放过程中使用解密密钥切换加密播放地址，第一次播放不能调用该接口，仅支持当前播放结束切换到下一个视频，或者播放过程中切换下一个视频
     * 对密钥做相关的校验,如果校验正确那么自动进行prepareAsync拉流操作，校验结果OnDecryptionListener中进行回调
     * 对于flv、hls加密视频的播放, 在已知密钥的情况下才能使用该方法进行切换加密播放地址。
     * 该接口 不能与switchContentUrlWithDecryptionToken、switchContentUrl同时使用
     * @param url 播放地址
     * @param flvKey 密钥
     * @param flvKeyLen 密钥长度
     */
    public abstract void switchContentUrlWithDecryptionKey(String url, byte[] flvKey, int flvKeyLen);

}
