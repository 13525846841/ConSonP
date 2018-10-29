package com.yksj.consultation.son.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.yksj.consultation.son.R;

/**
 * 字母排序 滚动条
 * @author dhh
 *
 */
public class SideBar extends View {  
	 private String[] indexs;  
	    private SectionIndexer sectionIndexter = null;  
	    private ListView list;  
	    private TextView mDialogText;
	    private int m_nItemHeight = 25;  
	//    private static int height;
	    public SideBar(Context context) {  
	        super(context);  
	        init();  
	    }  
	    public SideBar(Context context, AttributeSet attrs) {  
	        super(context, attrs);  
	        init();  
	    }  
	    
	    private void init() {  
	        indexs = new String[] {""};  
    }  
	    public SideBar(Context context, AttributeSet attrs, int defStyle) {  
	        super(context, attrs, defStyle); 
	        init();  
	    }  
	    public void setListView(ListView _list) {  
	        list = _list;  
	        sectionIndexter = (SectionIndexer) _list.getAdapter();  
	    }  
	    
	    public void setTextView(TextView mDialogText) {  
	    	this.mDialogText = mDialogText;  
	    }  
	    
	    public void setHiget(int height){
	  //  	this.height = height;
	    	if(indexs.length>0){
	    		m_nItemHeight = (getResources().getDisplayMetrics().heightPixels-height)/indexs.length;
	    	}
	    }
	    
	    public boolean onTouchEvent(MotionEvent event) {  
	        super.onTouchEvent(event); 
	        int i = (int) event.getY();  
	        int idx = i /m_nItemHeight;  
	        if (idx >= indexs.length) {  
	            idx = indexs.length - 1;  
	        } else if (idx < 0) {  
	            idx = 0;  
	        }  
	        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {  
	        	 this.setBackgroundResource(R.drawable.mm_text_bg_trans);
	        	mDialogText.setVisibility(View.VISIBLE);
	        	mDialogText.setText(""+indexs[idx]);
	            if (sectionIndexter == null) {  
	               sectionIndexter = (SectionIndexer) list.getAdapter();  
	            }  
	            int position = sectionIndexter.getPositionForSection(idx);  
	            if (position == -1) {  
	                return true;  
	            }  
	           list.setSelection(position); 
	           list.setSelectionFromTop(position, 0);
	        }else{
	        	 this.setBackgroundResource(0);
	        	mDialogText.setVisibility(View.INVISIBLE);
	        }  
	        return true;  
	    }  
	    
	    public void onDraw(Canvas canvas) {  
	        Paint paint = new Paint();  
	        paint.setColor(0xff595c61);  
	        paint.setTextSize(16);  
	        paint.setTextAlign(Paint.Align.CENTER); 
	        paint.setTypeface(Typeface.DEFAULT_BOLD);
	        float widthCenter = getMeasuredWidth() / 2;  
	        for (int i = 0; i < indexs.length; i++) {  
	            canvas.drawText(String.valueOf(indexs[i]), widthCenter,15+(i * m_nItemHeight), paint);  
	        }  
	        super.onDraw(canvas);  
	    } 
	    
	    public void setIndex(String[] indexs){
	    	this.indexs = indexs;
	    }
}
