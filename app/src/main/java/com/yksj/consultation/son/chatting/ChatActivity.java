package com.yksj.consultation.son.chatting;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.ChatAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.ChatInputControlFragment;
import com.yksj.consultation.comm.ChatInputControlFragment.ChatInputControlListener;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog.OnDilaogClickListener;
import com.yksj.consultation.comm.ImageGalleryActivity;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.AppData;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.bean.MyConstant;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.consultation.son.consultation.bean.ObjectType;
import com.yksj.consultation.son.consultation.main.OrderActivity;
import com.yksj.consultation.son.friend.DoctorClinicMainActivity;
import com.yksj.consultation.son.setting.SettingWebUIActivity;
import com.yksj.consultation.son.views.VUMeterView;
import com.yksj.healthtalk.db.ChatUserHelper;
import com.yksj.healthtalk.entity.GroupInfoEntity;
import com.yksj.healthtalk.entity.MessageEntity;
import com.yksj.healthtalk.media.ArmMediaPlay;
import com.yksj.healthtalk.media.ArmMediaPlay.ArmMediaPlayListener;
import com.yksj.healthtalk.media.ArmMediaRecord;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.BinaryHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.http.ObjectHttpResponseHandler;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.net.socket.SmartControlClient;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.services.CoreService;
import com.yksj.healthtalk.services.CoreService.CoreServiceBinder;
import com.yksj.healthtalk.utils.BitmapUtils;
import com.yksj.healthtalk.utils.DataParseUtil;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.LogUtil;
import com.yksj.healthtalk.utils.SharePreUtils;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.ToastUtil;

import org.apache.http.message.BasicNameValuePair;
import org.cropimage.CropUtils;
import org.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 聊天UI
 *
 * @author jack_tang
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class ChatActivity extends BaseFragmentActivity implements OnClickListener, ChatInputControlListener, ArmMediaPlayListener {


//	private final int TOP_REFRESH = 1;
//	private final int BOTTOM_REFRESH = 2;


    private boolean isGroupChat = false;//是否是群聊
    //    private boolean isThreeChat = false;//是否是三人群聊
    private String allCustomerId;//三个人的id拼的字符串
    private RelativeLayout memberLayout;//群聊成员布局
    //	private boolean isPayed = true;//是否已经支付


    public VUMeterView mChatVm;
    private PopupWindow mPopupWindow;
    private PopupWindow mOptionWindow;


    ChatInputControlFragment mChatInputControlFragment;
    GroupInfoEntity mGroupInfoEntity;
//    CustomerInfoEntity mCustomerInfoEntity;

    View mChatMenuV;
    MessageEntity mPlayingEntity;
    ListView mListView;
    ChatAdapter mChatAdapter;
    PullToRefreshListView mPullToRefreshListView;

    CoreService mPushService;//后台服务对象

    File mChatBgFile;//聊天背景图片
    private boolean isFristLoad = true;//是否是第一次加载

    private ImageView mChatBgImageV;//聊天背景


    private final static String TAG = ChatActivity.class.getName();
    public final static String KEY_PARAME = "parame";
    public final static String KEY_CONTENT = "retgretting";
    public final static String CONSULTATION_ID = "consult_id";//会诊id
    public final static String CONSULTATION_TYPE = "consult_type";//会诊id
    public final static String CONSULTATION_NAME = "consult_name";//会诊名称
    public final static String SINGLE_ID = "single_id";
    public final static String SINGLE_NAME = "single_name";
    public final static String GROUP_ID = "group_id";//群聊id
    public final static String OBJECT_TYPE = "object_type";//群聊id
    public final static String ORDER_ID = "order_id";//订单id
    public ArmMediaPlay mediaPlay;
    public ArmMediaRecord mediaRecord;
    ChatUserHelper mDbUserHelper;//数据库操作
    private ChatHandler mChatHandler;
    private Looper mLooper;
    AppData mAppData;
    private String consultationId = "";
    public String mChatId;//聊天id
    private String objectType = "";//单聊聊天类型 10 会诊 20 门诊预约 30 特殊服务
    private String mOrderId = "";//订单id
    private String mUserId;//登陆者id
    private int groupType = 0;// 0 单聊 1群聊 3特殊服务单聊
    boolean consultType = true;// false 不可以聊天 true 可以聊天


    //list view长按事件
    @SuppressWarnings("deprecation")
    final GestureDetector mGestureDetector = new GestureDetector(new ChatGestureListener() {
        public void onLongPress(MotionEvent e) {
            boolean isEditor = mChatAdapter.isEditor();
            if (isEditor) {
                mChatAdapter.onUnEditorMode();
            } else {
                mChatAdapter.onEditorMode();
            }
            mChatInputControlFragment.onChangeEditorMode(!isEditor);
        }

        ;
    });

    //后台服务绑定
    final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CoreServiceBinder binder = (CoreServiceBinder) service;
            mPushService = binder.getService();
            mChatHandler.sendEmptyMessage(1006);//发送一些转发的消息
        }
    };

    //注册通知
    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isFinishing()) return;
            String action = intent.getAction();
            if (action.equals(CoreService.ACTION_MESSAGE)) {//新消息
                String senderId = intent.getStringExtra("senderId");//发送者id
                if (mChatId.equals(senderId)) {//更新到界面上
                    mChatHandler.sendEmptyMessage(1000);
                    //弹出收费框
                }
                if (intent.hasExtra("order_id")) {
                    mOrderId = intent.getStringExtra("order_id");
                }
            } else if (CoreService.ACTION_OFFLINE_MESSAGE.equals(action)) {
                mChatHandler.sendEmptyMessage(1000);
            } else if (CoreService.ACTION_PAY_MESSAGE.equals(action)) {
                String mesgId = intent.getStringExtra(CoreService.PARAME_KEY);//消息id
                String note = intent.getStringExtra("tickNote");
                String senderId = intent.getStringExtra("senderId");//发送者id
                Bundle bundle = new Bundle();
                bundle.putString("note", note);
                bundle.putString("mesgId", mesgId);
                Message message = mChatHandler.obtainMessage();
                message.setData(bundle);
                message.what = 1007;
                mChatHandler.sendMessage(message);
            } else if (CoreService.MESSAGE_STATUS.equals(action)) {

            }
        }
    };

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
//        SDKInitializer.initialize(getApplicationContext());
        initData(bundle);
        setContentView(R.layout.chat_layout);
        initUI();
        initHistoryMesg();
    }

    /**
     * 初始化数据
     */
    private void initData(Bundle bundle) {
        HTalkApplication.getApplication().cancelNotify();

        HandlerThread handlerThread = new HandlerThread("chathandler");
        handlerThread.start();
        mLooper = handlerThread.getLooper();
        mChatHandler = new ChatHandler(mLooper);

        mDbUserHelper = ChatUserHelper.getInstance();

        //录音,播放初始化
        mediaPlay = new ArmMediaPlay();
        mediaRecord = new ArmMediaRecord();
        mediaPlay.setMediaPlayListener(this);
        mAppData = HTalkApplication.getAppData();
        mUserId = LoginServiceManeger.instance().getLoginUserId();

        if (bundle != null) {
            if (bundle.containsKey(ORDER_ID)) {
                mOrderId = bundle.getString(ORDER_ID);
            }
            if (bundle.containsKey("group_entity")) {
                mGroupInfoEntity = (GroupInfoEntity) bundle.getParcelable("group_entity");
                mChatId = mGroupInfoEntity.getId();
                consultationId = mChatId;
                isGroupChat = true;
            } else {
//                mCustomerInfoEntity = (CustomerInfoEntity) bundle.getSerializable("user_entity");
//                mChatId = mCustomerInfoEntity.getId();
//                objectType = mCustomerInfoEntity.getObjectType();
//                consultationId = mCustomerInfoEntity.getConsultationId();
                isGroupChat = false;
            }
        } else {
            //isPayed = getIntent().getBooleanExtra("isPayed",false);//是否已经支付
            Object object = getIntent().getParcelableExtra(KEY_PARAME);
            if (getIntent().hasExtra(OBJECT_TYPE)) {
                objectType = getIntent().getStringExtra(OBJECT_TYPE);
            }
            if (getIntent().hasExtra(ORDER_ID)) {
                mOrderId = getIntent().getStringExtra(ORDER_ID);
            }

            if (object instanceof GroupInfoEntity) {//群聊
                groupType = 1;
                mGroupInfoEntity = (GroupInfoEntity) object;
                mChatId = mGroupInfoEntity.getId();
                isGroupChat = true;
            } else if (getIntent().hasExtra(GROUP_ID)) {//会诊群聊
                groupType = 1;
                mChatId = getIntent().getStringExtra(GROUP_ID);
                consultationId = getIntent().getStringExtra(CONSULTATION_ID);
                isGroupChat = true;
            } else if (getIntent().hasExtra(MyConstant.SERVICE_CHAT)) {//特殊服务单聊
                groupType = 3;
                mChatId = getIntent().getStringExtra(MyConstant.SERVICE_CHAT);
                isGroupChat = false;
            } else {//会诊单聊
                mChatId = getIntent().getStringExtra(SINGLE_ID);
                objectType = getIntent().getStringExtra(OBJECT_TYPE);
                consultationId = getIntent().getStringExtra(CONSULTATION_ID);
                isGroupChat = false;
                if ("30".equals(objectType) || "10".equals(objectType)) {
                    groupType = 3;
                }
            }

        }
    }

    private void initUI() {
        initTitle();
        titleLeftBtn.setOnClickListener(this);
        mChatBgImageV = (ImageView) findViewById(R.id.chat_bg);
        mChatVm = (VUMeterView) findViewById(R.id.chat_vm);
        mChatVm.setMediaRecord(mediaRecord);
        if (getIntent().hasExtra(CONSULTATION_TYPE)) {
            consultType = false;
        }
//        if (isGroupChat) {//群聊
//            caseFlag = mGroupInfoEntity.getIsBL();
//            if ("1".equals(caseFlag)) {
//                titleRightBtn2.setVisibility(View.GONE);
//            } else {
//                titleRightBtn2.setVisibility(View.VISIBLE);
//                titleRightBtn2.setText("成员");
//                titleRightBtn2.setOnClickListener(this);
//            }
//        } else if (isThreeChat) {
//            memberLayout = (RelativeLayout) findViewById(R.id.chat_member_layout);
////            memberLayout.setVisibility(View.VISIBLE);
//            imgExpert = (ImageView) findViewById(R.id.chat_member_expert);
//            imgDoctor = (ImageView) findViewById(R.id.chat_member_doctor);
//            imgPatient = (ImageView) findViewById(R.id.chat_member_patient);
//            tvPatientName = (TextView) findViewById(R.id.chat_member_patient_name);
////            onLoadThreePersonInfo();
//        } else {
//            setRightMore(this);
//
//        }
        mChatInputControlFragment = (ChatInputControlFragment) getSupportFragmentManager().findFragmentById(R.id.input_control);

        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_listview);
        mPullToRefreshListView.setOnRefreshListener(mOnRefreshListener2);
        mListView = mPullToRefreshListView.getRefreshableView();
        mListView.setAdapter(mChatAdapter = new ChatAdapter(this, isGroupChat, mChatId, objectType));
        mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {//最后一条显示状态
                            mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);//始终滑动到最后
                        } else {
                            mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });

        if (isGroupChat) {
            if (getIntent().hasExtra(CONSULTATION_NAME)) {
                titleTextV.setText(getIntent().getStringExtra(CONSULTATION_NAME));
            } else if (mGroupInfoEntity != null) {
                titleTextV.setText(mGroupInfoEntity.getName());
            }
        } else if (getIntent().hasExtra(MyConstant.SERVICE_CHAT)) {
            String name = getIntent().getStringExtra(MyConstant.SERVICE_CHAT_NAME);
            if (!HStringUtil.isEmpty(name)) {
                titleTextV.setText(name);
            } else {
//                titleTextV.setText("未命名");
                titleTextV.setText(getIntent().getStringExtra(MyConstant.SERVICE_CHAT));
            }

        } else {
//            setRightMore(this, R.drawable.ig_delete);
//            if (mCustomerInfoEntity.isDoctor()) {
//                titleTextV.setText(mCustomerInfoEntity.getName() + "医生");
//                if (AppData.DYHSID.equals(mCustomerInfoEntity.getId()))
//                    titleTextV.setText("导医护士");
//            } else
            if (SmartFoxClient.helperId.equals(mChatId)) {
                titleTextV.setText("系统通知");//系统通知,不显显示输入框
                titleRightBtn2.setVisibility(View.INVISIBLE);
                findViewById(R.id.input_control).setVisibility(View.GONE);
            } else {
                titleTextV.setText(getIntent().getStringExtra(SINGLE_NAME));
            }
        }

        registerForContextMenu(mListView);
        //if(!isGroupChat)isPayedUser();
        //从支付宝页面过来的
