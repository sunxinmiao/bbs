package com.jeecms.common.web.springmvc;

import org.apache.log4j.Logger;

import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

/**
 * ViewResolver for SimpleFreeMarkerView
 * 
 * Override buildView, if viewName start with / , then ignore prefix.
 * 
 * @author liufang
 * 
 */
public class SimpleFreeMarkerViewResolver extends AbstractTemplateViewResolver {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SimpleFreeMarkerViewResolver.class);

	/**
	 * Set default viewClass
	 */
	public SimpleFreeMarkerViewResolver() {
		setViewClass(SimpleFreeMarkerView.class);
	}

	/**
	 * if viewName start with / , then ignore prefix.
	 */
	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("buildView(String) - start"); //$NON-NLS-1$
		}

		AbstractUrlBasedView view = super.buildView(viewName);
		// start with / ignore prefix
		if (viewName.startsWith("/")) {
			view.setUrl(viewName + getSuffix());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("buildView(String) - end"); //$NON-NLS-1$
		}
		return view;
	}
}
