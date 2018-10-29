package com.yksj.consultation.comm.imageload;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.yksj.healthtalk.utils.LogUtil;
/**
 * BitmapDrawable 缓存
 * @author zhao
 *
 */
public class BitmapDrawableCache {
	 private static final String TAG = "BitmapDrawableCache";
	    private Map<String, BitmapDrawable> cache=Collections.synchronizedMap(
	            new LinkedHashMap<String, BitmapDrawable>(10,1.5f,true));//Last argument true for LRU ordering
	    
	    private long size=0;//current allocated size
	    
	    private long limit=1000000;//max memory in bytes

	    public BitmapDrawableCache(){
	        //use 25% of available heap size
	        setLimit(Runtime.getRuntime().maxMemory()/4);
	    }
	    
	    public void setLimit(long new_limit){
	        limit=new_limit;
	        LogUtil.i(TAG, "MemoryCache will use up to "+limit/1024./1024.+"MB");
	    }

	    /**
	     * 
	     * 从内存中获得缓存图片
	     * @param id
	     * @return
	     */
	    public BitmapDrawable get(String id){
	        try{
	            if(!cache.containsKey(id))
	                return null;
	            //NullPointerException sometimes happen here http://code.google.com/p/osmdroid/issues/detail?id=78 
	            return cache.get(id);
	        }catch(NullPointerException ex){
	            return null;
	        }
	    }

	    public void put(String id, BitmapDrawable bitmap){
	        try{
	            if(cache.containsKey(id))
	                size-=getSizeInBytes(cache.get(id));
	            cache.put(id, bitmap);
	            size+=getSizeInBytes(bitmap);
	            checkSize();
	        }catch(Throwable th){
	            th.printStackTrace();
	        }
	    }
	    
	    /**
	     * 检测内存大小
	     */
	    private void checkSize() {
	    	LogUtil.i(TAG, "cache size="+size+" length="+cache.size());
	        if(size>limit){
	            Iterator<Entry<String, BitmapDrawable>> iter=cache.entrySet().iterator();//least recently accessed item will be the first one iterated  
	            while(iter.hasNext()){
	                Entry<String, BitmapDrawable> entry=iter.next();
	                size-=getSizeInBytes(entry.getValue());
	                iter.remove();
	                if(size<=limit)
	                    break;
	            }
	            LogUtil.i(TAG, "Clean cache. New size "+cache.size());
	        }
	    }

	    public void clear() {
	    	Map<String, BitmapDrawable> cache1 = Collections.synchronizedMap(new LinkedHashMap<String, BitmapDrawable>(10,1.5f,true));
	    	Set<String>  set = cache1.keySet();
	    	for (String key : set) {
	    		BitmapDrawable bitmap = cache1.get(key);
	    		if(bitmap != null && !bitmap.getBitmap().isRecycled()){
	    			bitmap.getBitmap().recycle();
	    			bitmap = null;
	    		}
			}
	        cache.clear();
	    }

	    /**
	     * 
	     * 计算图片占用空间大小
	     * @param bitmapDrawable
	     * @return
	     */
	    long getSizeInBytes(BitmapDrawable bitmapDrawable) {
	        if(bitmapDrawable==null)
	            return 0;
	        Bitmap bitmap = bitmapDrawable.getBitmap();
	        return bitmap.getRowBytes() * bitmap.getHeight();
	    }
}
