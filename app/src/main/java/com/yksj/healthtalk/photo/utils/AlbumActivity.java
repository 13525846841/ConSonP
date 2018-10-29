package com.yksj.healthtalk.photo.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yksj.consultation.adapter.AlbumGridViewAdapter;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.AlbumHelper;
import com.yksj.healthtalk.utils.Bimp;
import com.yksj.healthtalk.utils.ImageBucket;
import com.yksj.healthtalk.utils.ImageItem;


/**
 * 这个是进入相册显示所有图片的界面
 * 
 * @author king
 * @QQ:595163260
 * @version 2014�?10�?18�?  下午11:47:15
 */
public class AlbumActivity extends Activity implements OnClickListener{
	//显示手机里的所有图片的列表控件
	private GridView gridView;
	//当手机里没有图片时，提示用户没有图片的控件?
	private TextView tv;
	//gridView的adapter
	private AlbumGridViewAdapter gridImageAdapter;//适配器
	private Button okButton,preview;//完成按钮,预览按钮
	private Intent intent;
	private ArrayList<ImageItem> dataList;//数据源
	private AlbumHelper helper;
	public static List<ImageBucket> contentList;
	public static Bitmap bitmap;
	public ArrayList<ImageItem> selectBitmap=new ArrayList<ImageItem>();
	private String key="";
	private int maxNum;//当前行需要的最多的图片数量
//	private int comeNum;//跳转过来时是否已经有图片？假如有表示取消按钮是取消本次添加，假如没有取消按钮是表示取消所有图片
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plugin_camera_album);
		//注册�?个广播，这个广播主要是用于在GalleryActivity进行预览时，防止当所有图片都删除完后，再回到该页面时被取消�?�中的图片仍处于选中状�??
		IntentFilter filter = new IntentFilter("data.broadcast.action");  
		registerReceiver(broadcastReceiver, filter);  
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.plugin_camera_no_pictures);
        init();
		initListener();
		//这个函数主要用来控制预览和完成按钮的状�??
		isShowOkBt();
	}
	
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {  

        @Override  
        public void onReceive(Context context, Intent intent) {  
        	//mContext.unregisterReceiver(this);
        	gridImageAdapter.notifyDataSetChanged();
        }  
    };  

	// 初始化，给一些对象赋�?
	private void init() {
		intent = getIntent();
		if(intent.hasExtra("key")){
			key=intent.getStringExtra("key");
			maxNum=Bimp.imgMaxs.get(key);
			selectBitmap=Bimp.dataMap.get(key);
		}
		
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		
		contentList = helper.getImagesBucketList(false);
		dataList = new ArrayList<ImageItem>();
		for(int i = 0; i<contentList.size(); i++){
			dataList.addAll( contentList.get(i).imageList );
		}
		
		findViewById(R.id.album_back).setOnClickListener(this);//相册
		findViewById(R.id.album_cancel).setOnClickListener(this);//取消
		preview=(Button) findViewById(R.id.album_preview);//预览
		preview.setOnClickListener(this);
		okButton=(Button) findViewById(R.id.album_ok_button);//完成
		okButton.setOnClickListener(this);
		okButton.setText(getResources().getString(R.string.finish)+"(" + selectBitmap.size()
				+ "/"+maxNum+")");
		
		gridView = (GridView) findViewById(R.id.album_myGrid);
		gridImageAdapter = new AlbumGridViewAdapter(this,dataList,selectBitmap);
		gridView.setAdapter(gridImageAdapter);
		tv = (TextView) findViewById(R.id.album_myText);
		gridView.setEmptyView(tv);
	}

	private void initListener() {

		gridImageAdapter.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {

			@Override
			public void onItemClick(final ToggleButton toggleButton,
					int position, boolean isChecked,Button chooseBt) {
				if (selectBitmap.size() >= maxNum) {
					toggleButton.setChecked(false);
					chooseBt.setVisibility(View.GONE);
					if (!removeOneData(dataList.get(position))) {
						Toast.makeText(AlbumActivity.this, getResources().getString(R.string.only_choose_num),Toast.LENGTH_SHORT).show();
					}
					return;
				}
				if (isChecked) {
					chooseBt.setVisibility(View.VISIBLE);
					selectBitmap.add(dataList.get(position));
					okButton.setText(getResources().getString(R.string.finish)+"(" + selectBitmap.size()
							+ "/"+maxNum+")");
				} else {
					selectBitmap.remove(dataList.get(position));
					chooseBt.setVisibility(View.GONE);
					okButton.setText(getResources().getString(R.string.finish)+"(" + selectBitmap.size() + "/"+maxNum+")");
				}
				isShowOkBt();
			}
		});

	}

	private boolean removeOneData(ImageItem imageItem) {
		if (selectBitmap.contains(imageItem)) {
			selectBitmap.remove(imageItem);
			okButton.setText(getResources().getString(R.string.finish)+"(" +selectBitmap.size() + "/"+maxNum+")");
			return true;
		}
		return false;
	}
	
	public void isShowOkBt() {
		if (selectBitmap.size() > 0) {
			okButton.setText(getResources().getString(R.string.finish)+"(" + selectBitmap.size() + "/"+maxNum+")");
			preview.setPressed(true);
			okButton.setPressed(true);
			preview.setClickable(true);
			okButton.setClickable(true);
			okButton.setTextColor(Color.WHITE);
			preview.setTextColor(Color.WHITE);
		} else {
			okButton.setText(getResources().getString(R.string.finish)+"(" + selectBitmap.size() + "/"+maxNum+")");
			preview.setPressed(false);
			preview.setClickable(false);
			okButton.setPressed(false);
			okButton.setClickable(false);
			okButton.setTextColor(Color.parseColor("#E1E0DE"));
			preview.setTextColor(Color.parseColor("#E1E0DE"));
		}
	}
	
	@Override
	protected void onRestart() {
		isShowOkBt();
		super.onRestart();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.album_back://去相册
			Toast.makeText(AlbumActivity.this, "去文件相册暂时不支持", Toast.LENGTH_SHORT).show();
//			intent.setClass(AlbumActivity.this, ImageFile.class);
//			startActivity(intent);
			break;
		case R.id.album_cancel://取消,表示用户不想选择图片了
			selectBitmap.clear();
			onBackPressed();
			break;
		case R.id.album_preview://预览
			if (selectBitmap.size() > 0) {
				intent.setClass(AlbumActivity.this, GalleryActivity.class);
				intent.putExtra("position", "1");
				intent.putExtra("key", key);
				startActivity(intent);
				AlbumActivity.this.finish();
			}
			break;
		case R.id.album_ok_button://完成
			
//			Intent intent = getIntent();
//			intent.putExtra("END", 66);
//			//把界面二中的数据回传给界面一(需要把当前的Activity销毁，这个结果才会回传给界面一)
//			setResult(50, intent);//参数一表示的是结果的编码
			onBackPressed();
			break;
		}
	}
}
