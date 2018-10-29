package com.yksj.consultation.son.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.FamousDoctorAreaSelectAdapter;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.listener.OnRecyclerTypeClickListener;
import com.yksj.consultation.son.smallone.bean.Configs;
import com.yksj.healthtalk.entity.SelectChildCityEntity;
import com.yksj.healthtalk.entity.SelectProvinceEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.ToastUtil;

import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InstitutionJoinActivity extends Activity implements View.OnClickListener, OnRecyclerTypeClickListener {

    private PopupWindow popupWindow;
    private TextView tvInsType, tvCity;
    private LinearLayout lineInsType, lineSelectCity;
    private ImageView imgInsPre;
    private FamousDoctorAreaSelectAdapter cityAdapter;
    private FamousDoctorAreaSelectAdapter provinceAdapter;
    private List<SelectProvinceEntity.AreaBean> provinceList = new ArrayList<>();
    private List<SelectChildCityEntity.AreaBean> childCityList = new ArrayList<>();
    private String provinceAreaCode = "";//城市编码
    private PopupWindow cityPopupWindow;
    private String addressP;
    private String addressC;
    private String imgPath;
    private EditText edtInsName;
    private EditText edtInsIntroduction;
    private EditText edtInsAdzdress;
    private EditText edtInsPhone;
    private String class_type="1";
    private String Area_Code="";
    private FrameLayout tipPro;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0) {
                ToastUtil.onShow(InstitutionJoinActivity.this,"上传失败",1000);
            }else {
                ToastUtil.onShow(InstitutionJoinActivity.this,"上传成功",1000);
                finish();
            }
            tipPro.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institution_join);
        initView();
        cityPopupWindow();
    }

    private void cityPopupWindow() {
        cityPopupWindow = new PopupWindow(this);
        cityPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        cityPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        View inflate = View.inflate(this, R.layout.popupwindow_ins_join_city, null);
        //更多地区选择
        RecyclerView provinceRecycler = (RecyclerView) inflate.findViewById(R.id.provinceRecycler);
        provinceAdapter = new FamousDoctorAreaSelectAdapter(this, provinceList, 1);
        provinceAdapter.setOnRecyclerClick(this);
        provinceRecycler.setLayoutManager(new LinearLayoutManager(this));
        provinceRecycler.setAdapter(provinceAdapter);

        RecyclerView childCityRecycler = (RecyclerView) inflate.findViewById(R.id.childCityRecycler);
        cityAdapter = new FamousDoctorAreaSelectAdapter(this, childCityList, 2);
        cityAdapter.setOnRecyclerClick(this);
        childCityRecycler.setLayoutManager(new LinearLayoutManager(this));
        childCityRecycler.setAdapter(cityAdapter);
        //type  1是省  2是市
        loadDataProvince(1);
        cityPopupWindow.setContentView(inflate);
        cityPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        cityPopupWindow.setOutsideTouchable(true);
    }

    private void initView() {
        findViewById(R.id.title_back).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.title_lable);
        title.setText("入驻申请");
        edtInsName = (EditText) findViewById(R.id.edtInsName);
        edtInsIntroduction = (EditText) findViewById(R.id.edtInsIntroduction);
        edtInsAdzdress = (EditText) findViewById(R.id.edtInsAddress);
        edtInsPhone = (EditText) findViewById(R.id.edtInsPhone);
        imgInsPre = (ImageView) findViewById(R.id.imgInsPre);
        TextView tvInsUpload = (TextView) findViewById(R.id.tvInsUpload);
        tvInsType = (TextView) findViewById(R.id.tvInsType);
        tvCity = (TextView) findViewById(R.id.tvCity);
        tvInsUpload.setOnClickListener(this);
        Button btnInsJoinSubmit= (Button) findViewById(R.id.btnInsJoinSubmit);
        btnInsJoinSubmit.setOnClickListener(this);
        lineInsType = (LinearLayout) findViewById(R.id.lineInsType);
        lineInsType.setOnClickListener(this);
        lineSelectCity = (LinearLayout) findViewById(R.id.lineSelectCity);
        lineSelectCity.setOnClickListener(this);
        tipPro = (FrameLayout) findViewById(R.id.tipPro);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.lineSelectCity:
                cityPopupWindow.showAtLocation(lineSelectCity, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.tvInsUpload:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 666);

                break;
            case R.id.lineInsType:
                getInsTypePopup();
                break;
            case R.id.tvInsTijian:
                tvInsType.setText("体检中心");
                class_type="1";
                popupWindow.dismiss();
                break;
            case R.id.tvInsTuoz:
                tvInsType.setText("拓展中心");
                class_type="2";
                popupWindow.dismiss();
                break;
            case R.id.tvInsKangf:
                tvInsType.setText("康复中心");
                class_type="3";
                popupWindow.dismiss();
                break;
            case R.id.tvInsXingq:
                tvInsType.setText("兴趣中心");
                class_type="4";
                popupWindow.dismiss();
                break;
            case R.id.btnInsJoinSubmit:
                tipPro.setVisibility(View.VISIBLE);
                uploadData();
                break;
        }
    }

    private void getInsTypePopup() {
        popupWindow = new PopupWindow(this);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        View inflate = View.inflate(this, R.layout.popupwindow_instituion_join_instype, null);
        TextView tvInsTijian = (TextView) inflate.findViewById(R.id.tvInsTijian);
        tvInsTijian.setOnClickListener(this);
        TextView tvInsTuoz = (TextView) inflate.findViewById(R.id.tvInsTuoz);
        tvInsTuoz.setOnClickListener(this);
        TextView tvInsKangf = (TextView) inflate.findViewById(R.id.tvInsKangf);
        tvInsKangf.setOnClickListener(this);
        TextView tvInsXingq = (TextView) inflate.findViewById(R.id.tvInsXingq);
        tvInsXingq.setOnClickListener(this);
        popupWindow.setContentView(inflate);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        popupWindow.showAsDropDown(lineInsType);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 666) {
            if (data != null) {
                Uri imageUri = data.getData();
                Glide.with(this).load(imageUri).into(imgInsPre);
                if ("file".equalsIgnoreCase(imageUri.getScheme())) {
                    imgPath =getRealPathFromUri_Byfile(this, imageUri);
                } else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
                    imgPath =getRealPathFromUri_AboveApi19(this, imageUri);
                }
            }
        }
    }


    //针对图片URI格式为Uri:: file:///storage/emulated/0/DCIM/Camera/IMG_20170613_132837.jpg
    private static String getRealPathFromUri_Byfile(Context context, Uri uri){
        String uri2Str = uri.toString();
        String filePath = uri2Str.substring(uri2Str.indexOf(":") + 3);
        return filePath;}
    private static String getRealPathFromUri_AboveApi19(Context context, Uri uri) {
        String filePath = null;
        String wholeID = null;

        wholeID = DocumentsContract.getDocumentId(uri);

        // 使用':'分割
        String id = wholeID.split(":")[1];

        String[] projection = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media._ID + "=?";
        String[] selectionArgs = {id};

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                selection, selectionArgs, null);
        int columnIndex = cursor.getColumnIndex(projection[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }


    private void loadDataProvince(final int type) {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("op", "findArea"));
        if (type == 2) {
            pairs.add(new BasicNameValuePair("area_code", provinceAreaCode));
        }

        HttpRestClient.doGetProvinceSee(pairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                if (type == 1) {
                    SelectProvinceEntity selectProvinceEntity = gson.fromJson(response, SelectProvinceEntity.class);
                    List<SelectProvinceEntity.AreaBean> area = selectProvinceEntity.getArea();
                    provinceList.addAll(area);
                    provinceAdapter.notifyDataSetChanged();
                } else {
                    SelectChildCityEntity childCityEntity = gson.fromJson(response, SelectChildCityEntity.class);
                    List<SelectChildCityEntity.AreaBean> area = childCityEntity.getArea();
                    childCityList.addAll(area);
                    cityAdapter.notifyDataSetChanged();
                }
            }
        }, this);
    }

    @Override
    public void setOnCliCkListener(View view, int position, int type) {
        if (type == 1) {
            childCityList.clear();
            provinceAreaCode = provinceList.get(position).getAREA_CODE();
            addressP="";
            addressP = provinceList.get(position).getAREA_NAME();
//                areaCode= provinceList.get(position).getAREA_CODE();
            loadDataProvince(2);
            provinceAdapter.setSelectProvince(position);
            provinceAdapter.notifyDataSetChanged();
        } else {
            SelectChildCityEntity.AreaBean areaBean = childCityList.get(position);
            Area_Code=childCityList.get(position).getAREA_CODE();
            addressC = addressP + childCityList.get(position).getAREA_NAME();
            tvCity.setText(addressC);
            cityPopupWindow.dismiss();
        }
    }

    //Unit_Name--机构名称
