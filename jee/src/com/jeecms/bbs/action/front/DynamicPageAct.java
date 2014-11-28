

package com.jeecms.bbs.action.front;

import org.apache.log4j.Logger;


import static com.jeecms.bbs.Constants.TPLDIR_FORUM;
import static com.jeecms.bbs.Constants.TPLDIR_INDEX;
import static com.jeecms.bbs.Constants.TPLDIR_TOPIC;
import static com.jeecms.common.web.Constants.INDEX;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jeecms.bbs.cache.TopicCountEhCache;
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsPostType;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserGroup;
import com.jeecms.bbs.manager.BbsConfigMng;
import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.bbs.manager.BbsPostTypeMng;
import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.front.URLHelper;

@Controller
public class DynamicPageAct {
	private static final Logger logger = Logger.getLogger(DynamicPageAct.class);

	public static final String TPL_INDEX = "tpl.index";
	public static final String TPL_FORUM = "tpl.forum";
	public static final String TPL_TOPIC = "tpl.topic";
	public static final String TPL_NO_VIEW = "tpl.noview";
	
	public static final String LOGIN_INPUT = "tpl.loginInput";
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, ModelMap model,HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("index(HttpServletRequest, ModelMap, HttpServletResponse) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		String processCookies = (String) session.getAttribute(request, "processCookies");
		if(!("true".equals(processCookies))&&!("false".equals(processCookies))){
			String result = cookieLogin(request,model,response);
			if(cookieLogin(request,model,response)!=null){
				if (logger.isDebugEnabled()) {
					logger.debug("index(HttpServletRequest, ModelMap, HttpServletResponse) - end"); //$NON-NLS-1$
				}
				return result;
			}
		}
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_INDEX, TPL_INDEX);
		if (logger.isDebugEnabled()) {
			logger.debug("index(HttpServletRequest, ModelMap, HttpServletResponse) - end"); //$NON-NLS-1$
		}
		return returnString;
	}
	public String cookieLogin(HttpServletRequest request,ModelMap model,HttpServletResponse response){
		if (logger.isDebugEnabled()) {
			logger.debug("cookieLogin(HttpServletRequest, ModelMap, HttpServletResponse) - start"); //$NON-NLS-1$
		}

		Cookie c_username = CookieUtils.getCookie(request, "bbs_username");
		Cookie c_password = CookieUtils.getCookie(request, "bbs_password");
		if(c_username==null||c_password==null){
			if (logger.isDebugEnabled()) {
				logger.debug("cookieLogin(HttpServletRequest, ModelMap, HttpServletResponse) - end"); //$NON-NLS-1$
			}
			return null;
		}
		String username = c_username.getValue();
		String password = c_password.getValue();
		if(!"".equals(username)&&!"".equals(password)){
			if (logger.isDebugEnabled()) {
				logger.debug("cookieLogin(HttpServletRequest, ModelMap, HttpServletResponse) - end"); //$NON-NLS-1$
			}
			return "redirect:login_cookie.jspx";
		}

		if (logger.isDebugEnabled()) {
			logger.debug("cookieLogin(HttpServletRequest, ModelMap, HttpServletResponse) - end"); //$NON-NLS-1$
		}
		return null;
	}
	
	/**
	 * WEBLOGIC的默认路径
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/index.jhtml", method = RequestMethod.GET)
	public String indexForWeblogic(HttpServletRequest request, ModelMap model,HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("indexForWeblogic(HttpServletRequest, ModelMap, HttpServletResponse) - start"); //$NON-NLS-1$
		}

		String returnString = index(request, model, response);
		if (logger.isDebugEnabled()) {
			logger.debug("indexForWeblogic(HttpServletRequest, ModelMap, HttpServletResponse) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping(value = "/**/*.*", method = RequestMethod.GET)
	public String dynamic(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,String ty) {
		if (logger.isDebugEnabled()) {
			logger.debug("dynamic(HttpServletRequest, HttpServletResponse, ModelMap, String) - start"); //$NON-NLS-1$
		}

		if(ty==null){
			ty="a";
		}
		String[] paths = URLHelper.getPaths(request);
		String requestUrl = request.getRequestURI();
		String address = requestUrl.substring(requestUrl.lastIndexOf('/')+1,requestUrl.lastIndexOf('.'));
		if(ty!=null){
			if(ty.length()>=4){
				if("jing".equals(ty.substring(0,4))){
					String returnString = forum(paths[0], null, request, response, model, "index_jing", ty);
					if (logger.isDebugEnabled()) {
						logger.debug("dynamic(HttpServletRequest, HttpServletResponse, ModelMap, String) - end"); //$NON-NLS-1$
					}
					return returnString;
				}
			}
		}
		
		if("index_jing".equals(address)){
			String returnString = forum(paths[0], null, request, response, model, "index_jing", ty);
			if (logger.isDebugEnabled()) {
				logger.debug("dynamic(HttpServletRequest, HttpServletResponse, ModelMap, String) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		int len = paths.length;
		if (len == 1) {
			if (logger.isDebugEnabled()) {
				logger.debug("dynamic(HttpServletRequest, HttpServletResponse, ModelMap, String) - end"); //$NON-NLS-1$
			}
			return null;
		} else if (len == 2) {
			if (paths[1].equals(INDEX)) {
				// 主题列表
				String returnString = forum(paths[0], null, request, response, model, "index", ty);
				if (logger.isDebugEnabled()) {
					logger.debug("dynamic(HttpServletRequest, HttpServletResponse, ModelMap, String) - end"); //$NON-NLS-1$
				}
				return returnString;
			} else {
				// 主题详细页
				try {
					Integer topicId = Integer.parseInt(paths[1]);
					String returnString = topic(paths[0], topicId, request, response, model);
					if (logger.isDebugEnabled()) {
						logger.debug("dynamic(HttpServletRequest, HttpServletResponse, ModelMap, String) - end"); //$NON-NLS-1$
					}
					return returnString;
				} catch (NumberFormatException e) {
					logger.error("dynamic(HttpServletRequest, HttpServletResponse, ModelMap, String)", e); //$NON-NLS-1$

					logger.debug("topic id must String: "+paths[1] );
					return FrontUtils.pageNotFound(request, response, model);
				}
			}
		}else if(len==3){
			// 主题分类列表
			String returnString = forum(paths[0], paths[2], request, response, model, "index", ty);
			if (logger.isDebugEnabled()) {
				logger.debug("dynamic(HttpServletRequest, HttpServletResponse, ModelMap, String) - end"); //$NON-NLS-1$
			}
			return returnString;
		}else {
			logger.debug("Illegal path length: " + len + ", paths: " + paths);
			return FrontUtils.pageNotFound(request, response, model);
		}
	}

	private String forum(String path,String typeId, HttpServletRequest request,
			HttpServletResponse response, ModelMap model,String type,String ty) {
		if (logger.isDebugEnabled()) {
			logger.debug("forum(String, String, HttpServletRequest, HttpServletResponse, ModelMap, String, String) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		BbsForum forum = bbsForumMng.getByPath(site.getId(), path);
		boolean check = checkView(forum, user, site);
		if (!check) {
			model.put("msg",MessageResolver.getMessage(request, "member.hasnopermission"));
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_FORUM, TPL_NO_VIEW);
			if (logger.isDebugEnabled()) {
				logger.debug("forum(String, String, HttpServletRequest, HttpServletResponse, ModelMap, String, String) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		model.put("manager", checkManager(forum, user, site));
		model.put("uptop", checkUpTop(forum, user, site));
		model.put("sheild", checkShield(forum, user, site));
		model.put("moderators", checkModerators(forum, user, site));
		model.put("forum", forum);
		if(StringUtils.isNotBlank(typeId)){
			Integer typeIds=Integer.valueOf(typeId);
			BbsPostType postType=bbsPostTypeMng.findById(typeIds);
			if(postType!=null&&postType.getParent()==null){
				//有子类的，包含子类
				if(postType.getChilds()!=null&&postType.getChilds().size()>0){
					model.put("parentTypeId", typeIds);
				}else{
					//帖子大类(没有子类)
					model.put("typeId", typeIds);
				}
			}else{
				model.put("typeId", typeIds);
			}
		}
		model.put("type", type);
		model.put("ty", ty);
		FrontUtils.frontPageData(request, model);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_FORUM, TPL_FORUM);
		if (logger.isDebugEnabled()) {
			logger.debug("forum(String, String, HttpServletRequest, HttpServletResponse, ModelMap, String, String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	private String topic(String path, Integer topicId,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("topic(String, Integer, HttpServletRequest, HttpServletResponse, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		BbsForum forum = bbsForumMng.getByPath(site.getId(), path);
		boolean check = checkView(forum, user, site);
		if (!check) {
			model.put("msg",MessageResolver.getMessage(request, "member.hasnopermission"));
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_FORUM, TPL_NO_VIEW);
			if (logger.isDebugEnabled()) {
				logger.debug("topic(String, Integer, HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		BbsTopic topic = bbsTopicMng.findById(topicId);
		topicCountEhCache.setViewCount(topicId);
		if(topic!=null&&topic.getPostType()!=null){
			model.put("postTypeId", topic.getPostType().getId());
		}
		model.put("moderators", checkModerators(forum, user, site));
		model.put("forum", forum);
		model.put("topic", topic);
		FrontUtils.frontPageData(request, model);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_TOPIC);
		if (logger.isDebugEnabled()) {
			logger.debug("topic(String, Integer, HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	private boolean checkView(BbsForum forum, BbsUser user, CmsSite site) {
		if (logger.isDebugEnabled()) {
			logger.debug("checkView(BbsForum, BbsUser, CmsSite) - start"); //$NON-NLS-1$
		}

		if (forum.getGroupViews() == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("checkView(BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
			}
			return false;
		} else {
			BbsUserGroup group = null;
			if (user == null) {
				group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
			} else {
				group = user.getGroup();
			}
			if (group == null) {
				if (logger.isDebugEnabled()) {
					logger.debug("checkView(BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return false;
			}
			if (forum.getGroupViews().indexOf("," + group.getId() + ",") == -1) {
				if (logger.isDebugEnabled()) {
					logger.debug("checkView(BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return false;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("checkView(BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
		}
		return true;
	}

	private boolean checkManager(BbsForum forum, BbsUser user, CmsSite site) {
		if (logger.isDebugEnabled()) {
			logger.debug("checkManager(BbsForum, BbsUser, CmsSite) - start"); //$NON-NLS-1$
		}

		if (forum.getGroupViews() == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("checkManager(BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
			}
			return false;
		} else {
			BbsUserGroup group = null;
			if (user == null) {
				group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
			} else {
				group = user.getGroup();
			}
			if (group == null) {
				if (logger.isDebugEnabled()) {
					logger.debug("checkManager(BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return false;
			}
			if (!group.hasRight(forum, user)) {
				if (logger.isDebugEnabled()) {
					logger.debug("checkManager(BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return false;
			}
			if (!group.topicManage()) {
				if (logger.isDebugEnabled()) {
					logger.debug("checkManager(BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return false;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("checkManager(BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
		}
		return true;
	}

	private boolean checkUpTop(BbsForum forum, BbsUser user, CmsSite site) {
		if (logger.isDebugEnabled()) {
			logger.debug("checkUpTop(BbsForum, BbsUser, CmsSite) - start"); //$NON-NLS-1$
		}

		if (forum.getGroupViews() == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("checkUpTop(BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
			}
			return false;
		} else {
			BbsUserGroup group = null;
			if (user == null) {
				group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
			} else {
				group = user.getGroup();
			}
			if (group == null) {
				if (logger.isDebugEnabled()) {
					logger.debug("checkUpTop(BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return false;
			}
			if (!group.hasRight(forum, user)) {
				if (logger.isDebugEnabled()) {
					logger.debug("checkUpTop(BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return false;
			}
			if (group.topicTop() == 0) {
				if (logger.isDebugEnabled()) {
					logger.debug("checkUpTop(BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return false;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("checkUpTop(BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
		}
		return true;
	}

	private boolean checkShield(BbsForum forum, BbsUser user, CmsSite site) {
		if (logger.isDebugEnabled()) {
			logger.debug("checkShield(BbsForum, BbsUser, CmsSite) - start"); //$NON-NLS-1$
		}

		if (forum.getGroupViews() == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("checkShield(BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
			}
			return false;
		} else {
			BbsUserGroup group = null;
			if (user == null) {
				group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
			} else {
				group = user.getGroup();
			}
			if (group == null) {
				if (logger.isDebugEnabled()) {
					logger.debug("checkShield(BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return false;
			}
			if (!group.hasRight(forum, user)) {
				if (logger.isDebugEnabled()) {
					logger.debug("checkShield(BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return false;
			}
			if (!group.topicShield()) {
				if (logger.isDebugEnabled()) {
					logger.debug("checkShield(BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return false;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("checkShield(BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
		}
		return true;
	}

	private boolean checkModerators(BbsForum forum, BbsUser user, CmsSite site) {
		if (logger.isDebugEnabled()) {
			logger.debug("checkModerators(BbsForum, BbsUser, CmsSite) - start"); //$NON-NLS-1$
		}

		if (forum.getGroupViews() == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("checkModerators(BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
			}
			return false;
		} else {
			BbsUserGroup group = null;
			if (user == null) {
				group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
			} else {
				group = user.getGroup();
			}
			if (group == null) {
				if (logger.isDebugEnabled()) {
					logger.debug("checkModerators(BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return false;
			}
			if (!group.hasRight(forum, user)) {
				if (logger.isDebugEnabled()) {
					logger.debug("checkModerators(BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return false;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("checkModerators(BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
		}
		return true;
	}

	@Autowired
	private BbsTopicMng bbsTopicMng;
	@Autowired
	private BbsForumMng bbsForumMng;
	@Autowired
	private BbsConfigMng bbsConfigMng;
	@Autowired
	private TopicCountEhCache topicCountEhCache;
	@Autowired
	private SessionProvider session;
	@Autowired
	private BbsPostTypeMng bbsPostTypeMng;

}
