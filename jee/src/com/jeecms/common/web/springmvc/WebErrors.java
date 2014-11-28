package com.jeecms.common.web.springmvc;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * WEB错误信息
 * 
 * 可以通过MessageSource实现国际化。
 * 
 * @author liufang
 * 
 */
public abstract class WebErrors {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(WebErrors.class);

	/**
	 * email正则表达式
	 */
	public static final Pattern EMAIL_PATTERN = Pattern
			.compile("^\\w+(\\.\\w+)*@\\w+(\\.\\w+)+$");
	/**
	 * username正则表达式
	 */
	public static final Pattern USERNAME_PATTERN = Pattern
			.compile("^[0-9a-zA-Z\\u4e00-\\u9fa5\\.\\-@_]+$");

	/**
	 * 通过HttpServletRequest创建WebErrors
	 * 
	 * @param request
	 *            从request中获得MessageSource和Locale，如果存在的话。
	 */
	public WebErrors(HttpServletRequest request) {
		WebApplicationContext webApplicationContext = RequestContextUtils
				.getWebApplicationContext(request);
		if (webApplicationContext != null) {
			LocaleResolver localeResolver = RequestContextUtils
					.getLocaleResolver(request);
			Locale locale;
			if (localeResolver != null) {
				locale = localeResolver.resolveLocale(request);
				this.messageSource = webApplicationContext;
				this.locale = locale;
			}
		}
	}

	public WebErrors() {
	}

	/**
	 * 构造器
	 * 
	 * @param messageSource
	 * @param locale
	 */
	public WebErrors(MessageSource messageSource, Locale locale) {
		this.messageSource = messageSource;
		this.locale = locale;
	}

