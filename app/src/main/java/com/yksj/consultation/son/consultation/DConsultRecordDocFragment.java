package com.yksj.consultation.son.consultation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yksj.consultation.adapter.DynamicCaseTemplateAdapter;
import com.yksj.consultation.adapter.MultipleTextChoiceAdapter;
import com.yksj.consultation.comm.EditFragmentDialog;
import com.yksj.consultation.comm.ImageGalleryActivity;
import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.comm.SingleBtnFragmentDialog.OnClickSureBtnListener;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.views.widget.Tag;
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
 *  医生端会诊病历模板
 */
public class DConsultRecordDocFragment extends RootFragment implements
		OnClickListener{
	private static final int TAKE_PICTURE = 0x000001;
	private static final int TASK_KEY_WORDS_DATA = 0xfff2;

	private int caseType=2;//TYPE=1  等待基层医生发送病历模板,TYPE=2 首次填写，加载出病历模板,TYPE=3  填写后的病历详情
	private View view;//fragment布局
	private View wheelView;//滑轮选择器布局
	private LinearLayout contentLayout,complateLayout;
	private Button btnComplate;//完成
	private DynamicCaseTemplateAdapter mAdapter;
	CancelClickListener cancelListener =new CancelClickListener();
	private TextView tvTip,tvChoiseTip;//第二个是单选推送模板的提示文字
	private LinearLayout docBtnLayout;
	private Button btnDcoAdd,btnDcoSubmit,btnDocShare;//帮他补充,提交专家,共享病历
	
	private HashMap<Integer, TextView> multipleTexts;//多选的结果返回应填入对应的EditText
	private HashMap<Integer, View> itemViews=new HashMap<Integer, View>();//多选的图片结果返回应添加到对应的LinearLayout
	private HashMap<Integer, Integer> seqs=new HashMap<Integer, Integer>();//患者上传上传的图片的最后一张的seq
	public static StringBuilder dePics=new StringBuilder();
	PopupWindow mPopupWindow,pop,addPhotoPop;
	private PopupWindow agepop;// 年龄的pop
	
	private JSONArray postJson;//上传的json字符数据
	private JSONObject dataObject;
	private ArrayList<JSONObject> datas;//网络加载过来的数据
	private String consultionId="50",recordId;
	
	public static Bitmap bimap ;
	private String currentKey="-1";//当前行的ITEMID为-1
	private File storageFile=null;
	private ImageLoader mImageLoader;

	private ListView singleListView;//单选推送模板的ListView
	private ScrollView mScrolview;
	private Bundle bundle;
	SingleBtnFragmentDialog postDialog=null;//上传提示对话框
	private MultipleTextChoiceAdapter singleAdapter;//单选推送模板适配器
	private ArrayList<Map<String,Object>> templates;
	private int cusPos=-1;//单选模板当前选择的位置
	private boolean isUploading=false;//是否正在上传
	private EditText dateEdit;

	//患者信息
	private TextView tvPatientName,tvPatientSex,tvPatientBirthday,tvPatientAge,
			tvPatientZhiye,tvPatientPhone,tvPatientCode;
	private EditText editPatientName,editPatientZhiye,editPatientPhone,editPatientBirthday,editPatientCode;
	private TextView editPatientSex,editPatientAge;
	private ImageView imgAddImg,imgAddAudio,imgAddVideo;
	private RelativeLayout addImgResourceLayout;
	private LinearLayout caseImgLayout;//影像资料布局
	private boolean canEdit=false;//true为编辑模式,false为浏览模式
	private RelativeLayout keyWordsRelativeLayout;//关键词整个布局
	private LinearLayout keysLayout;//关键词
	private Button btnEditKeys;//编辑关键词
	private JSONArray keysArray;//关键词Array

	private ArrayList<Tag> systemTags;
	private JSONArray keysData;//用户选择的关键字


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fgt_dynamic_case_template, null);
		bundle = getArguments();
		consultionId=bundle.getString("consultationId");//拿到会诊id
		currentKey=DConsultRecordDocFragment.class.getSimpleName();
		initView();
		initData();
		return view;
	}

	//初始化数据
	private void initData() {
		HttpRestClient.doHttpConsultionCaseTemplate("4", null, consultionId, new AsyncHttpResponseHandler(getActivity()) {

			@Override
			public void onSuccess(String content) {
				JSONObject object;
				datas = new ArrayList<JSONObject>();
				try {
					object = new JSONObject(content);
					dataObject = object;
					if (!object.has("errormessage")) {
						caseType = Integer.parseInt(object.optString("TYPE"));
						if (caseType == 1) {//需要给用户选择模板,推送模板
							view.findViewById(R.id.case_patient_info_layout).setVisibility(View.GONE);
							view.findViewById(R.id.case_keywords_layout_id).setVisibility(View.GONE);
							view.findViewById(R.id.dynamic_case_img_audio_linearlayout).setVisibility(View.GONE);
							caseImgLayout.setVisibility(View.GONE);
							initSingleChoise();
							String arrayStr = object.optString("CONTENT");
							templates = JsonParseUtils.parseMultipleChoiseData(arrayStr);
							if (templates.size() != 0) {
								singleAdapter.onBoundData(templates);
							}

						} else if (caseType == 2) {//等待用户填写完成
							tvTip.setVisibility(View.VISIBLE);
							mScrolview.setVisibility(View.GONE);
							tvTip.setText(object.optString("CONTENT"));
						} else if (caseType == 3) {//患者已经填写
							if (!"1".equals(object.optString("COMMIT"))) {//不可以提交给专家
								btnDcoSubmit.setBackgroundResource(R.drawable.doctor_clinic_can_not_buy);
								btnDcoSubmit.setClickable(false);
							} else {
								btnDcoSubmit.setBackgroundResource(R.drawable.bt_short_green);
								btnDcoSubmit.setClickable(true);
							}
							if (!"1".equals(object.optString("EDIT"))) {//不可以给患者补充
								btnDcoAdd.setBackgroundResource(R.drawable.doctor_clinic_can_not_buy);
								btnDcoAdd.setClickable(false);
							}
							if ("1".equals(object.optString("ISSHARE"))) {//不可以分享
								btnDocShare.setBackgroundResource(R.drawable.doctor_clinic_can_not_buy);
								btnDocShare.setClickable(false);
							}

							mScrolview.setPadding(0, 0, 0, 100);
							docBtnLayout.setVisibility(View.VISIBLE);
							JSONArray array = object.getJSONArray("CONTENT");
							postJson = new JSONArray();//以作以后上传修改自用
							for (int i = 0; i < array.length(); i++) {
								postJson.put(JsonParseUtils.getPostTemplateObject(array.getJSONObject(i)));
								datas.add(array.getJSONObject(i));
							}
							canEdit = false;
							onBoundPatientInfo(object);
							onBoundDetailData(datas);//绑定数据
							recordId=object.optString("RECORDID");
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(content);
			}

		});
	}

	/**
	 * 绑定患者信息到控件
	 * @param object
	 */
	private void onBoundPatientInfo(JSONObject object){
		try {
			keysArray=object.getJSONArray("FLAGCONTENT");
			keysLayout.removeAllViews();
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
		if(keysArray==null||keysArray.length()==0){
			keyWordsRelativeLayout.setVisibility(View.GONE);
		}
		if(!canEdit){//展示患者信息,不可编辑
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

			btnEditKeys.setVisibility(View.GONE);
			addImgResourceLayout.setVisibility(View.GONE);
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
		}else{//帮患者编辑病历信息
			if(keysArray!=null&&keysArray.length()>0)
				btnEditKeys.setVisibility(View.VISIBLE);
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

			addImgResourceLayout.setVisibility(View.VISIBLE);
			editPatientName.setText(object.optString("NAME"));
			if("W".equals(object.optString("SEX"))){
				editPatientSex.setText("女");
			}else if("M".equals(object.optString("SEX"))){
				editPatientSex.setText("男");
			}
			if(object.has("PHONE")){//包含这个键
				editPatientPhone.setText(object.optString("PHONE").trim());
			}else{//不包含
				view.findViewById(R.id.create_case_tv_phone_left).setVisibility(View.GONE);
				editPatientPhone.setVisibility(View.GONE);
			}
			editPatientZhiye.setText(object.optString("METIER"));
			String bir=object.optString("BIRTHDAY");
			if(bir!=null&&bir.length()!=0){
				String nian=bir.substring(0,4);
				String yue=bir.substring(4,6);
				String ri=bir.substring(6, 8);
				Calendar cc=Calendar.getInstance();
				int age=cc.get(Calendar.YEAR)-Integer.parseInt(nian);
				if(age>=0){
					editPatientAge.setText(age+"");
					editPatientBirthday.setText(nian+"-"+yue+"-"+ri);
				}
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
			editPatientCode.setText(object.optString("CODE"));
			editPatientBirthday.setOnClickListener(this);
		}
		onBoundImgData(object.optString("RECORDFILE"), canEdit);
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
	 * 当基层医生还没给用户选择模板时
	 * 初始化模板选择列表
	 */
	private void initSingleChoise(){
		tvChoiseTip.setText(R.string.please_chose_template);
		singleListView.setVisibility(View.VISIBLE);
		complateLayout.setVisibility(View.VISIBLE);
		tvChoiseTip.setVisibility(View.VISIBLE);
		singleAdapter=new MultipleTextChoiceAdapter(mActivity);
		singleListView.setAdapter(singleAdapter);
		btnComplate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				submitTemplate();//提交模板
			}
		});
		singleListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				singleAdapter.itemCheck(position);
				if (cusPos == position) {//选中的位置当前位置一样
					cusPos = -1;
				} else {//不一样
					if (cusPos != -1)//
						singleAdapter.itemCheck(cusPos);
					cusPos = position;
				}
			}
		});
	}
	
	//提胶模板给患者填写的接口
	private void submitTemplate() {
		if(cusPos==-1){
			ToastUtil.showShort("请先选择病历模板");
			return;
		}
		String itemId=templates.get(cusPos).get("code").toString();
		final String templateName=templates.get(cusPos).get("name").toString();
		HttpRestClient.doHttpConsultionCaseTemplate("5", itemId, consultionId, new AsyncHttpResponseHandler(getActivity()) {

			@Override
			public void onSuccess(String content) {
				try {
					JSONObject object = new JSONObject(content);
					if (object.has("errormessage")) {
						ToastUtil.showShort(object.optString("errormessage"));
					} else {
						ToastUtil.showShort(object.optString("INFO"));
						tvChoiseTip.setText("已为患者选择 ：" + templateName + "，等待患者填写");
						singleListView.setVisibility(View.GONE);
						complateLayout.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(content);
			}

		});
		
	}
	
	/**
	 * 绑定LinearLayout数据
	 */
	private void onBoundData(ArrayList<JSONObject> datas) {
		contentLayout.removeAllViews();
		complateLayout.setVisibility(View.VISIBLE);
		multipleTexts=new HashMap<Integer, TextView>();
		for(int i=0;i<datas.size();i++){
			final int index=i;
			final JSONObject entity=datas.get(i);
			View itemView=LayoutInflater.from(getActivity()).inflate( R.layout.apt_consultion_case_item_text, null,true);
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
			Button imgAdd=(Button) itemView.findViewById(R.id.apt_case_template_item_img_add);
			
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
				editText.setText(entity.optString("INFO"));
				break;
			case 20://单选
				tvEditLeft.setText(entity.optString("ITEMNAME"));
				tvRight.setVisibility(View.VISIBLE);
				editText.setVisibility(View.GONE);
				HashMap<String, String> keyMap=new HashMap<String, String>();
				String keys=entity.optString("SELECTION");
				try {
					postJson.getJSONObject(i).put("SELECTION", keys);
					JSONArray option=entity.getJSONArray("OPTION");
					for(int l=0;l<option.length();l++){
						JSONObject optionObject=option.getJSONObject(l);
						keyMap.put(""+optionObject.optInt("OPTIONID"), optionObject.optString("OPTIONNAME"));
					}
					tvRight.setText(keyMap.get(keys));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
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
				editText.setVisibility(View.GONE);
				String selectStr2="";
				String selectionString=entity.optString("SELECTION");
				String[] keys2=selectionString.split(",");
				ArrayList<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
				try {
					postJson.getJSONObject(i).put("SELECTION", entity.optString("SELECTION"));
					JSONArray option=entity.getJSONArray("OPTION");
					for(int l=0;l<option.length();l++){
						JSONObject object=option.getJSONObject(l);
						HashMap< String, Object> map=new HashMap<String, Object>();
						map.put("name", object.optString("OPTIONNAME"));
						map.put("code", ""+object.optInt("OPTIONID"));
						map.put("isChecked", false);
						list.add(map);
					}
					if(!"".equals(keys2[0])){
						for(int m=0;m<keys2.length;m++){
							for(int n=0;n<list.size();n++){
								if(Integer.parseInt((String)list.get(n).get("code"))==Integer.parseInt(keys2[m])){
									if(m==0)
										selectStr2=(String)list.get(n).get("name");
									else
										selectStr2=selectStr2+"，"+(String)list.get(n).get("name");
									list.get(n).put("isChecked", true);
								}
							}
						}
					}
					tvRight.setText(selectStr2);
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				multipleTexts.put(index, tvRight);
				tvRight.setOnClickListener(new MultipleChoiseClickListener(list,entity.optString("ITEMNAME"), index));
				break;
			case 40://单数字填写
				tvEditLeft.setText(entity.optString("ITEMNAME"));
				editText.setVisibility(View.VISIBLE);
				editText.setInputType(InputType.TYPE_CLASS_NUMBER);
				tvRight.setVisibility(View.GONE);
				editText.setText(entity.optString("INFO"));
				break;
			case 80://有小数点的情况
				tvEditLeft.setText(entity.optString("ITEMNAME"));
				editText.setVisibility(View.VISIBLE);
				editText.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
				tvRight.setVisibility(View.GONE);
				editText.setText(entity.optString("INFO"));
				break;
			case 50://区域数字填写90~100
				tvEditLeft.setText(entity.optString("ITEMNAME"));
				editText.setVisibility(View.VISIBLE);
				editTextLeft.setVisibility(View.VISIBLE);
				editText.setInputType(InputType.TYPE_CLASS_NUMBER);
				tvRight.setVisibility(View.VISIBLE);
				tvRight.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				tvRight.setText("~");
				tvRight.setTextColor(getResources().getColor(R.color.color_green_blue_bg));
				editTextLeft.setText(entity.optString("INFO"));
				editText.setText(entity.optString("INFO2"));
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
				editText.setText(entity.optString("INFO"));
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
				bigEditText.setText(entity.optString("INFO"));
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
			case 40://单数字填写
			case 80://小数
			case 60://日期
				tvLeft.setText(entity.optString("INFO"));
				break;
			case 90://只有ItemName
				tvLeft.setVisibility(View.GONE);
				break;
			case 20://单选
			case 30://多选
				HashMap<String, String> keyMap=new HashMap<String, String>();
				String selectStr="";
				String[] keys=entity.optString("SELECTION").split(",");
				try {
					JSONArray option=entity.getJSONArray("OPTION");
					if(!"".equals(keys[0])){
						for(int l=0;l<option.length();l++){
							JSONObject optionObject=option.getJSONObject(l);
							keyMap.put(""+optionObject.optInt("OPTIONID"), optionObject.optString("OPTIONNAME"));
						}
						selectStr+=keyMap.get(keys[0]);
						for(int m=1;m<keys.length;m++){
							selectStr=selectStr+"，"+keyMap.get(keys[m]);
						}
					}
					tvLeft.setText(selectStr);
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
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
				break;
			}
			contentLayout.addView(itemView);
		}
	}

	private void onBoundImgData(String recordfiles,boolean canEdit){
		caseImgLayout.removeAllViews();
		Bimp.dataMap.get(currentKey).clear();//先清空以前的数据
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
				if(!canEdit){//可以编辑

					ImageView imageview=new ImageView(getActivity());
					imageview.setLayoutParams(new LayoutParams(DensityUtils.dip2px(getActivity(), 78), DensityUtils.dip2px(getActivity(), 78)));
					imageview.setScaleType(ScaleType.CENTER_CROP);
					mImageLoader.displayImage(imgObject.optString("ICON"), imageview);//加载小图片
					imageview.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getActivity(), ImageGalleryActivity.class);
							intent.putExtra(ImageGalleryActivity.URLS_KEY, sb.toString().split(","));
							intent.putExtra(ImageGalleryActivity.TYPE_KEY, 1);
							intent.putExtra("type", 1);// 0,1单个,多个
							intent.putExtra("position", imgPosition);
							startActivityForResult(intent, 100);
						}
					});
					caseImgLayout.addView(imageview);
				}else{
					String smallPath=imgObject.optString("ICON");
					ImageItem ii=new ImageItem();//存放网络图片
					ii.pidId=imgObject.optInt("ID");
					ii.isNetPic=true;
					ii.thumbnailPath=smallPath;
					ii.imagePath=smallPath.replace("-small", "");
					Bimp.dataMap.get(currentKey).add(ii);
				}

			}
			if(canEdit)//如果可编辑刷新一下
				update();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	//初始化控件
	private void initView() {
		tvTip = (TextView) view.findViewById(R.id.dynamic_case_template_doc_tip);
		mScrolview = (ScrollView) view.findViewById(R.id.dynamic_case_template_scrollview);
		docBtnLayout = (LinearLayout) view.findViewById(R.id.dynamic_case_template_doctor_layout);
		tvChoiseTip=(TextView) view.findViewById(R.id.template_single_choise_tip);
		singleListView=(ListView) view.findViewById(R.id.template_single_choise_listview);
		btnDcoAdd=(Button) view.findViewById(R.id.dynamic_case_template_doctor_add);//帮他补充
		btnDcoSubmit=(Button) view.findViewById(R.id.dynamic_case_template_doctor_submit);
		btnDocShare=(Button) view.findViewById(R.id.dynamic_case_template_doctor_share);
		mImageLoader=ImageLoader.getInstance();
		contentLayout=(LinearLayout) view.findViewById(R.id.dynamic_case_template_linearlayout);
		btnComplate=(Button) view.findViewById(R.id.dynamic_case_template_complate);
		complateLayout=(LinearLayout) view.findViewById(R.id.dynamic_case_template_patient_layout);
		btnComplate.setOnClickListener(this);
		btnDocShare.setOnClickListener(this);
		wheelView = getActivity().getLayoutInflater().inflate(R.layout.wheel, null);
		wheelView.findViewById(R.id.wheel_cancel).setOnClickListener(this);
		wheelView.findViewById(R.id.wheel_sure).setOnClickListener(this);

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

		addImgResourceLayout= (RelativeLayout) view.findViewById(R.id.dynamic_case_add_image_resource_layout);
		caseImgLayout= (LinearLayout) view.findViewById(R.id.dynamic_case_img_layout);
		imgAddImg= (ImageView) view.findViewById(R.id.create_case_add_image);
		imgAddAudio= (ImageView) view.findViewById(R.id.create_case_add_audio);
		imgAddVideo= (ImageView) view.findViewById(R.id.create_case_add_video);
		imgAddImg.setOnClickListener(this);
		imgAddAudio.setOnClickListener(this);
		imgAddVideo.setOnClickListener(this);

		caseImgLayout= (LinearLayout) view.findViewById(R.id.dynamic_case_img_layout);
		keyWordsRelativeLayout= (RelativeLayout) view.findViewById(R.id.case_keywords_layout_id);
		keysLayout= (LinearLayout) view.findViewById(R.id.create_case_keywords);
		btnEditKeys= (Button) view.findViewById(R.id.create_case_keywords_add);
		btnEditKeys.setOnClickListener(this);
		
		bimap = BitmapFactory.decodeResource(getResources(),R.drawable.img_add_icon);
		btnDcoAdd.setOnClickListener(this);
		btnDcoSubmit.setOnClickListener(this);
		ArrayList<ImageItem> itemImgs=new ArrayList<ImageItem>();//
		Bimp.dataMap.put(DConsultRecordDocFragment.class.getSimpleName(), itemImgs);
		Bimp.imgMaxs.put(DConsultRecordDocFragment.class.getSimpleName(), 32);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.btn_finish:
//			ToastUtil.showShort("完成");
//			break;
		case R.id.dynamic_case_template_doctor_add://帮他补充
			onHelpAdd();//帮他补充
			break;
		case R.id.dynamic_case_template_doctor_submit://提交专家
			onPostExpert();//上传给专家
			
			break;
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
							} else {
								ToastUtil.showShort(getActivity(), "无法完成分享病历操作，病历名称不能为空");
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
		case R.id.dynamic_case_template_complate://用户输入完成,遍历获取数据,开始提交
		
			if(verifyData()){//验证完成,提交
				complateLayout.setVisibility(View.INVISIBLE);
				RequestParams params=new RequestParams();
				params.putNullFile("file", new File(""));
				params.put("CUSTID", SmartFoxClient.getLoginUserId());
				params.put("RECORDID", recordId);//RECORDID
				params.put("CONSULTATIONID", consultionId);
				params.put("CONTENT", postJson.toString());
				//患者信息
				params.put("NAME", editPatientName.getText().toString().trim());
				params.put("METIER", editPatientZhiye.getText().toString().trim());
				params.put("PHONE", editPatientPhone.getText().toString().trim());
				params.put("CODE", editPatientCode.getText().toString().trim());
				if("男".equals(editPatientSex.getText().toString().trim())){
					params.put("SEX", "M");
				}else if("女".equals(editPatientSex.getText().toString().trim())){
					params.put("SEX", "W");
				}
				String bir=editPatientBirthday.getText().toString().trim();
				if(bir!=null&&bir.length()!=0){
					String[] srr=bir.split("-");
					params.put("BIRTHDAY", srr[0]+srr[1]+srr[2]+"0000");
				}else{
					params.put("BIRTHDAY","");
				}

				if(dePics.length()!=0)
					params.put("DEPIC", dePics.toString());
				putFile(params);
				
				HttpRestClient.doHttpPostConsultionCaseTemplate(params,
						new AsyncHttpResponseHandler(){
					@Override
					public void onStart() {
						postDialog=SingleBtnFragmentDialog.show(getChildFragmentManager(), "提示:",
								"正在上传,请耐心等待!", "确定", cancelListener);
						postDialog.setCancelable(false);//不可被取消
						super.onStart();
					}
					
					@Override
					public void onFinish() {
						postDialog.dismiss();
						isUploading=false;
						complateLayout.setVisibility(View.VISIBLE);
						super.onFinish();
					}
					
					@Override
					public void onFailure(Throwable error,
							String content) {
						isUploading=false;
						complateLayout.setVisibility(View.VISIBLE);
						super.onFailure(error, content);
					}
					
					@Override
					public void onSuccess(String content) {
						try {
							JSONObject object=new JSONObject(content);
							if(object.has("errorcode")){
								ToastUtil.showShort(object.optString("errormessage"));
							}else{
								ToastUtil.showShort(object.optString("INFO"));
								initData();//重新加载一下数据
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						super.onSuccess(content);
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
			dateEdit.setText(str[0].substring(0, str[0].length()-1)+"-" + str[1].substring(0, str[1].length()-1)
					+"-"+ str[2].substring(0, str[2].length()-1));
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
	
	//帮他补充,请求判断是否可以补充
	private void onHelpAdd() {
		HttpRestClient.doHttpConsultionCaseTemplate("7", null, consultionId, new AsyncHttpResponseHandler(getActivity()){
			@Override
			public void onSuccess(String content) {
				try {
					JSONObject object=new JSONObject(content);
					if(object.has("errormessage")){
						ToastUtil.showShort(object.optString("errormessage"));
					}else{
						recordId=object.optString("RECORDID");
						canEdit=true;
						onBoundPatientInfo(dataObject);
						onBoundData(datas);
						docBtnLayout.setVisibility(View.GONE);
						mScrolview.setPadding(0, 0, 0, 0);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(content);
			}
		});
		
	}

	//提交给专家
	private void onPostExpert() {
		RequestParams params=new RequestParams();
		params.put("OPTION", "6");
		params.put("CONSULTATIONID", consultionId);//会诊id
		params.put("DOCTORID", SmartFoxClient.getLoginUserId());
		HttpRestClient.doHttpSubmitCaseTemplate(params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				try {
					JSONObject object = new JSONObject(content);
					if (object.has("errormessage")) {
						ToastUtil.showShort(object.getString("errormessage"));
					} else {
						ToastUtil.showShort("提交成功");
						btnDcoSubmit.setBackgroundResource(R.drawable.doctor_clinic_can_not_buy);
						btnDcoSubmit.setClickable(false);
						if (!"1".equals(object.optString("EDIT"))) {//不可以给患者补充
							btnDcoAdd.setBackgroundResource(R.drawable.doctor_clinic_can_not_buy);
							btnDcoAdd.setClickable(false);
						} else {
							btnDcoAdd.setBackgroundResource(R.drawable.bt_short_green);
							btnDcoAdd.setClickable(true);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(content);
			}
		});
		
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
						btnDocShare.setBackgroundResource(R.drawable.doctor_clinic_can_not_buy);
						btnDocShare.setClickable(false);
					}
				} catch (JSONException e) {
					e.printStackTrace();
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
//		Iterator<Entry<Integer, View>> iter = itemViews.entrySet().iterator();
//		while(iter.hasNext()){
//	        Map.Entry<Integer, View> info= iter.next();
//	        final int key = info.getKey();//获取健
//	        int m=seqs.get(key);
//	        ArrayList<ImageItem> list=Bimp.dataMap.get(""+key);//根据ID寻找
//	        for(int i=0;i<list.size();i++){
//	        	if(!(list.get(i).isNetPic)){//不是网络图片才上传
//	        		m++;
//	        		try {
//	        			params.put(key+"-"+m+".jpg", onGetDecodeFileByPath(list.get(i).getImagePath()));
//	        		} catch (FileNotFoundException e) {
//	        			e.printStackTrace();
//	        		}
//	        	}
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
					params.put(index+".jpg", BitmapUtils.onGetDecodeFileByPath(
							getActivity(),list.get(i).getImagePath()));
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
		System.out.println("--------"+isUploading);
		if(isUploading){
			isUploading=true;
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
					postObject.put("INFO", input);
					if(object.optInt("NEFILL")==1)//是必填
						if(input==null||"".equals(input)){
							editresult.getLocationInWindow(location);
							mScrolview.scrollTo(location[0], location[1]-scrLocation[1]-100);
							ToastUtil.showShort("请正确输入"+object.optString("ITEMNAME"));
							return false;
						}
					break;
				case 20://单选
				case 30://多选
					TextView tvRight=(TextView) contentLayout.getChildAt(i).findViewById(R.id.apt_case_template_item_text_right);
					String singlestr=postObject.optString("SELECTION");
					if(object.optInt("NEFILL")==1)//是必填
						if(singlestr==null||"".equals(singlestr)){
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
					postObject.put("INFO", strLeft);
					postObject.put("INFO2", strRight);
					if(object.optInt("NEFILL")==1)//是必填
						if(HStringUtil.isEmpty(strLeft)||HStringUtil.isEmpty(strRight)){//为空或空字符串
							editLeft.getLocationInWindow(location);
							mScrolview.scrollTo(location[0], location[1]-scrLocation[1]-100);
							ToastUtil.showShort("请正确输入"+object.optString("ITEMNAME"));
							return false;
						}
					break;
				case 60://日期
					EditText tvdate=(EditText) contentLayout.getChildAt(i).findViewById(R.id.apt_case_template_item_edit_right);
					String datestr=tvdate.getText().toString().trim();
					postObject.put("INFO", datestr);
					if(object.optInt("NEFILL")==1)//是必填
						if(datestr==null||"".equals(datestr)){
							tvdate.getLocationInWindow(location);
							mScrolview.scrollTo(location[0], location[1]-scrLocation[1]-100);
							ToastUtil.showShort("请正确输入"+object.optString("ITEMNAME"));
							return false;
						}
					break;
				case 70://大文本域
					EditText bigeditresult=(EditText) contentLayout.getChildAt(i).findViewById(R.id.apt_case_template_item_text_edit_big);
					String biginput=bigeditresult.getText().toString().trim();
					postObject.put("INFO", biginput);
					if(object.optInt("NEFILL")==1)//是必填
						if(biginput==null||"".equals(biginput)){
							bigeditresult.getLocationInWindow(location);
							mScrolview.scrollTo(location[0], location[1]-scrLocation[1]-100);
							ToastUtil.showShort("请正确输入"+object.optString("ITEMNAME"));
							return false;
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
				if(!isSex) {
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

	//更新显示图片
	private void update() {
		if(!canEdit)
			return;
		caseImgLayout.removeAllViews();
		ArrayList<ImageItem> list=Bimp.dataMap.get(DConsultRecordDocFragment.class.getSimpleName());
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
					if(dePics.length()==0)
						dePics.append(Bimp.dataMap.get(currentKey).get(index).pidId);
					else
						dePics.append(","+Bimp.dataMap.get(currentKey).get(index).pidId);
					Bimp.dataMap.get(DConsultRecordDocFragment.class.getSimpleName()).remove(index);
					update();
				}
			});
			image.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(),GalleryActivity.class);
					intent.putExtra("key", DConsultRecordDocFragment.class.getSimpleName());
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
//				String fileName = String.valueOf(System.currentTimeMillis());
//				Bitmap bm = (Bitmap) data.getExtras().get("data");//这样返回的是小图
//				String path=FileUtils.saveBitmapPath(bm, fileName);
				if(storageFile!=null){
					ImageItem takePhoto = new ImageItem();
					takePhoto.setImagePath(storageFile.getAbsolutePath());
					ArrayList<ImageItem> list=Bimp.dataMap.get(currentKey);
					list.add(takePhoto);
				}
			}
			break;
		case 3306:
			
			break;
		case TASK_KEY_WORDS_DATA://返回关键词标签
			if ( resultCode == Activity.RESULT_OK) {
				try {
					systemTags= (ArrayList<Tag>) data.getSerializableExtra("result1");
//                        customerTags= (ArrayList<Tag>) data.getSerializableExtra("result2");
					keysData= new JSONArray();
					for (int i=0;i<systemTags.size();i++){
						JSONObject object=new JSONObject();
						if(systemTags.get(i).getsID()==-2)
							object.put("ID","");//是患者自定义的标签
						else
							object.put("ID",""+systemTags.get(i).getsID());
						object.put("NAME",systemTags.get(i).getTitle());
						keysData.put(object);
					}
					keysLayout.removeAllViews();
					for(int i=0;i<keysData.length();i++){
						JSONObject object=keysData.getJSONObject(i);
						Button btn=new Button(getActivity());
						btn.setText(object.optString("NAME"));
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
