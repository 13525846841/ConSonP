package org.universalimageloader.core;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import org.universalimageloader.cache.memory.MemoryCacheAware;
import org.universalimageloader.core.assist.ImageLoadingListener;
import org.universalimageloader.core.assist.ImageSize;
import org.universalimageloader.core.assist.MemoryCacheUtil;
import org.universalimageloader.core.assist.QueueProcessingType;
import org.universalimageloader.core.assist.SimpleImageLoadingListener;
import org.universalimageloader.core.assist.deque.LIFOLinkedBlockingDeque;
import org.universalimageloader.core.display.BitmapDisplayer;
import org.universalimageloader.core.display.FakeBitmapDisplayer;
import org.universalimageloader.core.display.RoundedBitmapDisplayer;
import org.universalimageloader.core.download.ImageDownloader;
import org.universalimageloader.utils.L;
import org.universalimageloader.utils.StorageUtils;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.healthtalk.utils.HStringUtil;


/**
 * 图片下载核心类
 * Singletone for image loading and displaying at {@link ImageView ImageViews}<br />
 * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before any other method.
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class ImageLoader {
	public static final String TAG = ImageLoader.class.getSimpleName();
	private static final String ERROR_WRONG_ARGUMENTS = "Wrong arguments were passed to displayImage() method (ImageView reference are required)";
	private static final String ERROR_NOT_INIT = "ImageLoader must be init with configuration before using";
	private static final String ERROR_INIT_CONFIG_WITH_NULL = "ImageLoader configuration can not be initialized with null";
	private static final String LOG_LOAD_IMAGE_FROM_MEMORY_CACHE = "Load image from memory cache [%s]";

	private ImageLoaderConfiguration configuration;//下载配置
	private ExecutorService imageLoadingExecutor;
	private ExecutorService cachedImageLoadingExecutor;

	private final ImageLoadingListener emptyListener = new SimpleImageLoadingListener();
	private final BitmapDisplayer fakeBitmapDisplayer = new FakeBitmapDisplayer();

	private final Map<Integer, String> cacheKeysForImageViews = Collections.synchronizedMap(new HashMap<Integer, String>());
	private final Map<String, ReentrantLock> uriLocks = new WeakHashMap<String, ReentrantLock>();
	private final AtomicBoolean paused = new AtomicBoolean(false);

	private static ImageLoader instance;
	private static Application mApplication;
	
	public final DisplayImageOptions maleImageOptions;//男
	public final DisplayImageOptions femalImageOptions;//女
	public final DisplayImageOptions unkounwImageOptions;//未知
	public final DisplayImageOptions groupImageOptions;//群
	public final DisplayImageOptions notImageOptions;//没有默认图片
	
	
	public static ImageLoader getInstance(){
		if(instance == null){
			synchronized(ImageLoader.class){
				if(instance == null){
					instance = new ImageLoader(HTalkApplication.getApplication());
				}
			}
		}
		return instance;
	}
	
	public static AssetManager getAssetManager(){
		if(mApplication == null)return null;
		return mApplication.getAssets();
	}
	
	protected ImageLoader(Application application) {
		mApplication = application;
		ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(mApplication);
		init(configuration);
		
		maleImageOptions = new DisplayImageOptions.Builder(mApplication)
		.showStubImage(R.drawable.default_head_mankind)
		.showImageForEmptyUri(R.drawable.default_head_mankind)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.cacheOnDisc(new File(StorageUtils.getHeadersPath()))
		.cacheInMemory()
		.displayer(new RoundedBitmapDisplayer(5))
		.build();
		
		femalImageOptions = new DisplayImageOptions.Builder(mApplication)
		.showStubImage(R.drawable.default_head_mankind)
		.showImageForEmptyUri(R.drawable.default_head_mankind)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.cacheOnDisc(new File(StorageUtils.getHeadersPath()))
		.cacheInMemory()
		.displayer(new RoundedBitmapDisplayer(5))
		.build();
		
		unkounwImageOptions = new DisplayImageOptions.Builder(mApplication)
		.showStubImage(R.drawable.default_head_mankind)
		.showImageForEmptyUri(R.drawable.default_head_mankind)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.cacheOnDisc(new File(StorageUtils.getHeadersPath()))
		.cacheInMemory()
		.displayer(new RoundedBitmapDisplayer(5))
		.build();
		
		groupImageOptions = new DisplayImageOptions.Builder(mApplication)
		.showStubImage(R.drawable.default_head_group)
		.showImageForEmptyUri(R.drawable.default_head_group)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.cacheOnDisc(new File(StorageUtils.getHeadersPath()))
		.cacheInMemory()
		.displayer(new RoundedBitmapDisplayer(5))
		.build();
		
		notImageOptions = new DisplayImageOptions.Builder(mApplication)
				.showStubImage(R.drawable.default_head_mankind)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.cacheOnDisc(new File(StorageUtils.getHeadersPath()))
		.cacheInMemory()
		.displayer(new RoundedBitmapDisplayer(5))
		.build();
	}

	/**
	 * 初始化下载单例模式
	 * Initializes ImageLoader's singleton instance with configuration. Method should be called <b>once</b> (each
	 * following call will have no effect)<br />
	 * @param configuration
	 *            {@linkplain ImageLoaderConfiguration ImageLoader configuration}
	 * @throws IllegalArgumentException
	 *             if <b>configuration</b> parameter is null
	 */
	public synchronized void init(ImageLoaderConfiguration configuration) {
		if (configuration == null) {
			throw new IllegalArgumentException(ERROR_INIT_CONFIG_WITH_NULL);
		}
		if (this.configuration == null) {
			this.configuration = configuration;
		}
	}

	/**
	 * Adds display image task to execution pool. Image will be set to ImageView when it's turn. <br/>
	 * Default {@linkplain DisplayImageOptions display image options} from {@linkplain ImageLoaderConfiguration
	 * configuration} will be used.<br />
	 * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
	 * 
	 * @param uri
	 *            Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
	 * @param imageView
	 *            {@link ImageView} which should display image
	 * @throws RuntimeException
	 *             if {@link #init(ImageLoaderConfiguration)} method wasn't called before
	 */
	public void displayImage(String uri, ImageView imageView) {
		displayImage(uri, imageView, null, null);
	}
	
	/**
	 * 根据性别显示默认 显示头像
	 * @param sex 头像类型
	 * @param uri
	 * @param imageView
	 */
	public void displayImage(String sex,String uri,ImageView imageView){
		if(!HStringUtil.isEmpty(uri) && uri.startsWith("assets/customerIcons"))
			uri=null;
		if("1".equals(sex) || "M".equalsIgnoreCase(sex)){//男
			displayImage(uri, imageView, maleImageOptions);
		}else if("2".equals(sex) || "W".equalsIgnoreCase(sex)){//女
			displayImage(uri, imageView, femalImageOptions);
		}else if("4".equals(sex)){//群
			displayImage(uri, imageView, groupImageOptions);
		}else if("5".equals(sex)){//没有默认图片
			displayImage(uri, imageView, notImageOptions);
		} else{//未知 || "X".equalsIgnoreCase(type)
			displayImage(uri, imageView, unkounwImageOptions);
		}
	}

	/**
	 * 通过ID直接加载头像
	 * @param sex  性别  
	 * @param id 话题 或者人的id
	 * @param imageView
	 */
	public void displayImageFromId(String sex,String id,ImageView imageView){
//		if(!HStringUtil.isEmpty(uri) && uri.startsWith("assets/customerIcons"))
//			uri=null;
		String uri = HTalkApplication.getHttpUrls().LOADCUSHIDPICSERVLET42+"?customerid="+id+"&isgroup=0";;
		if("1".equals(sex) || "M".equalsIgnoreCase(sex)){//男
			displayImage(uri, imageView, maleImageOptions);
		}else if("2".equals(sex) || "W".equalsIgnoreCase(sex)){//女
			displayImage(uri, imageView, femalImageOptions);
		}else if("4".equals(sex)){//群
			uri = HTalkApplication.getHttpUrls().LOADCUSHIDPICSERVLET42+"?customerid="+id+"&isgroup=1";
			displayImage(uri, imageView, groupImageOptions);
		}else if("5".equals(sex)){//没有默认图片
			displayImage(uri, imageView, notImageOptions);
		} else{//未知 || "X".equalsIgnoreCase(type)
			displayImage(uri, imageView, unkounwImageOptions);
		}
	}
	/**
	 * 通过ID直接加载头像
	 * @param sex  性别  
	 * @param id 话题 或者人的id
	 * @param imageView
	 */
	public void displayImageFromId(String id,ImageView imageView){
//		String uri = HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_FROM_ID+"?customerid="+id+"&isgroup=0";;
		String uri = HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_FROM_ID_NEW+"?customerid="+id+"&isgroup=0";;
		displayImage(uri, imageView, unkounwImageOptions);
	}
	/**
	 * Adds display image task to execution pool. Image will be set to ImageView when it's turn.<br />
	 * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
	 * 
	 * @param uri
	 *            Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
	 * @param imageView
	 *            {@link ImageView} which should display image
	 * @param options
	 *            {@linkplain DisplayImageOptions Display image options} for image displaying. If <b>null</b> - default
	 *            display image options
	 *            {@linkplain ImageLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayImageOptions) from
	 *            configuration} will be used.
	 * @throws RuntimeException
	 *             if {@link #init(ImageLoaderConfiguration)} method wasn't called before
	 */
	public void displayImage(String uri, ImageView imageView, DisplayImageOptions options) {
		displayImage(uri, imageView, options, null);
	}
	

	/**
	 * Adds display image task to execution pool. Image will be set to ImageView when it's turn.<br />
	 * Default {@linkplain DisplayImageOptions display image options} from {@linkplain ImageLoaderConfiguration
	 * configuration} will be used.<br />
	 * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
	 * 
	 * @param uri
	 *            Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
	 * @param imageView
	 *            {@link ImageView} which should display image
	 * @param listener
	 *            {@linkplain ImageLoadingListener Listener} for image loading process. Listener fires events on UI
	 *            thread.
	 * @throws RuntimeException
	 *             if {@link #init(ImageLoaderConfiguration)} method wasn't called before
	 */
	public void displayImage(String uri, ImageView imageView, ImageLoadingListener listener) {
		displayImage(uri, imageView, null, listener);
	}

	/**
	 * Adds display image task to execution pool. Image will be set to ImageView when it's turn.<br />
	 * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
	 * 
	 * @param uri
	 *            Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
	 * @param imageView
	 *            {@link ImageView} which should display image
	 * @param options
	 *            {@linkplain DisplayImageOptions Display image options} for image displaying. If <b>null</b> - default
	 *            display image options
	 *            {@linkplain ImageLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayImageOptions) from
	 *            configuration} will be used.
	 * @param listener
	 *            {@linkplain ImageLoadingListener Listener} for image loading process. Listener fires events on UI
	 *            thread.
	 * @throws RuntimeException
	 *             if {@link #init(ImageLoaderConfiguration)} method wasn't called before
	 */
	public void displayImage(String uri, ImageView imageView, DisplayImageOptions options, ImageLoadingListener listener) {
		
		if (configuration == null) {
			throw new RuntimeException(ERROR_NOT_INIT);
		}
		
		if (imageView == null) {
			L.w(TAG, ERROR_WRONG_ARGUMENTS);
			return;
		}
		if (listener == null) {
			listener = emptyListener;
		}
		if (options == null) {
			options = configuration.defaultDisplayImageOptions;
		}

		if (uri == null || uri.trim().length() == 0) {
			cacheKeysForImageViews.remove(imageView.hashCode());
			listener.onLoadingStarted();
			if (options.isShowImageForEmptyUri()) {
				imageView.setImageDrawable(options.getImageForEmptyDrawable());
			} else {
				imageView.setImageDrawable(null);
			}
			listener.onLoadingComplete(null);
			return;
		}
		
		uri = getDownPathUri(uri);

		ImageSize targetSize = getImageSizeScaleTo(imageView);
		String memoryCacheKey = MemoryCacheUtil.generateKey(uri, targetSize);
		cacheKeysForImageViews.put(imageView.hashCode(), memoryCacheKey);

		Bitmap bmp = configuration.memoryCache.get(memoryCacheKey);
		
		if (bmp != null && !bmp.isRecycled()) {
			if (configuration.loggingEnabled) L.i(LOG_LOAD_IMAGE_FROM_MEMORY_CACHE, memoryCacheKey);
			listener.onLoadingStarted();
			options.getDisplayer().display(bmp, imageView);
			listener.onLoadingComplete(bmp);
		} else {
			listener.onLoadingStarted();

			if (options.isShowStubImage()) {
				imageView.setImageDrawable(options.getStubDrawable());
			} else {
				if (options.isResetViewBeforeLoading()) {
					imageView.setImageDrawable(null);
				}
			}

			initExecutorsIfNeed();
			
			ImageLoadingInfo imageLoadingInfo = new ImageLoadingInfo(uri, imageView, targetSize, options, listener, getLockForUri(uri),null);
			LoadAndDisplayImageTask displayImageTask = new LoadAndDisplayImageTask(configuration, imageLoadingInfo, new Handler());
			
			boolean isImageCachedOnDisc = configuration.discCache.get(options.getDiscCacheDir(),uri).exists();
			
			if (isImageCachedOnDisc) {
				cachedImageLoadingExecutor.submit(displayImageTask);
			} else {
				imageLoadingExecutor.submit(displayImageTask);
			}
		}
	}

	
