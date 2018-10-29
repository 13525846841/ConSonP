package com.yksj.consultation.son.consultation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yksj.consultation.adapter.DynamicCaseTemplateAdapter;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog.OnDilaogClickListener;
import com.yksj.consultation.comm.ImageGalleryActivity;
import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.comm.SingleBtnFragmentDialog.OnClickSureBtnListener;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.photo.utils.AlbumActivity;
import com.yksj.healthtalk.photo.utils.GalleryActivity;
import com.yksj.healthtalk.utils.Bimp;
import com.yksj.healthtalk.utils.BitmapUtils;
import com.yksj.healthtalk.utils.DensityUtils;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ImageItem;
import com.yksj.healthtalk.utils.JsonParseUtils;
import com.yksj.healthtalk.utils.StringFormatUtils;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.ViewFinder;
import com.yksj.healthtalk.utils.WheelUtils;
import com.yksj.healthtalk.views.MessageImageView;

import org.cropimage.CropUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.ImageLoader;
import org.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HEKL
 *   动态会诊病历模板
 */
public class DConsultRecordFragment extends RootFragment implements
		OnClickListener {
	private static final int TAKE_PICTURE = 0x000001;

	private int caseType=2;//TYPE=1  等待基层医生发送病历模板,TYPE=2 首次填写，加载出病历模板,TYPE=3  填写后的病历详情
	private View view;//fragment布局
	private View wheelView;//滑轮选择器布局
	private LinearLayout contentLayout,complateLayout;//内容布局完成提交布局
	private Button btnComplate;//完成
	private DynamicCaseTemplateAdapter mAdapter;
	CancelClickListener cancelListener =new CancelClickListener();
	private TextView tvTip;
	
	private HashMap<Integer, TextView> multipleTexts;//多选的结果返回应填入对应的EditText
	private HashMap<Integer, View> imgLayouts=new HashMap<Integer, View>();//多选的图片结果返回应添加到对应的LinearLayout
	PopupWindow mPopupWindow,pop,addPhotoPop;
	private PopupWindow agepop;// 年龄的pop
	
	private JSONArray postJson;//上传的json字符数据
	private ArrayList<JSONObject> datas;//网络加载过来的数据
	private String consultionId="50";
	
	public static Bitmap bimap ;
	private String currentKey="-1";//当前行的ITEMID为-1
	private File storageFile=null;
	private ImageLoader mImageLoader;

	private ScrollView mScrolview;

	private Bundle bundle;
	SingleBtnFragmentDialog postDialog;//上传对话框
	private boolean isUploading=false;//是否正在上传
	private DAtyConsultDetails activity;

	private EditText dateEdit;
	private RequestParams postParams;//上传参数
	private TextView tvPatientName,tvPatientSex,tvPatientBirthday,tvPatientAge,
			tvPatientZhiye,tvPatientPhone,tvPatientCode;
	private EditText editPatientName,editPatientZhiye,editPatientPhone,editPatientBirthday,editPatientCode;
	private TextView editPatientSex,editPatientAge;
	private ImageView imgAddImg,imgAddAudio,imgAddVideo;
	private RelativeLayout addImgResourceLayout;

	private LinearLayout patientInfoLayout;
	private LinearLayout caseImgLayout;
	private boolean canEdit=false;//是否可编辑,true为天病历状态,false为浏览模式

	private RelativeLayout keyWordsRelativeLayout;//关键词整个布局
	private LinearLayout keysLayout;//关键词
	private Button btnEditKeys;//编辑关键词
	private JSONArray keysArray;//关键词Array



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fgt_dynamic_case_template, null);
		bundle = getArguments();
		consultionId=bundle.getString("consultationId");//拿到会诊id
		currentKey=DConsultRecordFragment.class.getSimpleName();//给图片集合赋值key,用来存和取
		initView();
		initData();
		return view;
	}

	//初始化数据
	private void initData() {
		contentLayout.removeAllViews();
		HttpRestClient.doHttpConsultionCaseTemplate("6",null, consultionId, new AsyncHttpResponseHandler(getActivity()){

			@Override
			public void onSuccess(String content) {
				JSONObject object;
				datas = new ArrayList<JSONObject>();
				try {
					object = new JSONObject(content);
					if(!object.has("errormessage")){//不包含错误信息,进行解析适配
						caseType=Integer.parseInt(object.optString("TYPE"));
						if(caseType==1){//等待推送模板
							view.findViewById(R.id.case_patient_info_layout).setVisibility(View.GONE);
							view.findViewById(R.id.dynamic_case_img_audio_linearlayout).setVisibility(View.GONE);
							caseImgLayout.setVisibility(View.GONE);
							tvTip.setVisibility(View.VISIBLE);
							tvTip.setText(object.optString("CONTENT"));
							super.onSuccess(content);
							return;
						}

						JSONArray array=object.getJSONArray("CONTENT");
						postJson=new JSONArray();
						for(int i=0;i<array.length();i++){
							postJson.put(JsonParseUtils.getPostTemplateObject(array.getJSONObject(i)));
							datas.add(array.getJSONObject(i));
						}


						if(caseType==2){//用户需要填写病历
							canEdit=true;
							if(datas.size()!=0)
								onBoundData(datas);//绑定数据到LinearLayout
							activity=(DAtyConsultDetails) getActivity();
							activity.isEditing=true;
						}else if(caseType==3){
							canEdit=false;
							if(datas.size()!=0)
								onBoundDetailData(datas);
						}
						onBoundPatientInfo(object);


//					view.findViewById(R.id.case_patient_info_layout).setVisibility(View.GONE);
//					view.findViewById(R.id.dynamic_case_img_audio_linearlayout).setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(content);
			}
			
		});
	}

	//绑定患者信息到控件
	private void onBoundPatientInfo(JSONObject object){
		try {
			keysArray=object.getJSONArray("FLAGCONTENT");
			for(int i=0;i<keysArray.length();i++){
				JSONObject keyObject=keysArray.getJSONObject(i);
				Button btn=new Button(getActivity());
				btn.setText(keyObject.optString("NAME"));
				btn.setTextColor(getResources().getColor(R.color.gray_text));
				btn.setGravity(Gravity.CENTER);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				layoutParams.setMargins(6, 0, 6, 0);
				btn.setLayoutParams(layoutParams);
				btn.setBackgroundResource(R.drawable.btn_topic_label_bg);

				keysLayout.addView(btn);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
//		if(keysArray!=null&&keysArray.length()>0){
//			keyWordsRelativeLayout.setVisibility(View.VISIBLE);
//		}
		if(!canEdit){//展示患者信息
			editPatientName.setVisibility(View.GONE);
			tvPatientName.setVisibility(View.VISIBLE);
			tvPatientZhiye.setVisibility(View.VISIBLE);
			editPatientZhiye.setVisibility(View.GONE);
			tvPatientCode.setVisibility(View.VISIBLE);
			editPatientCode.setVisibility(View.GONE);
			tvPatientPhone.setVisibility(View.VISIBLE);
			editPatientPhone.setVisibility(View.GONE);
			tvPatientSex.setVisibility(View.VISIBLE);
			editPatientSex.setVisibility(View.GONE);
			tvPatientBirthday.setVisibility(View.VISIBLE);
			tvPatientAge.setVisibility(View.VISIBLE);
			editPatientBirthday.setVisibility(View.GONE);
			editPatientAge.setVisibility(View.GONE);
			tvPatientName.setText(object.optString("NAME"));
			if("W".equals(object.optString("SEX"))){
				tvPatientSex.setText("女");
			}else if("M".equals(object.optString("SEX"))){
				tvPatientSex.setText("男");
			}
			if(object.has("PHONE")){//包含这个键
				tvPatientPhone.setText(object.optString("PHONE").trim());
			}else{//不包含
				view.findViewById(R.id.create_case_tv_phone_left).setVisibility(View.GONE);
				tvPatientPhone.setVisibility(View.GONE);
			}
			tvPatientZhiye.setText(object.optString("METIER"));
			String bir=object.optString("BIRTHDAY");
			if(bir!=null&&bir.length()!=0){
				String nian=bir.substring(0,4);
				String yue=bir.substring(4,6);
				String ri=bir.substring(6, 8);
				Calendar cc=Calendar.getInstance();
				int age=cc.get(Calendar.YEAR)-Integer.parseInt(nian);
				if(age>=0){
					tvPatientAge.setText(age+"");
					tvPatientBirthday.setText(nian+"-"+yue+"-"+ri);
				}
			}
			tvPatientCode.setText(object.optString("CODE"));
			addImgResourceLayout.setVisibility(View.GONE);
			onBoundImgData(object.optString("RECORDFILE"));
		}else{//不展示数据让用户填写
			keyWordsRelativeLayout.setVisibility(View.GONE);
			tvPatientName.setVisibility(View.GONE);
			editPatientName.setVisibility(View.VISIBLE);
			tvPatientZhiye.setVisibility(View.GONE);
			editPatientZhiye.setVisibility(View.VISIBLE);
			tvPatientCode.setVisibility(View.GONE);
			editPatientCode.setVisibility(View.VISIBLE);
			tvPatientPhone.setVisibility(View.GONE);
			editPatientPhone.setVisibility(View.VISIBLE);
			tvPatientSex.setVisibility(View.GONE);
			editPatientSex.setVisibility(View.VISIBLE);
			tvPatientBirthday.setVisibility(View.GONE);
			tvPatientAge.setVisibility(View.GONE);
			editPatientBirthday.setVisibility(View.VISIBLE);
			editPatientAge.setVisibility(View.VISIBLE);
			if(!object.has("PHONE")){//不包含这个键
				view.findViewById(R.id.create_case_tv_phone_left).setVisibility(View.GONE);
				editPatientPhone.setVisibility(View.GONE);
			}
			final ArrayList<Map<String,String>> sexList=new ArrayList<Map<String, String>>();
			HashMap< String, String> map=new HashMap<String, String>();
			map.put("name", "男");
			map.put("code", "M");
			HashMap< String, String> map2=new HashMap<String, String>();
			map2.put("name", "女");
			map2.put("code", "W");
			sexList.add(map);
			sexList.add(map2);
			editPatientSex.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					setSingleSelector(sexList, editPatientSex, 0, true);
				}
			});
			addImgResourceLayout.setVisibility(View.VISIBLE);

			editPatientBirthday.setOnClickListener(this);

		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TYPE = getArguments().getString("type");
	}
	
	/**
	 * 绑定LinearLayout数据
	 */
	private void onBoundData(ArrayList<JSONObject> datas) {
		complateLayout.setVisibility(View.VISIBLE);
		multipleTexts=new HashMap<Integer, TextView>();
		for(int i=0;i<datas.size();i++){
			final int index=i;
			final JSONObject entity=datas.get(i);
			View itemView=LayoutInflater.from(getActivity()).inflate(R.layout.apt_consultion_case_item_text, null, true);
			TextView tvCategoryTitle=(TextView) itemView.findViewById(R.id.apt_case_template_item_title);
			TextView tvEditLeft=(TextView) itemView.findViewById(R.id.apt_case_template_item_name);
			final TextView tvRight=(TextView) itemView.findViewById(R.id.apt_case_template_item_text_right);//右边文本,
			TextView tvStar=(TextView) itemView.findViewById(R.id.apt_case_template_item_text_star);//必填选填标记
			final EditText editText=(EditText) itemView.findViewById(R.id.apt_case_template_item_edit_right);//右边的右输入框
			EditText editTextLeft=(EditText) itemView.findViewById(R.id.apt_case_template_item_edit_left);//右边的左输入框
			LinearLayout horLayout=(LinearLayout) itemView.findViewById(R.id.apt_case_template_item_text_layout);
			EditText bigEditText=(EditText) itemView.findViewById(R.id.apt_case_template_item_text_edit_big);//下面大输入框
			LinearLayout imgLayout=(LinearLayout) itemView.findViewById(R.id.apt_case_template_item_img_layout);
			LinearLayout images=(LinearLayout) itemView.findViewById(R.id.apt_case_template_item_images);
			final Button imgAdd=(Button) itemView.findViewById(R.id.apt_case_template_item_img_add);
			
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
				tvStar.setVisibility(View.VISIBLE);
			}
			switch (entity.optInt("ITEMTYPE")) {
			case 10://文字填写
				tvEditLeft.setText(entity.optString("ITEMNAME"));
				editText.setVisibility(View.VISIBLE);
				tvRight.setVisibility(View.GONE);
				
				break;
			case 20://单选
				
				tvEditLeft.setText(entity.optString("ITEMNAME"));
				tvRight.setVisibility(View.VISIBLE);
				tvRight.setHint(getActivity().getResources().getString(R.string.please_choise));
				editText.setVisibility(View.GONE);
				final ArrayList<Map<String, String>> selectors=JsonParseUtils.parseTemplateItemData(entity.optString("OPTION"));
				tvRight.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						setSingleSelector(selectors, tvRight,index,false);
					}
				});
				
				break;
			case 30://多选
				
				tvEditLeft.setText(entity.optString("ITEMNAME"));
				tvRight.setVisibility(View.VISIBLE);
				tvRight.setHint(getActivity().getResources().getString(R.string.please_choise));
				editText.setVisibility(View.GONE);
				multipleTexts.put(index, tvRight);
				bigEditText.setFocusable(false);
				bigEditText.setEnabled(false);
				ArrayList<Map<String, Object>> list=JsonParseUtils.parseMultipleChoiseData(entity.optString("OPTION"));
				tvRight.setOnClickListener(new MultipleChoiseClickListener(list,entity.optString("ITEMNAME"), index));

				break;
			case 40://单数字填写
				tvEditLeft.setText(entity.optString("ITEMNAME"));
				editText.setVisibility(View.VISIBLE);
				editText.setInputType(InputType.TYPE_CLASS_NUMBER);
				tvRight.setVisibility(View.GONE);
				break;
			case 50://区域数字填写90~100
				tvEditLeft.setText(entity.optString("ITEMNAME"));
				editText.setVisibility(View.VISIBLE);
				editTextLeft.setVisibility(View.VISIBLE);
				editText.setInputType(InputType.TYPE_CLASS_NUMBER);
				tvRight.setVisibility(View.VISIBLE);
				tvRight.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				tvRight.setText("~");
				tvRight.setBackgroundColor(Color.TRANSPARENT);
				tvRight.setTextColor(getResources().getColor(R.color.color_green_blue_bg));
				break;
			case 60://日期
				tvEditLeft.setText(entity.optString("ITEMNAME"));
				editText.setVisibility(View.VISIBLE);
				editText.setHint(getActivity().getResources().getString(R.string.please_choise));
				editText.setFocusable(false);
//				editText.setEnabled(false);
//				editText.setVisibility(View.GONE);
				editText.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dateEdit=editText;
						setAgeDate(index);
					}
				});
				break;
			case 70://大文本域填写
				tvEditLeft.setText(entity.optString("ITEMNAME"));
				tvRight.setVisibility(View.GONE);
				editText.setVisibility(View.GONE);
				bigEditText.setVisibility(View.VISIBLE);
				bigEditText.setHint(getResources().getString(R.string.please_input)+entity.optString("ITEMNAME"));
				bigEditText.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						v.getParent().requestDisallowInterceptTouchEvent(true);
						return false;
					}
				});
