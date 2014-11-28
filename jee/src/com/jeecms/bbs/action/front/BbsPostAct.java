package com.jeecms.bbs.action.front;

import static com.jeecms.bbs.Constants.TPLDIR_POST;
import static com.jeecms.bbs.Constants.TPLDIR_TOPIC;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jeecms.bbs.cache.TopicCountEhCache;
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsGrade;
import com.jeecms.bbs.entity.BbsPost;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserGroup;
import com.jeecms.bbs.manager.BbsConfigMng;
import com.jeecms.bbs.manager.BbsGradeMng;
import com.jeecms.bbs.manager.BbsPostMng;
import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.bbs.manager.BbsUserGroupMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.MagicMessage;

@Controller
public class BbsPostAct {
	private static final Logger logger = Logger.getLogger(BbsPostAct.class);

	public static final String TPL_POSTADD = "tpl.postadd";
	public static final String TPL_POSTEDIT = "tpl.postedit";
	public static final String TPL_NO_LOGIN = "tpl.nologin";
	public static final String TPL_NO_URL = "tpl.nourl";
	public static final String TPL_POST_QUOTE = "tpl.postquote";
	public static final String TPL_GUANSHUI = "tpl.guanshui";
	public static final String TPL_POST_GRADE = "tpl.postgrade";
	public static final String TPL_NO_VIEW = "tpl.noview";

