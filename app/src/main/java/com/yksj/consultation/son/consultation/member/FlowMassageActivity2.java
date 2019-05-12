package com.yksj.consultation.son.consultation.member;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dmsj.newask.http.LodingFragmentDialog;
import com.squareup.okhttp.Request;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.AppData;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.consultation.son.doctor.SearchExpertActivity;
import com.yksj.healthtalk.bean.DoctorSimpleBean;
import com.yksj.healthtalk.db.DictionaryHelper;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.JsonsfHttpResponseHandler;
import com.yksj.healthtalk.net.http.MyResultCallback;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.photo.utils.AlbumActivity;
import com.yksj.healthtalk.photo.utils.GalleryActivity;
import com.yksj.healthtalk.utils.Bimp;
import com.yksj.healthtalk.utils.BitmapUtils;
import com.yksj.healthtalk.utils.DensityUtils;
import com.yksj.healthtalk.utils.EditTextUtil;
import com.yksj.healthtalk.utils.FileUtils;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ImageItem;
import com.yksj.healthtalk.utils.LogUtil;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.ValidatorUtil;
import com.yksj.healthtalk.utils.WheelUtils;
import com.yksj.healthtalk.views.MessageImageView;
import com.yksj.healthtalk.views.MessageThumbnailImageView;

import org.apache.http.message.BasicNameValuePair;
import org.cropimage.CropUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;
import org.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 填写信息（提交申请）
 * Created by zheng on 2015/9/15.
 */
public class FlowMassageActivity2 extends BaseFragmentActivity implements View.OnClickListener {
    private LinearLayout selectedExLL, codell, selectCheck;//所选专家布局，验证码布局，所选医生布局，选医生按钮
    private TextView selectedName, selectedDuty, selectedHospital, selectedSpeciality, CURRENTVIEW, suozaidi, pSexEdit, pAgeEdit, selectedAName, selectedADuty, selectedAHospital, selectedASpeciality;
    private ImageView selectedHeader, selectedAHeader;
    private EditText pNameEdit, pPhoneEdit, pCodeEdit;
    private EditText pIllness;
    private EditText etMainIllness;
    private EditText allergy;//过敏史
    private Button codeBtn, imgAdd, vdoAdd;
    private Runnable runnable;
    private boolean Sendcode = false, isApplying = false;//验证码是否发送 true为发送
    Handler handler = new Handler();
    private View lineView;
    private ImageLoader mInstance;
    private DisplayImageOptions mOptions;
    private View wheelView, topView, bottomView;
    private PopupWindow pop;
    private List<Map<String, String>> proList = null;
    private Map<String, List<Map<String, String>>> cityMap = new LinkedHashMap<String, List<Map<String, String>>>();
    private View mainView;
    private String locationCode = "";//所在地编码
    private ArrayList<ImageItem> results;

    private JSONObject lastMessage;
    private DoctorSimpleBean dsb, resultDsb;
    private PopupWindow addPhotoPop;
    private File storageFile = null;
    private Intent intent;
    private static final int TAKE_PICTURE = 0x000001;
    private CheckBox addDoctor;
    private String pNameText, pAgeText, pSexText, pPhoneText, pCodeText, dWellingText, pILLnessText, mainIllness, allergyText;//DWELLING
    private String promter, TYPE = null;
    private Bundle bundle;
    private String conId = null;
    private TextView hintText;
    private String customerid = "";
    private JSONArray array = new JSONArray();//删除图片
    private boolean clicked = false;
    private LodingFragmentDialog mLoadDialog;
    private DictionaryHelper dictionaryHelper;
    private String beforePhone = "";
    private String isVisitorStr;
    private String officeName;
    private String main_illness;//主诉


    private static final int RECODE_FLAG = 1000;//录制视频
    private static final int LOCAL_RECODE_FLAG = 1001;//本地视频
    public static final String IMAGEKEY = FlowMassageActivity2.class.getSimpleName() + "image";//图片标志
    public static final String VIDEOKEY = FlowMassageActivity2.class.getSimpleName() + "video";//视频标志
    private LinearLayout images;//图片线性布局
    private LinearLayout videos;//视频线性布局

    ArrayList<ImageItem> imagesList;//图片list类
    ArrayList<ImageItem> videoList;//视频list类

    ArrayList<JSONObject> videoDataList = new ArrayList<>();//视频数据list类
    ArrayList<JSONObject> thumbnailDataList = new ArrayList<>();//视频缩略图数据list类

