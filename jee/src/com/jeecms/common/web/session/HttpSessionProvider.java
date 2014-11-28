package com.jeecms.common.web.session;

import org.apache.log4j.Logger;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * HttpSession提供类
 * 
 * @author liufang
 * 
 */
public class HttpSessionProvider implements SessionProvider {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(HttpSessionProvider.class);

	public Serializable getAttribute(HttpServletRequest request, String name) {
		if (logger.isDebugEnabled()) {
			logger.debug("getAttribute(HttpServletRequest, String) - start"); //$NON-NLS-1$
		}

		HttpSession session = request.getSession(false);
		if (session != null) {
			Serializable returnSerializable = (Serializable) session.getAttribute(name);
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

	public void setAttribute(HttpServletRequest request,
			HttpServletResponse response, String name, Serializable value) {
		if (logger.isDebugEnabled()) {
			logger.debug("setAttribute(HttpServletRequest, HttpServletResponse, String, Serializable) - start"); //$NON-NLS-1$
		}

		HttpSession session = request.getSession();
		session.setAttribute(name, value);

		if (logger.isDebugEnabled()) {
			logger.debug("setAttribute(HttpServletRequest, HttpServletResponse, String, Serializable) - end"); //$NON-NLS-1$
		}
	}

	public String getSessionId(HttpServletRequest request,
			HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("getSessionId(HttpServletRequest, HttpServletResponse) - start"); //$NON-NLS-1$
		}

		String returnString = request.getSession().getId();
		if (logger.isDebugEnabled()) {
			logger.debug("getSessionId(HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	public void logout(HttpServletRequest request, HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("logout(HttpServletRequest, HttpServletResponse) - start"); //$NON-NLS-1$
		}

		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("logout(HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
		}
	}
}
