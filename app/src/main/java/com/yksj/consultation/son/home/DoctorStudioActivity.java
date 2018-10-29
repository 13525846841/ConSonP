package com.yksj.consultation.son.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.yksj.consultation.adapter.UtilsAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.CommonwealAidAty;
import com.yksj.consultation.son.consultation.PAtyConsultStudioGoPaying;
import com.yksj.consultation.son.consultation.bean.ObjectType;
import com.yksj.consultation.son.consultation.bean.ServiceType;
import com.yksj.consultation.son.consultation.member.FlowMassageActivity;
import com.yksj.consultation.son.friend.BuyServiceListFromPatientActivity;
import com.yksj.healthtalk.bean.DoctorSimpleBean;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.FriendHttpUtil;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

/**
 * 医生工作室
 */
public class DoctorStudioActivity extends BaseFragmentActivity implements View.OnClickListener, PlatformActionListener, AdapterView.OnItemClickListener {
    private List<JSONObject> mList = null;
    private TextView tv_doc_name;
    private TextView tv_position;
    private TextView tv_doc_pro;
    private TextView tv_doc_place;
    private TextView be_good_at;
    private TextView tv_doc_account;
    private ImageView imageView;
    private TextView comment_count;//评论数量
    private RatingBar mBar;// 评价星级
    private ImageView mTv;//右上角分享
    private ImageView care;
    private String doctor_id;
    private boolean isCare = false;
    private boolean isMenZhen = false;
    private boolean isHuiZhen = false;
    //  private ImageView menzhen;
//  private ImageView huizhen;
    private String expertName;//专家姓名
    private RelativeLayout appraise;//评价
    private String doctorType = "";//医生类型
    private CustomerInfoEntity entity;
    private String officeName = "";
    private String office_id = "";//科室id
    //弹出分享窗口
    PopupWindow mPopupWindow;
    private RelativeLayout mEmptyView;
    private LinearLayout ll_main;
    private Uri uri;

    private TextView mNConsult;

    private RelativeLayout rl_picandcul;//图文
    private RelativeLayout rl_phone;//电话
    private RelativeLayout rl_consul;//包月
    private RelativeLayout rl_video;//视频
    private RelativeLayout rl_addnum;//门诊
    private RelativeLayout rl_online2;//会诊

    private RelativeLayout rl_picandcul_site;//图文
    private RelativeLayout rl_phone_site;//电话
    private RelativeLayout rl_video_site;//视频


    private TextView freeNumber;
    private TextView picandcul_price;
    private TextView picandcul_price_site;
    private TextView phone_price;
    private TextView phone_price_site;
    private TextView consul_price;
    private TextView video_price;
    private TextView video_price_site;
    private TextView menzhen_price;
    private TextView huizhen_price;

    private String picandculPrice;
    private String phonePrice;
    private String consulPrice;
    private String videoPrice;

    private String picServiceId;//图文咨询ID
    private String phoneServiceId;//电话咨询ID
    private String consulServiceId;//包月咨询ID
    private String videoServiceId;//视频咨询ID

    private String experienceNumber;//体验条数
    private String currentDoctorId;//当前医生ID

    private List<JSONObject> orders = null;

    //5 图文 6 电话 7 包月  8 视频 free_order = 1 免费的
    //service_type_id,pay_id , free_order
    private String picFeelOrderId = "";//体验咨询订单ID
    private String picOrderId = "";//图文咨询订单ID
    private String phoneOrderId = "";//电话咨询订单ID
    private String consulOrderId = "";//包月咨询订单ID
    private ListView mUtilsList;//工具箱
    private UtilsAdapter adapter;//工具箱适配器
    private List<JSONObject> utilsList;
    private DoctorSimpleBean dsb;

    public static final int SERVICEONE = 1;//留言咨询
    public static final int SERVICETWO = 2;//预约咨询
    public static final int SERVICETHREE = 3;//门诊加号
    public static final int SERVICEFOUR = 4;//定制服务
    public static final int SERVICEFIVE = 5;//图文咨询
    public static final int SERVICESIX = 6;//电话咨询
    public static final int SERVICESEVEN = 7;//包月咨询
    public static final int SERVICEEIGHT = 8;//视频咨询


