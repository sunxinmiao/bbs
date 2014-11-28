package com.jeecms.common.web.session.id;

import org.apache.log4j.Logger;

import org.apache.commons.lang.StringUtils;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

/**
 * 通过UUID生成SESSION ID
 * 
 * @author liufang
 * 
 */
public class JugUUIDGenerator implements SessionIdGenerator {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(JugUUIDGenerator.class);

	public String get() {
		if (logger.isDebugEnabled()) {
			logger.debug("get() - start"); //$NON-NLS-1$
		}

		UUID uuid = UUIDGenerator.getInstance().generateRandomBasedUUID();
		String returnString = StringUtils.remove(uuid.toString(), '-');
		if (logger.isDebugEnabled()) {
			logger.debug("get() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	public static void main(String[] args) {
		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - start"); //$NON-NLS-1$
		}

		UUIDGenerator.getInstance().generateRandomBasedUUID();
		long time = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			UUIDGenerator.getInstance().generateRandomBasedUUID();
		}
		time = System.currentTimeMillis() - time;
		System.out.println(time);

		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - end"); //$NON-NLS-1$
		}
	}
}
