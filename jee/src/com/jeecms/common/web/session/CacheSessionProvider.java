package com.jeecms.common.web.session;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.jeecms.common.web.Constants;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.session.cache.SessionCache;
import com.jeecms.common.web.session.id.SessionIdGenerator;

/**
 * 使用Memcached分布式缓存实现Session
 * 
 * @author liufang
 * 
 */
public class CacheSessionProvider implements SessionProvider, InitializingBean {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CacheSessionProvider.class);

	public static final String CURRENT_SESSION = "_current_session";
	public static final String CURRENT_SESSION_ID = "_current_session_id";

	@SuppressWarnings("unchecked")
	public Serializable getAttribute(HttpServletRequest request, String name) {
		if (logger.isDebugEnabled()) {
			logger.debug("getAttribute(HttpServletRequest, String) - start"); //$NON-NLS-1$
		}

		// 为了避免同一个请求多次获取缓存session，所以将缓存session保存至request中。
		Map<String, Serializable> session = (Map<String, Serializable>) request
				.getAttribute(CURRENT_SESSION);
		if (session != null) {
			Serializable returnSerializable = session.get(name);
			if (logger.isDebugEnabled()) {
				logger.debug("getAttribute(HttpServletRequest, String) - end"); //$NON-NLS-1$
			}
			return returnSerializable;
		}

		String root = (String) request.getAttribute(CURRENT_SESSION_ID);
		if (root == null) {
			root = RequestUtils.getRequestedSessionId(request);
		}
		if (StringUtils.isBlank(root)) {
			request.setAttribute(CURRENT_SESSION,
					new HashMap<String, Serializable>());

			if (logger.isDebugEnabled()) {
				logger.debug("getAttribute(HttpServletRequest, String) - end"); //$NON-NLS-1$
			}
			return null;
		}
		session = sessionCache.getSession(root);
		if (session != null) {
			request.setAttribute(CURRENT_SESSION_ID, root);
			request.setAttribute(CURRENT_SESSION, session);
			Serializable returnSerializable = session.get(name);
			if (logger.isDebugEnabled()) {
				logger.debug("getAttribute(HttpServletRequest, String) - end"); //$NON-NLS-1$
			}
			return returnSerializable;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getAttribute(HttpServletRequest, String) - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public void setAttribute(HttpServletRequest request,
			HttpServletResponse response, String name, Serializable value) {
		if (logger.isDebugEnabled()) {
			logger.debug("setAttribute(HttpServletRequest, HttpServletResponse, String, Serializable) - start"); //$NON-NLS-1$
		}

		Map<String, Serializable> session = (Map<String, Serializable>) request
				.getAttribute(CURRENT_SESSION);
		String root;
		if (session == null) {
			root = RequestUtils.getRequestedSessionId(request);
			if (root != null && root.length() == 32) {
				session = sessionCache.getSession(root);
			}
			if (session == null) {
				session = new HashMap<String, Serializable>();
				do {
					root = sessionIdGenerator.get();
				} while (sessionCache.exist(root));
				response.addCookie(createCookie(request, root));
			}
			request.setAttribute(CURRENT_SESSION, session);
			request.setAttribute(CURRENT_SESSION_ID, root);
		} else {
			root = (String) request.getAttribute(CURRENT_SESSION_ID);
			if (root == null) {
				do {
					root = sessionIdGenerator.get();
				} while (sessionCache.exist(root));
				response.addCookie(createCookie(request, root));
				request.setAttribute(CURRENT_SESSION_ID, root);
			}
		}
		session.put(name, value);
		sessionCache.setSession(root, session, sessionTimeout);

		if (logger.isDebugEnabled()) {
			logger.debug("setAttribute(HttpServletRequest, HttpServletResponse, String, Serializable) - end"); //$NON-NLS-1$
		}
	}

	public String getSessionId(HttpServletRequest request,
			HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("getSessionId(HttpServletRequest, HttpServletResponse) - start"); //$NON-NLS-1$
		}

		String root = (String) request.getAttribute(CURRENT_SESSION_ID);
		if (root != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("getSessionId(HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
			}
			return root;
		}
		root = RequestUtils.getRequestedSessionId(request);
		if (root == null || root.length() != 32 || !sessionCache.exist(root)) {
			do {
				root = sessionIdGenerator.get();
			} while (sessionCache.exist(root));
			sessionCache.setSession(root, new HashMap<String, Serializable>(),
					sessionTimeout);
			response.addCookie(createCookie(request, root));
		}
		request.setAttribute(CURRENT_SESSION_ID, root);

		if (logger.isDebugEnabled()) {
			logger.debug("getSessionId(HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
		}
		return root;
	}

	public void logout(HttpServletRequest request, HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("logout(HttpServletRequest, HttpServletResponse) - start"); //$NON-NLS-1$
		}

		request.removeAttribute(CURRENT_SESSION);
		request.removeAttribute(CURRENT_SESSION_ID);
		String root = RequestUtils.getRequestedSessionId(request);
		if (!StringUtils.isBlank(root)) {
			sessionCache.clear(root);
			Cookie cookie = createCookie(request, null);
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("logout(HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
		}
	}

	private Cookie createCookie(HttpServletRequest request, String value) {
		if (logger.isDebugEnabled()) {
			logger.debug("createCookie(HttpServletRequest, String) - start"); //$NON-NLS-1$
		}

		Cookie cookie = new Cookie(Constants.JSESSION_COOKIE, value);
		String ctx = request.getContextPath();
		cookie.setPath(StringUtils.isBlank(ctx) ? "/" : ctx);

		if (logger.isDebugEnabled()) {
			logger.debug("createCookie(HttpServletRequest, String) - end"); //$NON-NLS-1$
		}
		return cookie;
	}

	public void afterPropertiesSet() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("afterPropertiesSet() - start"); //$NON-NLS-1$
		}

		Assert.notNull(sessionCache);
		Assert.notNull(sessionIdGenerator);

		if (logger.isDebugEnabled()) {
			logger.debug("afterPropertiesSet() - end"); //$NON-NLS-1$
		}
	}

	private SessionCache sessionCache;
	private SessionIdGenerator sessionIdGenerator;
	private int sessionTimeout = 30;

	public void setSessionCache(SessionCache sessionCache) {
		this.sessionCache = sessionCache;
	}

	/**
	 * 设置session过期时间
	 * 
	 * @param sessionTimeout
	 *            分钟
	 */
	public void setSessionTimeout(int sessionTimeout) {
		if (logger.isDebugEnabled()) {
			logger.debug("setSessionTimeout(int) - start"); //$NON-NLS-1$
		}

		Assert.isTrue(sessionTimeout > 0);
		this.sessionTimeout = sessionTimeout;

		if (logger.isDebugEnabled()) {
			logger.debug("setSessionTimeout(int) - end"); //$NON-NLS-1$
		}
	}

	public void setSessionIdGenerator(SessionIdGenerator sessionIdGenerator) {
		this.sessionIdGenerator = sessionIdGenerator;
	}
}
