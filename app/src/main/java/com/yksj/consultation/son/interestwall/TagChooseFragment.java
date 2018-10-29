package com.yksj.consultation.son.interestwall;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yksj.healthtalk.entity.TagEntity;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.XmlCategoryParserUtils;

/**
 * 按分类查找的fragment
 * @author crj
 * 
 */
public class TagChooseFragment extends Fragment implements OnItemClickListener {

	private ListView stairListView;
	private ListView secondaryListView;
	private TagChooseAdapter stairAdapter;
	private TagChooseAdapter secondaryAdapter;
	private List<TagEntity> stairData;
	private List<TagEntity> secondaryData;
	private Set<String> secondaryChoosedTag;
	private int stairOldPosition = -1;
	private int resId;
	
	private onTagChooseListener mChooseListener;

	public static TagChooseFragment getInstance(int resid) {
		TagChooseFragment f = new TagChooseFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("xml", resid);
		f.setArguments(bundle);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.resId = this.getArguments().getInt("xml");
		if(secondaryChoosedTag ==null) 
		secondaryChoosedTag = new HashSet<String>();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.category_choose_layout,
				container, false);
		initView(view);
		return view;
	}
	/**
	 * 传入xml文件位置
	 */
	public void initData() {
		stairData = XmlCategoryParserUtils.getCategoryData(getResources(), resId, 0);
		stairAdapter = new TagChooseAdapter(this.getActivity(), stairData);
		secondaryData = new ArrayList<TagEntity>();
		
	}

	public void initView(View view) {
		stairListView = (ListView) view.findViewById(R.id.stair_listview);
		stairListView.setOnItemClickListener(this);
		secondaryListView = (ListView) view.findViewById(R.id.secondary_listview);
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
		secondaryListView.setOnItemClickListener(this);
		stairListView.setAdapter(stairAdapter);
		stairListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		initListView(0);
	}
	

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long id) {
		switch (arg0.getId()) {
		case R.id.stair_listview:
			if(stairOldPosition!=-1) {
				stairData.get(stairOldPosition).setBeChoosed(false);
			}
			initListView(position);
			break;
		case R.id.secondary_listview:
			TagEntity entity = secondaryData.get(position);
			if(entity.isBeChoosed()) {
				secondaryChoosedTag.remove(id+"");
				secondaryData.get(position).setBeChoosed(false);
				mChooseListener.tagChoose(secondaryData.get(position),0);
			} else {
				secondaryChoosedTag.add(id + "");
				secondaryData.get(position).setBeChoosed(true);
				mChooseListener.tagChoose(secondaryData.get(position),1);
			}
			secondaryAdapter.notifyDataSetChanged();
			break;
		}
	}

	private void initListView(int position) {
		stairData.get(position).setBeChoosed(true);
		stairOldPosition = position;
		secondaryData = XmlCategoryParserUtils.getCategoryData(getResources(),resId,Integer.parseInt(stairData.get(position).getId())); 
		for(int i=0;i<secondaryData.size();i++) {
			if(secondaryChoosedTag.contains(secondaryData.get(i).getId())) {
				secondaryData.get(i).setBeChoosed(true);
			}
		}
		secondaryAdapter = new TagChooseAdapter(getActivity(),secondaryData);
		secondaryListView.setAdapter(secondaryAdapter);
		stairAdapter.notifyDataSetChanged();
	}

	public class TagChooseAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		private List<TagEntity> metaData;

		public TagChooseAdapter(Context context,
				List<TagEntity> metaData) {
			mInflater = LayoutInflater.from(context);
			this.metaData = metaData;
		}

		@Override
		public int getCount() {
			return metaData.size();
		}

		@Override
		public Object getItem(int arg0) {
			return metaData.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return Long.parseLong(metaData.get(arg0).getId());
		}

		@Override
		public View getView(final int arg0, View convertview, ViewGroup arg2) {
			ViewHolder holder;
			if (convertview == null) {
				holder = new ViewHolder();
				convertview = mInflater.inflate(R.layout.category_list_item,arg2, false);
				holder.mTextView = (TextView) convertview.findViewById(R.id.itemtext);
				holder.mImageView = (ImageView) convertview.findViewById(R.id.line);
				convertview.setTag(holder);
			} else {
				holder = (ViewHolder) convertview.getTag();
			}
			holder.mTextView.setTextColor(getResources().getColor(R.color.black));
			if (arg2.getId() == R.id.secondary_listview) {
				holder.mImageView.setVisibility(View.VISIBLE);
				if (secondaryChoosedTag.contains(this.getItemId(arg0)+"")) {
					metaData.get(arg0).setBeChoosed(true);
					holder.mTextView.setTextColor(Color.rgb(250,120,5));
				} else {
					metaData.get(arg0).setBeChoosed(false);
					holder.mTextView.setTextColor(getResources().getColor(R.color.black));
				}
			}
			if(arg2.getId()==R.id.stair_listview) {
				holder.mImageView.setVisibility(View.INVISIBLE);
//				convertview.setBackgroundResource(R.drawable.interest_tag_check);
				if (metaData.get(arg0).isBeChoosed()) {
					holder.mTextView.setBackgroundResource(R.drawable.biaoqian_xingqu_press);
				} else {
					holder.mTextView.setBackgroundColor(getResources().getColor(R.color.transparent));
				}
			}
			holder.mTextView.setText(metaData.get(arg0).getName());
			
			return convertview;
		}

		class ViewHolder {
			TextView mTextView;
			ImageView mImageView;
		}

	}

	public interface onTagChooseListener {
		public void tagChoose(TagEntity entity,int tag);
	}

	public void setOnTagChooseListener(onTagChooseListener mChooseListener) {
		this.mChooseListener = mChooseListener;
	}
	
	public void notifyDataChang(String id) {
		secondaryChoosedTag.remove(id);
		if(secondaryAdapter!=null)
			secondaryAdapter.notifyDataSetChanged();
	}
	
	public void notifyDataInit(List<TagEntity> data) {
		if(secondaryChoosedTag ==null) 
			secondaryChoosedTag = new HashSet<String>();
		for(TagEntity entity:data) {
			secondaryChoosedTag.add(entity.getId());
		}
	}

}
