package com.yksj.consultation.son.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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

import com.baidu.mapapi.map.Text;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.switfpass.pay.activity.QrcodeActivity;
import com.yksj.consultation.adapter.DoctorInfoServiceAdapter;
import com.yksj.consultation.adapter.DoctorInfoToolsAdapter;
import com.yksj.consultation.adapter.WorkstationIntroduceAdapter;
import com.yksj.consultation.adapter.WorkstationServiceAdapter;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.CommonwealAidAty;
import com.yksj.consultation.son.consultation.PAtyConsultStudioGoPaying;
import com.yksj.consultation.son.consultation.bean.ServiceType;
import com.yksj.consultation.son.friend.BuyServiceListFromPatientActivity;
import com.yksj.consultation.son.listener.OnRecyclerClickListener;
import com.yksj.healthtalk.entity.DoctorInfoEntity;
import com.yksj.healthtalk.entity.DoctorworksTeamEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
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
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorInfoActivity extends Activity implements View.OnClickListener, OnRecyclerClickListener, PlatformActionListener {
    private String qrCodeUrl = "";
    private TextView homeTitle;
    private CircleImageView doctorAvatar;
    private TextView doctorName;
    private TextView doctorAddress;
    private TextView chiefDoctor;
    private TextView hospitalDepartment;
    private TextView doctorInfoHospital;
    private List<DoctorInfoEntity.ResultBean.SiteDescBean> siteDescList=new ArrayList<>();
    private List<DoctorInfoEntity.ResultBean.SiteServiceBean> siteServiceList=new ArrayList<>();
    private List<DoctorInfoEntity.ResultBean.DoctorServiceBean> doctorServiceList=new ArrayList<>();
    private List<DoctorInfoEntity.ResultBean.ToolsBean> toolsList=new ArrayList<>();
    private WorkstationIntroduceAdapter introduceAdapter;
    private TextView doctorIntroduction;
    private TextView doctorIntroductionMore;
    private TextView userEvaluateName;
    private TextView userEvaluateContent;
    private WorkstationServiceAdapter serviceAdapter;
    private DoctorInfoServiceAdapter doctorInfoServiceAdapter;
    private DoctorInfoEntity.ResultBean result;
    private int customer_id=-1;
    private boolean isAttention = true;
    public static final String SITE_ID="site_id";
    //弹出分享窗口
    PopupWindow mPopupWindow;
    private String customer_id_a = LoginServiceManeger.instance().getLoginUserId();
    private ImageView imAttention;
    private DoctorInfoToolsAdapter toolsAdapter;
    private int site_id;
    private TextView tipTv;
    private String errTips = "加载失败，点击重试";
    private TextView moreEvaluate;
    private LinearLayout lineWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_info);
        initView();
        loadDataWorks();
    }

    private void initView() {
        customer_id = getIntent().getIntExtra("customer_id",-1);
        Log.i("ggg", "initView: " +customer_id);
        site_id = getIntent().getIntExtra(SITE_ID,-1);
        ImageView back= (ImageView) findViewById(R.id.title_back);
        back.setOnClickListener(this);
        homeTitle = (TextView) findViewById(R.id.homeTitle);
        doctorAvatar = (CircleImageView) findViewById(R.id.doctorAvatar);
        doctorName = (TextView) findViewById(R.id.doctorName);
        doctorAddress = (TextView) findViewById(R.id.doctorAddress);
        chiefDoctor = (TextView) findViewById(R.id.chiefDoctor);
        hospitalDepartment = (TextView) findViewById(R.id.hospital_department);
        doctorInfoHospital = (TextView) findViewById(R.id.doctor_info_hospital);
        tipTv = (TextView) findViewById(R.id.tipTv);
        tipTv.setOnClickListener(this);
        LinearLayout QRLine= (LinearLayout) findViewById(R.id.QRLine);
        QRLine.setOnClickListener(this);
        LinearLayout shareLine= (LinearLayout) findViewById(R.id.shareLine);
        shareLine.setOnClickListener(this);
        LinearLayout LectureHallLine= (LinearLayout) findViewById(R.id.LectureHallLine);
        LectureHallLine.setOnClickListener(this);
        LinearLayout wordLinear= (LinearLayout) findViewById(R.id.wordLinear);
        wordLinear.setOnClickListener(this);
        findViewById(R.id.docShare).setOnClickListener(this);
        imAttention = (ImageView) findViewById(R.id.docAttention);
        imAttention.setOnClickListener(this);
        RecyclerView worksRecycler= (RecyclerView) findViewById(R.id.worksRecycler);
        introduceAdapter = new WorkstationIntroduceAdapter(siteDescList, this);
        introduceAdapter.setmOnRecyclerClickListener(this);
        introduceAdapter.setRecyclerType(0);
        worksRecycler.setLayoutManager(new LinearLayoutManager(this));
        worksRecycler.setAdapter(introduceAdapter);
        RecyclerView worksServiceRecycler = (RecyclerView) findViewById(R.id.worksServiceRecycler);
        serviceAdapter = new WorkstationServiceAdapter(siteServiceList, this);
        serviceAdapter.setmOnRecyclerClickListener(this);
        serviceAdapter.setRecyclerType(1);
        worksServiceRecycler.setLayoutManager(new LinearLayoutManager(this));
        worksServiceRecycler.setAdapter(serviceAdapter);
        RecyclerView toolsRecycler= (RecyclerView) findViewById(R.id.toolsRecycler);
        toolsAdapter = new DoctorInfoToolsAdapter(toolsList, this);
        toolsAdapter.setmOnRecyclerClickListener(this);
        toolsAdapter.setRecyclerType(3);
        toolsRecycler.setLayoutManager(new LinearLayoutManager(this));
        toolsRecycler.setAdapter(toolsAdapter);
        RecyclerView doctorServiceRecycler= (RecyclerView) findViewById(R.id.doctorServiceRecycler);
        doctorInfoServiceAdapter = new DoctorInfoServiceAdapter(doctorServiceList, this);
        doctorInfoServiceAdapter.setmOnRecyclerClickListener(this);
        doctorInfoServiceAdapter.setRecyclerType(2);
        doctorServiceRecycler.setLayoutManager(new LinearLayoutManager(this));
        doctorServiceRecycler.setAdapter(doctorInfoServiceAdapter);
        doctorIntroduction = (TextView) findViewById(R.id.doctorIntroduction);
        doctorIntroductionMore = (TextView) findViewById(R.id.doctorIntroductionMore);
        doctorIntroductionMore.setOnClickListener(this);
        userEvaluateName = (TextView) findViewById(R.id.userEvaluateName);
        userEvaluateContent = (TextView) findViewById(R.id.userEvaluateContent);
        moreEvaluate = (TextView) findViewById(R.id.moreEvaluate);
        moreEvaluate.setOnClickListener(this);
        LinearLayout homeTools= (LinearLayout) findViewById(R.id.homeTools);
        homeTools.setOnClickListener(this);
        Button menzenyuyueBtn= (Button) findViewById(R.id.menzenyuyueBtn);
        menzenyuyueBtn.setOnClickListener(this);
        lineWork = (LinearLayout) findViewById(R.id.lineWork);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:finish();break;
            case R.id.tipTv:
                if (tipTv.getText().equals(errTips)){
                    tipTv.setText("加载中...");
                    loadDataWorks();
                }
                break;
            case R.id.docShare:
                showShare();
                break;
            case R.id.friendcircle:
                sendWXMS();
                quitPopWindow();
                break;
            case R.id.wechat:
                sendWX();
                quitPopWindow();
                break;
            case R.id.docAttention:
                if (isAttention) {
                    cancelCare();
                } else {
                    addCare();
                }
                break;
            case R.id.QRLine:
                Intent intent1 = new Intent(this, QRcodeDoctorActivity.class);
                intent1.putExtra("imgHeader",result.getICON_DOCTOR_PICTURE());
                intent1.putExtra("specially",result.getDOCTOR_SPECIALLY());
                intent1.putExtra("hisInfo",result.getDOCTOR_HOSPITAL()+"\t\t|\t\t"+result.getOFFICE_NAME());
                intent1.putExtra("doctor_id",result.getCUSTOMER_ID());
                intent1.putExtra("doctorName",result.getDOCTOR_REAL_NAME());
                intent1.putExtra("qrCodeUrl",qrCodeUrl);
                startActivity(intent1);
                break;
            case R.id.shareLine:
                Intent shareIntetn = new Intent(this, FamousDoctorShareActivity.class);
                shareIntetn.putExtra("type","doctor");
                shareIntetn.putExtra("Doctor_ID",result.getCUSTOMER_ID());
                startActivity(shareIntetn);
                break;
            case R.id.LectureHallLine:
                Intent intent4 = new Intent(this, HealthLectureActivity.class);
                intent4.putExtra("lectureType","doctor");
                intent4.putExtra("doctorName",result.getDOCTOR_REAL_NAME());
                intent4.putExtra("customer_id",result.getCUSTOMER_ID()+"");
                startActivity(intent4);
                break;
            case R.id.wordLinear:
                if (siteDescList.size()==0) {
                    ToastUtil.onShow(this,"该医生未加入工作站",1000);
                }else {
                    Intent intent = new Intent(this,DoctorWorkstationActivity.class);
                    intent.putExtra("doctor_id",result.getCUSTOMER_ID());
                    startActivity(intent);
                }
                break;
            case R.id.doctorIntroductionMore:break;
            case R.id.moreEvaluate:
               Intent intent = new Intent(this, EvaluationActivity.class);
//                intent = new Intent(this, SiteCommentListActivity.class);
                intent.putExtra("type","doctor");
                intent.putExtra("Doctor_ID", result.getCUSTOMER_ID()+"");
                startActivity(intent);
                break;

            case R.id.homeTools:break;
            case R.id.menzenyuyueBtn:
                Intent intent2 = new Intent(DoctorInfoActivity.this, BuyServiceListFromPatientActivity.class);
                intent2.putExtra("consultId", "");
                intent2.putExtra(BuyServiceListFromPatientActivity.DOCTOR_NAME, result.getDOCTOR_REAL_NAME());
                intent2.putExtra(BuyServiceListFromPatientActivity.DOCTOR_ID, result.getCUSTOMER_ID());//医生ID
                intent2.putExtra("type", 3);
                startActivity(intent2);
                break;
        }
    }

    private void loadDataWorks() {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("op", "queryDoctorInfo"));
        pairs.add(new BasicNameValuePair("customer_id", customer_id+""));
