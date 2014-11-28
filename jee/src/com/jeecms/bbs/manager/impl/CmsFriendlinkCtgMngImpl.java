package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.CmsFriendlinkCtgDao;
import com.jeecms.bbs.entity.CmsFriendlinkCtg;
import com.jeecms.bbs.manager.CmsFriendlinkCtgMng;
import com.jeecms.common.hibernate3.Updater;

@Service
@Transactional
public class CmsFriendlinkCtgMngImpl implements CmsFriendlinkCtgMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CmsFriendlinkCtgMngImpl.class);

	@Transactional(readOnly = true)
	public List<CmsFriendlinkCtg> getList(Integer siteId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer) - start"); //$NON-NLS-1$
		}

		List<CmsFriendlinkCtg> returnList = dao.getList(siteId);
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	public int countBySiteId(Integer siteId) {
		if (logger.isDebugEnabled()) {
			logger.debug("countBySiteId(Integer) - start"); //$NON-NLS-1$
		}

		int returnint = dao.countBySiteId(siteId);
		if (logger.isDebugEnabled()) {
			logger.debug("countBySiteId(Integer) - end"); //$NON-NLS-1$
		}
		return returnint;
	}

	@Transactional(readOnly = true)
	public CmsFriendlinkCtg findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		CmsFriendlinkCtg entity = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public CmsFriendlinkCtg save(CmsFriendlinkCtg bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(CmsFriendlinkCtg) - start"); //$NON-NLS-1$
		}

		dao.save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(CmsFriendlinkCtg) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public CmsFriendlinkCtg update(CmsFriendlinkCtg bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(CmsFriendlinkCtg) - start"); //$NON-NLS-1$
		}

		Updater<CmsFriendlinkCtg> updater = new Updater<CmsFriendlinkCtg>(bean);
		bean = dao.updateByUpdater(updater);

		if (logger.isDebugEnabled()) {
			logger.debug("update(CmsFriendlinkCtg) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public void updateFriendlinkCtgs(Integer[] ids, String[] names,
			Integer[] priorities) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateFriendlinkCtgs(Integer[], String[], Integer[]) - start"); //$NON-NLS-1$
		}

		if (ids == null || ids.length == 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("updateFriendlinkCtgs(Integer[], String[], Integer[]) - end"); //$NON-NLS-1$
			}
			return;
		}
		CmsFriendlinkCtg ctg;
		for (int i = 0; i < ids.length; i++) {
			ctg = dao.findById(ids[i]);
			ctg.setName(names[i]);
			ctg.setPriority(priorities[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("updateFriendlinkCtgs(Integer[], String[], Integer[]) - end"); //$NON-NLS-1$
		}
	}

	public CmsFriendlinkCtg deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		CmsFriendlinkCtg bean = dao.deleteById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public CmsFriendlinkCtg[] deleteByIds(Integer[] ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - start"); //$NON-NLS-1$
		}

		CmsFriendlinkCtg[] beans = new CmsFriendlinkCtg[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}

	private CmsFriendlinkCtgDao dao;

	@Autowired
	public void setDao(CmsFriendlinkCtgDao dao) {
		this.dao = dao;
	}
}