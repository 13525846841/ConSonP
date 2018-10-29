package com.yksj.consultation.son.home;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yksj.consultation.adapter.InterestAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.CommonExplainActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.views.TagsGridView;
import com.yksj.healthtalk.db.DictionaryHelper;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.entity.TagEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.JsonsfHttpResponseHandler;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.BitmapUtils;
import com.yksj.healthtalk.utils.DataParseUtil;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ImageUtils;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.ViewFinder;
import com.yksj.healthtalk.utils.WheelUtils;

import org.cropimage.CropUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.ImageLoader;
import org.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
/**
 * 个人资料  编辑
 * @author jack_tang
 *
 */
public class PersonInfoEditActivity extends BaseFragmentActivity implements OnClickListener, OnItemClickListener {

	private CustomerInfoEntity mLoginUserInfo;
	private PopupWindow listpop;//相册
	private PopupWindow pop;
	private View wheelView;
	private View mainView;
	private List<Map<String, String>> proList = null;
	public final int PHOTOHRAPH = 1001;// 拍照
	public final int PHOTORESOULT = 1003;// 结果
//	private final int ONEREQUEST = 1000;// 第一次请求数据
	private final int EDIT_DESC = 2000;// 描述
	private final int EDIT_NAME = 2001;// 名字
	private File currentCameraFile;
	private TextView CURRENTVIEW;
	private TextView zhiye;
	private TextView shengri;
	private TextView xingbie;
//	private TextView CURRENTVIEW;
	private TextView suozaidi;
	private PopupWindow agepop;// 年龄的pop
	private String locationCode = "";//所在地编码
	private File headerFile;//拍照文件
	private List<TagEntity> mBodyList, mXingQuList;
	private Map<String, List<Map<String, String>>> cityMap = new LinkedHashMap<String, List<Map<String, String>>>();
	private ImageView mHeadImage;
	private TagsGridView mGridXingQu;
	private TagsGridView mGridBody;
	private InterestAdapter mAdapterBody;
	private InterestAdapter mAdapterXingQu;
	private final int WHAT_PERSON_XINGQU = 100;// 兴趣list选择
	private final int WHAT_PERSON_BODY = 101;// 身体选择
	private boolean isEdit = false;//是否编辑过
	private ViewFinder mFinderView;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.person_info_edit_layout);
		initView();
	}

	
	private void initView() {
		mFinderView = new ViewFinder(this);
		mainView= getLayoutInflater().inflate(R.layout.person_info_edit_layout, null);
		mLoginUserInfo = SmartFoxClient.getLoginUserInfo();
		initTitle();
		titleLeftBtn.setOnClickListener(this);
		titleTextV.setText("编辑资料");
		mHeadImage = mFinderView.imageView(R.id.head);
		ImageLoader instance = ImageLoader.getInstance();
		instance.displayImage(mLoginUserInfo.getSex(),mLoginUserInfo.getNormalHeadIcon(),mHeadImage);
		mFinderView.setText(R.id.username, mLoginUserInfo.getNickName());
		mFinderView.setText(R.id.sex, mLoginUserInfo.getSexText());
		mFinderView.setText(R.id.birthday, mLoginUserInfo.getBirthday());
		mFinderView.setText(R.id.location, mLoginUserInfo.getDwellingplace());
		mFinderView.setText(R.id.occupation, mLoginUserInfo.getMetier());
		mFinderView.setText(R.id.desc, mLoginUserInfo.getDescription());
		mFinderView.setText(R.id.user_number, mLoginUserInfo.getUsername());
		mFinderView.onClick(this, new int[]{R.id.health_layout,R.id.style_layout,
				R.id.desc_action,R.id.occupation_action,R.id.location_action,
				R.id.birthday_action,R.id.sex_action,R.id.name_action,R.id.head_action});
		
		wheelView = getLayoutInflater().inflate(R.layout.wheel, null);
		wheelView.findViewById(R.id.wheel_cancel).setOnClickListener(this);
		wheelView.findViewById(R.id.wheel_sure).setOnClickListener(this);
		View checklist = getLayoutInflater().inflate(R.layout.personal_photo_check, null);
		pop = new PopupWindow(wheelView, LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		listpop = new PopupWindow(checklist, LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		checklist.findViewById(R.id.paizhao).setOnClickListener(this);
		checklist.findViewById(R.id.bendifenjian).setOnClickListener(this);
		checklist.findViewById(R.id.quxiao).setOnClickListener(this);
		suozaidi = (TextView) findViewById(R.id.location);
		shengri = (TextView) findViewById(R.id.birthday);
		zhiye = (TextView) findViewById(R.id.occupation);
		xingbie = (TextView) findViewById(R.id.sex);
		
		
		
		mGridXingQu = (TagsGridView) findViewById(R.id.style_view);
		mXingQuList = DataParseUtil.JsonToListEntity(mLoginUserInfo.getLableJson(), 0);
		mAdapterXingQu = new InterestAdapter(this, mXingQuList,
				R.layout.textiterm);
		mGridXingQu.setAdapter(mAdapterXingQu);
		if(mXingQuList.size()==0)findViewById(R.id.style_empty_view).setVisibility(View.VISIBLE);
		else findViewById(R.id.style_empty_view).setVisibility(View.GONE);
		
		mGridBody = (TagsGridView) findViewById(R.id.health_view);
		mBodyList = DataParseUtil.JsonToListEntity(mLoginUserInfo.getSameExperience(), 1);
		mAdapterBody = new InterestAdapter(this, mBodyList, R.layout.textiterm);
		mGridBody.setAdapter(mAdapterBody);
		if(mBodyList.size()==0)findViewById(R.id.health_empty_view).setVisibility(View.VISIBLE);
		else findViewById(R.id.health_empty_view).setVisibility(View.GONE);
		
		mGridXingQu.setOnItemClickListener(this);
		mGridBody.setOnItemClickListener(this);
		queryData();
	}
	/**
	 * 点击编辑的时候,开启分线程查询并封转城市数据及信息层面数据
	 */
	private void queryData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				proList = DictionaryHelper.getInstance(PersonInfoEditActivity.this).setCityData(
						PersonInfoEditActivity.this, cityMap);
			}
		}).start();
	}

	@Override
	public void onClick(View v) {
			isEdit=true;
		Intent intent;
		switch (v.getId()) {
		case R.id.health_layout://健康关注
//			changesPages(true);
			break;
		case R.id.style_layout://个性标签
//			changesPages(false);
			break;
		case R.id.desc_action://描述
			intent=new Intent(this,CommonExplainActivity.class);
			intent.putExtra(CommonExplainActivity.TITLE_NAME, "介绍");  
			intent.putExtra(CommonExplainActivity.TEXT_CONUT, 100);  //字数限制  默认是200
			intent.putExtra(CommonExplainActivity.TEXT_CONTENT, mFinderView.textView(R.id.desc).getText().toString());  //内容
			startActivityForResult(intent, EDIT_DESC);
			break;
		case R.id.occupation_action://职业
			CURRENTVIEW = zhiye;
			setZhiye();
			break;
		case R.id.location_action://位置选择
			CURRENTVIEW = suozaidi;
			setCity();
			break;
		case R.id.birthday_action://生日选择
			CURRENTVIEW = shengri;
			setAge();
			break;
		case R.id.sex_action://性别选择
			CURRENTVIEW = xingbie;
			setXingbie();
			break;
		case R.id.name_action://名字
			intent=new Intent(this,CommonExplainActivity.class);
			intent.putExtra(CommonExplainActivity.TITLE_NAME, "名字");  
			intent.putExtra(CommonExplainActivity.TEXT_CONUT, 10);  //字数限制  默认是200
			intent.putExtra(CommonExplainActivity.TEXT_CONTENT, mFinderView.textView(R.id.username).getText().toString());  //内容
			startActivityForResult(intent, EDIT_NAME);
			break;
		case R.id.head_action://头像
			WheelUtils.setPopeWindow(PersonInfoEditActivity.this, v,
					listpop);
			break;
		case R.id.paizhao: // 自拍照片
			listpop.dismiss();
			if (mLoginUserInfo.isDoctor())
				return;
			if (!SystemUtils.getScdExit()) {
				ToastUtil.showShort(this, R.string.chatting_sd_uninstall);
				return;
			}
			try {
				currentCameraFile = StorageUtils.createImageFile();
				Uri outUri = Uri.fromFile(currentCameraFile);
				intent = CropUtils.createPickForCameraIntent(outUri);
				startActivityForResult(intent,PHOTOHRAPH);
			} catch (Exception e) {
				e.printStackTrace();
				ToastUtil.showCreateFail();
			}
			break;

		case R.id.bendifenjian: // 相册选择图片
			listpop.dismiss();
			if (!SystemUtils.getScdExit()) {
				ToastUtil.showShort(PersonInfoEditActivity.this,
						"SD卡拔出,六一健康用户头像,语音,图片等功能不可用");
				return;
			}
			intent = CropUtils.createPickForFileIntent();
			startActivityForResult(intent,PHOTORESOULT);
			break;
		case R.id.quxiao:
			listpop.dismiss();
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
		case R.id.wheel_sure_age:
			if (CURRENTVIEW == shengri) {
				String[] str = (String[]) v.getTag();
				shengri.setText(str[0] + str[1] + str[2]);
			}
			break;
		case R.id.title_back:
			if (isEdit) {
				changePersonSubmit();
			}else{
				onBackPressed();
			}
			break;
		}
	}
	
	/**
	 * 对滚轮选中的类容进行文本匹配
	 */
	public void setText() {
		if (CURRENTVIEW.equals(suozaidi)) {
			suozaidi.setText(WheelUtils.getCurrent());
		} else if (CURRENTVIEW.equals(shengri)) {
			shengri.setText(WheelUtils.getCurrent());
		} else if (CURRENTVIEW.equals(zhiye)) {
			zhiye.setText(WheelUtils.getCurrent());
		} else if (CURRENTVIEW.equals(xingbie)) {
			xingbie.setText(WheelUtils.getCurrent());
		}
		locationCode = WheelUtils.getCode();
	}
	
	/**
	 * 设置年龄
	 */
	private void setAge() {
		if(agepop==null){
			agepop=WheelUtils.showThreeDateWheel(this, mainView, this);	
		}else{
			agepop.showAtLocation(mainView,Gravity.BOTTOM, 0, 0);
		}
		
	}

	/**
	 * 设置职业
	 */
	private void setZhiye() {
		String[] zhiye = getResources().getStringArray(R.array.zhiye);
		WheelUtils.setSingleWheel(this, zhiye, mainView, pop, wheelView, false);
	}


	/**
	 * 设置性别
	 */
	private void setXingbie() {
		String[] xingbie = getResources().getStringArray(R.array.sex);
		WheelUtils.setSingleWheel(this, xingbie, mainView, pop, wheelView,
				false);
	}

	/**
	 * 设置滚轮 城市数据
	 */
	private void setCity() {
		if (proList == null || cityMap == null) {
		} else {
			WheelUtils.setDoubleWheel(this, proList, cityMap, mainView, pop,
					wheelView);
		}
	}
	
	
	/**
	 * 图片裁剪
	 * @param path
	 */
	private void onHandlerCropImage(String path){
		if(!SystemUtils.getScdExit()){
			ToastUtil.showSDCardBusy();
			return;
		}
		try{
			headerFile = StorageUtils.createHeaderFile();
			Uri outUri = Uri.fromFile(new File(path));
			Uri saveUri = Uri.fromFile(headerFile);
			Intent intent = CropUtils.createHeaderCropIntent(this,outUri,saveUri,true);
			startActivityForResult(intent,3002);
		}catch(Exception e){
			ToastUtil.showCreateFail();
		}
	}
	
	/**
	 * 根据uri查询相册所对应的图片地址
	 * @param uri
	 * @return
	 */
	private String getImageUrlByAlbum(Uri uri) {
		String[] imageItems = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, imageItems, null, null, null);
		int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(index);
		return path;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case PHOTOHRAPH: // 拍照
			if(resultCode == Activity.RESULT_OK){
				String strFilePath = currentCameraFile.getAbsolutePath();
				onHandlerCropImage(strFilePath);
			}
			break;
		case PHOTORESOULT: // 相册
			if(resultCode == Activity.RESULT_OK){
				if (data != null) {
					Uri uri = data.getData();
					String scheme = uri.getScheme();
					String strFilePath = null;//图片地址
					// url类型content or file
					if ("content".equals(scheme)) {
						strFilePath = getImageUrlByAlbum(uri);
					} else {
						strFilePath = uri.getPath();
					}
					onHandlerCropImage(strFilePath);
				}
			}
			break;
		case 3002://裁剪后获取结果
			if(resultCode == Activity.RESULT_OK){
				if(resultCode == RESULT_OK){
					Bitmap bitmap = BitmapUtils.decodeBitmap(headerFile.getAbsolutePath(),
							CropUtils.HEADER_WIDTH,
							CropUtils.HEADER_HEIGTH);
					mHeadImage.setImageBitmap(bitmap);
				}else{
					if(headerFile != null)headerFile.deleteOnExit();
					headerFile = null;
				}
			}
			
			break;
		case WHAT_PERSON_XINGQU:
			if (data != null) {
				mXingQuList.clear();
				List<TagEntity> list=data.getParcelableArrayListExtra("attention");
				mXingQuList.addAll(list);
			}
			mAdapterXingQu.notifyDataSetChanged();
			if(mXingQuList.size()==0)findViewById(R.id.style_empty_view).setVisibility(View.VISIBLE);
			else findViewById(R.id.style_empty_view).setVisibility(View.GONE);
			break;
		case WHAT_PERSON_BODY:
			if (data != null) {
				mBodyList.clear();
				List<TagEntity> lists=data.getParcelableArrayListExtra("attention");
				mBodyList.addAll(lists);

			}
			mAdapterBody.notifyDataSetChanged();
			if(mBodyList.size()==0)findViewById(R.id.health_empty_view).setVisibility(View.VISIBLE);
			else findViewById(R.id.health_empty_view).setVisibility(View.GONE);
			break;
		case EDIT_DESC://描述
			if(data!=null && data.hasExtra("content")){
				mFinderView.setText(R.id.desc, data.getStringExtra("content"));
			}
			break;
		case EDIT_NAME://昵称
			if(data!=null && data.hasExtra("content")){
				mFinderView.setText(R.id.username, data.getStringExtra("content"));
			}
			break;
		}
	
	}

	/**
	 * 跳转
	 * @param is
	 */
	private void changesPages(boolean is){
//		Intent intent;
//		if(is){
//			intent = new Intent();
//			intent.setClass(PersonInfoEditActivity.this,
//					ChooseTagsActivity.class);
//			intent.putExtra("type", 2);
//			intent.putParcelableArrayListExtra("attentionInfo",
//					(ArrayList<? extends Parcelable>) mBodyList);
//			startActivityForResult(intent, WHAT_PERSON_BODY);
//		}else{
//			intent = new Intent();
//			intent.setClass(PersonInfoEditActivity.this,
//					ChooseTagsActivity.class);
//			intent.putParcelableArrayListExtra("attentionInfo",
//					(ArrayList<? extends Parcelable>) mXingQuList);
//			startActivityForResult(intent, WHAT_PERSON_XINGQU);
//			overridePendingTransition(R.anim.anim_enter,
//					R.anim.anim_exit);
//		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
//		switch (parent.getId()) {
//		case R.id.body_grid:
//			changesPages(true);
//			break;
//		case R.id.xingqu_grid:
//			changesPages(false);
//			break;
//		}
	}
	
	/*
	 * 处理back事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isEdit) {
				changePersonSubmit();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 主线程toast
	 * @param str
	 */
	private void toastOnUiThreadDialog(final String str) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ToastUtil.showShort(str);
			}
		});
	}
	
	/**
	 * 提交个人资料
	 */
	public void changePersonSubmit() {
		String all = "^[a-zA-Z0-9\u4e00-\u9fa5\n[s| ]*r]{1,20}$";// 数字、字母，不能大于20位
		Pattern pattern1 = Pattern.compile(all);//
		String mName = mFinderView.textView(R.id.username).getText().toString();
		if (HStringUtil.isEmpty(mName) || !pattern1.matcher(mName.trim()).matches()) {
			toastOnUiThreadDialog(getString(R.string.nechengchairoutside));
			return;
		}
		
		String desc = mFinderView.textView(R.id.desc).getText().toString();
		if (desc.length() > 100) {
			toastOnUiThreadDialog(getString(R.string.explanchairoutside));
			return;
		}
		
		JSONObject json = null;
		RequestParams requestParams = null;
		try {
			json = new JSONObject();
			json.put("customerNickname",mName.replaceAll("\n", ""));// 呢称
			json.put("personalNarrate", desc);// 一句话说明
			json.put("dwellingplace", suozaidi.getText().toString());// 所在地
			json.put("customerlocus", locationCode);// 所在地编码
			json.put("cultureLevel",mLoginUserInfo.getCultureLevel());// 学历
			json.put("metier", zhiye.getText().toString());// 职业
			json.put("marryFlag", mLoginUserInfo.getHunyin());// 婚姻
			String xingbie_sex = xingbie.getText().toString();

			if ("男".equals(xingbie_sex)) {
				json.put("customerSex", "1");
			} else if ("女".equals(xingbie_sex)) {
				json.put("customerSex", "2");
			} else {
				json.put("customerSex", "0");
			}
			json.put("customerId", mLoginUserInfo.getId());
			json.put("clientIconBackground", mLoginUserInfo.getNormalHeadIcon());
			json.put("bigIconBackground", mLoginUserInfo.getBigHeadIcon());
			String birth = shengri.getText().toString();
			if (birth != null && birth.length() == 11) {
				json.put("birthday", birth);
			}
			json.put("bodyList", DataParseUtil.TagsListEntityToJson(mBodyList));
			json.put("interestsList",
					DataParseUtil.TagsListEntityToJson(mXingQuList));

			requestParams = new RequestParams();
			requestParams.put("json", json.toString());
			if (headerFile != null) {
				requestParams.put("file", headerFile);
			} else {
				requestParams.putNullFile("file", new File(""));
			}

		} catch (Exception e) {
			ToastUtil.showBasicShortToast(getApplicationContext(), "内部解析出错");
			return;
		}

		HttpRestClient.doHttpUpdatePerson(requestParams,new JsonsfHttpResponseHandler(this) {
					@Override
					public void onSuccess(int statusCode,com.alibaba.fastjson.JSONObject response) {
						if(response.containsKey("error_code")){
							ToastUtil.showShort("修改失败,请稍后重试...");
						}else{
							resetCustomInfo(response.toString());
							setResult(RESULT_OK, getIntent());
							finish();
						}
						super.onSuccess(statusCode, response);
					}
				});
	}
	
	
	/**
	 * 修改个人资料成功,重新同步个人资料
	 * 
	 * @throws JSONException
	 */
	private void resetCustomInfo(String str) {
		JSONObject object;
		try {
			object = new JSONObject(str);
			SmartFoxClient foxClient = SmartFoxClient.getSmartFoxClient();
			mLoginUserInfo = DataParseUtil.JsonTocuStomUpdate(object,foxClient.getCustomerInfoEntity());
			if (mLoginUserInfo != null) {
				foxClient.setCustomerInfoEntity(mLoginUserInfo);
//				HTalkApplication.getAppData().updateCacheInfomation(mLoginUserInfo);
			}
			if (headerFile != null)// 表示没有修改头像
				ImageUtils.deleBitmap(headerFile.getAbsolutePath());
		} catch (JSONException e) {
				ToastUtil.showShort("内部解析错误 ,请刷新资料");
		}
	}
}
