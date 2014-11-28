package com.jeecms.common.web;

import org.apache.log4j.Logger;

import static com.jeecms.common.web.Constants.POST;
import static com.jeecms.common.web.Constants.UTF8;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


import org.springframework.web.util.UrlPathHelper;

/**
 * HttpServletRequest帮助类
 * 
 * @author liufang
 * 
 */
public class RequestUtils {
	private static final Logger logger = Logger.getLogger(RequestUtils.class);

	/**
	 * 获取QueryString的参数，并使用URLDecoder以UTF-8格式转码。如果请求是以post方法提交的，
	 * 那么将通过HttpServletRequest#getParameter获取。
	 * 
	 * @param request
	 *            web请求
	 * @param name
	 *            参数名称
	 * @return
	 */
	public static String getQueryParam(HttpServletRequest request, String name) {
		if (logger.isDebugEnabled()) {
			logger.debug("getQueryParam(HttpServletRequest, String) - start"); //$NON-NLS-1$
		}

		if (StringUtils.isBlank(name)) {
			if (logger.isDebugEnabled()) {
				logger.debug("getQueryParam(HttpServletRequest, String) - end"); //$NON-NLS-1$
			}
			return null;
		}
		if (request.getMethod().equalsIgnoreCase(POST)) {
			String returnString = request.getParameter(name);
			if (logger.isDebugEnabled()) {
				logger.debug("getQueryParam(HttpServletRequest, String) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		String s = request.getQueryString();
		if (StringUtils.isBlank(s)) {
			if (logger.isDebugEnabled()) {
				logger.debug("getQueryParam(HttpServletRequest, String) - end"); //$NON-NLS-1$
			}
			return null;
		}
		try {
			s = URLDecoder.decode(s, UTF8);
		} catch (UnsupportedEncodingException e) {
			logger.error("encoding " + UTF8 + " not support?", e);
		}
		String[] values = parseQueryString(s).get(name);
		if (values != null && values.length > 0) {
			String returnString = values[values.length - 1];
			if (logger.isDebugEnabled()) {
				logger.debug("getQueryParam(HttpServletRequest, String) - end"); //$NON-NLS-1$
			}
			return returnString;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getQueryParam(HttpServletRequest, String) - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getQueryParams(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getQueryParams(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		Map<String, String[]> map;
		if (request.getMethod().equalsIgnoreCase(POST)) {
			map = request.getParameterMap();
		} else {
			String s = request.getQueryString();
			if (StringUtils.isBlank(s)) {
				Map<String, Object> returnMap = new HashMap<String, Object>();
				if (logger.isDebugEnabled()) {
					logger.debug("getQueryParams(HttpServletRequest) - end"); //$NON-NLS-1$
				}
				return returnMap;
			}
			try {
				s = URLDecoder.decode(s, UTF8);
			} catch (UnsupportedEncodingException e) {
				logger.error("encoding " + UTF8 + " not support?", e);
			}
			map = parseQueryString(s);
		}

		Map<String, Object> params = new HashMap<String, Object>(map.size());
		int len;
		for (Map.Entry<String, String[]> entry : map.entrySet()) {
			len = entry.getValue().length;
			if (len == 1) {
				params.put(entry.getKey(), entry.getValue()[0]);
			} else if (len > 1) {
				params.put(entry.getKey(), entry.getValue());
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getQueryParams(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return params;
	}

	/**
	 * 
	 * Parses a query string passed from the client to the server and builds a
	 * <code>HashTable</code> object with key-value pairs. The query string
	 * should be in the form of a string packaged by the GET or POST method,
	 * that is, it should have key-value pairs in the form <i>key=value</i>,
	 * with each pair separated from the next by a &amp; character.
	 * 
	 * <p>
	 * A key can appear more than once in the query string with different
	 * values. However, the key appears only once in the hashtable, with its
	 * value being an array of strings containing the multiple values sent by
	 * the query string.
	 * 
	 * <p>
	 * The keys and values in the hashtable are stored in their decoded form, so
	 * any + characters are converted to spaces, and characters sent in
	 * hexadecimal notation (like <i>%xx</i>) are converted to ASCII characters.
	 * 
	 * @param s
	 *            a string containing the query to be parsed
	 * 
	 * @return a <code>HashTable</code> object built from the parsed key-value
	 *         pairs
	 * 
	 * @exception IllegalArgumentException
	 *                if the query string is invalid
	 * 
	 */
	public static Map<String, String[]> parseQueryString(String s) {
		if (logger.isDebugEnabled()) {
			logger.debug("parseQueryString(String) - start"); //$NON-NLS-1$
		}

		String valArray[] = null;
		if (s == null) {
			throw new IllegalArgumentException();
		}
		Map<String, String[]> ht = new HashMap<String, String[]>();
		StringTokenizer st = new StringTokenizer(s, "&");
		while (st.hasMoreTokens()) {
			String pair = (String) st.nextToken();
			int pos = pair.indexOf('=');
			if (pos == -1) {
				continue;
			}
			String key = pair.substring(0, pos);
			String val = pair.substring(pos + 1, pair.length());
			if (ht.containsKey(key)) {
				String oldVals[] = (String[]) ht.get(key);
				valArray = new String[oldVals.length + 1];
				for (int i = 0; i < oldVals.length; i++) {
					valArray[i] = oldVals[i];
				}
				valArray[oldVals.length] = val;
			} else {
				valArray = new String[1];
				valArray[0] = val;
			}
			ht.put(key, valArray);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("parseQueryString(String) - end"); //$NON-NLS-1$
		}
		return ht;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> getRequestMap(HttpServletRequest request,
			String prefix) {
		if (logger.isDebugEnabled()) {
			logger.debug("getRequestMap(HttpServletRequest, String) - start"); //$NON-NLS-1$
		}

		Map<String, String> map = new HashMap<String, String>();
		Enumeration<String> names = request.getParameterNames();
		String name;
		while (names.hasMoreElements()) {
			name = names.nextElement();
			if (name.startsWith(prefix)) {
				request.getParameterValues(name);
				map.put(name.substring(prefix.length()), StringUtils.join(
						request.getParameterValues(name), ','));
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getRequestMap(HttpServletRequest, String) - end"); //$NON-NLS-1$
		}
		return map;
	}

	/**
	 * 获取访问者IP
	 * 
	 * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
	 * 
	 * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
	 * 如果还不存在则调用Request .getRemoteAddr()。
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getIpAddr(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String ip = request.getHeader("X-Real-IP");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			if (logger.isDebugEnabled()) {
				logger.debug("getIpAddr(HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ip.indexOf(',');
			if (index != -1) {
				String returnString = ip.substring(0, index);
				if (logger.isDebugEnabled()) {
					logger.debug("getIpAddr(HttpServletRequest) - end"); //$NON-NLS-1$
				}
				return returnString;
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("getIpAddr(HttpServletRequest) - end"); //$NON-NLS-1$
				}
				return ip;
			}
		} else {
			String returnString = request.getRemoteAddr();
			if (logger.isDebugEnabled()) {
				logger.debug("getIpAddr(HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
	}

	/**
	 * 获得当的访问路径
	 * 
	 * HttpServletRequest.getRequestURL+"?"+HttpServletRequest.getQueryString
	 * 
	 * @param request
	 * @return
	 */
	public static String getLocation(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getLocation(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		UrlPathHelper helper = new UrlPathHelper();
		StringBuffer buff = request.getRequestURL();
		String uri = request.getRequestURI();
		String origUri = helper.getOriginatingRequestUri(request);
		buff.replace(buff.length() - uri.length(), buff.length(), origUri);
		String queryString = helper.getOriginatingQueryString(request);
		if (queryString != null) {
			buff.append("?").append(queryString);
		}
		String returnString = buff.toString();
		if (logger.isDebugEnabled()) {
			logger.debug("getLocation(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 获得请求的session id，但是HttpServletRequest#getRequestedSessionId()方法有一些问题。
	 * 当存在部署路径的时候，会获取到根路径下的jsessionid。
	 * 
	 * @see HttpServletRequest#getRequestedSessionId()
	 * 
	 * @param request
	 * @return
	 */
	public static String getRequestedSessionId(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getRequestedSessionId(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String sid = request.getRequestedSessionId();
		String ctx = request.getContextPath();
		// 如果session id是从url中获取，或者部署路径为空，那么是在正确的。
		if (request.isRequestedSessionIdFromURL() || StringUtils.isBlank(ctx)) {
			if (logger.isDebugEnabled()) {
				logger.debug("getRequestedSessionId(HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return sid;
		} else {
			// 手动从cookie获取
			Cookie cookie = CookieUtils.getCookie(request,
					Constants.JSESSION_COOKIE);
			if (cookie != null) {
				String returnString = cookie.getValue();
				if (logger.isDebugEnabled()) {
					logger.debug("getRequestedSessionId(HttpServletRequest) - end"); //$NON-NLS-1$
				}
				return returnString;
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("getRequestedSessionId(HttpServletRequest) - end"); //$NON-NLS-1$
				}
				return null;
			}
		}

	}

	public static void main(String[] args) {
	}
}
