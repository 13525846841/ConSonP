package com.yksj.consultation.son.consultation.old;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.yksj.consultation.adapter.AddImageGridAdapter;
import com.yksj.consultation.adapter.ImageGridAdapter;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog.OnDilaogClickListener;
import com.yksj.consultation.comm.ImageGalleryActivity;
import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.comm.SingleBtnFragmentDialog.OnClickSureBtnListener;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.consultationorders.AtyConsultEvaluate;
import com.yksj.consultation.son.friend.DoctorClinicMainActivity;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.photo.utils.AlbumActivity;
import com.yksj.healthtalk.photo.utils.GalleryActivity;
import com.yksj.healthtalk.utils.Bimp;
import com.yksj.healthtalk.utils.BitmapUtils;
import com.yksj.healthtalk.utils.ImageItem;
import com.yksj.healthtalk.utils.JsonParser;
import com.yksj.healthtalk.utils.LogUtil;
import com.yksj.healthtalk.utils.SharePreUtils;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.WheelUtils;

import org.cropimage.CropUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.yksj.healthtalk.utils.SharePreUtils.getSharedPreferences;

/**
 * @author HEKL
 *         <p/>
 *         会诊报告
 */
public class DFgtConsultReport extends RootFragment implements OnClickListener {
    private Button mButton, mEvaluabe, mOrder;// 保存并提交
    private TextView mNoServiceHint, mDrNoServiceHint, mAddpic;
    private EditText mEditText, mReadEdit;
    private RelativeLayout mHaveEvaluate, mOpinion;
    private GridView mGridView;
    private ImageView mVoice;//语音

    private AddImageGridAdapter gridAdapter;
    private PopupWindow mPopupWindow;// 上传图片的弹出框
    private ImageGridAdapter mAdapter;// 显示图片适配
    ArrayList<ImageItem> images = new ArrayList<ImageItem>();
    ArrayList<HashMap<String, String>> list = null;
    CancelClickListener cancelListener = new CancelClickListener();
    public static final int CAMERA_REQUESTCODE = 3;
    public static final int PHOTO_PICKED_WITH_DATA = 1;
    private CustomerInfoEntity mInfoEntity;
    private String mConsultOpinion;
    private File storageFile, tempFile, headerFile;
    private boolean finish = false;
    private boolean haseFile = false;// 图片文件是否存在
    private int TYPE;// 会诊报告类型
    private int conId;// 会诊id
    public int ISCOM;// 是否被评价
    private String str;

    private static String TAG = "DAtyConsultSuggestion";
    StringBuffer resultBuffer = null;
    private Toast mToast;

    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

