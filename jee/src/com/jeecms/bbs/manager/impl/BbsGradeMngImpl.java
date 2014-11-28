package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsGradeDao;
import com.jeecms.bbs.entity.BbsGrade;
import com.jeecms.bbs.entity.BbsPost;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsGradeMng;
import com.jeecms.common.hibernate3.Updater;
import com.jeecms.common.page.Pagination;

@Service
@Transactional
public class BbsGradeMngImpl implements BbsGradeMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsGradeMngImpl.class);

	public BbsGrade saveGrade(BbsGrade entity, BbsUser bbsuser, BbsPost post) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveGrade(BbsGrade, BbsUser, BbsPost) - start"); //$NON-NLS-1$
		}

		entity.setGrader(bbsuser);
		entity.setGradeTime(new Timestamp(System.currentTimeMillis()));
		entity.setPost(post);
		if (bbsuser.getGradeToday() != null) {
			bbsuser.setGradeToday(bbsuser.getGradeToday() + entity.getScore());
		} else {
			bbsuser.setGradeToday(entity.getScore());
		}
		post.getCreater().setPoint(
				post.getCreater().getPoint() + entity.getScore());
		dao.save(entity);

		if (logger.isDebugEnabled()) {
			logger.debug("saveGrade(BbsGrade, BbsUser, BbsPost) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPage(int, int) - start"); //$NON-NLS-1$
		}

		Pagination page = dao.getPage(pageNo, pageSize);

		if (logger.isDebugEnabled()) {
			logger.debug("getPage(int, int) - end"); //$NON-NLS-1$
		}
		return page;
	}

	@Transactional(readOnly = true)
	public BbsGrade findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsGrade entity = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsGrade save(BbsGrade bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsGrade) - start"); //$NON-NLS-1$
		}

		dao.save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsGrade) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsGrade update(BbsGrade bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsGrade) - start"); //$NON-NLS-1$
		}

		Updater<BbsGrade> updater = new Updater<BbsGrade>(bean);
		bean = dao.updateByUpdater(updater);

		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsGrade) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsGrade deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsGrade bean = dao.deleteById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsGrade[] deleteByIds(Integer[] ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - start"); //$NON-NLS-1$
		}

		BbsGrade[] beans = new BbsGrade[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}

	private BbsGradeDao dao;

	@Autowired
	public void setDao(BbsGradeDao dao) {
		this.dao = dao;
	}

}
