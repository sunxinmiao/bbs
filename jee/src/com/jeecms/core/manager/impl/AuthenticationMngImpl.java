package com.jeecms.core.manager.impl;

import org.apache.log4j.Logger;


import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.page.Pagination;
import com.jeecms.common.security.BadCredentialsException;
import com.jeecms.common.security.UsernameNotFoundException;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.core.dao.AuthenticationDao;
import com.jeecms.core.entity.Authentication;
import com.jeecms.core.entity.UnifiedUser;
import com.jeecms.core.manager.AuthenticationMng;
import com.jeecms.core.manager.UnifiedUserMng;

@Service
@Transactional
public class AuthenticationMngImpl implements AuthenticationMng {
	private Logger logger = Logger.getLogger(AuthenticationMngImpl.class);

	public Authentication login(String username, String password, String ip,
			HttpServletRequest request, HttpServletResponse response,
			SessionProvider session) throws UsernameNotFoundException,
			BadCredentialsException {
		if (logger.isDebugEnabled()) {
			logger.debug("login(String, String, String, HttpServletRequest, HttpServletResponse, SessionProvider) - start"); //$NON-NLS-1$
		}

		UnifiedUser user = unifiedUserMng.login(username, password, ip);
		Authentication auth = new Authentication();
		auth.setUid(user.getId());
		auth.setUsername(user.getUsername());
		auth.setEmail(user.getEmail());
		auth.setLoginIp(ip);
		save(auth);
		session.setAttribute(request, response, AUTH_KEY, auth.getId());
		session.setAttribute(request, response, USERNAME, username);

		if (logger.isDebugEnabled()) {
			logger.debug("login(String, String, String, HttpServletRequest, HttpServletResponse, SessionProvider) - end"); //$NON-NLS-1$
		}
		return auth;
	}
	public Authentication activeLogin(UnifiedUser user, String ip,
			HttpServletRequest request, HttpServletResponse response,
			SessionProvider session) {
		if (logger.isDebugEnabled()) {
			logger.debug("activeLogin(UnifiedUser, String, HttpServletRequest, HttpServletResponse, SessionProvider) - start"); //$NON-NLS-1$
		}

		unifiedUserMng.activeLogin(user, ip);
		Authentication auth = new Authentication();
		auth.setUid(user.getId());
		auth.setUsername(user.getUsername());
		auth.setEmail(user.getEmail());
		auth.setLoginIp(ip);
		save(auth);
		session.setAttribute(request, response, AUTH_KEY, auth.getId());
		session.setAttribute(request, response, USERNAME, user.getUsername());

		if (logger.isDebugEnabled()) {
			logger.debug("activeLogin(UnifiedUser, String, HttpServletRequest, HttpServletResponse, SessionProvider) - end"); //$NON-NLS-1$
		}
		return auth;
	}

