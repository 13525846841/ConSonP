package com.yksj.consultation.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.WheelUtils;

/**
 * 动态模板里面ListView的适配器
 * 有各种类型
 * @author lmk
 *
 */
public class DynamicCaseTemplateAdapter extends SimpleBaseAdapter<JSONObject> {

	private Context mContext;
	private final LayoutInflater mInflater;
	PopupWindow mPopupWindow,pop;
	private PopupWindow agepop;// 年龄的pop
	private View view,wheelView;//滑轮选择器布局
	
	public DynamicCaseTemplateAdapter(Context context,View view,View wheelView) {
		super(context);
		this.wheelView=wheelView;
		this.view=view;
		mInflater=LayoutInflater.from(context);
	}
	
	@Override
	public int getItemResource() {
		return R.layout.apt_consultion_case_item_text;
	}
	@Override
	public View getItemView(int position, View convertView,
			com.yksj.consultation.adapter.SimpleBaseAdapter.ViewHolder holder) {
		JSONObject entity=datas.get(position);
		TextView tvCategoryTitle=(TextView) holder.getView(R.id.apt_case_template_item_title);
		TextView tvEditLeft=(TextView) holder.getView(R.id.apt_case_template_item_name);
		final TextView tvRight=(TextView) holder.getView(R.id.apt_case_template_item_text_right);//右边文本
		tvRight.setText(context.getResources().getString(R.string.please_choise));
		EditText editText=(EditText) holder.getView(R.id.apt_case_template_item_edit_right);//右边输入框
		editText.setText("");
		LinearLayout horLayout=(LinearLayout) holder.getView(R.id.apt_case_template_item_text_layout);
		EditText bigEditText=(EditText) holder.getView(R.id.apt_case_template_item_text_edit_big);//下面大输入框
		bigEditText.setText("");
		LinearLayout imgGridView=(LinearLayout) holder.getView(R.id.apt_case_template_item_img_layout);
		
		if(position>=1){
			JSONObject entity2=datas.get(position-1);
			if(!(entity2.optInt("CLASSID")==entity.optInt("CLASSID"))){//分类开始
				tvCategoryTitle.setVisibility(View.VISIBLE);
				tvCategoryTitle.setText(entity.optString("CLASSNAME"));//分类名称
			}else{
				tvCategoryTitle.setVisibility(View.GONE);
			}
			
		}else{//第一个CLASSNAME
			tvCategoryTitle.setVisibility(View.VISIBLE);
			tvCategoryTitle.setText(entity.optString("CLASSNAME"));//分类名称
		}
		/**
		 * ITEMTYPE  类型（10-文字填写，20-单选，30-多选，40-单数字填写，50-区域数字填写，60-日期）
			PIC 病历项图片支持（1-支持图片，0-不支持图片）
		 */
		switch (entity.optInt("ITEMTYPE")) {
		case 10://文字填写
			tvEditLeft.setText(entity.optString("ITEMNAME"));
			editText.setVisibility(View.VISIBLE);
			tvRight.setVisibility(View.GONE);
			break;
		case 20://单选
			tvEditLeft.setText(entity.optString("ITEMNAME"));
			tvRight.setVisibility(View.VISIBLE);
			editText.setVisibility(View.GONE);
			final ArrayList<Map<String, String>> selectors=parseArrayData(entity.optString("OPTION"));
			tvRight.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					setSingleSelector(selectors, tvRight);
				}
			});
			break;
		case 30://多选
			tvEditLeft.setText(entity.optString("ITEMNAME"));
			tvRight.setVisibility(View.VISIBLE);
			editText.setVisibility(View.GONE);
			break;
		case 40://单数字填写
			tvEditLeft.setText(entity.optString("ITEMNAME"));
			editText.setVisibility(View.VISIBLE);
			tvRight.setVisibility(View.GONE);
			break;
		case 50://区域数字填写
			tvEditLeft.setText(entity.optString("ITEMNAME"));
			editText.setVisibility(View.VISIBLE);
			tvRight.setVisibility(View.GONE);
			break;
		case 60://日期
			tvEditLeft.setText(entity.optString("ITEMNAME"));
			tvRight.setVisibility(View.VISIBLE);
			editText.setVisibility(View.GONE);
			tvRight.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setAgeDate(tvRight);
				}
			});
			break;
		}
		
		return convertView;
	}
	
	//解析JSONArray
	private ArrayList<Map<String, String>> parseArrayData(String srr) {
		ArrayList<Map<String, String>> list=new ArrayList<Map<String,String>>();
		try {
			JSONArray array=new JSONArray(srr);
			for(int i=0;i<array.length();i++){
				JSONObject object=array.getJSONObject(i);
				HashMap< String, String> map=new HashMap<String, String>();
				map.put("name", object.optString("OPTIONNAME"));
				map.put("code", ""+object.optInt("OPTIONID"));
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

//	/**
//	 * 设置性别
//	 */
//	private void setSingleSelect(ArrayList<Map<String,String>> list) {
//		String[] xingbie = context.getResources().getStringArray(R.array.sex);
//		pop = new PopupWindow(wheelView, LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
//		WheelUtils.setSingleWheel(context, xingbie, view, pop, wheelView,false);
//	}
	
	/**
	 * 设置单选滑动选择器
	 */
	private void setSingleSelector(final ArrayList<Map<String,String>> list,final TextView tv){
		if(mPopupWindow != null && mPopupWindow.isShowing())mPopupWindow.dismiss();
		mPopupWindow = WheelUtils.showSingleWheel(context,list,tv,new OnClickListener() {
			@Override
			public void onClick(View v) {
				int index1 = (Integer)v.getTag(R.id.wheel_one);
				Map<String,String> map = list.get(index1);
				String name = map.get("name");
				map.get("code");
				tv.setText(name);
			}
		});
	}
	
	/**
	 * 设置出生日期
	 */
	private void setAgeDate(final TextView tvAge) {
		if(agepop==null){
			agepop=WheelUtils. showThreeDateWheel(context, view, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					switch (v.getId()) {
					case R.id.wheel_sure_age:
						String[] str = (String[]) v.getTag();
						tvAge.setText(str[0] + str[1] + str[2]);
						break;
					}
				}
			});	
		}else{
			agepop.showAtLocation(view,Gravity.BOTTOM, 0, 0);
		}
	}
	
	/**
	 * 用户填写完成后
	 * 遍历得到Json字符串作为提交内容
	 */
	public void getPostJsonString(){
		
	}



}
