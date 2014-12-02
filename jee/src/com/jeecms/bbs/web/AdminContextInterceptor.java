package com.jeecms.bbs.web;

import static com.jeecms.common.web.Constants.MESSAGE;
import static com.jeecms.core.action.front.LoginAct.PROCESS_URL;
import static com.jeecms.core.action.front.LoginAct.RETURN_URL;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.AuthenticationMng;
import com.jeecms.core.manager.CmsSiteMng;

/**
 * CMS上下文信息拦截器
 * 
 * 包括登录信息、权限信息、站点信息
 * 
 * @author liufang
 * 
 */
public class AdminContextInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = Logger
			.getLogger(AdminContextInterceptor.class);
	public static final String SITE_PARAM = "_site_id_param";
	public static final String SITE_COOKIE = "_site_id_cookie";
	public static final String PERMISSION_MODEL = "_permission_key";

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("preHandle(HttpServletRequest, HttpServletResponse, Object) - start"); //$NON-NLS-1$
		}

		// 获得站点
		CmsSite site = getSite(request, response);
		site.getConfig();
		CmsUtils.setSite(request, site);
		// Site加入线程变量
		CmsThreadVariable.setSite(site);

		String uri = getURI(request);
		// 不在验证的范围内
		if (exclude(uri)) {
			if (logger.isDebugEnabled()) {
				logger.debug("preHandle(HttpServletRequest, HttpServletResponse, Object) - end"); //$NON-NLS-1$
			}
			return true;
		}
		// 用户为null跳转到登陆页面
		if (null == request.getSession().getAttribute("isAdmin")) {
			response.sendRedirect(getLoginUrl(request));

			if (logger.isDebugEnabled()) {
				logger.debug("preHandle(HttpServletRequest, HttpServletResponse, Object) - end"); //$NON-NLS-1$
			}
			return false;
		}
		// 用户不是管理员，提示无权限。
		if (!Boolean.parseBoolean((String)request.getSession().getAttribute("isAdmin"))) {
			request.setAttribute(MESSAGE, MessageResolver.getMessage(request,
					"login.notAdmin"));
			response.sendError(HttpServletResponse.SC_FORBIDDEN);

			if (logger.isDebugEnabled()) {
				logger.debug("preHandle(HttpServletRequest, HttpServletResponse, Object) - end"); //$NON-NLS-1$
			}
			return false;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("preHandle(HttpServletRequest, HttpServletResponse, Object) - end"); //$NON-NLS-1$
		}
		return true;
	}

	
