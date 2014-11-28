package com.jeecms.bbs.action;

import org.apache.log4j.Logger;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.core.entity.CmsSite;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.core.manager.CmsSiteMng;
import com.jeecms.bbs.web.AdminContextInterceptor;
import com.jeecms.bbs.web.CmsUtils;

@Controller
public class WelcomeAct {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(WelcomeAct.class);

	@RequestMapping("/index.do")
	public String index() {
		if (logger.isDebugEnabled()) {
			logger.debug("index() - start"); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			logger.debug("index() - end"); //$NON-NLS-1$
		}
		return "index";
	}

	@RequestMapping("/top.do")
	public String top(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("top(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		// 需要获得站点列表
		List<CmsSite> siteList = cmsSiteMng.getList();
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		model.addAttribute("siteList", siteList);
		model.addAttribute("site", site);
		model.addAttribute("siteParam", AdminContextInterceptor.SITE_PARAM);
		model.addAttribute("user", user);

		if (logger.isDebugEnabled()) {
			logger.debug("top(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "top";
	}

	@RequestMapping("/main.do")
	public String main() {
		return "main";
	}

	@RequestMapping("/left.do")
	public String left() {
		return "left";
	}

	@RequestMapping("/right.do")
	public String right() {
		return "right";
	}

	@Autowired
	private CmsSiteMng cmsSiteMng;
}
