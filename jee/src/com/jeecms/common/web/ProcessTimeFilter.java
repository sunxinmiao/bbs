package com.jeecms.common.web;

import org.apache.log4j.Logger;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;




/**
 * 执行时间过滤器
 * 
 * @author liufang
 * 
 */
public class ProcessTimeFilter implements Filter {
	protected final Logger logger = Logger.getLogger(ProcessTimeFilter.class);
	/**
	 * 请求执行开始时间
	 */
	public static final String START_TIME = "_start_time";

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("doFilter(ServletRequest, ServletResponse, FilterChain) - start"); //$NON-NLS-1$
		}

		HttpServletRequest request = (HttpServletRequest) req;
		// 取用户访问的URL
		String url = request.getServletPath();
		System.out.println(url);
		long time = System.currentTimeMillis();
		request.setAttribute(START_TIME, time);
		chain.doFilter(request, response);
		time = System.currentTimeMillis() - time;
		logger.debug("process in "+time+" ms: "+  request.getRequestURI());
	}

	public void init(FilterConfig arg0) throws ServletException {
	}
}
