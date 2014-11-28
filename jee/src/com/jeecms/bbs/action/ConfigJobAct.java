package com.jeecms.bbs.action;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeecms.bbs.manager.BbsConfigMng;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsSiteMng;

public class ConfigJobAct {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ConfigJobAct.class);

	public void dayClear() {
		if (logger.isDebugEnabled()) {
			logger.debug("dayClear() - start"); //$NON-NLS-1$
		}

		List<CmsSite> siteList = cmsSiteMng.getList();
		if (siteList != null && siteList.size() > 0) {
			for (CmsSite site : siteList) {
				bbsConfigMng.updateConfigForDay(site.getId());
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("dayClear() - end"); //$NON-NLS-1$
		}
	}

	@Autowired
	private BbsConfigMng bbsConfigMng;
	@Autowired
	private CmsSiteMng cmsSiteMng;
}
