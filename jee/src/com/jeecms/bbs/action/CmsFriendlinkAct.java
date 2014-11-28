package com.jeecms.bbs.action;

import org.apache.log4j.Logger;

import java.util.List;

import javax.servlet.http.HttpServletRequest;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.entity.CmsFriendlink;
import com.jeecms.bbs.entity.CmsFriendlinkCtg;
import com.jeecms.bbs.manager.CmsFriendlinkCtgMng;
import com.jeecms.bbs.manager.CmsFriendlinkMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.WebErrors;
import com.jeecms.core.entity.CmsSite;


@Controller
public class CmsFriendlinkAct {
	private static final Logger logger = Logger.getLogger(CmsFriendlinkAct.class);

	@RequestMapping("/friendlink/v_list.do")
	public String list(Integer queryCtgId, HttpServletRequest request,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("list(Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		List<CmsFriendlink> list = manager.getList(site.getId(), queryCtgId,
				null);
		List<CmsFriendlinkCtg> ctgList = cmsFriendlinkCtgMng.getList(site
				.getId());
		model.addAttribute("list", list);
		model.addAttribute("ctgList", ctgList);
		if (queryCtgId != null) {
			model.addAttribute("queryCtgId", queryCtgId);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("list(Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "friendlink/list";
	}

	@RequestMapping("/friendlink/v_add.do")
	public String add(ModelMap model, HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("add(ModelMap, HttpServletRequest) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors = validateAdd(request);
		if (errors.hasErrors()) {
			String returnString = errors.showErrorPage(model);
			if (logger.isDebugEnabled()) {
				logger.debug("add(ModelMap, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		List<CmsFriendlinkCtg> ctgList = cmsFriendlinkCtgMng.getList(site
				.getId());
		model.addAttribute("ctgList", ctgList);

		if (logger.isDebugEnabled()) {
			logger.debug("add(ModelMap, HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return "friendlink/add";
	}

	@RequestMapping("/friendlink/v_edit.do")
	public String edit(Integer id, Integer queryCtgId,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("edit(Integer, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			String returnString = errors.showErrorPage(model);
			if (logger.isDebugEnabled()) {
				logger.debug("edit(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		model.addAttribute("cmsFriendlink", manager.findById(id));
		List<CmsFriendlinkCtg> ctgList = cmsFriendlinkCtgMng.getList(site
				.getId());
		model.addAttribute("ctgList", ctgList);
		if (queryCtgId != null) {
			model.addAttribute("queryCtgId", queryCtgId);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("edit(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "friendlink/edit";
	}

	@RequestMapping("/friendlink/o_save.do")
	public String save(CmsFriendlink bean, Integer ctgId,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(CmsFriendlink, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		WebErrors errors = validateSave(bean, request);
		if (errors.hasErrors()) {
			String returnString = errors.showErrorPage(model);
			if (logger.isDebugEnabled()) {
				logger.debug("save(CmsFriendlink, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		bean = manager.save(bean, ctgId);
		logger.info("save CmsFriendlink id="+ bean.getId());
		return "redirect:v_list.do";
	}

	@RequestMapping("/friendlink/o_update.do")
	public String update(CmsFriendlink bean, Integer ctgId, Integer queryCtgId,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(CmsFriendlink, Integer, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		WebErrors errors = validateUpdate(bean.getId(), request);
		if (errors.hasErrors()) {
			String returnString = errors.showErrorPage(model);
			if (logger.isDebugEnabled()) {
				logger.debug("update(CmsFriendlink, Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		bean = manager.update(bean, ctgId);
		logger.info("update CmsFriendlink id="+ bean.getId());
		return list(queryCtgId, request, model);
	}

	@RequestMapping("/friendlink/o_priority.do")
	public String priority(Integer[] wids, Integer[] priority,
			Integer queryCtgId, HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("priority(Integer[], Integer[], Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		WebErrors errors = validatePriority(wids, priority, request);
		if (errors.hasErrors()) {
			String returnString = errors.showErrorPage(model);
			if (logger.isDebugEnabled()) {
				logger.debug("priority(Integer[], Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		manager.updatePriority(wids, priority);
		logger.info("update CmsFriendlink priority.");
		return list(queryCtgId, request, model);
	}

	@RequestMapping("/friendlink/o_delete.do")
	public String delete(Integer[] ids, Integer queryCtgId,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("delete(Integer[], Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			String returnString = errors.showErrorPage(model);
			if (logger.isDebugEnabled()) {
				logger.debug("delete(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		CmsFriendlink[] beans = manager.deleteByIds(ids);
		for (CmsFriendlink bean : beans) {
			logger.info("delete CmsFriendlink id="+ bean.getId());
		}
		String returnString = list(queryCtgId, request, model);
		if (logger.isDebugEnabled()) {
			logger.debug("delete(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	private WebErrors validateAdd(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateAdd(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		if (cmsFriendlinkCtgMng.countBySiteId(site.getId()) <= 0) {
			errors.addErrorCode("cmsFriendlink.error.addFriendlinkCtgFirst");

			if (logger.isDebugEnabled()) {
				logger.debug("validateAdd(HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return errors;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validateAdd(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return errors;
	}

	private WebErrors validateSave(CmsFriendlink bean,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateSave(CmsFriendlink, HttpServletRequest) - start"); //$NON-NLS-1$
		}

		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		bean.setSite(site);

		if (logger.isDebugEnabled()) {
			logger.debug("validateSave(CmsFriendlink, HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return errors;
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

	private WebErrors validateUpdate(Integer id, HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateUpdate(Integer, HttpServletRequest) - start"); //$NON-NLS-1$
		}

		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		if (vldExist(id, site.getId(), errors)) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateUpdate(Integer, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return errors;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validateUpdate(Integer, HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return errors;
	}

	private WebErrors validatePriority(Integer[] ids, Integer[] priorities,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("validatePriority(Integer[], Integer[], HttpServletRequest) - start"); //$NON-NLS-1$
		}

		WebErrors errors = WebErrors.create(request);

		if (logger.isDebugEnabled()) {
			logger.debug("validatePriority(Integer[], Integer[], HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return errors;
	}

	private WebErrors validateDelete(Integer[] ids, HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateDelete(Integer[], HttpServletRequest) - start"); //$NON-NLS-1$
		}

		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		if (errors.ifEmpty(ids, "ids")) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateDelete(Integer[], HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return errors;
		}
		for (Integer id : ids) {
			vldExist(id, site.getId(), errors);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validateDelete(Integer[], HttpServletRequest) - end"); //$NON-NLS-1$
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
		CmsFriendlink entity = manager.findById(id);
		if (errors.ifNotExist(entity, CmsFriendlink.class, id)) {
			if (logger.isDebugEnabled()) {
				logger.debug("vldExist(Integer, Integer, WebErrors) - end"); //$NON-NLS-1$
			}
			return true;
		}
		if (!entity.getSite().getId().equals(siteId)) {
			errors.notInSite(CmsFriendlink.class, id);

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

	@Autowired
	private CmsFriendlinkCtgMng cmsFriendlinkCtgMng;
	@Autowired
	private CmsFriendlinkMng manager;
}