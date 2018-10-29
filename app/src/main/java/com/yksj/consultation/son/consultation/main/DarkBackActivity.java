package com.yksj.consultation.son.consultation.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yksj.consultation.adapter.DrakBackImageAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.photo.utils.AlbumActivity;
import com.yksj.healthtalk.utils.Bimp;
import com.yksj.healthtalk.utils.BitmapUtils;
import com.yksj.healthtalk.utils.ImageItem;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.WheelUtils;

import org.cropimage.CropUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.ImageLoader;
import org.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 申请退款界面
 */
public class DarkBackActivity extends BaseFragmentActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView mOrderNumber;
    private TextView mContent;
    private TextView mName;
    private TextView professTitle;//职称
    private TextView office;
    private TextView price;
    private TextView reason;

    private GridView gv;
    private DrakBackImageAdapter mAdapter;
    private File storageFile = null;
    private EditText mEditText;
    private static final int TAKE_PICTURE = 0x000001;
    public static final String IMAGEKEY = DarkBackActivity.class.getSimpleName() + "image";//图片标志
    public ArrayList<ImageItem> imagesList;//图片list类
    private ImageLoader mInstance;
    private JSONArray array = new JSONArray();//删除图片
    PopupWindow mPopupWindow;
    private View wheelView;
    private String sReason;
    private String doctor_id;
    private String orderNumber;
    private String order_id;//订单号
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dark_back);
        initView();
    }

    private void initView() {
        initTitle();
        titleLeftBtn.setOnClickListener(this);
        titleTextV.setText("申请退款");

        mOrderNumber = (TextView) findViewById(R.id.mOrderNumber);
        mContent = (TextView) findViewById(R.id.mContent);
        mName = (TextView) findViewById(R.id.mName);
        professTitle = (TextView) findViewById(R.id.professTitle);
        office = (TextView) findViewById(R.id.office);
        price = (TextView) findViewById(R.id.price);
        reason = (TextView) findViewById(R.id.reason);
        mEditText = (EditText) findViewById(R.id.et_reason);

        orderNumber = getIntent().getStringExtra("order_number");
        mOrderNumber.setText("订单号:" + getIntent().getStringExtra("order_number"));

        String aa = getIntent().getStringExtra("title");
        mName.setText(getIntent().getStringExtra("name"));
        professTitle.setText(aa);

        office.setText(getIntent().getStringExtra("tvDocAccount"));
        price.setText(getIntent().getStringExtra("service_money") + "元");

        doctor_id = getIntent().getStringExtra("doctor_id");
        order_id = getIntent().getStringExtra("order_id");
        String serviceTypeId = getIntent().getStringExtra("service_type_id");

        if ("5".equals(serviceTypeId)){
            mContent.setText("订单内容:  图文咨询");
        }else if ("6".equals(serviceTypeId)){
            mContent.setText("订单内容:  电话咨询");
        }else if ("7".equals(serviceTypeId)){
            mContent.setText("订单内容:  包月咨询");
        }else if ("8".equals(serviceTypeId)){
            mContent.setText("订单内容:  视频咨询");
        }

        findViewById(R.id.rl_select_reason).setOnClickListener(this);
        gv = (GridView) findViewById(R.id.darkback_gv);
        mAdapter = new DrakBackImageAdapter(this);
        gv.setAdapter(mAdapter);
        gv.setOnItemClickListener(this);
        mInstance = ImageLoader.getInstance();
        imagesList = new ArrayList<ImageItem>();
        Bimp.dataMap.put(IMAGEKEY, imagesList);
        Bimp.imgMaxs.put(IMAGEKEY, 12);
        wheelView = getLayoutInflater().inflate(R.layout.wheel, null);
        mPopupWindow = new PopupWindow(wheelView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        findViewById(R.id.apply_for).setOnClickListener(this);


    }

    private List<Map<String, String>> contentList = null;
    private Map<String,String> map=new HashMap<>();
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;

            case R.id.rl_select_reason:
                seleteReason();
                break;
            case R.id.apply_for:
                submit();
                break;

            case R.id.cameraadd:
                if (addPhotoPop.isShowing()) {
                    addPhotoPop.dismiss();
                }
                photo();
                break;
            case R.id.cancel://取消
                if (addPhotoPop != null && addPhotoPop.isShowing()) {
                    addPhotoPop.dismiss();
                }
                break;
            case R.id.galleryadd://从相册获取
                if (addPhotoPop.isShowing()) {
                    addPhotoPop.dismiss();
                }
                intent = new Intent(DarkBackActivity.this, AlbumActivity.class);
                intent.putExtra("key",IMAGEKEY);
                startActivityForResult(intent, 100);
                break;
        }
    }
    private String customer_id = LoginServiceManeger.instance().getLoginUserId();
    /**
     * 提交申请
     */
    private void submit() {
        String reasonNumber = "";
        if (TextUtils.isEmpty(sReason)) {
            ToastUtil.showToastPanl("请选择退款原因");
            return;
        }else if (sReason.equals("拍错了")){
            reasonNumber = "1";
        }else if (sReason.equals("长时间未提供服务")){
            reasonNumber = "2";
        }else if (sReason.equals("协商一致退款")){
            reasonNumber = "3";
        }else if (sReason.equals("其他")){
            reasonNumber = "4";
        }

        String wReason = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(wReason)) {
            ToastUtil.showToastPanl("请填写退款说明");
            return;
        }

        RequestParams params = new RequestParams();

        params.put("order_id",order_id);//订单号
        params.put("doctor_id",doctor_id);
        params.put("customer_id",customer_id);
        params.put("reason", reasonNumber);
        params.put("reasonStr", wReason);
        putFile(params);

        HttpRestClient.OKHttpBackOrderUploadServlet(params,new AsyncHttpResponseHandler(this){

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
            }
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                try {
                    JSONObject obj = new JSONObject(content);
                    if ("0".equals(obj.optString("code"))) {
                        ToastUtil.showShort(obj.optString("message"));
                        finish();
                    }else if ("1".equals(obj.optString("code"))){
                        ToastUtil.showShort(obj.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 选择原因
     */
    private void seleteReason(){
        contentList = new ArrayList<Map<String, String>>();
        String[] content = {"拍错了", "长时间未提供服务", "协商一致退款", "其他"};
        List<String> list = new ArrayList<>();

        for (int i = 0; i < content.length; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", content[i]);
            contentList.add(map);

        }
        mPopupWindow = WheelUtils.showReason(DarkBackActivity.this,contentList, mName, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index1 = (int) v.getTag(R.id.listview_reason);
                Map<String, String> map1 = contentList.get(index1);
                sReason = map1.get("name");
                reason.setText(sReason);
                mPopupWindow.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showPhoto();//展示图片

    }


    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        switch (arg0) {
            case TAKE_PICTURE://选图片
                if (arg1 == Activity.RESULT_OK) {
                    if (storageFile != null) {
                        ImageItem takePhoto = new ImageItem();
                        takePhoto.setImagePath(storageFile.getAbsolutePath());
                        imagesList = Bimp.dataMap.get(IMAGEKEY);
                        imagesList.add(takePhoto);
                        showPhoto();
                    }
                }
                break;
        }
    }
    /**
     * 将文件放入参数
     *
     * @param params
     */
    private void putFile(RequestParams params) {
        int imageCount = imagesList.size();
        //上传图片文件
        // params.putNullFile("file", new File(""));
        params.putNullFile("photo", new File(""));
        List<File> lists=new ArrayList<>();
        for (int i = 0; i < imageCount; i++) {
            int index = i;
            if (imagesList.get(i).isNetPic) {

            } else {
                try {
                    params.put(index + ".jpg", BitmapUtils.onGetDecodeFileByPath(DarkBackActivity.this, imagesList.get(i).getImagePath()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }

        }
        //   params.put("photo", lists);
    }
    /**
     * 显示图片
     */
    private void showPhoto() {
        if (imagesList.size()!=0){
            if (imagesList.size() > 0) {
                mAdapter.onBoundData(imagesList);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == parent.getChildCount() - 1) {
            showuploadPopWindow();
        }
    }
    private PopupWindow addPhotoPop;
    private void showuploadPopWindow() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.interest_image_and_video_add_action, null);
        View mainView = inflater.inflate(R.layout.interest_content, null);
        if (addPhotoPop == null) {
            addPhotoPop = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            addPhotoPop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        WheelUtils.setPopeWindow(this, mainView, addPhotoPop);
        view.findViewById(R.id.cameraadd).setOnClickListener(this);
        view.findViewById(R.id.galleryadd).setOnClickListener(this);
        view.findViewById(R.id.videoadd).setVisibility(View.GONE);
        view.findViewById(R.id.cancel).setOnClickListener(this);
    }
    private void photo() {
        storageFile = null;
        if (StorageUtils.isSDMounted()) {
            try {
                storageFile = StorageUtils.createCameraFile();
                Uri uri = Uri.fromFile(storageFile);
                Intent intent = CropUtils.createPickForCameraIntent(uri);
                startActivityForResult(intent, TAKE_PICTURE);
            } catch (Exception e) {
                ToastUtil.showLong(this, R.string.msg_camera_bug);
            }
        } else {
            SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "sdcard未加载");
        }
    }
}
