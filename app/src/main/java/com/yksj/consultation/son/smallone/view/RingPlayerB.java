package com.yksj.consultation.son.smallone.view;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

import com.yksj.consultation.son.R;

import java.util.concurrent.ConcurrentHashMap;



public class RingPlayerB {
    private static final String TAG = "RingPlayerB";
    //语音说话完毕
    public static final int TALK_END = R.raw.talk_end;


    //金币抽奖
//	public static final int RING_GOLD = R.raw.gold;
    private static SoundPool mSoundPool = null;
    private static AudioManager mAudioManager;
    private static ConcurrentHashMap<Integer, Integer> soundpoolHashMap = new ConcurrentHashMap<Integer, Integer>();

    private static OnLoadCompleteListener loadCompleteListener = new OnLoadCompleteListener() {
        @Override
        public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
            if (status == 0) soundPoolPlay(sampleId);
        }
    };


    /**
     * 初始化准备
     *
     * @param context
     */
    private static void init(Context context) {
        if (mSoundPool == null) {
            mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            mSoundPool = new SoundPool(5, AudioManager.STREAM_SYSTEM, 0);
            soundpoolHashMap = new ConcurrentHashMap<Integer, Integer>();
            mSoundPool.setOnLoadCompleteListener(loadCompleteListener);

        }
    }

    /**
     * 播放
     *
     * @param soundID
     */
    private static void soundPoolPlay(int soundID) {
        //重置所有的流
        mSoundPool.autoResume();
        int streamValue = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mSoundPool.play(soundID, streamValue, streamValue, 0, 0, 1f);
    }


    /**
     * 播放按键声音
     *
     * @param context
     */
    public static void playPressSound(Context context) {
        init(context);
        if (soundpoolHashMap.containsKey(TALK_END)) {
            soundPoolPlay(soundpoolHashMap.get(TALK_END));
            return;
        }
        int soundId = mSoundPool.load(context, TALK_END, 1);
        soundpoolHashMap.put(TALK_END, soundId);
    }

}
