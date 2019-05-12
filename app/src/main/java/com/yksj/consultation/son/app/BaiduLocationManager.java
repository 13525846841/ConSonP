package com.yksj.consultation.son.app;

/**
 * 百度地图工具类
 * 作用:1-获取坐标
 */
import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;


public class BaiduLocationManager {
	private static Context context;
	private static BaiduLocationManager manager;
	private LocationClient mLocationClient;
	private MyLocationListener mLocationListener;
	public static LocationListenerCallBack callBack;

	public static void init(Context context) {
		BaiduLocationManager.context = context;
	}

	public static  BaiduLocationManager getInstance() {
		if (manager == null) {
			manager = new BaiduLocationManager();
		}
		return manager;
	}

	public static  BaiduLocationManager getInstance(Context context) {
		if (manager == null) {
			manager = new BaiduLocationManager(context);
		}
		return manager;
	}

	//开始定位
	public void startLocation() {
		if (mLocationClient == null) {
			initLocation();
		}
		if (mLocationClient != null){

			mLocationClient.start();
			mLocationClient.requestLocation();

		}
	}

	//停止定位
	public void stopLocation() {
		if (mLocationClient != null) {
			mLocationClient.stop();
		}
	}

	private BaiduLocationManager() {
		initLocation();
	}

	private BaiduLocationManager(Context context) {
		this.context=context;
		initLocation();
	}
	/**
	 * 初始化参数
	 */
	private void initLocation() {
		mLocationClient = new LocationClient(context.getApplicationContext());
		mLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mLocationListener);
		LocationClientOption clientOption = new LocationClientOption();
		clientOption.setOpenGps(true);
		clientOption.setCoorType("gps");
		clientOption.setAddrType("detail");
//		clientOption.setScanSpan(0);
		clientOption.setIgnoreKillProcess(true);
		mLocationClient.setLocOption(clientOption);
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			stopLocation();
			if (callBack != null) {
				int type = location.getLocType();
				if(BDLocation.TypeNetWorkLocation == type
						|| BDLocation.TypeCacheLocation == type
						|| BDLocation.TypeOffLineLocation == type
						|| BDLocation.TypeGpsLocation == type) {
					callBack.locationListenerCallBack(location.getLongitude(),
							location.getLatitude());
				}else{
					callBack.locationListenerCallBackFaile();
				}
			}
		}
	}

	public void setCallBack(LocationListenerCallBack callBack) {
		this.callBack = callBack;
	}

	//调用定位时要实现此接口处理回调
	public interface LocationListenerCallBack {
		void locationListenerCallBack(double longitude, double latitude);

		void locationListenerCallBackFaile();
	}

//销毁
	public void destoryLocation(){
		mLocationClient.unRegisterLocationListener(mLocationListener);
	}

}
