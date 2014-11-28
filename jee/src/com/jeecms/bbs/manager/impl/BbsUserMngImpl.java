package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsUserDao;
import com.jeecms.bbs.entity.BbsCommonMagic;
import com.jeecms.bbs.entity.BbsMemberMagic;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserExt;
import com.jeecms.bbs.entity.BbsUserGroup;
import com.jeecms.bbs.manager.BbsCommonMagicMng;
import com.jeecms.bbs.manager.BbsMemberMagicMng;
import com.jeecms.bbs.manager.BbsUserExtMng;
import com.jeecms.bbs.manager.BbsUserGroupMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.common.email.EmailSender;
import com.jeecms.common.email.MessageTemplate;
import com.jeecms.common.hibernate3.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.core.entity.UnifiedUser;
import com.jeecms.core.manager.UnifiedUserMng;

@Service
@Transactional
public class BbsUserMngImpl implements BbsUserMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsUserMngImpl.class);

	@Transactional(readOnly = true)
	public Pagination getPage(String username, String email, Integer groupId,
			Boolean disabled, Boolean admin, Long pointMin, Long pointMax,
			Long prestigeMin, Long prestigeMax, Integer orderBy, int pageNo,
			int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPage(String, String, Integer, Boolean, Boolean, Long, Long, Long, Long, Integer, int, int) - start"); //$NON-NLS-1$
		}

		Pagination page = dao.getPage(username, email, groupId, disabled,
				admin, pointMin, pointMax, prestigeMin, prestigeMax, orderBy,
				pageNo, pageSize);

		if (logger.isDebugEnabled()) {
			logger.debug("getPage(String, String, Integer, Boolean, Boolean, Long, Long, Long, Long, Integer, int, int) - end"); //$NON-NLS-1$
		}
		return page;
	}

	@Transactional(readOnly = true)
	public List<BbsUser> getAdminList(Integer siteId, Boolean allChannel,
			Boolean disabled, Integer rank) {
		if (logger.isDebugEnabled()) {
			logger.debug("getAdminList(Integer, Boolean, Boolean, Integer) - start"); //$NON-NLS-1$
		}

		List<BbsUser> returnList = dao.getAdminList(siteId, allChannel, disabled, rank);
		if (logger.isDebugEnabled()) {
			logger.debug("getAdminList(Integer, Boolean, Boolean, Integer) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	@Transactional(readOnly = true)
	public BbsUser findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsUser entity = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	@Transactional(readOnly = true)
	public BbsUser findByUsername(String username) {
		if (logger.isDebugEnabled()) {
			logger.debug("findByUsername(String) - start"); //$NON-NLS-1$
		}

		BbsUser entity = dao.findByUsername(username);

		if (logger.isDebugEnabled()) {
			logger.debug("findByUsername(String) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsUser registerMember(String username, String email,
			String password, String ip, Integer groupId, BbsUserExt userExt) {
		if (logger.isDebugEnabled()) {
			logger.debug("registerMember(String, String, String, String, Integer, BbsUserExt) - start"); //$NON-NLS-1$
		}

		UnifiedUser unifiedUser = unifiedUserMng.save(username, email,
				password, ip);
		BbsUser user = new BbsUser();
		user.forMember(unifiedUser);
		BbsUserGroup group = null;
		if (groupId != null) {
			group = bbsUserGroupMng.findById(groupId);
		} else {
			group = bbsUserGroupMng.getRegDef();
		}
		if (group == null) {
			throw new RuntimeException(
					"register default member group not found!");
		}
		user.setGroup(group);
		user.init();
		dao.save(user);
		bbsUserExtMng.save(userExt, user);

		if (logger.isDebugEnabled()) {
			logger.debug("registerMember(String, String, String, String, Integer, BbsUserExt) - end"); //$NON-NLS-1$
		}
		return user;
	}

	public BbsUser registerMember(String username, String email,
			String password, String ip, Integer groupId, BbsUserExt userExt,
			Boolean activation, EmailSender sender, MessageTemplate msgTpl) {
		if (logger.isDebugEnabled()) {
			logger.debug("registerMember(String, String, String, String, Integer, BbsUserExt, Boolean, EmailSender, MessageTemplate) - start"); //$NON-NLS-1$
		}

		UnifiedUser unifiedUser = unifiedUserMng.save(username, email,
				password, ip, activation, sender, msgTpl);
		BbsUser user = new BbsUser();
		user.forMember(unifiedUser);
		BbsUserGroup group = null;
		if (groupId != null) {
			group = bbsUserGroupMng.findById(groupId);
		} else {
			group = bbsUserGroupMng.getRegDef();
		}
		if (group == null) {
			throw new RuntimeException(
					"register default member group not found!");
		}
		user.setGroup(group);
		user.init();
		dao.save(user);
		bbsUserExtMng.save(userExt, user);

		if (logger.isDebugEnabled()) {
			logger.debug("registerMember(String, String, String, String, Integer, BbsUserExt, Boolean, EmailSender, MessageTemplate) - end"); //$NON-NLS-1$
		}
		return user;
	}

	public void updateLoginInfo(Integer userId, String ip) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateLoginInfo(Integer, String) - start"); //$NON-NLS-1$
		}

		Date now = new Timestamp(System.currentTimeMillis());
		BbsUser user = findById(userId);
		if (user != null) {
			user.setLoginCount(user.getLoginCount() + 1);
			user.setLastLoginIp(ip);
			user.setLastLoginTime(now);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("updateLoginInfo(Integer, String) - end"); //$NON-NLS-1$
		}
	}

	public void updateUploadSize(Integer userId, Integer size) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateUploadSize(Integer, Integer) - start"); //$NON-NLS-1$
		}

		BbsUser user = findById(userId);
		user.setUploadTotal(user.getUploadTotal() + size);
		if (user.getUploadDate() != null) {
			if (BbsUser.isToday(user.getUploadDate())) {
				size += user.getUploadSize();
			}
		}
		user.setUploadDate(new java.sql.Date(System.currentTimeMillis()));
		user.setUploadSize(size);

		if (logger.isDebugEnabled()) {
			logger.debug("updateUploadSize(Integer, Integer) - end"); //$NON-NLS-1$
		}
	}

	public boolean isPasswordValid(Integer id, String password) {
		if (logger.isDebugEnabled()) {
			logger.debug("isPasswordValid(Integer, String) - start"); //$NON-NLS-1$
		}

		boolean returnboolean = unifiedUserMng.isPasswordValid(id, password);
		if (logger.isDebugEnabled()) {
			logger.debug("isPasswordValid(Integer, String) - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	public void updatePwdEmail(Integer id, String password, String email) {
		if (logger.isDebugEnabled()) {
			logger.debug("updatePwdEmail(Integer, String, String) - start"); //$NON-NLS-1$
		}

		BbsUser user = findById(id);
		if (!StringUtils.isBlank(email)) {
			user.setEmail(email);
		} else {
			user.setEmail(null);
		}
		unifiedUserMng.update(id, password, email);

		if (logger.isDebugEnabled()) {
			logger.debug("updatePwdEmail(Integer, String, String) - end"); //$NON-NLS-1$
		}
	}

	public BbsUser saveAdmin(String username, String email, String password,
			String ip, boolean viewOnly, boolean selfAdmin, int rank,
			Integer groupId, Integer[] roleIds, Integer[] channelIds,
			Integer[] siteIds, Byte[] steps, Boolean[] allChannels,
			BbsUserExt userExt) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveAdmin(String, String, String, String, boolean, boolean, int, Integer, Integer[], Integer[], Integer[], Byte[], Boolean[], BbsUserExt) - start"); //$NON-NLS-1$
		}

		UnifiedUser unifiedUser = unifiedUserMng.save(username, email,
				password, ip);
		BbsUser user = new BbsUser();
		user.forAdmin(unifiedUser, viewOnly, selfAdmin, rank);
		BbsUserGroup group = null;
		if (groupId != null) {
			group = bbsUserGroupMng.findById(groupId);
		} else {
			group = bbsUserGroupMng.getRegDef();
		}
		if (group == null) {
			throw new RuntimeException(
					"register default member group not setted!");
		}
		user.setGroup(group);
		user.init();
		dao.save(user);
		bbsUserExtMng.save(userExt, user);

		if (logger.isDebugEnabled()) {
			logger.debug("saveAdmin(String, String, String, String, boolean, boolean, int, Integer, Integer[], Integer[], Integer[], Byte[], Boolean[], BbsUserExt) - end"); //$NON-NLS-1$
		}
		return user;
	}

	public BbsUser updateAdmin(BbsUser bean, BbsUserExt ext, String password,
			Integer groupId, Integer[] roleIds, Integer[] channelIds,
			Integer[] siteIds, Byte[] steps, Boolean[] allChannels) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateAdmin(BbsUser, BbsUserExt, String, Integer, Integer[], Integer[], Integer[], Byte[], Boolean[]) - start"); //$NON-NLS-1$
		}

		Updater<BbsUser> updater = new Updater<BbsUser>(bean);
		updater.include("email");
		BbsUser user = dao.updateByUpdater(updater);
		user.setGroup(bbsUserGroupMng.findById(groupId));
		bbsUserExtMng.update(ext, user);
		unifiedUserMng.update(bean.getId(), password, bean.getEmail());

		if (logger.isDebugEnabled()) {
			logger.debug("updateAdmin(BbsUser, BbsUserExt, String, Integer, Integer[], Integer[], Integer[], Byte[], Boolean[]) - end"); //$NON-NLS-1$
		}
		return user;
	}

	public BbsUser updateMember(Integer id, String email, String password,
			Boolean isDisabled, String signed, String avatar, BbsUserExt ext,
			Integer groupId) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateMember(Integer, String, String, Boolean, String, String, BbsUserExt, Integer) - start"); //$NON-NLS-1$
		}

		BbsUser entity = findById(id);
		if (!StringUtils.isBlank(email)) {
			entity.setEmail(email);
		}
		if (isDisabled != null) {
			entity.setDisabled(isDisabled);
		}
		if (signed != null) {
			entity.setSigned(signed);
		}
		if (avatar != null) {
			entity.setAvatar(avatar);
		}
		if (groupId != null) {
			entity.setGroup(bbsUserGroupMng.findById(groupId));
		}
		bbsUserExtMng.update(ext, entity);
		unifiedUserMng.update(id, password, email);

		if (logger.isDebugEnabled()) {
			logger.debug("updateMember(Integer, String, String, Boolean, String, String, BbsUserExt, Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsUser deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		unifiedUserMng.deleteById(id);
		BbsUser bean = dao.deleteById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsUser[] deleteByIds(Integer[] ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - start"); //$NON-NLS-1$
		}

		BbsUser[] beans = new BbsUser[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}

	public boolean usernameNotExist(String username) {
		if (logger.isDebugEnabled()) {
			logger.debug("usernameNotExist(String) - start"); //$NON-NLS-1$
		}

		boolean returnboolean = dao.countByUsername(username) <= 0;
		if (logger.isDebugEnabled()) {
			logger.debug("usernameNotExist(String) - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	public boolean emailNotExist(String email) {
		if (logger.isDebugEnabled()) {
			logger.debug("emailNotExist(String) - start"); //$NON-NLS-1$
		}

		boolean returnboolean = dao.countByEmail(email) <= 0;
		if (logger.isDebugEnabled()) {
			logger.debug("emailNotExist(String) - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	@Transactional(readOnly = true)
	public List<BbsUser> getSuggestMember(String username, Integer count) {
		if (logger.isDebugEnabled()) {
			logger.debug("getSuggestMember(String, Integer) - start"); //$NON-NLS-1$
		}

		List<BbsUser> returnList = dao.getSuggestMember(username, count);
		if (logger.isDebugEnabled()) {
			logger.debug("getSuggestMember(String, Integer) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	public void updatePoint(Integer userId, Integer point, Integer prestige,
			String mid, int num, int operator) {
		if (logger.isDebugEnabled()) {
			logger.debug("updatePoint(Integer, Integer, Integer, String, int, int) - start"); //$NON-NLS-1$
		}

		BbsUser user = findById(userId);
		if (point != null) {
			user.setPoint(user.getPoint() + point);
		}
		if (prestige != null) {
			user.setPrestige(user.getPrestige() + prestige);
		}
		// operator=-1无须下面操作
		if (StringUtils.isNotBlank(mid) && operator != -1) {
			BbsMemberMagic magic;
			BbsCommonMagic comMagic = magicMng.findByIdentifier(mid);
			magic = user.getMemberMagic(mid);
			// operator==0出售道具，=1使用道具 =2丢弃道具 =3购买道具=4系统赠送道具
			if (operator == 0) {
				// 用户存在该道具---减少数量
				if (magic != null) {
					magic.setNum(magic.getNum() - num);
					// 减少包容量
					user.setMagicPacketSize(user.getMagicPacketSize() - num
							* comMagic.getWeight());
					// 增加系统包数量
					comMagic.setNum(num + comMagic.getNum());
					magicMng.update(comMagic);
				}
			} else if (operator == 1) {
				// 减少数量
				if (magic != null) {
					magic.setNum(magic.getNum() - num);
					// 减少包容量
					user.setMagicPacketSize(user.getMagicPacketSize() - num
							* comMagic.getWeight());
				}
			} else if (operator == 2) {
				// 减少数量
				if (magic != null) {
					magic.setNum(magic.getNum() - num);
					// 减少包容量
					user.setMagicPacketSize(user.getMagicPacketSize() - num
							* comMagic.getWeight());
				}
			} else if (operator == 3) {
				// 增加数量
				if (magic != null) {
					magic.setNum(magic.getNum() + num);
					// 增加包容量
					user.setMagicPacketSize(user.getMagicPacketSize() + num
							* comMagic.getWeight());
					// 减少系统包数量
					comMagic.setNum(comMagic.getNum() - num);
					magicMng.update(comMagic);
				} else {
					magic = new BbsMemberMagic();
					magic.setMagic(comMagic);
					magic.setNum(num);
					magic.setUser(user);
					memberMagicMng.save(magic);
					user.addToMemberMagics(magic);
				}
			} else if (operator == 4) {
				// 增加数量
				if (magic != null) {
					magic.setNum(magic.getNum() + num);
					// 增加包容量
					user.setMagicPacketSize(user.getMagicPacketSize() + num
							* comMagic.getWeight());
					magicMng.update(comMagic);
				} else {
					magic = new BbsMemberMagic();
					magic.setMagic(comMagic);
					magic.setNum(num);
					magic.setUser(user);
					memberMagicMng.save(magic);
					user.addToMemberMagics(magic);
				}
			}

		}

		if (logger.isDebugEnabled()) {
			logger.debug("updatePoint(Integer, Integer, Integer, String, int, int) - end"); //$NON-NLS-1$
		}
	}

	// private CmsSiteMng cmsSiteMng;
	private BbsUserGroupMng bbsUserGroupMng;
	private UnifiedUserMng unifiedUserMng;
	private BbsUserExtMng bbsUserExtMng;
	private BbsUserDao dao;
	@Autowired
	private BbsCommonMagicMng magicMng;
	@Autowired
	private BbsMemberMagicMng memberMagicMng;

	// @Autowired
	// public void setCmsSiteMng(CmsSiteMng cmsSiteMng) {
	// this.cmsSiteMng = cmsSiteMng;
	// }

	@Autowired
	public void setUnifiedUserMng(UnifiedUserMng unifiedUserMng) {
		this.unifiedUserMng = unifiedUserMng;
	}

	@Autowired
	public void setBbsUserExtMng(BbsUserExtMng bbsUserExtMng) {
		this.bbsUserExtMng = bbsUserExtMng;
	}

	@Autowired
	public void setDao(BbsUserDao dao) {
		this.dao = dao;
	}

	@Autowired
	public void setBbsUserGroupMng(BbsUserGroupMng bbsUserGroupMng) {
		this.bbsUserGroupMng = bbsUserGroupMng;
	}
}