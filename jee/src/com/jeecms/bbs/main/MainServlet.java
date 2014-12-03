package com.jeecms.bbs.main;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.jeecms.bbs.config.SpecialAccountConfig;

public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 272563432299954472L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String realPath = config.getServletContext().getRealPath("/").replace(
                "\\", "/");
		
		SpecialAccountConfig.init(realPath + config.getInitParameter("specialAccount"));
		
	}
}
