package com.yksj.healthtalk.utils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.universalimageloader.core.assist.ImageScaleType;
import org.universalimageloader.core.assist.ImageSize;
import org.universalimageloader.core.assist.ViewScaleType;
import org.universalimageloader.utils.StorageUtils;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.yksj.consultation.comm.SingleBtnFragmentDialog;

public class BitmapUtils {
	public static final int POTO_MAX_SILDE_SIZE = 960;// 照片图片最大边
	/**
	 * 计算缩放比例
	 * 
	 * @param options
	 * @param maxSideSize
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			float maxSideSize) {
		int inSampleSize = 1;
		final int height = options.outHeight;
		final int width = options.outWidth;
		if (width > height) {
			inSampleSize = Math.round((float) height / (float) maxSideSize);// 宽度大于高度以宽度为标准进行缩放
		} else {
			inSampleSize = Math.round((float) width / (float) maxSideSize);
		}
		return Math.max(1, inSampleSize);
	}

	/**
	 * 缩放bitmap
	 * 
	 * @param bytes
	 * @param maxSideSize
	 *            最大边长度
	 * @return
	 */
	public static Bitmap saveBitmpBytes(byte[] bytes, int maxSideSize) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			options.inPurgeable = true;
			options.inInputShareable = true;
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
			// 缩放比
			int inSampleSize = calculateInSampleSize(options, maxSideSize);
			options.inJustDecodeBounds = false;
			options.inSampleSize = inSampleSize;

			bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
					options);
			if (inSampleSize == 1) {
				if (maxSideSize < Math.max(options.outHeight, options.outWidth)) {
					return bitmap;
				}
			}

			// 再次缩放到制定宽度
			int outHeight = options.outHeight;
			int outWidth = options.outWidth;

			float slcae = 1;// 缩放倍数
			boolean isRotate = outWidth > outHeight ? true : false;// 当宽度大于高度进行图片旋转
			Matrix matrix = new Matrix();
			if (isRotate) {// 宽度大于高度
				slcae = maxSideSize / outWidth;
				matrix.postRotate(90);
			} else {
				slcae = maxSideSize / outHeight;
			}
			matrix.postScale(slcae, slcae);
			Bitmap bitmap3 = Bitmap.createBitmap(bitmap, 0, 0, outWidth,
					outHeight, matrix, true);
			if(bitmap3 != bitmap)bitmap.recycle();
			return bitmap3;
		} catch (OutOfMemoryError e) {
		}
		return bitmap;
	}

	/**
	 * 发送聊天图片
	 * 
	 * @param file
	 * @return
	 */
	public static Bitmap createChatBitmap(File file) {
		return scaleBitmap(file, POTO_MAX_SILDE_SIZE);
	}

	/**
	 * 缩放图片到指定大小
	 * 
	 * @param file
	 * @param maxSideSize
	 * @return
	 */
	public static Bitmap scaleBitmap(File file, float maxSideSize) {
		Bitmap bitmap3 = null;
		if (file.exists()) {
			FileInputStream fileInputStream = null;
			try {
				fileInputStream = new FileInputStream(file);
				BitmapFactory.Options options = new Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFileDescriptor(fileInputStream.getFD(),
						null, options);
				int outHeight = options.outHeight;
				int outWidth = options.outWidth;
				int inSampleSize = calculateInSampleSize(options, maxSideSize);
				options.inJustDecodeBounds = false;
				options.inSampleSize = inSampleSize;
				Bitmap bitmap = BitmapFactory.decodeFileDescriptor(
						fileInputStream.getFD(), null, options);
				if (inSampleSize == 1) {
					if (maxSideSize > Math.max(options.outHeight,
							options.outWidth)) {
						return bitmap;
					}
				}

				// 再次缩放到制定宽度
				outHeight = options.outHeight;
				outWidth = options.outWidth;
				float slcae = 1;// 缩放倍数
				boolean isRotate = outWidth > outHeight ? true : false;// 当宽度大于高度进行图片旋转
				Matrix matrix = new Matrix();
				if (isRotate) {// 宽度大于高度
					slcae = maxSideSize / outHeight;
					matrix.postRotate(90);
				} else {
					slcae = maxSideSize / outWidth;
				}
				matrix.postScale(slcae, slcae);
				bitmap3 = Bitmap.createBitmap(bitmap, 0, 0, outWidth,
						outHeight, matrix, true);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fileInputStream != null)
					try {
						fileInputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
		return bitmap3;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {
		Bitmap bitmap = null;
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		try{
			bitmap = BitmapFactory.decodeResource(res, resId, options);
		}catch(OutOfMemoryError e){
			e.printStackTrace();
		}
		return bitmap;
	}
	
	public static Bitmap decodeBitmap(FileDescriptor fileDescriptor,int reqWidth, int reqHeight){
		Bitmap bitmap = null;
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		try{
			bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
		}catch(OutOfMemoryError e){
		}
		return bitmap;
	}
	
	public static Bitmap decodeBitmap(String path,int reqWidth, int reqHeight){
		Bitmap bitmap = null;
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		try{
			bitmap = BitmapFactory.decodeFile(path, options);
			int progres = getBitmapProgress(new File(path));
			if(progres > 0){
				bitmap = rotateBitmap(bitmap,progres);
			}
		}catch(OutOfMemoryError e){
			e.printStackTrace();
		}
		return bitmap;
	}
	
	public static Bitmap decodeBitmap(Uri uri,ContentResolver contentResolver,int reqWidth, int reqHeight){
		if(uri == null)return null;
		ParcelFileDescriptor parcelFileDescriptor = null;
		Bitmap bitmap = null;
		try{
			if(uri.getScheme().equals("file")){
				parcelFileDescriptor = ParcelFileDescriptor.open(new File(uri.getPath()),ParcelFileDescriptor.MODE_READ_ONLY);
			}else{
				parcelFileDescriptor = contentResolver.openFileDescriptor(uri,"r");
			}
			if(parcelFileDescriptor == null)return null;
			bitmap = BitmapUtils.decodeBitmap(parcelFileDescriptor.getFileDescriptor(), reqWidth, reqHeight);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			parcelFileDescriptor = null;
		}finally{
			if(parcelFileDescriptor != null){
				try {
					parcelFileDescriptor.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return bitmap;
	}
	
	/**
	 * 获得背景图片
	 * @param resources
	 * @param id
	 * @return
	 */
	public static Bitmap getBackGroundBitmap(Resources resources,int id){
		Bitmap bitmap = null;
		try{
			InputStream inputStream =  resources.openRawResource(id);
			bitmap = BitmapFactory.decodeStream(inputStream);
		}catch(OutOfMemoryError e){
			e.printStackTrace();
		}
		return bitmap;
	}
	
	/**
	 *  旋转bitmap
	 * @param bitmap
	 * @param progress
	 * @return
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap,int progress){
		if(progress != 0 && bitmap != null){
			Matrix matrix = new Matrix();
			matrix.setRotate(progress, bitmap.getWidth()/2,bitmap.getHeight()/2);
			try{
				Bitmap bitmap1 = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
				if(bitmap1 != bitmap){
					bitmap.recycle();
					bitmap = bitmap1;
				}
			}catch(OutOfMemoryError e){
			}
		}
		return bitmap;
	}
	
	public static boolean rotateBitmap(File file,int width,int heigth) {
		Bitmap tempBitmap = decodeBitmap(file.getAbsolutePath(), width, heigth);
		int progres = getBitmapProgress(file);
		if(progres > 0){
			Bitmap mBitmap = rotateBitmap(tempBitmap,progres);
			if(tempBitmap != mBitmap){
				tempBitmap.recycle();
				tempBitmap = null;
				tempBitmap  = mBitmap;
			}
		}
		return StorageUtils.saveImageOnImagsDir(tempBitmap,file);
	}
	
	/**
	 * 获取照片的方向 
	 * @param file
	 * @return
	 */
	public static int getBitmapProgress(File file){
		try {
			ExifInterface exifInterface = new ExifInterface(file.getPath());
			int progress = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
			if(progress == ExifInterface.ORIENTATION_ROTATE_90){
				return 90;
			}else if(progress == ExifInterface.ORIENTATION_ROTATE_180){
				return 180;
			}else if(progress == exifInterface.ORIENTATION_ROTATE_270){
				return 270;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch(NullPointerException exception) {
			exception.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 从view 得到图片
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
	}
	
	
	/**
	 * 解码图片
	 * @param targetSize
	 * @param scaleType
	 * @param viewScaleType
	 * @return
	 * @throws Exception 
	 */
	public static Bitmap decode(File file,ImageSize targetSize, ImageScaleType scaleType, ViewScaleType viewScaleType) throws Exception{
		FileInputStream imageStream = new FileInputStream(file);
		Options decodeOptions = getBitmapOptionsForImageDecoding(new FileInputStream(file),targetSize, scaleType, viewScaleType);
		Bitmap subsampledBitmap;
		try {
			subsampledBitmap = BitmapFactory.decodeStream(imageStream, null, decodeOptions);
		} finally {
			imageStream.close();
		}
		if (subsampledBitmap == null) {
			return null;
		}
		// Scale to exact size if need
		if (scaleType == ImageScaleType.EXACTLY || scaleType == ImageScaleType.EXACTLY_STRETCHED) {
			subsampledBitmap = scaleImageExactly(subsampledBitmap, targetSize, scaleType, viewScaleType);
		}
		return subsampledBitmap;
	}
	
	public static Options getBitmapOptionsForImageDecoding(InputStream imageStream,ImageSize targetSize, ImageScaleType scaleType, ViewScaleType viewScaleType) throws IOException {
		Options decodeOptions = new Options();
		decodeOptions.inSampleSize = computeImageScale(imageStream,targetSize, scaleType, viewScaleType);
		decodeOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		return decodeOptions;
	}
	
	public static int computeImageScale(InputStream imageStream,ImageSize targetSize, ImageScaleType scaleType, ViewScaleType viewScaleType) throws IOException {
		int targetWidth = targetSize.getWidth();
		int targetHeight = targetSize.getHeight();
		// decode image size
		Options options = new Options();
		options.inJustDecodeBounds = true;
		try {
			BitmapFactory.decodeStream(imageStream, null, options);
		}catch(OutOfMemoryError e) {
		}finally {
			imageStream.close();
		}
		int scale = 1;
		int imageWidth = options.outWidth;
		int imageHeight = options.outHeight;
		int widthScale = imageWidth / targetWidth;
		int heightScale = imageHeight / targetHeight;

		if (viewScaleType == ViewScaleType.FIT_INSIDE) {
			if (scaleType == ImageScaleType.IN_SAMPLE_POWER_OF_2 || scaleType == ImageScaleType.POWER_OF_2) {
				while (imageWidth / 2 >= targetWidth || imageHeight / 2 >= targetHeight) { // ||
					imageWidth /= 2;
					imageHeight /= 2;
					scale *= 2;
				}
			} else {
				scale = Math.max(widthScale, heightScale); // max
			}
		} else { // ViewScaleType.CROP
			if (scaleType == ImageScaleType.IN_SAMPLE_POWER_OF_2 || scaleType == ImageScaleType.POWER_OF_2) {
				while (imageWidth / 2 >= targetWidth && imageHeight / 2 >= targetHeight) { // &&
					imageWidth /= 2;
					imageHeight /= 2;
					scale *= 2;
				}
			} else {
				scale = Math.min(widthScale, heightScale); // min
			}
		}
		if (scale < 1) {
			scale = 1;
		}
		return scale;
	}
	
	private static Bitmap scaleImageExactly(Bitmap subsampledBitmap, ImageSize targetSize, ImageScaleType scaleType, ViewScaleType viewScaleType) {
		float srcWidth = subsampledBitmap.getWidth();
		float srcHeight = subsampledBitmap.getHeight();

		float widthScale = srcWidth / targetSize.getWidth();
		float heightScale = srcHeight / targetSize.getHeight();

		int destWidth;
		int destHeight;
		if ((viewScaleType == ViewScaleType.FIT_INSIDE && widthScale >= heightScale) || (viewScaleType == ViewScaleType.CROP && widthScale < heightScale)) {
			destWidth = targetSize.getWidth();
			destHeight = (int) (srcHeight / widthScale);
		} else {
			destWidth = (int) (srcWidth / heightScale);
			destHeight = targetSize.getHeight();
		}

		Bitmap scaledBitmap;
		if ((scaleType == ImageScaleType.EXACTLY && destWidth < srcWidth && destHeight < srcHeight)
				|| (scaleType == ImageScaleType.EXACTLY_STRETCHED && destWidth != srcWidth && destHeight != srcHeight)) {
			scaledBitmap = Bitmap.createScaledBitmap(subsampledBitmap, destWidth, destHeight, true);
			if (scaledBitmap != subsampledBitmap) {
				subsampledBitmap.recycle();
			}
		} else {
			scaledBitmap = subsampledBitmap;
		}
		return scaledBitmap;
	}

	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 540, 960);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * 按路径压缩图片
	 */
	public static File onGetDecodeFileByPath(FragmentActivity activity,String path){
		File mBigFile=null;
		if(StorageUtils.isSDMounted()){//内存卡处于加载状态
			try {
				//创建文件
				mBigFile = StorageUtils.createPhotoFile();//根据名称创建文件
//				Bitmap bitmap = decodeBitmap(path,BitmapUtils.POTO_MAX_SILDE_SIZE,BitmapUtils.POTO_MAX_SILDE_SIZE);
				Bitmap bitmap=getSmallBitmap(path);
				if(bitmap == null){
					mBigFile = null;
					SingleBtnFragmentDialog.showDefault(activity.getSupportFragmentManager(), "保存图片失败");
					return mBigFile;
				}
				StorageUtils.saveImageOnImagsDir(bitmap,mBigFile);
				if(bitmap != null && !bitmap.isRecycled())bitmap.recycle();

			} catch (IOException e) {
				e.printStackTrace();
			}
			return mBigFile;

		}else{
			SingleBtnFragmentDialog.showDefault(activity.getSupportFragmentManager(),  "sdcard未加载");
			return mBigFile;
		}
	}
	
}
