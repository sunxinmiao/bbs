package com.jeecms.bbs.action;

import org.apache.log4j.Logger;

import java.util.List;

import javax.servlet.http.HttpServletRequest;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.entity.CmsSensitivity;
import com.jeecms.bbs.manager.CmsSensitivityMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.WebErrors;

@Controller
public class CmsSensitivityAct {
	private static final Logger logger = Logger.getLogger(CmsSensitivityAct.class);

	@RequestMapping("/sensitivity/v_list.do")
	public String list(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("list(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		Integer siteId = CmsUtils.getSiteId(request);
		List<CmsSensitivity> list = manager.getList(siteId, false);
		model.addAttribute("list", list);

		if (logger.isDebugEnabled()) {
			logger.debug("list(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "sensitivity/list";
	}

	@RequestMapping("/sensitivity/o_save.do")
	public String save(CmsSensitivity bean, HttpServletRequest request,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(CmsSensitivity, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		WebErrors errors = validateSave(bean, request);
		if (errors.hasErrors()) {
			String returnString = errors.showErrorPage(model);
			if (logger.isDebugEnabled()) {
				logger.debug("save(CmsSensitivity, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		Integer siteId = CmsUtils.getSiteId(request);
		bean = manager.save(bean, siteId);
		model.addAttribute("message", "global.success");
		logger.info("save CmsSensitivity id="+ bean.getId());
		return list(request, model);
	}

	@RequestMapping("/sensitivity/o_update.do")
	public String update(Integer[] id, String[] search, String[] replacement,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(Integer[], String[], String[], HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		WebErrors errors = validateUpdate(id, search, replacement, request);
		if (errors.hasErrors()) {
			String returnString = errors.showErrorPage(model);
			if (logger.isDebugEnabled()) {
				logger.debug("update(Integer[], String[], String[], HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		manager.updateEnsitivity(id, search, replacement);
		model.addAttribute("message", "global.success");
		logger.info("update CmsSensitivity.");
		return list(request, model);
	}

	@RequestMapping("/sensitivity/o_delete.do")
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
		CmsSensitivity[] beans = manager.deleteByIds(ids);
		for (CmsSensitivity bean : beans) {
			logger.info("delete CmsSensitivity id="+ bean.getId());
		}
		model.addAttribute("message", "global.success");
		String returnString = list(request, model);
		if (logger.isDebugEnabled()) {
			logger.debug("delete(Integer[], HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	private WebErrors validateSave(CmsSensitivity bean,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateSave(CmsSensitivity, HttpServletRequest) - start"); //$NON-NLS-1$
		}

		WebErrors errors = WebErrors.create(request);

		if (logger.isDebugEnabled()) {
			logger.debug("validateSave(CmsSensitivity, HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return errors;
	}

	private WebErrors validateUpdate(Integer[] ids, String[] searchs,
			String[] replacements, HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateUpdate(Integer[], String[], String[], HttpServletRequest) - start"); //$NON-NLS-1$
		}

		WebErrors errors = WebErrors.create(request);
		if (errors.ifEmpty(ids, "id")) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateUpdate(Integer[], String[], String[], HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return errors;
		}
		if (errors.ifEmpty(searchs, "name")) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateUpdate(Integer[], String[], String[], HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return errors;
		}
		if (errors.ifEmpty(replacements, "url")) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateUpdate(Integer[], String[], String[], HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return errors;
		}
		if (ids.length != searchs.length || ids.length != replacements.length) {
			errors.addErrorString("id, searchs, replacements length"
					+ " not equals");

			if (logger.isDebugEnabled()) {
				logger.debug("validateUpdate(Integer[], String[], String[], HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return errors;
		}
		for (Integer id : ids) {
			vldExist(id, errors);

			if (logger.isDebugEnabled()) {
				logger.debug("validateUpdate(Integer[], String[], String[], HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return errors;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validateUpdate(Integer[], String[], String[], HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return errors;
	}

	private WebErrors validateDelete(Integer[] ids, HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateDelete(Integer[], HttpServletRequest) - start"); //$NON-NLS-1$
		}

		WebErrors errors = WebErrors.create(request);
		if (errors.ifEmpty(ids, "ids")) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateDelete(Integer[], HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return errors;
		}
		for (Integer id : ids) {
			vldExist(id, errors);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validateDelete(Integer[], HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return errors;
	}

	private boolean vldExist(Integer id, WebErrors errors) {
		if (logger.isDebugEnabled()) {
			logger.debug("vldExist(Integer, WebErrors) - start"); //$NON-NLS-1$
		}

		if (errors.ifNull(id, "id")) {
			if (logger.isDebugEnabled()) {
				logger.debug("vldExist(Integer, WebErrors) - end"); //$NON-NLS-1$
			}
			return true;
		}
		CmsSensitivity entity = manager.findById(id);
		if (errors.ifNotExist(entity, CmsSensitivity.class, id)) {
			if (logger.isDebugEnabled()) {
				logger.debug("vldExist(Integer, WebErrors) - end"); //$NON-NLS-1$
			}
			return true;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("vldExist(Integer, WebErrors) - end"); //$NON-NLS-1$
		}
		return false;
	}

	@Autowired
	private CmsSensitivityMng manager;
}