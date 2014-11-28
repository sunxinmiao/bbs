package com.jeecms.core.entity;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.jeecms.common.email.EmailSender;
import com.jeecms.common.email.MessageTemplate;
import com.jeecms.core.entity.base.BaseConfig;

public class Config extends BaseConfig {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Config.class);

	private static final long serialVersionUID = 1L;

	/* [CONSTRUCTOR MARKER BEGIN] */
	public Config() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Config(java.lang.String id) {
		super(id);
	}

	/* [CONSTRUCTOR MARKER END] */

	public static class ConfigLogin {
		public static String LOGIN_ERROR_INTERVAL = "login_error_interval";
		public static String LOGIN_ERROR_TIMES = "login_error_times";

		private Map<String, String> attr;

		public static ConfigLogin create(Map<String, String> map) {
			if (logger.isDebugEnabled()) {
				logger.debug("create(Map<String,String>) - start"); //$NON-NLS-1$
			}

			ConfigLogin configLogin = new ConfigLogin();
			configLogin.setAttr(map);

			if (logger.isDebugEnabled()) {
				logger.debug("create(Map<String,String>) - end"); //$NON-NLS-1$
			}
			return configLogin;
		}

		public Map<String, String> getAttr() {
			if (logger.isDebugEnabled()) {
				logger.debug("getAttr() - start"); //$NON-NLS-1$
			}

			if (attr == null) {
				attr = new HashMap<String, String>();
			}

			if (logger.isDebugEnabled()) {
				logger.debug("getAttr() - end"); //$NON-NLS-1$
			}
			return attr;
		}

		public void setAttr(Map<String, String> attr) {
			this.attr = attr;
		}

		public Integer getErrorInterval() {
			if (logger.isDebugEnabled()) {
				logger.debug("getErrorInterval() - start"); //$NON-NLS-1$
			}

			String interval = getAttr().get(LOGIN_ERROR_INTERVAL);
			if (NumberUtils.isDigits(interval)) {
				Integer returnInteger = Integer.parseInt(interval);
				if (logger.isDebugEnabled()) {
					logger.debug("getErrorInterval() - end"); //$NON-NLS-1$
				}
				return returnInteger;
			} else {
				// 默认间隔30分钟

				if (logger.isDebugEnabled()) {
					logger.debug("getErrorInterval() - end"); //$NON-NLS-1$
				}
				return 30;
			}
		}

		public void setErrorInterval(Integer errorInterval) {
			if (logger.isDebugEnabled()) {
				logger.debug("setErrorInterval(Integer) - start"); //$NON-NLS-1$
			}

			if (errorInterval != null) {
				getAttr().put(LOGIN_ERROR_INTERVAL, errorInterval.toString());
			} else {
				getAttr().put(LOGIN_ERROR_INTERVAL, null);
			}

			if (logger.isDebugEnabled()) {
				logger.debug("setErrorInterval(Integer) - end"); //$NON-NLS-1$
			}
		}

		public Integer getErrorTimes() {
			if (logger.isDebugEnabled()) {
				logger.debug("getErrorTimes() - start"); //$NON-NLS-1$
			}

			String times = getAttr().get(LOGIN_ERROR_TIMES);
			if (NumberUtils.isDigits(times)) {
				Integer returnInteger = Integer.parseInt(times);
				if (logger.isDebugEnabled()) {
					logger.debug("getErrorTimes() - end"); //$NON-NLS-1$
				}
				return returnInteger;
			} else {
				// 默认3次

				if (logger.isDebugEnabled()) {
					logger.debug("getErrorTimes() - end"); //$NON-NLS-1$
				}
				return 3;
			}
		}

		public void setErrorTimes(Integer errorTimes) {
			if (logger.isDebugEnabled()) {
				logger.debug("setErrorTimes(Integer) - start"); //$NON-NLS-1$
			}

			if (errorTimes != null) {
				getAttr().put(LOGIN_ERROR_TIMES, errorTimes.toString());
			} else {
				getAttr().put(LOGIN_ERROR_TIMES, null);
			}

			if (logger.isDebugEnabled()) {
				logger.debug("setErrorTimes(Integer) - end"); //$NON-NLS-1$
			}
		}
	}

	public static class ConfigEmailSender implements EmailSender {
		public static String EMAIL_HOST = "email_host";
		public static String EMAIL_PORT = "email_port";
		public static String EMAIL_ENCODING = "email_encoding";
		public static String EMAIL_USERNAME = "email_username";
		public static String EMAIL_PASSWORD = "email_password";
		public static String EMAIL_PERSONAL = "email_personal";

		private Map<String, String> attr;

		public static ConfigEmailSender create(Map<String, String> map) {
			if (logger.isDebugEnabled()) {
				logger.debug("create(Map<String,String>) - start"); //$NON-NLS-1$
			}

			if (map == null || StringUtils.isBlank(map.get(EMAIL_HOST))
					|| StringUtils.isBlank(map.get(EMAIL_USERNAME))) {
				// 信息不完整返回null。

				if (logger.isDebugEnabled()) {
					logger.debug("create(Map<String,String>) - end"); //$NON-NLS-1$
				}
				return null;
			}
			ConfigEmailSender sender = new ConfigEmailSender();
			sender.attr = map;

			if (logger.isDebugEnabled()) {
				logger.debug("create(Map<String,String>) - end"); //$NON-NLS-1$
			}
			return sender;

		}

		public Map<String, String> getAttr() {
			if (logger.isDebugEnabled()) {
				logger.debug("getAttr() - start"); //$NON-NLS-1$
			}

			if (attr == null) {
				attr = new HashMap<String, String>();
			}

			if (logger.isDebugEnabled()) {
				logger.debug("getAttr() - end"); //$NON-NLS-1$
			}
			return attr;
		}

		public String getHost() {
			if (logger.isDebugEnabled()) {
				logger.debug("getHost() - start"); //$NON-NLS-1$
			}

			String returnString = getAttr().get(EMAIL_HOST);
			if (logger.isDebugEnabled()) {
				logger.debug("getHost() - end"); //$NON-NLS-1$
			}
			return returnString;
		}

		public void setHost(String host) {
			if (logger.isDebugEnabled()) {
				logger.debug("setHost(String) - start"); //$NON-NLS-1$
			}

			getAttr().put(EMAIL_HOST, host);

			if (logger.isDebugEnabled()) {
				logger.debug("setHost(String) - end"); //$NON-NLS-1$
			}
		}

		public Integer getPort() {
			if (logger.isDebugEnabled()) {
				logger.debug("getPort() - start"); //$NON-NLS-1$
			}

			String port = getAttr().get(EMAIL_HOST);
			if (StringUtils.isNotBlank(port) && NumberUtils.isDigits(port)) {
				Integer returnInteger = Integer.parseInt(port);
				if (logger.isDebugEnabled()) {
					logger.debug("getPort() - end"); //$NON-NLS-1$
				}
				return returnInteger;
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("getPort() - end"); //$NON-NLS-1$
				}
				return null;
			}
		}

		public void setPort(Integer port) {
			if (logger.isDebugEnabled()) {
				logger.debug("setPort(Integer) - start"); //$NON-NLS-1$
			}

			getAttr().put(EMAIL_PORT, port != null ? port.toString() : null);

			if (logger.isDebugEnabled()) {
				logger.debug("setPort(Integer) - end"); //$NON-NLS-1$
			}
		}

		public String getEncoding() {
			if (logger.isDebugEnabled()) {
				logger.debug("getEncoding() - start"); //$NON-NLS-1$
			}

			String encoding = getAttr().get(EMAIL_ENCODING);
			String returnString = StringUtils.isNotBlank(encoding) ? encoding : null;
			if (logger.isDebugEnabled()) {
				logger.debug("getEncoding() - end"); //$NON-NLS-1$
			}
			return returnString;
		}

		public void setEncoding(String encoding) {
			if (logger.isDebugEnabled()) {
				logger.debug("setEncoding(String) - start"); //$NON-NLS-1$
			}

			getAttr().put(EMAIL_ENCODING, encoding);

			if (logger.isDebugEnabled()) {
				logger.debug("setEncoding(String) - end"); //$NON-NLS-1$
			}
		}

		public String getUsername() {
			if (logger.isDebugEnabled()) {
				logger.debug("getUsername() - start"); //$NON-NLS-1$
			}

			String returnString = getAttr().get(EMAIL_USERNAME);
			if (logger.isDebugEnabled()) {
				logger.debug("getUsername() - end"); //$NON-NLS-1$
			}
			return returnString;
		}

		public void setUsername(String username) {
			if (logger.isDebugEnabled()) {
				logger.debug("setUsername(String) - start"); //$NON-NLS-1$
			}

			getAttr().put(EMAIL_USERNAME, username);

			if (logger.isDebugEnabled()) {
				logger.debug("setUsername(String) - end"); //$NON-NLS-1$
			}
		}

		public String getPassword() {
			if (logger.isDebugEnabled()) {
				logger.debug("getPassword() - start"); //$NON-NLS-1$
			}

			String password = getAttr().get(EMAIL_PASSWORD);
			String returnString = StringUtils.isNotBlank(password) ? password : null;
			if (logger.isDebugEnabled()) {
				logger.debug("getPassword() - end"); //$NON-NLS-1$
			}
			return returnString;
		}

		public void setPassword(String password) {
			if (logger.isDebugEnabled()) {
				logger.debug("setPassword(String) - start"); //$NON-NLS-1$
			}

			getAttr().put(EMAIL_PASSWORD, password);

			if (logger.isDebugEnabled()) {
				logger.debug("setPassword(String) - end"); //$NON-NLS-1$
			}
		}

		public String getPersonal() {
			if (logger.isDebugEnabled()) {
				logger.debug("getPersonal() - start"); //$NON-NLS-1$
			}

			String personal = getAttr().get(EMAIL_PERSONAL);
			String returnString = StringUtils.isNotBlank(personal) ? personal : null;
			if (logger.isDebugEnabled()) {
				logger.debug("getPersonal() - end"); //$NON-NLS-1$
			}
			return returnString;
		}

		public void setPersonal(String personal) {
			if (logger.isDebugEnabled()) {
				logger.debug("setPersonal(String) - start"); //$NON-NLS-1$
			}

			getAttr().put(EMAIL_PERSONAL, personal);

			if (logger.isDebugEnabled()) {
				logger.debug("setPersonal(String) - end"); //$NON-NLS-1$
			}
		}
	}

	public static class ConfigMessageTemplate implements MessageTemplate {
		public static String MESSAGE_FORGOTPASSWORD_SUBJECT = "message_forgotpassword_subject";
		public static String MESSAGE_FORGOTPASSWORD_TEXT = "message_forgotpassword_text";
		public static String MESSAGE_REGISTER_SUBJECT = "message_register_subject";
		public static String MESSAGE_REGISTER_TEXT = "message_register_text";

		private Map<String, String> attr;

		public static ConfigMessageTemplate createForgotPasswordMessageTemplate(Map<String, String> map) {
			if (logger.isDebugEnabled()) {
				logger.debug("createForgotPasswordMessageTemplate(Map<String,String>) - start"); //$NON-NLS-1$
			}

			if (map == null || StringUtils.isBlank(map.get(MESSAGE_FORGOTPASSWORD_SUBJECT))
					|| StringUtils.isBlank(map.get(MESSAGE_FORGOTPASSWORD_TEXT))) {
				// 信息不完整，返回null。

				if (logger.isDebugEnabled()) {
					logger.debug("createForgotPasswordMessageTemplate(Map<String,String>) - end"); //$NON-NLS-1$
				}
				return null;
			}
			ConfigMessageTemplate tpl = new ConfigMessageTemplate();
			tpl.setAttr(map);

			if (logger.isDebugEnabled()) {
				logger.debug("createForgotPasswordMessageTemplate(Map<String,String>) - end"); //$NON-NLS-1$
			}
			return tpl;
		}
		
		public static ConfigMessageTemplate createRegisterMessageTemplate(Map<String, String> map) {
			if (logger.isDebugEnabled()) {
				logger.debug("createRegisterMessageTemplate(Map<String,String>) - start"); //$NON-NLS-1$
			}

			if (map == null || StringUtils.isBlank(map.get(MESSAGE_REGISTER_SUBJECT))
					|| StringUtils.isBlank(map.get(MESSAGE_REGISTER_TEXT))) {
				// 信息不完整，返回null。

				if (logger.isDebugEnabled()) {
					logger.debug("createRegisterMessageTemplate(Map<String,String>) - end"); //$NON-NLS-1$
				}
				return null;
			}
			ConfigMessageTemplate tpl = new ConfigMessageTemplate();
			tpl.setAttr(map);

			if (logger.isDebugEnabled()) {
				logger.debug("createRegisterMessageTemplate(Map<String,String>) - end"); //$NON-NLS-1$
			}
			return tpl;
		}

		public Map<String, String> getAttr() {
			if (logger.isDebugEnabled()) {
				logger.debug("getAttr() - start"); //$NON-NLS-1$
			}

			if (attr == null) {
				attr = new HashMap<String, String>();
			}

			if (logger.isDebugEnabled()) {
				logger.debug("getAttr() - end"); //$NON-NLS-1$
			}
			return this.attr;
		}

		public void setAttr(Map<String, String> attr) {
			this.attr = attr;
		}

		public String getForgotPasswordSubject() {
			if (logger.isDebugEnabled()) {
				logger.debug("getForgotPasswordSubject() - start"); //$NON-NLS-1$
			}

			String returnString = getAttr().get(MESSAGE_FORGOTPASSWORD_SUBJECT);
			if (logger.isDebugEnabled()) {
				logger.debug("getForgotPasswordSubject() - end"); //$NON-NLS-1$
			}
			return returnString;
		}

		public void setForgotPasswordSubject(String subject) {
			if (logger.isDebugEnabled()) {
				logger.debug("setForgotPasswordSubject(String) - start"); //$NON-NLS-1$
			}

			getAttr().put(MESSAGE_FORGOTPASSWORD_SUBJECT, subject);

			if (logger.isDebugEnabled()) {
				logger.debug("setForgotPasswordSubject(String) - end"); //$NON-NLS-1$
			}
		}

		public String getForgotPasswordText() {
			if (logger.isDebugEnabled()) {
				logger.debug("getForgotPasswordText() - start"); //$NON-NLS-1$
			}

			String returnString = getAttr().get(MESSAGE_FORGOTPASSWORD_TEXT);
			if (logger.isDebugEnabled()) {
				logger.debug("getForgotPasswordText() - end"); //$NON-NLS-1$
			}
			return returnString;
		}

		public void setForgotPasswordText(String text) {
			if (logger.isDebugEnabled()) {
				logger.debug("setForgotPasswordText(String) - start"); //$NON-NLS-1$
			}

			getAttr().put(MESSAGE_FORGOTPASSWORD_TEXT, text);

			if (logger.isDebugEnabled()) {
				logger.debug("setForgotPasswordText(String) - end"); //$NON-NLS-1$
			}
		}
		
		public String getRegisterSubject() {
			if (logger.isDebugEnabled()) {
				logger.debug("getRegisterSubject() - start"); //$NON-NLS-1$
			}

			String returnString = getAttr().get(MESSAGE_REGISTER_SUBJECT);
			if (logger.isDebugEnabled()) {
				logger.debug("getRegisterSubject() - end"); //$NON-NLS-1$
			}
			return returnString;
		}

		public void setRegisterSubject(String subject) {
			if (logger.isDebugEnabled()) {
				logger.debug("setRegisterSubject(String) - start"); //$NON-NLS-1$
			}

			getAttr().put(MESSAGE_REGISTER_SUBJECT, subject);

			if (logger.isDebugEnabled()) {
				logger.debug("setRegisterSubject(String) - end"); //$NON-NLS-1$
			}
		}

		public String getRegisterText() {
			if (logger.isDebugEnabled()) {
				logger.debug("getRegisterText() - start"); //$NON-NLS-1$
			}

			String returnString = getAttr().get(MESSAGE_REGISTER_TEXT);
			if (logger.isDebugEnabled()) {
				logger.debug("getRegisterText() - end"); //$NON-NLS-1$
			}
			return returnString;
		}

		public void setRegisterText(String text) {
			if (logger.isDebugEnabled()) {
				logger.debug("setRegisterText(String) - start"); //$NON-NLS-1$
			}

			getAttr().put(MESSAGE_REGISTER_TEXT, text);

			if (logger.isDebugEnabled()) {
				logger.debug("setRegisterText(String) - end"); //$NON-NLS-1$
			}
		}
	}

}