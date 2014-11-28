package com.jeecms.bbs.action.member;

import org.apache.log4j.Logger;

import static com.jeecms.bbs.Constants.TPLDIR_MEMBER;
import static com.jeecms.bbs.Constants.TPLDIR_TOPIC;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeecms.bbs.entity.BbsCreditExchange;
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserExt;
import com.jeecms.bbs.manager.BbsCreditExchangeMng;
import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.security.encoder.PwdEncoder;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.UnifiedUserMng;

@Controller
public class UserPostAct {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(UserPostAct.class);

	public static final String MEMBER_CENTER = "tpl.memberCenter";
	public static final String MEMBER_INFORM = "tpl.information";
	public static final String MEMBER_TOPIC = "tpl.memberTopic";
	public static final String MEMBER_POST = "tpl.memberPost";
	public static final String SEARCH = "tpl.search";
	public static final String SEARCH_RESULT = "tpl.searchResult";
	public static final String TPL_NO_LOGIN = "tpl.nologin";
	public static final String TPL_CREDIT = "tpl.creditMng";
	public static final String TPL_EDIT_USERIMG = "tpl.edituserimg";

	@RequestMapping("/member/index.jhtml")
	public String index(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("index(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("index(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		model.put("user", user);
		FrontUtils.frontPageData(request, model);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, MEMBER_CENTER);
		if (logger.isDebugEnabled()) {
			logger.debug("index(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/member/information.jhtml")
	public String information(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("information(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("information(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		model.put("user", user);
		FrontUtils.frontPageData(request, model);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, MEMBER_INFORM);
		if (logger.isDebugEnabled()) {
			logger.debug("information(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}
	
	
	@RequestMapping("/member/editUserImg.jhtml")
	public String editUserImg(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("editUserImg(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("editUserImg(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		model.put("user", user);
		FrontUtils.frontPageData(request, model);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, TPL_EDIT_USERIMG);
		if (logger.isDebugEnabled()) {
			logger.debug("editUserImg(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/member/update.jspx")
	public String informationSubmit(String email,
			String newPassword, String signed, String avatar, BbsUserExt ext,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("informationSubmit(String, String, String, String, BbsUserExt, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("informationSubmit(String, String, String, String, BbsUserExt, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		user = manager.updateMember(user.getId(), email, newPassword, null, signed,
				avatar, ext, null);
		model.put("user", user);
		FrontUtils.frontPageData(request, model);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, MEMBER_INFORM);
		if (logger.isDebugEnabled()) {
			logger.debug("informationSubmit(String, String, String, String, BbsUserExt, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/member/mytopic*.jhtml")
	public String mytopic(Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("mytopic(Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("mytopic(Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		model.put("user", user);
		FrontUtils.frontPageData(request, model);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, MEMBER_TOPIC);
		if (logger.isDebugEnabled()) {
			logger.debug("mytopic(Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/member/mypost*.jhtml")
	public String mypost(Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("mypost(Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("mypost(Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		model.put("user", user);
		FrontUtils.frontPageData(request, model);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, MEMBER_POST);
		if (logger.isDebugEnabled()) {
			logger.debug("mypost(Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping(value = "/member/inputSearch.jhtml", method = RequestMethod.GET)
	public String search(Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("search(Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("search(Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		FrontUtils.frontPageData(request, model);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, SEARCH);
		if (logger.isDebugEnabled()) {
			logger.debug("search(Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping(value = "/member/search*.jhtml")
	public String searchSubmit( Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchSubmit(Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("searchSubmit(Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		String keywords = RequestUtils.getQueryParam(request, "keywords");
		
		Integer forumId=Integer.parseInt(RequestUtils.getQueryParam(request, "forumId"));
		
		model.put("keywords", keywords);
		model.put("forumId", forumId);
		FrontUtils.frontPageData(request, model);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, SEARCH_RESULT);
		if (logger.isDebugEnabled()) {
			logger.debug("searchSubmit(Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/member/creditManager.jhtml")
	public String creditManager(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("creditManager(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("creditManager(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		BbsCreditExchange creditExchangeRule = creditExchangeMng.findById(site
				.getId());
		Boolean exchangeAvailable = false;
		if (creditExchangeRule.getPointinavailable()
				&& creditExchangeRule.getPrestigeoutavailable()
				|| creditExchangeRule.getPointoutavailable()
				&& creditExchangeRule.getPrestigeinavailable()) {
			exchangeAvailable = true;
		} else {
			exchangeAvailable = false;
		}
		if (exchangeAvailable) {
			if (!creditExchangeRule.getExpoint().equals(0)
					&& !creditExchangeRule.getExprestige().equals(0)) {
				exchangeAvailable = true;
			} else {
				exchangeAvailable = false;
			}
		}
		List<BbsForum> forums = forumMng.getList(site.getId());
		model.put("user", user);
		model.put("exchangeAvailable", exchangeAvailable);
		model.put("pointInAvail", creditExchangeRule.getPointinavailable());
		model.put("pointOutAvail", creditExchangeRule.getPointoutavailable());
		model.put("prestigeInAvail", creditExchangeRule
				.getPrestigeinavailable());
		model.put("prestigeOutAvail", creditExchangeRule
				.getPrestigeoutavailable());
		model.put("forums", forums);
		model.put("creditExchangeRule", creditExchangeRule);
		FrontUtils.frontPageData(request, model);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, TPL_CREDIT);
		if (logger.isDebugEnabled()) {
			logger.debug("creditManager(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping(value = "/member/getCreditOutValue.jspx")
	public void getCreditOutValue(Integer creditIn, Integer creditInType,
			Integer creditOutType, double exchangetax,
			HttpServletRequest request, HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("getCreditOutValue(Integer, Integer, Integer, double, HttpServletRequest, HttpServletResponse) - start"); //$NON-NLS-1$
		}

		JSONObject object = new JSONObject();
		Long creditOutValue = null;
		Double temp = 0.0;
		BbsCreditExchange rule = CmsUtils.getSite(request).getCreditExchange();
		if (creditInType.equals(1) && creditOutType.equals(2)) {
			// 积分换取威望
			temp = creditIn * rule.getExpoint() * 1.0 / rule.getExprestige()
					* (1.0 + exchangetax);
		} else if (creditInType.equals(2) && creditOutType.equals(1)) {
			// 威望换积分
			temp = creditIn * rule.getExprestige() * 1.0 / rule.getExpoint()
					* (1.0 + exchangetax);
		}
		creditOutValue = Long.valueOf((long) Math.ceil(temp));
		try {
			object.put("creditOutValue", creditOutValue);
		} catch (JSONException e) {
			logger.error("getCreditOutValue(Integer, Integer, Integer, double, HttpServletRequest, HttpServletResponse)", e); //$NON-NLS-1$

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, object.toString());

		if (logger.isDebugEnabled()) {
			logger.debug("getCreditOutValue(Integer, Integer, Integer, double, HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
		}
	}

	@RequestMapping(value = "/member/creditExchange.jspx")
	public void creditExchange(Integer creditIn, Integer creditOut,
			Integer creditOutType, Integer miniBalance, String password,
			HttpServletRequest request, HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("creditExchange(Integer, Integer, Integer, Integer, String, HttpServletRequest, HttpServletResponse) - start"); //$NON-NLS-1$
		}

		JSONObject object = new JSONObject();
		BbsUser user = CmsUtils.getUser(request);
		Boolean result=true;
		Boolean balance=false;
		Integer flag=0;
		String message=MessageResolver.getMessage(request, "cmspoint.success");;
		//兑出的积分是否充足
		if(user!=null&&creditOutType.equals(1)){
			if(user.getPoint()-creditOut>miniBalance){
				balance=true;
			}else{
				flag=1;
			}
		}
		//兑出的威望是否充足
		if(user!=null&&creditOutType.equals(2)){
			if(user.getPrestige()-creditOut>miniBalance){
				balance=true;
			}else{
				flag=2;
			}
		}
		if (!pwdEncoder.isPasswordValid(unifiedUserMng.getByUsername(
				user.getUsername()).getPassword(), password)) {
			result = false;
			message=MessageResolver.getMessage(request, "cmscredit.passworderror");
		} else if (!balance) {
			result = false;
			if(flag.equals(1)){
				message=MessageResolver.getMessage(request, "cmscredit.pointisnotenough",miniBalance);
			}else if(flag.equals(2)){
				message=MessageResolver.getMessage(request, "cmscredit.prestigeisnotenough",miniBalance);
			}
		}else{
			if(creditOutType.equals(1)){
				user.setPoint(user.getPoint()-creditOut);
				user.setPrestige(user.getPrestige()+creditIn);
			}else if(creditOutType.equals(2)){
				user.setPrestige(user.getPrestige()-creditOut);
				user.setPoint(user.getPoint()+creditIn);
			}
			//此处更新用户积分威望信息
			manager.updatePwdEmail(user.getId(), password, user.getEmail());
		}
		try {
			object.put("message", message);
			object.put("result", result);
		} catch (JSONException e) {
			logger.error("creditExchange(Integer, Integer, Integer, Integer, String, HttpServletRequest, HttpServletResponse)", e); //$NON-NLS-1$

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, object.toString());

		if (logger.isDebugEnabled()) {
			logger.debug("creditExchange(Integer, Integer, Integer, Integer, String, HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
		}
	}

	@Autowired
	private BbsUserMng manager;
	@Autowired
	private BbsCreditExchangeMng creditExchangeMng;
	@Autowired
	private BbsForumMng forumMng;
	@Autowired
	private PwdEncoder pwdEncoder;
	@Autowired
	private UnifiedUserMng unifiedUserMng;
}
