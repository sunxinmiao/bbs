package com.jeecms.common.web;

import org.apache.log4j.Logger;

import java.io.IOException;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.ObjectExistsException;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;
import net.sf.ehcache.config.DiskStoreConfiguration;



import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

/**
 * 用于Web的EhCacheManagerFacotryBean。可以基于Web根目录指定diskStore地址。
 * 
 * @author liufang
 * 
 */
public class WebEhCacheManagerFacotryBean implements FactoryBean<CacheManager>,
		InitializingBean, DisposableBean {

	private final Logger logger = Logger.getLogger(WebEhCacheManagerFacotryBean.class);

	private Resource configLocation;
	private Resource diskStoreLocation;

	private String cacheManagerName;

	private CacheManager cacheManager;

	/**
	 * Set the location of the EHCache config file. A typical value is
	 * "/WEB-INF/ehcache.xml".
	 * <p>
	 * Default is "ehcache.xml" in the root of the class path, or if not found,
	 * "ehcache-failsafe.xml" in the EHCache jar (default EHCache
	 * initialization).
	 * 
	 * @see net.sf.ehcache.CacheManager#create(java.io.InputStream)
	 * @see net.sf.ehcache.CacheManager#CacheManager(java.io.InputStream)
	 */
	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}

	/**
	 * 设置ehcahce的diskStore path，例如：/WEB-INF/cache
	 * 
	 * 如设置了此项，则在ehcache配置文件中不要配置<diskStore path=""/>，否则本设置无效。
	 * 
	 * @param diskStoreLocation
	 */
	public void setdiskStoreLocation(Resource diskStoreLocation) {
		this.diskStoreLocation = diskStoreLocation;
	}

	/**
	 * Set the name of the EHCache CacheManager (if a specific name is desired).
	 * 
	 * @see net.sf.ehcache.CacheManager#setName(String)
	 */
	public void setCacheManagerName(String cacheManagerName) {
		this.cacheManagerName = cacheManagerName;
	}

	public void afterPropertiesSet() throws IOException, CacheException {
		logger.info("Initializing EHCache CacheManager");
		Configuration config = null;
		if (this.configLocation != null) {
			config = ConfigurationFactory
					.parseConfiguration(this.configLocation.getInputStream());
			if (this.diskStoreLocation != null) {
				DiskStoreConfiguration dc = new DiskStoreConfiguration();
				dc.setPath(this.diskStoreLocation.getFile().getAbsolutePath());
				try {
					config.addDiskStore(dc);
				} catch (ObjectExistsException e) {
					logger.warn("if you want to config distStore in spring,"
							+ " please remove diskStore in config file!", e);
				}
			}
		}
		if (config != null) {
			this.cacheManager = new CacheManager(config);
		} else {
			this.cacheManager = new CacheManager();
		}
		if (this.cacheManagerName != null) {
			this.cacheManager.setName(this.cacheManagerName);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("afterPropertiesSet() - end"); //$NON-NLS-1$
		}
	}

	public CacheManager getObject() {
		return this.cacheManager;
	}

	public Class<? extends CacheManager> getObjectType() {
		if (logger.isDebugEnabled()) {
			logger.debug("getObjectType() - start"); //$NON-NLS-1$
		}

		Class<? extends CacheManager> returnClass = (this.cacheManager != null ? this.cacheManager.getClass() : CacheManager.class);
		if (logger.isDebugEnabled()) {
			logger.debug("getObjectType() - end"); //$NON-NLS-1$
		}
		return returnClass;
	}

	public boolean isSingleton() {
		return true;
	}

	public void destroy() {
		logger.info("Shutting down EHCache CacheManager");
		this.cacheManager.shutdown();

		if (logger.isDebugEnabled()) {
			logger.debug("destroy() - end"); //$NON-NLS-1$
		}
	}
}