//				if(entity.optInt("SPIC")==1){
//					imgLayout.setVisibility(View.VISIBLE);
//					//假如SPIC=1，则本行具备添加图片的潜质，给他开辟list存放选择的图片
//					imgLayouts.put(entity.optInt("ITEMID"), itemView);
//					String itemKey=""+entity.optInt("ITEMID");
//					ArrayList<ImageItem> itemImgs=new ArrayList<ImageItem>();//
//					Bimp.dataMap.put(itemKey, itemImgs);
//					Bimp.imgMaxs.put(itemKey, entity.optInt("PICNUM"));
//					imgAdd.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							currentKey=""+entity.optInt("ITEMID");
//							showuploadPopWindow();
//						}
//					});
//				}
				break;
			case 80://有小数点的情况
				tvEditLeft.setText(entity.optString("ITEMNAME"));
				editText.setVisibility(View.VISIBLE);
				editText.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
				editText.addTextChangedListener(new TextWatcher() {
					
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
					}
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count,
							int after) {
					}
					
					@Override
					public void afterTextChanged(Editable s) {
					}
				});
				tvRight.setVisibility(View.GONE);
				break;
				case 90:
					tvEditLeft.setText(entity.optString("ITEMNAME"));
					tvRight.setVisibility(View.GONE);
					editText.setVisibility(View.GONE);
					break;
			}
			contentLayout.addView(itemView);
		}
		
	}
	
	/**
	 * 绑定LinearLayout数据
	 * 患者已经填写完了病历并且上传成功,这时是加载显示病历
	 */
	private void onBoundDetailData(ArrayList<JSONObject> datas) {
		complateLayout.setVisibility(View.GONE);
		contentLayout.removeAllViews();//先去掉所有的View
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
			case 80://小数
				tvLeft.setText(entity.optString("INFO"));
				break;
			case 90://只有ItemName
				tvLeft.setVisibility(View.GONE);
				break;
			case 50://区域数字填写90~100
				tvLeft.setText(entity.optString("INFO"));
				tvMiddle.setVisibility(View.VISIBLE);
				tvRight.setVisibility(View.VISIBLE);
				tvRight.setText(entity.optString("INFO2"));
				break;
			case 70://大文本域填写
				tvLeft.setText(entity.optString("INFO"));
				final StringBuilder sb=new StringBuilder();
				if(entity.optInt("SPIC")==1&&entity.optInt("PICNUM")>0){//包含图片
					imgLayout.setVisibility(View.VISIBLE);//图片布局可以显示
					JSONArray imgArray;
					try {
						imgArray = new JSONArray(entity.optString("PICS"));
						for(int m=0;m<imgArray.length();m++){
							sb.append(imgArray.getJSONObject(m).optString("PIC").replace("-small", "")+",");
						}
						if(sb.length()>0)
							sb.deleteCharAt(sb.length()-1);
						for(int k=0;k<imgArray.length();k++){
							final int imgPosition=k;
							final JSONObject imgObject=imgArray.getJSONObject(k);
							ImageView imageview=new ImageView(getActivity());
							imageview.setLayoutParams(new LayoutParams(DensityUtils.dip2px(getActivity(), 78), DensityUtils.dip2px(getActivity(), 78)));
							imageview.setScaleType(ScaleType.CENTER_CROP);
							mImageLoader.displayImage(imgObject.optString("PIC"), imageview);//加载小图片
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
							imgLayout.addView(imageview);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;
			}
			contentLayout.addView(itemView);
		}
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


	//初始化控件
	private void initView() {
		tvTip = (TextView) view.findViewById(R.id.dynamic_case_template_doc_tip);
		mScrolview = (ScrollView) view.findViewById(R.id.dynamic_case_template_scrollview);
		mImageLoader=ImageLoader.getInstance();
		contentLayout=(LinearLayout) view.findViewById(R.id.dynamic_case_template_linearlayout);
		btnComplate=(Button) view.findViewById(R.id.dynamic_case_template_complate);
		complateLayout=(LinearLayout) view.findViewById(R.id.dynamic_case_template_patient_layout);
		btnComplate.setOnClickListener(this);
		wheelView = getActivity().getLayoutInflater().inflate(R.layout.wheel, null);
		wheelView.findViewById(R.id.wheel_cancel).setOnClickListener(this);
		wheelView.findViewById(R.id.wheel_sure).setOnClickListener(this);
		bimap = BitmapFactory.decodeResource(getResources(),R.drawable.img_add_icon);

		tvPatientName= (TextView) view.findViewById(R.id.create_case_input_name);
		tvPatientSex= (TextView) view.findViewById(R.id.create_case_input_sex);
		tvPatientZhiye= (TextView) view.findViewById(R.id.create_case_input_zhiye);
		tvPatientAge= (TextView) view.findViewById(R.id.create_case_input_age);
		tvPatientPhone= (TextView) view.findViewById(R.id.create_case_input_address);
		tvPatientBirthday= (TextView) view.findViewById(R.id.create_case_input_birthday);
		tvPatientCode= (TextView) view.findViewById(R.id.create_case_input_card_num);
		editPatientName= (EditText) view.findViewById(R.id.create_case_input_edit_name);
		editPatientZhiye= (EditText) view.findViewById(R.id.create_case_input_edit_zhiye);
		editPatientCode= (EditText) view.findViewById(R.id.create_case_input_edit_code);
		editPatientPhone= (EditText) view.findViewById(R.id.create_case_input_edit_address);
		editPatientBirthday= (EditText) view.findViewById(R.id.create_case_input_edit_birthday);
		editPatientSex= (TextView) view.findViewById(R.id.create_case_input_edit_sex);
		editPatientAge= (TextView) view.findViewById(R.id.create_case_input_edit_age);

		keyWordsRelativeLayout= (RelativeLayout) view.findViewById(R.id.case_keywords_layout_id);
		keyWordsRelativeLayout.setVisibility(View.GONE);
		keysLayout= (LinearLayout) view.findViewById(R.id.create_case_keywords);
		btnEditKeys= (Button) view.findViewById(R.id.create_case_keywords_add);

		addImgResourceLayout= (RelativeLayout) view.findViewById(R.id.dynamic_case_add_image_resource_layout);
		caseImgLayout= (LinearLayout) view.findViewById(R.id.dynamic_case_img_layout);
		imgAddImg= (ImageView) view.findViewById(R.id.create_case_add_image);
		imgAddAudio= (ImageView) view.findViewById(R.id.create_case_add_audio);
		imgAddVideo= (ImageView) view.findViewById(R.id.create_case_add_video);
		imgAddImg.setOnClickListener(this);
		imgAddAudio.setOnClickListener(this);
		imgAddVideo.setOnClickListener(this);
		ArrayList<ImageItem> itemImgs=new ArrayList<ImageItem>();//
		Bimp.dataMap.put(DConsultRecordFragment.class.getSimpleName(), itemImgs);
		Bimp.imgMaxs.put(DConsultRecordFragment.class.getSimpleName(), 32);

	}

	@Override
	public void onClick(View v) {
		SystemUtils.hideSoftAnyHow(getActivity());
		switch (v.getId()) {
//		case R.id.btn_finish:
//			ToastUtil.showShort("完成");
//			break;
		case R.id.dynamic_case_template_complate://用户输入完成,遍历获取数据,开始提交
			if(verifyData()){//验证完成,提交
				complateLayout.setVisibility(View.INVISIBLE);
				postParams = new RequestParams();
				postParams.putNullFile("file", new File(""));
				postParams.put("CUSTID", SmartFoxClient.getLoginUserId());
				postParams.put("RECORDID", "");
				postParams.put("CONSULTATIONID", consultionId);
				postParams.put("CENTERID", HTalkApplication.APP_CONSULTATION_CENTERID);
				postParams.put("CONTENT", postJson.toString());
				//患者信息
				postParams.put("NAME", editPatientName.getText().toString().trim());
				postParams.put("METIER", editPatientZhiye.getText().toString().trim());
				postParams.put("PHONE", editPatientPhone.getText().toString().trim());
				postParams.put("CODE", editPatientCode.getText().toString().trim());

				if("男".equals(editPatientSex.getText().toString().trim())){
					postParams.put("SEX", "M");
				}else if("女".equals(editPatientSex.getText().toString().trim())){
					postParams.put("SEX", "W");
				}
				String bir=editPatientBirthday.getText().toString().trim();
				if(bir!=null&&bir.length()!=0){
					String[] srr=bir.split("-");
					postParams.put("BIRTHDAY", srr[0]+srr[1]+srr[2]+"0000");
				}else{
					postParams.put("BIRTHDAY","");
				}

				putFile(postParams);
				DoubleBtnFragmentDialog.showDefault(getChildFragmentManager(), 
						"提交后，此病历将自动发送给您的会诊医生，会诊医生可以继续帮您修改病历，您确定提交吗?", "取消", "确定", new OnDilaogClickListener() {
					@Override
					public void onDismiss(DialogFragment fragment) {
						isUploading=false;
						complateLayout.setVisibility(View.VISIBLE);
					}
					@Override
					public void onClick(DialogFragment fragment, View v) {
						doUpload(postParams);//执行上传
					}
					
				});
			}else{
				isUploading=!isUploading;
			}
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
			Intent intent = new Intent(getActivity(),AlbumActivity.class);
			intent.putExtra("key", currentKey);
			startActivity(intent);
//			getActivity().overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
			break;
		case R.id.wheel_sure_age:
			String[] str = (String[]) v.getTag();
			if(dateEdit==editPatientBirthday){
				Calendar cc=Calendar.getInstance();
				int age=cc.get(Calendar.YEAR)-Integer.parseInt(str[0].substring(0, str[0].length()-1));//得到年领
				if (age<=0){
					ToastUtil.showShort(getActivity(),"请正确输入出生日期!");
					return;
				}
				editPatientAge.setText(age+"");
			}
			dateEdit.setText(str[0].substring(0, str[0].length() - 1) + "-" + str[1].substring(0, str[1].length() - 1)
					+ "-" + str[2].substring(0, str[2].length() - 1));
			break;
		case R.id.create_case_input_edit_birthday:
			dateEdit=editPatientBirthday;
			setAgeDate(-1);
			break;
		case R.id.create_case_add_image://添加图片
			showuploadPopWindow();
			break;
		case R.id.create_case_add_audio://添加音频

			break;
		case R.id.create_case_add_video://添加视频

			break;
		}
	}

	//执行上传
	private void doUpload(RequestParams params) {
			HttpRestClient.doHttpPostConsultionCaseTemplate(params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onStart() {
							postDialog = SingleBtnFragmentDialog.show(getChildFragmentManager(), "提示:",
									"正在上传,请耐心等待!", "确定", cancelListener);
							postDialog.setCancelable(false);//不可被取消
							super.onStart();
						}

						@Override
						public void onFinish() {
							postDialog.dismiss();
							isUploading = false;
							complateLayout.setVisibility(View.VISIBLE);
							super.onFinish();
						}

						@Override
						public void onFailure(Throwable error,
											  String content) {
							isUploading = false;
							complateLayout.setVisibility(View.VISIBLE);
							super.onFailure(error, content);
						}

						@Override
						public void onSuccess(String content) {

							if (content.contains("errorcode")) {
								try {
									JSONObject object = new JSONObject(content);
									ToastUtil.showShort(object.optString("errormessage"));
								} catch (JSONException e) {
									e.printStackTrace();
								}
							} else {
								activity.isEditing = false;
								ToastUtil.showShort("上传成功");
								initData();
							}
							super.onSuccess(content);
						}
					});
	}
	/**
	 * 点击取消上传的监听
	 */
	class CancelClickListener implements OnClickSureBtnListener{

		@Override
		public void onClickSureHander() {
			
		}
		
	}
	
	
//	//将文件放入参数
//	private void putFile(RequestParams params) {
//		Iterator<Entry<Integer, View>> iter = imgLayouts.entrySet().iterator();
//		while(iter.hasNext()){
//	        Map.Entry<Integer, View> info= iter.next();
//	        final int key = info.getKey();//获取健
//	        ArrayList<ImageItem> list=Bimp.dataMap.get(""+key);//根据ID寻找
//	        for(int i=0;i<list.size();i++){
//	        	int index=i+1;
//	        	try {
//					params.put(key+"-"+index+".jpg", onGetDecodeFileByPath(list.get(i).getImagePath()));
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				}
//	        }
//		}
//	}

	//将文件放入参数
	private void putFile(RequestParams params) {
		ArrayList<ImageItem> list=Bimp.dataMap.get(currentKey);//根据ID寻找
		for(int i=0;i<list.size();i++){
			int index=i+1;
			if(!(list.get(i).isNetPic)){//不是网络图片才上传
				try {
					params.put(index+".jpg", BitmapUtils.onGetDecodeFileByPath(getActivity(),list.get(i).getImagePath()));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 得到并且去验证用户输入的数据
	 * @return  true表示可以上传   false 表示不可以上传
	 */
	private boolean verifyData() {
		int[] location=new int[2];
		int[] scrLocation=new int[2];
		contentLayout.getLocationInWindow(scrLocation);
		System.out.println(isUploading+"--------");
		if(isUploading){
			isUploading=false;
			return false;
		}
		isUploading=true;//正在上传
		if(editPatientPhone.getText().toString().trim().length()>0){

		if(!StringFormatUtils.isPhoneNum(editPatientPhone.getText().toString().trim())){
			ToastUtil.showShort("请输入正确的手机号");
			return false;
		}
		}
		if(editPatientCode.getText().toString().trim().length()>0){

		if(!StringFormatUtils.isIDCardNumber(editPatientCode.getText().toString().trim())){
			//身份证号码有误
			ToastUtil.showShort("请输入正确的身份证号码");
			return false;
		}
		}
		try {
			for(int i=0;i<postJson.length();i++){
				JSONObject object=datas.get(i);
				JSONObject postObject=postJson.getJSONObject(i);

				switch (object.optInt("ITEMTYPE")) {
				case 10://文本
				case 40://单数字
				case 80://带小数
					EditText editresult=(EditText) contentLayout.getChildAt(i).findViewById(R.id.apt_case_template_item_edit_right);
					String input=editresult.getText().toString().trim();
					if(input==null||"".equals(input)){
						if(object.optInt("NEFILL")!=1)//不是必填,可以直接跳过判断
							continue;
						editresult.getLocationInWindow(location);
						mScrolview.scrollTo(location[0], location[1]-scrLocation[1]-100);
						ToastUtil.showShort("请正确输入"+object.optString("ITEMNAME"));
						return false;
					}else{
						postObject.put("INFO", input);
					}
					break;
				case 20://单选
				case 30://多选
					TextView tvRight=(TextView) contentLayout.getChildAt(i).findViewById(R.id.apt_case_template_item_text_right);
					String singlestr=postObject.optString("SELECTION");
					if(singlestr==null||"".equals(singlestr)){
						if(object.optInt("NEFILL")!=1)//不是必填,可以直接跳过判断
							continue;
						tvRight.getLocationOnScreen(location);
						mScrolview.scrollTo(location[0], location[1]-scrLocation[1]-100);
						ToastUtil.showShort("请正确输入"+object.optString("ITEMNAME"));
						return false;
					}
					break;
				case 50://区域文字
					EditText editLeft=(EditText) contentLayout.getChildAt(i).findViewById(R.id.apt_case_template_item_edit_left);
					EditText editRight=(EditText) contentLayout.getChildAt(i).findViewById(R.id.apt_case_template_item_edit_right);
					String strLeft=editLeft.getText().toString().trim();
					String strRight=editRight.getText().toString().trim();
					if(HStringUtil.isEmpty(strLeft)||HStringUtil.isEmpty(strRight)){//为空或空字符串
						if(object.optInt("NEFILL")!=1)//不是必填,可以直接跳过判断
							continue;
						editLeft.getLocationInWindow(location);
						mScrolview.scrollTo(location[0], location[1]-scrLocation[1]-100);
						ToastUtil.showShort("请正确输入"+object.optString("ITEMNAME"));
						return false;
					}else{
						postObject.put("INFO", strLeft);
						postObject.put("INFO2", strRight);
					}
					break;
				case 60://日期
					EditText tvdate=(EditText) contentLayout.getChildAt(i).findViewById(R.id.apt_case_template_item_edit_right);
					String datestr=tvdate.getText().toString().trim();
					if(datestr==null||"".equals(datestr)){
						if(object.optInt("NEFILL")!=1)//不是必填,可以直接跳过判断
							continue;
						tvdate.getLocationInWindow(location);
						mScrolview.scrollTo(location[0], location[1]-scrLocation[1]-100);
						ToastUtil.showShort("请正确输入"+object.optString("ITEMNAME"));
						return false;
					}else{
						postObject.put("INFO", datestr);
					}
					break;
				case 70://大文本域
					EditText bigeditresult=(EditText) contentLayout.getChildAt(i).findViewById(R.id.apt_case_template_item_text_edit_big);
					String biginput=bigeditresult.getText().toString().trim();
					if(biginput==null||"".equals(biginput)){
						if(object.optInt("NEFILL")!=1)//不是必填,可以直接跳过判断
							continue;
						bigeditresult.getLocationInWindow(location);
						mScrolview.scrollTo(location[0], location[1]-scrLocation[1]-100);
						ToastUtil.showShort("请正确输入"+object.optString("ITEMNAME"));
						return false;
					}else{
						postObject.put("INFO", biginput);
					}
					break;
				}
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 设置单选滑动选择器
	 */
	private void setSingleSelector(final ArrayList<Map<String,String>> list,final TextView tv,final int index,final boolean isSex){
		SystemUtils.hideSoftAnyHow(getActivity());
		if(mPopupWindow != null && mPopupWindow.isShowing())mPopupWindow.dismiss();
		mPopupWindow = WheelUtils.showSingleWheel(getActivity(),list,tv,new OnClickListener() {
			@Override
			public void onClick(View v) {
				int index1 = (Integer)v.getTag(R.id.wheel_one);
				Map<String,String> map = list.get(index1);
				String name = map.get("name");
				if(!isSex){
					try {
						postJson.getJSONObject(index).put("SELECTION", map.get("code"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				tv.setText(name);
			}
		});
	}
	
	/**
	 * 设置出生日期
	 */
	private void setAgeDate(final int index) {
		if(agepop == null ){
			agepop=WheelUtils.showThreeDateWheel(getActivity(), view, this);
		}else if(agepop.isShowing()){
			agepop.dismiss();
		}else{
//			agepop.showAsDropDown(view);
			agepop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		update();
	}

	
//	//更新显示图片
//	private void update() {
//		Iterator<Entry<Integer, View>> iter = imgLayouts.entrySet().iterator();
//		while(iter.hasNext()){
//	        Map.Entry<Integer, View> info= iter.next();
//	        final int key = info.getKey();//获取健
//	        LinearLayout horLayout = (LinearLayout) info.getValue().findViewById(R.id.apt_case_template_item_images);//获取值
//	        horLayout.removeAllViews();
//	        ArrayList<ImageItem> list=Bimp.dataMap.get(""+key);//根据ID寻找
//	        LinearLayout horScrollLayout=(LinearLayout) info.getValue().findViewById(R.id.apt_case_template_item_images);
//	        if(list.size()>0){//
//	        	horScrollLayout.setLayoutParams(new LinearLayout.LayoutParams(
//	        			LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1f));
//	        }else//将加号放在中间
//	        	horScrollLayout.setLayoutParams(new LinearLayout.LayoutParams(
//	        			LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,0f));
//	        if(list.size()==Bimp.imgMaxs.get(""+key)){
//	        	info.getValue().findViewById(R.id.apt_case_template_item_img_add).setVisibility(View.GONE);
//	        }else
//	        	info.getValue().findViewById(R.id.apt_case_template_item_img_add).setVisibility(View.VISIBLE);
//	        for(int i=0;i<list.size();i++){
//	        	final int index=i;
//	        	System.out.println("---ThumbnailPath---"+list.get(index).getThumbnailPath()+
//	        			"---ImagePath---"+list.get(index).getImagePath());
//	        	MessageImageView image=new MessageImageView(getActivity());
//	        	image.setLayoutParams(new LayoutParams(DensityUtils.dip2px(getActivity(), 78), DensityUtils.dip2px(getActivity(), 78)));
//	        	image.setScaleType(ScaleType.CENTER_CROP);
//	        	image.setImageBitmap(list.get(i).getBitmap());
//	        	image.setDeleteListener(new OnClickListener() {//删除图片
//
//					@Override
//					public void onClick(View v) {
//						Bimp.dataMap.get(""+key).remove(index);
//						update();
//					}
//				});
//	        	image.setOnClickListener(new OnClickListener() {
//
//	        		@Override
//	        		public void onClick(View v) {
//	        			Intent intent = new Intent(getActivity(),GalleryActivity.class);
//	        			intent.putExtra("key", key+"");
//	        			intent.putExtra("position", "1");
//	        			intent.putExtra("ID", index);
//	        			startActivity(intent);
//	        		}
//	        	});
//	        	horLayout.addView(image);
//	        }
//		}
//
//	}

	//更新显示图片
	private void update() {
		if(!canEdit)
			return;
		caseImgLayout.removeAllViews();
		ArrayList<ImageItem> list=Bimp.dataMap.get(DConsultRecordFragment.class.getSimpleName());
		for(int i=0;i<list.size();i++){
			final int index=i;
			MessageImageView image=new MessageImageView(getActivity());
			image.setLayoutParams(new ViewGroup.LayoutParams(DensityUtils.dip2px(getActivity(), 78), DensityUtils.dip2px(getActivity(), 78)));
			image.setScaleType(ImageView.ScaleType.CENTER_CROP);
			ImageItem ii=list.get(index);
			if(ii.isNetPic)//如果是网络图片
				mImageLoader.displayImage(list.get(i).thumbnailPath, image.getImage());
			else
				image.setImageBitmap(list.get(i).getBitmap());
			image.setDeleteListener(new View.OnClickListener() {//删除图片

				@Override
				public void onClick(View v) {

					Bimp.dataMap.get(DConsultRecordFragment.class.getSimpleName()).remove(index);
					update();
				}
			});
			image.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(),GalleryActivity.class);
					intent.putExtra("key", DConsultRecordFragment.class.getSimpleName());
					intent.putExtra("position", "1");
					intent.putExtra("ID", index);
					startActivity(intent);
				}
			});
			caseImgLayout.addView(image);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if ( resultCode == Activity.RESULT_OK) {
				if(storageFile!=null){
					ImageItem takePhoto = new ImageItem();
					takePhoto.setImagePath(storageFile.getAbsolutePath());
					ArrayList<ImageItem> list=Bimp.dataMap.get(currentKey);
					list.add(takePhoto);
				}
			}
			break;
		default://多选返回
			if(resultCode==Activity.RESULT_OK){
				TextView multiText=multipleTexts.get(requestCode);
				StringBuilder sb=new StringBuilder();
				StringBuilder ids=new StringBuilder();
				ArrayList<Map<String,Object>> res=(ArrayList<Map<String, Object>>) data.getSerializableExtra("result");
				for(int i=0;i<res.size();i++){
					if((Boolean) res.get(i).get("isChecked")){
						ids.append(res.get(i).get("code").toString()+",");
						sb.append(res.get(i).get("name")+"\t\t");
					}
//					}else{
//						if((Boolean) res.get(i).get("isChecked")){
//							sb.append(+res.get(i).get("name"));
//						}
//					}
				}
				try {
					if(ids.length()>0)
						ids.deleteCharAt(ids.length()-1);
					postJson.getJSONObject(requestCode).put("SELECTION", ids);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				multiText.setText(sb.toString().trim());
				multiText.setOnClickListener(new MultipleChoiseClickListener(res, data.getStringExtra("title"), requestCode));
				
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 作用是:多选跳转的点击监听器
	 */
	class MultipleChoiseClickListener implements OnClickListener{
		ArrayList<Map<String,Object>> list;
		String name;
		int index;
		
		public MultipleChoiseClickListener(ArrayList<Map<String,Object>> list,String name, int index) {
			super();
			this.list = list;
			this.name=name;
			this.index = index;
		}

		@Override
		public void onClick(View v) {
			Intent intent=new Intent(getActivity(),TemplateItemMultipleChoiceActivity.class);
			intent.putExtra("list", list);
			intent.putExtra("title", name);
			startActivityForResult(intent,index);
		}
	}
	
	/**
	 * 弹出上传图片的选择布局
	 */
	public void showuploadPopWindow() {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View view = inflater.inflate(R.layout.interest_image_add_action, null);
		View mainView = inflater.inflate(R.layout.interest_content, null);
		if (addPhotoPop == null) {
			addPhotoPop = new PopupWindow(view, LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT);
			addPhotoPop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		}
		WheelUtils.setPopeWindow(getActivity(), mainView, addPhotoPop);
		Button cameraAdd = (Button) view.findViewById(R.id.cameraadd);
		Button galleryAdd = (Button) view.findViewById(R.id.galleryadd);
		Button cancel = (Button) view.findViewById(R.id.cancel);

		cameraAdd.setOnClickListener(this);
		galleryAdd.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}
	
	//相机
	public void photo() {
		storageFile=null;
		if(StorageUtils.isSDMounted()){
			try {
				storageFile = StorageUtils.createCameraFile();
				Uri uri = Uri.fromFile(storageFile);
				Intent intent = CropUtils.createPickForCameraIntent(uri);
				startActivityForResult(intent, TAKE_PICTURE);
			} catch (Exception e) {
				ToastUtil.showLong(getActivity(), "系统相机异常");
			}
		}else{
			SingleBtnFragmentDialog.showDefault(getChildFragmentManager(),  "sdcard未加载");
		}
	}
	
}
