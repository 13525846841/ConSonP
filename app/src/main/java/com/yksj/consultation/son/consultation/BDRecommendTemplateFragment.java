package com.yksj.consultation.son.consultation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yksj.consultation.adapter.MultipleTextChoiceAdapter;
import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.utils.JsonParseUtils;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * 基层医生推荐病历模板界面
 * @author lmk
 */
public class BDRecommendTemplateFragment extends RootFragment {
	private MultipleTextChoiceAdapter mAdapter;
	private ArrayList<Map<String,Object>> datas;
	private View view;
	private Button btnSure;//完成按钮
	private ListView mListview;//
	private TextView tvName,tvTip;//名称
	private LinearLayout choiseLayout;
	private ArrayList<Map<String, Object>> list;
	
	DAtyConsultDetails dActivity;//父Activity
	private int cusPos=-1;//选择的位置
	private String consultationId;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fgt_consultation_recommend_template, null);
		dActivity=(DAtyConsultDetails) getActivity();
		initView();
		return view;
	}

	//初始化视图
	private void initView() {
		Bundle bundle=getArguments();
		consultationId=bundle.getString("consultationId");
		tvTip=(TextView) view.findViewById(R.id.single_choise_tip);
		choiseLayout=(LinearLayout) view.findViewById(R.id.single_choise_layout);
		btnSure=(Button) view.findViewById(R.id.single_choise_sure);
		tvName=(TextView) view.findViewById(R.id.single_choise_name);
		tvName.setText(R.string.please_chose_template);
		mListview=(ListView) view.findViewById(R.id.single_choise_listview);
		mAdapter=new MultipleTextChoiceAdapter(getActivity());
		mListview.setAdapter(mAdapter);
		btnSure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				submitTemplate();//提交模板
			}
			
		});
		mListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mAdapter.itemCheck(position);
				if(cusPos==position){//选中的位置当前位置一样
					cusPos=-1;
				}else{//不一样
					if(cusPos!=-1)//
						mAdapter.itemCheck(cusPos);
					cusPos=position;
				}
			}
		});
		initData();
	}
	
	//提胶模板给患者填写的接口
	private void submitTemplate() {
		if(cusPos==-1){
			ToastUtil.showShort("请先选择病历模板");
			return;
		}
		String itemId=list.get(cusPos).get("code").toString();
		final String templateName=list.get(cusPos).get("name").toString();
		HttpRestClient.doHttpConsultionCaseTemplate("5",itemId, consultationId, new AsyncHttpResponseHandler(getActivity()){

			@Override
			public void onSuccess(String content) {
				try {
					JSONObject object=new JSONObject(content);
					if(object.has("errormessage")){
						ToastUtil.showShort(object.optString("errormessage"));
					}else{
						ToastUtil.showShort(object.optString("INFO"));
						tvTip.setVisibility(View.VISIBLE);
						tvTip.setText("已为患者选择 ："+templateName+"，等待患者填写,请到“会诊中”页签查看患者填写情况。");
						choiseLayout.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(content);
			}
			
		});
		
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser){
			if(dActivity.state==1){
				initData();
			}
		}
	}

	//初始化数据
	private void initData() {
		HttpRestClient.doHttpConsultionCaseTemplate("4",null, consultationId, new AsyncHttpResponseHandler(getActivity()){

			@Override
			public void onSuccess(String content) {
				try {
					JSONObject object=new JSONObject(content);
					if("1".equals(object.optString("TYPE"))){//没有发送模板给患者
						tvTip.setVisibility(View.GONE);
						choiseLayout.setVisibility(View.VISIBLE);
						String arrayStr=object.optString("CONTENT");
						list=JsonParseUtils.parseMultipleChoiseData(arrayStr);
						if(list.size()!=0){
							mAdapter.onBoundData(list);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(content);
			}
			
		});
	}
	
}