   SpeechRecognizer mIat ;
    RecognizerDialog  mIatDialog;
    SharedPreferences mSharedPreferences;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private final String PREFER_NAME = "com.iflytek.setting";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fgt_consult_report, null);
        initView(view);
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return view;
    }

    @SuppressLint("ShowToast")
    private void initView(View view) {
        resultBuffer = new StringBuffer();
        mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
        conId = getArguments().getInt("consultationId");
        TYPE = getArguments().getInt("type");
        mDrNoServiceHint = (TextView) view.findViewById(R.id.tv_hint2);
        mNoServiceHint = (TextView) view.findViewById(R.id.tv_hint1);
        mHaveEvaluate = (RelativeLayout) view.findViewById(R.id.rl_haveEvaluate);
        mGridView = (GridView) view.findViewById(R.id.gv_pic);
        mReadEdit = (EditText) view.findViewById(R.id.et_opinion2);
        mEditText = (EditText) view.findViewById(R.id.et_opinion);
        mEvaluabe = (Button) view.findViewById(R.id.btn_evaluate);
        mOpinion = (RelativeLayout) view.findViewById(R.id.rl_opinion);
        mAddpic = (TextView) view.findViewById(R.id.tv_addpic);
        mVoice = (ImageView) view.findViewById(R.id.image_voice);
        mButton = (Button) view.findViewById(R.id.btn_savesubmit);
        mOrder = (Button) view.findViewById(R.id.btn_order);
        mHaveEvaluate.setOnClickListener(this);
        mEvaluabe.setOnClickListener(this);
        mButton.setOnClickListener(this);
        mOrder.setOnClickListener(this);
        mVoice.setOnClickListener(this);

        mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mConsultOpinion = mEditText.getText().toString();// 会诊报告内容
                SharePreUtils.editorStringFromUserID(mActivity, "CONSULTATIONOPINION", mConsultOpinion);
                SharePreUtils.editorStringFromUserID(mActivity, "CONSULTATIONID", conId + "");
                if (s.length() != 0) {
                    resultBuffer.append(s.toString());
                } else {
                    resultBuffer.setLength(0);
                }
                if (s.length() >= 1500) {
                    ToastUtil.showShort("您输入的字数已经超过了限制！");
                }
            }
        });
        Bimp.dataMap.put(DFgtConsultReport.class.getSimpleName(), images);
        Bimp.imgMaxs.put(DFgtConsultReport.class.getSimpleName(), 8);
        gridAdapter = new AddImageGridAdapter(mActivity.getApplicationContext(),
                DFgtConsultReport.class.getSimpleName());
        LogUtil.e("CONSULTRE", conId + "");
        LogUtil.e("CONSULTRE", TYPE + "");

        switch (TYPE) {
            case 2:// 资深医生 给会诊报告---
                mGridView.setVisibility(View.VISIBLE);
                mButton.setVisibility(View.VISIBLE);
                mAddpic.setVisibility(View.VISIBLE);
                mOpinion.setVisibility(View.VISIBLE);
                if (((conId + "").equals(SharePreUtils.feachStringFromUserID(mActivity, "CONSULTATIONID", null))) && SharePreUtils.feachStringFromUserID(mActivity, "CONSULTATIONOPINION", null) != null) {
                    mEditText.setText(SharePreUtils.feachStringFromUserID(mActivity, "CONSULTATIONOPINION", null));
                } else {
                    mEditText.setText("");
                }
                mEditText.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        return false;
                    }
                });
                mEvaluabe.setVisibility(View.GONE);
                mGridView.setAdapter(gridAdapter);
                mGridView.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == Bimp.dataMap.get(DFgtConsultReport.class.getSimpleName()).size()) {// 添加图片
                            showuploadPopWindow();
                        } else {
                            Intent intent = new Intent(getActivity(), GalleryActivity.class);
                            intent.putExtra("key", DFgtConsultReport.class.getSimpleName());
                            intent.putExtra("position", "1");
                            intent.putExtra("ID", position);// 设置当前的图片
                            startActivity(intent);
                        }
                    }
                });
                break;
            case 3:// 会诊专家 会诊报告已完成----
                mOpinion.setVisibility(View.GONE);
                mAddpic.setVisibility(View.GONE);
                mEvaluabe.setVisibility(View.GONE);
                mReadEdit.setVisibility(View.VISIBLE);
                mGridView.setVisibility(View.VISIBLE);
                expertShowOpinion();
                mReadEdit.setFocusable(false);
                break;
            case 15:// 基层医生端 等待会诊报告提示----
                mNoServiceHint.setVisibility(View.VISIBLE);
                mAddpic.setVisibility(View.GONE);
                mGridView.setVisibility(View.GONE);
                mEvaluabe.setVisibility(View.GONE);
                break;
            case 25:// 患者端 评价专家
                mAddpic.setVisibility(View.GONE);
                showOpinion();
                mGridView.setVisibility(View.VISIBLE);
                mReadEdit.setVisibility(View.VISIBLE);
                mReadEdit.setFocusable(false);
                break;
            case 23:// 患者端 等待会诊报告提示---
                mNoServiceHint.setVisibility(View.VISIBLE);
                mDrNoServiceHint.setVisibility(View.VISIBLE);
                mAddpic.setVisibility(View.GONE);
                mGridView.setVisibility(View.GONE);
                mEvaluabe.setVisibility(View.GONE);
                break;

        }
        initVoiceData();
    }

    private void initVoiceData() {
        SpeechUtility.createUtility(mActivity, "appid=" + getString(R.string.app_id));
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
       mIat = SpeechRecognizer.createRecognizer(mActivity, mInitListener);
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(mActivity, mInitListener);
        mSharedPreferences = mActivity.getSharedPreferences(PREFER_NAME, Activity.MODE_PRIVATE);
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
//                ToastUtil.showToastPanl("初始化失败，错误码：" + code);
                ToastUtil.showShort(mActivity,"初始化失败，错误码：" + code);
//				showTip("初始化失败，错误码：" + code);
            }
        }
    };
    

    @Override
    public void onClick(View v) {
        Intent intent;
        mConsultOpinion = mEditText.getText().toString();// 会诊报告内容
        switch (v.getId()) {
            case R.id.btn_savesubmit:
                if (mConsultOpinion.equals("")) {
                    ToastUtil.showShort("请输入会诊意见");
                } else {
//                    SharePreUtils.editorStringFromUserID(mActivity, "CONSULTATIONOPINION", mConsultOpinion);
//                    SharePreUtils.editorStringFromUserID(mActivity, "CONSULTATIONID", conId + "");
                    DoubleBtnFragmentDialog.showDefault(getChildFragmentManager(), "确认保存并提交此报告给患者吗 ？", "取消", "确定",
                            new OnDilaogClickListener() {

                                @Override
                                public void onDismiss(DialogFragment fragment) {

                                }

                                @Override
                                public void onClick(DialogFragment fragment, View v) {
                                    sendConsultOpinion();
                                }
                            });
                }
                break;
            case R.id.btn_agree:
                mButton.setVisibility(View.GONE);
                mGridView.setVisibility(View.VISIBLE);
                mEditText.setFocusable(false);
                break;
            case R.id.rl_haveEvaluate:
                intent = new Intent(mActivity, AtyConsultEvaluate.class);
                intent.putExtra("iscom", 1);
                intent.putExtra("CONID", conId);
                startActivity(intent);
                break;
            case R.id.btn_reject:
                mButton.setVisibility(View.VISIBLE);
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
                    intent = new Intent(mActivity, AlbumActivity.class);
                    intent.putExtra("key", DFgtConsultReport.class.getSimpleName());
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
                        ToastUtil.showLong(getActivity(), "系统相机异常");
                    }
                } else {
                    SingleBtnFragmentDialog.showDefault(getChildFragmentManager(), "sdcard未加载");
                }
                break;
            case R.id.cancel:// 取消
                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                break;
            // 评价专家
            case R.id.btn_evaluate:
                intent = new Intent(mActivity, AtyConsultEvaluate.class);
                intent.putExtra("iscom", 0);
                intent.putExtra("CONID", conId);
                startActivityForResult(intent, 100);
                break;
            case R.id.btn_order://预约专家门诊加号
                intent = new Intent(getActivity(), DoctorClinicMainActivity.class);
                intent.putExtra("id", str);
