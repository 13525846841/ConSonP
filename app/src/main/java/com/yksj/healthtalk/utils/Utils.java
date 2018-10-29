/*
 * 文 件 名:  Utils.java
 * 版    权:  ISOFTSTONE GROUP AND RELATED PARTIES. ALL RIGHTS RESERVED.
 * 描    述:  <文件主要内容描述>
 * 作    者:  ddliu
 * 创建时间:  2012-6-14 上午08:51:28
 */

package com.yksj.healthtalk.utils;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.yksj.consultation.son.friend.RGBLuminanceSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author ddliu
 * @version [1.0.0.0, 2012-6-14]
 */
public class Utils {
    public static final String PAINT_DEFAULT_SAVE_PATH = "/sdcard/Android2DCode/";

    public static final String SELECT_FILE_PATH = "filePath";

    /**
     * 用字符串生成二维码
     * 
     * @param str
     * @author ddliu
     * @return
     * @throws WriterException
     */
    public static Bitmap Create2DCode(String str) throws WriterException {
          Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");// 注意的地方，这里设置中文编码为UTF-8
        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败,hints 防止是乱码
        BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 500, 500,
                hints);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        // 二维矩阵转为一维像素数组,也就是一直横着排了
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }else{
                    pixels[y * width + x] = 0xffffffff;
                } 

            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 保存二维码图片
     * 
     * @param bm
     * @return
     * @author ddliu
     */
    public static boolean saveBitmap(Bitmap bm) {
        FileOutputStream fos = null;
        try {
            File froot = new File(PAINT_DEFAULT_SAVE_PATH);
            if (!froot.exists()) {
                froot.mkdirs();
            }
            // 获取要保存的名称
            File file = new File(PAINT_DEFAULT_SAVE_PATH + getPhotoFileName(".png"));
            fos = new FileOutputStream(file);
            // 保存涂鸦后的图片
            bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * 获取保存相片名称
     * 
     * @return
     * @author issuser
     */
    public static String getPhotoFileName(String endSuffix) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'Code'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + endSuffix;
    }

    /**
     * 从二维码图片获取文本
     * 
     * @param
     * @return
     * @author ddliu
     */
    public static Result getTextFromImage(Bitmap bitmap) {
        Result result = null;
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");

        try {
            RGBLuminanceSource source = new RGBLuminanceSource(bitmap);
            BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
            QRCodeReader reader2 = new QRCodeReader();
            result = reader2.decode(bitmap1, hints);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        } 
        return result;
    }

}