//	public void displayImageNice(List<UrlEntity> entities,ImageView imageView){
//		DisplayImageOptions options = null;
//		if (configuration == null) {
//			throw new RuntimeException(ERROR_NOT_INIT);
//		}
//		if (imageView == null) {
//			L.w(TAG, ERROR_WRONG_ARGUMENTS);
//			return;
//		}
//		if (options == null) {
//			options = configuration.defaultDisplayImageOptions;
//		}
//		
//		//设置默认图片
//		if (entities == null || entities.size() == 0) {
//			cacheKeysForImageViews.remove(imageView.hashCode());
//			imageView.setImageResource(R.drawable.default_jiugong_icon);
//			return;
//		}
//		//获取key
//		ImageSize targetSize = getImageSizeScaleTo(imageView);
//		
//		final int size = entities.size();
//		Bitmap composeBitmap = Bitmap.createBitmap(targetSize.getWidth(),targetSize.getHeight(),options.getBitmapConfig());
//		Canvas canvas = new Canvas(composeBitmap);
//		canvas.drawColor(0xFFE2E2E2);
//		ImageSize imageSize  = new ImageSize(targetSize.getWidth()/3, targetSize.getHeight()/3);
//		int leftP = 0;//y坐标
//		int topP = 0;//x坐标
//		int index = 0;
//		
//		for (int i = 0; i < 3; i++) {
//			for (int j = 0; j < 3; j++) {
//				if(index >= size)break;
//				final String string = entities.get(index).getUrl();
//				Bitmap bitmap2 = tryLoadBitmap(string,imageSize);//下载图片
//				
//				if(bitmap2 != null){
//					canvas.drawBitmap(bitmap2,leftP, topP, null);
//					leftP += imageSize.getWidth();
//				}
//				index++;
//			}
//			leftP = 0;
//			topP += imageSize.getHeight();
//		}
//		canvas.save(Canvas.ALL_SAVE_FLAG);
//		canvas.restore();
//	}
	
	
	/**
	 * 显示九宫格图片
	 * @param collections
	 * @param imageView
	 * @param options
	 */
	public void displayImage(List<String> collections,ImageView imageView,DisplayImageOptions options,ImageLoadingListener listener){
		if (configuration == null) {
			throw new RuntimeException(ERROR_NOT_INIT);
		}
		if (imageView == null) {
			L.w(TAG, ERROR_WRONG_ARGUMENTS);
			return;
		}
		if (options == null) {
			options = configuration.defaultDisplayImageOptions;
		}
		
		if (listener == null) {
			listener = emptyListener;
		}
		
		//设置默认图片
		if (collections == null || collections.size() == 0) {
			cacheKeysForImageViews.remove(imageView.hashCode());
			if (options.isShowImageForEmptyUri()) {
				imageView.setImageResource(options.getImageForEmptyUri());
			} else {
				imageView.setImageBitmap(null);
			}
			return;
		}
		
		//获取key
		ImageSize targetSize = getImageSizeScaleTo(imageView);
		String memoryCacheKey = MemoryCacheUtil.generateKey(collections.toString(), targetSize);
		cacheKeysForImageViews.put(imageView.hashCode(), memoryCacheKey);
		Bitmap bmp = configuration.memoryCache.get(memoryCacheKey);
		//内存中获取
		if(bmp != null && !bmp.isRecycled()){
			options.getDisplayer().display(bmp, imageView);
		//网络，本地获取	
		}else{
			if (options.isShowStubImage()) {
				imageView.setImageResource(options.getStubImage());
			} else {
				if (options.isResetViewBeforeLoading()) {
					imageView.setImageBitmap(null);
				}
			}
			//初始化线程池
			initExecutorsIfNeed();
			String urls = collections.toString();
			ImageLoadingInfo imageLoadingInfo = new ImageLoadingInfo(null, imageView, targetSize, options, listener, getLockForUri(urls),collections);
			MulitLoadAndDisplayImageTask displayImageTask = new MulitLoadAndDisplayImageTask(configuration, imageLoadingInfo, new Handler());
			//是否已经在本地缓存
			boolean isImageCachedOnDisc = configuration.discCache.get(options.getDiscCacheDir(),urls).exists();
			if (isImageCachedOnDisc) {
				cachedImageLoadingExecutor.submit(displayImageTask);
			} else {
				imageLoadingExecutor.submit(displayImageTask);
			}
		}
	}
	
	/**
	 * 显示九宫格图片
	 * @param strs
	 * @param imageView
	 * @param options
	 * 
	 */
	public void displayImage(String[] strs,ImageView imageView,DisplayImageOptions options){
		List<String> list = null;
		if(strs != null){
			list = Arrays.asList(strs);
		}
		displayImage(list, imageView, options);
	}
	
	/**
	 * 显示九宫格图片
	 * @param strs
	 * @param imageView
	 * @param options
	 * 
	 */
	public void displayImage(List<String> strs,ImageView imageView,DisplayImageOptions options){
		displayImage(strs, imageView, options,null);
	}
	
	/**
	 * Adds load image task to execution pool. Image will be returned with
	 * {@link ImageLoadingListener#onLoadingComplete(Bitmap) callback}.<br />
	 * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
	 * 
	 * @param context
	 *            Application context (used for creation of fake {@link ImageView})
	 * @param uri
	 *            Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
	 * @param listener
	 *            {@linkplain ImageLoadingListener Listener} for image loading process. Listener fires events on UI
	 *            thread.
	 */
	public void loadImage(Context context, String uri, ImageLoadingListener listener) {
		loadImage(context, uri, null, null, listener);
	}

	/**
	 * Adds load image task to execution pool. Image will be returned with
	 * {@link ImageLoadingListener#onLoadingComplete(Bitmap) callback}.<br />
	 * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
	 * 
	 * @param context
	 *            Application context (used for creation of fake {@link ImageView})
	 * @param uri
	 *            Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
	 * @param minImageSize
	 *            Minimal size for {@link Bitmap} which will be returned in
	 *            {@linkplain ImageLoadingListener#onLoadingComplete(Bitmap) callback}. Downloaded image will be decoded
	 *            and scaled to {@link Bitmap} of the size which is <b>equal or larger</b> (usually a bit larger) than
	 *            incoming minImageSize .
	 * @param listener
	 *            {@linkplain ImageLoadingListener Listener} for image loading process. Listener fires events on UI
	 *            thread.
	 */
	public void loadImage(Context context, String uri, ImageSize minImageSize, ImageLoadingListener listener) {
		loadImage(context, uri, minImageSize, null, listener);
	}

	/**
	 * Adds load image task to execution pool. Image will be returned with
	 * {@link ImageLoadingListener#onLoadingComplete(Bitmap) callback}.<br />
	 * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
	 * 
	 * @param context
	 *            Application context (used for creation of fake {@link ImageView})
	 * @param uri
	 *            Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
	 * @param options
	 *            {@linkplain DisplayImageOptions Display image options} for image displaying. If <b>null</b> - default
	 *            display image options
	 *            {@linkplain ImageLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayImageOptions) from
	 *            configuration} will be used.<br />
	 *            Incoming options should contain {@link FakeBitmapDisplayer} as displayer.
	 * @param listener
	 *            {@linkplain ImageLoadingListener Listener} for image loading process. Listener fires events on UI
	 *            thread.
	 */
	public void loadImage(Context context, String uri, DisplayImageOptions options, ImageLoadingListener listener) {
		loadImage(context, uri, null, options, listener);
	}

	/**
	 * Adds load image task to execution pool. Image will be returned with
	 * {@link ImageLoadingListener#onLoadingComplete(Bitmap) callback}.<br />
	 * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
	 * 
	 * @param context
	 *            Application context (used for creation of fake {@link ImageView})
	 * @param uri
	 * 				fileassets:///image.png
	 *            Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
	 * @param minImageSize
	 *            Minimal size for {@link Bitmap} which will be returned in
	 *            {@linkplain ImageLoadingListener#onLoadingComplete(Bitmap) callback}. Downloaded image will be decoded
	 *            and scaled to {@link Bitmap} of the size which is <b>equal or larger</b> (usually a bit larger) than
	 *            incoming minImageSize .
	 * @param options
	 *            {@linkplain DisplayImageOptions Display image options} for image displaying. If <b>null</b> - default
	 *            display image options
	 *            {@linkplain ImageLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayImageOptions) from
	 *            configuration} will be used.<br />
	 *            Incoming options should contain {@link FakeBitmapDisplayer} as displayer.
	 * @param listener
	 *            {@linkplain ImageLoadingListener Listener} for image loading process. Listener fires events on UI
	 *            thread.
	 */
	public void loadImage(Context context, String uri, ImageSize minImageSize, DisplayImageOptions options, ImageLoadingListener listener) {
		if (minImageSize == null) {
			minImageSize = new ImageSize(configuration.maxImageWidthForMemoryCache, configuration.maxImageHeightForMemoryCache);
		}
		if (options == null) {
			options = configuration.defaultDisplayImageOptions;
		}
		
		DisplayImageOptions optionsWithFakeDisplayer;
		if (options.getDisplayer() instanceof FakeBitmapDisplayer) {
			optionsWithFakeDisplayer = options;
		} else {
			optionsWithFakeDisplayer = new DisplayImageOptions.Builder(mApplication)
				.cloneFrom(options)
				.displayer(fakeBitmapDisplayer)
				.build();
		}
		
		ImageView fakeImage = new ImageView(context);
		fakeImage.setLayoutParams(new LayoutParams(minImageSize.getWidth(), minImageSize.getHeight()));
		fakeImage.setScaleType(ScaleType.CENTER_CROP);
		
		displayImage(uri, fakeImage, optionsWithFakeDisplayer, listener);
	}
	
	public String getDownPathUri(String url){//path=assets/customerIcons/s_zcmale_24.png
		if(url == null ||  url.startsWith("http:") || url.startsWith("https:") || "".equals(url.trim()) || url.startsWith("file:///") ||  url.startsWith(ImageDownloader.PROTOCOL_FILE_ASSETS)){
			return url;
		}else if ((url.equals("assets/customerIcons/系统默认头像女.png")||url.equals("assets/customerIcons/系统默认头像女_24.png"))) {
			return url = ImageDownloader.PROTOCOL_FILE_ASSETS+ ":///customerIcons/default_head_female.png";
		} else if ( url.equals("assets/customerIcons/系统默认头像男.png")) {
			return url = ImageDownloader.PROTOCOL_FILE_ASSETS+ ":///customerIcons/default_head_mankind.png";
		} else if (url.equals("assets/customerIcons/s_zcmale.png")) {
			return url = ImageDownloader.PROTOCOL_FILE_ASSETS+ ":///customerIcons/default_head_mankind.png";
		} else if (url.equals("assets/customerIcons/default_women.png")) {//女头像
			return url = ImageDownloader.PROTOCOL_FILE_ASSETS+ ":///customerIcons/default_head_female.png";
		}else if (url.equals("assets/customerIcons/default_women_24.png")) {//24*24女头像
			return url = ImageDownloader.PROTOCOL_FILE_ASSETS+ ":///customerIcons/default_head_female.png";
		}else if( url.equals("assets/customerIcons/s_zcmale_24.png")){//24*24 男头像
			return url = ImageDownloader.PROTOCOL_FILE_ASSETS+ ":///customerIcons/default_head_mankind.png";
		}else if( url.equals("assets/group/groupIcon.png")){//24*24 话题头像
			return url = ImageDownloader.PROTOCOL_FILE_ASSETS+ ":///customerIcons/default_head_group.png";
		}else{
			final StringBuffer sb = new StringBuffer(configuration.downRootUrlPath);
			sb.append(url);
//			LogUtil.d("TAG","图片下载路径"+sb.toString());
			return sb.toString();
		}
	}

	
	private void initExecutorsIfNeed() {
		if (imageLoadingExecutor == null || imageLoadingExecutor.isShutdown()) {
			imageLoadingExecutor = createExecutor();
		}
		if (cachedImageLoadingExecutor == null || cachedImageLoadingExecutor.isShutdown()) {
			cachedImageLoadingExecutor = createExecutor();
		}
	}

	/**
	 * 创建一个线程池
	 * @return
	 */
	private ExecutorService createExecutor() {
		boolean lifo = configuration.tasksProcessingType == QueueProcessingType.LIFO;
		BlockingQueue<Runnable> taskQueue = lifo ? new LIFOLinkedBlockingDeque<Runnable>() : new LinkedBlockingQueue<Runnable>();
		return new ThreadPoolExecutor(configuration.threadPoolSize, configuration.threadPoolSize, 0L, TimeUnit.MILLISECONDS, taskQueue,
				configuration.displayImageThreadFactory);
	}

	/** Returns memory cache */
	public MemoryCacheAware<String, Bitmap> getMemoryCache() {
		return configuration.memoryCache;
	}

	/**
	 * Clear memory cache.<br />
	 * Do nothing if {@link #init(ImageLoaderConfiguration)} method wasn't called before.
	 */
	public void clearMemoryCache() {
		if (configuration != null) {
			configuration.memoryCache.clear();
		}
	}

	/** Returns disc cache */
