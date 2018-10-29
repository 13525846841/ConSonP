package org.universalimageloader.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

import com.yksj.consultation.son.app.HTalkApplication;

/**
 * 存储路径
 * Provides application storage paths
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public final class StorageUtils {

	private static final String INDIVIDUAL_DIR_NAME = "uil-images";

	private StorageUtils() {
		
	}

	/**
	 * Returns application cache directory. Cache directory will be created on SD card
	 * <i>("/Android/data/[app_package_name]/cache")</i> if card is mounted. Else - Android defines cache directory on
	 * device's file system.
	 * 
	 * @param context
	 *            Application context
	 * @return Cache {@link File directory}
	 */
/*	public static File getCacheDirectory(Context context) {
		File appCacheDir = null;
		if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			appCacheDir = getExternalCacheDir(context);
		}
		if (appCacheDir == null) {
			appCacheDir = context.getCacheDir();
		}
		return appCacheDir;
	}*/
	
	/*
	*//**
	 * 用户头像缓存目录
	 * @param context
	 * @return
	 *//*
	public static File getHeaderCacheDir(Context context,String userStrMd5){
		if(isSDMounted(context)){
			userStrMd5 = userStrMd5+"/headers/";
			return getHealthTalkExternalCacheDir(context,userStrMd5);
		}
		return null;
	}
	
	*//**
	 * 
	 * 图片缓存目录
	 * @param context
	 * @return
	 *//*
	public static File getImagesCacheDir(Context context,String userStrMd5){
		if(isSDMounted(context)){
			userStrMd5 = userStrMd5+"/images/";
			return getHealthTalkExternalCacheDir(context,userStrMd5);
		}
		return null;
	}*/
	
	public static File createMapsFile(){
		if(isSDMounted()){
			String path = getMapsPath();
			File dir = createRootFileDir(path);
			try {
				UUID uid = UUID.randomUUID();
				dir = File.createTempFile(uid.toString(),".jpg",dir);
				return dir;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	
	/**
	 * 创建文件
	 * @param context
	 * @param userMD5Str
	 * @throws IOException 
	 */
	public static File createImageFile() throws IOException{
		String path = getImagePath();
		File dir = createRootFileDir(path);
		UUID uid = UUID.randomUUID();
		dir = File.createTempFile(uid.toString(),".jpg",dir);
		return dir;
	}

	/**
	 * 创建图片文件
	 * @throws IOException
	 */
	public static File createPhotoFile() throws IOException{
		String path = getPhotoPath();
		File dir = createRootFileDir(path);
		UUID uid = UUID.randomUUID();
		dir = File.createTempFile(uid.toString(),".jpg",dir);
		return dir;
	}
	
	/**
	 *  根据名称创建图片文件
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static File createImageFileByName(String filename) throws IOException{
		String path = getImagePath();
		File dir = createRootFileDir(path);
		dir = File.createTempFile(filename,".jpg",dir);
		return dir;
	}
	
	/**
	 * 照相默认的存储路径
	 * @param context
	 * @param userStrMd5
	 * @return
	 */
	public static File createCameraFile(){
		if(isSDMounted()){
			String path = getCameraPath();
			File dir = createRootFileDir(path);
			try {
				UUID uid = UUID.randomUUID();
				dir = File.createTempFile(uid.toString(),".jpg",dir);
				return dir;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 二维码默认地址
	 * @return
	 */
	public static File createQrFile(){
		if(isSDMounted()){
			String path = getQrPath();
			File dir = new File(path);
			if(!dir.exists()){
				dir.mkdirs();
			}
			try {
				UUID uid = UUID.randomUUID();
				dir = File.createTempFile(uid.toString(),".png",dir);
				return dir;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 头像
	 * @param context
	 * @param userStrMd5
	 * @param fileName
	 * @return
	 */
	public static File createHeaderFile()throws Exception{
		String path = getHeadersPath();
		File dir = createRootFileDir(path);
		UUID uid = UUID.randomUUID();
		dir = File.createTempFile(uid.toString(),".jpg",dir);
		return dir;
	}
	
	/**
	 * 语音
	 * @param context
	 * @return
	 * @throws Exception 
	 */
	public static File createVoiceFile() throws Exception{
		String path = getVoicePath();
		File dir = createRootFileDir(path);
		UUID uid = UUID.randomUUID();
		dir = File.createTempFile(uid.toString(),".arm",dir);
		return dir;
	}
	
	
	/**
	 * 创建语音文件
	 * @param name
	 * @return
	 */
	public static File createVoiceFile(String name){
		if(isSDMounted()){
			String path = getVoicePath();
			File dir = createRootFileDir(path);
			File file = new File(dir,name);
			try {
				boolean bl = file.createNewFile();
				if(bl)return file;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 *创建聊天背景文件 
	 * @return
	 */
	public static File createThemeFile(){
		if(isSDMounted()){
			try {
				String path = getThemeCachePath();
				UUID uid = UUID.randomUUID();
				File file = File.createTempFile(uid.toString(),".jpg",new File(path));
				return file;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 *保存bitmap 
	 * @param bitmap
	 * @param tagFile
	 * @return
	 */
	public static boolean saveImageOnImagsDir(Bitmap bitmap,File tagFile){
		FileOutputStream fileOutputStream = null;
		if(bitmap != null && tagFile != null){
			try {
				if(!tagFile.exists()) {
					tagFile.mkdirs();
					tagFile.delete();
				}
				fileOutputStream = new FileOutputStream(tagFile.getAbsolutePath());
				bitmap.compress(CompressFormat.JPEG,100,fileOutputStream);
				fileOutputStream.flush();
				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}catch(IOException e){
				
			}finally{
				if(fileOutputStream != null)
					try {
						fileOutputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		} 
		return false;
	}

	/**
	 * Returns individual application cache directory (for only image caching from ImageLoader). Cache directory will be
	 * created on SD card <i>("/Android/data/[app_package_name]/cache/uil-images")</i> if card is mounted. Else -
	 * Android defines cache directory on device's file system.
	 * 
	 * @param context
	 *            Application context
	 * @return Cache {@link File directory}
	 */
/*	public static File getIndividualCacheDirectory(Context context) {
		File cacheDir = getCacheDirectory(context);
		File individualCacheDir = new File(cacheDir, INDIVIDUAL_DIR_NAME);
		if (!individualCacheDir.exists()) {
			if (!individualCacheDir.mkdir()) {
				individualCacheDir = cacheDir;
			}
		}
		return individualCacheDir;
	}*/

	/**
	 * Returns specified application cache directory. Cache directory will be created on SD card by defined path if card
	 * is mounted. Else - Android defines cache directory on device's file system.
	 * 
	 * @param context
	 *            Application context
	 * @param cacheDir
	 *            Cache directory path (e.g.: "AppCacheDir", "AppDir/cache/images")
	 * @return Cache {@link File directory}
	 */
	public static File getOwnCacheDirectory(Context context, String cacheDir) {
		File appCacheDir = null;
		if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			appCacheDir = new File(Environment.getExternalStorageDirectory(), cacheDir);
		}
		if(appCacheDir == null || (!appCacheDir.exists() && !appCacheDir.mkdirs())){
			appCacheDir = context.getCacheDir();
		}
		return appCacheDir;
	}

	private static File getExternalCacheDir(Context context) {
		File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "universal"), "data");
		File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
		if (!appCacheDir.exists()) {
			try {
				new File(dataDir, ".nomedia").createNewFile();
			} catch (IOException e) {
				L.e(e, "Can't create \".nomedia\" file in application external cache directory");
			}
			if (!appCacheDir.mkdirs()) {
				L.w("Unable to create external cache directory");
				return null;
			}
		}
		return appCacheDir;
	}
	
	/**
	 * 获得项目存储的根目录
	 * @param contextd
	 * @return
	 */
	public static File getHealthTalkExternalCacheDir(Context context){
		String dirName = HTalkApplication.getMd5UserId();
		File dataDir = new File(Environment.getExternalStorageDirectory(),"consultation");
		if(dirName == null)dirName = "test";
		File appCachDir = new File(dataDir,dirName);
		if(!dataDir.exists()){
			try{
				new File(dataDir, ".nomedia").createNewFile();
			}catch(IOException e){
				L.e(e, "Can't create \".nomedia\" file in application external cache directory");
			}
		}
		boolean b = appCachDir.mkdirs();
		if(b){
			try{
				new File(appCachDir, ".nomedia").createNewFile();
			}catch(IOException e){
				L.e(e, "Can't create \".nomedia\" file in application external cache directory");
			}
		}
		return appCachDir;
	}
	
	public static File createRootFileDir(String path){
		File dataDir = new File(path);
		if(!dataDir.exists()){
			boolean b = dataDir.mkdirs();
			if(b){
				try{
					new File(dataDir, ".nomedia").createNewFile();
				}catch(IOException e){
					L.e(e, "Can't create \".nomedia\" file in application external cache directory");
				}
			}
		}
		return dataDir;
	}
	
	
	/**
	 * sd卡状态
	 * @param context
	 * @return
	 */
	public static boolean isSDMounted(){
		return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)?true:false;
	}
	
	public static String getRootPath(String md5Str,String typeName){
		String path = Environment.getExternalStorageDirectory().toString()+"/consultation/"+md5Str+"/"+typeName+"/";
		return path;
	}
	
	/**
	 * 缓存图片
	 * @param md5Str
	 * @return
	 */
	public static String getImagePath(){
		return getRootPath(HTalkApplication.getMd5UserId(),"images");
	}
	/**
	 * 用户操作图片
	 * @return
	 */
	public static String getPhotoPath(){
		return getRootPath(HTalkApplication.getMd5UserId(),"photo");
	}
	
	/**
	 * 语音
	 * @param md5Str
	 * @return
	 */
	public static String getVoicePath(){
		return getRootPath(HTalkApplication.getMd5UserId(), "voices");
	}
	
	/**
	 * 头像
	 * @param md5Str
	 * @return
	 */
	public static String getHeadersPath(){
		return getRootPath(HTalkApplication.getMd5UserId(), "headers");
	}
	
	public static File  getHeaderFileDir(){
		File file = new File(getHeadersPath());
		if(!file.exists()){
			createRootFileDir(file.getAbsolutePath());
		}
		return file;
	}
	
	/**
	 * 相机
	 * @param md5Str
	 * @return
	 */
	public static String getCameraPath(){
		return getRootPath(HTalkApplication.getMd5UserId(),"camera");
	}
	
	/**
	 * 二维码
	 * @param md5Str
	 * @return
	 */
	public static String getQrPath(){
//		return getRootPath(HTalkApplication.getMd5UserId(),"QrCode");
		return  Environment.getExternalStorageDirectory().toString()+"/QrCode/";
	}
	

	/**
	 * 地图
	 * @param md5Str
	 * @return
	 */
	public static String getMapsPath(){
		return getRootPath(HTalkApplication.getMd5UserId(),"maps");
	}
	
	/**
	 * 删除语音文件
	 * @param name
	 */
	public static void deleteVoiceFile(String name){
		try{
			name = getFileName(name);
			File file = new File(getVoicePath(),name);
			file.deleteOnExit();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除图片文件
	 * @param name
	 */
	public static void deleteImageFile(String name){
		try {
			name = getFileName(name);
			File file = new File(getImagePath(),name);
			file.deleteOnExit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 删除地图文件
	 * @param name
	 */
	public static void deleteMapFile(String name){
		try {
			name = getFileName(name);
			File file = new File(getMapsPath(),name);
			file.deleteOnExit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getFileName(String path){
		int separatorIndex = path.lastIndexOf("/");
		path = (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
		return path;
	}
	
	public static String getMIMEType(File file){//获取文件类型
		
		return null;
	}
	
	/**
	 * @return
	 */
	public static String getDownCachePath(){
		String path = getRootPath(HTalkApplication.getMd5UserId(),"downs");
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
			createRootFileDir(path);
		}
		return path;
	}
	
	public static String getThemeCachePath(){
		String path = getRootPath(HTalkApplication.getMd5UserId(),"themes");
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
			createRootFileDir(path);
		}
		return path;
	}
	
	public static String[][] getMIMEFile(){
		String[][] MIME_MapTable={
				 //{后缀名，    MIME类型}
				{".3gp",    "video/3gpp"},
				{".apk",    "application/vnd.android.package-archive"},
				{".asf",    "video/x-ms-asf"},
				{".avi",    "video/x-msvideo"},
				{".bin",    "application/octet-stream"},
				{".bmp",      "image/bmp"},
				{".c",        "text/plain"},
				{".class",    "application/octet-stream"},
				{".conf",    "text/plain"},
				{".cpp",    "text/plain"},
				{".doc",    "application/msword"},
				{".exe",    "application/octet-stream"},
				{".gif",    "image/gif"},
				{".gtar",    "application/x-gtar"},
				{".gz",        "application/x-gzip"},
				{".h",        "text/plain"},
				{".htm",    "text/html"},
				{".html",    "text/html"},
				{".jar",    "application/java-archive"},
				{".java",    "text/plain"},
				{".jpeg",    "image/jpeg"},
				{".jpg",    "image/jpeg"},
				{".js",        "application/x-javascript"},
				{".log",    "text/plain"},
				{".m3u",    "audio/x-mpegurl"},
				{".m4a",    "audio/mp4a-latm"},
				{".m4b",    "audio/mp4a-latm"},
				{".m4p",    "audio/mp4a-latm"},
				{".m4u",    "video/vnd.mpegurl"},
				{".m4v",    "video/x-m4v"},    
				{".mov",    "video/quicktime"},
				{".mp2",    "audio/x-mpeg"},
				{".mp3",    "audio/x-mpeg"},
				{".mp4",    "video/mp4"},
				{".mpc",    "application/vnd.mpohun.certificate"},        
				{".mpe",    "video/mpeg"},    
				{".mpeg",    "video/mpeg"},    
				{".mpg",    "video/mpeg"},    
				{".mpg4",    "video/mp4"},    
				{".mpga",    "audio/mpeg"},
				{".msg",    "application/vnd.ms-outlook"},
				{".ogg",    "audio/ogg"},
				{".pdf",    "application/pdf"},
				{".png",    "image/png"},
				{".pps",    "application/vnd.ms-powerpoint"},
				{".ppt",    "application/vnd.ms-powerpoint"},
				{".prop",    "text/plain"},
				{".rar",    "application/x-rar-compressed"},
				{".rc",        "text/plain"},
				{".rmvb",    "audio/x-pn-realaudio"},
				{".rtf",    "application/rtf"},
				{".sh",        "text/plain"},
				{".tar",    "application/x-tar"},    
				{".tgz",    "application/x-compressed"}, 
				{".txt",    "text/plain"},
				{".wav",    "audio/x-wav"},
				{".wma",    "audio/x-ms-wma"},
				{".wmv",    "audio/x-ms-wmv"},
				{".wps",    "application/vnd.ms-works"},
				//{".xml",    "text/xml"},
				{".xml",    "text/plain"},
				{".z",        "application/x-compress"},
				{".zip",    "application/zip"},
				{"",        "*/*"}       
		};
		return MIME_MapTable;
	}
	
}
