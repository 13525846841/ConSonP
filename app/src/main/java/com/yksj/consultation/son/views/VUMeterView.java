package com.yksj.consultation.son.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.yksj.healthtalk.media.ArmMediaRecord;
import com.yksj.healthtalk.media.ArmMediaRecord.MediaState;
import com.yksj.consultation.son.R;

public class VUMeterView extends View{
	final int INTERVAL_TIME = 70;//刷新间隔时间
//	Bitmap  mPointerBitmap;
//	final Matrix matrix = new Matrix();
	private final Rect bgDrawableRect = new Rect();
	private final RectF mCancelRect = new RectF();//录音删除
	private final RectF mFrameRect = new RectF();//录音音量
	final List<Drawable> mFrameList = new ArrayList<Drawable>();
	
	Drawable bgDrawable;
	Drawable mCancelDrawable;
	Drawable mFrameDrawable0;
	
	final PaintFlagsDrawFilter paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
	ArmMediaRecord mArmMediaRecord;
	//Paint mTextPaint;
	float mCurrentAmplitude;//当前的音量幅度
	
	/*float maxAngle = 150;//旋转最大角度
	float minAngle = 10;//初始角度
	float mCurrentAngle;//当前的角度值
*/	
	public VUMeterView(Context context) {
		super(context);
		init(context);
	}

	public VUMeterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public void setMediaRecord(ArmMediaRecord mediaRecorder){
		this.mArmMediaRecord = mediaRecorder;
		invalidate();
	}
	
	private int getMaxAmplitude(){
		if(mArmMediaRecord != null){
			return mArmMediaRecord.getMaxAmplitude();
		}
		return 0;
	}
	
	private String getRecordDuration(){
		if(mArmMediaRecord != null){
			return mArmMediaRecord.getRecordDuration();
		}
		return "00:00";
	}
	
