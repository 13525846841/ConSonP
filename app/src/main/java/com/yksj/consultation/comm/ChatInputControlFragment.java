package com.yksj.consultation.comm;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.dmsj.newask.http.LodingFragmentDialog;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.yksj.consultation.adapter.QuickReplyChattingAdapter;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog.OnDilaogClickListener;
import com.yksj.consultation.comm.FacePanelFragment.FaceItemOnClickListener;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.AppTools;
import com.yksj.consultation.son.app.BaiduLocationManager;
import com.yksj.consultation.son.chatting.ChatActivity;
import com.yksj.consultation.son.chatting.QuickReplyChattingActivity;
import com.yksj.consultation.son.consultation.avchat.team.Container;
import com.yksj.consultation.son.consultation.avchat.team.ModuleProxy;
import com.yksj.consultation.son.consultation.avchat.team.TeamAVChatAction;
import com.yksj.consultation.son.consultation.avchat.team.TeamAVChatHelper;
import com.yksj.consultation.son.views.VUMeterView;
import com.yksj.healthtalk.media.ArmMediaPlay;
import com.yksj.healthtalk.media.ArmMediaRecord;
import com.yksj.healthtalk.media.ArmMediaRecord.ArmMediaRecordListener;
import com.yksj.healthtalk.media.ArmMediaRecord.MediaState;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.JsonHttpResponseHandler;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.net.socket.SmartControlClient;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.BitmapUtils;
import com.yksj.healthtalk.utils.FaceParse;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.ViewUtils;

import org.cropimage.CropUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.ImageLoader;
import org.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 聊天控制区域
 *
 * @author jack_tang
 */
public class ChatInputControlFragment extends Fragment implements OnClickListener, FaceItemOnClickListener, ArmMediaRecordListener, OnItemClickListener, BaiduLocationManager.LocationListenerCallBack, ModuleProxy {

    private final static String TAG = ChatInputControlFragment.class.getSimpleName();
    public final static int REQUEST_CAMERA_CODE = 2000;//相机
    public final static int REQUEST_FILE_CODE = 2001;//相册
    private static final int RECODE_FLAG = 1000;

    EditText mEditText;
    TextView mNumbTxtV;
    VUMeterView mChatVm;
    View mFacePanelV;
    View mChatBoxPanelV;
    View mTxtPanelV;
    View mRecordPanelV;
    //	View mTxtSelectorBtnV;//文字切换
//	View mVoiceSelectorBtnV;//语音切换
    View mDeletePanelV;//删除界面
    View mInputPanelV;//消息输入区域面板
    Button mDeletBtn;
    //    LinearLayout mQuickButton;//快速回复按钮
    Button mSpeakButton;//语音
    Button mTxtButton;//键盘

    CheckBox mArrowCheckBox;
    ChatInputControlListener mInputControlListener;
    ArmMediaPlay mediaPlay;
    ArmMediaRecord mediaRecord;
    File mChatImgeFile;//当前相册或相机调用返回的图片
    private QuickReplyChattingAdapter quickReplyChattingAdapter;
    private PopupWindow mPopupWindow;//快速聊天pop
    private boolean isGroup = false;

    @Override
    public void locationListenerCallBackFaile() {
        onShowResultDialog(R.string.request_location_fail);
    }

