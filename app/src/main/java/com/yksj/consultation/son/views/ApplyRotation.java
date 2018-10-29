package com.yksj.consultation.son.views;


import android.app.Activity;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;

public class ApplyRotation extends Activity {
	/**
	 * Setup a new 3D rotation on the container view.
	 * 
	 * @param position
	 *            the item that was clicked to show a picture, or -1 to show the
	 *            list
	 * @param start
	 *            the start angle at which the rotation must begin
	 * @param end
	 *            the end angle of the rotation
	 */
	public  void applyRotation(ViewGroup mContainer, float start,
			float end, int time, boolean rotate , Runnable swapViews) {
		// 计算中心点
		final float centerX = mContainer.getWidth() / 2.0f;
		final float centerY = mContainer.getHeight() / 2.0f;
		// Create a new 3D rotation with the supplied parameter
		// The animation listener is used to trigger the next animation
		final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end,
				centerX, centerY, 310.0f, rotate);
		rotation.setDuration(time);
		rotation.setFillAfter(true);
		// 加速动画插入器
		rotation.setInterpolator(new AccelerateInterpolator());
		// 在动画效果发生时做一个监听，如：动画结束后有什么事件发生，动画重复执行的次数
		rotation.setAnimationListener(new DisplayNextView(mContainer,swapViews));
		mContainer.startAnimation(rotation);
	}

	/**
	 * This class listens for the end of the first half of the animation. It
	 * then posts a new action that effectively swaps the views when the
	 * container is rotated 90 degrees and thus invisible.
	 */
	private final class DisplayNextView implements Animation.AnimationListener {
		private final ViewGroup mContainer;
		private final Runnable swapViews;

		private DisplayNextView( ViewGroup mContainer,
				Runnable swapViews) {
			this.mContainer = mContainer;
			this.swapViews = swapViews;
		}

		public void onAnimationStart(Animation animation) {
		}

		// 动画结束
		public void onAnimationEnd(Animation animation) {
			// Runnable被添加到消息队列，执行用户界面线程
			mContainer.post(swapViews);
		}

		public void onAnimationRepeat(Animation animation) {
		}
	}
}
