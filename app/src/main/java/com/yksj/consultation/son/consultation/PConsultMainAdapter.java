/**
 * 
 */
package com.yksj.consultation.son.consultation;

import java.util.Map;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.yksj.consultation.adapter.SimpleBaseAdapter;
import com.yksj.consultation.son.R;

/**
 * 六一健康首页适配器
 * @author zheng
 *
 */
public class PConsultMainAdapter extends SimpleBaseAdapter<Map<String,String>> {

	public PConsultMainAdapter(Context context) {
		super(context);
	}

	@Override
	public int getItemResource() {
		return R.layout.consultation_main_list_itme;
	}

	@Override
	public View getItemView(int position, View convertView,
			com.yksj.consultation.adapter.SimpleBaseAdapter.ViewHolder holder) {
		final Map<String ,String> map=datas.get(position);
		TextView name=(TextView) holder.getView(R.id.consultation_item_name);
//		TextView state=(TextView) holder.getView(R.id.join_state);
		name.setText(map.get("OFFICE_NAME"));
		
//		if(map.get("CON").equals("20")){//context.getResources().getColor(R.color.color_blue)
//			state.setVisibility(View.VISIBLE);
//			state.setTextColor(context.getResources().getColor(R.color.color_blue));
//			state.setText("已加入");
//		}if(map.get("CON").equals("10")){
//			state.setVisibility(View.VISIBLE);
//			state.setTextColor(context.getResources().getColor(R.color.color_orange_bg));
//			state.setText("审核中");
//		}
		return convertView;
	}


}
