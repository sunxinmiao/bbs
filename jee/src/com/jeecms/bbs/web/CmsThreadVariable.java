package com.jeecms.bbs.web;

import org.apache.log4j.Logger;

import java.util.List;

import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.CmsSensitivity;
import com.jeecms.core.entity.CmsSite;

/**
 * CMS线程变量
 * 
 * @author liufang
 * 
 */
public class CmsThreadVariable {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CmsThreadVariable.class);

	/**
	 * 当前用户线程变量
	 */
	private static ThreadLocal<BbsUser> BbsUserVariable = new ThreadLocal<BbsUser>();
	/**
	 * 当前站点线程变量
	 */
	private static ThreadLocal<CmsSite> cmsSiteVariable = new ThreadLocal<CmsSite>();

	/**
	 * 当前站点敏感词线程变量
	 */
	private static ThreadLocal<List<CmsSensitivity>> cmsSensitivityVariable = new ThreadLocal<List<CmsSensitivity>>();

	/**
	 * 获得当前用户
	 * 
	 * @return
	 */
	public static BbsUser getUser() {
		if (logger.isDebugEnabled()) {
			logger.debug("getUser() - start"); //$NON-NLS-1$
		}

		BbsUser returnBbsUser = BbsUserVariable.get();
		if (logger.isDebugEnabled()) {
			logger.debug("getUser() - end"); //$NON-NLS-1$
		}
		return returnBbsUser;
	}

	/**
	 * 设置当前用户
	 * 
	 * @param user
	 */
	public static void setUser(BbsUser user) {
		if (logger.isDebugEnabled()) {
			logger.debug("setUser(BbsUser) - start"); //$NON-NLS-1$
		}

		BbsUserVariable.set(user);

		if (logger.isDebugEnabled()) {
			logger.debug("setUser(BbsUser) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 移除当前用户
	 */
	public static void removeUser() {
		if (logger.isDebugEnabled()) {
			logger.debug("removeUser() - start"); //$NON-NLS-1$
		}

		BbsUserVariable.remove();

		if (logger.isDebugEnabled()) {
			logger.debug("removeUser() - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 获得当前站点
	 * 
	 * @return
	 */
	public static CmsSite getSite() {
		if (logger.isDebugEnabled()) {
			logger.debug("getSite() - start"); //$NON-NLS-1$
		}

		CmsSite returnCmsSite = cmsSiteVariable.get();
		if (logger.isDebugEnabled()) {
			logger.debug("getSite() - end"); //$NON-NLS-1$
		}
		return returnCmsSite;
	}

	/**
	 * 设置当前站点
	 * 
	 * @param site
	 */
	public static void setSite(CmsSite site) {
		if (logger.isDebugEnabled()) {
			logger.debug("setSite(CmsSite) - start"); //$NON-NLS-1$
		}

		cmsSiteVariable.set(site);

		if (logger.isDebugEnabled()) {
			logger.debug("setSite(CmsSite) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 移除当前站点
	 */
	public static void removeSite() {
		if (logger.isDebugEnabled()) {
			logger.debug("removeSite() - start"); //$NON-NLS-1$
		}

		cmsSiteVariable.remove();

		if (logger.isDebugEnabled()) {
			logger.debug("removeSite() - end"); //$NON-NLS-1$
		}
	}
	
	/**
	 * 获得当前站点敏感词线程变量
	 * 
	 * @return
	 */
	public static List<CmsSensitivity> getSensitivityList(){
		if (logger.isDebugEnabled()) {
			logger.debug("getSensitivityList() - start"); //$NON-NLS-1$
		}

		List<CmsSensitivity> returnList = cmsSensitivityVariable.get();
		if (logger.isDebugEnabled()) {
			logger.debug("getSensitivityList() - end"); //$NON-NLS-1$
		}
		return returnList;
	}
	
	/**
	 * 设置当前站点敏感词
	 * 
	 * @param cmsSensitivityList
	 */
	public static void setSensitivityList(List<CmsSensitivity> sensitivityList) {
		if (logger.isDebugEnabled()) {
			logger.debug("setSensitivityList(List<CmsSensitivity>) - start"); //$NON-NLS-1$
		}

		cmsSensitivityVariable.set(sensitivityList);

		if (logger.isDebugEnabled()) {
			logger.debug("setSensitivityList(List<CmsSensitivity>) - end"); //$NON-NLS-1$
		}
	}
	
	/**
	 * 移除当前站点敏感词
	 */
	public static void removeSensitivityList() {
		if (logger.isDebugEnabled()) {
			logger.debug("removeSensitivityList() - start"); //$NON-NLS-1$
		}

		cmsSensitivityVariable.remove();

		if (logger.isDebugEnabled()) {
			logger.debug("removeSensitivityList() - end"); //$NON-NLS-1$
		}
	}
}
