package com.yksj.consultation.son.consultation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.cropimage.CropUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.utils.StorageUtils;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog.OnDilaogClickListener;
import com.yksj.consultation.comm.EmptyLayout;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.comm.SingleBtnFragmentDialog.OnClickSureBtnListener;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.R.drawable;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.setting.SettingWebUIActivity;
import com.yksj.healthtalk.db.DictionaryHelper;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.JsonsfHttpResponseHandler;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.photo.utils.AlbumActivity;
import com.yksj.healthtalk.photo.utils.GalleryActivity;
import com.yksj.healthtalk.utils.Bimp;
import com.yksj.healthtalk.utils.BitmapUtils;
import com.yksj.healthtalk.utils.DensityUtils;
import com.yksj.healthtalk.utils.ImageItem;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.ValidatorUtil;
import com.yksj.healthtalk.utils.WheelUtils;
import com.yksj.healthtalk.views.MessageImageView;
/**
 * 填写申请
 * @author zheng
 */
public class PConsultApplyFlowActivity extends BaseFragmentActivity implements OnClickListener {

	private String pNumber,address,illnessKindT,illnessStateT,pNumCodeT,landDoctorNameT;
	private CustomerInfoEntity mLoginUserInfo;
	private CheckBox landDoctor;
	private EditText landDoctorName,phoneNun;
	private Boolean FLAG=true;
	private Button getPCode;
	private boolean Sendcode = false,isApplying=false;//验证码是否发送 true为发送
	private EditText pNumCode;
	private PopupWindow addPhotoPop;
	private File storageFile=null;
	private static final int TAKE_PICTURE = 0x000001;
	private Button imgAdd;
	private View wheelView;
	private List<Map<String, String>> proList = null;
	private Map<String, List<Map<String, String>>> cityMap = new LinkedHashMap<String, List<Map<String, String>>>();
	private View mainView;
	private PopupWindow pop;
	private TextView CURRENTVIEW,protocol;
	private TextView suozaidi;
	private String locationCode = "";//所在地编码
	private Dialog dia;
	private EditText illnessKind,illnessState;
	ArrayList<ImageItem> imagesList;
	private View landLine;
	private LinearLayout images;
	private SharedPreferences applyData;
	private Runnable runnable;
	private boolean isExistDoctor=false;
	SingleBtnFragmentDialog postDialog;
	CancelClickListener cancelListener =new CancelClickListener();
	Handler handler = new Handler();
	private EmptyLayout mEmptyLayout;
	private HorizontalScrollView hsv;
    private ScrollView flowSv;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.consultation_apply_flow_activity);
		initView();
	}
	private void initView() {
		initTitle();
		titleTextV.setText(R.string.apply);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn2.setVisibility(View.VISIBLE);
		titleRightBtn2.setText(R.string.setting_tijiao);
		titleRightBtn2.setOnClickListener(this);
		mLoginUserInfo = SmartFoxClient.getLoginUserInfo();
        flowSv = (ScrollView) findViewById(R.id.flow_dossier_sv);
		mainView= getLayoutInflater().inflate(R.layout.consultation_apply_flow_activity, null);
		illnessKind=(EditText) findViewById(R.id.illness_kind);
		illnessState=(EditText) findViewById(R.id.illness_state);
		protocol=(TextView) findViewById(R.id.agreetment1);//服务协议
		protocol.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		protocol.setOnClickListener(this);
		landDoctor=(CheckBox) findViewById(R.id.Land);
		landDoctor.setOnClickListener(this);
		landDoctorName=(EditText) findViewById(R.id.land_doctor_name);
		phoneNun =(EditText) findViewById(R.id.phone_nunber);
		pNumCode =(EditText) findViewById(R.id.phone_nunber_code);
		landLine=findViewById(R.id.Land_line);
		getPCode=(Button) findViewById(R.id.get_phone_code);
		getPCode.setOnClickListener(this);
		
		findViewById(R.id.interestpic_layout).setOnClickListener(this);
		hsv=(HorizontalScrollView) findViewById(R.id.illness_img);
		images=(LinearLayout) findViewById(R.id.item_images);
		imgAdd=(Button) findViewById(R.id.item_img_add);
		imgAdd.setOnClickListener(this);
		findViewById(R.id.location_action1).setOnClickListener(this);
		wheelView = getLayoutInflater().inflate(R.layout.wheel, null);
		wheelView.findViewById(R.id.wheel_cancel).setOnClickListener(this);
		wheelView.findViewById(R.id.wheel_sure).setOnClickListener(this);
		suozaidi = (TextView) findViewById(R.id.location1);
		pop = new PopupWindow(wheelView, LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		imagesList=new ArrayList<ImageItem>();
		Bimp.dataMap.put(PConsultApplyFlowActivity.class.getSimpleName(), imagesList);
		Bimp.imgMaxs.put(PConsultApplyFlowActivity.class.getSimpleName(), 16);
		applyData = getSharedPreferences("applydata", Context.MODE_PRIVATE);
		getText1();
		queryData();
		getSharedPreference(landDoctorName, phoneNun, suozaidi);
	}

	/**
	 * 获取文本
	 */
	private void getText1(){
		pNumber=phoneNun.getText().toString().trim();
		address=suozaidi.getText().toString().trim();
		illnessKindT=illnessKind.getText().toString().trim();
		illnessStateT=illnessState.getText().toString().trim();
		pNumCodeT=pNumCode.getText().toString().trim();
		landDoctorNameT=landDoctorName.getText().toString().trim();
	}
	/**
	 * 填写SharedPreference
	 */
	private void putSharedPreference(){
		getText1();
		applyData = getSharedPreferences("applydata", Context.MODE_PRIVATE);
		Editor editor = applyData.edit();
		editor.putString("UERSID", SmartFoxClient.getLoginUserId());
		editor.putString("NAME", landDoctorNameT);
		editor.putString("PNUMBER", pNumber);
		editor.putString("ADDRESS", address);
		editor.commit();
	}
	/**
	 * 得到SharedPreference
	 * @param landDoctorName
	 * @param phoneNun
	 * @param suozaidi
	 */
	private void getSharedPreference(EditText landDoctorName,EditText phoneNun,TextView suozaidi){
		applyData = getSharedPreferences("applydata", Context.MODE_PRIVATE);
		String uersId=applyData.getString("UERSID", "");
		if(uersId.equals(SmartFoxClient.getLoginUserId())){
			String name = applyData.getString("NAME", "");
			String pNumber = applyData.getString("PNUMBER", "");
			String address =applyData.getString("ADDRESS", "");
			landDoctorName.setText(name);
			phoneNun.setText(pNumber);
			suozaidi.setText(address);

		}
	}
//	SharePreUtils
	/**
	 * 获取地区
	 */
	private void queryData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				proList = DictionaryHelper.getInstance(PConsultApplyFlowActivity.this).setCityData(
						PConsultApplyFlowActivity.this, cityMap);
			}
		}).start();
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
	 * 设置内容
	 */
	public void setText() {
		if (CURRENTVIEW.equals(suozaidi)) {
			suozaidi.setText(WheelUtils.getCurrent());
		}
		locationCode = WheelUtils.getCode();
		applyData.edit().putString("LANDDOCTOR", locationCode).commit();

	}
	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		SystemUtils.hideSoftBord(getApplicationContext(), landDoctorName);
		Intent intent;
		switch (v.getId()) {
		case R.id.title_back:
			onBackPressed();
			break;
		case R.id.title_right2:
			if(isApplying==false){
				getText1();
				putSharedPreference();
				fillError();
			}
			break;
		case R.id.Land:
			if(FLAG){
				landDoctor.setBackgroundResource(drawable.select);
				landLine.setVisibility(View.VISIBLE);
				landDoctorName.setVisibility(View.VISIBLE);
                flowSv.fullScroll(View.FOCUS_DOWN);//滚动到底部
				FLAG=false;
			}else{
				landDoctor.setBackgroundResource(drawable.no_select);
				landDoctorName.setVisibility(View.GONE);
				landLine.setVisibility(View.GONE);
				FLAG=true;
			}
			break;
		case R.id.ensure_hint:
			dia.dismiss();
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
		case R.id.location_action1://位置选择
			CURRENTVIEW = suozaidi;
			setCity();
			break;
		case R.id.cameraadd://相机
			if (addPhotoPop.isShowing()) {
				addPhotoPop.dismiss();
			}
			photo();
			break;
		case R.id.cancel://取消
			if (addPhotoPop!=null &&  addPhotoPop.isShowing()) {
				addPhotoPop.dismiss();
			}
			break;
		case R.id.galleryadd://从相册获取
			if (addPhotoPop.isShowing()) {
				addPhotoPop.dismiss();
			}
			intent=new Intent(PConsultApplyFlowActivity.this,AlbumActivity.class);
			intent.putExtra("key", PConsultApplyFlowActivity.class.getSimpleName());
			startActivityForResult(intent, 100);
			break;
		case R.id.item_img_add:
			showuploadPopWindow();
			break;
		case R.id.get_phone_code:// 获取验证码
			if (Sendcode)
				return;
			getAuthCode();
			
			break;
		case R.id.agreetment1:
			intent = new Intent(this,SettingWebUIActivity.class);
			intent.putExtra("title", "医生服务协议");
			intent.putExtra("url",HTalkApplication.getApplication().getDoctorAgentPath());
			startActivity(intent);
			break;
		case R.id.interestpic_layout:
//          scrollView.fullScroll(ScrollView.FOCUS_DOWN);滚动到底部
//			hsv.fullScroll(android.view.View.FOCUS_RIGHT);
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		showPhoto(Bimp.dataMap.get(PConsultApplyFlowActivity.class.getSimpleName()));
	}

	/**
	 * 弹出上传图片的选择布局
	 */
	public void showuploadPopWindow() {
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.interest_image_add_action, null);
		View mainView = inflater.inflate(R.layout.interest_content, null);
		if (addPhotoPop == null) {
			addPhotoPop = new PopupWindow(view, LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT);
			addPhotoPop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		}
		WheelUtils.setPopeWindow(this, mainView, addPhotoPop);
		view.findViewById(R.id.cameraadd).setOnClickListener(this);
		view.findViewById(R.id.galleryadd).setOnClickListener(this);
		view.findViewById(R.id.cancel).setOnClickListener(this);
	}

    /**
     *相机
     */
	public void photo() {
		storageFile=null;
		if(StorageUtils.isSDMounted()){
			try {
				storageFile = StorageUtils.createCameraFile();
				Uri uri = Uri.fromFile(storageFile);
				Intent intent = CropUtils.createPickForCameraIntent(uri);
				startActivityForResult(intent, TAKE_PICTURE);
			} catch (Exception e) {
				ToastUtil.showLong(this, R.string.msg_camera_bug);
			}
		}else{
			SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(),  "sdcard未加载");
		}
	}
	/**
	 * 请求
	 */
	private void applyConsult(){
		getText1();
		JSONObject json = new JSONObject();
		try {
			json.put("CONSULTATION_NAME",illnessKindT );
			json.put("CONSULTATION_DESC",illnessStateT );
			json.put("CONSULTATION_CENTER_ID",HTalkApplication.APP_CONSULTATION_CENTERID );
			json.put("PATIENTID",mLoginUserInfo.getId());
			json.put("PATIENTTEL_PHONE",pNumber);
			applyData=getSharedPreferences("applydata", Context.MODE_PRIVATE);
			json.put("AREA_CODE",applyData.getString("LANDDOCTOR", ""));
			if(landDoctor.isChecked()){
				json.put("DOCTOR_NAME",landDoctorNameT);
			}else {
				json.put("DOCTOR_NAME","");
			}
			json.put("EXPERT_ID",getIntent().getStringExtra("CUSTOMER_ID"));
			json.put("VERIFICATION_CODE",pNumCodeT);
			json.put("SERVICE_PRICE", getIntent().getStringExtra("PAYMENT"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		RequestParams params = new RequestParams();
		params.put("PARAMETER", json.toString());
		putFile(params);//添加图片
		HttpRestClient.doHttpApplyConsultation( params, new AsyncHttpResponseHandler(this){

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				try {
					if(content!=null&&content.contains("error_code")){
						JSONObject obj=new JSONObject(content);
						ToastUtil.showToastPanl(obj.optString("error_message"));
						isApplying=false;
					}else{
						if(content==null){
							SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "提交失败");
							isApplying=false;
							return;
						}
						JSONObject obj=new JSONObject(content);
						String conten=obj.getString("message");
						if(conten.contains("验证码")){
							ToastUtil.showToastPanl(conten);
							isApplying=false;
							return;
						}
						String flag=obj.getString("flag");
						if(flag.equals("0")){
							DoubleBtnFragmentDialog.show(getSupportFragmentManager(), "提示", conten, "取消", "接受", new OnDilaogClickListener(){

								@Override
								public void onDismiss(DialogFragment fragment) {
									
								}
								@Override
								public void onClick(DialogFragment fragment, View v) {
									Intent intent=getIntent();
									intent.putExtra("FINISH", 55);
									setResult(33, intent);
									landDoctor.setChecked(false);
									if(!isExistDoctor){
										applyConsult();
										isExistDoctor=true;
									}
									finish();
								}
							});
							return;
						}
						if(isExistDoctor){
							return;
						}
						DoubleBtnFragmentDialog.show(getSupportFragmentManager(), "提示", conten, "马上去看", "稍后再说", new OnDilaogClickListener() {

							@Override
							public void onDismiss(DialogFragment fragment) {
								Intent intent=new Intent(PConsultApplyFlowActivity.this,AtyConsultServer.class);
								intent.putExtra("CONFRAG", 2);
								isApplying=false;
								Intent intent1=getIntent();
								intent1.putExtra("FINISH", 55);
								setResult(33, intent1);
								finish();
								startActivity(intent);
							}

							@Override
							public void onClick(DialogFragment fragment, View v) {
								Intent intent=getIntent();
								intent.putExtra("FINISH", 55);
								isApplying=false;
								setResult(33, intent);
								finish();
							}
						});
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onStart() {
				isApplying=true;
				super.onStart();
			}
			@Override
			public void onFinish() {
				postDialog.dismiss();
				isApplying=false;
				super.onFinish();
			}
			@Override
			public void onFailure(Throwable error) {
				isApplying=false;
				ToastUtil.showToastPanl("请求失败");
				super.onFailure(error);
			}
		});
	}
	//将文件放入参数
	private void putFile(RequestParams params) {
		params.putNullFile("file", new File(""));
		ArrayList<ImageItem> list=Bimp.dataMap.get(PConsultApplyFlowActivity.class.getSimpleName());//根据ID寻找
		for(int i=0;i<list.size();i++){
			int index=i+1;
			try {
				params.put(index+".jpg", BitmapUtils.onGetDecodeFileByPath(
						PConsultApplyFlowActivity.this,list.get(i).getImagePath()));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
//	@Override
//	protected void onResume() {
//		super.onResume();
//		hsv.fullScroll(android.view.View.FOCUS_RIGHT);
//	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		switch (arg0) {
		case TAKE_PICTURE:
			if ( arg1 == Activity.RESULT_OK) {
				if(storageFile!=null){
					ImageItem takePhoto = new ImageItem();
					takePhoto.setImagePath(storageFile.getAbsolutePath());
					ArrayList<ImageItem> list=Bimp.dataMap.get(PConsultApplyFlowActivity.class.getSimpleName());
					list.add(takePhoto);
					showPhoto(list);
				}
			}
			break;
		}
		if(arg0 == 100 & arg1 == 50){
			int name = arg2.getIntExtra("END",65);
			switch (name) {
			case 66:
				break;
			}
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

		String phone = phoneNun.getText().toString();
		if(TextUtils.isEmpty(phone)){
			ToastUtil.showToastPanl("请填写手机号码");
			return;
		}
		if(!ValidatorUtil.checkMobile(phone)){
			ToastUtil.showToastPanl("手机号码有误");
			return;
		}
		if ( ValidatorUtil.checkMobile(phone)) {
			HttpRestClient.doHttpSendVerificationCode(phone, new JsonsfHttpResponseHandler(this) {
				@Override
				public void onSuccess(int statusCode,com.alibaba.fastjson.JSONObject object) {
					super.onSuccess(statusCode, object);
					if (object.containsKey("error_code")) {
						SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), object.getString("error_message"));
					} else {
						Sendcode = true;
						timerTaskC();
						ToastUtil.showShort(PConsultApplyFlowActivity.this,object.getString("message"));
					}
				}
			});
		} else {
			SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), getString(R.string.phone_toastSpecialcharter));
		}
	}

	/**
	 * 
	 * 屏幕宽度
	 */
	private int getScreenWidth(Context context) { 
		WindowManager manager = (WindowManager) context 
				.getSystemService(Context.WINDOW_SERVICE); 
		Display display = manager.getDefaultDisplay(); 
		return display.getWidth()/4;
	} 

	/**
	 * 验证数据的存在
	 */
	private void fillError(){
		if(TextUtils.isEmpty(illnessKindT)){
			ToastUtil.showToastPanl("请填写会诊疾病");
			return;
		}
		if(illnessKindT.trim().length() < 2){
			ToastUtil.showToastPanl("会诊疾病2~50字");
			return;
		}
		if(TextUtils.isEmpty(illnessStateT)){
			ToastUtil.showToastPanl("请填写病情说明");
			return;
		}
		if(illnessStateT.trim().length() < 10 ){
			ToastUtil.showToastPanl("病情说明10~1000字");
			return;
		}
		if(TextUtils.isEmpty(pNumber)){
			ToastUtil.showToastPanl("请填写手机号码");
			return;
		}
		if(!ValidatorUtil.checkMobile(pNumber)){
			ToastUtil.showToastPanl("手机号码有误");
			return;
		}
		if(TextUtils.isEmpty(pNumCodeT)){
			ToastUtil.showToastPanl("请填写验证码");
			return;
		}
		if(TextUtils.isEmpty(address)){
			ToastUtil.showToastPanl("请选择您的所在地");
			return;
		}
		if(landDoctor.isChecked()){
			if(TextUtils.isEmpty(landDoctorNameT)){
				ToastUtil.showToastPanl("请填写当地医生名字");
				return;
			}
		}
//		isApplying=true;
		postDialog=SingleBtnFragmentDialog.show(getSupportFragmentManager(), "提示", "提交过程可能需要较长时间，请保持网络连接","知道了",cancelListener);
//		postDialog.setCancelable(false);//不可被取消
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
					getPCode.setText("发送验证码");
					getPCode.setEnabled(true);
					Sendcode=false;
					return;
				} else {
					--i;
					handler.postDelayed(this, 1000);
					getPCode.setText(i + "");
					getPCode.setEnabled(false);
				}
			}
		};
		handler.postDelayed(runnable, 1000);
	}
	/**
	 * 显示图片
	 */
	private void showPhoto(ArrayList<ImageItem> imagesList1){
		images.removeAllViews();
		if(imagesList1.size()>0){//
			images.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1f));
		}else//将加号放在中间
			images.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,0f));
		if(imagesList1.size()==Bimp.imgMaxs.get(PConsultApplyFlowActivity.class.getSimpleName())){
			findViewById(R.id.item_img_add).setVisibility(View.GONE);
		}else
			findViewById(R.id.item_img_add).setVisibility(View.VISIBLE);
		for(int i=0;i<imagesList1.size();i++){
			final int index=i;
			MessageImageView image=new MessageImageView(PConsultApplyFlowActivity.this);
			image.setLayoutParams(new LayoutParams(DensityUtils.dip2px(this, 78), DensityUtils.dip2px(this, 78)));
//			image.setLayoutParams(new LayoutParams(getScreenWidth(this),getScreenWidth(this)));
			image.setPadding(10, 0, 10, 0);
			image.setImageBitmap(imagesList.get(i).getBitmap());
			image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(PConsultApplyFlowActivity.this,GalleryActivity.class);
					intent.putExtra("key", PConsultApplyFlowActivity.class.getSimpleName());
					intent.putExtra("position", "1");
					intent.putExtra("ID",index);
					startActivity(intent);
				}
			});
			image.setDeleteListener(new OnClickListener() {//删除图片

				@Override
				public void onClick(View v) {
					Bimp.dataMap.get(PConsultApplyFlowActivity.class.getSimpleName()).remove(index);
					showPhoto(Bimp.dataMap.get(PConsultApplyFlowActivity.class.getSimpleName()));
				}
			});
			images.addView(image);
		}
	}
	/**
	 * 点击取消上传的监听
	 */
	class CancelClickListener implements OnClickSureBtnListener{

		@Override
		public void onClickSureHander() { 
			applyConsult();
		}
	}
	public void onShowLoading(View view) {
		//clear the list and show the loading layout
		mEmptyLayout.showLoading();
	}
	@Override
	public void onBackPressed() {
		DoubleBtnFragmentDialog.show(getSupportFragmentManager(), "提示", "退出后，已填写内容将不会保存，您确定要退出吗？", "确定", "取消", new OnDilaogClickListener() {
			
			@Override
			public void onDismiss(DialogFragment fragment) {
                putSharedPreference();
				finish();
			}
			
			@Override
			public void onClick(DialogFragment fragment, View v) {
				
			}
		});
	}
    /**
     * 设置性别
     */
    private void setXingbie() {
        String[] xingbie = getResources().getStringArray(R.array.sex);
        WheelUtils.setSingleWheel(this, xingbie, mainView, pop, wheelView,
                false);
    }
}
