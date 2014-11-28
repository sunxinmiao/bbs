package com.jeecms.bbs.web;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


public class OnlineCountListener implements HttpSessionAttributeListener ,HttpSessionListener{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(OnlineCountListener.class);

	/**
	 * 定义监听的session属性名.
	 */
	public final static String LISTENER_NAME = "user_name";

	/**
	 * 定义存储客户登录session的集合.
	 */
	private static List sessions = new ArrayList();
	
	private static int userSum=0;

	/**
	 * 加入session时的监听方法.
	 * 
	 * @param HttpSessionBindingEvent
	 *            session事件
	 */
	public void attributeAdded(HttpSessionBindingEvent sbe) {
		if (logger.isDebugEnabled()) {
			logger.debug("attributeAdded(HttpSessionBindingEvent) - start"); //$NON-NLS-1$
		}

		if (LISTENER_NAME.equals(sbe.getName())) {
			sessions.add(sbe.getValue());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("attributeAdded(HttpSessionBindingEvent) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * session失效时的监听方法.
	 * 
	 * @param HttpSessionBindingEvent
	 *            session事件
	 */
	public void attributeRemoved(HttpSessionBindingEvent sbe) {
		if (logger.isDebugEnabled()) {
			logger.debug("attributeRemoved(HttpSessionBindingEvent) - start"); //$NON-NLS-1$
		}

		if (LISTENER_NAME.equals(sbe.getName())) {
			sessions.remove(sbe.getValue());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("attributeRemoved(HttpSessionBindingEvent) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * session覆盖时的监听方法.
	 * 
	 * @param HttpSessionBindingEvent
	 *            session事件
	 */
	public void attributeReplaced(HttpSessionBindingEvent sbe) {
		if (logger.isDebugEnabled()) {
			logger.debug("attributeReplaced(HttpSessionBindingEvent) - start"); //$NON-NLS-1$
		}

		if (LISTENER_NAME.equals(sbe.getName())) {
			sessions.remove(sbe.getValue());
			sessions.add(sbe.getValue());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("attributeReplaced(HttpSessionBindingEvent) - end"); //$NON-NLS-1$
		}
	}
	public void sessionCreated(HttpSessionEvent arg) {
		if (logger.isDebugEnabled()) {
			logger.debug("sessionCreated(HttpSessionEvent) - start"); //$NON-NLS-1$
		}

		userSum++;

		if (logger.isDebugEnabled()) {
			logger.debug("sessionCreated(HttpSessionEvent) - end"); //$NON-NLS-1$
		}
	}

	public void sessionDestroyed(HttpSessionEvent arg) {
		if (logger.isDebugEnabled()) {
			logger.debug("sessionDestroyed(HttpSessionEvent) - start"); //$NON-NLS-1$
		}

		userSum--;

		if (logger.isDebugEnabled()) {
			logger.debug("sessionDestroyed(HttpSessionEvent) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 返回客户登录session的集合.
	 * 
	 * @return
	 */
	public static List getSessions() {
		return sessions;
	}
	
	public static int getMemberSum() {
		if (logger.isDebugEnabled()) {
			logger.debug("getMemberSum() - start"); //$NON-NLS-1$
		}

		int returnint = sessions.size();
		if (logger.isDebugEnabled()) {
			logger.debug("getMemberSum() - end"); //$NON-NLS-1$
		}
		return returnint;
	}
	
	public static int getSum() {
		return userSum;
	}

	public static Boolean isExistInSessions(String username) {
		if (logger.isDebugEnabled()) {
			logger.debug("isExistInSessions(String) - start"); //$NON-NLS-1$
		}

		String user_name;
		for (Integer i = 0; i < sessions.size(); i++) {
			user_name = (String) sessions.get(i);
			if (user_name.equals(username)) {
				if (logger.isDebugEnabled()) {
					logger.debug("isExistInSessions(String) - end"); //$NON-NLS-1$
				}
				return true;
			} else {
				continue;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("isExistInSessions(String) - end"); //$NON-NLS-1$
		}
		return false;
	}

}
