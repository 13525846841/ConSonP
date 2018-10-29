package org.universalimageloader.core.download;

import org.universalimageloader.core.ImageLoader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * 图片下载基类
 * Provides retrieving of {@link InputStream} of image by URI.
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public abstract class ImageDownloader{

	public static final String PROTOCOL_FILE = "file";
	public static final String PROTOCOL_FILE_ASSETS = "fileassets";//assets目录获取

	protected static final String PROTOCOL_HTTP = "http";
	protected static final String PROTOCOL_HTTPS = "https";
	protected static final String PROTOCOL_FTP = "ftp";

	protected static final int BUFFER_SIZE = 8 * 1024; // 8 Kb

	/** Retrieves {@link InputStream} of image by URI. Image can be located as in the network and on local file system. */
	public InputStream getStream(URI imageUri) throws IOException {
		String scheme = imageUri.getScheme();
		if (PROTOCOL_HTTP.equals(scheme) || PROTOCOL_HTTPS.equals(scheme) || PROTOCOL_FTP.equals(scheme)) {
			return getStreamFromNetwork(imageUri);
		}else if(PROTOCOL_FILE_ASSETS.equals(scheme)){//
			String path = imageUri.getPath();
			if(path.startsWith("/")){
				path = path.substring(1);
			}
			return ImageLoader.getAssetManager().open(path);
		} else if (PROTOCOL_FILE.equals(scheme)) {
			return getStreamFromFile(imageUri);
		} else {
			return getStreamFromOtherSource(imageUri);
		}
	}
//	new URI("file:///android_asset/icon_healper.png").getRawUserInfo()
	/**
	 * Retrieves {@link InputStream} of image by URI from other source. Should be overriden by successors to implement
	 * image downloading from special sources (not local file and not web URL).
	 */
	protected InputStream getStreamFromOtherSource(URI imageUri) throws IOException {
		return null;
	}

	/** Retrieves {@link InputStream} of image by URI (image is located in the network) */
	protected abstract InputStream getStreamFromNetwork(URI imageUri) throws IOException;

	/** Retrieves {@link InputStream} of image by URI (image is located on the local file system or SD card) */
	protected InputStream getStreamFromFile(URI imageUri) throws IOException {
		return new BufferedInputStream(imageUri.toURL().openStream(), BUFFER_SIZE);
	}
}