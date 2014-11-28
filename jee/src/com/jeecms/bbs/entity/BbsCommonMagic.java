package com.jeecms.bbs.entity;

import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.jeecms.bbs.entity.base.BaseBbsCommonMagic;



public class BbsCommonMagic extends BaseBbsCommonMagic {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsCommonMagic.class);

	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsCommonMagic () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsCommonMagic (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsCommonMagic (
		java.lang.Integer id,
		java.lang.Boolean available,
		java.lang.String name,
		java.lang.String identifier,
		java.lang.String description,
		java.lang.Byte displayorder,
		java.lang.Byte credit,
		java.lang.Integer price,
		java.lang.Integer num,
		java.lang.Integer salevolume,
		java.lang.Integer supplytype,
		java.lang.Integer supplynum,
		java.lang.Integer useperoid,
		java.lang.Integer usenum,
		java.lang.Integer weight,
		java.lang.Boolean useevent) {

		super (
			id,
			available,
			name,
			identifier,
			description,
			displayorder,
			credit,
			price,
			num,
			salevolume,
			supplytype,
			supplynum,
			useperoid,
			usenum,
			weight,
			useevent);
	}
	public void addToGroups(BbsUserGroup group) {
		if (logger.isDebugEnabled()) {
			logger.debug("addToGroups(BbsUserGroup) - start"); //$NON-NLS-1$
		}

		Set<BbsUserGroup> groups = getUseGroups();
		if (groups == null) {
			groups = new HashSet<BbsUserGroup>();
			setUseGroups(groups);
		}
		groups.add(group);

		if (logger.isDebugEnabled()) {
			logger.debug("addToGroups(BbsUserGroup) - end"); //$NON-NLS-1$
		}
	}
	public void addToToUseGroups(BbsUserGroup group) {
		if (logger.isDebugEnabled()) {
			logger.debug("addToToUseGroups(BbsUserGroup) - start"); //$NON-NLS-1$
		}

		Set<BbsUserGroup> groups = getToUseGroups();
		if (groups == null) {
			groups = new HashSet<BbsUserGroup>();
			setToUseGroups(groups);
		}
		groups.add(group);

		if (logger.isDebugEnabled()) {
			logger.debug("addToToUseGroups(BbsUserGroup) - end"); //$NON-NLS-1$
		}
	}
	public Integer[] getGroupIds() {
		if (logger.isDebugEnabled()) {
			logger.debug("getGroupIds() - start"); //$NON-NLS-1$
		}

		Set<BbsUserGroup> groups = getUseGroups();
		Integer[] returnIntegerArray = fetchIds(groups);
		if (logger.isDebugEnabled()) {
			logger.debug("getGroupIds() - end"); //$NON-NLS-1$
		}
		return returnIntegerArray;
	}
	public Integer[] getToUseGroupIds() {
		if (logger.isDebugEnabled()) {
			logger.debug("getToUseGroupIds() - start"); //$NON-NLS-1$
		}

		Set<BbsUserGroup> groups = getToUseGroups();
		Integer[] returnIntegerArray = fetchIds(groups);
		if (logger.isDebugEnabled()) {
			logger.debug("getToUseGroupIds() - end"); //$NON-NLS-1$
		}
		return returnIntegerArray;
	}
	public static Integer[] fetchIds(Collection<BbsUserGroup> groups) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchIds(Collection<BbsUserGroup>) - start"); //$NON-NLS-1$
		}

		if (groups == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("fetchIds(Collection<BbsUserGroup>) - end"); //$NON-NLS-1$
			}
			return null;
		}
		Integer[] ids = new Integer[groups.size()];
		int i = 0;
		for (BbsUserGroup g : groups) {
			ids[i++] =g.getId();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("fetchIds(Collection<BbsUserGroup>) - end"); //$NON-NLS-1$
		}
		return ids;
	}

/*[CONSTRUCTOR MARKER END]*/


}