package com.yksj.consultation.son.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class SceneLauncherView extends FrameLayout {
	
	private VelocityTracker mVelocityTracker;//滑动计算
	private Scroller mScroller;
	private int mTouchSlop;//一个拖拽的距离
	private int mMinimumVelocity;
	private int mMaximumVelocity;
	private boolean mIsBeginDragged = false;//是否是拖动状态
	private float mLastMotionX;//上一个按下的位置
	private float mInitialMotionX;//第一次按下的位置,用于action_up时候计算总共滑动的距离
	private int mCurrentPager = 1;
	private final int MAX_PAGER = 2;//最大页数
	private int mFlingDistance;//滑动的距离
	private static final int INVALID_POINTER = -1;//无效的点id
	private int mActivePointerId = INVALID_POINTER;//当前按下的点id

	//是否正在显示金币金币,在显示状态不让滑动
	private boolean isGoldShowing = false;
	private int mLeftPoint;//到达左边停留的位置
	private int mCenterPoint;//右边停留位置
	private int mRightPoint;//金币停留位置
	
	public SceneLauncherView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
		// TODO Auto-generated constructor stub
	}

	public SceneLauncherView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub
	}

	public SceneLauncherView(Context context) {
		super(context);
		init(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void addView(View child) {
		if (getChildCount() > 0) {
            throw new IllegalStateException("MyLauncher can host only one direct child");
        }
		super.addView(child);
	}
	
	/*
	 * 计算子控件的大小
	 * (non-Javadoc)
	 * @see android.view.ViewGroup#measureChild(android.view.View, int, int)
	 */
	@Override
	protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
		ViewGroup.LayoutParams lp = child.getLayoutParams();
		int childWidthMeasureSpec;
		int childHeightMeasureSpec;
		childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec, getPaddingTop()
				+getPaddingBottom(), lp.height);
		childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
	}

	/*
	 * 计算子视图Margin间距
	 * (non-Javadoc)
	 * @see android.view.ViewGroup#measureChildWithMargins(android.view.View, int, int, int, int)
	 */
	@Override
	protected void measureChildWithMargins(View child,
			int parentWidthMeasureSpec, int widthUsed,
			int parentHeightMeasureSpec, int heightUsed) {
		final MarginLayoutParams mlp = (MarginLayoutParams)child.getLayoutParams();
		//计算高度
		final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
				getPaddingTop() + getPaddingBottom() + mlp.topMargin + mlp.bottomMargin
				+ heightUsed, mlp.height);
		//计算宽度
		final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
				mlp.leftMargin + mlp.rightMargin, MeasureSpec.UNSPECIFIED);
		child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
	}
	
	@Override
	public void computeScroll() {
		if(mScroller.computeScrollOffset()){
			scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
			postInvalidate();  
		}
	}
	 
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 初始化
	 * @param context
	 */
	private void init(Context context){
		mScroller = new Scroller(context);
		final ViewConfiguration configuration = ViewConfiguration.get(context);
		mTouchSlop = configuration.getScaledTouchSlop();
		mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();//fling手势的最小值
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();//fling手势的最大值
//      mOverscrollDistance = configuration.getScaledOverscrollDistance();
//      mOverflingDistance = configuration.getScaledOverflingDistance();
        final float density = context.getResources().getDisplayMetrics().density;
        mFlingDistance = (int) (25 * density);//一个认为滑动的距离
	}
	
	
	
}
