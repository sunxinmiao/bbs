package com.jeecms.bbs.main;

import org.apache.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.jeecms.bbs.config.SpecialAccountConfig;

public class MainServlet extends HttpServlet {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MainServlet.class);

	private static final long serialVersionUID = 272563432299954472L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("init(ServletConfig) - start"); //$NON-NLS-1$
		}

		super.init(config);
		String realPath = config.getServletContext().getRealPath("/").replace(
                "\\", "/");
		
		SpecialAccountConfig.init(realPath + config.getInitParameter("specialAccount"));
		

		if (logger.isDebugEnabled()) {
			logger.debug("init(ServletConfig) - end"); //$NON-NLS-1$
		}
	}
}
