package com.jeecms.bbs.web;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class SiteNotFoundException extends RuntimeException {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SiteNotFoundException.class);

	private String domain;

	public SiteNotFoundException(String domain) {
		this.domain = domain;
	}

	public String getDomain() {
		return this.domain;
	}
}
