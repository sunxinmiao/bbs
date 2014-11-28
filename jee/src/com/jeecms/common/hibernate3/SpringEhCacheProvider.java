package com.jeecms.common.hibernate3;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.ObjectExistsException;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;
import net.sf.ehcache.config.DiskStoreConfiguration;

import org.hibernate.cache.Cache;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.CacheProvider;
import org.hibernate.cache.Timestamper;


import org.springframework.core.io.Resource;

/**
 * 为WEB应用提供缓存。
 * 
 * 解决配置文件地址和缓存文件存放地址的问题。支持/WEB-INF的地址格式。
 * 
 * @author liufang
 * 
 */
@SuppressWarnings("deprecation")
public final class SpringEhCacheProvider implements CacheProvider {
	private static final Logger logger = Logger.getLogger(SpringEhCacheProvider.class);

	private Resource configLocation;
	private Resource diskStoreLocation;
	private CacheManager manager;

	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}

	public void setDiskStoreLocation(Resource diskStoreLocation) {
		this.diskStoreLocation = diskStoreLocation;
	}

	public final Cache buildCache(String name, Properties properties)
			throws CacheException {
		if (logger.isDebugEnabled()) {
			logger.debug("buildCache(String, Properties) - start"); //$NON-NLS-1$
		}

		try {
			net.sf.ehcache.Ehcache cache = manager.getEhcache(name);
			if (cache == null) {
				logger.warn("Could not find a specific ehcache configuration for cache named [" +name+ "]; using defaults.");
				manager.addCache(name);
				cache = manager.getEhcache(name);
				logger.debug("started EHCache region: " + name);
			}
			Cache returnCache = new net.sf.ehcache.hibernate.EhCache(cache);
			if (logger.isDebugEnabled()) {
				logger.debug("buildCache(String, Properties) - end"); //$NON-NLS-1$
			}
			return returnCache;
		} catch (net.sf.ehcache.CacheException e) {
			logger.error("buildCache(String, Properties)", e); //$NON-NLS-1$

			throw new CacheException(e);
		}
	}

	/**
	 * Returns the next timestamp.
	 */
	public final long nextTimestamp() {
		if (logger.isDebugEnabled()) {
			logger.debug("nextTimestamp() - start"); //$NON-NLS-1$
		}

		long returnlong = Timestamper.next();
		if (logger.isDebugEnabled()) {
			logger.debug("nextTimestamp() - end"); //$NON-NLS-1$
		}
		return returnlong;
	}

	/**
	 * Callback to perform any necessary initialization of the underlying cache
	 * implementation during SessionFactory construction.
	 * <p/>
	 * 
	 * @param properties
	 *            current configuration settings.
	 */
	public final void start(Properties properties) throws CacheException {
		if (logger.isDebugEnabled()) {
			logger.debug("start(Properties) - start"); //$NON-NLS-1$
		}

		if (manager != null) {
			String s = "Attempt to restart an already started EhCacheProvider. Use sessionFactory.close() "
					+ " between repeated calls to buildSessionFactory. Using previously created EhCacheProvider."
					+ " If this behaviour is required, consider using SingletonEhCacheProvider.";
			logger.warn(s);
			return;
		}
		Configuration config = null;
		try {
			if (configLocation != null) {
				config = ConfigurationFactory.parseConfiguration(configLocation
						.getInputStream());
				if (this.diskStoreLocation != null) {
					DiskStoreConfiguration dc = new DiskStoreConfiguration();
					dc.setPath(this.diskStoreLocation.getFile()
							.getAbsolutePath());
					try {
						config.addDiskStore(dc);
					} catch (ObjectExistsException e) {
						String s = "if you want to config distStore in spring,"
								+ " please remove diskStore in config file!";
						logger.warn(s, e);
					}
				}
			}
		} catch (IOException e) {
			logger.warn("create ehcache config failed!", e);
		}
		if (config != null) {
			manager = new CacheManager(config);
		} else {
			manager = new CacheManager();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("start(Properties) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * Callback to perform any necessary cleanup of the underlying cache
	 * implementation during SessionFactory.close().
	 */
	public final void stop() {
		if (logger.isDebugEnabled()) {
			logger.debug("stop() - start"); //$NON-NLS-1$
		}

		if (manager != null) {
			manager.shutdown();
			manager = null;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("stop() - end"); //$NON-NLS-1$
		}
	}

	/**
	 * Not sure what this is supposed to do.
	 * 
	 * @return false to be safe
	 */
	public final boolean isMinimalPutsEnabledByDefault() {
		return false;
	}
}
