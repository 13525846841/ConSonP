package com.yksj.consultation.son.consultation.old;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.iflytek.cloud.SpeechUtility;
import com.iflytek.sunflower.FlowerCollector;
import com.yksj.consultation.adapter.BaseListPagerAdpater;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.DConsultRecordDocFragment;
import com.yksj.consultation.son.consultation.DConsultRecordShowFragment;
import com.yksj.consultation.son.consultation.DFgtConsultApplyForm;
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
 *会诊详情带报告页
 */
public class DAtyConsultSuggestion extends BaseFragmentActivity implements OnClickListener, OnCheckedChangeListener,
		OnPageChangeListener {

	private static final String TAG = DAtyConsultSuggestion.class.getSimpleName();
	private ViewPager mViewPager;
	private RadioGroup mRadioGroup;
	private BaseListPagerAdpater mAdpater;
	private ArrayList<Fragment> mList;
	private CustomerInfoEntity entity = null;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	private int type;// 会诊报告类型符
	private int conId;
	DFgtConsultReport payingFragment;
	private String titleName, titleIllName, docName,docId;
	private boolean isDoctor = SmartFoxClient.getLoginUserInfo().isDoctor();// 是否为医生
	private JSONObject objectDoctor;
	private JSONObject objectExpert;
	private JSONObject objectPatient;
	private JSONObject objectExpertId;

	@Override
	protected void onCreate(Bundle arg0) {
		SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));
		super.onCreate(arg0);
		setContentView(R.layout.consultation_suggestion_activity_layout);
		initView();
	}

	private void initView() {
		initTitle();
		titleLeftBtn.setOnClickListener(this);
		conId = getIntent().getIntExtra("CONID", 1);
		LogUtil.e("SUGGESTION", conId + "");
		type = getIntent().getIntExtra("HEKL", 0);
//		titleName = getIntent().getStringExtra("CUSTOMERNAME");
//		titleIllName = getIntent().getStringExtra("ILLNAME");
		mImageLoader = ImageLoader.getInstance();
		mOptions = DefaultConfigurationFactory.createHeadDisplayImageOptions(this);
		assistantLoadData();
		mImageView.setOnClickListener(this);
		mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
		mRadioGroup.setOnCheckedChangeListener(this);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mAdpater = new BaseListPagerAdpater(getSupportFragmentManager());
		mViewPager.setAdapter(mAdpater);
		mList = new ArrayList<Fragment>();
		mViewPager.setOnPageChangeListener(this);
		/**
		 * 会诊详情带报告
		 */
		// 申请单
		Fragment allFragment = new DFgtConsultApplyForm();
		Bundle apply = new Bundle();
		apply.putInt("type", type);
		apply.putInt("CONSULTATIONID", conId);
		allFragment.setArguments(apply);
		mList.add(allFragment);

		// 会诊病历
		if(SmartFoxClient.getLoginUserInfo().isDoctor()&&"0".equals(SmartFoxClient.getLoginUserInfo().getDoctorPosition())){
			Fragment serverFragment = new DConsultRecordDocFragment();
			Bundle server = new Bundle();
			server.putString("consultationId", conId + "");
			serverFragment.setArguments(server);
			mList.add(serverFragment);
		}else{//不是会诊医生
			Fragment serverFragment = new DConsultRecordShowFragment();
			Bundle server = new Bundle();
			server.putString("consultationId", conId + "");
			serverFragment.setArguments(server);
			mList.add(serverFragment);
		}

		// 会诊报告
		payingFragment = new DFgtConsultReport();
		Bundle report = new Bundle();
		report.putInt("type", type);
		report.putInt("consultationId", conId);
//		report.putString("objectExpertId", docId);
		payingFragment.setArguments(report);
		mList.add(payingFragment);
		mAdpater.onBoundFragment(mList);
		mViewPager.setCurrentItem(2, false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			onBackPressed();
			break;
		case R.id.title_rigth_pic:// 与会诊医生聊天
			entity = new CustomerInfoEntity();
				entity.setName(docName);
				entity.setId(docId);
				FriendHttpUtil.chatFromPerson(this, entity);
			break;
		}
	}


	@Override
	protected void onResume() {
		// 移动数据统计分析
		FlowerCollector.onResume(this);
		FlowerCollector.onPageStart(TAG);
		super.onResume();
	}

	@Override
	protected void onPause() {
		// 移动数据统计分析
		FlowerCollector.onPageEnd(TAG);
		FlowerCollector.onPause(this);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (payingFragment!=null){
			payingFragment.release();
		}

		super.onDestroy();

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
		RadioButton mButton = (RadioButton) mRadioGroup.getChildAt(arg0);
		mButton.setChecked(true);
	}

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
							if ("0".equals(SmartFoxClient.getLoginUserInfo().getDoctorPosition()) && object.has("EXPERT")
									&& object.has("PATIENT")) {
//								objectExpertId=object.getJSONObject("EXPERTID");
								objectExpert = object.getJSONObject("EXPERT");
								objectPatient = object.getJSONObject("PATIENT");
							} else if (object.has("DOCTOR") && object.has("PATIENT")) {
								objectDoctor = object.getJSONObject("DOCTOR");
								objectPatient = object.getJSONObject("PATIENT");
							}
						} else if (object.has("DOCTOR") && object.has("EXPERT")) {
//							objectExpertId=object.getJSONObject("EXPERTID");
							objectExpert = object.getJSONObject("EXPERT");
							objectDoctor = object.getJSONObject("DOCTOR");
						} else if (object.has("EXPERT") && (!object.has("PATIENT")) && (!object.has("DOCTOR"))) {
							objectExpert = object.getJSONObject("EXPERT");
//							objectExpertId=object.getJSONObject("EXPERTID");
						}
					}
				} catch (JSONException e) {
					return null;
				}
				return object;
			}

			@Override
			public void onSuccess(Object response) {
				if (type == 23) {
					titleTextV.setText("我的会诊");
					mImageView.setVisibility(View.VISIBLE);
					docName = objectDoctor.optString("DOCTORNAME");
					docId =objectDoctor.optString("DOCTORID");
					mImageLoader.displayImage(objectDoctor.optString("DOCTORICON"), mImageView, mOptions);
				} else if (type == 25) {
					titleTextV.setText("我的会诊");
//					docId=objectExpertId.optString("EXPERTID");
				} else if (type == 15 || type == 3) {
					titleName=objectPatient.optString("PATIENTNAME");
					titleTextV.setText(titleName+"的会诊");
					mImageView.setVisibility(View.VISIBLE);
					docName = titleName;
					docId =objectPatient.optString("PATIENTID");
					mImageLoader.displayImage(objectPatient.optString("PATIENTICON"), mImageView, mOptions);
				} else if (type == 2) {// 专家给意见
					titleName=objectPatient.optString("PATIENTNAME");
					titleTextV.setText(titleName+"的会诊");
					mImageView.setVisibility(View.VISIBLE);
					docName = objectDoctor.optString("DOCTORNAME");
					docId =objectDoctor.optString("DOCTORID");
					mImageLoader.displayImage(objectDoctor.optString("DOCTORICON"), mImageView, mOptions);
				}
				super.onSuccess(response);
			}
		});
	}
}
