package com.jeecms.common.hibernate3;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Comparator;

@SuppressWarnings("serial")
public class PriorityComparator implements Comparator<PriorityInterface>,
		Serializable {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(PriorityComparator.class);

	public static final PriorityComparator INSTANCE = new PriorityComparator();

	public int compare(PriorityInterface o1, PriorityInterface o2) {
		if (logger.isDebugEnabled()) {
			logger.debug("compare(PriorityInterface, PriorityInterface) - start"); //$NON-NLS-1$
		}

		Number v1 = o1.getPriority();
		Number v2 = o2.getPriority();
		Number id1 = o1.getId();
		Number id2 = o2.getId();
		if (id1 != null && id2 != null && id1.equals(id2)) {
			if (logger.isDebugEnabled()) {
				logger.debug("compare(PriorityInterface, PriorityInterface) - end"); //$NON-NLS-1$
			}
			return 0;
		} else if (v1 == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("compare(PriorityInterface, PriorityInterface) - end"); //$NON-NLS-1$
			}
			return 1;
		} else if (v2 == null) {
			int returnint = -1;
			if (logger.isDebugEnabled()) {
				logger.debug("compare(PriorityInterface, PriorityInterface) - end"); //$NON-NLS-1$
			}
			return returnint;
		} else if (v1.longValue() > v2.longValue()) {
			if (logger.isDebugEnabled()) {
				logger.debug("compare(PriorityInterface, PriorityInterface) - end"); //$NON-NLS-1$
			}
			return 1;
		} else if (v1.longValue() < v2.longValue()) {
			int returnint = -1;
			if (logger.isDebugEnabled()) {
				logger.debug("compare(PriorityInterface, PriorityInterface) - end"); //$NON-NLS-1$
			}
			return returnint;
		} else if (id1 == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("compare(PriorityInterface, PriorityInterface) - end"); //$NON-NLS-1$
			}
			return 1;
		} else if (id2 == null) {
			int returnint = -1;
			if (logger.isDebugEnabled()) {
				logger.debug("compare(PriorityInterface, PriorityInterface) - end"); //$NON-NLS-1$
			}
			return returnint;
		} else if (id1.longValue() > id2.longValue()) {
			if (logger.isDebugEnabled()) {
				logger.debug("compare(PriorityInterface, PriorityInterface) - end"); //$NON-NLS-1$
			}
			return 1;
		} else if (id1.longValue() < id2.longValue()) {
			int returnint = -1;
			if (logger.isDebugEnabled()) {
				logger.debug("compare(PriorityInterface, PriorityInterface) - end"); //$NON-NLS-1$
			}
			return returnint;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("compare(PriorityInterface, PriorityInterface) - end"); //$NON-NLS-1$
			}
			return 0;
		}
	}
}
