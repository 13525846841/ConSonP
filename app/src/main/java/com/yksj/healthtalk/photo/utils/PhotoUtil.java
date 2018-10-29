package com.yksj.healthtalk.photo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 拍照和从相册中上传头像的功能的工具类
 * by chen
 */
public class PhotoUtil {

    public static final int SELECT_PIC = 2001;
    public static final int SELECT_PIC_KITKAT = 2002;
    private static final int ICON_SIZE = 96;
    public static final int CAMERA_WITH_DATAA = 3023;
    public static final String FOLDER_IMAGES_THUMBNAILS="/Yun/Images/Thumbnails/";//聊天收到的缩略图
    public static final String FOLDER_IMAGES_ORIGINAL="/Yun/Images/Original/";//聊天收到的原图

    /**
     * 拍照选择
     * @param activity
     */
    public static String doTakePhoto(Activity activity){
        String state = Environment.getExternalStorageState(); // 判断是否存在sd卡
        if (state.equals(Environment.MEDIA_MOUNTED)) { // 调用系统的照相机
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String fileName = getFileName(activity);
//            String filePath = activity.getApplicationContext().getFilesDir().getAbsolutePath()+"/Yun/Images/"+fileName;
            String filePath = Environment.getExternalStorageDirectory()+"/Yun/Images/"+fileName;
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePath)));
            intent.putExtra("image_path",filePath);
            activity.startActivityForResult(intent, CAMERA_WITH_DATAA);
            return filePath;
        } else {
            Toast.makeText(activity, "请检查手机是否有sd卡", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    /**
     * 拍照产生临时文件名
     * @return
     */
    private static String getFileName(Activity activity) {
        String saveDir = Environment.getExternalStorageDirectory()+ "/Yun/Images";
        File dir = new File(saveDir);
        if (!dir.exists()) {
            dir.mkdirs(); // 创建文件夹
        }
        return getRandomFileName();
    }

    /**
     * 生成一个随机的文件名
     * @return
     */
    public static String getRandomFileName() {
        String rel="";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        rel = formatter.format(curDate);
        rel = rel+new Random().nextInt(1000);
        return rel;
    }

    /**
     * 从相册选择图片(为了解决4.4不能返回path的问题)
     * @param activity
     */
    public static void selectImgFromGallery(Activity activity){
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/jpeg");
        if(android.os.Build.VERSION.SDK_INT >= 19){
            activity.startActivityForResult(intent, SELECT_PIC_KITKAT);
        }else{
            activity.startActivityForResult(intent, SELECT_PIC);
        }
    }

    /**
     * 读取本地图片
     * @param filePath
     * @return
     */
    public static Bitmap readBitmapFromPath(Activity context,String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        options.inJustDecodeBounds = false;
        int be = calculateInSampleSize(context,outWidth,outHeight);
        options.inSampleSize = be;
        options.inPurgeable =true;
        options.inInputShareable = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        try {
            return BitmapFactory.decodeFile(filePath, options);
        }catch (OutOfMemoryError e){
            System.gc();
            try {
                options.inSampleSize = be+1;
                return BitmapFactory.decodeFile(filePath, options);

            }catch (OutOfMemoryError e2){
                return null;
            }
        }
    }

    /**
     * 计算图片缩放比例
     * @param outWidth
     * @param outHeight
     * @return
     */
    public static int calculateInSampleSize(Activity context,int outWidth,int outHeight){
        int screenWidth = context.getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = context.getWindowManager().getDefaultDisplay().getHeight();
        int be;
        if(outWidth>screenWidth || outHeight>1.5*screenHeight){
            int heightRatio = Math.round(((float) outHeight) / ((float) 1.5 * screenHeight));
            int widthRatio = Math.round(((float) outWidth) / ((float) screenWidth));
            int sample = heightRatio > widthRatio ? heightRatio : widthRatio;
            if (sample < 3)
                be = sample;
            else if (sample < 6.5)
                be = 4;
            else if (sample < 8)
                be = 8;
            else
                be = sample;
        }else{
            be = 1;
        }
        if(be <= 0){
            be = 1;
        }
        return be;
    }


    /**
     * 图片质量压缩，单张图片大小不超过定值
     * @param image
     * @return
     */
    public static  byte[] compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 90, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;
        while ( baos.toByteArray().length / 1024>150) {	//循环判断如果压缩后图片是否大于150kb,大于继续压缩
            if(options<0){
                break;
            }
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        Log.i("image_size", baos.toByteArray().length + "");
        return baos.toByteArray();
    }

    public static Bitmap compressImage(Activity activity,Bitmap image, int screenWidth, int screenHight) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 60, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = screenHight;
        float ww = screenWidth;
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据高度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        try {
            bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        }catch (OutOfMemoryError e){
            try {
                newOpts.inPreferredConfig = Bitmap.Config.RGB_565;//降低图片从ARGB888到RGB565
                bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
            }catch (OutOfMemoryError o){
                System.gc();
                bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
            }

        }
        int bitWidth = bitmap.getWidth();
        if(!(screenWidth - bitWidth < 20)){
            bitmap = zoomImgToFitScreen(bitmap,activity);
        }
        return bitmap;

    }

    /**
     * 调整图片宽高，默认以屏幕宽为比例，若放大后高度超过屏幕，则以屏幕高度为比例
     * @param bm 原始图片
     * @param context Activity
     * @return 缩放后的新图片
     */
    public static Bitmap zoomImgToFitScreen(Bitmap bm,Activity context){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        int screenWidth = context.getWindowManager().getDefaultDisplay().getWidth() - 10;
        int screenHeightt = context.getWindowManager().getDefaultDisplay().getHeight() - 10;
        int newWidth = screenWidth;
        int newHeight = (screenWidth*height)/width;
        if(newHeight > screenHeightt){
            newHeight = screenHeightt;
            newWidth = (newHeight*width)/height;
        }
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    /**
     * 获取jpeg的旋转信息
     * @param filepath
     * @return
     */
    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            //LogUtil.i("test", "cannot read exif");
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            Log.i("ORIENTATION", orientation + "");
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }
        return degree;
    }

    /**
     * 旋转图片
     * @param bitmap 原图
     * @param angle2 旋转角度
     * @return
     */
    public static Bitmap rotaingImageView(Bitmap bitmap, int angle2) {
        Matrix matrix = new Matrix();
        // 旋转图片 动作
        matrix.postRotate(angle2);
        System.out.println("angle2=" + angle2);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        bitmap = BitmapFactory.decodeStream(bitmap2IS(bitmap), null, options);
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        bitmap.recycle();
        return resizedBitmap;
    }

    /**
     * Bitmap转换成InputStream方法
     *
     * @param bm
     * @return
     */
    public static InputStream bitmap2IS(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
        return sbs;
    }

    /**
     * 保存图片到sd卡
     * @param bm
     * @param mContext
     * @param fileDir "/Yun/Images/Thumbnails"--收到的聊天缩略图  "/Yun/Images/Original"--收到的聊天原图  "/Yun/Images/"--其他
     * @return
     */
    public static String saveBitmaptoSdCard(Bitmap bm,Activity mContext,String fileDir,String url) {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File sdCardDir = Environment.getExternalStorageDirectory();
            File yaoYanDir = new File(sdCardDir.getPath()+fileDir);
            if(!yaoYanDir.exists()){
                yaoYanDir.mkdirs();
            }
            String filename="";
            if(FOLDER_IMAGES_ORIGINAL.equals(fileDir)){
                filename=url.split("/")[url.split("/").length-1];
            }else{
                filename = getRandomFileName();
            }
            FileOutputStream fos;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            byte[] buffer = baos.toByteArray();
            File f = new File(yaoYanDir,filename);
            if (!f.exists()) {
                try {
                    f.createNewFile();
                    fos = new FileOutputStream(f);
                    fos.write(buffer,0,buffer.length);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return filename;
        }else{
            Toast.makeText(mContext, "sd卡不存在", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    /**
     * 保存图片到sd卡
     * @param bm
     * @return
     */
    public static String saveBitmaptoSdCard(Bitmap bm,String fileDir) {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File sdCardDir = Environment.getExternalStorageDirectory();
            File yaoYanDir = new File(sdCardDir.getPath()+fileDir);
            if(!yaoYanDir.exists()){
                yaoYanDir.mkdirs();
            }
            String filename = getRandomFileName();
            FileOutputStream fos;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            byte[] buffer = baos.toByteArray();
            File f = new File(yaoYanDir,filename);
            if (!f.exists()) {
                try {
                    f.createNewFile();
                    fos = new FileOutputStream(f);
                    fos.write(buffer,0,buffer.length);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return filename;
        }else{
//            Toast.makeText(mContext,"sd卡不存在",Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    /**
     * 通过uri获取图片的绝对路径
     * @param context
     * @param uri
     * @return
     */
    public static String getFilePathByContentResolver(Context context, Uri uri) {
        if (null == uri) {
            return null;
        }
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);
        String filePath  = null;
        if (null == c) {
            throw new IllegalArgumentException(
                    "Query on " + uri + " returns null result.");
        }
        try {
            if ((c.getCount() != 1) || !c.moveToFirst()) {
            } else {
                filePath = c.getString(
                        c.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
            }
        } finally {
            c.close();
        }
        return filePath;
    }
}