    private int lastSEQ = 0;//图片顺序下标

    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_person_message2);
        if (!LoginServiceManeger.instance().isVisitor) {//游客判断
            customerid = LoginServiceManeger.instance().getLoginEntity().getId();
        }
        initView();
    }

    private void initView() {
        initTitle();
        titleTextV.setText("填写信息");
        titleLeftBtn.setOnClickListener(this);
        mInstance = ImageLoader.getInstance();
        findViewById(R.id.tijiao_btn).setOnClickListener(this);
        selectedExLL = ((LinearLayout) findViewById(R.id.seleted_person_layout));
        codell = ((LinearLayout) findViewById(R.id.layout_4));
        hintText = (TextView) findViewById(R.id.hint_text);
        lineView = findViewById(R.id.line_doc);
        selectedHeader = ((ImageView) findViewById(R.id.seleted_person));
        selectedName = ((TextView) findViewById(R.id.seleted_person_name));
        selectedDuty = ((TextView) findViewById(R.id.seleted_person_duty));
        selectedHospital = ((TextView) findViewById(R.id.seleted_person_hospital));
        selectedSpeciality = ((TextView) findViewById(R.id.seleted_person_speciality));
        pNameEdit = ((EditText) findViewById(R.id.full_name_edit));
        pSexEdit = ((TextView) findViewById(R.id.full_sex_edit));
        pAgeEdit = ((TextView) findViewById(R.id.full_age_edit));
        pPhoneEdit = ((EditText) findViewById(R.id.full_phone_edit));
        pCodeEdit = ((EditText) findViewById(R.id.full_code_edit));
        pIllness = ((EditText) findViewById(R.id.illness_state));
        etMainIllness = ((EditText) findViewById(R.id.et_main));
        allergy = (EditText) findViewById(R.id.allergy_state);
        addDoctor = ((CheckBox) findViewById(R.id.add_doctor));
        codeBtn = ((Button) findViewById(R.id.btn_code));
        selectCheck = ((LinearLayout) findViewById(R.id.select_check));
        selectedAHeader = ((ImageView) findViewById(R.id.seleted_ac));
        selectedAName = ((TextView) findViewById(R.id.seleted_ac_name));
        selectedADuty = ((TextView) findViewById(R.id.seleted_ac_duty));
        selectedAHospital = ((TextView) findViewById(R.id.seleted_ac_hospital));
        selectedASpeciality = ((TextView) findViewById(R.id.seleted_ac_speciality));
        topView = findViewById(R.id.select_topline);
        bottomView = findViewById(R.id.select_bottomline);

        View checklist = getLayoutInflater().inflate(R.layout.case_videos_list, null);
        listpop = new PopupWindow(checklist, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        checklist.findViewById(R.id.luzhi).setOnClickListener(this);
        checklist.findViewById(R.id.bendifenjian).setOnClickListener(this);
        checklist.findViewById(R.id.quxiao).setOnClickListener(this);

        dictionaryHelper = DictionaryHelper.getInstance(FlowMassageActivity2.this);
        wheelView = getLayoutInflater().inflate(R.layout.wheel, null);
        wheelView.findViewById(R.id.wheel_cancel).setOnClickListener(this);
        wheelView.findViewById(R.id.wheel_sure).setOnClickListener(this);
        pop = new PopupWindow(wheelView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mainView = getLayoutInflater().inflate(R.layout.full_person_message, null);

        findViewById(R.id.location_action1).setOnClickListener(this);
        suozaidi = (TextView) findViewById(R.id.location1);
        imgAdd = (Button) findViewById(R.id.item_img_add);
        imgAdd.setOnClickListener(this);
        vdoAdd = (Button) findViewById(R.id.item_vdo_add);
        vdoAdd.setOnClickListener(this);

        images = (LinearLayout) findViewById(R.id.item_images);
        videos = (LinearLayout) findViewById(R.id.item_videos);
        imagesList = new ArrayList<ImageItem>();
        videoList = new ArrayList<ImageItem>();
        Bimp.dataMap.put(IMAGEKEY, imagesList);
        Bimp.dataMap.put(VIDEOKEY, videoList);
        Bimp.imgMaxs.put(IMAGEKEY, 12);

        bundle = getIntent().getExtras();
        TYPE = bundle.getString("TYPE");
        if (TYPE != null) {//修改申请单类型
            titleTextV.setText("修改");
            conId = bundle.getString("CONSULTATION_ID");
            beforePhone = bundle.getString("PATIENTTEL_PHONE");
            locationCode = bundle.getString("AREA_CODE");
            officeName = bundle.getString("OFFICENAME");
            main_illness=bundle.getString("MAINCASE");
            upData();//修改前数据
        } else {
//            initLast();//上一次会诊
        }
        queryData();//获取地区信息
        selectedDoc();//所选的专家

        if (getIntent().hasExtra("SELECTA")) {//帮我选专家
            selectCheck.setVisibility(View.GONE);
            hintText.setVisibility(View.VISIBLE);//最上部提示语
        }
        if (getIntent().hasExtra("OFFICENAME")) {
            officeName = getIntent().getStringExtra("OFFICENAME");
        }

        codeBtn.setOnClickListener(this);//获取验证码监听
        pSexEdit.setOnClickListener(this);//性别监听
        pAgeEdit.setOnClickListener(this);//年龄监听
        addDoctor.setOnClickListener(this);//添加医生监听
        EditTextUtil.setEditTextInhibitInputSpeChat(pNameEdit, this);

    }

    //修改会诊单以前数据
    private void upData() {
        pIllness.setText(bundle.getString("CONSULTATION_DESC"));
        etMainIllness.setText(bundle.getString("MEDICAL_NAME"));
        allergy.setText(bundle.getString("ALLERGY"));
        pPhoneEdit.setText(bundle.getString("PATIENTTEL_PHONE"));
        pAgeEdit.setText(bundle.getString("PATIENT_AGE"));
        pSexEdit.setText(bundle.getString("PATIENT_SEX"));
        pNameEdit.setText(bundle.getString("PATIENT_NAME"));
        suozaidi.setText(bundle.getString("DWELLING_PLACE"));
        locationCode = bundle.getString("AREA_CODE");
        LogUtil.v("TAG", "修改会诊原地区编码" + locationCode);
        if (TextUtils.isEmpty(pPhoneEdit.getText().toString().trim())) {
            codell.setVisibility(View.VISIBLE);
        } else {
            pPhoneEdit.addTextChangedListener(new TextWatcher() {


                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (beforePhone.equals(pPhoneEdit.getText().toString().trim())) {
                        codell.setVisibility(View.GONE);
                    } else {
                        codell.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        pastConImage();
    }

    /**
     * 所选的专家
     */
    private void selectedDoc() {
        if (getIntent().hasExtra("data")) {
            if (getIntent().getSerializableExtra("data") != null) {
                selectedExLL.setVisibility(View.VISIBLE);
                lineView.setVisibility(View.VISIBLE);
                mInstance = ImageLoader.getInstance();
                mOptions = DefaultConfigurationFactory.createHeadDisplayImageOptions(FlowMassageActivity2.this);
                dsb = (DoctorSimpleBean) getIntent().getSerializableExtra("data");
                mInstance.displayImage(dsb.ICON_DOCTOR_PICTURE, selectedHeader, mOptions);
                if (!"null".equals(dsb.DOCTOR_REAL_NAME)) {
                    selectedName.setText(dsb.DOCTOR_REAL_NAME);
                }
                if (!"null".equals(dsb.TITLE_NAME)) {
                    selectedDuty.setText(dsb.TITLE_NAME);
                }

                if (!HStringUtil.isEmpty(dsb.UNIT_NAME)) {
                    selectedHospital.setText(dsb.UNIT_NAME);
                } else {
                    selectedHospital.setVisibility(View.GONE);
                }


                if (!"null".equals(dsb.DOCTOR_SPECIALLY)) {
                    selectedSpeciality.setText("擅长:" + dsb.DOCTOR_SPECIALLY);
                }
            }
        }
    }

    private PopupWindow listpop;//相册

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back://返回
                onBackPressed();
                break;
            case R.id.tijiao_btn://提交
                if (isApplying == false) {
                    nullError();
                }
                break;
            case R.id.btn_code://发送验证码
                getAuthCode();
                break;
            case R.id.wheel_cancel://滑轮取消
                if (pop != null)
                    pop.dismiss();
                break;
            case R.id.wheel_sure://滑轮确定
                if (pop != null)
                    pop.dismiss();
                if (WheelUtils.getCurrent() != null) {
                    setText();
                }
                break;
            case R.id.location_action1://位置选择
                closeKeyBoard();
                CURRENTVIEW = suozaidi;
                setCity();
                break;
            case R.id.item_img_add://添加图片
                showuploadPopWindow();
                break;
            case R.id.item_vdo_add://添加视频
                WheelUtils.setPopeWindow(FlowMassageActivity2.this, view,
                        listpop);
                break;
            case R.id.cameraadd://相机
                if (addPhotoPop.isShowing()) {
                    addPhotoPop.dismiss();
                }
                photo();
                break;
            case R.id.videoadd://弹出框添加视频选项

                break;
            case R.id.cancel://取消
                if (addPhotoPop != null && addPhotoPop.isShowing()) {
                    addPhotoPop.dismiss();
                }
                break;
            case R.id.galleryadd://从相册获取
                if (addPhotoPop.isShowing()) {
                    addPhotoPop.dismiss();
                }
                intent = new Intent(FlowMassageActivity2.this, AlbumActivity.class);
                intent.putExtra("key", IMAGEKEY);
                startActivityForResult(intent, 100);
                break;
            case R.id.add_doctor://添加医生
                if (clicked) {
                    resultDsb = null;
                    clicked = false;
                } else {
                    Intent intent = new Intent(FlowMassageActivity2.this, SearchExpertActivity.class);
                    intent.putExtra("type", 1);
                    intent.putExtra("Tip", "");
                    startActivityForResult(intent, 201);
                }
                break;
            case R.id.full_sex_edit://性别选择
                closeKeyBoard();
                CURRENTVIEW = pSexEdit;
                setXingbie();
                break;
            case R.id.full_age_edit://填写年龄
                closeKeyBoard();
                CURRENTVIEW = pAgeEdit;
                setAge();
                break;

            case R.id.luzhi: // 录制
                listpop.dismiss();
                startActivityForResult(new Intent(this, RecordMadeAty.class), RECODE_FLAG);
                break;

            case R.id.bendifenjian: // 本地文件
                listpop.dismiss();
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, LOCAL_RECODE_FLAG);
                break;
            case R.id.quxiao:
                listpop.dismiss();
                break;
        }
    }

    /**
     * 发送申请
     */
    private void sendData() {
        JSONObject json = new JSONObject();
        try {
            if (TYPE == null) {//修改申请
                if (getIntent().hasExtra("OFFICECODE")) {
                    json.put("OFFICEID", getIntent().getStringExtra("OFFICECODE"));//科室id
                } else {
                    json.put("OFFICEID", " ");
                }
//              json.put("CONSULTATION_NAME", pNameText + "的会诊");//会诊名称
                json.put("CONSULTATION_CENTER_ID", HTalkApplication.APP_CONSULTATION_CENTERID);
                json.put("DWELLING_PLACE", dWellingText);//所在地名称
                if (getIntent().hasExtra("data")) {
                    if (getIntent().getSerializableExtra("data") != null) {
                        json.put("EXPERT_ID", dsb.CUSTOMER_ID + "");//专家id
                        json.put("SERVICE_PRICE", dsb.SERVICE_PRICE + "");//价格
                    } else {
                        json.put("EXPERT_ID", "");//专家id
                        /*****************************************************meiyou***************************/
                        json.put("SERVICE_PRICE", "");//价格
                    }
                } else {
                    json.put("EXPERT_ID", "");//专家id
                    json.put("SERVICE_PRICE", "");//价格
                }
                if (resultDsb != null && addDoctor.isChecked()) {
                    json.put("DOCTORID", resultDsb.CUSTOMER_ID + "");//基层医生id
                } else {
                    json.put("DOCTORID", "");
                }
                /**
                 * 发起人类型（会诊单来源）
                 10-患者指定专家，医生为系统分配，
                 --20-系统自动分配医生，医生指定专家
                 --30-患者自己指定医生，医生指定专家
                 --40-医生发起，列表选择患者，医生指定专家
                 --50-医生发起，扫码指定患者，医生指定专家
                 */
                //发起人类型
                if (getIntent().hasExtra("PROMTER")) {
                    promter = getIntent().getStringExtra("PROMTER");
                } else {
                    if (resultDsb != null) {
                        promter = "30";
                    } else {
                        promter = "20";
                    }
                }
                switch (promter) {
                    case "10":
                        json.put("PROMOTER_TYPE", "10");
                        break;
                    case "20":
                        json.put("PROMOTER_TYPE", "20");
                        break;
                    case "30":
                        json.put("PROMOTER_TYPE", "30");
                        break;
                    case "40":
                        json.put("PROMOTER_TYPE", "40");
                        break;
                    case "50":
                        json.put("PROMOTER_TYPE", "50");
                        break;
                }
//                if (LoginServiceManeger.instance().isVisitor) {//游客判断
//                    json.put("LOGINMK",0);
//                }else if (!LoginServiceManeger.instance().isVisitor){
//                    json.put("LOGINMK",1);
//                }
            }
            if (TYPE != null) {//删除图片
                results = HTalkApplication.getHTalkApplication().getmImages();
                for (int i = 0; i < results.size(); i++) {
                    ImageItem imageItem = results.get(i);
                    JSONObject object = new JSONObject();
                    object.put("PICTUREID", imageItem.pidId + "");
                    object.put("SMALLPIC", imageItem.getThumbnailPath());
                    object.put("BIG_PICTURE", imageItem.getImagePath());
                    array.put(object);
                }
                json.put("PICLIST", array);
            }
            json.put("BEFORE_PATIENTTEL_PHONE", beforePhone);
            if (HStringUtil.isEmpty(officeName)) {
                json.put("CONSULTATION_NAME", pNameText + "的会诊");//会诊名称
            } else {
                json.put("CONSULTATION_NAME", pNameText + officeName + "的会诊");//会诊名称
            }
            json.put("CONSULTATION_ID", conId);//会诊id
            json.put("CONSULTATION_DESC", pILLnessText);//会诊描述
            json.put("ALLERGY", allergyText);//过敏史描述
            json.put("MAINILLNESS", mainIllness);//主诉
            json.put("PATIENTID", customerid);//患者id
            json.put("BEFORE_PATIENTTEL_PHONE", pPhoneText);//患者手机
            if (locationCode == null | "".equals(locationCode) | "null".equals(locationCode)) {
                ToastUtil.showToastPanl("请填写患者位置");
                return;
            } else {
                json.put("AREA_CODE", locationCode);//所在地编码
            }
            json.put("PATIENT_NAME", pNameText);//患者姓名
            if ("男".equals(pSexText)) {
                json.put("PATIENT_SEX", "M");//患者性别
            } else if ("女".equals(pSexText)) {
                json.put("PATIENT_SEX", "W");//患者性别
            }
            json.put("PATIENT_AGE", pAgeText);//患者年龄
            json.put("VERIFICATION_CODE", pCodeText);//验证码
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("PARAMETER", json.toString());
        putFile(params);
        //修改会诊单
        if (TYPE != null) {
            HttpRestClient.doHttpCaseUpdateConsultationById(params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String content) {
                    super.onSuccess(content);
                    try {
                        if (content != null) {
                            JSONObject object = new JSONObject(content);
                            LogUtil.d("TAG", content.toString());
                            ToastUtil.showToastPanl(object.optString("message"));
                            if ("1".equals(object.optString("code"))) {
                                finish();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStart() {
                    isApplying = true;
                    FlowMassageActivity2.this.mLoadDialog = LodingFragmentDialog.showLodingDialog(getSupportFragmentManager(), getResources());
                    FlowMassageActivity2.this.mLoadDialog.setCancelable(false);
                    super.onStart();
                }

                @Override
                public void onFinish() {
                    isApplying = false;
                    FlowMassageActivity2.this.mLoadDialog.dismiss();
                    super.onFinish();
                }

                @Override
                public void onFailure(Throwable error) {
                    isApplying = false;
                    FlowMassageActivity2.this.mLoadDialog.dismiss();
                    super.onFailure(error);
                }
            });
        } else {//流程申请
            HttpRestClient.doHttpSaveApplyConsult(params, new AsyncHttpResponseHandler(this) {
                @Override
                public void onSuccess(String content) {
                    super.onSuccess(content);
                    try {
                        if (content != null) {
                            JSONObject object = new JSONObject(content);
                            LogUtil.d("TAG", content.toString());
                            ToastUtil.showToastPanl(object.optString("message"));
                            if ("1".equals(object.optString("code"))) {
                                finish();
//                                final JSONObject result = object.optJSONObject("result");
//                                String username = result.getString("username");
//                                String password = result.getString("password");
//                                if (LoginServiceManeger.instance().isVisitor && result != null && result.length() > 0) {
//                                    LoginServiceManeger.instance().login(username, password, "", "", "0");
//                                }
//                                SingleBtnFragmentDialog.show(getSupportFragmentManager(), "六一健康", object.optString("message"), "查看订单", new SingleBtnFragmentDialog.OnClickSureBtnListener() {
//                                    @Override
//                                    public void onClickSureHander() {
//                                        Intent intent = new Intent(FlowMassageActivity2.this, AtyOrdersDetails.class);
////                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                        String conId = result.optString("CONSULTATION_ID");
//                                        intent.putExtra("CONID", Integer.parseInt(conId));
//                                        intent.putExtra("BACK", 2);
//                                        startActivity(intent);
//                                    }
//                                });
////                                isApplying=true;
//                            } else if ("0".equals(object.optString("code"))) {
//                                String string = object.optString("message");
//                                if (string.contains("验证码")) {
//                                    ToastUtil.showToastPanl(object.optString("message"));
//                                    isApplying = false;
//                                } else {
//                                    ToastUtil.showToastPanl(object.optString("message"));
//                                    isApplying = false;
//                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStart() {
                    isApplying = true;
                    FlowMassageActivity2.this.mLoadDialog = LodingFragmentDialog.showLodingDialog(getSupportFragmentManager(), getResources());
                    FlowMassageActivity2.this.mLoadDialog.setCancelable(false);
//                  FlowMassageActivity.this.mLoadDialog.setCanceledOnTouchOutside(false);
                    super.onStart();
                }

                @Override
                public void onFinish() {
                    FlowMassageActivity2.this.mLoadDialog.dismiss();
                    isApplying = false;
                    super.onFinish();
                }

                @Override
                public void onFailure(Throwable error) {
                    isApplying = false;
                    FlowMassageActivity2.this.mLoadDialog.dismiss();
                    super.onFailure(error);
                }
            });
        }

    }

    /**
     * 获取填写数据
     */
    private void getContetStr() {//pNameText,pAgeText,pSexText,pPhoneText,pCodeText,dwelling,pILLnessText;
        pNameText = pNameEdit.getText().toString().trim();
        pAgeText = pAgeEdit.getText().toString().trim();
        pSexText = pSexEdit.getText().toString().trim();
        pILLnessText = pIllness.getText().toString().trim();
        mainIllness = etMainIllness.getText().toString().trim();
        allergyText = allergy.getText().toString().trim();
        pPhoneText = pPhoneEdit.getText().toString().trim();
        pCodeText = pCodeEdit.getText().toString().trim();
        dWellingText = suozaidi.getText().toString().trim();
    }

    /**
     * 填写错误或者没填或者字数限制
     */
    private void nullError() {
        getContetStr();
        if (TextUtils.isEmpty(pNameText)) {
            ToastUtil.showToastPanl("请填写姓名");
            return;
        }
//        if (!ValidatorUtil.nameRegular(pNameText)) {
//            ToastUtil.showToastPanl("姓名不可输入非法字符");
//            return;
//        }
        if (pNameText.trim().length() > 20) {
            ToastUtil.showToastPanl("姓名1~20个字");
        }
        if (TextUtils.isEmpty(pAgeText)) {
            ToastUtil.showToastPanl("请选择年龄");
            return;
        }
        if (TextUtils.isEmpty(pSexText)) {
            ToastUtil.showToastPanl("请选择性别");
            return;
        }
        if (TextUtils.isEmpty(pPhoneText)) {
            ToastUtil.showToastPanl("请填写手机号");
            return;
        }
        if (!ValidatorUtil.checkMobile(pPhoneText)) {
            ToastUtil.showToastPanl("手机号码有误");
            return;
        }
        if (View.VISIBLE == codell.getVisibility()) {
            if (TextUtils.isEmpty(pCodeText)) {
                ToastUtil.showToastPanl("请填写验证码");
                return;
            }
        }
        if (TextUtils.isEmpty(dWellingText)) {
            ToastUtil.showToastPanl("请填写患者位置");
            return;
        }
        if (TextUtils.isEmpty(mainIllness)) {
            ToastUtil.showToastPanl("请填写主诉");
            return;
        }
        if (TextUtils.isEmpty(pILLnessText)) {
            ToastUtil.showToastPanl("请填写病情描述");
            return;
        }
//        if (TextUtils.isEmpty(allergyText)) {
//            ToastUtil.showToastPanl("请填写过敏史描述");
//            return;
//        }
        sendData();
    }

    /**
     * 获取验证码
     */
    private void getAuthCode() {
        if (!SystemUtils.isNetWorkValid(this)) {
            ToastUtil.showShort(this, R.string.getway_error_note);
            return;
        }
        String phone = pPhoneEdit.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToastPanl("请填写手机号码");
            return;
        }
        if (!ValidatorUtil.checkMobile(phone)) {
            ToastUtil.showToastPanl("手机号码有误");
            return;
        }
        if (ValidatorUtil.checkMobile(phone)) {
            if (TYPE != null) {//修改申请单
                HttpRestClient.doHttpConsultationInfoSet(phone, new JsonsfHttpResponseHandler(this) {
                    @Override
                    public void onSuccess(int statusCode, com.alibaba.fastjson.JSONObject object) {
                        super.onSuccess(statusCode, object);
                        LogUtil.d("TAG", "修改会诊单验证吗" + object.toString());
                        if ("0".equals(object.getString("code"))) {
                            SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), object.getString("message"));
                        } else {
                            Sendcode = true;
                            timerTaskC();
                            ToastUtil.showShort(FlowMassageActivity2.this, object.getString("message"));
                        }
                    }
                });
            } else {//申请单
                if (LoginServiceManeger.instance().isVisitor) {//游客判断
                    isVisitorStr = "0";
                } else if (!LoginServiceManeger.instance().isVisitor) {
                    isVisitorStr = "1";
                }
                LogUtil.d("TAG", "游客生情" + isVisitorStr);
                HttpRestClient.doHttpSendApplyConsuCode(isVisitorStr, customerid, phone, new JsonsfHttpResponseHandler(this) {
                    @Override
                    public void onSuccess(int statusCode, com.alibaba.fastjson.JSONObject object) {
                        super.onSuccess(statusCode, object);
                        LogUtil.d("TAG", "会诊单验证" + object.toString());
                        if ("0".equals(object.getString("code"))) {
                            SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), object.getString("message"));
                        } else {
                            Sendcode = true;
                            timerTaskC();
                            ToastUtil.showShort(FlowMassageActivity2.this, object.getString("message"));
                        }
                    }
                });
            }
        } else {
            SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), getString(R.string.phone_toastSpecialcharter));
        }
    }

    /**
     * 设置六十秒
     */
    private void timerTaskC() {
        runnable = new Runnable() {
            int i = 60;

            @Override
            public void run() {
                if (i == 0) {
                    codeBtn.setText("发送验证码");
                    codeBtn.setEnabled(true);
                    Sendcode = false;
                    return;
                } else {
                    --i;
                    handler.postDelayed(this, 1000);
                    codeBtn.setText(i + "");
                    codeBtn.setEnabled(false);
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    /**
     * 所在地
     */
    private void setCity() {
        if (proList == null || cityMap == null) {
        } else {
            WheelUtils.setDoubleWheel(this, proList, cityMap, mainView, pop,
                    wheelView);
        }
    }

    /**
     * 获取地区
     */
    private void queryData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                proList = DictionaryHelper.getInstance(FlowMassageActivity2.this).setCityData(
                        FlowMassageActivity2.this, cityMap);
            }
        }).start();
    }

    /**
     * 设置内容
     */
    public void setText() {
        if (CURRENTVIEW.equals(suozaidi)) {
            suozaidi.setText(WheelUtils.getCurrent());
        } else if (CURRENTVIEW.equals(pSexEdit)) {
            pSexEdit.setText(WheelUtils.getCurrent());
        } else if (CURRENTVIEW.equals(pAgeEdit)) {
            pAgeEdit.setText(WheelUtils.getCurrent());
        }
        locationCode = WheelUtils.getCode();
    }

    /**
     * 上次申请成功数据
     */
    private void initLast() {
        HttpRestClient.doFindbeforeConsuPatientInfo(customerid, new MyResultCallback<JSONObject>(this) {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(JSONObject response) {
                super.onResponse(response);
                LogUtil.d("TAG", "上一次的会诊信息" + response.toString());
                try {
                    if (response != null) {
                        if ("1".equals(response.optString("code"))) {
                            lastMessage = response.optJSONObject("result");
                        } else if ("2".equals(response.optString("code"))) {
                            ToastUtil.showToastPanl(response.optString("message"));
                        } else {
//                            ToastUtil.showToastPanl(response.toString());
                        }
                    } else {
                        ToastUtil.showToastPanl("网络错误");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                onBandData();
            }
        }, this);
    }

    /**
     * 绑定数据
     */
    private void onBandData() {
        try {
            String realName = lastMessage.optString("REAL_NAME");
            String patientSex = lastMessage.optString("PATIENT_SEX");
            String patientTelphone = lastMessage.optString("PATIENT_TELPHONE");
            String patientAge = lastMessage.optString("AGE");
            String consultationDesc = lastMessage.optString("CONSULTATION_DESC");
            String allergyDesc = lastMessage.optString("ALLERGY");
            String areaCode = lastMessage.optString("AREA_CODE");
            locationCode = areaCode;
            beforePhone = lastMessage.optString("PATIENT_TELPHONE");
            if (!"null".equals(areaCode)) {
                String areaName = dictionaryHelper.queryAddressNew(FlowMassageActivity2.this, areaCode);
                suozaidi.setText(areaName);
            }
            if (!"null".equals(realName)) {
                pNameEdit.setText(realName);
            }
            if (!"null".equals(patientSex)) {
                if ("M".equals(patientSex)) {
                    pSexEdit.setText("男");
                } else if ("W".equals(patientSex)) {
                    pSexEdit.setText("女");
                } else {
                    pSexEdit.setText("");
                }
            }
            if (!"null".equals(patientTelphone)) {
                pPhoneEdit.setText(patientTelphone);
            }

            if (!"null".equals(patientAge)) {
                pAgeEdit.setText(patientAge);
            }
            if (!"null".equals(consultationDesc)) {
                pIllness.setText(consultationDesc);
            }
            if (!"null".equals(allergyDesc)) {
                allergy.setText(allergyDesc);
            }

            if (TextUtils.isEmpty(pPhoneEdit.getText().toString().trim())) {
                codell.setVisibility(View.VISIBLE);
            } else {
                pPhoneEdit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (beforePhone.equals(pPhoneEdit.getText().toString().trim())) {
                            codell.setVisibility(View.GONE);
                        } else {
                            codell.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将文件放入参数
     *
     * @param params
     */
    private void putFile(RequestParams params) {
        int videoCount = videoList.size();
        int imageCount = imagesList.size();
        params.putNullFile("file", new File(""));
        //上传图片文件
        for (int i = 0; i < imageCount; i++) {
            int index = i;
            try {
                if (imagesList.get(i).isNetPic) {

                } else {
                    params.put(index + ".jpg", BitmapUtils.onGetDecodeFileByPath(FlowMassageActivity2.this, imagesList.get(i).getImagePath()));
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        //上传视频文件
        for (int j = 0; j < videoCount; j++) {
            int index2 = imageCount + j;
            try {
                if (videoList.get(j).isNetPic) {

                } else {
                    params.put(index2 + ".mp4", new File(videoList.get(j).getImagePath()));
                    params.put("[thumbnail]" + (index2 + videoCount) + ".png", FileUtils.saveChatPhotoBitmapToFile(PlayVideoActiviy.getVideoThumbnail(videoList.get(j).getImagePath())));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 弹出上传图片的选择布局
     */
    public void showuploadPopWindow() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.interest_image_and_video_add_action, null);
        View mainView = inflater.inflate(R.layout.interest_content, null);
        if (addPhotoPop == null) {
            addPhotoPop = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            addPhotoPop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        WheelUtils.setPopeWindow(this, mainView, addPhotoPop);
        view.findViewById(R.id.cameraadd).setOnClickListener(this);
        view.findViewById(R.id.galleryadd).setOnClickListener(this);
        view.findViewById(R.id.videoadd).setVisibility(View.GONE);
        view.findViewById(R.id.videoadd).setOnClickListener(this);
        view.findViewById(R.id.cancel).setOnClickListener(this);
    }

    /**
     * 相机
     */
    public void photo() {
        storageFile = null;
        if (StorageUtils.isSDMounted()) {
            try {
                storageFile = StorageUtils.createCameraFile();
                Uri uri = Uri.fromFile(storageFile);
                Intent intent = CropUtils.createPickForCameraIntent(uri);
                startActivityForResult(intent, TAKE_PICTURE);
            } catch (Exception e) {
                ToastUtil.showLong(this, R.string.msg_camera_bug);
            }
        } else {
            SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "sdcard未加载");
        }
    }

    /**
     * 显示图片
     */
    private void showPhoto() {
        images.removeAllViews();
        if (imagesList.size() > 0) {//
            images.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        } else//将加号放在中间
            images.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0f));
        if (imagesList.size() == Bimp.imgMaxs.get(IMAGEKEY)) {
            findViewById(R.id.item_img_add).setVisibility(View.GONE);
        } else
            findViewById(R.id.item_img_add).setVisibility(View.VISIBLE);
        for (int i = 0; i < imagesList.size(); i++) {
            final int index = i;
            MessageImageView image = new MessageImageView(FlowMassageActivity2.this);
            image.setLayoutParams(new ViewGroup.LayoutParams(DensityUtils.dip2px(this, 78), DensityUtils.dip2px(this, 78)));
//			image.setLayoutParams(new LayoutParams(getScreenWidth(this),getScreenWidth(this)));
            image.setPadding(10, 0, 10, 0);
            if (imagesList.get(i).isNetPic) {
                mInstance.displayImage(imagesList.get(i).getThumbnailPath(), image.getImage());
            } else {
                image.setImageBitmap(imagesList.get(i).getBitmap());
            }
            image.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FlowMassageActivity2.this, GalleryActivity.class);
                    intent.putExtra("key", IMAGEKEY);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", index);
                    startActivityForResult(intent, 100);
                }
            });
            image.setDeleteListener(new View.OnClickListener() {//删除图片

                @Override
                public void onClick(View v) {
                    ImageItem imageItem = imagesList.get(index);
                    try {
                        JSONObject object = new JSONObject();
                        object.put("PICTUREID", imageItem.pidId + "");
                        object.put("SMALLPIC", imageItem.getThumbnailPath());
                        object.put("BIG_PICTURE", imageItem.getImagePath());
                        array.put(object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Bimp.dataMap.get(IMAGEKEY).remove(index);
                    showPhoto();
                }
            });
            images.addView(image);
        }
    }

    /**
     * 显示视频
     */
    private void showVideo() {
        videos.removeAllViews();
        if (videoList.size() > 0) {
            videos.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        } else//将加号放在中间
            videos.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0f));
        if (videoList.size() == 3) {
            findViewById(R.id.item_vdo_add).setVisibility(View.GONE);
        } else
            findViewById(R.id.item_vdo_add).setVisibility(View.VISIBLE);
        for (int i = 0; i < videoList.size(); i++) {
            final int index = i;
            MessageThumbnailImageView image = new MessageThumbnailImageView(FlowMassageActivity2.this);
            image.setLayoutParams(new ViewGroup.LayoutParams(DensityUtils.dip2px(this, 78), DensityUtils.dip2px(this, 78)));
//			image.setLayoutParams(new LayoutParams(getScreenWidth(this),getScreenWidth(this)));
            image.setPadding(10, 0, 10, 0);
            if (videoList.get(i).isNetPic) {
                String thumbnail = videoList.get(i).get_thumbnailPath();
                if (!HStringUtil.isEmpty(thumbnail)) {
                    mInstance.displayImage(videoList.get(i).get_imagePath(), image.getImage());
//                    image.getImage().setBackgroundResource(R.drawable.video_src_erral);
                } else {
                    image.getImage().setBackgroundResource(R.drawable.video_src_erral);
                }
            } else {
                image.setImageBitmap(videoList.get(i).getBitmap());
            }
            image.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Uri uri = null;
                    try {
                        if (videoList.get(index).isNetPic) {
                            uri = Uri.parse(HTalkApplication.getHttpUrls().URL_DOWNLOAVIDEO + videoList.get(index).getImagePath());
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(uri, "video/mp4");
                            startActivity(intent);
                        } else {
                            startActivity(new Intent(FlowMassageActivity2.this, PlayVideoActiviy2.class).putExtra(PlayVideoActiviy2.KEY_FILE_PATH, videoList.get(index).getImagePath()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            image.setDeleteListener(new View.OnClickListener() {//删除图片

                @Override
                public void onClick(View v) {
                    ImageItem imageItem = videoList.get(index);
                    try {
                        JSONObject object = new JSONObject();
                        object.put("PICTUREID", imageItem.pidId + "");
                        object.put("SMALLPIC", imageItem.getThumbnailPath());
                        object.put("BIG_PICTURE", imageItem.getImagePath());
                        JSONObject thumbnailObject = new JSONObject();
                        thumbnailObject.put("PICTUREID", imageItem.thumbnailId + "");
                        thumbnailObject.put("SMALLPIC", imageItem.get_thumbnailPath());
                        thumbnailObject.put("BIG_PICTURE", imageItem.get_imagePath());
                        array.put(object);
                        array.put(thumbnailObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    videoList.remove(index);
                    showVideo();
                }
            });
            videos.addView(image);
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        switch (arg0) {
            case TAKE_PICTURE://选图片
                if (arg1 == Activity.RESULT_OK) {
                    if (storageFile != null) {
                        ImageItem takePhoto = new ImageItem();
                        takePhoto.setImagePath(storageFile.getAbsolutePath());
                        imagesList = Bimp.dataMap.get(IMAGEKEY);
                        imagesList.add(takePhoto);
                        showPhoto();
                    }
                }
                break;
            case RECODE_FLAG://录制视频
                if (arg1 == Activity.RESULT_OK) {
                    String result = arg2.getExtras().getString("filePath");
                    ImageItem videoItem = new ImageItem();
                    videoItem.setImagePath(result);
                    videoItem.setBitmap(PlayVideoActiviy.getVideoThumbnail(result));
                    videoList.add(videoItem);
                    showVideo();
                }
                break;
            case LOCAL_RECODE_FLAG://本地视频
                if (arg1 == Activity.RESULT_OK) {
                    Uri uri = arg2.getData();
                    String path = getDataColumn(uri, null, null);

                    ImageItem videoItem = new ImageItem();
                    videoItem.setImagePath(path);
                    videoItem.setBitmap(PlayVideoActiviy.getVideoThumbnail(path));
                    videoList.add(videoItem);
                    showVideo();
                }
                break;
            case 201://选医生
                if (arg1 == Activity.RESULT_OK) {
                    resultDsb = (DoctorSimpleBean) arg2.getSerializableExtra("data");
                }
                break;
        }
    }

    /**
     * 设置性别
     */
    private void setXingbie() {
        String[] xingbie = getResources().getStringArray(R.array.sex);
        WheelUtils.setSingleWheel(this, xingbie, mainView, pop, wheelView,
                false);
    }

    /**
     * 设置年龄
     */
    private void setAge() {
        String[] ageList = new String[100];
        for (int i = 0; i < 100; i++) {
            ageList[i] = i + "";
        }
        WheelUtils.setSingleWheel(this, ageList, mainView, pop, wheelView,
                false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        showPhoto();//展示图片
        showVideo();//展示视频
    }

    /**
     * 关闭软键盘
     */
    private void closeKeyBoard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onStart() {//选完医生回来显示医生信息
        super.onStart();
        if (resultDsb != null) {
            topView.setVisibility(View.VISIBLE);
            bottomView.setVisibility(View.VISIBLE);
            addDoctor.setChecked(true);
            mInstance = ImageLoader.getInstance();
            mInstance.displayImage("", resultDsb.ICON_DOCTOR_PICTURE, selectedAHeader);
            if ("null".equals(resultDsb.DOCTOR_REAL_NAME)) {
                selectedAName.setText("暂无");
            } else {
                selectedAName.setText(resultDsb.DOCTOR_REAL_NAME);
            }
            if ("null".equals(resultDsb.TITLE_NAME)) {
                selectedADuty.setText("暂无");
            } else {
                selectedADuty.setText(resultDsb.TITLE_NAME);
            }
            if ("null".equals(resultDsb.UNIT_NAME)) {
                selectedAHospital.setText("暂无");
            } else {
                selectedAHospital.setText(resultDsb.UNIT_NAME);
            }
            if ("null".equals(resultDsb.DOCTOR_SPECIALLY)) {
                selectedASpeciality.setText("暂无");
            } else {
                selectedASpeciality.setText(resultDsb.DOCTOR_SPECIALLY);
            }
            clicked = true;
        } else {
            addDoctor.setChecked(false);
        }

    }

    /**
     * 修改申请单以前的图片
     */
    private void pastConImage() {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("case_discussion_id", conId));
        pairs.add(new BasicNameValuePair("op", "queryPatientCaseInfo"));
//        pairs.add(new BasicNameValuePair("CONSULTATIONID", conId + ""));
//        pairs.add(new BasicNameValuePair("CUSTID", LoginServiceManeger.instance().getLoginEntity().getId()));
        HttpRestClient.OKHttpCaseInfo(pairs, new OkHttpClientManager.ResultCallback<JSONObject>() {

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(JSONObject response) {
                if (1 == response.optInt("code")) {
                    JSONObject object = response.optJSONObject("result");
                    JSONArray array = object.optJSONArray("PICS");
                    int count = array.length();

                    //资源准备
                    for (int m = 0; m < count; m++) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = array.getJSONObject(m);
                            if (AppData.PIC_TYPE.equals(jsonObject.optString("PIC_TYPE"))) {
                                ImageItem imageItem = new ImageItem();
                                imageItem.pidId = jsonObject.optInt("PICTURE_ID");
                                imageItem.seq = jsonObject.optInt("PICTURE_SEQ");
                                imageItem.setThumbnailPath(jsonObject.optString("SMALL_PICTURE"));
                                imageItem.setImagePath(jsonObject.optString("BIG_PICTURE"));
                                imageItem.isNetPic = true;
                                imagesList.add(imageItem);
                            } else if (AppData.VIDEO_TYPE.equals(jsonObject.optString("PIC_TYPE"))) {
                                videoDataList.add(jsonObject);
                            } else if (AppData.THUMBNAIL_TYPE.equals(jsonObject.optString("PIC_TYPE"))) {
                                thumbnailDataList.add(jsonObject);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    int videoCount = videoDataList.size();
                    if (videoCount > 0) {
                        for (int i = 0; i < videoCount; i++) {
                            ImageItem imageItem = new ImageItem();
                            imageItem.pidId = videoDataList.get(i).optInt("PICTURE_ID");
                            imageItem.setThumbnailPath(videoDataList.get(i).optString("SMALL_PICTURE"));
                            imageItem.setImagePath(videoDataList.get(i).optString("BIG_PICTURE"));
                            if (thumbnailDataList.size() == videoCount) {
                                imageItem.thumbnailId = thumbnailDataList.get(i).optInt("PICTURE_ID");
                                imageItem.set_thumbnailPath(thumbnailDataList.get(i).optString("SMALL_PICTURE"));
                                imageItem.set_imagePath(thumbnailDataList.get(i).optString("BIG_PICTURE"));
                            }
                            imageItem.isNetPic = true;
                            videoList.add(imageItem);
                        }
                    }
                    showPhoto();
                    showVideo();
                }
            }
        }, this);
    }

    @Override
    public void onBackPressed() {
        if (TYPE != null) {
            EventBus.getDefault().post(new MyEvent("refresh", 2));
        }
        super.onBackPressed();

    }

    /**
     * 根据uri获取当前路径
     */
    public String getDataColumn(Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";//路径保存在downloads表中的_data字段
        final String[] projection = {column};
        try {
            cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

}