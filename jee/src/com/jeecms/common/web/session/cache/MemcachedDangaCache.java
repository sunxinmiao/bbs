package com.jeecms.common.web.session.cache;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

public class MemcachedDangaCache implements SessionCache, InitializingBean {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MemcachedDangaCache.class);

	private MemCachedClient client;
	private String[] servers;
	private Integer[] weights;

	@SuppressWarnings("unchecked")
	public HashMap<String, Serializable> getSession(String root) {
		if (logger.isDebugEnabled()) {
			logger.debug("getSession(String) - start"); //$NON-NLS-1$
		}

		HashMap<String, Serializable> returnHashMap = (HashMap<String, Serializable>) client.get(root);
		if (logger.isDebugEnabled()) {
			logger.debug("getSession(String) - end"); //$NON-NLS-1$
		}
		return returnHashMap;
	}

	public void setSession(String root, Map<String, Serializable> session,
			int exp) {
		if (logger.isDebugEnabled()) {
			logger.debug("setSession(String, Map<String,Serializable>, int) - start"); //$NON-NLS-1$
		}

		client.set(root, session, new Date(System.currentTimeMillis() + exp
				* 60 * 1000));

		if (logger.isDebugEnabled()) {
			logger.debug("setSession(String, Map<String,Serializable>, int) - end"); //$NON-NLS-1$
		}
	}

	public Serializable getAttribute(String root, String name) {
		if (logger.isDebugEnabled()) {
			logger.debug("getAttribute(String, String) - start"); //$NON-NLS-1$
		}

		HashMap<String, Serializable> session = getSession(root);
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

		HashMap<String, Serializable> session = getSession(root);
		if (session == null) {
			session = new HashMap<String, Serializable>();
		}
		session.put(name, value);
		Date expDate = new Date(System.currentTimeMillis() + exp * 60 * 1000);
		client.set(root, session, expDate);

		if (logger.isDebugEnabled()) {
			logger.debug("setAttribute(String, String, Serializable, int) - end"); //$NON-NLS-1$
		}
	}

	public void clear(String root) {
		if (logger.isDebugEnabled()) {
			logger.debug("clear(String) - start"); //$NON-NLS-1$
		}

		client.delete(root);

		if (logger.isDebugEnabled()) {
			logger.debug("clear(String) - end"); //$NON-NLS-1$
		}
	}

	public boolean exist(String root) {
		if (logger.isDebugEnabled()) {
			logger.debug("exist(String) - start"); //$NON-NLS-1$
		}

		boolean returnboolean = client.keyExists(root);
		if (logger.isDebugEnabled()) {
			logger.debug("exist(String) - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	public void afterPropertiesSet() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("afterPropertiesSet() - start"); //$NON-NLS-1$
		}

		client = new MemCachedClient();
		// grab an instance of our connection pool
		SockIOPool pool = SockIOPool.getInstance();

		// set the servers and the weights
		pool.setServers(servers);
		pool.setWeights(weights);

		// set some basic pool settings
		// 5 initial, 5 min, and 250 max conns
		// and set the max idle time for a conn
		// to 6 hours
		pool.setInitConn(5);
		pool.setMinConn(5);
		pool.setMaxConn(250);
		pool.setMaxIdle(1000 * 60 * 60 * 6);

		// set the sleep for the maint thread
		// it will wake up every x seconds and
		// maintain the pool size
		pool.setMaintSleep(30);

		// set some TCP settings
		// disable nagle
		// set the read timeout to 3 secs
		// and don't set a connect timeout
		pool.setNagle(false);
		pool.setSocketTO(3000);
		pool.setSocketConnectTO(0);

		// initialize the connection pool
		pool.initialize();

		// lets set some compression on for the client
		// compress anything larger than 64k
		client.setCompressEnable(true);
		client.setCompressThreshold(64 * 1024);

		if (logger.isDebugEnabled()) {
			logger.debug("afterPropertiesSet() - end"); //$NON-NLS-1$
		}
	}

	public String[] getServers() {
		return servers;
	}

	public void setServers(String[] servers) {
		this.servers = servers;
	}

	public Integer[] getWeights() {
		return weights;
	}

	public void setWeights(Integer[] weights) {
		this.weights = weights;
	}
}
