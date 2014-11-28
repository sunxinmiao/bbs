package com.jeecms.bbs.action.member;

import org.apache.log4j.Logger;

import static com.jeecms.bbs.Constants.TPLDIR_MEMBER;
import static com.jeecms.bbs.Constants.TPLDIR_TOPIC;
import static com.jeecms.bbs.entity.BbsFriendShip.REFUSE;
import static com.jeecms.bbs.entity.BbsFriendShip.ACCEPT;
import static com.jeecms.bbs.entity.BbsFriendShip.APPLYING;

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

import com.jeecms.bbs.entity.BbsFriendShip;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsFriendShipMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.MagicMessage;


@Controller
public class FriendAct {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FriendAct.class);

	public static final String FRIEND_SEARCH = "tpl.friendSearch";
	public static final String FRIEND_SEARCH_RESULT = "tpl.friendSearchResult";
	public static final String SUGGEST = "tpl.suggest";
	public static final String FRIEND_APPLY_RESULT = "tpl.friendApplyResult";
	public static final String MYFRIEND = "tpl.myfriend";
	public static final String FRIEND_APPLY = "tpl.friendApply";
	public static final String TPL_NO_LOGIN = "tpl.nologin";
	public static final String TPL_GET_MSG_SEND = "tpl.msgsendpage";

	@RequestMapping(value = "/member/friendSearch*.jhtml", method = RequestMethod.GET)
	public String search(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("search(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("search(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		model.addAttribute("kw", RequestUtils.getQueryParam(request, "kw"));
		model.addAttribute("user", user);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, FRIEND_SEARCH);
		if (logger.isDebugEnabled()) {
			logger.debug("search(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/member/suggest.jhtml")
	public String suggest(HttpServletRequest request,
			HttpServletResponse response, String username, Integer count,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("suggest(HttpServletRequest, HttpServletResponse, String, Integer, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		List<BbsUser> list = bbsUserMng.getSuggestMember(username, count);
		model.addAttribute("suggests", list);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, SUGGEST);
		if (logger.isDebugEnabled()) {
			logger.debug("suggest(HttpServletRequest, HttpServletResponse, String, Integer, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping(value = "/member/apply.jhtml")
	public String apply(HttpServletRequest request,
			HttpServletResponse response, String u, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("apply(HttpServletRequest, HttpServletResponse, String, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		BbsUser friend = bbsUserMng.findByUsername(u);
		if (validateApply(user, friend)) {
			bbsFriendShipMng.apply(user, friend);
			model.addAttribute("message", "friend.applyed");
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, FRIEND_APPLY_RESULT);
			if (logger.isDebugEnabled()) {
				logger.debug("apply(HttpServletRequest, HttpServletResponse, String, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("apply(HttpServletRequest, HttpServletResponse, String, ModelMap) - end"); //$NON-NLS-1$
		}
		return null;
	}

	@RequestMapping(value = "/member/applyJson.jhtml")
	public void applyJson(HttpServletRequest request,
			HttpServletResponse response, String u, ModelMap model)
			throws JSONException {
		if (logger.isDebugEnabled()) {
			logger.debug("applyJson(HttpServletRequest, HttpServletResponse, String, ModelMap) - start"); //$NON-NLS-1$
		}

		BbsUser user = CmsUtils.getUser(request);
		BbsUser friend = bbsUserMng.findByUsername(u);
		JSONObject object = new JSONObject();
		MagicMessage magicMessage = MagicMessage.create(request);
		if (user == null) {
			object.put("message", magicMessage
					.getMessage("friend.apply.nologin"));
		} else if (validateApply(user, friend)) {
			bbsFriendShipMng.apply(user, friend);
			object.put("message", magicMessage.getMessage("friend.applyed"));
		} else {
			int status = validateApplyJson(user, friend);
			if (status == 0) {
				object.put("message", magicMessage
						.getMessage("friend.apply.nologin"));
			} else if (status == 1) {
				object.put("message", magicMessage
						.getMessage("friend.apply.noexistuser"));
			} else if (status == 2) {
				object.put("message", magicMessage
						.getMessage("friend.apply.yourself"));
			} else if (status == 3) {
				object.put("message", magicMessage
						.getMessage("friend.apply.success"));
			} else if (status == 4) {
				object.put("message", magicMessage
						.getMessage("friend.apply.applying"));
			}
		}
		ResponseUtils.renderJson(response, object.toString());

		if (logger.isDebugEnabled()) {
			logger.debug("applyJson(HttpServletRequest, HttpServletResponse, String, ModelMap) - end"); //$NON-NLS-1$
		}
	}

	@RequestMapping("/member/getsendmsgpage.jhtml")
	public String getmagicpage(String username, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("getmagicpage(String, HttpServletRequest, HttpServletResponse, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		model.addAttribute("username",username);
		FrontUtils.frontData(request, model, site);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, TPL_GET_MSG_SEND);
		if (logger.isDebugEnabled()) {
			logger.debug("getmagicpage(String, HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping(value = "/member/accept.jhtml")
	public String accept(HttpServletRequest request,
			HttpServletResponse response, Integer id, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("accept(HttpServletRequest, HttpServletResponse, Integer, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		BbsFriendShip friendShip = bbsFriendShipMng.findById(id);
		if (validateAccept(user, friendShip)) {
			bbsFriendShipMng.accept(friendShip);
			model.addAttribute("message", "friend.accepted");
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, FRIEND_APPLY_RESULT);
			if (logger.isDebugEnabled()) {
				logger.debug("accept(HttpServletRequest, HttpServletResponse, Integer, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("accept(HttpServletRequest, HttpServletResponse, Integer, ModelMap) - end"); //$NON-NLS-1$
		}
		return null;
	}

	@RequestMapping(value = "/member/refuse.jhtml")
	public String refuse(HttpServletRequest request,
			HttpServletResponse response, Integer id, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("refuse(HttpServletRequest, HttpServletResponse, Integer, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		BbsFriendShip friendShip = bbsFriendShipMng.findById(id);
		if (validateRefuse(user, friendShip)) {
			bbsFriendShipMng.refuse(friendShip);
			model.addAttribute("message", "friend.refused");
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, FRIEND_APPLY_RESULT);
			if (logger.isDebugEnabled()) {
				logger.debug("refuse(HttpServletRequest, HttpServletResponse, Integer, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("refuse(HttpServletRequest, HttpServletResponse, Integer, ModelMap) - end"); //$NON-NLS-1$
		}
		return null;
	}

	@RequestMapping("/member/myfriend*.jhtml")
	public String myfriend(Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("myfriend(Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("myfriend(Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		model.put("user", user);
		FrontUtils.frontPageData(request, model);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, MYFRIEND);
		if (logger.isDebugEnabled()) {
			logger.debug("myfriend(Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/member/friendApply.jhtml")
	public String friendApply(Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("friendApply(Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("friendApply(Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		model.put("user", user);
		FrontUtils.frontPageData(request, model);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, FRIEND_APPLY);
		if (logger.isDebugEnabled()) {
			logger.debug("friendApply(Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	private boolean validateApply(BbsUser user, BbsUser friend) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateApply(BbsUser, BbsUser) - start"); //$NON-NLS-1$
		}

		// 用户未登录
		if (user == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateApply(BbsUser, BbsUser) - end"); //$NON-NLS-1$
			}
			return false;
		}
		// 好友不存在
		if (friend == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateApply(BbsUser, BbsUser) - end"); //$NON-NLS-1$
			}
			return false;
		}
		// 不允许加自己为好友
		if (user.equals(friend)) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateApply(BbsUser, BbsUser) - end"); //$NON-NLS-1$
			}
			return false;
		}
		// 申请被拒绝则可以重新申请好友
		BbsFriendShip bean = bbsFriendShipMng.getFriendShip(user.getId(),
				friend.getId());
		if (bean != null && bean.getStatus() != REFUSE) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateApply(BbsUser, BbsUser) - end"); //$NON-NLS-1$
			}
			return false;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validateApply(BbsUser, BbsUser) - end"); //$NON-NLS-1$
		}
		return true;
	}

	private int validateApplyJson(BbsUser user, BbsUser friend) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateApplyJson(BbsUser, BbsUser) - start"); //$NON-NLS-1$
		}

		int returnValue = -1;
		// 用户未登录
		if (user == null) {
			returnValue = 0;
		}
		// 好友不存在
		if (friend == null) {
			returnValue = 1;
		}
		// 不允许加自己为好友
		if (user.equals(friend)) {
			returnValue = 2;
		}
		BbsFriendShip bean = bbsFriendShipMng.getFriendShip(user.getId(),
				friend.getId());
		if (bean != null && bean.getStatus() == ACCEPT) {
			returnValue = 3;
		}
		if (bean != null && bean.getStatus() == APPLYING) {
			returnValue = 4;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validateApplyJson(BbsUser, BbsUser) - end"); //$NON-NLS-1$
		}
		return returnValue;
	}

	private boolean validateAccept(BbsUser user, BbsFriendShip friendShip) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateAccept(BbsUser, BbsFriendShip) - start"); //$NON-NLS-1$
		}

		// 用户未登录
		if (user == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateAccept(BbsUser, BbsFriendShip) - end"); //$NON-NLS-1$
			}
			return false;
		}
		// 好友申请不存在
		if (friendShip == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateAccept(BbsUser, BbsFriendShip) - end"); //$NON-NLS-1$
			}
			return false;
		}
		// 只处理申请中的好友关系
		if (friendShip.getStatus() != APPLYING) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateAccept(BbsUser, BbsFriendShip) - end"); //$NON-NLS-1$
			}
			return false;
		}
		// 只处理自己的好友关系
		if (!user.equals(friendShip.getFriend())) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateAccept(BbsUser, BbsFriendShip) - end"); //$NON-NLS-1$
			}
			return false;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validateAccept(BbsUser, BbsFriendShip) - end"); //$NON-NLS-1$
		}
		return true;
	}

	private boolean validateRefuse(BbsUser user, BbsFriendShip friendShip) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateRefuse(BbsUser, BbsFriendShip) - start"); //$NON-NLS-1$
		}

		// 用户未登录
		if (user == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateRefuse(BbsUser, BbsFriendShip) - end"); //$NON-NLS-1$
			}
			return false;
		}
		// 好友申请不存在
		if (friendShip == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateRefuse(BbsUser, BbsFriendShip) - end"); //$NON-NLS-1$
			}
			return false;
		}
		// 只处理申请中的好友关系
		if (friendShip.getStatus() != APPLYING) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateRefuse(BbsUser, BbsFriendShip) - end"); //$NON-NLS-1$
			}
			return false;
		}
		// 只处理自己的好友关系
		if (!user.equals(friendShip.getFriend())) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateRefuse(BbsUser, BbsFriendShip) - end"); //$NON-NLS-1$
			}
			return false;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validateRefuse(BbsUser, BbsFriendShip) - end"); //$NON-NLS-1$
		}
		return true;
	}

	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private BbsFriendShipMng bbsFriendShipMng;
}
