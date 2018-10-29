package com.yksj.consultation.comm.imageload;

import java.io.File;

import org.universalimageloader.utils.StorageUtils;

import android.content.Context;

public class FileCache {
	
    //文件缓存目录
//    private File cacheDir;
//    private File headerDir;//头像路径
    
    //项目跟路径
//    String rootPath = FileUtils.getRootProjectPath();
   
    //头像跟路径
//    String rootHeader = StorageUtils.getHeadersPath();
    //地图根路径
//    String rootMap = StorageUtils.getMapsPath();
	File mHeaderDir;
	
    
    public FileCache(Context context){
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
//            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"LazyList");
        	mHeaderDir = StorageUtils.getHeaderFileDir();
        }
    }
//    
//    /**
//     * 判断路径是否是一个本地路径
//     * @param url
//     * @return
//     */
    public boolean isLocationUrl(String url){
    	return false;
//    	return url.contains(FileUtils.getRootProjectPath());
    }
//    
//    /**
//     * 获得远程文件的文件名
//     * @param url
//     * @return
//     */
    public String getFileName(String url){
    	String fileName = StorageUtils.getFileName(url);
    	return fileName;
    }
//    
//    
//    
    public File getFile(String url,int type){
    	File file = null;
    	/*File file = null;
    	//头像
    	if(type == 1){
    		//之前已经下载过
    		if(url.contains(rootPath)){
    			file = new File(url);
    		//头像路径	
    		}else{
    			String fileName =  getFileName(url);
    			File fileDir = new File(rootHeader);
    			if(!fileDir.exists()){
    				fileDir.mkdirs();
    			}
    			file = new File(fileDir,fileName);
    		}
    	//发送图片	
    	}else if(type == 2){
    		String[] path = url.split("&");
    		String fileName = getFileName(path[0]);
    		String filePath = FileUtils.getImageRootPath();
    		File fileDir = new File(filePath);
    		if(!fileDir.exists()){
    			fileDir.mkdirs();
    		}
    		file = new File(fileDir,fileName);
    	//地图	
    	}else if(type == 3){
    		File fileDir = new File(rootMap);
    		if(!fileDir.exists()){
    			fileDir.mkdirs();
    		}
    		file = new File(fileDir,url+".jpg");
    	}*/
        return file;
        
    }
//    
//    public void clear(){
////        File[] files=cacheDir.listFiles();
////        if(files==null)
////            return;
////        for(File f:files)
////            f.delete();
//    }

}