    public static final String SITE_ID = "site_id";//集团id
    private String site_id = "";//集团id
    private TextView textSiteName;
    private boolean siteTuwen = false;//医生集团 图文咨询开通标记
    private boolean tuWen = false;//医生工作站 图文咨询开通标记
    private boolean baoyue = false;//医生工作站 包月开通标记

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_studio);
        initView();
    }

    private void initView() {
        initTitle();
        Intent intent = getIntent();
        doctor_id = intent.getStringExtra("DOCTOR_ID");
        site_id = intent.getStringExtra(SITE_ID);
//        Log.i("kkk", "initView: "+site_id);
        if (getIntent().hasExtra("data")) {
            dsb = (DoctorSimpleBean) getIntent().getSerializableExtra("data");
        }


        ll_main = (LinearLayout) findViewById(R.id.studio_main);
        mEmptyView = (RelativeLayout) findViewById(R.id.load_faile_layout);
        textSiteName = (TextView) findViewById(R.id.site_name);
        titleLeftBtn.setOnClickListener(this);

        mTv = (ImageView) findViewById(R.id.iv_share);
        mTv.setVisibility(View.VISIBLE);
        mTv.setOnClickListener(this);

        care = (ImageView) findViewById(R.id.iv_care);
        care.setVisibility(View.VISIBLE);
        care.setOnClickListener(this);

//        menzhen = (ImageView) findViewById(R.id.menzhen);
//        huizhen = (ImageView) findViewById(R.id.huizhen);
//        menzhen.setOnClickListener(this);
//        huizhen.setOnClickListener(this);

        appraise = (RelativeLayout) findViewById(R.id.appraise);
        imageView = (ImageView) findViewById(R.id.det_img_head);
        tv_doc_name = (TextView) findViewById(R.id.tv_doc_name);
        tv_doc_pro = (TextView) findViewById(R.id.tv_doc_pro);
        tv_doc_place = (TextView) findViewById(R.id.tv_doc_place);
        be_good_at = (TextView) findViewById(R.id.be_good_at);
        tv_position = (TextView) findViewById(R.id.tv_position);
        comment_count = (TextView) findViewById(R.id.comment_count);
        mBar = (RatingBar) findViewById(R.id.rb_speed_studio);
        tv_doc_account = (TextView) findViewById(R.id.tv_doc_account);
        mNConsult = (TextView) findViewById(R.id.text_number_for_consult);
        freeNumber = (TextView) findViewById(R.id.free_number);

        rl_picandcul = (RelativeLayout) findViewById(R.id.rl_picandcul);
        rl_phone = (RelativeLayout) findViewById(R.id.rl_phone);
        rl_consul = (RelativeLayout) findViewById(R.id.rl_consul);
        rl_video = (RelativeLayout) findViewById(R.id.rl_video);
        rl_addnum = (RelativeLayout) findViewById(R.id.rl_addnum);
        rl_online2 = (RelativeLayout) findViewById(R.id.rl_online2);

        rl_picandcul_site = (RelativeLayout) findViewById(R.id.rl_picandcul_site);
        rl_phone_site = (RelativeLayout) findViewById(R.id.rl_phone_site);
        rl_video_site = (RelativeLayout) findViewById(R.id.rl_video_site);

        picandcul_price = (TextView) findViewById(R.id.picandcul_price);
        picandcul_price_site = (TextView) findViewById(R.id.picandcul_price_site);
        phone_price = (TextView) findViewById(R.id.phone_price);
        phone_price_site = (TextView) findViewById(R.id.phone_price_site);
        consul_price = (TextView) findViewById(R.id.consul_price);
        video_price_site = (TextView) findViewById(R.id.video_price_site);
        video_price = (TextView) findViewById(R.id.video_price);
        menzhen_price = (TextView) findViewById(R.id.menzhen_price);
        huizhen_price = (TextView) findViewById(R.id.huizhen_price);

        findViewById(R.id.rl_pic_free).setOnClickListener(this);
        findViewById(R.id.rl_picandcul).setOnClickListener(this);
        findViewById(R.id.rl_picandcul_site).setOnClickListener(this);
        findViewById(R.id.rl_phone_site).setOnClickListener(this);
        findViewById(R.id.rl_video_site).setOnClickListener(this);
        findViewById(R.id.rl_phone).setOnClickListener(this);
        findViewById(R.id.rl_consul).setOnClickListener(this);
        findViewById(R.id.rl_video).setOnClickListener(this);
        findViewById(R.id.rl_addnum).setOnClickListener(this);
        findViewById(R.id.rl_menzhen_site).setOnClickListener(this);
        findViewById(R.id.rl_online2).setOnClickListener(this);
        findViewById(R.id.appraise).setOnClickListener(this);


        mUtilsList = (ListView) findViewById(R.id.utils_lv);
        adapter = new UtilsAdapter(this);
        mUtilsList.setAdapter(adapter);
        mUtilsList.setOnItemClickListener(this);

        mBar.setStepSize(1f);
        entity = new CustomerInfoEntity();
        entity.setId(doctor_id);
        uri = Uri.parse("android.resource://" + HTalkApplication.getApplication().getPackageName() + "/"
                + R.drawable.launcher_logo);
        initUtilsData();// 加载工具箱内容

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    /**
     * 加载工具箱内容
     */
    private void initUtilsData() {
        findViewById(R.id.ll_tools).setVisibility(View.VISIBLE);
        Map<String, String> map = new HashMap<>();
        map.put("doctor_id", doctor_id);//doctor_id
        HttpRestClient.OKHttpDoctorStudioUtils(map, new HResultCallback<String>(this) {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if ("1".equals(obj.optString("code"))) {
                        utilsList = new ArrayList<JSONObject>();
                        JSONArray array = obj.getJSONArray("result");
                        JSONObject item;
                        for (int i = 0; i < array.length(); i++) {
                            item = array.getJSONObject(i);
                            utilsList.add(item);
                        }
                        adapter.onBoundData(utilsList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this);
    }

    private String customer_id = LoginServiceManeger.instance().getLoginUserId();
//    private String customer_id = LoginServiceManeger.instance().getLoginEntity().getId();
    /**
     * 加载数据
     */
    public boolean flag = false;

    private void initData() {
        if (HStringUtil.isEmpty(customer_id)) {
            ToastUtil.showShort("数据异常");
            return;
        }
        Map<String, String> map = new HashMap<>();
        if (isSited()) {
            map.put("show_flag", "1");//"239139"
            Log.i("ggg", "initData: 1");
        } else {
            map.put("show_flag", "0");//"239139"
            Log.i("ggg", "initData: 0");
        }
        map.put("customer_id", customer_id);//"239139"
        map.put("doctor_id", doctor_id);//"124951"

        HttpRestClient.OKHttpDoctorStudio(map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
//                findViewById(R.id.rl_pic_free).setVisibility(View.GONE);
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
//                findViewById(R.id.rl_pic_free).setVisibility(View.GONE);
            }

            @Override
            public void onAfter() {
                super.onAfter();
//                findViewById(R.id.rl_pic_free).setVisibility(View.VISIBLE);
            }

            @Override
            public void onResponse(String content) {
                Log.i("ggg", "onClick: "+content);
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))) {

                        mEmptyView.setVisibility(View.GONE);
                        ll_main.setVisibility(View.VISIBLE);

                        JSONObject infoObject = obj.optJSONObject("info");
                        JSONObject object = infoObject.optJSONObject("docInfo");
                        expertName = object.optString("DOCTOR_REAL_NAME");
                        tv_doc_name.setText(expertName);
                        titleTextV.setText(object.optString("DOCTOR_REAL_NAME") + "医生");
                        tv_doc_account.setText("医生六一账号:  " + object.optString("CUSTOMER_ACCOUNTS"));

                        experienceNumber = infoObject.optString("experienceCount");

                        if (infoObject.optString("isfriend").equals("true")) {
                            isCare = true;
                            care.setSelected(true);
                        } else if (infoObject.optString("isfriend").equals("false")) {
                            isCare = false;
                            care.setSelected(false);
                        }
                        JSONArray arrayIds = infoObject.getJSONArray("payIds");
                        int count = arrayIds.length();
                        if (count > 0) {
                            for (int i = 0; i < count; i++) {
                                JSONObject objId = arrayIds.getJSONObject(i);
                                if ("5".equals(objId.optString("SERVICE_TYPE_ID")) && "1".equals(objId.optString("FREE_ORDER"))) {
                                    picFeelOrderId = objId.optString("ORDER_ID");
                                }
                                if ("5".equals(objId.optString("SERVICE_TYPE_ID")) && !"1".equals(objId.optString("FREE_ORDER"))) {
                                    picOrderId = objId.optString("ORDER_ID");
                                }
                                if ("6".equals(objId.optString("SERVICE_TYPE_ID"))) {
                                    phoneOrderId = objId.optString("ORDER_ID");
                                }
                                if ("7".equals(objId.optString("SERVICE_TYPE_ID"))) {
                                    consulOrderId = objId.optString("ORDER_ID");
                                }
                            }
                        }
                        if (!HStringUtil.isEmpty(infoObject.optString("free_order"))) {
                            JSONObject freeOrderObject = infoObject.optJSONObject("free_order");
                            currentDoctorId = freeOrderObject.optString("SERVICE_CUSTOMER_ID");
                        }
                        if (!HStringUtil.isEmptyAndZero(infoObject.optString("site_id"))) {
                            site_id = infoObject.optString("site_id");
                        }

                        if (!HStringUtil.isEmptyAndZero(infoObject.optString("site_name"))) {
                            textSiteName.setText("集团: " + infoObject.optString("site_name"));
                        }

                        if (!HStringUtil.isEmpty(currentDoctorId)) {
                            if (currentDoctorId.equals(doctor_id)) {
                                if ("0".equals(infoObject.optString("experienceCount"))) {
                                    freeNumber.setText("0条");
                                } else {
                                    freeNumber.setText(infoObject.optString("experienceCount") + "条");
                                }
                            } else {
                                freeNumber.setVisibility(View.GONE);
                            }
                        } else {
                            if ("0".equals(infoObject.optString("experienceCount"))) {
                                freeNumber.setText("0条");
                            } else {
                                freeNumber.setText(infoObject.optString("experienceCount") + "条");
                            }
                        }

                        if (!HStringUtil.isEmpty(currentDoctorId)) {
                            if (currentDoctorId.equals(doctor_id)) {
//                                mNConsult.setVisibility(View.VISIBLE);
//                                mNConsult.setText("您还有" + infoObject.optString("experienceCount") + "条体验咨询的机会");
                                if ("0".equals(infoObject.optString("experienceCount"))) {
                                    mNConsult.setVisibility(View.GONE);
                                } else {
                                    mNConsult.setVisibility(View.VISIBLE);
                                    mNConsult.setText("您还有" + infoObject.optString("experienceCount") + "条体验咨询的机会");
                                }
                            } else {
                                mNConsult.setVisibility(View.GONE);
                            }
                        } else {
                            mNConsult.setVisibility(View.VISIBLE);
                            mNConsult.setText("您还有" + infoObject.optString("experienceCount") + "条体验咨询的机会");
                        }

//                        if ("0".equals(infoObject.optString("experienceCount"))) {
//                            mNConsult.setVisibility(View.GONE);
//                        } else {
//                            mNConsult.setVisibility(View.VISIBLE);
//                            mNConsult.setText("您还有" + infoObject.optString("experienceCount") + "条体验咨询的机会");
//                        }


                        /**
                         * 1是开通，0是没有开通
                         */
                        JSONObject object1 = infoObject.optJSONObject("isOpanTuwen");//图文
                        if (object1!=null){
                            if (isSited()) {
                                siteTuwen = object1.optBoolean("isBuyService");
                                rl_picandcul.setVisibility(View.GONE);
                                if (object1.optString("isOpen").equals("0")) {
                                    rl_picandcul_site.setVisibility(View.GONE);
                                } else if (object1.optString("isOpen").equals("1")) {
                                    rl_picandcul_site.setVisibility(View.VISIBLE);
                                    picandculPrice = object1.optString("price");
                                    picServiceId = object1.optString("service_item_id");
                                    picandcul_price.setText(picandculPrice + "元/次");
                                    picandcul_price_site.setText(picandculPrice + "元/次");
                                }
                            } else {
                                tuWen = object1.optBoolean("isBuyService");
                                if (object1.optString("isOpen").equals("0")) {
                                    rl_picandcul.setVisibility(View.GONE);
                                } else if (object1.optString("isOpen").equals("1") && object1.optString("Free_Medical_Flag").equals("0")) {
                                    rl_picandcul.setVisibility(View.VISIBLE);
                                    picandculPrice = object1.optString("price");
                                    picServiceId = object1.optString("service_item_id");
                                    picandcul_price.setText(picandculPrice + "元/次");
                                    picandcul_price_site.setText(picandculPrice + "元/次");
                                } else if (object1.optString("isOpen").equals("1") && object1.optString("Free_Medical_Flag").equals("1")) {
                                    rl_picandcul.setVisibility(View.VISIBLE);
                                    picandculPrice = object1.optString("Free_Medical_Price");
                                    picServiceId = object1.optString("service_item_id");
                                    picandcul_price.setText(picandculPrice + "元/次");
                                    picandcul_price_site.setText(picandculPrice + "元/次");
                                }
                            }
                        }

                        /*******电话咨询，暂时删掉***/
                        JSONObject object2 = infoObject.optJSONObject("isOpanDianHua");//电话
                        if (object2!=null){
                            //电话
                            if (isSited()) {
                                if (object2.optString("isOpen").equals("0")) {
                                    rl_phone_site.setVisibility(View.GONE);
                                } else if (object2.optString("isOpen").equals("1") && object2.optString("Free_Medical_Flag").equals("0")) {
                                    rl_phone_site.setVisibility(View.VISIBLE);
                                    phonePrice = object2.optString("price");
                                    phoneServiceId = object2.optString("service_item_id");
                                    phone_price_site.setText(phonePrice + "元/次");
                                } else if (object2.optString("isOpen").equals("1") && object2.optString("Free_Medical_Flag").equals("1")) {
                                    rl_phone_site.setVisibility(View.VISIBLE);
                                    phonePrice = object2.optString("Free_Medical_Price");
                                    phoneServiceId = object2.optString("service_item_id");
                                    phone_price_site.setText(phonePrice + "元/次");
                                }
                            } else {
                                if (object2.optString("isOpen").equals("0")) {
                                    rl_phone.setVisibility(View.GONE);
                                } else if (object2.optString("isOpen").equals("1") && object2.optString("Free_Medical_Flag").equals("0")) {
                                    rl_phone.setVisibility(View.VISIBLE);
                                    phonePrice = object2.optString("price");
                                    phoneServiceId = object2.optString("service_item_id");
                                    phone_price.setText(phonePrice + "元/次");
                                } else if (object2.optString("isOpen").equals("1") && object2.optString("Free_Medical_Flag").equals("1")) {
                                    rl_phone.setVisibility(View.VISIBLE);
                                    phonePrice = object2.optString("Free_Medical_Price");
                                    phoneServiceId = object2.optString("service_item_id");
                                    phone_price.setText(phonePrice + "元/次");
                                }
                            }
                        }


                        JSONObject object3 = infoObject.optJSONObject("isOpanBaoYue");
                        if (object3!=null){
                            //包月
                            baoyue = object3.optBoolean("isBuyService");
                            if (object3.optString("isOpen").equals("0")) {
                                rl_consul.setVisibility(View.GONE);
                            } else if (object3.optString("isOpen").equals("1") && object3.optString("Free_Medical_Flag").equals("0")) {
                                rl_consul.setVisibility(View.VISIBLE);
                                consulPrice = object3.optString("price");
                                consulServiceId = object3.optString("service_item_id");
                                consul_price.setText(consulPrice + "元/30天");
                            } else if (object3.optString("isOpen").equals("1") && object3.optString("Free_Medical_Flag").equals("1")) {
                                rl_consul.setVisibility(View.VISIBLE);
                                consulPrice = object3.optString("Free_Medical_Price");
                                consulServiceId = object3.optString("service_item_id");
                                consul_price.setText(consulPrice + "元/30天");
                            }
                        }


                        JSONObject object4 = infoObject.optJSONObject("isOpanShipin");//视频
                        if (object4!=null){
                            //视频
                            if (isSited()) {
                                if (object4.optString("isOpen").equals("0")) {
                                    rl_video_site.setVisibility(View.GONE);
                                } else if (object4.optString("isOpen").equals("1") && object4.optString("Free_Medical_Flag").equals("0")) {
                                    rl_video_site.setVisibility(View.VISIBLE);
                                    videoPrice = object4.optString("price");
                                    videoServiceId = object4.optString("service_item_id");
                                    video_price_site.setText(videoPrice + "元/次");
                                } else if (object4.optString("isOpen").equals("1") && object4.optString("Free_Medical_Flag").equals("1")) {
                                    rl_video_site.setVisibility(View.VISIBLE);
                                    videoPrice = object4.optString("Free_Medical_Price");
                                    videoServiceId = object4.optString("service_item_id");
                                    video_price_site.setText(videoPrice + "元/次");
                                }
                            } else {
                                if (object4.optString("isOpen").equals("0")) {
                                    rl_video.setVisibility(View.GONE);
                                } else if (object4.optString("isOpen").equals("1") && object4.optString("Free_Medical_Flag").equals("0")) {
                                    rl_video.setVisibility(View.VISIBLE);
                                    videoPrice = object4.optString("price");
                                    videoServiceId = object4.optString("service_item_id");
                                    video_price.setText(videoPrice + "元/次");
                                } else if (object4.optString("isOpen").equals("1") && object4.optString("Free_Medical_Flag").equals("1")) {
                                    rl_video.setVisibility(View.VISIBLE);
                                    videoPrice = object4.optString("Free_Medical_Price");
                                    videoServiceId = object4.optString("service_item_id");
                                    video_price.setText(videoPrice + "元/次");
                                }
                            }
                        }
                        JSONObject object5 = infoObject.optJSONObject("isOpanMenzhen");// 门诊
                        if (object5!=null){
                            if (isSited()){
                                if ("1".equals(object5.optString("isOpen"))) {
                                    findViewById(R.id.rl_menzhen_site).setVisibility(View.GONE);
                                    isHuiZhen = true;
                                    //  menzhen_price.setText(object5.optString("price")+"元/次");
                                } else if ("0".equals(object5.optString("isOpen"))) {
                                    isHuiZhen = false;
                                    findViewById(R.id.rl_menzhen_site).setVisibility(View.GONE);
                                    //menzhen_price.setText(object5.optString("price")+"元/次");
                                }
                            }else {
                                if ("1".equals(object5.optString("isOpen"))) {
                                    rl_addnum.setVisibility(View.VISIBLE);
                                    isHuiZhen = true;
                                    //  menzhen_price.setText(object5.optString("price")+"元/次");
                                } else if ("0".equals(object5.optString("isOpen"))) {
                                    isHuiZhen = false;
                                    rl_addnum.setVisibility(View.GONE);
                                    //menzhen_price.setText(object5.optString("price")+"元/次");
                                }
                            }
                        }


                        /**
                         * 在线会诊
                         */
                        JSONObject object6 = infoObject.optJSONObject("isOpanHui");//会诊
                        if ("true".equals(object6.optString("isOpanHui"))) {
                            rl_online2.setVisibility(View.VISIBLE);
                            isMenZhen = true;
                            if (HStringUtil.isEmpty(object6.optString("free_medical_price"))) {
                                if (HStringUtil.isEmpty(object6.optString("price"))) {
                                    huizhen_price.setText("");
                                } else {
                                    huizhen_price.setText(object6.optString("price") + "元/次");
                                }
                            } else {
                                huizhen_price.setText(object6.optString("free_medical_price") + "元/次");
                            }
                        } else if ("false".equals(object6.optString("isOpanHui"))) {
                            isMenZhen = false;
                            rl_online2.setVisibility(View.GONE);
                            if (HStringUtil.isEmpty(object6.optString("free_medical_price"))) {
                                if (HStringUtil.isEmpty(object6.optString("price"))) {
                                    huizhen_price.setText("");
                                } else {
                                    huizhen_price.setText(object6.optString("price") + "元/次");
                                }
                            } else {
                                huizhen_price.setText(object6.optString("free_medical_price") + "元/次");
                            }
                        }


                        if (infoObject.optString("DOCTOR_CLASS").equals("20")) {
                            doctorType = "doctor";
                            appraise.setVisibility(View.GONE);
                        } else if (infoObject.optString("isOpanHui").equals("10")) {
                            doctorType = "expert";
                            appraise.setVisibility(View.VISIBLE);
                        }


                        if (!HStringUtil.isEmpty(object.optString("TITLE_NAME"))) {
                            tv_position.setText(object.optString("TITLE_NAME"));
                        }
                        officeName = object.optString("OFFICE_NAME");
                        if (!HStringUtil.isEmpty(officeName)) {
                            tv_doc_pro.setText(officeName);
                        }
                        if (!HStringUtil.isEmpty(object.optString("DOCTOR_HOSPITAL"))) {
                            tv_doc_place.setText(object.optString("DOCTOR_HOSPITAL"));
                        } else {
                            tv_doc_place.setVisibility(View.GONE);
                        }


                        if (!HStringUtil.isEmpty(object.optString("DOCTOR_SPECIALLY"))) {
                            be_good_at.setText(object.optString("DOCTOR_SPECIALLY"));
                        }

//                        if (!HStringUtil.isEmpty(object.optString("Doctor_Office2"))) {
//                            office_id = object.optString("Doctor_Office2");
//                        }
                        if (!HStringUtil.isEmpty(object.optString("DOCTOR_OFFICE2"))) {
                            office_id = object.optString("DOCTOR_OFFICE2");
                        }
                        mBar.setRating(Float.parseFloat(object.optString("STAR_LEVEL")));

                        comment_count.setText("(" + object.optString("COMMENT_COUNT") + ")");

                        String url = HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW + object.optString("DOCTOR_PICTURE");
                        Picasso.with(DoctorStudioActivity.this).load(url).placeholder(R.drawable.default_head_doctor).into(imageView);

                        if (dsb == null) {
                            dsb = new DoctorSimpleBean();
                            dsb.CUSTOMER_ID = doctor_id;
                            dsb.SERVICE_PRICE = object6.optString("price");
                            dsb.DOCTOR_SPECIALLY = object.optString("DOCTOR_SPECIALLY");
                            dsb.DOCTOR_REAL_NAME = expertName;
                            dsb.TITLE_NAME = object.optString("TITLE_NAME");
                            dsb.UNIT_NAME = object.optString("DOCTOR_HOSPITAL");
                            dsb.ICON_DOCTOR_PICTURE = object.optString("DOCTOR_PICTURE");
                        }
                    } else {
                        titleTextV.setText("医生");
                        mEmptyView.setVisibility(View.VISIBLE);
                        ll_main.setVisibility(View.GONE);
                        ToastUtil.showShort(obj.optString("message"));
                    }

                    if (!isSited()) {//个人工作室
                        findViewById(R.id.ll_site_service).setVisibility(View.GONE);
                    } else {//带集团的
                        findViewById(R.id.ll_site_service).setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.iv_share:
                showShare();
                break;
            case R.id.appraise://医生评价
                intent = new Intent(DoctorStudioActivity.this, MyAssessActivity.class);
                intent.putExtra(MyAssessActivity.ID, doctor_id);
                startActivity(intent);
                break;
            case R.id.rl_pic_free://体验
                if (Integer.parseInt(experienceNumber) != 0) {
                    if (!HStringUtil.isEmpty(currentDoctorId)) {
                        if (currentDoctorId.equals(doctor_id)) {
                            if (Integer.parseInt(experienceNumber) == 10) {
                                DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), getResources().getString(R.string.studio_experience), "取消", "确定", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
                                    @Override
                                    public void onDismiss(DialogFragment fragment) {

                                    }

                                    @Override
                                    public void onClick(DialogFragment fragment, View v) {
                                        FriendHttpUtil.chatFromPerson(DoctorStudioActivity.this, doctor_id, expertName, picFeelOrderId, ObjectType.SPECIAL_SERVER);
                                    }
                                });
                            } else {
                                FriendHttpUtil.chatFromPerson(DoctorStudioActivity.this, doctor_id, expertName, picFeelOrderId, ObjectType.SPECIAL_SERVER);
                            }
                        } else {
                            SingleBtnFragmentDialog.showDefault(DoctorStudioActivity.this.getSupportFragmentManager(), "您无法对该医生发起体验咨询，此服务均享受一次，您已使用过该项服务，如需咨询，请购买相关服务");
                        }
                    } else {
                        DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), getResources().getString(R.string.studio_experience), "取消", "确定", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
                            @Override
                            public void onDismiss(DialogFragment fragment) {

                            }

                            @Override
                            public void onClick(DialogFragment fragment, View v) {
                                FriendHttpUtil.chatFromPerson(DoctorStudioActivity.this, doctor_id, expertName, picFeelOrderId, ObjectType.SPECIAL_SERVER);
                            }
                        });
                    }
                } else {
                    SingleBtnFragmentDialog.showDefault(DoctorStudioActivity.this.getSupportFragmentManager(), "当前体验咨询条数已经用完，如需继续咨询，请购买相关服务");
                }
                break;
            case R.id.rl_picandcul://图文
                if (tuWen) {
                    DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "您还有未使用完的条数，是否继续购买?", "取消", "确定", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
                        @Override
                        public void onDismiss(DialogFragment fragment) {

                        }

                        @Override
                        public void onClick(DialogFragment fragment, View v) {
                            Intent intent = new Intent(DoctorStudioActivity.this, PAtyConsultStudioGoPaying.class);
                            intent.putExtra("service_id", "5");
                            intent.putExtra("service_item_id", picServiceId);
                            intent.putExtra("price", picandculPrice+"");//价格
                            intent.putExtra("doctor_id", doctor_id);//医生ID
                            intent.putExtra("expertName", expertName);//医生名字
                            intent.putExtra("officeName", officeName);//医生科室
                            startActivity(intent);
                        }
                    });
                } else {
                    intent = new Intent(DoctorStudioActivity.this, PAtyConsultStudioGoPaying.class);
                    intent.putExtra("service_id", "5");
                    intent.putExtra("service_item_id", picServiceId);
                    intent.putExtra("price", picandculPrice+"");//价格
                    intent.putExtra("doctor_id", doctor_id);//医生ID
                    intent.putExtra("expertName", expertName);//医生名字
                    intent.putExtra("officeName", officeName);//医生科室
                    startActivity(intent);
                }
                break;
            case R.id.rl_picandcul_site://工作站图文咨询
                type = SERVICEFIVE;
                DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "您要发起一笔图文咨询吗?", "取消", "确定", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
                    @Override
                    public void onDismiss(DialogFragment fragment) {

                    }

                    @Override
                    public void onClick(DialogFragment fragment, View v) {
                        Intent intent = new Intent(DoctorStudioActivity.this, PAtyConsultStudioGoPaying.class);
                        intent.putExtra("service_id", ServiceType.TW);
                        intent.putExtra("service_item_id", "0");
                        intent.putExtra("price", picandculPrice+"");//价格
                        intent.putExtra("doctor_id", doctor_id);//医生ID
                        intent.putExtra("expertName", expertName);//医生名字
                        intent.putExtra("officeName", officeName);//医生科室
                        intent.putExtra(PAtyConsultStudioGoPaying.SERVICETYPEID, ServiceType.TW);
                        intent.putExtra(PAtyConsultStudioGoPaying.SITE_ID, site_id);
                        startActivity(intent);
                    }
                });
