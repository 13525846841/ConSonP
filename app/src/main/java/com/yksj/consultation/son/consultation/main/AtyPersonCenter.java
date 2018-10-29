package com.yksj.consultation.son.consultation.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dmsj.newask.Activity.WelcomeActivity;
import com.squareup.picasso.Picasso;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.son.FragmentContentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.AppData;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.AtyPatientMassage;
import com.yksj.consultation.son.consultation.CommonwealAidAty;
import com.yksj.consultation.son.consultation.PublicNumberAty;
import com.yksj.consultation.son.friend.FriendSearchAboutZxingActivity;
import com.yksj.consultation.son.home.DoctorBlocActivity;
import com.yksj.consultation.son.home.InstitutionHomeActivity;
import com.yksj.consultation.son.home.MyDoctorMainUI;
import com.yksj.consultation.son.home.MyDoctorPlan;
import com.yksj.consultation.son.home.StationMainUI;
import com.yksj.consultation.son.login.UserLoginActivity;
import com.yksj.consultation.son.smallone.bean.Configs;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.services.CoreService;

import org.universalimageloader.core.ImageLoader;

/**
 * Created by HEKL on 2015/9/15.
 * Used for 个人中心
 */
public class AtyPersonCenter extends BaseFragmentActivity implements View.OnClickListener {
    private ImageLoader mImageLoader;//图片异步加载
    private TextView mName;//用户姓名
    private ImageView mHead;//用户头像
    private TextView mAccount;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_personcenter);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();


        CustomerInfoEntity entity = LoginServiceManeger.instance().getLoginEntity();

