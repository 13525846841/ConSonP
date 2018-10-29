package com.yksj.consultation.son.consultation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yksj.consultation.comm.EditFragmentDialog;
import com.yksj.consultation.comm.ImageGalleryActivity;
import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.DensityUtils;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.ViewFinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * @author lmk
 *   动态会诊病历模板展示Fragment
 */
public class DConsultRecordShowFragment extends RootFragment implements
		OnClickListener {
	private View view;
	private String consultionId="50",recordId;//会诊id,病历id
	private ArrayList<JSONObject> datas;//网络加载过来的数据
	private LinearLayout contentLayout;//
	
	
	private TextView tvTip;
	private LinearLayout docBtnLayout;
	private Button btnDcoAdd,btnDcoSubmit;//帮他补充,提交专家
	private ScrollView mScrolview;
	private ImageLoader mImageLoader;
	private TextView tvPatientName,tvPatientSex,tvPatientBirthday,tvPatientAge,
			tvPatientZhiye,tvPatientPhone,tvPatientCode;
	private LinearLayout caseImgLayout;//展示图片的布局

	private RelativeLayout keyWordsRelativeLayout;//关键词整个布局
	private LinearLayout keysLayout;//关键词
	private Button btnEditKeys;//编辑关键词
	private JSONArray keysArray;//关键词Array
	private Button btnShare;//共享病历

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fgt_dynamic_case_template, null);
		Bundle bundle=getArguments();
		if(bundle!=null){
			consultionId=bundle.getString("consultationId");
		}
		initView();
		initData();
		return view;
	}

	//初始化数据
	private void initData() {
		HttpRestClient.doHttpConsultionCaseTemplate("6", null, consultionId, new AsyncHttpResponseHandler(getActivity()) {

			@Override
			public void onSuccess(String content) {
				JSONObject object;
				datas = new ArrayList<JSONObject>();
				try {
					object = new JSONObject(content);
					JSONArray array = object.getJSONArray("CONTENT");
					for (int i = 0; i < array.length(); i++) {
						datas.add(array.getJSONObject(i));
					}

					tvPatientName.setText(object.optString("NAME"));
					if ("W".equals(object.optString("SEX"))) {
						tvPatientSex.setText("女");
					} else if ("M".equals(object.optString("SEX"))) {
						tvPatientSex.setText("男");
					}
					if (object.has("PHONE")) {//包含这个键
						tvPatientPhone.setText(object.optString("PHONE").trim());
					} else {//不包含
						view.findViewById(R.id.create_case_tv_phone_left).setVisibility(View.GONE);
						tvPatientPhone.setVisibility(View.GONE);
					}
					if (SmartFoxClient.getLoginUserInfo().isDoctor() && !"0".equals(SmartFoxClient.getLoginUserInfo().getDoctorPosition())) {
						if ("1".equals(object.optString("ISSHARE"))) {//不可以分享
							btnShare.setBackgroundResource(R.drawable.doctor_clinic_can_not_buy);
							btnShare.setClickable(false);
						}
					}

					recordId = object.optString("RECORDID");
					tvPatientZhiye.setText(object.optString("METIER"));
					String bir = object.optString("BIRTHDAY");
					if (bir != null && bir.length() != 0) {
						String nian = bir.substring(0, 4);
						String yue = bir.substring(4, 6);
						String ri = bir.substring(6, 8);
						Calendar cc = Calendar.getInstance();
						int age = cc.get(Calendar.YEAR) - Integer.parseInt(nian);
						if (age >= 0) {
							tvPatientAge.setText(age + "");
							tvPatientBirthday.setText(nian + "-" + yue + "-" + ri);
						}
					}
					tvPatientCode.setText(object.optString("CODE"));
					onBoundImgData(object.optString("RECORDFILE"));
					keysArray = object.getJSONArray("FLAGCONTENT");
					for (int i = 0; i < keysArray.length(); i++) {
						JSONObject keyObject = keysArray.getJSONObject(i);
						Button btn = new Button(getActivity());
						btn.setText(keyObject.optString("NAME"));
						btn.setTextColor(getResources().getColor(R.color.gray_text));
						btn.setGravity(Gravity.CENTER);
						LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
						layoutParams.setMargins(6, 0, 6, 0);
						btn.setLayoutParams(layoutParams);
						btn.setBackgroundResource(R.drawable.btn_topic_label_bg);

						keysLayout.addView(btn);
					}
					if (keysArray == null || keysArray.length() == 0) {
						keyWordsRelativeLayout.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}


				if (datas.size() != 0) {
					onBoundDetailData(datas);//绑定数据到LinearLayout
				}
				super.onSuccess(content);
			}

		});
	}

	private void onBoundImgData(String recordfiles){
		caseImgLayout.removeAllViews();
		final StringBuilder sb=new StringBuilder();
		try {
			JSONArray imgArray = new JSONArray(recordfiles);
			for(int m=0;m<imgArray.length();m++){
				sb.append(imgArray.getJSONObject(m).optString("ICON").replace("-small", "")+",");
			}
			if(sb.length()>0)
				sb.deleteCharAt(sb.length()-1);
			for(int k=0;k<imgArray.length();k++){
				final int imgPosition=k;
				final JSONObject imgObject=imgArray.getJSONObject(k);
				ImageView imageview=new ImageView(getActivity());
				imageview.setLayoutParams(new LayoutParams(DensityUtils.dip2px(getActivity(), 78), DensityUtils.dip2px(getActivity(), 78)));
				imageview.setScaleType(ScaleType.CENTER_CROP);
				mImageLoader.displayImage(imgObject.optString("ICON"), imageview);//加载小图片
				imageview.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),ImageGalleryActivity.class);
						intent.putExtra(ImageGalleryActivity.URLS_KEY,sb.toString().split(","));
						intent.putExtra(ImageGalleryActivity.TYPE_KEY,1);
						intent.putExtra("type", 1);// 0,1单个,多个
						intent.putExtra("position", imgPosition);
						startActivityForResult(intent, 100);
					}
				});
				caseImgLayout.addView(imageview);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 绑定LinearLayout数据
	 * 患者已经填写完了病历并且上传成功,这时是加载显示病历
	 */
	private void onBoundDetailData(ArrayList<JSONObject> datas) {
		for(int i=0;i<datas.size();i++){
			ViewFinder finder;
			final int index=i;
			final JSONObject entity=datas.get(i);
			View itemView=LayoutInflater.from(getActivity()).inflate( R.layout.apt_consultion_case_item_show, null,true);
			finder=new ViewFinder(itemView);
			
			TextView tvCategoryTitle=finder.find(R.id.case_template_item_show_title);;
			if(i==0){//第一个一定是开始,显示CLASSNAME
				tvCategoryTitle.setVisibility(View.VISIBLE);
				tvCategoryTitle.setText(entity.optString("CLASSNAME"));//分类名称
			}else{//后面的家判断是否显示CLASSNAME
				JSONObject entity2=datas.get(i-1);
				if(!(entity2.optInt("CLASSID")==entity.optInt("CLASSID"))){//分类开始
					tvCategoryTitle.setVisibility(View.VISIBLE);
					tvCategoryTitle.setText(entity.optString("CLASSNAME"));//分类名称
				}
			}
			if(entity.optInt("NEFILL")==1){//必填
				finder.find(R.id.case_template_item_show_star).setVisibility(View.VISIBLE);
			}
			finder.setText(R.id.case_template_item_show_name, entity.optString("ITEMNAME"));//先把本item的标题附上去
			
			TextView tvLeft=(TextView) itemView.findViewById(R.id.case_template_item_show_text_left);
			TextView tvMiddle=(TextView) itemView.findViewById(R.id.case_template_item_show_text_middle);
			TextView tvRight=(TextView) itemView.findViewById(R.id.case_template_item_show_text_right);
			LinearLayout imgLayout=finder.find(R.id.case_template_item_show_images);
			
			switch (entity.optInt("ITEMTYPE")) {
			case 10://文字填写
			case 20://单选
			case 30://多选
			case 40://单数字填写
			case 60://日期
			case 80://日期
				tvLeft.setText(entity.optString("INFO"));
				break;
			case 50://区域数字填写90~100
				tvLeft.setText(entity.optString("INFO"));
				tvMiddle.setVisibility(View.VISIBLE);
				tvRight.setVisibility(View.VISIBLE);
				tvRight.setText(entity.optString("INFO2"));
				break;
			case 70://大文本域填写
				tvLeft.setText(entity.optString("INFO"));
				break;
			case 90://只有ItemName
				tvLeft.setVisibility(View.GONE);
				break;
			}
			contentLayout.addView(itemView);
		}
	}

	//初始化控件
	private void initView() {
		tvTip = (TextView) view.findViewById(R.id.dynamic_case_template_doc_tip);
		mImageLoader=ImageLoader.getInstance();
		contentLayout=(LinearLayout) view.findViewById(R.id.dynamic_case_template_linearlayout);
		if(SmartFoxClient.getLoginUserInfo().isDoctor()&&!"0".equals(SmartFoxClient.getLoginUserInfo().getDoctorPosition())){
			view.findViewById(R.id.dynamic_case_template_doctor_layout).setVisibility(View.VISIBLE);
			view.findViewById(R.id.dynamic_case_template_doctor_add).setVisibility(View.GONE);
			view.findViewById(R.id.dynamic_case_template_doctor_submit).setVisibility(View.GONE);
			btnShare= (Button) view.findViewById(R.id.dynamic_case_template_doctor_share);
			btnShare.setOnClickListener(this);
		}


		tvPatientName= (TextView) view.findViewById(R.id.create_case_input_name);
		tvPatientSex= (TextView) view.findViewById(R.id.create_case_input_sex);
		tvPatientZhiye= (TextView) view.findViewById(R.id.create_case_input_zhiye);
		tvPatientAge= (TextView) view.findViewById(R.id.create_case_input_age);
		tvPatientPhone= (TextView) view.findViewById(R.id.create_case_input_address);
		tvPatientBirthday= (TextView) view.findViewById(R.id.create_case_input_birthday);
		tvPatientCode= (TextView) view.findViewById(R.id.create_case_input_card_num);
		caseImgLayout= (LinearLayout) view.findViewById(R.id.dynamic_case_img_layout);

		keyWordsRelativeLayout= (RelativeLayout) view.findViewById(R.id.case_keywords_layout_id);
		keysLayout= (LinearLayout) view.findViewById(R.id.create_case_keywords);
		btnEditKeys= (Button) view.findViewById(R.id.create_case_keywords_add);


	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.dynamic_case_template_doctor_share://共享病历

			EditFragmentDialog.show(getChildFragmentManager(),
					"编辑病历标题", 10, "取消", "确定", new EditFragmentDialog.OnDilaogClickListener() {
						@Override
						public void onDismiss(DialogFragment fragment) {

						}

						@Override
						public void onClick(DialogFragment fragment, View v) {
							EditFragmentDialog dialog = (EditFragmentDialog) fragment;
							String editStr = dialog.getEditTextStr();
							if (editStr != null && editStr.length() != 0) {
								onShareCase(editStr);
							}else{
								ToastUtil.showShort(getActivity(),"无法完成分享病历操作，病历名称不能为空");
							}
						}
					});



//			DoubleBtnFragmentDialog.show(getChildFragmentManager(), "提示", "您愿意将此病历共享到病历讨论模块与其他医生一起讨论吗？",
//					"取消", "确定", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
//						@Override
//						public void onDismiss(DialogFragment fragment) {
//
//						}
//
//						@Override
//						public void onClick(DialogFragment fragment, View v) {
//							onShareCase();
//						}
//					});

			break;
		}
	}

	//共享病历
	private void onShareCase(String str) {
		//TempletClassMRTServlet?OPTION=9&RECORDID=28&CUSTID=2400
		RequestParams params=new RequestParams();
		params.put("OPTION", "9");
		params.put("RECORDID", recordId);//会诊id
		params.put("RECORDNAME", str);//会诊id
		params.put("CUSTID", SmartFoxClient.getLoginUserId());
		HttpRestClient.doHttpConsultionCaseTemplateShare(params, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(String content) {
				try {
					JSONObject object=new JSONObject(content);
					if(object.has("errormessage")){
						ToastUtil.showShort(object.getString("errormessage"));
					}else{
						ToastUtil.showShort(object.getString("INFO"));
						btnShare.setBackgroundResource(R.drawable.doctor_clinic_can_not_buy);
						btnShare.setClickable(false);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(content);
			}
		});

	}

	
}
