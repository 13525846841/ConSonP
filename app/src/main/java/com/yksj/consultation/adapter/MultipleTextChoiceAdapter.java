package com.yksj.consultation.adapter;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.yksj.consultation.son.R;

public class MultipleTextChoiceAdapter extends SimpleBaseAdapter<Map<String,Object>> {

	public HashMap<Integer, Boolean> isSelected; 
	
	public MultipleTextChoiceAdapter(Context context) {
		super(context);
		isSelected = new HashMap<Integer, Boolean>(); 
	}
	
	public void itemCheck(int pos){
		Boolean check=(Boolean) datas.get(pos).get("isChecked");
		datas.get(pos).put("isChecked", !check);
		notifyDataSetChanged();
	}

	@Override
	public int getItemResource() {
		return R.layout.template_multiple_choise_item_layout;
	}

	@Override
	public View getItemView(final int position, View convertView,
			com.yksj.consultation.adapter.SimpleBaseAdapter.ViewHolder holder) {
		Map<String, Object> map=datas.get(position);
		CheckBox cb=(CheckBox) holder.getView(R.id.template_multiple_choise_item_checkbox);
		TextView tvName=(TextView) holder.getView(R.id.template_multiple_choise_item_name);
		tvName.setText((CharSequence) map.get("name"));
		cb.setChecked((Boolean) map.get("isChecked"));
//		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				isSelected.put(position, isChecked);
//			}
//		});
//		if(isSelected.containsKey(position)){
//			cb.setChecked(isSelected.get(position));
//		}else{
//			cb.setChecked(false);
//		}
		
		return convertView;
	}

}
