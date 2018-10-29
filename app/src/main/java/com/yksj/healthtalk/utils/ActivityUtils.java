package com.yksj.healthtalk.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;

import com.yksj.consultation.son.app.AppManager;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.http.CustomerInfoHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.services.CoreService;

public class ActivityUtils {


    public static void startWebView(Activity activity, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);
    }

    /**
     * 强制更新
     */
    public static void forceUpdateApp(Context context, String url) {
        context.stopService(new Intent(context, CoreService.class));
        AppManager.getInstance().finishAllActivity();
//		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getResources().getString(R.string.app_download_url)));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        try {
            Thread.sleep(800l);
        } catch (Exception e) {
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 跳转到客户资料
     *
     * @param activity
     * @param manager
     * @param id
     */
    public static void startUserInfoActivity(Activity activity, FragmentManager manager, String id) {
        CustomerInfoEntity entity = (CustomerInfoEntity) HTalkApplication.getAppData().cacheInformation.get(id);
        //内存中查找
        if (entity != null) {
            PersonInfoUtil.choiceActivity(entity.getId(), activity, String.valueOf(entity.getRoldid()));
        } else {
            //网络请求
            HttpRestClient.doHttpFindCustomerInfoByCustId(null, null, id, null, new CustomerInfoHttpResponseHandler(activity, manager));
        }
    }

    public static void startMapActivity(Context context, String la, String lt, String name) {
        try {
            Uri mUri = Uri.parse("geo:" + la + "," + lt + "?q=" + name);
            Intent mIntent = new Intent(Intent.ACTION_VIEW, mUri);
            context.startActivity(mIntent);
        } catch (Exception e) {
            ToastUtil.showShort(context, "您手机上未安装导航应用,将无法为您导航");
        }

    }

    /**
     * 跳转界面
     */
    public static void intentAty(Context context, Class<?> to) {
        Intent intent = new Intent(context, to);
        context.startActivity(intent);
    }

    /**
     * 带参跳转界面
     */
    public static void intentAty(Context context, Class<?> to, String[] keys, String[] values) {
        Intent intent = new Intent(context, to);
        if (values != null) {
            for (int i = 0; i < keys.length; i++) {
                intent.putExtra(keys[i], values[i]);
            }
        }
        context.startActivity(intent);
    }
}
