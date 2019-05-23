package com.yksj.consultation.son.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.WorksationTeamMainAdapter;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.PAtyConsultStudioGoPaying;
import com.yksj.consultation.son.consultation.bean.ServiceType;
import com.yksj.consultation.son.listener.OnRecyclerClickListener;
import com.yksj.consultation.son.login.GuiDeUitwoActivity;
import com.yksj.healthtalk.entity.DoctorInfoEntity;
import com.yksj.healthtalk.entity.DoctorWorkstationMainEntity;
import com.yksj.healthtalk.entity.HospitaFindTeamEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.ToastUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class DoctorWorkstationMainActivity extends Activity implements View.OnClickListener, ViewPager.OnPageChangeListener, OnRecyclerClickListener, PlatformActionListener {

    private List<ImageView> mBannerList=new ArrayList<>();
    private List<View> dotViewList=new ArrayList<>();
    private TextView workName;
    private TextView departmentName;
    private TextView workHospital;
    private TextView browsNum;
    private TextView doctorNum;
    private TextView hospitalIntroduction;
    private TextView workIntroduction;
    private TextView masterIntroduction;
    private TextView userEvaluateName;
    private TextView userEvaluateContent;
    private TextView zixunPrice;
    private TextView tvZixunNum;
    private RecyclerView memberRecycler;
    private TextView hospitalIntroductionMore;
    private TextView workIntroductionMore;
    private TextView masterIntroductionMore;
    private DoctorWorkstationMainEntity.ResultBean resultBean;
    private String site_id;
    public static final String SITE_ID="site_id";
    private List<DoctorWorkstationMainEntity.ResultBean.SiteMemberBean> siteMember;
    //弹出分享窗口
    PopupWindow mPopupWindow;
    private ViewPager workBannerVp;
    private LinearLayout vpDotLinear;
    private TextView tipTv;
    private String errTips = "加载失败，点击重试";
    private String qrCodeUrl = "";
    private ImageView workAttention;
    private TextView moreEvaluate;
    private int IS_FOLLOW;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workstation_main);
        initView();
        loadDataWss();
    }

    private void initView() {
        site_id = getIntent().getStringExtra("site_id");
        Log.i("asdf", "initView: "+site_id);
        ImageView titleBack= (ImageView) findViewById(R.id.title_back);
        titleBack.setOnClickListener(this);
        workAttention = (ImageView) findViewById(R.id.work_attention);
        workAttention.setOnClickListener(this);
        ImageView workShare= (ImageView) findViewById(R.id.work_share);
        workShare.setOnClickListener(this);
        tipTv = (TextView) findViewById(R.id.tipTv);
        tipTv.setOnClickListener(this);
        //banner
        workBannerVp = (ViewPager) findViewById(R.id.work_bannerVp);
        //viewpager显示的点
        vpDotLinear = (LinearLayout) findViewById(R.id.vpDot);
        LinearLayout workstationMember= (LinearLayout) findViewById(R.id.workstationMember);//工作成员
        workstationMember.setOnClickListener(this);
        LinearLayout docShare= (LinearLayout) findViewById(R.id.docShare);//名医分享
        docShare.setOnClickListener(this);
        LinearLayout healthLecture= (LinearLayout) findViewById(R.id.healthLecture);//健康讲堂
        healthLecture.setOnClickListener(this);
        //名字
        workName = (TextView) findViewById(R.id.workName);
        //科室
        departmentName = (TextView) findViewById(R.id.departmentName);
        //所属医院
        workHospital = (TextView) findViewById(R.id.work_hospital);
        //浏览次数
        browsNum = (TextView) findViewById(R.id.browsNum);
        //医生个数
        doctorNum = (TextView) findViewById(R.id.doctorNum);
        RelativeLayout displayQR= (RelativeLayout) findViewById(R.id.displayQR);//查看二维码
        displayQR.setOnClickListener(this);
        //医院简介内容
        hospitalIntroduction = (TextView) findViewById(R.id.hospitalIntroduction);
        //查看全部医院简介内容
        hospitalIntroductionMore = (TextView) findViewById(R.id.hospitalIntroductionMore);
        hospitalIntroductionMore.setOnClickListener(this);
        //工作站简介内容
        workIntroduction = (TextView) findViewById(R.id.workIntroduction);
        //查看工作站简介内容
        workIntroductionMore = (TextView) findViewById(R.id.workIntroductionMore);
        workIntroductionMore.setOnClickListener(this);
        //站长简介内容
        masterIntroduction = (TextView) findViewById(R.id.masterIntroduction);
        //查看站长简介内容
        masterIntroductionMore = (TextView) findViewById(R.id.masterIntroductionMore);
        masterIntroductionMore.setOnClickListener(this);
        //评价用户的昵称
        userEvaluateName = (TextView) findViewById(R.id.userEvaluateName);
        //评价内容
        userEvaluateContent = (TextView) findViewById(R.id.userEvaluateContent);
        //查看更多评价
        moreEvaluate = (TextView) findViewById(R.id.moreEvaluate);
        moreEvaluate.setOnClickListener(this);
        RelativeLayout workAdvisory= (RelativeLayout) findViewById(R.id.workAdvisory);//向工作站咨询
        workAdvisory.setOnClickListener(this);
        //咨询价格
        zixunPrice = (TextView) findViewById(R.id.tvZixunPrice);
        //购买次数
        tvZixunNum = (TextView) findViewById(R.id.tvZixunNum);
        TextView moreMember= (TextView) findViewById(R.id.moreMember);//查看更多工作成员
        moreMember.setOnClickListener(this);
        //部分工作站成员
        memberRecycler = (RecyclerView) findViewById(R.id.memberRecycler);
        Button zixunBtn= (Button) findViewById(R.id.zixunBtn);//点击咨询
        zixunBtn.setOnClickListener(this);
        zixunBtn.setVisibility(View.GONE);
    }

    private void loadVpBanner(ViewPager workBannerVp, LinearLayout vpDotLinear) {
        for (int i=0;i<=0;i++){
            ImageView imageView = new ImageView(this);
            Glide.with(DoctorWorkstationMainActivity.this).load(ImageLoader.getInstance().getDownPathUri(resultBean.getSiteInfo().getSITE_BIG_PIC())).centerCrop().placeholder(R.drawable.top_image)
                    .dontAnimate().error(R.drawable.top_image).into(imageView);
            mBannerList.add(imageView);
        }
        workBannerVp.addOnPageChangeListener(this);
        workBannerVp.setAdapter(new BannerVpAdapter());
        int size = mBannerList.size();
        if (size>1){
            for (int i = 0; i < size; i++) {
                View viewDot= LayoutInflater.from(this).inflate(R.layout.dot_layout,null);
                View viewById = viewDot.findViewById(R.id.dotGreen);
                if (i==0)viewById.setBackgroundResource(R.drawable.shape_viewpager_tip_select_dot);
                else viewById.setBackgroundResource(R.drawable.shape_viewpager_tip_not_select_dot);
                vpDotLinear.addView(viewDot);
                dotViewList.add(viewDot);
            }
        }
    }

    private boolean isAttention=false;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.tipTv:
                if (tipTv.getText().equals(errTips)){
                    tipTv.setText("加载中...");
                    loadDataWss();
                }
                break;
            case R.id.work_attention:
                loadCollection();
                break;
            case R.id.friendcircle:
                sendWXMS();
                quitPopWindow();
                break;
            case R.id.wechat:
                sendWX();
                quitPopWindow();
                break;
            case R.id.work_share:
                showShare();
                break;
            case R.id.workstationMember:
                Intent intent = new Intent(this, DoctorTeamMemberActivity.class);
                intent.putExtra("site_id",resultBean.getSiteInfo().getSITE_ID());
                startActivity(intent);
                break;
            case R.id.docShare:
                Intent shareIntent = new Intent(this, FamousDoctorShareActivity.class);
                shareIntent.putExtra("type","works");
                shareIntent.putExtra("Site_Id",site_id);
                startActivity(shareIntent);
                break;
            case R.id.healthLecture:
                Intent intent4 = new Intent(this, HealthLectureActivity.class);
                intent4.putExtra("lectureType","works");
                intent4.putExtra("site_id",site_id);
                startActivity(intent4);
                break;
            case R.id.displayQR:
                Intent intent1 = new Intent(this, QRcodeDoctorActivity.class);
                intent1.putExtra("imgHeader",resultBean.getSiteInfo().getSITE_BIG_PIC());
                intent1.putExtra("specially",resultBean.getSiteInfo().getSITE_NAME());
                intent1.putExtra("hisInfo",resultBean.getSiteInfo().getSITE_HOSPOTAL()+"\t\t|\t\t"+resultBean.getSiteInfo().getOFFICE_NAME());
                intent1.putExtra("qrCodeUrl",qrCodeUrl);
                startActivity(intent1);
                break;
            case R.id.hospitalIntroductionMore:
                hospitalIntroduction.setMaxLines(Integer.MAX_VALUE);
                hospitalIntroductionMore.setVisibility(View.GONE);
                break;
            case R.id.workIntroductionMore:
                workIntroduction.setMaxLines(Integer.MAX_VALUE);
                workIntroductionMore.setVisibility(View.GONE);
                break;
            case R.id.masterIntroductionMore:
                masterIntroduction.setMaxLines(Integer.MAX_VALUE);
                masterIntroductionMore.setVisibility(View.GONE);
                break;
            case R.id.moreEvaluate:
                Intent intent2 = new Intent(this, EvaluationActivity.class);
                intent2.putExtra("type", "works");
                intent2.putExtra("Site_Id", resultBean.getSiteInfo().getSITE_ID()+"");
                startActivity(intent2);
                break;
            case R.id.workAdvisory:
               workstationTuWenZiXun();
                break;
            case R.id.moreMember:
                Intent intent3 = new Intent(this, DoctorTeamMemberActivity.class);
                intent3.putExtra("site_id",resultBean.getSiteInfo().getSITE_ID());
                startActivity(intent3);
                break;
            case R.id.zixunBtn:
                workstationTuWenZiXun();
                break;
        }
    }

    //添加关注
    private void loadCollection() {

        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("Type", "followSite"));
        pairs.add(new BasicNameValuePair("customer_id", LoginServiceManeger.instance().getLoginUserId()));
        pairs.add(new BasicNameValuePair("site_id", site_id));

        HttpRestClient.doGetWss(pairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                Log.i("hhh", "onResponse: "+response);
                if (IS_FOLLOW==0) {
                    workAttention.setImageResource(R.drawable.quxiaoshoucang);
                    ToastUtil.onShow(DoctorWorkstationMainActivity.this,"关注成功",1000);
                    IS_FOLLOW=1;
                }else {
                    workAttention.setImageResource(R.drawable.works_shoucang);
                    ToastUtil.onShow(DoctorWorkstationMainActivity.this,"关注取消",1000);
                    IS_FOLLOW=0;
                }
            }
        },this);

    }

    private void workstationTuWenZiXun() {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("op", "addWorkSiteOrder"));
        pairs.add(new BasicNameValuePair("service_customer_id", LoginServiceManeger.instance().getLoginUserId()));
        pairs.add(new BasicNameValuePair("service_type_id",ServiceType.TW));