//
                break;
            case R.id.rl_phone_site://工作站电话咨询
                type = SERVICESIX;
                DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "您要发起一笔电话咨询吗?", "取消", "确定", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
                    @Override
                    public void onDismiss(DialogFragment fragment) {

                    }

                    @Override
                    public void onClick(DialogFragment fragment, View v) {
                        Intent intent = new Intent(DoctorStudioActivity.this, PAtyConsultStudioGoPaying.class);
                        intent.putExtra("service_id", ServiceType.DH);
                        intent.putExtra("service_item_id", "0");
                        intent.putExtra("price", phonePrice+"");//价格
                        intent.putExtra("doctor_id", doctor_id);//医生ID
                        intent.putExtra("expertName", expertName);//医生名字
                        intent.putExtra("officeName", officeName);//医生科室
                        intent.putExtra(PAtyConsultStudioGoPaying.SERVICETYPEID, ServiceType.DH);
                        intent.putExtra(PAtyConsultStudioGoPaying.SITE_ID, site_id);
                        startActivity(intent);
                    }
                });
//
                break;
            case R.id.rl_video_site://工作站视频咨询
                type = SERVICEEIGHT;
                DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "您要发起一笔视频咨询吗?", "取消", "确定", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
                    @Override
                    public void onDismiss(DialogFragment fragment) {

                    }

                    @Override
                    public void onClick(DialogFragment fragment, View v) {
                        Intent intent = new Intent(DoctorStudioActivity.this, PAtyConsultStudioGoPaying.class);
                        intent.putExtra("service_id", ServiceType.SP);
                        intent.putExtra("service_item_id", "0");
                        intent.putExtra("price", videoPrice+"");//价格
                        intent.putExtra("doctor_id", doctor_id);//医生ID
                        intent.putExtra("expertName", expertName);//医生名字
                        intent.putExtra("officeName", officeName);//医生科室
                        intent.putExtra(PAtyConsultStudioGoPaying.SERVICETYPEID, ServiceType.SP);
                        intent.putExtra(PAtyConsultStudioGoPaying.SITE_ID, site_id);
                        startActivity(intent);
                    }
                });
