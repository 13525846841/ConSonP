package org.universalimageloader.core;

import java.io.File;

import org.universalimageloader.cache.disc.DiscCacheAware;
import org.universalimageloader.cache.disc.impl.FileCountLimitedDiscCache;
import org.universalimageloader.cache.disc.impl.TotalSizeLimitedDiscCache;
import org.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import org.universalimageloader.cache.disc.naming.FileNameGenerator;
import org.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import org.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import org.universalimageloader.cache.memory.MemoryCacheAware;
import org.universalimageloader.cache.memory.impl.FuzzyKeyMemoryCache;
import org.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import org.universalimageloader.core.assist.ImageScaleType;
import org.universalimageloader.core.assist.MemoryCacheUtil;
import org.universalimageloader.core.display.BitmapDisplayer;
import org.universalimageloader.core.display.FadeInBitmapDisplayer;
import org.universalimageloader.core.display.RoundedBitmapDisplayer;
import org.universalimageloader.core.display.SimpleBitmapDisplayer;
import org.universalimageloader.core.download.ImageDownloader;
import org.universalimageloader.core.download.URLConnectionImageDownloader;
import org.universalimageloader.utils.StorageUtils;

import android.content.Context;
import android.graphics.Bitmap;

import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;


/**
 * 默认配置工厂创建类
 * Factory for providing of default options for {@linkplain ImageLoaderConfiguration configuration}
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class DefaultConfigurationFactory {

    /**
     * Create {@linkplain HashCodeFileNameGenerator default implementation} of FileNameGenerator
     */
    public static FileNameGenerator createFileNameGenerator() {
        return new Md5FileNameGenerator();
    }

    public static FileNameGenerator createHtttpNameGenerator() {
        return new HashCodeFileNameGenerator();
    }

    /**
     * 创建一个默认的sd卡缓存方式 Create default implementation of {@link DisckCacheAware} depends on incoming parameters
     */
    public static DiscCacheAware createDiscCache(Context context, FileNameGenerator discCacheFileNameGenerator, int discCacheSize, int discCacheFileCount) {
        if (discCacheSize > 0) {
            File individualCacheDir = StorageUtils.getHealthTalkExternalCacheDir(context);
            return new TotalSizeLimitedDiscCache(individualCacheDir, discCacheFileNameGenerator, discCacheSize);
        } else if (discCacheFileCount > 0) {
            File individualCacheDir = StorageUtils.getHealthTalkExternalCacheDir(context);
            return new FileCountLimitedDiscCache(individualCacheDir, discCacheFileNameGenerator, discCacheFileCount);
        } else {
            File cacheDir = StorageUtils.getHealthTalkExternalCacheDir(context);
            return new UnlimitedDiscCache(cacheDir, discCacheFileNameGenerator);
        }
    }

    /**
     * 创建一个默认的内存缓存方式 Create default implementation of {@link MemoryCacheAware} depends on incoming parameters
     */
    public static MemoryCacheAware<String, Bitmap> createMemoryCache(int memoryCacheSize, boolean denyCacheImageMultipleSizesInMemory) {
        MemoryCacheAware<String, Bitmap> memoryCache = new UsingFreqLimitedMemoryCache(memoryCacheSize);
        if (denyCacheImageMultipleSizesInMemory) {
            memoryCache = new FuzzyKeyMemoryCache<String, Bitmap>(memoryCache, MemoryCacheUtil.createFuzzyKeyComparator());
        }
        return memoryCache;
    }

    /**
     * Create default implementation of {@link ImageDownloader}
     */
    public static ImageDownloader createImageDownloader() {
        return new URLConnectionImageDownloader();
    }

    /**
     * 创建默认的显示方式 Create default implementation of {@link BitmapDisplayer}
     */
    public static BitmapDisplayer createBitmapDisplayer() {
        return new SimpleBitmapDisplayer();
    }

    /**
     * 创建一个默认的缓存目录
     *
     * @param context
     * @return
     */
    public static File createDefaultCacheDir(Context context) {
        File file = StorageUtils.getHealthTalkExternalCacheDir(context);
        return file;
    }

    /**
     * 创建头像图片显示方式
     * @param context
     * @return
     *//*
    public static DisplayImageOptions createHeaderDisplayImageOptions(Context context){
		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.unkonw_head_mankind)
		.showStubImage(R.drawable.unkonw_head_mankind)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.cacheOnDisc(new File(StorageUtils.getHeadersPath()))
		.cacheInMemory()
		.displayer(new RoundedBitmapDisplayer(5))
		.build(context);
		return displayImageOptions;
	}
	
	public static DisplayImageOptions createSexHeaderDisplayImageOptions(String sex,
			Context context) {
		if("1".equalsIgnoreCase(sex)){
			return createMaleHeaderDisplayImageOptions(context);
		}else if("2".equalsIgnoreCase(sex)){
			return createFemaleHeaderDisplayImageOptions(context);
		}else{
			return createUnknowHeaderDisplayImageOptions(context);
		}
		 
	}
	public static DisplayImageOptions createChatMapDisplayImageOptions(Context context){
		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.chat_fail_default_bg)
		.showStubImage(R.drawable.chat_default_bg)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.cacheOnDisc(new File(StorageUtils.getHeadersPath()))
		.cacheInMemory()
		.displayer(new RoundedBitmapDisplayer(5))
		.build(context);
		return displayImageOptions;
	}
	*//**
     * 男头像
     * @param context
     * @return
     *//*
    public static DisplayImageOptions createMaleHeaderDisplayImageOptions(Context context){
		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.default_head_mankind)
		.showStubImage(R.drawable.default_head_mankind)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.cacheOnDisc(new File(StorageUtils.getHeadersPath()))
		.cacheInMemory()
		.displayer(new RoundedBitmapDisplayer(5))
		.build(context);
		return displayImageOptions;
	}
	
	*//**
     * 女头像
     * @param context
     * @return
     *//*
    public static DisplayImageOptions createFemaleHeaderDisplayImageOptions(Context context){
		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.default_head_female)
		.showStubImage(R.drawable.default_head_female)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.cacheOnDisc(new File(StorageUtils.getHeadersPath()))
		.cacheInMemory()
		.displayer(new RoundedBitmapDisplayer(5))
		.build(context);
		return displayImageOptions;
	}
	*//**
     * 未知头像
     * @param context
     * @return
     *//*
	public static DisplayImageOptions createUnknowHeaderDisplayImageOptions(Context context){
		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.unkonw_head_mankind)
		.showStubImage(R.drawable.unkonw_head_mankind)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.cacheOnDisc(new File(StorageUtils.getHeadersPath()))
		.cacheInMemory()
		.displayer(new RoundedBitmapDisplayer(5))
		.build(context);
		return displayImageOptions;
	}
	
	
	*//**
     * 九宫格默认
     * @param context
     * @return
     *//*
	public static DisplayImageOptions createUnknowNineHeaderDisplayImageOptions(Context context){
		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.default_jiugong_icon)
		.showStubImage(R.drawable.default_jiugong_icon)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.cacheOnDisc(new File(StorageUtils.getHeadersPath()))
		.cacheInMemory()
		.displayer(new RoundedBitmapDisplayer(5))
		.build(context);
		return displayImageOptions;
	}
	
	*//**
     * 商品
     * @param context
     * @return
     *//*
	public static DisplayImageOptions createUnknowShopImageOptions(Context context){
		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.store_pic)
		.showStubImage(R.drawable.store_pic)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.cacheOnDisc(new File(StorageUtils.getHeadersPath()))
		.cacheInMemory()
		.displayer(new RoundedBitmapDisplayer(5))
		.build(context);
		return displayImageOptions;
	}
	
	*//**
     * 图片廊显示方式
     * @param context
     * @param str
     * @return
     *//*
	public static DisplayImageOptions createGalleryDisplayImageOptions(Context context){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.waterfall_default)
		.showStubImage(R.drawable.waterfall_default)
		.resetViewBeforeLoading()
		.cacheOnDisc(new File(StorageUtils.getImagePath()))
		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.cacheInMemory()
		.displayer(new FadeInBitmapDisplayer(300))
		.build(context);
		return options;
	}
	
	*//**
     * 二维码
     * @param context
     * @param str
     * @return
     *//*
	public static DisplayImageOptions createQrCodeDisplayImageOptions(Context context){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.cacheOnDisc(new File(StorageUtils.getImagePath()))
		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.cacheInMemory()
		.displayer(new FadeInBitmapDisplayer(300))
		.build(context);
		return options;
	}
	
	*//**
     * 话题头像显示方式
     * @param context
     * @param str
     * @return
     *//*
	public static DisplayImageOptions createSalonDisplayImageOptions(Context context){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.default_head_group)
		.showStubImage(R.drawable.default_head_group)
		.resetViewBeforeLoading()
		.cacheInMemory()
		.cacheOnDisc(new File(StorageUtils.getImagePath()))
		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new RoundedBitmapDisplayer(5))
		.build(context);
		return options;
	}
	
	public static DisplayImageOptions createPrizeDraw(){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.resetViewBeforeLoading()
		.showImageForEmptyUri(R.drawable.default_head_group)
		.showStubImage(R.drawable.default_head_group)
		.cacheInMemory()
		.cacheOnDisc(new File(StorageUtils.getImagePath()))
		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new RoundedBitmapDisplayer(5))
		.build(HTalkApplication.getHTalkApplication());
		return options;
	}
	
	*//**
     *
     * 建筑默认图
     * @param context
     * @return
     *//*
	public static DisplayImageOptions createBuildDisplayImageOption(Context context){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.resetViewBeforeLoading()
		.cacheOnDisc(new File(StorageUtils.getImagePath()))
		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.cacheInMemory()
		.build(context);
		return options;
	}

	*//**
     * 话题头像显示方式
     * @param context
     * @param str
     * @return
     *//*
	public static DisplayImageOptions createServerDisplayImageOptions(Context context,int id){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(id)
		.showStubImage(id)
		.cacheInMemory()
		.resetViewBeforeLoading()
		.cacheOnDisc(new File(StorageUtils.getImagePath()))
		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(300))
		.build(context);
		return options;
	}
	
	*/

    /**
     * 新闻
     *
     * @param context
     * @param str
     * @return
     *//*
	public static DisplayImageOptions createNewSDisplayImageOptions(Context context,int id){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(id)
		.cacheInMemory()
		.cacheOnDisc(DefaultConfigurationFactory.createDefaultCacheDir(context))
		.showStubImage(R.drawable.waterfall_default)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.imageScaleType(ImageScaleType.EXACTLY)
		.displayer(DefaultConfigurationFactory.createBitmapDisplayer())
		.build(context);
		return options;
	}*/
    public static DisplayImageOptions createPrizeDraw(Context context) {
        DisplayImageOptions options = new DisplayImageOptions.Builder(context)
                .resetViewBeforeLoading()
                .showStubImage(R.drawable.chat_default_bg)
                .cacheInMemory()
                .cacheOnDisc(new File(StorageUtils.getImagePath()))
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(5))
                .build();
        return options;
    }

    public static DisplayImageOptions createCircularPrizeDraw(Context context) {
        DisplayImageOptions options = new DisplayImageOptions.Builder(context)
                .resetViewBeforeLoading()
                .showStubImage(R.drawable.chat_default_bg)
                .cacheInMemory()
                .cacheOnDisc(new File(StorageUtils.getImagePath()))
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(360))
                .build();
        return options;
    }

    public static DisplayImageOptions createUnknowShopImageOptions(Context context) {
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder(context)
                .showStubImage(R.drawable.store_pic)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheOnDisc(new File(StorageUtils.getHeadersPath()))
                .cacheInMemory()
                .displayer(new RoundedBitmapDisplayer(5))
                .build();
        return displayImageOptions;
    }

    public static DisplayImageOptions createUnknowNineHeaderDisplayImageOptions() {
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder(HTalkApplication.getApplication())
                .showStubImage(R.drawable.default_jiugong_icon)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheOnDisc(new File(StorageUtils.getHeadersPath()))
                .cacheInMemory()
                .displayer(new RoundedBitmapDisplayer(5))
                .build();
        return displayImageOptions;
    }

    public static DisplayImageOptions createChatMapDisplayImageOptions(Context context) {
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder(context)
                .showStubImage(R.drawable.chat_default_bg)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheOnDisc(new File(StorageUtils.getHeadersPath()))
                .cacheInMemory()
                .displayer(new RoundedBitmapDisplayer(5))
                .build();
        return displayImageOptions;
    }

    /**
     * 新闻
     *
     * @param context
     * @param str
     * @return
     */
    public static DisplayImageOptions createNewSDisplayImageOptions(Context context, int id) {
        DisplayImageOptions options = new DisplayImageOptions.Builder(context)
                .showStubImage(id)
                .cacheInMemory()
                .showImageForEmptyUri(R.drawable.load_result_is_null_big)
                .cacheOnDisc(new File(StorageUtils.getImagePath()))
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(5))
                .build();
        return options;
    }

    public static DisplayImageOptions createServerDisplayImageOptions(Context context) {
        DisplayImageOptions options = new DisplayImageOptions.Builder(context)
                .cacheInMemory()
                .resetViewBeforeLoading()
                .cacheOnDisc(new File(StorageUtils.getImagePath()))
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(5))
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        return options;
    }

    public static DisplayImageOptions createGalleryDisplayImageOptions(Context context) {
        DisplayImageOptions options = new DisplayImageOptions.Builder(context)
                .showStubImage(R.drawable.waterfall_default)
                .cacheOnDisc(new File(StorageUtils.getImagePath()))
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(5))
                .cacheInMemory()
                .build();
        return options;
    }

    public static DisplayImageOptions createVideoDisplayImageOptions(Context context) {
        DisplayImageOptions options = new DisplayImageOptions.Builder(context)
                .showStubImage(R.drawable.video_btn_error)
                .cacheOnDisc(new File(StorageUtils.getImagePath()))
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(5))
                .cacheInMemory()
                .build();
        return options;
    }

    public static DisplayImageOptions PublicNumberDisplayImageOptions(Context context) {
        DisplayImageOptions options = new DisplayImageOptions.Builder(context)
                .cacheOnDisc(new File(StorageUtils.getImagePath()))
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(5))
                .cacheInMemory()
                .build();
        return options;
    }

    public static DisplayImageOptions createDynamicMesDisplayImageOptions(Context context) {
        DisplayImageOptions options = new DisplayImageOptions.Builder(context)
                .showStubImage(R.drawable.waterfall_default)
                .cacheOnDisc(new File(StorageUtils.getImagePath()))
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(5))
                .cacheInMemory()
                .build();
        return options;
    }

    public static DisplayImageOptions createApplyPicDisplayImageOptions(Context context) {
        DisplayImageOptions options = new DisplayImageOptions.Builder(context)
                .showStubImage(R.drawable.waterfall_default)
                .cacheOnDisc(new File(StorageUtils.getImagePath()))
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(1))
                .cacheInMemory()
                .build();
        return options;
    }

    public static DisplayImageOptions createHeadDisplayImageOptions(Context context) {
        DisplayImageOptions options = new DisplayImageOptions.Builder(context)
                .showStubImage(R.drawable.default_head_female)
                .cacheOnDisc(new File(StorageUtils.getImagePath()))
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(5))
                .cacheInMemory()
                .build();
        return options;
    }


}
