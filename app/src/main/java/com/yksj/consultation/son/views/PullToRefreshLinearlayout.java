package com.yksj.consultation.son.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.yksj.consultation.son.R;


public class PullToRefreshLinearlayout extends PullToRefreshBase<LinearLayout> {

	private final OnRefreshListener defaultOnRefreshListener = new OnRefreshListener() {

		@Override
		public void onRefresh(int currentcode) {
			onRefreshComplete();
		}

	};


	public PullToRefreshLinearlayout(Context context) {
		super(context);

		/**
		 * Added so that by default, Pull-to-Refresh refreshes the page
		 */
		setOnRefreshListener(defaultOnRefreshListener);
	}

	public PullToRefreshLinearlayout(Context context, int mode) {
		super(context, mode);

		/**
		 * Added so that by default, Pull-to-Refresh refreshes the page
		 */
		setOnRefreshListener(defaultOnRefreshListener);
	}

	public PullToRefreshLinearlayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		/**
		 * Added so that by default, Pull-to-Refresh refreshes the page
		 */
		setOnRefreshListener(defaultOnRefreshListener);
	}

	@Override
	protected LinearLayout createRefreshableView(Context context, AttributeSet attrs) {
		LinearLayout scrollView = new LinearLayout(context, attrs);

		scrollView.setId(R.id.refresh_view);
		return scrollView;
	}

	@Override
	protected boolean isReadyForPullDown() {
		return refreshableView.getScrollY() == 0;
	}

	@Override
	protected boolean isReadyForPullUp() {
		LinearLayout view = getRefreshableView();
		int off=view.getScrollY()+view.getHeight()-view.getChildAt(0).getHeight();
	
			return true;
	
	}
}
