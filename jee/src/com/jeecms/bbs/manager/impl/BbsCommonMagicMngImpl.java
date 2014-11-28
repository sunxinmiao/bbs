package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsCommonMagicDao;
import com.jeecms.bbs.entity.BbsCommonMagic;
import com.jeecms.bbs.entity.BbsUserGroup;
import com.jeecms.bbs.manager.BbsCommonMagicMng;
import com.jeecms.bbs.manager.BbsUserGroupMng;
import com.jeecms.common.hibernate3.Updater;

@Service
@Transactional
public class BbsCommonMagicMngImpl implements BbsCommonMagicMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsCommonMagicMngImpl.class);

	@Transactional(readOnly = true)
	public List<BbsCommonMagic> getList() {
		if (logger.isDebugEnabled()) {
			logger.debug("getList() - start"); //$NON-NLS-1$
		}

		List<BbsCommonMagic> returnList = dao.getList();
		if (logger.isDebugEnabled()) {
			logger.debug("getList() - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	@Transactional(readOnly = true)
	public BbsCommonMagic findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsCommonMagic config = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return config;
	}

	@Transactional(readOnly = true)
	public BbsCommonMagic findByIdentifier(String mid) {
		if (logger.isDebugEnabled()) {
			logger.debug("findByIdentifier(String) - start"); //$NON-NLS-1$
		}

		BbsCommonMagic returnBbsCommonMagic = dao.findByIdentifier(mid);
		if (logger.isDebugEnabled()) {
			logger.debug("findByIdentifier(String) - end"); //$NON-NLS-1$
		}
		return returnBbsCommonMagic;
	}

	public BbsCommonMagic update(BbsCommonMagic bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsCommonMagic) - start"); //$NON-NLS-1$
		}

		BbsCommonMagic entity = findById(bean.getId());
		Updater<BbsCommonMagic> updater = new Updater<BbsCommonMagic>(bean);
		entity = dao.updateByUpdater(updater);

		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsCommonMagic) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsCommonMagic updateByGroup(BbsCommonMagic bean,
			Integer[] groupIds, Integer[] beUsedGroupIds) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateByGroup(BbsCommonMagic, Integer[], Integer[]) - start"); //$NON-NLS-1$
		}

		BbsCommonMagic entity = findById(bean.getId());
		Updater<BbsCommonMagic> updater = new Updater<BbsCommonMagic>(bean);
		entity = dao.updateByUpdater(updater);
		// 可使用组权限
		Set<BbsUserGroup> groups = entity.getUseGroups();
		groups.clear();
		if (groupIds != null && groupIds.length > 0) {
			for (Integer groupId : groupIds) {
				entity.addToGroups(groupMng.findById(groupId));
			}
		}
		// 允许被使用的用户组
		Set<BbsUserGroup> beUsedGroups = entity.getToUseGroups();
		beUsedGroups.clear();
		if (beUsedGroupIds != null&& beUsedGroupIds.length > 0) {
			for (Integer groupId : beUsedGroupIds) {
				entity.addToToUseGroups(groupMng.findById(groupId));
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("updateByGroup(BbsCommonMagic, Integer[], Integer[]) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	private BbsCommonMagicDao dao;
	@Autowired
	private BbsUserGroupMng groupMng;

	@Autowired
	public void setDao(BbsCommonMagicDao dao) {
		this.dao = dao;
	}

}
