package com.yksj.healthtalk.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.healthtalk.net.socket.SmartFoxClient;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FileUtils {
	public static String SDPATH = Environment.getExternalStorageDirectory()+ "/formats/";
	// private static final String TAG = "FileUtils";
	/**
	 * 拍照临时文件
	 */
	private static final String PHOTO_PATH = "/consultation/camera";

	// public static final String MIMETYPE_JPG = ".jpg";
	/**
	 * 语音文件路径
	 */
	public static final String PROJECT_ROOT_PATH = "/consultation/";


	public static final String MIMETYPE_PNG = ".png";
	/**
	 *
	 *
	 * 创建一个唯一的id
	 * 
	 * @return
	 */
/*	public static String getUUId() {
		return UUID.randomUUID().toString();
	}*/

	/*
	 * 相册保存路径
	 */
	// public static File getCameraRootPath(){
	// File file = new File(SystemUtils.getScdRootPath()+"/Camera");
	// if(!file.exists()){
	// file.mkdirs();
	// }
	// return file;
	// }
	/**
	 * 获得拍照临时文件地址
	 * 
	 * @return
	 */
	// public static File getPhotoRootPath() {
	// File file = new File(SystemUtils.getScdRootPath() + PHOTO_PATH);
	// if (!file.exists()) {
	// file.mkdirs();
	// createNomediaFile(file);
	// }
	// File file2 = new File(file, String.valueOf(System.currentTimeMillis()));
	// return file2;
	// }

	/***
	 * 创建一个空的语音文件
	 * 
	 * @return
	 */
	// public static File createVoiceFile() {
	// UUID uid = UUID.randomUUID();
	// String path = FileUtils.getVoiceRootPath();
	// try {
	// File rooFile = new File(path);
	// if(!rooFile.exists()){
	// rooFile.mkdirs();
	// createNomediaFile(rooFile);
	// }
	// rooFile = File.createTempFile(String.valueOf(uid.toString()),
	// ".am", rooFile);
	// return rooFile;
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (SecurityException e) {
	// e.printStackTrace();
	// } catch (IllegalArgumentException e) {
	// e.printStackTrace();
	// }
	// return null;
	// }

	/**
	 * 保存bitmap到文件
	 * 
	 * @param bitmap
	 * @param suffix
	 *            后缀名 为null时候以.为后缀
	 * @return
	 */
	 public static File saveChatPhotoBitmapToFile(Bitmap bitmap) {
	 if (bitmap == null)
	 return null;
	 UUID uuid = UUID.randomUUID();
	 String path = SystemUtils.getScdRootPath() + PROJECT_ROOT_PATH
	 + SmartFoxClient.getSmartFoxClient().getUserName();
	 File file = new File(path);
	 if(!file.exists()){
	 file.mkdirs();
	 createNomediaFile(file);
	 }
	 FileOutputStream fileOutputStream = null;
	 try {
	 file = File.createTempFile(uuid.toString(), MIMETYPE_PNG, file);
	 fileOutputStream = new FileOutputStream(file);
	 bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fileOutputStream);
	 } catch (IOException e) {
	 file = null;
	 } finally {
	 try {
	 if (fileOutputStream != null) {
	 fileOutputStream.flush();
	 fileOutputStream.close();
	 }
	 if (bitmap != null && !bitmap.isRecycled())
	 bitmap.recycle();
	 } catch (IOException e) {
	 }
	 }
	 return file;
	 }

	/**
	 * 创建一个空文件用于存放聊天中的图片
	 * 
	 * @return
	 */
	// public static File createChatImageFile() {
	// UUID uuid = UUID.randomUUID();
	// String path = FileUtils.getImageRootPath();
	// File file = null;
	// try {
	// file = new File(path);
	// if(!file.exists()){
	// file.mkdirs();
	// createNomediaFile(file);
	// }
	// file = File.createTempFile(uuid.toString(), MIMETYPE_JPG, file);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (IllegalArgumentException e) {
	// e.printStackTrace();
	// }
	// return file;
	// }

	/**
	 * 
	 * 保存用户头像图片
	 * 
	 * @param bmp
	 * @param customerId
	 * @return
	 */
	// public static String saveBitmap(Bitmap bmp) {
	// String rootPath = FileUtils.getHeaderRootPath();
	// File rootFile = new File(rootPath);
	// FileOutputStream output = null;
	// try {
	// if(!rootFile.exists()){
	// rootFile.mkdirs();
	// createNomediaFile(rootFile);
	// }
	// File file = new File(rootFile, "/head.jpg");
	// output = new FileOutputStream(file);
	// bmp.compress(CompressFormat.JPEG, 80, output);
	// return file.getAbsolutePath();
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch (SecurityException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (output != null)
	// output.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// return null;
	// }

	/**
	 * 将图片保存在本地
	 * 
	 * @param bm
	 */
	// public static boolean saveImageLocation(Bitmap bm) {
	// String rootPath = FileUtils.getHeaderRootPath();
	// FileOutputStream b = null;
	// File file = new File(rootPath);
	// File fileName = null;
	// if (!file.exists()) {
	// file.mkdirs();
	// createNomediaFile(file);
	// }
	// fileName = new File(file, System.currentTimeMillis() + ".jpg");
	// try {
	// b = new FileOutputStream(fileName);
	// bm.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
	// return true;
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// b.flush();
	// b.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// return false;
	// }

	/**
	 * 图片保存
	 * 
	 * @param bm
	 * @param http
	 *            Bitmap bitmap = BitmapUtils.scaleBitmap(file,
	 *            BitmapUtils.POTO_MAX_SILDE_SIZE);
	 * @return
	 */
	// public static File FileSaveImage(Bitmap bm, String contentName) {
	// FileOutputStream b = null;
	// File file = new File(FileUtils.getHeaderRootPath() + contentName);
	// File fileName = null;
	// if (!file.exists()) {
	// file.mkdirs();
	// createNomediaFile(file);
	// }
	// fileName = new File(file, System.currentTimeMillis() + ".jpg");
	// try {
	// b = new FileOutputStream(fileName);
	// bm.compress(Bitmap.CompressFormat.JPEG, 80, b);
	// return fileName;
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (b != null) {
	// b.flush();
	// b.close();
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// return fileName;
	// }
	/**
	 * 照片保存
	 */
	// public static void FileSavePhoto(Bitmap bm, String contentName) {
	// FileOutputStream b = null;
	// File file = new File(FileUtils.getHeaderRootPath());
	// File fileName = null;
	// if (!file.exists()) {
	// file.mkdirs();
	// createNomediaFile(file);
	// }
	// fileName = new File(file, FileUtils.getFileName(contentName));
	// try {
	// b = new FileOutputStream(fileName);
	// bm.compress(Bitmap.CompressFormat.JPEG, 100, b);
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (b != null) {
	// b.flush();
	// b.close();
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	/**
	 * 保存文件
	 * 
	 * @param inputStream
	 *            文件输入流
	 * @return 返回文件保存的绝对路径
	 */
	public static String saveFileForInputStream(InputStream inputStream) {
		File rooFile = new File(PROJECT_ROOT_PATH);
		if (!rooFile.exists()) {
			rooFile.mkdirs();
			createNomediaFile(rooFile);
		}
		FileOutputStream fileInputStream = null;
		try {
			rooFile = File.createTempFile(
					String.valueOf(System.currentTimeMillis()), ".", rooFile);
			fileInputStream = new FileOutputStream(rooFile);

			byte[] bytes = new byte[8192];
			int lengths;
			while ((lengths = inputStream.read(bytes)) > 0) {
				fileInputStream.write(bytes, 0, lengths);
			}

			return rooFile.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.flush();
					fileInputStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;

	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 删除失败返回false
	 */
	public static boolean deleteFileByPath(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
			return true;
		}
		return false;
	}

	/**
	 * 设置头像
	 * 
	 * @param context
	 * @param image
	 * @param savepath
	 *            图片路径
	 * @param isGroup
	 *            是不是群图片 true 是
	 * @param sex
	 *            性别
	 */
	public static void setImage(Context context, ImageView image, String path,
			boolean isGroup, String sex) {
		if (image == null)
			return;
		int defut_header;
		if (isGroup) {
			defut_header = R.drawable.default_head_group;
		} else {
			if ("W".equals(sex) || "w".equals(sex))
				defut_header = R.drawable.default_head_female;
			else
				defut_header = R.drawable.default_head_mankind;
		}
		if (path == null || "".equals(path) || "NOPHOTO".equals(path)) {
			image.setImageResource(defut_header);
			return;
		}

		if (path.contains("assets")) {
			if (!isGroup) {
				image.setImageResource(defut_header);
				return;
			}
		}
		Bitmap bitmap = pathToDrawable(context, path);
		if (bitmap != null)
			image.setImageDrawable(ImageUtils.setShadow(context, bitmap));
		else
			image.setImageResource(getDefultHead(isGroup, sex));
	}

	/**
	 * 获得默认头像
	 * 
	 * @param isGroup
	 *            是不是群图片 true 是
	 * @param sex
	 *            性别
	 * @return
	 */
	public static int getDefultHead(boolean isGroup, String sex) {
		if (isGroup)
			return R.drawable.default_head_group;
		else {
			if ("W".equals(sex))
				return R.drawable.default_head_female;
			else
				return R.drawable.default_head_mankind;
		}
	}

	private static InputStream pathToStream(Context context, String savepath) {
		if (savepath == null || "".equals(savepath)) {
			return null;
		}
		String path = savepath.substring(savepath.indexOf("/") + 1);
		InputStream in = null;
		// 系统头像
		if (savepath.contains("assets")) {
			try {
				in = context.getAssets().open(path);
			} catch (IOException e) {
				return null;
			}
		}

		// 自定义头像
		if (!savepath.contains("assets")) {
			try {
				ImageUtils.updateFileTime(savepath);
				in = new FileInputStream(new File(savepath));
			} catch (FileNotFoundException e) {
				return null;
			}
		}
		return in;
	}

	/**
	 * 根据路径从assets中获得drawable
	 * 
	 * @param context
	 * @param savepath
	 *            图片路径
	 * @return
	 */
	public static Bitmap pathToDrawable(Context context, String savepath) {
		Bitmap bitmap = null;
		InputStream in = pathToStream(context, savepath);
		if (in != null) {
			bitmap = BitmapFactory.decodeStream(in);
		}
		return bitmap;
	}

	/**
	 * 根据路径从assets中获得Bitmap
	 * 
	 * @param context
	 * @param savepath
	 *            图片路径
	 * @return
	 */
	public static Bitmap pathToBitmap(Context context, String savepath) {
		Bitmap b = null;
		InputStream in = pathToStream(context, savepath);
		if (in != null) {
			b = BitmapFactory.decodeStream(in);
		}
		return b;
	}

	/**
	 * 保存bitmap到文件
	 * 
	 * @param bitmap
	 * @param suffix
	 *            后缀名 为null时候以.为后缀
	 * @return
	 */
	// public static File saveChatPhotoBitmapToFile(Bitmap bitmap, String
	// suffix) {
	// if (bitmap == null)
	// return null;
	// UUID uuid = UUID.randomUUID();
	// File file = new File(getImageRootPath());
	// if(!file.exists()){
	// file.mkdirs();
	// createNomediaFile(file);
	// }
	// FileOutputStream fileOutputStream = null;
	// try {
	// file = File.createTempFile(uuid.toString(),
	// suffix == null ? MIMETYPE_JPG : suffix, file);
	// fileOutputStream = new FileOutputStream(file);
	// bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fileOutputStream);
	// } catch (IOException e) {
	// file = null;
	// } finally {
	// try {
	// if (fileOutputStream != null) {
	// fileOutputStream.flush();
	// fileOutputStream.close();
	// }
	// if (bitmap != null && !bitmap.isRecycled())
	// bitmap.recycle();
	// } catch (IOException e) {
	// }
	// }
	// return file;
	// }

	/**
	 * 保存地图切图
	 * 
	 * @param bitmap
	 * @return 保存失败返回null
	 */
	// public static File saveChatMapImage(Bitmap bitmap) {
	// if (bitmap == null)
	// return null;
	// String path = getMapRootPath();
	// UUID uuid = UUID.randomUUID();
	// File fileDirectory = new File(path);
	// FileOutputStream fileOutputStream = null;
	// File file = null;
	// try {
	// if(!fileDirectory.exists()){
	// fileDirectory.mkdirs();
	// createNomediaFile(fileDirectory);
	// }
	// file = File.createTempFile(uuid.toString(), ".jpg", fileDirectory);
	// fileOutputStream = new FileOutputStream(file);
	// bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (SecurityException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (fileOutputStream != null) {
	// fileOutputStream.flush();
	// fileOutputStream.close();
	// }
	// if (bitmap != null && !bitmap.isRecycled())
	// bitmap.recycle();
	// bitmap = null;
	// } catch (IOException e) {
	// }
	// }
	// return file;
	// }

	/**
	 * 项目资源存放根路径
	 * 
	 * @return
	 */
	// public static String getRootProjectPath() {
	// String path = SystemUtils.getScdRootPath() + "/consultation/";
	// createNomediaFile(new File(path));
	// return path;
	// }
	//
	// public static String getProjectPathForRoot(){
	// String path = SystemUtils.getScdRootPath() + "/healthtalk";
	// return path;
	// }

	/**
	 * 
	 * 头像根路径
	 * 
	 * @return
	 */
	// public static String getHeaderRootPath() {
	// return getRootProjectPath() +
	// SmartFoxClient.getSmartFoxClient().getUserId() + "/headers/";
	// }

	/**
	 * 语音根路径
	 * 
	 * @return
	 */
	// public static String getVoiceRootPath() {
	// return getRootProjectPath() + "resource/chat/" +
	// SmartFoxClient.getSmartFoxClient().getUserId()
	// + "/sounds/";
	// }

	/**
	 * 图片根路径
	 * 
	 * @return
	 */
	// public static String getImageRootPath() {
	// return getRootProjectPath() + "resource/chat/" +
	// SmartFoxClient.getSmartFoxClient().getUserId()
	// + "/image/";
	// }

	/**
	 * 地图根路径
	 * 
	 * @return
	 */
	// public static String getMapRootPath() {
	// return getRootProjectPath() + "resource/chat/" +
	// SmartFoxClient.getSmartFoxClient().getUserId()
	// + "/maps/";
	// }

	// public static void createNomediaForRootPath(){
	// if(SystemUtils.getScdExit()){
	// String rootPath = getRootProjectPath();
	// File dir = new File(rootPath);
	// if(!dir.exists())dir.mkdirs();
	// createNomediaFile(dir);
	// }
	// }

	/**
	 * 获得文件的文件名
	 * 
	 * @param url
	 * @return
	 */
	public static String getFileName(String url) {
		String[] fileStr = url.split("/");
		String fileName = fileStr[fileStr.length - 1];
		return fileName;
	}

	/**
	 * 创建空的文件用于隐藏多媒体文件不被系统搜索到
	 * 
	 * @param dir
	 */
	public static void createNomediaFile(File dir) {
		try {
			new File(dir, ".nomedia").createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 获得头像目录
	 * 
	 * @param url
	 * @return
	 */
	// public static File getHeaderFile(String url) {
	// String headerUrl = getFileName(url);
	// File fileDir = new File(FileUtils.getHeaderRootPath());
	// File file = null;
	// if (fileDir != null) {
	// if (!fileDir.exists()) {
	// fileDir.mkdirs();
	// }
	// file = new File(fileDir, headerUrl);
	// }
	// return file;
	// }

	// public static void deleteVoiceFile(String str) {
	// if (SystemUtils.getScdExit()) {
	// try {
	// File file = new File(FileUtils.getRootProjectPath() + str);
	// if (file.exists())
	// file.delete();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// }

	// public static void deleteImageFile(String str) {
	// if (SystemUtils.getScdExit()) {
	// String[] path = str.split("&");
	// try {
	// if (path != null && path.length >= 2) {
	// File file = new File(FileUtils.getRootProjectPath()
	// + path[0]);
	// File file2 = new File(FileUtils.getRootProjectPath()
	// + path[1]);
	// if (file.exists())
	// file.delete();
	// if (file2.exists())
	// file2.delete();
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// }

	/**
	 * 删除地图文件
	 * 
	 * @param str
	 */
	// public static void deleteMapFile(String str) {
	// if (SystemUtils.getScdExit()) {
	// try {
	// File file = new File(FileUtils.getMapRootPath() + str);
	// if (file.exists())
	// file.delete();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// }

	/**
	 * 保存已经看过的新闻id
	 */
	public static void writeNewsLookedIds(HashMap<String, String> map,
			Context context) {
		final String filePath = "news_ids_"
				+ SmartFoxClient.getSmartFoxClient().getUserName();
		FileOutputStream fileOutputStream = null;
		ObjectOutputStream outputStream = null;
		try {
			fileOutputStream = context.openFileOutput(filePath,
					Context.MODE_PRIVATE);
			outputStream = new ObjectOutputStream(fileOutputStream);
			outputStream.writeObject(map);
			outputStream.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (outputStream != null)
					outputStream.close();
				if (fileOutputStream != null)
					fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读取已经看过的新闻id
	 * 
	 * @param map
	 *            HashMap<String,String> 新闻id,null
	 */
	public static HashMap<String, String> readNewsLookedIds(Context context) {
		final String filePath = "news_ids_"
				+ SmartFoxClient.getSmartFoxClient().getUserName();
		HashMap<String, String> map = new HashMap<String, String>();
		FileInputStream fileInputStream = null;
		ObjectInputStream inputStream = null;
		try {
			fileInputStream = context.openFileInput(filePath);
			inputStream = new ObjectInputStream(fileInputStream);
			HashMap<String, String> mapObj = (HashMap<String, String>) inputStream
					.readObject();
			if (mapObj != null)
				map = mapObj;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
				if (fileInputStream != null)
					fileInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	/**
	 * 删除新闻id集合文件
	 * 
	 * @param context
	 */
	public static void deleteNewsLookdIdsFile(Context context) {
		context.deleteFile("news_ids");
	}

	/**
	 * 保存聊天背景地址
	 * 
	 * @param filePath
	 * @param context
	 */
	public static void saveBackground(String fileName,
			HashMap<String, String> map, Context context) {
		FileOutputStream fileOutputStream = null;
		ObjectOutputStream outputStream = null;
		try {
			fileOutputStream = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			outputStream = new ObjectOutputStream(fileOutputStream);
			outputStream.writeObject(map);
			outputStream.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (outputStream != null)
					outputStream.close();
				if (fileOutputStream != null)
					fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 聊天对象id
	 * 
	 * @param chatId
	 * @param context
	 * @return 获得背景图片的地址
	 */
	public static HashMap<String, String> getBackgroundHashmap(String chatId,
			String fileName, Context context) {
		FileInputStream fileInputStream = null;
		ObjectInputStream inputStream = null;
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			fileInputStream = context.openFileInput(fileName);
			inputStream = new ObjectInputStream(fileInputStream);
			HashMap<String, String> mapObj = (HashMap<String, String>) inputStream
					.readObject();
			if (mapObj != null)
				map = mapObj;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
				if (fileInputStream != null)
					fileInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	/**
	 * 
	 * 删除聊天背景图片
	 * 
	 * @param chatid
	 * @param context
	 */
	public static void deleteOrUpdateChatBackground(String chatId,
			String filePath, Context context) {
		final String fileName = "chat_bgconfig_"
				+ SmartFoxClient.getSmartFoxClient().getUserName();
		HashMap<String, String> map = getBackgroundHashmap(chatId, fileName,
				context);
		if (filePath == null) {
			map.remove(chatId);
		} else {
			map.put(chatId, filePath);
		}
		saveBackground(fileName, map, context);
	}

	/**
	 * 获得聊天背景图片地址
	 * 
	 * @param chatId
	 * @param context
	 * @return
	 */
	public static String getChatBackgroundPath(String chatId, Context context) {
		final String fileName = "chat_bgconfig_"
				+ SmartFoxClient.getSmartFoxClient().getUserId();
		HashMap<String, String> map = getBackgroundHashmap(chatId, fileName,
				context);
		if (map == null)
			return null;
		return map.get(chatId);
	}

	/**
	 * 清除缓存
	 */
	// public static void clearAllCache(){
	// String path = getProjectPathForRoot();
	// File file = new File(path);
	// deleteDir(file);
	// }

	private static boolean deleteDir(File dir) {
		if (dir.isDirectory() && dir.getName().toString() != "camera") {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	public static File uriToFile(Uri uri, ContentResolver resolver) {
		String scheme = uri.getScheme();
		File mFile = null;
		if ("content".equals(scheme)) {
			Cursor cursor = resolver.query(uri, null, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				int index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				String path = cursor.getString(index);
				mFile = new File(path);
				return mFile;
			}
		} else if ("file".equals(scheme)) {
			try {
				mFile = new File(new URI(uri.getPath()));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		return mFile;
	}

	/**
	 * 兴趣墙图片是否收藏
	 * 
	 * @param id
	 * @return
	 */
	public static boolean isInterestCollection(String id) {
		Object object = null;
		try {
			object = readObjectForFile(getInterestColletionName());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<String> list = null;
		if (object == null) {
			list = new ArrayList<String>();
		} else {
			list = (List) object;
		}
		return list.contains(id);
	}

	/**
	 * 
	 * 更新兴趣墙收藏id
	 * 
	 * @param id
	 * @param isCollection
	 */
	public static void updateInterestCollection(String id, boolean isCollection) {
		Object object = null;
		try {
			object = readObjectForFile(getInterestColletionName());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<String> list = null;
		if (object == null) {
			list = new ArrayList<String>();
		} else {
			list = (ArrayList<String>) object;
		}
		if (isCollection) {
			list.add(id);
		} else {
			list.remove(id);
		}
		try {
			saveObjectToFile(list, getInterestColletionName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 兴趣墙保存
	 * 
	 * @return
	 */
	private static String getInterestColletionName() {
		String md5Str = SmartFoxClient.getSmartFoxClient().getUserMd5Id();
		md5Str += SharePreUtils.INTEREST_COLLECTION;
		return md5Str;
	}

	/**
	 * 
	 * 更新服务中心新闻收藏id
	 * 
	 * @param id
	 * @param isCollection
	 */
	public static void updateNewsCollection(String id, boolean isCollection) {
		Object object = null;
		List<String> list = null;
		try {
			object = readObjectForFile(getNewsColletionName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (object == null) {
			list = new ArrayList<String>();
		} else {
			list = (List<String>) object;
		}
		if (isCollection) {
			list.add(id);
		} else {
			list.remove(id);
		}
		try {
			saveObjectToFile(list, getInterestColletionName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 获取新闻已读id集合
	 * @return
	 * 
	 */
	public static  HashMap<String,String> fatchReadedNews(){
		HashMap<String,String> map;
		final String name = getNewsReadedsCacheFileName();
		try {
			map = (HashMap<String,String>)readObjectForFile(name);
			if(map != null) return map;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return map = new HashMap<String, String>();
	}
	
	/**
	 * 更新新闻已读缓存id
	 * @param map
	 */
	public static void updateReadedNewIds(HashMap<String,String> map){
		if(map == null) return;
		final String name = getNewsReadedsCacheFileName();
		try {
			saveObjectToFile(map,name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * 获取动态消息已读id集合
	 * @return
	 * 
	 */
	public static  HashMap<String,String> fatchReadedDynMes(){
		HashMap<String,String> map;
		final String name = getDynamicReadedsCacheFileName();
		try {
			map = (HashMap<String,String>)readObjectForFile(name);
			if(map != null) return map;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return map = new HashMap<String, String>();
	}
	/**
	 * 已读新闻id文件名称
	 * @return
	 */
	private static String getNewsReadedsCacheFileName(){
		String md5Str = SmartFoxClient.getSmartFoxClient().getUserMd5Id();
		md5Str += SharePreUtils.NEWS_READED_IDS_CACHE;
		return md5Str;
	}
	/**
	 * 已读动态资讯id文件名称
	 * @return
	 */
	private static String getDynamicReadedsCacheFileName(){
		String md5Str = SmartFoxClient.getSmartFoxClient().getUserMd5Id();
		md5Str += SharePreUtils.DYNMES_READED_IDS_CACHE;
		return md5Str;
	}
	/**
	 * 更新动态资讯已读缓存id
	 * @param map
	 */
	public static void updateReadedDynMesIds(HashMap<String,String> map){
		if(map == null) return;
		final String name = getDynamicReadedsCacheFileName();
		try {
			saveObjectToFile(map,name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 邀请查询缓存
	 * @return
	 */
	public static List<String> fatchSearchCacheList(){
		List<String> list = null;
		try {
			list = (List<String>)readObjectForFile(getSearchCacheName());
			if(list == null) list = new ArrayList<String>();
			return list;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return  new ArrayList<String>();
	}
	/**
	 * 邀请更新查询缓存
	 * @return
	 */
	public static void updateSearchCacheList(List<String> list){
		try {
			saveObjectToFile(list,getSearchCacheName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private static String getSearchCacheName(){
		String md5Str = SmartFoxClient.getSmartFoxClient().getUserMd5Id();
		return md5Str += "invite_search.cach";
	}
	/**
	 * 服务中心新闻收藏文件名称
	 * @return
	 */
	private static String getNewsColletionName() {
		String md5Str = SmartFoxClient.getSmartFoxClient().getUserMd5Id();
		md5Str += SharePreUtils.SERVER_NEWS_COLLECTION;
		return md5Str;
	}

	/**
	 * 从文件中读取对象
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object readObjectForFile(String file) throws IOException,
			ClassNotFoundException {
		FileInputStream inputStream = null;
		ObjectInputStream objStream = null;
		try {
			File fileDir = HTalkApplication.getHTalkApplication().getFilesDir();
			if (!fileDir.exists())
				fileDir.mkdirs();
			File file2 = new File(fileDir, file);
			if (!file2.exists())
				file2.createNewFile();
			inputStream = HTalkApplication.getHTalkApplication().openFileInput(
					file);
			if (inputStream.available() <= 0)
				return null;
			objStream = new ObjectInputStream(inputStream);
			return objStream.readObject();
		} finally {
			close(inputStream);
			close(objStream);
		}
	}

	/**
	 * 保存对象到文件
	 * 
	 * @param object
	 * @param file
	 * @throws IOException
	 */
	public static void saveObjectToFile(Object object, String file)
			throws IOException {
		FileOutputStream outputStream = null;
		ObjectOutputStream objOutputStream = null;
		try {
			outputStream = HTalkApplication.getHTalkApplication()
					.openFileOutput(file, Context.MODE_PRIVATE);
			objOutputStream = new ObjectOutputStream(outputStream);
			objOutputStream.writeObject(object);
			objOutputStream.flush();
		} finally {
			close(outputStream);
			close(objOutputStream);
		}
	}

	public static void close(Closeable closeable) {
		if (closeable != null)
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * 保存Bitmap
	 * @param bm
	 * @param picName
	 */
	public static String saveBitmapPath(Bitmap bm, String picName) {
		try {
			if (!isFileExist("")) {
				File tempf = createSDDir("");
			}
			File f = new File(PHOTO_PATH, picName + ".JPEG"); 
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
			return f.getAbsolutePath();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	public static File createSDDir(String dirName) throws IOException {
		File dir = new File(PHOTO_PATH + dirName);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			System.out.println("createSDDir:" + dir.getAbsolutePath());
			System.out.println("createSDDir:" + dir.mkdir());
		}
		return dir;
	}
	
	public static boolean isFileExist(String fileName) {
		File file = new File(PHOTO_PATH + fileName);
		file.isFile();
		return file.exists();
	}
	
	public static void delFile(String fileName){
		File file = new File(PHOTO_PATH + fileName);
		if(file.isFile()){
			file.delete();
        }
		file.exists();
	}

	public static void deleteDir() {
		File dir = new File(PHOTO_PATH);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;
		
		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); 
			else if (file.isDirectory())
				deleteDir(); 
		}
		dir.delete();
	}

}
