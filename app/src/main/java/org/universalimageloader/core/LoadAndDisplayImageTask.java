package org.universalimageloader.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import org.universalimageloader.cache.disc.DiscCacheAware;
import org.universalimageloader.core.assist.FailReason;
import org.universalimageloader.core.assist.ImageLoadingListener;
import org.universalimageloader.core.assist.ImageScaleType;
import org.universalimageloader.core.assist.ImageSize;
import org.universalimageloader.core.assist.ViewScaleType;
import org.universalimageloader.core.download.ImageDownloader;
import org.universalimageloader.utils.FileUtils;
import org.universalimageloader.utils.L;
import org.universalimageloader.utils.StorageUtils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.ImageView;


/**
 * Presents load'n'display image task. Used to load image from Internet or file system, decode it to {@link Bitmap}, and
 * display it in {@link ImageView} through {@link DisplayBitmapTask}.
 * 
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @see ImageLoaderConfiguration
 * @see ImageLoadingInfo
 */
final class LoadAndDisplayImageTask implements Runnable {

	private static final String LOG_WAITING_FOR_RESUME = "ImageLoader is paused. Waiting...  [%s]";
	private static final String LOG_RESUME_AFTER_PAUSE = ".. Resume loading [%s]";
	private static final String LOG_DELAY_BEFORE_LOADING = "Delay %d ms before loading...  [%s]";
	private static final String LOG_START_DISPLAY_IMAGE_TASK = "Start display image task [%s]";
	private static final String LOG_WAITING_FOR_IMAGE_LOADED = "Image already is loading. Waiting... [%s]";
	private static final String LOG_GET_IMAGE_FROM_MEMORY_CACHE_AFTER_WAITING = "...Get cached bitmap from memory after waiting. [%s]";
	private static final String LOG_LOAD_IMAGE_FROM_INTERNET = "Load image from Internet [%s]";
	private static final String LOG_LOAD_IMAGE_FROM_DISC_CACHE = "Load image from disc cache [%s]";
	private static final String LOG_CACHE_IMAGE_IN_MEMORY = "Cache image in memory [%s]";
	private static final String LOG_CACHE_IMAGE_ON_DISC = "Cache image on disc [%s]";
	private static final String LOG_TASK_CANCELLED = "ImageView is reused for another image. Task is cancelled. [%s]";
	private static final String LOG_TASK_INTERRUPTED = "Task was interrupted [%s]";

	private static final int ATTEMPT_COUNT_TO_DECODE_BITMAP = 3;
	private static final int BUFFER_SIZE = 8 * 1024; // 8 Kb

	private final ImageLoaderConfiguration configuration;
	private final ImageLoadingInfo imageLoadingInfo;
	private final Handler handler;

	// Helper references
	private final ImageDownloader downloader;
	private final boolean loggingEnabled;
	private final String uri;
	private final String memoryCacheKey;
	private final ImageView imageView;
	private final ImageSize targetSize;
	private final DisplayImageOptions options;
	private final ImageLoadingListener listener;

	public LoadAndDisplayImageTask(ImageLoaderConfiguration configuration, ImageLoadingInfo imageLoadingInfo, Handler handler) {
		this.configuration = configuration;
		this.imageLoadingInfo = imageLoadingInfo;
		this.handler = handler;

		downloader = configuration.downloader;
		loggingEnabled = configuration.loggingEnabled;
		uri = imageLoadingInfo.uri;
		memoryCacheKey = imageLoadingInfo.memoryCacheKey;
		imageView = imageLoadingInfo.imageView;
		targetSize = imageLoadingInfo.targetSize;
		options = imageLoadingInfo.options;
		listener = imageLoadingInfo.listener;
	}

