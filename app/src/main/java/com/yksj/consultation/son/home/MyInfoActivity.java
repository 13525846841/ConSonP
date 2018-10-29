package com.yksj.consultation.son.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.EvelateAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.ImageGalleryActivity;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.DataParseUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 医生端我的信息界面,可编辑
 * Created by lmk on 15/10/13.
 */
public class MyInfoActivity extends BaseFragmentActivity implements View.OnClickListener{

    private TextView tvDuomeiId,tvName,tvHospital,tvAddr,tvOffice,tvDocTitle;
    private EditText editDuomeiId,editName,editHospital,editAddr,editOffice,editDocTitle;
    private ImageView imgRealPic,imgQualification;
    private ImageView indexSpecial,indexResume;
    private TextView tvSpecial,tvResume,tvCommentNum;
    private EvelateAdapter mAdapter;
    private boolean specialExpanded=false,resumeExpanded=false;

    private CustomerInfoEntity mCusInfoEctity;
    private ListView commentList;
    private ImageLoader mInstance;
    private String docId,docName;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_my_info);
        mInstance=ImageLoader.getInstance();
        docId=getIntent().getStringExtra("docId");
        docName=getIntent().getStringExtra("docName");
        initView();
        initData();
    }


    private void initView() {
        initTitle();
        titleTextV.setText(docName);
        titleLeftBtn.setOnClickListener(this);
//        titleRightBtn2.setVisibility(View.VISIBLE);
        tvOffice= (TextView) findViewById(R.id.my_info_office);
        tvName= (TextView) findViewById(R.id.my_info_name);
        tvHospital= (TextView) findViewById(R.id.my_info_hospital);
        tvAddr= (TextView) findViewById(R.id.my_info_addr);
        tvDocTitle= (TextView) findViewById(R.id.my_info_doc_title);
        tvDuomeiId= (TextView) findViewById(R.id.my_info_duomei);
        tvSpecial= (TextView) findViewById(R.id.my_info_specialty_content);
        tvResume= (TextView) findViewById(R.id.my_info_resume_content);
        indexSpecial= (ImageView) findViewById(R.id.my_info_specialty_index);
        indexResume= (ImageView) findViewById(R.id.my_info_resume_index);
        imgRealPic= (ImageView) findViewById(R.id.my_info_img_real);
        tvCommentNum= (TextView) findViewById(R.id.my_info_comment_num);
        if (!"0".equals(LoginServiceManeger.instance().getLoginEntity().getDoctorPosition()))
            findViewById(R.id.my_info_comment_layout).setVisibility(View.GONE);
        findViewById(R.id.my_info_comment_more).setOnClickListener(this);
        commentList= (ListView) findViewById(R.id.my_info_comment_list);
        imgQualification= (ImageView) findViewById(R.id.my_info_img_qualification);
        imgRealPic.setOnClickListener(this);
        imgQualification.setOnClickListener(this);
        indexResume.setOnClickListener(this);
        tvSpecial.setOnClickListener(this);
        indexSpecial.setOnClickListener(this);
        tvResume.setOnClickListener(this);
        mAdapter=new EvelateAdapter(MyInfoActivity.this);
        commentList.setAdapter(mAdapter);


    }

    private void initData() {
//DuoMeiHealth/ConsultationInfoSet?TYPE=findCustomerInfo&CUSTOMERID=
        List<BasicNameValuePair> pairs=new ArrayList<>();
        pairs.add(new BasicNameValuePair("TYPE","findCustomerInfo"));
        pairs.add(new BasicNameValuePair("CUSTOMERID", docId));
        HttpRestClient.doGetConsultationInfoSet(pairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj=new JSONObject(response);
                    if ("1".equals(obj.optString("code"))){
                        mCusInfoEctity=DataParseUtil.JsonToDocCustmerInfo(obj.getJSONObject("result"));
                        onBoundData();
                        if ("0".equals(LoginServiceManeger.instance().getLoginEntity().getDoctorPosition()))
                            loadComment();
                    }else {
                        ToastUtil.showShort(MyInfoActivity.this,obj.optString("message"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, this);

    }

    private void onBoundData() {
        tvOffice.setText(mCusInfoEctity.getOfficeName2());
        tvAddr.setText(mCusInfoEctity.getDoctorWorkaddress());
        tvDocTitle.setText(mCusInfoEctity.getDoctorTitleName());
        tvName.setText(mCusInfoEctity.getRealname());
        tvDuomeiId.setText(mCusInfoEctity.getUsername());
        tvHospital.setText(mCusInfoEctity.getHospital());
        tvSpecial.setText(mCusInfoEctity.getSpecial());
        tvResume.setText(mCusInfoEctity.getIntroduction());
        mInstance.displayImage(mCusInfoEctity.getDoctorClientPicture(), imgRealPic);
        mInstance.displayImage(mCusInfoEctity.getDoctorCertificate(), imgQualification);


    }


    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.title_back:
                onBackPressed();
                break;

            case R.id.my_info_specialty_index:
            case R.id.my_info_specialty_content:
                if(specialExpanded){
                    specialExpanded=false;
                    tvSpecial.setMaxLines(2);
                    indexSpecial.setImageResource(R.drawable.gengduos);
                }else{
                    specialExpanded=true;
                    tvSpecial.setMaxLines(100);
                    indexSpecial.setImageResource(R.drawable.shouqis);
                }
                break;
            case R.id.my_info_resume_content:
            case R.id.my_info_resume_index:
                if(resumeExpanded){
                    resumeExpanded=false;
                    tvResume.setMaxLines(2);
                    indexResume.setImageResource(R.drawable.gengduos);
                }else{
                    resumeExpanded=true;
                    tvResume.setMaxLines(100);
                    indexResume.setImageResource(R.drawable.shouqis);
                }
                break;
            case R.id.my_info_img_real://真实照片
                intent=new Intent(MyInfoActivity.this, ImageGalleryActivity.class);
                intent.putExtra(ImageGalleryActivity.URLS_KEY, new String[]{mCusInfoEctity.getDoctorClientPicture()});
                intent.putExtra(ImageGalleryActivity.TYPE_KEY, 0);//0,1单个,多个
                intent.putExtra("type", 1);
                startActivityForResult(intent, 3000);
                break;
            case R.id.my_info_img_qualification:
                intent=new Intent(MyInfoActivity.this, ImageGalleryActivity.class);
                intent.putExtra(ImageGalleryActivity.URLS_KEY, new String[]{mCusInfoEctity.getDoctorCertificate()});
                intent.putExtra(ImageGalleryActivity.TYPE_KEY, 0);//0,1单个,多个
                intent.putExtra("type", 1);
                startActivityForResult(intent, 3000);

                break;
//            case R.id.my_info_comment_more://更多评论
//                intent=new Intent(MyInfoActivity.this, SiteCommentListActivity.class);
//                startActivity(intent);
//
//                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==201&&resultCode==RESULT_OK){//成功
            initData();
        }
    }

    //加载评论列表
    private void loadComment() {
        //http://220.194.46.204/DuoMeiHealth/ConsultationInfoSet?TYPE=findCommentList&CUSTOMERID=3783&PAGESIZE=1&PAGENUM=20
        List<BasicNameValuePair> pairs=new ArrayList<>();
        pairs.add(new BasicNameValuePair("TYPE","findCommentList"));
        pairs.add(new BasicNameValuePair("PAGESIZE","1"));
        pairs.add(new BasicNameValuePair("PAGENUM","3"));//只加载3条数据
        pairs.add(new BasicNameValuePair("CUSTOMERID", LoginServiceManeger.instance().getLoginEntity().getId()));
        HttpRestClient.doGetConsultationInfoSet(pairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if ("1".equals(obj.optString("code"))) {
                        JSONObject result=obj.optJSONObject("result");
                        tvCommentNum.setText("("+result.optInt("commentNum")+")");
                        ArrayList<Map<String,String>> list=new ArrayList<Map<String, String>>();
                        JSONArray array=result.getJSONArray("commentList");
                        Map<String,String> map=null;
                        for (int i=0;i<array.length();i++){
                            JSONObject jsonObject=array.getJSONObject(i);
                            map=new HashMap<String, String>();
                            map.put("COMMENT_RESULT",jsonObject.optString("COMMENT_RESULT"));
                            map.put("PATIENT_ID",jsonObject.optString("PATIENT_ID")+"");
                            map.put("SERVICE_LEVEL",jsonObject.optString("SERVICE_LEVEL"));
                            map.put("REAL_NAME",jsonObject.optString("REAL_NAME"));
                            list.add(map);

                        }
                        if (list.size()>0){
                            mAdapter.add(list);
                        }

                    } else {
                        ToastUtil.showShort(MyInfoActivity.this, obj.optString("message"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, this);

    }


}
