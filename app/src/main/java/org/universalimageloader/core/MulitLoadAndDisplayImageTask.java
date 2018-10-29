package org.universalimageloader.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import org.universalimageloader.cache.disc.DiscCacheAware;
import org.universalimageloader.core.assist.FailReason;
import org.universalimageloader.core.assist.ImageLoadingListener;
import org.universalimageloader.core.assist.ImageScaleType;
import org.universalimageloader.core.assist.ImageSize;
import org.universalimageloader.core.assist.MemoryCacheUtil;
import org.universalimageloader.core.assist.ViewScaleType;
import org.universalimageloader.core.download.ImageDownloader;
import org.universalimageloader.utils.FileUtils;
import org.universalimageloader.utils.L;
import org.universalimageloader.utils.StorageUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;

/**
 * 
 * 加载显示多个图片
 * @author origin
 *
 */
public class MulitLoadAndDisplayImageTask implements Runnable{
	
	private static final int ATTEMPT_COUNT_TO_DECODE_BITMAP = 3;
	private static final int BUFFER_SIZE = 8 * 1024; // 8 Kb
	private static final int IMAGE_COLUMN_SIZE = 3;//显示多少列数
	
	private final ImageLoaderConfiguration configuration;
	private final ImageLoadingInfo imageLoadingInfo;
	private final Handler handler;

	// Helper references
	private final ImageDownloader downloader;
	private final boolean loggingEnabled;
	private final List<String> urls;
	private final String memoryCacheKey;
	private final ImageView imageView;
	private final ImageSize targetSize;
	private final DisplayImageOptions options;
	private final ImageLoadingListener listener;
	
	public MulitLoadAndDisplayImageTask(ImageLoaderConfiguration configuration, ImageLoadingInfo imageLoadingInfo, Handler handler) {
		this.configuration = configuration;
		this.imageLoadingInfo = imageLoadingInfo;
		this.handler = handler;

		downloader = configuration.downloader;
		loggingEnabled = configuration.loggingEnabled;
		urls = imageLoadingInfo.urls;
		memoryCacheKey = imageLoadingInfo.memoryCacheKey;
		imageView = imageLoadingInfo.imageView;
		targetSize = imageLoadingInfo.targetSize;
		options = imageLoadingInfo.options;
		listener = imageLoadingInfo.listener;
	}

	@Override
	public void run() {
		//原子操作方式，确保线程之间的同步
		AtomicBoolean pause = ImageLoader.getInstance().getPause();
		if(pause.get()){
			synchronized(pause){
				try{
					pause.wait();
				}catch(Exception e){
//					L.e(LOG_TASK_INTERRUPTED, memoryCacheKey);
					return;
				}
			}
		}
		//检查url 是否和imageview 匹配
		if (checkTaskIsNotActual()) return;
		
		//延迟加载
		if (options.isDelayBeforeLoading()) {
			try {
				Thread.sleep(options.getDelayBeforeLoading());
			} catch (InterruptedException e) {
				return;
			}
			if (checkTaskIsNotActual()) return;
		}
		ReentrantLock loadFromUriLock = imageLoadingInfo.loadFromUriLock;
		loadFromUriLock.lock();
		Bitmap bmp;
		try {
			//检查url是否匹配当前所对应的imageview对象
			if (checkTaskIsNotActual()) return;
			//检查内存中是否有对象
			bmp = ImageLoader.getInstance().getMemoryCache().get(memoryCacheKey);
			if (bmp == null) {
				bmp = tryLoadAllBitmap();
				if (bmp == null) return;
				if (checkTaskIsNotActual() || checkTaskIsInterrupted()) return;
				if (options.isCacheInMemory()) {
					configuration.memoryCache.put(memoryCacheKey, bmp);
				}
			}
		} finally {
			loadFromUriLock.unlock();
		}
		if (checkTaskIsNotActual() || checkTaskIsInterrupted()) return;
		DisplayBitmapTask displayBitmapTask = new DisplayBitmapTask(bmp, imageLoadingInfo);
		displayBitmapTask.setLoggingEnabled(loggingEnabled);
		handler.post(displayBitmapTask);
	}
	
