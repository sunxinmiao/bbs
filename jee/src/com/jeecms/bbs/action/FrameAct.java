package com.jeecms.bbs.action;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrameAct {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FrameAct.class);

	@RequestMapping("/frame/config_main.do")
	public String configMain(ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("configMain(ModelMap) - start"); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			logger.debug("configMain(ModelMap) - end"); //$NON-NLS-1$
		}
		return "frame/config_main";
	}

	@RequestMapping("/frame/config_left.do")
	public String configLeft(ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("configLeft(ModelMap) - start"); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			logger.debug("configLeft(ModelMap) - end"); //$NON-NLS-1$
		}
		return "frame/config_left";
	}

	@RequestMapping("/frame/config_right.do")
	public String configRight(ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("configRight(ModelMap) - start"); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			logger.debug("configRight(ModelMap) - end"); //$NON-NLS-1$
		}
		return "frame/config_right";
	}

	@RequestMapping("/frame/user_main.do")
	public String userMain(ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("userMain(ModelMap) - start"); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			logger.debug("userMain(ModelMap) - end"); //$NON-NLS-1$
		}
		return "frame/user_main";
	}

	@RequestMapping("/frame/user_left.do")
	public String userLeft(ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("userLeft(ModelMap) - start"); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			logger.debug("userLeft(ModelMap) - end"); //$NON-NLS-1$
		}
		return "frame/user_left";
	}

	@RequestMapping("/frame/user_right.do")
	public String userRight(ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("userRight(ModelMap) - start"); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			logger.debug("userRight(ModelMap) - end"); //$NON-NLS-1$
		}
		return "frame/user_right";
	}

	@RequestMapping("/frame/category_main.do")
	public String generateMain(ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("generateMain(ModelMap) - start"); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			logger.debug("generateMain(ModelMap) - end"); //$NON-NLS-1$
		}
		return "frame/category_main";
	}

	@RequestMapping("/frame/category_left.do")
	public String generateLeft(ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("generateLeft(ModelMap) - start"); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			logger.debug("generateLeft(ModelMap) - end"); //$NON-NLS-1$
		}
		return "frame/category_left";
	}

	@RequestMapping("/frame/category_right.do")
	public String generateRight(ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("generateRight(ModelMap) - start"); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			logger.debug("generateRight(ModelMap) - end"); //$NON-NLS-1$
		}
		return "frame/category_right";
	}

	@RequestMapping("/frame/forum_main.do")
	public String forumMain(ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("forumMain(ModelMap) - start"); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			logger.debug("forumMain(ModelMap) - end"); //$NON-NLS-1$
		}
		return "frame/forum_main";
	}

	@RequestMapping("/frame/forum_left.do")
	public String forumLeft(ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("forumLeft(ModelMap) - start"); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			logger.debug("forumLeft(ModelMap) - end"); //$NON-NLS-1$
		}
		return "frame/forum_left";
	}

	@RequestMapping("/frame/forum_right.do")
	public String forumRight(ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("forumRight(ModelMap) - start"); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			logger.debug("forumRight(ModelMap) - end"); //$NON-NLS-1$
		}
		return "frame/forum_right";
	}

	@RequestMapping("/frame/template_main.do")
	public String templateMain(ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("templateMain(ModelMap) - start"); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			logger.debug("templateMain(ModelMap) - end"); //$NON-NLS-1$
		}
		return "frame/template_main";
	}

	@RequestMapping("/frame/resource_main.do")
	public String resourceMain(ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("resourceMain(ModelMap) - start"); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			logger.debug("resourceMain(ModelMap) - end"); //$NON-NLS-1$
		}
		return "frame/resource_main";
	}
	
	@RequestMapping("/frame/maintain_main.do")
	public String maintainMain(ModelMap model){
		if (logger.isDebugEnabled()) {
			logger.debug("maintainMain(ModelMap) - start"); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			logger.debug("maintainMain(ModelMap) - end"); //$NON-NLS-1$
		}
		return "frame/maintain_main";
	}
	
	@RequestMapping("/frame/maintain_left.do")
	public String maintainLeft(ModelMap model){
		if (logger.isDebugEnabled()) {
			logger.debug("maintainLeft(ModelMap) - start"); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			logger.debug("maintainLeft(ModelMap) - end"); //$NON-NLS-1$
		}
		return "frame/maintain_left";
	}
	
	@RequestMapping("/frame/maintain_right.do")
	public String maintainRight(ModelMap model){
		if (logger.isDebugEnabled()) {
			logger.debug("maintainRight(ModelMap) - start"); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			logger.debug("maintainRight(ModelMap) - end"); //$NON-NLS-1$
		}
		return "frame/maintain_right";
	}

}
