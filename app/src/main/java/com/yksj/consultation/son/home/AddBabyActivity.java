package com.yksj.consultation.son.home;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.listener.DialogOnClickListener;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.entity.DocPlanEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.photo.utils.PhotoUtil;
import com.yksj.healthtalk.utils.BitmapUtils;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ImageItem;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.WheelUtils;
import com.yksj.healthtalk.views.SelectPopupWindow;

import org.cropimage.CropUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 添加宝贝界面
 */
public class AddBabyActivity extends BaseFragmentActivity implements View.OnClickListener {

    public static final String TYPE = "TYPE";
    private ImageView addHeadView;
    private RelativeLayout rl_name;
    private RelativeLayout rl_sex;
    private RelativeLayout rl_time;
    private RelativeLayout rl_height;
    private RelativeLayout rl_weight;
    private RelativeLayout rl_note;
    private TextView mName;//姓名
    private TextView mTV_text;//备注
    private TextView tv_weight;//体重
    private TextView tv_height;//身高
    private TextView birthday;//出生日期
    private TextView tv_sex;//性别
    private static final int SHOW_DATAPICK = 0;
    private static final int DATE_DIALOG_ID = 1;
    private PopupWindow addPhotoPop;
    public static final String IMAGEKEY = AddBabyActivity.class.getSimpleName() + "image";//图片标志
    private int mYear;
    private int mMonth;
    private int mDay;
    private TextView modify;
    SelectPopupWindow menuWindow;
    ArrayList<ImageItem> imagesList;//图片list类
    private File storageFile = null;
    private static final int TAKE_PICTURE = 0x000001;
    private static final int PHOTO_PICKED_WITH_DATA = 1881;
    private static final int CAMERA_WITH_DATA = 1882;
    private static final int CAMERA_CROP_RESULT = 1883;
    private static final int PHOTO_CROP_RESOULT = 1884;
    private static final int ICON_SIZE = 96;
    private File mCurrentPhotoFile;
    private Bitmap imageBitmap;
    private PopupWindow listpop;//相册
    private TextView duoMeiHao;
    private CustomerInfoEntity mLoginUserInfo;
    public final int PHOTOHRAPH = 1001;// 拍照
    public final int PHOTORESOULT = 1003;// 结果
    private File currentCameraFile;
    private File headerFile = null;//拍照文件
    public String type = "";
    private TextView CURRENTVIEW;
    /**
     * 处理日期控件的Handler
     * 出生日期
     */
    Handler dateandtimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AddBabyActivity.SHOW_DATAPICK:
                    showDialog(DATE_DIALOG_ID);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_baby);
        initView();
    }

    private View wheelView;
    private PopupWindow pop;
    private PopupWindow birPop;
    private View mainView;

    private String children_id;
    private String name1 = "";//得到的姓名
    private String sex1;//得到的性别数据
    private String birthday1;
    //private String hight;
//  private String wight;
    private String remark;//备注

    private DocPlanEntity mDocPlanEntity;//宝贝计划实体

    private String weight;//体重
    private String height;//身高
    private String name;//名字
    private String text;//备注
    private String sex;//性别  传递给服务器的值，0 女 1 男
    private String birthdayNote;
