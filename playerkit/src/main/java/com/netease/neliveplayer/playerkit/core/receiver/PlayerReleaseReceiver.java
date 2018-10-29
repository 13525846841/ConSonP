package com.netease.neliveplayer.playerkit.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.netease.neliveplayer.playerkit.common.log.LogUtil;
import com.netease.neliveplayer.playerkit.sdk.PlayerReleaseObserver;
import com.netease.neliveplayer.sdk.NELivePlayer;


public class PlayerReleaseReceiver extends BroadcastReceiver {
    private final static String TAG = PlayerReleaseReceiver.class.getSimpleName();
    private PlayerReleaseObserver observer;

    public PlayerReleaseReceiver(PlayerReleaseObserver observer) {
        this.observer = observer;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //接收播放器资源释放结束消息
        if (intent.getAction().equals(context.getPackageName() + NELivePlayer.NELP_ACTION_RECEIVE_RELEASE_SUCCESS_NOTIFICATION)) {
            LogUtil.info("NELivePlayer RELEASE SUCCESS!");
            observer.onReceiver();
        }
    }
}