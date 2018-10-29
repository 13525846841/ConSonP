package org.universalimageloader.core;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.universalimageloader.core.assist.ImageLoadingListener;
import org.universalimageloader.core.assist.ImageSize;
import org.universalimageloader.core.assist.MemoryCacheUtil;

import android.net.Uri;
import android.widget.ImageView;


/**
 * Information for load'n'display image task
 * 
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @see MemoryCacheUtil
 * @see DisplayImageOptions
 * @see ImageLoadingListener
 */
final class ImageLoadingInfo {
	String uri;
	final List<String> urls;
	final String memoryCacheKey;
	final ImageView imageView;
	final ImageSize targetSize;
	final DisplayImageOptions options;
	final ImageLoadingListener listener;
	final ReentrantLock loadFromUriLock;

	public ImageLoadingInfo(String uri, ImageView imageView, ImageSize targetSize, DisplayImageOptions options, ImageLoadingListener listener, ReentrantLock loadFromUriLock,List<String> urls) {
		this.uri = Uri.encode(uri, "@#&=*+-_.,:!?()/~'%");
		this.imageView = imageView;
		this.targetSize = targetSize;
		this.options = options;
		this.listener = listener;
		this.urls = urls;
		if(urls != null)this.uri = urls.toString();
		this.loadFromUriLock = loadFromUriLock;
		memoryCacheKey = MemoryCacheUtil.generateKey(this.uri, targetSize);
	}
}
