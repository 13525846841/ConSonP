package com.yksj.consultation.son.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.avchat.annotation.MPermission;
import com.yksj.consultation.son.consultation.avchat.annotation.OnMPermissionDenied;
import com.yksj.consultation.son.consultation.avchat.annotation.OnMPermissionGranted;
import com.yksj.consultation.son.consultation.avchat.annotation.OnMPermissionNeverAskAgain;
import com.yksj.healthtalk.permission.AndPermission;
import com.yksj.healthtalk.permission.Permission;
import com.yksj.healthtalk.permission.PermissionNo;
import com.yksj.healthtalk.permission.PermissionYes;
import com.yksj.healthtalk.permission.Rationale;
import com.yksj.healthtalk.permission.RationaleListener;
import com.yksj.healthtalk.utils.PermissionUtils;
import com.yksj.healthtalk.utils.WeakHandler;

import java.util.ArrayList;
import java.util.List;

public class GuiDeUitwoActivity extends BaseFragmentActivity {

    private Button button;
    private ViewPager view_pager;
    AnimationDrawable drawable;
    private int currIndex;
    //private ImageView car;
    private int tabLineLength;
    private ImageView[] tips;// 点
    double dou;
    List<View> lists;
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.guide_layout);
//        test();
//        checkPermissons();
        requestBasicPermission();
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        tabLineLength = metrics.widthPixels;

        if (tabLineLength < 490) {
            dou = 2.3;
        } else {
            dou = 2.7;
        }

        ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);

        view_pager = (ViewPager) findViewById(R.id.view_pager);

        button = (Button) findViewById(R.id.button);
        // car = (ImageView) findViewById(R.id.car);
        // car.setImageResource(R.anim.lodingt);
        // drawable = (AnimationDrawable)car.getDrawable();
        // drawable.start();

        lists = new ArrayList<View>();

        ImageView imageView1 = new ImageView(this);
        imageView1.setBackgroundResource(R.drawable.guide3);

        ImageView imageView2 = new ImageView(this);
        imageView2.setBackgroundResource(R.drawable.guide222);

        ImageView imageView3 = new ImageView(this);
        imageView3.setBackgroundResource(R.drawable.guide1);

        lists.add(imageView1);
        lists.add(imageView2);
        lists.add(imageView3);

        ViewPagerAdapter adapter = new ViewPagerAdapter(lists);
        view_pager.setAdapter(adapter);

        tips = new ImageView[lists.size()];
        for (int i = 0; i < tips.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LayoutParams(5, 5));
            tips[i] = imageView;
            if (i == 0) {
                tips[i].setBackgroundResource(R.drawable.icon_white_dot);
            } else {
                tips[i].setBackgroundResource(R.drawable.icon_gray_dot);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            group.addView(imageView, layoutParams);
        }

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                startActivity(new Intent(GuiDeUitwoActivity.this, WelcomeActivity.class));
                finish();
            }
        });
        final WeakHandler mHander = new WeakHandler();
        view_pager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // 如果是最后一个引导界面的话，就出现按钮
                // 如果不是最后一个的话，就不出现
                currIndex = arg0;
                setImageBackground(arg0 % lists.size());
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                if (arg0 == (lists.size() - 1) && arg1 == 0
                        && arg2 == 0) {
                    mHander.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(GuiDeUitwoActivity.this, WelcomeActivity.class));
//                            startActivity(new Intent(GuiDeUitwoActivity.this, AtyConsultMain.class));
                            finish();
                        }
                    }, 2000);
                }
