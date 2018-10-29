package com.yksj.healthtalk.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 选择图片工具类
 * @author 第三方
 *
 */
public class Bimp{
	//选择图片存放的Map，每行都有一个key
	public static HashMap<String, ArrayList<ImageItem>> dataMap=new HashMap<String, ArrayList<ImageItem>>();
	public static HashMap<String, ArrayList<ImageItem>> thumbnailDataMap=new HashMap<String, ArrayList<ImageItem>>();
	//每行对应最大照片张数
	public static HashMap<String, Integer> imgMaxs=new HashMap<String, Integer>();
	
	public static ArrayList<ImageItem> tempSelectBitmap=new ArrayList<ImageItem>();

	public static Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 400)&& (options.outHeight >> i <= 400)) {
				in = new BufferedInputStream(new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}
	
	public static Bitmap fitSizeImg(String path) {
        if(path == null || path.length()<1 ) return null;
        File file = new File(path);
        Bitmap resizeBmp = null;
        BitmapFactory.Options opts = new   BitmapFactory.Options();
        // 数字越大读出的图片占用的heap越小 不然总是溢出
        if (file.length() < 20480) {       // 0-20k
         opts.inSampleSize = 1;
        } else if (file.length() < 51200) { // 20-50k
         opts.inSampleSize = 2;
        } else if (file.length() < 307200) { // 50-300k
         opts.inSampleSize = 4;
        } else if (file.length() < 819200) { // 300-800k
         opts.inSampleSize = 6;
        } else if (file.length() < 1048576) { // 800-1024k
         opts.inSampleSize = 8;
        } else {
         opts.inSampleSize = 10;
        }
        resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);
        return resizeBmp;
      }

	public static Bitmap compressBitmap(Bitmap image,int maxkb) {
		//L.showlog(压缩图片);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
//      Log.i(test,原始大小 + baos.toByteArray().length);
		while (baos.toByteArray().length / 1024 > maxkb&&options>10) { // 循环判断如果压缩后图片是否大于(maxkb)50kb,大于继续压缩
//          Log.i(test,压缩一次!);
			baos.reset();// 重置baos即清空baos
			options -= 10;// 每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
		}
//      Log.i(test,压缩后大小 + baos.toByteArray().length);
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 位图保存
	 * @param name
	 * @param bitmap
     */
	public static void saveBitmap(String name,Bitmap bitmap) {
		try {
			File file = new File("/sdcard/XIAOYI/");
			if (!file.exists())
				file.mkdir();

			file = new File("/sdcard/XIAOYI/"+name);
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
