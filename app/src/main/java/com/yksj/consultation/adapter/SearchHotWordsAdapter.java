package com.yksj.consultation.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.yksj.consultation.son.R;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 搜索医生搜索历史的适配器
 * @author lmk
 *
 */
public class SearchHotWordsAdapter extends SimpleBaseAdapter<HashMap<String,String>> {

	public SearchHotWordsAdapter(Context context) {
		super(context);
	}
	
	public void setData(ArrayList<HashMap<String,String>> data){
		datas=data;
	}

	@Override
	public int getItemResource() {
		return R.layout.search_doctor_hot_item;
	}

	@Override
	public View getItemView(int position, View convertView,
			com.yksj.consultation.adapter.SimpleBaseAdapter.ViewHolder holder) {
		Button btn=(Button) convertView.findViewById(R.id.search_hot_item_num);
		TextView text=(TextView) convertView.findViewById(R.id.search_hot_item_text);
		btn.setText(position+1+"");
		if(position==0){
			btn.setBackgroundResource(R.drawable.search_hot_words_level1);
		}else if(position==1){
			btn.setBackgroundResource(R.drawable.search_hot_words_level2);
		}else if(position==2){
			btn.setBackgroundResource(R.drawable.search_hot_words_level3);
		}else{
			btn.setBackgroundResource(R.drawable.search_hot_words_level_normal);
		}
		text.setText(datas.get(position).get("name"));
		return convertView;
	}
	
}