//        pairs.add(new BasicNameValuePair("customer_id", "3774"));
//        pairs.add(new BasicNameValuePair("loginUserId", "3774"));
        pairs.add(new BasicNameValuePair("loginUserId", customer_id_a+""));
        if (site_id!=-1) {
            pairs.add(new BasicNameValuePair("site_id", site_id+""));
        }else{
            pairs.add(new BasicNameValuePair("site_id",""));
        }
        HttpRestClient.doGetWorks(pairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                tipTv.setText(errTips);
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
            }

            @Override
            public void onAfter() {
                super.onAfter();
            }
            @Override
            public void onResponse(String response) {
                if (response==null)return;
                Log.i("ggg", "onResponse: "+response);
                Gson gson = new Gson();
                DoctorInfoEntity doctorInfoEntity = gson.fromJson(response, DoctorInfoEntity.class);
                if (doctorInfoEntity.getCode().equals("0")) {
                    Toast.makeText(DoctorInfoActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                    return;
                }
                result = doctorInfoEntity.getResult();
                qrCodeUrl=result.getQrCodeUrl();
                homeTitle.setText(result.getDOCTOR_REAL_NAME()+"医生主页");
                Glide.with(DoctorInfoActivity.this).load(ImageLoader.getInstance().getDownPathUri(result.getICON_DOCTOR_PICTURE()))
                        .placeholder(R.drawable.default_head_doctor).error(R.drawable.default_head_doctor).into(doctorAvatar);
                doctorName.setText(result.getDOCTOR_REAL_NAME());
                doctorAddress.setText(result.getWORK_LOCATION_DESC());
                chiefDoctor.setText(result.getTITLE_NAME());
                hospitalDepartment.setText(result.getOFFICE_NAME());
                doctorInfoHospital.setText(result.getDOCTOR_HOSPITAL());
                siteDescList.addAll(result.getSiteDesc());
                if (siteDescList.size()==0) {
                    lineWork.setVisibility(View.GONE);
                }
                introduceAdapter.notifyDataSetChanged();
                doctorIntroduction.setText(result.getINTRODUCTION());
                if (doctorIntroduction.getLineCount()<3){
                    doctorIntroductionMore.setVisibility(View.GONE);
                }else {
                    doctorIntroductionMore.setVisibility(View.VISIBLE);
                }

                DoctorInfoEntity.ResultBean.EvaluateBean evaluate = result.getEvaluate();
                if (evaluate!=null){
                    userEvaluateName.setText(evaluate.getREAL_NAME());
                    userEvaluateContent.setText(evaluate.getNOTE());
                }else {
                    userEvaluateName.setVisibility(View.GONE);
                    userEvaluateContent.setText("暂无评价");
                    moreEvaluate.setVisibility(View.GONE);
                }

                siteServiceList.addAll(result.getSiteService());
                serviceAdapter.notifyDataSetChanged();

                toolsList.addAll(result.getTools());
                toolsAdapter.notifyDataSetChanged();

                List<DoctorInfoEntity.ResultBean.DoctorServiceBean> doctorService = result.getDoctorService();
                doctorServiceList.addAll(doctorService);
                doctorInfoServiceAdapter.notifyDataSetChanged();

                if (result.getIsFollow()==1) {
                    isAttention=false;
                    imAttention.setSelected(true);
                }else {
                    isAttention=true;
                    imAttention.setSelected(false);
                }
                tipTv.setVisibility(View.GONE);
            }
        }, this);

    }

    @Override
    public void onRecyclerItemClickListener(int position, View itemView,int type) {
        //0是工作站介绍的type  目前没用
        if (type==1){//工作站服务
            DoctorInfoEntity.ResultBean.SiteServiceBean siteServiceBean = siteServiceList.get(position);
            Intent intent = new Intent(DoctorInfoActivity.this, PAtyConsultStudioGoPaying.class);
            intent.putExtra("service_id", siteServiceBean.getSERVICE_TYPE_ID()+"");
            intent.putExtra("service_item_id", "0");
            intent.putExtra("price", siteServiceBean.getSERVICE_PRICE()+"");//价格
            intent.putExtra("doctor_id", result.getCUSTOMER_ID()+"");//医生ID
            intent.putExtra("expertName", result.getDOCTOR_REAL_NAME());//医生名字
            intent.putExtra("officeName", result.getOFFICE_NAME());//医生科室
            intent.putExtra(PAtyConsultStudioGoPaying.SERVICETYPEID, siteServiceBean.getSERVICE_TYPE_ID()+"");
            intent.putExtra(PAtyConsultStudioGoPaying.SITE_ID, siteServiceBean.getSITE_ID()+"");
            startActivity(intent);
//            workstationTuWenZiXun(siteServiceBean.getSITE_ID()+"");
        }else if (type==2){//医生服务
            DoctorInfoEntity.ResultBean.DoctorServiceBean doctorServiceBean = doctorServiceList.get(position);
            if (doctorServiceBean.getSERVICE_TYPE_ID() == 3) {//门诊预约
                Intent intent2 = new Intent(DoctorInfoActivity.this, BuyServiceListFromPatientActivity.class);
                intent2.putExtra("consultId", "");
                intent2.putExtra(BuyServiceListFromPatientActivity.DOCTOR_NAME, result.getDOCTOR_REAL_NAME());
                intent2.putExtra(BuyServiceListFromPatientActivity.DOCTOR_ID, result.getCUSTOMER_ID());//医生ID
                intent2.putExtra("type", 3);
                startActivity(intent2);
            }else {
                Intent intent = new Intent(DoctorInfoActivity.this, PAtyConsultStudioGoPaying.class);
                intent.putExtra("service_id", doctorServiceBean.getSERVICE_TYPE_ID()+"");
                intent.putExtra("service_item_id", doctorServiceBean.getSERVICE_ITEM_ID()+"");
                intent.putExtra("price", doctorServiceBean.getSERVICE_PRICE()+"");//价格
                intent.putExtra("doctor_id", result.getCUSTOMER_ID()+"");//医生ID
                intent.putExtra("expertName", result.getDOCTOR_REAL_NAME());//医生名字
                intent.putExtra("officeName", result.getOFFICE_NAME());//医生科室
                startActivity(intent);
            }
        }else if (type==3){//工具箱
            Intent intent = new Intent(this, CommonwealAidAty.class);
            intent.putExtra(CommonwealAidAty.URL, toolsList.get(position).getTOOL_URL());
            intent.putExtra(CommonwealAidAty.TITLE, toolsList.get(position).getTOOL_NAME());
            startActivity(intent);
        }
    }

    private void workstationTuWenZiXun(String site_id) {
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
                    ToastUtil.onShow(DoctorInfoActivity.this,message,2000);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },this);


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
                    WindowManager.LayoutParams params = DoctorInfoActivity.this.getWindow()
                            .getAttributes();
                    params.alpha = 1.0f;
                    DoctorInfoActivity.this.getWindow().setAttributes(params);
                }
            });
        } else if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            return;
        }

        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.alpha = 0.5f;
        this.getWindow().setAttributes(params);
        mPopupWindow.showAtLocation(this.findViewById(R.id.docShare), Gravity.BOTTOM, 0, 0);

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
//        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
//        sp.setImageData(getBitmapFromUri(uri));
        sp.setUrl("https://www.61120.cn/DuoMeiHealth/DO.action?op=doctor&Doctor_ID=" + result.getCUSTOMER_ID()+"&Site_ID="+site_id);   //网友点进链接后，可以看到分享的详情
        //  sp.setUrl(HTalkApplication.getHttpUrls().HTML + "/shopstroe.html?" + "doctor_id=" + doctor_id + "?customer_id=" + customer_id);   //网友点进链接后，可以看到分享的详情
        //3、非常重要：获取平台对象
        Platform wechat = ShareSDK.getPlatform(WechatMoments.NAME);
        wechat.setPlatformActionListener(this); // 设置分享事件回调
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
        sp.setUrl("https://www.61120.cn/DuoMeiHealth/DO.action?op=doctor&Doctor_ID=" + result.getCUSTOMER_ID()+"&Site_ID="+site_id);   //网友点进链接后，可以看到分享的详情
