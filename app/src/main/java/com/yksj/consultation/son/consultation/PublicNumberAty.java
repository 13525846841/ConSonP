package com.yksj.consultation.son.consultation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ImageView;

import com.dmsj.newask.http.LodingFragmentDialog;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.AppData;
import com.yksj.healthtalk.utils.ScreenShot;
import com.yksj.healthtalk.utils.ToastUtil;

import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by HEKL on 16/5/9.
 * Used for 六一健康二维码
 */

public class PublicNumberAty extends BaseFragmentActivity implements View.OnClickListener {

    private ImageLoader instance;
    private DisplayImageOptions mOptions;
    private ImageView imageQR;//二维码
    LodingFragmentDialog mDialog;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_public_number);
        initView();
    }

    private void initView() {
        initTitle();
        titleLeftBtn.setVisibility(View.VISIBLE);
        titleLeftBtn.setOnClickListener(this);
        titleTextV.setText("六一健康公众号");
        titleRightBtn2.setVisibility(View.VISIBLE);
        titleRightBtn2.setOnClickListener(this);
        titleRightBtn2.setText("保存");

        imageQR = (ImageView) findViewById(R.id.iv_ad);
        instance = ImageLoader.getInstance();
        mOptions = DefaultConfigurationFactory.PublicNumberDisplayImageOptions(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        instance.displayImage(AppData.KNOWLEDGEAD, imageQR, mOptions);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right2:
                DoubleBtnFragmentDialog.show(getSupportFragmentManager(), "提示", "保存到本地?", "取消", "确定" +
                        "", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
                    @Override
                    public void onDismiss(DialogFragment fragment) {

                    }

                    @Override
                    public void onClick(DialogFragment fragment, View v) {
                        mDialog = LodingFragmentDialog.showLodingDialog(getSupportFragmentManager(), getResources());
                        saveImageView();
                    }
                });
                break;
        }
    }

    /**
     * 保存二维码
     */
    private void saveImageView() {
        String filePath = Environment.getExternalStorageDirectory() + "/DCIM/"
                + "consultationQr.png";
        ScreenShot.shoot(this, new File(filePath));
        saveImageToGallery(this, getLoacalBitmap(filePath));
    }

    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存图片到相册
     *
     * @param context
     * @param bmp
     */
    public void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Screenshots");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
            ToastUtil.showShort("保存成功");
        } catch (FileNotFoundException e) {
            ToastUtil.showShort("保存失败");
            e.printStackTrace();
        }
        if (mDialog != null) mDialog.dismissAllowingStateLoss();
        // 最后通知图库更新
//		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(file.getPath()))));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