//		if(getIntent().hasExtra("pay_type")){
//			SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "您可以咨询医生,如预约时间未到也可留言,离开此页面再次进入,请到\"我的六一健康-我的医生\"找该医生诊所");
//		}
        mChatInputControlFragment.setChatType(isGroupChat, mChatId, titleTextV.getText().toString(), consultType);
    }

    private int currentP = 0;
    /**
     * 加载历史消息
     */
    private final OnRefreshListener2<ListView> mOnRefreshListener2 = new OnRefreshListener2<ListView>() {
        public void onPullDownToRefresh(org.handmark.pulltorefresh.library.PullToRefreshBase<ListView> refreshView) {
            currentP = mListView.getSelectedItemPosition();
            if (groupType == 1) {
                onLoadMesgForHttp();
            } else {
                mAppData.messageCllection.remove(mChatId);
                if (ObjectType.SPECIAL_SERVER.equals(objectType)) {
                    onLoadMsgFromServer();
                } else {
                    onLoadMesgForDB();
                }
            }
        }

        public void onPullUpToRefresh(org.handmark.pulltorefresh.library.PullToRefreshBase<ListView> refreshView) {

        }

    };

    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        onBindService();
        onRegisterReceiver();
        super.onStart();
    }


    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        unregisterReceiver(mReceiver);
        if (mediaPlay.isPlaying()) {
            mediaPlay.stop();
        }
        onReleaseBg();
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mChatHandler.sendEmptyMessage(1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 3000://聊天背景设置相册获取
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        Uri uri = data.getData();
                        String scheme = uri.getScheme();
                        String strFilePath = null;//图片地址
                        // url类型content or file
                        if ("content".equals(scheme)) {
                            strFilePath = mChatInputControlFragment.getImageUrlByAlbum(uri);
                        } else {
                            strFilePath = uri.getPath();
                        }
                        onHandlerChatBg(strFilePath);
                    }
                }
                break;
            case 3001://聊天背景设置相机获取
                if (resultCode == Activity.RESULT_OK) {
                    String strFilePath = mChatBgFile.getAbsolutePath();
                    onHandlerChatBg(strFilePath);
                }
                mChatBgFile = null;
                break;
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        if (mLooper != null) mLooper.quit();
        if (mPopupWindow != null && mPopupWindow.isShowing()) mPopupWindow.dismiss();
        if (mOptionWindow != null && mOptionWindow.isShowing()) mOptionWindow.dismiss();
        mOptionWindow = null;
        mPopupWindow = null;
