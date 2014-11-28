package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;

import static com.jeecms.bbs.entity.BbsFriendShip.APPLYING;
import static com.jeecms.bbs.entity.BbsFriendShip.ACCEPT;
import static com.jeecms.bbs.entity.BbsFriendShip.REFUSE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsFriendShipDao;
import com.jeecms.bbs.entity.BbsFriendShip;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsFriendShipMng;
import com.jeecms.common.hibernate3.Updater;
import com.jeecms.common.page.Pagination;

@Service
@Transactional
public class BbsFriendShipMngImpl implements BbsFriendShipMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsFriendShipMngImpl.class);

	@Transactional(readOnly = true)
	public BbsFriendShip findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsFriendShip entity = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsFriendShip save(BbsFriendShip bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsFriendShip) - start"); //$NON-NLS-1$
		}

		dao.save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsFriendShip) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsFriendShip update(BbsFriendShip bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsFriendShip) - start"); //$NON-NLS-1$
		}

		Updater<BbsFriendShip> updater = new Updater<BbsFriendShip>(bean);
		bean = dao.updateByUpdater(updater);

		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsFriendShip) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsFriendShip deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsFriendShip bean = dao.deleteById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsFriendShip[] deleteByIds(Integer[] ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - start"); //$NON-NLS-1$
		}

		BbsFriendShip[] beans = new BbsFriendShip[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}

	public BbsFriendShip getFriendShip(Integer userId, Integer friendId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getFriendShip(Integer, Integer) - start"); //$NON-NLS-1$
		}

		BbsFriendShip returnBbsFriendShip = dao.getFriendShip(userId, friendId);
		if (logger.isDebugEnabled()) {
			logger.debug("getFriendShip(Integer, Integer) - end"); //$NON-NLS-1$
		}
		return returnBbsFriendShip;
	}

	public void apply(BbsUser user, BbsUser friend) {
		if (logger.isDebugEnabled()) {
			logger.debug("apply(BbsUser, BbsUser) - start"); //$NON-NLS-1$
		}

		BbsFriendShip bean = getFriendShip(user.getId(), friend.getId());
		if (bean != null) {
			bean.setStatus(APPLYING);
		}else{
			bean = new BbsFriendShip();
			bean.setUser(user);
			bean.setFriend(friend);
			bean.init();
			save(bean);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("apply(BbsUser, BbsUser) - end"); //$NON-NLS-1$
		}
	}
	
	public void accept(BbsFriendShip friendShip) {
		if (logger.isDebugEnabled()) {
			logger.debug("accept(BbsFriendShip) - start"); //$NON-NLS-1$
		}

		friendShip.setStatus(ACCEPT);
		BbsFriendShip bean = new BbsFriendShip();
		bean.setUser(friendShip.getFriend());
		bean.setFriend(friendShip.getUser());
		bean.setStatus(ACCEPT);
		save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("accept(BbsFriendShip) - end"); //$NON-NLS-1$
		}
	}
	
	public void refuse(BbsFriendShip friendShip) {
		if (logger.isDebugEnabled()) {
			logger.debug("refuse(BbsFriendShip) - start"); //$NON-NLS-1$
		}

		friendShip.setStatus(REFUSE);

		if (logger.isDebugEnabled()) {
			logger.debug("refuse(BbsFriendShip) - end"); //$NON-NLS-1$
		}
	}

	public Pagination getPageByUserId(Integer userId, Integer pageNo,
			Integer pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPageByUserId(Integer, Integer, Integer) - start"); //$NON-NLS-1$
		}

		Pagination returnPagination = dao.getPageByUserId(userId, pageNo, pageSize);
		if (logger.isDebugEnabled()) {
			logger.debug("getPageByUserId(Integer, Integer, Integer) - end"); //$NON-NLS-1$
		}
		return returnPagination;
	}
	
	public Pagination getApplyByUserId(Integer userId, Integer pageNo,
			Integer pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getApplyByUserId(Integer, Integer, Integer) - start"); //$NON-NLS-1$
		}

		Pagination returnPagination = dao.getApplyByUserId(userId, pageNo, pageSize);
		if (logger.isDebugEnabled()) {
			logger.debug("getApplyByUserId(Integer, Integer, Integer) - end"); //$NON-NLS-1$
		}
		return returnPagination;
	}

	private BbsFriendShipDao dao;

	@Autowired
	public void setDao(BbsFriendShipDao dao) {
		this.dao = dao;
	}
}