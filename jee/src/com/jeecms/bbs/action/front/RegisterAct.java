package com.jeecms.bbs.action.front;

import org.apache.log4j.Logger;

import static com.jeecms.bbs.Constants.TPLDIR_MEMBER;

import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeecms.bbs.cache.BbsConfigEhCache;
import com.jeecms.bbs.entity.BbsConfig;
import com.jeecms.bbs.entity.BbsLoginLog;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserExt;
import com.jeecms.bbs.entity.BbsUserGroup;
import com.jeecms.bbs.entity.BbsUserOnline;
import com.jeecms.bbs.manager.BbsConfigMng;
import com.jeecms.bbs.manager.BbsLoginLogMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.manager.BbsUserOnlineMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.bbs.web.WebErrors;
import com.jeecms.common.email.EmailSender;
import com.jeecms.common.email.MessageTemplate;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.UnifiedUser;
import com.jeecms.core.manager.AuthenticationMng;
import com.jeecms.core.manager.ConfigMng;
import com.jeecms.core.manager.UnifiedUserMng;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;

/**
 * 前台会员注册Action
 * 
 * @author liqiang
 * 
 */
@Controller
public class RegisterAct {
	private static final Logger logger = Logger.getLogger(RegisterAct.class);

	public static final String REGISTER = "tpl.register";
	public static final String REGISTER_RESULT = "tpl.registerResult";
	public static final String REGISTER_ACTIVE_SUCCESS = "tpl.registerActiveSuccess";
	public static final String LOGIN_INPUT = "tpl.loginInput";