//        pairs.add(new BasicNameValuePair("Site_id","354"));
        pairs.add(new BasicNameValuePair("site_id",site_id));


        HttpRestClient.doGetWorksZixun(pairs, new OkHttpClientManager.ResultCallback<String>() {


            @Override
            public void onError(Request request, Exception e) {

                tipTv.setText(errTips);
            }


            @Override
            public void onResponse(String response) {
                Log.i("haha", "onResponse: "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    ToastUtil.onShow(DoctorWorkstationMainActivity.this,message,2000);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },this);


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotViewList.size(); i++) {
            View view = dotViewList.get(i).findViewById(R.id.dotGreen);
            if (i==position){
                view.setBackgroundResource(R.drawable.shape_viewpager_tip_select_dot);
            }else {
                view.setBackgroundResource(R.drawable.shape_viewpager_tip_not_select_dot);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private void loadDataWss(){
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("Type", "workSiteHome"));
//        pairs.add(new BasicNameValuePair("Site_Id","354"));
        pairs.add(new BasicNameValuePair("Site_Id",site_id));
        pairs.add(new BasicNameValuePair("Customer_Id",LoginServiceManeger.instance().getLoginUserId()));

        HttpRestClient.doGetWss(pairs, new OkHttpClientManager.ResultCallback<String>() {

            private DoctorWorkstationMainEntity.ResultBean.SiteServiceBean siteServiceTwo;
            private DoctorWorkstationMainEntity.ResultBean.SiteServiceBean siteServiceOne;
            private DoctorWorkstationMainEntity.ResultBean.SiteeValuateBean evaluate;
            private DoctorWorkstationMainEntity.ResultBean.SiteInfoBean siteInfo;

            @Override
            public void onError(Request request, Exception e) {

                tipTv.setText(errTips);
            }

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Log.i("asdf",response);
                DoctorWorkstationMainEntity doctorWorkstationMainEntity = gson.fromJson(response, DoctorWorkstationMainEntity.class);
                resultBean = doctorWorkstationMainEntity.getResult();
                siteInfo = resultBean.getSiteInfo();
                if (siteInfo.getIS_FOLLOW()==0) {
                    workAttention.setImageResource(R.drawable.works_shoucang);
                    IS_FOLLOW=0;
                }else {
                    workAttention.setImageResource(R.drawable.quxiaoshoucang);
                    IS_FOLLOW=1;
                }
                workName.setText(siteInfo.getDOCTOR_NAME());
                departmentName.setText(siteInfo.getOFFICE_NAME());
                workHospital.setText(siteInfo.getSITE_HOSPOTAL());
                browsNum.setText(siteInfo.getVISIT_TIME()+"");
                doctorNum.setText(siteInfo.getMEMBER_NUM()+"");
                hospitalIntroduction.setText(siteInfo.getHOSPITAL_DESC());
                workIntroduction.setText(siteInfo.getSITE_DESC());
                masterIntroduction.setText(siteInfo.getSITE_CREATEOR_DESC());
                visibleIntroduction(hospitalIntroduction,hospitalIntroductionMore);
                visibleIntroduction(workIntroduction,workIntroductionMore);
                visibleIntroduction(masterIntroduction,masterIntroductionMore);
                evaluate = resultBean.getSiteeValuate();
                if (evaluate!=null){
                    userEvaluateName.setText(evaluate.getCUSTOMER_NAME());
                    userEvaluateContent.setText(evaluate.getREPLY_CONTENT());
                }else {
                    userEvaluateName.setVisibility(View.GONE);
                    moreEvaluate.setVisibility(View.GONE);
                    userEvaluateContent.setText("暂无评价");
                }
                qrCodeUrl=resultBean.getQrCodeUrl();
//                siteServiceOne = resultBean.getSiteService().get(0);
                List<DoctorWorkstationMainEntity.ResultBean.SiteServiceBean> siteService = resultBean.getSiteService();
                for (int i = 0; i < siteService.size(); i++) {
                    if (siteService.get(i).getSERVICE_TYPE_ID()==5){
                        siteServiceOne = siteService.get(i);
                        break;
                    }
                }
                if (siteServiceOne==null){
                    zixunPrice.setText("RMB:?/次");
                    tvZixunNum.setText("共0次购买");
                }else {
                    zixunPrice.setText("RMB:"+siteServiceOne.getSERVICE_PRICE()+"/次");
                    tvZixunNum.setText("共"+siteServiceOne.getORDER_COUNT()+"次购买");
                }
                siteMember = resultBean.getSiteMember();
                DoctorWorkstationMainActivity context = DoctorWorkstationMainActivity.this;
                WorksationTeamMainAdapter mainAdapter = new WorksationTeamMainAdapter(context, siteMember);
                mainAdapter.setOnRecyclerClickListener(context);
                memberRecycler.setLayoutManager(new GridLayoutManager(context,3));
                memberRecycler.setAdapter(mainAdapter);
                loadVpBanner(workBannerVp, vpDotLinear);
                tipTv.setVisibility(View.GONE);
                Log.i("haha", "onResponse: "+response);
            }
        },this);

    }

    @Override
    public void onRecyclerItemClickListener(int position, View itemView,int type) {
        Intent intent = new Intent(this, DoctorInfoActivity.class);
        intent.putExtra("customer_id",siteMember.get(position).getCUSTOMER_ID());
        intent.putExtra(DoctorInfoActivity.SITE_ID,resultBean.getSiteInfo().getSITE_ID());
        startActivity(intent);
    }

    private void visibleIntroduction(TextView introduction,TextView more){
        if (introduction.getLineCount()<3){
            more.setVisibility(View.GONE);
        }else {
            more.setVisibility(View.VISIBLE);
        }
    }
    //分享回调
    @Override
    public void onCancel(Platform arg0, int arg1) {//回调的地方是子线程，进行UI操作要用handle处理
        handler.sendEmptyMessage(5);
    }

    @Override
    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {//回调的地方是子线程，进行UI操作要用handle处理
        if (arg0.getName().equals(SinaWeibo.NAME)) {// 判断成功的平台是不是新浪微博
            handler.sendEmptyMessage(1);
        } else if (arg0.getName().equals(Wechat.NAME)) {
            handler.sendEmptyMessage(2);
        } else if (arg0.getName().equals(WechatMoments.NAME)) {
            handler.sendEmptyMessage(3);
        } else if (arg0.getName().equals(QZone.NAME)) {
            handler.sendEmptyMessage(4);
        }
    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {//回调的地方是子线程，进行UI操作要用handle处理
        arg2.printStackTrace();
        Message msg = new Message();
        msg.what = 6;
        msg.obj = arg2.getMessage();
        handler.sendMessage(msg);
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(getApplicationContext(), "微博分享成功", Toast.LENGTH_LONG).show();
                    break;

                case 2:
                    Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "朋友圈分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), "QQ分享成功", Toast.LENGTH_LONG).show();
                    break;

                case 5:
                    Toast.makeText(getApplicationContext(), "取消分享", Toast.LENGTH_LONG).show();
                    break;
                case 6:
                    Toast.makeText(getApplicationContext(), "分享失败啊" + msg.obj, Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }
    };


    class BannerVpAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return mBannerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mBannerList.get(position%6));

            return mBannerList.get(position%6);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mBannerList.get(position%6));
        }
    }

    private void quitPopWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    private void showShare() {
        if (mPopupWindow == null) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.alert_dialog_share, null);
            view.findViewById(R.id.friendcircle).setOnClickListener(this);
            view.findViewById(R.id.wechat).setOnClickListener(this);
            view.findViewById(R.id.weibo).setVisibility(View.GONE);
            view.findViewById(R.id.qqroom).setVisibility(View.GONE);
