package com.yksj.healthtalk.views;

import com.yksj.consultation.son.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 组合控件可以使用系统中的属性
 * 带有小删除按钮的ImageView
 * @author lmk
 *
 */
public class MessageImageView extends RelativeLayout {

	private Context mcontext;
	private TextView tvNum;//消息数量
	private Button imageDelete;//删除button
	private ImageView image;//
	
	
	public MessageImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MessageImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mcontext=context;
		init(attrs);
	}

	public MessageImageView(Context context) {
		super(context);
		this.mcontext=context;
		init(null);
	}
	
	/*
	 * 初始化方法 加载图片  设置图片在edittext的右边
	 */
	public void init(AttributeSet attrs){
		LayoutInflater.from(mcontext).inflate(R.layout.message_image_view, this,true);
		tvNum=(TextView) findViewById(R.id.message_tv_num);
		image=(ImageView) findViewById(R.id.message_image);
		imageDelete=(Button) findViewById(R.id.message_btn_delete);
		if(attrs!=null){
			TypedArray array=mcontext.obtainStyledAttributes(attrs, R.styleable.MessageImageView);
			CharSequence ch=array.getText(R.styleable.MessageImageView_android_text);
			if(ch!=null){
				tvNum.setText(ch);//设置文本
			}
			Drawable drawable=array.getDrawable(R.styleable.MessageImageView_android_src);
			if(drawable!=null){
				image.setImageDrawable(drawable);//设置图片
			}
			array.recycle();
		}
	}

	/**
	 * 设置小Button 的点击事件
	 * @param deleteListener
	 */
	public void setDeleteListener(OnClickListener deleteListener) {
		imageDelete.setOnClickListener(deleteListener);
	}
	/**
	 * 设置TextView 的数字
	 * @param mesgNum
	 */
	public void setTextNum(int mesgNum) {
		tvNum.setText(""+mesgNum);
	}
	/**
	 * 设置小Button 的背景
	 * @param
	 */
	public void setButtonBackGround(int resId) {
		imageDelete.setBackgroundResource(resId);
	}
	
	public void setScaleType(ScaleType scaleType){
		image.setScaleType(scaleType);
	}

	public ImageView getImage() {
		return image;
	}

	public void setImage(ImageView image) {
		this.image = image;
	}
	
	public void setImageBitmap(Bitmap bm){
		image.setImageBitmap(bm);
	}
	
	

}


