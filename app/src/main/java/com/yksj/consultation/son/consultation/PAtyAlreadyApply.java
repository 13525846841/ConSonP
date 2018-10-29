/**
 * 
 */
package com.yksj.consultation.son.consultation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dmsj.newask.Views.CircleImageView;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.ImageGalleryActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.consultationorders.AtyCancelConsult;
import com.yksj.healthtalk.entity.ApplyFormEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.ObjectHttpResponseHandler;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.LogUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 我的服务-查看详情-已申请
 * 
 * @author zheng
 * 
 */
public class PAtyAlreadyApply extends BaseFragmentActivity implements OnClickListener {

	private RelativeLayout rl1, rl2, rl3, rl4;
	private Button btn;
	private int conID;
	private TextView mIllnessPic, textView, textView1, mTextIllness, mTextIllDetails, mTextPhone, mTextAdress,
			mTextCount, mTextIncome, mConsultApplyTime, mDoctorName, mDoctorName2, mAssistantName, mTextConsultProcess;
	private View mViewLine1, mViewLine2;
	private HorizontalScrollView mView2;
	private Intent intent;
	private ApplyFormEntity entity;// 会诊申请单详情实体
	private LinearLayout mGallery;
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	private DisplayImageOptions mOptions2;
	private CircleImageView mDoctorPic, mDoctorPic2, mDoctorPic3;
	private StringBuilder sb;
	private String titleName, titleIllName, docName, docId;
	private boolean isDoctor = SmartFoxClient.getLoginUserInfo().isDoctor();// 是否为医生
	private JSONObject objectDoctor;
	private JSONObject objectExpert;
	private JSONObject objectPatient;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.aty_already_apply);
		init();
	}

	private void init() {
		initTitle();
		titleTextV.setText("我的会诊");
		titleLeftBtn.setOnClickListener(this);

		rl1 = (RelativeLayout) findViewById(R.id.rl_fillCaseDeleteServer0);
		rl2 = (RelativeLayout) findViewById(R.id.rl_canceled0);
		rl3 = (RelativeLayout) findViewById(R.id.rl_doctor0);
		rl4 = (RelativeLayout) findViewById(R.id.rl_doctor77);
		btn = (Button) findViewById(R.id.btn_fillCaseDeleteServer0);// 取消服务
		conID = getIntent().getIntExtra("CONID", 0);
		mInflater = LayoutInflater.from(this);
		mView2 = (HorizontalScrollView)findViewById(R.id.hs_gallery);
		mGallery = (LinearLayout) findViewById(R.id.ll_gallery0);
		textView = (TextView) findViewById(R.id.tv_canceled0);// 改变文字
		textView1 = (TextView) findViewById(R.id.tv_cancelReason0);// 查看原因
		mIllnessPic = (TextView) findViewById(R.id.tv_illpic0);
		mViewLine1 = findViewById(R.id.view_line);
		mViewLine2 = findViewById(R.id.view_line6);
		// 会诊疾病
		mTextIllness = (TextView) findViewById(R.id.tv_illness0);
		// 病情说明
		mTextIllDetails = (TextView) findViewById(R.id.tv_illdetail0);
		// 手机号码
		mTextPhone = (TextView) findViewById(R.id.tv_phone_content0);
		// 就诊地区
		mTextAdress = (TextView) findViewById(R.id.tv_adress0);
		// 收入
		mTextCount = (TextView) findViewById(R.id.tv_count0);
		// 会诊费用或收入标题
		mTextIncome = (TextView) findViewById(R.id.tv_income0);
		// 申请时间
		mConsultApplyTime = (TextView) findViewById(R.id.tv_applyTime20);
		// 专家姓名
		mDoctorName = (TextView) findViewById(R.id.tv_name0);
		// 专家姓名
		mDoctorName2 = (TextView) findViewById(R.id.tv_name77);
		// 助理姓名

		mAssistantName = (TextView) findViewById(R.id.tv_assis_name88);
		mDoctorPic = (CircleImageView) findViewById(R.id.expert_head0);
		mDoctorPic2 = (CircleImageView) findViewById(R.id.expert_hed88);
		mDoctorPic3 = (CircleImageView) findViewById(R.id.expert_head77);
		if (getIntent().getIntExtra("KEY", 0) == 998) {
			textView.setText("已申请");
			rl1.setVisibility(View.VISIBLE);// 删除服务
			rl2.setOnClickListener(this);
			rl3.setVisibility(View.VISIBLE);
			rl4.setVisibility(View.GONE);
			btn.setOnClickListener(this);// 删除服务按钮
			applyFormLoadData();
			return;
		}
		rl2.setOnClickListener(this);
		textView1.setVisibility(View.VISIBLE);
//		AnimationUtils.startGuiPager(this, getClass().getName());
		applyFormLoadData();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.title_back:
			onBackPressed();
			break;
		case R.id.btn_fillCaseDeleteServer0:
			// ToastUtil.onShow(this, "删除服务按钮", 0);
			intent = new Intent(this, AtyCancelConsult.class);
			intent.putExtra("FLAG", 0);
			intent.putExtra("CONID", "" + conID);
			startActivity(intent);
			break;
		case R.id.rl_canceled0:
			if (getIntent().getIntExtra("KEY", 0) == 998) {// 已申请查看流程
				intent = new Intent(this, PConsultApplyActivity.class);
				intent.putExtra("conId", getIntent().getIntExtra("CONID", 0));
				startActivity(intent);
			} else {// 查看原因
				intent = new Intent(this, DAtyConsultCancelReason.class);
				intent.putExtra("conId", getIntent().getIntExtra("CONID", 0));
				startActivity(intent);
			}
			break;
		case R.id.ll_gallery:// 申请单图片点击放大
			String[] array = new String[entity.getPics().size()];
			for (int i = 0; i < entity.getPics().size(); i++) {
				array[i] = entity.getPics().get(i).get("BIG").toString();
			}
			intent = new Intent(this, ImageGalleryActivity.class);
			intent.putExtra(ImageGalleryActivity.URLS_KEY, array);
			intent.putExtra(ImageGalleryActivity.TYPE_KEY, 1);// 0,1单个,多个
			intent.putExtra("type", 1);// 0,1单个,多个
			startActivity(intent);
			break;
		}
	}

	/**
	 * 申请单详情数据下载
	 */
	private void applyFormLoadData() {
		HttpRestClient.doHttpServerDetailServlet(conID + "", new ObjectHttpResponseHandler(this) {
			JSONObject object;
			ArrayList<HashMap<String, String>> list = null;

			@Override
			public Object onParseResponse(String content) {
				try {
					object = new JSONObject(content);
					if (object.has("errormessage")) {
						ToastUtil.showShort(object.optString("errormessage"));
					} else {
						entity = new ApplyFormEntity();
						entity.setName(object.optString("NAME"));
						entity.setCondesc(object.optString("CONDESC"));
						entity.setPatientId(object.optInt("PATIENTID"));
						entity.setPhone(object.optString("PHONE"));
						entity.setArea(object.optString("AREA"));
						entity.setPrice(object.optInt("PRICE"));
						entity.setTime(object.optString("TIME"));
						entity.setStauts(object.optInt("STATUS"));
						entity.setAssistantId(object.optInt("ASSISTANTID"));
						// entity.setAssistant(object.optString("ASSISTANT"));
						// entity.setaId(object.optString("AID"));''
						// entity.setDoctor(object.optString("DOCTOR"));
						// entity.setDoctorIcon(object.optString("DOCTORICON"));
						// entity.setAssistantIcon(object.optString("ASSISTANTICON"));
						entity.setPatientPic(object.optString("PATIENTPIC"));
						entity.setStatusName(object.optString("STATUSNAME"));
						JSONArray picsArray = object.getJSONArray("PICS");
						if (isDoctor) {
							if ("0".equals(SmartFoxClient.getLoginUserInfo().getDoctorPosition()) && object.has("EXPERT")
									&& object.has("PATIENT")) {
								objectExpert = object.getJSONObject("EXPERT");
								objectPatient = object.getJSONObject("PATIENT");
							} else if (!"0".equals(SmartFoxClient.getLoginUserInfo().getDoctorPosition())
									&& object.has("DOCTOR") && object.has("PATIENT")) {
								objectDoctor = object.getJSONObject("DOCTOR");
								objectPatient = object.getJSONObject("PATIENT");
							}
						} else if (isDoctor == false && object.has("DOCTOR") && object.has("EXPERT")) {
							objectExpert = object.getJSONObject("EXPERT");
							objectDoctor = object.getJSONObject("DOCTOR");
						} else if (isDoctor == false && object.has("EXPERT") && (!object.has("PATIENT"))
								&& (!object.has("DOCTOR"))) {
							objectExpert = object.getJSONObject("EXPERT");
						}
						LogUtil.e("STATUS", "" + object.optInt("STATUS"));
						list = new ArrayList<HashMap<String, String>>();
						for (int t = 0; t < picsArray.length(); t++) {
							JSONObject obj = picsArray.getJSONObject(t);
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("ID", "" + obj.optInt("ID"));
							map.put("SMALL", obj.optString("SMALL"));
							map.put("BIG", obj.optString("BIG"));
							map.put("SEQ", "" + obj.optInt("SEQ"));
							LogUtil.e("SEQ", "" + obj.optInt("SEQ"));
							LogUtil.e("BIG", obj.optString("BIG"));
							LogUtil.e("SMALL", obj.optString("SMALL"));
							LogUtil.e("ID", "" + obj.optInt("ID"));
							list.add(map);
						}
						entity.setPics(list);
						LogUtil.e("PICSSIZE", list.size() + "");
						LogUtil.e("ApplyFormEntity", entity.toString() + "entity");

					}
				} catch (JSONException e) {
					return null;
				}
				return entity;
			}

			@Override
			public void onSuccess(Object response) {
				if (response instanceof ApplyFormEntity) {
					entity = (ApplyFormEntity) response;
					applyFormShowData();// 申请单展示
				}
				;
				super.onSuccess(response);
			}
		});
	}

	/**
	 * 申请单详情数据匹配
	 * 
	 */
	protected void applyFormShowData() {
		mTextIllness.setText(entity.getName().toString());// 会诊疾病
		mTextIllDetails.setText(entity.getCondesc().toString());// 病情说明
		mTextPhone.setText(entity.getPhone() + "");// 手机号码
		mTextAdress.setText(entity.getArea().toString());// 就诊地区
		mTextCount.setText(entity.getPrice() + "");// 收入或会诊费用
		mConsultApplyTime.setText(entity.getTime().toString());// 申请时间
		mDoctorName2.setText(objectExpert.optString("EXPERTNAME"));// 专家姓名
		mDoctorName.setText(objectExpert.optString("EXPERTNAME"));// 专家姓名
		mAssistantName.setText(objectDoctor.optString("DOCTORNAME"));// 助理姓名
		mImageLoader = ImageLoader.getInstance();
		mOptions = DefaultConfigurationFactory.createHeadDisplayImageOptions(this);
		mOptions2 = DefaultConfigurationFactory.createApplyPicDisplayImageOptions(this);
		mImageLoader.displayImage(objectExpert.optString("EXPERTICON"), mDoctorPic, mOptions);
		mImageLoader.displayImage(objectExpert.optString("EXPERTICON"), mDoctorPic3, mOptions);
		mImageLoader.displayImage(objectDoctor.optString("DOCTORICON"), mDoctorPic2, mOptions);
		if ("".equals(objectDoctor.optString("DOCTORNAME"))) {
			rl3.setVisibility(View.VISIBLE);
			rl4.setVisibility(View.GONE);
		}
		// mImageLoader.displayImage(objectExpert.optString("EXPERTICON"),
		// mDoctorPic3, mOptions);// 单个个头像会诊医生
		if (entity.getPics().size() == 0) {
			mView2.setVisibility(View.GONE);
			mIllnessPic.setVisibility(View.GONE);
			mViewLine1.setVisibility(View.GONE);
			mViewLine2.setVisibility(View.GONE);
		} else if (entity.getPics().size() > 0) {
			sb = new StringBuilder();
			for (int i = 0; i < entity.getPics().size(); i++) {
				if (i == entity.getPics().size() - 1)
					sb.append(entity.getPics().get(i).get("BIG"));
				else
					sb.append(entity.getPics().get(i).get("BIG") + ",");
			}
			for (int i = 0; i < entity.getPics().size(); i++) {
				final int imgPosition = i;
				LogUtil.e("PICSIZE", entity.getPics().size() + "");
				View view = mInflater.inflate(R.layout.aty_applyform_gallery, mGallery, false);
				ImageView img = (ImageView) view.findViewById(R.id.image_illpic);
				img.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(PAtyAlreadyApply.this, ImageGalleryActivity.class);
						intent.putExtra(ImageGalleryActivity.URLS_KEY, sb.toString().split(","));
						intent.putExtra(ImageGalleryActivity.TYPE_KEY, 1);
						intent.putExtra("type", 1);// 0,1单个,多个
						intent.putExtra("position", imgPosition);
						startActivityForResult(intent, 100);
					}
				});
				mImageLoader.displayImage(entity.getPics().get(i).get("SMALL").toString(), img, mOptions2);
				LogUtil.e("ImageView", entity.getPics().get(i).get("SMALL").toString());
				mGallery.addView(view);
			}
		}
	}

}
