package com.yksj.consultation.son.views;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yksj.consultation.son.R;

public class BaseFooterBar extends LinearLayout implements BaseBar {
	
	private Context context;
	private LinearLayout textLayout;
	private LinearLayout buttonLayout;
	private TextView titleTextView;
	private TextView contentTextView;
	private boolean flag = false;
	
	public BaseFooterBar(Context context) {
		super(context);
		this.context = context;
	}

	public BaseFooterBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	public BaseFooterBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public void initView() {
		this.setOrientation(VERTICAL);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		textLayout = new LinearLayout(context);
		params.setMargins(20, 0, 0, 20);
		textLayout.setOrientation(VERTICAL);
		titleTextView = new TextView(context);
		titleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrows_down, 0);
		titleTextView.setText("标题");
		titleTextView.setLayoutParams(params);
		titleTextView.setTextSize(16);
		titleTextView.setTextColor(getResources().getColor(R.color.white));
		titleTextView.setPadding(0, 0, 10, 0);
		contentTextView = new TextView(context);
		contentTextView.setText("内容");
		contentTextView.setTextSize(14);
		contentTextView.setLayoutParams(params);
		contentTextView.setTextColor(getResources().getColor(R.color.white));
		textLayout.addView(titleTextView);
		textLayout.addView(contentTextView);
		textLayout.setBackgroundColor(getResources().getColor(R.color.text_bg));
		buttonLayout = new LinearLayout(context);
		buttonLayout.setOrientation(HORIZONTAL);
		contentTextView.setVisibility(View.GONE);
		titleTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!flag) {
					contentTextView.setVisibility(View.VISIBLE);
					titleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrows_up, 0);
				} else {
					contentTextView.setVisibility(View.GONE);
					titleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrows_down, 0);
				}
				flag = !flag;
			}
		});
		
		this.addView(textLayout);
		this.addView(buttonLayout);
	}
	
	public void removeOut() {
		TranslateAnimation tran = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0, 
				Animation.RELATIVE_TO_PARENT, 0, 
				Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 1.0f);
		tran.setDuration(300);
		tran.setFillAfter(true);
		this.startAnimation(tran);
	}

	public void removeIn() {
		TranslateAnimation tran = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0, 
				Animation.RELATIVE_TO_PARENT, 0, 
				Animation.RELATIVE_TO_SELF, 1.0f,
				Animation.RELATIVE_TO_SELF, 0);
		tran.setDuration(300);
		tran.setFillAfter(true);
		this.startAnimation(tran);
	}

	public void addButton(int background,
			OnClickListener click) {
		setOnClickListener(null);
		ImageButton button = createButton();
		button.setImageDrawable(context.getResources().getDrawable(background));
		button.setOnClickListener(click);
		button.setPadding(1, 1, 1, 1);
		buttonLayout.addView(button);
	}

	private ImageButton createButton() {
		ImageButton button = new ImageButton(context);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.weight = 1;
		layoutParams.leftMargin = 1;
		layoutParams.rightMargin = 1;
		button.setLayoutParams(layoutParams);
		button.setBackgroundResource(R.drawable.imageleft);
		return button;
	}
	
	public void setTitleText(String content) {
		titleTextView.setText(content);
	}
	
	public void setContentText(String content) {
		contentTextView.setText(content);
	}

}
