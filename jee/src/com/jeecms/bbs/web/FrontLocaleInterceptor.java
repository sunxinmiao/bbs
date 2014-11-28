package com.jeecms.bbs.web;

import org.apache.log4j.Logger;

import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.jeecms.core.entity.CmsSite;

/**
 * 前台本地化信息拦截器
 * 
 * @author liufang
 * 
 */
public class FrontLocaleInterceptor extends HandlerInterceptorAdapter {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FrontLocaleInterceptor.class);

	/**
	 * 本地化字符串在ModelMap中的名称
	 */
	public static final String LOCALE = "locale";

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler)
			throws ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("preHandle(HttpServletRequest, HttpServletResponse, Object) - start"); //$NON-NLS-1$
		}

		LocaleResolver localeResolver = RequestContextUtils
				.getLocaleResolver(request);
		if (localeResolver == null) {
			throw new IllegalStateException(
					"No LocaleResolver found: not in a DispatcherServlet request?");
		}
		CmsSite site = CmsUtils.getSite(request);
		String newLocale = site.getLocaleFront();
		LocaleEditor localeEditor = new LocaleEditor();
		localeEditor.setAsText(newLocale);
		localeResolver.setLocale(request, response, (Locale) localeEditor
				.getValue());
		// Proceed in any case.

		if (logger.isDebugEnabled()) {
			logger.debug("preHandle(HttpServletRequest, HttpServletResponse, Object) - end"); //$NON-NLS-1$
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("postHandle(HttpServletRequest, HttpServletResponse, Object, ModelAndView) - start"); //$NON-NLS-1$
		}

		LocaleResolver localeResolver = RequestContextUtils
				.getLocaleResolver(request);
		if (localeResolver == null) {
			throw new IllegalStateException(
					"No LocaleResolver found: not in a DispatcherServlet request?");
		}
		if (modelAndView != null) {
			modelAndView.getModelMap().addAttribute(LOCALE,
					localeResolver.resolveLocale(request).toString());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("postHandle(HttpServletRequest, HttpServletResponse, Object, ModelAndView) - end"); //$NON-NLS-1$
		}
	}
}