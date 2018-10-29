package com.netease.neliveplayer.playerkit.core.player;

import android.content.Context;

import com.netease.neliveplayer.playerkit.common.log.LogUtil;
import com.netease.neliveplayer.playerkit.common.net.ConnectivityWatcher;
import com.netease.neliveplayer.playerkit.common.net.NetworkEnums;
import com.netease.neliveplayer.playerkit.common.net.NetworkUtil;
import com.netease.neliveplayer.playerkit.sdk.LivePlayer;
import com.netease.neliveplayer.playerkit.sdk.constant.CauseCode;
import com.netease.neliveplayer.playerkit.sdk.model.VideoOptions;
import com.netease.neliveplayer.sdk.NEMediaDataSource;


/**
 * @author netease
 * <p>
 * 播放器高级扩展类，在基础扩展类上面封装播放器前后台切换及断网重连策略：
 * <p>
 * 1. APP退后台，不重置播放器，引擎继续保持拉流。
 * 2. 网络断开，立即重置播放器。
 * 3. 在网络恢复、APP回到前台时尝试恢复拉流(重新初始化播放器)。
 * 4. 尝试拉流：必须在网络可用 && APP在前台 && 播放器已经被重置 三个条件都符合时才重新初始化流。
 * 补充：
 * 1. 在后台超过3分钟回到前台就重置播放器
 * 2. 4G切回wifi的时候，必须重置再重新拉流，否则会出现缓冲错误
 */

public class AdvanceLivePlayer extends BaseLivePlayer {

    /// constant
    // 如果app应用层没有使用service进行长时间后台播放，这里可以设置为3 * 60 * 1000，在后台超过3分钟回到前台就重置播放器
    // 如果app应用层使用service进行长时间后台播放，这里可以设置为最长的后台播放时长，比如30 * 60 * 1000，在后台超过30分钟回到前台就重置播放器
    private static final long BACKGROUND_RESET_TIME = 30 * 60 * 1000;

    /// component
    private ConnectivityWatcher connectivityWatcher;

    /// 前后台状态及断网重连
    private boolean foreground = true; // app是否在前台(默认在前台)
    private boolean netAvailable; // 网络是否连通
    private long backgroundTime; // 退到后台的时间点

    AdvanceLivePlayer(Context context, String videoPath, VideoOptions options) {
        super(context, videoPath, options);
    }

    AdvanceLivePlayer(Context context, NEMediaDataSource mediaDataSource, VideoOptions options) {
        super(context, mediaDataSource, options);
    }

    /**
     * ***************************** abstract impl *************************
     */

    @Override
    void onChildInit() {
        // status
        netAvailable = NetworkUtil.isNetAvailable(this.context);

        // observer
        //没有开启循环播放时打开网络监听进行网络重连，如果开启循环播放，播放器SDK内部会进行数据缓冲，所以这里以网络不重连进行示例，开发中可以根据需求进行自行设置
        if (connectivityWatcher == null && options != null && options.loopCount == 0) {
            connectivityWatcher = new ConnectivityWatcher(this.context, connectivityCallback);
            connectivityWatcher.startup();
            LogUtil.info("connectivity watcher startup...");
        }
    }

    @Override
    void onChildDestroy() {
        if (connectivityWatcher != null) {
            connectivityWatcher.shutdown();
            connectivityWatcher = null;
            LogUtil.info("connectivity watcher shutdown");
        }
    }

    /**
     * ***************************** player interface *************************
     */

    @Override
    public void onActivityStop(boolean isLive) {
        super.onActivityStop(isLive);
        LogUtil.app("activity on stop");

        foreground = false; // 切到后台
        backgroundTime = System.currentTimeMillis();
        if (isLive) { //直播
            if (options.hardwareDecode) {
                // 使用硬件解码，直播进入后台停止播放，进入前台重新拉流播放
                LogUtil.info("force reset live player, as app use hardwareDecode! ");
                resetPlayer();
            } else {
                if (options.isPlayLongTimeBackground) {
                    LogUtil.info("no reset live player, as app use softwareDecode and isPlayLongTimeBackground is true! ");
                    //使用软件解码，isPlayLongTimeBackground为true，长时间后台播放,直播进入后台不做处理，继续播放，此时需要APP应用层配合使用service来长时间播放，参考demo

                } else {
                    LogUtil.info("force reset live player, as app use softwareDecode and isPlayLongTimeBackground is false! ");
                    //使用软件解码，isPlayLongTimeBackground为false，后台停止播放,直播进入后台重置
                    resetPlayer();
                }
            }

        } else {  //点播
            if (options.isPlayLongTimeBackground) {
                //使用硬件解码，点播进入后台统一停止播放，进入前台的话重新拉流播放
                if (options.hardwareDecode) {
                    //使用硬件解码，isPlayLongTimeBackground为true，点播进入后台统一停止播放，进入前台的话重新拉流播放
                    LogUtil.info("force reset vod player, as app use hardwareDecode and isPlayLongTimeBackground is true! ");
                    //因为使用硬件解码在后台播放会在某些机器有兼容性问题，
                    // 使用SurfaceView作为显示控件必需打开下面两行代码，不支持进行后台播放，
                    // 使用TextureView作为显示控件建议：
                    // a.建议打开下面两行代码,硬件解码时重置播放器,不进行后台播放;
                    // b.如果不打开下面两行代码,硬件解码时不会重置播放器，而是后台播放，此时需要忍受在某些机器有兼容性问题（因为TextureView中的surface在不同版本的手机上表现不一样）。
//                    savePlayPosition();
//                    resetPlayer();

                } else {
                    LogUtil.info("no reset vod player, as app use softwareDecode and isPlayLongTimeBackground is true! ");
                    //使用软件解码，isPlayLongTimeBackground为true，长时间后台播放,点播进入后台不做处理，继续播放，此时需要APP应用层配合使用service来长时间播放，参考demo

                }
            } else {
                //isPlayLongTimeBackground为false,使用软件编码或者硬件解码，点播进入后台暂停，进入前台恢复播放
                LogUtil.info("pause vod player, as app use softwareDecode or hardwareDecode and isPlayLongTimeBackground is false! ");
                pause();
            }
        }
    }