	@RequestMapping(value = "/register.jspx", method = RequestMethod.GET)
	public String input(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("input(HttpServletRequest, HttpServletResponse, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, REGISTER);
		if (logger.isDebugEnabled()) {
			logger.debug("input(HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping(value = "/register.jspx", method = RequestMethod.POST)
	public String submit(String username, String email, String password,
			BbsUserExt userExt, String captcha, String nextUrl,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("submit(String, String, String, BbsUserExt, String, String, HttpServletRequest, HttpServletResponse, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsConfig config = bbsConfigMng.findById(site.getId());
		WebErrors errors = validateSubmit(username, email, password, captcha,
				site, request, response);
		if (errors.hasErrors()) {
			String returnString = FrontUtils.showError(request, response, model, errors);
			if (logger.isDebugEnabled()) {
				logger.debug("submit(String, String, String, BbsUserExt, String, String, HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		String ip = RequestUtils.getIpAddr(request);
		Integer groupId = null;
		BbsUserGroup group = bbsConfigMng.findById(site.getId())
				.getRegisterGroup();
		if (group != null) {
			groupId = group.getId();
		}
		BbsUser user = null;
		
		if(config.getEmailValidate()){
			EmailSender sender = configMng.getEmailSender();
			MessageTemplate msgTpl = configMng.getRegisterMessageTemplate();
			if (sender == null) {
				// 邮件服务器没有设置好
				model.addAttribute("status", 4);
			} else if (msgTpl == null) {
				// 邮件模板没有设置好
				model.addAttribute("status", 5);
			} else {
				try {
					user = bbsUserMng.registerMember(username, email, password, ip,
							groupId, userExt, false, sender, msgTpl);
					//信箱校验
					Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
					Matcher matcher = pattern.matcher(user.getEmail());
					
					if(matcher.matches() && user.getEmail().length()<=100){
						bbsConfigEhCache.setBbsConfigCache(0, 0, 0, 1, user, site.getId());
						model.addAttribute("status", 0);
					}
					
				} catch (Exception e) {
					// 发送邮件异常
					model.addAttribute("status", 100);
					model.addAttribute("message", e.getMessage());
					logger.error("send email exception.", e);
				}
			}
			logger.info("member register success. username=" + username);
			if (!StringUtils.isBlank(nextUrl)) {
				response.sendRedirect(nextUrl);

				if (logger.isDebugEnabled()) {
					logger.debug("submit(String, String, String, BbsUserExt, String, String, HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
				}
				return null;
			} else {
				FrontUtils.frontData(request, model, site);
				FrontUtils.frontPageData(request, model);
				String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, REGISTER_RESULT);
				if (logger.isDebugEnabled()) {
					logger.debug("submit(String, String, String, BbsUserExt, String, String, HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
				}
				return returnString;
			}
		}else{ 
			user = bbsUserMng.registerMember(username, email, password,
			  ip, groupId, userExt);
			//信箱校验
			Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
			Matcher matcher = pattern.matcher(user.getEmail());
			
			if(matcher.matches() && user.getEmail().length()<=100){
				bbsConfigEhCache.setBbsConfigCache(0, 0, 0, 1, user, site.getId());
				logger.info("member register success. username="+ username );
				FrontUtils.frontData(request, model, site);
				FrontUtils.frontPageData(request, model);
				model.addAttribute("success",true);
				String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, LOGIN_INPUT);
				if (logger.isDebugEnabled()) {
					logger.debug("submit(String, String, String, BbsUserExt, String, String, HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
				}
				return returnString;
			}

			if (logger.isDebugEnabled()) {
				logger.debug("submit(String, String, String, BbsUserExt, String, String, HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
			}
			return null;
		}
		
		/*
		 * BbsUser user = bbsUserMng.registerMember(username, email, password,
		 * ip, groupId, userExt);
		 */
	}

	// 激活账号
	@RequestMapping(value = "/active.jspx", method = RequestMethod.GET)
	public String active(String username, String key,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("active(String, String, HttpServletRequest, HttpServletResponse, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors = validateActive(username, key, request, response);
		if (errors.hasErrors()) {
			String returnString = FrontUtils.showError(request, response, model, errors);
			if (logger.isDebugEnabled()) {
				logger.debug("active(String, String, HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		UnifiedUser user = unifiedUserMng.active(username, key);
		BbsUser bbsUser = bbsUserMng.findById(user.getId());
		String ip = RequestUtils.getIpAddr(request);
		authMng.activeLogin(user, ip, request, response, session);
		// 登录记录
		BbsLoginLog loginLog = new BbsLoginLog();
		loginLog.setIp(RequestUtils.getIpAddr(request));
		Calendar calendar = Calendar.getInstance();
		loginLog.setLoginTime(calendar.getTime());
		loginLog.setUser(bbsUser);
		bbsLoginMng.save(loginLog);
		// 在线时长统计
		BbsUserOnline online = bbsUser.getUserOnline();
		// 首次登陆
		online = new BbsUserOnline();
		online.setUser(bbsUser);
		online.initial();
		userOnlineMng.save(online);
		FrontUtils.frontData(request, model, site);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, REGISTER_ACTIVE_SUCCESS);
		if (logger.isDebugEnabled()) {
			logger.debug("active(String, String, HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping(value = "/username_unique.jspx")
	public void usernameUnique(HttpServletRequest request,
			HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("usernameUnique(HttpServletRequest, HttpServletResponse) - start"); //$NON-NLS-1$
		}

		String username = RequestUtils.getQueryParam(request, "username");
		// 用户名为空，返回false。
		if (StringUtils.isBlank(username)) {
			ResponseUtils.renderJson(response, "false");

			if (logger.isDebugEnabled()) {
				logger.debug("usernameUnique(HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
			}
			return;
		}
		// 用户名存在，返回false。
		if (unifiedUserMng.usernameExist(username)) {
			ResponseUtils.renderJson(response, "false");

			if (logger.isDebugEnabled()) {
				logger.debug("usernameUnique(HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
			}
			return;
		}
		ResponseUtils.renderJson(response, "true");

		if (logger.isDebugEnabled()) {
			logger.debug("usernameUnique(HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
		}
	}

	@RequestMapping(value = "/email_unique.jspx")
	public void emailUnique(HttpServletRequest request,
			HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("emailUnique(HttpServletRequest, HttpServletResponse) - start"); //$NON-NLS-1$
		}

		String email = RequestUtils.getQueryParam(request, "email");
		// email为空，返回false。
		if (StringUtils.isBlank(email)) {
			ResponseUtils.renderJson(response, "false");

			if (logger.isDebugEnabled()) {
				logger.debug("emailUnique(HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
			}
			return;
		}
		// email存在，返回false。
		if (unifiedUserMng.emailExist(email)) {
			ResponseUtils.renderJson(response, "false");

			if (logger.isDebugEnabled()) {
				logger.debug("emailUnique(HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
			}
			return;
		}
		ResponseUtils.renderJson(response, "true");

		if (logger.isDebugEnabled()) {
			logger.debug("emailUnique(HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
		}
	}

	private WebErrors validateSubmit(String username, String email,
			String password, String captcha, CmsSite site,
			HttpServletRequest request, HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateSubmit(String, String, String, String, CmsSite, HttpServletRequest, HttpServletResponse) - start"); //$NON-NLS-1$
		}

		WebErrors errors = WebErrors.create(request);
		try {
			if (!imageCaptchaService.validateResponseForID(session
					.getSessionId(request, response), captcha)) {
				errors.addErrorCode("error.invalidCaptcha");

				if (logger.isDebugEnabled()) {
					logger.debug("validateSubmit(String, String, String, String, CmsSite, HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
				}
				return errors;
			}
		} catch (CaptchaServiceException e) {
			logger.error("validateSubmit(String, String, String, String, CmsSite, HttpServletRequest, HttpServletResponse)", e); //$NON-NLS-1$

			errors.addErrorCode("error.exceptionCaptcha");
			logger.warn("", e);
			return errors;
		}
		if (errors.ifMaxLength(email, "email", 100)) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateSubmit(String, String, String, String, CmsSite, HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
			}
			return errors;
		}
		//校验信箱合法性
		if(errors.ifIllegalEmail(email)) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateSubmit(String, String, String, String, CmsSite, HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
			}
			return errors;
		}
		
		// 用户名存在，返回false。
		if (unifiedUserMng.usernameExist(username)) {
			errors.addErrorCode("error.usernameExist");

			if (logger.isDebugEnabled()) {
				logger.debug("validateSubmit(String, String, String, String, CmsSite, HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
			}
			return errors;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validateSubmit(String, String, String, String, CmsSite, HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
		}
		return errors;
	}

	private WebErrors validateActive(String username, String activationCode,
			HttpServletRequest request, HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateActive(String, String, HttpServletRequest, HttpServletResponse) - start"); //$NON-NLS-1$
		}

		WebErrors errors = WebErrors.create(request);
		if (StringUtils.isBlank(username)
				|| StringUtils.isBlank(activationCode)) {
			errors.addErrorCode("error.exceptionParams");

			if (logger.isDebugEnabled()) {
				logger.debug("validateActive(String, String, HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
			}
			return errors;
		}
		UnifiedUser user = unifiedUserMng.getByUsername(username);
		if (user == null) {
			errors.addErrorCode("error.usernameNotExist");

			if (logger.isDebugEnabled()) {
				logger.debug("validateActive(String, String, HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
			}
			return errors;
		}
		if (user.getActivation()
				|| StringUtils.isBlank(user.getActivationCode())) {
			errors.addErrorCode("error.usernameActivated");

			if (logger.isDebugEnabled()) {
				logger.debug("validateActive(String, String, HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
			}
			return errors;
		}
		if (!user.getActivationCode().equals(activationCode)) {
			errors.addErrorCode("error.exceptionActivationCode");

			if (logger.isDebugEnabled()) {
				logger.debug("validateActive(String, String, HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
			}
			return errors;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validateActive(String, String, HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
		}
		return errors;
	}

	@Autowired
	private UnifiedUserMng unifiedUserMng;
	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private SessionProvider session;
	@Autowired
	private BbsConfigMng bbsConfigMng;
	@Autowired
	private BbsConfigEhCache bbsConfigEhCache;
	@Autowired
	private ImageCaptchaService imageCaptchaService;
	@Autowired
	private ConfigMng configMng;
	@Autowired
	private AuthenticationMng authMng;
	@Autowired
	private BbsLoginLogMng bbsLoginMng;
	@Autowired
	private BbsUserOnlineMng userOnlineMng;

}
