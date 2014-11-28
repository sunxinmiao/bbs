package com.jeecms.bbs.entity;

import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.jeecms.bbs.entity.base.BaseBbsUser;
import com.jeecms.common.hibernate3.PriorityInterface;
import com.jeecms.common.util.DateUtils;
import com.jeecms.core.entity.UnifiedUser;

public class BbsUser extends BaseBbsUser implements PriorityInterface {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsUser.class);

	private static final long serialVersionUID = 1L;

	/**
	 * 本地头像
	 */
	public static final short AVATAR_LOCAL = 0;
	/**
	 * 链接头像
	 */
	public static final short AVATAR_LINK = 1;

	/**
	 * 不禁言
	 */
	public static final short PROHIBIT_NO = 0;
	/**
	 * 永久禁言
	 */
	public static final short PROHIBIT_FOREVER = 1;
	/**
	 * 暂时禁言
	 */
	public static final short PROHIBIT_TEMPORARY = 2;

	public boolean getProhibit() {
		if (logger.isDebugEnabled()) {
			logger.debug("getProhibit() - start"); //$NON-NLS-1$
		}

		if (getProhibitPost() == PROHIBIT_FOREVER) {
			if (logger.isDebugEnabled()) {
				logger.debug("getProhibit() - end"); //$NON-NLS-1$
			}
			return true;
		}
		if (getProhibitPost() == PROHIBIT_TEMPORARY) {
			Date time = new Date();
			if (time.before(getProhibitTime())) {
				if (logger.isDebugEnabled()) {
					logger.debug("getProhibit() - end"); //$NON-NLS-1$
				}
				return true;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getProhibit() - end"); //$NON-NLS-1$
		}
		return false;
	}

	public Boolean getModerator() {
		if (logger.isDebugEnabled()) {
			logger.debug("getModerator() - start"); //$NON-NLS-1$
		}

		if (getGroup().getType().equals(BbsUserGroup.SYSTEM)) {
			if (logger.isDebugEnabled()) {
				logger.debug("getModerator() - end"); //$NON-NLS-1$
			}
			return true;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getModerator() - end"); //$NON-NLS-1$
			}
			return false;
		}
	}

	public String getRealname() {
		if (logger.isDebugEnabled()) {
			logger.debug("getRealname() - start"); //$NON-NLS-1$
		}

		BbsUserExt ext = getUserExt();
		if (ext != null) {
			String returnString = ext.getRealname();
			if (logger.isDebugEnabled()) {
				logger.debug("getRealname() - end"); //$NON-NLS-1$
			}
			return returnString;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getRealname() - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	public Boolean getGender() {
		if (logger.isDebugEnabled()) {
			logger.debug("getGender() - start"); //$NON-NLS-1$
		}

		BbsUserExt ext = getUserExt();
		if (ext != null) {
			Boolean returnBoolean = ext.getGender();
			if (logger.isDebugEnabled()) {
				logger.debug("getGender() - end"); //$NON-NLS-1$
			}
			return returnBoolean;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getGender() - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	public Date getBirthday() {
		if (logger.isDebugEnabled()) {
			logger.debug("getBirthday() - start"); //$NON-NLS-1$
		}

		BbsUserExt ext = getUserExt();
		if (ext != null) {
			Date returnDate = ext.getBirthday();
			if (logger.isDebugEnabled()) {
				logger.debug("getBirthday() - end"); //$NON-NLS-1$
			}
			return returnDate;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getBirthday() - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	public String getIntro() {
		if (logger.isDebugEnabled()) {
			logger.debug("getIntro() - start"); //$NON-NLS-1$
		}

		BbsUserExt ext = getUserExt();
		if (ext != null) {
			String returnString = ext.getIntro();
			if (logger.isDebugEnabled()) {
				logger.debug("getIntro() - end"); //$NON-NLS-1$
			}
			return returnString;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getIntro() - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	public String getComefrom() {
		if (logger.isDebugEnabled()) {
			logger.debug("getComefrom() - start"); //$NON-NLS-1$
		}

		BbsUserExt ext = getUserExt();
		if (ext != null) {
			String returnString = ext.getComefrom();
			if (logger.isDebugEnabled()) {
				logger.debug("getComefrom() - end"); //$NON-NLS-1$
			}
			return returnString;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getComefrom() - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	public String getQq() {
		if (logger.isDebugEnabled()) {
			logger.debug("getQq() - start"); //$NON-NLS-1$
		}

		BbsUserExt ext = getUserExt();
		if (ext != null) {
			String returnString = ext.getQq();
			if (logger.isDebugEnabled()) {
				logger.debug("getQq() - end"); //$NON-NLS-1$
			}
			return returnString;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getQq() - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	public String getMsn() {
		if (logger.isDebugEnabled()) {
			logger.debug("getMsn() - start"); //$NON-NLS-1$
		}

		BbsUserExt ext = getUserExt();
		if (ext != null) {
			String returnString = ext.getMsn();
			if (logger.isDebugEnabled()) {
				logger.debug("getMsn() - end"); //$NON-NLS-1$
			}
			return returnString;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getMsn() - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	public String getPhone() {
		if (logger.isDebugEnabled()) {
			logger.debug("getPhone() - start"); //$NON-NLS-1$
		}

		BbsUserExt ext = getUserExt();
		if (ext != null) {
			String returnString = ext.getPhone();
			if (logger.isDebugEnabled()) {
				logger.debug("getPhone() - end"); //$NON-NLS-1$
			}
			return returnString;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getPhone() - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	public String getMoble() {
		if (logger.isDebugEnabled()) {
			logger.debug("getMoble() - start"); //$NON-NLS-1$
		}

		BbsUserExt ext = getUserExt();
		if (ext != null) {
			String returnString = ext.getMoble();
			if (logger.isDebugEnabled()) {
				logger.debug("getMoble() - end"); //$NON-NLS-1$
			}
			return returnString;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getMoble() - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	public BbsUserExt getUserExt() {
		if (logger.isDebugEnabled()) {
			logger.debug("getUserExt() - start"); //$NON-NLS-1$
		}

		Set<BbsUserExt> set = getUserExtSet();
		if (set != null && set.size() > 0) {
			BbsUserExt returnBbsUserExt = set.iterator().next();
			if (logger.isDebugEnabled()) {
				logger.debug("getUserExt() - end"); //$NON-NLS-1$
			}
			return returnBbsUserExt;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getUserExt() - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	public BbsLoginLog getUserLaestLoginLog() {
		if (logger.isDebugEnabled()) {
			logger.debug("getUserLaestLoginLog() - start"); //$NON-NLS-1$
		}

		Set<BbsLoginLog> set = getLoginLogs();
		if (set != null && set.size() > 0) {
			BbsLoginLog returnBbsLoginLog = set.iterator().next();
			if (logger.isDebugEnabled()) {
				logger.debug("getUserLaestLoginLog() - end"); //$NON-NLS-1$
			}
			return returnBbsLoginLog;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getUserLaestLoginLog() - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	public int getLaestOnLineMinute() {
		if (logger.isDebugEnabled()) {
			logger.debug("getLaestOnLineMinute() - start"); //$NON-NLS-1$
		}

		BbsLoginLog log = getUserLaestLoginLog();
		int timout;
		if (log == null) {
			timout = 0;
		} else {
			timout = (int) ((log.getLogoutTime().getTime() - log.getLoginTime()
					.getTime()) / 1000 / 60);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getLaestOnLineMinute() - end"); //$NON-NLS-1$
		}
		return timout;
	}

	public Double getOnlineLatest() {
		if (logger.isDebugEnabled()) {
			logger.debug("getOnlineLatest() - start"); //$NON-NLS-1$
		}

		if (getUserOnline() != null) {
			Double returnDouble = splitDouble(getUserOnline().getOnlineLatest() / 60.0);
			if (logger.isDebugEnabled()) {
				logger.debug("getOnlineLatest() - end"); //$NON-NLS-1$
			}
			return returnDouble;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getOnlineLatest() - end"); //$NON-NLS-1$
			}
			return 0d;
		}
	}

	public int getDayOnLineMinute() {
		if (logger.isDebugEnabled()) {
			logger.debug("getDayOnLineMinute() - start"); //$NON-NLS-1$
		}

		Set<BbsLoginLog> logs = getLoginLogs();
		Iterator<BbsLoginLog> it = logs.iterator();
		BbsLoginLog log;
		int timout = 0;
		while (it.hasNext()) {
			log = it.next();
			if (DateUtils.isToday(log.getLoginTime())) {
				timout += (int) ((log.getLogoutTime().getTime() - log
						.getLoginTime().getTime()) / 1000 / 60);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getDayOnLineMinute() - end"); //$NON-NLS-1$
		}
		return timout;
	}

	public Double getOnlineDay() {
		if (logger.isDebugEnabled()) {
			logger.debug("getOnlineDay() - start"); //$NON-NLS-1$
		}

		if (getUserOnline() != null) {
			Double returnDouble = splitDouble(getUserOnline().getOnlineDay() / 60.0);
			if (logger.isDebugEnabled()) {
				logger.debug("getOnlineDay() - end"); //$NON-NLS-1$
			}
			return returnDouble;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getOnlineDay() - end"); //$NON-NLS-1$
			}
			return 0d;
		}
	}

	public int getWeekOnLineMinute() {
		if (logger.isDebugEnabled()) {
			logger.debug("getWeekOnLineMinute() - start"); //$NON-NLS-1$
		}

		Set<BbsLoginLog> logs = getLoginLogs();
		Iterator<BbsLoginLog> it = logs.iterator();
		BbsLoginLog log;
		int timout = 0;
		while (it.hasNext()) {
			log = it.next();
			if (DateUtils.isThisWeek(log.getLoginTime())) {
				timout += (int) ((log.getLogoutTime().getTime() - log
						.getLoginTime().getTime()) / 1000 / 60);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getWeekOnLineMinute() - end"); //$NON-NLS-1$
		}
		return timout;
	}

	public Double getOnlineWeek() {
		if (logger.isDebugEnabled()) {
			logger.debug("getOnlineWeek() - start"); //$NON-NLS-1$
		}

		if (getUserOnline() != null) {
			Double returnDouble = splitDouble(getUserOnline().getOnlineWeek() / 60.0);
			if (logger.isDebugEnabled()) {
				logger.debug("getOnlineWeek() - end"); //$NON-NLS-1$
			}
			return returnDouble;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getOnlineWeek() - end"); //$NON-NLS-1$
			}
			return 0d;
		}
	}

	public int getMonthOnLineMinute() {
		if (logger.isDebugEnabled()) {
			logger.debug("getMonthOnLineMinute() - start"); //$NON-NLS-1$
		}

		Set<BbsLoginLog> logs = getLoginLogs();
		Iterator<BbsLoginLog> it = logs.iterator();
		BbsLoginLog log;
		int timout = 0;
		while (it.hasNext()) {
			log = it.next();
			if (DateUtils.isThisMonth(log.getLoginTime())) {
				timout += (int) ((log.getLogoutTime().getTime() - log
						.getLoginTime().getTime()) / 1000 / 60);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getMonthOnLineMinute() - end"); //$NON-NLS-1$
		}
		return timout;
	}

	public Double getOnlineMonth() {
		if (logger.isDebugEnabled()) {
			logger.debug("getOnlineMonth() - start"); //$NON-NLS-1$
		}

		if (getUserOnline() != null) {
			Double returnDouble = splitDouble(getUserOnline().getOnlineMonth() / 60.0);
			if (logger.isDebugEnabled()) {
				logger.debug("getOnlineMonth() - end"); //$NON-NLS-1$
			}
			return returnDouble;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getOnlineMonth() - end"); //$NON-NLS-1$
			}
			return 0d;
		}
	}

	public int getYearOnLineMinute() {
		if (logger.isDebugEnabled()) {
			logger.debug("getYearOnLineMinute() - start"); //$NON-NLS-1$
		}

		Set<BbsLoginLog> logs = getLoginLogs();
		Iterator<BbsLoginLog> it = logs.iterator();
		BbsLoginLog log;
		int timout = 0;
		while (it.hasNext()) {
			log = it.next();
			if (DateUtils.isThisYear(log.getLoginTime())) {
				timout += (int) ((log.getLogoutTime().getTime() - log
						.getLoginTime().getTime()) / 1000 / 60);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getYearOnLineMinute() - end"); //$NON-NLS-1$
		}
		return timout;
	}

	public Double getOnlineYear() {
		if (logger.isDebugEnabled()) {
			logger.debug("getOnlineYear() - start"); //$NON-NLS-1$
		}

		if (getUserOnline() != null) {
			Double returnDouble = splitDouble(getUserOnline().getOnlineYear() / 60.0);
			if (logger.isDebugEnabled()) {
				logger.debug("getOnlineYear() - end"); //$NON-NLS-1$
			}
			return returnDouble;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getOnlineYear() - end"); //$NON-NLS-1$
			}
			return 0d;
		}
	}
	
	private double splitDouble(double d){
		if (logger.isDebugEnabled()) {
			logger.debug("splitDouble(double) - start"); //$NON-NLS-1$
		}

		double returndouble = ((double) Math.round(d * 100)) / 100;
		if (logger.isDebugEnabled()) {
			logger.debug("splitDouble(double) - end"); //$NON-NLS-1$
		}
		return returndouble;
	}

	public Set<BbsPostType> getPostTypeByForum(BbsForum forum) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPostTypeByForum(BbsForum) - start"); //$NON-NLS-1$
		}

		Set<BbsPostType> forumPostTypes = forum.getPostTypes();
		Iterator<BbsPostType> uit = getGroup().getPostTypes().iterator();
		Set<BbsPostType> result = new HashSet<BbsPostType>();
		BbsPostType type;
		while (uit.hasNext()) {
			type = uit.next();
			if (forumPostTypes.contains(type)) {
				result.add(type);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getPostTypeByForum(BbsForum) - end"); //$NON-NLS-1$
		}
		return result;
	}

	public BbsMemberMagic getMemberMagic(String mid) {
		if (logger.isDebugEnabled()) {
			logger.debug("getMemberMagic(String) - start"); //$NON-NLS-1$
		}

		Set<BbsMemberMagic> magics = getMemberMagics();
		Iterator<BbsMemberMagic> it = magics.iterator();
		BbsMemberMagic magic;
		while (it.hasNext()) {
			magic = it.next();
			if (magic.getMagic().getIdentifier().equals(mid)) {
				if (logger.isDebugEnabled()) {
					logger.debug("getMemberMagic(String) - end"); //$NON-NLS-1$
				}
				return magic;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getMemberMagic(String) - end"); //$NON-NLS-1$
		}
		return null;
	}

	public void addToMemberMagics(BbsMemberMagic magic) {
		if (logger.isDebugEnabled()) {
			logger.debug("addToMemberMagics(BbsMemberMagic) - start"); //$NON-NLS-1$
		}

		Set<BbsMemberMagic> magics = getMemberMagics();
		if (magics == null) {
			magics = new HashSet<BbsMemberMagic>();
			setMemberMagics(magics);
		}
		magics.add(magic);

		if (logger.isDebugEnabled()) {
			logger.debug("addToMemberMagics(BbsMemberMagic) - end"); //$NON-NLS-1$
		}
	}

	public void setMemberMagicNum(String mid, int num, int operator) {
		if (logger.isDebugEnabled()) {
			logger.debug("setMemberMagicNum(String, int, int) - start"); //$NON-NLS-1$
		}

		Set<BbsMemberMagic> magics = getMemberMagics();
		Iterator<BbsMemberMagic> it = magics.iterator();
		BbsMemberMagic magic;
		BbsCommonMagic commomMagic;
		while (it.hasNext()) {
			magic = it.next();
			commomMagic = magic.getMagic();
			if (commomMagic.getIdentifier().equals(mid)) {
				if (operator == 0) {
					// 减少数量
					commomMagic.setNum(commomMagic.getNum() - num);
				} else if (operator == 1) {
					// 增加数量
					commomMagic.setNum(commomMagic.getNum() + num);
				}
				break;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("setMemberMagicNum(String, int, int) - end"); //$NON-NLS-1$
		}
	}

	public void forMember(UnifiedUser u) {
		if (logger.isDebugEnabled()) {
			logger.debug("forMember(UnifiedUser) - start"); //$NON-NLS-1$
		}

		forUser(u);
		setAdmin(false);

		if (logger.isDebugEnabled()) {
			logger.debug("forMember(UnifiedUser) - end"); //$NON-NLS-1$
		}
	}

	public void forAdmin(UnifiedUser u, boolean viewonly, boolean selfAdmin,
			int rank) {
		if (logger.isDebugEnabled()) {
			logger.debug("forAdmin(UnifiedUser, boolean, boolean, int) - start"); //$NON-NLS-1$
		}

		forUser(u);
		setAdmin(true);

		if (logger.isDebugEnabled()) {
			logger.debug("forAdmin(UnifiedUser, boolean, boolean, int) - end"); //$NON-NLS-1$
		}
	}

	public void forUser(UnifiedUser u) {
		if (logger.isDebugEnabled()) {
			logger.debug("forUser(UnifiedUser) - start"); //$NON-NLS-1$
		}

		setDisabled(false);
		setId(u.getId());
		setUsername(u.getUsername());
		setEmail(u.getEmail());
		setRegisterIp(u.getRegisterIp());
		setRegisterTime(u.getRegisterTime());
		setLastLoginIp(u.getLastLoginIp());
		setLastLoginTime(u.getLastLoginTime());
		setLoginCount(0);

		if (logger.isDebugEnabled()) {
			logger.debug("forUser(UnifiedUser) - end"); //$NON-NLS-1$
		}
	}

	public void init() {
		if (logger.isDebugEnabled()) {
			logger.debug("init() - start"); //$NON-NLS-1$
		}

		if (getUploadTotal() == null) {
			setUploadTotal(0L);
		}
		if (getUploadSize() == null) {
			setUploadSize(0);
		}
		if (getUploadDate() == null) {
			setUploadDate(new java.sql.Date(System.currentTimeMillis()));
		}
		if (getAdmin() == null) {
			setAdmin(false);
		}
		if (getProhibitPost() == null) {
			setProhibitPost(PROHIBIT_NO);
		}
		if (getDisabled() == null) {
			setDisabled(false);
		}
		if (getUploadToday() == null) {
			setUploadToday(0);
		}
		if (getPoint() == null) {
			setPoint(0L);
		}
		if(getPrestige()==null){
			setPrestige(0L);
		}
		if(getMagicPacketSize()==null){
			setMagicPacketSize(0);
		}
		if (getAvatarType() == null) {
			setAvatarType(AVATAR_LOCAL);
		}
		if (getTopicCount() == null) {
			setTopicCount(0);
		}
		if (getReplyCount() == null) {
			setReplyCount(0);
		}
		if (getPrimeCount() == null) {
			setPrimeCount(0);
		}
		if (getPostToday() == null) {
			setPostToday(0);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("init() - end"); //$NON-NLS-1$
		}
	}

	public static Integer[] fetchIds(Collection<BbsUser> users) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchIds(Collection<BbsUser>) - start"); //$NON-NLS-1$
		}

		if (users == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("fetchIds(Collection<BbsUser>) - end"); //$NON-NLS-1$
			}
			return null;
		}
		Integer[] ids = new Integer[users.size()];
		int i = 0;
		for (BbsUser u : users) {
			ids[i++] = u.getId();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("fetchIds(Collection<BbsUser>) - end"); //$NON-NLS-1$
		}
		return ids;
	}

	/**
	 * 用于排列顺序。此处优先级为0，则按ID升序排。
	 */
	public Number getPriority() {
		return 0;
	}

	/**
	 * 是否是今天。根据System.currentTimeMillis() / 1000 / 60 / 60 / 24计算。
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isToday(Date date) {
		if (logger.isDebugEnabled()) {
			logger.debug("isToday(Date) - start"); //$NON-NLS-1$
		}

		long day = date.getTime() / 1000 / 60 / 60 / 24;
		long currentDay = System.currentTimeMillis() / 1000 / 60 / 60 / 24;
		boolean returnboolean = day == currentDay;
		if (logger.isDebugEnabled()) {
			logger.debug("isToday(Date) - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public BbsUser() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsUser(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsUser(java.lang.Integer id,
			com.jeecms.bbs.entity.BbsUserGroup group,
			java.lang.String username, java.util.Date registerTime,
			java.lang.String registerIp, java.lang.Integer loginCount,
			java.lang.Long uploadTotal, java.lang.Integer uploadToday,
			java.lang.Integer uploadSize, java.lang.Boolean admin,
			java.lang.Boolean disabled, java.lang.Long point,
			java.lang.Short avatarType, java.lang.Integer topicCount,
			java.lang.Integer replyCount, java.lang.Integer primeCount,
			java.lang.Integer postToday, java.lang.Short prohibitPost) {

		super(id, group, username, registerTime, registerIp, loginCount,
				uploadTotal, uploadToday, uploadSize, admin, disabled, point,
				avatarType, topicCount, replyCount, primeCount, postToday,
				prohibitPost);
	}

	/* [CONSTRUCTOR MARKER END] */

}