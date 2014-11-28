package com.jeecms.bbs.action.member;

import org.apache.log4j.Logger;

import static com.jeecms.bbs.Constants.TPLDIR_MEMBER;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.bbs.web.WebErrors;
import com.jeecms.common.email.EmailSender;
import com.jeecms.common.email.MessageTemplate;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.UnifiedUser;
import com.jeecms.core.manager.ConfigMng;
import com.jeecms.core.manager.UnifiedUserMng;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;

/**
 * 找回密码Action
 * 
 * 用户忘记密码后点击找回密码链接，输入用户名、邮箱和验证码<li>
 * 如果信息正确，返回一个提示页面，并发送一封找回密码的邮件，邮件包含一个链接及新密码，点击链接新密码即生效<li>
 * 如果输入错误或服务器邮箱等信息设置不完整，则给出提示信息<li>
 * 
 * @author 
 * 
 */
@Controller
public class ForgotPasswordAct {
	private static Logger logger = Logger.getLogger(ForgotPasswordAct.class);

	public static final String FORGOT_PASSWORD_INPUT = "tpl.forgotPasswordInput";
	public static final String FORGOT_PASSWORD_RESULT = "tpl.forgotPasswordResult";
	public static final String PASSWORD_RESET = "tpl.passwordReset";

	/**
	 * 找回密码输入页
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/member/forgot_password.jspx", method = RequestMethod.GET)
	public String forgotPasswordInput(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("forgotPasswordInput(HttpServletRequest, HttpServletResponse, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, FORGOT_PASSWORD_INPUT);
		if (logger.isDebugEnabled()) {
			logger.debug("forgotPasswordInput(HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 找回密码提交页
	 * 
	 * @param username
	 * @param email
	 * @param captcha
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/member/forgot_password.jspx", method = RequestMethod.POST)
	public String forgotPasswordSubmit(String username, String email,
			String captcha, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("forgotPasswordSubmit(String, String, String, HttpServletRequest, HttpServletResponse, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		username=RequestUtils.getQueryParam(request,"username");
		WebErrors errors = validateForgotPasswordSubmit(username, email,
				captcha, request, response);
		if (errors.hasErrors()) {
			String returnString = FrontUtils.showError(request, response, model, errors);
			if (logger.isDebugEnabled()) {
				logger.debug("forgotPasswordSubmit(String, String, String, HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		UnifiedUser user = unifiedUserMng.getByUsername(username);
		EmailSender sender = configMng.getEmailSender();
		MessageTemplate msgTpl = configMng.getForgotPasswordMessageTemplate();
		model.addAttribute("user", user);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			// 用户名不存在
			model.addAttribute("status", 1);
		} else if (StringUtils.isBlank(user.getEmail())) {
			// 用户没有设置邮箱
			model.addAttribute("status", 2);
		} else if (!user.getEmail().equals(email)) {
			// 邮箱输入错误
			model.addAttribute("status", 3);
		} else if (sender == null) {
			// 邮件服务器没有设置好
			model.addAttribute("status", 4);
		} else if (msgTpl == null) {
			// 邮件模板没有设置好
			model.addAttribute("status", 5);
		} else {
			try {
				unifiedUserMng.passwordForgotten(user.getId(), sender, msgTpl);
				model.addAttribute("status", 0);
			} catch (Exception e) {
				// 发送邮件异常
				model.addAttribute("status", 100);
				model.addAttribute("message", e.getMessage());
				logger.error("send email exception.", e);
			}
		}
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, FORGOT_PASSWORD_RESULT);
		if (logger.isDebugEnabled()) {
			logger.debug("forgotPasswordSubmit(String, String, String, HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping(value = "/member/password_reset.jspx", method = RequestMethod.GET)
	public String passwordReset(Integer uid, String key,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("passwordReset(Integer, String, HttpServletRequest, HttpServletResponse, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors = validatePasswordReset(uid, key, request);
		if (errors.hasErrors()) {
			String returnString = FrontUtils.showError(request, response, model, errors);
			if (logger.isDebugEnabled()) {
				logger.debug("passwordReset(Integer, String, HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		UnifiedUser user = unifiedUserMng.findById(uid);
		if (user == null) {
			// 用户不存在
			model.addAttribute("status", 1);
		} else if (StringUtils.isBlank(user.getResetKey())) {
			// resetKey不存在
			model.addAttribute("status", 2);
		} else if (!user.getResetKey().equals(key)) {
			// 重置key错误
			model.addAttribute("status", 3);
		} else {
			unifiedUserMng.resetPassword(uid);
			model.addAttribute("status", 0);
		}
		FrontUtils.frontData(request, model, site);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, PASSWORD_RESET);
		if (logger.isDebugEnabled()) {
			logger.debug("passwordReset(Integer, String, HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	private WebErrors validateForgotPasswordSubmit(String username,
			String email, String captcha, HttpServletRequest request,
			HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateForgotPasswordSubmit(String, String, String, HttpServletRequest, HttpServletResponse) - start"); //$NON-NLS-1$
		}

		WebErrors errors = WebErrors.create(request);
		if (errors.ifBlank(username, "username", 100)) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateForgotPasswordSubmit(String, String, String, HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
			}
			return errors;
		}
		if (errors.ifBlank(email, "email", 100)) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateForgotPasswordSubmit(String, String, String, HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
			}
			return errors;
		}
		if (errors.ifBlank(captcha, "captcha", 20)) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateForgotPasswordSubmit(String, String, String, HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
			}
			return errors;
		}
		try {
			if (!imageCaptchaService.validateResponseForID(session
					.getSessionId(request, response), captcha)) {
				errors.addErrorCode("error.invalidCaptcha");

				if (logger.isDebugEnabled()) {
					logger.debug("validateForgotPasswordSubmit(String, String, String, HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
				}
				return errors;
			}
		} catch (CaptchaServiceException e) {
			logger.error("validateForgotPasswordSubmit(String, String, String, HttpServletRequest, HttpServletResponse)", e); //$NON-NLS-1$

			errors.addErrorCode("error.exceptionCaptcha");
			logger.warn("", e);
			return errors;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validateForgotPasswordSubmit(String, String, String, HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
		}
		return errors;
	}

	private WebErrors validatePasswordReset(Integer uid, String key,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("validatePasswordReset(Integer, String, HttpServletRequest) - start"); //$NON-NLS-1$
		}

		WebErrors errors = WebErrors.create(request);
		if (errors.ifNull(uid, "uid")) {
			if (logger.isDebugEnabled()) {
				logger.debug("validatePasswordReset(Integer, String, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return errors;
		}
		if (errors.ifBlank(key, "key", 50)) {
			if (logger.isDebugEnabled()) {
				logger.debug("validatePasswordReset(Integer, String, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return errors;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validatePasswordReset(Integer, String, HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return errors;
	}

	@Autowired
	private UnifiedUserMng unifiedUserMng;
	@Autowired
	private ConfigMng configMng;
	@Autowired
	private SessionProvider session;
	@Autowired
	private ImageCaptchaService imageCaptchaService;
}
