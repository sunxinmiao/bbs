package com.jeecms.core.web;

import org.apache.log4j.Logger;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;

public class MagicMessage extends com.jeecms.common.web.springmvc.MagicMessage {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MagicMessage.class);

	/**
	 * 通过HttpServletRequest创建MagicMessage
	 * 
	 * @param request
	 *            从request中获得MessageSource和Locale，如果存在的话。
	 * @return 如果LocaleResolver存在则返回国际化MagicMessage
	 */
	public static MagicMessage create(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("create(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		MagicMessage returnMagicMessage = new MagicMessage(request);
		if (logger.isDebugEnabled()) {
			logger.debug("create(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnMagicMessage;
	}

	public MagicMessage() {
	}

	public MagicMessage(HttpServletRequest request) {
		super(request);
	}

	/**
	 * 构造器
	 * 
	 * @param messageSource
	 * @param locale
	 */
	public MagicMessage(MessageSource messageSource, Locale locale) {
		super(messageSource, locale);
	}
}
