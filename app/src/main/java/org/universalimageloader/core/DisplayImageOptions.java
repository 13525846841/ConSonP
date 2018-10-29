package org.universalimageloader.core;

import java.io.File;

import org.universalimageloader.core.assist.ImageScaleType;
import org.universalimageloader.core.display.BitmapDisplayer;
import org.universalimageloader.core.display.SimpleBitmapDisplayer;
import org.universalimageloader.utils.StorageUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;


/**
 * Contains options for image display. Defines:
 * <ul>
 * <li>whether stub image will be displayed in {@link android.widget.ImageView ImageView} during image loading</li>
 * <li>whether stub image will be displayed in {@link android.widget.ImageView ImageView} if empty URI is passed</li>
 * <li>whether {@link android.widget.ImageView ImageView} should be reset before image loading start</li>
 * <li>whether loaded image will be cached in memory</li>
 * <li>whether loaded image will be cached on disc</li>
 * <li>image scale type</li>
 * <li>bitmap decoding configuration</li>
 * <li>delay before loading of image</li>
 * <li>how decoded {@link Bitmap} will be displayed</li>
 * </ul>
 * 
 * You can create instance:
 * <ul>
 * <li>with {@link Builder}:<br />
 * <b>i.e.</b> :
 * <code>new {@link DisplayImageOptions}.{@link Builder#Builder() Builder()}.{@link Builder#cacheInMemory() cacheInMemory()}.
 * {@link Builder#showStubImage(int) showStubImage()}.{@link Builder#build() build()}</code><br />
 * </li>
 * <li>or by static method: {@link #createSimple()}</li> <br />
 * 
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public final class DisplayImageOptions {
	
	private Drawable stubDrawable;
	private Drawable imageForEmptyDrawable;
	
	private final int stubImage;
	private final int imageForEmptyUri;
	
	private final boolean resetViewBeforeLoading;
	private final boolean cacheInMemory;
	private final boolean cacheOnDisc;
	private final ImageScaleType imageScaleType;
	private final Bitmap.Config bitmapConfig;
	private final int delayBeforeLoading;
	private final BitmapDisplayer displayer;
	private final File discCacheDir;//保存目录

	private DisplayImageOptions(Builder builder) {
		stubDrawable = builder.stubDrawable;
		imageForEmptyDrawable = builder.imageForEmptyDrawable;
		
		stubImage = builder.stubImage;
		imageForEmptyUri = builder.imageForEmptyUri;
		
		resetViewBeforeLoading = builder.resetViewBeforeLoading;
		cacheInMemory = builder.cacheInMemory;
		cacheOnDisc = builder.cacheOnDisc;
		imageScaleType = builder.imageScaleType;
		bitmapConfig = builder.bitmapConfig;
		delayBeforeLoading = builder.delayBeforeLoading;
		displayer = builder.displayer;
		discCacheDir = builder.discCacheDir;
	}

	boolean isShowStubImage() {
		return stubImage != 0;
	}

	boolean isShowImageForEmptyUri() {
		return imageForEmptyUri != 0;
	}
	
	public File getDiscCacheDir() {
		return discCacheDir;
	}

	Integer getStubImage() {
		return stubImage;
	}

	Integer getImageForEmptyUri() {
		return imageForEmptyUri;
	}
	
	public Drawable getStubDrawable() {
		return stubDrawable;
	}

	public void setStubDrawable(Drawable stubDrawable) {
		this.stubDrawable = stubDrawable;
	}

	public Drawable getImageForEmptyDrawable() {
		return imageForEmptyDrawable;
	}

	public void setImageForEmptyDrawable(Drawable imageForEmptyDrawable) {
		this.imageForEmptyDrawable = imageForEmptyDrawable;
	}

	boolean isResetViewBeforeLoading() {
		return resetViewBeforeLoading;
	}

	boolean isCacheInMemory() {
		return cacheInMemory;
	}

	boolean isCacheOnDisc() {
		return cacheOnDisc;
	}

	ImageScaleType getImageScaleType() {
		return imageScaleType;
	}

	Bitmap.Config getBitmapConfig() {
		return bitmapConfig;
	}

	boolean isDelayBeforeLoading() {
		return delayBeforeLoading > 0;
	}

	int getDelayBeforeLoading() {
		return delayBeforeLoading;
	}

	BitmapDisplayer getDisplayer() {
		return displayer;
	}

	/**
	 * Builder for {@link DisplayImageOptions}
	 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
	 */
	public static class Builder {
		
		private Drawable stubDrawable;
		private Drawable imageForEmptyDrawable;
		
		private int stubImage = 0;
		private int imageForEmptyUri = 0;
		private Context mContext;
		private boolean resetViewBeforeLoading = false;
		private boolean cacheInMemory = false;
		private boolean cacheOnDisc = false;
		private ImageScaleType imageScaleType = ImageScaleType.IN_SAMPLE_POWER_OF_2;
		private Bitmap.Config bitmapConfig = Bitmap.Config.ARGB_8888;
		private int delayBeforeLoading = 0;
		private BitmapDisplayer displayer = DefaultConfigurationFactory.createBitmapDisplayer();
		private File discCacheDir;//缓存的根路径
		
		public Builder(Context context) {
			mContext = context.getApplicationContext();
		}
		
		/**
		 * Stub image will be displayed in {@link android.widget.ImageView ImageView} during image loading
		 * @param stubImageRes
		 * Stub image resource
		 */
		public Builder showStubImage(int stubImageRes) {
			stubImage = stubImageRes;
			try {
				stubDrawable = mContext.getResources().getDrawable(stubImageRes);
			} catch (Exception e) {
			}
			return this;
		}

		/**
		 * Image will be displayed in {@link android.widget.ImageView ImageView} if empty URI (null or empty string)
		 * will be passed to <b>ImageLoader.displayImage(...)</b> method.
		 * 
		 * @param imageRes
		 *            Image resource
		 */
		public Builder showImageForEmptyUri(int imageRes) {
			imageForEmptyUri = imageRes;
			try {
				imageForEmptyDrawable = mContext.getResources().getDrawable(imageRes);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return this;
		}

		/** {@link android.widget.ImageView ImageView} will be reset (set <b>null</b>) before image loading start */
		public Builder resetViewBeforeLoading() {
			resetViewBeforeLoading = true;
			return this;
		}

		/** Loaded image will be cached in memory */
		public Builder cacheInMemory() {
			cacheInMemory = true;
			return this;
		}

		/** Loaded image will be cached on disc */
		public Builder cacheOnDisc(File discCacheDir) {
			if(discCacheDir != null){
				cacheOnDisc = true;
				this.discCacheDir = discCacheDir;
			}else{
				cacheOnDisc = false;
			}
			return this;
		}

		/**
		 * Sets {@link ImageScaleType decoding type} for image loading task. Default value -
		 * {@link ImageScaleType#POWER_OF_2}
		 */
		public Builder imageScaleType(ImageScaleType imageScaleType) {
			this.imageScaleType = imageScaleType;
			return this;
		}

		/** Sets {@link Bitmap.Config bitmap config} for image decoding. Default value - {@link Bitmap.Config#ARGB_8888} */
		public Builder bitmapConfig(Bitmap.Config bitmapConfig) {
			this.bitmapConfig = bitmapConfig;
			return this;
		}

		/** Sets delay time before starting loading task. Default - no delay. */
		public Builder delayBeforeLoading(int delayInMillis) {
			this.delayBeforeLoading = delayInMillis;
			return this;
		}

		/**
		 * Sets custom {@link BitmapDisplayer displayer} for image loading task. Default value -
		 * {@link DefaultConfigurationFactory#createBitmapDisplayer()}
		 */
		public Builder displayer(BitmapDisplayer displayer) {
			this.displayer = displayer;
			return this;
		}

		/** Sets all options equal to incoming options */
		public Builder cloneFrom(DisplayImageOptions options) {
			stubImage = options.stubImage;
			imageForEmptyUri = options.imageForEmptyUri;
			resetViewBeforeLoading = options.resetViewBeforeLoading;
			cacheInMemory = options.cacheInMemory;
			cacheOnDisc = options.cacheOnDisc;
			imageScaleType = options.imageScaleType;
			bitmapConfig = options.bitmapConfig;
			delayBeforeLoading = options.delayBeforeLoading;
			displayer = options.displayer;
			discCacheDir = options.discCacheDir;
			return this;
		}

		/** Builds configured {@link DisplayImageOptions} object */
		public DisplayImageOptions build() {
			if(this.discCacheDir == null){
				this.discCacheDir  = DefaultConfigurationFactory.createDefaultCacheDir(mContext);
			}
			return new DisplayImageOptions(this);
		}
	}

	/**
	 * Creates options appropriate for single displaying:
	 * <ul>
	 * <li>View will <b>not</b> be reset before loading</li>
	 * <li>Loaded image will <b>not</b> be cached in memory</li>
	 * <li>Loaded image will <b>not</b> be cached on disc</li>
	 * <li>{@link ImageScaleType#IN_SAMPLE_POWER_OF_2} decoding type will be used</li>
	 * <li>{@link Bitmap.Config#ARGB_8888} bitmap config will be used for image decoding</li>
	 * <li>{@link SimpleBitmapDisplayer} will be used for image displaying</li>
	 * </ul>
	 * 创建一个简单的options 默认加入缓存
	 * These option are appropriate for simple single-use image (from drawables or from Internet) displaying.
	 */
	public static DisplayImageOptions createSimple(Context context) {
		return new Builder(context).cacheOnDisc(new File(StorageUtils.getHeadersPath()))
				.cacheInMemory().build();
	}
	
	/**
	 * 默认没有缓存
	 * @param context
	 * @return
	 */
	public static DisplayImageOptions createSimpleIsCache(Context context){
		return new Builder(context).build();
	}
	
}