//            view.findViewById(R.id.weibo).setOnClickListener(this);
//            view.findViewById(R.id.qqroom).setOnClickListener(this);
            view.findViewById(R.id.btn_cancel).setOnClickListener(this);

            mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams params = DoctorWorkstationMainActivity.this.getWindow()
                            .getAttributes();
                    params.alpha = 1.0f;
                    DoctorWorkstationMainActivity.this.getWindow().setAttributes(params);
                }
            });
        } else if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            return;
        }

        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.alpha = 0.5f;
        this.getWindow().setAttributes(params);
        mPopupWindow.showAtLocation(DoctorWorkstationMainActivity.this.findViewById(R.id.work_share), Gravity.BOTTOM, 0, 0);

    }

    /**
     * 微信朋友圈
     */
    private void sendWXMS() {
        //2、设置分享内容
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
        sp.setTitle(getString(R.string.string_share_title)); //分享标题
        sp.setText(getString(R.string.string_share_content)); //分享文本
        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
//        sp.setImageData(getBitmapFromUri(uri));
        sp.setUrl("https://www.61120.cn/DuoMeiHealth/DO.action?op=site&Site_ID=" + resultBean.getSiteInfo().getSITE_ID());   //网友点进链接后，可以看到分享的详情
//          sp.setUrl(HTalkApplication.getHttpUrls().HTML + "/shopstroe.html?" + "doctor_id=" + resultBean.getSiteeValuate().getDOCTOR_ID() + "?customer_id=" + resultBean.getSiteeValuate().getCUSTOMER_ID());   //网友点进链接后，可以看到分享的详情
        //3、非常重要：获取平台对象
        Platform wechat = ShareSDK.getPlatform(WechatMoments.NAME);
        wechat.setPlatformActionListener(DoctorWorkstationMainActivity.this); // 设置分享事件回调
        // 执行分享
        wechat.share(sp);
    }

    /**
     * 微信分享
     */
    private void sendWX() {
        //2、设置分享内容
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
        sp.setTitle(getString(R.string.string_share_title)); //分享标题
        sp.setText(getString(R.string.string_share_content)); //分享文本
//        sp.setImageData(getBitmapFromUri(uri));
        // sp.setUrl(HTalkApplication.getHttpUrls().HTML + "/shopstroe.html?" + "doctor_id=" + doctor_id + "?customer_id=" + customer_id);   //网友点进链接后，可以看到分享的详情
        sp.setUrl("https://www.61120.cn/DuoMeiHealth/DO.action?op=site&Site_ID=" + resultBean.getSiteInfo().getSITE_ID());   //网友点进链接后，可以看到分享的详情
        //3、非常重要：获取平台对象
        Platform wechatms = ShareSDK.getPlatform(Wechat.NAME);
        wechatms.setPlatformActionListener(DoctorWorkstationMainActivity.this); // 设置分享事件回调
        // 执行分享
        wechatms.share(sp);
    }

}
