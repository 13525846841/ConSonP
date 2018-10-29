package com.yksj.consultation.son.consultation;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.yksj.consultation.adapter.BaseListPagerAdpater;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog.OnDilaogClickListener;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.DFgtConsultApplyForm.SwitchFragmentListener;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.ObjectHttpResponseHandler;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.FriendHttpUtil;
import com.yksj.healthtalk.utils.LogUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * @author HEKL
 * 
 *         会诊详情页
 */
public class DAtyConsultDetails extends BaseFragmentActivity implements OnClickListener, OnCheckedChangeListener,
		OnPageChangeListener, SwitchFragmentListener {

	private ViewPager mViewPager;
	private RadioGroup mRadioGroup;
	private ArrayList<Fragment> mlList;
	private BaseListPagerAdpater mAdpater;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	private CustomerInfoEntity entity = null;
	public int state = 0;// 0表示还未同意接诊,1表示同意接诊跳转过来
	int patientId, conId, type, docId, mRefund,expterId;
	String picPath, patientName, titleIllName, docName,expertName;
	public boolean isEditing = false;// 病历是否在编辑
	private boolean isDoctor = SmartFoxClient.getLoginUserInfo().isDoctor();// 是否为医生
	private JSONObject objectDoctor;
	private JSONObject objectExpert;
	private JSONObject objectPatient;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.consultation_details_activity_layout);
		initView();
	}

	private void initView() {
		conId = getIntent().getIntExtra("CONID", 1);
		type = getIntent().getIntExtra("HEKL", 0);
		objectDoctor=new JSONObject();
		objectPatient=new JSONObject();
		// titleIllName = getIntent().getStringExtra("ILLNAME");
		mRefund = getIntent().getIntExtra("STATUS", 0);
		LogUtil.e("CONID", conId + "");
		LogUtil.e("HEKL", type + "");
		// 标题
		initTitle();
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn.setOnClickListener(this);
		mImageView.setOnClickListener(this);
		mImageLoader = ImageLoader.getInstance();
		mOptions = DefaultConfigurationFactory.createHeadDisplayImageOptions(this);
		
		mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
		mRadioGroup.setOnCheckedChangeListener(this);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mAdpater = new BaseListPagerAdpater(getSupportFragmentManager());
		mViewPager.setAdapter(mAdpater);
		mlList = new ArrayList<Fragment>();
		mViewPager.setOnPageChangeListener(this);

		/**
		 * 会诊详情不含报告
		 */
		// 申请单
		DFgtConsultApplyForm allFragment = new DFgtConsultApplyForm();
		allFragment.setSwitchFragListener(this);
		Bundle all = new Bundle();
		all.putInt("type", type);
		all.putInt("CONSULTATIONID", conId);
		all.putInt("REFUND", mRefund);
		allFragment.setArguments(all);
		mlList.add(allFragment);

		// 会诊病历
		Fragment consultationrecord = null;
		Bundle b = new Bundle();
		if (!"0".equals(SmartFoxClient.getLoginUserInfo().getDoctorPosition())) {// 会诊专家
			consultationrecord = new DConsultRecordShowFragment();
			b.putString("consultationId", conId + "");// 传递会诊id
			b.putInt("type", type);
		} else if (SmartFoxClient.getLoginUserInfo().isDoctor()) {// 医生
			if (type == 11) {// 待接诊
				consultationrecord = new BDRecommendTemplateFragment();
				b.putString("consultationId", "" + conId);// 传递会诊id
			} else {// 填病历
				consultationrecord = new DConsultRecordDocFragment();
				b.putString("consultationId", "" + conId);// 传递会诊id
			}
			b.putInt("type", type);
			consultationrecord.setArguments(b);
		} else {// 患者
			consultationrecord = new DConsultRecordFragment();
			if (type == 21)
				b.putString("consultationId", "" + conId);// 传递会诊id
			else
				b.putString("consultationId", "" + conId);// 传递会诊id
			b.putInt("type", type);
		}
		consultationrecord.setArguments(b);
		mlList.add(consultationrecord);
		mAdpater.onBoundFragment(mlList);
		mViewPager.setCurrentItem(0, false);
//		if (0==mViewPager.getCurrentItem()) {
//		AnimationUtils.startGuiPager(this, getClass().getName());
//		}else{
//			return;
//		}
		assistantLoadData();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			onBackPressed();
			break;
		case R.id.title_rigth_pic:
			entity = new CustomerInfoEntity();
			if (type == 21 || type == 22 || type == 24 || type == 25) {
				entity.setName(docName);
				entity.setId(docId + "");
				entity.setConsultationId("" + conId);
				FriendHttpUtil.chatFromPerson(this, entity);
			} else {
				entity.setName(patientName);
				entity.setId(patientId + "");
				FriendHttpUtil.chatFromPerson(this, entity);
			}
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		for (int i = 0; i < group.getChildCount(); i++) {
			RadioButton childAt = (RadioButton) group.getChildAt(i);
			if (childAt.isChecked())
				mViewPager.setCurrentItem(i, false);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		int type1 = getIntent().getIntExtra("HEKL", 0);
		RadioButton mButton = (RadioButton) mRadioGroup.getChildAt(arg0);
		mButton.setChecked(true);
	}

	@Override
	public void switchFragment(int index) {
		state = 1;
		mViewPager.setCurrentItem(index);
	}
	// public void showHead(String picPath, int doctorId, String doctorName) {
	// mImageLoader.displayImage(picPath, mImageView, mOptions);
	// docId = doctorId;
	// docName = doctorName;
	// }

	/**
	 * 助理头像
	 */
	private void assistantLoadData() {
		HttpRestClient.doHttpServerDetailServlet(conId + "", new ObjectHttpResponseHandler() {
			JSONObject object;
			@Override
			public Object onParseResponse(String content) {
				try {
					object = new JSONObject(content);
					if (object.has("errormessage")) {
						ToastUtil.showShort(object.optString("errormessage"));
					}else {
						if (isDoctor) {
							if ("0".equals(SmartFoxClient.getLoginUserInfo().getDoctorPosition())&& object.has("EXPERT")
									&& object.has("PATIENT")) {
								LogUtil.e(this.getClass().getName(),SmartFoxClient.getLoginUserInfo().getDoctorPosition()+"");
								objectExpert = object.getJSONObject("EXPERT");
								objectPatient = object.getJSONObject("PATIENT");
							} else if (object.has("DOCTOR") && object.has("PATIENT")) {
								objectDoctor = object.getJSONObject("DOCTOR");
								objectPatient = object.getJSONObject("PATIENT");
							}
						} else if (object.has("DOCTOR") && object.has("EXPERT")) {
							objectExpert = object.getJSONObject("EXPERT");
							objectDoctor = object.getJSONObject("DOCTOR");
						} else if (object.has("EXPERT") && (!object.has("PATIENT")) && (!object.has("DOCTOR"))) {
							objectExpert = object.getJSONObject("EXPERT");
						}
					}
				} catch (JSONException e) {
					return null;
				}
				return object;
			}

			@Override
			public void onSuccess(Object response) {
				if (type == 21 || type == 22 || type == 24) {
					// titleTextV.setText("我的" + titleIllName + "会诊");
					titleTextV.setText("我的会诊");
					mImageView.setVisibility(View.VISIBLE);
					docId=Integer.valueOf(objectDoctor.optString("DOCTORID"));
					docName=objectDoctor.optString("DOCTORNAME");
					picPath = objectDoctor.optString("DOCTORICON");
					mImageLoader.displayImage(picPath, mImageView, mOptions);
					if (type == 21) {
						switchFragment(1);
					}
				} else if (type == 11 || type == 12 || type == 13 || type == 14) {
					patientName = objectPatient.optString("PATIENTNAME");
					picPath = objectPatient.optString("PATIENTICON");
					patientId =Integer.valueOf(objectPatient.optString("PATIENTID"));
					titleTextV.setText(patientName + "的会诊");
					mImageView.setVisibility(View.VISIBLE);
					mImageLoader.displayImage(picPath, mImageView, mOptions);
					if (type == 12) {
						switchFragment(1);
					}
				} else if (type == 0 || type == 1 || type == 6 || type == 4) {
					patientName = objectPatient.optString("PATIENTNAME");
					titleTextV.setText(patientName + "的会诊");
				}
//				docName = object.optString("ASSISTANT");
//				docId = object.optInt("ASSISTANTID");
//				LogUtil.e("docName+docId", object.optString("ASSISTANT") + object.optInt("ASSISTANTID"));
//				mImageLoader.displayImage(object.optString("ASSISTANTICON"), mImageView, mOptions);
				super.onSuccess(response);
			}
		});
	}

	@Override
	public void onBackPressed() {

		if (isEditing) {// 病历为编辑完成
			DoubleBtnFragmentDialog.show(getSupportFragmentManager(), "提示", "病历尚未编辑完成，您确定要离开吗", "取消", "确定",
					new OnDilaogClickListener() {

						@Override
						public void onDismiss(DialogFragment fragment) {
						}

						@Override
						public void onClick(DialogFragment fragment, View v) {
							DAtyConsultDetails.super.onBackPressed();
						}
					});
		} else {
			super.onBackPressed();
		}
	}
}