//        if (isGroupChat) SmartFoxClient.sendLogoutGroup(mChatId);
        HTalkApplication.getApplication().cancelNotify();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right2://
                if (isGroupChat) {
                    mChatInputControlFragment.hideSoftBord();
                    intent = new Intent(this, AtyChatMembers.class);
                    intent.putExtra("consultationId", mChatId);
                    startActivity(intent);
//				onShowOptionMenu(v);
                } else {
                    manaGerChat();
                }
                break;
            case R.id.popup_menu1://相册获取
                mPopupWindow.dismiss();
                onChatBgPhotoClick(v);
                break;
            case R.id.popup_menu2://相机获取图片
                mPopupWindow.dismiss();
                onChatBgCameraClick(v);
                break;
            case R.id.popup_menu3://使用默认图片
                mPopupWindow.dismiss();
                SharePreUtils.clearChatBg(this, mUserId, mChatId);
//                onSetBg();
                break;
            case R.id.popup_menu_cancel://退出
                mPopupWindow.dismiss();
                break;
            case R.id.popup_menu4://关注的人
                mPopupWindow.dismiss();
                onInviteFriend(1);
                break;
            case R.id.popup_menu5://邀请多么号,昵称
//                mPopupWindow.dismiss();
//                intent = new Intent(this, InviteByNameActivity.class);
//                intent.putExtra("groupId", mChatId);
//                startActivity(intent);
                break;
            case R.id.popup_menu6://邀请条件
                mPopupWindow.dismiss();
                onInviteFriend(3);
                break;
            case R.id.popup_menu7://邀请身边的人
                mPopupWindow.dismiss();
                onInviteFriend(4);
                break;
            case R.id.option_popmenu7://管理发言
            case R.id.option_popmenu1://删除对话
                manaGerChat();

                break;
            case R.id.option_popmenu2://聊天背景
                mOptionWindow.dismiss();
                onShowBgPopUpMenu();
                break;
            case R.id.option_popmenu3://服务内容
                mOptionWindow.dismiss();
                intent = new Intent(this, SettingWebUIActivity.class);
                intent.putExtra("TextSize", 100);
                intent.putExtra("url", "http://www.h-tlk.com/JumpPage/JumpPageServlet?Type=DoctorServiceIntroduce");
                intent.putExtra("title", "服务内容");
                startActivity(intent);
                break;
            case R.id.option_popmenu4://邀请加入
                mOptionWindow.dismiss();
                onInviteFriend(1);
//			onShowInvitePopUpMenu();
                break;
            case R.id.option_popmenu5://在线成员
                mOptionWindow.dismiss();
                onShowGroupListNumber();
                break;
//            case R.id.option_popmenu6://禁止发言
//                mOptionWindow.dismiss();
//                intent = new Intent(this, ForbiddenWordsListActivity.class);
//                intent.putExtra("groupId", mChatId);
//                startActivity(intent);
//                break;
            case R.id.option_popmenu8://只看群主
                mOptionWindow.dismiss();
                break;
            case R.id.option_popmenu9://医生资料