//	@Override
//	public boolean preHandle(HttpServletRequest request,
//			HttpServletResponse response, Object handler) throws Exception {
//		if (logger.isDebugEnabled()) {
//			logger.debug("preHandle(HttpServletRequest, HttpServletResponse, Object) - start"); //$NON-NLS-1$
//		}
//
//		// 获得站点
//		CmsSite site = getSite(request, response);
//		site.getConfig();
//		CmsUtils.setSite(request, site);
//		// Site加入线程变量
//		CmsThreadVariable.setSite(site);
//		// 获得用户
//		BbsUser user = null;
//		if (adminId != null) {
//			// 指定管理员（开发状态）
//			user = bbsUserMng.findById(adminId);
//			if (user == null) {
//				throw new IllegalStateException("User ID=" + adminId
//						+ " not found!");
//			}
//		} else {
//			// 正常状态
//			Integer userId = authMng
//					.retrieveUserIdFromSession(session, request);
//			if (userId != null) {
//				user = bbsUserMng.findById(userId);
//			}
//		}
//		// 此时用户可以为null
//		CmsUtils.setUser(request, user);
//		// User加入线程变量
//		CmsThreadVariable.setUser(user);
//
//		String uri = getURI(request);
//		// 不在验证的范围内
//		if (exclude(uri)) {
//			if (logger.isDebugEnabled()) {
//				logger.debug("preHandle(HttpServletRequest, HttpServletResponse, Object) - end"); //$NON-NLS-1$
//			}
//			return true;
//		}
//		// 用户为null跳转到登陆页面
//		if (user == null) {
//			response.sendRedirect(getLoginUrl(request));
//
//			if (logger.isDebugEnabled()) {
//				logger.debug("preHandle(HttpServletRequest, HttpServletResponse, Object) - end"); //$NON-NLS-1$
//			}
//			return false;
//		}
//		// 用户不是管理员，提示无权限。
//		if (!user.getAdmin()) {
//			request.setAttribute(MESSAGE, MessageResolver.getMessage(request,
//					"login.notAdmin"));
//			response.sendError(HttpServletResponse.SC_FORBIDDEN);
//
//			if (logger.isDebugEnabled()) {
//				logger.debug("preHandle(HttpServletRequest, HttpServletResponse, Object) - end"); //$NON-NLS-1$
//			}
//			return false;
//		}
//
//		if (logger.isDebugEnabled()) {
//			logger.debug("preHandle(HttpServletRequest, HttpServletResponse, Object) - end"); //$NON-NLS-1$
//		}
//		return true;
//	}
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler, ModelAndView mav)
			throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("afterCompletion(HttpServletRequest, HttpServletResponse, Object, Exception) - start"); //$NON-NLS-1$
		}

		// Sevlet容器有可能使用线程池，所以必须手动清空线程变量。
		CmsThreadVariable.removeUser();
		CmsThreadVariable.removeSite();

		if (logger.isDebugEnabled()) {
			logger.debug("afterCompletion(HttpServletRequest, HttpServletResponse, Object, Exception) - end"); //$NON-NLS-1$
		}
	}

	private String getLoginUrl(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getLoginUrl(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		StringBuilder buff = new StringBuilder();
		if (loginUrl.startsWith("/")) {
			String ctx = request.getContextPath();
			if (!StringUtils.isBlank(ctx)) {
				buff.append(ctx);
			}
		}
		buff.append(loginUrl).append("?");
		buff.append(RETURN_URL).append("=").append(returnUrl);
		if (!StringUtils.isBlank(processUrl)) {
			buff.append("&").append(PROCESS_URL).append("=").append(
					getProcessUrl(request));
		}
		String returnString = buff.toString();
		if (logger.isDebugEnabled()) {
			logger.debug("getLoginUrl(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	private String getProcessUrl(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getProcessUrl(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		StringBuilder buff = new StringBuilder();
		if (loginUrl.startsWith("/")) {
			String ctx = request.getContextPath();
			if (!StringUtils.isBlank(ctx)) {
				buff.append(ctx);
			}
		}
		buff.append(processUrl);
		String returnString = buff.toString();
		if (logger.isDebugEnabled()) {
			logger.debug("getProcessUrl(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 按参数、cookie、域名、默认。
	 * 
	 * @param request
	 * @return 不会返回null，如果站点不存在，则抛出异常。
	 */
	private CmsSite getSite(HttpServletRequest request,
			HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("getSite(HttpServletRequest, HttpServletResponse) - start"); //$NON-NLS-1$
		}

		CmsSite site = getByParams(request, response);
		if (site == null) {
			site = getByCookie(request);
		}
		if (site == null) {
			site = getByDomain(request);
		}
		if (site == null) {
			site = getByDefault();
		}
		if (site == null) {
			throw new RuntimeException("cannot get site!");
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getSite(HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
			}
			return site;
		}
	}

	private CmsSite getByParams(HttpServletRequest request,
			HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("getByParams(HttpServletRequest, HttpServletResponse) - start"); //$NON-NLS-1$
		}

		String p = request.getParameter(SITE_PARAM);
		if (!StringUtils.isBlank(p)) {
			try {
				Integer siteId = Integer.parseInt(p);
				CmsSite site = cmsSiteMng.findById(siteId);
				if (site != null) {
					// 若使用参数选择站点，则应该把站点保存至cookie中才好。
					CookieUtils.addCookie(request, response, SITE_COOKIE, site
							.getId().toString(), null);

					if (logger.isDebugEnabled()) {
						logger.debug("getByParams(HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
					}
					return site;
				}
			} catch (NumberFormatException e) {
				logger.warn("param site id format exception", e);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getByParams(HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
		}
		return null;
	}

	private CmsSite getByCookie(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getByCookie(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		Cookie cookie = CookieUtils.getCookie(request, SITE_COOKIE);
		if (cookie != null) {
			String v = cookie.getValue();
			if (!StringUtils.isBlank(v)) {
				try {
					Integer siteId = Integer.parseInt(v);
					CmsSite returnCmsSite = cmsSiteMng.findById(siteId);
					if (logger.isDebugEnabled()) {
						logger.debug("getByCookie(HttpServletRequest) - end"); //$NON-NLS-1$
					}
					return returnCmsSite;
				} catch (NumberFormatException e) {
					logger.warn("cookie site id format exception", e);
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getByCookie(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return null;
	}

	private CmsSite getByDomain(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getByDomain(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String domain = request.getServerName();
		if (!StringUtils.isBlank(domain)) {
			CmsSite returnCmsSite = cmsSiteMng.findByDomain(domain, true);
			if (logger.isDebugEnabled()) {
				logger.debug("getByDomain(HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return returnCmsSite;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getByDomain(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return null;
	}

	private CmsSite getByDefault() {
		if (logger.isDebugEnabled()) {
			logger.debug("getByDefault() - start"); //$NON-NLS-1$
		}

		List<CmsSite> list = cmsSiteMng.getListFromCache();
		if (list.size() > 0) {
			CmsSite returnCmsSite = list.get(0);
			if (logger.isDebugEnabled()) {
				logger.debug("getByDefault() - end"); //$NON-NLS-1$
			}
			return returnCmsSite;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getByDefault() - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	private boolean exclude(String uri) {
		if (logger.isDebugEnabled()) {
			logger.debug("exclude(String) - start"); //$NON-NLS-1$
		}

		if (excludeUrls != null) {
			for (String exc : excludeUrls) {
				if (exc.equals(uri)) {
					if (logger.isDebugEnabled()) {
						logger.debug("exclude(String) - end"); //$NON-NLS-1$
					}
					return true;
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exclude(String) - end"); //$NON-NLS-1$
		}
		return false;
	}

//	private boolean permistionPass(String uri, Set<String> perms,
//			boolean viewOnly) {
//		String u = null;
//		int i;
//		for (String perm : perms) {
//			if (uri.startsWith(perm)) {
//				// 只读管理员
//				if (viewOnly) {
//					// 获得最后一个 '/' 的URI地址。
//					i = uri.lastIndexOf("/");
//					if (i == -1) {
//						throw new RuntimeException("uri must start width '/':"
//								+ uri);
//					}
//					u = uri.substring(i + 1);
//					// 操作型地址被禁止
//					if (u.startsWith("o_")) {
//						return false;
//					}
//				}
//				return true;
//			}
//		}
//		return false;
//	}

	/**
	 * 获得第三个路径分隔符的位置
	 * 
	 * @param request
	 * @throws IllegalStateException
	 *             访问路径错误，没有三(四)个'/'
	 */
	private static String getURI(HttpServletRequest request)
			throws IllegalStateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getURI(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		UrlPathHelper helper = new UrlPathHelper();
		String uri = helper.getOriginatingRequestUri(request);
		String ctxPath = helper.getOriginatingContextPath(request);
		int start = 0, i = 0, count = 2;
		if (!StringUtils.isBlank(ctxPath)) {
			count++;
		}
		while (i < count && start != -1) {
			start = uri.indexOf('/', start + 1);
			i++;
		}
		if (start <= 0) {
			throw new IllegalStateException(
					"admin access path not like '/jeeadmin/jspgou/...' pattern: "
							+ uri);
		}
		String returnString = uri.substring(start);
		if (logger.isDebugEnabled()) {
			logger.debug("getURI(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	private SessionProvider session;
	private AuthenticationMng authMng;
	private CmsSiteMng cmsSiteMng;
	private BbsUserMng bbsUserMng;

	
	private Integer adminId;
	//private boolean auth = true;
	private String[] excludeUrls;

	private String loginUrl;
	private String processUrl;
	private String returnUrl;

	@Autowired
	public void setSession(SessionProvider session) {
		this.session = session;
	}

	@Autowired
	public void setCmsSiteMng(CmsSiteMng cmsSiteMng) {
		this.cmsSiteMng = cmsSiteMng;
	}

	@Autowired
	public void setBbsUserMng(BbsUserMng bbsUserMng) {
		this.bbsUserMng = bbsUserMng;
	}

	@Autowired
	public void setAuthMng(AuthenticationMng authMng) {
		this.authMng = authMng;
	}
	
//	public void setAuth(boolean auth) {
//		this.auth = auth;
//	}

	public void setExcludeUrls(String[] excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public void setProcessUrl(String processUrl) {
		this.processUrl = processUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

}