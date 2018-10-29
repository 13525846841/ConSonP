package com.yksj.consultation.son.consultation.consultationorders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.yksj.consultation.adapter.AddImageGridAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.photo.utils.AlbumActivity;
import com.yksj.healthtalk.photo.utils.GalleryActivity;
import com.yksj.healthtalk.utils.Bimp;
import com.yksj.healthtalk.utils.BitmapUtils;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ImageItem;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.WheelUtils;

import org.cropimage.CropUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;


/**
 * Created by HEKL on 15/10/19.
 * Used for 患者提出疑问_
 */
public class AtyQuestion extends BaseFragmentActivity implements View.OnClickListener {
    private Button mButton;// 保存并提交
    private EditText mEditText;
    private RelativeLayout mOpinion;
    private GridView mGridView;
    private AddImageGridAdapter gridAdapter;
    private PopupWindow mPopupWindow;// 上传图片的弹出框
    ArrayList<ImageItem> images = new ArrayList<ImageItem>();
    public static final int CAMERA_REQUESTCODE = 3;
    private String mConsultOpinion;
    private File storageFile;
    private int TYPE;// 会诊报告类型
    private int conId;// 会诊id
    private int clickCount;

    StringBuffer resultBuffer = null;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.pop_quesion);
        initView();
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void initView() {
        initTitle();
        titleLeftBtn.setOnClickListener(this);
        titleTextV.setText("提出疑问");
        resultBuffer = new StringBuffer();
        conId = getIntent().getIntExtra("conId", 0);
        TYPE = getIntent().getIntExtra("type", 0);
        mGridView = (GridView) findViewById(R.id.gv_pic);
        mEditText = (EditText) findViewById(R.id.et_question);
        mOpinion = (RelativeLayout) findViewById(R.id.rl_opinion);
        mButton = (Button) findViewById(R.id.btn_savesubmit);
        mButton.setOnClickListener(this);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() >= 1000) {
                    ToastUtil.showShort("您输入的字数已经超过了限制！");
                }
            }
        });
        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        Bimp.dataMap.put(AtyQuestion.class.getSimpleName(), images);
        Bimp.imgMaxs.put(AtyQuestion.class.getSimpleName(), 8);
        gridAdapter = new AddImageGridAdapter(this, AtyQuestion.class.getSimpleName());
        switch (TYPE) {
            case 2:// 资深医生 给会诊报告---
                mGridView.setVisibility(View.VISIBLE);
                mButton.setVisibility(View.VISIBLE);
                mOpinion.setVisibility(View.VISIBLE);
                mGridView.setAdapter(gridAdapter);
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == Bimp.dataMap.get(AtyQuestion.class.getSimpleName()).size()) {// 添加图片
                            showuploadPopWindow();
                        } else {
                            Intent intent = new Intent(AtyQuestion.this, GalleryActivity.class);
                            intent.putExtra("key", AtyQuestion.class.getSimpleName());
                            intent.putExtra("position", "1");
                            intent.putExtra("ID", position);// 设置当前的图片
                            startActivity(intent);
                        }
                    }
                });
                break;

        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        mConsultOpinion = mEditText.getText().toString();// 会诊报告内容
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.btn_savesubmit:
                if (HStringUtil.isEmpty(mConsultOpinion.trim())) {
                    ToastUtil.showShort("请输入您的问题");
                } else {
                    DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "确认保存并提交此疑问给专家吗 ？", "取消", "确定",
                            new DoubleBtnFragmentDialog.OnDilaogClickListener() {

                                @Override
                                public void onDismiss(DialogFragment fragment) {

                                }

                                @Override
                                public void onClick(DialogFragment fragment, View v) {
                                    sendConsultQuestion();
                                }
                            });
                }
                break;
            case R.id.galleryadd:// 从相册获取
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                try {
                    if (!SystemUtils.getScdExit()) {
                        ToastUtil.showSDCardBusy();
                        return;
                    }
                    intent = new Intent(AtyQuestion.this, AlbumActivity.class);
                    intent.putExtra("key", AtyQuestion.class.getSimpleName());
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.cameraadd:// 相机拍照
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                if (!SystemUtils.getScdExit()) {
                    ToastUtil.showSDCardBusy();
                    return;
                }
                storageFile = null;
                if (StorageUtils.isSDMounted()) {
                    try {
                        storageFile = StorageUtils.createCameraFile();
                        Uri outUri = Uri.fromFile(storageFile);
                        intent = CropUtils.createPickForCameraIntent(outUri);
                        startActivityForResult(intent, CAMERA_REQUESTCODE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showLong(AtyQuestion.this, "系统相机异常");
                    }
                } else {
                    SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "sdcard未加载");
                }
                break;
            case R.id.cancel:// 取消
                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                break;

        }
    }

    /**
     * 弹出上传图片的选择布局
     */
    public void showuploadPopWindow() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.interest_image_add_action, null);
        View mainView = inflater.inflate(R.layout.interest_content, null);
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        WheelUtils.setPopeWindow(this, mainView, mPopupWindow);
        Button cameraAdd = (Button) view.findViewById(R.id.cameraadd);
        Button galleryAdd = (Button) view.findViewById(R.id.galleryadd);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        cameraAdd.setOnClickListener(this);
        galleryAdd.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    // 将文件放入参数
    private void putFile(RequestParams params) {
        images = Bimp.dataMap.get(AtyQuestion.class.getSimpleName());// 根据ID寻找
        for (int i = 0; i < images.size(); i++) {
            int index = i + 1;
            try {
                params.put(index + ".jpg", BitmapUtils.onGetDecodeFileByPath(AtyQuestion.this, images.get(i).getImagePath()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUESTCODE:// 相机拍照返回
                if (storageFile != null) {
                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setImagePath(storageFile.getAbsolutePath());
                    ArrayList<ImageItem> list = Bimp.dataMap.get(AtyQuestion.class.getSimpleName());
                    list.add(takePhoto);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        gridAdapter.notifyDataSetChanged();
    }

    /**
     * 发送患者疑问
     */
    private void sendConsultQuestion() {
        if (clickCount > 0) {
            return;
        }
        clickCount++;
        String str = mEditText.getText().toString();
        RequestParams params = new RequestParams();
        params.putNullFile("file", new File(""));
        params.put("CUSTID", SmartFoxClient.getLoginUserId());
        params.put("CONSULTATIONID", conId + "");
        params.put("CONTENT", str);
        putFile(params);
        HttpRestClient.doHttpPostConsultQuestion(params, new AsyncHttpResponseHandler(this) {
            JSONObject object = null;

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                clickCount = 0;
            }

            @Override
            public void onSuccess(String content) {
                if (!TextUtils.isEmpty(content)) {
                    try {
                        object = new JSONObject(content);
                        ToastUtil.showShort(object.optString("message"));
                        if (object.optInt("code") == 1) {
                            EventBus.getDefault().post(new MyEvent("opinion", 2));
                            finish();
                        } else {
                            clickCount = 0;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(content);
            }
        });
    }
}