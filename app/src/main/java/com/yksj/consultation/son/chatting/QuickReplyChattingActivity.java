package com.yksj.consultation.son.chatting;

import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.JsonHttpResponseHandler;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.consultation.adapter.QuickReplyChattingAdapter;
import com.yksj.consultation.adapter.QuickReplyChattingAdapter.OnClickDeleteListener;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.comm.FacePanelFragment;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog.OnDilaogClickListener;
import com.yksj.consultation.comm.FacePanelFragment.FaceItemOnClickListener;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.FaceParse;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.ToastUtil;
/**
 * 
 * @author jack_tang
 * 聊天快速回复
 *
 */
public class QuickReplyChattingActivity extends BaseFragmentActivity implements FaceItemOnClickListener,OnClickListener, OnClickDeleteListener{

	private PullToRefreshListView listView;
	private EditText mChatEdit;
	private View mFacePanelV;//表情
	private QuickReplyChattingAdapter mAdapter;
	private Button mDelete , mCancle;
	private View mEmptyView; 
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.quickreply_chatting_layout);
		initView();
	}
	private void initView() {
		initTitle();
		titleLeftBtn.setOnClickListener(this);
		titleTextV.setText("快速回复设置");
		listView = (PullToRefreshListView) findViewById(R.id.pull_refresh_listview);
		titleRightBtn2.setVisibility(View.VISIBLE);
		titleRightBtn2.setBackgroundResource(R.drawable.ig_delete);
		titleRightBtn2.setOnClickListener(this);
		mDelete = (Button) findViewById(R.id.chat_delete_btn2);
		mDelete.setOnClickListener(this);
		mCancle = (Button) findViewById(R.id.chat_delete_btn3);
		mCancle.setOnClickListener(this);
		findViewById(R.id.face_btn).setOnClickListener(this);
		findViewById(R.id.chat_send_btn).setOnClickListener(this);
		mFacePanelV = findViewById(R.id.face_panel);
		mChatEdit = (EditText) findViewById(R.id.chat_edit);
		mChatEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				mFacePanelV.setVisibility(View.GONE);
			}
		});
		
		mChatEdit.setOnClickListener(this);
		FacePanelFragment facePanelFragment = (FacePanelFragment)getSupportFragmentManager().findFragmentById(R.id.face_fragment);
		facePanelFragment.setmFaceItemOnClickListener(this);
		mAdapter=new QuickReplyChattingAdapter(this,1);
		mAdapter.setOnClickDeleteListener(this);
		listView.setAdapter(mAdapter);
		mFacePanelV.setVisibility(View.GONE);
		mEmptyView = findViewById(R.id.empty_view);
		listView.getRefreshableView().setDividerHeight(2);
		initData();
	}
	
	/**
	 * 查询
	 */
	private void initData(){
		RequestParams params=new RequestParams();
		params.put("Type", "queryQuick");
		params.put("CUSTOMER_ID",SmartFoxClient.getLoginUserId());
		HttpRestClient.doHttpQUICKREPLYSERVLET(params, new JsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, JSONArray response) {
				super.onSuccess(statusCode, response);
				mAdapter.onBountData(response);
				if(mAdapter.jsonArray.length() == 0){
					mEmptyView.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
				}else{
					mEmptyView.setVisibility(View.GONE);
					listView.setVisibility(View.VISIBLE);
				}
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			onBackPressed();
			break;
		case R.id.face_btn://点击表情
			onFaceBtnClick(v);
			break;
		case R.id.chat_send_btn://发送
			onSendTxtMessage();
			break;
		case R.id.chat_edit://隐藏表情
			mFacePanelV.setVisibility(View.GONE);
			break;
		case R.id.title_right2://删除
			if(mAdapter.isEdit())mAdapter.selectAll();
			else onDeleteAction();
			break;
		case R.id.chat_delete_btn2://删除操作
			delete();
			break;
		case R.id.chat_delete_btn3://取消删除操作
			onDeleteAction();
			break;
		}
	}
	
	private void delete() {
		if(HStringUtil.isEmpty(mAdapter.getSelected()))return;
		/**
		 * 选择删除快速回复
			QuickReplyUpSavServlet
			Type=deleteSeleteQuick
			QUICK_REPLY_ID 内容id（把多个打包成jsonarray传过来）
		 */
		RequestParams params =new RequestParams();
		params.put("Type", "deleteSeleteQuick");
		params.put("QUICK_REPLY_ID",mAdapter.getSelected());
		params.put("CUSTOMER_ID",SmartFoxClient.getLoginUserId());
		HttpRestClient.doHttpQUICKREPLYUPSAVSERVLET(params, new JsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				super.onSuccess(statusCode, response);
				if(0!=response.optInt("error_code")){
					initData();
				}
				mChatEdit.setText(null);
				ToastUtil.showShort(response.optString("error_message"));
			}
		});
	}
	
	private void onDeleteAction() {
		if(mAdapter.isEdit()){
			titleRightBtn2.setBackgroundResource(R.drawable.ig_delete);
			titleRightBtn2.setText("");
			mAdapter.setEdit(false);
			findViewById(R.id.chat_input_panel).setVisibility(View.VISIBLE);
			findViewById(R.id.delete_panel).setVisibility(View.GONE);
		}else{
			titleRightBtn2.setBackgroundDrawable(null);
			titleRightBtn2.setText("全选");
			mAdapter.setEdit(true);
			findViewById(R.id.chat_input_panel).setVisibility(View.GONE);
			findViewById(R.id.delete_panel).setVisibility(View.VISIBLE);
		}
	}
	
	
	/**
	 * 表情点击
	 */
	private void onFaceBtnClick(View v){
		if(mFacePanelV.getVisibility() == View.GONE){
			SystemUtils.hideSoftBord(getApplicationContext(), mChatEdit);
			mFacePanelV.setVisibility(View.VISIBLE);
		}else{
			mFacePanelV.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 保存
	 */
	private void onSendTxtMessage(){
		SystemUtils.hideSoftBord(getApplicationContext(), mChatEdit);
		String content = mChatEdit.getEditableText().toString();
		mFacePanelV.setVisibility(View.GONE);
		if(content.trim().length() != 0){
			questQuickReplyContent(true,content);
		}
	}

	/**
	 * 快速回复
	 * 编辑快速回复
	QuickReplyUpSavServlet
	Type=saveQuick
	QUICK_REPLY_CONTENT 内容
	CUSTOMER_ID 客户id

	删除快速回复
	QuickReplyUpSavServlet
	Type=deleteQuick
	QUICK_REPLY_ID 内容id
	boolean true 保存 false 删除 
	 */
	public void questQuickReplyContent(final boolean isSave,String typeContent){
		RequestParams params=new RequestParams();
		if(isSave){
			params.put("Type", "saveQuick");
			params.put("QUICK_REPLY_CONTENT",typeContent);
		}else{
			params.put("Type", "deleteQuick");
			params.put("QUICK_REPLY_ID",typeContent);
		}
		params.put("CUSTOMER_ID",SmartFoxClient.getLoginUserId());
		
		HttpRestClient.doHttpQUICKREPLYUPSAVSERVLET(params, new JsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				super.onSuccess(statusCode, response);
				if(0!=response.optInt("error_code")){
					initData();
				}
				mChatEdit.setText(null);
				ToastUtil.showShort(response.optString("error_message"));
			}
		});
	}
	
	
	/**
	 * 点击表情处理
	 */
	@Override
	public void onItemClick(String text, Drawable drawable, FaceParse mFaceParse) {
		mFaceParse.insertToEdite(mChatEdit,drawable,text);
	}

	/**
	 * 删除
	 */
	@Override
	public void onClickDelete(final int position) {
		
		DoubleBtnFragmentDialog.show(getSupportFragmentManager(), "提示", "您确定要删除此快速回复内容吗？", "取消", "确定", new OnDilaogClickListener() {
			@Override
			public void onDismiss(DialogFragment fragment) {
				fragment.dismissAllowingStateLoss();
			}
			
			@Override
			public void onClick(DialogFragment fragment, View v) {
				questQuickReplyContent(false,mAdapter.getReply_id(position));
			}
		});
		
	}
}
