package com.yksj.healthtalk.photo.utils;

import java.util.ArrayList;
import java.util.List;

import org.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.king.photo.zoom.PhotoView;
import com.king.photo.zoom.ViewPagerFixed;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.DConsultRecordDocFragment;
import com.yksj.healthtalk.utils.Bimp;
import com.yksj.healthtalk.utils.ImageItem;

/**
 * 这个是用于进行图片浏览时的界�?
 *
 * @author king
 * @QQ:595163260
 * @version 2014�?10�?18�?  下午11:47:53
 */
public class GalleryActivity extends Activity {
	private Intent intent;
    // 返回按钮
    private Button back_bt;
	// 发�?�按�?
	private Button send_bt;
	//删除按钮
	private Button del_bt;
	//顶部显示预览图片位置的textview
	private TextView positionTextView;
	//获取前一个activity传过来的position
	private int position;
	//当前的位�?
	private int location = 0;
	
	private ArrayList<View> listViews = null;
	private ViewPagerFixed pager;
	private MyPageAdapter adapter;

	public List<Bitmap> bmp = new ArrayList<Bitmap>();
	public List<String> drr = new ArrayList<String>();
	public List<String> del = new ArrayList<String>();
	
	private Context mContext;
	private String key="";
	private int maxNum;//当前行，也就是从之跳过来的那一行最多的图片数量
	private int max;
	private ArrayList<ImageItem> selectBitmap=new ArrayList<ImageItem>();
	private ImageLoader mInstance;

	RelativeLayout photo_relativeLayout;
	private ArrayList<ImageItem> galleryDels=new ArrayList<>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plugin_camera_gallery);// 切屏到主界面
		mContext = this;
		mInstance=ImageLoader.getInstance();
		back_bt = (Button) findViewById(R.id.gallery_back);
		send_bt = (Button) findViewById(R.id.send_button);
		del_bt = (Button)findViewById(R.id.gallery_del);
		back_bt.setOnClickListener(new BackListener());
		send_bt.setOnClickListener(new GallerySendListener());
		del_bt.setOnClickListener(new DelListener());
		intent = getIntent();
		key=intent.getStringExtra("key");
		maxNum=Bimp.imgMaxs.get(key);//得到这一行的数量
		position = Integer.parseInt(intent.getStringExtra("position"));//
		selectBitmap=Bimp.dataMap.get(key);//得到已选择的图片
		isShowOkBt();
		// 为发送按钮设置文�?
		pager = (ViewPagerFixed) findViewById(R.id.gallery01);
		pager.setOnPageChangeListener(pageChangeListener);
		for (int i = 0; i < selectBitmap.size(); i++) {
			initListViews( selectBitmap.get(i) );
		}
		
		adapter = new MyPageAdapter(listViews);
		pager.setAdapter(adapter);
		pager.setPageMargin((int)getResources().getDimensionPixelOffset(R.dimen.ui_10_dip));
		int id = intent.getIntExtra("ID", 0);
		pager.setCurrentItem(id);
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {
			location = arg0;
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageScrollStateChanged(int arg0) {

		}
	};
	
	private void initListViews(ImageItem ii) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		PhotoView img = new PhotoView(this);
		img.setBackgroundColor(0xff000000);
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		if(ii.isNetPic){//网络图片
			mInstance.displayImage(ii.getImagePath(), img);
		}else{//本地图片
			if(!"".equals(ii.getImagePath())){//根据图片地址显示
				Bitmap bm = Bimp.fitSizeImg(ii.getImagePath());
				img.setImageBitmap(bm);
			}else//按照bitmap显示
				img.setImageBitmap(ii.getBitmap());
		}
		listViews.add(img);
	}
	
	// 返回按钮添加的监听器
	private class BackListener implements OnClickListener {

		public void onClick(View v) {
			Toast.makeText(GalleryActivity.this, "去文件相册暂时不支持", Toast.LENGTH_SHORT).show();
//			intent.setClass(GalleryActivity.this, ImageFile.class);
//			startActivity(intent);
		}
	}

	// 删除按钮添加的监听器
	private class DelListener implements OnClickListener {

		public void onClick(View v) {
			if(selectBitmap.get(location).isNetPic){
				StringBuilder sb=DConsultRecordDocFragment.dePics;
				if(sb.length()==0)
					sb.append(selectBitmap.get(location).pidId);
				else{
					sb.append(","+selectBitmap.get(location).pidId);
				}
			}
			if (listViews.size() == 1) {
				if(selectBitmap.get(location).isNetPic){
					delList(selectBitmap.get(location));
				}
				selectBitmap.clear();
				max = 0;
				send_bt.setText(getResources().getString(R.string.finish)+"(" + selectBitmap.size() + "/"+maxNum+")");
				Intent intent = new Intent("data.broadcast.action");  
                sendBroadcast(intent);
				HTalkApplication.getHTalkApplication().setmImages(galleryDels);
				finish();
			} else {
				if(selectBitmap.get(location).isNetPic){
					delList(selectBitmap.get(location));
				}
				selectBitmap.remove(location);
				max--;
				pager.removeAllViews();
				listViews.remove(location);
				adapter.setListViews(listViews);
				send_bt.setText(getResources().getString(R.string.finish)+"(" + selectBitmap.size() + "/"+maxNum+")");
				adapter.notifyDataSetChanged();
			}
		}
	}

	// 完成按钮的监听
	private class GallerySendListener implements OnClickListener {
		public void onClick(View v) {
//			Intent data=new Intent();
//			data.putIntegerArrayListExtra("result",galleryDels);
//			setResult(Activity.RESULT_OK, data);
			onBackPressed();
		}
	}

	public void isShowOkBt() {
		if (selectBitmap.size() > 0) {
			send_bt.setText(getResources().getString(R.string.finish)+"(" + selectBitmap.size() + "/"+maxNum+")");
			send_bt.setPressed(true);
			send_bt.setClickable(true);
			send_bt.setTextColor(Color.WHITE);
		} else {
			send_bt.setPressed(false);
			send_bt.setClickable(false);
			send_bt.setTextColor(Color.parseColor("#E1E0DE"));
		}
	}

//	/**
//	 * 监听返回按钮
//	 */
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if(position==1){
//				this.finish();
//				intent.setClass(GalleryActivity.this, AlbumActivity.class);
//				startActivity(intent);
//			}else if(position==2){
//				this.finish();
//				intent.setClass(GalleryActivity.this, ShowAllPhoto.class);
//				startActivity(intent);
//			}
//		}
//		return true;
//	}
	
	
	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;

		private int size;
		public MyPageAdapter(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {
			try {
				((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}
	private void delList(ImageItem index){
		galleryDels.add(index);
	}

	@Override
	public void onBackPressed() {
		HTalkApplication.getHTalkApplication().setmImages(galleryDels);
		super.onBackPressed();
	}
}