	public String getMessage(String code, Object... args) {
		if (logger.isDebugEnabled()) {
			logger.debug("getMessage(String, Object) - start"); //$NON-NLS-1$
		}

		if (messageSource == null) {
			throw new IllegalStateException("MessageSource cannot be null.");
		}
		String returnString = messageSource.getMessage(code, args, locale);
		if (logger.isDebugEnabled()) {
			logger.debug("getMessage(String, Object) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 添加错误代码
	 * 
	 * @param code
	 *            错误代码
	 * @param args
	 *            错误参数
	 * @see org.springframework.context.MessageSource#getMessage
	 */
	public void addErrorCode(String code, Object... args) {
		if (logger.isDebugEnabled()) {
			logger.debug("addErrorCode(String, Object) - start"); //$NON-NLS-1$
		}

		getErrors().add(getMessage(code, args));

		if (logger.isDebugEnabled()) {
			logger.debug("addErrorCode(String, Object) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 添加错误代码
	 * 
	 * @param code
	 *            错误代码
	 * @see org.springframework.context.MessageSource#getMessage
	 */
	public void addErrorCode(String code) {
		if (logger.isDebugEnabled()) {
			logger.debug("addErrorCode(String) - start"); //$NON-NLS-1$
		}

		getErrors().add(getMessage(code));

		if (logger.isDebugEnabled()) {
			logger.debug("addErrorCode(String) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 添加错误字符串
	 * 
	 * @param error
	 */
	public void addErrorString(String error) {
		if (logger.isDebugEnabled()) {
			logger.debug("addErrorString(String) - start"); //$NON-NLS-1$
		}

		getErrors().add(error);

		if (logger.isDebugEnabled()) {
			logger.debug("addErrorString(String) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 添加错误，根据MessageSource是否存在，自动判断为code还是string。
	 * 
	 * @param error
	 */
	public void addError(String error) {
		if (logger.isDebugEnabled()) {
			logger.debug("addError(String) - start"); //$NON-NLS-1$
		}

		// if messageSource exist
		if (messageSource != null) {
			error = messageSource.getMessage(error, null, error, locale);
		}
		getErrors().add(error);

		if (logger.isDebugEnabled()) {
			logger.debug("addError(String) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 是否存在错误
	 * 
	 * @return
	 */
	public boolean hasErrors() {
		if (logger.isDebugEnabled()) {
			logger.debug("hasErrors() - start"); //$NON-NLS-1$
		}

		boolean returnboolean = errors != null && errors.size() > 0;
		if (logger.isDebugEnabled()) {
			logger.debug("hasErrors() - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/**
	 * 错误数量
	 * 
	 * @return
	 */
	public int getCount() {
		if (logger.isDebugEnabled()) {
			logger.debug("getCount() - start"); //$NON-NLS-1$
		}

		int returnint = errors == null ? 0 : errors.size();
		if (logger.isDebugEnabled()) {
			logger.debug("getCount() - end"); //$NON-NLS-1$
		}
		return returnint;
	}

	/**
	 * 错误列表
	 * 
	 * @return
	 */
	public List<String> getErrors() {
		if (logger.isDebugEnabled()) {
			logger.debug("getErrors() - start"); //$NON-NLS-1$
		}

		if (errors == null) {
			errors = new ArrayList<String>();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getErrors() - end"); //$NON-NLS-1$
		}
		return errors;
	}

	/**
	 * 将错误信息保存至ModelMap，并返回错误页面。
	 * 
	 * @param model
	 * @return 错误页面地址
	 * @see org.springframework.ui.ModelMap
	 */
	public String showErrorPage(ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("showErrorPage(ModelMap) - start"); //$NON-NLS-1$
		}

		toModel(model);
		String returnString = getErrorPage();
		if (logger.isDebugEnabled()) {
			logger.debug("showErrorPage(ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 将错误信息保存至ModelMap
	 * 
	 * @param model
	 */
	public void toModel(Map<String, Object> model) {
		if (logger.isDebugEnabled()) {
			logger.debug("toModel(Map<String,Object>) - start"); //$NON-NLS-1$
		}

		Assert.notNull(model);
		if (!hasErrors()) {
			throw new IllegalStateException("no errors found!");
		}
		model.put(getErrorAttrName(), getErrors());

		if (logger.isDebugEnabled()) {
			logger.debug("toModel(Map<String,Object>) - end"); //$NON-NLS-1$
		}
	}

	public boolean ifNull(Object o, String field) {
		if (logger.isDebugEnabled()) {
			logger.debug("ifNull(Object, String) - start"); //$NON-NLS-1$
		}

		if (o == null) {
			addErrorCode("error.required", field);

			if (logger.isDebugEnabled()) {
				logger.debug("ifNull(Object, String) - end"); //$NON-NLS-1$
			}
			return true;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("ifNull(Object, String) - end"); //$NON-NLS-1$
			}
			return false;
		}
	}

	public boolean ifEmpty(Object[] o, String field) {
		if (logger.isDebugEnabled()) {
			logger.debug("ifEmpty(Object[], String) - start"); //$NON-NLS-1$
		}

		if (o == null || o.length <= 0) {
			addErrorCode("error.required", field);

			if (logger.isDebugEnabled()) {
				logger.debug("ifEmpty(Object[], String) - end"); //$NON-NLS-1$
			}
			return true;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("ifEmpty(Object[], String) - end"); //$NON-NLS-1$
			}
			return false;
		}
	}

	public boolean ifBlank(String s, String field, int maxLength) {
		if (logger.isDebugEnabled()) {
			logger.debug("ifBlank(String, String, int) - start"); //$NON-NLS-1$
		}

		if (StringUtils.isBlank(s)) {
			addErrorCode("error.required", field);

			if (logger.isDebugEnabled()) {
				logger.debug("ifBlank(String, String, int) - end"); //$NON-NLS-1$
			}
			return true;
		}
		if (ifMaxLength(s, field, maxLength)) {
			if (logger.isDebugEnabled()) {
				logger.debug("ifBlank(String, String, int) - end"); //$NON-NLS-1$
			}
			return true;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ifBlank(String, String, int) - end"); //$NON-NLS-1$
		}
		return false;
	}

	public boolean ifMaxLength(String s, String field, int maxLength) {
		if (logger.isDebugEnabled()) {
			logger.debug("ifMaxLength(String, String, int) - start"); //$NON-NLS-1$
		}

		if (s != null && s.length() > maxLength) {
			addErrorCode("error.maxLength", field, maxLength);

			if (logger.isDebugEnabled()) {
				logger.debug("ifMaxLength(String, String, int) - end"); //$NON-NLS-1$
			}
			return true;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ifMaxLength(String, String, int) - end"); //$NON-NLS-1$
		}
		return false;
	}

	public boolean ifOutOfLength(String s, String field, int minLength,
			int maxLength) {
		if (logger.isDebugEnabled()) {
			logger.debug("ifOutOfLength(String, String, int, int) - start"); //$NON-NLS-1$
		}

		if (s == null) {
			addErrorCode("error.required", field);

			if (logger.isDebugEnabled()) {
				logger.debug("ifOutOfLength(String, String, int, int) - end"); //$NON-NLS-1$
			}
			return true;
		}
		int len = s.length();
		if (len < minLength || len > maxLength) {
			addErrorCode("error.outOfLength", field, minLength, maxLength);

			if (logger.isDebugEnabled()) {
				logger.debug("ifOutOfLength(String, String, int, int) - end"); //$NON-NLS-1$
			}
			return true;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ifOutOfLength(String, String, int, int) - end"); //$NON-NLS-1$
		}
		return false;
	}

	public boolean ifNotEmail(String email, String field, int maxLength) {
		if (logger.isDebugEnabled()) {
			logger.debug("ifNotEmail(String, String, int) - start"); //$NON-NLS-1$
		}

		if (ifBlank(email, field, maxLength)) {
			if (logger.isDebugEnabled()) {
				logger.debug("ifNotEmail(String, String, int) - end"); //$NON-NLS-1$
			}
			return true;
		}
		Matcher m = EMAIL_PATTERN.matcher(email);
		if (!m.matches()) {
			addErrorCode("error.email", field);

			if (logger.isDebugEnabled()) {
				logger.debug("ifNotEmail(String, String, int) - end"); //$NON-NLS-1$
			}
			return true;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ifNotEmail(String, String, int) - end"); //$NON-NLS-1$
		}
		return false;
	}

	public boolean ifNotUsername(String username, String field, int minLength,
			int maxLength) {
		if (logger.isDebugEnabled()) {
			logger.debug("ifNotUsername(String, String, int, int) - start"); //$NON-NLS-1$
		}

		if (ifOutOfLength(username, field, minLength, maxLength)) {
			if (logger.isDebugEnabled()) {
				logger.debug("ifNotUsername(String, String, int, int) - end"); //$NON-NLS-1$
			}
			return true;
		}
		Matcher m = USERNAME_PATTERN.matcher(username);
		if (!m.matches()) {
			addErrorCode("error.username", field);

			if (logger.isDebugEnabled()) {
				logger.debug("ifNotUsername(String, String, int, int) - end"); //$NON-NLS-1$
			}
			return true;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ifNotUsername(String, String, int, int) - end"); //$NON-NLS-1$
		}
		return false;
	}

	public boolean ifNotExist(Object o, Class<?> clazz, Serializable id) {
		if (logger.isDebugEnabled()) {
			logger.debug("ifNotExist(Object, Class<?>, Serializable) - start"); //$NON-NLS-1$
		}

		if (o == null) {
			addErrorCode("error.notExist", clazz.getSimpleName(), id);

			if (logger.isDebugEnabled()) {
				logger.debug("ifNotExist(Object, Class<?>, Serializable) - end"); //$NON-NLS-1$
			}
			return true;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("ifNotExist(Object, Class<?>, Serializable) - end"); //$NON-NLS-1$
			}
			return false;
		}
	}

	public void noPermission(Class<?> clazz, Serializable id) {
		if (logger.isDebugEnabled()) {
			logger.debug("noPermission(Class<?>, Serializable) - start"); //$NON-NLS-1$
		}

		addErrorCode("error.noPermission", clazz.getSimpleName(), id);

		if (logger.isDebugEnabled()) {
			logger.debug("noPermission(Class<?>, Serializable) - end"); //$NON-NLS-1$
		}
	}

	private MessageSource messageSource;
	private Locale locale;
	private List<String> errors;

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * 获得本地化信息
	 * 
	 * @return
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * 设置本地化信息
	 * 
	 * @param locale
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * 获得错误页面的地址
	 * 
	 * @return
	 */
	protected abstract String getErrorPage();

	/**
	 * 获得错误参数名称
	 * 
	 * @return
	 */
	protected abstract String getErrorAttrName();
}
