package com.yksj.consultation.son.consultation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.PatientConsuListAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.MyResultCallback;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.BitmapUtils;
import com.yksj.healthtalk.utils.DataParseUtil;
import com.yksj.healthtalk.utils.LogUtil;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.ValidatorUtil;
import com.yksj.healthtalk.utils.WheelUtils;

import org.cropimage.CropUtils;
import org.json.JSONObject;
import org.universalimageloader.core.ImageLoader;
import org.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 编辑个人资料
 * Created by zheng on 15/10/8.
 */
public class AtyEditorPatientMessage extends BaseFragmentActivity implements View.OnClickListener {
    private ImageView patientHeader;
    private TextView duoMeiHao, ageEdit, sexEdit;
    private EditText nameEdit, phoneEdit, shuoming, codeEdit;
    private EditText mAllergy;//过敏史
    private CustomerInfoEntity mLoginUserInfo;
    private List<JSONObject> jsonLst;
    private PatientConsuListAdapter mAdapter;
    private ListView mListView;
    private LinearLayout conListLL,codell;//con_list_ll
    private boolean Sendcode = false, isApplying = false;//验证码是否发送 true为发送
    private Runnable runnable;
    private Button codeBtn;
    private PopupWindow listpop;//相册
    Handler handler = new Handler();
    private Intent intent;
    public final int PHOTOHRAPH = 1001;// 拍照
    public final int PHOTORESOULT = 1003;// 结果
    private File currentCameraFile;
    private File headerFile = null;//拍照文件
    private View mainView;
    private PopupWindow pop;
    private View wheelView;
    private TextView CURRENTVIEW;
    private JSONObject resultPerson;
    private String beforePhone="";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_patient_message_edit);
        initView();
    }

    private void initView() {
        initTitle();
        titleTextV.setText("编辑信息");
        titleLeftBtn.setOnClickListener(this);
        titleRightBtn2.setVisibility(View.VISIBLE);
        titleRightBtn2.setText("存储");
        titleRightBtn2.setOnClickListener(this);
        mLoginUserInfo = LoginServiceManeger.instance().getLoginEntity();

        patientHeader = ((ImageView) findViewById(R.id.patient_header));

        mainView = getLayoutInflater().inflate(R.layout.full_person_message, null);
        wheelView = getLayoutInflater().inflate(R.layout.wheel, null);
        wheelView.findViewById(R.id.wheel_cancel).setOnClickListener(this);
        wheelView.findViewById(R.id.wheel_sure).setOnClickListener(this);
        pop = new PopupWindow(wheelView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View checklist = getLayoutInflater().inflate(R.layout.personal_photo_check, null);
        listpop = new PopupWindow(checklist, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        checklist.findViewById(R.id.paizhao).setOnClickListener(this);
        checklist.findViewById(R.id.bendifenjian).setOnClickListener(this);
        checklist.findViewById(R.id.quxiao).setOnClickListener(this);
        conListLL = (LinearLayout) findViewById(R.id.con_list_ll);
        codell = (LinearLayout) findViewById(R.id.layout_4);
        codeBtn = (Button) findViewById(R.id.btn_code);
        codeEdit = (EditText) findViewById(R.id.full_code_edit);//full_code_edit
        shuoming = (EditText) findViewById(R.id.illness_state);
        mListView = (ListView) findViewById(R.id.case_list);
        duoMeiHao = (TextView) findViewById(R.id.duomei_hao);
        nameEdit = ((EditText) findViewById(R.id.name2_edit));
        ageEdit = ((TextView) findViewById(R.id.age2_edit));
        sexEdit = ((TextView) findViewById(R.id.sex2_edit));
        phoneEdit = ((EditText) findViewById(R.id.phone2_edit));
        mAllergy = (EditText) findViewById(R.id.allergy_state);
        duoMeiHao.setText(mLoginUserInfo.getUsername());

        patientHeader.setOnClickListener(this);
        codeBtn.setOnClickListener(this);
        ageEdit.setOnClickListener(this);
        sexEdit.setOnClickListener(this);
        initDate();
    }

    //获取资料
    private void initDate() {
        mAdapter = new PatientConsuListAdapter(this);
        mListView.setAdapter(mAdapter);
        onBandData();
    }

    //绑定资料
    private void onBandData() {
        HttpRestClient.OKHttpFindCustomerInfo(new MyResultCallback<JSONObject>(this) {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(JSONObject response) {
                super.onResponse(response);
                if (response != null) {
                    if ("1".equals(response.optString("code"))) {
                        JSONObject object = response.optJSONObject("result");
                        resultPerson = object;
                        ImageLoader instance = ImageLoader.getInstance();
                        instance.displayImage(object.optString("customerSex"), object.optString("bigIconBackground"), patientHeader);
                        nameEdit.setText(object.optString("realName"));
                        if ("M".equals(object.optString("customerSex"))) {
                            sexEdit.setText("男");
                        } else if ("W".equals(object.optString("customerSex"))) {
                            sexEdit.setText("女");
                        } else {
                            sexEdit.setText("未知");
                        }
                        ageEdit.setText(object.optString("age"));
                        phoneEdit.setText(object.optString("phone"));
                        beforePhone=object.optString("phone");
                        shuoming.setText(object.optString("diseaseDesc"));
                        mAllergy.setText(object.optString("allergy"));
                        if (TextUtils.isEmpty(phoneEdit.getText().toString().trim())) {
                            codell.setVisibility(View.VISIBLE);
                        } else {
                            phoneEdit.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void afterTextChanged(Editable editable) {
                                    if(beforePhone.equals(phoneEdit.getText().toString().trim())){
                                        codell.setVisibility(View.GONE);
                                    }else {
                                        codell.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        }, this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right2:
                upData();
                break;
            case R.id.btn_code:
                getAuthCode();
                break;
            case R.id.patient_header:
                closeKeyBoard();
                WheelUtils.setPopeWindow(AtyEditorPatientMessage.this, view,
                        listpop);
                break;
            case R.id.paizhao: // 自拍照片
                listpop.dismiss();
                if (mLoginUserInfo.isDoctor())
                    return;
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

            case R.id.bendifenjian: // 相册选择图片
                listpop.dismiss();
                if (!SystemUtils.getScdExit()) {
                    ToastUtil.showShort(AtyEditorPatientMessage.this,
                            "SD卡拔出,六一健康用户头像,语音,图片等功能不可用");
                    return;
                }
                intent = CropUtils.createPickForFileIntent();
                startActivityForResult(intent, PHOTORESOULT);
                break;
            case R.id.quxiao:
                listpop.dismiss();
                break;
            case R.id.wheel_cancel:
                if (pop != null)
                    pop.dismiss();
                break;

            case R.id.wheel_sure:
                if (pop != null)
                    pop.dismiss();
                if (WheelUtils.getCurrent() != null) {
                    setText();
                }
                break;
            case R.id.age2_edit:
                closeKeyBoard();
                CURRENTVIEW = ageEdit;
                setAge();
                break;
            case R.id.sex2_edit:
                closeKeyBoard();
                CURRENTVIEW = sexEdit;
                setXingbie();
                break;
        }
    }

    public void upData() {
        nullErrer();
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("PATIENT_NAME", nameEdit.getText().toString().trim());
            if ("女".equals(sexEdit.getText().toString().trim())) {
                object.put("PATIENT_SEX", "W");
            } else if ("男".equals(sexEdit.getText().toString().trim())) {
                object.put("PATIENT_SEX", "M");
            } else {
                object.put("PATIENT_SEX", "Z");
            }
            object.put("PATIENTID", LoginServiceManeger.instance().getLoginEntity().getId());
            object.put("PATIENTTEL_PHONE", phoneEdit.getText().toString().trim());
            object.put("BEFORE_PATIENTTEL_PHONE",beforePhone);
            object.put("VERIFICATION_CODE", codeEdit.getText().toString().trim());
            object.put("AREA_CODE", "");
            object.put("DWELLING_PLACE", "");
            object.put("PATIENT_AGE", ageEdit.getText().toString().trim());
            object.put("CONSULTATION_DESC", shuoming.getText().toString().trim());
            object.put("ALLERGY", mAllergy.getText().toString().trim());//过敏史

            if (headerFile == null) {
                object.put("BIG_ICON_BACKGROUND", resultPerson.optString("bigIconBackground"));//clientIconBackground
                object.put("CLIENT_ICON_BACKGROUND", resultPerson.optString("clientIconBackground"));
            } else {
                object.put("BIG_ICON_BACKGROUND", "");
                object.put("CLIENT_ICON_BACKGROUND", "");
            }

            LogUtil.d("TAG", "患者端修改个人信息" + object.toString());
            OkHttpClientManager.Param param = new OkHttpClientManager.Param("PARAMETER", object.toString());
            OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{param};
            HttpRestClient.doPostUpdatePatientInfo("file", headerFile, params, new MyResultCallback<JSONObject>(this) {
                @Override
                public void onError(Request request, Exception e) {
                    LogUtil.d("TAG", "患者端修改个人信息错误");
                }

                @Override
                public void onResponse(JSONObject response) {
                    super.onResponse(response);
                    if (response != null) {
                        LogUtil.d("TAG","患者端修改个人信息返回信息"+response.toString());
                        if ("1".equals(response.optString("code"))) {
                            LogUtil.d("TAG", "患者端修改个人信息返回信息" + response.toString());
                            ToastUtil.showToastPanl("修改成功");
                            DataParseUtil.jsonUpDataCustomerInfo(response.optJSONObject("result"));
                            EventBus.getDefault().post(response);
                            finish();
                        }else {
                            ToastUtil.showToastPanl(response.optString("message"));
                        }
                    }
                }
            }, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void nullErrer() {
        if (!ValidatorUtil.checkMobile(phoneEdit.getText().toString().trim())) {
            ToastUtil.showToastPanl("手机号码有误");
            return;
        }
    }

    /**
     * 获取验证码
     */
    private void getAuthCode() {
        if (!SystemUtils.isNetWorkValid(this)) {
            ToastUtil.showShort(this, R.string.getway_error_note);
            return;
        }
        String phone = phoneEdit.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToastPanl("请填写手机号码");
            return;
        }
        if (!ValidatorUtil.checkMobile(phone)) {
            ToastUtil.showToastPanl("手机号码有误");
            return;
        }
        if (ValidatorUtil.checkMobile(phone)) {
            HttpRestClient.OKHttpSendUpdatePatientInfoCode(phone, new MyResultCallback<com.alibaba.fastjson.JSONObject>(this) {
                @Override
                public void onError(Request request, Exception e) {

                }

                @Override
                public void onResponse(com.alibaba.fastjson.JSONObject response) {
                    super.onResponse(response);
                    if ("0".equals(response.getString("code"))) {
                        SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), response.getString("message"));
                    } else {
                        Sendcode = true;
                        timerTaskC();
                        ToastUtil.showShort(AtyEditorPatientMessage.this, response.getString("message"));
                    }
                }
            }, this);
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
     * 设置年龄
     */
    private void setAge() {
        String[] ageList = new String[100];
        for (int i = 0; i < 100; i++) {
            ageList[i] = i + 1 + "";
        }
        WheelUtils.setSingleWheel(this, ageList, mainView, pop, wheelView,
                false);

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
     * 设置内容
     */
    public void setText() {
        if (CURRENTVIEW.equals(ageEdit)) {
            ageEdit.setText(WheelUtils.getCurrent());
        } else if (CURRENTVIEW.equals(sexEdit)) {
            sexEdit.setText(WheelUtils.getCurrent());
        }
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
                        patientHeader.setImageBitmap(bitmap);
                    } else {
                        if (headerFile != null) headerFile.deleteOnExit();
                        headerFile = null;
                    }
                }

                break;
        }
    }
}
