package com.yksj.consultation.comm;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.WheelUtils;
/**
 * 说明
 * @author Administrator
 * intent.putExtra(CommonExplainActivity.TITLE_NAME, value);  //title name
 * intent.putExtra(CommonExplainActivity.TEXT_CONUT, value);  //字数限制  默认是200
 * intent.putExtra(CommonExplainActivity.TEXT_CONTENT, value);  //内容
 * intent.putExtra(CommonExplainActivity.DESC, "认真填写取消预约原因，有助于得到对方的理解"); // 描述
 * intent.putExtra(CommonExplainActivity.RIGHT_TEXT, "确定"); // 描述
 * intent.putExtra(CommonExplainActivity.CONTENT_NOT_NULL, true); // 不能为空

 * 
 * 返回 内容
 * Intent intent=getIntent();
	intent.putExtra("content", value);
	setResult(RESULT_OK, intent);
	finish();
 */
public class CommonExplainActivity extends BaseFragmentActivity implements OnClickListener{
	private EditText mEditText;
	private TextView textNum;
	private int mContentCount=200;//字数限制
	private int textNumber = 0;
	private TextView mDesc ;//描述的的TextView
	public static String DESC = "DESC";//描述
	public static String RIGHT_TEXT = "RIGHT_TEXT";//右上角按钮的文字
	public static String TITLE_NAME = "TITLE_NAME";//右上角按钮的文字
	public static String TEXT_CONUT = "TEXT_CONUT";//文字数量
	public static String TEXT_CONTENT = "TEXT_CONTENT";//文字内容
	public static String CONTENT_NOT_NULL = "CONTENT_NOT_NULL";//文字内容
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.common_base_explain);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		initView();
	}
	private void initView() {
		initTitle();
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn2.setOnClickListener(this);
		titleRightBtn2.setVisibility(View.VISIBLE);
//		titleTextV.setText(getIntent().getStringExtra("title"));
		mDesc = (TextView) findViewById(R.id.msg_txt);
		mContentCount=getIntent().getIntExtra(TEXT_CONUT, 200);
		textNum = (TextView)findViewById(R.id.textcount);
		textNum.setText("0/"+mContentCount);
		mEditText = (EditText)findViewById(R.id.setting_feedback_text);
		mEditText.addTextChangedListener(textWatcher); 
		mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mContentCount)});
		
		if(getIntent().hasExtra(RIGHT_TEXT)){
			titleRightBtn2.setText(getIntent().getStringExtra(RIGHT_TEXT));
		}else{
			titleRightBtn2.setText("存储");
		}
		
		
		
		if(getIntent().hasExtra(TITLE_NAME)){
			titleTextV.setText(getIntent().getStringExtra(TITLE_NAME));
		}
		
		
		if(getIntent().hasExtra("hintcontent")){
			mEditText.setHint(getIntent().getStringExtra("hintcontent"));
		}
		
		
		if(getIntent().hasExtra(DESC)){//温馨提示
			mDesc.setText(getIntent().getStringExtra(DESC));
			findViewById(R.id.message).setVisibility(View.VISIBLE);
		}
		
		if(getIntent().hasExtra(TEXT_CONTENT)){
			String str=getIntent().getStringExtra(TEXT_CONTENT);
			if(!HStringUtil.isEmpty(str)){
				mEditText.setText(str);
				mEditText.setSelection(str.length());
			}
			
		}
		
		//如果是公告板回复,就显示留言说明
		if(getIntent().hasExtra("message_leave")){
			findViewById(R.id.message_leave).setVisibility(View.VISIBLE);
		}
		
		if(getIntent().hasExtra("title")){
//			if ("取消预约原因".equals(titleTextV.getText().toString())) {
//				findViewById(R.id.message).setVisibility(View.VISIBLE);
//			}
			
			if (getIntent().getStringExtra(TITLE_NAME).contains("手机")) {
				String digits = "1234567890";
				mEditText.setKeyListener(DigitsKeyListener.getInstance(digits));   
			}
		}

	}

	
	/**
	 * 文字监听
	 */
	private TextWatcher textWatcher = new TextWatcher() {  
        @Override    
        public void afterTextChanged(Editable s) { 
        	
        }   
        @Override 
        public void beforeTextChanged(CharSequence s, int start, int count,  
                int after) {  
        }  
         @Override    
        public void onTextChanged(CharSequence s, int start, int before,     
                int count) {   
      	 if((start +count)<=mContentCount && mEditText.getText().toString().length()<=mContentCount){
        		 textNumber = mEditText.getText().toString().length();
        		 textNum.setText(textNumber+"/"+mContentCount); 
        	 }else{
        		 mEditText.setText(s.subSequence(0, mContentCount));
        		 ToastUtil.showShort(CommonExplainActivity.this, "最多可输入"+mContentCount+"个字符");
        	 }
        }                     
    };  
    
	@Override
	public void onClick(View v) {
			switch (v.getId()) {
			case R.id.title_back:
				SystemUtils.hideSoftBord(this,mEditText);
				finish();
				break;
				
			case R.id.title_right2:
				submitFeedback();
				break;
			}
		}
	
	/**
	 * 提交意见内容
	 */
	private void submitFeedback(){
		WheelUtils.hideInput(CommonExplainActivity.this,mEditText.getWindowToken());
		final String value = mEditText.getEditableText().toString();
		if(getIntent().hasExtra(CONTENT_NOT_NULL) && HStringUtil.isEmpty(value))
		{
			SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "内容不能为空.");
		}else{
			Intent intent=getIntent();
			intent.putExtra("content", value);
			setResult(RESULT_OK, intent);
			finish();
		}

	}

}