//  private int year;
//  private int month;
//  private int day;
    private MyAlertDialog dialog;//姓名，备注 dialog
    private SexAlertDialog sDialog;//性别 dialog
    private WheelViewDialog mDialog;//身高，体重，dialog
    private TimeWheelDialog timeDialog;//生日

    private List<Map<String, String>> numberList = new ArrayList<Map<String, String>>();
    private List<Map<String, String>> mUnit = new ArrayList<Map<String, String>>();;

    private void initView() {
        initTitle();
        titleRightBtn2.setVisibility(View.VISIBLE);
        titleRightBtn2.setText("完成");
        mName = (TextView) findViewById(R.id.name);
        mTV_text = (TextView) findViewById(R.id.tv_text);
        tv_weight = (TextView) findViewById(R.id.tv_weight);
        tv_height = (TextView) findViewById(R.id.tv_height);
        modify = (TextView) findViewById(R.id.modify);
        tv_sex = (TextView) findViewById(R.id.sex);
        birthday = (TextView) findViewById(R.id.birthday);
        addHeadView = (ImageView) findViewById(R.id.iv_picture);
        if (getIntent().hasExtra(TYPE))
            type = getIntent().getStringExtra(TYPE);


        if (getIntent().hasExtra("content")) {
            mDocPlanEntity = (DocPlanEntity) getIntent().getSerializableExtra("content");
            children_id = mDocPlanEntity.getCHILDREN_ID();
            name1 = mDocPlanEntity.getCHILDREN_NAME();
            sex1 = mDocPlanEntity.getCHILDREN_SEX();
            height = mDocPlanEntity.getCHILDREN_HIGHT();
            weight = mDocPlanEntity.getCHILDREN_WEIGHT();
            remark = mDocPlanEntity.getCHILDREN_REMARK();
            birthday1 = mDocPlanEntity.getCHILDREN_BIRTHDAY();

        }

        if ("add".equals(type)) {
            modify.setVisibility(View.GONE);
            titleTextV.setText("添加宝贝");

        } else if ("modify".equals(type)) {
            modify.setVisibility(View.VISIBLE);
            modify.setOnClickListener(this);
            titleTextV.setText("修改宝贝");
            mName.setText(name1);

            //tv_sex.setText(sex1);
            if (sex1.equals("1")) {
                tv_sex.setText("男");
            } else if (sex1.equals("0")) {
                tv_sex.setText("女");
            }

            if (HStringUtil.isEmpty(birthday1)) {
                birthday.setText("");
            } else {
                birthday.setText(birthday1);
            }

            tv_height.setText(height +"cm");
            tv_weight.setText(weight + "kg");
            mTV_text.setText(remark);
            //图片展示
            String url = HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW + mDocPlanEntity.getHEAD_PORTRAIT_ICON();
            Picasso.with(AddBabyActivity.this).load(url).placeholder(R.drawable.waterfall_default).into(addHeadView);
        }

        titleLeftBtn.setOnClickListener(this);
        titleRightBtn2.setOnClickListener(this);
        addHeadView.setOnClickListener(this);

        rl_name = (RelativeLayout) findViewById(R.id.rl_name);
        rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);
        rl_time = (RelativeLayout) findViewById(R.id.rl_time);
        rl_height = (RelativeLayout) findViewById(R.id.rl_height);
        rl_weight = (RelativeLayout) findViewById(R.id.rl_weight);
        rl_note = (RelativeLayout) findViewById(R.id.rl_note);

        rl_name.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        rl_time.setOnClickListener(this);
        rl_height.setOnClickListener(this);
        rl_weight.setOnClickListener(this);
        rl_note.setOnClickListener(this);


        wheelView = getLayoutInflater().inflate(R.layout.wheel, null);
        wheelView.findViewById(R.id.wheel_cancel).setOnClickListener(this);
        wheelView.findViewById(R.id.wheel_sure).setOnClickListener(this);
        pop = new PopupWindow(wheelView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mainView = getLayoutInflater().inflate(R.layout.activity_add_baby, null);

        View checklist = getLayoutInflater().inflate(R.layout.personal_photo_check, null);
        listpop = new PopupWindow(checklist, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        checklist.findViewById(R.id.paizhao).setOnClickListener(this);
        checklist.findViewById(R.id.bendifenjian).setOnClickListener(this);
        checklist.findViewById(R.id.quxiao).setOnClickListener(this);
        // setDateTime();
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right2:
                if ("add".equals(type)) {
                    finishAdd();
                } else if ("modify".equals(type)) {
                    modifyData();
                }
                break;
            case R.id.modify://删除数据
                DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "你确定要删除吗？", "取消", "确定", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
                    @Override
                    public void onDismiss(DialogFragment fragment) {

                    }

                    @Override
                    public void onClick(DialogFragment fragment, View v) {
                        DelectBaby();
                    }
                });
                break;
            case R.id.iv_picture:
                //showuploadPopWindow();
                WheelUtils.setPopeWindow(AddBabyActivity.this, v,
                        listpop);
                break;
            case R.id.rl_name://姓名
                initMDDialog();
                dialog.show();
                break;
            case R.id.rl_sex://性别
                CURRENTVIEW = tv_sex;
                initSexDialog();
                //sDialog.show();
                break;
            case R.id.rl_time://出生时间
                CURRENTVIEW = birthday;
                initBirthdayDialog();
                //timeDialog.show();
                break;
            case R.id.rl_height://身高
                CURRENTVIEW = tv_height;
                initHeightDialog();
                //mDialog.show();
                break;
            case R.id.rl_weight://体重
                CURRENTVIEW = tv_weight;
                initWeightDialog();
                //mDialog.show();
                break;
            case R.id.rl_note://备注
                initMDDialog2();
                dialog.show();
                break;

            case R.id.paizhao: // 自拍照片
                listpop.dismiss();
                if (!SystemUtils.getScdExit()) {
                    ToastUtil.showShort(this, R.string.chatting_sd_uninstall);
                    return;
                }
                try {
                    currentCameraFile = StorageUtils.createImageFile();
                    Uri outUri = Uri.fromFile(currentCameraFile);
                    intent = CropUtils.createPickForCameraIntent(outUri);
                    startActivityForResult(intent, PHOTOHRAPH);
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtil.showCreateFail();
                }
                break;
            case R.id.wheel_cancel://滑轮取消
                if (pop != null)
                    pop.dismiss();
                break;
            case R.id.wheel_sure://滑轮确定
                if (pop != null)
                    pop.dismiss();
                if (WheelUtils.getCurrent() != null) {
                    setTextContent();
                }
                break;
            case R.id.bendifenjian: // 相册选择图片
                listpop.dismiss();
                if (!SystemUtils.getScdExit()) {
                    ToastUtil.showShort(AddBabyActivity.this,
                            "SD卡拔出,六一健康用户头像,语音,图片等功能不可用");
                    return;
                }
                intent = CropUtils.createPickForFileIntent();
                startActivityForResult(intent, PHOTORESOULT);
                break;
            case R.id.quxiao:
                listpop.dismiss();
                break;
        }
    }

    private void setTextContent() {
        if (CURRENTVIEW.equals(tv_sex)) {
            tv_sex.setText(WheelUtils.getCurrent());
            if ("男".equals(tv_sex.getText())){
                sex = "1";
            }else if ("女".equals(tv_sex.getText())){
                sex = "0";
            }
        } else if (CURRENTVIEW.equals(tv_weight)) {
//            weight = WheelUtils.getCurrent();
            weight = WheelUtils.getCurrent1()+"."+WheelUtils.getCurrent2();
            tv_weight.setText(weight + "kg");
        } else if (CURRENTVIEW.equals(tv_height)) {
//            height = WheelUtils.getCurrent();
            height = WheelUtils.getCurrent1()+"."+WheelUtils.getCurrent2();
            tv_height.setText(height + "cm");
        }
    }
    private String  customer_id =  SmartFoxClient.getLoginUserId();;
    /**
     * 修改数据
     */
    private void modifyData() {
        name = mName.getText().toString().trim();
        text = mTV_text.getText().toString();
        birthdayNote = birthday.getText().toString();

        if (tv_sex.getText().toString().equals("女")){
            sex = "0";
        }else if (tv_sex.getText().toString().equals("男")){
            sex = "1";
        }

        if (TextUtils.isEmpty(birthdayNote)) {
            ToastUtil.showToastPanl("请填写出生日期");
            return;
        }

        if (TextUtils.isEmpty(height)) {
            ToastUtil.showToastPanl("请填写身高");
            return;
        }
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showToastPanl("请填写姓名");
            return;
        }
        if (TextUtils.isEmpty(text)) {
            ToastUtil.showToastPanl("请填写备注");
            return;
        }
        if (!SystemUtils.isNetWorkValid(this)) {
            ToastUtil.showShort(this, R.string.getway_error_note);
            return;
        }

        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{new OkHttpClientManager.Param("children_hight", height)
                , new OkHttpClientManager.Param("children_weight", weight)
                , new OkHttpClientManager.Param("children_name", name)
                , new OkHttpClientManager.Param("children_id", children_id)
                , new OkHttpClientManager.Param("customer_id", customer_id)
                , new OkHttpClientManager.Param("children_remark", text)
                , new OkHttpClientManager.Param("children_birthday", birthdayNote)
                , new OkHttpClientManager.Param("op", "updateChildren")
                , new OkHttpClientManager.Param("children_sex", sex)};


        HttpRestClient.OKHttpModityInformation("photo", headerFile, params, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {}

            @Override
            public void onResponse(String content) {
                if (!HStringUtil.isEmpty(content)) {
                    try {
                        JSONObject object = new JSONObject(content);
                        if (HttpResult.SUCCESS.equals(object.optString("code"))) {
                            finish();
                        }
                        ToastUtil.showShort(object.optString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, this);
    }

    /**
     * 完成
     */
    private void finishAdd() {

        if (TextUtils.isEmpty(name)) {
            ToastUtil.showToastPanl("请填写姓名");
            return;
        }
        if (TextUtils.isEmpty(sex)) {
            ToastUtil.showToastPanl("请填写性别");
            return;
        }
        if (TextUtils.isEmpty(birthdayNote)) {
            ToastUtil.showToastPanl("请填写出生日期");
            return;
        }


        if (TextUtils.isEmpty(weight)) {
            ToastUtil.showToastPanl("请填写体重");
            return;
        }
        if (TextUtils.isEmpty(height)) {
            ToastUtil.showToastPanl("请填写身高");
            return;
        }

        if (TextUtils.isEmpty(text)) {
            ToastUtil.showToastPanl("请填写备注");
            return;
        }
        if (headerFile==null){
            ToastUtil.showToastPanl("请选择照片");
            return;
        }

        if (!SystemUtils.isNetWorkValid(this)) {
            ToastUtil.showShort(this, R.string.getway_error_note);
            return;
        }

        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{new OkHttpClientManager.Param("children_hight", height)
                , new OkHttpClientManager.Param("children_weight", weight)
                , new OkHttpClientManager.Param("children_name", name)
                , new OkHttpClientManager.Param("customer_id", customer_id)
                , new OkHttpClientManager.Param("children_remark", text)
                , new OkHttpClientManager.Param("children_birthday", birthdayNote)
                , new OkHttpClientManager.Param("op", "addChildren")
                , new OkHttpClientManager.Param("children_sex", sex)};
        HttpRestClient.OKHttpSaveInformation("photo", headerFile, params, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String content) {
                if (!HStringUtil.isEmpty(content)) {
                    try {
                        JSONObject object = new JSONObject(content);
                        if (HttpResult.SUCCESS.equals(object.optString("code"))) {
                            finish();
                        }
                        ToastUtil.showShort(object.optString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, this);
    }

    /**
     * 删除宝贝
     */
    private void DelectBaby() {
        Map<String, String> map = new HashMap<>();
        map.put("children_id", children_id);
        HttpRestClient.OKHttpDelectPlan(map, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))) {
                        ToastUtil.showShort(obj.optString("message"));
                    } else {
                        ToastUtil.showShort(obj.optString("message"));
                    }
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this);
    }
    /**
     * 生日dialog
     */
    private void initBirthdayDialog() {
        if(birPop == null ){
            birPop=WheelUtils.showThreeDateWheel(this, mainView, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.wheel_sure_age:
                            String[] str = (String[]) v.getTag();
                            birthdayNote = str[0] + str[1] + str[2];
                            birthday.setText(str[0] + str[1] + str[2]);
                            break;
                    }
                }
            });
        }else if(birPop.isShowing()){
            birPop.dismiss();
        }else{
            birPop.showAtLocation(mainView, Gravity.BOTTOM, 0, 0);
        }
    }

    public final static int WEIGHT = 0;

    /**
     * 体重的dialog
     */
    private void initWeightDialog() {

//        String[] weightList = new String[100];
//        for (int i = 0; i < 100; i++) {
//            weightList[i] = i + "";
//        }
//        WheelUtils.setSingleWheel(this, weightList, mainView, pop, wheelView,
//                false);

        String[] number = new String[50];
        for (int i = 0; i < 50; i++) {
            number[i] = i + "";
        }

        for (int i = 0; i < number.length; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", number[i]);
            numberList.add(map);
        }

        String[] pointUnit = new String[10];
        for (int j = 0; j < 10; j++) {
            pointUnit[j] = j + "";
        }
        for (int i = 0; i < pointUnit.length; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", pointUnit[i]);
            mUnit.add(map);
        }

        WheelUtils.setDoubleWheel1(AddBabyActivity.this, numberList, mUnit, mainView, pop,
                wheelView);
    }

    /**
     * 性别
     */
    private void initSexDialog() {
        String[] xingbie = getResources().getStringArray(R.array.sex);
        WheelUtils.setSingleWheel(this, xingbie, mainView, pop, wheelView,
                false);
    }
    /**
     * 身高的dialog
     */
    private void initHeightDialog() {
//        String[] heightList = new String[200];
//        for (int i = 0; i < 200; i++) {
//            heightList[i] = i + "";
//        }
//        WheelUtils.setSingleWheel(this, heightList, mainView, pop, wheelView,
//                false);

        String[] number = new String[50];
        for (int i = 0; i < 50; i++) {
            number[i] = i + "";
        }

        for (int i = 0; i < number.length; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", number[i]);
            numberList.add(map);
        }

        String[] pointUnit = new String[10];
        for (int j = 0; j < 10; j++) {
            pointUnit[j] = j + "";
        }
        for (int i = 0; i < pointUnit.length; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", pointUnit[i]);
            mUnit.add(map);
        }

        WheelUtils.setDoubleWheel1(AddBabyActivity.this, numberList, mUnit, mainView, pop,
                wheelView);
    }

    /**
     * 备注
     */
    private void initMDDialog2() {
        dialog = new MyAlertDialog.Builder(AddBabyActivity.this)
                .setTitleText("备注")
                .setHeight(0.21f)  //屏幕高度*0.21
                .setWidth(0.7f)  //屏幕宽度*0.7
                .setEdittext(remark)
                .setOnclickListener(new DialogOnClickListener() {
                    @Override
                    public void clickButton(View view) {
                        text = dialog.edittext();
                        if (text.length()>10){
                            ToastUtil.showShort("备注名称不超过10个字");
                        }else {
                            mTV_text.setText(text);
                            dialog.dismiss();
                        }
                    }
                })
                .build();
    }

    /**
     * 弹出dialog
     */
    private void initMDDialog() {
        dialog = new MyAlertDialog.Builder(AddBabyActivity.this)
                .setTitleText("姓名")
                .setHeight(0.21f)  //屏幕高度*0.21\
                .setWidth(0.7f)  //屏幕宽度*0.
                .setEdittext(name1)
                .setOnclickListener(new DialogOnClickListener() {
                    @Override
                    public void clickButton(View view) {
//                        if (!HStringUtil.isEmpty(name1)){
//                            dialog.setEditText(name1);
//                        }else {
//                            dialog.setEditText("");
//                        }
                        name = dialog.edittext();
                        if (name.length()>5){
                            ToastUtil.showShort("名称字数不超过5个字");
                        }else{
                            mName.setText(name);
                            dialog.dismiss();
                        }
                    }
                })
                .build();
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.phote://相册
                    // 相册获取
                    doPickPhotoFromGallery();
                    break;
                case R.id.picture://拍照
                    // 拍照获取
                    doTakePhoto();
                    break;
                case R.id.btn_cancel:
                    ToastUtil.showShort("取 消");
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 调用系统相机拍照
     */
    protected void doTakePhoto() {
        try {
            // Launch camera to take photo for selected contact
            File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/Photo");
            if (!file.exists()) {
                file.mkdirs();
            }
            mCurrentPhotoFile = new File(file, PhotoUtil.getRandomFileName());
            final Intent intent = getTakePickIntent(mCurrentPhotoFile);
            startActivityForResult(intent, CAMERA_WITH_DATA);
        } catch (ActivityNotFoundException e) {
            ToastUtil.showShort("系统中无可用相册");
        }
    }


    public static Intent getTakePickIntent(File f) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;
    }

    /**
     * 获取系统剪裁图片的Intent.
     */
    public static Intent getCropImageIntent(Uri photoUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", ICON_SIZE);
        intent.putExtra("outputY", ICON_SIZE);
        intent.putExtra("return-data", true);
        return intent;
    }

    /**
     * 从相册选择图片
     */
    protected void doPickPhotoFromGallery() {
        try {
            final Intent intent = getPhotoPickIntent();
            startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
        } catch (ActivityNotFoundException e) {
            ToastUtil.showShort("系统中无可用相册");
        }
    }

    /**
     * 获取调用相册的Intent
     */
    public static Intent getPhotoPickIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        return intent;
    }

    /**
     * 图片裁剪
     *
     * @param path
     */
    private void onHandlerCropImage(String path) {
        if (!SystemUtils.getScdExit()) {
            ToastUtil.showSDCardBusy();
            return;
        }
        try {
            headerFile = StorageUtils.createHeaderFile();
            Uri outUri = Uri.fromFile(new File(path));
            Uri saveUri = Uri.fromFile(headerFile);
            Intent intent = CropUtils.createHeaderCropIntent(this, outUri, saveUri, true);
            startActivityForResult(intent, 3002);
        } catch (Exception e) {
            ToastUtil.showCreateFail();
        }
    }

    /**
     * 根据uri查询相册所对应的图片地址
     *
     * @param uri
     * @return
     */
    private String getImageUrlByAlbum(Uri uri) {
        String[] imageItems = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, imageItems, null, null, null);
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(index);
        return path;
    }
    //弹出popup
    private void showuploadPopWindow() {
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
        view.findViewById(R.id.cancel).setOnClickListener(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTOHRAPH: // 拍照
                if (resultCode == Activity.RESULT_OK) {
                    String strFilePath = currentCameraFile.getAbsolutePath();
                    onHandlerCropImage(strFilePath);
                }
                break;
            case PHOTORESOULT: // 相册
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        Uri uri = data.getData();
                        String scheme = uri.getScheme();
                        String strFilePath = null;//图片地址
                        // url类型content or file
                        if ("content".equals(scheme)) {
                            strFilePath = getImageUrlByAlbum(uri);
                        } else {
                            strFilePath = uri.getPath();
                        }
                        onHandlerCropImage(strFilePath);
                    }
                }
                break;
            case 3002://裁剪后获取结果
                if (resultCode == Activity.RESULT_OK) {
                    if (resultCode == RESULT_OK) {
                        Bitmap bitmap = BitmapUtils.decodeBitmap(headerFile.getAbsolutePath(),
                                CropUtils.HEADER_WIDTH,
                                CropUtils.HEADER_HEIGTH);
                        addHeadView.setImageBitmap(bitmap);
                    } else {
                        if (headerFile != null) headerFile.deleteOnExit();
                        headerFile = null;
                    }
                }

                break;
        }
    }
}

