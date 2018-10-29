package com.yksj.consultation.son;

import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;


public class RingPlayer {
	private static final String TAG = "RingPlayer";
	//弹出面板
	public static final int RING_POPUP_PANEL = R.raw.popup_panel;
	//登陆成功
	public static final int RING_LOGIN_SUCCEED = R.raw.login_succeed;
	//滑轮滚动
	public static final int RING_IDLER_WHEEL = R.raw.idler_wheel;
	//片头音乐
	public static final int RING_BEGIN = R.raw.logo;
	//选择按钮
	public static final int RING_CHOICE_BTN = R.raw.choice_btn;
	//主页泡泡点击声音
	public static final int RING_PAO_PAO_CLICK = R.raw.pao_pao_click;
	
	
	//金币抽奖
	public static final int RING_GOLD = R.raw.gold;
	private static SoundPool mSoundPool = null;
	private static AudioManager mAudioManager;
	private static ConcurrentHashMap<Integer, Integer> soundpoolHashMap  = new ConcurrentHashMap<Integer, Integer>();
	
	private static OnLoadCompleteListener loadCompleteListener = new OnLoadCompleteListener() {
		@Override
		public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
			if(status == 0)soundPoolPlay(sampleId);
		}
	};
	
	/**
	 * 播放按键声音
	 * @param context
	 */
	public static void playPressSound(Context context){
//		if(!SettingManager.getSettingManager(context).isSoundOpen())return;
		init(context);
		if(soundpoolHashMap.containsKey(RING_CHOICE_BTN)){
			soundPoolPlay(soundpoolHashMap.get(RING_CHOICE_BTN));
			return;
		}
		int soundId = mSoundPool.load(context,RING_CHOICE_BTN,1);
		soundpoolHashMap.put(RING_CHOICE_BTN, soundId);
	}
	
	/**
	 * 初始化准备
	 * @param context
	 */
	private static void init(Context context){
		if(mSoundPool == null){
			mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
			mSoundPool = new SoundPool(5, AudioManager.STREAM_SYSTEM,0);
			soundpoolHashMap  = new ConcurrentHashMap<Integer, Integer>();
			mSoundPool.setOnLoadCompleteListener(loadCompleteListener);
			
		}
	}
	
	/**
	 * 播放
	 * @param soundID
	 */
	private static void soundPoolPlay(int soundID){
		//重置所有的流
		mSoundPool.autoResume();
		int streamValue = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(soundID, streamValue, streamValue, 0, 0,1f);
	}
	
}