	public Authentication retrieve(String authId) {
		if (logger.isDebugEnabled()) {
			logger.debug("retrieve(String) - start"); //$NON-NLS-1$
		}

		long current = System.currentTimeMillis();
		// 是否刷新数据库
		if (refreshTime < current) {
			refreshTime = getNextRefreshTime(current, interval);
			int count = dao.deleteExpire(new Date(current - timeout));
			logger.info("refresh Authentication, delete count: "+ count);
		}
		Authentication auth = findById(authId);
		if (auth != null && auth.getUpdateTime().getTime() + timeout > current) {
			auth.setUpdateTime(new Timestamp(current));

			if (logger.isDebugEnabled()) {
				logger.debug("retrieve(String) - end"); //$NON-NLS-1$
			}
			return auth;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("retrieve(String) - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	public Integer retrieveUserIdFromSession(SessionProvider session,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("retrieveUserIdFromSession(SessionProvider, HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String authId = (String) session.getAttribute(request, AUTH_KEY);
		if (authId == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("retrieveUserIdFromSession(SessionProvider, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return null;
		}
		Authentication auth = retrieve(authId);
		if (auth == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("retrieveUserIdFromSession(SessionProvider, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return null;
		}
		Integer returnInteger = auth.getUid();
		if (logger.isDebugEnabled()) {
			logger.debug("retrieveUserIdFromSession(SessionProvider, HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnInteger;
	}

	public void storeAuthIdToSession(SessionProvider session,
			HttpServletRequest request, HttpServletResponse response,
			String authId) {
		if (logger.isDebugEnabled()) {
			logger.debug("storeAuthIdToSession(SessionProvider, HttpServletRequest, HttpServletResponse, String) - start"); //$NON-NLS-1$
		}

		session.setAttribute(request, response, AUTH_KEY, authId);

		if (logger.isDebugEnabled()) {
			logger.debug("storeAuthIdToSession(SessionProvider, HttpServletRequest, HttpServletResponse, String) - end"); //$NON-NLS-1$
		}
	}

	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPage(int, int) - start"); //$NON-NLS-1$
		}

		Pagination page = dao.getPage(pageNo, pageSize);

		if (logger.isDebugEnabled()) {
			logger.debug("getPage(int, int) - end"); //$NON-NLS-1$
		}
		return page;
	}

	@Transactional(readOnly = true)
	public Authentication findById(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(String) - start"); //$NON-NLS-1$
		}

		Authentication entity = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(String) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public Authentication save(Authentication bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(Authentication) - start"); //$NON-NLS-1$
		}

		bean.setId(StringUtils.remove(UUID.randomUUID().toString(), '-'));
		bean.init();
		dao.save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(Authentication) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public Authentication deleteById(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(String) - start"); //$NON-NLS-1$
		}

		Authentication bean = dao.deleteById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(String) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public Authentication[] deleteByIds(String[] ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(String[]) - start"); //$NON-NLS-1$
		}

		Authentication[] beans = new Authentication[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(String[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}

	// 过期时间
	private int timeout = 30 * 60 * 1000; // 30分钟

	// 间隔时间
	private int interval = 4 * 60 * 60 * 1000; // 4小时

	// 刷新时间。
	private long refreshTime = getNextRefreshTime(System.currentTimeMillis(),
			this.interval);

	private UnifiedUserMng unifiedUserMng;
	private AuthenticationDao dao;

	@Autowired
	public void setDao(AuthenticationDao dao) {
		this.dao = dao;
	}

	@Autowired
	public void setUserMng(UnifiedUserMng unifiedUserMng) {
		this.unifiedUserMng = unifiedUserMng;
	}

	/**
	 * 设置认证过期时间。默认30分钟。
	 * 
	 * @param timeout
	 *            单位分钟
	 */
	public void setTimeout(int timeout) {
		if (logger.isDebugEnabled()) {
			logger.debug("setTimeout(int) - start"); //$NON-NLS-1$
		}

		this.timeout = timeout * 60 * 1000;

		if (logger.isDebugEnabled()) {
			logger.debug("setTimeout(int) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 设置刷新数据库时间。默认4小时。
	 * 
	 * @param interval
	 *            单位分钟
	 */
	public void setInterval(int interval) {
		if (logger.isDebugEnabled()) {
			logger.debug("setInterval(int) - start"); //$NON-NLS-1$
		}

		this.interval = interval * 60 * 1000;
		this.refreshTime = getNextRefreshTime(System.currentTimeMillis(),
				this.interval);

		if (logger.isDebugEnabled()) {
			logger.debug("setInterval(int) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 获得下一个刷新时间。
	 * 
	 * 
	 * 
	 * @param current
	 * @param interval
	 * @return 随机间隔时间
	 */
	private long getNextRefreshTime(long current, int interval) {
		if (logger.isDebugEnabled()) {
			logger.debug("getNextRefreshTime(long, int) - start"); //$NON-NLS-1$
		}

		long returnlong = current + interval;
		if (logger.isDebugEnabled()) {
			logger.debug("getNextRefreshTime(long, int) - end"); //$NON-NLS-1$
		}
		return returnlong;
		// 为了防止多个应用同时刷新，间隔时间=interval+RandomUtils.nextInt(interval/4);
		// return current + interval + RandomUtils.nextInt(interval / 4);
	}
}