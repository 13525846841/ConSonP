package com.yksj.consultation.son.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
/**
 * 自定义GridView,控制GridView显示不完整和禁止滚动
 * @author Administrator
 *
 */
public class TagsGridView extends GridView {

	public TagsGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	
	
	public TagsGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}



	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
