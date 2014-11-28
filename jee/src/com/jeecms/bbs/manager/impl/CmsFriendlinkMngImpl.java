package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.CmsFriendlinkDao;
import com.jeecms.bbs.entity.CmsFriendlink;
import com.jeecms.bbs.manager.CmsFriendlinkCtgMng;
import com.jeecms.bbs.manager.CmsFriendlinkMng;
import com.jeecms.common.hibernate3.Updater;

@Service
@Transactional
public class CmsFriendlinkMngImpl implements CmsFriendlinkMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CmsFriendlinkMngImpl.class);

	@Transactional(readOnly = true)
	public List<CmsFriendlink> getList(Integer siteId, Integer ctgId,
			Boolean enabled) {
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer, Integer, Boolean) - start"); //$NON-NLS-1$
		}

		List<CmsFriendlink> list = dao.getList(siteId, ctgId, enabled);

		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer, Integer, Boolean) - end"); //$NON-NLS-1$
		}
		return list;
	}

	@Transactional(readOnly = true)
	public int countByCtgId(Integer ctgId) {
		if (logger.isDebugEnabled()) {
			logger.debug("countByCtgId(Integer) - start"); //$NON-NLS-1$
		}

		int returnint = dao.countByCtgId(ctgId);
		if (logger.isDebugEnabled()) {
			logger.debug("countByCtgId(Integer) - end"); //$NON-NLS-1$
		}
		return returnint;
	}

	@Transactional(readOnly = true)
	public CmsFriendlink findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		CmsFriendlink entity = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public int updateViews(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateViews(Integer) - start"); //$NON-NLS-1$
		}

		CmsFriendlink link = findById(id);
		if (link != null) {
			link.setViews(link.getViews() + 1);
		}
		int returnint = link.getViews();
		if (logger.isDebugEnabled()) {
			logger.debug("updateViews(Integer) - end"); //$NON-NLS-1$
		}
		return returnint;
	}

	public CmsFriendlink save(CmsFriendlink bean, Integer ctgId) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(CmsFriendlink, Integer) - start"); //$NON-NLS-1$
		}

		bean.setCategory(cmsFriendlinkCtgMng.findById(ctgId));
		bean.init();
		dao.save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(CmsFriendlink, Integer) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public CmsFriendlink update(CmsFriendlink bean, Integer ctgId) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(CmsFriendlink, Integer) - start"); //$NON-NLS-1$
		}

		Updater<CmsFriendlink> updater = new Updater<CmsFriendlink>(bean);
		bean = dao.updateByUpdater(updater);
		if (ctgId != null) {
			bean.setCategory(cmsFriendlinkCtgMng.findById(ctgId));
		}
		bean.blankToNull();

		if (logger.isDebugEnabled()) {
			logger.debug("update(CmsFriendlink, Integer) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public void updatePriority(Integer[] ids, Integer[] priorities) {
		if (logger.isDebugEnabled()) {
			logger.debug("updatePriority(Integer[], Integer[]) - start"); //$NON-NLS-1$
		}

		if (ids == null || priorities == null || ids.length <= 0
				|| ids.length != priorities.length) {
			if (logger.isDebugEnabled()) {
				logger.debug("updatePriority(Integer[], Integer[]) - end"); //$NON-NLS-1$
			}
			return;
		}
		CmsFriendlink link;
		for (int i = 0, len = ids.length; i < len; i++) {
			link = findById(ids[i]);
			link.setPriority(priorities[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("updatePriority(Integer[], Integer[]) - end"); //$NON-NLS-1$
		}
	}

	public CmsFriendlink deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		CmsFriendlink bean = dao.deleteById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public CmsFriendlink[] deleteByIds(Integer[] ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - start"); //$NON-NLS-1$
		}

		CmsFriendlink[] beans = new CmsFriendlink[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}

	private CmsFriendlinkDao dao;
	private CmsFriendlinkCtgMng cmsFriendlinkCtgMng;

	@Autowired
	public void setDao(CmsFriendlinkDao dao) {
		this.dao = dao;
	}

	@Autowired
	public void setCmsFriendlinkCtgMng(CmsFriendlinkCtgMng cmsFriendlinkCtgMng) {
		this.cmsFriendlinkCtgMng = cmsFriendlinkCtgMng;
	}
}