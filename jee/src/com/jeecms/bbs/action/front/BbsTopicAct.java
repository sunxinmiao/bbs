package com.jeecms.bbs.action.front;

import static com.jeecms.bbs.Constants.TPLDIR_FORUM;
import static com.jeecms.bbs.Constants.TPLDIR_TOPIC;
import static com.jeecms.bbs.entity.BbsTopic.TOPIC_NORMAL;
import static com.jeecms.bbs.entity.BbsTopic.TOPIC_VOTE;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.jeecms.bbs.cache.TopicCountEhCache;
import com.jeecms.bbs.entity.BbsCategory;
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsPostType;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserGroup;
import com.jeecms.bbs.entity.BbsVoteItem;
import com.jeecms.bbs.entity.BbsVoteTopic;
import com.jeecms.bbs.manager.BbsCategoryMng;
import com.jeecms.bbs.manager.BbsConfigMng;
import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.bbs.manager.BbsUserGroupMng;
import com.jeecms.bbs.manager.BbsVoteItemMng;
import com.jeecms.bbs.manager.BbsVoteRecordMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.bbs.web.WebErrors;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.core.entity.CmsSite;

@Controller
public class BbsTopicAct {
	private static final Logger logger = Logger.getLogger(BbsTopicAct.class);
	public static final String CATEGORY_VOTE = "vote";

	public static final String TPL_TOPICADD = "tpl.topicadd";
	public static final String TPL_TOPICEDIT = "tpl.topicedit";
	public static final String TPL_NO_LOGIN = "tpl.nologin";
	public static final String TPL_GUANSHUI = "tpl.guanshui";
	public static final String TPL_TOPIC_MOVE = "tpl.topicmove";
	public static final String TPL_TOPIC_SHIELD = "tpl.topicshield";
	public static final String TPL_TOPIC_LOCK = "tpl.topiclock";
	public static final String TPL_TOPIC_UPORDOWN = "tpl.topicupordown";
	public static final String TPL_TOPIC_PRIME = "tpl.topicprime";
	public static final String TPL_TOPIC_UPTOP = "tpl.topicuptop";
	public static final String TPL_TOPIC_HIGHLIGHT = "tpl.topichighlight";
	public static final String TPL_TOPIC_VOTERESULT = "tpl.topicVoteResult";
	public static final String TPL_DAY_TOPIC = "tpl.daytopic";
	public static final String TPL_NO_VIEW = "tpl.noview";