    @Override
    public void locationListenerCallBack(final double longitude, final double latitude) {
        HttpRestClient.doHttpQueryMapAddress(String.valueOf(longitude), String.valueOf(latitude), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                super.onSuccess(statusCode, response);
                try {
                    String state = response.getString("status");
                    if ("OK".equalsIgnoreCase(state)) {
                        JSONArray jsonArray2 = response.getJSONArray("results");
                        response = jsonArray2.getJSONObject(0);
                        String locationAddrs = response.getString("formatted_address");
                        onSendLocationMessage(String.valueOf(longitude), String.valueOf(latitude), locationAddrs);
                    }
                } catch (JSONException e) {
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                onDialogDismisss();
            }
        });
    }

    @Override
    public boolean sendMessage(IMMessage msg) {
        return false;
    }

    @Override
    public void onInputPanelExpand() {

    }

    @Override
    public void shouldCollapseInputPanel() {

    }

    @Override
    public boolean isLongClickEnabled() {
        return false;
    }

    @Override
    public void onItemFooterClick(IMMessage message) {

    }

    public interface ChatInputControlListener {
        void onSendTxtMesg(String content);

        void onSendLocationMesg(String longitude, String latitude, String address);

        void onSendImageMesg(String miniNmae, String bigName);

        void onSendVideoMesg(String video);

        void onSendVoiceMesg(String path, String timeStr, int time);

        void onSelectAll();//全选

        void onDeletAll();//删除所有

        void onDeletSelect();//删除选中

        void onDeletCancle();//删除选中
    }


    private Context context;
    private FaceParse mChatFaceParse;
    Button mSendMesg;//发送消息

    @Override
    public void onAttach(Activity activity) {
        if (activity instanceof ChatInputControlListener) {
            mInputControlListener = (ChatInputControlListener) activity;
        }
        ChatActivity chatActivity = (ChatActivity) activity;
        this.mediaPlay = chatActivity.mediaPlay;
        this.mediaRecord = chatActivity.mediaRecord;
        this.mediaRecord.setmRecordListener(this);
        context = chatActivity;
        mChatFaceParse = FaceParse.getChatFaceParse(activity);
        quickReplyChattingAdapter = new QuickReplyChattingAdapter(getActivity(), 1);
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ChatActivity chatActivity = (ChatActivity) getActivity();
        mChatVm = chatActivity.mChatVm;
        if (SmartControlClient.helperId.equals(chatActivity.mChatId)) {
            mArrowCheckBox.setVisibility(View.GONE);
        }
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.chat_control_layout, null);
        view.findViewById(R.id.face_btn).setOnClickListener(this);
//		view.findViewById(R.id.chat_voice_btn).setOnClickListener(this);
//		view.findViewById(R.id.chat_txt_btn).setOnClickListener(this);
        view.findViewById(R.id.chat_photo_btn).setOnClickListener(this);
        view.findViewById(R.id.chat_camera_btn).setOnClickListener(this);
        view.findViewById(R.id.chat_video_btn).setOnClickListener(this);
        view.findViewById(R.id.chat_location_btn).setOnClickListener(this);
//		view.findViewById(R.id.chat_delete_btn1).setOnClickListener(this);
//		view.findViewById(R.id.chat_delete_btn2).setOnClickListener(this);
        view.findViewById(R.id.chat_delete_cancle).setOnClickListener(this);
//        mQuickButton = (LinearLayout) view.findViewById(R.id.quick_button);
//        mQuickButton.setVisibility(View.GONE);
        mSpeakButton = (Button) view.findViewById(R.id.chat_send_btn);
        mTxtButton = (Button) view.findViewById(R.id.chat_text_input_btn);
//        mQuickButton.setOnClickListener(this);
        mSpeakButton.setOnClickListener(this);
        mTxtButton.setOnClickListener(this);
        mNumbTxtV = (TextView) view.findViewById(R.id.chat_input_size_tv);
        mDeletBtn = (Button) view.findViewById(R.id.chat_delete_btn3);
        mDeletBtn.setOnClickListener(this);
        mSendMesg = (Button) view.findViewById(R.id.send_message);
        mSendMesg.setOnClickListener(this);
        mInputPanelV = view.findViewById(R.id.input_mesg_panel);
        mDeletePanelV = view.findViewById(R.id.delete_panel);
        mTxtPanelV = view.findViewById(R.id.txt_panel);
        mRecordPanelV = view.findViewById(R.id.recod_panel);

