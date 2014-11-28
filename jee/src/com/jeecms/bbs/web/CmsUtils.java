package com.jeecms.bbs.web;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.core.entity.CmsSite;

/**
 * 提供一些CMS系统中使用到的共用方法
 * 
 * 比如获得会员信息,获得后台站点信息
 * 
 * @author liufang
 * 
 */
public class CmsUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CmsUtils.class);

	/**
	 * 用户KEY
	 */
	public static final String USER_KEY = "_user_key";
	/**
	 * 站点KEY
	 */
	public static final String SITE_KEY = "_site_key";

	/**
	 * 获得用户
	 * 
	 * @param request
	 * @return
	 */
	public static BbsUser getUser(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getUser(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		BbsUser returnBbsUser = (BbsUser) request.getAttribute(USER_KEY);
		if (logger.isDebugEnabled()) {
			logger.debug("getUser(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnBbsUser;
	}

	/**
	 * 获得用户ID
	 * 
	 * @param request
	 * @return
	 */
	public static Integer getUserId(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getUserId(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		BbsUser user = getUser(request);
		if (user != null) {
			Integer returnInteger = user.getId();
			if (logger.isDebugEnabled()) {
				logger.debug("getUserId(HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return returnInteger;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getUserId(HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	/**
	 * 设置用户
	 * 
	 * @param request
	 * @param user
	 */
	public static void setUser(HttpServletRequest request, BbsUser user) {
		if (logger.isDebugEnabled()) {
			logger.debug("setUser(HttpServletRequest, BbsUser) - start"); //$NON-NLS-1$
		}

		request.setAttribute(USER_KEY, user);

		if (logger.isDebugEnabled()) {
			logger.debug("setUser(HttpServletRequest, BbsUser) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 获得站点
	 * 
	 * @param request
	 * @return
	 */
	public static CmsSite getSite(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getSite(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		CmsSite returnCmsSite = (CmsSite) request.getAttribute(SITE_KEY);
		if (logger.isDebugEnabled()) {
			logger.debug("getSite(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnCmsSite;
	}

	/**
	 * 设置站点
	 * 
	 * @param request
	 * @param site
	 */
	public static void setSite(HttpServletRequest request, CmsSite site) {
		if (logger.isDebugEnabled()) {
			logger.debug("setSite(HttpServletRequest, CmsSite) - start"); //$NON-NLS-1$
		}

		request.setAttribute(SITE_KEY, site);

		if (logger.isDebugEnabled()) {
			logger.debug("setSite(HttpServletRequest, CmsSite) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 获得站点ID
	 * 
	 * @param request
	 * @return
	 */
	public static Integer getSiteId(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getSiteId(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		Integer returnInteger = getSite(request).getId();
		if (logger.isDebugEnabled()) {
			logger.debug("getSiteId(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnInteger;
	}

	/**
	 * 获取IP地址
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
		}
		String returnString = request.getRemoteAddr();
		if (logger.isDebugEnabled()) {
			logger.debug("getIpAddr(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnString;
	}
}
