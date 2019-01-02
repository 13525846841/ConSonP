package com.yksj.consultation.son.home;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.yksj.consultation.comm.ZoomImgeDialogFragment;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ZxingUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hekl on 18/5/3.
 */

public class QRcodeDoctorActivity extends FragmentActivity implements View.OnClickListener, PlatformActionListener {
    private int doctor_Id=-1;
    private ImageView imQRcode;
    private String doctorName;
    private Uri uri;
    private JSONObject jsonObject;
    private String qrCodeUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_qr_code);
        initView();
    }

    private void initView() {
        String imgHeader = getIntent().getStringExtra("imgHeader");
        String hisInfo = getIntent().getStringExtra("hisInfo");
        String specially = getIntent().getStringExtra("specially");
        doctorName = getIntent().getStringExtra("doctorName");
        doctor_Id = getIntent().getIntExtra("doctor_id",-1);
        qrCodeUrl = getIntent().getStringExtra("qrCodeUrl");
        CircleImageView doctorHeader= (CircleImageView) findViewById(R.id.doctorHeader);
        TextView doctorDepartment= (TextView) findViewById(R.id.doctorDepartment);
        doctorDepartment.setText(specially);
        TextView doctorDepartmentBottom= (TextView) findViewById(R.id.doctorDepartmentBottom);
        doctorDepartmentBottom.setText(specially);
        TextView infoTv= (TextView) findViewById(R.id.infoTv);
        infoTv.setText(hisInfo);
        imQRcode = (ImageView) findViewById(R.id.imageQR);
        imQRcode.setOnClickListener(this);
        findViewById(R.id.QRShare).setOnClickListener(this);
        if (imgHeader!=null){
            Glide.with(this).load(ImageLoader.getInstance().getDownPathUri(imgHeader)).placeholder(R.drawable.default_head_doctor).dontAnimate()
                    .error(R.drawable.default_head_doctor).into(doctorHeader);
        }else {
            doctorHeader.setVisibility(View.GONE);
            findViewById(R.id.diWorks).setVisibility(View.INVISIBLE);
            findViewById(R.id.works).setVisibility(View.INVISIBLE);

        }
        uri = Uri.parse("android.resource://" + HTalkApplication.getApplication().getPackageName() + "/"
                + R.drawable.launcher_logo);
        findViewById(R.id.title_back).setOnClickListener(this);
        TextView title= (TextView) findViewById(R.id.title_lable);
        title.setText("二维码");
        Glide.with(this).load(qrCodeUrl).error(R.drawable.waterfall_default).placeholder(R.drawable.waterfall_default).dontAnimate().into(imQRcode);
//        if (doctor_Id!=-1){
//            loadQR();
//        }
//        if (qrCodeUrl!=null){
//            int v = (int) ((QRcodeDoctorActivity.this.getResources().getDisplayMetrics().density) * 150 + 0.5f);
//            imQRcode.setImageBitmap(ZxingUtils.createQRImage(qrCodeUrl,v,v));
//        }
    }

    private void loadQR() {
            Map<String, String> map = new HashMap<>();
            map.put("doctor_id", doctor_Id+"");//124951  doctor_id
            HttpRestClient.doGetBarCode(map, new HResultCallback<String>(this) {
                @Override
                public void onResponse(String response) {
                    super.onResponse(response);
                    if (!HStringUtil.isEmpty(response)) {

                        Log.i("tyty", "onResponse:           "+response);
                        try {
                            jsonObject = new JSONObject(response);
                            if ("0".equals(jsonObject.optString("code"))) {
                                String url = HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW+ LoginServiceManeger.instance().getLoginEntity().getNormalHeadIcon();
//                                Picasso.with(QRcodeDoctorActivity.this).load(url).placeholder(R.drawable.default_head_doctor).into(header);
//                                infoTextView.setText(doctor_name+"\n\n"+doctor_titleName+"\n\n"+ doctor_hospital+"   |   "+doctor_office);
//                                mAccount.setText(jsonObject.optString("customer_account"));
                                Log.i("ggg",jsonObject.optString("path"));
//                                Glide.with(QRcodeDoctorActivity.this).load(jsonObject.optString("path")).error(R.drawable.waterfall_default).placeholder(R.drawable.waterfall_default).into(imQRcode);
//                                Picasso.with(QRcodeDoctorActivity.this).invalidate(jsonObject.optString("path"));
                                int v = (int) ((QRcodeDoctorActivity.this.getResources().getDisplayMetrics().density) * 150 + 0.5f);
                                imQRcode.setImageBitmap(ZxingUtils.createQRImage(jsonObject.optString("path"),v,v));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, this);
        }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.title_back:finish();break;
           case R.id.imageQR:
//               if (qrCodeUrl!=null){
//                   ZoomImgeDialogFragment.showQr(qrCodeUrl,getSupportFragmentManager());
//               }else {
//                   ZoomImgeDialogFragment.showQr(jsonObject.optString("path"),getSupportFragmentManager());
//               }
               ZoomImgeDialogFragment.showQr(qrCodeUrl,getSupportFragmentManager());

               break;
           case R.id.QRShare :
               ShareWeiChat();
               break;
       }
    }

    /**
     * 分享到微信
     */
    private void ShareWeiChat() {
        //2、设置分享内容
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
        sp.setTitle(doctorName+"的医生工作室"); //分享标题
        sp.setText(getString(R.string.string_share_content)); //分享文本
        sp.setImageData(getBitmapFromUri(uri));
        sp.setUrl("http://wx.61120.net/DuoMeiHealth/DO.action?Doctor_ID="+ doctor_Id);   //网友点进链接后，可以看到分享的详情
        //3、非常重要：获取平台对象
        Platform wechatms = ShareSDK.getPlatform(Wechat.NAME);
        wechatms.setPlatformActionListener(this); // 设置分享事件回调
        // 执行分享
        wechatms.share(sp);
    }
    /**
     * 获取图片Bitmap
     * @param uri
     * @return
     */
    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(Platform platform, int i) {

    }
}