	private void init(Context context){
/*		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setAntiAlias(true);
		mTextPaint.setColor(Color.BLACK);
		mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
		mTextPaint.setTextSize(35);*/
		final Resources resources = context.getResources();
		
		bgDrawable = resources.getDrawable(R.drawable.record_bg);
		mCancelDrawable = resources.getDrawable(R.drawable.record_cancel);
		
		mFrameList.add(mFrameDrawable0 = resources.getDrawable(R.drawable.record_frame0));
		mFrameList.add(resources.getDrawable(R.drawable.record_frame2));
		mFrameList.add(resources.getDrawable(R.drawable.record_frame4));
		mFrameList.add(resources.getDrawable(R.drawable.record_frame5));
		mFrameList.add(resources.getDrawable(R.drawable.record_frame6));
		mFrameList.add(resources.getDrawable(R.drawable.record_frame7));
		mFrameList.add(resources.getDrawable(R.drawable.record_frame8));
		mFrameList.add(resources.getDrawable(R.drawable.record_frame9));
		mFrameList.add(resources.getDrawable(R.drawable.record_frame10));
		mFrameList.add(resources.getDrawable(R.drawable.record_frame11));
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(bgDrawable.getIntrinsicWidth(), bgDrawable.getIntrinsicWidth());
		
		bgDrawableRect.set(
				0,
				0,
				bgDrawable.getIntrinsicWidth(), 
				bgDrawable.getIntrinsicHeight()
				);
		bgDrawable.setBounds(bgDrawableRect);
		
		int width  = mCancelDrawable.getIntrinsicHeight();
		int heigth = mCancelDrawable.getIntrinsicWidth();
		
		mCancelRect.set(
				0,
				0,
				(bgDrawableRect.right-width)/2, 
				(bgDrawableRect.bottom-heigth)/2);
		
		width  = mFrameDrawable0.getIntrinsicWidth();
		heigth = mFrameDrawable0.getIntrinsicHeight();
		mFrameRect.set(0,0, 
				(bgDrawableRect.right-width)/2, 
				(bgDrawableRect.bottom-heigth)/2);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		bgDrawable.draw(canvas);
		//退出
		if(mArmMediaRecord != null && 
				mArmMediaRecord.getRecordState() == MediaState.STATE_CANCEL){
			canvas.setDrawFilter(paintFlagsDrawFilter);
			canvas.drawBitmap(((BitmapDrawable)(mCancelDrawable)).getBitmap(),
					mCancelRect.right,
					mCancelRect.bottom, 
					null);
		}else{
			//音量
			float amplitude = getMaxAmplitude()/32768f;
			if(amplitude  > mCurrentAmplitude){
				mCurrentAmplitude = amplitude;
			}else{
				mCurrentAmplitude = Math.max(amplitude,(mCurrentAmplitude - 0.15f));
			}
			mCurrentAmplitude = Math.min(0.9f,mCurrentAmplitude);
			int frame = (int)(mCurrentAmplitude * 10);
			canvas.setDrawFilter(paintFlagsDrawFilter);
			canvas.drawBitmap(((BitmapDrawable)(mFrameList.get(frame))).getBitmap(),
					mFrameRect.right,
					mFrameRect.bottom, 
					null);
		}
		if(mArmMediaRecord != null && 
				(mArmMediaRecord.getRecordState() == MediaState.STATE_START || mArmMediaRecord.getRecordState() == MediaState.STATE_CANCEL))
			postInvalidateDelayed(INTERVAL_TIME);
		
//		int amplitude = (getMaxAmplitude()/32768)*10;
//		int w = getMeasuredWidth();
//		int h = getMeasuredHeight();
//		canvas.drawRect(new Rect(0,0,0,0), paint);
		
//		canvas.setDrawFilter(paintFlagsDrawFilter);
//		canvas.drawBitmap(((BitmapDrawable)(mFrameDrawable0)).getBitmap(),mFrameRect.right,mFrameRect.bottom, null);
		
//		canvas.save();
//		canvas.setDrawFilter(paintFlagsDrawFilter);
//		Paint paint = new Paint();
//		canvas.drawBitmap(((BitmapDrawable)(mCancelDrawable)).getBitmap(),null,new RectF(0, 0, 0, 0), paint);
//		canvas.restore();
		
		//canvas.drawBitmap(((BitmapDrawable)(mCancelDrawable)).getBitmap(), mCancelRect, null, null);
		//canvas.drawBitmap(((BitmapDrawable)(mCancelDrawable)).getBitmap(),bgDrawableRect.right/2, bgDrawableRect.bottom/2, null);
/*		
		canvas.save();
		canvas.setDrawFilter(paintFlagsDrawFilter);
		canvas.drawBitmap(((BitmapDrawable)(mCancelDrawable)).getBitmap(), 0f,0f, null);
		canvas.restore();*/
		
		//mCancelDrawable.draw(canvas);
		//mCancelDrawable.draw(canvas);
		
/*		
  		canvas.setDrawFilter(paintFlagsDrawFilter);
		float wP = getWidth()/2; 
		float hP = getHeight()/2;
		
		float angle  = minAngle;
		int amplitude = 0;
		if((amplitude = getMaxAmplitude()) != 0){
			angle += amplitude/32768f*(maxAngle-minAngle);
		}
		if(angle > mCurrentAngle){
			mCurrentAngle = angle;
		}else{
			mCurrentAngle = Math.max(angle,mCurrentAngle-minAngle);
		}
		mCurrentAngle = Math.min(maxAngle,mCurrentAngle);
		
        canvas.save();
        canvas.rotate(mCurrentAngle, wP, hP);
        canvas.drawBitmap(mPointerBitmap, 0, hP-mPointerBitmap.getHeight(), null);
        canvas.restore();
        canvas.drawText(getRecordDuration(),150f,260f, mTextPaint);
      */
      /*  
		if(mArmMediaRecord != null && mArmMediaRecord.getRecordState() == MediaState.STATE_START)
			postInvalidateDelayed(INTERVAL_TIME);*/
		
	}
}
