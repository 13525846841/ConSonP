package com.yksj.consultation.comm;
import java.util.ArrayList;
import java.util.List;

import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.views.CirclePageIndicator;
import com.yksj.healthtalk.utils.ActivityUtils;
/**
 * 广告
 * @author zhao
 */
public class AdvertFragment extends RootFragment{
	ViewPager mViewPager;
	ImageLoader mImageLoader;
	DisplayImageOptions mDisplayImageOptions;
	AdvertFragmentAdapter mAdapter;
	int mCurrentIndex = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mImageLoader = mImageLoader.getInstance();
		mDisplayImageOptions = DefaultConfigurationFactory.createGalleryDisplayImageOptions(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.advert_layout,null);
		mViewPager = (ViewPager)view.findViewById(R.id.pager);
		mAdapter = new AdvertFragmentAdapter(getActivity());
		mViewPager.setAdapter(mAdapter);
		final CirclePageIndicator circlePageIndicator = (CirclePageIndicator)view.findViewById(R.id.indicator);
		circlePageIndicator.setViewPager(mViewPager);
		circlePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int index) {
				mCurrentIndex = index;
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}
	
	
	/**
	 * 数据改变
	 * @param jsonObject
	 */
	public void onChangeData(final List<AdvertEntity> list){
		mAdapter.onChangeData(list);
		mViewPager.setCurrentItem(mCurrentIndex);
		/*new Thread(){
			public void run() {
				final List<AdvertEntity> entities = new ArrayList<AdvertEntity>();
				try {
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonObject = array.getJSONObject(i);
						JSONObject jsonObject = array.getJSONObject(i);
						String linkPath = jsonObject.getString("adver_Content_Path");
						String advertImagPath = jsonObject.getString("adver_Icon_Path");
						String advertId = jsonObject.getString("adver_id");
						String advertId = i+"";
						String linkPath = "http://g.hiphotos.baidu.com/album/s%3D900%3Bq%3D90/sign=dff67f0b8a13632711edce33a1b4d1d1/0823dd54564e92581f2065399c82d158cdbf4ed5.jpg";
						String advertImagPath = "http://g.hiphotos.baidu.com/album/s%3D900%3Bq%3D90/sign=dff67f0b8a13632711edce33a1b4d1d1/0823dd54564e92581f2065399c82d158cdbf4ed5.jpg";
						AdvertEntity advertEntity = new AdvertEntity(advertId,0,linkPath, advertImagPath);
						entities.add(advertEntity);
					}
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							
						}
					});
				} catch (JSONException e) {
				}
			};
		}.start();*/
	}
	
	private class AdvertFragmentAdapter extends PagerAdapter{
		
		List<AdvertEntity> entities = new ArrayList<AdvertEntity>();
		
		LayoutInflater  mLayoutInflater;
		
		public void onChangeData(List<AdvertEntity> list){
			entities = list;
			this.notifyDataSetChanged();
		}
		
		public AdvertFragmentAdapter(Context context) {
			mLayoutInflater = LayoutInflater.from(context);
		}
		
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager)container).removeView((View)object);
		}
		
		@Override
		public Object instantiateItem(View container, int position) {
			final AdvertEntity advertEntity = entities.get(position);
			View view = mLayoutInflater.inflate(R.layout.advert_list_item,null);
			ImageView imageView = (ImageView)view.findViewById(R.id.advert_image);
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					HttpRestClient.doHttpUpdateAdverCount(advertEntity.getAdvertId(),SmartFoxClient.getLoginUserId());
					ActivityUtils.startWebView(getActivity(),advertEntity.getLinkUrl());
				}
			});
			((ViewPager)container).addView(view,0);
			mImageLoader.displayImage(advertEntity.getImageUrl(),imageView,mDisplayImageOptions);
			return view;
		}
		
		@Override
		public int getCount() {
			return entities.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
	}
	
	public class AdvertEntity{
		private String advertId;
		private int type;
		private String linkUrl;
		private String imageUrl;
		
		public AdvertEntity(String advertId, int type, String linkUrl,
				String imageUrl) {
			super();
			this.advertId = advertId;
			this.type = type;
			this.linkUrl = linkUrl;
			this.imageUrl = imageUrl;
		}
		public String getAdvertId() {
			return advertId;
		}
		public void setAdvertId(String advertId) {
			this.advertId = advertId;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public String getLinkUrl() {
			return linkUrl;
		}
		public void setLinkUrl(String linkUrl) {
			this.linkUrl = linkUrl;
		}
		public String getImageUrl() {
			return imageUrl;
		}
		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}
	}
	
}