//        Log.i("kkk", "https://www.61120.cn/DuoMeiHealth/DO.action?op=doctor&Doctor_ID=" + result.getCUSTOMER_ID()+"&Site_ID="+site_id);
        //3、非常重要：获取平台对象
        Platform wechatms = ShareSDK.getPlatform(Wechat.NAME);
        wechatms.setPlatformActionListener(this); // 设置分享事件回调
        // 执行分享
        wechatms.share(sp);
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

    /**
     * 取消关心
     */
    private void cancelCare() {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", customer_id_a);
        map.put("doctor_id", customer_id+"");
        HttpRestClient.OKHttpDoctorStudioCare(map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }
            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))) {
                        isAttention = false;
                        imAttention.setSelected(true);
                        ToastUtil.showShort(obj.optString("message"));
                    } else {
                        ToastUtil.showShort(obj.optString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this);
    }
    /**
     * 添加关注
     */
    private void addCare() {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", customer_id_a);
        map.put("doctor_id", customer_id+"");
        HttpRestClient.OKHttpDoctorStudioCancelCare(map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))) {
                        isAttention = true;
                        imAttention.setSelected(false);
                        ToastUtil.showShort(obj.optString("message"));
                    } else {
                        ToastUtil.showShort(obj.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this);
    }
}