//		mTxtSelectorBtnV = view.findViewById(R.id.selector_panel_txt);//文字切换
//		mVoiceSelectorBtnV = view.findViewById(R.id.selector_panel_voice);

        mEditText = (EditText) view.findViewById(R.id.chat_edit);
        mArrowCheckBox = (CheckBox) view.findViewById(R.id.chat_arrow_btn);
        mArrowCheckBox.setOnClickListener(this);
        mChatBoxPanelV = view.findViewById(R.id.chat_box_panel);
        mFacePanelV = view.findViewById(R.id.face_panel);
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideAllPanel();
            }
        });
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAllPanel();
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int len = 1000 - s.length();
                mNumbTxtV.setText(len == 1000 ? "" : String.valueOf(len));
                if (s.length() == 0) {
                    mSendMesg.setVisibility(View.GONE);
                    mArrowCheckBox.setVisibility(View.VISIBLE);

                } else {
                    mArrowCheckBox.setVisibility(View.GONE);
                    mSendMesg.setVisibility(View.VISIBLE);
                }
            }
        });


        mEditText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event != null && KeyEvent.ACTION_DOWN == event.getAction() && keyCode == KeyEvent.KEYCODE_ENTER) {
                    //登陆
                    onSendTxtMessage();
                    return true;
                } else
                    return false;
            }
        });


        mEditText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_SEND) {
                    //登陆
                    onSendTxtMessage();
                    return true;
                }
                return false;
            }
        });

        //录音
        view.findViewById(R.id.chat_recod_btn).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        if (event.getY() < -10) {
                            mediaRecord.changeCancelState(true);
                        } else {
                            mediaRecord.changeCancelState(false);
                        }
                        break;
                    case MotionEvent.ACTION_DOWN:
                        if (!StorageUtils.isSDMounted()) {
                            return true;
                        }
                        File file;
                        try {
                            file = StorageUtils.createVoiceFile();
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtil.showCreateFail();
                            return true;
                        }
                        mediaRecord.start(file);
                        break;
                    case MotionEvent.ACTION_UP:
                        mediaRecord.stop();
                        break;
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        FacePanelFragment facePanelFragment = (FacePanelFragment) getChildFragmentManager().findFragmentByTag("face_fragment");
        facePanelFragment.setmFaceItemOnClickListener(this);
        super.onStart();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CAMERA_CODE://相机获取
                if (resultCode == Activity.RESULT_OK) {
                    onSendImagMessage(mChatImgeFile.getAbsolutePath());
                }
                mChatImgeFile = null;
                break;
            case RECODE_FLAG://录制视频获取
                if (resultCode == Activity.RESULT_OK) {
                    String result = data.getExtras().getString("filePath");
                    onSendVideoMessage(result);
                }
                mChatImgeFile = null;
                break;
            case REQUEST_FILE_CODE://文件获取
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        Uri uri = data.getData();
                        String strFilePath = getImageAbsolutePath(getActivity(), uri);
//                        String scheme = uri.getScheme();
//                        String strFilePath;//图片地址
//                        // url类型content or file
//                        if ("content".equals(scheme)) {
//                            strFilePath = getImageUrlByAlbum(uri);
//                        } else {
//                            strFilePath = uri.getPath();
//                        }
                        onSendImagMessage(strFilePath);
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.face_btn://表情点击
                onFaceBtnClick(v);
                break;
            case R.id.chat_arrow_btn://箭头点击
                onArrowCheckBoxClick((CheckBox) v);
                break;
            case R.id.chat_send_btn://语音切换
//			onSendTxtMessage();
//			mTxtSelectorBtnV.setVisibility(View.VISIBLE);
//			mVoiceSelectorBtnV.setVisibility(View.GONE);
//			mTxtPanelV.setVisibility(View.GONE);
                SystemUtils.hideSoftBord(getActivity(), mEditText);
                mEditText.setVisibility(View.GONE);
//                mQuickButton.setVisibility(View.GONE);
                mRecordPanelV.setVisibility(View.VISIBLE);
                mChatBoxPanelV.setVisibility(View.GONE);
                mTxtButton.setVisibility(View.VISIBLE);
                mSpeakButton.setVisibility(View.GONE);
                hideAllPanel();
                break;
            case R.id.chat_text_input_btn://键盘切换
                mEditText.setVisibility(View.VISIBLE);
//                mQuickButton.setVisibility(View.VISIBLE);
                mRecordPanelV.setVisibility(View.GONE);
                mChatBoxPanelV.setVisibility(View.GONE);
                mSpeakButton.setVisibility(View.VISIBLE);
                mTxtButton.setVisibility(View.GONE);
                hideAllPanel();
                break;
//		case R.id.chat_voice_btn://语音切换
//			mTxtSelectorBtnV.setVisibility(View.VISIBLE);
////			mVoiceSelectorBtnV.setVisibility(View.GONE);
//			mTxtPanelV.setVisibility(View.GONE);
//			mRecordPanelV.setVisibility(View.VISIBLE);
//			mChatBoxPanelV.setVisibility(View.GONE);
//			hideAllPanel();
//			break;
//		case R.id.chat_txt_btn://文字选择
//			mTxtPanelV.setVisibility(View.VISIBLE);
////			mTxtSelectorBtnV.setVisibility(View.GONE);
////			mVoiceSelectorBtnV.setVisibility(View.VISIBLE);
//			mRecordPanelV.setVisibility(View.GONE);
//			mChatBoxPanelV.setVisibility(View.GONE);
//			showSoftBord();
//			hideAllPanel();
//			break;
            case R.id.chat_photo_btn://照片
                hideAllPanel();
                onPhotoClick(v);
                break;
            case R.id.chat_video_btn://视频
