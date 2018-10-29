package com.yksj.consultation.son.chatting;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yksj.consultation.adapter.SymptomAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.data.BoyBoady;
import com.yksj.healthtalk.data.FemaleBoady;
import com.yksj.healthtalk.data.FemaleHeader;
import com.yksj.healthtalk.data.GirlBoady;
import com.yksj.healthtalk.data.IBoady;
import com.yksj.healthtalk.data.ManBoady;
import com.yksj.healthtalk.entity.SymptomEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.ObjectHttpResponseHandler;
import com.yksj.healthtalk.utils.SharePreUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * 
 * 人体图
 * @author zhao
 */
public class HumanBodyActivity extends BaseFragmentActivity implements OnClickListener,AdapterView.OnItemClickListener{
	/**
	 * 当前类型 1人体 2头部
	 */
	int mType = 1;
	int sex = 1;//1man 2women 3girl 4boy 
	/**
	 * 方向 0向前 1向后
	 */
	int mDerition = 0;//
	
	//String mSymptomCode = "";//症状编码
	Bitmap bitmap;//当前图形
//	ManBoady manBoady;//男
//	FemaleBoady femaleBoady;//女
	IBoady mBoady;
	float mDensity;//屏幕密度
	FrameLayout mRootView;//根视图
	
	View mBoadyView;//身体
	View mHeaderView;//头部
	
	ImageView mBoadyImagV;//身体后部分
	ImageView mBoadyLayerImagV;//身体前部分
	
	ImageView mHeaderImagV;//头像后部分
	ImageView mHeaderLayerImagV;//头像前部分
	
	View mSymptomView;//症状列表
	
	ListView mListView;
	SymptomAdapter mSymptomAdapter;
	PopupWindow mPopupWindow;
	TextView mTitleV;//标题
	String codes;
	
	private int[] whSize;//宽,高
	float boadyYScale = 1.0f;//x坐标缩放比例
	
	float boadyXScale = 1.0f;//y坐标缩放比例
	
	float headerScale = 1.0f;//头部缩放比例
	
	List<String> mSymptomCode = new ArrayList<String>();//症状编码
	//记录的选中列表
	LinkedHashMap<String,List<SymptomEntity>> mLinkedHashMap = new LinkedHashMap<String, List<SymptomEntity>>();
	//刚进入,默认选中的
	final LinkedHashMap<String,SymptomEntity> mSelectedMap = new LinkedHashMap<String,SymptomEntity>();
	//图片缓存
	WeakHashMap<Integer,Bitmap> mWeakHashMap = new WeakHashMap<Integer,Bitmap>();
	//当前数据
	JSONArray mJsonArray;
    MyGestureListener listener = new MyGestureListener();
	
    //视图加载完成之后需要计算缩放比例
    Handler mHandler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		switch(msg.what){
    		case 1://身体
    			int width = mBoadyImagV.getWidth();
    			int height = mBoadyImagV.getHeight();
    			BitmapDrawable bitmapDrawable = (BitmapDrawable)mBoadyImagV.getDrawable();
    			int wB = bitmapDrawable.getBitmap().getWidth();
    			int hB = bitmapDrawable.getBitmap().getHeight();
    			if(hB>height){
    				boadyYScale = Float.valueOf(hB)/height;
    			}
    			if(wB > width){
    				boadyXScale = width/getDensity()/wB;
    			}
    			break;
    		}	
    	}
    };
    
    
	class MyGestureListener implements OnGestureListener{
		float xP;
		float yP;
		@Override
		public boolean onDown(MotionEvent e) {
			xP = e.getX();
			yP = e.getY();
			boolean isBoady = true;//isBoadyOnClick((int)xP,(int)yP);
			//当是人体的时候,显示不全需要缩放
			if(mType == 1){
				xP = xP/getDensity();
				yP = yP/getDensity()*boadyYScale;
			//头部	
			}else{
				xP = xP/headerScale;
				yP = yP/headerScale;
			}
			int position = -1;//部位id 没有返回-1
			//女
			if(sex == 2){
				//人体
				if(mType == 1){
					//向前
					if(mDerition == 0){
						position = handlerFront(xP,yP);
					//向后
					}else{
						position = handleBack(xP,yP);
					}
				//头	
				}else{
					position = handlerHeader(xP,yP);
				}
			//男
			}else{
				//人体
				if(mType == 1){
					//向前
					if(mDerition == 0){
						position = handlerFront(xP,yP);
					//向后
					}else{
						position = handleBack(xP,yP);
					}
				//头	
				}else{
					position = handlerHeader(xP,yP);
				}
			}
			//回到身体界面
			if(mType == 2 && position == -1){
				showBoadyView();
				return false;
			}
			return true;
		}
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
		}
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			
		}
		//用户轻触后松开
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			showSymListData(xP, yP);
			return true;
		}
	}
	
	 	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.boady_layout);
	        whSize = getScreenWidth();
	        sex = getIntent().getIntExtra("sex",3);
	        codes = getIntent().getStringExtra("code");
//	        sex = 3;
//	        codes = "3";
	        mSymptomCode.addAll(Arrays.asList(codes.split(",")));
	        mRootView = (FrameLayout)findViewById(R.id.boady_root);
	        if(sex == 1){
	        	 mBoady = new ManBoady();
	        }else if(sex == 2){
	        	mBoady = new FemaleBoady();
	        }else if(sex == 3){
	        	mBoady = new GirlBoady();
	        }else{
	        	mBoady = new BoyBoady();
	        }
	        initUI();
