package com.yksj.healthtalk.utils;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.yksj.consultation.son.app.HTalkApplication;

/**
 * 系统工具用于检查系统状态,如网络,内存卡加载状态
 *
 * @author zhao
 */
public class SystemUtils {
    // private static final SimpleDateFormat dateFormat = new
    // SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * 判断手机网络是否有效
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 退出应用程序
     *
     * @param context
     */
    public static void forceLogoutDialog(Context context) {
//		System.out.println("");
        /*
		 * Intent intent = new Intent(Intent.ACTION_MAIN);
		 * intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 * intent.addCategory(Intent.CATEGORY_HOME);
		 * context.startActivity(intent);
		 */

        // manager.killBackgroundProcesses(context.getPackageName());
		/*
		 * intent = new Intent(Intent.ACTION_MAIN);
		 * intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 * intent.addCategory(Intent.CATEGORY_HOME);
		 * context.startActivity(intent);
		 */

		/*Intent intent = new Intent(context, MessagePushService.class);
		context.stopService(intent);
		intent = new Intent(context, UserLoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("type", 1);
		context.startActivity(intent);*/
    }

    /**
     * 返回sd卡状态
     *
     * @return
     */
    public static boolean getScdExit() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState()))
            return true;
        return false;
    }

    /**
     * 获取sdk号
     *
     * @return
     */
    public static String getSdkVersion() {
        return android.os.Build.VERSION.SDK;
    }

    /**
     * 检查当前网络是否可用
     *
     * @param context
     * @return 可用返回true 否则false
     */
    public static final boolean isNetWorkValid(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        return false;
    }

    /**
     * 获得手机imei号
     *
     * @param context
     * @return
     */
    public static String getImeiId() {
        return SharePreUtils.getIMEI();
    }

    /**
     * 获取手机唯一ID
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager telephonemanage = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonemanage.getDeviceId();
        if (HStringUtil.isEmpty(deviceId)) {
            return SystemUtils.getImeiId();
        } else {
            return deviceId;
        }
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public static String getAppVersionName(Context context) {
        return HTalkApplication.getAppVersionName();
    }

    public static String getAppVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return String.valueOf(packageInfo.versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * 获取存储卡空间大小
     *
     * @return
     */
    public static long getAvailableExternalMemorySize() {
        if (getScdExit()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        }
        return 0;
    }

	/*	*/

    /**
     * 返回sd卡路径
     *
     * @return
     */

    public static String getScdRootPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }


    /**
     * 启动震动
     *
     * @param context
     */
    public static void startVibrator(Context context) {
        Vibrator vibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 判断手机号码
     *
     * @return
     */
    public static boolean checkPhone(String phone) {
        Pattern pattern = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,3-9]))\\d{8}$");
        Matcher matcher = pattern.matcher(phone);

        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 判断邮箱的输入格式
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        Pattern pattern = Pattern
                .compile("^\\w+([-.]\\w+)*@\\w+([-]\\w+)*\\.(\\w+([-]\\w+)*\\.)*[a-z]{2,3}$");
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 获取系统版本号
     *
     * @return
     */
    public static String getVersionRelease() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 隐藏输入键盘
     */
    public static void hideSoftBord(Context context, EditText mEditText) {
        if (mEditText != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context
                    .getSystemService(Service.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        }
    }

    /**
     * 无论如何隐藏输入键盘
     */
    public static void hideSoftAnyHow(FragmentActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static boolean isServiceRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> list = activityManager
                .getRunningServices(Integer.MAX_VALUE);
        for (RunningServiceInfo runningServiceInfo : list) {
            if ("com.yksj.healthtalk".equals(runningServiceInfo.process)) {
                return runningServiceInfo.started;
            }
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void clipTxt(CharSequence txt, Context context) {
        int version = VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager clipManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("copy", txt);
            clipManager.setPrimaryClip(clipData);
        } else {
            android.text.ClipboardManager clipManager = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipManager.setText(txt);
        }

    }

    /**
     * 好评
     */
    public static void commentGood(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + HTalkApplication.getApplication().getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            ToastUtil.showShort("您的手机未安装应用市场.");
        }
    }

    /**
     * 弹出键盘
     *
     * @param editText
     */
    public static void showSoftMode(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }


    /**
     * 获得手机imei号
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonemanage = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonemanage.getDeviceId();
        if (HStringUtil.isEmpty(deviceId)) {
            return getDeviceId(context);
        } else {
            return deviceId;
        }
    }

    /**
     * 拨打电话
     *
     * @param cn
     * @param phone
     */
    public static void callPhone(Context cn, String phone) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            cn.startActivity(intent);
        } catch (Exception e) {

        }

    }

    /**
     * 打开输入键盘
     */
    public static void openSoftBord(Context context, EditText mEditText) {
        if (mEditText != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context
                    .getSystemService(Service.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(mEditText, 0);
        }
    }
}
