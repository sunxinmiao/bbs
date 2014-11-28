package com.jeecms.bbs.web;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.context.support.WebApplicationContextUtils;


public class EnhancedHttpSessionEventPublisher extends
		HttpSessionEventPublisher {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(EnhancedHttpSessionEventPublisher.class);

	private static List OnlineUserList = new ArrayList<String>();  
	@Override
	public void sessionCreated(HttpSessionEvent event) {
		if (logger.isDebugEnabled()) {
			logger.debug("sessionCreated(HttpSessionEvent) - start"); //$NON-NLS-1$
		}

		// 将用户加入到在线用户列表中
		saveOrDeleteOnlineUser(event, Type.SAVE);
		HttpSessionCreatedEvent e = new HttpSessionCreatedEvent(event.getSession());
        getContext(event.getSession().getServletContext()).publishEvent(e);

		if (logger.isDebugEnabled()) {
			logger.debug("sessionCreated(HttpSessionEvent) - end"); //$NON-NLS-1$
		}
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		if (logger.isDebugEnabled()) {
			logger.debug("sessionDestroyed(HttpSessionEvent) - start"); //$NON-NLS-1$
		}

		// 将用户从在线用户列表中移除
		saveOrDeleteOnlineUser(event, Type.DELETE);
		 HttpSessionDestroyedEvent e = new HttpSessionDestroyedEvent(event.getSession());
		 getContext(event.getSession().getServletContext()).publishEvent(e);

		if (logger.isDebugEnabled()) {
			logger.debug("sessionDestroyed(HttpSessionEvent) - end"); //$NON-NLS-1$
		}
	}
	
	ApplicationContext getContext(ServletContext servletContext) {
		if (logger.isDebugEnabled()) {
			logger.debug("getContext(ServletContext) - start"); //$NON-NLS-1$
		}

		ApplicationContext returnApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		if (logger.isDebugEnabled()) {
			logger.debug("getContext(ServletContext) - end"); //$NON-NLS-1$
		}
	        return returnApplicationContext;
	    }

	public void saveOrDeleteOnlineUser(HttpSessionEvent event, Type type) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveOrDeleteOnlineUser(HttpSessionEvent, Type) - start"); //$NON-NLS-1$
		}

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		SecurityContext ctx=SecurityContextHolder.getContext();
		if (auth != null) {
			Object principal = auth.getPrincipal();
			if (principal instanceof User) {
				User user = (User) principal;

				switch (type) {
				case SAVE:
					OnlineUserList.add(user.getUsername());// List<String>
					break;
				case DELETE:
					OnlineUserList.remove(user.getUsername());
					break;
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("saveOrDeleteOnlineUser(HttpSessionEvent, Type) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 定义一个简单的内部枚举
	 */
	private static enum Type {
		SAVE, DELETE;
	}

}
