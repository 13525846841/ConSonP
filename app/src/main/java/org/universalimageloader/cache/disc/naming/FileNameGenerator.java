package org.universalimageloader.cache.disc.naming;

/**
 * 文件名字自动生成
 * Generates names for files at disc cache
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public interface FileNameGenerator {
	/** Generates unique file name for image defined by URI */
	public abstract String generate(String imageUri);
}
