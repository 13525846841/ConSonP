package com.yksj.consultation.son.dossier;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.ImageGalleryActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.chatting.ChatActivity;
import com.yksj.consultation.son.salon.SalonSelectPaymentOptionActivity;
import com.yksj.healthtalk.entity.GroupInfoEntity;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.DensityUtils;
import com.yksj.healthtalk.utils.SalonHttpUtil;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.ViewFinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 病历详情
 * Created by zheng on 2015/7/13.
 */
public class AtyDossierDetails extends BaseFragmentActivity implements View.OnClickListener,
        SalonSelectPaymentOptionActivity.OnBuyTicketHandlerListener {
    private String recordId="50";//会诊id,病历id
    private ArrayList<JSONObject> datas;//网络加载过来的数据
    private LinearLayout contentLayout;//

    private ImageLoader mImageLoader;
    private TextView tvPatientName,tvPatientSex,tvPatientBirthday,tvPatientAge,
            tvPatientZhiye,tvPatientPhone,tvPatientCode;
    private LinearLayout caseImgLayout;//展示图片的布局
    private int btnState=0;//0 去关注,1  取消关注  2 删除

    private Button declareBtn;//发布意见按钮
    private RelativeLayout keyWordsRelativeLayout;//关键词整个布局
    private LinearLayout keysLayout;//关键词
    private Button btnEditKeys;//编辑关键词
    private JSONArray keysArray;//关键词Array

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_dossier_details);
        recordId=getIntent().getStringExtra("ID");
        initView();
        initData();
    }

    private void initView() {
        initTitle();
        if(getIntent().hasExtra("TITLE")){
            titleTextV.setText(getIntent().getStringExtra("TITLE"));
        }
        titleLeftBtn.setOnClickListener(this);
        titleRightBtn2.setVisibility(View.VISIBLE);
        titleRightBtn2.setOnClickListener(this);
        declareBtn = (Button) findViewById(R.id.declare);
        declareBtn.setOnClickListener(this);

        mImageLoader=ImageLoader.getInstance();
        contentLayout=(LinearLayout) findViewById(R.id.dynamic_case_template_linearlayout);

        tvPatientName= (TextView) findViewById(R.id.create_case_input_name);
        tvPatientSex= (TextView) findViewById(R.id.create_case_input_sex);
        tvPatientZhiye= (TextView) findViewById(R.id.create_case_input_zhiye);
        tvPatientAge= (TextView) findViewById(R.id.create_case_input_age);
        tvPatientPhone= (TextView) findViewById(R.id.create_case_input_address);
        tvPatientBirthday= (TextView) findViewById(R.id.create_case_input_birthday);
        tvPatientCode= (TextView) findViewById(R.id.create_case_input_card_num);
        caseImgLayout= (LinearLayout) findViewById(R.id.dynamic_case_img_layout);

        keyWordsRelativeLayout= (RelativeLayout) findViewById(R.id.case_keywords_layout_id);
        keysLayout= (LinearLayout) findViewById(R.id.create_case_keywords);
        btnEditKeys= (Button) findViewById(R.id.create_case_keywords_add);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMessageNum();
    }

    //加载聊天条数
    private void loadMessageNum() {
        //TempletClassMRTServlet?OPTION=13&RECORDID=124616
        RequestParams params=new RequestParams();
        params.put("RECORDID",recordId);
        params.put("OPTION", "13");
        HttpRestClient.doHttpConsultionCaseTemplateShare(params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                if (content.contains("errormessage")) {
                    ToastUtil.showBasicErrorShortToast(AtyDossierDetails.this);
                } else {
                    declareBtn.setText("进入讨论区" + "(" + content + ")条");
                }

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right2:
                if(getIntent().hasExtra("ID")){
                    initcollect();
                }
                break;
            case R.id.declare://发表意见
                doChatGroup(recordId, "1", getIntent().getStringExtra("TITLE"));
                break;
        }
    }

    /**
     * chat
     *
     * @param id   groupid
     * @param isBl ;//是否是病历：（1是病历，0是会诊）
     * @param name groupname
     */
    private void doChatGroup(String id, String isBl, String name) {
        GroupInfoEntity entity = new GroupInfoEntity();
        entity.setId(id);
        entity.setIsBL(isBl);
        entity.setName(name);
        SalonHttpUtil.onItemClick(this, AtyDossierDetails.this, getSupportFragmentManager(), entity, false);
    }

    //关注
    private void initcollect() {

        RequestParams params=new RequestParams();
        params.put("MEDICAL_RECORD_ID",getIntent().getStringExtra("ID"));//
        switch (btnState){
            case 0:
                params.put("TYPE","focusMedicalRecord");
                params.put("CUSTOMERID",SmartFoxClient.getLoginUserId());
                break;
            case 1:
                params.put("TYPE","cancelFocusMedicalRecord");
                params.put("CUSTOMERID",SmartFoxClient.getLoginUserId());
                break;
            case 2://
                params.put("TYPE","removeMedicalRecord");

                break;
        }
        HttpRestClient.doHttpFINDMYPatients(params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                try {
                    JSONObject object = new JSONObject(content);
                    if (content.contains("error_code")) {
                        ToastUtil.showToastPanl(object.optString("error_message"));
                    }else{
                        ToastUtil.showToastPanl(object.optString("message"));
                        switch (btnState){
                            case 0://关注成功
                                btnState=1;
                                titleRightBtn2.setText("取消关注");
                                break;
                            case 1://取消关注成功
                                titleRightBtn2.setText("关注");
                                btnState=0;
                                break;
                            case 2://
                                AtyDossierDetails.this.finish();//销毁
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //初始化数据
    private void initData() {
        //TempletClassMRTServlet?OPTION=8&RECORDID=28&CUSTID=2400
        RequestParams params=new RequestParams();
        params.put("OPTION","8");
        params.put("RECORDID",recordId);
        params.put("CUSTID", SmartFoxClient.getLoginUserId());
        HttpRestClient.doHttpConsultionCaseTemplateShare(params, new AsyncHttpResponseHandler(AtyDossierDetails.this) {

            @Override
            public void onSuccess(String content) {
                JSONObject object;
                datas = new ArrayList<JSONObject>();
                try {
                    object = new JSONObject(content);
                    JSONArray array = object.getJSONArray("ITEMCONTENT");
                    for (int i = 0; i < array.length(); i++) {
                        datas.add(array.getJSONObject(i));
                    }
//                    int value=object.optInt("VALUE");
//                    declareBtn.setText("进入讨论区"+"("+value+")条");
                    if(object.optInt("CREATOR")==0){
                        if(object.optInt("ATTENTION")==0){//关注
                            titleRightBtn2.setText("关注");
                            btnState=0;
                        }else{//要取消关注
                            titleRightBtn2.setText("取消关注");
                            btnState=1;
                        }
                    }else{//等于1可以删除
                        titleRightBtn2.setText("取消共享");
                        btnState=2;
                    }
                    tvPatientName.setText(object.optString("NAME"));
                    if("W".equals(object.optString("SEX"))){
                        tvPatientSex.setText("女");
                    }else if("M".equals(object.optString("SEX"))){
                        tvPatientSex.setText("男");
                    }
                    if (object.has("PHONE")) {//包含这个键
                        tvPatientPhone.setText(object.optString("PHONE").trim());
                    } else {//不包含
                        findViewById(R.id.create_case_tv_phone_left).setVisibility(View.GONE);
                        tvPatientPhone.setVisibility(View.GONE);
                    }
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
                    keysArray=object.getJSONArray("FLAGCONTENT");
                    for(int i=0;i<keysArray.length();i++){
                        JSONObject keyObject=keysArray.getJSONObject(i);
                        Button btn=new Button(AtyDossierDetails.this);
                        btn.setText(keyObject.optString("NAME"));
                        btn.setTextColor(getResources().getColor(R.color.gray_text));
                        btn.setGravity(Gravity.CENTER);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(6, 0, 6, 0);
                        btn.setLayoutParams(layoutParams);
                        btn.setBackgroundResource(R.drawable.btn_topic_label_bg);

                        keysLayout.addView(btn);
                    }
                    if(keysArray==null||keysArray.length()==0){
                        keyWordsRelativeLayout.setVisibility(View.GONE);
                    }

                    onBoundImgData(object.optString("RECORDFILE"));
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
                ImageView imageview=new ImageView(AtyDossierDetails.this);
                imageview.setLayoutParams(new ViewGroup.LayoutParams(DensityUtils.dip2px(AtyDossierDetails.this, 78), DensityUtils.dip2px(AtyDossierDetails.this, 78)));
                imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mImageLoader.displayImage(imgObject.optString("ICON"), imageview);//加载小图片
                imageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AtyDossierDetails.this,ImageGalleryActivity.class);
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
            View itemView= LayoutInflater.from(AtyDossierDetails.this).inflate( R.layout.apt_consultion_case_item_show, null,true);
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
            }
            contentLayout.addView(itemView);
        }
    }

    public void onTicketHandler(String state, GroupInfoEntity entity) {
        if ("0".equals(state)) {
        } else if ("-1".equals(state)) {
            ToastUtil.showShort("服务器出错");
        } else {
            Intent intent1 = new Intent();
            intent1.putExtra(ChatActivity.KEY_PARAME, entity);
            intent1.setClass(this, ChatActivity.class);
            startActivity(intent1);
        }
    }


}
