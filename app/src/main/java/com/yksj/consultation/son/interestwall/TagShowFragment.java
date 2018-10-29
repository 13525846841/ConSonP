package com.yksj.consultation.son.interestwall;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.yksj.healthtalk.entity.TagEntity;
import com.yksj.consultation.son.R;

/**
 * 标签选择界面内上半部分变迁显示界面
 * @author crj
 *
 */
public class TagShowFragment extends Fragment implements OnItemClickListener{
	/**
	 * 展示主gridview
	 */
	private GridView mGridView;
	/**
	 * 数据源
	 */
	private List<TagEntity> metaData;
	private TagAdapter mAdapter;
	private int fragmentFlag = -1;
	/**
	 * 监听器
	 */
	private OnTagShowItemClickListener mClickListener;
	
	public static TagShowFragment newInstance(int position,ArrayList<TagEntity> data) {
		TagShowFragment f = new TagShowFragment();
		Bundle mBundle = new Bundle();
		mBundle.putInt("position",position);
		mBundle.putParcelableArrayList("data", data);
		f.setArguments(mBundle);
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragmentFlag = this.getArguments().getInt("position");
		metaData = this.getArguments().getParcelableArrayList("data");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view  = inflater.inflate(R.layout.tag_show_layout, container,false);
		initData();
		initView(view);
		return view;
	}

	private void initData() {
		if(metaData!=null)
			mAdapter = new TagAdapter(this.getActivity());
	}

	private void initView(View view) {
		mGridView = (GridView) view.findViewById(R.id.tag_show_gridview);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(this);
	}
	
	public class TagAdapter extends BaseAdapter {
		
		private LayoutInflater mInflater;
		
		public TagAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return metaData.size();
		}

		@Override
		public TagEntity  getItem(int position) {
			return metaData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if(convertView==null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.tag_show_item, parent,false);
				holder.mTextView = (TextView) convertView.findViewById(R.id.tag_show_name);
//				holder.mTextView.setTextColor(getResources().getColor(R.color.blue));
				holder.mTextView.setTextColor(getResources().getColor(R.color.black));
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.mTextView.setText(metaData.get(position).getName());
			return convertView;
		}
	}
	
	static class ViewHolder {
		TextView mTextView;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(mClickListener!=null)
			mClickListener.onTagClick(fragmentFlag, fragmentFlag, metaData.get(position));
		mAdapter.notifyDataSetChanged();
	}
	
	public interface OnTagShowItemClickListener {
		public void onTagClick(int gridviewposition,int pageposition,TagEntity itemData);
	}
	
	public void setOnTagShowItemClickListener(OnTagShowItemClickListener mClickListener) {
		this.mClickListener = mClickListener;
	}
	
	public void notifyGridView(List<TagEntity> tags) {
		metaData = tags;
		mAdapter.notifyDataSetChanged();
	}
	
} 