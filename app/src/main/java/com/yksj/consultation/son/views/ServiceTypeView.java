package com.yksj.consultation.son.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yksj.healthtalk.entity.TickMesg;
import com.yksj.consultation.son.R;

/**
 * 普通咨询下面的服务标题和费用
 * 
 * @author Administrator
 * 
 */
public class ServiceTypeView extends LinearLayout implements OnClickListener {

	private LayoutInflater inflater;
	private RadioGroup group;
	private int GROUPSTEP = 10;
	private int DEFAUTGROUPID = 10; // RelativeLayout ID 按钮的ID +1 文字的ID +2
	private OnClickListener listener;
	private ServiceTypeSubContentListener mServiceTypeSubContentListener;

	public ServiceTypeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ServiceTypeView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		if (context instanceof OnClickListener) {
			listener = (OnClickListener) context;
		}
		if (context instanceof ServiceTypeSubContentListener) {
			mServiceTypeSubContentListener = (ServiceTypeSubContentListener)context;
		}
		removeAllViews();
		inflater = LayoutInflater.from(context);
	}

	// [{"SERVICE_TYPE_SUB":"按天服务（24小时）","SERVICE_PRICE":1,
	// // "SERVICE_ITEM_ID":2887,"SERVICE_TYPE_SUB_ID":1}]
	public void addData(Context context, JSONArray array) {
		if (array.size()>0) {
			initWidget(context, array);
		}
	}

	// 初始化控件
	public void initWidget(Context context,JSONArray array) {
		int count = array.size();
		// 整个布局
		group = new RadioGroup(context);
		group.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		group.setOrientation(VERTICAL);
		for (int i = 0; i < count; i++) {
			JSONObject object = array.getJSONObject(i);
			RelativeLayout v = (RelativeLayout) inflater.inflate(R.layout.service_type_item, null);
			v.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			RadioButton mRadioButton = (RadioButton) v.findViewById(R.id.rb);
			TextView mTextView = (TextView) v.findViewById(R.id.money);
//			TextView mContent = (TextView)v.findViewById(R.id.content);
			//赋值
			mRadioButton.setText(object.getString("SERVICE_TYPE_SUB"));
			mRadioButton.setTag(array.get(i).toString());
			mTextView.setText(object.getString("SERVICE_PRICE")+"元");
			//设置ID
			v.setId(DEFAUTGROUPID + GROUPSTEP * (i));
			mRadioButton.setId(DEFAUTGROUPID + GROUPSTEP * (i) + 1);
			mTextView.setId(DEFAUTGROUPID + GROUPSTEP * (i) + 2);
			mRadioButton.setOnClickListener(this);
			v.setOnClickListener(this);
			group.addView(v);
//			// 添加分割线
//			if (i < count - 1) {
//				// 分割线
//				ImageView iv = new ImageView(context);
//				iv.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, 1));
//				iv.setBackgroundResource(R.drawable.line);
//				group.addView(iv);
//			}
			
			
		}
		// 服务类型的个数 设置背景
		if (count == 1) { 
			group.findViewById(DEFAUTGROUPID).setBackgroundResource(R.drawable.text_layout_background_normal);
		} else {
			for (int i = 0; i < count; i++) {   
				if (i == 0) {
					group.findViewById(DEFAUTGROUPID).setBackgroundResource(R.drawable.textview_background_up);
				} else if (i == count - 1) {
					group.findViewById(DEFAUTGROUPID * count).setBackgroundResource(R.drawable.textview_background_down);
				} else {
					group.findViewById(DEFAUTGROUPID * (i + 1)).setBackgroundResource(R.drawable.textview_background_middle);
				}
			}
		}
		addView(group);
		group.check(DEFAUTGROUPID+1);
		if (listener != null) {
			group.setTag(getServiceTickMesg());
			listener.onClick(group);
		}
	}

	@Override
	public void onClick(View v) {
		if (v instanceof RelativeLayout) {
			if (mServiceTypeSubContentListener != null) {
				mServiceTypeSubContentListener.onClickServiceTypeSubContent((v.getId()/DEFAUTGROUPID) - 1);
			}
			
		}else {
			group.check(v.getId());
			if (listener != null) {
				//整个布局设置TAG
				RelativeLayout lin = (RelativeLayout) findViewById(v.getId()-1);
				
				lin.setTag(getServiceTickMesg());
				listener.onClick(lin);
			}
		}
	};
	
	
	//获取按钮信息
	public TickMesg getServiceTickMesg(){ 
		if (group != null) {
			return JSONObject.parseObject(findViewById(group.getCheckedRadioButtonId()).getTag().toString(), TickMesg.class);
		}
		return null;
	}
	
	public interface ServiceTypeSubContentListener{
		void onClickServiceTypeSubContent(int position);
	}

}
