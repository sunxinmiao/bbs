package com.jeecms.common.web.session.cache;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.spy.memcached.MemcachedClient;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class MemcachedSpyCache implements SessionCache, InitializingBean,
		DisposableBean {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MemcachedSpyCache.class);

	private MemcachedClient client;
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

		client.set(root, exp * 60, session);

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
		client.set(root, exp * 60, session);

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

		boolean returnboolean = client.get(root) != null;
		if (logger.isDebugEnabled()) {
			logger.debug("exist(String) - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	public void afterPropertiesSet() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("afterPropertiesSet() - start"); //$NON-NLS-1$
		}

		List<InetSocketAddress> addr = new ArrayList<InetSocketAddress>(
				servers.length);
		int index;
		for (String s : servers) {
			index = s.indexOf(":");
			addr.add(new InetSocketAddress(s.substring(0, index), Integer
					.parseInt(s.substring(index + 1))));
		}
		client = new MemcachedClient(addr);

		if (logger.isDebugEnabled()) {
			logger.debug("afterPropertiesSet() - end"); //$NON-NLS-1$
		}
	}

	public void destroy() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("destroy() - start"); //$NON-NLS-1$
		}

		client.shutdown();

		if (logger.isDebugEnabled()) {
			logger.debug("destroy() - end"); //$NON-NLS-1$
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