//                mOptionWindow.dismiss();
//                intent = new Intent(this, PersonInfoActivity.class);
//                intent.putExtra("id", mCustomerInfoEntity.getId());
//                startActivity(intent);
                break;
        /*case R.id.option_popmenu11://文件浏览
            mOptionWindow.dismiss();
			intent = new Intent(this,DocumentsChatActivity.class);
			intent.putExtra("groupId",mChatId);
			intent.putExtra("userId", mUserId);
			startActivityForResult(intent,-1);
			break;*/
        }
    }

    /**
     * 管理聊天历史
     */
    private void manaGerChat() {
        boolean isEditor = mChatAdapter.isEditor();
        if (isEditor) {
            mChatAdapter.onUnEditorMode();
        } else {
            mChatAdapter.onEditorMode();
        }
        mChatInputControlFragment.onChangeEditorMode(!isEditor);
        if (mOptionWindow != null) mOptionWindow.dismiss();
    }

    @Override
    public void onSendTxtMesg(String content) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setId(String.valueOf(System.currentTimeMillis()));
        messageEntity.setSenderId(mUserId);
        messageEntity.setReceiverId(mChatId);
        messageEntity.setType(MessageEntity.TYPE_TEXT);
        messageEntity.setSendFlag(true);
        messageEntity.setContent(content);
        messageEntity.setConsultationId(consultationId);
        messageEntity.setSendState(MessageEntity.STATE_PROCESING);
        if (!HStringUtil.isEmpty(consultationId)) {
            messageEntity.setOrderId(consultationId);
        } else {
            messageEntity.setOrderId(mOrderId);
        }

        saveMessage(messageEntity);
        mChatAdapter.addNew(messageEntity);
        mListView.setSelection(mChatAdapter.getCount());
        mPushService.onSendChatMessage(messageEntity, objectType, groupType, MessageEntity.TYPE_TEXT);
    }

    @Override
    public void onSendLocationMesg(String longitude, String latitude, String address) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setSenderId(mUserId);
        messageEntity.setReceiverId(mChatId);
        messageEntity.setId(String.valueOf(System.currentTimeMillis()));
        messageEntity.setType(MessageEntity.TYPE_LOCATION);
        messageEntity.setSendState(MessageEntity.STATE_PROCESING);
        messageEntity.setSendFlag(true);
        messageEntity.setConsultationId(consultationId);
        messageEntity.setContent(latitude + "&" + longitude);
        messageEntity.setAddress(address);
        if (!HStringUtil.isEmpty(consultationId)) {
            messageEntity.setOrderId(consultationId);
        } else {
            messageEntity.setOrderId(mOrderId);
        }
        if (isGroupChat) {
            if (null != mGroupInfoEntity) {
                messageEntity.setIsBL(mGroupInfoEntity.getIsBL());
            }
        }
        saveMessage(messageEntity);
        mChatAdapter.addNew(messageEntity);
        mPushService.onSendChatMessage(messageEntity, objectType, groupType, MessageEntity.TYPE_LOCATION);
        mListView.setSelection(mChatAdapter.getCount());
    }

    @Override
    public void onSendImageMesg(String miniFile, String bigFile) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setId(String.valueOf(System.currentTimeMillis()));
        messageEntity.setSenderId(mUserId);
        messageEntity.setReceiverId(mChatId);
        messageEntity.setType(MessageEntity.TYPE_PICTURE);
        messageEntity.setSendState(MessageEntity.STATE_PROCESING);
        messageEntity.setSendFlag(true);
        messageEntity.setConsultationId(consultationId);
        messageEntity.setContent(miniFile + "&" + bigFile);
        if (!HStringUtil.isEmpty(consultationId)) {
            messageEntity.setOrderId(consultationId);
        } else {
            messageEntity.setOrderId(mOrderId);
        }
        if (isGroupChat) {
            if (null != mGroupInfoEntity) {
                messageEntity.setIsBL(mGroupInfoEntity.getIsBL());
            }
        }
        saveMessage(messageEntity);
        mChatAdapter.addNew(messageEntity);
        mPushService.onSendChatMessage(messageEntity, objectType, groupType, MessageEntity.TYPE_PICTURE);
        mListView.setSelection(mChatAdapter.getCount());
    }

    @Override
    public void onSendVideoMesg(String video) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setId(String.valueOf(System.currentTimeMillis()));
        messageEntity.setSenderId(mUserId);
        messageEntity.setReceiverId(mChatId);
        messageEntity.setType(MessageEntity.TYPE_VIDEO);
        messageEntity.setSendState(MessageEntity.STATE_PROCESING);
        messageEntity.setSendFlag(true);
        messageEntity.setConsultationId(consultationId);
        messageEntity.setContent(video);
        if (!HStringUtil.isEmpty(consultationId)) {
            messageEntity.setOrderId(consultationId);
        } else {
            messageEntity.setOrderId(mOrderId);
        }
        if (isGroupChat) {
            if (null != mGroupInfoEntity) {
                messageEntity.setIsBL(mGroupInfoEntity.getIsBL());
            }
        }
        saveMessage(messageEntity);
        mChatAdapter.addNew(messageEntity);
        mPushService.onSendChatMessage(messageEntity, objectType, groupType, MessageEntity.TYPE_VIDEO);
        mListView.setSelection(mChatAdapter.getCount());
    }

    /**
     * 发送语音
     */
    @Override
    public void onSendVoiceMesg(String path, String timeStr, int time) {
        MessageEntity entity = new MessageEntity();
        int resultTime = (int) Math.floor(time / 1000);
        float floatTime = resultTime;
        entity.setVoiceLength(floatTime + "");
        entity.setVoiceLength(timeStr);
        entity.setSendFlag(true);
        String name = new File(path).getName();
        entity.setContent(name);
        entity.setSenderId(mUserId);
        entity.setReceiverId(mChatId);
        entity.setConsultationId(consultationId);
        entity.setSendState(MessageEntity.STATE_PROCESING);
        entity.setType(MessageEntity.TYPE_VOICE);
        if (!HStringUtil.isEmpty(consultationId)) {
            entity.setOrderId(consultationId);
        } else {
            entity.setOrderId(mOrderId);
        }
        if (isGroupChat) {
            if (null != mGroupInfoEntity) {
                entity.setIsBL(mGroupInfoEntity.getIsBL());
            }
        }

        saveMessage(entity);
        mChatAdapter.addNew(entity);
        mPushService.onSendChatMessage(entity, objectType, groupType, MessageEntity.TYPE_VOICE);
        mListView.setSelection(mChatAdapter.getCount());
    }

    @Override
    public void onSelectAll() {
        mChatAdapter.onSelectAll();
    }

    @Override
    public void onDeletAll() {
        List<MessageEntity> list = mChatAdapter.getList();
        if (list.isEmpty()) return;
        Message message = mChatHandler.obtainMessage();
        message.obj = new ArrayList<MessageEntity>(list);
        message.what = 1002;
        mChatHandler.sendMessage(message);
    }

    public void onDeletSelect() {
        List<MessageEntity> list = mChatAdapter.onDeletSelectedMesg();
        if (list.isEmpty()) return;
        Message message = mChatHandler.obtainMessage();
        message.obj = list;
        message.what = 1003;
        mChatHandler.sendMessage(message);
    }


    /**
     * 地图消息点击
     *
     * @param messageEntity
     */
    public void onLocationMesgClick(MessageEntity messageEntity) {

        Intent intent = new Intent(this, ChatMapActivity.class);
        String[] str = messageEntity.getContent().split("&");
        intent.putExtra("lo", str[0]);
        intent.putExtra("la", str[1]);
        startActivity(intent);
    }


    /**
     * 群历史消息加载
     */
    private void onLoadMesgForHttp() {
        int size = mChatAdapter.getCount();
        String serverid = null;//获取最大的id
        if (size == 0) {
            serverid = String.valueOf(Long.MAX_VALUE);
        } else {
            serverid = mChatAdapter.getFirstMesgId();
        }
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("groupid", mChatId));
        pairs.add(new BasicNameValuePair("TYPE", "queryLixianLISHIJILU"));
        pairs.add(new BasicNameValuePair("serverid", serverid));
        pairs.add(new BasicNameValuePair("Object_Type", objectType));
        pairs.add(new BasicNameValuePair("pagenum", "10"));
        HttpRestClient.doGetHZPushManagementServlet(pairs, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                if (mPullToRefreshListView != null && !mPullToRefreshListView.isRefreshing())
                    mPullToRefreshListView.setRefreshing();
            }

            @Override
            public void onAfter() {
                mPullToRefreshListView.onRefreshComplete();
                super.onAfter();
            }

            @Override
            public void onResponse(String response) {
                List<MessageEntity> list = DataParseUtil.parseGroupMessage(response, mUserId);
                List<MessageEntity> caches = new ArrayList<MessageEntity>();
                if (mChatAdapter.getCount() != 0 && AppData.DYHSID.equals(mChatId)) {
                    int size = list.size();
                    for (int i = 0; i < size; i++) {
                        if ("0".equals(list.get(i).getServerId())) ;
                        caches.add(list.get(i));
                    }
                }
                list.removeAll(caches);
                if (list.size() > 0) {
                    mChatAdapter.addCollectionToTop(list);
                    if (mChatAdapter.getCount() != 0) {
                        MessageEntity messageEntity = mChatAdapter.getList().get(mChatAdapter.getList().size() - 1);
                        onDeleteMessageFromMessgae(messageEntity);
                        deleteOfflineMessage();
                    }
                }
            }
        }, this);
    }

    /**
     * 从单聊中查询
     */
    private void onLoadMesgForDB() {
        int size = mChatAdapter.getCount();
        String serverid = null;//获取最大的id
        //判断小壹加载消息
        if (mChatId.equals(SmartFoxClient.helperId)) {
            serverid = mChatAdapter.getHelperMesgId();
        } else if (size == 0) {
            serverid = String.valueOf(Long.MAX_VALUE);
        } else {
            serverid = mChatAdapter.getFirstMesgId();
        }
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("customerId", LoginServiceManeger.instance().getLoginEntity().getId()));
        pairs.add(new BasicNameValuePair("sms_target_id", mChatId));
        pairs.add(new BasicNameValuePair("offline_id", String.valueOf(Long.MAX_VALUE)));
        String id = "";
        if (!HStringUtil.isEmpty(consultationId)) {
            id = consultationId;
        } else {
            id = mOrderId;
        }
        pairs.add(new BasicNameValuePair("consultationId", id));
        pairs.add(new BasicNameValuePair("Object_Type", objectType));
        HttpRestClient.doGetTalkHistoryServlet(pairs, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                if (mPullToRefreshListView != null && !mPullToRefreshListView.isRefreshing())
                    mPullToRefreshListView.setRefreshing();
            }

            @Override
            public void onAfter() {
                mPullToRefreshListView.onRefreshComplete();
                super.onAfter();
            }

            @Override
            public void onResponse(String response) {
                List<MessageEntity> list = DataParseUtil.parseGroupMessage(response, mUserId);
                List<MessageEntity> caches = new ArrayList<MessageEntity>();
                if (mChatAdapter.getCount() != 0 && AppData.DYHSID.equals(mChatId)) {
                    int size = list.size();
                    for (int i = 0; i < size; i++) {
                        if ("0".equals(list.get(i).getServerId())) ;
                        caches.add(list.get(i));
                    }
                }
                list.removeAll(caches);
                if (list.size() > 0) {
                    mChatAdapter.addCollectionToTopOutP(list);
                    if (mChatAdapter.getCount() != 0) {
                        MessageEntity messageEntity = mChatAdapter.getList().get(mChatAdapter.getList().size() - 1);
                        onDeleteMessageFromMessgae(messageEntity);
                    }
                }
            }
        }, this);


    }

    /**
     * 加载历史消息
     */
    private void initHistoryMesg() {
        if (groupType == 1) {
            onLoadMesgForHttp();//群聊
        } else {
            mAppData.messageCllection.remove(mChatId);
            if (ObjectType.SPECIAL_SERVER.equals(objectType)) {
                onLoadMsgFromServer();
            } else {
                onLoadMesgForDB();
            }
        }
//        boolean bl = mAppData.messageCllection.containsKey(mChatId);
//        if (bl) {//从缓存中获取消息
//            if (groupType == 0 || groupType == 3) {//单聊
//                mChatHandler.sendEmptyMessage(1000);
//            } else {//群聊
//                mChatHandler.sendEmptyMessage(9999);
//            }
//        } else {
//            if (groupType == 1) {
//                onLoadMesgForHttp();//群聊
//            } else {
//                onLoadMesgForDB();
//            }
//        }
    }

    /**
     * 消息处理
     *
     * @author zhao
     */
    private final class ChatHandler extends Handler {
        public ChatHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(final Message msg) {//异步线程
            switch (msg.what) {
                case 1000://获取的新消息
                    final List<MessageEntity> list = onLoadMesgForCache();
                    if (list == null || list.size() == 0) return;
                    deleteOffLineMessage(list, true);
                    break;
                case 9999://缓存消息(不需要加载到adapter中去)
                    final List<MessageEntity> lists = onLoadMesgForCache();
                    if (lists == null || lists.size() == 0){
                        deleteOfflineMessageSingle();
                        return;
                    }
                    deleteOffLineMessage(lists, false);
                    break;
                case 1001://db获取消息
                    final List<MessageEntity> entities = mDbUserHelper.queryChatMessageByAfterId(mUserId, mChatId, isGroupChat, (String) msg.obj, "10");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (entities != null && entities.size() > 0) {
                                mChatAdapter.addCollectionToTop(entities);
                            }
                            mPullToRefreshListView.onRefreshComplete();
                        }
                    });
                    break;
                case 1002://全部删除
                    if (groupType > 0) {
                        //群聊删除
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                HttpRestClient.deleteGroupMessages(mChatId, 1, null, new AsyncHttpResponseHandler(ChatActivity.this) {
                                    @Override
                                    public void onSuccess(int statusCode, String content) {
                                        if ("0".equals(content)) {
                                        } else {
                                            mChatAdapter.onDeleteAll();
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        //单聊删除
                        mDbUserHelper.deleteAllMessageByChatId(mChatId, mUserId);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                HttpRestClient.deleteCustomPersonMessages(mChatId, 1, null, new AsyncHttpResponseHandler(ChatActivity.this) {
                                    @Override
                                    public void onSuccess(int statusCode, String content) {
                                        if (!"0".equals(content)) {
                                            mChatAdapter.onDeleteAll();
                                        }
                                    }
                                });
                            }
                        });
                    }
                    break;
                case 1003://选中删除
                    final List<MessageEntity> messageEntities = (List<MessageEntity>) msg.obj;
                    final StringBuffer messageBuffer = new StringBuffer();
                    for (MessageEntity messageEntity : messageEntities) {
                        String id = messageEntity.getId();
                        if (id == null) continue;
                        messageBuffer.append(id);
                        messageBuffer.append(",");
                    }
                    if (isGroupChat) {
                        //群聊删除
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                HttpRestClient.deleteGroupMessages(mChatId, 0, messageBuffer.toString(), new AsyncHttpResponseHandler(ChatActivity.this) {
                                    @Override
                                    public void onSuccess(int statusCode, String content) {
                                        mChatAdapter.onDeleteSelectedl(messageEntities);
                                    }
                                });
                            }
                        });
                    } else {
                        //单聊删除
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mChatAdapter.onDeleteSelectedl(messageEntities);
                                HttpRestClient.deleteCustomPersonMessages(mChatId, 0, messageBuffer.toString(), new AsyncHttpResponseHandler(ChatActivity.this) {
                                    @Override
                                    public void onSuccess(int statusCode, String content) {
                                        if (!"0".equals(content)) {
                                            mChatAdapter.onDeleteSelectedl(messageEntities);
                                        }
                                    }
                                });
                            }
                        });
                    }
                    break;
                case 1005://收费框弹出
