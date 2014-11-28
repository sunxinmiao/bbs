package com.jeecms.common.security.rememberme;

import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.jeecms.common.security.AccountStatusException;
import com.jeecms.common.security.UsernameNotFoundException;
import com.jeecms.common.security.userdetails.AccountStatusUserDetailsChecker;
import com.jeecms.common.security.userdetails.UserDetails;
import com.jeecms.common.security.userdetails.UserDetailsChecker;
import com.jeecms.common.security.userdetails.UserDetailsService;

public abstract class AbstractRememberMeServices implements RememberMeService,
		InitializingBean {
	public static final String REMEMBER_ME_COOKIE_KEY = "remember_me_cookie";
	private static final String DELIMITER = ":";
	public static final String DEFAULT_PARAMETER = "remember_me";
	public static final int TWO_WEEKS_S = 1209600;
	private String cookieName = REMEMBER_ME_COOKIE_KEY;
	private String parameter = DEFAULT_PARAMETER;
	private int tokenValiditySeconds = TWO_WEEKS_S;
	private boolean alwaysRemember;
	private boolean alwaysRememberCookie;
	private String key;
	private UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();

	private UserDetailsService userDetailsService;
	protected final Logger logger = Logger.getLogger(getClass());

	public final UserDetails autoLogin(HttpServletRequest request,
			HttpServletResponse response) throws CookieTheftException {
		if (logger.isDebugEnabled()) {
			logger.debug("autoLogin(HttpServletRequest, HttpServletResponse) - start"); //$NON-NLS-1$
		}

		String rememberMeCookie = extractRememberMeCookie(request);

		if (rememberMeCookie == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("autoLogin(HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
			}
			return null;
		}

		logger.debug("Remember-me cookie detected");

		UserDetails user = null;

		String[] cookieTokens;
		try {
			cookieTokens = decodeCookie(rememberMeCookie);
			user = processAutoLoginCookie(cookieTokens, request, response);
			userDetailsChecker.check(user);
		} catch (CookieTheftException cte) {
			cancelCookie(request, response);
			throw cte;
		} catch (UsernameNotFoundException noUser) {
			logger.error("autoLogin(HttpServletRequest, HttpServletResponse)", noUser); //$NON-NLS-1$

			cancelCookie(request, response);
			logger.debug("Remember-me login was valid"
					+ " but corresponding user not found.", noUser);
			return null;
		} catch (InvalidCookieException invalidCookie) {
			logger.error("autoLogin(HttpServletRequest, HttpServletResponse)", invalidCookie); //$NON-NLS-1$

			cancelCookie(request, response);
			logger.debug("Invalid remember-me cookie: "
					+ invalidCookie.getMessage());
			return null;
		} catch (AccountStatusException statusInvalid) {
			logger.error("autoLogin(HttpServletRequest, HttpServletResponse)", statusInvalid); //$NON-NLS-1$

			cancelCookie(request, response);
			logger.debug("Invalid UserDetails: " + statusInvalid.getMessage());
			return null;
		} catch (RememberMeAuthenticationException e) {
			logger.error("autoLogin(HttpServletRequest, HttpServletResponse)", e); //$NON-NLS-1$

			cancelCookie(request, response);
			logger.debug(e.getMessage());
			return null;
		}
		logger.debug("Remember-me cookie accepted");
		return user;
	}

	/**
	 * Examines the incoming request and checks for the presence of the
	 * configured "remember me" parameter. If it's present, or if
	 * <tt>alwaysRemember</tt> is set to true, calls <tt>onLoginSucces</tt>.
	 */
	public final boolean loginSuccess(HttpServletRequest request,
			HttpServletResponse response, UserDetails user) {
		if (logger.isDebugEnabled()) {
			logger.debug("loginSuccess(HttpServletRequest, HttpServletResponse, UserDetails) - start"); //$NON-NLS-1$
		}

		if (!rememberMeRequested(request, parameter)) {
			logger.debug("Remember-me login not requested.");
			return false;
		}

		return onLoginSuccess(request, response, user);
	}

	public final void loginFail(HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug("Interactive login attempt was unsuccessful.");
		cancelCookie(request, response);
		onLoginFail(request, response);

		if (logger.isDebugEnabled()) {
			logger.debug("loginFail(HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
		}
	}

	public void logout(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("Remember-me logout.");
		cancelCookie(request, response);
		onLogout(request, response);

		if (logger.isDebugEnabled()) {
			logger.debug("logout(HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * Locates the Spring Security remember me cookie in the request and returns
	 * its value.
	 * 
	 * @param request
	 *            the submitted request which is to be authenticated
	 * @return the cookie value (if present), null otherwise.
	 */
	protected String extractRememberMeCookie(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("extractRememberMeCookie(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		Cookie[] cookies = request.getCookies();

		if ((cookies == null) || (cookies.length == 0)) {
			if (logger.isDebugEnabled()) {
				logger.debug("extractRememberMeCookie(HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return null;
		}

		for (int i = 0; i < cookies.length; i++) {
			if (cookieName.equals(cookies[i].getName())) {
				String returnString = cookies[i].getValue();
				if (logger.isDebugEnabled()) {
					logger.debug("extractRememberMeCookie(HttpServletRequest) - end"); //$NON-NLS-1$
				}
				return returnString;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("extractRememberMeCookie(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return null;
	}

	/**
	 * Allows customization of whether a remember-me login has been requested.
	 * The default is to return true if <tt>alwaysRemember</tt> is set or the
	 * configured parameter name has been included in the request and is set to
	 * the value "true".
	 * 
	 * @param request
	 *            the request submitted from an interactive login, which may
	 *            include additional information indicating that a persistent
	 *            login is desired.
	 * @param parameter
	 *            the configured remember-me parameter name.
	 * 
	 * @return true if the request includes information indicating that a
	 *         persistent login has been requested.
	 */
	protected boolean rememberMeRequested(HttpServletRequest request,
			String parameter) {
		if (logger.isDebugEnabled()) {
			logger.debug("rememberMeRequested(HttpServletRequest, String) - start"); //$NON-NLS-1$
		}

		if (alwaysRemember) {
			if (logger.isDebugEnabled()) {
				logger.debug("rememberMeRequested(HttpServletRequest, String) - end"); //$NON-NLS-1$
			}
			return true;
		}

		String paramValue = request.getParameter(parameter);

		if (paramValue != null) {
			if (paramValue.equalsIgnoreCase("true")
					|| paramValue.equalsIgnoreCase("on")
					|| paramValue.equalsIgnoreCase("yes")
					|| paramValue.equals("1")) {
				if (logger.isDebugEnabled()) {
					logger.debug("rememberMeRequested(HttpServletRequest, String) - end"); //$NON-NLS-1$
				}
				return true;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Did not send remember-me cookie"
					+ " (principal did not set parameter '" + parameter + "')");
		}

		return false;
	}

	/**
	 * Sets the cookie on the response
	 * 
	 * @param tokens
	 *            the tokens which will be encoded to make the cookie value.
	 * @param maxAge
	 *            the value passed to {@link Cookie#setMaxAge(int)}
	 * @param request
	 *            the request
	 * @param response
	 *            the response to add the cookie to.
	 */
	protected void setCookie(String[] tokens, int maxAge,
			HttpServletRequest request, HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("setCookie(String[], int, HttpServletRequest, HttpServletResponse) - start"); //$NON-NLS-1$
		}

		String cookieValue = encodeCookie(tokens);
		Cookie cookie = new Cookie(cookieName, cookieValue);
		String ctx = request.getContextPath();
		cookie.setPath(StringUtils.hasText(ctx) ? ctx : "/");
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);

		if (logger.isDebugEnabled()) {
			logger.debug("setCookie(String[], int, HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * Sets a "cancel cookie" (with maxAge = 0) on the response to disable
	 * persistent logins.
	 * 
	 * @param request
	 * @param response
	 */
	protected void cancelCookie(HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug("Cancelling cookie");
		Cookie cookie = new Cookie(cookieName, null);
		String ctx = request.getContextPath();
		cookie.setPath(StringUtils.hasText(ctx) ? ctx : "/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);

		if (logger.isDebugEnabled()) {
			logger.debug("cancelCookie(HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * Decodes the cookie and splits it into a set of token strings using the
	 * ":" delimiter.
	 * 
	 * @param cookieValue
	 *            the value obtained from the submitted cookie
	 * @return the array of tokens.
	 * @throws InvalidCookieException
	 *             if the cookie was not base64 encoded.
	 */
	protected String[] decodeCookie(String cookieValue)
			throws InvalidCookieException {
		if (logger.isDebugEnabled()) {
			logger.debug("decodeCookie(String) - start"); //$NON-NLS-1$
		}

		StringBuilder sb = new StringBuilder(cookieValue.length() + 3)
				.append(cookieValue);
		for (int j = 0; j < sb.length() % 4; j++) {
			sb.append("=");
		}
		cookieValue = sb.toString();
		if (!Base64.isArrayByteBase64(cookieValue.getBytes())) {
			throw new InvalidCookieException(
					"Cookie token was not Base64 encoded; value was '"
							+ cookieValue + "'");
		}

		String cookieAsPlainText = new String(Base64.decodeBase64(cookieValue
				.getBytes()));

		String[] returnStringArray = StringUtils.delimitedListToStringArray(cookieAsPlainText, DELIMITER);
		if (logger.isDebugEnabled()) {
			logger.debug("decodeCookie(String) - end"); //$NON-NLS-1$
		}
		return returnStringArray;
	}

	/**
	 * Inverse operation of decodeCookie.
	 * 
	 * @param cookieTokens
	 *            the tokens to be encoded.
	 * @return base64 encoding of the tokens concatenated with the ":"
	 *         delimiter.
	 */
	protected String encodeCookie(String[] cookieTokens) {
		if (logger.isDebugEnabled()) {
			logger.debug("encodeCookie(String[]) - start"); //$NON-NLS-1$
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cookieTokens.length; i++) {
			sb.append(cookieTokens[i]);

			if (i < cookieTokens.length - 1) {
				sb.append(DELIMITER);
			}
		}

		String value = sb.toString();

		sb = new StringBuilder(
				new String(Base64.encodeBase64(value.getBytes())));

		while (sb.charAt(sb.length() - 1) == '=') {
			sb.deleteCharAt(sb.length() - 1);
		}

		String returnString = sb.toString();
		if (logger.isDebugEnabled()) {
			logger.debug("encodeCookie(String[]) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * Called from autoLogin to process the submitted persistent login cookie.
	 * Subclasses should validate the cookie and perform any additional
	 * management required.
	 * 
	 * @param cookieTokens
	 *            the decoded and tokenized cookie value
	 * @param request
	 *            the request
	 * @param response
	 *            the response, to allow the cookie to be modified if required.
	 * @return the UserDetails for the corresponding user account if the cookie
	 *         was validated successfully.
	 * @throws RememberMeAuthenticationException
	 *             if the cookie is invalid or the login is invalid for some
	 *             other reason.
	 * @throws UsernameNotFoundException
	 *             if the user account corresponding to the login cookie
	 *             couldn't be found (for example if the user has been removed
	 *             from the system).
	 */
	protected abstract UserDetails processAutoLoginCookie(
			String[] cookieTokens, HttpServletRequest request,
			HttpServletResponse response)
			throws RememberMeAuthenticationException, UsernameNotFoundException;

	/**
	 * Called from loginSuccess when a remember-me login has been requested.
	 * Typically implemented by subclasses to set a remember-me cookie and
	 * potentially store a record of it if the implementation requires this.
	 */
	protected abstract boolean onLoginSuccess(HttpServletRequest request,
			HttpServletResponse response, UserDetails user);

	protected void onLoginFail(HttpServletRequest request,
			HttpServletResponse response) {
	}

	protected void onLogout(HttpServletRequest request,
			HttpServletResponse response) {
	}

	public void afterPropertiesSet() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("afterPropertiesSet() - start"); //$NON-NLS-1$
		}

		Assert.hasLength(key);
		Assert.hasLength(parameter);
		Assert.hasLength(cookieName);
		Assert.notNull(userDetailsService);

		if (logger.isDebugEnabled()) {
			logger.debug("afterPropertiesSet() - end"); //$NON-NLS-1$
		}
	}

	protected String getCookieName() {
		return cookieName;
	}

	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}

	public boolean isAlwaysRemember() {
		return alwaysRemember;
	}

	public void setAlwaysRemember(boolean alwaysRemember) {
		this.alwaysRemember = alwaysRemember;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	protected int getTokenValiditySeconds() {
		return tokenValiditySeconds;
	}

	public void setTokenValiditySeconds(int tokenValiditySeconds) {
		this.tokenValiditySeconds = tokenValiditySeconds;
	}

	public boolean isAlwaysRememberCookie() {
		return alwaysRememberCookie;
	}

	public void setAlwaysRememberCookie(boolean alwaysRememberCookie) {
		this.alwaysRememberCookie = alwaysRememberCookie;
	}
}
