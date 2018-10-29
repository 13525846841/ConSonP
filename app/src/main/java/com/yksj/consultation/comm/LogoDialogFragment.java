package com.yksj.consultation.comm;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.yksj.consultation.son.R;

public class LogoDialogFragment extends DialogFragment {
	
	ImageView mImageView;
	
	public static void onShow(FragmentManager manager){
		LogoDialogFragment fragment = new LogoDialogFragment();
		fragment.show(manager, "LOGO_DIALOG");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCancelable(false);
		setStyle(STYLE_NO_FRAME,android.R.style.Theme_Holo_Light);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.logo_layout,null);
		Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.logo_anim);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				LogoDialogFragment.this.dismissAllowingStateLoss();
			}
		});
		mImageView.startAnimation(animation);
		return view;
	}
	
}
