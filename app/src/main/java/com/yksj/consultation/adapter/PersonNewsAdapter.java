package com.yksj.consultation.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.yksj.healthtalk.entity.CollectAndPublishEntity;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.ToastUtil;

public class PersonNewsAdapter extends BaseAdapter {
	
	private Context con;
	public List<CollectAndPublishEntity>lists = new ArrayList<CollectAndPublishEntity>();
	public boolean type;
	public PersonNewsAdapter(Context context,List<CollectAndPublishEntity> entitys){
		
		this.con = context;
		this.lists.clear();
		this.lists.addAll(entitys);
	}
	
	@Override
	public int getCount() {
		
		if(lists.size()!=0){return lists.size();}else{
			return lists.size();
		}
		
	}
	@Override
	public Object getItem(int position) {
		return null;
	}
	@Override
	public long getItemId(int position) {
		return 0;
	}
	/**
	 * 是否消失全选框
	 * @param Type
	 */
	public void isSeleteAll(boolean Type){
		this.type = Type;
		notifyDataSetChanged();
	}
	
	public String getIds(){
		StringBuffer sb = new StringBuffer("");
		List<String>Ids = new ArrayList<String>();
		
		Iterator<CollectAndPublishEntity> iter = lists.iterator();  
		while(iter.hasNext()){  
			CollectAndPublishEntity s = iter.next();  
		    if(s.isCheck){  
//		    	Ids.add(s.MENUCODE);
		    	Ids.add(s.infoId);
		        iter.remove();  
		    }  
		}  
		
		
		for(int j=0;j<Ids.size();j++){
			sb.append(Ids.get(j)+",");
	   }
	 return sb.toString();
	}
	
	/**
	 * 是否全选
	 * @param isCheckAll
	 */
	public void isCheckAll(boolean isCheckAll){
		for(int j=0;j<lists.size();j++){
			lists.get(j).isCheck=isCheckAll;
		}
		notifyDataSetChanged();
	}
	/**
	 * 删除
	 */
	public void delete(){
		HttpRestClient.doHttpCancelCollectedInfoBatch(getIds(), new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				try {
					JSONObject jsonO = new JSONObject(content);
					if("0".equals(jsonO.optString("CODE"))){
						ToastUtil.showShort(jsonO.optString("INFO"));
						isSeleteAll(false);
						isCheckAll(false);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
		});
		
	}
//	public void delete(){
//		HttpRestClient.doHttpPersonDeleteNews(SmartFoxClient.getLoginUserId(),
//				getIds(), new AsyncHttpResponseHandler(){
//			@Override
//			public void onSuccess(int statusCode, String content) {
//				super.onSuccess(statusCode, content);
//				try {
//					JSONObject jsonO = new JSONObject(content);
//					if("0".equals(jsonO.optString("CODE"))){
//						ToastUtil.showShort(jsonO.optString("INFO"));
//						isSeleteAll(false);
//						isCheckAll(false);
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//			
//		});
//		
//	}
	
	public boolean deleteIsCheck(){
		
		for(int j=0;j<lists.size();j++){
			
			if(lists.get(j).isCheck==true){
				
				return true;
				
			}
			
		}
		return false;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final CollectAndPublishEntity entity = lists.get(position);
		if(convertView ==null){
			convertView = View.inflate(con, R.layout.collectandpublish_text_layout, null);
		}
		TextView content = (TextView) convertView.findViewById(R.id.col_content);
		content.setText(entity.MENUNAME);
		TextView time = (TextView) convertView.findViewById(R.id.col_time);
		time.setText(entity.MENUTIME);
		CheckBox box = (CheckBox) convertView.findViewById(R.id.coll_check);
		if(type){
			box.setVisibility(View.VISIBLE);
		}else{
			box.setVisibility(View.GONE);
		}
		box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				entity.isCheck = isChecked;
			}
		});
		box.setChecked(entity.isCheck);
		return convertView;
	}



	
	

	

}