//	        AnimationUtils.startGuiPager(this, getClass().getName());
	    }
	    
	    public void onStop(){
	    	super.onStop();
	    }
	    
	    public void onDestroy(){
	    	super.onDestroy();
	    	if(mPopupWindow != null){
	    		if(mPopupWindow.isShowing())mPopupWindow.dismiss();
	    		mPopupWindow = null;
	    	}
	    	mJsonArray = null;
	    	mLinkedHashMap.clear();
	    	mLinkedHashMap = null;
	    	if(mHeaderImagV != null){
	    		mHeaderImagV.setBackgroundDrawable(null);
		    	mHeaderLayerImagV.setBackgroundDrawable(null);
	    	}
	    	mBoadyImagV.setImageBitmap(null);
	    	mBoadyLayerImagV.setImageBitmap(null);
	    	if(bitmap != null && !bitmap.isRecycled()){
	    		bitmap.recycle();
	    	}
	    	bitmap = null;
	    	mRootView.getBackground().setCallback(null);
	    	mRootView.setBackgroundDrawable(null);
	    	
	    	destroyBitmap();
	    	
	    }
	    
	    public void onBackPressed(){
	    	Intent intent = getIntent();
			intent.putExtra("parame",codes);
			setResult(1002,intent);
			finish();
	    }
	    
	    /**
	     * 销毁bitmap
	     */
	    private void destroyBitmap(){
	    	Set<Integer> set = mWeakHashMap.keySet();
	    	for (Integer integer : set) {
	    		Bitmap bitmap = mWeakHashMap.get(integer);
	    		if(bitmap != null){
	    			if(!bitmap.isRecycled())bitmap.recycle();
	    		}
			}
	    }
	    
	    private void initUI(){
	    	findViewById(R.id.selected_over).setOnClickListener(this);
	    	findViewById(R.id.check_selected).setOnClickListener(this);
	    	showBoadyView();
//	    	Log.d(TAG,"---------------Width:"+bitmap.getWidth()+"-------------"+"Height:"+bitmap.getHeight());
	    	queryDataByHttp();
	        mHandler.sendEmptyMessageDelayed(1,2000l);
	    }
	 
	    
	/**
	 * 显示症状列表
	 */
	private void showSymListData(float xP,float yP){
		//女
		if(sex == 2){
			//人体
			if(mType == 1){
				//向前
				if(mDerition == 0){
					showFrontListData(xP, yP);
				//向后
				}else{
					showBackListData(xP,yP);
				}
			//头	
			}else{
				showHeaderListData(xP,yP);
			}
		//男
		}else{
			//人体
			if(mType == 1){
				//向前
				if(mDerition == 0){
					showFrontListData(xP, yP);
				//向后
				}else{
					showBackListData(xP, yP);
				}
			//头	
			}else{
				showHeaderListData(xP, yP);
			}
		}
	}
	
	
	
	/**
	 * 女背部症状数据
	 */
	private void showBackListData(float x,float y){
		int part = mBoady.isRectCollisionBehindBoady(x, y);
		 switch(part){
		 case FemaleBoady.positionBack://背部
			 showBackData();
			 break;
		 case FemaleBoady.positionWaist://腰部
			 showWaistData();
			 break;
		 case FemaleBoady.positionAss://屁股
			 showAssData();
			 break;
		 case FemaleBoady.positionLimbs://四肢
			 showLimbsData();
			 break;
		 case FemaleBoady.positionHeader://头部
//			 showHeaderView();
			 showBrainData();
			 break;	 
		 }
	}
	
	/**
	 * 女背部
	 */
/*	private void handlerFemaleBack(float x,float y){
		 int part = mBoady.isRectCollisionBehindBoady(x, y);
		 switch(part){
		 case FemaleBoady.positionBack://背部
			 setFrontDrawable(R.drawable.female_behind_back);
			 break;
		 case FemaleBoady.positionWaist://腰部
			 setFrontDrawable(R.drawable.female_behind_waist);
			 break;
		 case FemaleBoady.positionAss://屁股
			 setFrontDrawable(R.drawable.female_behind_ass);
			 break;
		 case FemaleBoady.positionHeader://头部
			 setFrontDrawable(R.drawable.female_behind_header);
			 break;
		 case FemaleBoady.positionLimbs://四肢
			 setFrontDrawable(R.drawable.female_behind_limbs);
			 break;	 
		 }
	}*/
	
	/*
	 * 女前部数据
	 */
	private void showFrontListData(float x,float y){
		int part  = mBoady.isRectCollisionFrontBoady(x, y);
		switch(part){
		case FemaleBoady.positionLimbs://四肢
			showLimbsData();
			break;
		case FemaleBoady.positionChest://胸部 
			showChestData();
			break;
		case FemaleBoady.positionAbdomen://腹部
			showAbdomenData();
			break;
		case FemaleBoady.positionPrivateParts://阴部
			if(mBoady instanceof GirlBoady || mBoady instanceof FemaleBoady){
				showFemalPrivateParts();
			}else if(mBoady instanceof BoyBoady || mBoady instanceof ManBoady){
				showManPrivateParts();
			}
			break;
		case FemaleBoady.positionHeader://头部
			showHeaderView();
			break;
		}
	}
	
	//女头部数据
	private void showHeaderListData(float xP,float yP){
		int part  = mBoady.isRectCollisionHeader(xP, yP);
		switch(part){
		case FemaleHeader.positionBrain://脑
			showBrainData();
			break;
		case FemaleHeader.positionEar://耳朵
			showEarData();
			break;
		case FemaleHeader.positionEye://眼睛
			showEyeData();
			break;
		case FemaleHeader.positionMouth://口
			showMouthData();
			break;
		case FemaleHeader.positionNose://鼻子
			showNoseData();
			break;
		case FemaleHeader.positionThroat://咽喉
			showThroatData();
			break;
		case FemaleHeader.positionNeck://颈部
			showNeckData();
			break;
		case FemaleHeader.positionFace://面部
			showFaceData();
			break;
		}
	}


	/**
	 * 
	 * 男背部症状列表
	 * @param x
	 * @param y
	 */
