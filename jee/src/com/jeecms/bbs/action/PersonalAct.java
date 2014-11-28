package com.jeecms.bbs.action;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserExt;
import com.jeecms.bbs.manager.BbsUserExtMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.WebErrors;
import com.jeecms.common.web.ResponseUtils;

@Controller
public class PersonalAct {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(PersonalAct.class);

	@RequestMapping("/personal/v_profile.do")
	public String profileEdit(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("profileEdit(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		BbsUser user = CmsUtils.getUser(request);
		model.addAttribute("user", user);

		if (logger.isDebugEnabled()) {
			logger.debug("profileEdit(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "personal/profile";
	}

	@RequestMapping("/personal/o_profile.do")
	public String profileUpdate(String origPwd, String newPwd, String email,
			String realname, HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("profileUpdate(String, String, String, String, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		BbsUser user = CmsUtils.getUser(request);
		WebErrors errors = validatePasswordSubmit(user.getId(), origPwd,
				newPwd, email, realname, request);
		if (errors.hasErrors()) {
			String returnString = errors.showErrorPage(model);
			if (logger.isDebugEnabled()) {
				logger.debug("profileUpdate(String, String, String, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		BbsUserExt ext = user.getUserExt();
		ext.setRealname(realname);
		bbsUserExtMng.update(ext, user);
		bbsUserMng.updatePwdEmail(user.getId(), newPwd, email);
		model.addAttribute("message", "global.success");
		String returnString = profileEdit(request, model);
		if (logger.isDebugEnabled()) {
			logger.debug("profileUpdate(String, String, String, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 验证密码是否正确
	 * 
	 * @param origPwd
	 *            原密码
	 * @param request
	 * @param response
	 */
	@RequestMapping("/personal/v_checkPwd.do")
	public void checkPwd(String origPwd, HttpServletRequest request,
			HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("checkPwd(String, HttpServletRequest, HttpServletResponse) - start"); //$NON-NLS-1$
		}

		BbsUser user = CmsUtils.getUser(request);
		boolean pass = bbsUserMng.isPasswordValid(user.getId(), origPwd);
		ResponseUtils.renderJson(response, pass ? "true" : "false");

		if (logger.isDebugEnabled()) {
			logger.debug("checkPwd(String, HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
		}
	}

	private WebErrors validatePasswordSubmit(Integer id, String origPwd,
			String newPwd, String email, String realname,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("validatePasswordSubmit(Integer, String, String, String, String, HttpServletRequest) - start"); //$NON-NLS-1$
		}

		WebErrors errors = WebErrors.create(request);
		if (errors.ifBlank(origPwd, "origPwd", 32)) {
			if (logger.isDebugEnabled()) {
				logger.debug("validatePasswordSubmit(Integer, String, String, String, String, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return errors;
		}
		if (errors.ifMaxLength(newPwd, "newPwd", 32)) {
			if (logger.isDebugEnabled()) {
				logger.debug("validatePasswordSubmit(Integer, String, String, String, String, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return errors;
		}
		if (errors.ifMaxLength(email, "email", 100)) {
			if (logger.isDebugEnabled()) {
				logger.debug("validatePasswordSubmit(Integer, String, String, String, String, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return errors;
		}
		if (errors.ifMaxLength(realname, "realname", 100)) {
			if (logger.isDebugEnabled()) {
				logger.debug("validatePasswordSubmit(Integer, String, String, String, String, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return errors;
		}
		if (!bbsUserMng.isPasswordValid(id, origPwd)) {
			errors.addErrorCode("member.origPwdInvalid");

			if (logger.isDebugEnabled()) {
				logger.debug("validatePasswordSubmit(Integer, String, String, String, String, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return errors;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validatePasswordSubmit(Integer, String, String, String, String, HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return errors;
	}

	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private BbsUserExtMng bbsUserExtMng;
}