	@Override
	public void run() {
		AtomicBoolean pause = ImageLoader.getInstance().getPause();
		if (pause.get()) {
			synchronized (pause) {
				if (loggingEnabled) L.i(LOG_WAITING_FOR_RESUME, memoryCacheKey);
				try {
					pause.wait();
				} catch (InterruptedException e) {
					L.e(LOG_TASK_INTERRUPTED, memoryCacheKey);
					return;
				}
				if (loggingEnabled) L.i(LOG_RESUME_AFTER_PAUSE, memoryCacheKey);
			}
		}
		
		if (checkTaskIsNotActual()) return;

		if (options.isDelayBeforeLoading()) {
			if (loggingEnabled) L.i(LOG_DELAY_BEFORE_LOADING, options.getDelayBeforeLoading(), memoryCacheKey);
			try {
				Thread.sleep(options.getDelayBeforeLoading());
			} catch (InterruptedException e) {
				L.e(LOG_TASK_INTERRUPTED, memoryCacheKey);
				return;
			}

			if (checkTaskIsNotActual()) return;
		}

		ReentrantLock loadFromUriLock = imageLoadingInfo.loadFromUriLock;
		if (loggingEnabled) {
			L.i(LOG_START_DISPLAY_IMAGE_TASK, memoryCacheKey);
			if (loadFromUriLock.isLocked()) {
				L.i(LOG_WAITING_FOR_IMAGE_LOADED, memoryCacheKey);
			}
		}

		loadFromUriLock.lock();
		Bitmap bmp;
		try {
			
			if (checkTaskIsNotActual()) return;

			bmp = ImageLoader.getInstance().getMemoryCache().get(memoryCacheKey);
			if (bmp == null) {
				bmp = tryLoadBitmap();
				if (bmp == null) return;
				if (checkTaskIsNotActual() || checkTaskIsInterrupted()) return;
				if (options.isCacheInMemory()) {
					if (loggingEnabled) L.i(LOG_CACHE_IMAGE_IN_MEMORY, memoryCacheKey);
					configuration.memoryCache.put(memoryCacheKey, bmp);
				}
			} else {
				if (loggingEnabled) L.i(LOG_GET_IMAGE_FROM_MEMORY_CACHE_AFTER_WAITING, memoryCacheKey);
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
	 * Check whether the image URI of this task matches to image URI which is actual for current ImageView at this
	 * moment and fire {@link ImageLoadingListener#onLoadingCancelled()} event if it doesn't.
	 */
	private synchronized boolean checkTaskIsNotActual() {
		String currentCacheKey = ImageLoader.getInstance().getLoadingUriForView(imageView);
		// Check whether memory cache key (image URI) for current ImageView is actual. 
		// If ImageView is reused for another task then current task should be cancelled.
		boolean imageViewWasReused = !memoryCacheKey.equals(currentCacheKey);
		if (imageViewWasReused) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					listener.onLoadingCancelled();
				}
			});
		}

		if (loggingEnabled && imageViewWasReused) L.i(LOG_TASK_CANCELLED, memoryCacheKey);
		return imageViewWasReused;
	}

	/** Check whether the current task was interrupted */
	private boolean checkTaskIsInterrupted() {
		boolean interrupted = Thread.interrupted();
		if (loggingEnabled && interrupted) L.e(LOG_TASK_INTERRUPTED, memoryCacheKey);
		return interrupted;
	}

	/**
	 * 加载bitmap
	 * @return
	 */
	private Bitmap tryLoadBitmap() {
		DiscCacheAware discCache = configuration.discCache;
		File imageFile = discCache.get(options.getDiscCacheDir(),uri);

		Bitmap bitmap = null;
		try {
			//assets 目录
			if(uri.startsWith(ImageDownloader.PROTOCOL_FILE_ASSETS)){
				Bitmap b = decodeImage(new URI(uri));
				if (b != null) {
					return b;
				}
				// Try to load image from disc cache	
			}else if (imageFile.exists() && imageFile.length() > 0)   {
				if (loggingEnabled) L.i(LOG_LOAD_IMAGE_FROM_DISC_CACHE, memoryCacheKey);

				Bitmap b = decodeImage(imageFile.toURI());
				if (b != null) {
					return b;
				}
			//android assets目录	
			}

			// Load image from Web
			if (loggingEnabled) L.i(LOG_LOAD_IMAGE_FROM_INTERNET, memoryCacheKey);

			URI imageUriForDecoding;
			if (options.isCacheOnDisc()) {
				if(!options.getDiscCacheDir().exists()){
					options.getDiscCacheDir().mkdirs();
					StorageUtils.createRootFileDir(options.getDiscCacheDir().getAbsolutePath());
				}
				if (loggingEnabled) L.i(LOG_CACHE_IMAGE_ON_DISC, memoryCacheKey);
				
				//保存文件
				saveImageOnDisc(imageFile);
				
				discCache.put(uri, imageFile);
				
				imageUriForDecoding = imageFile.toURI();
			} else {
				imageUriForDecoding = new URI(uri);
			}

			bitmap = decodeImage(imageUriForDecoding);
			if (bitmap == null) {
				fireImageLoadingFailedEvent(FailReason.IO_ERROR);
			}
		} catch (IOException e) {
			L.e(e);
			fireImageLoadingFailedEvent(FailReason.IO_ERROR);
			if (imageFile.exists()) {
				imageFile.delete();
			}
		} catch (OutOfMemoryError e) {
			L.e(e);
			fireImageLoadingFailedEvent(FailReason.OUT_OF_MEMORY);
		} catch (Throwable e) {
			L.e(e);
			fireImageLoadingFailedEvent(FailReason.UNKNOWN);
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

	private void saveImageOnDisc(File targetFile) throws IOException, URISyntaxException {
		int width = configuration.maxImageWidthForDiscCache;
		int height = configuration.maxImageHeightForDiscCache;
		if (width > 0 || height > 0) {
			// Download, decode, compress and save image
			ImageSize targetImageSize = new ImageSize(width, height);
			ImageDecoder decoder = new ImageDecoder(new URI(uri), downloader, options);
			decoder.setLoggingEnabled(loggingEnabled);
			Bitmap bmp = decoder.decode(targetImageSize, ImageScaleType.IN_SAMPLE_INT, ViewScaleType.FIT_INSIDE);
			/*if(!targetFile.exists()){
				targetFile.mkdirs();
			}*/
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