/*	private void showManBackListData(float x,float y){
		int part = manBoady.isRectCollisionBehindBoady(x, y);
		 switch(part){
		 case FemaleBoady.positionBack://背部
			 showBackData();
			 break;
		 case FemaleBoady.positionWaist://腰部
			 showWaistData();
			 break;
		 case FemaleBoady.positionAss://屁股
			 showAssData();
			 break;
		 case FemaleBoady.positionLimbs://四肢
			 showLimbsData();
			 break;
		 case FemaleBoady.positionHeader://头部
//			 showHeaderView();
			 showBrainData();
			 break;	 
		 }
	}*/
	
	/**
	 * 背部
	 */
	private int handleBack(float x,float y){
		int part = mBoady.isRectCollisionBehindBoady(x, y);
		 switch(part){
		 case FemaleBoady.positionBack://背部
			 if(mBoady instanceof ManBoady){
					setFrontDrawable(R.drawable.man_behind_back);
				}else if(mBoady instanceof FemaleBoady){
					setFrontDrawable(R.drawable.female_behind_back);
				}else if(mBoady instanceof GirlBoady){
					setFrontDrawable(R.drawable.girl_behind_back);
				}else{
					setFrontDrawable(R.drawable.boy_behind_back);
				}
			 break;
		 case FemaleBoady.positionWaist://腰部
			 if(mBoady instanceof ManBoady){
				 setFrontDrawable(R.drawable.man_behind_waist);
			 }else if(mBoady instanceof FemaleBoady){
				 setFrontDrawable(R.drawable.female_behind_waist);
			 }else if(mBoady instanceof GirlBoady){
				 setFrontDrawable(R.drawable.girl_behind_waist);
			 }else{
				 setFrontDrawable(R.drawable.boy_behind_waist);
			 }
			 break;
		 case FemaleBoady.positionAss://屁股
			 if(mBoady instanceof ManBoady){
				 setFrontDrawable(R.drawable.man_behind_ass);
			 }else if(mBoady instanceof FemaleBoady){
				 setFrontDrawable(R.drawable.female_behind_ass);
			 }else if(mBoady instanceof GirlBoady){
				 setFrontDrawable(R.drawable.girl_behind_ass);
			 }else{
				 setFrontDrawable(R.drawable.boy_behind_ass);
			 }
			 break;
		 case FemaleBoady.positionLimbs://四肢
			 if(mBoady instanceof ManBoady){
				 setFrontDrawable(R.drawable.man_behind_limbs);
			 }else if(mBoady instanceof FemaleBoady){
				 setFrontDrawable(R.drawable.female_behind_limbs);
			 }else if(mBoady instanceof GirlBoady){
				 setFrontDrawable(R.drawable.girl_behind_limbs);
			 }else{
				 setFrontDrawable(R.drawable.boy_behind_limbs);
			 }
			 break;	
		 case FemaleBoady.positionHeader://头部
			 if(mBoady instanceof ManBoady){
				 setFrontDrawable(R.drawable.man_behind_header);
			 }else if(mBoady instanceof FemaleBoady){
				 setFrontDrawable(R.drawable.female_behind_header);
			 }else if(mBoady instanceof GirlBoady){
				 setFrontDrawable(R.drawable.girl_behind_header);
			 }else{
				 setFrontDrawable(R.drawable.boy_behind_header);
			 }
			 break;
		 }
		 return part;
	}
	/**
	 * 前部
	 */
	private int handlerFront(float x,float y){
		int part  = mBoady.isRectCollisionFrontBoady(x, y);
		switch(part){
		case FemaleBoady.positionHeader://头部
			if(mBoady instanceof ManBoady){
				setFrontDrawable(R.drawable.man_front_header);
			}else if(mBoady instanceof FemaleBoady){
				setFrontDrawable(R.drawable.female_front_header);
			}else if(mBoady instanceof GirlBoady){
				setFrontDrawable(R.drawable.girl_front_header);
			}else{
				setFrontDrawable(R.drawable.boy_front_header);
			}
			break;
		case FemaleBoady.positionLimbs://四肢
			if(mBoady instanceof ManBoady){
				setFrontDrawable(R.drawable.man_front_limbs);
			}else if(mBoady instanceof FemaleBoady){
				setFrontDrawable(R.drawable.female_front_limbs);
			}else if(mBoady instanceof GirlBoady){
				setFrontDrawable(R.drawable.girl_front_limbs);
			}else{
				setFrontDrawable(R.drawable.boy_front_limbs);
			}
			break;
		case FemaleBoady.positionChest://胸部 
			if(mBoady instanceof ManBoady){
				setFrontDrawable(R.drawable.man_front_chest);
			}else if(mBoady instanceof FemaleBoady){
				setFrontDrawable(R.drawable.female_front_chest);
			}else if(mBoady instanceof GirlBoady){
				setFrontDrawable(R.drawable.girl_front_chest);
			}else{
				setFrontDrawable(R.drawable.boy_front_chest);
			}
			break;
		case FemaleBoady.positionAbdomen://腹部
			if(mBoady instanceof ManBoady){
				setFrontDrawable(R.drawable.man_front_abdomen);
			}else if(mBoady instanceof FemaleBoady){
				setFrontDrawable(R.drawable.female_front_abdomen);
			}else if(mBoady instanceof GirlBoady){
				setFrontDrawable(R.drawable.girlfront_abdomen);
			}else{
				setFrontDrawable(R.drawable.boy_front_abdomen);
			}
			break;
		case FemaleBoady.positionPrivateParts://阴部
			if(mBoady instanceof ManBoady){
				setFrontDrawable(R.drawable.man_front_pravtparts);
			}else if(mBoady instanceof FemaleBoady){
				setFrontDrawable(R.drawable.female_front_pravtparts);
			}else if(mBoady instanceof GirlBoady){
				setFrontDrawable(R.drawable.girl_front_pravtpart);
			}else{
				setFrontDrawable(R.drawable.boy_front_pravtparts);
			}
			break;
		}
		return part;
	}
	/**
	 * 头部区域响应
	 */
	private int handlerHeader(float x,float y){
		int part  = mBoady.isRectCollisionHeader(x, y);
		switch(part){
		case FemaleHeader.positionBrain://脑
			if(mBoady instanceof ManBoady){
				setFrontDrawable(R.drawable.man_brain);
			}else if(mBoady instanceof FemaleBoady){
				setFrontDrawable(R.drawable.female_brain);
			}else if(mBoady instanceof GirlBoady){
				setFrontDrawable(R.drawable.girl_brain);
			}else{
				setFrontDrawable(R.drawable.boy_brain);
			}
			break;
		case FemaleHeader.positionEar://耳朵
			if(mBoady instanceof ManBoady){
				setFrontDrawable(R.drawable.man_ear);
			}else if(mBoady instanceof FemaleBoady){
				setFrontDrawable(R.drawable.female_ear);
			}else if(mBoady instanceof GirlBoady){
				setFrontDrawable(R.drawable.girl_ear);
			}else{
				setFrontDrawable(R.drawable.boy_ear);
			}
			break;
		case FemaleHeader.positionEye://眼睛
			if(mBoady instanceof ManBoady){
				setFrontDrawable(R.drawable.man_eye);
			}else if(mBoady instanceof FemaleBoady){
				setFrontDrawable(R.drawable.female_eye);
			}else if(mBoady instanceof GirlBoady){
				setFrontDrawable(R.drawable.girl_eye);
			}else{
				setFrontDrawable(R.drawable.boy_eye);
			}
			break;
		case FemaleHeader.positionMouth://口
			if(mBoady instanceof ManBoady){
				setFrontDrawable(R.drawable.man_mouth);
			}else if(mBoady instanceof FemaleBoady){
				setFrontDrawable(R.drawable.female_mouth);
			}else if(mBoady instanceof GirlBoady){
				setFrontDrawable(R.drawable.girl_mouth);
			}else{
				setFrontDrawable(R.drawable.boy_mouth);
			}
			break;
		case FemaleHeader.positionNose://鼻子
			if(mBoady instanceof ManBoady){
				setFrontDrawable(R.drawable.man_nose);
			}else if(mBoady instanceof FemaleBoady){
				setFrontDrawable(R.drawable.female_nose);
			}else if(mBoady instanceof GirlBoady){
				setFrontDrawable(R.drawable.girl_nose);
			}else{
				setFrontDrawable(R.drawable.boy_nose);
			}
			break;
		case FemaleHeader.positionThroat://咽喉
			if(mBoady instanceof ManBoady){
				setFrontDrawable(R.drawable.man_throat);
			}else if(mBoady instanceof FemaleBoady){
				setFrontDrawable(R.drawable.female_throat);
			}else if(mBoady instanceof GirlBoady){
				setFrontDrawable(R.drawable.girl_throat);
			}else{
				setFrontDrawable(R.drawable.boy_throat);
			}
			break;
		case FemaleHeader.positionNeck://颈部
			if(mBoady instanceof ManBoady){
				setFrontDrawable(R.drawable.man_neck);
			}else if(mBoady instanceof FemaleBoady){
				setFrontDrawable(R.drawable.female_neck);
			}else if(mBoady instanceof GirlBoady){
				setFrontDrawable(R.drawable.girl_neck);
			}else{
				setFrontDrawable(R.drawable.boy_neck);
			}
			break;
		case FemaleHeader.positionFace://面部
			if(mBoady instanceof ManBoady){
				setFrontDrawable(R.drawable.man_face);
			}else if(mBoady instanceof FemaleBoady){
				setFrontDrawable(R.drawable.female_face);
			}else if(mBoady instanceof GirlBoady){
				setFrontDrawable(R.drawable.girl_face);
			}else{
				setFrontDrawable(R.drawable.boy_face);
			}
			break;
		}
		return part;
	}
	
	//当前选中部分图层
	int curretnResourceId = 0;
	
	/**
	 * 设置前面遮罩图片
	 * @param id
	 */
	private void setFrontDrawable(int id){
		Bitmap drawable = null;
		//不等于当前选择的部位
		drawable = mWeakHashMap.get(id);
		if(drawable == null){
			drawable = ((BitmapDrawable)getResources().getDrawable(id)).getBitmap();
			mWeakHashMap.put(id,drawable);
		}
		
		curretnResourceId = id;
		if(mType == 2){
			if(drawable != null){
				BitmapDrawable bitmapDrawable = new BitmapDrawable(drawable);
				bitmapDrawable.setCallback(null);
				mHeaderLayerImagV.setDrawingCacheEnabled(false);
				mHeaderLayerImagV.setBackgroundDrawable(bitmapDrawable);
			}
			//人体
		}else{
			if(drawable != null){
				mBoadyLayerImagV.setDrawingCacheEnabled(false);
				mBoadyLayerImagV.setImageBitmap(drawable);
			}
		}
		System.gc();
	}
	
	
	/**
	 * 判断是否点击在人体身上
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isBoadyOnClick(int x ,int y){
		if(bitmap.getWidth() < x || bitmap.getHeight() < y)return false;
		int color = bitmap.getPixel(x, y);
		color = Color.alpha(color);
		return color==0?false:true;
	}
	
	/**
	 * 解析虚拟医生传过来的症状编码,找到对应的部位 和名称
	 */
	/*private void parseSymptomCode(){
		if(mSymptomCode != null){
			String[] code = mSymptomCode.split(",");
			int n = 0;//匹配的次数
			if(mJsonArray != null){
				for (int i = 0; i < mJsonArray.length(); i++) {
					try{
						JSONObject jsonObject = mJsonArray.getJSONObject(i);
						String positionName = jsonObject.getString("POSITION_NAME");
						JSONArray jsonArray = jsonObject.getJSONArray("SITUATION");
						for (int j = 0; j < jsonArray.length(); j++) {
							jsonObject = jsonArray.getJSONObject(j);
							String situAtionCode = jsonObject.getString("SITUATION_CODE");
							String name = jsonObject.getString("SITUATION_NAME");
							for(String str:code){
								if(situAtionCode.equals(str)){
									List<SymptomEntity> list = null;
									if(mLinkedHashMap.containsKey(positionName)){
										list = mLinkedHashMap.get(positionName);
										SymptomEntity symptomEntity = new SymptomEntity();
										symptomEntity.setCode(Integer.valueOf(situAtionCode));
										symptomEntity.setName(name);
										symptomEntity.setPositionName(positionName);
										symptomEntity.setSelected(true);
										list.add(symptomEntity);
									}else{
										SymptomEntity symptomEntity = new SymptomEntity();
										symptomEntity.setCode(Integer.valueOf(situAtionCode));
										symptomEntity.setName(name);
										symptomEntity.setPositionName(positionName);
										symptomEntity.setSelected(true);
										list = new ArrayList<SymptomEntity>();
										list.add(symptomEntity);
										mLinkedHashMap.put(positionName,list);
									}
									n++;
									if(code.length >= n)return;
								}
							}
						}
					}catch(JSONException e){
					}
				}
			}
		}
	}*/
    
    List<SymptomEntity> listLimbsData = new ArrayList<SymptomEntity>();
    List<String> tagLimbsList = new ArrayList<String>();
    /**
     * 四肢的数据
     */
    private void showLimbsData(){
    	//1026 1027 1028 1029
    	showSymptomListWondinw(false);
    	mTitleV.setText("肌肉不适");
    	parseJsonBySuperposition("10", listLimbsData,tagLimbsList);
//    	parseJson(new String[]{"1026","1027","1028","1029"},listLimbsData,tagLimbsList);
    	mSymptomAdapter.changeDataSource(listLimbsData,tagLimbsList);
//    	mSymptomAdapter.changeDataSource(listLimbsData,tagLimbsList);
    }
    
    
    List<SymptomEntity> listBackData = new ArrayList<SymptomEntity>();
    List<String> tagBackList = new ArrayList<String>();
    /**
     * 背部
     */
    private void showBackData(){
    	//1011
    	showSymptomListWondinw(false);
    	mTitleV.setText("背部不适");
    	parseJson(new String[]{"1011"},listBackData,tagBackList);
    	mSymptomAdapter.changeDataSource(listBackData,tagBackList);

    }
    
    List<SymptomEntity> listAssData = new ArrayList<SymptomEntity>();
    List<String> tagAssList = new ArrayList<String>();
    /**
     * 臀部和盆腔
     */
    private void showAssData(){
    	//1017  1018
    	showSymptomListWondinw(false);
    	mTitleV.setText("臀部和盆腔");
    	parseJsonBySuperposition("7",listAssData,tagAssList);
//    	parseJson(new String[]{"1017","1018"},listWaistData,tagWaistList);
    	mSymptomAdapter.changeDataSource(listAssData,tagAssList);

    }
    
    List<SymptomEntity> listWaistData = new ArrayList<SymptomEntity>();
    List<String> tagWaistList = new ArrayList<String>();
    /**
     * 腰部
     */
    private void showWaistData(){
    	//1015 1016
    	showSymptomListWondinw(false);
    	mTitleV.setText("腰骶部不适");
    	parseJsonBySuperposition("6",listWaistData,tagWaistList);
//    	parseJson(new String[]{"1015","1016"},listWaistData,tagWaistList);
    	mSymptomAdapter.changeDataSource(listWaistData,tagWaistList);

    }
    
    List<SymptomEntity> listChestData = new ArrayList<SymptomEntity>();
    List<String> tagChestList = new ArrayList<String>();
    /**
     * 胸部
     */
    private void showChestData(){
    	//1010 1009
    	showSymptomListWondinw(false);
    	mTitleV.setText("胸部不适");
    	parseJsonBySuperposition("3",listChestData,tagChestList);
//    	parseJson(new String[]{"1010","1009"},listChestData,tagChestList);
    	mSymptomAdapter.changeDataSource(listChestData,tagChestList);
    }
    
    
    List<SymptomEntity> listAbdomenData = new ArrayList<SymptomEntity>();
    List<String> tagAbdomenList = new ArrayList<String>();
    /**
     * 腹部
     */
    private void showAbdomenData(){
    	//1014 1013 1012
    	showSymptomListWondinw(false);
    	mTitleV.setText("腹部不适");
    	parseJsonBySuperposition("5",listAbdomenData,tagAbdomenList);
//    	parseJson(new String[]{"1014","1013","1012"},listAbdomenData,tagAbdomenList);
    	mSymptomAdapter.changeDataSource(listAbdomenData,tagAbdomenList);
    }
    
    List<SymptomEntity> listManPrivatePartsData = new ArrayList<SymptomEntity>();
    List<String> tagManPrivatePartsList = new ArrayList<String>();
    /**
     * 男性生殖
     */
    private void showManPrivateParts(){
    	//1021 1020 1019
    	showSymptomListWondinw(false);
    	mTitleV.setText("阴部不适");
    	parseJsonBySuperposition("8",listManPrivatePartsData,tagManPrivatePartsList);
//    	parseJson(new String[]{"1021","1020","1019"},listManPrivatePartsData,tagManPrivatePartsList);
    	mSymptomAdapter.changeDataSource(listManPrivatePartsData,tagManPrivatePartsList);
    }
    
    List<SymptomEntity> listFemalPrivatePartsData = new ArrayList<SymptomEntity>();
    List<String> tagFemalPrivatePartsList = new ArrayList<String>();
    /**
     * 女性生殖
     */
    private void showFemalPrivateParts(){
    	//1022 1023 1024 1025
    	showSymptomListWondinw(false);
    	mTitleV.setText("阴部不适");
    	parseJsonBySuperposition("9",listFemalPrivatePartsData,tagFemalPrivatePartsList);
//    	parseJson(new String[]{"1022","1023","1024","1025"},listFemalPrivatePartsData,tagFemalPrivatePartsList);
    	mSymptomAdapter.changeDataSource(listFemalPrivatePartsData,tagFemalPrivatePartsList);
    }
    
    List<SymptomEntity> listEyeData = new ArrayList<SymptomEntity>();
    List<String> tagEyetList = new ArrayList<String>();
    /**
     * 眼部
     */
    private void showEyeData(){
    	//1004
    	showSymptomListWondinw(false);
    	mTitleV.setText("眼睛不适");
    	parseJson(new String[]{"1004"},listEyeData,tagEyetList);
    	mSymptomAdapter.changeDataSource(listEyeData,tagEyetList);

    }
    
    List<SymptomEntity> listEarData = new ArrayList<SymptomEntity>();
    List<String> tagEartList = new ArrayList<String>();
    /**
     * 耳朵
     */
    private void showEarData(){
    	//1006
    	showSymptomListWondinw(false);
    	mTitleV.setText("耳部不适");
    	parseJson(new String[]{"1003"},listEarData,tagEartList);
    	mSymptomAdapter.changeDataSource(listEarData,tagEartList);
    }
    
    List<SymptomEntity> listMouthData = new ArrayList<SymptomEntity>();
    List<String> tagMouthtList = new ArrayList<String>();
    /**
     * 嘴巴
     */
    private void showMouthData(){
    	//1006
    	showSymptomListWondinw(false);
    	mTitleV.setText("口腔不适");
    	parseJson(new String[]{"1006"},listMouthData,tagMouthtList);
    	mSymptomAdapter.changeDataSource(listMouthData,tagMouthtList);

    }
    
    List<SymptomEntity> listNoseData = new ArrayList<SymptomEntity>();
    List<String> tagNosetList = new ArrayList<String>();
    /**
     * 鼻子
     */
    private void showNoseData(){
    	//1005
    	showSymptomListWondinw(false);
    	mTitleV.setText("鼻部不适");
    	parseJson(new String[]{"1005"},listNoseData,tagNosetList);
    	mSymptomAdapter.changeDataSource(listNoseData,tagNosetList);

    }
    
    List<SymptomEntity> listBrainData = new ArrayList<SymptomEntity>();
    List<String> tagBraintList = new ArrayList<String>();
    /**
     * 脑部
     */
    private void showBrainData(){
    	showSymptomListWondinw(false);
    	mTitleV.setText("头部不适");
    	parseJson(new String[]{"1001"},listBrainData,tagBraintList);
    	mSymptomAdapter.changeDataSource(listBrainData,tagBraintList);

    }
    
    
    List<SymptomEntity> listThroatData = new ArrayList<SymptomEntity>();
    List<String> tagThroatList = new ArrayList<String>();
    /**
     *咽喉 
     */
    private void showThroatData(){
    	showSymptomListWondinw(false);
    	mTitleV.setText("咽喉部不适");
    	parseJson(new String[]{"1007"},listThroatData,tagThroatList);
    	mSymptomAdapter.changeDataSource(listThroatData,tagThroatList);
    }
    
    List<SymptomEntity> listNeckData = new ArrayList<SymptomEntity>();
    List<String> tagNeckList = new ArrayList<String>();
    /**
     * 颈部
     */
    private void showNeckData(){
    	showSymptomListWondinw(false);
    	mTitleV.setText("颈部不适");
    	parseJson(new String[]{"1008"},listNeckData,tagNeckList);
    	mSymptomAdapter.changeDataSource(listNeckData,tagNeckList);
    }
    
    
    List<SymptomEntity> listFaceData = new ArrayList<SymptomEntity>();
    List<String> tagFaceList = new ArrayList<String>();
    /**
     * 面部
     */
    private void showFaceData(){
    	showSymptomListWondinw(false);
    	mTitleV.setText("面部不适");
    	parseJson(new String[]{"1002"},listFaceData,tagFaceList);
    	mSymptomAdapter.changeDataSource(listFaceData,tagFaceList);
    }
    
    List<SymptomEntity> listWholeBoadyData = new ArrayList<SymptomEntity>();
    List<String> tagWholeBoadyList = new ArrayList<String>();
    /**
     * 显示全身
     */
    private void showWholeBoadyData(){
    	showSymptomListWondinw(false);
    	mTitleV.setText("全身");
    	parseJsonBySuperposition("11",listWholeBoadyData,tagWholeBoadyList);
    	mSymptomAdapter.changeDataSource(listWholeBoadyData,tagWholeBoadyList);
    }
    
    List<SymptomEntity> listSkinData = new ArrayList<SymptomEntity>();
    List<String> tagSkinList = new ArrayList<String>();
    /**
     * 显示皮肤
     */
    private void showSkinData(){
    	showSymptomListWondinw(false);
    	mTitleV.setText("皮肤");
    	parseJsonBySuperposition("12",listSkinData,tagSkinList);
    	mSymptomAdapter.changeDataSource(listSkinData,tagSkinList);
    }
    
    GestureDetector gestureDetector;
    
    /**
     * 显示身体
     */
    private void showBoadyView(){
    	if(mBoadyView == null){
    		gestureDetector = new GestureDetector(this, listener);
    		mBoadyView = getLayoutInflater().inflate(R.layout.boady_view,null);
    		mBoadyImagV = (ImageView)mBoadyView.findViewById(R.id.boady);
    		mBoadyLayerImagV = (ImageView)mBoadyView.findViewById(R.id.boady_front_layer);
    		mBoadyImagV.setOnTouchListener(new View.OnTouchListener() {
    				@Override
    				public boolean onTouch(View v, MotionEvent event) {
    					if(event.getAction() == MotionEvent.ACTION_UP){
    						mBoadyLayerImagV.setImageBitmap(null);
    					}
    					if(gestureDetector.onTouchEvent(event)){
    						return true;
    					}
    					return true;
    				}
    			});
    		TextView textView = (TextView)mBoadyView.findViewById(R.id.ok);
    		textView.setOnClickListener(this);
    		
    		textView = (TextView)mBoadyView.findViewById(R.id.give_up1);
    		textView.setOnClickListener(this);
    		
    		mBoadyView.findViewById(R.id.derition).setOnClickListener(this);
    		mBoadyView.findViewById(R.id.whole_body).setOnClickListener(this);
    		mBoadyView.findViewById(R.id.skin).setOnClickListener(this);
    	}
    	onRecycledBitmap();
    	Drawable drawable;
    	//男
    	if(sex == 1){
    		drawable = getResources().getDrawable(R.drawable.man_front);
    	}else if(sex == 2){
    		drawable = getResources().getDrawable(R.drawable.female_front);
    	}else if(sex == 3){
    		drawable = getResources().getDrawable(R.drawable.girl_front);
    	}else{
    		drawable = getResources().getDrawable(R.drawable.boy_front);
    	}
    	bitmap = ((BitmapDrawable)drawable).getBitmap();
    	mBoadyImagV.setImageBitmap(bitmap);
    	if(mHeaderView != null){
    		mHeaderView.setVisibility(View.GONE);
    		mBoadyView.setVisibility(View.VISIBLE);
    		mType = 1;
    		return;
    	}
    	
    	mRootView.addView(mBoadyView,0);
    	mType = 1;
    }
    
    private void onRecycledBitmap(){
      	if(bitmap != null && !bitmap.isRecycled()){
    		bitmap.recycle();
    	}
    	bitmap = null;
    }
    
    
    /**
     * 显示头部
     */
    private void showHeaderView(){
    	if(mHeaderView == null){
    		final GestureDetector gestureDetector1 = new GestureDetector(this, listener);
    		mHeaderView = getLayoutInflater().inflate(R.layout.header_view,null);
    		
    		TextView textView = (TextView)mHeaderView.findViewById(R.id.give_up2);
    		textView.setOnClickListener(this);
    		textView = (TextView)mHeaderView.findViewById(R.id.complete2);
    		textView.setOnClickListener(this);
    		mHeaderView.findViewById(R.id.whole_body).setOnClickListener(this);
    		mHeaderView.findViewById(R.id.skin).setOnClickListener(this);
    		mHeaderImagV = (ImageView)mHeaderView.findViewById(R.id.header);
//    		Log.d(TAG, "头部大小 getIntrinsicHeight"+drawable.getIntrinsicHeight()+"---"+drawable.getIntrinsicWidth());
    		Options options = new BitmapFactory.Options();
    		options.inJustDecodeBounds = true;
    		BitmapFactory.decodeResource(getResources(), R.drawable.female_header, options);
    		int h = options.outHeight;
    		int w = options.outWidth;
    		headerScale = whSize[0]/Float.valueOf(w);
    		ViewGroup.LayoutParams layoutParams = mHeaderImagV.getLayoutParams();
    		layoutParams.width = whSize[0];
    		layoutParams.height =(int)(h*headerScale);
    		mHeaderImagV.setLayoutParams(layoutParams);
    		
    		mHeaderLayerImagV = (ImageView)mHeaderView.findViewById(R.id.header_front_layer);
    		mHeaderLayerImagV.setLayoutParams(layoutParams);
    		mHeaderImagV.setOnTouchListener(new View.OnTouchListener() {
    				@Override
    				public boolean onTouch(View v, MotionEvent event) {
    					if(event.getAction() == MotionEvent.ACTION_UP){
    						mHeaderLayerImagV.setBackgroundDrawable(null);
    					}
    					if(gestureDetector1.onTouchEvent(event)){
    						return true;
    					}
    					return false;
    				}
    			});
    	}
    	mHeaderImagV.setImageBitmap(null);
    	onRecycledBitmap();
    	Drawable drawable = null;
		if(sex == 1){
    		drawable = getResources().getDrawable(R.drawable.man_header);
    	}else if(sex == 2){
    		drawable = getResources().getDrawable(R.drawable.female_header);
    	}else if(sex == 3){
    		drawable = getResources().getDrawable(R.drawable.girl_header);
    	}else{
    		drawable = getResources().getDrawable(R.drawable.boy_header);
    	}
		bitmap = ((BitmapDrawable)drawable).getBitmap();
		mHeaderImagV.setImageBitmap(bitmap);
    	if(mRootView.getChildCount() >= 3){
    		mBoadyView.setVisibility(View.GONE);
    		mHeaderView.setVisibility(View.VISIBLE);
    		mType = 2;
    		return;
    	}
    	
		mBoadyView.setVisibility(View.GONE);
		mRootView.addView(mHeaderView,0);
    	mType = 2;
//    	getScreenWidth();
    }
    
    
    
    /**
     * 获取屏幕密度
     */
    public float getDensity(){
    	DisplayMetrics metrics = new DisplayMetrics();
    	getWindowManager().getDefaultDisplay().getMetrics(metrics);
/*    	Log.d(TAG, "metrics.widthPixels:"+metrics.widthPixels+"metrics.heightPixels:"+metrics.heightPixels);
    	Log.d(TAG, "密度："+metrics.density);
    	Log.d(TAG, "密度DPI"+metrics.densityDpi);*/
    	return metrics.density;
    }
    
    /**
     * 获取屏幕宽度
     */
    private int[] getScreenWidth(){
    	DisplayMetrics metrics = new DisplayMetrics();
    	getWindowManager().getDefaultDisplay().getMetrics(metrics);
    	int hPix = metrics.heightPixels;
    	int wPix = metrics.widthPixels;
//    	Log.d(TAG,"hPix="+hPix+"----------"+"wPix="+wPix);
    	return new int[]{wPix,hPix};
    }
    
    
    View completeLayoutV;//已选择列表,准备提交按钮
    View choiceLayoutV;//选择列表
    /**
     * 是否是已选择列表
     * 显示数据列表
     */
    public void showSymptomListWondinw(boolean isComplete){
    	if(mSymptomView == null){
    		mSymptomView = getLayoutInflater().inflate(R.layout.symptom_list_layout,null);
    		mListView = (ListView)mSymptomView.findViewById(R.id.list);
    		mPopupWindow = new PopupWindow(mSymptomView,mRootView.getWidth()-60,LayoutParams.FILL_PARENT);
    		mPopupWindow.setAnimationStyle(R.style.AnimationPreview1);
    		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
    		mPopupWindow.setFocusable(true);
    		mPopupWindow.setOutsideTouchable(true);
    		mPopupWindow.showAtLocation(mSymptomView,Gravity.LEFT,60,0);
    		mListView.setOnItemClickListener(this);
    		
    		
    		//symptom_choice_layout
    		completeLayoutV = mSymptomView.findViewById(R.id.symptom_complete_layout);
    		choiceLayoutV = mSymptomView.findViewById(R.id.symptom_choice_layout);
    		mSymptomView.findViewById(R.id.symptom_giveup).setOnClickListener(this);
    		mSymptomView.findViewById(R.id.symptom_submit).setOnClickListener(this);
    		mSymptomView.findViewById(R.id.symptom_choice_agin).setOnClickListener(this);
    		mSymptomView.findViewById(R.id.symptom_choice_submit).setOnClickListener(this);
    		
    		mTitleV = (TextView)mSymptomView.findViewById(R.id.title_txt);
    		initListData();
    	}
    	if(isComplete){
    		completeLayoutV.setVisibility(View.VISIBLE);
    		choiceLayoutV.setVisibility(View.GONE);
    	}else{
    		completeLayoutV.setVisibility(View.GONE);
    		choiceLayoutV.setVisibility(View.VISIBLE);
    	}
    	mPopupWindow.showAtLocation(mSymptomView,Gravity.LEFT,60,0);
    }
    
    private void initListData(){
    	mSymptomAdapter = new SymptomAdapter(this);
    	mListView.setAdapter(mSymptomAdapter);
    }
    
    private void queryDataByHttp(){
    	String version = SharePreUtils.getSymptomJsonVersion(this);
    	if(version == null){
    		version = "0";
    	}
    	HttpRestClient.doHttpQuerySymptom(version,
    			new  ObjectHttpResponseHandler(this) {
			@Override
			public Object onParseResponse(String content) {
				if(content.equals("0")){
    				mJsonArray = SharePreUtils.getSymptomJsonArray(HumanBodyActivity.this);
    			}else{
    				try{
    					mJsonArray = new JSONArray(content);
        				JSONObject jsonObject = mJsonArray.getJSONObject(mJsonArray.length()-1);
            			String version  = jsonObject.getString("SERVERVERSION");
            			SharePreUtils.updateSymptomVersion(HumanBodyActivity.this, version,content);
        			}catch(Exception e){
        				e.printStackTrace();
        			}
    			}
				onInitSelected();
				return null;
			}
		});
    }
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		//方位
		case R.id.derition:
			changeBoadyDerition();
			mBoadyLayerImagV.setImageDrawable(null);
			break;
		case R.id.ok://确定弹出已选项