    @Override
    public void onActivityResume(boolean isLive) {
        super.onActivityResume(isLive);
        LogUtil.app("activity on resume");

        foreground = true; // 回到前台

        if (player == null) {
            return;
        }

        // 考虑需要重置的场景
        if (!hasReset.get()) {
            final LivePlayer.STATE state = getCurrentState().getState();
            if (options.isPlayLongTimeBackground && System.currentTimeMillis() - backgroundTime >= BACKGROUND_RESET_TIME) {
                // 如果在后台时间太长超过了 BACKGROUND_RESET_TIME 的时长且没有重置过，那么立即重置。case: 长时间在后台，超过设置的后台重置时长，在一些极端的情况下播放会停止，但没有收到任何回调，此时回到前台需要重置后重新拉流。
                LogUtil.info("force reset player, as app on background for a long time! ");
                savePlayPosition();
                resetPlayer();
            } else if (state == LivePlayer.STATE.PLAYING && !player.isPlaying()) {
                // 当前状态与播放器底层状态不一致，立即重置。
                LogUtil.info("force reset player, as current state is PLAYING, but player engine is not playing!");
                savePlayPosition();
                resetPlayer();
            }
        }

        // 重新恢复拉流视频
        recoverPlayer(false);
    }

    /**
     * ******************************* 断网重连 ******************************
     */

    private ConnectivityWatcher.Callback connectivityCallback = new ConnectivityWatcher.Callback() {
        @Override
        public void onNetworkEvent(NetworkEnums.Event event) {
            switch (event) {
                case NETWORK_AVAILABLE:
                    onNetworkAvailable();
                    break;
                case NETWORK_UNAVAILABLE:
                    onNetworkUnavailable();
                    break;
                case NETWORK_CHANGE:
                    onNetworkChange();
                    break;
            }
        }
    };

    private void onNetworkAvailable() {
        LogUtil.app("network available!!!");
        netAvailable = true;

        recoverPlayer(true); // 可能需要重启播放器
    }

    private void onNetworkUnavailable() {
        LogUtil.app("network unavailable!!!");
        netAvailable = false;

        // reset
        savePlayPosition();
        resetPlayer();

        // state
        setCurrentState(LivePlayer.STATE.STOPPED, CauseCode.CODE_VIDEO_STOPPED_AS_NET_UNAVAILABLE);
    }

    private void onNetworkChange() {
        if (connectivityWatcher == null) {
            return;
        }

        // wifi -> 4G: 中间会有断网通知 network unavailable -> network changed to MOBILE -> network available，这里会return掉
        // 4G -> wifi: 中间不会收到断网通知，这里必须要reset再重连，否则会出现缓冲错误(-1004)
        if (netAvailable = connectivityWatcher.isAvailable()) {
            LogUtil.app("network type changed to " + connectivityWatcher.getNetworkType() + ", recover video...");
            savePlayPosition();
            resetPlayer();
            recoverPlayer(true);
        }
    }

    /**
     * *********************************** core *******************************
     */

    private void recoverPlayer(boolean netRecovery) {
        if (player == null) {
            return;
        }

        if (!hasReset.get() && getCurrentState().getState() != STATE.PAUSED) {
            return; // 没有重置过播放器并且不是点播暂停状态，这里就不需要恢复了
        }

        // 没有网络或者没有在前台就不需要重新初始化视频了
        if (netRecovery && !foreground) {
            // case 1: 如果APP在后台网络恢复了，那么等回到前台后再重新初始化播放器。如果在前台网络断了恢复后，立即初始化
            LogUtil.app("cancel recover video from net recovery, as app in background!");
            return;
        }

        if (!netRecovery && !netAvailable) {
            // case 2: 如果APP回到前台，发现没有网络，那么不立即初始化，等待网络连通了再初始化
            LogUtil.app("cancel recover video from activity on resume, as network is unavailable!");
            return;
        }

        // 如果播放器已经重置过了，才需要重新初始化。比如退到后台，实际上有Service继续拉流，那么回到前台时，SurfaceView onCreate之后会继续渲染拉流
        LogUtil.app("recover video from " + (netRecovery ? "net available" : "activity on resume" + ", foreground=" + foreground));
        start();
    }
}
