package com.jeecms.common.web.springmvc;

import org.apache.log4j.Logger;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * 国际化信息帮助类
 * 
 * @author liufang
 * 
 */
public final class MessageResolver {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MessageResolver.class);

	/**
	 * 获得国际化信息
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param code
	 *            国际化代码
	 * @param args
	 *            替换参数
	 * @return
	 * @see org.springframework.context.MessageSource#getMessage(String,
	 *      Object[], Locale)
	 */
	public static String getMessage(HttpServletRequest request, String code,
			Object... args) {
		if (logger.isDebugEnabled()) {
			logger.debug("getMessage(HttpServletRequest, String, Object) - start"); //$NON-NLS-1$
		}

		WebApplicationContext messageSource = RequestContextUtils
				.getWebApplicationContext(request);
		if (messageSource == null) {
			throw new IllegalStateException("WebApplicationContext not found!");
		}
		LocaleResolver localeResolver = RequestContextUtils
				.getLocaleResolver(request);
		Locale locale;
		if (localeResolver != null) {
			locale = localeResolver.resolveLocale(request);
		} else {
			locale = request.getLocale();
		}
		String returnString = messageSource.getMessage(code, args, locale);
		if (logger.isDebugEnabled()) {
			logger.debug("getMessage(HttpServletRequest, String, Object) - end"); //$NON-NLS-1$
		}
		return returnString;
	}
}
