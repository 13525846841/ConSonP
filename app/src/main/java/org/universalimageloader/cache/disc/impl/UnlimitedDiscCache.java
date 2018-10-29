package org.universalimageloader.cache.disc.impl;

import java.io.File;

import org.universalimageloader.cache.disc.BaseDiscCache;
import org.universalimageloader.cache.disc.DiscCacheAware;
import org.universalimageloader.cache.disc.naming.FileNameGenerator;
import org.universalimageloader.core.DefaultConfigurationFactory;


/**
 * 
 * 无限的缓存空间
 * Default implementation of {@linkplain DiscCacheAware disc cache}. Cache size is unlimited.
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @see BaseDiscCache
 */
public class UnlimitedDiscCache extends BaseDiscCache {
	/**
	 * @param cacheDir
	 *            Directory for file caching
	 */
	public UnlimitedDiscCache(File cacheDir) {
		this(cacheDir, DefaultConfigurationFactory.createFileNameGenerator());
	}

	/**
	 * @param cacheDir
	 *            Directory for file caching
	 * @param fileNameGenerator
	 *            Name generator for cached files
	 */
	public UnlimitedDiscCache(File cacheDir, FileNameGenerator fileNameGenerator) {
		super(cacheDir, fileNameGenerator);
	}

	@Override
	public void put(String key, File file) {
		// Do nothing
	}
}