//			showSelectedWindow();
			submitHandle();
			break;
		case R.id.give_up1://放弃跳转到对话页面
			onBackPressed();
			break;
		case R.id.symptom_choice_agin://重选
			doChoiceAgain();
			break;
		case R.id.symptom_choice_submit://确定
			closeSelectedWindow();
			break;
		case R.id.symptom_giveup://放弃
			if(mPopupWindow != null){
				mPopupWindow.dismiss();
			}
			break;
		case R.id.symptom_submit://完成并提交
			submitHandle();
			break;
		case R.id.whole_body://全身
			showWholeBoadyData();
			break;
		case R.id.skin://皮肤
			showSkinData();
			break;
		case R.id.complete2://头部继续
			showBoadyView();
			break;
		case R.id.give_up2://头部放弃
			showBoadyView();
			if(mLinkedHashMap.containsKey("头部")){
				List<SymptomEntity> list = mLinkedHashMap.get("1");
				if(list == null)return;
				for (int i = 0; i < list.size(); i++) {
					SymptomEntity symptomEntity = list.get(i);
					symptomEntity.setSelected(false);
				}
				mLinkedHashMap.remove("1");
			}
			break;
		case R.id.selected_over://选完了
			submitHandle();
			break;
		case R.id.check_selected://查看已选
			showSelectedWindow();
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mSymptomAdapter.changeSelectedState(view, position,mLinkedHashMap);
	}
	
	/**
	 * 重选
	 */
	private void doChoiceAgain(){
		List<SymptomEntity> list = mSymptomAdapter.getmSelectedList();
		for (SymptomEntity symptomEntity : list) {
			symptomEntity.setSelected(false);
			mLinkedHashMap.remove(symptomEntity.getPositionName());
		}
		closeSelectedWindow();
	}
	
	private void closeSelectedWindow(){
		mPopupWindow.dismiss();
	}
	
	/**
	 * 显示已经选择了的
	 */
	private void showSelectedWindow(){
		List<SymptomEntity> listData = new ArrayList<SymptomEntity>();
		List<String> tagList = new ArrayList<String>();
		Set<String> set = mLinkedHashMap.keySet();
		Iterator<String> iterator = set.iterator();
		while(iterator.hasNext()){
			String value = iterator.next();
			tagList.add(value);
			//标签头
			SymptomEntity symptomEntity = new SymptomEntity();
			symptomEntity.setName(value);
			listData.add(symptomEntity);
			
			listData.addAll(mLinkedHashMap.get(value));
		}
		showSymptomListWondinw(true);
		mTitleV.setText(getString(R.string.symtptom_selected_note));
		mSymptomAdapter.changeDataSource(listData, tagList);
	}
	
	/**
	 * 提交完成
	 */
	private void submitHandle(){
		StringBuffer stringBuffer = new StringBuffer();
		//去掉重复的code
		Map<Integer,Integer> codeMap = new LinkedHashMap<Integer, Integer>();
		try{
			Set<String> set = mLinkedHashMap.keySet();
			for (String string : set) {
				List<SymptomEntity> list = mLinkedHashMap.get(string);
				if(list == null)continue;
				for (int i = 0; i < list.size(); i++) {
					int code = list.get(i).getCode();
					codeMap.put(code,code);
				}
			}
			Set<Integer> set2 = codeMap.keySet();
			for (Integer integer : set2) {
				stringBuffer.append(integer.intValue());
				stringBuffer.append(",");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		Intent intent = getIntent();
		intent.putExtra("parame",stringBuffer.toString());
		setResult(1002,intent);
		finish();
	}
	
	
	/**
	 * 改变人体方位
	 */
	private void changeBoadyDerition(){
		Bitmap bitmap = null;
		//女
		if(sex == 2 || sex == 3){
			//向前
			if(mDerition == 0){
				mDerition = 1;
				if(sex == 2){
					bitmap = mWeakHashMap.get(R.drawable.female_behind);
				}else{
					bitmap = mWeakHashMap.get(R.drawable.girl_behind);
				}
				if(bitmap == null){
					Drawable drawable = null;
					if(sex == 2){
						drawable = getResources().getDrawable(R.drawable.female_behind);
					}else{
						drawable = getResources().getDrawable(R.drawable.girl_behind);
					}
					bitmap = ((BitmapDrawable)drawable).getBitmap();
					mWeakHashMap.put(R.drawable.female_behind,bitmap);
				}
			}else{
				mDerition = 0;
				if(sex == 2){
					bitmap = mWeakHashMap.get(R.drawable.female_front);
				}else{
					bitmap = mWeakHashMap.get(R.drawable.girl_front);
				}
				if(bitmap == null){
					Drawable drawable = null;
					if(sex == 2){
						drawable = getResources().getDrawable(R.drawable.female_front);
					}else{
						drawable = getResources().getDrawable(R.drawable.girl_front);
					}
					bitmap = ((BitmapDrawable)drawable).getBitmap();
					mWeakHashMap.put(R.drawable.female_front,bitmap);
				}
			}
		}else{
			//向前
			if(mDerition == 0){
				mDerition = 1;
				if(sex == 1){
					bitmap = mWeakHashMap.get(R.drawable.man_behind);
				}else{
					bitmap = mWeakHashMap.get(R.drawable.boy_behind);
				}
				if(bitmap == null){
					Drawable drawable = null;
					if(sex == 1){
						 drawable = getResources().getDrawable(R.drawable.man_behind);
					}else{
						 drawable = getResources().getDrawable(R.drawable.boy_behind);
					}
					bitmap = ((BitmapDrawable)drawable).getBitmap();
					mWeakHashMap.put(R.drawable.man_behind,bitmap);
				}
			}else{
				mDerition = 0;
				if(sex == 1){
					bitmap = mWeakHashMap.get(R.drawable.man_front);
				}else{
					bitmap = mWeakHashMap.get(R.drawable.boy_front);
				}
				if(bitmap == null){
					Drawable drawable = null;
					if(sex == 1){
						 drawable = getResources().getDrawable(R.drawable.man_front);
					}else{
						 drawable = getResources().getDrawable(R.drawable.boy_front);
					}
					bitmap = ((BitmapDrawable)drawable).getBitmap();
					mWeakHashMap.put(R.drawable.man_front,bitmap);
				}
			}
		}
		mBoadyImagV.setImageBitmap(bitmap);
	}

	/**
	 * 查询内容
	 * @param code
	 */
	public void queryContent(int code,String name){
		Intent intent = new Intent(this,SymptomContentActivity.class);
		intent.putExtra("id",code);
		intent.putExtra("title", name);
		startActivityForResult(intent,1000);
	}
	
	private void parseJson(String[] codes,List<SymptomEntity> list,List<String> tagList){
		if(list.size()>0 || mJsonArray == null)return;
		int index = 0;
		try{
			for (int i = 0; i < mJsonArray.length(); i++) {
				if(index >= codes.length)break;
				JSONObject jsonObject = mJsonArray.getJSONObject(i);
				String  positionName = jsonObject.getString("POSITION_NAME");//部位名称
				String  subCode = jsonObject.getString("SUBPOSITION_CODE");
				String  superName  = jsonObject.getString("SUBPOSITION_NAME");
				String  superCode = jsonObject.getString("SUPERPOSITION");//大部位
				if(subCode.equals(codes[index])){
					index++;
					SymptomEntity symptomEntity = new SymptomEntity();
					symptomEntity.setPositionName(positionName);
					symptomEntity.setName(superName);
					list.add(symptomEntity);
					tagList.add(superName);
					
					JSONArray array = jsonObject.getJSONArray("SITUATION");
					for (int j = 0; j < array.length(); j++) {
						//子级内容
						JSONObject object1 =  array.getJSONObject(j);
						int code = object1.getInt("SITUATION_CODE");
						String name = object1.getString("SITUATION_NAME");
						symptomEntity = new SymptomEntity();
						if(mSelectedMap.containsKey(String.valueOf(code))){
							symptomEntity = mSelectedMap.get(String.valueOf(code));
							List<SymptomEntity> listEntities = null;
							if(mLinkedHashMap.containsKey(positionName)){
								listEntities = mLinkedHashMap.get(positionName);
							}else{
								listEntities = new ArrayList<SymptomEntity>();
								mLinkedHashMap.put(positionName, listEntities);
							}
							if(!listEntities.contains(symptomEntity))listEntities.add(symptomEntity);
							mSymptomCode.remove(String.valueOf(code));
						}
						symptomEntity.setName(name);
						symptomEntity.setCode(code);
						symptomEntity.setSuperposition(superCode);
						symptomEntity.setPositionName(positionName);
						list.add(symptomEntity);
					}
				}
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 解析已经选择的数据
	 */
	private void onInitSelected(){
		if(mJsonArray == null || mSymptomCode.size() == 0)return;
		try{
			for (int i = 0; i < mJsonArray.length(); i++) {
				JSONObject jsonObject = mJsonArray.getJSONObject(i);
				String  positionName = jsonObject.has("POSITION_NAME")?jsonObject.getString("POSITION_NAME"):"";//部位名称
				//				String  superName  = jsonObject.getString("SUBPOSITION_NAME");
				String  superCode = jsonObject.optString("SUPERPOSITION","");//大部位
				//					SymptomEntity symptomEntity = new SymptomEntity();
				//					symptomEntity.setPositionName(positionName);
				//					symptomEntity.setName(superName);
				JSONArray array = jsonObject.getJSONArray("SITUATION");
				for (int j = 0; j < array.length(); j++) {
					//子级内容
					JSONObject object1 =  array.getJSONObject(j);
					int code = object1.getInt("SITUATION_CODE");
					String name = object1.getString("SITUATION_NAME");
					//包含,已经选择了的
					if(mSymptomCode.contains(String.valueOf(code))){
						SymptomEntity symptomEntity = new SymptomEntity();
						symptomEntity.setName(name);
						symptomEntity.setCode(code);
						symptomEntity.setSuperposition(superCode);
						symptomEntity.setPositionName(positionName);
						symptomEntity.setSelected(true);
						List<SymptomEntity> listEntities = null;
						if(mLinkedHashMap.containsKey(positionName)){
							listEntities = mLinkedHashMap.get(positionName);
						}else{
							listEntities = new ArrayList<SymptomEntity>();
							mLinkedHashMap.put(positionName, listEntities);
						}
						listEntities.add(symptomEntity);
						mSelectedMap.put(String.valueOf(code),symptomEntity);
						mSymptomCode.remove(String.valueOf(code));
						if(mSymptomCode.size() == 0) return ;
					}
				}
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
	}
	/**
	 * 通过大部位解析
	 * @param codes
	 * @param list
	 * @param tagList
	 */
	private void parseJsonBySuperposition(String codes,List<SymptomEntity> list,List<String> tagList){
		if(list.size()>0 || mJsonArray == null)return;
		int index = 0;
		try{
			for (int i = 0; i < mJsonArray.length(); i++) {
				JSONObject jsonObject = mJsonArray.getJSONObject(i);
				String  positionName = jsonObject.has("POSITION_NAME")?jsonObject.getString("POSITION_NAME"):"";//部位名称
				//String  subCode = jsonObject.getString("SUBPOSITION_CODE");
				String  superName  = jsonObject.getString("SUBPOSITION_NAME");
				String  superCode = jsonObject.getString("SUPERPOSITION");//大部位
				if(superCode.equals(codes)){
					index++;
					SymptomEntity symptomEntity = new SymptomEntity();
					symptomEntity.setPositionName(positionName);
					symptomEntity.setName(superName);
					list.add(symptomEntity);
					tagList.add(superName);
					
					JSONArray array = jsonObject.optJSONArray("SITUATION");
					if(array == null) continue;
					for (int j = 0; j < array.length(); j++) {
						//子级内容
						JSONObject object1 =  array.getJSONObject(j);
						int code = object1.getInt("SITUATION_CODE");
						String name = object1.getString("SITUATION_NAME");
						symptomEntity = new SymptomEntity();
						if(mSelectedMap.containsKey(String.valueOf(code))){
							symptomEntity = mSelectedMap.get(String.valueOf(code));
							symptomEntity.setSelected(true);
							List<SymptomEntity> listEntities = null;
							if(mLinkedHashMap.containsKey(positionName)){
								listEntities = mLinkedHashMap.get(positionName);
							}else{
								listEntities = new ArrayList<SymptomEntity>();
								mLinkedHashMap.put(positionName, listEntities);
							}
							listEntities.add(symptomEntity);
						}
						symptomEntity.setName(name);
						symptomEntity.setCode(code);
						symptomEntity.setSuperposition(superCode);
						symptomEntity.setPositionName(positionName);
						list.add(symptomEntity);
					}
				}
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
	}
	

}