//                startActivityForResult(new Intent(getActivity(), RecordMadeAty.class), RECODE_FLAG);//小视频
                getGroupData(groupId);
//                ArrayList<String> selectedAccounts=new ArrayList<>();
//                selectedAccounts.add("15028760690");
//                selectedAccounts.add("wshwwzbj");
////                onCreateRoomSuccess(list);
//
//
//
//
//
//                // 定制加号点开后可以包含的操作， 默认已经有图片，视频等消息了
//                final TeamAVChatAction avChatAction = new TeamAVChatAction(AVChatType.VIDEO);
//                Container container = new Container(getActivity(), "136114919", SessionTypeEnum.Team, this);
//                avChatAction.setContainer(container);
//                TeamAVChatHelper.sharedInstance().registerObserver(true);
//                avChatAction.onSelectedAccountsResult(selectedAccounts);
                //// TODO: 17/9/26 视频通话
                break;
            case R.id.chat_camera_btn://拍照
                hideAllPanel();
                onCameraClick(v);
                break;
            case R.id.chat_location_btn://地图
                hideAllPanel();
                onLocationClick(v);
                break;
//		case R.id.chat_delete_btn1://全选
//			if(mInputControlListener != null)mInputControlListener.onSelectAll();
//			break;
//		case R.id.chat_delete_btn2://删除所有
//			if(mInputControlListener != null)mInputControlListener.onDeletAll();
//			break;
            case R.id.chat_delete_btn3://删除选中
                if (mInputControlListener != null) mInputControlListener.onDeletSelect();
                break;
            case R.id.quick_button://快速回复
                /**
                 * 旋转动画
                 * @param toDegrees
                 * @param pivotXValue
                 * @param pivotYValue
                 * @return
                 */
//                AnimationUtils.startRotateAnimation(mQuickButton, 0, 180);
                ViewUtils.setGone(mFacePanelV, true);
                ViewUtils.setGone(mChatBoxPanelV, true);
                showChattingQuickReplyPop(context, v);

                break;
            case R.id.setting://快速回复编辑
                if (mPopupWindow != null) mPopupWindow.dismiss();
                Intent intent = new Intent(context, QuickReplyChattingActivity.class);
                startActivity(intent);
                break;
            case R.id.send_message://发送消息
                onSendTxtMessage();
                break;
            case R.id.chat_delete_cancle://退出编辑模式
                if (mInputControlListener != null) mInputControlListener.onDeletCancle();
//			onSendTxtMessage();
                break;
        }
    }

    /**
     * 快速聊天pop
     *
     * @param context
     */
    private ListView listview;

    public void showChattingQuickReplyPop(final Context context, final View parent) {
        SystemUtils.hideSoftBord(getActivity(), mEditText);
        if (mPopupWindow == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.chat_quick_pop_layout, null);

            mPopupWindow = new PopupWindow(view, mEditText.getWidth(), AppTools.getWindowSize(getActivity()).heightPixels / 2);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setFocusable(true);
            mPopupWindow.setTouchable(true);
            view.findViewById(R.id.setting).setOnClickListener(this);
            listview = (ListView) view.findViewById(R.id.listview);
        } else if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            return;
        }
        listview.setAdapter(quickReplyChattingAdapter);
        mPopupWindow.setAnimationStyle(R.style.AnimationPreview);
        mPopupWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
