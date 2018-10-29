package com.yksj.consultation.comm;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.yksj.consultation.son.R;
import com.yksj.consultation.son.views.CirclePageIndicator;
import com.yksj.healthtalk.utils.FaceParse;

public class FacePanelFragment extends Fragment {
	ViewPager mViewPager;
	List<String[]> faceGroupList;
	FaceParse mFaceParse;
	
	FaceItemOnClickListener mFaceItemOnClickListener;
	
	public interface FaceItemOnClickListener{
		void onItemClick(String text,Drawable drawable,FaceParse mFaceParse);
	}
	
	public FaceItemOnClickListener getmFaceItemOnClickListener() {
		return mFaceItemOnClickListener;
	}

	public void setmFaceItemOnClickListener(
			FaceItemOnClickListener mFaceItemOnClickListener) {
		this.mFaceItemOnClickListener = mFaceItemOnClickListener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mFaceParse = FaceParse.getChatFaceParse(getActivity());
		faceGroupList = mFaceParse.getFaceGroups(7*3);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.face_panel_layout,null);
		mViewPager = (ViewPager)view.findViewById(R.id.pager);
		final CirclePageIndicator circlePageIndicator = (CirclePageIndicator)view.findViewById(R.id.indicator);
		mViewPager.setAdapter(new FacePagerAdapter(getActivity(),faceGroupList));
		circlePageIndicator.setViewPager(mViewPager);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	private class FacePagerAdapter extends PagerAdapter{
		final Context mContext;
		final LayoutInflater mInflater;
		private final List<GridView> mGridViews;
		
		public FacePagerAdapter(Context context,List<String[]> list){
			this.mContext = context;
			this.mInflater = LayoutInflater.from(mContext);
			this.mGridViews = new ArrayList<GridView>();
			for (String[] strings : list) {
				GridView gridView = (GridView)mInflater.inflate(R.layout.face_gridview,null);
				gridView.setAdapter(new FaceImageAdapter(strings,mInflater));
				mGridViews.add(gridView);
			}
		}
		
		@Override
		public Object instantiateItem(View container, int position) {
			View view = mGridViews.get(position);
			((ViewPager)container).addView(view,0);
			return view;
		}
		
		@Override
		public int getCount() {
			return mGridViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager)container).removeView((View)object);
		}
	}
	
	private class FaceImageAdapter extends BaseAdapter{
		final String[] mTxtSmileArray;
		final LayoutInflater mInflater;
		
		public  FaceImageAdapter(String[] faceArray,LayoutInflater inflater){
			this.mTxtSmileArray = faceArray;
			this.mInflater = inflater;
		}
		
		@Override
		public int getCount() {
			return mTxtSmileArray.length;
		}

		@Override
		public String getItem(int position) {
			return mTxtSmileArray[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		public Drawable getDrawable(int position){
			return mFaceParse.getDrawable(getItem(position));
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final String faceStr = getItem(position);
			ViewHolder viewHolder;
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.face_list_item,null);
				viewHolder = new ViewHolder();
				viewHolder.imageView = (ImageView)convertView.findViewById(R.id.face_item_imge);
				convertView.setTag(viewHolder);
				viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(mFaceItemOnClickListener != null){
							mFaceItemOnClickListener.onItemClick(faceStr,getDrawable(position),mFaceParse);
						}
					}
				});
			}else{
				viewHolder = (ViewHolder)convertView.getTag();
			}
			viewHolder.imageView.setBackgroundDrawable(getDrawable(position));
			return convertView;
		}
	}
	
	private static class ViewHolder{
		ImageView imageView;
	}	
	
	
}
