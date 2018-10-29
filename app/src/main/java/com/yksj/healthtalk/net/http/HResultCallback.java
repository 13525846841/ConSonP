package com.yksj.healthtalk.net.http;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import com.dmsj.newask.http.LodingFragmentDialog;
import com.squareup.okhttp.Request;
import com.yksj.healthtalk.utils.ToastUtil;

import java.lang.ref.WeakReference;
import java.net.ConnectException;

public abstract class HResultCallback<T> extends OkHttpClientManager.ResultCallback<T>{
private WeakReference<FragmentActivity> mActivityReference;
private LodingFragmentDialog mLoadDialog;

        public HResultCallback(){}

        public HResultCallback(FragmentActivity activity){
            this();
            if (activity!=null){
                mActivityReference=new WeakReference<FragmentActivity>(activity);
            }
        }

        @Override
        public void onBefore(Request request)
        {
            super.onBefore(request);
            if(mActivityReference != null){
                FragmentActivity activity = mActivityReference.get();
                mLoadDialog = LodingFragmentDialog.showLodingDialog(activity.getSupportFragmentManager(), activity.getResources());
                mLoadDialog.setCancelable(false);
            }
//            setTitle("loading...");
        }

        @Override
        public void onAfter()
        {
            super.onAfter();
            if(mLoadDialog != null && !mLoadDialog.isDetached()){
                mLoadDialog.dismissAllowingStateLoss();
            }
//            setTitle("Sample-okHttp");
        }

        @Override
        public void onResponse(T response) {

        }

        @Override
        public void onError(Request request, Exception e) {
            if(mActivityReference != null){
                Activity activity = mActivityReference.get();
                if(activity != null && !activity.isFinishing()){
                    if(e==null||e.getCause() instanceof ConnectException){
                        ToastUtil.showToastPanlPro("网络不给力或连接中断，请在设置中退出登录并重新登录。");
                    }else{
                        ToastUtil.showToastPanlPro("网络不给力或连接中断，请在设置中退出登录并重新登录。");
                    }
                }
            }
        }
    }