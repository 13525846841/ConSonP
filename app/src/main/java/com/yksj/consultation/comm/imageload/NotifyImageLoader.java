package com.yksj.consultation.comm.imageload;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yksj.healthtalk.entity.ImgTagEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.healthtalk.utils.ImageUtils;
import com.yksj.healthtalk.utils.SystemUtils;

/**
 * 此类用于通知消息图片下载
 * @author zhao
 */
public class NotifyImageLoader {
	
	private Map<TextView, List<Object>> textViews = Collections.synchronizedMap(new WeakHashMap<TextView,List<Object>>());
	
    ExecutorService executorService;
    
    Context mContext;
    
    //图片下载路径
    final String downPath;
    
    FileCache fileCache;
    
    BitmapDrawableCache cache = new BitmapDrawableCache();
    
    BaseAdapter mBaseAdapter;
    
    int headerSize = 30;
    
    public NotifyImageLoader(Context context,BaseAdapter baseAdapter){
    	mContext = context.getApplicationContext();
    	downPath = HTalkApplication.getHTalkApplication().getHttpUrls().URL_QUERYHEADIMAGE;
    	mBaseAdapter = baseAdapter;
    	fileCache = new FileCache(mContext);
    	executorService = Executors.newFixedThreadPool(3);
    	headerSize = (int)context.getResources().getDimension(R.dimen.notifyHeaderSize);
    }
    
    /**
     * 无法从网络上下载头像
     * @param context
     */
    public NotifyImageLoader(Context context){
    	mContext = context.getApplicationContext();
    	downPath = HTalkApplication.getHTalkApplication().getHttpUrls().URL_QUERYHEADIMAGE;
    	fileCache = new FileCache(mContext);
    	executorService = Executors.newFixedThreadPool(3);
    }
    
    /**
     * 为textview设置图文混排内容
     * @param map
     * @param textView
     */
    public void dispalyTextImage(List<Object> list,TextView textView){
    	if(!SystemUtils.getScdExit())return;
    	textViews.put(textView, list);
    	queueImage(textView, list);
    }
    
    /**
     * 从缓存中获取图片
     * @param url
     * @return
     */
    public BitmapDrawable getBitmapDrawableForCache(String url){
    	return cache.get(url);
    }
    
