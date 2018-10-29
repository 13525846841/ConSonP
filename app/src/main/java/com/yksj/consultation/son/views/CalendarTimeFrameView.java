package com.yksj.consultation.son.views;

import java.util.Iterator;
import java.util.LinkedHashMap;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSONObject;
import com.yksj.consultation.son.R;

/**
 * 日历 时间间隔
 * 
 * @author Administrator
 * 
 */
public class CalendarTimeFrameView extends RadioGroup implements OnClickListener {
	private int id = 1;
	private int count;
	private LinkedHashMap<String, JSONObject> mLinkedHashMap; // key 时间间隔 value// item_id
	private OnClickListener listener;
	private PayListener payListener;
	public CalendarTimeFrameView(Context context) {
		super(context);
		if (context instanceof OnClickListener) {
			listener = (OnClickListener) context;
		}
		if (context instanceof PayListener) {
			payListener = (PayListener) context;
		}
		mLinkedHashMap = new LinkedHashMap<String, JSONObject>();
		
	}

	public CalendarTimeFrameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (context instanceof PayListener) {
			payListener = (PayListener) context;
		}
		if (context instanceof OnClickListener) {
			listener = (OnClickListener) context;
		}
		mLinkedHashMap = new LinkedHashMap<String, JSONObject>();
		
	}

	// 添加button
	private void addView(Context context) {
		LinearLayout layout = new LinearLayout(context);
		layout.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		layout.setOrientation(VERTICAL);
		for (int i = 0; i < 2; i++) {
			RadioButton button = new RadioButton(context);
			button.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
			button.setButtonDrawable(R.drawable.login_checkbox_choice);
			button.setId(id);
			button.setOnClickListener(this);
			button.setTextColor(context.getResources().getColor(R.color.tab_txt_gray));
			id++;
			layout.addView(button);
		}
		addView(layout);
	}

	public void setButtonCount(int count) {
		this.count = count;
	}

	public void initView(Context context, LinkedHashMap<String, JSONObject> map) {
		id = 1;//初始化ID
		removeAllViews();
		clearCheck();
		mLinkedHashMap.clear();
		mLinkedHashMap.putAll(map);
		int count = map.size();
		// 加载布局
		if (count % 2 == 0) {
			for (int i = 0; i < count / 2; i++) {
				addView(context);
			}
		} else {
			// 隐藏最后一个
			for (int i = 0; i < (count + 1) / 2; i++) {
				addView(context);
			}
			findViewById(count + 1).setVisibility(View.INVISIBLE);
		}

		//设置显示值
		Iterator<String> iterator = mLinkedHashMap.keySet().iterator();
		for (int i = 0; i < mLinkedHashMap.keySet().size(); i++) {
			if (iterator.hasNext()) {
				((RadioButton)findViewById(i+1)).setText(iterator.next());
			}
		}
		
	}

	@Override
	public void onClick(View v) {
		this.check(v.getId());
		v.setTag(setObject());
		if (listener != null) {
			listener.onClick(v);
		}
		if (payListener != null) {
			payListener.isCanTalkOrBuy(isBuy(), isTalk(),setObject());
		}
		
	}
	
	//购买服务所传参数
	public String  getItemId (){
		if (getCheckedRadioButtonId() != -1) {
			RadioButton button  =  (RadioButton)findViewById(getCheckedRadioButtonId());
			if (button == null) {
				return null;
			}
			JSONObject object = mLinkedHashMap.get(button.getText());
			return object.getString("SERVICE_ITEM_ID");
		}else {
			return null;
		}
	}
	
	public JSONObject setObject(){
			RadioButton button  =  (RadioButton)findViewById(getCheckedRadioButtonId());
			JSONObject object = null;
			if (button != null ) {
				 object = mLinkedHashMap.get(button.getText());
			}
			return object;
	}
	
	
	public Boolean isBuy(){
		RadioButton button  =  (RadioButton)findViewById(getCheckedRadioButtonId());
		JSONObject object = mLinkedHashMap.get(button.getText());
		if (object.getString("ISBUY").equals("0")) {
			return false;
		}else {
			return true;
		}
		
}
	
	public Boolean isTalk(){
		RadioButton button  =  (RadioButton)findViewById(getCheckedRadioButtonId());
		JSONObject object = mLinkedHashMap.get(button.getText());
		if (object.getString("ISTALK").equals("0")) {
			return false;
		}else {
			return true;
		}
	}
	
	public interface PayListener{
		void isCanTalkOrBuy(Boolean isBuy,Boolean isTalk,JSONObject object);
	}
	
}