//                intent.putExtra("id","3774");
                getActivity().startActivity(intent);
                break;
            case R.id.image_voice://语音识别
//                mIatDialog.setListener(recognizerDialogListener);
//                mIatDialog.show();
//                showTip("请开始说话…");
//              setParam();
//                mIatDialog.setListener(recognizerDialogListener);
//                mIatDialog.show();
//              ToastUtil.showShort("语音");
                 //设置参数
                setParam();
                mIatDialog.setListener(recognizerDialogListener);
                mIatDialog.show();

                break;
        }
    }


    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            ToastUtil.showShort(mActivity,error.getPlainDescription(true));

        }
    };

    /**
     * 参数设置
     *
     * @param
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        String lag = mSharedPreferences.getString("iat_language_preference",
                "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));
        // 设置音频保存路径，保存音频格式仅为pcm，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/iflytek/wavaudio.pcm");

        // 设置听写结果是否结果动态修正，为“1”则在听写过程中动态递增地返回结果，否则只在听写结束之后返回最终结果
        // 注：该参数暂时只对在线听写有效
        mIat.setParameter(SpeechConstant.ASR_DWA, mSharedPreferences.getString("iat_dwa_preference", "0"));
    }

    @Override
    public void onResume() {
        super.onResume();
        gridAdapter.notifyDataSetChanged();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUESTCODE:// 相机拍照返回
                if (storageFile != null) {
                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setImagePath(storageFile.getAbsolutePath());
                    ArrayList<ImageItem> list = Bimp.dataMap.get(DFgtConsultReport.class.getSimpleName());
                    list.add(takePhoto);
                }
                break;
            case (100):
                if (resultCode == 20) {
                    mEditText.setFocusable(false);
                    mEvaluabe.setVisibility(View.GONE);
                    mHaveEvaluate.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    /**
     * 弹出上传图片的选择布局
     */
    public void showuploadPopWindow() {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.interest_image_add_action, null);
        View mainView = inflater.inflate(R.layout.interest_content, null);
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
            mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        WheelUtils.setPopeWindow(mActivity, mainView, mPopupWindow);
        Button cameraAdd = (Button) view.findViewById(R.id.cameraadd);
        Button galleryAdd = (Button) view.findViewById(R.id.galleryadd);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        cameraAdd.setOnClickListener(this);
        galleryAdd.setOnClickListener(this);
        cancel.setOnClickListener(this);
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
            tempFile = StorageUtils.createHeaderFile();
            Uri outUri = Uri.fromFile(new File(path));
            Uri saveUri = Uri.fromFile(tempFile);
            Intent intent = CropUtils.createHeaderCropIntent(mActivity, outUri, saveUri, true);
            startActivityForResult(intent, 3002);
        } catch (Exception e) {
            ToastUtil.showCreateFail();
        }
    }

    /**
     * 发送会诊意见
     */
    private void sendConsultOpinion() {
        String str = mEditText.getText().toString();
        RequestParams params = new RequestParams();
        params.putNullFile("file", new File(""));
        params.put("CUSTID", SmartFoxClient.getLoginUserId());
        params.put("CONSULTATIONID", conId + "");
        params.put("CONTENT", str);
        putFile(params);
        HttpRestClient.doHttpPostConsultQuestion(params, new AsyncHttpResponseHandler(getActivity()) {
            JSONObject object = null;

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                if (finish) {
                    mActivity.finish();
                    SharedPreferences sp = getSharedPreferences(HTalkApplication.getMd5UserId()
                            + "version" + HTalkApplication.getAppVersionName() + "_comment");
                    Editor editor = sp.edit();
                    editor.putString("CONSULTATIONOPINION", "");
                    editor.commit();
                }
                super.onFinish();
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
            }

            @Override
            public void onSuccess(String content) {
                try {
                    object = new JSONObject(content);
                    if (content.contains("errorcode")) {
                        ToastUtil.showShort(object.optString("errormessage"));
                    } else {
                        finish = true;
                        ToastUtil.showShort(object.optString("INFO"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                super.onSuccess(content);
            }
        });
    }

    /**
     * 患者查看会诊意见
     */
    private void showOpinion() {
        HttpRestClient.doHttpShowOpinion(conId + "", new AsyncHttpResponseHandler(getActivity()) {
            JSONObject object;

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                ToastUtil.showShort("下载完成");
                super.onFinish();
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
            }

            @Override
            public void onSuccess(int statusCode, String content) {
                try {
                    object = new JSONObject(content);
                    if (object.has("errormessage")) {
                        ToastUtil.showShort(object.optString("errormessage"));
                    } else {
                        str = object.optString("EXID");
                        mReadEdit.setText(object.optString("CONTENT"));
                        ISCOM = object.optInt("ISCOM");
                        mOrder.setVisibility(View.VISIBLE);
                        if (ISCOM == 0) {
                            mEvaluabe.setVisibility(View.VISIBLE);
                        } else if (ISCOM == 1) {
                            mEvaluabe.setVisibility(View.GONE);
                            mHaveEvaluate.setVisibility(View.VISIBLE);
                        }
                        JSONArray picsArray = object.getJSONArray("PICS");
                        list = new ArrayList<HashMap<String, String>>();
                        for (int t = 0; t < picsArray.length(); t++) {
                            JSONObject obj = picsArray.getJSONObject(t);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("SMALL", obj.optString("SMALL"));
                            map.put("SEQ", "" + obj.optInt("SEQ"));
                            LogUtil.e("SEQ", "" + obj.optInt("SEQ"));
                            LogUtil.e("SMALL", obj.optString("SMALL"));
                            list.add(map);
                        }
                        mAdapter = new ImageGridAdapter(mActivity.getApplicationContext(), list);
                        mGridView.setAdapter(mAdapter);
                        mGridView.setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String[] array = new String[list.size()];
                                for (int i = 0; i < list.size(); i++) {
                                    array[i] = list.get(i).get("SMALL").toString().replace("-small", "");
                                }
                                Intent intent = new Intent(getActivity(), ImageGalleryActivity.class);
                                intent.putExtra(ImageGalleryActivity.URLS_KEY, array);
                                intent.putExtra(ImageGalleryActivity.TYPE_KEY, 1);// 0,1单个,多个
                                intent.putExtra("position", position);
                                intent.putExtra("type", 1);// 0,1单个,多个
                                startActivity(intent);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onSuccess(statusCode, content);
            }
        });
    }

    /**
     * 专家查看会诊意见
     */
    private void expertShowOpinion() {
        HttpRestClient.doHttpShowOpinion(conId + "", new AsyncHttpResponseHandler(getActivity()) {
            JSONObject object;

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                ToastUtil.showShort("下载完成");
                super.onFinish();
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
            }

            @Override
            public void onSuccess(int statusCode, String content) {
                try {
                    object = new JSONObject(content);
                    if (object.has("errormessage")) {
                        ToastUtil.showShort(object.optString("errormessage"));
                    } else {
                        mReadEdit.setText(object.optString("CONTENT"));
                        JSONArray picsArray = object.getJSONArray("PICS");
                        list = new ArrayList<HashMap<String, String>>();
                        for (int t = 0; t < picsArray.length(); t++) {
                            JSONObject obj = picsArray.getJSONObject(t);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("SMALL", obj.optString("SMALL"));
                            map.put("SEQ", "" + obj.optInt("SEQ"));
                            LogUtil.e("SEQ", "" + obj.optInt("SEQ"));
                            LogUtil.e("SMALL", obj.optString("SMALL"));
                            list.add(map);
                        }
                        mAdapter = new ImageGridAdapter(mActivity.getApplicationContext(), list);
                        mGridView.setAdapter(mAdapter);
                        mGridView.setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String[] array = new String[list.size()];
                                for (int i = 0; i < list.size(); i++) {
                                    array[i] = list.get(i).get("SMALL").toString().replace("-small", "");
                                }
                                Intent intent = new Intent(getActivity(), ImageGalleryActivity.class);
                                intent.putExtra(ImageGalleryActivity.URLS_KEY, array);
                                intent.putExtra(ImageGalleryActivity.TYPE_KEY, 1);// 0,1单个,多个
                                intent.putExtra("position", position);
                                intent.putExtra("type", 1);// 0,1单个,多个
                                startActivity(intent);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onSuccess(statusCode, content);
            }
        });
    }

    // 将文件放入参数
    private void putFile(RequestParams params) {
        images = Bimp.dataMap.get(DFgtConsultReport.class.getSimpleName());// 根据ID寻找
        for (int i = 0; i < images.size(); i++) {
            int index = i + 1;
            try {
                params.put(index + ".jpg", BitmapUtils.onGetDecodeFileByPath(getActivity(),images.get(i).getImagePath()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 释放
     */
    public void release() {
        if (mIat!=null)
        {
            mIat.cancel();
            mIat.destroy();
        }
    }

    /**
     * 点击取消上传的监听
     */
    class CancelClickListener implements OnClickSureBtnListener {

        @Override
        public void onClickSureHander() {

        }

    }


    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());
        mEditText.setText(mEditText.getText() + text);
        int len = mEditText.getText().length();
        mEditText.setSelection(len);
    }


    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

}
