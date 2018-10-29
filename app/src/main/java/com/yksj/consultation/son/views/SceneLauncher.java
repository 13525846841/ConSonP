package com.yksj.consultation.son.views;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class SceneLauncher extends FrameLayout {
	
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
	
	private SceneLauncherPageChangeListener mChangeListener;
	
	public static interface SceneLauncherPageChangeListener{
		void onSceneChange(int index);
		void onGoldChange(boolean b);
	}
	
	public void setChangeListener(SceneLauncherPageChangeListener mChangeListener) {
		this.mChangeListener = mChangeListener;
	}

	public SceneLauncher(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public SceneLauncher(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SceneLauncher(Context context) {
		super(context);
		init(context);
	}
	
	public int getmLeftPoint() {
		return mLeftPoint;
	}

	public void setmLeftPoint(int mLeftPoint) {
		this.mLeftPoint = mLeftPoint;
	}

	public int getmCenterPoint() {
		return mCenterPoint;
	}

	public void setmCenterPoint(int mCenterPoint) {
		this.mCenterPoint = mCenterPoint;
	}

	public int getmRightPoint() {
		return mRightPoint;
	}

	public void setmRightPoint(int mRightPoint) {
		this.mRightPoint = mRightPoint;
	}

	public int getmCurrentPager() {
		return mCurrentPager;
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
//        mOverscrollDistance = configuration.getScaledOverscrollDistance();//
//        mOverflingDistance = configuration.getScaledOverflingDistance();
        final float density = context.getResources().getDisplayMetrics().density;
        mFlingDistance = (int) (25 * density);//一个认为滑动的距离
	}
	
	@Override
	public void addView(View child) {
		if (getChildCount() > 0) {
            throw new IllegalStateException("MyLauncher can host only one direct child");
        }
		super.addView(child);
	}
	
	/*
	 * 
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
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		//setCurrtentItem(mCurrentPager);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//setCurrtentItem(mCurrentPager);
	}
	
	@Override
	public void computeScroll() {
		if(mScroller.computeScrollOffset()){
			scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
			postInvalidate();  
		}
	}
	
	/**
	 * 事件拦截
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(isGoldShowing) return true;
		final int action = ev.getAction();
		if((action == MotionEvent.ACTION_MOVE) && mIsBeginDragged){//已经是拖动状态了
			return true;
		}
		switch(ev.getActionMasked()){
			case MotionEvent.ACTION_MOVE: {//移动状态
	            final int activePointerId = mActivePointerId;
	            if (activePointerId == INVALID_POINTER) {//当前按下是一个无效的点
	                break;
	            }
	            
	            final int pointerIndex = MotionEventCompat.findPointerIndex(ev,activePointerId);//获得按下的点
	            final float x = MotionEventCompat.getX(ev,pointerIndex);//根据点获得x坐标
	            final int xDiff = (int) Math.abs(x - mLastMotionX);//求这次和上一次滑动的距离
	            if (xDiff > mTouchSlop) {//判断距离是否是一个滑动距离
	                mIsBeginDragged = true;//拖动
	                //mLastMotionX = x;
	                mLastMotionX = xDiff > 0 ? mInitialMotionX + mTouchSlop :mInitialMotionX - mTouchSlop;
	            }
	            //执行拖拽
	            if(mIsBeginDragged){
	            	performDrag(x);
	            }
	            break;
			}
			case MotionEvent.ACTION_DOWN:{
				final float x = ev.getX();
				/*if(inChild((int)x,(int)ev.getY())){//不在范围之内
					mIsBeginDragged = false;
					break;
				}*/
				mLastMotionX = mInitialMotionX = x;//记录当前的位置
				mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
				mIsBeginDragged = false;//!mScroller.isFinished();//滚动是否已经完成
				break;
			}
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				endDrag();
				break;
			case MotionEvent.ACTION_POINTER_UP:
				onSecondPointUp(ev);
				break;
		}
		return mIsBeginDragged;
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(ev.getAction() == MotionEvent.ACTION_DOWN && ev.getEdgeFlags() != 0){//
			return false;
		}
		if(mVelocityTracker == null){
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);
		final int action = ev.getAction();
		switch(action){
			case MotionEvent.ACTION_DOWN:{
				mIsBeginDragged = true;
				if(!mScroller.isFinished()){//是否已经完成
					mScroller.abortAnimation();//未完成则终止动画
				}
				mInitialMotionX = mLastMotionX = ev.getX();
				mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
				break;
			}
			case MotionEvent.ACTION_MOVE://开始移动
				if (!mIsBeginDragged) {
                    final int pointerIndex = MotionEventCompat.findPointerIndex(ev,mActivePointerId);
                    final float x = MotionEventCompat.getX(ev, pointerIndex);
                    final float xDiff = Math.abs(x - mLastMotionX);
                    final float y = MotionEventCompat.getY(ev, pointerIndex);
                    if (xDiff > mTouchSlop) {
                    	mIsBeginDragged = true;
                        mLastMotionX = x - mInitialMotionX > 0 ? mInitialMotionX + mTouchSlop : mInitialMotionX - mTouchSlop;
                    }
                }
                // Not else! Note that mIsBeingDragged can be set above.
                if (mIsBeginDragged) {
                    // Scroll to follow the motion event
                    final int activePointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                    final float x = MotionEventCompat.getX(ev, activePointerIndex);
                    performDrag(x);
                }
				break;
			case MotionEvent.ACTION_UP:
				if(mIsBeginDragged){//是否是拖拽状态
					final VelocityTracker tracker = mVelocityTracker;
					tracker.computeCurrentVelocity(1000,mMaximumVelocity);
					int initialVelocity = (int)tracker.getXVelocity(mActivePointerId);//计算速度
					final int activePointerIndex = ev.findPointerIndex(mActivePointerId);//活动点的索引
					final float x = ev.getX(activePointerIndex);
					final int totalDelta = (int)(x - mInitialMotionX);//总共拖动的位置距离
					final float pageOffset = Math.abs(totalDelta)/(float)getWidth();//计算滑动了多少百分比
					int pager = computerTagertPager(mCurrentPager,initialVelocity,totalDelta,pageOffset);//计算滑动到那一页
					if(getScrollX() >= mRightPoint){
						swipToGold();
					}else{
						setCurrtentItem(pager);
					}
					endDrag();
				}
				break;
			case MotionEvent.ACTION_CANCEL://
				if(mIsBeginDragged && getChildCount() > 0 ){
					endDrag();
				}
				break;
			case MotionEvent.ACTION_POINTER_UP://一个手指弹起的时候
				onSecondPointUp(ev);
				mLastMotionX = MotionEventCompat.getX(ev,MotionEventCompat.findPointerIndex(ev,mActivePointerId));
				break;
		}
		return true;
	}

	public void onGoldChange(boolean b){
		isGoldShowing = b;
		if(mChangeListener != null)mChangeListener.onGoldChange(b);
	}
	
	private void swipToGold(){
		int destX = mRightPoint;
		destX -= getScrollX();
		mScroller.startScroll(getScrollX(),getScrollY(),destX, getScrollY());
		invalidate();
		onGoldChange(true);
	}
	
	public void setCurrtentItem(int item){
		/*if(mCurrentPager != item && isGoldShowing){
			mCurrentPager = item;
		}
		*/
		if(isGoldShowing){
			isGoldShowing = false;
		}
		int destX = 0;
		if(item == 1){
			destX = mLeftPoint;
		}else{//第二屏幕
			destX = mCenterPoint;
		}
		destX -= getScrollX();
		mScroller.startScroll(getScrollX(),getScrollY(),destX, getScrollY());
		invalidate();
		if(mChangeListener != null && mCurrentPager != item){
			mCurrentPager = item;
			mChangeListener.onSceneChange(mCurrentPager);
		}
	}
	
	/**
	 * 计算目标页
	 */
	private int computerTagertPager(int currentPage,int velocity,int deltaX,float pageOffset){
		int tagertPage;
		if(Math.abs(deltaX) > mFlingDistance && Math.abs(velocity) > mMinimumVelocity){//滑动距离和手势
			tagertPage = velocity > 0 ? currentPage-1:currentPage+1;
		}else{
			if(pageOffset >= 0.5){//够到下一屏幕
				if(velocity > 0 ){//向左滑动
					currentPage-=1;
				}else{//向右滑动
					currentPage+=1;
				}
			}
			tagertPage = currentPage;
		}
		tagertPage = Math.max(1,Math.min(tagertPage,MAX_PAGER));//计算要滑动到的页数
		return tagertPage;
	}
	/**
	 * 
	 * 执行拖动
	 * @param x
	 */
	private void performDrag(float x){
		final float deltaX = mLastMotionX - x;//计算拖动的距离
		mLastMotionX = x;//记录这次x开始位置
		float oldScrollX = getScrollX();//上一次的位置
		float scrollX = oldScrollX + deltaX;//滑动的最终位置
		
		float leftBound = 0;//左右的最大位置
		float rightBound = getChildAt(0).getWidth() - getWidth();//右边的最大位置
		
		if(scrollX <= leftBound){//滑动是否已经超过最左边
			scrollX = leftBound;
		}else if(scrollX >= rightBound){//已经超过最右边
			scrollX = rightBound;
		}
		mLastMotionX += scrollX - (int)scrollX;
		scrollTo((int)scrollX,getScrollY());
	}
	
	/**
	 * 停止拖拽释放资源
	 */
	private void endDrag(){
		mActivePointerId = INVALID_POINTER;
		mIsBeginDragged = false;
		if(mVelocityTracker != null){
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
	}
	
	/**
	 * 第二个手指弹起的时候
	 * @param ev
	 */
	private void onSecondPointUp(MotionEvent ev){
		final int pointerIndex =  (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) //获得点击索引
					>> MotionEvent.ACTION_POINTER_INDEX_SHIFT ;//右移八位获得索引
		final int pointerId = ev.getPointerId(pointerIndex);//根据所以获得点id
			if(pointerId == mActivePointerId){//如果当前活动的点和这个店相等
				final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
				mLastMotionX  = ev.getX(newPointerIndex);
				mActivePointerId = ev.getPointerId(newPointerIndex);
				if(mVelocityTracker != null){
					mVelocityTracker.clear();
				}
			}
	}
	/**
	 * 是否在范围之内
	 * @param x
	 * @param y
	 * @return
	 */
/*    private boolean inChild(int x, int y) {
        if (getChildCount() > 0) {
            final int scrollX = getScrollX();//当前x的位置
            final View child = getChildAt(0);//获取第一个view
            return !(y < child.getTop()
                    || y >= child.getBottom()
                    || x < child.getLeft() - scrollX
                    || x >= child.getRight() - scrollX);
        }
        return false;
    }*/

}
