package com.jeecms.common.web.springmvc;

import org.apache.log4j.Logger;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

@Component
public class ServletContextRealPathResolver implements RealPathResolver,
		ServletContextAware {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ServletContextRealPathResolver.class);

	public String get(String path) {
		if (logger.isDebugEnabled()) {
			logger.debug("get(String) - start"); //$NON-NLS-1$
		}

		String returnString = context.getRealPath(path);
		if (logger.isDebugEnabled()) {
			logger.debug("get(String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	public void setServletContext(ServletContext servletContext) {
		this.context = servletContext;
	}

	private ServletContext context;
}
