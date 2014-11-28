package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsUserExtDao;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserExt;
import com.jeecms.bbs.manager.BbsUserExtMng;
import com.jeecms.common.hibernate3.Updater;

@Service
@Transactional
public class BbsUserExtMngImpl implements BbsUserExtMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsUserExtMngImpl.class);

	public BbsUserExt save(BbsUserExt ext, BbsUser user) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsUserExt, BbsUser) - start"); //$NON-NLS-1$
		}

		ext.blankToNull();
		ext.setUser(user);
		dao.save(ext);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsUserExt, BbsUser) - end"); //$NON-NLS-1$
		}
		return ext;
	}

	public BbsUserExt update(BbsUserExt ext, BbsUser user) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsUserExt, BbsUser) - start"); //$NON-NLS-1$
		}

		BbsUserExt entity = dao.findById(user.getId());
		if (entity == null) {
			entity = save(ext, user);
			user.getUserExtSet().add(entity);

			if (logger.isDebugEnabled()) {
				logger.debug("update(BbsUserExt, BbsUser) - end"); //$NON-NLS-1$
			}
			return entity;
		} else {
			Updater<BbsUserExt> updater = new Updater<BbsUserExt>(ext);
			updater.include("gender");
			updater.include("birthday");
			ext = dao.updateByUpdater(updater);
			ext.blankToNull();

			if (logger.isDebugEnabled()) {
				logger.debug("update(BbsUserExt, BbsUser) - end"); //$NON-NLS-1$
			}
			return ext;
		}
	}

	private BbsUserExtDao dao;

	@Autowired
	public void setDao(BbsUserExtDao dao) {
		this.dao = dao;
	}
}