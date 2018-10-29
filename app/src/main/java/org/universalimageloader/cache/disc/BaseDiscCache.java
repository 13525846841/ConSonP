package org.universalimageloader.cache.disc;

import java.io.File;

import org.universalimageloader.cache.disc.naming.FileNameGenerator;
import org.universalimageloader.core.DefaultConfigurationFactory;


/**
 * 文件缓存基类
 * Base disc cache. Implements common functionality for disc cache.
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @see DiscCacheAware
 * @see FileNameGenerator
 */
public abstract class BaseDiscCache implements DiscCacheAware {

	private File cacheDir;

	private FileNameGenerator fileNameGenerator;

	public BaseDiscCache(File cacheDir) {
		this(cacheDir, DefaultConfigurationFactory.createFileNameGenerator());
	}

	public BaseDiscCache(File cacheDir, FileNameGenerator fileNameGenerator) {
		this.cacheDir = cacheDir;
		this.fileNameGenerator = fileNameGenerator;
	}

	//生成文件名
	@Override
	public File get(File cacheDir,String key) {
		String fileName = fileNameGenerator.generate(key);
		return new File(cacheDir, fileName);
	}

	@Override
	public void clear() {
		File[] files = cacheDir.listFiles();
		if (files != null) {
			for (File f : files) {
				deleteDir(f);
			}
		}
	}

	private static boolean deleteDir(File dir) {
		if (dir.isDirectory() && dir.getName().toString()!="camera") {
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
	
	protected File getCacheDir() {
		return cacheDir;
	}
}