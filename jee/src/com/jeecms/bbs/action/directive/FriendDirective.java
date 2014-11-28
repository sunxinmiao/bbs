package com.jeecms.bbs.action.directive;

import org.apache.log4j.Logger;

import static com.jeecms.common.web.freemarker.DirectiveUtils.OUT_BEAN;
import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeecms.bbs.entity.BbsFriendShip;
import com.jeecms.bbs.manager.BbsFriendShipMng;
import com.jeecms.common.web.freemarker.DirectiveUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;


public class FriendDirective implements TemplateDirectiveModel {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FriendDirective.class);

	public static final String PARAM_USERID = "userId";
	public static final String PARAM_FRIENDID = "friendId";

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("execute(Environment, Map, TemplateModel[], TemplateDirectiveBody) - start"); //$NON-NLS-1$
		}

		Integer userId = getUserId(params);
		Integer friendId = getFriendId(params);
		BbsFriendShip bean = bbsFriendShipMng.getFriendShip(userId, friendId);
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(
				params);
		paramWrap.put(OUT_BEAN, DEFAULT_WRAPPER.wrap(bean));
		Map<String, TemplateModel> origMap = DirectiveUtils
				.addParamsToVariable(env, paramWrap);
		body.render(env.getOut());
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);

		if (logger.isDebugEnabled()) {
			logger.debug("execute(Environment, Map, TemplateModel[], TemplateDirectiveBody) - end"); //$NON-NLS-1$
		}
	}

	private Integer getUserId(Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getUserId(Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		Integer userId = DirectiveUtils.getInt(PARAM_USERID, params);
		Integer returnInteger = userId == null ? 0 : userId;
		if (logger.isDebugEnabled()) {
			logger.debug("getUserId(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
		}
		return returnInteger;
	}

	private Integer getFriendId(Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getFriendId(Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		Integer friendId = DirectiveUtils.getInt(PARAM_FRIENDID, params);
		Integer returnInteger = friendId == null ? 0 : friendId;
		if (logger.isDebugEnabled()) {
			logger.debug("getFriendId(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
		}
		return returnInteger;
	}

	@Autowired
	private BbsFriendShipMng bbsFriendShipMng;
}