    /**
     * 
     * 开始下载图片
     * @param url
     * @param type
     * @return
     */
    private Bitmap getBitmap(String url,int type) 
    {
        File f = fileCache.getFile(url,type);
        Bitmap bitmap  = decodeFile(f ,type);
        if(bitmap != null) return bitmap;
        String URL = null;
        //头像和图片下载
       if(type == 1){
    	   URL = downPath+url;
       }else if(type == 2){
    	 String[] path = url.split("&");
    	 URL = downPath+path[0];
       }else if(type == 3){
    	   String[] locations = url.split("&");
    	   if(locations!=null && locations.length>=2){
    		   URL = HttpRestClient.getGoogleMapUrl(locations[0],locations[1]);
    	   }else{
    		   return null;
    	   }
       }
        try {
            URL imageUrl = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is=conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            bitmap = decodeFile(f,type);
            return bitmap;
        } catch (Exception ex){
           ex.printStackTrace();
        }
        return null;
    } 
        
       
    	/**
    	 * 从assets目录获取图像
    	 * @param name
    	 * @return
    	 */
    	public BitmapDrawable getBitmapFromAssets(String name){
    		//name = name.replace("assets/","");
    		BitmapDrawable bitmapDrawable  = cache.get(name);
    		if(bitmapDrawable != null)return bitmapDrawable;
    		Bitmap bitmap = null;
    		InputStream inputStream = null;
    		try{
    			if(name.endsWith("系统默认头像女.png")){
    				bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.default_head_female);
    			}else if(name.equals("头像不知名_24.png")){
    				bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.unkonw_head_mankind);
    			}else if(name.equals("s_zcmale_24.png")){//男
    				bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.default_head_mankind);
    			}else if(name.equals("groupdefault_24")){//组
    				bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.default_head_group);
    			}else if(name.equals("assets/customerIcons/系统默认头像女_24.png")){
    				bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.default_head_female);
    			}else if(name.startsWith("assets/group/")){
    				String url = name.replace("assets/","");
    				inputStream = mContext.getResources().getAssets().open(url);
        			bitmap = BitmapFactory.decodeStream(inputStream);
        			if(inputStream != null)inputStream.close();
    			}else{
    				bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.default_head_mankind);
    			}
    		}catch(IOException e){
    			e.printStackTrace();
    		}
    		bitmapDrawable = setBitmapDrawableSize(bitmap);
    		cache.put(name, bitmapDrawable);
    		return bitmapDrawable;
    	}

    	/**
    	 * 设置图片大小
    	 * @param bitmapDrawable
    	 */
    	private BitmapDrawable setBitmapDrawableSize(Bitmap bitmap){
    		BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
    		bitmapDrawable.setBounds(0,0,headerSize,headerSize);
    		return bitmapDrawable;
    	}
    	
        /**
         * 
         * 解析下载图片,将图片保存的内存卡
         *  //decodes image and scales it to reduce memory consumption
         * @param f
         * @return
         */
        private Bitmap decodeFile(File f,int type){
            try {
                //decode image size
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(new FileInputStream(f),null,o);
                int REQUIRED_SIZE=60;
                if(type == 1){
                	
                }else if(type == 2){
                	REQUIRED_SIZE = 100;
                }else if(type == 3){
                	REQUIRED_SIZE = 200;
                }
                int width_tmp=o.outWidth, height_tmp=o.outHeight;
                
                int maxSize = Math.max(width_tmp,height_tmp);
                
                int scale=1;
                
                while(true){
                	if(maxSize/2<REQUIRED_SIZE)break;
                	maxSize/=2;
                	scale*=2;
                }
                //decode with inSampleSize
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize=scale;
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
                if(type == 1 && bitmap != null) 
                	bitmap = ImageUtils.toRoundCorner(bitmap,5);
                return bitmap;
            } catch (FileNotFoundException e) {}
            return null;
        }   
        
    
    /**
     * 启动线程去下载
     * @param textView
     * @param map
     */
    private void queueImage(TextView textView,List<Object> list){
    	ImageLoadProperty imageLoadProperty = new ImageLoadProperty(textView,list);
    	executorService.submit(new ImageToLoader(imageLoadProperty));
    }
    
    /**
     * 当前重用的对象是否正在下载
     * @param imageLoadProperty
     * @return
     */
    boolean isTextViewReused(ImageLoadProperty imageLoadProperty){
    	List<Object> list = textViews.get(imageLoadProperty.textView);
    	if(list == null || list != textViews.get(imageLoadProperty.textView))return true;
    	return false;
    }
    
    private class ImageLoadProperty{
    	public List<Object> list;
    	
    	public TextView textView;
    	
    	public ImageLoadProperty(TextView textView,List<Object> list){
    		this.textView = textView;
    		this.list = list;
    	}
    }
    
    class ImageToLoader implements Runnable{
    	ImageLoadProperty mImageLoadProperty;
    	ImageToLoader(ImageLoadProperty imageLoadProperty){
    		mImageLoadProperty = imageLoadProperty;
    	}
    	public void run() {
    		if(isTextViewReused(mImageLoadProperty))return;
    		for (Object obj : mImageLoadProperty.list) {
				if(obj instanceof ImgTagEntity){
					ImgTagEntity imgTagEntity = (ImgTagEntity)obj;
					String srcImag = imgTagEntity.getSrc();
					String placeImag= imgTagEntity.getPlaceHolder();
					if(cache.get(srcImag) == null){//需要下载图片
						Bitmap bitmap = getBitmap(srcImag,1);
						if(bitmap == null){//头像下载失败,需要使用一个默认头像代替
							BitmapDrawable bitmapDrawable = getBitmapFromAssets(placeImag);
							cache.put(srcImag,bitmapDrawable);
						}else{
							cache.put(srcImag,setBitmapDrawableSize(bitmap));
						}
					}
				}
			}
    		if(isTextViewReused(mImageLoadProperty))return;
    		Activity activity = (Activity)mImageLoadProperty.textView.getContext();
    		ImageTextViewDisplay textViewDisplay = new ImageTextViewDisplay(mImageLoadProperty);
    		activity.runOnUiThread(textViewDisplay);
		}
	}
    
    class ImageTextViewDisplay implements Runnable{
    	ImageLoadProperty imageLoadProperty;
    	
    	ImageTextViewDisplay(ImageLoadProperty imageLoadProperty){
    		this.imageLoadProperty = imageLoadProperty;
    	}
    	
		@Override
		public void run() {
			if(isTextViewReused(imageLoadProperty))return;
//			((AnnouncementAdapter)mBaseAdapter).bindTextViewSpaneble(imageLoadProperty.list,imageLoadProperty.textView);
		}
		
    }
}
