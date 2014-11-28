package com.jeecms.bbs.action.member;

import org.apache.log4j.Logger;

import static com.jeecms.bbs.Constants.TPLDIR_MEMBER;
import static com.jeecms.bbs.Constants.TPLDIR_TOPIC;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeecms.bbs.entity.BbsMessage;
import com.jeecms.bbs.entity.BbsMessageReply;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsMessageMng;
import com.jeecms.bbs.manager.BbsMessageReplyMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.MagicMessage;

@Controller
public class MessageAct {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MessageAct.class);

	public static final String SEND_MSG = "tpl.sendMsg";
	public static final String MY_MSG = "tpl.myMsg";
	public static final String REPLY = "tpl.reply";
	public static final String TPL_NO_LOGIN = "tpl.nologin";
	public static final String MY_REMIND = "tpl.myremind";

	@RequestMapping(value = "/member/sendMsg.jhtml", method = RequestMethod.GET)
	public String message(String username, Integer type,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("message(String, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("message(String, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		model.addAttribute("username", username);
		model.addAttribute("type", type);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, SEND_MSG);
		if (logger.isDebugEnabled()) {
			logger.debug("message(String, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping(value = "/member/sendMsg.jhtml", method = RequestMethod.POST)
	public String messageSubmit(HttpServletRequest request,HttpServletResponse response, String u,
			BbsMessage msg, ModelMap model) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("messageSubmit(HttpServletRequest, HttpServletResponse, String, BbsMessage, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("messageSubmit(HttpServletRequest, HttpServletResponse, String, BbsMessage, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		if (validateSendMsg(request,user, u, model)) {
			bbsMessageMng.sendMsg(user, bbsUserMng.findByUsername(u), msg);
		}
		response.sendRedirect("myMsg.jhtml");

		if (logger.isDebugEnabled()) {
			logger.debug("messageSubmit(HttpServletRequest, HttpServletResponse, String, BbsMessage, ModelMap) - end"); //$NON-NLS-1$
		}
		return null;
	}
	
	@RequestMapping(value = "/member/sendMsgJson.jhtml", method = RequestMethod.POST)
	public void messageJsonSubmit(HttpServletRequest request,HttpServletResponse response, String username,
			String content,Integer msgType, ModelMap model) throws JSONException {
		if (logger.isDebugEnabled()) {
			logger.debug("messageJsonSubmit(HttpServletRequest, HttpServletResponse, String, String, Integer, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		JSONObject object = new JSONObject();
		BbsUser receiver=bbsUserMng.findByUsername(username);
		MagicMessage messageResource = MagicMessage.create(request);
		if (user == null) {
			object.put("message", messageResource
					.getMessage("friend.apply.nologin"));
		}
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		if (validateSendMsg(request,user, username, model)) {
			BbsMessage msg=new BbsMessage();
			msg.setContent(content);
			msg.setCreateTime(new Date());
			msg.setReceiver(receiver);
			msg.setSender(user);
			msg.setUser(user);
			msg.setMsgType(msgType);
			msg.init();
			bbsMessageMng.sendMsg(user, receiver, msg);
			object.put("message", messageResource
					.getMessage("greet.success"));
		}else{
			object.put("message", messageResource
					.getMessage("greet.fail"));
		}
		ResponseUtils.renderJson(response, object.toString());

		if (logger.isDebugEnabled()) {
			logger.debug("messageJsonSubmit(HttpServletRequest, HttpServletResponse, String, String, Integer, ModelMap) - end"); //$NON-NLS-1$
		}
	}

	//我的信息
	@RequestMapping(value = "/member/myMsg*.jhtml")
	public String myMsg(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("myMsg(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("myMsg(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		model.put("user", user);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, MY_MSG);
		if (logger.isDebugEnabled()) {
			logger.debug("myMsg(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	//我的留言
	@RequestMapping(value = "/member/myguestbook*.jhtml")
	public String myguestbook(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("myguestbook(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("myguestbook(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		model.put("user", user);
		model.put("typeId", 2);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, MY_MSG);
		if (logger.isDebugEnabled()) {
			logger.debug("myguestbook(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}
	//我的提醒
	@RequestMapping(value = "/member/myremind*.jhtml")
	public String myremind(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("myremind(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("myremind(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		model.put("user", user);
		model.put("typeId", 3);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, MY_REMIND);
		if (logger.isDebugEnabled()) {
			logger.debug("myremind(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping(value = "/member/reply*.jhtml")
	public String reply(HttpServletRequest request, Integer mid, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("reply(HttpServletRequest, Integer, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_TOPIC, TPL_NO_LOGIN);
			if (logger.isDebugEnabled()) {
				logger.debug("reply(HttpServletRequest, Integer, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		model.put("user", user);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		if (validateReply(user, mid, model)) {
			model.put("msg", bbsMessageMng.findById(mid));
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, REPLY);
			if (logger.isDebugEnabled()) {
				logger.debug("reply(HttpServletRequest, Integer, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("reply(HttpServletRequest, Integer, ModelMap) - end"); //$NON-NLS-1$
		}
		return "redirect:/member/myMsg.jhtml";
	}

	@RequestMapping(value = "/member/ajax_delete_msg.jhtml")
	public void deleteMessage(HttpServletRequest request,
			HttpServletResponse response, Integer mid, ModelMap model)
			throws JSONException {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteMessage(HttpServletRequest, HttpServletResponse, Integer, ModelMap) - start"); //$NON-NLS-1$
		}

		JSONObject json = new JSONObject();
		BbsUser user = CmsUtils.getUser(request);
		if (validateDeleteMessage(user, mid)) {
			bbsMessageMng.deleteById(mid);
			json.put("success", true);
		} else {
			json.put("success", false);
		}
		ResponseUtils.renderJson(response, json.toString());

		if (logger.isDebugEnabled()) {
			logger.debug("deleteMessage(HttpServletRequest, HttpServletResponse, Integer, ModelMap) - end"); //$NON-NLS-1$
		}
	}

	@RequestMapping(value = "/member/ajax_delete_reply.jhtml")
	public void deleteReply(HttpServletRequest request,
			HttpServletResponse response, Integer rid, ModelMap model)
			throws JSONException {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteReply(HttpServletRequest, HttpServletResponse, Integer, ModelMap) - start"); //$NON-NLS-1$
		}

		JSONObject json = new JSONObject();
		BbsUser user = CmsUtils.getUser(request);
		if (validateDeleteReply(user, rid)) {
			bbsMessageReplyMng.deleteById(rid);
			json.put("success", true);
		} else {
			json.put("success", false);
		}
		ResponseUtils.renderJson(response, json.toString());

		if (logger.isDebugEnabled()) {
			logger.debug("deleteReply(HttpServletRequest, HttpServletResponse, Integer, ModelMap) - end"); //$NON-NLS-1$
		}
	}

	private boolean validateDeleteMessage(BbsUser user, Integer rid) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateDeleteMessage(BbsUser, Integer) - start"); //$NON-NLS-1$
		}

		if (user == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateDeleteMessage(BbsUser, Integer) - end"); //$NON-NLS-1$
			}
			return false;
		}
		BbsMessage bean = bbsMessageMng.findById(rid);
		if (bean == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateDeleteMessage(BbsUser, Integer) - end"); //$NON-NLS-1$
			}
			return false;
		}
		if (!bean.getUser().equals(user)) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateDeleteMessage(BbsUser, Integer) - end"); //$NON-NLS-1$
			}
			return false;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validateDeleteMessage(BbsUser, Integer) - end"); //$NON-NLS-1$
		}
		return true;
	}

	private boolean validateDeleteReply(BbsUser user, Integer rid) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateDeleteReply(BbsUser, Integer) - start"); //$NON-NLS-1$
		}

		if (user == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateDeleteReply(BbsUser, Integer) - end"); //$NON-NLS-1$
			}
			return false;
		}
		BbsMessageReply bean = bbsMessageReplyMng.findById(rid);
		if (bean == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateDeleteReply(BbsUser, Integer) - end"); //$NON-NLS-1$
			}
			return false;
		}
		if (!bean.getMessage().getUser().equals(user)) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateDeleteReply(BbsUser, Integer) - end"); //$NON-NLS-1$
			}
			return false;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validateDeleteReply(BbsUser, Integer) - end"); //$NON-NLS-1$
		}
		return true;
	}

	private boolean validateReply(BbsUser user, Integer mid, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateReply(BbsUser, Integer, ModelMap) - start"); //$NON-NLS-1$
		}

		BbsMessage bean = bbsMessageMng.findById(mid);
		if (bean == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateReply(BbsUser, Integer, ModelMap) - end"); //$NON-NLS-1$
			}
			return false;
		}
		if (!bean.getUser().equals(user)) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateReply(BbsUser, Integer, ModelMap) - end"); //$NON-NLS-1$
			}
			return false;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validateReply(BbsUser, Integer, ModelMap) - end"); //$NON-NLS-1$
		}
		return true;
	}

	private boolean validateSendMsg(HttpServletRequest request,BbsUser user, String receiver,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateSendMsg(HttpServletRequest, BbsUser, String, ModelMap) - start"); //$NON-NLS-1$
		}

		// 用户名为空
		if (StringUtils.isBlank(receiver)) {
			model.addAttribute("message", MessageResolver.getMessage(request, "friend.send.noname"));

			if (logger.isDebugEnabled()) {
				logger.debug("validateSendMsg(HttpServletRequest, BbsUser, String, ModelMap) - end"); //$NON-NLS-1$
			}
			return false;
		}
		// 不允许发送消息给自己
		if (receiver.equals(user.getUsername())) {
			model.addAttribute("message", MessageResolver.getMessage(request, "friend.sendmyself"));

			if (logger.isDebugEnabled()) {
				logger.debug("validateSendMsg(HttpServletRequest, BbsUser, String, ModelMap) - end"); //$NON-NLS-1$
			}
			return false;
		}
		// 用户名不存在
		if (bbsUserMng.findByUsername(receiver) == null) {
			model.addAttribute("message", MessageResolver.getMessage(request, "friend.send.noexistname"));

			if (logger.isDebugEnabled()) {
				logger.debug("validateSendMsg(HttpServletRequest, BbsUser, String, ModelMap) - end"); //$NON-NLS-1$
			}
			return false;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validateSendMsg(HttpServletRequest, BbsUser, String, ModelMap) - end"); //$NON-NLS-1$
		}
		return true;
	}

	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private BbsMessageMng bbsMessageMng;
	@Autowired
	private BbsMessageReplyMng bbsMessageReplyMng;
}