	/**
	 * url 是否和对应的imageview相匹配
	 */
	private boolean checkTaskIsNotActual() {
		String currentCacheKey = ImageLoader.getInstance().getLoadingUriForView(imageView);
		// Check whether memory cache key (image URI) for current ImageView is actual. 
		// If ImageView is reused for another task then current task should be cancelled.
		boolean imageViewWasReused = !memoryCacheKey.equals(currentCacheKey);
		//如果当前的url不和imageview匹配的话,就退出加载
		if (imageViewWasReused) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					listener.onLoadingCancelled();
				}
			});
		}
		return imageViewWasReused;
	}
	
	/** 当前线程是否终止*/
	private boolean checkTaskIsInterrupted() {
		boolean interrupted = Thread.interrupted();
		return interrupted;
	}
	
	/**
	 * 
	 * 加载九宫格bitmap
	 * @return bitmap
	 * 
	 */
	private Bitmap tryLoadAllBitmap() {
		//缓存文件名称
		final String fileName = imageLoadingInfo.uri;
		DiscCacheAware discCache = configuration.discCache;
		File compimageFile = discCache.get(options.getDiscCacheDir(),fileName);
		List<String> urlList = urls;
		Bitmap bitmap = null;//九宫格合成图片
		
		try {
			//先从本地缓存目录中查找
			if (compimageFile.exists()) {
				Bitmap b = decodeImage(compimageFile.toURI());
				if (b != null) return b;
			}
			
			//没有查找到则生成九宫格图片
			final int size = urlList.size();
			Bitmap composeBitmap = Bitmap.createBitmap(targetSize.getWidth(),targetSize.getHeight(),options.getBitmapConfig());
			Canvas canvas = new Canvas(composeBitmap);
//			canvas.drawARGB(0, 0, 0, 0);
			canvas.drawColor(0xFFE2E2E2);
			ImageSize imageSize  = new ImageSize(targetSize.getWidth()/3, targetSize.getHeight()/3);
			
			int leftP = 0;//y坐标
			int topP = 0;//x坐标
			int index = 0;
			
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if(index >= size)break;
					final String string = urlList.get(index);
					Bitmap bitmap2 = tryLoadBitmap(string,imageSize);
					if(bitmap2 != null){
						canvas.drawBitmap(bitmap2,leftP, topP, null);
						leftP += imageSize.getWidth();
					}
					index++;
				}
				leftP = 0;
				topP += imageSize.getHeight();
			}
			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.restore();
			if(options.isCacheOnDisc())saveImageOnDisc(compimageFile, composeBitmap);
			bitmap = composeBitmap;
		} catch (IOException e) {
			L.e(e);
			fireImageLoadingFailedEvent(FailReason.IO_ERROR);
			/*if (imageFile.exists()) {
				imageFile.delete();
			}*/
		} catch (OutOfMemoryError e) {
			L.e(e);
			fireImageLoadingFailedEvent(FailReason.OUT_OF_MEMORY);
		} catch (Throwable e) {
			L.e(e);
			fireImageLoadingFailedEvent(FailReason.UNKNOWN);
		}
		return bitmap;
	}
	
	private Bitmap getDefaultBitmap(){
		Options options = new Options();
		options.inDensity = DisplayMetrics.DENSITY_DEFAULT;
		return BitmapFactory.decodeResource(HTalkApplication.getAppResources(),R.drawable.default_head_mankind);
	}
	
	private Bitmap tryLoadBitmap(String urlPath,ImageSize imageSize) throws IOException, URISyntaxException{
		Bitmap bitmap = null;
		final ImageLoader loader = ImageLoader.getInstance();
		//获取下载地址
		final String uri = loader.getDownPathUri(urlPath);
		final DiscCacheAware discCache = configuration.discCache;
		final File imageFilePath = discCache.get(options.getDiscCacheDir(),uri);
		bitmap = loader.getMemoryCache().get(MemoryCacheUtil.generateKey(uri, targetSize));
		//内存中获取
		if(bitmap != null && !bitmap.isRecycled()){
			Matrix matrix = new Matrix();
			matrix.postScale(imageSize.getWidth()/(float)bitmap.getWidth(), imageSize.getHeight()/((float)bitmap.getHeight()));
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			return bitmap;
		}else if(uri.startsWith(ImageDownloader.PROTOCOL_FILE_ASSETS)){//assets 目录加载
			bitmap = decodeImage(new URI(uri));
		}else if (imageFilePath.exists()) {//本地缓存加载
			bitmap = decodeImage(imageFilePath.toURI());
		}else{//web 远程加载
			URI imageUriForDecoding;
			if (options.isCacheOnDisc()) {
				if(!options.getDiscCacheDir().exists()){
					options.getDiscCacheDir().mkdirs();
					StorageUtils.createRootFileDir(options.getDiscCacheDir().getAbsolutePath());
				}
				//保存文件
				saveImageOnDisc(imageFilePath,uri);
				//放到缓存中
				discCache.put(uri, imageFilePath);
				//地址转换
				imageUriForDecoding = imageFilePath.toURI();
			} else {
				imageUriForDecoding = new URI(uri);
			}
			bitmap = decodeImage(imageUriForDecoding);
		}
		if(bitmap == null)bitmap = getDefaultBitmap();
		if(bitmap != null){
			Matrix matrix = new Matrix();
			matrix.postScale(imageSize.getWidth()/(float)bitmap.getWidth(), imageSize.getHeight()/((float)bitmap.getHeight()));
			Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			if(bitmap2 != bitmap){
				bitmap.recycle();
			}
			bitmap = bitmap2;
		}
		return bitmap;
	}
	
	private Bitmap decodeImage(URI imageUri) throws IOException {
		Bitmap bmp = null;
		if (configuration.handleOutOfMemory) {
			bmp = decodeWithOOMHandling(imageUri);
		} else {
			ImageDecoder decoder = new ImageDecoder(imageUri, downloader, options);
			decoder.setLoggingEnabled(loggingEnabled);
			ViewScaleType viewScaleType = ViewScaleType.fromImageView(imageView);
			bmp = decoder.decode(targetSize, options.getImageScaleType(), viewScaleType);
		}
		return bmp;
	}
	
	/**
	 * 
	 * oom异常获取
	 * @param imageUri
	 * @return
	 * @throws IOException
	 * 
	 */
	private Bitmap decodeWithOOMHandling(URI imageUri) throws IOException {
		Bitmap result = null;
		ImageDecoder decoder = new ImageDecoder(imageUri, downloader, options);
		decoder.setLoggingEnabled(loggingEnabled);
		for (int attempt = 1; attempt <= ATTEMPT_COUNT_TO_DECODE_BITMAP; attempt++) {
			try {
				ViewScaleType viewScaleType = ViewScaleType.fromImageView(imageView);
				result = decoder.decode(targetSize, options.getImageScaleType(), viewScaleType);
			} catch (OutOfMemoryError e) {
				L.e(e);
				switch (attempt) {
					case 1:
						System.gc();
						break;
					case 2:
						configuration.memoryCache.clear();
						System.gc();
						break;
					case 3:
						throw e;
				}
				// Wait some time while GC is working
				SystemClock.sleep(attempt * 1000);
				continue;
			}
			break;
		}
		return result;
	}
	
	private void saveImageOnDisc(File targetFile,Bitmap bitmap){
		if(bitmap == null)return;
		OutputStream os = null;
		try {
			os = new BufferedOutputStream(new FileOutputStream(targetFile), BUFFER_SIZE);
			bitmap.compress(configuration.imageCompressFormatForDiscCache, configuration.imageQualityForDiscCache, os);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(os != null)
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	private void saveImageOnDisc(File targetFile,String uri) throws IOException, URISyntaxException {
		int width = configuration.maxImageWidthForDiscCache;
		int height = configuration.maxImageHeightForDiscCache;
		if (width > 0 || height > 0) {
			// Download, decode, compress and save image
			ImageSize targetImageSize = new ImageSize(width, height);
			ImageDecoder decoder = new ImageDecoder(new URI(uri), downloader, options);
			decoder.setLoggingEnabled(loggingEnabled);
			Bitmap bmp = decoder.decode(targetImageSize, ImageScaleType.IN_SAMPLE_INT, ViewScaleType.FIT_INSIDE);
			
			OutputStream os = new BufferedOutputStream(new FileOutputStream(targetFile), BUFFER_SIZE);
			boolean compressedSuccessfully = bmp.compress(configuration.imageCompressFormatForDiscCache, configuration.imageQualityForDiscCache, os);
			if (compressedSuccessfully) {
				bmp.recycle();
				return;
			}
		}

		// If previous compression wasn't needed or failed
		// Download and save original image
		InputStream is = downloader.getStream(new URI(uri));
		
		//下载失败
		if(is == null) return;
		
		try {
			OutputStream os = new BufferedOutputStream(new FileOutputStream(targetFile), BUFFER_SIZE);
			try {
				FileUtils.copyStream(is, os);
			} finally {
				os.close();
			}
		} finally {
			if(is != null)is.close();
		}
	}

	private void fireImageLoadingFailedEvent(final FailReason failReason) {
		if (!Thread.interrupted()) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					listener.onLoadingFailed(failReason);
				}
			});
		}
	}
	
	
}