/*	private DiscCacheAware getDiscCache() {
		return configuration.discCache;
	}*/

	/**
	 * Clear disc cache.<br />
	 * Do nothing if {@link #init(ImageLoaderConfiguration)} method wasn't called before.
	 */
	public void clearDiscCache() {
		if (configuration != null) {
			configuration.discCache.clear();
		}
	}
	
	/**
	 *获取本地保存的文件名 
	 * @param dir
	 * @param name
	 * @return
	 */
	public File getOnDiscFileName(File dir,String name){
		name = getDownPathUri(name);
		return configuration.discCache.get(dir,name);
	}

	/** Returns URI of image which is loading at this moment into passed {@link ImageView} */
	public String getLoadingUriForView(ImageView imageView) {
		return cacheKeysForImageViews.get(imageView.hashCode());
	}

	/**
	 * 退出正在显示的图片线程
	 * Cancel the task of loading and displaying image for passed {@link ImageView}.
	 * @param imageView
	 *            {@link ImageView} for which display task will be cancelled
	 */
	public void cancelDisplayTask(ImageView imageView) {
		cacheKeysForImageViews.remove(imageView.hashCode());
	}

	/**
	 * 暂停下载
	 * Pause ImageLoader. All new "load&display" tasks won't be executed until ImageLoader is {@link #resume() resumed}.<br />
	 * Already running tasks are not paused.
	 */
	public void pause() {
		paused.set(true);
	}

	/**重启下 Resumes waiting "load&display" tasks */
	public void resume() {
		synchronized (paused) {
			paused.set(false);
			paused.notifyAll();
		}
	}

	/**停止所有的下载 Stops all running display image tasks, discards all other scheduled tasks */
	public void stop() {
		if (imageLoadingExecutor != null) {
			imageLoadingExecutor.shutdownNow();
		}
		if (cachedImageLoadingExecutor != null) {
			cachedImageLoadingExecutor.shutdownNow();
		}
	}

	/**
	 * 定义下载大小
	 * Defines image size for loading at memory (for memory economy) by {@link ImageView} parameters.<br />
	 * Size computing algorithm:<br />
	 * 1) Get <b>layout_width</b> and <b>layout_height</b>. If both of them haven't exact value then go to step #2.</br>
	 * 2) Get <b>maxWidth</b> and <b>maxHeight</b>. If both of them are not set then go to step #3.<br />
	 * 3) Get <b>maxImageWidthForMemoryCache</b> and <b>maxImageHeightForMemoryCache</b> from configuration. If both of
	 * them are not set then go to step #3.<br />
	 * 4) Get device screen dimensions.
	 */
	private ImageSize getImageSizeScaleTo(ImageView imageView) {
		final DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();

		LayoutParams params = imageView.getLayoutParams();
		int width = params.width; // Get layout width parameter
		if (width <= 0) width = getFieldValue(imageView, "mMaxWidth"); // Check maxWidth parameter
		if (width <= 0) width = configuration.maxImageWidthForMemoryCache;
		if (width <= 0) width = displayMetrics.widthPixels;

		int height = params.height; // Get layout height parameter
		if (height <= 0) height = getFieldValue(imageView, "mMaxHeight"); // Check maxHeight parameter
		if (height <= 0) height = configuration.maxImageHeightForMemoryCache;
		if (height <= 0) height = displayMetrics.heightPixels;

		return new ImageSize(width, height);
	}

	private int getFieldValue(Object object, String fieldName) {
		int value = 0;
		try {
			Field field = ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue = (Integer) field.get(object);
			if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
				value = fieldValue;
			}
		} catch (Exception e) {
			L.e(e);
		}
		return value;
	}

	private ReentrantLock getLockForUri(String uri) {
		ReentrantLock lock = uriLocks.get(uri);
		if (lock == null) {
			lock = new ReentrantLock();
			uriLocks.put(uri, lock);
		}
		return lock;
	}

	AtomicBoolean getPause() {
		return paused;
	}
	
	
}
