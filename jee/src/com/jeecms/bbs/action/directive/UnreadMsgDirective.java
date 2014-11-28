package com.jeecms.bbs.action.directive;

import org.apache.log4j.Logger;

import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsFriendShipMng;
import com.jeecms.bbs.manager.BbsMessageMng;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.web.freemarker.DirectiveUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class UnreadMsgDirective implements TemplateDirectiveModel {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(UnreadMsgDirective.class);

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("execute(Environment, Map, TemplateModel[], TemplateDirectiveBody) - start"); //$NON-NLS-1$
		}

		BbsUser user = FrontUtils.getUser(env);
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(
				params);
		paramWrap.put("applyFriend", DEFAULT_WRAPPER
				.wrap(hasUnProcessApplyFriend(user)));
		paramWrap.put("unReadMsg", DEFAULT_WRAPPER.wrap(hasUnReadMsg(user)));
		paramWrap.put("unReadGuestbook", DEFAULT_WRAPPER
				.wrap(hasUnReadGuestbook(user)));
		paramWrap
				.put("unReadGreet", DEFAULT_WRAPPER.wrap(hasUnReadGreet(user)));
		Map<String, TemplateModel> origMap = DirectiveUtils
				.addParamsToVariable(env, paramWrap);
		body.render(env.getOut());
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);

		if (logger.isDebugEnabled()) {
			logger.debug("execute(Environment, Map, TemplateModel[], TemplateDirectiveBody) - end"); //$NON-NLS-1$
		}
	}

	private boolean hasUnProcessApplyFriend(BbsUser user) {
		if (logger.isDebugEnabled()) {
			logger.debug("hasUnProcessApplyFriend(BbsUser) - start"); //$NON-NLS-1$
		}

		if (friendShipMng.getApplyByUserId(user.getId(), 1, 5).getTotalCount() > 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("hasUnProcessApplyFriend(BbsUser) - end"); //$NON-NLS-1$
			}
			return true;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("hasUnProcessApplyFriend(BbsUser) - end"); //$NON-NLS-1$
		}
		return false;
	}

	private boolean hasUnReadMsg(BbsUser user) {
		if (logger.isDebugEnabled()) {
			logger.debug("hasUnReadMsg(BbsUser) - start"); //$NON-NLS-1$
		}

		if (messageMng.hasUnReadMessage(user.getId(), 1)) {
			if (logger.isDebugEnabled()) {
				logger.debug("hasUnReadMsg(BbsUser) - end"); //$NON-NLS-1$
			}
			return true;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("hasUnReadMsg(BbsUser) - end"); //$NON-NLS-1$
		}
		return false;
	}

	private boolean hasUnReadGuestbook(BbsUser user) {
		if (logger.isDebugEnabled()) {
			logger.debug("hasUnReadGuestbook(BbsUser) - start"); //$NON-NLS-1$
		}

		if (messageMng.hasUnReadMessage(user.getId(), 2)) {
			if (logger.isDebugEnabled()) {
				logger.debug("hasUnReadGuestbook(BbsUser) - end"); //$NON-NLS-1$
			}
			return true;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("hasUnReadGuestbook(BbsUser) - end"); //$NON-NLS-1$
		}
		return false;
	}

	private boolean hasUnReadGreet(BbsUser user) {
		if (logger.isDebugEnabled()) {
			logger.debug("hasUnReadGreet(BbsUser) - start"); //$NON-NLS-1$
		}

		if (messageMng.hasUnReadMessage(user.getId(), 3)) {
			if (logger.isDebugEnabled()) {
				logger.debug("hasUnReadGreet(BbsUser) - end"); //$NON-NLS-1$
			}
			return true;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("hasUnReadGreet(BbsUser) - end"); //$NON-NLS-1$
		}
		return false;
	}

	@Autowired
	private BbsFriendShipMng friendShipMng;
	@Autowired
	private BbsMessageMng messageMng;
}
