package com.yksj.consultation.son.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.CheckBox;

/**
 * drawableLeft与文本一起居中显示
 * @author 农民伯伯
 * @see http://www.cnblogs.com/over140/p/3464348.html
 * 
 */
public class DrawableCenterCheckBox extends CheckBox {
    
    public DrawableCenterCheckBox(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public DrawableCenterCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public DrawableCenterCheckBox(Context context) {
        super(context);
    }
    
    @Override  
    protected void onDraw(Canvas canvas) {  
        Drawable[] drawables = getCompoundDrawables();  
        if (drawables != null) {  
            Drawable drawableLeft = drawables[2];  
                if (drawableLeft != null) {  
              
                float textWidth = getPaint().measureText(getText().toString());  
                int drawablePadding = getCompoundDrawablePadding();  
                int drawableWidth = 0;  
                drawableWidth = drawableLeft.getIntrinsicWidth();  
                float bodyWidth = textWidth + drawableWidth + drawablePadding;  
                setPadding(0, 0, (int)(getWidth() - bodyWidth), 0);  
                canvas.translate((getWidth() - bodyWidth) / 2, 0);  
            }  
        }  
        super.onDraw(canvas);  
    }  
}