//
                break;
            case R.id.rl_phone://电话
                intent = new Intent(DoctorStudioActivity.this, PAtyConsultStudioGoPaying.class);
                intent.putExtra("service_id", "6");
                intent.putExtra("service_item_id", phoneServiceId);
                intent.putExtra("price", phonePrice+"");//价格
                intent.putExtra("doctor_id", doctor_id);//医生ID
                intent.putExtra("expertName", expertName);//医生名
                intent.putExtra("officeName", officeName);//医生科室
                if (isSited()) {
                    intent.putExtra(PAtyConsultStudioGoPaying.SERVICETYPEID, "6");
                    intent.putExtra(PAtyConsultStudioGoPaying.SITE_ID, site_id);
                }
                startActivity(intent);
                break;
            case R.id.rl_consul://包月
                if (baoyue) {
                    DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "您还有未使用完的条数，是否继续购买?", "取消", "确定", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
                        @Override
                        public void onDismiss(DialogFragment fragment) {

                        }

                        @Override
                        public void onClick(DialogFragment fragment, View v) {
                            Intent intent = new Intent(DoctorStudioActivity.this, PAtyConsultStudioGoPaying.class);
                            intent.putExtra("service_id", "7");
                            intent.putExtra("service_item_id", consulServiceId);
                            intent.putExtra("price", consulPrice+"");//价格
                            intent.putExtra("doctor_id", doctor_id);//医生ID
                            intent.putExtra("expertName", expertName);//医生名字
                            intent.putExtra("officeName", officeName);//医生科室
                            startActivity(intent);
                        }
                    });
                } else {
                    intent = new Intent(DoctorStudioActivity.this, PAtyConsultStudioGoPaying.class);
                    intent.putExtra("service_id", "7");
                    intent.putExtra("service_item_id", consulServiceId);
                    intent.putExtra("price", consulPrice+"");//价格
                    intent.putExtra("doctor_id", doctor_id);//医生ID
                    intent.putExtra("expertName", expertName);//医生名字
                    intent.putExtra("officeName", officeName);//医生科室
                    startActivity(intent);
                }


                break;
            case R.id.rl_video://视频
                intent = new Intent(DoctorStudioActivity.this, PAtyConsultStudioGoPaying.class);
                intent.putExtra("service_id", ServiceType.SP);
                intent.putExtra("service_item_id", videoServiceId);
                intent.putExtra("price", videoPrice+"");//价格
                intent.putExtra("doctor_id", doctor_id);//医生ID
                intent.putExtra("expertName", expertName);//医生名
                intent.putExtra("officeName", officeName);//医生科室
                if (isSited()) {
                    intent.putExtra(PAtyConsultStudioGoPaying.SERVICETYPEID, "8");
                    intent.putExtra(PAtyConsultStudioGoPaying.SITE_ID, site_id);
                }
                startActivity(intent);
                break;

            case R.id.rl_menzhen_site://门诊预约
            case R.id.rl_addnum://门诊预约