	@RequestMapping("/topic/v_add{forumId}.jspx")
	public String add(@PathVariable Integer forumId, String category,
			Integer fid, HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("add(Integer, String, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("add(Integer, String, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		BbsForum forum = null;
		if (forumId != null) {
			model.put("forumId", forumId);
			forum = bbsForumMng.findById(forumId);
		} else {
			model.put("forumId", fid);
			forum = bbsForumMng.findById(fid);
		}
		String msg = checkTopic(request,forum, user, site);
		Set<BbsPostType>postTypes=user.getPostTypeByForum(forum);
		model.put("postTypes", postTypes);
		if(postTypes.size()<=0){
			msg=MessageResolver.getMessage(request, "member.hasnoforumpermission",forum.getTitle());
		}
		if (msg != null) {
			model.put("msg", msg);
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
			if (logger.isDebugEnabled()) {
				logger.debug("add(Integer, String, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		model.put("category", parseCategory(category));
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_TOPICADD);
		if (logger.isDebugEnabled()) {
			logger.debug("add(Integer, String, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/topic/o_save.jspx")
	public String save(Integer forumId, Integer postTypeId, String title,
			String content, Integer category, String[] name,
			@RequestParam(value = "code", required = false) List<String> code,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(Integer, Integer, String, String, Integer, String[], List<String>, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("save(Integer, Integer, String, String, Integer, String[], List<String>, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		BbsForum forum = bbsForumMng.findById(forumId);
		String msg = checkTopic(request,forum, user, site);
		if (msg != null) {
			model.put("msg", msg);
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
			if (logger.isDebugEnabled()) {
				logger.debug("save(Integer, Integer, String, String, Integer, String[], List<String>, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		boolean flag = topicCountEhCache.getLastReply(user.getId(), 15);
		if (!flag) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_GUANSHUI);
			if (logger.isDebugEnabled()) {
				logger.debug("save(Integer, Integer, String, String, Integer, String[], List<String>, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		String ip = RequestUtils.getIpAddr(request);
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		BbsTopic bean = manager.postTopic(user.getId(), site.getId(), forumId,
				postTypeId, title, content, ip, category, name,
				multipartRequest.getFiles("attachment"), code);
		logger.info("save BbsTopic id="+ bean.getId());
		bbsUserGroupMng.findNearByPoint(user.getPoint(), user);
		String returnString = "redirect:" + bean.getRedirectUrl();
		if (logger.isDebugEnabled()) {
			logger.debug("save(Integer, Integer, String, String, Integer, String[], List<String>, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/topic/v_moveInput.jspx")
	public String moveInput(Integer[] topicIds, Integer forumId,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("moveInput(Integer[], Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("moveInput(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		if (forumId != null) {
			BbsForum forum = bbsForumMng.findById(forumId);
			String msg = checkShield(request,forum, user, site);
			if (msg != null) {
				model.put("msg", msg);
				String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
				if (logger.isDebugEnabled()) {
					logger.debug("moveInput(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
				}
				return returnString;
			}
			model.put("forum", forum);
		}
		List<BbsCategory> categoryList = bbsCategoryMng.getList(site.getId());
		model.put("categoryList", categoryList);
		model.put("topicIds", topicIds);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_TOPIC_MOVE);
		if (logger.isDebugEnabled()) {
			logger.debug("moveInput(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/topic/o_moveSubmit.jspx")
	public String moveSubmit(Integer[] topicIds, Integer moveTo,
			Integer forumId, String reason, HttpServletRequest request,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("moveSubmit(Integer[], Integer, Integer, String, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("moveSubmit(Integer[], Integer, Integer, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		BbsForum forum = bbsForumMng.findById(forumId);
		String msg = checkShield(request,forum, user, site);
		if (msg != null) {
			model.put("msg", msg);
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
			if (logger.isDebugEnabled()) {
				logger.debug("moveSubmit(Integer[], Integer, Integer, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		manager.move(topicIds, moveTo, reason, user);
		String returnString = "redirect:" + forum.getRedirectUrl();
		if (logger.isDebugEnabled()) {
			logger.debug("moveSubmit(Integer[], Integer, Integer, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/topic/v_shieldInput.jspx")
	public String shieldInput(Integer[] topicIds, Integer forumId,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("shieldInput(Integer[], Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("shieldInput(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		if (forumId != null) {
			BbsForum forum = bbsForumMng.findById(forumId);
			String msg = checkShield(request,forum, user, site);
			if (msg != null) {
				model.put("msg", msg);
				String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
				if (logger.isDebugEnabled()) {
					logger.debug("shieldInput(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
				}
				return returnString;
			}
			model.put("forum", forum);
		}
		model.put("topicIds", topicIds);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_TOPIC_SHIELD);
		if (logger.isDebugEnabled()) {
			logger.debug("shieldInput(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/topic/o_shieldSubmit.jspx")
	public String shieldSubmit(Integer[] topicIds, Integer forumId,
			boolean shield, String reason, HttpServletRequest request,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("shieldSubmit(Integer[], Integer, boolean, String, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("shieldSubmit(Integer[], Integer, boolean, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		BbsForum forum = bbsForumMng.findById(forumId);
		String msg = checkShield(request,forum, user, site);
		if (msg != null) {
			model.put("msg", msg);
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
			if (logger.isDebugEnabled()) {
				logger.debug("shieldSubmit(Integer[], Integer, boolean, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		manager.shieldOrOpen(topicIds, shield, reason, user);
		String returnString = "redirect:" + forum.getRedirectUrl();
		if (logger.isDebugEnabled()) {
			logger.debug("shieldSubmit(Integer[], Integer, boolean, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/topic/v_lockInput.jspx")
	public String lockInput(Integer[] topicIds, Integer forumId,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("lockInput(Integer[], Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("lockInput(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		if (forumId != null) {
			BbsForum forum = bbsForumMng.findById(forumId);
			String msg = checkManager(request,forum, user, site);
			if (msg != null) {
				model.put("msg", msg);
				String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
				if (logger.isDebugEnabled()) {
					logger.debug("lockInput(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
				}
				return returnString;
			}
			model.put("forum", forum);
		}
		model.put("topicIds", topicIds);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_TOPIC_LOCK);
		if (logger.isDebugEnabled()) {
			logger.debug("lockInput(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/topic/o_lockSumbit.jspx")
	public String lockSubmit(Integer[] topicIds, Integer forumId, boolean lock,
			String reason, HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("lockSubmit(Integer[], Integer, boolean, String, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("lockSubmit(Integer[], Integer, boolean, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		BbsForum forum = bbsForumMng.findById(forumId);
		String msg = checkManager(request,forum, user, site);
		if (msg != null) {
			model.put("msg", msg);
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
			if (logger.isDebugEnabled()) {
				logger.debug("lockSubmit(Integer[], Integer, boolean, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		manager.lockOrOpen(topicIds, lock, reason, user);
		String returnString = "redirect:" + forum.getRedirectUrl();
		if (logger.isDebugEnabled()) {
			logger.debug("lockSubmit(Integer[], Integer, boolean, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/topic/v_upordownInput.jspx")
	public String upOrDownInput(Integer[] topicIds, Integer forumId,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("upOrDownInput(Integer[], Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("upOrDownInput(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		if (forumId != null) {
			BbsForum forum = bbsForumMng.findById(forumId);
			String msg = checkManager(request,forum, user, site);
			if (msg != null) {
				model.put("msg", msg);
				String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
				if (logger.isDebugEnabled()) {
					logger.debug("upOrDownInput(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
				}
				return returnString;
			}
			model.put("forum", forum);
		}
		model.put("topicIds", topicIds);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_TOPIC_UPORDOWN);
		if (logger.isDebugEnabled()) {
			logger.debug("upOrDownInput(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/topic/o_upordownSubmit.jspx")
	public String upOrDownSubmit(Integer[] topicIds, Integer forumId,
			Date time, String reason, HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("upOrDownSubmit(Integer[], Integer, Date, String, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("upOrDownSubmit(Integer[], Integer, Date, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		BbsForum forum = bbsForumMng.findById(forumId);
		String msg = checkManager(request,forum, user, site);
		if (msg != null) {
			model.put("msg", msg);
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
			if (logger.isDebugEnabled()) {
				logger.debug("upOrDownSubmit(Integer[], Integer, Date, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		manager.upOrDown(topicIds, time, reason, user);
		String returnString = "redirect:" + forum.getRedirectUrl();
		if (logger.isDebugEnabled()) {
			logger.debug("upOrDownSubmit(Integer[], Integer, Date, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/topic/v_primeInput.jspx")
	public String primeInput(Integer[] topicIds, Integer forumId,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("primeInput(Integer[], Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("primeInput(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		if (forumId != null) {
			BbsForum forum = bbsForumMng.findById(forumId);
			String msg = checkManager(request,forum, user, site);
			if (msg != null) {
				model.put("msg", msg);
				String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
				if (logger.isDebugEnabled()) {
					logger.debug("primeInput(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
				}
				return returnString;
			}
			model.put("forum", forum);
		}
		model.put("topicIds", topicIds);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_TOPIC_PRIME);
		if (logger.isDebugEnabled()) {
			logger.debug("primeInput(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/topic/o_primeSubmit.jspx")
	public String primeSubmit(Integer[] topicIds, Integer forumId,
			short primeLevel, String reason, HttpServletRequest request,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("primeSubmit(Integer[], Integer, short, String, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("primeSubmit(Integer[], Integer, short, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		BbsForum forum = bbsForumMng.findById(forumId);
		String msg = checkManager(request,forum, user, site);
		if (msg != null) {
			model.put("msg", msg);
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
			if (logger.isDebugEnabled()) {
				logger.debug("primeSubmit(Integer[], Integer, short, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		manager.prime(topicIds, primeLevel, reason, user);
		String returnString = "redirect:" + forum.getRedirectUrl();
		if (logger.isDebugEnabled()) {
			logger.debug("primeSubmit(Integer[], Integer, short, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/topic/v_upTopInput.jspx")
	public String upTopInput(Integer[] topicIds, Integer forumId,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("upTopInput(Integer[], Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("upTopInput(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		if (forumId != null) {
			BbsForum forum = bbsForumMng.findById(forumId);
			String msg = checkUpTop(request,forum, user, site, BbsTopic.NORMAL);
			if (msg != null) {
				model.put("msg", msg);
				String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
				if (logger.isDebugEnabled()) {
					logger.debug("upTopInput(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
				}
				return returnString;
			}
			model.put("forum", forum);
		}
		model.put("topicIds", topicIds);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_TOPIC_UPTOP);
		if (logger.isDebugEnabled()) {
			logger.debug("upTopInput(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/topic/o_upTopSubmit.jspx")
	public String upTopSubmit(Integer[] topicIds, Integer forumId,
			short topLevel, String reason, HttpServletRequest request,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("upTopSubmit(Integer[], Integer, short, String, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("upTopSubmit(Integer[], Integer, short, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		BbsForum forum = bbsForumMng.findById(forumId);
		String msg = checkUpTop(request,forum, user, site, BbsTopic.NORMAL);
		if (msg != null) {
			model.put("msg", msg);
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
			if (logger.isDebugEnabled()) {
				logger.debug("upTopSubmit(Integer[], Integer, short, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		manager.upTop(topicIds, topLevel, reason, user);
		String returnString = "redirect:" + forum.getRedirectUrl();
		if (logger.isDebugEnabled()) {
			logger.debug("upTopSubmit(Integer[], Integer, short, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/topic/v_highlightInput.jspx")
	public String highlightInput(Integer[] topicIds, Integer forumId,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("highlightInput(Integer[], Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("highlightInput(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		if (forumId != null) {
			BbsForum forum = bbsForumMng.findById(forumId);
			String msg = checkManager(request,forum, user, site);
			if (msg != null) {
				model.put("msg", msg);
				String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
				if (logger.isDebugEnabled()) {
					logger.debug("highlightInput(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
				}
				return returnString;
			}
			model.put("forum", forum);
		}
		model.put("topicIds", topicIds);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_TOPIC_HIGHLIGHT);
		if (logger.isDebugEnabled()) {
			logger.debug("highlightInput(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/topic/o_highlightSubmit.jspx")
	public String highlightSubmit(Integer[] topicIds, Integer forumId,
			String color, boolean bold, boolean italic, Date time,
			String reason, HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("highlightSubmit(Integer[], Integer, String, boolean, boolean, Date, String, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("highlightSubmit(Integer[], Integer, String, boolean, boolean, Date, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		BbsForum forum = bbsForumMng.findById(forumId);
		String msg = checkManager(request,forum, user, site);
		if (msg != null) {
			model.put("msg", msg);
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
			if (logger.isDebugEnabled()) {
				logger.debug("highlightSubmit(Integer[], Integer, String, boolean, boolean, Date, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		manager.highlight(topicIds, color, bold, italic, time, reason, user);
		String returnString = "redirect:" + forum.getRedirectUrl();
		if (logger.isDebugEnabled()) {
			logger.debug("highlightSubmit(Integer[], Integer, String, boolean, boolean, Date, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/topic/v_searchDayTopic.jspx")
	public String searchByDay(Integer forumId, Integer day,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchByDay(Integer, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		BbsForum forum = bbsForumMng.findById(forumId);
		boolean check = checkView(forum, user, site);
		if (!check) {
			model.put("msg",MessageResolver.getMessage(request, "member.hasnopermission"));
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_FORUM, TPL_NO_VIEW);
			if (logger.isDebugEnabled()) {
				logger.debug("searchByDay(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		model.put("manager", checkManager(request,forum, user, site) == null ? true
				: false);
		model.put("uptop", checkUpTop(forum, user, site));
		model.put("sheild", checkShield(request,forum, user, site) == null ? true
				: false);
		model.put("moderators", checkModerators(forum, user, site));
		model.put("forum", forum);
		model.put("day", day);
		FrontUtils.frontPageData(request, model);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_DAY_TOPIC);
		if (logger.isDebugEnabled()) {
			logger.debug("searchByDay(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/topic/o_delete.jspx")
	public String delete(Integer[] topicIds, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("delete(Integer[], Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		BbsTopic[] beans = manager.deleteByIds(topicIds);
		BbsForum forum = beans[0].getForum();
		for (BbsTopic bean : beans) {
			logger.info("delete BbsTopic id="+ bean.getId());
		}
		String returnString = "redirect:" + forum.getRedirectUrl();
		if (logger.isDebugEnabled()) {
			logger.debug("delete(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/topic/vote_result.jspx")
	public String result(Integer tid, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("result(Integer, HttpServletRequest, HttpServletResponse, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		BbsTopic topic = manager.findById(tid);
		if (bbsVoteRecordMng.findRecord(user == null ? null : user.getId(), tid) != null) {
			model.addAttribute("voted", true);
		}
		List<BbsVoteItem> list = bbsVoteItemMng.findByTopic(tid);
		model.addAttribute("voteItems", list);
		model.addAttribute("topic", topic);
		FrontUtils.frontData(request, model, site);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_TOPIC_VOTERESULT);
		if (logger.isDebugEnabled()) {
			logger.debug("result(Integer, HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/topic/vote.jspx")
	public void vote(Integer tid, Integer[] itemIds,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws JSONException {
		if (logger.isDebugEnabled()) {
			logger.debug("vote(Integer, Integer[], HttpServletRequest, HttpServletResponse, ModelMap) - start"); //$NON-NLS-1$
		}

		BbsUser user = CmsUtils.getUser(request);
		BbsVoteTopic topic = (BbsVoteTopic) manager.findById(tid);
		JSONObject json = new JSONObject();
		WebErrors errors = checkVote(request, user, topic, itemIds);
		if (!errors.hasErrors()) {
			bbsVoteItemMng.vote(user, topic, itemIds);
			json.put("success", true);
		} else {
			json.put("success", false);
			json.put("message", errors.getErrors());
		}
		ResponseUtils.renderJson(response, json.toString());

		if (logger.isDebugEnabled()) {
			logger.debug("vote(Integer, Integer[], HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
		}
	}

	private WebErrors checkVote(HttpServletRequest request, BbsUser user,
			BbsTopic topic, Integer[] itemIds) {
		if (logger.isDebugEnabled()) {
			logger.debug("checkVote(HttpServletRequest, BbsUser, BbsTopic, Integer[]) - start"); //$NON-NLS-1$
		}

		WebErrors errors = WebErrors.create(request);
		if (user == null) {
			errors.addErrorCode("vote.noLogin");

			if (logger.isDebugEnabled()) {
				logger.debug("checkVote(HttpServletRequest, BbsUser, BbsTopic, Integer[]) - end"); //$NON-NLS-1$
			}
			return errors;
		}
		if (itemIds == null) {
			errors.addErrorCode("vote.noChoose");

			if (logger.isDebugEnabled()) {
				logger.debug("checkVote(HttpServletRequest, BbsUser, BbsTopic, Integer[]) - end"); //$NON-NLS-1$
			}
			return errors;
		}
		// 选项不是同一主题
		for (Integer itemid : itemIds) {
			if (!topic.equals(bbsVoteItemMng.findById(itemid).getTopic())) {
				errors.addErrorCode("vote.wrongItem");

				if (logger.isDebugEnabled()) {
					logger.debug("checkVote(HttpServletRequest, BbsUser, BbsTopic, Integer[]) - end"); //$NON-NLS-1$
				}
				return errors;
			}
		}
		// 已经投过票
		if (bbsVoteRecordMng.findRecord(user.getId(), topic.getId()) != null) {
			errors.addErrorCode("vote.hasVoted");

			if (logger.isDebugEnabled()) {
				logger.debug("checkVote(HttpServletRequest, BbsUser, BbsTopic, Integer[]) - end"); //$NON-NLS-1$
			}
			return errors;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("checkVote(HttpServletRequest, BbsUser, BbsTopic, Integer[]) - end"); //$NON-NLS-1$
		}
		return errors;
	}

	private String checkTopic(HttpServletRequest request,BbsForum forum, BbsUser user, CmsSite site) {
		if (logger.isDebugEnabled()) {
			logger.debug("checkTopic(HttpServletRequest, BbsForum, BbsUser, CmsSite) - start"); //$NON-NLS-1$
		}

		String msg="";
		if (forum.getGroupTopics() == null) {
			msg=MessageResolver.getMessage(request, "member.hasnopermission");

			if (logger.isDebugEnabled()) {
				logger.debug("checkTopic(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
			}
			return msg;
		} else {
			BbsUserGroup group = null;
			if (user == null) {
				group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
			} else {
				if (user.getProhibit()) {
					msg=MessageResolver.getMessage(request, "member.gag");

					if (logger.isDebugEnabled()) {
						logger.debug("checkTopic(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
					}
					return msg;
				}
				group = user.getGroup();
			}
			if (group == null) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger.debug("checkTopic(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
			if (!group.allowTopic()) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger.debug("checkTopic(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
			if (forum.getGroupTopics().indexOf("," + group.getId() + ",") == -1) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger.debug("checkTopic(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
			if (!group.checkPostToday(user.getPostToday())) {
				msg=MessageResolver.getMessage(request, "member.posttomany");

				if (logger.isDebugEnabled()) {
					logger.debug("checkTopic(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("checkTopic(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
		}
		return null;
	}

	private String checkManager(HttpServletRequest request,BbsForum forum, BbsUser user, CmsSite site) {
		if (logger.isDebugEnabled()) {
			logger.debug("checkManager(HttpServletRequest, BbsForum, BbsUser, CmsSite) - start"); //$NON-NLS-1$
		}

		String msg;
		if (forum.getGroupTopics() == null) {
			msg=MessageResolver.getMessage(request, "member.hasnopermission");

			if (logger.isDebugEnabled()) {
				logger.debug("checkManager(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
			}
			return msg;
		} else {
			BbsUserGroup group = null;
			if (user == null) {
				group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
			} else {
				group = user.getGroup();
			}
			if (group == null) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger.debug("checkManager(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
			if (!group.allowTopic()) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger.debug("checkManager(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
			if (forum.getGroupTopics().indexOf("," + group.getId() + ",") == -1) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger.debug("checkManager(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
			if (!group.hasRight(forum, user)) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger.debug("checkManager(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
			if (!group.topicManage()) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger.debug("checkManager(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("checkManager(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
		}
		return null;
	}

	private String checkUpTop(HttpServletRequest request,BbsForum forum, BbsUser user, CmsSite site,
			short topLevel) {
		if (logger.isDebugEnabled()) {
			logger.debug("checkUpTop(HttpServletRequest, BbsForum, BbsUser, CmsSite, short) - start"); //$NON-NLS-1$
		}

		String msg;
		if (forum.getGroupTopics() == null) {
			msg=MessageResolver.getMessage(request, "member.hasnopermission");

			if (logger.isDebugEnabled()) {
				logger.debug("checkUpTop(HttpServletRequest, BbsForum, BbsUser, CmsSite, short) - end"); //$NON-NLS-1$
			}
			return msg;
		} else {
			BbsUserGroup group = null;
			if (user == null) {
				group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
			} else {
				group = user.getGroup();
			}
			if (group == null) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger.debug("checkUpTop(HttpServletRequest, BbsForum, BbsUser, CmsSite, short) - end"); //$NON-NLS-1$
				}
				return msg;
			}
			if (!group.allowTopic()) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger.debug("checkUpTop(HttpServletRequest, BbsForum, BbsUser, CmsSite, short) - end"); //$NON-NLS-1$
				}
				return msg;
			}
			if (forum.getGroupTopics().indexOf("," + group.getId() + ",") == -1) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger.debug("checkUpTop(HttpServletRequest, BbsForum, BbsUser, CmsSite, short) - end"); //$NON-NLS-1$
				}
				return msg;
			}
			if (!group.hasRight(forum, user)) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger.debug("checkUpTop(HttpServletRequest, BbsForum, BbsUser, CmsSite, short) - end"); //$NON-NLS-1$
				}
				return msg;
			}
			if (group.topicTop() == 0) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger.debug("checkUpTop(HttpServletRequest, BbsForum, BbsUser, CmsSite, short) - end"); //$NON-NLS-1$
				}
				return msg;
			}
			if (topLevel > 0) {
				if (group.topicTop() < topLevel) {
					msg=MessageResolver.getMessage(request, "member.hasnopermission");

					if (logger.isDebugEnabled()) {
						logger.debug("checkUpTop(HttpServletRequest, BbsForum, BbsUser, CmsSite, short) - end"); //$NON-NLS-1$
					}
					return msg;
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("checkUpTop(HttpServletRequest, BbsForum, BbsUser, CmsSite, short) - end"); //$NON-NLS-1$
		}
		return null;
	}

	private String checkShield(HttpServletRequest request,BbsForum forum, BbsUser user, CmsSite site) {
		if (logger.isDebugEnabled()) {
			logger.debug("checkShield(HttpServletRequest, BbsForum, BbsUser, CmsSite) - start"); //$NON-NLS-1$
		}

		String msg;
		if (forum.getGroupTopics() == null) {
			msg=MessageResolver.getMessage(request, "member.hasnopermission");

			if (logger.isDebugEnabled()) {
				logger.debug("checkShield(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
			}
			return msg;
		} else {
			BbsUserGroup group = null;
			if (user == null) {
				group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
			} else {
				group = user.getGroup();
			}
			if (group == null) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger.debug("checkShield(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
			if (!group.allowTopic()) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger.debug("checkShield(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
			if (forum.getGroupTopics().indexOf("," + group.getId() + ",") == -1) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger.debug("checkShield(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
			if (!group.hasRight(forum, user)) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger.debug("checkShield(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
			if (!group.topicShield()) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger.debug("checkShield(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("checkShield(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
		}
		return null;
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

	private Integer parseCategory(String category) {
		if (logger.isDebugEnabled()) {
			logger.debug("parseCategory(String) - start"); //$NON-NLS-1$
		}

		if (CATEGORY_VOTE.equals(category)) {
			if (logger.isDebugEnabled()) {
				logger.debug("parseCategory(String) - end"); //$NON-NLS-1$
			}
			return TOPIC_VOTE;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("parseCategory(String) - end"); //$NON-NLS-1$
		}
		return TOPIC_NORMAL;
	}

	@Autowired
	private BbsTopicMng manager;
	@Autowired
	private BbsForumMng bbsForumMng;
	@Autowired
	private BbsCategoryMng bbsCategoryMng;
	@Autowired
	private BbsConfigMng bbsConfigMng;
	@Autowired
	private TopicCountEhCache topicCountEhCache;
	@Autowired
	private BbsUserGroupMng bbsUserGroupMng;
	@Autowired
	private BbsVoteItemMng bbsVoteItemMng;
	@Autowired
	private BbsVoteRecordMng bbsVoteRecordMng;
}