//				final String note = (String)msg.obj;
//				runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						onShowPayDialog(note);
//					}
//				});
                    break;
                case 1006://发送一些转发的消息
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (getIntent().hasExtra(KEY_CONTENT) && mPushService != null) {
                                String content = getIntent().getStringExtra(KEY_CONTENT);
                                if (!TextUtils.isEmpty(content)) onSendTxtMesg(content);
                                getIntent().removeExtra(KEY_CONTENT);
                            }
                        }
                    });
                    break;
                case 1007:
                    final Bundle data = msg.getData();
                    final String note = data.getString("note");
//				final String note = (String)msg.obj;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mChatAdapter.onChangMesge(data.getString("mesgId"));
                            onShowPayDialog(note);
                        }
                    });


                    break;

            }
        }
    }

    /**
     * 注册通知
     */
    private void onRegisterReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CoreService.ACTION_MESSAGE);
        filter.addAction(CoreService.ACTION_PAY_MESSAGE);
        filter.addAction(CoreService.ACTION_OFFLINE_MESSAGE);
        filter.addAction(CoreService.MESSAGE_STATUS);
        if (SmartFoxClient.helperId.equals(mChatId))
            filter.addAction(CoreService.ACTION_GROUP_INVITE);//好友邀请
        registerReceiver(mReceiver, filter);
    }

    /**
     * 从缓存中加载消息
     */
    private List<MessageEntity> onLoadMesgForCache() {
        final List<MessageEntity> list = mAppData.messageCllection.get(mChatId);
        if (list != null) mAppData.messageCllection.remove(mChatId);
        return list;
    }

    //发送消息 表示已经看过
    private class RequestHttpSendMessage extends ObjectHttpResponseHandler {
        //		private JSONArray arrays;//消息id
        public RequestHttpSendMessage() {
//			this.arrays=leavemessages;
        }

        @Override
        public Object onParseResponse(String content) {
            List<String> msgsId = new ArrayList<String>();
            try {
                org.json.JSONObject object = new org.json.JSONObject(content);
                if (1 == object.optInt("error_code", -1)) {
//					for (int i = 0; i < arrays.length(); i++) {
//						msgsId.add(arrays.get(i).toString());
//					}
                } else {
                    LogUtil.e("=======CHAT", "聊天 发送删除离线消息失败");
                }
            } catch (Exception e) {
                LogUtil.e("=======CHAT", "聊天 发送删除离线消息失败");
            }
            return msgsId;
        }

        @Override
        public void onSuccess(Object response) {
            super.onSuccess(response);
            if (response == null) return;
            ChatUserHelper.getInstance().updateChatMesageDeleteState((Collection<String>) response);

        }
    }

    /**
     * 删除离线消息
     * list 消息集合
     * isAddList 是否将消息添加到adapter中
     */
    private void deleteOffLineMessage(final List<MessageEntity> list, final boolean isAddList) {

        runOnUiThread(new Runnable() {
            public void run() {
                /**
                 * DeleteLixian42
                 Type=deleteLixian42
                 customerId
                 sms_target_id
                 offid
                 删除离线
                 */
                MessageEntity messageEntity = list.get(list.size() - 1);
                if (groupType == 1) {//群聊
                    deleteOfflineMessage();
                } else {//单聊
                    deleteOfflineMessageSingle();
                    onDeleteMessageFromMessgae(messageEntity);
                }
//                onDeleteMessageFromMessgae(messageEntity);
//				HttpRestClient.doHttpDeleteLeaveOnlineMessage(SmartFoxClient.getLoginUserId(), leavemessages.toString(), new RequestHttpSendMessage(leavemessages));
                if (list != null && list.size() > 0 && isAddList) {
                    mChatAdapter.addCollectionToEnd(list);
                    mListView.setSelection(mChatAdapter.getCount());
                } else if (!isAddList && list != null) {
//                    onLoadMesgForDB();
                    onLoadMesgForHttp();
                }
                mPullToRefreshListView.onRefreshComplete();
            }
        });
    }


    public void onDeleteMessageFromMessgae(MessageEntity messageEntity) {
//        Map<String, String> map = new HashMap<>();
//        map.put("customer_id", LoginServiceManeger.instance().getLoginUserId());
//        map.put("op", "deleteOffLineMsgRecord");
//        map.put("group_id", mChatId);
////        map.put("friend_id", mChatId);
//        if (groupType == 1) {
//            map.put("isgroup", "1");//1删除群离线消息,0删除单聊离线消息
//        } else {
//            map.put("isgroup", "0");//1删除群离线消息,0删除单聊离线消息
//        }
//        HttpRestClient.OKHttpGetFriends(map, new OkHttpClientManager.ResultCallback<String>() {
//
//            @Override
//            public void onError(Request request, Exception e) {
//
//            }
//
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject object = new JSONObject(response);
//                    if (HttpResult.SUCCESS.equals(object.optString("code"))) {
//
//                    } else {
//                        Logger.e(TAG, "聊天 发送删除离线消息失败");
//                    }
//                } catch (Exception e) {
//                    Logger.e(TAG, "聊天 发送删除离线消息失败" + e.toString());
//                }
//            }
//        }, this);

        RequestParams params = new RequestParams();
        params.put("Type", "deleteLixian42");
        params.put("customerId", SmartFoxClient.getLoginUserId());
        params.put("sms_target_id", mChatId);
        params.put("consultationId", consultationId);
        params.put("Object_Type", objectType);
        params.put("offid", messageEntity.getServerId());
        LogUtil.d(TAG, "删除离线消息");
        HttpRestClient.doHttpDELETELIXIAN42(params, new RequestHttpSendMessage());
    }

    public void onDeleteMessageFromMessgae() {

//        Map<String, String> map = new HashMap<>();
//        map.put("customer_id", LoginServiceManeger.instance().getLoginUserId());
//        map.put("op", "deleteOffLineMsgRecord");
//        map.put("group_id", mChatId);
//        if (groupType == 1) {
//            map.put("isgroup", "1");//1删除群离线消息,0删除单聊离线消息
//        } else {
//            map.put("isgroup", "0");//1删除群离线消息,0删除单聊离线消息
//        }
//        HttpRestClient.OKHttpGetFriends(map, new OkHttpClientManager.ResultCallback<String>() {
//
//            @Override
//            public void onError(Request request, Exception e) {
//
//            }
//
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject object = new JSONObject(response);
//                    if (HttpResult.SUCCESS.equals(object.optString("code"))) {
//
//                    } else {
//                        Logger.e(TAG, "聊天 发送删除离线消息失败");
//                    }
//                } catch (Exception e) {
//                    Logger.e(TAG, "聊天 发送删除离线消息失败" + e.toString());
//                }
//            }
//        }, this);

        RequestParams params = new RequestParams();
        params.put("Type", "deleteLixian42");
        params.put("customerId", SmartFoxClient.getLoginUserId());
        params.put("sms_target_id", mChatId);
        params.put("consultationId", consultationId);
        params.put("Object_Type", objectType);
        params.put("offid", Long.MAX_VALUE + "");
        LogUtil.d(TAG, "删除离线消息");
        HttpRestClient.doHttpDELETELIXIAN42(params, new RequestHttpSendMessage());

    }

    /**
     * 语音文件下载
     *
     * @author zhao
     */
    protected class VoiceFileHttpResponseHandler extends BinaryHttpResponseHandler {
        final MessageEntity mEntity;

        public VoiceFileHttpResponseHandler(MessageEntity entity) {
            mEntity = entity;
        }

        @Override
        public boolean onProcess(byte[] bytes) throws IOException {
            if (bytes == null || bytes.length == 0) throw new IOException("Save file fail");
            String fileName = StorageUtils.getFileName(mEntity.getContent());
            File file = StorageUtils.createVoiceFile(fileName);
            if (file == null) throw new IOException("Save file fail");
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(bytes);
            } finally {
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            }
            return false;
        }

        @Override
        public void onSuccess(int statusCode, byte[] binaryData) {
            super.onSuccess(statusCode, binaryData);
            if (mPlayingEntity == mEntity) {//当前下载的等于需要播放的
                mPlayingEntity = null;
                mediaPlay.play(mEntity);
            }
        }
    }


    public final void onUpdateSelectedNumber(int size) {
        mChatInputControlFragment.onUpdateSelectedNumber(size);
    }


    //当前需要复制的view
    TextView mCopyTxtV;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        switch (v.getId()) {
            case R.id.chat_content:
                mCopyTxtV = (TextView) v;
                menu.add(0, 1, 0, "复制");
                menu.add(0, 2, 0, "重发此消息");
                break;
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1://复制
                if (mCopyTxtV != null)
                    SystemUtils.clipTxt(mCopyTxtV.getText(), this);
                return true;
            case 2:
                if (mCopyTxtV != null)
                    onSendTxtMesg(mCopyTxtV.getText().toString());
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onContextMenuClosed(Menu menu) {
        mCopyTxtV = null;
        super.onContextMenuClosed(menu);
    }


    /**
     * 弹出支付窗口
     */
    private void onShowPayDialog(String note) {
        DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), note, "知道了", "现在去购买", new OnDilaogClickListener() {
            @Override
            public void onDismiss(DialogFragment fragment) {
                fragment.dismissAllowingStateLoss();
            }

            @Override
            public void onClick(DialogFragment fragment, View v) {
                toPay();
            }
        });
    }

    /**
     * 去购买医生服务
     */
    public void toPay() {
        String name = getIntent().getComponent().getClassName();
        if (name.equals(DoctorClinicMainActivity.class.getName())) {
            onBackPressed();
        } else {
            Intent intent = new Intent(this, DoctorClinicMainActivity.class);
            intent.putExtra("id", mChatId);
            startActivity(intent);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            boolean b = mChatInputControlFragment.isAllPanelGone();//隐藏未隐藏的面板
            if (!b) return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 绑定后台服务
     */
    private void onBindService() {
        Intent intent = new Intent(this, CoreService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mGroupInfoEntity != null) outState.putParcelable("group_entity", mGroupInfoEntity);
//        if (mCustomerInfoEntity != null)
//            outState.putSerializable("user_entity", mCustomerInfoEntity);
        if (!HStringUtil.isEmpty(mOrderId)) outState.putString("order_id", mOrderId);
    }

    /**
     * 视频播放
     *
     * @param str
     */
    public void onShowVideo(String str) {
//      Uri uri = Uri.parse(HTalkApplication.getHttpUrls().URL_DOWNLOAVIDEO + pVideosList.get(index).getImagePath());
        Uri uri = Uri.parse(str);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "video/mp4");
        startActivity(intent);
    }

    /**
     * 语音播放
     *
     * @param entity
     */
    public void onVoicePlay(MessageEntity entity) {
        if (StorageUtils.isSDMounted()) {
            mPlayingEntity = entity;
            String path = entity.getContent();
            if (path.contains("/")) {//远程路径
                String name = StorageUtils.getFileName(path);
                File file = new File(StorageUtils.getVoicePath(), name);
                if (file.exists()) {//文件不存在需要去下载
                    mediaPlay.play(entity);
                } else {
                    HttpRestClient.doHttpDownChatFile(path, new VoiceFileHttpResponseHandler(entity));
                }
            } else {
                mediaPlay.play(entity);
            }
        }
    }

    /**
     * 播放错误
     */
    @Override
    public void onPlayError() {

    }

    /**
     * 显示聊天大图片
     *
     * @param str
     */
    public void onShowBigImage(String str) {
        Intent intent = new Intent(this, ImageGalleryActivity.class);
        intent.putExtra(ImageGalleryActivity.URLS_KEY, new String[]{str});
        intent.putExtra(ImageGalleryActivity.TYPE_KEY, 0);//0,1单个,多个
        intent.putExtra("type", 1);
        startActivityForResult(intent, 3000);
    }

//    /**
//     * 显示menu菜单
//     *
//     * @param view
//     */
//    private void onShowOptionMenu(View view) {
//        if (mOptionWindow != null && mOptionWindow.isShowing()) {
//            mOptionWindow.dismiss();
//            return;
//        }
//        if (mChatMenuV == null) {
//            final LayoutInflater inflater = getLayoutInflater();
//            if (isGroupChat) {//群聊菜单
//                mChatMenuV = inflater.inflate(R.layout.window_menu_chat_group_layout, null);
//                ViewGroup group = (ViewGroup) mChatMenuV.findViewById(R.id.contain);
//                boolean isGrouper = mUserId.equals(mGroupInfoEntity.getCreateCustomerID());//是否是群主
//                for (int i = 0; i < group.getChildCount(); i++) {
//                    group.getChildAt(i).setOnClickListener(this);
//                }
//                if (!isGrouper) {//非群主菜单
//                    group.findViewById(R.id.option_popmenu7).setVisibility(View.GONE);
//                    group.findViewById(R.id.option_popmenu6).setVisibility(View.GONE);//
//                    group.findViewById(R.id.option_popmenu8).setVisibility(View.GONE);//只看群主
//                }
//                //单聊菜单
//            } else {
//                mChatMenuV = inflater.inflate(R.layout.window_menu_chat_layout, null);
//                ViewGroup group = (ViewGroup) mChatMenuV.findViewById(R.id.contain);
//                for (int i = 0; i < group.getChildCount(); i++) {
//                    group.getChildAt(i).setOnClickListener(this);
//                }
//                boolean isDoctor = mCustomerInfoEntity.isShowDoctorV() || SmartFoxClient.getLoginUserInfo().isShowDoctorV();//是否是医生
////				group.findViewById(R.id.option_popmenu9).setVisibility(isDoctor ? View.VISIBLE : View.GONE);
//                group.findViewById(R.id.option_popmenu2).setVisibility(isDoctor ? View.GONE : View.VISIBLE);//聊天背景
////				group.findViewById(R.id.option_popmenu3).setVisibility(isDoctor ? View.VISIBLE : View.GONE);
//            }
//            if (mOptionWindow == null)
//                mOptionWindow = new PopupWindow(mChatMenuV, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
//            mOptionWindow.setTouchable(true);
//            mOptionWindow.setBackgroundDrawable(new BitmapDrawable());
//            mOptionWindow.setOutsideTouchable(true);
//        }
//        mOptionWindow.setContentView(mChatMenuV);
//        mOptionWindow.showAsDropDown(view);
//    }

    /**
     * 处理聊天背景图片
     *
     * @param path
     */
    private void onHandlerChatBg(String path) {
        int size = getDisplayMaxSlidSize();
        Bitmap bitmap = BitmapUtils.decodeBitmap(path, size, size);
        if (bitmap == null) return;
        File file = StorageUtils.createThemeFile();
        if (file != null) {
            StorageUtils.saveImageOnImagsDir(bitmap, file);
            SharePreUtils.saveChatBg(this, file.getAbsolutePath(), mUserId, mChatId);
            mChatBgImageV.setBackgroundDrawable(new BitmapDrawable(bitmap));
        }
    }

    /**
     * 释放背景图
     */
    private void onReleaseBg() {
        BitmapDrawable drawable = (BitmapDrawable) mChatBgImageV.getBackground();
        if (drawable == null) return;
        mChatBgImageV.setBackgroundDrawable(null);
        drawable.setCallback(null);
        if (!drawable.getBitmap().isRecycled()) {
            drawable.getBitmap().recycle();
        }
        drawable = null;
        System.gc();
    }

    /**
     * 获得屏幕分辨率最大的边
     *
     * @return
     */
    private int getDisplayMaxSlidSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return Math.max(metrics.heightPixels, metrics.widthPixels);
    }

    /**
     * 点击照片,相册获取
     *
     * @param v
     */
    private void onChatBgPhotoClick(View v) {
        Intent intent = CropUtils.createPickForFileIntent();
        startActivityForResult(intent, 3000);
    }

    /**
     * 点击相机
     *
     * @param v
     */
    private void onChatBgCameraClick(View v) {
        try {
            mChatBgFile = StorageUtils.createImageFile();
            Uri outUri = Uri.fromFile(mChatBgFile);
            Intent intent = CropUtils.createPickForCameraIntent(outUri);
            startActivityForResult(intent, 3001);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出编辑模式
     */
    @Override
    public void onDeletCancle() {
        manaGerChat();
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new MyEvent("refresh", 2));
        super.onBackPressed();
    }

    /**
     * {
     * "sms_req_content": "您的体验条数已用完，请购买服务。",
     * "customerId": "230536",
     * "sms_target_id": "3774",
     * "server_code": 7057,
     * "order_id": "14369"
     * }
     *
     * @param event 特殊服务系统提示
     */
    private boolean isRemoved = false;//用于提示删除最后一条消息

    public void onEventMainThread(MyEvent event) {
        if (SmartControlClient.SERVICE_SINGLE_SYS_MSG == (event.code)) {
            try {
                JSONObject obj = new JSONObject(event.what);
//                if (mChatId.equals(obj.optString("sms_target_id"))) {
                    if (!HStringUtil.isEmpty(obj.optString("sms_req_content"))) {
                        SingleBtnFragmentDialog.showDefaultNot(getSupportFragmentManager(), obj.optString("sms_req_content"), new SingleBtnFragmentDialog.OnClickSureBtnListener() {
                            @Override
                            public void onClickSureHander() {
                                if (isRemoved) {
                                    mChatAdapter.removeLast();
                                }
                                isRemoved = true;
                            }
                        });
                    }
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (SmartControlClient.SERVICE_SINGLE_SYS_MSG_TIP == (event.code)) {
            try {
                JSONObject obj = new JSONObject(event.what);
                if (mChatId.equals(obj.optString("sms_target_id"))) {
                    if (!HStringUtil.isEmpty(obj.optString("sms_req_content"))) {
                        SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), obj.optString("sms_req_content"), null);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查询在线成员
     */
    public void onShowGroupListNumber() {
//        Intent intent = new Intent(this, TopicMemberInfoUI.class);
//        intent.putExtra("groupId", mChatId);
////		intent.putExtra("type",1000);
//        intent.putExtra("type", 0);//0表示成员信息
//        startActivityForResult(intent, -1);
    }

    /**
     * 邀请好友
     *
     * @param type
     */
    private void onInviteFriend(int type) {
//        Intent intent;
//        if (type == 3) {
//            intent = new Intent(this, FriendSearchAboutFriendActivity.class);
//            intent.putExtra("type", type);
//        } else {
//            intent = new Intent(this, InviteMyAttentionMainUI.class);//邀请我关注的人
//        }
//        intent.putExtra("groupId", mChatId);
//        startActivity(intent);
    }

    View mChangeBgV;//选着背景图

    private void onShowBgPopUpMenu() {// 设置聊天背景图片
        onShowPopUpMenu(1);
    }

    View mInviteV;//好友邀请

    private void onShowInvitePopUpMenu() {
        onShowPopUpMenu(0);
    }

    private void onShowPopUpMenu(int type) {
        View contentView = null;
        final LayoutInflater inflater = getLayoutInflater();
        if (type == 0) {//好友邀请
            if (mInviteV == null) {
                mInviteV = inflater.inflate(R.layout.window_popup_invite_layout, null);
                ViewGroup group = (ViewGroup) mInviteV;
                for (int i = 0; i < group.getChildCount(); i++) {
                    group.getChildAt(i).setOnClickListener(this);
                }
            }
            contentView = mInviteV;
        }
        if (type == 1) {//背景图片
            if (mChangeBgV == null) {
                mChangeBgV = inflater.inflate(R.layout.window_popup_choosebg_layout, null);
                ViewGroup group = (ViewGroup) mChangeBgV;
                for (int i = 0; i < group.getChildCount(); i++) {
                    group.getChildAt(i).setOnClickListener(this);
                }
            }
            contentView = mChangeBgV;
        }
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(contentView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
            mPopupWindow.setTouchable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setAnimationStyle(R.style.AnimationPreview);
        }
        mPopupWindow.setContentView(contentView);
        mPopupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 只有第一次才发送系统通知消息
     */
    private void sendMesgeFromHelper() {
        //单聊
        if (SmartFoxClient.helperId.equals(mChatId) && isFristLoad) {
            final MessageEntity entity = new MessageEntity();
            entity.setType(MessageEntity.TYPE_TEXT);
            entity.setContent("您好，我是您的系统通知，将为您发送各类消息通知，请您及时查看。");
            entity.setSenderId(SmartFoxClient.helperId);
            entity.setReceiverId(SmartFoxClient.getLoginUserId());
            mDbUserHelper.insertChatMessageFromSelf(entity, false);
            mChatAdapter.addNew(entity);
            mListView.setSelection(mChatAdapter.getCount());
            mPullToRefreshListView.onRefreshComplete();
        }
        isFristLoad = false;
    }

    private void saveMessage(MessageEntity messageEntity) {//保存消息到db
//		if(!isGroupChat){
//			mDbUserHelper.insertChatMessage(messageEntity,false);
//		}
    }


    private int pageNum = 1;//分页页数

    /**
     * 从特殊服务单聊中查询历史记录http
     */
    private void onLoadMsgFromServer() {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("op", "queryOrderMsg"));
        pairs.add(new BasicNameValuePair("friend_id", mChatId));
        pairs.add(new BasicNameValuePair("customer_id", LoginServiceManeger.instance().getLoginEntity().getId()));
        pairs.add(new BasicNameValuePair("num", "20"));
        pairs.add(new BasicNameValuePair("page", pageNum + ""));
        HttpRestClient.doGetTalkHistoryServletS(pairs, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                if (mPullToRefreshListView != null && !mPullToRefreshListView.isRefreshing())
                    mPullToRefreshListView.setRefreshing();
            }

            @Override
            public void onAfter() {
                mPullToRefreshListView.onRefreshComplete();
                super.onAfter();
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if ("1".equals(obj.optString("code"))) {
                        String content = obj.optString("result");
                        final List<MessageEntity> list = DataParseUtil.parseGroupMessage(content, mUserId);
                        if (list != null) {
                            if (pageNum == 1)//第一次加载
                            {
                                mChatAdapter.removeAll();
                                onDeleteMessageFromMessgae();
                            }
                            if (list.size() != 0) {//加载出了数据
                                mChatAdapter.addCollectionToTop(list);

                                mListView.post(new Runnable() {
                                    @Override
                                    public void run() {
//                                        mListView.smoothScrollToPosition(currentP + list.size());
                                        mListView.setSelection(currentP + 1 + list.size());
                                    }
                                });
                                currentP = mListView.getSelectedItemPosition();
                            } else {
                                if (pageNum != 1) {
                                    ToastUtil.showShort("没有更多了");
                                }

                            }
                            pageNum++;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, this);

    }

    /**
     * 去支付setGoSee+order_type+site_id++doctor_id+price
     */
    public void getServiceOrder(String content) {
        Intent intent = new Intent(this, OrderActivity.class);
        startActivity(intent);
        finish();
//        String[] keys = content.split("&");
//        if (content.contains("&") && keys.length > 3) {
//            Intent intent = new Intent(this, PAtyConsultStudioGoPaying.class);
//            intent.putExtra(PAtyConsultStudioGoPaying.SITE_ID, keys[1]);
//            intent.putExtra("service_item_id", "0");
//            intent.putExtra("price", keys[3]);//价格
//            intent.putExtra("doctor_id", keys[2]);//医生ID
//
//            if (content.startsWith("siteGoSee5")) {//医生集团图文
//                intent.putExtra("service_id", "5");
//                intent.putExtra(PAtyConsultStudioGoPaying.SERVICETYPEID, "5");
//            } else if (content.startsWith("siteGoSee6")) {//医生集团电话
//                intent.putExtra("service_id", "6");
//                intent.putExtra(PAtyConsultStudioGoPaying.SERVICETYPEID, "6");
//            } else if (content.startsWith("siteGoSee8")) {//医生集团视频
//                intent.putExtra("service_id", "8");
//                intent.putExtra(PAtyConsultStudioGoPaying.SERVICETYPEID, "8");
//            }
//            startActivity(intent);
//        } else {
//            ToastUtil.showShort("数据异常");
//        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (isGroupChat) {
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE,
                    SessionTypeEnum.None);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isGroupChat)
            NIMClient.getService(MsgService.class).setChattingAccount(mChatId, SessionTypeEnum.Team);
    }


    /**
     * 删除单聊离线
     */
    public void deleteOfflineMessageSingle() {
        Map<String, String> map = new HashMap<>();
        map.put("op", "deleteOffLineMsgRecord");
        map.put("customer_id", LoginServiceManeger.instance().getLoginUserId());
        map.put("friend_id", mChatId);
        map.put("isgroup", "0");//1删除群离线消息,0删除单聊离线消息
        HttpRestClient.OKHttpGetFriends(map, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (HttpResult.SUCCESS.equals(object.optString("code"))) {

                    } else {
                        Logger.e(TAG, "聊天 发送删除离线消息失败");
                    }
                } catch (Exception e) {
                    Logger.e(TAG, "聊天 发送删除离线消息失败" + e.toString());
                }
            }
        }, this);

    }

    /**
     * 删除群聊离线
     */
    public void deleteOfflineMessage() {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", LoginServiceManeger.instance().getLoginUserId());
        map.put("op", "deleteOffLineMsgRecord");
        map.put("group_id", mChatId);
        map.put("isgroup", "1");//1删除群离线消息,0删除单聊离线消息
        HttpRestClient.OKHttpGetFriends(map, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (HttpResult.SUCCESS.equals(object.optString("code"))) {

                    } else {
                        Logger.e(TAG, "聊天 发送删除离线消息失败");
                    }
                } catch (Exception e) {
                    Logger.e(TAG, "聊天 发送删除离线消息失败" + e.toString());
                }
            }
        }, this);

    }
}
