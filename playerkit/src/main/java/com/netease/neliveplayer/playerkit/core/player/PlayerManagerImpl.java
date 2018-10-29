package com.netease.neliveplayer.playerkit.core.player;

import android.content.Context;
import android.content.IntentFilter;

import com.netease.neliveplayer.playerkit.core.receiver.PlayerReleaseReceiver;
import com.netease.neliveplayer.playerkit.sdk.LivePlayer;
import com.netease.neliveplayer.playerkit.sdk.PlayerReleaseObserver;
import com.netease.neliveplayer.playerkit.sdk.VodPlayer;
import com.netease.neliveplayer.playerkit.sdk.model.SDKOptions;
import com.netease.neliveplayer.playerkit.sdk.model.VideoOptions;
import com.netease.neliveplayer.sdk.NELivePlayer;
import com.netease.neliveplayer.sdk.NEMediaDataSource;
import com.netease.neliveplayer.sdk.NESDKConfig;

import java.util.ArrayList;
import java.util.Map;

public class PlayerManagerImpl {
    private static PlayerReleaseReceiver playerReceiver;

    /**
     * 初始化SDK,使用播放器时必须先进行初始化才能进行后续操作。
     *
     * @param context 调用上下文
     * @param options sdk配置信息
     */
    public static void init(Context context, SDKOptions options) {
        NESDKConfig sdkConfig = new NESDKConfig();
        if (options != null) {
            sdkConfig.refreshPreLoadDuration = options.refreshPreLoadDuration;
            sdkConfig.isCloseTimeOutProtect = options.isCloseTimeOutProtect;
            sdkConfig.dynamicLoadingConfig = options.dynamicLoadingConfig;
            sdkConfig.dataUploadListener = options.dataUploadListener;
            sdkConfig.logListener = options.logListener;
            sdkConfig.supportDecodeListener = options.supportDecodeListener;
            sdkConfig.isCloseTimeOutProtect = true;
        }
        NELivePlayer.init(context, sdkConfig);
    }

    /**
     * 获取是否已经准备好so库文件
     * 仅在初始化 init 接口中配置动态加载才能使用该接口查询
     *
     * @return 是否准备好
     */
    public static boolean isDynamicLoadReady() {
        return NELivePlayer.isDynamicLoadReady();
    }

    /**
     * 构造播放器实例对象
     *
     * @param context   上下文
     * @param videoPath 视频资源路径
     * @param options   播放选项
     * @return 播放器实例对象
     */
    public static LivePlayer buildLivePlayer(Context context, String videoPath, VideoOptions options) {
        return new LivePlayerImpl(context, videoPath, options);
    }

    /**
     * 构造播放器实例对象
     *
     * @param context   上下文
     * @param videoPath 视频资源路径
     * @param options   播放选项
     * @return 播放器实例对象
     */
    public static VodPlayer buildVodPlayer(Context context, String videoPath, VideoOptions options) {
        return new LivePlayerImpl(context, videoPath, options);
    }

    /**
     * 构造播放器实例对象
     *
     * @param context   上下文
     * @param mediaDataSource 视频资源,需要实现 NEMediaDataSource 自定义数据源
     * @param options   播放选项
     * @return 播放器实例对象
     */
    public static VodPlayer buildVodPlayer(Context context, NEMediaDataSource mediaDataSource, VideoOptions options) {
        return new LivePlayerImpl(context, mediaDataSource, options);
    }


    /**
     * 添加预加载拉流链接地址
     *
     * @param urls 拉流链接地址
     */
    public static void addPreloadUrls(ArrayList<String> urls) {
        NELivePlayer.addPreloadUrls(urls);
    }

    /**
     * 移除预加载拉流链接地址
     *
     * @param urls 拉流链接地址
     */
    public static void removePreloadUrls(ArrayList<String> urls) {
        NELivePlayer.removePreloadUrls(urls);

    }

    /**
     * 查询预加载拉流链接地址的结果信息
     *
     * @return Map<String,  Integer> String是链接地址，Integer是状态,状态码参考 {@link com.netease.neliveplayer.playerkit.sdk.constant.PreloadStatusType}
     */
    public static Map<String, Integer> queryPreloadUrls() {
        return NELivePlayer.queryPreloadUrls();
    }

    public static void registerPlayerReceiver(Context context, PlayerReleaseObserver observer, boolean register) {
        if (register) {
            if (playerReceiver != null) {
                try {
                    context.unregisterReceiver(playerReceiver);
                }catch (IllegalArgumentException e){
                    //兼容 oppo 手机异常操作

                }
                playerReceiver = null;
            }
            playerReceiver = new PlayerReleaseReceiver(observer);
            context.registerReceiver(playerReceiver, new IntentFilter(context.getPackageName() + NELivePlayer.NELP_ACTION_RECEIVE_RELEASE_SUCCESS_NOTIFICATION));
        } else {
            if (playerReceiver != null) {
                context.unregisterReceiver(playerReceiver);
                playerReceiver = null;
            }
        }
    }

}
