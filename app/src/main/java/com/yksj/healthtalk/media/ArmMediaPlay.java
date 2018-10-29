package com.yksj.healthtalk.media;

import java.io.File;
import java.lang.ref.WeakReference;

import org.universalimageloader.utils.StorageUtils;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;

import com.yksj.consultation.adapter.ChatAdapter.ViewHolder;
import com.yksj.healthtalk.entity.MessageEntity;
import com.yksj.healthtalk.utils.LogUtil;


public class ArmMediaPlay implements OnErrorListener,OnCompletionListener,OnInfoListener,OnPreparedListener,OnBufferingUpdateListener{
	private static final String TAG = ArmMediaPlay.class.getName();
	private MediaPlayer mMediaPlayer;
	private volatile MessageEntity mMessageEntity;
	private ArmMediaPlayListener mediaPlayListener;
	
	public interface ArmMediaPlayListener{
		void onPlayError();
	}
	
	//改变播放进度条
	final Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(mMediaPlayer != null && mMediaPlayer.isPlaying()){//改变进度条
				sendEmptyMessageDelayed(0,500);
				onUpdateProgress();
			}
		};
	};
	
	public ArmMediaPlayListener getMediaPlayListener() {
		return mediaPlayListener;
	}

	public void setMediaPlayListener(ArmMediaPlayListener mediaPlayListener) {
		this.mediaPlayListener = mediaPlayListener;
	}

	/**
	 * 当前播放状态
	 * @return
	 */
	public boolean isPlaying(){
		if(mMediaPlayer == null)return false;
		return mMediaPlayer.isPlaying();
	}
	
	public void play(MessageEntity entity){
		if(mMessageEntity != null && mMessageEntity == entity){//当前正在播放
			stop();
			return;
		}
		stop();
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setOnCompletionListener(this);
		mMediaPlayer.setOnErrorListener(this);
		mMediaPlayer.setOnInfoListener(this);
		mMediaPlayer.setOnPreparedListener(this);
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			File file = new File(StorageUtils.getVoicePath(),getFileName(entity.getContent()));
			mMediaPlayer.setDataSource(file.getAbsolutePath());
			mMediaPlayer.prepareAsync();
			this.mMessageEntity = entity;
		} catch (Exception e) {
			e.printStackTrace();
			mMessageEntity = null;
		}
	}
	
	/**
	 * 获得文件名
	 * @param path
	 * @return
	 */
	private String getFileName(String path){
		int separatorIndex = path.lastIndexOf("/");
		path = (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
		return path;
	}
	
	/**
	 * 停止播放
	 */
	public void stop(){
		if(mMediaPlayer == null){
			mMessageEntity = null;
			return;
		}
		try{
			if(mMediaPlayer.isPlaying()){
				mMediaPlayer.stop();
			}
			mMediaPlayer.release();
			mMediaPlayer = null;
			onEndUpdateProgress();
			mMessageEntity = null;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 播放结束
	 */
	private void onEndUpdateProgress(){
		if(mMessageEntity != null){
			mMessageEntity.playProgres = 0;
			final WeakReference<ViewHolder> reference = mMessageEntity.viewHolder;
			if(reference != null){
				ViewHolder holder = reference.get();
				if(holder != null && holder.playPbV != null){
					holder.playPbV.setProgress(mMessageEntity.playProgres);
				}
			}
		}
	}
	
	/**
	 * 改变进度条
	 * @param progres
	 */
	private void onUpdateProgress(){
		final WeakReference<ViewHolder> reference = mMessageEntity.viewHolder;
		if(reference != null){
			ViewHolder holder = reference.get();
			if(holder != null && holder.playPbV != null){
				int currentPosition = mMediaPlayer.getCurrentPosition();
				float length = mMediaPlayer.getDuration();
				int progress = (int)((currentPosition/length)*100);
				progress = 100 - progress;
				mMessageEntity.playProgres  = progress;
				holder.playPbV.setProgress(progress);
			}
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {//准备完成
		try{
			mp.start();
			mHandler.sendEmptyMessage(0);
		}catch(Exception e){
			e.printStackTrace();
			mMessageEntity = null;
		}
	}
	

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {//播放完成
		stop();
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {//播放错误
		stop();
		if(mediaPlayListener != null)mediaPlayListener.onPlayError();
		return false;
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		LogUtil.d(TAG,"onBufferingUpdate"+percent);
	}
	
}
