package com.yksj.consultation.son.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.yksj.consultation.son.R;

public class ShopPointDrawView extends Drawable{

	private Bitmap bitmap;
	private int hight;
	private int width;
	private boolean isShow;
	private Context context;

	public ShopPointDrawView(Context context){
		this.context = context;
		bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.shopping_trolley);
		width = bitmap.getWidth();
		hight = bitmap.getHeight();
	}
	public void showPoint(Boolean boolean1){
		isShow = boolean1;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return hight;
	}

	@Override
	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		canvas.drawBitmap(bitmap,width, 0, null);
//		if (isShow != true) {
//			
//		}
		if (isShow) {
			paint.setColor(Color.RED);
		}else {
			paint.setColor(Color.TRANSPARENT);
		}
		canvas.drawCircle(width-15, 15, 10, paint);
		
	}

	@Override
	public int getOpacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub
		
	}

}
