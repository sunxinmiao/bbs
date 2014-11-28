package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;

import java.util.List;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.cache.BbsConfigCache;
import com.jeecms.bbs.dao.BbsConfigDao;
import com.jeecms.bbs.entity.BbsConfig;
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.manager.BbsConfigMng;
import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.common.hibernate3.Updater;

@Service
@Transactional
public class BbsConfigMngImpl implements BbsConfigMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsConfigMngImpl.class);

	@Transactional(readOnly = true)
	public BbsConfig findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsConfig config = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return config;
	}

	public BbsConfig update(BbsConfig bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsConfig) - start"); //$NON-NLS-1$
		}

		BbsConfig entity = findById(bean.getId());
		Updater<BbsConfig> updater = new Updater<BbsConfig>(bean);
		entity = dao.updateByUpdater(updater);

		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsConfig) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsConfig updateConfigForDay(Integer siteId) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateConfigForDay(Integer) - start"); //$NON-NLS-1$
		}

		List<BbsForum> flist = bbsForumMng.getList(siteId);
		for (BbsForum forum : flist) {
			forum.setPostToday(0);
			bbsForumMng.update(forum);
		}
		BbsConfig bbsConfig = dao.findById(siteId);
		Element e = cache.get(siteId);
		if (e != null) {
			BbsConfigCache configCache = (BbsConfigCache) e.getValue();
			bbsConfig.setLastUser(configCache.getLastUser());
			bbsConfig.setPostMax(configCache.getPostMax());
			bbsConfig.setPostMaxDate(configCache.getPostMaxDate());
			bbsConfig.setPostToday(0);
			bbsConfig.setPostTotal(configCache.getPostTotal());
			bbsConfig.setPostYesterday(configCache.getPostToday());
			bbsConfig.setTopicTotal(configCache.getTopicTotal());
			bbsConfig.setUserTotal(configCache.getUserTotal());
			configCache.setPostYestoday(configCache.getPostToday());
			configCache.setPostToday(0);
			cache.put(new Element(siteId, configCache));
		}
		update(bbsConfig);

		if (logger.isDebugEnabled()) {
			logger.debug("updateConfigForDay(Integer) - end"); //$NON-NLS-1$
		}
		return bbsConfig;
	}

	private BbsConfigDao dao;
	private Ehcache cache;
	private BbsForumMng bbsForumMng;

	@Autowired
	public void setCache(@Qualifier("bbsconfigCount") Ehcache cache) {
		this.cache = cache;
	}

	@Autowired
	public void setDao(BbsConfigDao dao) {
		this.dao = dao;
	}

	@Autowired
	public void setBbsForumMng(BbsForumMng bbsForumMng) {
		this.bbsForumMng = bbsForumMng;
	}

}