//        ImageLoader instance = ImageLoader.getInstance();
//        instance.displayImage(entity.getSex(), entity.getNormalHeadIcon(), mHead);
        String uri = HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW + entity.getNormalHeadIcon();
        Picasso.with(this).load(uri).error(R.drawable.head_img).placeholder(R.drawable.head_img).into(mHead);

        if ("".equals(entity.getRealname())) {
            mName.setText(entity.getUsername());
        } else {
            mName.setText(entity.getRealname());
        }
        if ("".equals(entity.getRealname())) {
            mAccount.setText("六一账号：" + entity.getUsername());
        } else {
            mAccount.setText("六一账号：" + entity.getUsername());
        }

    }

    private void initView() {
        initTitle();
        titleTextV.setText("个人中心");
        mName = (TextView) findViewById(R.id.tv_name);
        mAccount = (TextView) findViewById(R.id.tv_name_number);
        mHead = (ImageView) findViewById(R.id.image_head);
        titleLeftBtn.setOnClickListener(this);
        findViewById(R.id.rl_person).setOnClickListener(this);
        //四个横向的按钮
        findViewById(R.id.rl_mydoctors).setOnClickListener(this);//我的医生
        findViewById(R.id.rl_outpatient).setOnClickListener(this);//预约订单
        findViewById(R.id.ll_chat).setOnClickListener(this);//医教计划
        findViewById(R.id.rl_accuntinfo).setOnClickListener(this);//账户管理'
        findViewById(R.id.rl_user_account).setOnClickListener(this);//账户管理

        // findViewById(R.id.rl_mydoctors_plan).setOnClickListener(this);//医教计划
        findViewById(R.id.rl_coupon).setOnClickListener(this);//优惠卷
        findViewById(R.id.rl_user_scan).setOnClickListener(this);//扫一扫
        findViewById(R.id.rl_user_random).setOnClickListener(this);//随访计划
        findViewById(R.id.rl_user_bloc).setOnClickListener(this);//医生联盟
        findViewById(R.id.rl_user_ins).setOnClickListener(this);//找机构
        findViewById(R.id.rl_user_assis).setOnClickListener(this);//使用助理
        findViewById(R.id.rl_publicnumber).setOnClickListener(this);//关注六一微信公众号
        findViewById(R.id.rl_lianjie).setOnClickListener(this);//链接到61健康网
        findViewById(R.id.rl_settings).setOnClickListener(this);//设置
        findViewById(R.id.rl_user_WeChat).setOnClickListener(this);//设置
        findViewById(R.id.rl_jiuzhenren).setOnClickListener(this);//就诊人
        findViewById(R.id.rl_bl).setOnClickListener(this);//病历管理
        findViewById(R.id.rl_shop).setOnClickListener(this);//商城
        findViewById(R.id.rl_call).setOnClickListener(this);//客服
        findViewById(R.id.rl_station).setOnClickListener(this);//名医联诊

        findViewById(R.id.logout_btn).setOnClickListener(this);//退出登录
        mImageLoader = ImageLoader.getInstance();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.rl_person://我的资料
                intent = new Intent(this, AtyPatientMassage.class);
                startActivity(intent);
                break;
            case R.id.rl_mydoctors://我的医生
                intent = new Intent(this, MyDoctorMainUI.class);
                startActivity(intent);
                break;
            case R.id.ll_chat://医教计划
                intent = new Intent(this, MyDoctorPlan.class);
                // intent = new Intent(this, PAytSuccess.class);
                startActivity(intent);

                break;
            case R.id.rl_user_account://钱包
                intent = new Intent(this, AtyAccountInfo.class);
//                intent = new Intent(this, preferActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_accuntinfo://优惠券
//                intent = new Intent(this, AtyAccountInfo.class);
                intent = new Intent(this, preferActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_outpatient://预约订单
                //intent = new Intent(this, MyOrdersActivity.class);
                intent = new Intent(this, OrderActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_coupon://优惠卷
                intent = new Intent(this, preferActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_user_random://随访计划
                intent = new Intent(this, AtyFollowUpPlan.class);
                startActivity(intent);
                break;
            case R.id.rl_user_bloc:
                startActivity(new Intent(this, DoctorBlocActivity.class));
                break;
            case R.id.rl_user_ins:
                startActivity(new Intent(this, InstitutionHomeActivity.class));
                break;
            case R.id.rl_station://名医联诊
                intent = new Intent(this, StationMainUI.class);
                startActivity(intent);
                break;
            case R.id.rl_user_scan://扫一扫
//                intent = new Intent(this, FriendSearchAboutZxingActivity.class);
//                intent.putExtra("type","5");
//                startActivity(intent);

//                intent = new Intent(this, AppraiseActivity.class);
//                startActivity(intent);

                break;
            case R.id.rl_user_WeChat://绑定微信
                intent = new Intent(this, FriendSearchAboutZxingActivity.class);
                intent.putExtra("type", "6");
                startActivity(intent);
                break;
            case R.id.rl_user_assis://使用助理
//                intent = new Intent(AtyPersonCenter.this, WelcomeActivityB.class);
//                startActivity(intent);

                WelcomeActivity.MERCHANT_ID = Configs.MERCHANT_ID;
                WelcomeActivity.site_id = Configs.site_id;
                WelcomeActivity.themeId = Configs.theme_Id;
                intent = new Intent(this, com.dmsj.newask.Activity.ChatActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_publicnumber://六一健康公众号
                intent = new Intent(this, PublicNumberAty.class);
                startActivity(intent);
                break;
            case R.id.rl_lianjie://链接到61健康网
                intent = new Intent(this, CommonwealAidAty.class);
                intent.putExtra(CommonwealAidAty.URL, getString(R.string.strings_61web));
                intent.putExtra(CommonwealAidAty.TITLE, "六一健康网");
                startActivity(intent);
                break;
            case R.id.rl_settings://设置
                intent = new Intent(this, FragmentContentActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
            case R.id.rl_jiuzhenren://就诊人
                intent = new Intent(this, PersonSeekActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_bl://病历管理
                intent = new Intent(this, CaseManage.class);
                startActivity(intent);
                break;
            case R.id.rl_shop://商城
                intent = new Intent(this, ShopActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_call://客服
                checkCall();
                break;
            case R.id.logout_btn://退出登录
                DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), getResources().getString(R.string.quit_tip), "取消", "确定", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
                    @Override
                    public void onDismiss(DialogFragment fragment) {

                    }

                    @Override
                    public void onClick(DialogFragment fragment, View v) {
                        CoreService.actionLogout(getApplicationContext());
                        Intent intent = new Intent(getApplicationContext(), UserLoginActivity.class);
                        startActivity(intent);
                        AtyPersonCenter.this.finish();
                    }
                });
                break;
//            case R.id.rl_code_doc://设置
//                intent = new Intent(AtyPersonCenter.this, FriendSearchAboutZxingActivity.class);
//                intent.putExtra("type", "3");// 0-社交场 1-医生馆 2 商户
//                startActivity(intent);
//                break;
        }
    }

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;


    private void CallPhone() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        //url:统一资源定位符
        //uri:统一资源标示符（更广）
        intent.setData(Uri.parse("tel:" + AppData.SERVER_CALL));
        //开启系统拨号器
        startActivity(intent);
    }

    /**
     * 拨打客服电话
     */
    private void checkCall() {
        // 检查是否获得了权限（Android6.0运行时权限）
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)

        {
            // 没有获得授权，申请授权
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                // 返回值：
                //如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
                //如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
                //如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                // 弹窗需要解释为何需要该权限，再次请求授权
                Toast.makeText(this, "请授权！", Toast.LENGTH_LONG).show();
                // 帮跳转到该应用的设置界面，让用户手动授权
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            } else {
                // 不需要解释为何需要该权限，直接请求授权
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
        } else

        {
            // 已经获得授权，可以打电话
            makeCall();
        }
    }

    /**
     * 拨打客服电话
     */
    private void makeCall() {
        DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), AppData.SERVER_CALL, "取消", "拨打", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
            @Override
            public void onDismiss(DialogFragment fragment) {

            }

            @Override
            public void onClick(DialogFragment fragment, View v) {
                CallPhone();
            }
        });
    }
}

