package org.universalimageloader.cache.disc.naming;


/**
 * http命名
 * @author zhao
 */
public class HttpFileNameGenerator implements FileNameGenerator {
	@Override
	public String generate(String path) {
		int separatorIndex = path.lastIndexOf("/");
		path = (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
		return path;
	}

}
