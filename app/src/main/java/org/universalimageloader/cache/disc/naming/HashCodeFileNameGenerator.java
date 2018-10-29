package org.universalimageloader.cache.disc.naming;

/**
 * 通过hashcode作为文件名称
 * Names image file as image URI {@linkplain String#hashCode() hashcode}
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class HashCodeFileNameGenerator implements FileNameGenerator {
	@Override
	public String generate(String imageUri) {
		return String.valueOf(imageUri.hashCode());
	}
}
