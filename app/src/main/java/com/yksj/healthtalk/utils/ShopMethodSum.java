package com.yksj.healthtalk.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.cropimage.CropUtils;
import org.universalimageloader.utils.StorageUtils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

public class ShopMethodSum {
	
	private static String[] smallPic;
	private static String[] largePic;
	
	/**
	 * 各种馆 共用方法： 图片处理，处理处大图小图分别存到不同位置
	 * @param context
	 * @param largeBm
	 * @param smallBm
	 * @param mpic
	 * @return
	 */
	public static File[] bitmapAction(Context context,Bitmap largeBm,Bitmap smallBm,ImageView mpic) {
		if(largeBm!=null) {
			File fileTemp1 = null;
			try {
				fileTemp1 = StorageUtils.createImageFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long time = System.currentTimeMillis();
			File smallImageFile = new File(fileTemp1.toString().replace(".", "1"), time + "_small.jpg");
			File fileTemp2 = StorageUtils.createCameraFile();
			File largeImageFile = new File(fileTemp2.toString().replace(".", "1"), time + "_large.jpg");
			boolean largeFlag = StorageUtils.saveImageOnImagsDir(largeBm,
					largeImageFile);
			boolean smallFlag = StorageUtils.saveImageOnImagsDir(smallBm,
					smallImageFile);
			if (StorageUtils.isSDMounted() && largeFlag && smallFlag) {
				mpic.setImageBitmap(smallBm);
			} else {
				ToastUtil.showLong(context, "sdcard被占用！");
			}
			return new File[]{smallImageFile,largeImageFile};
		} else {
			return null;
		}
	}
	
	/**
	 * 通过路径剪切出两张图片
	 * @param bitmapPath
	 */
	public static File[] cameraBitmapAction(Context context, File bitmapPath,ImageView mImageView) {
			BitmapUtils.rotateBitmap(bitmapPath, 300, 300);
		Bitmap largeBm = BitmapUtils.decodeBitmap(bitmapPath.getAbsolutePath(),
				900, 900);
		Bitmap smallBm = BitmapUtils.decodeBitmap(bitmapPath.getAbsolutePath(),
				300, 300);
		// 图片路径
		smallPic = new String[] {smallBm.getWidth()+"",smallBm.getHeight()+""};
		if(largeBm != null)
			largePic = new String[] {largeBm.getWidth()+"",largeBm.getHeight()+""};
		return bitmapAction(context,largeBm, smallBm,mImageView);
	}
	
	/**
	 * @param context
	 * @param uri
	 * @return
	 */
	public static Bitmap getBitmapFromUri(Context context, Uri uri) {
		ContentResolver resolver = context.getContentResolver();
		Bitmap result = null;
		try {
			result = MediaStore.Images.Media.getBitmap(resolver, uri);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static File[] galleryBitmapAction(Context context, Uri uri,ImageView mImageView) {
		
		Bitmap largeBm = BitmapUtils.decodeBitmap(uri, context.getContentResolver(),
				900, 900);
		Bitmap smallBm = BitmapUtils.decodeBitmap(uri, context.getContentResolver(),
				300, 300);
		smallPic = new String[] {smallBm.getWidth()+"",smallBm.getHeight()+""};
		largePic = new String[] {largeBm.getWidth()+"",largeBm.getHeight()+""};
		return bitmapAction(context,largeBm, smallBm,mImageView);
	}
	
	/**
	 * 初始化照相照片存储地址
	 * 
	 * @return
	 */
	public static Intent initCameraStore(Context context, File storageFile) {
		storageFile = StorageUtils.createCameraFile();
		Uri uri = Uri.fromFile(storageFile);
		Intent intent = CropUtils.createPickForCameraIntent(uri);
		return intent;
	}
	
	/**
	 * 初始化从相册获取图片
	 * @return
	 */
	public static Intent getPhotoPickIntent() {
		Intent intent = CropUtils.createPickForFileIntent();
		return intent;
	}
	
	public static String[] getSmallPicData() {
		return smallPic;
	}
	
	public static String[] getLargePicData() {
		return largePic;
	}
	
}
