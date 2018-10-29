package com.yksj.consultation.son.chatting;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.JsonHttpResponseHandler;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;

/**
 * 
 * 人体图症状详细内容
 * @author zhao
 *
 */
public class SymptomContentActivity extends BaseFragmentActivity implements OnClickListener {
	LinearLayout mRootLinearLayout;
	int currentId = -1;
	TextView mTitlTextV;
	String mTitleName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.symptom_content_layout);
		currentId = getIntent().getIntExtra("id", -1);
		mTitleName = getIntent().getStringExtra("title");
		mTitleName = mTitleName.concat(getString(R.string.explain));
		
		initUI();
		
		initData();
	}
	
	
	/**
	 * 
	 */
	private void initTitle1(){
		findViewById(R.id.title_back).setOnClickListener(this);
		mTitlTextV = (TextView)findViewById(R.id.title_txt);
		mTitlTextV.setText(mTitleName);
	}
	
	private void initUI(){
		initTitle1();
		
		mRootLinearLayout = (LinearLayout)findViewById(R.id.content_root);
		ScrollView scrollView = (ScrollView)mRootLinearLayout.getParent();
		try {
			Method method = scrollView.getClass().getMethod("setOverScrollMode",int.class);
			Object[] args1 = { new Integer(2)};
			method.invoke(scrollView.getClass(),args1);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initData(){
		HttpRestClient.doHttpQuerySymptomContent(String.valueOf(currentId),
				new JsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				onParseJson(response);
				super.onSuccess(statusCode, response);
			}
		});
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	public void onClick(View v){
		switch(v.getId()){
		case R.id.title_back:
			onBackPressed();
			break;
		}
	}
	
	
	/**
	 * 解析json内容
	 * @param value
	 */
	private void onParseJson(JSONObject jsonObject){
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		try{
			JSONArray jsonArray = jsonObject.getJSONArray("SITUATION_INFO");
			for (int i = 0; i < jsonArray.length(); i++) {
					jsonObject = jsonArray.getJSONObject(i);
					String title = jsonObject.getString("menucode");
					String content = jsonObject.getString("menuname");
					View view = layoutInflater.inflate(R.layout.symptom_content_item,null);
					TextView titleTextV = (TextView)view.findViewById(R.id.list_textv_tag);
					TextView contentTextV = (TextView)view.findViewById(R.id.symptom_content);
					titleTextV.setText(title);
					titleTextV.setSingleLine(false);
					contentTextV.setText(content);
					contentTextV.setSingleLine(false);
					mRootLinearLayout.addView(view);
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
	}
}
