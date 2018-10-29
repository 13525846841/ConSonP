package com.yksj.consultation.son.chatting;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;

public class ChatMapActivity extends BaseFragmentActivity implements OnClickListener{
	
	private BaiduMap mBaiduMap;
		@Override
		protected void onCreate(Bundle arg0) {
			super.onCreate(arg0);
			 SDKInitializer.initialize(getApplicationContext());  
			 setContentView(R.layout.chat_map_layout);
			 initUI();
		}
		
		private void initUI(){
			initTitle();
			titleLeftBtn.setOnClickListener(this);
			MapView mMapView = (MapView)findViewById(R.id.map);
			mBaiduMap = mMapView.getMap();  
			MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
			mBaiduMap.setMapStatus(msu);
//			mMapView.setBuiltInZoomControls(true);  
			
			//设置启用内置的缩放控件  
		/*	MapController mMapController=mapView.getController();  
			// 得到mMapView的控制权,可以用它控制和驱动平移和缩放  
			GeoPoint point =new GeoPoint((int)(39.915* 1E6),(int)(116.404* 1E6));  
			//用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)  
			mMapController.setCenter(point);//设置地图中心点  
			mMapController.setZoom(12);//设置地图zoom级别  
*/			
			LatLng point = new LatLng(Double.valueOf(getIntent().getStringExtra("lo")), 
					Double.valueOf(getIntent().getStringExtra("la")));
			//构建Marker图标  
			BitmapDescriptor bitmap = BitmapDescriptorFactory  
			    .fromResource(R.drawable.icon_marka);  
			OverlayOptions option = new MarkerOptions().position(point).icon(bitmap); 
			mBaiduMap.addOverlay(option);
			MapStatus mMapStatus = new MapStatus.Builder() .target(point) .zoom(16) .build();
	        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
	        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
	        //改变地图状态
	        mBaiduMap.setMapStatus(mMapStatusUpdate);
			
//			MyLocationOverlay myLocationOverlay = new MyLocationOverlay(mMapView);  
//			LocationData locData = new LocationData();  
			//手动将位置源置为天安门，在实际应用中，请使用百度定位SDK获取位置信息，要在SDK中显示一个位置，需要使用百度经纬度坐标（bd09ll）  
//			locData.latitude = 39.945;  
//			locData.longitude = 116.404; 
//			mMapView.getController().setZoom(18);
//			try{
//				locData.latitude =  Double.valueOf(getIntent().getStringExtra("lo"));  
//				locData.longitude =   Double.valueOf(getIntent().getStringExtra("la"));  
//			}catch(Exception e){
//			}
//			locData.direction = 2.0f;  
//			myLocationOverlay.setData(locData);  
//			mMapView.getOverlays().add(myLocationOverlay);  
//			mMapView.refresh();  
//			mMapView.getController().animateTo(new GeoPoint((int)(locData.latitude*1e6),  
//			(int)(locData.longitude* 1e6)));
		}

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.title_back:
				onBackPressed();
				break;
			}
		}
}