//                if (arg0 == (lists.size() - 1)) {
//                    if (isScrolling) {
//                        if (lastValue > arg2) {
//                            // 递减，向右侧滑动
//                            right = true;
//                            left = false;
//                            startActivity(new Intent(GuiDeUitwoActivity.this, AtyConsultMain.class));
//                            finish();
//                        } else if (lastValue < arg2) {
//                            // 递减，向右侧滑动
//                            right = false;
//                            left = true;
//                        } else if (lastValue == arg2) {
//                            right = left = false;
//                        }
//                    }
//                    lastValue = arg2;
//
//                }

                // RelativeLayout.LayoutParams ll =
                // (android.widget.RelativeLayout.LayoutParams) car
                // .getLayoutParams();
                //
                // if(currIndex == arg0){
                // ll.leftMargin = (int) ((int) (currIndex * car.getWidth() +
                // arg1
                // * car.getWidth())* dou);
                // }else if(currIndex > arg0){
                // ll.leftMargin = (int) ((int) (currIndex * car.getWidth() - (1
                // - arg1)* car.getWidth())* dou);
                // }
                // car.setLayoutParams(ll);

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }



    private boolean left = false;
    private boolean right = false;
    private boolean isScrolling = false;
    private int lastValue = -1;

    public class ViewPagerAdapter extends PagerAdapter {
        private List<View> pages;

        public ViewPagerAdapter(List<View> lists) {
            this.pages = lists;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(pages.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(pages.get(position));
            return pages.get(position);
        }

        @Override
        public int getCount() {
            return pages.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

    /**
     * 设置选中的tip的背景
     *
     * @param selectItems
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.length; i++) {
            if (i == selectItems) {
                tips[i].setBackgroundResource(R.drawable.icon_white_dot);
            } else {
                tips[i].setBackgroundResource(R.drawable.icon_gray_dot);
            }
        }
    }
    private void test() {
        if (ContextCompat.checkSelfPermission(GuiDeUitwoActivity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
        } else {
            //
        }
        PermissionUtils.requestPermission(this, PermissionUtils.CODE_CAMERA, mPermissionGrant);
        PermissionUtils.requestPermission(this, PermissionUtils.CODE_RECORD_AUDIO, mPermissionGrant);
        PermissionUtils.requestPermission(this, PermissionUtils.CODE_GET_ACCOUNTS, mPermissionGrant);
        PermissionUtils.requestPermission(this, PermissionUtils.CODE_CALL_PHONE, mPermissionGrant);
        PermissionUtils.requestPermission(this, PermissionUtils.CODE_READ_PHONE_STATE, mPermissionGrant);
        PermissionUtils.requestPermission(this, PermissionUtils.CODE_ACCESS_FINE_LOCATION, mPermissionGrant);
        PermissionUtils.requestPermission(this, PermissionUtils.CODE_ACCESS_COARSE_LOCATION, mPermissionGrant);



        PermissionUtils.requestPermission(this, PermissionUtils.CODE_READ_EXTERNAL_STORAGE, mPermissionGrant);
        PermissionUtils.requestPermission(this, PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, mPermissionGrant);

    }

    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_RECORD_AUDIO:
//                    Toast.makeText(GuiDeUitwoActivity.this, "Result Permission Grant CODE_RECORD_AUDIO", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_GET_ACCOUNTS:
//                    Toast.makeText(GuiDeUitwoActivity.this, "Result Permission Grant CODE_GET_ACCOUNTS", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_READ_PHONE_STATE:
//                    Toast.makeText(GuiDeUitwoActivity.this, "Result Permission Grant CODE_READ_PHONE_STATE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_CALL_PHONE:
//                    Toast.makeText(GuiDeUitwoActivity.this, "Result Permission Grant CODE_CALL_PHONE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_CAMERA:
//                    Toast.makeText(GuiDeUitwoActivity.this, "Result Permission Grant CODE_CAMERA", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_ACCESS_FINE_LOCATION:
//                    Toast.makeText(GuiDeUitwoActivity.this, "Result Permission Grant CODE_ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_ACCESS_COARSE_LOCATION:
//                    Toast.makeText(GuiDeUitwoActivity.this, "Result Permission Grant CODE_ACCESS_COARSE_LOCATION", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_READ_EXTERNAL_STORAGE:
//                    Toast.makeText(GuiDeUitwoActivity.this, "Result Permission Grant CODE_READ_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
//                    Toast.makeText(GuiDeUitwoActivity.this, "Result Permission Grant CODE_WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };


//    /**
//     * Callback received when a permissions request has been completed.
//     */
//    @Override
//    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
//    }


    /**
     * 基本权限管理
     */
    private final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.READ_CONTACTS,
    //            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private final int BASIC_PERMISSION_REQUEST_CODE = 100;

    private void requestBasicPermission() {
        MPermission.printMPermissionResult(true, this, BASIC_PERMISSIONS);
        MPermission.with(this)
                .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(BASIC_PERMISSIONS)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
        Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        Toast.makeText(this, "未全部授权，部分功能可能无法正常运行！", Toast.LENGTH_SHORT).show();
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
    }













    private static final int REQUEST_CODE_PERMISSION_MULTI = 101;
    /**
     * 1、多个权限申请 两种回调
     */
    private void checkPermissons() {
        // 申请多个权限。
        AndPermission.with(this)
                .requestCode(REQUEST_CODE_PERMISSION_MULTI)
                .permission(Permission.STORAGE,Permission.CAMERA,Permission.MICROPHONE, Permission.CONTACTS,Permission.PHONE,Permission.LOCATION)
                .callback(this)//.callback(permissionListener)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                        AndPermission.rationaleDialog(GuiDeUitwoActivity.this, rationale).show();
                    }
                })
                .start();
    }

    private static final int REQUEST_CODE_SETTING = 300;
    @PermissionYes(REQUEST_CODE_PERMISSION_MULTI)
    private void getMultiYes(@NonNull List<String> grantedPermissions) {
        Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show();
    }

    @PermissionNo(REQUEST_CODE_PERMISSION_MULTI)
    private void getMultiNo(@NonNull List<String> deniedPermissions) {
        Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();

        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
                    .setTitle(R.string.title_dialog)
                    .setMessage(R.string.message_permission_failed)
                    .setPositiveButton(R.string.yes)
                    .setNegativeButton(R.string.no, null)
                    .show();

            // 更多自定dialog，请看上面。
        }
    }
}