//                ToastUtil.showShort("去支付");
                intent = new Intent(DoctorStudioActivity.this, BuyServiceListFromPatientActivity.class);
                intent.putExtra("consultId", "");
                intent.putExtra(BuyServiceListFromPatientActivity.DOCTOR_NAME, expertName);
                intent.putExtra(BuyServiceListFromPatientActivity.DOCTOR_ID, doctor_id);//医生ID
                intent.putExtra("type", 3);
                startActivity(intent);
                break;
            case R.id.rl_online2://在线会诊
                intent = new Intent(DoctorStudioActivity.this, FlowMassageActivity.class);
                intent.putExtra("data", dsb);
                intent.putExtra("PROMTER", "10");
                intent.putExtra("OFFICECODE", office_id);//1024
                intent.putExtra("OFFICENAME", officeName);
                startActivity(intent);
                break;

//            case R.id.menzhen:
//                intent = new Intent(DoctorStudioActivity.this, BuyServiceListFromPatientActivity.class);
//                intent.putExtra("consultId", "");
//                intent.putExtra("titleName", expertName);
//                intent.putExtra("type", 3);
//                intent.putExtra("mCustomerInfoEntity", entity);
//                startActivity(intent);
//                break;
//            case R.id.rl_online2:
//                intent = new Intent(DoctorStudioActivity.this, FlowMassageActivity.class);
//                intent.putExtra("OFFICECODE", office_id);//1024
//                intent.putExtra("OFFICENAME", officeName);
//                startActivity(intent);
//                break;
//
//            switch (doctorType) {
//                case "expert":
//                    intent = new Intent(DoctorStudioActivity.this, FlowMassageActivity.class);
//                    intent.putExtra("SELECTA", "SELECTA");
//                    intent.putExtra("OFFICECODE", "1024");//officeCode
//                    intent.putExtra("OFFICENAME", officeName);
//                    startActivity(intent);
//                    break;
//                case "doctor":
//                    ToastUtil.showShort("基层医生");
//                    intent = new Intent(DoctorStudioActivity.this, PConsultMainActivity.class);
////                    intent.putExtra("SELECTA", "SELECTA");
////                    intent.putExtra("OFFICECODE", "1024");//officeCode
////                    intent.putExtra("OFFICENAME", officeName);
//                    startActivity(intent);
//                    break;
//                }
//                break;
            case R.id.iv_care:
                if (isCare == false) {
//                    isCare=true;
//                    care.setSelected(true);
                    addCare();
                } else {
//                    isCare=false;
//                    care.setSelected(false);
                    cancelCare();
                }
                break;
            case R.id.friendcircle://微信朋友圈
                //showShare(WechatMoments.NAME);
                sendWXMS();
                quitPopWindow();
                break;
            case R.id.wechat://微信好友
                //showShare(Wechat.NAME);
                sendWX();
                quitPopWindow();
                break;
