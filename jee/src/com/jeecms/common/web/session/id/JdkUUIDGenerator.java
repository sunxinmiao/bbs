package com.jeecms.common.web.session.id;

import org.apache.log4j.Logger;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;

public class JdkUUIDGenerator implements SessionIdGenerator {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(JdkUUIDGenerator.class);

	public String get() {
		if (logger.isDebugEnabled()) {
			logger.debug("get() - start"); //$NON-NLS-1$
		}

		String returnString = StringUtils.remove(UUID.randomUUID().toString(), '-');
		if (logger.isDebugEnabled()) {
			logger.debug("get() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	public static void main(String[] args) {
		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - start"); //$NON-NLS-1$
		}

		UUID.randomUUID();
		long time = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			UUID.randomUUID();
		}
		time = System.currentTimeMillis() - time;
		System.out.println(time);

		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - end"); //$NON-NLS-1$
		}
	}
}
