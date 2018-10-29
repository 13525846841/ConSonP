package org.universalimageloader.cache.disc.impl;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.universalimageloader.cache.disc.BaseDiscCache;
import org.universalimageloader.cache.disc.naming.FileNameGenerator;
import org.universalimageloader.core.DefaultConfigurationFactory;


/**
 * 
 * 文件未使用的最大时限,超过将删除
 * Cache which deletes files which were loaded more than defined time. Cache size is unlimited.
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @see BaseDiscCache
 */
public class LimitedAgeDiscCache extends BaseDiscCache {

	private final long maxFileAge;

	private final Map<File, Long> loadingDates = Collections.synchronizedMap(new HashMap<File, Long>());

	/**
	 * @param cacheDir
	 *            Directory for file caching
	 * @param maxAge
	 *            Max file age (in seconds). If file age will exceed this value then it'll be removed on next treatment
	 *            (and therefore be reloaded).
	 */
	public LimitedAgeDiscCache(File cacheDir, long maxAge) {
		this(cacheDir, DefaultConfigurationFactory.createFileNameGenerator(), maxAge);
	}

	/**
	 * @param cacheDir
	 *            Directory for file caching
	 * @param fileNameGenerator
	 *            Name generator for cached files
	 * @param maxAge
	 *            Max file age (in seconds). If file age will exceed this value then it'll be removed on next treatment
	 *            (and therefore be reloaded).
	 */
	public LimitedAgeDiscCache(File cacheDir, FileNameGenerator fileNameGenerator, long maxAge) {
		super(cacheDir, fileNameGenerator);
		this.maxFileAge = maxAge * 1000; // to milliseconds
		readLoadingDates();
	}

	private void readLoadingDates() {
		File[] cachedFiles = getCacheDir().listFiles();
		for (File cachedFile : cachedFiles) {
			loadingDates.put(cachedFile, cachedFile.lastModified());
		}
	}

	@Override
	public void put(String key, File file) {
		long currentTime = System.currentTimeMillis();
		file.setLastModified(currentTime);
		loadingDates.put(file, currentTime);
	}

	@Override
	public File get(File cacheDir,String key) {
		File file = super.get(cacheDir,key);
		if (file.exists()) {
			Long loadingDate = loadingDates.get(file);
			if (loadingDate == null) {
				loadingDate = file.lastModified();
			}
			if (System.currentTimeMillis() - loadingDate > maxFileAge) {
				file.delete();
				loadingDates.remove(file);
			}
		}
		return file;
	}
}