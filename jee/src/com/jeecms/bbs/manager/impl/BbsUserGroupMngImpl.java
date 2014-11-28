package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsUserDao;
import com.jeecms.bbs.dao.BbsUserGroupDao;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserGroup;
import com.jeecms.bbs.manager.BbsPostTypeMng;
import com.jeecms.bbs.manager.BbsUserGroupMng;
import com.jeecms.common.hibernate3.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.core.entity.CmsSite;

@Service
@Transactional
public class BbsUserGroupMngImpl implements BbsUserGroupMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsUserGroupMngImpl.class);

	@Transactional(readOnly = true)
	public BbsUserGroup findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsUserGroup entity = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	@Transactional(readOnly = true)
	public BbsUserGroup getRegDef() {
		if (logger.isDebugEnabled()) {
			logger.debug("getRegDef() - start"); //$NON-NLS-1$
		}

		BbsUserGroup returnBbsUserGroup = dao.getRegDef();
		if (logger.isDebugEnabled()) {
			logger.debug("getRegDef() - end"); //$NON-NLS-1$
		}
		return returnBbsUserGroup;
	}

	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPage(int, int) - start"); //$NON-NLS-1$
		}

		Pagination returnPagination = dao.getPage(pageNo, pageSize);
		if (logger.isDebugEnabled()) {
			logger.debug("getPage(int, int) - end"); //$NON-NLS-1$
		}
		return returnPagination;
	}

	public void updateRegDef(Integer regDefId, Integer siteId) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateRegDef(Integer, Integer) - start"); //$NON-NLS-1$
		}

		if (regDefId != null) {
			for (BbsUserGroup g : getList(siteId)) {
				if (g.getId().equals(regDefId)) {
					g.setDefault(true);
				} else {
					g.setDefault(false);
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("updateRegDef(Integer, Integer) - end"); //$NON-NLS-1$
		}
	}

	public BbsUserGroup save(BbsUserGroup bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsUserGroup) - start"); //$NON-NLS-1$
		}

		dao.save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsUserGroup) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsUserGroup update(BbsUserGroup bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsUserGroup) - start"); //$NON-NLS-1$
		}

		Updater<BbsUserGroup> updater = new Updater<BbsUserGroup>(bean);
		BbsUserGroup entity = dao.updateByUpdater(updater);

		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsUserGroup) - end"); //$NON-NLS-1$
		}
		return entity;
	}
	
	public BbsUserGroup update(BbsUserGroup bean,Integer postTypeIds[]) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsUserGroup, Integer[]) - start"); //$NON-NLS-1$
		}

		Updater<BbsUserGroup> updater = new Updater<BbsUserGroup>(bean);
		BbsUserGroup entity = dao.updateByUpdater(updater);
		//先清除帖子分类
		entity.getPostTypes().clear();
		if(postTypeIds!=null){
			for(Integer typeId:postTypeIds){
				entity.addToPostTypes(bbsPostTypeMng.findById(typeId));
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsUserGroup, Integer[]) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsUserGroup deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsUserGroup bean = dao.deleteById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsUserGroup[] deleteByIds(Integer[] ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - start"); //$NON-NLS-1$
		}

		BbsUserGroup[] beans = new BbsUserGroup[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}

	public void saveOrUpdateGroups(Integer siteId, short groupType,
			String[] name, String[] imgPath, Integer[] id, Long[] point) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveOrUpdateGroups(Integer, short, String[], String[], Integer[], Long[]) - start"); //$NON-NLS-1$
		}

		List<BbsUserGroup> list = getList(siteId);
		if (id != null && id.length > 0) {
			for (int i = 0; i < id.length; i++) {
				BbsUserGroup group = findById(id[i]);
				if (list.contains(group)) {
					group.setName(name[i]);
					group.setImgPath(imgPath[i]);
					group.setPoint(point[i]);
					update(group);
				}
			}
			for (int i = id.length; i < name.length; i++) {
				if (name[i] == null || StringUtils.isBlank(name[i])) {
					continue;
				}
				BbsUserGroup group = new BbsUserGroup();
				group.setName(name[i]);
				group.setImgPath(imgPath[i]);
				group.setPoint(point[i]);
				group.setSite(new CmsSite(siteId));
				group.setType(groupType);
				group.setDefault(false);
				group.setGradeNum(0);
				save(group);
			}
		} else {
			for (int i = 0; i < name.length; i++) {
				if (name[i] == null || StringUtils.isBlank(name[i])) {
					continue;
				}
				BbsUserGroup group = new BbsUserGroup();
				group.setName(name[i]);
				group.setImgPath(imgPath[i]);
				group.setPoint(point[i]);
				group.setSite(new CmsSite(siteId));
				group.setType(groupType);
				group.setDefault(false);
				group.setGradeNum(0);
				save(group);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("saveOrUpdateGroups(Integer, short, String[], String[], Integer[], Long[]) - end"); //$NON-NLS-1$
		}
	}

	public List<BbsUserGroup> getList(Integer siteId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer) - start"); //$NON-NLS-1$
		}

		List<BbsUserGroup> returnList = dao.getList(siteId);
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	public List<BbsUserGroup> getList(Integer siteId, short groupType) {
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer, short) - start"); //$NON-NLS-1$
		}

		List<BbsUserGroup> groupList = getList(siteId);
		int i = 0;
		while (i < groupList.size()) {
			BbsUserGroup group = groupList.get(i);
			if (group.getType() != groupType) {
				groupList.remove(i);
			} else {
				i++;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer, short) - end"); //$NON-NLS-1$
		}
		return groupList;
	}

	private BbsUserGroupDao dao;

	@Autowired
	public void setDao(BbsUserGroupDao dao) {
		this.dao = dao;
	}

	/* (non-Javadoc)
	 * @see com.jeecms.bbs.manager.BbsUserGroupMng#findNearByPoint(java.lang.Integer)
	 */
	public BbsUserGroup findNearByPoint(Long point,BbsUser user) {
		if (logger.isDebugEnabled()) {
			logger.debug("findNearByPoint(Long, BbsUser) - start"); //$NON-NLS-1$
		}

		// TODO Auto-generated method stub
		BbsUserGroup group = dao.findNearByPoint(point);
 		//user = bbsUserDao.findById(user.getId());
		if(user.getGroup().getType()==1){
			user.setGroup(group);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("findNearByPoint(Long, BbsUser) - end"); //$NON-NLS-1$
		}
 		return group;
	}
	@Autowired
	private BbsUserDao bbsUserDao;
	@Autowired
	private BbsPostTypeMng bbsPostTypeMng;

}
