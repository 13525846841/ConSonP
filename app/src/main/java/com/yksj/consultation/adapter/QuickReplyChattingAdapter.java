package com.yksj.consultation.adapter;

import java.util.Collection;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.FaceParse;
/**
 * 快速回复adapter
 * @author jack_tang
 *
 */
public class QuickReplyChattingAdapter extends BaseAdapter{

	private OnClickDeleteListener clickListener;
	private FaceParse mFaceParse;
	private final LayoutInflater layoutInflater;
	private int TYPE=1;//1 表示没有删除  0表示有删除
	public JSONArray jsonArray=new JSONArray();
	private final LinkedHashMap<JSONObject, JSONObject> mChoosedMap = new LinkedHashMap<JSONObject, JSONObject>();// 已经选中的消息
    public interface OnClickDeleteListener {
        void onClickDelete(int position);
    }
    
    public void setOnClickDeleteListener(OnClickDeleteListener clickListener){
    	this.clickListener=clickListener;
    }
    
    
    public String getSelected(){
		JSONArray array =new JSONArray();
			Collection<JSONObject> collection = mChoosedMap.values();
			for (JSONObject json : collection) {
				array.put(json.optString("QUICK_REPLY_ID"));
			}
			return array.toString();
    }
    
	public QuickReplyChattingAdapter(Activity activity,int type) {
		this.mFaceParse = FaceParse.getChatFaceParse(activity);
		this.TYPE=type;
		layoutInflater = activity.getLayoutInflater();
	}
	public void setEdit(boolean isEdit){
		mChoosedMap.clear();
		if(isEdit){
			this.TYPE=0;
		}else{
			this.TYPE=1;
		}
	}
	
	public boolean isEdit(){
		return TYPE==0;
	}
	
	@Override
	public int getCount() {
		return jsonArray.length();
	}

	@Override
	public JSONObject getItem(int position) {
		try {
			return (JSONObject) jsonArray.get(position);
		} catch (JSONException e) {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void onBountData(JSONArray array){
		jsonArray=array;
		notifyDataSetChanged();
	}
	/**
	 * 获取id
	 * @param position
	 * @return
	 */
	public String getReply_id(int position){
		return getItem(position).optString("QUICK_REPLY_ID", "");
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		try {
				final JSONObject object = (JSONObject) jsonArray.get(position);
				if(convertView==null){
					holder =new ViewHolder();
					convertView=layoutInflater.inflate(R.layout.quick_chat_list_item, null);
					holder.image=(CheckBox)convertView.findViewById(R.id.selected);
					holder.content=(TextView)convertView.findViewById(R.id.content);
					convertView.setTag(holder);
				}else{
					holder= (ViewHolder) convertView.getTag();
				}
				holder=(ViewHolder) convertView.getTag();
				holder.content.setText(mFaceParse.parseSmileTxt((CharSequence) object.optString("QUICK_REPLY_CONTENT")));
		
				if(1==TYPE){
					holder.image.setVisibility(View.GONE);	
				}else{
					holder.image.setVisibility(View.VISIBLE);
				}
				
				holder.image.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						if (mChoosedMap.containsValue(object)) {
							mChoosedMap.remove(object);
						} else {
							mChoosedMap.put(object, object);
						}
					}
				});
				holder.image.setChecked(!mChoosedMap.containsKey(object));
			} catch (JSONException e) {
		}
		return convertView;
	}
	
	class ViewHolder{
		public TextView content;
		public CheckBox image;
	}

	public void selectAll() {
		mChoosedMap.clear();
		for (int i = 0; i < jsonArray.length(); i++) {
			mChoosedMap.put(jsonArray.optJSONObject(i), jsonArray.optJSONObject(i));
		}
		notifyDataSetChanged();
		
	}
}
