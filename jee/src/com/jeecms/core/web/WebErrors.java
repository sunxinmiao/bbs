package com.jeecms.core.web;

import org.apache.log4j.Logger;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;

public class WebErrors extends com.jeecms.common.web.springmvc.WebErrors {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(WebErrors.class);

	/**
	 * 默认错误页面
	 */
	public static final String ERROR_PAGE = "/common/error_message";
	/**
	 * 默认错误信息属性名称
	 */
	public static final String ERROR_ATTR_NAME = "errors";

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

	@Override
	protected String getErrorAttrName() {
		return ERROR_ATTR_NAME;
	}

	@Override
	protected String getErrorPage() {
		return ERROR_PAGE;
	}
}
