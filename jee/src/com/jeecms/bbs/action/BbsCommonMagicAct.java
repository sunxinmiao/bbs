package com.jeecms.bbs.action;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.MagicConstants;
import com.jeecms.bbs.entity.BbsCommonMagic;
import com.jeecms.bbs.entity.BbsMagicConfig;
import com.jeecms.bbs.entity.BbsMagicLog;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserGroup;
import com.jeecms.bbs.manager.BbsCommonMagicMng;
import com.jeecms.bbs.manager.BbsMagicConfigMng;
import com.jeecms.bbs.manager.BbsMagicLogMng;
import com.jeecms.bbs.manager.BbsUserGroupMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.WebErrors;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.page.SimplePage;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.MagicMessage;

@Controller
public class BbsCommonMagicAct {
	private static final Logger logger = Logger.getLogger(BbsCommonMagicAct.class);

	@RequestMapping("/magic/v_list.do")
	public String list(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("list(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		List<BbsCommonMagic> magics = manager.getList();
		model.addAttribute("magics", magics);

		if (logger.isDebugEnabled()) {
			logger.debug("list(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "magic/list";
	}

	@RequestMapping("/magic/user_list.do")
	public String user_list(String username, Integer groupId, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("user_list(String, Integer, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		Pagination pagination = userMng.getPage(username, null, groupId, false,
				null, null, null, null, null, null, SimplePage.cpn(pageNo),
				CookieUtils.getPageSize(request));
		List<BbsUserGroup> groupList = bbsUserGroupMng.getList(site.getId());
		model.addAttribute("groupList", groupList);
		model.addAttribute("pagination", pagination);
		model.addAttribute("groupId", groupId);
		model.addAttribute("username", username);

		if (logger.isDebugEnabled()) {
			logger.debug("user_list(String, Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "magic/userlist";
	}

	@RequestMapping("/magic/select_magic.do")
	public String select_magic(Integer userIds[], Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("select_magic(Integer[], Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		List<BbsCommonMagic> magics = manager.getList();
		model.addAttribute("magics", magics);
		model.addAttribute("userIds", userIds);

		if (logger.isDebugEnabled()) {
			logger.debug("select_magic(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "magic/selectmagic";
	}

	@RequestMapping("/magic/give_magic.do")
	public String give_magic(Integer userIds[], Integer ids[], Integer nums[],
			Integer pageNo, HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("give_magic(Integer[], Integer[], Integer[], Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		MagicMessage magicMessage = MagicMessage.create(request);
		String msg = magicMessage.getMessage("magic.give.success");
		List<Integer> numList = new ArrayList<Integer>();
		BbsCommonMagic magic;
		BbsUser user = CmsUtils.getUser(request);
		BbsUser targetUser;
		Integer id;
		Integer numTemp;
		// 去除无用的道具数量
		for (Integer num : nums) {
			if (!num.equals(0)) {
				numList.add(num);
			}
		}
		// 给用户赠送道具
		for (Integer userId : userIds) {
			targetUser = userMng.findById(userId);
			for (int i = 0; i < ids.length; i++) {
				id = ids[i];
				numTemp = numList.get(i);
				magic = manager.findById(id);
				userMng.updatePoint(userId, null, null, magic.getIdentifier(),
						numTemp, 4);
				saveMagicLog(magic, user, targetUser, numTemp,
						MagicConstants.MAGIC_OPERATOR_GIVE);
			}
		}
		model.addAttribute("message", "magic.give.success");
		model.addAttribute("msg", msg);
		model.addAttribute("userIds", userIds);
		model.addAttribute("ids", ids);
		model.addAttribute("nums", nums);

		if (logger.isDebugEnabled()) {
			logger.debug("give_magic(Integer[], Integer[], Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "magic/givesucc";
	}

	@RequestMapping("/magic/v_edit.do")
	public String edit(Integer id, Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("edit(Integer, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			String returnString = errors.showErrorPage(model);
			if (logger.isDebugEnabled()) {
				logger.debug("edit(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		Integer siteId = CmsUtils.getSiteId(request);
		List<BbsUserGroup> groups = bbsUserGroupMng.getList(siteId);
		BbsCommonMagic magic = manager.findById(id);
		boolean hasBeUsedGroups=hasBeUsedGroups(magic);
		model.addAttribute("magic", magic);
		model.addAttribute("groupIds", magic.getGroupIds());
		model.addAttribute("beUsedGroupIds", magic.getToUseGroupIds());
		model.addAttribute("groups", groups);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("hasBeUsedGroups", hasBeUsedGroups);

		if (logger.isDebugEnabled()) {
			logger.debug("edit(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "magic/edit";
	}

	@RequestMapping("/magic/o_update.do")
	public String update(BbsCommonMagic bean, Integer[] groupIds,
			Integer[] beUsedGroupIds, HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsCommonMagic, Integer[], Integer[], HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		manager.updateByGroup(bean, groupIds, beUsedGroupIds);
		logger.info("update BbsCommonMagic id="+ bean.getId());
		return list(request, model);
	}

	@RequestMapping("/magic/o_priority.do")
	public String priorityUpdate(Integer[] mids, Byte[] prioritys,
			Integer[] magicAvail, HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("priorityUpdate(Integer[], Byte[], Integer[], HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		if (mids == null || mids.length <= 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("priorityUpdate(Integer[], Byte[], Integer[], HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return "redirect:v_list.do";
		}
		BbsCommonMagic magic;
		Integer id;
		Byte priority;
		Integer magicAvailable;
		for (int i = 0; i < mids.length; i++) {
			id = mids[i];
			priority = prioritys[i];
			magicAvailable = magicAvail[i];
			if (id != null && priority != null) {
				magic = manager.findById(id);
				if (magic != null) {
					magic.setDisplayorder(priority);
					if (magicAvailable.equals(1)) {
						magic.setAvailable(true);
					} else {
						magic.setAvailable(false);
					}
					manager.update(magic);
				}
			}
		}
		String returnString = list(request, model);
		if (logger.isDebugEnabled()) {
			logger.debug("priorityUpdate(Integer[], Byte[], Integer[], HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/magic/v_config.do")
	public String config_edit(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("config_edit(HttpServletRequest, HttpServletResponse, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsMagicConfig config = magicConfigMng.findById(site.getId());
		model.addAttribute("config", config);

		if (logger.isDebugEnabled()) {
			logger.debug("config_edit(HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
		}
		return "magic/config";
	}

	@RequestMapping("/magic/o_config.do")
	public String config_update(HttpServletRequest request,
			HttpServletResponse response, BbsMagicConfig bean, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("config_update(HttpServletRequest, HttpServletResponse, BbsMagicConfig, ModelMap) - start"); //$NON-NLS-1$
		}

		bean = magicConfigMng.update(bean);
		model.addAttribute("config", bean);
		model.addAttribute("message", "global.success");

		if (logger.isDebugEnabled()) {
			logger.debug("config_update(HttpServletRequest, HttpServletResponse, BbsMagicConfig, ModelMap) - end"); //$NON-NLS-1$
		}
		return "magic/config";
	}

	private void saveMagicLog(BbsCommonMagic magic, BbsUser user,
			BbsUser targetUser, Integer buynum, Byte Operate) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveMagicLog(BbsCommonMagic, BbsUser, BbsUser, Integer, Byte) - start"); //$NON-NLS-1$
		}

		BbsMagicLog log = new BbsMagicLog();
		log.setLogTime(new Date());
		log.setMagic(magic);
		log.setOperator(Operate);
		log.setPrice(magic.getPrice());
		log.setNum(buynum);
		log.setUser(user);
		log.setTargetUser(targetUser);
		magicLogMng.save(log);

		if (logger.isDebugEnabled()) {
			logger.debug("saveMagicLog(BbsCommonMagic, BbsUser, BbsUser, Integer, Byte) - end"); //$NON-NLS-1$
		}
	}

	private WebErrors validateEdit(Integer id, HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateEdit(Integer, HttpServletRequest) - start"); //$NON-NLS-1$
		}

		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		if (vldExist(id, site.getId(), errors)) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateEdit(Integer, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return errors;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validateEdit(Integer, HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return errors;
	}

	private boolean vldExist(Integer id, Integer siteId, WebErrors errors) {
		if (logger.isDebugEnabled()) {
			logger.debug("vldExist(Integer, Integer, WebErrors) - start"); //$NON-NLS-1$
		}

		if (errors.ifNull(id, "id")) {
			if (logger.isDebugEnabled()) {
				logger.debug("vldExist(Integer, Integer, WebErrors) - end"); //$NON-NLS-1$
			}
			return true;
		}
		BbsCommonMagic entity = manager.findById(id);
		if (errors.ifNotExist(entity, BbsCommonMagic.class, id)) {
			if (logger.isDebugEnabled()) {
				logger.debug("vldExist(Integer, Integer, WebErrors) - end"); //$NON-NLS-1$
			}
			return true;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("vldExist(Integer, Integer, WebErrors) - end"); //$NON-NLS-1$
		}
		return false;
	}

	private boolean hasBeUsedGroups(BbsCommonMagic magic) {
		if (logger.isDebugEnabled()) {
			logger.debug("hasBeUsedGroups(BbsCommonMagic) - start"); //$NON-NLS-1$
		}

		String identifier = magic.getIdentifier();
		if (identifier.equals(MagicConstants.MAGIC_CHECKONLINE)
				|| identifier.equals(MagicConstants.MAGIC_CLOSE)
				|| identifier.equals(MagicConstants.MAGIC_NAMEPOST)
				|| identifier.equals(MagicConstants.MAGIC_OPEN)
				|| identifier.equals(MagicConstants.MAGIC_SHOWIP)
				|| identifier.equals(MagicConstants.MAGIC_SOFA)) {
			if (logger.isDebugEnabled()) {
				logger.debug("hasBeUsedGroups(BbsCommonMagic) - end"); //$NON-NLS-1$
			}
			return true;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("hasBeUsedGroups(BbsCommonMagic) - end"); //$NON-NLS-1$
		}
		return false;
	}

	@Autowired
	private BbsCommonMagicMng manager;
	@Autowired
	private BbsMagicConfigMng magicConfigMng;
	@Autowired
	private BbsUserMng userMng;
	@Autowired
	private BbsUserGroupMng bbsUserGroupMng;
	@Autowired
	private BbsMagicLogMng magicLogMng;
}