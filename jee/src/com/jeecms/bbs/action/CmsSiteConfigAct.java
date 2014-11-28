package com.jeecms.bbs.action;

import org.apache.log4j.Logger;

import java.util.List;

import javax.servlet.http.HttpServletRequest;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.entity.BbsConfig;
import com.jeecms.bbs.entity.BbsCreditExchange;
import com.jeecms.bbs.entity.BbsUserGroup;
import com.jeecms.bbs.manager.BbsConfigMng;
import com.jeecms.bbs.manager.BbsCreditExchangeMng;
import com.jeecms.bbs.manager.BbsUserGroupMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.WebErrors;
import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.Ftp;
import com.jeecms.core.entity.Config.ConfigEmailSender;
import com.jeecms.core.entity.Config.ConfigLogin;
import com.jeecms.core.entity.Config.ConfigMessageTemplate;
import com.jeecms.core.manager.CmsConfigMng;
import com.jeecms.core.manager.CmsSiteMng;
import com.jeecms.core.manager.ConfigMng;
import com.jeecms.core.manager.FtpMng;

@Controller
public class CmsSiteConfigAct {
	private static final Logger logger = Logger.getLogger(CmsSiteConfigAct.class);

	@RequestMapping("/site_config/v_system_edit.do")
	public String systemEdit(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("systemEdit(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		model.addAttribute("cmsConfig", cmsConfigMng.get());

		if (logger.isDebugEnabled()) {
			logger.debug("systemEdit(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "site_config/system_edit";
	}

	@RequestMapping("/site_config/o_system_update.do")
	public String systemUpdate(CmsConfig bean, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("systemUpdate(CmsConfig, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		WebErrors errors = validateSystemUpdate(bean, request);
		if (errors.hasErrors()) {
			String returnString = errors.showErrorPage(model);
			if (logger.isDebugEnabled()) {
				logger.debug("systemUpdate(CmsConfig, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		bean = cmsConfigMng.update(bean);
		model.addAttribute("message", "global.success");
		logger.info("update systemConfig of CmsConfig.");
		return systemEdit(request, model);
	}

	@RequestMapping("/site_config/v_base_edit.do")
	public String baseEdit(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("baseEdit(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		List<Ftp> ftpList = ftpMng.getList();
		model.addAttribute("ftpList", ftpList);
		model.addAttribute("cmsSite", site);

		if (logger.isDebugEnabled()) {
			logger.debug("baseEdit(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "site_config/base_edit";
	}

	@RequestMapping("/site_config/o_base_update.do")
	public String baseUpdate(CmsSite bean, Integer uploadFtpId,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("baseUpdate(CmsSite, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		WebErrors errors = validateBaseUpdate(bean, request);
		if (errors.hasErrors()) {
			String returnString = errors.showErrorPage(model);
			if (logger.isDebugEnabled()) {
				logger.debug("baseUpdate(CmsSite, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		CmsSite site = CmsUtils.getSite(request);
		bean.setId(site.getId());
		bean = manager.update(bean, uploadFtpId);
		model.addAttribute("message", "global.success");
		logger.info("update CmsSite success. id="+ site.getId());
		return baseEdit(request, model);
	}

	@RequestMapping("/bbs_config/v_edit.do")
	public String bbsEdit(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("bbsEdit(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsConfig config = bbsConfigMng.findById(site.getId());
		List<BbsUserGroup> groupList = bbsUserGroupMng.getList(site.getId());
		model.addAttribute("config", config);
		model.addAttribute("groupList", groupList);

		if (logger.isDebugEnabled()) {
			logger.debug("bbsEdit(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "site_config/bbs_edit";
	}

	@RequestMapping("/bbs_config/o_update.do")
	public String bbsUpdate(BbsConfig bean, Integer registerGroupId,
			Integer defaultGroupId, HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("bbsUpdate(BbsConfig, Integer, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		bean.setSite(site);
		bean.setRegisterGroup(bbsUserGroupMng.findById(registerGroupId));
		bean.setDefaultGroup(bbsUserGroupMng.findById(defaultGroupId));
		bbsConfigMng.update(bean);
		String returnString = bbsEdit(request, model);
		if (logger.isDebugEnabled()) {
			logger.debug("bbsUpdate(BbsConfig, Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}
	@RequestMapping("/bbs_config/v_login_edit.do")
	public String loginEdit(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("loginEdit(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		model.addAttribute("configLogin", configMng.getConfigLogin());
		model.addAttribute("emailSender", configMng.getEmailSender());
		model.addAttribute("forgotPasswordTemplate", configMng.getForgotPasswordMessageTemplate());
		model.addAttribute("registerTemplate", configMng.getRegisterMessageTemplate());

		if (logger.isDebugEnabled()) {
			logger.debug("loginEdit(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "site_config/login_edit";
	}

	@RequestMapping("/bbs_config/o_login_update.do")
	public String loginUpdate(ConfigLogin configLogin,
			ConfigEmailSender emailSender, ConfigMessageTemplate msgTpl,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("loginUpdate(ConfigLogin, ConfigEmailSender, ConfigMessageTemplate, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		configMng.updateOrSave(configLogin.getAttr());
		configMng.updateOrSave(emailSender.getAttr());
		configMng.updateOrSave(msgTpl.getAttr());
		model.addAttribute("message", "global.success");
		logger.info("update loginCoinfig of Config.");
		return loginEdit(request, model);
	}
	
	@RequestMapping("/bbs_config/v_creditExchangeEdit.do")
	public String bbsCreditExchangeEdit(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("bbsCreditExchangeEdit(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsCreditExchange creditExchange =creditExchangeMng.findById(site.getId());
		model.addAttribute("creditExchange",creditExchange);

		if (logger.isDebugEnabled()) {
			logger.debug("bbsCreditExchangeEdit(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "site_config/credit_exchange_rule";
	}
	@RequestMapping("/bbs_config/v_creditExchangeUpdate.do")
	public String bbsCreditExchangeUpdate(BbsCreditExchange creditExchange ,HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("bbsCreditExchangeUpdate(BbsCreditExchange, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		if(creditExchange.getExchangetax()<1.0&&creditExchange.getExchangetax()>=0.0){
			creditExchangeMng.update(creditExchange);
			model.addAttribute("message", "global.success");
			logger.info("update BbsCreditExchange");
		}
		String returnString = bbsCreditExchangeEdit(request, model);
		if (logger.isDebugEnabled()) {
			logger.debug("bbsCreditExchangeUpdate(BbsCreditExchange, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	private WebErrors validateSystemUpdate(CmsConfig bean,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateSystemUpdate(CmsConfig, HttpServletRequest) - start"); //$NON-NLS-1$
		}

		WebErrors errors = WebErrors.create(request);

		if (logger.isDebugEnabled()) {
			logger.debug("validateSystemUpdate(CmsConfig, HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return errors;
	}

	private WebErrors validateBaseUpdate(CmsSite bean,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateBaseUpdate(CmsSite, HttpServletRequest) - start"); //$NON-NLS-1$
		}

		WebErrors errors = WebErrors.create(request);

		if (logger.isDebugEnabled()) {
			logger.debug("validateBaseUpdate(CmsSite, HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return errors;
	}

	@Autowired
	private CmsSiteMng manager;
	@Autowired
	private FtpMng ftpMng;
	@Autowired
	private BbsConfigMng bbsConfigMng;
	@Autowired
	private BbsUserGroupMng bbsUserGroupMng;
	@Autowired
	private CmsConfigMng cmsConfigMng;
	@Autowired
	private ConfigMng configMng;
	@Autowired
	private BbsCreditExchangeMng creditExchangeMng;
}