package com.jeecms.common.web.session.cache;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class EhcacheSessionCache implements SessionCache, InitializingBean {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(EhcacheSessionCache.class);

	@SuppressWarnings("unchecked")
	public Map<String, Serializable> getSession(String root) {
		if (logger.isDebugEnabled()) {
			logger.debug("getSession(String) - start"); //$NON-NLS-1$
		}

		Element e = cache.get(root);
		Map<String, Serializable> returnMap = e != null ? (HashMap<String, Serializable>) e.getValue() : null;
		if (logger.isDebugEnabled()) {
			logger.debug("getSession(String) - end"); //$NON-NLS-1$
		}
		return returnMap;
	}

	public void setSession(String root, Map<String, Serializable> session,
			int exp) {
		if (logger.isDebugEnabled()) {
			logger.debug("setSession(String, Map<String,Serializable>, int) - start"); //$NON-NLS-1$
		}

		cache.put(new Element(root, session));

		if (logger.isDebugEnabled()) {
			logger.debug("setSession(String, Map<String,Serializable>, int) - end"); //$NON-NLS-1$
		}
	}

	public Serializable getAttribute(String root, String name) {
		if (logger.isDebugEnabled()) {
			logger.debug("getAttribute(String, String) - start"); //$NON-NLS-1$
		}

		Map<String, Serializable> session = getSession(root);
		Serializable returnSerializable = session != null ? session.get(name) : null;
		if (logger.isDebugEnabled()) {
			logger.debug("getAttribute(String, String) - end"); //$NON-NLS-1$
		}
		return returnSerializable;
	}

	public void setAttribute(String root, String name, Serializable value,
			int exp) {
		if (logger.isDebugEnabled()) {
			logger.debug("setAttribute(String, String, Serializable, int) - start"); //$NON-NLS-1$
		}

		Map<String, Serializable> session = getSession(root);
		if (session == null) {
			session = new HashMap<String, Serializable>();
		}
		session.put(name, value);
		cache.put(new Element(root, session));

		if (logger.isDebugEnabled()) {
			logger.debug("setAttribute(String, String, Serializable, int) - end"); //$NON-NLS-1$
		}
	}

	public void clear(String root) {
		if (logger.isDebugEnabled()) {
			logger.debug("clear(String) - start"); //$NON-NLS-1$
		}

		cache.remove(root);

		if (logger.isDebugEnabled()) {
			logger.debug("clear(String) - end"); //$NON-NLS-1$
		}
	}

	public boolean exist(String root) {
		if (logger.isDebugEnabled()) {
			logger.debug("exist(String) - start"); //$NON-NLS-1$
		}

		boolean returnboolean = cache.isKeyInCache(root);
		if (logger.isDebugEnabled()) {
			logger.debug("exist(String) - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	public void afterPropertiesSet() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("afterPropertiesSet() - start"); //$NON-NLS-1$
		}

		Assert.notNull(cache);

		if (logger.isDebugEnabled()) {
			logger.debug("afterPropertiesSet() - end"); //$NON-NLS-1$
		}
	}

	private Ehcache cache;

	public void setCache(Ehcache cache) {
		this.cache = cache;
	}

}
