package com.jeecms.common.web.springmvc;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.servlet.view.AbstractTemplateView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import freemarker.core.ParseException;
import freemarker.template.Configuration;

/**
 * 轻量级的FreeemarkerView
 * 
 * 不支持jsp标签、不支持request、session、application等对象，可用于前台模板页面。
 * 
 * @author liufang
 * 
 */
public class SimpleFreeMarkerView extends AbstractTemplateView {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SimpleFreeMarkerView.class);

	/**
	 * 部署路径调用名称
	 */
	public static final String CONTEXT_PATH = "base";

	private Configuration configuration;

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	protected Configuration getConfiguration() {
		return this.configuration;
	}

	/**
	 * 自动检测FreeMarkerConfig
	 * 
	 * @return
	 * @throws BeansException
	 */
	protected FreeMarkerConfig autodetectConfiguration() throws BeansException {
		if (logger.isDebugEnabled()) {
			logger.debug("autodetectConfiguration() - start"); //$NON-NLS-1$
		}

		try {
			FreeMarkerConfig returnFreeMarkerConfig = (FreeMarkerConfig) BeanFactoryUtils.beanOfTypeIncludingAncestors(getApplicationContext(), FreeMarkerConfig.class, true, false);
			if (logger.isDebugEnabled()) {
				logger.debug("autodetectConfiguration() - end"); //$NON-NLS-1$
			}
			return returnFreeMarkerConfig;
		} catch (NoSuchBeanDefinitionException ex) {
			logger.error("autodetectConfiguration()", ex); //$NON-NLS-1$

			throw new ApplicationContextException(
					"Must define a single FreeMarkerConfig bean in this web application context "
							+ "(may be inherited): FreeMarkerConfigurer is the usual implementation. "
							+ "This bean may be given any name.", ex);
		}
	}

	/**
	 * Invoked on startup. Looks for a single FreeMarkerConfig bean to find the
	 * relevant Configuration for this factory.
	 * <p>
	 * Checks that the template for the default Locale can be found: FreeMarker
	 * will check non-Locale-specific templates if a locale-specific one is not
	 * found.
	 * 
	 * @see freemarker.cache.TemplateCache#getTemplate
	 */
	protected void initApplicationContext() throws BeansException {
		if (logger.isDebugEnabled()) {
			logger.debug("initApplicationContext() - start"); //$NON-NLS-1$
		}

		super.initApplicationContext();

		if (getConfiguration() == null) {
			FreeMarkerConfig config = autodetectConfiguration();
			setConfiguration(config.getConfiguration());
		}
		checkTemplate();

		if (logger.isDebugEnabled()) {
			logger.debug("initApplicationContext() - end"); //$NON-NLS-1$
		}
	}

	/**
	 * Check that the FreeMarker template used for this view exists and is
	 * valid.
	 * <p>
	 * Can be overridden to customize the behavior, for example in case of
	 * multiple templates to be rendered into a single view.
	 * 
	 * @throws ApplicationContextException
	 *             if the template cannot be found or is invalid
	 */
	protected void checkTemplate() throws ApplicationContextException {
		if (logger.isDebugEnabled()) {
			logger.debug("checkTemplate() - start"); //$NON-NLS-1$
		}

		try {
			// Check that we can get the template, even if we might subsequently
			// get it again.
			getConfiguration().getTemplate(getUrl());
		} catch (ParseException ex) {
			logger.error("checkTemplate()", ex); //$NON-NLS-1$

			throw new ApplicationContextException(
					"Failed to parse FreeMarker template for URL [" + getUrl()
							+ "]", ex);
		} catch (IOException ex) {
			logger.error("checkTemplate()", ex); //$NON-NLS-1$

			throw new ApplicationContextException(
					"Could not load FreeMarker template for URL [" + getUrl()
							+ "]", ex);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("checkTemplate() - end"); //$NON-NLS-1$
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedTemplateModel(Map model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("renderMergedTemplateModel(Map, HttpServletRequest, HttpServletResponse) - start"); //$NON-NLS-1$
		}

		model.put(CONTEXT_PATH, request.getContextPath());
		getConfiguration().getTemplate(getUrl()).process(model,
				response.getWriter());

		if (logger.isDebugEnabled()) {
			logger.debug("renderMergedTemplateModel(Map, HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
		}
	}
}
