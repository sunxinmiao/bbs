package com.jeecms.bbs.entity;

import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.jeecms.bbs.entity.base.BaseBbsUserGroup;

public class BbsUserGroup extends BaseBbsUserGroup {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsUserGroup.class);

	private static final long serialVersionUID = 1L;
	/**
	 * 普通组
	 */
	public static final short NORMAL = 1;
	/**
	 * 系统组
	 */
	public static final short SYSTEM = 2;
	/**
	 * 特殊组
	 */
	public static final short SPECIAL = 3;
	/**
	 * 默认组
	 */
	public static final short DEFAULT = 0;

	/**
	 * 是否有版主权限
	 * 
	 * @param forum
	 *            相应的板块
	 * @return
	 */
	public boolean hasRight(BbsForum forum, BbsUser user) {
		if (logger.isDebugEnabled()) {
			logger.debug("hasRight(BbsForum, BbsUser) - start"); //$NON-NLS-1$
		}

		if (forum == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("hasRight(BbsForum, BbsUser) - end"); //$NON-NLS-1$
			}
			return false;
		}
		if (superModerator()) {
			if (logger.isDebugEnabled()) {
				logger.debug("hasRight(BbsForum, BbsUser) - end"); //$NON-NLS-1$
			}
			return true;
		}
		if (user == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("hasRight(BbsForum, BbsUser) - end"); //$NON-NLS-1$
			}
			return false;
		}
		if (("," + forum.getCategory().getModerators() + ",").indexOf(","
				+ user.getUsername() + ",") > -1
				|| ("," + forum.getModerators() + ",").indexOf(","
						+ user.getUsername() + ",") > -1) {
			if (logger.isDebugEnabled()) {
				logger.debug("hasRight(BbsForum, BbsUser) - end"); //$NON-NLS-1$
			}
			return true;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("hasRight(BbsForum, BbsUser) - end"); //$NON-NLS-1$
			}
			return false;
		}
	}

	/**
	 * 是否允许发表主题
	 * 
	 * @return
	 */
	public boolean allowTopic() {
		if (logger.isDebugEnabled()) {
			logger.debug("allowTopic() - start"); //$NON-NLS-1$
		}

		String s = getPerms().get("allow_topic");
		boolean returnboolean = StringUtils.equals(s, "true");
		if (logger.isDebugEnabled()) {
			logger.debug("allowTopic() - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/**
	 * 是否允许发表回复
	 * 
	 * @return
	 */
	public boolean allowReply() {
		if (logger.isDebugEnabled()) {
			logger.debug("allowReply() - start"); //$NON-NLS-1$
		}

		String s = getPerms().get("allow_reply");
		boolean returnboolean = StringUtils.equals(s, "true");
		if (logger.isDebugEnabled()) {
			logger.debug("allowReply() - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/**
	 * 检查今日发贴数
	 * 
	 * @param count
	 *            今日已发贴数
	 * @return true:允许发帖；false:已达最大发贴数
	 */
	public boolean checkPostToday(int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("checkPostToday(int) - start"); //$NON-NLS-1$
		}

		int max = getPostPerDay();
		if (max == 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("checkPostToday(int) - end"); //$NON-NLS-1$
			}
			return true;
		} else {
			boolean returnboolean = max > count;
			if (logger.isDebugEnabled()) {
				logger.debug("checkPostToday(int) - end"); //$NON-NLS-1$
			}
			return returnboolean;
		}
	}

	/**
	 * 每日最大发贴数
	 * 
	 * @return
	 */
	public int getPostPerDay() {
		if (logger.isDebugEnabled()) {
			logger.debug("getPostPerDay() - start"); //$NON-NLS-1$
		}

		int returnint = NumberUtils.toInt(getPerms().get("post_per_day"));
		if (logger.isDebugEnabled()) {
			logger.debug("getPostPerDay() - end"); //$NON-NLS-1$
		}
		return returnint;
	}

	/**
	 * 发帖时间间隔
	 * 
	 * @return
	 */
	public int getPostInterval() {
		if (logger.isDebugEnabled()) {
			logger.debug("getPostInterval() - start"); //$NON-NLS-1$
		}

		int returnint = NumberUtils.toInt(getPerms().get("post_interval"));
		if (logger.isDebugEnabled()) {
			logger.debug("getPostInterval() - end"); //$NON-NLS-1$
		}
		return returnint;
	}

	/**
	 * 超级版主。无需指定成为板块版主即有管理权限
	 * 
	 * @return
	 */
	public boolean superModerator() {
		if (logger.isDebugEnabled()) {
			logger.debug("superModerator() - start"); //$NON-NLS-1$
		}

		String s = getPerms().get("super_moderator");
		boolean returnboolean = StringUtils.equals(s, "true");
		if (logger.isDebugEnabled()) {
			logger.debug("superModerator() - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/**
	 * 发贴不受限制
	 * 
	 * @return
	 */
	public boolean postLimit() {
		if (logger.isDebugEnabled()) {
			logger.debug("postLimit() - start"); //$NON-NLS-1$
		}

		String s = getPerms().get("post_limit");
		boolean returnboolean = StringUtils.equals(s, "true");
		if (logger.isDebugEnabled()) {
			logger.debug("postLimit() - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/**
	 * 获得置顶权限级别
	 * 
	 * @return 0：无权限；1：本版置顶；2：分区置顶；3：全局置顶
	 */
	public short topicTop() {
		if (logger.isDebugEnabled()) {
			logger.debug("topicTop() - start"); //$NON-NLS-1$
		}

		short top = (short) NumberUtils.toInt(getPerms().get("topic_top"));
		if (top < 1 || top > 3) {
			if (logger.isDebugEnabled()) {
				logger.debug("topicTop() - end"); //$NON-NLS-1$
			}
			return 0;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("topicTop() - end"); //$NON-NLS-1$
			}
			return top;
		}
	}

	/**
	 * 主题管理权限。精、锁、提、亮、压
	 * 
	 * @return
	 */
	public boolean topicManage() {
		if (logger.isDebugEnabled()) {
			logger.debug("topicManage() - start"); //$NON-NLS-1$
		}

		String s = getPerms().get("post_limit");
		boolean returnboolean = StringUtils.equals(s, "true");
		if (logger.isDebugEnabled()) {
			logger.debug("topicManage() - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/**
	 * 编辑帖子
	 * 
	 * @return
	 */
	public boolean topicEdit() {
		if (logger.isDebugEnabled()) {
			logger.debug("topicEdit() - start"); //$NON-NLS-1$
		}

		String s = getPerms().get("topic_edit");
		boolean returnboolean = StringUtils.equals(s, "true");
		if (logger.isDebugEnabled()) {
			logger.debug("topicEdit() - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/**
	 * 删除帖子
	 * 
	 * @return
	 */
	public boolean topicDelete() {
		if (logger.isDebugEnabled()) {
			logger.debug("topicDelete() - start"); //$NON-NLS-1$
		}

		String s = getPerms().get("topic_delete");
		boolean returnboolean = StringUtils.equals(s, "true");
		if (logger.isDebugEnabled()) {
			logger.debug("topicDelete() - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/**
	 * 屏蔽帖子,移动帖子
	 * 
	 * @return
	 */
	public boolean topicShield() {
		if (logger.isDebugEnabled()) {
			logger.debug("topicShield() - start"); //$NON-NLS-1$
		}

		String s = getPerms().get("topic_shield");
		boolean returnboolean = StringUtils.equals(s, "true");
		if (logger.isDebugEnabled()) {
			logger.debug("topicShield() - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/**
	 * 查看IP
	 * 
	 * @return
	 */
	public boolean viewIp() {
		if (logger.isDebugEnabled()) {
			logger.debug("viewIp() - start"); //$NON-NLS-1$
		}

		String s = getPerms().get("view_ip");
		boolean returnboolean = StringUtils.equals(s, "true");
		if (logger.isDebugEnabled()) {
			logger.debug("viewIp() - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/**
	 * 屏蔽帖子
	 * 
	 * @return
	 */
	public boolean memberProhibit() {
		if (logger.isDebugEnabled()) {
			logger.debug("memberProhibit() - start"); //$NON-NLS-1$
		}

		String s = getPerms().get("member_prohibit");
		boolean returnboolean = StringUtils.equals(s, "true");
		if (logger.isDebugEnabled()) {
			logger.debug("memberProhibit() - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public BbsUserGroup() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsUserGroup(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsUserGroup(java.lang.Integer id,
			com.jeecms.core.entity.CmsSite site, java.lang.String name,
			java.lang.Short type, java.lang.Long point,
			java.lang.Boolean m_default, java.lang.Integer gradeNum) {

		super(id, site, name, type, point, m_default, gradeNum);
	}

	public void addToPostTypes(BbsPostType postType) {
		if (logger.isDebugEnabled()) {
			logger.debug("addToPostTypes(BbsPostType) - start"); //$NON-NLS-1$
		}

		Set<BbsPostType> postTypes = getPostTypes();
		if (postTypes == null) {
			postTypes = new HashSet<BbsPostType>();
			setPostTypes(postTypes);
		}
		postTypes.add(postType);

		if (logger.isDebugEnabled()) {
			logger.debug("addToPostTypes(BbsPostType) - end"); //$NON-NLS-1$
		}
	}

	public static Integer[] fetchIds(Collection<BbsPostType> types) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchIds(Collection<BbsPostType>) - start"); //$NON-NLS-1$
		}

		if (types == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("fetchIds(Collection<BbsPostType>) - end"); //$NON-NLS-1$
			}
			return null;
		}
		Integer[] ids = new Integer[types.size()];
		int i = 0;
		for (BbsPostType t : types) {
			ids[i++] = t.getId();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("fetchIds(Collection<BbsPostType>) - end"); //$NON-NLS-1$
		}
		return ids;
	}

	/* [CONSTRUCTOR MARKER END] */

}