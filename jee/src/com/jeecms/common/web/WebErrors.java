package com.jeecms.common.web;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;

public class WebErrors extends com.jeecms.core.web.WebErrors {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(WebErrors.class);

	/**
	 * 通过HttpServletRequest创建WebErrors
	 * 
	 * @param request
	 *            从request中获得MessageSource和Locale，如果存在的话。
	 * @return 如果LocaleResolver存在则返回国际化WebErrors
	 */
	public static WebErrors create(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("create(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		WebErrors returnWebErrors = new WebErrors(request);
		if (logger.isDebugEnabled()) {
			logger.debug("create(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnWebErrors;
	}

	public WebErrors() {
	}

	public WebErrors(HttpServletRequest request) {
		super(request);
	}

	/**
	 * 构造器
	 * 
	 * @param messageSource
	 * @param locale
	 */
	public WebErrors(MessageSource messageSource, Locale locale) {
		super(messageSource, locale);
	}

	/**
	 * 非站点内数据
	 * 
	 * @param clazz
	 * @param id
	 */
	public void notInSite(Class<?> clazz, Serializable id) {
		if (logger.isDebugEnabled()) {
			logger.debug("notInSite(Class<?>, Serializable) - start"); //$NON-NLS-1$
		}

		addErrorCode("error.notInSite", clazz.getSimpleName(), id);

		if (logger.isDebugEnabled()) {
			logger.debug("notInSite(Class<?>, Serializable) - end"); //$NON-NLS-1$
		}
	}
}
