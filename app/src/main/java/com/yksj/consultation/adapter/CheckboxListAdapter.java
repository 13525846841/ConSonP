package com.yksj.consultation.adapter;

import org.universalimageloader.core.ImageLoader;

import android.graphics.drawable.LevelListDrawable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.son.R;


public class CheckboxListAdapter extends BaseAdapter {
	public final JSONArray mChoosedArray = new JSONArray();
	public final JSONArray mOtherArray = new JSONArray();//已经在其他分组当中的成员
	JSONArray mArray = new JSONArray();
	LayoutInflater mInflater;
	private final ImageLoader mImageLoader;
	public String mPatient_Group_ID;
	private final FragmentManager mFragmentManager;
	
	public CheckboxListAdapter(LayoutInflater inflater,FragmentManager fragment){
		this.mInflater = inflater;
		this.mImageLoader = ImageLoader.getInstance();
		mFragmentManager=fragment;
	}
	
	public void onDataChange(JSONArray jsonArray){
		mChoosedArray.clear();
		mOtherArray.clear();
		this.mArray = jsonArray;
		notifyDataSetChanged();
	}
	
	public JSONArray getChooseMap(){
		return mChoosedArray;
	}
	
	public void onChoose(Object obj,boolean isChoosed){
		if(isChoosed){
			if(!mChoosedArray.contains(obj)){
				mChoosedArray.add(obj);
			}
		}else{
			mChoosedArray.remove(obj);
		}
	}
	
	@Override
	public int getCount() {
		return mArray.size();
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public JSONObject getItem(int position) {
		return mArray.getJSONObject(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		final JSONObject object = getItem(position);
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.commn_checkbox_list_item,null);
			viewHolder.headerImageV = (ImageView)convertView.findViewById(R.id.head_image);
			viewHolder.nameTextV = (TextView)convertView.findViewById(R.id.name);
			viewHolder.sexImageV = (ImageView)convertView.findViewById(R.id.head_sex);
			viewHolder.levelImageV = (ImageView)convertView.findViewById(R.id.levl);
			viewHolder.noteTextV = (TextView)convertView.findViewById(R.id.note);
			viewHolder.chooseBox = (CheckBox)convertView.findViewById(R.id.choose);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		LevelListDrawable listDrawable = (LevelListDrawable)viewHolder.sexImageV.getDrawable();
		String sex = object.getString("CUSTOMER_SEX");
		if("W".equalsIgnoreCase(sex)){
			sex = "2";
		}else if("M".equalsIgnoreCase(sex)){
			sex = "1";
		}else if("X".equalsIgnoreCase(sex)){
			sex = "0";
		}
		try {
			listDrawable.setLevel(Integer.valueOf(sex));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		String account = null;
		if(object.getString("display_name") == null){
			String nickName = object.getString("CUSTOMER_NICKNAME");
			account = object.getString("CUSTOMER_ACCOUNTS");
			if(nickName != null){
				account = nickName +"("+account+")";
			}
			object.put("display_name", account);
		}else{
			account=object.getString("display_name");
		}
		String name = object.getString("PERSONAL_NARRATE");
		viewHolder.noteTextV.setText(name);
		viewHolder.nameTextV.setText(account);
		viewHolder.chooseBox.setChecked(mChoosedArray.contains(object));
		viewHolder.chooseBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox box  = (CheckBox)v;
				if(SmartFoxClient.getLoginUserId().equals(object.getString("CUSTOMER_ID"))){
					SingleBtnFragmentDialog.showDefault(mFragmentManager,  "您不能把自己添加进特殊收费人群");
					box.setChecked(false);
					return;
				}
				
				if(box.isChecked()){
					String Patient_Group_ID = object.getString("SPECIAL_GROUP_ID");
					if(Patient_Group_ID != null && Patient_Group_ID.length() != 0 && !Patient_Group_ID.equals(mPatient_Group_ID)){
						mOtherArray.add(object);
					}
					mChoosedArray.add(object);
				}else{
					mChoosedArray.remove(object);
					mOtherArray.remove(object);
				}
			}
		});
		mImageLoader.displayImage(sex,object.getString("CLIENT_ICON_BACKGROUND"),viewHolder.headerImageV);
		return convertView;
	}
	
	/**
	 * 选择回调
	 * @param position
	 */
	public void onCheck(int position){
		JSONObject jsonObject = getItem(position);
		if(mChoosedArray.contains(jsonObject))mChoosedArray.remove(jsonObject);
		else mChoosedArray.add(jsonObject);
		notifyDataSetChanged();
	}
	
	private static class ViewHolder{
		ImageView headerImageV;
		TextView nameTextV;
		ImageView sexImageV;
		ImageView levelImageV;
		TextView noteTextV;
		CheckBox chooseBox;
	}
}
