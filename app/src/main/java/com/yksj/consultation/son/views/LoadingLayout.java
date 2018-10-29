package com.yksj.consultation.son.views;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yksj.consultation.son.R;


public class LoadingLayout extends FrameLayout{
	static final int DEFAULT_ROTATION_ANIMATION_DURATION = 150;

//	private final ImageView headerImage;
//	private final ProgressBar headerProgress;
//	private final TextView headerText;

	private final Animation rotateAnimation, resetRotateAnimation;

    private EditText edit;

	private TextView headerText;

	private ImageView headerImage;

	private ProgressBar headerProgress;

	private String releaseLabel;

	private String pullLabel;

	private String refreshingLabel;
	private final String SHARE_KRY = "refreshtime";
	private final String SHARE_TIME_KRY = "time";
	private int headerType = 0;

	private TextView headerTime;

	private SharedPreferences share;
	//public static final int LAY = -1;

	public LoadingLayout(Context context, final int mode,int type,String releaseLabel, String pullLabel, String refreshingLabel) {
		super(context);
		this.headerType = type;
		switch (headerType) {
		case PullToRefreshBase.HEADER_INPUT:
			setLayView(context, mode);
			break;
		case PullToRefreshBase.HEADER_PROGRESSBAR:
			setProgressbarView(context, mode);
			break;
		case PullToRefreshBase.HEADER_NOMAL:
			setNormalView(context, mode);
			break;
		case PullToRefreshBase.HEADER_MORE:
			setMoreView(context);
			break;
		case PullToRefreshBase.HEADER_DIFFERENT:
			setDifferentView(context, mode);
			break;
		default:
			break;
		}
		
		final Interpolator interpolator = new LinearInterpolator();
		rotateAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
		        0.5f);
		rotateAnimation.setInterpolator(interpolator);
		rotateAnimation.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
		rotateAnimation.setFillAfter(true);

		resetRotateAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f,
		        Animation.RELATIVE_TO_SELF, 0.5f);
		resetRotateAnimation.setInterpolator(interpolator);
		resetRotateAnimation.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
		resetRotateAnimation.setFillAfter(true);
		
		this.releaseLabel = releaseLabel;
		this.pullLabel = pullLabel;
		this.refreshingLabel = refreshingLabel;
	}
	
	/**
	 *  listview 刷新
	 * @param context
	 * @param mode
	 */
	private void setNormalView(Context context,int mode){
		ViewGroup header = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header_normal, this);
		headerText = (TextView) header.findViewById(R.id.pull_to_refresh_text);
		headerTime = (TextView) header.findViewById(R.id.pull_to_refresh_time);
		headerImage = (ImageView) header.findViewById(R.id.pull_to_refresh_image);
		headerProgress = (ProgressBar) header.findViewById(R.id.pull_to_refresh_progress);
		share = context.getSharedPreferences(SHARE_KRY, Context.MODE_PRIVATE);
		String time = share.getString(SHARE_TIME_KRY,"");
		headerTime.setText(time);
		switch (mode) {
		case PullToRefreshBase.MODE_PULL_UP_TO_REFRESH:
			headerImage.setImageResource(R.drawable.pulltorefresh_up_arrow);
			break;
		case PullToRefreshBase.MODE_PULL_DOWN_TO_REFRESH:
		default:
			headerImage.setImageResource(R.drawable.pulltorefresh_down_arrow);
			break;
	    }
	}
	
	
	/**
	 *  listview 刷新
	 * @param context
	 * @param mode
	 */
	private void setMoreView(Context context){
		ViewGroup header = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.group_item_more, this);
	}
	
	/**
	 * 进度条headerview
	 * @param context
	 * @param mode
	 */
	public void setDifferentView(Context context ,int mode){
		switch(mode){
		case PullToRefreshBase.MODE_PULL_DOWN_TO_REFRESH:
			setNormalView(context, mode);
			break;
		case PullToRefreshBase.MODE_PULL_UP_TO_REFRESH:
			ViewGroup header = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.group_item_more, this);
			if(headerImage != null)
			  headerImage.setImageResource(R.drawable.pulltorefresh_up_arrow);
			break;
		default:
			if(headerImage != null)
			   headerImage.setImageResource(R.drawable.pulltorefresh_down_arrow);
			break;
		}
	}
	
	/**
	 * 进度条headerview
	 * @param context
	 * @param mode
	 */
	public void setProgressbarView(Context context ,int mode){
		ViewGroup header = (ViewGroup)LayoutInflater.from(context).inflate(R.layout.progresbar_pull_refresh_header_layout,this);
		headerProgress = (ProgressBar) header.findViewById(R.id.pull_to_refresh_progress);
		switch(mode){
		case PullToRefreshBase.MODE_PULL_DOWN_TO_REFRESH:
			break;
		case PullToRefreshBase.MODE_PULL_UP_TO_REFRESH:
			break;
		}
	}

	
	/**
	 * 非listview刷新
	 * @param context
	 * @param mode
	 */
	private void setLayView(Context context,int mode){
		ViewGroup header = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header_edit, this);
		final Button btn =(Button)header.findViewById(R.id.refresh_header_btn);
		edit = (EditText) header.findViewById(R.id.refresh_header_edit);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn.setVisibility(View.GONE);
			    edit.setText("");	
			}
		});
		
		edit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length() > 0){
					btn.setVisibility(View.VISIBLE);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		switch (mode) {
		case PullToRefreshBase.MODE_PULL_UP_TO_REFRESH:
			header.setVisibility(View.INVISIBLE);
			break;
		case PullToRefreshBase.MODE_PULL_DOWN_TO_REFRESH:
			header.setVisibility(View.VISIBLE);
		default:
			break;
	    }
	}
	
	public void setTime(Context context){
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		String time = format.format(new Date());
		headerTime.setText(time);
		Editor editor = share.edit();
		editor.putString(SHARE_TIME_KRY, time);
		editor.commit();
	}
	
	public void reset() {
		if(headerType == PullToRefreshBase.HEADER_NOMAL){
			headerText.setText(pullLabel);
			headerImage.setVisibility(View.VISIBLE);
			headerProgress.setVisibility(View.GONE);
		}
/*		else if(headerType == PullToRefreshBase.HEADER_PROGRESSBAR){
			headerProgress.setVisibility(View.GONE);
		}*/
	}

	/**
	 * 释放刷新
	 */
	public void releaseToRefresh() {
		if(headerType == PullToRefreshBase.HEADER_NOMAL){
			headerText.setText(releaseLabel);
			headerImage.clearAnimation();
			headerImage.startAnimation(rotateAnimation);
		}else if(headerType == PullToRefreshBase.HEADER_PROGRESSBAR){
			headerProgress.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 正在刷新
	 */
	public void refreshing() {
		if(headerType == PullToRefreshBase.HEADER_NOMAL){
			headerText.setText(refreshingLabel);
			headerImage.clearAnimation();
			headerImage.setVisibility(View.INVISIBLE);
			headerProgress.setVisibility(View.VISIBLE);
		}else if(headerType == PullToRefreshBase.HEADER_PROGRESSBAR){
			headerProgress.setVisibility(View.VISIBLE);
		}
	}
	
	public void setPullLabel(String pullLabel) {
		this.pullLabel = pullLabel;
	}

	
	public void setRefreshingLabel(String refreshingLabel) {
		this.refreshingLabel = refreshingLabel;
	}

	public void setReleaseLabel(String releaseLabel) {
		this.releaseLabel = releaseLabel;
	}

	public void pullToRefresh() {
		if(headerType == PullToRefreshBase.HEADER_NOMAL){
			headerText.setText(pullLabel);
			headerImage.clearAnimation();
			headerImage.startAnimation(resetRotateAnimation);
		}
	}

	public void setTextColor(int color) {
		if(headerType == PullToRefreshBase.HEADER_NOMAL)
		          headerText.setTextColor(color);
	}
}
