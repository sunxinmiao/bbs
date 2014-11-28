package com.jeecms.bbs.action.front;

import org.apache.log4j.Logger;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeecms.bbs.manager.CmsFriendlinkMng;
import com.jeecms.common.web.ResponseUtils;

/**
 * 友情链接点击次数Action
 * 
 * @author liufang
 * 
 */

@Controller
public class FriendlinkAct {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FriendlinkAct.class);

	// private static final Logger log = LoggerFactory
	// .getLogger(FriendlinkAct.class);

	@RequestMapping(value = "/friendlink_view.jspx", method = RequestMethod.GET)
	public void view(Integer id, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("view(Integer, HttpServletRequest, HttpServletResponse, ModelMap) - start"); //$NON-NLS-1$
		}

		if (id != null) {
			cmsFriendlinkMng.updateViews(id);
			ResponseUtils.renderJson(response, "true");
		} else {
			ResponseUtils.renderJson(response, "false");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("view(Integer, HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
		}
	}
	
	@Autowired
	private CmsFriendlinkMng cmsFriendlinkMng;
}
