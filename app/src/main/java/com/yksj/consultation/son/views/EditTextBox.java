package com.yksj.consultation.son.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

public class EditTextBox extends EditText {
    private OnBottomListener listener;
    private boolean isFrist = false;
    private int height = 0;
    private View inputView;
    private int temp = 0;
	public EditTextBox(Context context) {
		super(context);
		height = context.getResources().getDisplayMetrics().heightPixels;
		temp = height;
	}

	public EditTextBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		height = context.getResources().getDisplayMetrics().heightPixels;
		temp = height;
	}

	public EditTextBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		height = context.getResources().getDisplayMetrics().heightPixels;
		temp = height;
	}
	
	public void setInputView(View view){
		inputView = view;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Rect rect = new Rect();
		int[] location = new int[2];
		getLocationOnScreen(location);
	   // Log.i("test", "----------"+location[0]+":"+location[1]+":"+height);
		if(location[1] < temp){
			if(null != inputView)
				inputView.setVisibility(View.VISIBLE);
			if(isFrist){
				isFrist = false;
				this.setText("");
			}
		}
		
		if(location[1] > temp){
			if(null != inputView)
				inputView.setVisibility(View.GONE);
			isFrist = true;
		}
		temp = location[1];
		height = rect.bottom;
		if(null != listener)
			listener.setBottom();
		super.onDraw(canvas);
	}
	
	public void setOnBottomListener(OnBottomListener listener){
		this.listener = listener;
	}
	
	public interface OnBottomListener{
		public void setBottom();
	}
}
