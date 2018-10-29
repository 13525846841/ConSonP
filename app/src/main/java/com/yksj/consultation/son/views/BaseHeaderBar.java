package com.yksj.consultation.son.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;


public class BaseHeaderBar extends RelativeLayout implements BaseBar{

	
	private Context context;

	public BaseHeaderBar(Context context) {
		super(context);
		this.context = context;
	}

	public BaseHeaderBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public BaseHeaderBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public void removeOut() {
		TranslateAnimation tran = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0,
				Animation.RELATIVE_TO_PARENT, 0, 
				Animation.RELATIVE_TO_PARENT, 0,
				Animation.RELATIVE_TO_PARENT, -1.0f);
		tran.setDuration(300);
		tran.setFillAfter(true);
		this.startAnimation(tran);
	}
	public void removeIn() {
		TranslateAnimation tran = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0,
				Animation.RELATIVE_TO_PARENT, 0, 
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0);
		tran.setDuration(300);
		tran.setFillAfter(true);
		this.startAnimation(tran);
	}

	public void addButton(int place,int background,OnClickListener click) {
		setOnClickListener(null);
		Button button = null;
		button = createButton(place);
		button.setBackgroundResource(background);
		button.setOnClickListener(click);
		button.setPadding(1, 1, 1, 1);
		this.addView(button);
	}

	private Button createButton(int place){
		Button button = new Button(context);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		switch(place) {
		case CREATE_RIGHTBUTTON:
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			break;
		case CREATE_LEFTBUTTON:
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			break;
		case CREATE_CENTERBUTTON:
			layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
			break;
			default:
			try {
				throw new Exception("the argument place should one of the class constant!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		button.setLayoutParams(layoutParams);
		return button;
	}

}