//    Unit_pic1--机构背景图
//    Unit_specialty_Desc--机构介绍
//    class_type--机构类型
//    Area_Code--所属地区编码
//    Unit_Address_Desc--详细地址
//    Unit_Tel1--联系电话
    private void uploadData(){
        String[] split = imgPath.split("/");
        String s = split[split.length - 1];
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), new File(imgPath));
        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("Unit_pic1", s, requestBody)
                .addFormDataPart("Unit_Name", edtInsName.getText().toString())
                .addFormDataPart("Unit_Tel1", edtInsPhone.getText().toString())
                .addFormDataPart("Unit_specialty_Desc", edtInsIntroduction.getText().toString())
                .addFormDataPart("class_type", class_type)
                .addFormDataPart("Area_Code", Area_Code)
                .addFormDataPart("Unit_Address_Desc", addressC+edtInsAdzdress.getText().toString())
                .addFormDataPart("address", addressC)
                .addFormDataPart("customerId", LoginServiceManeger.instance().getLoginUserId())
                .build();

//        Log.i("kkk", "uploadData: "+edtInsName.getText().toString()+"----"
//                +edtInsIntroduction.getText().toString()+"----"
//                +class_type+"----"
//                +Area_Code+"----"
//                +addressC+edtInsAdzdress.getText().toString()+"----"
//                +edtInsPhone.getText().toString()+"----"
//                +LoginServiceManeger.instance().getLoginUserId()+""
//        );

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(Configs.WEB_IP + "/DuoMeiHealth/CreateInstitutions")
                .post(body)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("lll", "onFailure: "+e.getMessage());
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("lll", "onResponse: ");
                mHandler.sendEmptyMessage(1);
            }
        });
    }
}
