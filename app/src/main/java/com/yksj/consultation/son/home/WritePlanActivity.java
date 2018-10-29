package com.yksj.consultation.son.home;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.squareup.okhttp.Request;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.photo.utils.AlbumActivity;
import com.yksj.healthtalk.photo.utils.GalleryActivity;
import com.yksj.healthtalk.utils.Bimp;
import com.yksj.healthtalk.utils.BitmapUtils;
import com.yksj.healthtalk.utils.DensityUtils;
import com.yksj.healthtalk.utils.ImageItem;
import com.yksj.healthtalk.utils.ToastUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WritePlanActivity extends BaseFragmentActivity implements View.OnClickListener {

    private Button addpic;//添加图片//从图库获取照片的标示
  //public static final int USERPIC_REQUEST_CODE_LOCAL = 101;
    private PopupWindow addPhotoPop;
    private File storageFile = null;
    private static final int TAKE_PICTURE = 0x000001;
    public static final String IMAGEKEY = WritePlanActivity.class.getSimpleName() + "image";//图片标志
    ArrayList<ImageItem> imagesList;//图片list类
    private LinearLayout images;//图片线性布局
    private JSONArray array = new JSONArray();//删除图片
    private ImageLoader mInstance;
    private EditText addreword;
    private String customer_id = SmartFoxClient.getLoginUserId();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_plan);
        initView();
    }
    private String plan_id = "";
    private String children_id="";
    private String CUSTOMER_REMARK = "";
    private void initView() {
        initTitle();
        Intent intent = getIntent();
        plan_id = intent.getStringExtra("plan_id");
        children_id = intent.getStringExtra("children_id");
        CUSTOMER_REMARK = intent.getStringExtra("CUSTOMER_REMARK");

        titleTextV.setText("填写关爱计划");
        titleRightBtn2.setVisibility(View.VISIBLE);
        titleRightBtn2.setText("完成");
        mInstance = ImageLoader.getInstance();
        titleLeftBtn.setOnClickListener(this);
        titleRightBtn2.setOnClickListener(this);
        addpic = (Button) findViewById(R.id.addpic);
        addpic.setOnClickListener(this);
        addreword = (EditText) findViewById(R.id.addreword);
        images = (LinearLayout) findViewById(R.id.item_images_plan);
        imagesList = new ArrayList<ImageItem>();
        Bimp.dataMap.put(IMAGEKEY, imagesList);
        Bimp.imgMaxs.put(IMAGEKEY, 12);

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right2:
                addPlan2();
                break;
            case R.id.addpic:
                showuploadPopWindow();
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
                intent = new Intent(WritePlanActivity.this, AlbumActivity.class);
                intent.putExtra("key",IMAGEKEY);
                startActivityForResult(intent, 100);
                break;

        }
    }

//    private void addPlan() {
//        String addRewordString= addreword.getText().toString();
//        Map<String,String> map=new HashMap<>();
//        map.put("children_id", children_id);
//        map.put("customer_id", customer_id);
//        map.put("customer_remark", CUSTOMER_REMARK);
//        map.put("op", "addRecord");
//        map.put("plan_id", plan_id);
//        map.put("reword_content", addRewordString);
//
//        //图片参数
//        RequestParams params = new RequestParams();
//        putFile(params);
//        map.put("photo",params.toString());
//
//        HttpRestClient.OKHttpAddCare(map, new OkHttpClientManager.ResultCallback<String>() {
//
//            @Override
//            public void onError(Request request, Exception e) {
//                ToastUtil.showShort("添加失败");
//            }
//            @Override
//            public void onResponse(String content) {
//                try {
//                    JSONObject obj = new JSONObject(content);
//                    if ("0".equals(obj.optString("code"))){
//                        ToastUtil.showShort("添加成功");
//                        finish();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        },this);
//    }

    private void addPlan2() {
        String addRewordString= addreword.getText().toString().trim();

        if (TextUtils.isEmpty(addRewordString)) {
            ToastUtil.showToastPanl("请填写您的关爱记录");
            return;
        }

        RequestParams params = new RequestParams();
        params.put("children_id", children_id);
        params.put("plan_id",plan_id );
        params.put("customer_id",customer_id);
        params.put("customer_remark", CUSTOMER_REMARK);//CUSTOMER_REMARK
        params.put("op", "addRecord");
        params.put("record_content", addRewordString);
        putFile(params);

        HttpRestClient.doHttpFillCareRecord(params,new AsyncHttpResponseHandler(this){

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
            }

            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))){
                        ToastUtil.showShort(obj.optString("message"));
                        setResult(RESULT_OK);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

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
     * 显示图片
     */
    private void showPhoto() {
        images.removeAllViews();
        if (imagesList.size() > 0) {//
            images.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        } else//将加号放在中间
            images.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0f));
        if (imagesList.size() == Bimp.imgMaxs.get(IMAGEKEY)) {
            findViewById(R.id.addpic).setVisibility(View.GONE);
        } else
            findViewById(R.id.addpic).setVisibility(View.VISIBLE);
        for (int i = 0; i < imagesList.size(); i++) {
            final int index = i;
            MessageImageView image = new MessageImageView(WritePlanActivity.this);
            image.setLayoutParams(new ViewGroup.LayoutParams(DensityUtils.dip2px(this, 78), DensityUtils.dip2px(this, 78)));
            image.setPadding(10, 0, 10, 0);
            if (imagesList.get(i).isNetPic) {
                mInstance.displayImage(imagesList.get(i).getThumbnailPath(), image.getImage());
            } else {
                image.setImageBitmap(imagesList.get(i).getBitmap());
            }
            image.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WritePlanActivity.this, GalleryActivity.class);
                    intent.putExtra("key", IMAGEKEY);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", index);
                    startActivityForResult(intent, 100);
                }
            });
            image.setDeleteListener(new View.OnClickListener() {//删除图片

                @Override
                public void onClick(View v) {
                    ImageItem imageItem = imagesList.get(index);
                    try {
                        JSONObject object = new JSONObject();
                        object.put("PICTUREID", imageItem.pidId + "");
                        object.put("SMALLPIC", imageItem.getThumbnailPath());
                        object.put("BIG_PICTURE", imageItem.getImagePath());
                        array.put(object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Bimp.dataMap.get(IMAGEKEY).remove(index);
                    showPhoto();
                }
            });
            images.addView(image);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        showPhoto();//展示图片

    }
    /**
     * 将文件放入参数
     *
     * @param params
     */
    private void putFile(RequestParams params) {
        int imageCount = imagesList.size();
        params.putNullFile("file", new File(""));
        //上传图片文件
        List<File> lists=new ArrayList<>();
        for (int i = 0; i < imageCount; i++) {
            int index = i;
            if (imagesList.get(i).isNetPic) {

            } else {
                try {
                    params.put(index + ".jpg", BitmapUtils.onGetDecodeFileByPath(WritePlanActivity.this, imagesList.get(i).getImagePath()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }

        }
     //   params.put("photo", lists);
    }
    //弹出popup
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

}
