package com.yksj.healthtalk.utils;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.view.View;

import com.yksj.consultation.son.R;

public class ImageUtils {
	private static final String TAG = "ImageUtils";
	private static final double FREE_SD_SPACE_NEEDED_TO_CACHE = 10;
	private static final double CACHE_SIZE = 30;
	private static final double mTimeDiff = 30;
	private static final double MB = 1024*1024;
	public static final String IMAGE_UNSPECIFIED = "image/*";
	public static final String ACTION_CROP = "com.android.camera.action.CROP"; //直接调用裁剪界面
	public static final String ACTION_GALLRY = Intent.ACTION_GET_CONTENT; //调用系统相册
	
	 /**
     * 计算sdcard上的剩余空间
     * @return
     */
    private static int freeSpaceOnSd(){
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory() .getPath());
        double sdFreeMB = ((double)stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
        return (int) sdFreeMB;
    } 
    
    /**
     * 修改文件的最后修改时间
     * @param path
     */
    public static void updateFileTime(String path) {
        File file = new File(path);       
        long newModifiedTime =System.currentTimeMillis();
        file.setLastModified(newModifiedTime);
    } 
    
    /**
     * 计算存储目录下的文件大小,文件大于所规定的空间
     * 那么删除40%最近没有被使用的文件
     * @param dirPath
     * @param filename
     */
    public static void removeCache(File dir) {
       // File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        int dirSize = 0;
        for (int i = 0; i < files.length;i++) {
            dirSize += files[i].length();
        }
        if (dirSize > CACHE_SIZE * MB ||FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            int removeFactor = (int) ((0.4 *files.length));
            Arrays.sort(files, new FileLastModifSort());
            for (int i = 0; i < removeFactor && i < files.length; i++) {
                 files[i].delete();             
            }
        }
    }
    
    
    /**
     * 删除过期文件
     * @param dirPath 
     * @param filename
     */
    public static void removeExpiredCache(String dirPath, String filename) {
        File file = new File(dirPath,filename);
        if (System.currentTimeMillis() -file.lastModified() > mTimeDiff) {
            file.delete();
        }
    }
    
    
    public static void deleBitmap(String path){
    	File file = new File(path);
    	if(file.exists())
    		file.delete();
    }
    
    /**
     * 根据文件的最后修改时间进行排序 *
     */
    private static class FileLastModifSort implements Comparator<File>{
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() >arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() ==arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }
    
    /**
     * 图片转成圆角
     * @param bitmap
     * @param pixels
     * @return
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels){                    
    	Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_4444);          
    	Canvas canvas = new Canvas(output);            
    	final int color = 0xff424242;          
    	final Paint paint = new Paint();          
    	final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());          
    	final RectF rectF = new RectF(rect);          
    	final float roundPx = pixels;            
    	paint.setAntiAlias(true);          
    	canvas.drawARGB(0, 0, 0, 0);          
    	paint.setColor(color);          
    	canvas.drawRoundRect(rectF, roundPx, roundPx, paint);            
    	paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));         
    	canvas.drawBitmap(bitmap, rect, rect, paint);            
    	return output;      
    }
    
    /**
     * 添加阴影
     * @param context
     * @param bitmap
     * @return
     */
    public static Drawable setShadow(Context context,Bitmap bitmap) {
		Bitmap bitmap1 = ((BitmapDrawable) context.getResources().getDrawable(
				R.drawable.shadow)).getBitmap();
		Bitmap bitmap2 = toRoundCorner(bitmap, 5);
		
		
		return new BitmapDrawable(bitmap2);
	}
	
	/**
	 * 封装请求Gallery的intent
	 * @return
	 */
	public static Intent getPhotoPickIntent(String action,Uri uri){
		Intent intent = new Intent(action, null);
		if(uri != null)
		   intent.setDataAndType(uri,IMAGE_UNSPECIFIED); 
		else
		intent.setType(IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1); 
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		return intent;
	}
	
	/**
	 * 
	* @Title: getPhotoPickAboutHeadIntent 
	* @Description: 剪切并设置头像
	* @param @param action
	* @param @param uri
	* @param @return    
	* @return Intent    
	* @throws
	 */
	public static Intent getPhotoPickAboutHeadIntent(String action,Uri uri){
		Intent intent = new Intent(action, null);
		if(uri != null)
		   intent.setDataAndType(uri,IMAGE_UNSPECIFIED); 
		else
		intent.setType(IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 80);
		intent.putExtra("outputY", 80);
		intent.putExtra("return-data", true);
		return intent;
	}
	
	
	
	/**
	 * 相册保存大图片的intent
	 * @param action
	 * @param uri
	 * @return
	 */
	public static Intent getAlbumPicturePickIntent(String action,Uri uri){
		Intent intent = new Intent(action);
		if(uri != null)
		   intent.setDataAndType(uri,IMAGE_UNSPECIFIED); 
		else{
			intent.setType(IMAGE_UNSPECIFIED);
		}
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		return intent;
	}

	
	/**
	 * 获取view的缓存bitmap
	 * @param v
	 * @return 
	 * @return
	 */
	public static Bitmap getDrawingBitmap(View v){
		boolean willNotCache = v.willNotCacheDrawing();
		v.setWillNotCacheDrawing(false);
		int color = v.getDrawingCacheBackgroundColor();
		v.setDrawingCacheBackgroundColor(0);
		if(color != 0)
			v.destroyDrawingCache();
		
		v.buildDrawingCache();
		Bitmap bmp = v.getDrawingCache();
		if(bmp == null)
			return null;
		Bitmap bitmap = Bitmap.createBitmap(bmp);
		
		v.destroyDrawingCache();
		v.setWillNotCacheDrawing(willNotCache);
		v.setDrawingCacheBackgroundColor(color);
		return bitmap;
	}
	
    
    public static final int FLAG_LEFT = -1;
    public static final int FLAG_RIGHT = 1;
    
    /**
     * 图片裁剪
     * @param bmp
     * @param flag
     * @return
     */
   public static Bitmap getBitmapHalf(Bitmap bmp, int flag)  
    {  
        final int width = bmp.getWidth();  
        final int height = bmp.getHeight();  
        int startWidth = 0; // 起始宽度位置   
        int endWidth = width / 2; // 结束宽度位置   
        Bitmap bitmap = Bitmap.createBitmap(endWidth, height, Bitmap.Config.RGB_565); // 创建新的图片，宽度只有原来的一半   
          
        switch (flag)  
        {  
        case FLAG_LEFT:  
            break;  
        case FLAG_RIGHT:  
            startWidth = endWidth;  
            endWidth = width;  
            break;  
        }  
          
        Rect r = new Rect(startWidth, 0, endWidth, height); // 图片要切的范围   
        Rect rect = new Rect(0, 0, width / 2, height); // 新图片的大小   
        Canvas canvas = new Canvas(bitmap);  
        canvas.drawBitmap(bmp, r, rect, null); // 切割图片   
        return bitmap;  
    }  

    
    
}
