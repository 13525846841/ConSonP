package com.yksj.consultation.son.views;


import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.yksj.consultation.son.R;

public class Panel extends LinearLayout{
	
	public interface PanelClosedEvent {
		void onPanelClosed(View panel);
	}
	
	public interface PanelOpenedEvent {
		void onPanelOpened(View panel);
	}
	private final static int HANDLE_HIGHT=80;
	/**每次自动展开/收缩的范围*/
	private final static int MOVE_HIGHT=20;
	private Button btnHandle;
	private LinearLayout panelContainer;
	private int mtopMargin=0;
	private Context mContext;
	private PanelClosedEvent panelClosedEvent=null;
	private PanelOpenedEvent panelOpenedEvent=null;

	public Panel(Context context,View otherView,int width,int height) {
		super(context);
		this.mContext=context;
		
		
		//改变Panel附近组件的属性
		LayoutParams otherLP=(LayoutParams) otherView.getLayoutParams();
		otherLP.weight=1;//支持压挤
		otherView.setLayoutParams(otherLP);
		
		//设置Panel本身的属性
		LayoutParams lp=new LayoutParams(width, height);
		lp.topMargin=-lp.height+HANDLE_HIGHT;//Panel的Container在屏幕不可视区域，Handle在可视区域
		mtopMargin=Math.abs(lp.topMargin);
		this.setLayoutParams(lp);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setBackgroundColor(Color.WHITE);
		
//		//设置Container的属性
//		panelContainer=new LinearLayout(context);
//		panelContainer.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,height-HANDLE_HIGHT));
//		this.addView(panelContainer);
		
		//新建测试组件
		Button tvTest=new Button(context);
		tvTest.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,height-HANDLE_HIGHT));
		tvTest.setText("测试组件，红字白底");
		tvTest.setTextColor(Color.RED);
//		tvTest.setBackgroundColor(Color.WHITE);
		tvTest.setBackgroundResource(R.drawable.button);
		//加入到Panel里面
//		fillPanelContainer(tvTest);
		this.addView(tvTest);
		
		//设置Handle的属性
		btnHandle=new Button(context);
		btnHandle.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,HANDLE_HIGHT));
//		btnHandle.setBackgroundResource(R.drawable.pulltorefresh_down_arrow);
		btnHandle.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				LayoutParams lp = (LayoutParams) Panel.this.getLayoutParams();
				if (lp.topMargin < 0)// CLOSE的状态
					new AsynMove().execute(new Integer[] { MOVE_HIGHT });// 正数展开
				else if (lp.topMargin >= 0)// OPEN的状态
					new AsynMove().execute(new Integer[] { -MOVE_HIGHT });// 负数收缩
			}
			
		});
		//btnHandle.setOnTouchListener(HandleTouchEvent);
		this.addView(btnHandle);
		
		
	}

	/**
	 * 定义收缩时的回调函数
	 * @param event
	 */
	public void setPanelClosedEvent(PanelClosedEvent event)
	{
		this.panelClosedEvent=event;
	}
	
	/**
	 * 定义展开时的回调函数
	 * @param event
	 */
	public void setPanelOpenedEvent(PanelOpenedEvent event)
	{
		this.panelOpenedEvent=event;
	}
	
	/**
	 * 把View放在Panel的Container
	 * @param v
	 */
	public void fillPanelContainer(View v)
	{
		panelContainer.addView(v);
	}
	
	class AsynMove extends AsyncTask<Integer, Integer, Void> {

		@Override
		protected Void doInBackground(Integer... params) {
			int times;
			if (mtopMargin % Math.abs(params[0]) == 0)// 整除
				times = mtopMargin / Math.abs(params[0]);
			else
				// 有余数
				times = mtopMargin / Math.abs(params[0]) + 1;

			for (int i = 0; i < times; i++) {
				publishProgress(params);
				try {
					Thread.sleep(Math.abs(params[0]));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... params) {
			LayoutParams lp = (LayoutParams) Panel.this.getLayoutParams();
			if (params[0] < 0)
				lp.topMargin = Math.max(lp.topMargin + params[0],
						(-mtopMargin));
			else
				lp.topMargin = Math.min(lp.topMargin + params[0], 0);

			if(lp.topMargin==0 && panelOpenedEvent!=null){//展开之后
				panelOpenedEvent.onPanelOpened(Panel.this);//调用OPEN回调函数
			}
			else if(lp.topMargin==-(mtopMargin) && panelClosedEvent!=null){//收缩之后
				panelClosedEvent.onPanelClosed(Panel.this);//调用CLOSE回调函数
			}
			Panel.this.setLayoutParams(lp);
		}
	}

}