//            case R.id.weibo://新浪微博分享
//                showShare(SinaWeibo.NAME);
//                quitPopWindow();
//                break;
//            case R.id.qqroom://qq空间
//                showShare(QZone.NAME);
//                quitPopWindow();
//                break;
            case R.id.btn_cancel:
                ToastUtil.showShort("取消");
                quitPopWindow();
                break;

        }
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
        sp.setImageData(getBitmapFromUri(uri));
        sp.setUrl("http://wx.61120.net/DuoMeiHealth/DO.action?Doctor_ID=" + doctor_id);   //网友点进链接后，可以看到分享的详情
        //  sp.setUrl(HTalkApplication.getHttpUrls().HTML + "/shopstroe.html?" + "doctor_id=" + doctor_id + "?customer_id=" + customer_id);   //网友点进链接后，可以看到分享的详情
        //3、非常重要：获取平台对象
        Platform wechat = ShareSDK.getPlatform(WechatMoments.NAME);
        wechat.setPlatformActionListener(DoctorStudioActivity.this); // 设置分享事件回调
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
        sp.setImageData(getBitmapFromUri(uri));
        // sp.setUrl(HTalkApplication.getHttpUrls().HTML + "/shopstroe.html?" + "doctor_id=" + doctor_id + "?customer_id=" + customer_id);   //网友点进链接后，可以看到分享的详情
        sp.setUrl("http://wx.61120.net/DuoMeiHealth/DO.action?Doctor_ID=" + doctor_id);   //网友点进链接后，可以看到分享的详情
        //3、非常重要：获取平台对象
        Platform wechatms = ShareSDK.getPlatform(Wechat.NAME);
        wechatms.setPlatformActionListener(DoctorStudioActivity.this); // 设置分享事件回调
        // 执行分享
        wechatms.share(sp);
    }

    /**
     * 第三方分享
     *
     * @param name
     */

    private void showShare(String name) {

        //2、设置分享内容
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(getString(R.string.string_share_title));
        sp.setText(getString(R.string.string_share_content));
//      sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
        sp.setImageData(getBitmapFromUri(uri));
        sp.setSiteUrl(HTalkApplication.getHttpUrls().HTML + "/shopstroe.html?" + "doctor_id=" + doctor_id + "?customer_id=" + customer_id);//doctor_id
        sp.setTitleUrl(HTalkApplication.getHttpUrls().HTML + "/shopstroe.html?" + "doctor_id=" + doctor_id + "?customer_id=" + customer_id);//网友点进链接后，可以看到分享的详情
        //3、非常重要：获取平台对象
        Platform sinaWeibo = ShareSDK.getPlatform(name);
        sinaWeibo.setPlatformActionListener(DoctorStudioActivity.this); // 设置分享事件回调
        // 执行分享
        sinaWeibo.share(sp);
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
                    WindowManager.LayoutParams params = DoctorStudioActivity.this.getWindow()
                            .getAttributes();
                    params.alpha = 1.0f;
                    DoctorStudioActivity.this.getWindow().setAttributes(params);
                }
            });
        } else if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            return;
        }

        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.alpha = 0.5f;
        this.getWindow().setAttributes(params);
        mPopupWindow.showAtLocation(DoctorStudioActivity.this.findViewById(R.id.ll_studio_main), Gravity.BOTTOM, 0, 0);

    }

    /**
     * 添加关注
     */
    private void addCare() {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", customer_id);
        map.put("doctor_id", doctor_id);
        HttpRestClient.OKHttpDoctorStudioCare(map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))) {
                        isCare = true;
                        care.setSelected(true);
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
     * 取消关心
     */
    private void cancelCare() {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", customer_id);
        map.put("doctor_id", doctor_id);
        HttpRestClient.OKHttpDoctorStudioCancelCare(map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))) {
                        isCare = false;
                        care.setSelected(false);
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
     * 获取图片Bitmap
     *
     * @param uri
     * @return
     */
    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 点击进入不同的工具箱
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(DoctorStudioActivity.this, CommonwealAidAty.class);
//        intent.putExtra(CommonwealAidAty.URL, "https://www.baidu.com/");
        intent.putExtra(CommonwealAidAty.URL, utilsList.get(position).optString("TOOL_URL"));
        intent.putExtra(CommonwealAidAty.TITLE, utilsList.get(position).optString("TOOL_NAME"));
        // intent.putExtra(CommonwealAidAty.TITLE,"工具箱");
        startActivity(intent);
    }

    /**
     * 是否在医生集团
     *
     * @return false 有集团
     */
    private boolean isSited() {
        return !HStringUtil.isEmptyAndZero(site_id);
    }

    private int type = 0;

//    /**
//     * 购买当前集团 图文咨询
//     *
//     * @param serviceType
//     */
//    private void sendService(int serviceType) {
//        Map<String, String> map = new HashMap<>();
//        map.put("service_customer_id", LoginServiceManeger.instance().getLoginEntity().getId());
//        map.put("service_type_id", serviceType + "");
//        map.put("site_id", site_id);
//        map.put("op", "addWorkSiteOrder");
//        HttpRestClient.OKHttpStationCommonUrl(map, new HResultCallback<String>(this) {
//            @Override
//            public void onResponse(String response) {
//                super.onResponse(response);
//                if (!HStringUtil.isEmpty(response)) {
//                    JSONObject obj = null;
//                    try {
//                        obj = new JSONObject(response);
//                        ToastUtil.showShort(obj.optString("message"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }, this);
//    }

}
