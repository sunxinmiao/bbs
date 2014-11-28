package com.jeecms.core.template;

import org.apache.log4j.Logger;

import com.jeecms.common.developer.ModuleGenerator;

public class CoreModuleGenerator {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CoreModuleGenerator.class);

	private static String packName = "com.jeecms.core.template";
	private static String fileName = "jeecore.properties";

	public static void main(String[] args) {
		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - start"); //$NON-NLS-1$
		}

		new ModuleGenerator(packName, fileName).generate();

		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - end"); //$NON-NLS-1$
		}
	}
}
