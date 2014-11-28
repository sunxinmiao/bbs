package com.jeecms.common.web;

import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.FieldCache.IntParser;
import org.springframework.util.Assert;

/**
 * Cookie 辅助类
 * 
 * @author liufang
 * 
 */
public class CookieUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CookieUtils.class);

	/**
	 * 每页条数cookie名称
	 */
	public static final String COOKIE_PAGE_SIZE = "_cookie_page_size";
	/**
	 * 默认每页条数
	 */
	public static final int DEFAULT_SIZE = 20;
	/**
	 * 最大每页条数
	 */
	public static final int MAX_SIZE = 200;

	/**
	 * 获得cookie的每页条数
	 * 
	 * 使用_cookie_page_size作为cookie name
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return default:20 max:200
	 */
	public static int getPageSize(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPageSize(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		Assert.notNull(request);
		Cookie cookie = getCookie(request, COOKIE_PAGE_SIZE);
		int count = 0;
		if (cookie != null) {
			try {
				count = Integer.parseInt(cookie.getValue());
			} catch (Exception e) {
				logger.warn("getPageSize(HttpServletRequest) - exception ignored", e); //$NON-NLS-1$
			}
		}
		if (count <= 0) {
			count = DEFAULT_SIZE;
		} else if (count > MAX_SIZE) {
			count = MAX_SIZE;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getPageSize(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return count;
	}

	/**
	 * 获得cookie
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param name
	 *            cookie name
	 * @return if exist return cookie, else return null.
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		if (logger.isDebugEnabled()) {
			logger.debug("getCookie(HttpServletRequest, String) - start"); //$NON-NLS-1$
		}

		Assert.notNull(request);
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie c : cookies) {
				if (c.getName().equals(name)) {
					if (logger.isDebugEnabled()) {
						logger.debug("getCookie(HttpServletRequest, String) - end"); //$NON-NLS-1$
					}
					return c;
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getCookie(HttpServletRequest, String) - end"); //$NON-NLS-1$
		}
		return null;
	}

	/**
	 * 根据部署路径，将cookie保存在根目录。
	 * 
	 * @param request
	 * @param response
	 * @param name
	 * @param value
	 * @param expiry
	 * @return
	 */
	public static Cookie addCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String value,
			Integer expiry) {
		if (logger.isDebugEnabled()) {
			logger.debug("addCookie(HttpServletRequest, HttpServletResponse, String, String, Integer) - start"); //$NON-NLS-1$
		}

		Cookie cookie = new Cookie(name, value);
		if (expiry != null) {
			cookie.setMaxAge(expiry);
		}
		String ctx = request.getContextPath();
		cookie.setPath(StringUtils.isBlank(ctx) ? "/" : ctx);
		response.addCookie(cookie);

		if (logger.isDebugEnabled()) {
			logger.debug("addCookie(HttpServletRequest, HttpServletResponse, String, String, Integer) - end"); //$NON-NLS-1$
		}
		return cookie;
	}

	/**
	 * 取消cookie
	 * 
	 * @param response
	 * @param name
	 * @param domain
	 */
	public static void cancleCookie(HttpServletResponse response, String name,
			String domain) {
		if (logger.isDebugEnabled()) {
			logger.debug("cancleCookie(HttpServletResponse, String, String) - start"); //$NON-NLS-1$
		}

		Cookie cookie = new Cookie(name, null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		if (!StringUtils.isBlank(domain)) {
			cookie.setDomain(domain);
		}
		response.addCookie(cookie);

		if (logger.isDebugEnabled()) {
			logger.debug("cancleCookie(HttpServletResponse, String, String) - end"); //$NON-NLS-1$
		}
	}
}
