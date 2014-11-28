package com.jeecms.common.web;

import org.apache.log4j.Logger;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;




/**
 * HttpServletResponse帮助类
 * 
 * @author liufang
 * 
 */
public final class ResponseUtils {
	public static final Logger logger = Logger.getLogger(ResponseUtils.class);

	/**
	 * 发送文本。使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public static void renderText(HttpServletResponse response, String text) {
		if (logger.isDebugEnabled()) {
			logger.debug("renderText(HttpServletResponse, String) - start"); //$NON-NLS-1$
		}

		render(response, "text/plain;charset=UTF-8", text);

		if (logger.isDebugEnabled()) {
			logger.debug("renderText(HttpServletResponse, String) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 发送json。使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public static void renderJson(HttpServletResponse response, String text) {
		if (logger.isDebugEnabled()) {
			logger.debug("renderJson(HttpServletResponse, String) - start"); //$NON-NLS-1$
		}

		render(response, "application/json;charset=UTF-8", text);

		if (logger.isDebugEnabled()) {
			logger.debug("renderJson(HttpServletResponse, String) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 发送xml。使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public static void renderXml(HttpServletResponse response, String text) {
		if (logger.isDebugEnabled()) {
			logger.debug("renderXml(HttpServletResponse, String) - start"); //$NON-NLS-1$
		}

		render(response, "text/xml;charset=UTF-8", text);

		if (logger.isDebugEnabled()) {
			logger.debug("renderXml(HttpServletResponse, String) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 发送内容。使用UTF-8编码。
	 * 
	 * @param response
	 * @param contentType
	 * @param text
	 */
	public static void render(HttpServletResponse response, String contentType,
			String text) {
		if (logger.isDebugEnabled()) {
			logger.debug("render(HttpServletResponse, String, String) - start"); //$NON-NLS-1$
		}

		response.setContentType(contentType);
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		try {
			response.getWriter().write(text);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("render(HttpServletResponse, String, String) - end"); //$NON-NLS-1$
		}
	}
}
