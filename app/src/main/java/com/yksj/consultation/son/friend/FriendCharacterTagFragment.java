package com.yksj.consultation.son.friend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yksj.healthtalk.entity.ChooseTagsEntity;
import com.yksj.consultation.adapter.InfoAdapter;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.views.PullToRefreshListView;
import com.yksj.healthtalk.utils.XMLUtils;

public class FriendCharacterTagFragment extends Fragment implements OnClickListener,OnItemClickListener{
	
	private Button titleLeftBtn;
	private Button titleRightBtn2;
	private TextView titleTextV;
	private RelativeLayout title;
	private ListView mListView;
	private List<ChooseTagsEntity> tags;
	private ArrayList<String> names;
	private ArrayList<String> ids;
	private String[] datas;
	private InfoAdapter adapter;
	private String tag;
	private String[] tagName;
	private onBackListener listener;
	private ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.friend_search_aboutexperience, container, false);
		initWidget(v);
		initData(v);
		return v;
	}
	
	@Override
	public void onAttach(Activity activity) {
		try {
			 listener = (onBackListener) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString() + " must implement onBackListener");
	        }
		super.onAttach(activity);
	}
	
	private void initData(View v) {
		titleRightBtn2.setVisibility(View.VISIBLE);
		titleRightBtn2.setText(R.string.sure);
		titleTextV.setText(R.string.tag);
		
		tag = (String) getArguments().get("tag");
		tagName = tag.split(",");
		List<ChooseTagsEntity> tags = XMLUtils.parseUserLablesTags(getResources(), R.xml.user_chooice_tags, 1);
		if (ids == null) {
			ids = new ArrayList<String>();
			names = new ArrayList<String>();
			ids.add("0");
			names.add("全部");
			for (ChooseTagsEntity tags2 : tags) {
				names.add(tags2.getName());
				ids.add(tags2.getId());
			}
		}
		datas = (String[]) names.toArray(new String[names.size()]);
		adapter = new InfoAdapter(getActivity(), data, false);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);
		ArrayList<Integer> items = new ArrayList<Integer>();
		if (tagName != null) {
			for (int i = 0; i < tagName.length; i++) {
				if (names.contains(tagName[i])) {
					items.add(names.indexOf(tagName[i]));
				}
			}
			adapter.addSelectAll(items);
		}
		setData(datas);
	}
	private void setData(String[] strs) {
		data.clear();
		for (int i = 0; i < strs.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", i);
			map.put("title", strs[i]);
			map.put("content", "");
			data.add(map);
		}
		adapter.notifyDataSetChanged();
	}
	private void initWidget(View v) {
		// TODO Auto-generated method stub
		titleLeftBtn = (Button)v.findViewById(R.id.title_back);
		titleRightBtn2 = (Button)v.findViewById(R.id.title_right2);
		titleTextV = (TextView)v.findViewById(R.id.title_lable);
		title = (RelativeLayout)v.findViewById(R.id.title_root);
		
		PullToRefreshListView pullRefreshView = (PullToRefreshListView) v.findViewById(R.id.att_list);
		pullRefreshView.setLayoutInvisible();
		mListView = pullRefreshView.getRefreshableView();
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn2.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			listener.onBack(null,getTagCodeToStr());
			break;
		case R.id.title_right2:
			listener.onBack(getTagToStr(),getTagCodeToStr());
			break;
		default:
			break;
		}
	}
	public static Fragment getInstance(String str) {
		FriendCharacterTagFragment fragment = new FriendCharacterTagFragment();
		Bundle args = new Bundle();
		args.putString("tag", str);
		fragment.setArguments(args);
		return fragment;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ImageView image = (ImageView) view.findViewById(R.id.att_icon);
		
		//全部  互斥
		if (position == 0) {
			if (adapter.getSelects().contains(0)) {
				adapter.removeSelect(0);
			}else {
				adapter.getSelects().clear();
				adapter.addSelect(0);
			}
		}else {
			if (adapter.getSelects().contains(position)) {
				adapter.removeSelect(position);
			}else {
				adapter.addSelect(position);
				adapter.removeSelect(0);
			}
		
		}
			image.setVisibility(View.VISIBLE);
		    adapter.notifyDataSetChanged();
	}
	
	public interface onBackListener{
		void onBack(String name,String code);
	}
	
	public String  getTagToStr(){
		if (adapter.getSelects().size() != 0) {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < adapter.getSelects().size(); i++) {
				builder.append(data.get(adapter.getSelects().get(i)).get("title"));
				builder.append(",");
			}
			builder.deleteCharAt(builder.toString().length()-1);
			return builder.toString();
		}
		return "";
	}
	
	/**
	 * 获取标签的id
	 * @return
	 */
	public String getTagCodeToStr(){
		if (adapter.getSelects().size() != 0) {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < adapter.getSelects().size(); i++) {
				if (names.contains(data.get(adapter.getSelects().get(i)).get("title"))) {
					builder.append(ids.get(names.indexOf(data.get(adapter.getSelects().get(i)).get("title"))));
					builder.append(",");
				}
			}
			builder.deleteCharAt(builder.toString().length()-1);
			return builder.toString();
		}
		return "";
	}
}
