package com.yksj.healthtalk.utils;

import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

import com.yksj.consultation.son.R;

/**
 * @author zhao yuan
 *
 */
public class RingPlayer{
	private static final String TAG = "RingPlayer";
	
	//警告音
//	public static final int RING_DIALOG = R.raw.dialog;
	//弹出面板
	public static final int RING_POPUP_PANEL = R.raw.popup_panel;
	//登陆成功
	public static final int RING_LOGIN_SUCCEED = R.raw.login_succeed;
/*	//快速移动
	public static final int RING_TOUCH_MOVE = R.raw.touch_move;
	//关门
	public static final int RING_CLOSE_DOOR = R.raw.close_door;*/
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
	
	
	
	private static AudioManager mAudioManager;
	
	private static SoundPool mSoundPool = null;
	
	private static SoundPool mSound = null;
	
	private static ConcurrentHashMap<Integer, Integer> soundpoolHashMap  = new ConcurrentHashMap<Integer, Integer>();
	/**
	 * 泡泡点击声音
	 */
	public static final int RING_POP = 1;
	
	private static OnLoadCompleteListener loadCompleteListener = new OnLoadCompleteListener() {
		@Override
		public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
			if(status == 0)soundPoolPlay(sampleId);
		}
	};
	
	public static void play(Context context,int ringId){
		//if(!SettingManager.getSettingManager(context).isSoundOpen())return;
		init(context);
		if(soundpoolHashMap.containsKey(ringId)){
			soundPoolPlay(soundpoolHashMap.get(ringId));
			return;
		}
		int soundId = mSoundPool.load(context,ringId,1);
		soundpoolHashMap.put(ringId, soundId);
	}
	
	/**
	 * 播放按键声音
	 * @param context
	 */
	public static void playPressSound(Context context){
		init(context);
		if(soundpoolHashMap.containsKey(RING_CHOICE_BTN)){
			soundPoolPlay(soundpoolHashMap.get(RING_CHOICE_BTN));
			return;
		}
		int soundId = mSoundPool.load(context,RING_CHOICE_BTN,1);
		soundpoolHashMap.put(RING_CHOICE_BTN, soundId);
	}
	
	public static void playGoldSound(Context context){
		init(context);
		if(soundpoolHashMap.containsKey(RING_GOLD)){
			soundPoolPlay(soundpoolHashMap.get(RING_GOLD));
			return;
		}
		int soundId = mSoundPool.load(context,RING_GOLD,1);
		soundpoolHashMap.put(RING_GOLD, soundId);
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
	 * 释放所有内存
	 */
	public static void releaseAll(){
		mSoundPool.release();
		soundpoolHashMap.clear();
		soundpoolHashMap = null;
		mSoundPool = null;
		mAudioManager = null;
	}
	
	/**
	 * 卸载资源
	 * @param ringId
	 */
	public static void unload(int ringId){
		if(soundpoolHashMap.containsKey(ringId)){
			mSoundPool.unload(soundpoolHashMap.get(ringId));
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
