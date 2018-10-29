package com.yksj.consultation.son.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.dmsj.newask.http.LodingFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.MyZoneGroupEntity;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 开放指定设置
 * @author crj
 *
 */
public class ShopOpenContactFragment extends Fragment implements OnItemClickListener,OnClickListener{

	//改fragment标志位
	public static final int TAG_SHOPSHOW = 1;
	
	private ListView openListView;
	private String[] arrays = new String[] {
			"全部开放","全部关闭","指定人"
	};
	/**
	 * 初始化的position
	 */
	private int initPosition ;
	/**
	 * 表示点击了 哪个分组进去的此界面。classid为点击分组id
	 */
	private String classId;
	/**
	 * 单选适配器
	 */
	private SingleChoiseAdapter adapter;
	/**
	 * 返回按钮监听器
	 */
	private OnBackClickListener backClickListener;

	private String shopId;
	List<MyZoneGroupEntity> entities;
	private int clickPosition;

	
	public static ShopOpenContactFragment getIntance(String classId,int position,String shopId,List<MyZoneGroupEntity> entities,int clickPosition) {
		ShopOpenContactFragment f = new ShopOpenContactFragment();
		Bundle mBundle = new Bundle();
		mBundle.putParcelableArrayList("entities", (ArrayList<? extends Parcelable>) entities);
		mBundle.putString("classId", classId);
		mBundle.putInt("position", position);
		mBundle.putInt("clickPosition", clickPosition);
		mBundle.putString("shopId", shopId);
		f.setArguments(mBundle);
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		classId = getArguments().getString("classId");
		initPosition = getArguments().getInt("position");
		shopId = getArguments().getString("shopId");
		clickPosition = getArguments().getInt("clickPosition");
		entities =getArguments().getParcelableArrayList("entities");
		initData();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.shop_show_main, container,false);
		intView(view);
		return view;
	}
	
	private void intView(View view){
		initTitle(view);
		openListView = (ListView) view.findViewById(R.id.openselectlistview);
		openListView.setAdapter(adapter);
		openListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		openListView.setOnItemClickListener(this);
		//避免刚开始初始化的时候没有任何一项被选择
		if(initPosition!=0){
			initPosition=initPosition-1;
			adapter.setPosition(initPosition);//如果初始化位置不为0 则需要减1	
		}
		else adapter.setPosition(0);
	}
	
	public void initTitle(View view) {
		view.findViewById(R.id.title_back).setOnClickListener(this);
		TextView titleLable=(TextView)view.findViewById(R.id.title_lable);
		titleLable.setText("分类设置");
		titleLable.setTextColor(Color.BLACK);
		Button rightButton = (Button) view.findViewById(R.id.title_right2);
		rightButton.setVisibility(View.VISIBLE);
		rightButton.setOnClickListener(this);
		rightButton.setText("确定");
	}
	
	public void initData(){
		adapter =  new SingleChoiseAdapter(getActivity(), Arrays.asList(arrays));
	}
	
	private class SingleChoiseAdapter extends BaseAdapter {
		
		private int choicePosition = 0;
		private List<String> list;
		private LayoutInflater inflater;
		
		public SingleChoiseAdapter(Context context,List<String> list) {
			this.list = list;
			inflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView==null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.shop_show_item, parent,false);
				holder.mTextView = (TextView) convertView.findViewById(R.id.shop_show_text);
				holder.mButton = (CheckBox) convertView.findViewById(R.id.shop_show_radio);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				if(holder.mButton.isChecked()) {
					holder.mButton.setChecked(false);
				}
			}
			if(choicePosition == position) {
				holder.mButton.setChecked(true);
			} else  {
				holder.mButton.setChecked(false);
			} 
			
			holder.mTextView.setText(list.get(position));
			switch (position) {
			case 0:
				convertView.setBackgroundResource(R.drawable.textview_background_up);
				break;
			case 1:
				convertView.setBackgroundResource(R.drawable.textview_background_middle);
				break;
			case 2:
				convertView.setBackgroundResource(R.drawable.textview_background_down);
				break;
			}
			return convertView;
		}
		
		private class ViewHolder {
			TextView mTextView;
			CheckBox mButton;
		}
		
		
		public void setPosition(int position) {
			this.choicePosition = position;
			this.notifyDataSetChanged();
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(position==2){
//			Intent intent=new Intent(getActivity(),ShopChoosePersonActivity.class);
//			intent.putExtra("classId", classId);
//			intent.putExtra("shopId", shopId);
//			intent.putParcelableArrayListExtra("entities", (ArrayList<? extends Parcelable>) entities);
//			intent.putExtra("clickPosition", clickPosition);
//			startActivityForResult(intent, 1000);
		}else{
			adapter.setPosition(position);
			initPosition = position;	
		}
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		backClickListener = (OnBackClickListener) activity;
	}
	
	public interface OnBackClickListener {
		public void onBackClick(List<MyZoneGroupEntity> entities);
	}
	
	 /* 
	  * 全选设置
	 */
	private void setAuthority(final String type,String cusids) {
		HttpRestClient.doHttpSetAuthority(type,classId,shopId,cusids,new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				if(!content.toString().equals("Y")) {
					ToastUtil.showShort(getActivity(), "设置失败");
				} else {
					//设置成功更新数据
					entities.get(clickPosition).setAppOpen(Integer.parseInt(type));
					ToastUtil.showToastPanl("修改成功");
				}
			}
			@Override
			public void onFailure(Throwable e, String errorResponse) {
				super.onFailure(e, errorResponse);
			}
			@Override
			public void onStart() {
				super.onStart();
				LodingFragmentDialog.showLodingDialog(getChildFragmentManager(), "马上来了");
			}
			@Override
			public void onFinish() {
				super.onFinish();
				LodingFragmentDialog.dismiss(getChildFragmentManager());
			}
		});
	}
	 
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1000:
			if(data!=null&&data.hasExtra("entities")){
				entities= data.getParcelableArrayListExtra("entities");
				adapter.setPosition(entities.get(clickPosition).getAppOpen()-1);
				adapter.notifyDataSetChanged();
			}
			break;
		}
	}
	
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.title_back:
			backClickListener.onBackClick(entities);
			break;
		case R.id.title_right2:
			if(initPosition==2) {//点击的开放指定人
//				Intent intent=new Intent(getActivity(),ShopChoosePersonActivity.class);
//				intent.putExtra("classId", classId);
//				intent.putExtra("shopId", shopId);
//				intent.putParcelableArrayListExtra("entities", (ArrayList<? extends Parcelable>) entities);
//				intent.putExtra("clickPosition", clickPosition);
//				startActivityForResult(intent, 1000);
			}else {
				//选择了全部开放和全部关闭 直接设置
				setAuthority(initPosition+1+"","");
			}
			break;
		}
	}
	
}
