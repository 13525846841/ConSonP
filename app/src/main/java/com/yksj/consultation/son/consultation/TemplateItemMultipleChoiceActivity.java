package com.yksj.consultation.son.consultation;

import java.util.ArrayList;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.yksj.consultation.adapter.MultipleTextChoiceAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;

/**
 * 动态病历模板的一个Item多选时跳转的ACtivity
 * 在这个Activity中进行多选,然后返回
 * @author lmk
 *
 */
public class TemplateItemMultipleChoiceActivity extends BaseFragmentActivity implements 
OnClickListener{

	private ListView mListView;
	private TextView tvName;
	private MultipleTextChoiceAdapter mAdapter;
	private ArrayList<Map<String,Object>> datas;
	
	@Override
	protected void onCreate(Bundle arg0) {
		
		super.onCreate(arg0);
		setContentView(R.layout.aty_template_item_multi_choise);
		initView();
		initData();
	}

	//初始化数据
	private void initData() {
		titleTextV.setText(getIntent().getStringExtra("title"));
		tvName.setText(getIntent().getStringExtra("title"));
		datas=(ArrayList<Map<String, Object>>) getIntent().getSerializableExtra("list");
		mAdapter=new MultipleTextChoiceAdapter(this);
		mListView.setAdapter(mAdapter);
		mAdapter.addAll(datas);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mAdapter.itemCheck(position);
			}
		});
	}

	private void initView() {
		initTitle();
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn2.setVisibility(View.VISIBLE);
		titleRightBtn2.setText(R.string.setting_tijiao);
		titleRightBtn2.setOnClickListener(this);
		mListView=(ListView) findViewById(R.id.multiple_choise_listview);
		tvName=(TextView) findViewById(R.id.multiple_choise_name);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back://返回
			onBackPressed();
			break;

		case R.id.title_right2://提交
//			ArrayList<Map<String,String>> result=new ArrayList<Map<String,String>>();
//			Iterator<Entry<Integer, Boolean>> it=mAdapter.isSelected.entrySet().iterator();
//			while(it.hasNext()){
//				Map.Entry<Integer, Boolean> entry=it.next();
//				if(entry.getValue()){
//					result.add(datas.get(entry.getKey()));
//				}
//			}
			
			Intent data=new Intent();
			data.putExtra("result", (ArrayList<Map<String, Object>>)mAdapter.datas);
			data.putExtra("title", tvName.getText().toString());
			setResult(RESULT_OK, data);
			this.finish();
			
			break;
		}
	}
	
	
}
