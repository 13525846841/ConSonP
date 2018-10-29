package com.yksj.consultation.son.consultation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dmsj.newask.Views.CircleImageView;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog.OnDilaogClickListener;
import com.yksj.consultation.comm.ImageGalleryActivity;
import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.comm.SingleBtnFragmentDialog.OnClickSureBtnListener;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.consultationorders.AtyCancelConsult;
import com.yksj.healthtalk.entity.ApplyFormEntity;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.ObjectHttpResponseHandler;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.LogUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 申请单详情
 * 
 * @author HEKL
 */
public class DFgtConsultApplyForm extends RootFragment implements OnClickListener, PullToRefreshBase.OnRefreshListener2<ScrollView> {

	private static final int ACTIVITY_FINISH = 401;//销毁本界面
	private Button mBtnAgree, mBtnReject, mBtnGoToPay, mBtnDeleteService, mBtnget, mBtnRejectConsult, mBtnSendToExpert,
			mBtnDelete, mBtnRefund1, mBtnRefund2, mBtnRefund3;
	private TextView mIllnessPic, mTextIllness, mTextIllDetails, mTextPhone, mTextAdress, mTextCount,
			mTextConsultProcess, mTextIncome, mConsultApplyTime, mCharacterName, mExpertName, mCharacterName2,
			mCharacterType1, mCharacterType2;
	private RelativeLayout mJoinApply, mWaitingAgree, mCanceled, mGoToPay, mWaitRefund, mWaitRefund2, mWaitRefund3,
			mGetconsultation, mGetconsultation2, mSendToExpert, mDelete, mDoctor1, mDoctor2;
	private ImageView mArrow;//查看流程箭头
	private HorizontalScrollView mView2;
	private com.yksj.consultation.son.views.PullToRefreshScrollView mPullToRefreshScrollView;
	private View mView;// 不重要
	private View mViewLine1, mViewLine2;
	private LinearLayout mGallery;
	private LayoutInflater mInflater;
	private ApplyFormEntity entity;// 会诊申请单详情实体
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	private DisplayImageOptions mOptions2;
	private CircleImageView mCharacterPic, mCharacterPic2, mCharacterPic3;
	private JSONObject object;
	private int consultationId, mRefund;// 会诊id
	private int TYPE;// 判断显示
	private int saveState = 0;// 是否是第一次查看
	public int Status;// 判断状态
	public String small;// 助理头像地址
	public int doctorId;// 助理Id
	private String[] array = null;
	public SwitchFragmentListener switchlistener;
	private ArrayList<HashMap<String, String>> list = null;
	private boolean isDoctor = SmartFoxClient.getLoginUserInfo().isDoctor();// 是否为医生
	private JSONObject objectDoctor;
	private JSONObject objectExpert;
	private JSONObject objectPatient;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.application_form_fragment_layout, null);
		initView(view);
		return view;
	}

	@Override
	public void onStart() {
		if (saveState == 1) {
			applyFormLoadData();
		}
		super.onStart();
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		saveState = 1;
		super.onSaveInstanceState(outState);
	}

	private void initView(View view) {
		consultationId = getArguments().getInt("CONSULTATIONID");// 会诊Id
		TYPE = getArguments().getInt("type");
		mPullToRefreshScrollView = (com.yksj.consultation.son.views.PullToRefreshScrollView) view.findViewById(R.id.pull_refresh_scrollview);
		mPullToRefreshScrollView.setOnRefreshListener((com.yksj.consultation.son.views.PullToRefreshBase.OnRefreshListener) this);
		mBtnAgree = (Button) view.findViewById(R.id.btn_agree);
		mBtnReject = (Button) view.findViewById(R.id.btn_reject);
		mView = view.findViewById(R.id.view_delete);
		mBtnAgree.setOnClickListener(this);
		mBtnReject.setOnClickListener(this);
		mView2 = (HorizontalScrollView) view.findViewById(R.id.hs_gallery);
		mGallery = (LinearLayout) view.findViewById(R.id.ll_gallery);
		mGallery.setOnClickListener(this);
		mIllnessPic = (TextView) view.findViewById(R.id.tv_illpic);
		mViewLine1 = view.findViewById(R.id.view_line);
		mViewLine2 = view.findViewById(R.id.view_line6);
		mInflater = LayoutInflater.from(mActivity);
		// 会诊疾病
		mTextIllness = (TextView) view.findViewById(R.id.tv_illness);
		// 病情说明
		mTextIllDetails = (TextView) view.findViewById(R.id.tv_illdetail);
		// 手机号码
		mTextPhone = (TextView) view.findViewById(R.id.tv_phone_content);
		// 就诊地区
		mTextAdress = (TextView) view.findViewById(R.id.tv_adress);
		// 收入
		mTextCount = (TextView) view.findViewById(R.id.tv_count);
		// 会诊费用或收入标题
		mTextIncome = (TextView) view.findViewById(R.id.tv_income);
		// 申请时间
		mConsultApplyTime = (TextView) view.findViewById(R.id.tv_applyTime2);
		// 专家姓名
		mCharacterName = (TextView) view.findViewById(R.id.tv_name);
		mExpertName = (TextView) view.findViewById(R.id.tv_expert_name);
		// 助理姓名
		mCharacterName2 = (TextView) view.findViewById(R.id.tv_assis_name);
		// 会诊流程状态名称
		mTextConsultProcess = (TextView) view.findViewById(R.id.tv_consultProcess);

		mCharacterPic = (CircleImageView) view.findViewById(R.id.expert_head);
		mCharacterPic2 = (CircleImageView) view.findViewById(R.id.expert_hed);
		mCharacterPic3 = (CircleImageView) view.findViewById(R.id.expert_head2);

		mArrow= (ImageView) view.findViewById(R.id.image_arrow);
		mJoinApply = (RelativeLayout) view.findViewById(R.id.rl_joinapply);
		mDoctor1 = (RelativeLayout) view.findViewById(R.id.rl_doctor);
		mDoctor2 = (RelativeLayout) view.findViewById(R.id.rl_doctor2);
		mWaitingAgree = (RelativeLayout) view.findViewById(R.id.rl_waitingAgreement);
		mCanceled = (RelativeLayout) view.findViewById(R.id.rl_canceled);
		// 去支付
		mGoToPay = (RelativeLayout) view.findViewById(R.id.rl_gotoPay);
		mBtnGoToPay = (Button) view.findViewById(R.id.btn_gotoPay);
		mBtnDeleteService = (Button) view.findViewById(R.id.btn_deleteServer);
		mBtnGoToPay.setOnClickListener(this);
		mBtnDeleteService.setOnClickListener(this);

		mJoinApply.setOnClickListener(this);
		mCanceled.setOnClickListener(this);
		// 待退款
		mWaitRefund = (RelativeLayout) view.findViewById(R.id.rl_waitingRefund);
		mWaitRefund2 = (RelativeLayout) view.findViewById(R.id.rl_waitingRefund2);
		mWaitRefund3 = (RelativeLayout) view.findViewById(R.id.rl_waitingRefund3);
		// 待接诊
		mGetconsultation = (RelativeLayout) view.findViewById(R.id.rl_getconsultation);
		mGetconsultation2 = (RelativeLayout) view.findViewById(R.id.rl_getconsultation2);
		mBtnget = (Button) view.findViewById(R.id.btn_get);
		mBtnRejectConsult = (Button) view.findViewById(R.id.btn_rejectconsultation);
		mBtnget.setOnClickListener(this);
		mBtnRejectConsult.setOnClickListener(this);
		// 取消服务
		mDelete = (RelativeLayout) view.findViewById(R.id.rl_fillCaseDeleteServer);
		mBtnDelete = (Button) view.findViewById(R.id.btn_fillCaseDeleteServer);
		mBtnDelete.setOnClickListener(this);
		// 待退款
		mBtnRefund1 = (Button) view.findViewById(R.id.btn_refundzfb);
		mBtnRefund1.setOnClickListener(this);
		mBtnRefund2 = (Button) view.findViewById(R.id.btn_refundBankCards);
		mBtnRefund2.setOnClickListener(this);
		mBtnRefund3 = (Button) view.findViewById(R.id.btn_refundPurse);
		mBtnRefund3.setOnClickListener(this);
		mCharacterType1 = (TextView) view.findViewById(R.id.tv_characterName1);
		mCharacterType2 = (TextView) view.findViewById(R.id.tv_characterName2);
		applyFormLoadData();
		// mTextConsultProcess.setText(entity.getStatusName().toString());
		switch (TYPE) {
		case 0:// 资深医生 待同意
			mWaitingAgree.setVisibility(View.VISIBLE);
			mTextIncome.setText("收入(元)");
			// mTextConsultProcess.setText("待同意");
			break;
		case 1:// 资深医生 待付款
				// mTextConsultProcess.setText("待付款");
			mTextIncome.setText("收入(元)");
			break;
		case 2:// 资深医生 给意见
				// mTextConsultProcess.setText("给意见");
			mTextIncome.setText("收入(元)");
			break;
		case 3:// 资深和基层医生 已完成
				// mTextConsultProcess.setText("已完成");
			mTextIncome.setText("收入(元)");
			break;
		case 4:// 资深和基层医生和患者 已取消
			mJoinApply.setVisibility(View.GONE);
			mTextIncome.setText("收入(元)");
			mCanceled.setVisibility(View.VISIBLE);
			break;
		case 6:
			mJoinApply.setVisibility(View.VISIBLE);
			mTextConsultProcess.setText("已申请");
			mDelete.setVisibility(View.VISIBLE);
			mTextIncome.setText("会诊费用(元)");
			mDoctor1.setVisibility(view.GONE);
			mDoctor2.setVisibility(view.VISIBLE);
			break;
		case 12:// 医生端 填病历
			mTextIncome.setText("收入(元)");
			// mTextConsultProcess.setText("填写病历");
			break;
		case 11:// 医生端 待接诊
			mTextIncome.setText("收入(元)");
			break;
		case 13:// 医生端 待同意
			mTextIncome.setText("收入(元)");
			// mTextConsultProcess.setText("等待专家同意");
			break;
		case 14:// 医生端 待付款
			mTextIncome.setText("收入(元)");
			// mTextConsultProcess.setText("等待患者付款中");
			break;
		case 15:// 医生端 待服务
			mTextIncome.setText("收入(元)");
			// mTextConsultProcess.setText("待服务");
			break;
		case 21:// 患者端 填病历取消服务
			mDelete.setVisibility(View.VISIBLE);
			mTextIncome.setText("会诊费用(元)");
			// mTextConsultProcess.setText("填病历");
			break;
		case 22:// 患者端 待支付
			// mTextConsultProcess.setText("待支付");
			mGoToPay.setVisibility(View.VISIBLE);
			mTextIncome.setText("会诊费用(元)");
			break;
		case 23:// 患者端 待服务
			// mTextConsultProcess.setText("待服务");
			mTextIncome.setText("会诊费用(元)");
			break;
		// case 24:
		//
		// break;
		case 25:// 患者端 已完成
			// mTextConsultProcess.setText("已完成");
			mTextIncome.setText("会诊费用(元)");
			break;
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_agree:// 资深医生 待同意
			receiveCosultation(consultationId, 7);
			break;
		case R.id.btn_reject:// 资深医生拒绝会诊和取消服务原因
			intent = new Intent(mActivity.getApplicationContext(), AtyCancelConsult.class);
			intent.putExtra("CONID", consultationId + "");
			intent.putExtra("KEY", 1);
			startActivity(intent);
			break;
		case R.id.rl_canceled:// 会诊取消原因
			intent = new Intent(mActivity.getApplicationContext(), DAtyConsultCancelReason.class);
			intent.putExtra("conId", consultationId);
			startActivity(intent);
			break;
		case R.id.btn_get:// 助理接诊
			if (Status == 10) {
				receiveCosultation(consultationId, 5);
			} else {
				// mTextConsultProcess.setText("已接诊");
				switchlistener.switchFragment(1);
				mJoinApply.setVisibility(View.VISIBLE);
				mGetconsultation.setVisibility(View.GONE);
				mGetconsultation2.setVisibility(View.GONE);
			}
			break;
		case R.id.rl_joinapply:// 会诊流程
			intent = new Intent(mActivity, PConsultApplyActivity.class);
			intent.putExtra("conId", consultationId);
			startActivity(intent);
			break;
		case R.id.btn_rejectconsultation:// 拒绝接诊
			DoubleBtnFragmentDialog.showDefault(getFragmentManager(), "您确定拒绝该会诊吗？", "取消", "确定",
					new OnDilaogClickListener() {

						@Override
						public void onDismiss(DialogFragment fragment) {

						}

						@Override
						public void onClick(DialogFragment fragment, View v) {
							rejectConsultation();
						}
					});

			break;
		case R.id.btn_fillCaseDeleteServer:// 取消服务
			intent = new Intent(mActivity, AtyCancelConsult.class);
			intent.putExtra("CONID", consultationId + "");
			intent.putExtra("KEY", 0);
			startActivity(intent);
			break;
		case R.id.btn_gotoPay:// 去支付
			intent = new Intent(mActivity.getApplicationContext(), PAtyConsultGoPaying.class);
			intent.putExtra("conId", consultationId);
			startActivityForResult(intent,ACTIVITY_FINISH);
			break;
		case R.id.btn_deleteServer:// 支付中的取消服务
			intent = new Intent(mActivity.getApplicationContext(), AtyCancelConsult.class);
			intent.putExtra("CONID", consultationId + "");
			intent.putExtra("KEY", 0);
			startActivity(intent);
			break;
		case R.id.btn_refundzfb:// 退款到支付宝
			intent = new Intent(getActivity(), ConsultationBackPayActivity.class);
			intent.putExtra("type", 1);// 支付宝
			intent.putExtra("conId", consultationId);
			startActivityForResult(intent, 2000);
			break;
		case R.id.btn_refundBankCards:// 退款到银行卡
			intent = new Intent(getActivity(), ConsultationBackPayActivity.class);
			intent.putExtra("type", 2);// 银联
			intent.putExtra("conId", consultationId);
			startActivityForResult(intent, 3000);
			break;
		case R.id.btn_refundPurse:// 退款到钱包
			DoubleBtnFragmentDialog.showDefault(getChildFragmentManager(), "为了您的账户安全，存入钱包后请绑定手机并设置支付密码。您确定将会诊金额（"
					+ entity.getPrice() + "元）存入钱包吗？", "放弃", "确定", new OnDilaogClickListener() {
				@Override
				public void onDismiss(DialogFragment fragment) {
				}

				@Override
				public void onClick(DialogFragment fragment, View v) {
					RequestParams params = new RequestParams();
					params.put("OPTION", "1");
					params.put("CONSULTATIONID", consultationId + "");

					HttpRestClient.doHttpConsultationBackPay(params, new AsyncHander(R.id.wallet));
				}
			});
			break;
		case R.id.ll_gallery:// 申请单图片点击放大
			String[] array = new String[entity.getPics().size()];
			for (int i = 0; i < entity.getPics().size(); i++) {
				array[i] = entity.getPics().get(i).get("BIG").toString();
			}
			intent = new Intent(mActivity, ImageGalleryActivity.class);
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
		HttpRestClient.doHttpServerDetailServlet(consultationId + "", new ObjectHttpResponseHandler() {

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
						// entity.setaId(object.optString("AID"));
						// entity.setDoctor(object.optString("DOCTOR"));
						// dActivity.showHead(entity.getAssistantIcon().toString(),
						// entity.getAssistantId(),
						// entity.getAssistant().toString());
						// entity.setDoctorIcon(object.optString("DOCTORICON"));
						entity.setAssistantIcon(object.optString("ASSISTANTICON"));
						entity.setPatientPic(object.optString("PATIENTPIC"));
						entity.setStatusName(object.optString("STATUSNAME"));
						entity.setPromoterType(object.optString("PROMOTER_TYPE"));
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
			public void onStart() {

				super.onStart();
			}

			@Override
			public void onFailure(Throwable error) {

				super.onFailure(error);
			}

			@Override
			public void onFinish() {
				mPullToRefreshScrollView.onRefreshComplete();

				super.onFinish();
			}

			@Override
			public void onSuccess(Object response) {
				if (response instanceof ApplyFormEntity) {
					entity = (ApplyFormEntity) response;
					applyFormShowData();// 申请单展示
				}
				super.onSuccess(response);
			}
		});
	}

	/**
	 * 申请单详情数据匹配
	 */
	protected void applyFormShowData() {
		// 会诊疾病
//		mTextIllness.setText(entity.getName().toString());
		mTextIllness.setText(object.optString("NAME"));
		mTextIllDetails.setText(entity.getCondesc().toString());// 病情说明
		mTextPhone.setText(entity.getPhone() + "");// 手机号码
		mTextAdress.setText(entity.getArea().toString());// 就诊地区
		mTextCount.setText(entity.getPrice() + "");// 收入或会诊费用
		mConsultApplyTime.setText(entity.getTime().toString());// 申请时间
		mImageLoader = ImageLoader.getInstance();
		mOptions = DefaultConfigurationFactory.createHeadDisplayImageOptions(mActivity);
		mOptions2 = DefaultConfigurationFactory.createApplyPicDisplayImageOptions(mActivity);
		if ("20".equals(object.optString("PROMOTER_TYPE"))){
			mArrow.setVisibility(View.GONE);
			mJoinApply.setClickable(false);
		}
		try {
			Status = object.getInt("STATUS");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (TYPE == 11) {
			switch (Status) {
			case 10:
				mDoctor1.setVisibility(View.GONE);
				mDoctor2.setVisibility(View.VISIBLE);
				mGetconsultation.setVisibility(View.VISIBLE);
				mGetconsultation2.setVisibility(View.INVISIBLE);
				mView.setVisibility(View.GONE);
				break;
			default:
				if (entity.getAssistantId() != Integer.valueOf(SmartFoxClient.getLoginUserId())) {
					mDoctor1.setVisibility(View.GONE);
					mDoctor2.setVisibility(View.VISIBLE);
					mGetconsultation.setVisibility(View.GONE);
					mGetconsultation2.setVisibility(View.GONE);
					ToastUtil.showShort("很遗憾，该患者已被其他医生接诊了。");
					mActivity.finish();
				} else if (entity.getAssistantId() == Integer.valueOf(SmartFoxClient.getLoginUserId())) {
					mDoctor1.setVisibility(View.VISIBLE);
					mDoctor2.setVisibility(View.GONE);
					mView.setVisibility(View.GONE);
					mTextConsultProcess.setText(entity.getStatusName().toString());
				}
				break;
			}

		} else if (TYPE == 22) {//
			switch (Status) {
			case 80:
				mGoToPay.setVisibility(View.GONE);
				break;
			}
		} else if (TYPE == 24) {// 患者端 待退款
			switch (Status) {
			case 222:
				mWaitRefund.setVisibility(View.VISIBLE);
				mTextIncome.setText("会诊费用(元)");
				mJoinApply.setVisibility(View.GONE);
				break;
			case 232:
				mWaitRefund2.setVisibility(View.VISIBLE);
				mTextIncome.setText("会诊费用(元)");
				mJoinApply.setVisibility(View.GONE);
				break;
			case 242:
				mWaitRefund3.setVisibility(View.VISIBLE);
				mTextIncome.setText("会诊费用(元)");
				mJoinApply.setVisibility(View.GONE);
				break;
			}
		}
		if (isDoctor) {
			if ("0".equals(SmartFoxClient.getLoginUserInfo().getDoctorPosition())) {
				mCharacterName.setText(objectExpert.optString("EXPERTNAME"));// 人物姓名1
				mCharacterName2.setText(objectPatient.optString("PATIENTNAME"));// 人物姓名2
				mImageLoader.displayImage(objectExpert.optString("EXPERTICON"), mCharacterPic, mOptions);// 双个头像会诊专家
				mCharacterType1.setText("会诊专家");
				mExpertName.setText(objectExpert.optString("EXPERTNAME"));// 专家姓名
				mImageLoader.displayImage(objectExpert.optString("EXPERTICON"), mCharacterPic3, mOptions);// 单个个头像会诊医生
			} else {
				if (objectDoctor.optString("DOCTORNAME")==null||objectPatient.optString("PATIENTNAME")==null||objectDoctor.optString("DOCTORICON")==null){
					return;
				}else{
					mCharacterName.setText(objectDoctor.optString("DOCTORNAME"));// 人物姓名1
					mCharacterName2.setText(objectPatient.optString("PATIENTNAME"));// 人物姓名2
					mImageLoader.displayImage(objectDoctor.optString("DOCTORICON"), mCharacterPic, mOptions);// 双个头像会诊医生
					mCharacterType1.setText("会诊医生");
				}
			}
			mCharacterType2.setText("患者");
			mImageLoader.displayImage(objectPatient.optString("PATIENTICON"), mCharacterPic2, mOptions);// 双个头像患者
		} else {
			mCharacterName.setText(objectExpert.optString("EXPERTNAME"));// 人物姓名1
			mCharacterName2.setText(objectDoctor.optString("DOCTORNAME"));// 人物姓名2
			mImageLoader.displayImage(objectExpert.optString("EXPERTICON"), mCharacterPic, mOptions);// 双个头像会诊专家
			mImageLoader.displayImage(objectDoctor.optString("DOCTORICON"), mCharacterPic2, mOptions);// 双个头像会诊医生
			mCharacterType1.setText("会诊专家");
			mCharacterType2.setText("会诊医生");
			mExpertName.setText(objectExpert.optString("EXPERTNAME"));// 专家姓名
			mImageLoader.displayImage(objectExpert.optString("EXPERTICON"), mCharacterPic3, mOptions);// 单个个头像会诊医生
		}

		mTextConsultProcess.setText(entity.getStatusName().toString());
		if (entity.getPics().size() == 0) {
			mView2.setVisibility(View.GONE);
			mIllnessPic.setVisibility(View.GONE);
			mViewLine1.setVisibility(View.GONE);
			mViewLine2.setVisibility(View.GONE);
		} else if (entity.getPics().size() > 0) {
			array = new String[entity.getPics().size()];
			for (int t = 0; t < entity.getPics().size(); t++) {
				array[t] = entity.getPics().get(t).get("BIG").toString();
			}
			for (int i = 0; i < entity.getPics().size(); i++) {
				final int index = i;
				LogUtil.e("PICSIZE", entity.getPics().size() + "");
				View view = mInflater.inflate(R.layout.aty_applyform_gallery, mGallery, false);
				ImageView img = (ImageView) view.findViewById(R.id.image_illpic);
				mImageLoader.displayImage(entity.getPics().get(i).get("SMALL").toString(), img, mOptions2);
				LogUtil.e("ImageView", entity.getPics().get(i).get("SMALL").toString());
				img.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mActivity, ImageGalleryActivity.class);
						intent.putExtra(ImageGalleryActivity.URLS_KEY, array);
						intent.putExtra(ImageGalleryActivity.TYPE_KEY, 1);
						intent.putExtra("type", 1);// 0,1单个,多个
						intent.putExtra("position", index);
						startActivity(intent);
					}
				});
				mGallery.addView(view);
			}
		}
	}

	/**
	 * 基层医生和会诊专家接诊
	 */
	private void receiveCosultation(int consultationId, final int option) {

		// http://220.194.46.204:8080/DuoMeiHealth/ServerDetailServlet?CONSULTATIONID=460&OPTION=1
		HttpRestClient.doHttpReceiveConsultation(consultationId, option, new AsyncHttpResponseHandler(mActivity) {
			@Override
			public void onStart() {
				mGetconsultation2.setVisibility(View.GONE);
				mGetconsultation.setVisibility(View.GONE);
				super.onStart();
			}

			@Override
			public void onFailure(Throwable error, String content) {
				mGetconsultation.setVisibility(View.GONE);
				mGetconsultation2.setVisibility(View.GONE);
				super.onFailure(error, content);
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				try {
					JSONObject obj = new JSONObject(content);
					if (obj.has("errormessage")) {
						ToastUtil.showShort(obj.optString("errormessage"));
					} else {
						SingleBtnFragmentDialog.showDefault(getChildFragmentManager(), obj.optString("INFO"));
						switch (option) {
						case 5:
							mTextConsultProcess.setText("填病历");
							switchlistener.switchFragment(1);
							mJoinApply.setVisibility(View.VISIBLE);
							mGetconsultation.setVisibility(View.GONE);
							mGetconsultation2.setVisibility(View.GONE);
							break;
						case 7:
							mTextConsultProcess.setText("待付款");
							mWaitingAgree.setVisibility(View.GONE);
							break;
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				super.onSuccess(statusCode, content);
			}
		});
	}

	class AsyncHander extends AsyncHttpResponseHandler {
		private int id;

		public AsyncHander(int id) {
			super(getActivity());
			this.id = id;
		}

		@Override
		public void onSuccess(int statusCode, String content) {
			try {
				super.onSuccess(statusCode, content);
				JSONObject object2 = null;
				if (content != null)
					object2 = new JSONObject(content);

				switch (id) {
				case R.id.wallet:// 退回钱包
					if (!object2.has("errormessage"))
						SingleBtnFragmentDialog.showDefault(getChildFragmentManager(), "存入钱包成功",
								new OnClickSureBtnListener() {

									@Override
									public void onClickSureHander() {
										mWaitRefund.setVisibility(View.GONE);
									}
								});
					else {
						ToastUtil.showShort(object2.optString("errormessage"));
					}
					break;
				}

			} catch (Exception e) {
			}
		}

	}

	/**
	 * 基础医生拒绝会诊
	 */
	private void rejectConsultation() {
		RequestParams params = new RequestParams();
		params.put("OPTION", "4");
		params.put("CONSULTATIONID", consultationId + "");
		params.put("DOCTORID", SmartFoxClient.getLoginUserId());
		HttpRestClient.doHttpDoctorService(params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				JSONObject object;
				try {
					object = new JSONObject(content);
					if (content.contains("errormessage")) {// 错误
						ToastUtil.showShort(object.getString("errormessage"));
					} else {
						ToastUtil.showShort(object.getString("INFO"));
						mActivity.finish();
						// mSendToExpert.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(content);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null || resultCode != Activity.RESULT_OK)
			return;
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case ACTIVITY_FINISH:
				getActivity().finish();
				break;
		case 2000:// 支付宝退款成功
			mWaitRefund.setVisibility(View.GONE);
			break;
		case 3000:// 银行卡退款成功
			mWaitRefund.setVisibility(View.GONE);
			break;
		}
	}

	public void setSwitchFragListener(SwitchFragmentListener l) {
		switchlistener = l;
	}

	public interface SwitchFragmentListener {
		public void switchFragment(int index);
	}

	// 下拉刷新
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		mGallery.removeAllViews();
		applyFormLoadData();
	}

	// 上拉刷新
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		mGallery.removeAllViews();
		applyFormLoadData();
	}

	@Override
	public void onStop() {
		mGallery.removeAllViews();
		super.onStop();
	}
}
