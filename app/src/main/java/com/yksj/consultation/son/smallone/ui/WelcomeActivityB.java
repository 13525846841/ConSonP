//package com.yksj.consultation.son.smallone.ui;
//
//import android.content.Intent;
//import android.os.Bundle;
//
//import com.yksj.consultation.comm.BaseFragmentActivity;
//import com.yksj.consultation.son.R;
//import com.yksj.consultation.son.smallone.service.CoreServiceB;
//import com.yksj.healthtalk.utils.WeakHandler;
//
//import static com.yksj.consultation.son.smallone.manager.LoginServiceManegerB.instance;
//
//
///**
// * 欢迎页面
// *
// * @author jack_tang
// */
//public class WelcomeActivityB extends BaseFragmentActivity {
////    public static String name = "全科医院";
////    public static String IsKF = "0";
////    public static String ACTIVATE_CODE = "";
////    public static String MERCHANT_ID = "100001";
////    public static int headdrawid = R.drawable.shequ;
//
//
////    public static String name = "孕产医院";
////    public static String IsKF = "0";
////    public static String ACTIVATE_CODE = "";
////    public static String MERCHANT_ID = "100002";
////    public static int headdrawid = R.drawable.baodao;
//
//
//    public static String name = "儿科医院";
//    public static String IsKF = "0";
//    public static String ACTIVATE_CODE = "";
//    public static String MERCHANT_ID = "298";
//    //    public static int headdrawid = R.drawable.shiyue;
//    WeakHandler mHandler = new WeakHandler();
//
//    @Override
//    protected void onCreate(Bundle arg0) {
//        super.onCreate(arg0);
//        setContentView(R.layout.activity_welcome_layout);
//        startService(new Intent(this, CoreServiceB.class));
////      LiteOrmDBUtil.createDb(this);
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                instance().login("", "");
//                Intent intent = new Intent(WelcomeActivityB.this, ChatActivityB.class);
//                // Intent intent = new Intent(LoginRegisterActivity.this, ChatActivity.class);
//                intent.putExtra("MERCHANT_NAME", name);
//                intent.putExtra("IsKF", IsKF);
//                intent.putExtra("MERCHANT_ID", MERCHANT_ID);
//                intent.putExtra("ACTIVATE_CODE", ACTIVATE_CODE);
//                startActivity(intent);
//                //  LoginServiceManegerB.instance().login("", "");
//                //  intent.putExtra("ICON_ADDRESS", R.drawable.icon_back);
//                // startActivity(intent);
//                finish();
//                //   }
//            }
//        }, 1000);
//
//    }
//
//
//    private void jumpChat(boolean isLogin) {
////        Intent intent = new Intent(WelcomeActivityB.this, LoginRegisterActivity.class);
////        intent.putExtra("isLogin", isLogin);
////        startActivity(intent);
////        finish();
//    }
//
//}
