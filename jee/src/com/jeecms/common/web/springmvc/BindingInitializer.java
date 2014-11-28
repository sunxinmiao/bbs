package com.jeecms.common.web.springmvc;

import org.apache.log4j.Logger;

import java.util.Date;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

/**
 * 数据绑定初始化类
 * 
 * @author liufang
 * 
 */
public class BindingInitializer implements WebBindingInitializer {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BindingInitializer.class);

	/**
	 * 初始化数据绑定
	 */
	public void initBinder(WebDataBinder binder, WebRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("initBinder(WebDataBinder, WebRequest) - start"); //$NON-NLS-1$
		}

		binder.registerCustomEditor(Date.class, new DateTypeEditor());

		if (logger.isDebugEnabled()) {
			logger.debug("initBinder(WebDataBinder, WebRequest) - end"); //$NON-NLS-1$
		}
	}
}
