package com.jeecms.bbs.action;

import org.apache.log4j.Logger;

import java.util.List;

import javax.servlet.http.HttpServletRequest;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.entity.CmsFriendlinkCtg;
import com.jeecms.bbs.manager.CmsFriendlinkCtgMng;
import com.jeecms.bbs.manager.CmsFriendlinkMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.WebErrors;
import com.jeecms.core.entity.CmsSite;


@Controller
public class CmsFriendlinkCtgAct {
	private static final Logger logger = Logger.getLogger(CmsFriendlinkCtgAct.class);

	@RequestMapping("/friendlink_ctg/v_list.do")
	public String list(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("list(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		List<CmsFriendlinkCtg> list = manager.getList(site.getId());
		model.addAttribute("list", list);

		if (logger.isDebugEnabled()) {
			logger.debug("list(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "friendlink_ctg/list";
	}

	@RequestMapping("/friendlink_ctg/o_save.do")
	public String save(CmsFriendlinkCtg bean, HttpServletRequest request,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(CmsFriendlinkCtg, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		WebErrors errors = validateSave(bean, request);
		if (errors.hasErrors()) {
			String returnString = errors.showErrorPage(model);
			if (logger.isDebugEnabled()) {
				logger.debug("save(CmsFriendlinkCtg, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		bean = manager.save(bean);
		logger.info("save CmsFriendlinkCtg id="+ bean.getId());
		return "redirect:v_list.do";
	}

	@RequestMapping("/friendlink_ctg/o_update.do")
	public String update(Integer[] wids, String[] name, Integer[] priority,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(Integer[], String[], Integer[], HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		WebErrors errors = validateUpdate(wids, name, priority, request);
		if (errors.hasErrors()) {
			String returnString = errors.showErrorPage(model);
			if (logger.isDebugEnabled()) {
				logger.debug("update(Integer[], String[], Integer[], HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		manager.updateFriendlinkCtgs(wids, name, priority);
		logger.info("update CmsFriendlinkCtg.");
		return "redirect:v_list.do";
	}

	@RequestMapping("/friendlink_ctg/o_delete.do")
	public String delete(Integer[] ids, HttpServletRequest request,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("delete(Integer[], HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			String returnString = errors.showErrorPage(model);
			if (logger.isDebugEnabled()) {
				logger.debug("delete(Integer[], HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		CmsFriendlinkCtg[] beans = manager.deleteByIds(ids);
		for (CmsFriendlinkCtg bean : beans) {
			logger.info("delete CmsFriendlinkCtg id="+ bean.getId());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("delete(Integer[], HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "redirect:v_list.do";
	}

	private WebErrors validateSave(CmsFriendlinkCtg bean,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateSave(CmsFriendlinkCtg, HttpServletRequest) - start"); //$NON-NLS-1$
		}

		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		bean.setSite(site);

		if (logger.isDebugEnabled()) {
			logger.debug("validateSave(CmsFriendlinkCtg, HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return errors;
	}

	private WebErrors validateUpdate(Integer[] ids, String[] names,
			Integer[] priorities, HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateUpdate(Integer[], String[], Integer[], HttpServletRequest) - start"); //$NON-NLS-1$
		}

		WebErrors errors = WebErrors.create(request);

		if (logger.isDebugEnabled()) {
			logger.debug("validateUpdate(Integer[], String[], Integer[], HttpServletRequest) - end"); //$NON-NLS-1$
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
			if (cmsFriendlinkMng.countByCtgId(id) > 0) {
				String code = "cmsFriendlinkCtg.error.delFriendlinkFirst";
				errors.addErrorCode(code);

				if (logger.isDebugEnabled()) {
					logger.debug("validateDelete(Integer[], HttpServletRequest) - end"); //$NON-NLS-1$
				}
				return errors;
			}
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
		CmsFriendlinkCtg entity = manager.findById(id);
		if (errors.ifNotExist(entity, CmsFriendlinkCtg.class, id)) {
			if (logger.isDebugEnabled()) {
				logger.debug("vldExist(Integer, Integer, WebErrors) - end"); //$NON-NLS-1$
			}
			return true;
		}
		if (!entity.getSite().getId().equals(siteId)) {
			errors.notInSite(CmsFriendlinkCtg.class, id);

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
	private CmsFriendlinkMng cmsFriendlinkMng;
	@Autowired
	private CmsFriendlinkCtgMng manager;
}