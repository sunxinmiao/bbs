package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsForumDao;
import com.jeecms.bbs.entity.BbsCategory;
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.manager.BbsCategoryMng;
import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.common.hibernate3.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.core.entity.CmsSite;

@Service
@Transactional
public class BbsForumMngImpl implements BbsForumMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsForumMngImpl.class);

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
	public List<BbsForum> getList(Integer siteId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer) - start"); //$NON-NLS-1$
		}

		List<BbsForum> returnList = dao.getList(siteId);
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	@Transactional(readOnly = true)
	public List<BbsForum> getList(Integer siteId, Integer categoryId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer, Integer) - start"); //$NON-NLS-1$
		}

		List<BbsForum> returnList = dao.getList(siteId, categoryId);
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer, Integer) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	@Transactional(readOnly = true)
	public int countPath(Integer siteId, String path) {
		if (logger.isDebugEnabled()) {
			logger.debug("countPath(Integer, String) - start"); //$NON-NLS-1$
		}

		int returnint = dao.countPath(siteId, path);
		if (logger.isDebugEnabled()) {
			logger.debug("countPath(Integer, String) - end"); //$NON-NLS-1$
		}
		return returnint;
	}

	@Transactional(readOnly = true)
	public BbsForum getByPath(Integer siteId, String path) {
		if (logger.isDebugEnabled()) {
			logger.debug("getByPath(Integer, String) - start"); //$NON-NLS-1$
		}

		BbsForum returnBbsForum = dao.getByPath(siteId, path);
		if (logger.isDebugEnabled()) {
			logger.debug("getByPath(Integer, String) - end"); //$NON-NLS-1$
		}
		return returnBbsForum;
	}

	@Transactional(readOnly = true)
	public BbsForum findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsForum entity = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsForum save(BbsForum bean, Integer categoryId, CmsSite site,
			Integer[] views, Integer[] topics, Integer[] replies) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsForum, Integer, CmsSite, Integer[], Integer[], Integer[]) - start"); //$NON-NLS-1$
		}

		BbsCategory category = bbsCategoryMng.findById(categoryId);
		bean.setCategory(category);
		bean.setSite(site);
		if (views != null && views.length > 0) {
			String v = ",";
			for (Integer vgroupId : views) {
				v = v + vgroupId + ",";
			}
			bean.setGroupViews(v);
		}
		if (topics != null && topics.length > 0) {
			String t = ",";
			for (Integer tgroupId : topics) {
				t = t + tgroupId + ",";
			}
			bean.setGroupTopics(t);
		}
		if (replies != null && replies.length > 0) {
			String r = ",";
			for (Integer rgroupId : replies) {
				r = r + rgroupId + ",";
			}
			bean.setGroupReplies(r);
		}
		bean.init();
		dao.save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsForum, Integer, CmsSite, Integer[], Integer[], Integer[]) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsForum save(BbsForum bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsForum) - start"); //$NON-NLS-1$
		}

		dao.save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsForum) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsForum update(BbsForum bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsForum) - start"); //$NON-NLS-1$
		}

		Updater<BbsForum> updater = new Updater<BbsForum>(bean);
		bean = dao.updateByUpdater(updater);

		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsForum) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsForum update(BbsForum bean, Integer categoryId, CmsSite site,
			Integer[] views, Integer[] topics, Integer[] replies) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsForum, Integer, CmsSite, Integer[], Integer[], Integer[]) - start"); //$NON-NLS-1$
		}

		Updater<BbsForum> updater = new Updater<BbsForum>(bean);
		bean = dao.updateByUpdater(updater);
		BbsCategory category = bbsCategoryMng.findById(categoryId);
		bean.setCategory(category);
		bean.setSite(site);
		if (views != null && views.length > 0) {
			String v = ",";
			for (Integer vgroupId : views) {
				v = v + vgroupId + ",";
			}
			bean.setGroupViews(v);
		} else {
			bean.setGroupViews("");
		}
		if (topics != null && topics.length > 0) {
			String t = ",";
			for (Integer tgroupId : topics) {
				t = t + tgroupId + ",";
			}
			bean.setGroupTopics(t);
		} else {
			bean.setGroupTopics("");
		}
		if (replies != null && replies.length > 0) {
			String r = ",";
			for (Integer rgroupId : replies) {
				r = r + rgroupId + ",";
			}
			bean.setGroupReplies(r);
		} else {
			bean.setGroupReplies("");
		}
		bean.init();

		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsForum, Integer, CmsSite, Integer[], Integer[], Integer[]) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsForum deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsForum bean = dao.deleteById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsForum[] deleteByIds(Integer[] ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - start"); //$NON-NLS-1$
		}

		BbsForum[] beans = new BbsForum[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}
	
	public String getModerators(Integer siteId){
		if (logger.isDebugEnabled()) {
			logger.debug("getModerators(Integer) - start"); //$NON-NLS-1$
		}

		List<BbsForum>forums=getList(siteId);
		StringBuffer moderatorsBuf=new StringBuffer();
		for(BbsForum forum:forums){
			moderatorsBuf.append(forum.getModerators());
		}
		String returnString = moderatorsBuf.toString();
		if (logger.isDebugEnabled()) {
			logger.debug("getModerators(Integer) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	private BbsCategoryMng bbsCategoryMng;
	private BbsForumDao dao;

	@Autowired
	public void setDao(BbsForumDao dao) {
		this.dao = dao;
	}

	@Autowired
	public void setBbsCategoryMng(BbsCategoryMng bbsCategoryMng) {
		this.bbsCategoryMng = bbsCategoryMng;
	}

	public void updateAll_topic_today() {
		if (logger.isDebugEnabled()) {
			logger.debug("updateAll_topic_today() - start"); //$NON-NLS-1$
		}

		// TODO Auto-generated method stub
		dao.updateAll_topic_today();

		if (logger.isDebugEnabled()) {
			logger.debug("updateAll_topic_today() - end"); //$NON-NLS-1$
		}
	}
}