//                AnimationUtils.startRotateAnimation(mQuickButton, 180, 360);
                if (!HStringUtil.isEmpty(mEditText.getText().toString())) {
                    mEditText.setSelection(mEditText.getText().toString().length());
                }
                showSoftBord();
            }
        });
        listview.setOnItemClickListener(ChatInputControlFragment.this);
        final View inputView = ((Activity) context).findViewById(R.id.chat_input_panel);

        RequestParams params = new RequestParams();
        params.put("Type", "queryQuick");
        params.put("CUSTOMER_ID", SmartFoxClient.getLoginUserId());
        HttpRestClient.doHttpQUICKREPLYSERVLET(params, new JsonHttpResponseHandler(getActivity()) {
            @Override
            public void onSuccess(int statusCode, JSONArray response) {
                super.onSuccess(statusCode, response);
                quickReplyChattingAdapter.onBountData(response);
//				 int x =AppTools.getWindowSize(getActivity()).widthPixels/2;
//				AppTools.getWindowSize(getActivity()).widthPixels / 2 -mEditText.getWidth();
                int[] location = new int[2];
                mEditText.getLocationOnScreen(location);
                int x = location[0];
                int y = location[1];
                mPopupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.LEFT, x, inputView.getHeight() - 1);
            }
        });
    }


    @Override
    public void onItemClick(String text, Drawable drawable, FaceParse mFaceParse) {
        mFaceParse.insertToEdite(mEditText, drawable, text);
    }

    @Override
    public void onRecordError(ArmMediaRecord record, int error) {//录音错误
        switch (error) {
            case MediaState.ERROR_SHORT:
                ToastUtil.showToastPanl("录音时间太短");
                break;
            case MediaState.ERROR_UNKNOWN:
                ToastUtil.showToastPanl("录音错误");
                mChatVm.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onRecordStateChnage(int state) {//录音状态改变
        switch (state) {
            case MediaState.STATE_IDLE:
                mChatVm.setVisibility(View.GONE);
                break;
            case MediaState.STATE_START:
                mChatVm.setVisibility(View.VISIBLE);
                break;
            case MediaState.STATE_PARE:
                mChatVm.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 录音完成
     */
    @Override
    public void onRecordOver(ArmMediaRecord record, File file, String time,
                             long durationTime) {//1410598912.0
        if (mInputControlListener != null) {
            mInputControlListener.onSendVoiceMesg(file.getAbsolutePath(), time, (int) durationTime);
        }
    }

    /**
     * 编辑模式
     *
     * @param b
     */
    public void onChangeEditorMode(boolean b) {
        if (b) {//编辑模式下
            hideSoftBord();
            mInputPanelV.setVisibility(View.GONE);
            mDeletePanelV.setVisibility(View.VISIBLE);
        } else {//非编辑模式下
            mInputPanelV.setVisibility(View.VISIBLE);
            mDeletePanelV.setVisibility(View.GONE);
        }
    }


    /**
     * 购买服务
     */
    private void showPayDialog() {
        DoubleBtnFragmentDialog.showDefault(getFragmentManager(),
                "对不起！您需要购买该医生的服务才能与医生进行对话，也可以到该医生的公告板给医生留言",
                "知道了",
                "去购买",
                new OnDilaogClickListener() {
                    @Override
                    public void onDismiss(DialogFragment fragment) {
                    }

                    //去购买
                    @Override
                    public void onClick(DialogFragment fragment, View v) {
                    }
                });
    }

    /**
     * 更新选中个数
     *
     * @param size
     */
    public void onUpdateSelectedNumber(int size) {
        mDeletBtn.setText("删除选中(" + size + ")");
    }

    /**
     * 点击照片,相册获取
     *
     * @param v
     */
    private void onPhotoClick(View v) {
        Intent intent = CropUtils.createPickForFileIntent();
        startActivityForResult(intent, REQUEST_FILE_CODE);
    }

    /**
     * 点击相机
     *
     * @param v
     */
    private void onCameraClick(View v) {
        try {
            mChatImgeFile = StorageUtils.createImageFile();
            Uri outUri = Uri.fromFile(mChatImgeFile);
            Intent intent = CropUtils.createPickForCameraIntent(outUri);
            startActivityForResult(intent, REQUEST_CAMERA_CODE);
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.showCreateFail();
        }
    }

    /**
     * 点击地图
     *
     * @param v
     */
    BaiduLocationManager mBaiduManager;

    private void onLocationClick(View v) {
        if (mBaiduManager == null) {
//			BaiduLocationManager.init(getActivity());
            mBaiduManager = BaiduLocationManager.getInstance(getActivity());
            mBaiduManager.setCallBack(this);

        }
        mBaiduManager.startLocation();
        onShowLodingDialog(R.string.request_location);
    }


    /**
     * 所有的面板是否隐藏
     *
     * @return
     */
    public boolean isAllPanelGone() {
        if (mFacePanelV.getVisibility() == View.VISIBLE
                || mChatBoxPanelV.getVisibility() == View.VISIBLE) {
            mArrowCheckBox.setChecked(true);
            mFacePanelV.setVisibility(View.GONE);
            mChatBoxPanelV.setVisibility(View.GONE);
            return false;
        }
        return true;
    }

    /**
     * 隐藏所有的面板
     */
    private void hideAllPanel() {
        mArrowCheckBox.setChecked(true);
        mChatBoxPanelV.setVisibility(View.GONE);
        mFacePanelV.setVisibility(View.GONE);
    }

    /**
     * 表情点击
     */
    private void onFaceBtnClick(View v) {
        mEditText.setVisibility(View.VISIBLE);
//		mSpeakButton.setVisibility(View.GONE);
        mRecordPanelV.setVisibility(View.GONE);
        if (mFacePanelV.getVisibility() == View.GONE) {
            hideSoftBord();
            mChatBoxPanelV.setVisibility(View.GONE);
            mFacePanelV.setVisibility(View.VISIBLE);
        } else {
            mFacePanelV.setVisibility(View.GONE);
        }
    }

    /**
     * 箭头点击
     */
    private void onArrowCheckBoxClick(CheckBox checkBox) {
        if (checkBox.isChecked()) {
            mChatBoxPanelV.setVisibility(View.GONE);
            mEditText.setVisibility(View.VISIBLE);
//            mQuickButton.setVisibility(View.VISIBLE);
            mRecordPanelV.setVisibility(View.GONE);

//			mTxtSelectorBtnV.setVisibility(View.GONE);
//			mVoiceSelectorBtnV.setVisibility(View.VISIBLE);
            showSoftBord();
            hideAllPanel();
        } else {
            hideSoftBord();
            mFacePanelV.setVisibility(View.GONE);
            mChatBoxPanelV.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏键盘
     */
    public void hideSoftBord() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        /*try {
            Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
    }

    /**
     * 显示键盘
     */
    private void showSoftBord() {
        mEditText.setFocusable(true);
        mEditText.requestFocus();
//		InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//		inputMethodManager.showSoftInput(mEditText,0);
//		try {
//			Thread.sleep(300);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
        SystemUtils.showSoftMode(mEditText);
    }

    /**
     * 发送图片
     *
     * @param path
     */
    private void onSendImagMessage(String path) {
        if (StorageUtils.isSDMounted()) {//内存卡处于加载状态
            //大文件
            Bitmap bitmap = BitmapUtils.decodeBitmap(path, BitmapUtils.POTO_MAX_SILDE_SIZE, BitmapUtils.POTO_MAX_SILDE_SIZE);
            if (bitmap == null) return;
            //创建文件
            try {
                File bigFile = StorageUtils.createImageFile();
                if (bigFile == null) return;
                File miniFile = StorageUtils.createImageFile();
                if (miniFile == null) return;
                ImageLoader loader = ImageLoader.getInstance();
                File bigFile2 = loader.getOnDiscFileName(bigFile.getParentFile(), bigFile.getName());
                File miniFile2 = loader.getOnDiscFileName(miniFile.getParentFile(), miniFile.getName());
                StorageUtils.saveImageOnImagsDir(bitmap, bigFile2);
                if (bitmap != null && !bitmap.isRecycled()) bitmap.recycle();
                //小文件
                bitmap = BitmapUtils.decodeBitmap(bigFile2.getAbsolutePath(), 100, 100);
                StorageUtils.saveImageOnImagsDir(bitmap, miniFile2);
                if (bitmap != null && !bitmap.isRecycled()) bitmap.recycle();
                if (mInputControlListener != null)
                    mInputControlListener.onSendImageMesg(miniFile.getName(), bigFile.getName());
            } catch (IOException e) {
                e.printStackTrace();
                ToastUtil.showCreateFail();
                return;
            }
        }
    }

    /**
     * 发送视频
     *
     * @param path
     */
    private void onSendVideoMessage(String path) {
        if (StorageUtils.isSDMounted()) {//内存卡处于加载状态
            //创建文件
            if (mInputControlListener != null) {
                mInputControlListener.onSendVideoMesg(path);
            }
        }
    }

    /**
     * 地图发送
     *
     * @param longitude
     * @param latitude
     */
    private void onSendLocationMessage(String longitude, String latitude, String address) {
        if (mInputControlListener != null)
            mInputControlListener.onSendLocationMesg(longitude, latitude, address);
    }

    /**
     * 发送文字
     */
    private void onSendTxtMessage() {
        String content = mEditText.getEditableText().toString();
        if (content.length() != 0) {
            mEditText.setText(null);
            if (mInputControlListener != null)
                mInputControlListener.onSendTxtMesg(content);
        }
    }

    /**
     * 设置聊天类型
     */
    public void setChatType(boolean isGroup, String id, String roomName, boolean canTalk) {
        this.isGroup = isGroup;
        this.groupId = id;
        this.roomName = roomName;
        if (canTalk) {
            if (isGroup) {
                view.findViewById(R.id.selector_panel_video).setVisibility(View.VISIBLE);
            } else {
                view.findViewById(R.id.selector_panel_video).setVisibility(View.GONE);
            }
        } else {
            ((Activity) context).findViewById(R.id.chat_input_panel).setVisibility(View.GONE);
        }

    }

    /**
     * 根据uri查询相册所对应的图片地址
     *
     * @param uri
     * @return
     */
    public String getImageUrlByAlbum(Uri uri) {
        String[] imageItems = {MediaColumns.DATA};
        Cursor cursor = context.getContentResolver().query(uri, imageItems,
                null, null, null);
//		Cursor cursor = getActivity().managedQuery(uri, imageItems, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columIndex = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
            String ImagePath = cursor.getString(columIndex);
            cursor.close();
            return ImagePath;
        }

        return uri.toString();
//		int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//		cursor.moveToFirst();
//		String path = cursor.getString(index);
//		return path;
    }

    private void onShowLodingDialog(int resid) {
        if (!isDetached()) {
            LodingFragmentDialog.showLodingDialog(getFragmentManager(), getString(resid));
        }
    }

    private void onShowResultDialog(int resid) {
        if (!isDetached()) {
            String content = getString(resid);
            ResultFragmtDialog.showFailDialog(getFragmentManager(), content);
        }
    }

    private void onDialogDismisss() {
        if (!isDetached()) {
            LodingFragmentDialog.dismiss(getFragmentManager());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (mPopupWindow != null) mPopupWindow.dismiss();
        JSONObject object;
        try {
            object = (JSONObject) quickReplyChattingAdapter.jsonArray.get(position);
            mEditText.append(mChatFaceParse.parseSmileTxt(object.optString("QUICK_REPLY_CONTENT")));
        } catch (JSONException e) {
        }

    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param imageUri
     * @author yaoxing
     * @date 2014-10-12
     */
    @TargetApi(19)
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private String groupId = "";
    private ArrayList<String> accounts;//视频通话账号
    private String roomName;//视频通话房间名字

    private void onCreateRoomSuccess(ArrayList<String> accounts) {
        // 定制加号点开后可以包含的操作， 默认已经有图片，视频等消息了
        final TeamAVChatAction avChatAction = new TeamAVChatAction(AVChatType.VIDEO);
        Container container = new Container(getActivity(), groupId, SessionTypeEnum.ChatRoom, this);
        TeamAVChatHelper.sharedInstance().registerObserver(true);
        avChatAction.setContainer(container);
        avChatAction.onSelectedAccountsResult(accounts, groupId);
    }

    private ArrayList<String> list = null;

    /**
     * 获取群资料
     */
    private void getGroupData(final String groupId) {
        if (HStringUtil.isEmpty(groupId)) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("group_id", groupId);
        map.put("op", "queryGroupPerson");
        HttpRestClient.OKHttpGetFriends(map, new HResultCallback<String>(getActivity()) {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);

                if (!HStringUtil.isEmpty(response)) {
                    try {
                        list = new ArrayList<String>();
                        JSONObject object = new JSONObject(response);
                        if ("1".equals(object.optString("code")) && LoginServiceManeger.instance().getLoginEntity() != null) {
                            String customerAccount = LoginServiceManeger.instance().getLoginEntity().getSixOneAccoutn();
                            roomName = object.getJSONObject("result").optString("record_name");
                            JSONArray array = object.getJSONObject("result").getJSONArray("groupPerson");
                            int count = array.length();
                            if (count > 0) {
                                for (int i = 0; i < count; i++) {
                                    if (!customerAccount.equals(array.getJSONObject(i).optString("CUSTOMER_ACCOUNTS"))) {
                                        list.add(array.getJSONObject(i).optString("CUSTOMER_ACCOUNTS"));
                                    }
                                }
                            }
                            onCreateRoomSuccess(list);
                        } else {
                            ToastUtil.showShort(object.optString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, this);
    }
}
