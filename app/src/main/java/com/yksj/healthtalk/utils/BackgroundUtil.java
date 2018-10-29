package com.yksj.healthtalk.utils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.view.View;
import android.view.Window;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
/**
 * 根据不同时段不同分辨率计算不同背景图
 * @author zhao
 *
 */
public class BackgroundUtil {
	final HashMap<String,List<int[]>> mHashMap;
	
	//背景图缩放比例
	public static float scale = 0f;
	//title高度
	public static int titleHeight = 0;
	
	public static int MORNING_TIME = 5;//早上
	public static int NOONING_TIME = 9;//中午
	public static int AFTERNOON_TIME = 17;//下午
	public static int NIGHT_TIME = 20;//晚上
	
	//屏幕分辨率
	public static int heightPixels = 0;
	public static int widthPixels = 0;
	
	
	View mView;
	Context mContext;
	//时间定时器
	Timer timer = null;
	
	final Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			setHealthCityBackgroundDrawable(msg.what);
		}
	};
	
	private void setupXYPoint(){
		/**
		 * @param bubbleMedicineLib 医库
		 * @param bubbleNews 新闻
		 * @param bubbleDoctor 诊所
		 * @param bubbleAttention 关注
		 * @param bubbleFamily 家庭
		 * @param bubbleEnvironment环境
		 */
		List<int[]> list = new  ArrayList<int[]>();
		list.add(new int[]{64,459});
		list.add(new int[]{239,385});
		list.add(new int[]{298,500});
		list.add(new int[]{414,375});
		list.add(new int[]{162,307});
		list.add(new int[]{331,292});
		mHashMap.put("480&800",list);
		list = new  ArrayList<int[]>();
		list.add(new int[]{70,552});
		list.add(new int[]{268,463});
		list.add(new int[]{376,576});
		list.add(new int[]{466,451});
		list.add(new int[]{182,370});
		list.add(new int[]{373,351});
		mHashMap.put("540&690",list);
		list = new  ArrayList<int[]>();
		list.add(new int[]{64,459});
		list.add(new int[]{268,463});
		list.add(new int[]{334,504});
		list.add(new int[]{415,400});
		list.add(new int[]{162,328});
		list.add(new int[]{331,311});
		mHashMap.put("480&854",list);
		list = new  ArrayList<int[]>();
		list.add(new int[]{42,274});
		list.add(new int[]{160,232});
		list.add(new int[]{222,283});
		list.add(new int[]{277,225});
		list.add(new int[]{109,185});
		list.add(new int[]{221,176});
		mHashMap.put("320&480",list);
		/**
		 * @param bubbleMedicineLib 医库
		 * @param bubbleNews 新闻
		 * @param bubbleDoctor 诊所
		 * @param bubbleAttention 关注
		 * @param bubbleFamily 家庭
		 * @param bubbleEnvironment环境
		 */
		
	}
	
	public BackgroundUtil(View view,Context context,final Window window){
		mView = view;
		mContext = context;
		mHashMap = new HashMap<String, List<int[]>>();
		setupXYPoint();
	}
	
	public static void measureTitleHeight(Window window){
		Rect rect = new Rect();
		window.getDecorView().getWindowVisibleDisplayFrame(rect);
		titleHeight = rect.top;
    	scale = (heightPixels-titleHeight)/Float.valueOf(heightPixels);
	}
	
	/**
	 * 设置背景图片在onStart中调用,
	 * 在onStop中调用releaseBackground()释放背景资源
	 */
	public void setBackground(){
		Time time = new Time();
		time.setToNow();
		int hour = time.hour;
		//计算时间段
		if(hour >= MORNING_TIME && hour<NOONING_TIME){
			startTimer(NOONING_TIME);
			setHealthCityBackgroundDrawable(MORNING_TIME);
		}else if(hour >= NOONING_TIME && hour < AFTERNOON_TIME){
			startTimer(AFTERNOON_TIME);
			setHealthCityBackgroundDrawable(AFTERNOON_TIME);
		}else if(hour >= AFTERNOON_TIME && hour < NIGHT_TIME){
			startTimer(NIGHT_TIME);
			setHealthCityBackgroundDrawable(AFTERNOON_TIME);
		}else{
			setHealthCityBackgroundDrawable(NIGHT_TIME);
		}
	}
	
	/**
	 * 释放背景资源
	 */
	public void releaseBackground(){
		if(timer != null)timer.cancel();
		timer = null;
		mView.setBackgroundDrawable(null);
	}
	
	private void startTimer(final int hour){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.HOUR,hour);
		if(timer == null)timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Message message = Message.obtain();
				message.what = hour;    
				mHandler.sendMessage(message);
				timer.cancel();
			}
		},calendar.getTime(),1L);
	
	}
	
	/**
	 * 平铺背景图片
	 * @param resourceId
	 * @param resources
	 * @return
	 */
	public static void setTileModeDrawable(int resourceId,Resources resources,ImageView imageView){
		Drawable drawable = resources.getDrawable(resourceId);
		BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
		bitmapDrawable.setTileModeXY(TileMode.REPEAT,TileMode.REPEAT);
		imageView.setBackgroundDrawable(bitmapDrawable);
	}
	
	/**
	 * 
	 * 获得建康城背景图
	 * @param context
	 * @param time 时间段
	 * @return
	 */
	public  void setHealthCityBackgroundDrawable(int time){
		switch(BackgroundUtil.heightPixels){
		case 800:
			if(time == 5){
//				//mView.setBackgroundResource(R.drawable.morning_480_800);
			}else if(time == 9){
//				//mView.setBackgroundResource(R.drawable.nooning_480_800);
			}else if(time == 17){
//				//mView.setBackgroundResource(R.drawable.afternoon_480_800);
			}else if(time == 20){
//				//mView.setBackgroundResource(R.drawable.night_480_800);
			}
			break;
		case 854:
			if(time == 5){
				//mView.setBackgroundResource(R.drawable.morning_480_854);
			}else if(time == 9){
				//mView.setBackgroundResource(R.drawable.nooning_480_854);
			}else if(time == 17){
				//mView.setBackgroundResource(R.drawable.afternoon_480_854);
			}else if(time == 20){
				//mView.setBackgroundResource(R.drawable.night_480_854);
			}
			break;
		case 960:
			if(BackgroundUtil.widthPixels == 640){
				if(time == 5){
					//mView.setBackgroundResource(R.drawable.morning_640_960);
				}else if(time == 9){
					//mView.setBackgroundResource(R.drawable.nooning_640_960);
				}else if(time == 17){
					//mView.setBackgroundResource(R.drawable.afternoon_640_960);
				}else if(time == 20){
					//mView.setBackgroundResource(R.drawable.night_640_960);
				}
			}else if(BackgroundUtil.widthPixels == 540){
				if(time == 5){
					//mView.setBackgroundResource(R.drawable.morning_540_960);
				}else if(time == 9){
					//mView.setBackgroundResource(R.drawable.nooning_540_960);
				}else if(time == 17){
					//mView.setBackgroundResource(R.drawable.afternoon_540_960);
				}else if(time == 20){
					//mView.setBackgroundResource(R.drawable.night_540_960);
				}
			}
			break;
		case 480:
			if(time == 5){
				//mView.setBackgroundResource(R.drawable.morning_320_480);
			}else if(time == 9){
				//mView.setBackgroundResource(R.drawable.nooning_320_480);
			}else if(time == 17){
				//mView.setBackgroundResource(R.drawable.afternoon_320_480);
			}else if(time == 20){
				//mView.setBackgroundResource(R.drawable.night_320_480);
			}
			break;
		case 320:
			
			break;
		default:
			
			break;
		}
	}
	
	private int measureXPoint(int[] point,int width){
		return point[0]-width/2;
	}
	
	private int measureYPoint(int[] point,int height){
		int pointY  = (int)((point[1]-height/2)*scale);
		return pointY;
	}
	
	/**
	 * 
	 * @param bubbleMedicineLib 医库
	 * @param bubbleNews 新闻
	 * @param bubbleDoctor 诊所
	 * @param bubbleAttention 关注
	 * @param bubbleFamily 家园
	 * @param bubbleEnvironment环境
	 */
	public void measurePopImagePoint(ImageView bubbleMedicineLib,ImageView bubbleNews,ImageView bubbleDoctor,ImageView bubbleAttention,ImageView bubbleFamily,ImageView bubbleEnvironment){
		int height = 0;
		int width = 0;
		List<int[]> list = mHashMap.get(widthPixels+"&"+heightPixels);
		int[] point;
		//知识库
		android.widget.AbsoluteLayout.LayoutParams layoutParams = (android.widget.AbsoluteLayout.LayoutParams)bubbleMedicineLib.getLayoutParams();
		height = bubbleMedicineLib.getDrawable().getIntrinsicHeight();
		width  = bubbleMedicineLib.getDrawable().getIntrinsicWidth();
		point  = list.get(0); 
		layoutParams.x = measureXPoint(point,width);
		layoutParams.y = measureYPoint(point,height);
		bubbleMedicineLib.setLayoutParams(layoutParams);
		
		//新闻
		layoutParams = (AbsoluteLayout.LayoutParams)bubbleNews.getLayoutParams();
		height = bubbleNews.getDrawable().getIntrinsicHeight();
		width  = bubbleNews.getDrawable().getIntrinsicWidth();
		point  = list.get(1); 
		layoutParams.x = measureXPoint(point,width);
		layoutParams.y = measureYPoint(point,height);
		bubbleNews.setLayoutParams(layoutParams);
		
		//诊所
		layoutParams = (AbsoluteLayout.LayoutParams)bubbleDoctor.getLayoutParams();
		height = bubbleDoctor.getDrawable().getIntrinsicHeight();
		width  = bubbleDoctor.getDrawable().getIntrinsicWidth();
		point  = list.get(2); 
		layoutParams.x = measureXPoint(point,width);
		layoutParams.y = measureYPoint(point,height);
		bubbleDoctor.setLayoutParams(layoutParams);
		
		layoutParams = (AbsoluteLayout.LayoutParams)bubbleAttention.getLayoutParams();
		height = bubbleAttention.getDrawable().getIntrinsicHeight();
		width  = bubbleAttention.getDrawable().getIntrinsicWidth();
		point  = list.get(3); 
		layoutParams.x = measureXPoint(point,width);
		layoutParams.y = measureYPoint(point,height);
		bubbleAttention.setLayoutParams(layoutParams);
		
		layoutParams = (AbsoluteLayout.LayoutParams)bubbleFamily.getLayoutParams();
		height = bubbleFamily.getDrawable().getIntrinsicHeight();
		width  = bubbleFamily.getDrawable().getIntrinsicWidth();
		point  = list.get(4); 
		layoutParams.x = measureXPoint(point,width);
		layoutParams.y = measureYPoint(point,height);
		bubbleFamily.setLayoutParams(layoutParams);
		
		layoutParams = (AbsoluteLayout.LayoutParams)bubbleEnvironment.getLayoutParams();
		height = bubbleEnvironment.getDrawable().getIntrinsicHeight();
		width  = bubbleEnvironment.getDrawable().getIntrinsicWidth();
		point  = list.get(5); 
		layoutParams.x = measureXPoint(point,width);
		layoutParams.y = measureYPoint(point,height);
		bubbleEnvironment.setLayoutParams(layoutParams);
	}
	public static Drawable getLifeTreeBackgroundDrawable(Context context){
		
		return null;
	}
	
	/**
	 * 释放背景图片
	 * @param imageView
	 */
	public static void releaseBackground(View view){
		Drawable drawable = view.getBackground();
		view.setBackgroundDrawable(null);
		BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
		Bitmap bitmap = bitmapDrawable.getBitmap();
		if(!bitmap.isRecycled()){
			bitmap.recycle();
		}
		bitmap = null;
		bitmapDrawable = null;
		drawable = null;
	}
	/**
	 * 释放imageview图片
	 * @param v
	 */
	public static void releaseImageView(ImageView v){
		BitmapDrawable bitmapDrawable = (BitmapDrawable)v.getDrawable();
		v.setImageDrawable(null);
		if(bitmapDrawable != null){
			Bitmap bitmap = bitmapDrawable.getBitmap();
			if(!bitmap.isRecycled()){
				bitmap.recycle();
			}
			bitmap = null;
		}
		bitmapDrawable = null;
	}
}