	@RequestMapping("/post/v_add{topicId}.jspx")
	public String add(@PathVariable
	Integer topicId, Integer tid, HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("add(Integer, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		if (topicId == null && tid == null) {
			String returnString = FrontUtils.getTplPath(request, site
					.getSolutionPath(), TPLDIR_POST, TPL_NO_URL);
			if (logger.isDebugEnabled()) {
				logger
						.debug("add(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site
					.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger
						.debug("add(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		BbsTopic topic = null;
		if (topicId != null) {
			model.put("topicId", topicId);
			topic = bbsTopicMng.findById(topicId);
		} else if (tid != null) {
			model.put("topicId", tid);
			topic = bbsTopicMng.findById(tid);
		}
		String msg = null;
		// 主题是否关闭
		if (!topic.getAllayReply()) {
			MagicMessage magicMessage = MagicMessage.create(request);
			msg = magicMessage.getMessage("magic.open.error");
		} else {
			// 检查用户权限
			msg = checkReply(request, topic.getForum(), user, site);
		}
		if (msg != null) {
			model.put("msg", msg);
			String returnString = FrontUtils.getTplPath(request, site
					.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
			if (logger.isDebugEnabled()) {
				logger
						.debug("add(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		if (topic != null && topic.getPostType() != null) {
			model.put("postTypeId", topic.getPostType().getId());
		}
		String returnString = FrontUtils.getTplPath(request, site
				.getSolutionPath(), TPLDIR_POST, TPL_POSTADD);
		if (logger.isDebugEnabled()) {
			logger
					.debug("add(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/post/v_edit{id}.jspx")
	public String edit(@PathVariable
	Integer id, Integer pageNo, HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("edit(Integer, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site
					.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger
						.debug("edit(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		if (id != null) {
			String msg = checkEdit(request, manager.findById(id).getTopic()
					.getForum(), manager.findById(id), user, site);
			if (msg != null) {
				model.put("msg", msg);
				String returnString = FrontUtils.getTplPath(request, site
						.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
				if (logger.isDebugEnabled()) {
					logger
							.debug("edit(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
				}
				return returnString;
			}
			model.addAttribute("post", manager.findById(id));
		} else {
			String returnString = FrontUtils.getTplPath(request, site
					.getSolutionPath(), TPLDIR_POST, TPL_NO_URL);
			if (logger.isDebugEnabled()) {
				logger
						.debug("edit(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		String returnString = FrontUtils.getTplPath(request, site
				.getSolutionPath(), TPLDIR_POST, TPL_POSTEDIT);
		if (logger.isDebugEnabled()) {
			logger
					.debug("edit(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/post/o_save.jspx")
	public String save(BbsPost bean, Integer topicId, Integer postTypeId,
			String title, String content,
			@RequestParam(value = "code", required = false)
			List<String> code, HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("save(BbsPost, Integer, Integer, String, String, List<String>, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site
					.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger
						.debug("save(BbsPost, Integer, Integer, String, String, List<String>, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		String msg = checkReply(request, bbsTopicMng.findById(topicId)
				.getForum(), user, site);
		if (msg != null) {
			model.put("msg", msg);
			String returnString = FrontUtils.getTplPath(request, site
					.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
			if (logger.isDebugEnabled()) {
				logger
						.debug("save(BbsPost, Integer, Integer, String, String, List<String>, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		boolean flag = topicCountEhCache.getLastReply(user.getId(), user
				.getGroup().getPostInterval());
		if (!flag) {
			String returnString = FrontUtils.getTplPath(request, site
					.getSolutionPath(), TPLDIR_TOPIC, TPL_GUANSHUI);
			if (logger.isDebugEnabled()) {
				logger
						.debug("save(BbsPost, Integer, Integer, String, String, List<String>, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		String ip = RequestUtils.getIpAddr(request);
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		bean = manager.reply(user.getId(), site.getId(), topicId, postTypeId,
				title, content, ip, multipartRequest.getFiles("attachment"),
				code);
		logger.info("save BbsPost id=" + bean.getId());
		bbsUserGroupMng.findNearByPoint(user.getPoint(), user);
		String returnString = "redirect:" + bean.getRedirectUrl();
		if (logger.isDebugEnabled()) {
			logger
					.debug("save(BbsPost, Integer, Integer, String, String, List<String>, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/post/o_update.jspx")
	public String update(BbsPost bean, Integer postId, String title,
			String content, Integer pageNo, HttpServletRequest request,
			@RequestParam(value = "code", required = false)
			List<String> code, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("update(BbsPost, Integer, String, String, Integer, HttpServletRequest, List<String>, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site
					.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger
						.debug("update(BbsPost, Integer, String, String, Integer, HttpServletRequest, List<String>, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		String msg = checkEdit(request, manager.findById(postId).getTopic()
				.getForum(), manager.findById(postId), user, site);
		if (msg != null) {
			model.put("msg", msg);
			String returnString = FrontUtils.getTplPath(request, site
					.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
			if (logger.isDebugEnabled()) {
				logger
						.debug("update(BbsPost, Integer, String, String, Integer, HttpServletRequest, List<String>, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		String ip = RequestUtils.getIpAddr(request);

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		bean = manager.updatePost(postId, title, content, user, ip,
				multipartRequest.getFiles("attachment"), code);
		logger.info("update BbsPost id=" + bean.getId());
		return "redirect:" + bean.getRedirectUrl();
	}

	@RequestMapping("/post/v_quote{postId}.jspx")
	public String quote(@PathVariable
	Integer postId, Integer pid, HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("quote(Integer, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site
					.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger
						.debug("quote(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		BbsPost post = null;
		if (postId != null) {
			post = manager.findById(postId);
		} else if (pid != null) {
			post = manager.findById(pid);
		}
		model.put("topicId", post.getTopic().getId());
		if (post != null) {
			String msg = checkReply(request, post.getTopic().getForum(), user,
					site);
			if (msg != null) {
				model.put("msg", msg);
				String returnString = FrontUtils.getTplPath(request, site
						.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
				if (logger.isDebugEnabled()) {
					logger
							.debug("quote(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
				}
				return returnString;
			}
			model.put("post", post);
			model.put("topicId", post.getTopic().getId());
			model.put("postTypeId", post.getPostType().getId());
		}
		model.put("otype", 1);
		String returnString = FrontUtils.getTplPath(request, site
				.getSolutionPath(), TPLDIR_POST, TPL_POSTADD);
		if (logger.isDebugEnabled()) {
			logger
					.debug("quote(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/post/v_reply{postId}.jspx")
	public String reply(@PathVariable
	Integer postId, Integer pid, HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("reply(Integer, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site
					.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger
						.debug("reply(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		BbsPost post = null;
		if (postId != null) {
			post = manager.findById(postId);
		} else if (pid != null) {
			post = manager.findById(pid);
			model.put("postTypeId", post.getPostType().getId());
		}
		if (post != null) {
			String msg = checkReply(request, post.getTopic().getForum(), user,
					site);
			if (msg != null) {
				model.put("msg", msg);
				String returnString = FrontUtils.getTplPath(request, site
						.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
				if (logger.isDebugEnabled()) {
					logger
							.debug("reply(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
				}
				return returnString;
			}
			model.put("post", post);
			model.put("topicId", post.getTopic().getId());
			model.put("postTypeId", post.getPostType().getId());
		}
		model.put("otype", 2);

		String returnString = FrontUtils.getTplPath(request, site
				.getSolutionPath(), TPLDIR_POST, TPL_POSTADD);
		if (logger.isDebugEnabled()) {
			logger
					.debug("reply(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/post/v_grade{postId}.jspx")
	public String grade(@PathVariable
	Integer postId, Integer pid, HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("grade(Integer, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site
					.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger
						.debug("grade(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		BbsPost post = null;
		if (postId != null) {
			post = manager.findById(postId);
		} else if (pid != null) {
			post = manager.findById(pid);
		}
		if (post != null) {
			String msg = checkGrade(request, post.getTopic().getForum(), user,
					site);
			if (msg != null) {
				model.put("msg", msg);
				String returnString = FrontUtils.getTplPath(request, site
						.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
				if (logger.isDebugEnabled()) {
					logger
							.debug("grade(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
				}
				return returnString;
			}
			model.put("post", post);
		}
		String returnString = FrontUtils.getTplPath(request, site
				.getSolutionPath(), TPLDIR_POST, TPL_POST_GRADE);
		if (logger.isDebugEnabled()) {
			logger
					.debug("grade(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/post/o_grade.jspx")
	public String gradeSubmit(BbsGrade bean, Integer postId,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("gradeSubmit(BbsGrade, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site
					.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger
						.debug("gradeSubmit(BbsGrade, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		if (postId != null) {
			BbsPost post = manager.findById(postId);
			String msg = checkGrade(request, post.getTopic().getForum(), user,
					site);
			if (msg != null) {
				model.put("msg", msg);
				String returnString = FrontUtils.getTplPath(request, site
						.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
				if (logger.isDebugEnabled()) {
					logger
							.debug("gradeSubmit(BbsGrade, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
				}
				return returnString;
			}
			bbsGradeMng.saveGrade(bean, user, post);
			String returnString = "redirect:" + post.getRedirectUrl();
			if (logger.isDebugEnabled()) {
				logger
						.debug("gradeSubmit(BbsGrade, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		String returnString = FrontUtils.getTplPath(request, site
				.getSolutionPath(), TPLDIR_POST, TPL_POSTADD);
		if (logger.isDebugEnabled()) {
			logger
					.debug("gradeSubmit(BbsGrade, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/post/v_shield{postId}_{status}.jspx")
	public String shield(@PathVariable
	Integer postId, Integer pid, @PathVariable
	Short status, HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("shield(Integer, Integer, Short, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site
					.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger
						.debug("shield(Integer, Integer, Short, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		BbsPost post = null;
		if (postId != null) {
			String msg = checkShield(request, manager.findById(postId)
					.getTopic().getForum(), user, site);
			if (msg != null) {
				model.put("msg", msg);
				String returnString = FrontUtils.getTplPath(request, site
						.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
				if (logger.isDebugEnabled()) {
					logger
							.debug("shield(Integer, Integer, Short, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
				}
				return returnString;
			}
			post = manager.shield(postId, null, user, status);
		} else if (pid != null) {
			String msg = checkShield(request, manager.findById(pid).getTopic()
					.getForum(), user, site);
			if (msg != null) {
				model.put("msg", msg);
				String returnString = FrontUtils.getTplPath(request, site
						.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
				if (logger.isDebugEnabled()) {
					logger
							.debug("shield(Integer, Integer, Short, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
				}
				return returnString;
			}
			post = manager.shield(pid, null, user, status);
		}
		String returnString = "redirect:" + post.getRedirectUrl();
		if (logger.isDebugEnabled()) {
			logger
					.debug("shield(Integer, Integer, Short, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/post/o_shield.jspx")
	public String shieldSubmit(Integer postId, HttpServletRequest request,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("shieldSubmit(Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site
					.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger
						.debug("shieldSubmit(Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		BbsPost post = null;
		if (postId != null) {
			post = manager.findById(postId);
		}
		if (post != null) {
			String msg = checkShield(request, post.getTopic().getForum(), user,
					site);
			if (msg != null) {
				model.put("msg", msg);
				String returnString = FrontUtils.getTplPath(request, site
						.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
				if (logger.isDebugEnabled()) {
					logger
							.debug("shieldSubmit(Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
				}
				return returnString;
			}
			model.put("post", post);
			model.put("topicId", post.getTopic().getId());
			model.put("postTypeId", post.getPostType().getId());
		}
		String returnString = FrontUtils.getTplPath(request, site
				.getSolutionPath(), TPLDIR_POST, TPL_POSTADD);
		if (logger.isDebugEnabled()) {
			logger
					.debug("shieldSubmit(Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/member/o_prohibit.jspx")
	public String prohibit(Integer postId, Integer userId,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("prohibit(Integer, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site
					.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger
						.debug("prohibit(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		if (!user.getModerator()) {
			String msg = MessageResolver.getMessage(request,
					"login.groupAccessForbidden", user.getGroup().getName());
			model.put("msg", msg);
			String returnString = FrontUtils.getTplPath(request, site
					.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_VIEW);
			if (logger.isDebugEnabled()) {
				logger
						.debug("prohibit(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		BbsUser bbsuser = bbsUserMng.findById(userId);
		BbsPost post = manager.findById(postId);
		bbsuser.setProhibitPost(BbsUser.PROHIBIT_FOREVER);
		String returnString = "redirect:" + post.getRedirectUrl();
		if (logger.isDebugEnabled()) {
			logger
					.debug("prohibit(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/post/o_delete.jspx")
	public String delete(Integer[] ids, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("delete(Integer[], Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		BbsPost[] beans = manager.deleteByIds(ids);
		for (BbsPost bean : beans) {
			logger.info("delete BbsPost id=" + bean.getId());
		}

		if (logger.isDebugEnabled()) {
			logger
					.debug("delete(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return null;
	}

	// public String checkIp(String ip){
	//		
	// return "";
	// }

	private String checkReply(HttpServletRequest request, BbsForum forum,
			BbsUser user, CmsSite site) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("checkReply(HttpServletRequest, BbsForum, BbsUser, CmsSite) - start"); //$NON-NLS-1$
		}

		String msg = "";
		if (forum.getGroupReplies() == null) {
			msg = MessageResolver.getMessage(request,
					"login.groupAccessForbidden", user.getGroup().getName());

			if (logger.isDebugEnabled()) {
				logger
						.debug("checkReply(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
			}
			return msg;
		} else {
			BbsUserGroup group = null;
			if (user == null) {
				group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
			} else {
				if (user.getProhibit()) {
					msg = MessageResolver.getMessage(request, "member.gag");

					if (logger.isDebugEnabled()) {
						logger
								.debug("checkReply(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
					}
					return msg;
				}
				group = user.getGroup();
			}
			if (group == null) {
				msg = MessageResolver.getMessage(request,
						"member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger
							.debug("checkReply(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
			if (!group.allowReply()) {
				msg = MessageResolver.getMessage(request,
						"member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger
							.debug("checkReply(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
			if (forum.getGroupReplies().indexOf("," + group.getId() + ",") == -1) {
				msg = MessageResolver.getMessage(request,
						"member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger
							.debug("checkReply(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
			if (!group.checkPostToday(user.getPostToday())) {
				msg = MessageResolver.getMessage(request, "member.posttomany");

				if (logger.isDebugEnabled()) {
					logger
							.debug("checkReply(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
		}

		if (logger.isDebugEnabled()) {
			logger
					.debug("checkReply(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
		}
		return null;
	}

	private String checkEdit(HttpServletRequest request, BbsForum forum,
			BbsPost post, BbsUser user, CmsSite site) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("checkEdit(HttpServletRequest, BbsForum, BbsPost, BbsUser, CmsSite) - start"); //$NON-NLS-1$
		}

		String msg = "";
		if (forum.getGroupReplies() == null) {
			msg = MessageResolver.getMessage(request, "member.hasnopermission");

			if (logger.isDebugEnabled()) {
				logger
						.debug("checkEdit(HttpServletRequest, BbsForum, BbsPost, BbsUser, CmsSite) - end"); //$NON-NLS-1$
			}
			return msg;
		} else {
			if (user == null) {
				msg = MessageResolver.getMessage(request,
						"member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger
							.debug("checkEdit(HttpServletRequest, BbsForum, BbsPost, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
			BbsUserGroup group = user.getGroup();
			// if (!post.getCreater().equals(user)) {
			// return "不能编辑别人的帖子";
			// }
			if (forum.getGroupReplies().indexOf("," + group.getId() + ",") == -1) {
				msg = MessageResolver.getMessage(request,
						"member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger
							.debug("checkEdit(HttpServletRequest, BbsForum, BbsPost, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
		}

		if (logger.isDebugEnabled()) {
			logger
					.debug("checkEdit(HttpServletRequest, BbsForum, BbsPost, BbsUser, CmsSite) - end"); //$NON-NLS-1$
		}
		return null;
	}

	private String checkGrade(HttpServletRequest request, BbsForum forum,
			BbsUser user, CmsSite site) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("checkGrade(HttpServletRequest, BbsForum, BbsUser, CmsSite) - start"); //$NON-NLS-1$
		}

		String msg = "";
		if (forum.getGroupReplies() == null) {
			msg = MessageResolver.getMessage(request, "member.hasnopermission");

			if (logger.isDebugEnabled()) {
				logger
						.debug("checkGrade(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
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
				msg = MessageResolver.getMessage(request,
						"member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger
							.debug("checkGrade(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
			if (forum.getGroupReplies().indexOf("," + group.getId() + ",") == -1) {
				msg = MessageResolver.getMessage(request,
						"member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger
							.debug("checkGrade(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
			if (user.getGradeToday() != null
					&& user.getGradeToday() >= group.getGradeNum()) {
				msg = MessageResolver.getMessage(request, "member.doesnomark");

				if (logger.isDebugEnabled()) {
					logger
							.debug("checkGrade(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
		}

		if (logger.isDebugEnabled()) {
			logger
					.debug("checkGrade(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
		}
		return null;
	}

	private String checkShield(HttpServletRequest request, BbsForum forum,
			BbsUser user, CmsSite site) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("checkShield(HttpServletRequest, BbsForum, BbsUser, CmsSite) - start"); //$NON-NLS-1$
		}

		String msg;
		if (forum.getGroupTopics() == null) {
			msg = MessageResolver.getMessage(request, "member.hasnopermission");

			if (logger.isDebugEnabled()) {
				logger
						.debug("checkShield(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
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
				msg = MessageResolver.getMessage(request,
						"member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger
							.debug("checkShield(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
			if (!group.allowTopic()) {
				msg = MessageResolver.getMessage(request,
						"member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger
							.debug("checkShield(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
			if (forum.getGroupTopics().indexOf("," + group.getId() + ",") == -1) {
				msg = MessageResolver.getMessage(request,
						"member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger
							.debug("checkShield(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
			if (!group.hasRight(forum, user)) {
				msg = MessageResolver.getMessage(request,
						"member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger
							.debug("checkShield(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
			if (!group.topicShield()) {
				msg = MessageResolver.getMessage(request,
						"member.hasnopermission");

				if (logger.isDebugEnabled()) {
					logger
							.debug("checkShield(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
				}
				return msg;
			}
		}

		if (logger.isDebugEnabled()) {
			logger
					.debug("checkShield(HttpServletRequest, BbsForum, BbsUser, CmsSite) - end"); //$NON-NLS-1$
		}
		return null;
	}

	@Autowired
	private BbsPostMng manager;
	@Autowired
	private BbsTopicMng bbsTopicMng;
	@Autowired
	private BbsGradeMng bbsGradeMng;
	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private BbsConfigMng bbsConfigMng;
	@Autowired
	private TopicCountEhCache topicCountEhCache;
	@Autowired
	private BbsUserGroupMng bbsUserGroupMng;
}
