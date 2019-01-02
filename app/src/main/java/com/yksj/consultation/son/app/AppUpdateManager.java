package com.yksj.consultation.son.app;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog.OnDilaogClickListener;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.comm.SingleBtnFragmentDialog.OnClickSureBtnListener;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.JsonsfHttpResponseHandler;
import com.yksj.healthtalk.utils.ActivityUtils;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.SystemUtils;

public class AppUpdateManager {
    //	private final String webRootDownLoad="http://www.yijiankangv.com/DuoMeiHealth/HealthTalk.apk";
    FragmentActivity mActivity;
    final boolean isShow;

    public AppUpdateManager(FragmentActivity activity, boolean isShowNoUpdate) {
        this.mActivity = activity;
        this.isShow = isShowNoUpdate;
    }

    /**
     * 检查更新
     */
    public void checkeUpdate() {
        HttpRestClient.doHttpCheckAppVersion(HTalkApplication.getAppVersionName(), new JsonsfHttpResponseHandler(isShow ? mActivity : null) {
            @Override
            public void onSuccess(int statusCode, final JSONObject response) {
                super.onSuccess(statusCode, response);

                Log.i("kkk", "onSuccess: "+response);

                if (mActivity.isFinishing()) {
                    mActivity = null;
                    return;
                }
                //有更新   MUST_FLAG 1为强制更新 0选择更新
                if (response.containsKey("server_version")) {
                    String url = "";
                    String urls = "";
                    if (!HStringUtil.isEmpty(response.getString("DOWNLOAD_URL"))) {
                        urls = response.getString("DOWNLOAD_URL");
                    }
                    String[] temp = null;
                    temp = urls.split("&");
                    url = temp[1];
                    if (!mActivity.isFinishing() && 1 == response.getIntValue("MUST_FLAG")) {
                        final String finalUrl = url;
                        SingleBtnFragmentDialog.show(mActivity.getSupportFragmentManager(), "六一健康", response.getString("update_message"), "现在更新", new OnClickSureBtnListener() {
                            @Override
                            public void onClickSureHander() {
                                ActivityUtils.forceUpdateApp(HTalkApplication.getApplication(), finalUrl);
//                                new AppUpdate().donwloadApp(finalUrl);
                            }
                        });
                    } else {
                        final String finalUrl1 = url;
                        DoubleBtnFragmentDialog dialog = DoubleBtnFragmentDialog.showDefault(mActivity.getSupportFragmentManager(), response.getString("update_message"), "稍后更新", "现在更新",
                                new OnDilaogClickListener() {
                                    @Override
                                    public void onDismiss(DialogFragment fragment) {
//											if(1==response.getIntValue("MUST_FLAG")){//强制更新
//												ActivityUtils.forceUpdateApp(HTalkApplication.getApplication());
//											}
                                    }

                                    @Override
                                    public void onClick(DialogFragment fragment, View v) {
                                        if (1 == response.getIntValue("MUST_FLAG")) {//强制更新
                                            ActivityUtils.forceUpdateApp(HTalkApplication.getApplication(), finalUrl1);
//                                            new AppUpdate().donwloadApp(finalUrl1);
                                        } else {
                                            long sdcardSize = SystemUtils.getAvailableExternalMemorySize();
                                            if (sdcardSize > 25 * 1024 * 1024) {
//												new AppUpdate().donwloadApp(mActivity.getResources().getString(R.string.app_download_url));
//                                                new AppUpdate().donwloadApp(finalUrl1);
                                                ActivityUtils.forceUpdateApp(HTalkApplication.getApplication(), finalUrl1);
                                            } else {
                                                SingleBtnFragmentDialog.showDefault(mActivity.getSupportFragmentManager(), mActivity.getResources().getString(R.string.sdcard_not_enough)
                                                );
                                            }
                                        }
                                    }
                                });
                        dialog.setCancelable(false);

                    }
                    //提示没有更新
                } else if (isShow) {
                    SingleBtnFragmentDialog.showDefault(mActivity.getSupportFragmentManager(), "您当前为最新版本");
                }
            }
        });
    }
}
