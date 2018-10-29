package com.yksj.healthtalk.media;

import java.io.File;
import java.text.DecimalFormat;

import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Environment;
import android.os.Handler;


public class ArmMediaRecord implements OnErrorListener,OnInfoListener {
	
	public static String TAG = ArmMediaRecord.class.getName();
	
	public static class MediaState {
		
		public static final int STATE_CANCEL = 3;//取消
		public static final int STATE_IDLE = 0;//未开始
		public static final int STATE_START = 2;//录音中
		public static final int STATE_PARE = 1;//准备
		public static final int ERROR_UNKNOWN = -1;//未知错误
		public static final int ERROR_SHORT = -2;//录音时间太短
		
	}
	
	public interface ArmMediaRecordListener{
		void onRecordStateChnage(int state);//状态改变
		void onRecordError(ArmMediaRecord record,int error);
		void onRecordOver(ArmMediaRecord record,File file,String time,long durationTime);//录音结束
	}
	
	final DecimalFormat mDecimalFormat2 = new DecimalFormat("0.00");
	final DecimalFormat mDecimalFormat = new DecimalFormat("00.00");
	
	private int mState = MediaState.STATE_IDLE;//初始状态
	private MediaRecorder mRecorder;
	private long mStartTime;//录音时间
	private ArmMediaRecordListener mRecordListener;
	private File mFile;

	/**
	 * 事件监听
	 */
	final Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case MediaState.STATE_PARE://录音开始准备
			case MediaState.STATE_IDLE://录音停止
			case MediaState.STATE_START://录音启动
				if(mRecordListener != null){
					mRecordListener.onRecordStateChnage(msg.what);
				}
				break;
			case MediaState.ERROR_SHORT://录音太短错误
				onError(msg.what);
				break;
			case MediaState.ERROR_UNKNOWN://未知错误
				onError(msg.what);
				break;
			}
		};
	};
	
	public ArmMediaRecordListener getmRecordListener() {
		return mRecordListener;
	}

	public void setmRecordListener(ArmMediaRecordListener mRecordListener) {
		this.mRecordListener = mRecordListener;
	}
	
	public MediaRecorder getmRecorder() {
		return mRecorder;
	}

	/**
	 * 异步录音线程
	 * @author zhao
	 */
	class RecordThread extends Thread{
		@Override
		public void run() {
			super.run();
			synchronized (ArmMediaRecord.this) {
				try {
					if(getRecordState() != MediaState.STATE_IDLE){
						mRecorder.prepare();
						mHandler.sendEmptyMessage(MediaState.STATE_PARE);//录音准备
						mRecorder.start();
						mHandler.sendEmptyMessage(MediaState.STATE_START);//录音启动
						mStartTime = System.currentTimeMillis();
						mState = MediaState.STATE_START;//改变状态
					}
				} catch (Exception e) {
					e.printStackTrace();
					mHandler.sendEmptyMessage(MediaState.ERROR_UNKNOWN);//录音启动
				}
			}
		}
	}
	
	
	/**
	 * 获得当前录音状态
	 * @return
	 */
	public synchronized int getRecordState(){
		return mState;
	}
	
	/**
	 * 获取时间 00:00
	 * @return
	 */
	public String getRecordDuration(){
		long time = System.currentTimeMillis() - mStartTime;
		return mDecimalFormat.format(time/1000f);
	}
	
	/**
	 * 退出状态
	 * @param b
	 */
	public synchronized void changeCancelState(boolean b){
		if(b){
			this.mState = MediaState.STATE_CANCEL;
		}else{
			this.mState = MediaState.STATE_START;
		}
	}
	
	/**
	 * 
	 * 开始录音
	 * @param file
	 */
	public synchronized void start(File file){
		mStartTime = 0;
		if(!Environment.getExternalStorageState().
				equals(android.os.Environment.MEDIA_MOUNTED)){
			
			return;
		}
		stop();
		mRecorder  = new MediaRecorder();
		mRecorder.setOnErrorListener(this);
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mRecorder.setOutputFile(file.getAbsolutePath());
		
		this.mFile = file;
		this.mState = MediaState.STATE_PARE;
		new RecordThread().start();//启动新线程
	}
	
	/**
	 * 停止录音
	 */
	public synchronized void stop(){
		if(mRecorder == null)return;
		if(mState == MediaState.STATE_PARE){//录音处于准备阶段就停止
			mRecorder.release();
			mRecorder = null;
			onError(MediaState.ERROR_SHORT);//录音时间太短
		}else if(mState == MediaState.STATE_START || mState == MediaState.STATE_CANCEL){//处于正在录音当中停止
			try{
				mRecorder.stop();
				mRecorder.reset();
				mRecorder.release();
				mRecorder = null;
				if(mStartTime==0){
					onError(MediaState.ERROR_SHORT);
					deleteFile();
				}else{
					mStartTime = System.currentTimeMillis() - mStartTime;
				
				//移除未发送的消息
				mHandler.removeMessages(MediaState.STATE_PARE);
				mHandler.removeMessages(MediaState.STATE_START);
				
				if(mStartTime >= 1000L){
					//手动退出发送
					if(mState == MediaState.STATE_CANCEL){
						deleteFile();
					}else{
						if(mRecordListener != null){
							mRecordListener.onRecordOver(this,mFile,mDecimalFormat2.format(mStartTime/1000f),mStartTime);//调用录音完成事件
						}
					}
				}else{//录音时间不够
					onError(MediaState.ERROR_SHORT);
					deleteFile();
				}
				}
			}catch(RuntimeException e){
				e.printStackTrace();
				mRecorder = null;
			}
		}
		mStartTime = 0;
		mState = MediaState.STATE_IDLE;
		if(mRecordListener != null)
			mRecordListener.onRecordStateChnage(MediaState.STATE_IDLE);
	}
	
	/**
	 * 录音错误
	 * @param error
	 */
	private void onError(int error){
		if(mRecordListener != null)mRecordListener.onRecordError(this,error);
		try{
			if(mRecorder == null)return;
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
		}catch(Exception e){
			e.printStackTrace();
			mRecorder = null;
		}
	}
	
	private void deleteFile(){
		if(mFile != null){
			try{
				mFile.deleteOnExit();
			}catch(Exception e){
			}
		}
	}
	
	/**
	 * 获得当前录音的最大振幅
	 */
	public int getMaxAmplitude(){
		if(mRecorder == null)return 0;
		return mRecorder.getMaxAmplitude();
	}

	@Override
	public void onInfo(MediaRecorder mr, int what, int extra) {
		/*switch(what){
		case MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED://录音最大时间
			break;
		case MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED://最大的文件
			break;
		}*/
	}

	@Override
	public void onError(MediaRecorder mr, int what, int extra) {
		onError(MediaState.ERROR_UNKNOWN);
	}
	
}
