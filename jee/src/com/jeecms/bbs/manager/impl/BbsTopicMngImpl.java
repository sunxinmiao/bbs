package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;

import static com.jeecms.bbs.entity.BbsTopic.TOPIC_VOTE;
import static com.jeecms.bbs.entity.BbsTopic.TOPIC_NORMAL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jeecms.bbs.Constants;
import com.jeecms.bbs.MagicConstants;
import com.jeecms.bbs.cache.BbsConfigEhCache;
import com.jeecms.bbs.dao.BbsTopicDao;
import com.jeecms.bbs.entity.BbsCommonMagic;
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsPost;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsTopicText;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsVoteItem;
import com.jeecms.bbs.entity.BbsVoteTopic;
import com.jeecms.bbs.manager.BbsCommonMagicMng;
import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.bbs.manager.BbsMagicConfigMng;
import com.jeecms.bbs.manager.BbsOperationMng;
import com.jeecms.bbs.manager.BbsPostMng;
import com.jeecms.bbs.manager.BbsPostTypeMng;
import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.manager.BbsVoteItemMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.OnlineCountListener;
import com.jeecms.common.hibernate3.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.core.manager.CmsSiteMng;
import com.jeecms.core.web.MagicMessage;

@Service
@Transactional
public class BbsTopicMngImpl implements BbsTopicMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsTopicMngImpl.class);
	
	public static final String AUTH_KEY = "auth_key";

	public void move(Integer[] ids, Integer forumId, String reason,
			BbsUser operator) {
		if (logger.isDebugEnabled()) {
			logger.debug("move(Integer[], Integer, String, BbsUser) - start"); //$NON-NLS-1$
		}

		BbsTopic topic;
		BbsForum origForum;
		BbsForum currForum;

		for (Integer id : ids) {

			topic = dao.findById(id);
			origForum = topic.getForum();
			if (!origForum.getId().equals(forumId)) {
				currForum = bbsForumMng.findById(forumId);
				topic.setForum(currForum);
				origForum.setTopicTotal(origForum.getTopicTotal() - 1);
				currForum.setTopicTotal(currForum.getTopicTotal() + 1);
				if (origForum.getLastPost() != null) {
					if (origForum.getLastPost().getTopic().getId() == id) {
						BbsPost lastPost = bbsPostMng.getLastPost(origForum
								.getId(), 0);
						if (lastPost != null) {
							origForum.setLastPost(lastPost);
						} else {
							origForum.setLastPost(null);
						}
					}
				}
			}
			bbsOperationMng.saveOpt(topic.getSite(), operator, "移动主题", reason,
					topic);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("move(Integer[], Integer, String, BbsUser) - end"); //$NON-NLS-1$
		}
	}

	public void shieldOrOpen(Integer[] ids, boolean shield, String reason,
			BbsUser operator) {
		if (logger.isDebugEnabled()) {
			logger.debug("shieldOrOpen(Integer[], boolean, String, BbsUser) - start"); //$NON-NLS-1$
		}

		BbsTopic topic;
		for (Integer id : ids) {
			topic = dao.findById(id);
			short status = topic.getStatus();
			if (shield) {
				if (status == BbsTopic.NORMAL) {
					topic.setStatus(BbsTopic.SHIELD);
				}
				bbsOperationMng.saveOpt(topic.getSite(), operator, "屏蔽主题",
						reason, topic);
			} else {
				if (status == BbsTopic.SHIELD) {
					topic.setStatus(BbsTopic.NORMAL);
				}
				bbsOperationMng.saveOpt(topic.getSite(), operator, "解除主题",
						reason, topic);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("shieldOrOpen(Integer[], boolean, String, BbsUser) - end"); //$NON-NLS-1$
		}
	}

	public void lockOrOpen(Integer[] ids, boolean lock, String reason,
			BbsUser operator) {
		if (logger.isDebugEnabled()) {
			logger.debug("lockOrOpen(Integer[], boolean, String, BbsUser) - start"); //$NON-NLS-1$
		}

		BbsTopic topic;
		for (Integer id : ids) {
			topic = dao.findById(id);
			short status = topic.getStatus();
			if (lock) {
				if (status == BbsTopic.NORMAL) {
					topic.setStatus(BbsTopic.LOCKED);
				}
				bbsOperationMng.saveOpt(topic.getSite(), operator, "锁定主题",
						reason, topic);
			} else {
				if (status == BbsTopic.LOCKED) {
					topic.setStatus(BbsTopic.NORMAL);
				}
				bbsOperationMng.saveOpt(topic.getSite(), operator, "解除主题",
						reason, topic);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("lockOrOpen(Integer[], boolean, String, BbsUser) - end"); //$NON-NLS-1$
		}
	}

	public void upOrDown(Integer[] ids, Date time, String reason,
			BbsUser operator) {
		if (logger.isDebugEnabled()) {
			logger.debug("upOrDown(Integer[], Date, String, BbsUser) - start"); //$NON-NLS-1$
		}

		BbsTopic topic;
		for (Integer id : ids) {
			topic = dao.findById(id);
			topic.setSortTime(time);
			bbsOperationMng.saveOpt(topic.getSite(), operator, "提升/下沉主题",
					reason, topic);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("upOrDown(Integer[], Date, String, BbsUser) - end"); //$NON-NLS-1$
		}
	}

	public void prime(Integer[] ids, short primeLevel, String reason,
			BbsUser operator) {
		if (logger.isDebugEnabled()) {
			logger.debug("prime(Integer[], short, String, BbsUser) - start"); //$NON-NLS-1$
		}

		BbsTopic topic;
		BbsUser toUser;
		BbsForum topicForum;
		for (Integer id : ids) {
			topic = dao.findById(id);
			topic.setPrimeLevel(primeLevel);
			toUser=topic.getCreater();
			topicForum=topic.getForum();
			if(primeLevel==1){
				toUser.setPrestige(toUser.getPrestige()+topicForum.getPrestigePrime1());
				toUser.setPrimeCount(toUser.getPrimeCount()+1);
			}else if(primeLevel==2){
				toUser.setPrestige(toUser.getPrestige()+topicForum.getPrestigePrime2());
				toUser.setPrimeCount(toUser.getPrimeCount()+1);
			}else if(primeLevel==3){
				toUser.setPrestige(toUser.getPrestige()+topicForum.getPrestigePrime3());
				toUser.setPrimeCount(toUser.getPrimeCount()+1);
			}else if(primeLevel==0){
				toUser.setPrestige(toUser.getPrestige()+topicForum.getPrestigePrime0());
				toUser.setPrimeCount(toUser.getPrimeCount()-1);
			}
			bbsOperationMng.saveOpt(topic.getSite(), operator, "精华", reason,
					topic);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("prime(Integer[], short, String, BbsUser) - end"); //$NON-NLS-1$
		}
	}

	public void upTop(Integer[] ids, short topLevel, String reason,
			BbsUser operator) {
		if (logger.isDebugEnabled()) {
			logger.debug("upTop(Integer[], short, String, BbsUser) - start"); //$NON-NLS-1$
		}

		BbsTopic topic;
		for (Integer id : ids) {
			topic = dao.findById(id);
			topic.setTopLevel(topLevel);
			bbsOperationMng.saveOpt(topic.getSite(), operator, "置顶", reason,
					topic);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("upTop(Integer[], short, String, BbsUser) - end"); //$NON-NLS-1$
		}
	}

	public void highlight(Integer[] ids, String color, boolean bold,
			boolean italic, Date time, String reason, BbsUser operator) {
		if (logger.isDebugEnabled()) {
			logger.debug("highlight(Integer[], String, boolean, boolean, Date, String, BbsUser) - start"); //$NON-NLS-1$
		}

		BbsTopic topic;
		for (Integer id : ids) {
			topic = dao.findById(id);
			topic.setStyleColor(color);
			topic.setStyleTime(time);
			topic.setStyleBold(bold);
			topic.setStyleItalic(italic);
			bbsOperationMng.saveOpt(topic.getSite(), operator, "高亮", reason,
					topic);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("highlight(Integer[], String, boolean, boolean, Date, String, BbsUser) - end"); //$NON-NLS-1$
		}
	}
	
	public void highlightWithNoLog(Integer[] ids, String color, boolean bold,
			boolean italic, Date time, String reason, BbsUser operator) {
		if (logger.isDebugEnabled()) {
			logger.debug("highlightWithNoLog(Integer[], String, boolean, boolean, Date, String, BbsUser) - start"); //$NON-NLS-1$
		}

		BbsTopic topic;
		for (Integer id : ids) {
			topic = dao.findById(id);
			topic.setStyleColor(color);
			topic.setStyleTime(time);
			topic.setStyleBold(bold);
			topic.setStyleItalic(italic);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("highlightWithNoLog(Integer[], String, boolean, boolean, Date, String, BbsUser) - end"); //$NON-NLS-1$
		}
	}

	public BbsTopic updateTitle(Integer id, String title, BbsUser editor) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateTitle(Integer, String, BbsUser) - start"); //$NON-NLS-1$
		}

		BbsTopic topic = dao.findById(id);
		topic.setTitle(title);
		bbsOperationMng.saveOpt(topic.getSite(), editor, "修改主题标题", null, topic);

		if (logger.isDebugEnabled()) {
			logger.debug("updateTitle(Integer, String, BbsUser) - end"); //$NON-NLS-1$
		}
		return topic;
	}

	public BbsTopic updateTopic(Integer id, String title, String content,
			BbsUser editor, String ip) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateTopic(Integer, String, String, BbsUser, String) - start"); //$NON-NLS-1$
		}

		BbsTopic topic = dao.findById(id);
		topic.setTitle(title);
		bbsPostMng.updatePost(topic.getFirstPost().getId(), title, content,
				editor, ip);

		if (logger.isDebugEnabled()) {
			logger.debug("updateTopic(Integer, String, String, BbsUser, String) - end"); //$NON-NLS-1$
		}
		return topic;
	}

	public BbsTopic postTopic(Integer userId, Integer siteId, Integer forumId,
			Integer postTypeId, String title, String content, String ip,
			Integer category, String[] name, List<MultipartFile> file,
			List<String> code) {
		if (logger.isDebugEnabled()) {
			logger.debug("postTopic(Integer, Integer, Integer, Integer, String, String, String, Integer, String[], List<MultipartFile>, List<String>) - start"); //$NON-NLS-1$
		}

		BbsForum forum = bbsForumMng.findById(forumId);
		BbsUser user = bbsUserMng.findById(userId);
		BbsTopicText text = new BbsTopicText();
		BbsTopic topic = createTopic(category);
		topic.setSite(siteMng.findById(siteId));
		topic.setForum(forum);
		if (postTypeId != null) {
			topic.setPostType(bbsPostTypeMng.findById(postTypeId));
		}
		topic.setCreater(user);
		topic.setLastReply(user);
		topic.setTopicText(text);
		topic.setTopicTitle(title);
		text.setTitle(title);
		text.setTopic(topic);
		if (file != null && file.size() > 0) {
			topic.setAffix(true);
		} else {
			topic.setAffix(false);
		}
		topic.init();
		save(topic);
		handleVoteItem(topic, name);
		BbsPost post = bbsPostMng.post(userId, siteId, topic.getId(),
				postTypeId, title, content, ip, file, code);
		topic.setFirstPost(post);
		updateTopicCount(topic, user);
		bbsConfigEhCache.setBbsConfigCache(1, 1, 0, 0, null, siteId);

		if (logger.isDebugEnabled()) {
			logger.debug("postTopic(Integer, Integer, Integer, Integer, String, String, String, Integer, String[], List<MultipartFile>, List<String>) - end"); //$NON-NLS-1$
		}
		return topic;
	}

	@Transactional(readOnly = true)
	public Pagination getForTag(Integer siteId, Integer forumId,Integer parentPostTypeId,Integer postTypeId, Short status,
			Short primeLevel, String keyWords, String creater,
			Integer createrId, Short topLevel, int pageNo, int pageSize,String jinghua) {
		if (logger.isDebugEnabled()) {
			logger.debug("getForTag(Integer, Integer, Integer, Integer, Short, Short, String, String, Integer, Short, int, int, String) - start"); //$NON-NLS-1$
		}

		Pagination returnPagination = dao.getForTag(siteId, forumId, parentPostTypeId, postTypeId, status, primeLevel, keyWords, creater, createrId, topLevel, pageNo, pageSize, jinghua);
		if (logger.isDebugEnabled()) {
			logger.debug("getForTag(Integer, Integer, Integer, Integer, Short, Short, String, String, Integer, Short, int, int, String) - end"); //$NON-NLS-1$
		}
		return returnPagination;

	}

	@Transactional(readOnly = true)
	public Pagination getMemberTopic(Integer webId, Integer memberId,
			int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getMemberTopic(Integer, Integer, int, int) - start"); //$NON-NLS-1$
		}

		Pagination returnPagination = dao.getMemberTopic(webId, memberId, pageNo, pageSize);
		if (logger.isDebugEnabled()) {
			logger.debug("getMemberTopic(Integer, Integer, int, int) - end"); //$NON-NLS-1$
		}
		return returnPagination;
	}

	@Transactional(readOnly = true)
	public List<BbsTopic> getTopTopic(Integer webId, Integer ctgId,
			Integer forumId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getTopTopic(Integer, Integer, Integer) - start"); //$NON-NLS-1$
		}

		List<BbsTopic> returnList = dao.getTopTopic(webId, ctgId, forumId);
		if (logger.isDebugEnabled()) {
			logger.debug("getTopTopic(Integer, Integer, Integer) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	@Transactional(readOnly = true)
	public Pagination getMemberReply(Integer webId, Integer memberId,
			int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getMemberReply(Integer, Integer, int, int) - start"); //$NON-NLS-1$
		}

		Pagination returnPagination = dao.getMemberReply(webId, memberId, pageNo, pageSize);
		if (logger.isDebugEnabled()) {
			logger.debug("getMemberReply(Integer, Integer, int, int) - end"); //$NON-NLS-1$
		}
		return returnPagination;
	}

	@Transactional(readOnly = true)
	public Pagination getTopicByTime(Integer webId, int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getTopicByTime(Integer, int, int) - start"); //$NON-NLS-1$
		}

		Pagination returnPagination = dao.getTopicByTime(webId, pageNo, pageSize);
		if (logger.isDebugEnabled()) {
			logger.debug("getTopicByTime(Integer, int, int) - end"); //$NON-NLS-1$
		}
		return returnPagination;
	}

	@Transactional(readOnly = true)
	public Pagination getForSearchDate(Integer siteId, Integer forumId,
			Short primeLevel, Integer day, int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getForSearchDate(Integer, Integer, Short, Integer, int, int) - start"); //$NON-NLS-1$
		}

		Pagination returnPagination = dao.getForSearchDate(siteId, forumId, primeLevel, day, pageNo, pageSize);
		if (logger.isDebugEnabled()) {
			logger.debug("getForSearchDate(Integer, Integer, Short, Integer, int, int) - end"); //$NON-NLS-1$
		}
		return returnPagination;
	}

	public BbsTopic save(BbsTopic topic) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsTopic) - start"); //$NON-NLS-1$
		}

		initTopic(topic);
		dao.save(topic);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsTopic) - end"); //$NON-NLS-1$
		}
		return topic;
	}

	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPage(int, int) - start"); //$NON-NLS-1$
		}

		Pagination page = dao.getPage(pageNo, pageSize);

		if (logger.isDebugEnabled()) {
			logger.debug("getPage(int, int) - end"); //$NON-NLS-1$
		}
		return page;
	}

	@Transactional(readOnly = true)
	public BbsTopic findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsTopic entity = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsTopic update(BbsTopic bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsTopic) - start"); //$NON-NLS-1$
		}

		Updater<BbsTopic> updater = new Updater<BbsTopic>(bean);
		bean = dao.updateByUpdater(updater);

		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsTopic) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsTopic deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsTopic bean = dao.findById(id);
		bean.setFirstPost(null);
		bean.setLastPost(null);
		List<BbsPost> postList = bbsPostMng.getPostByTopic(id);
		for (BbsPost post : postList) {
			if (post.equals(bean.getForum().getLastPost())) {
				BbsPost post1 = bbsPostMng.getLastPost(bean.getForum().getId(),
						id);
				if (post1 == null) {
					bean.getForum().setLastPost(null);
					bean.getForum().setLastReply(null);
					bean.getForum().setLastTime(null);
				} else {
					bean.getForum().setLastPost(post1);
					bean.getForum().setLastReply(post1.getCreater());
					bean.getForum().setLastTime(post1.getCreateTime());
				}
			}
			bbsPostMng.deleteById(post.getId());
		}
		dao.deleteById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsTopic[] deleteByIds(Integer[] ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - start"); //$NON-NLS-1$
		}

		BbsTopic[] beans = new BbsTopic[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}

	private void initTopic(BbsTopic topic) {
		if (logger.isDebugEnabled()) {
			logger.debug("initTopic(BbsTopic) - start"); //$NON-NLS-1$
		}

		Date now = new Timestamp(System.currentTimeMillis());
		topic.setCreateTime(now);
		topic.setLastTime(now);
		topic.setSortTime(now);
		topic.setViewCount(0L);
		topic.setReplyCount(0);
		topic.setStatus(BbsTopic.NORMAL);
		if (topic.getTopLevel() == null) {
			topic.setTopLevel((short) 0);
		}
		if (topic.getPrimeLevel() == null) {
			topic.setPrimeLevel((short) 0);
		}
		if (topic.getStyleBold() == null) {
			topic.setStyleBold(false);
		}
		if (topic.getStyleItalic() == null) {
			topic.setStyleItalic(false);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initTopic(BbsTopic) - end"); //$NON-NLS-1$
		}
	}

	public void updateTopicCount(BbsTopic topic, BbsUser user) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateTopicCount(BbsTopic, BbsUser) - start"); //$NON-NLS-1$
		}

		BbsForum forum = topic.getForum();
		forum.setLastPost(topic.getFirstPost());
		forum.setLastReply(topic.getCreater());
		forum.setLastTime(topic.getSortTime());
		forum.setPostToday(forum.getPostToday() + 1);
		forum.setPostTotal(forum.getPostTotal() + 1);
		forum.setTopicTotal(forum.getTopicTotal() + 1);
		//是否启用积分
		if(forum.getPointAvailable()){
			user.setPoint(user.getPoint() + forum.getPointTopic());
		}
		//是否启用威望
		if(forum.getPrestigeAvailable()){
			user.setPrestige(user.getPrestige()+forum.getPrestigeTopic());
		}
		user.setTopicCount(user.getTopicCount() + 1);
		user.setPostToday(user.getPostToday() + 1);

		if (logger.isDebugEnabled()) {
			logger.debug("updateTopicCount(BbsTopic, BbsUser) - end"); //$NON-NLS-1$
		}
	}

	public List<BbsTopic> getListByForumId(Integer forumId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getListByForumId(Integer) - start"); //$NON-NLS-1$
		}

		// TODO Auto-generated method stub
		List<BbsTopic> returnList = dao.getListByForumId(forumId);
		if (logger.isDebugEnabled()) {
			logger.debug("getListByForumId(Integer) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	public List<BbsTopic> getNewList(Integer count) {
		if (logger.isDebugEnabled()) {
			logger.debug("getNewList(Integer) - start"); //$NON-NLS-1$
		}

		// TODO Auto-generated method stub
		List<BbsTopic> returnList = dao.getNewList(count);
		if (logger.isDebugEnabled()) {
			logger.debug("getNewList(Integer) - end"); //$NON-NLS-1$
		}
		return returnList;
	}
	/**
	 *  使用道具
	 */
	public String useMagic(HttpServletRequest request,Integer siteId,Integer tid,
			String magicName,Integer userId,String ip,String color,Integer postCreaterId){
		if (logger.isDebugEnabled()) {
			logger.debug("useMagic(HttpServletRequest, Integer, Integer, String, Integer, String, String, Integer) - start"); //$NON-NLS-1$
		}

		BbsTopic topic=null;
		if(tid!=null){
			topic=findById(tid);
		}
		BbsUser user=bbsUserMng.findById(userId);
		BbsPost post;
		BbsUser postCreater;
		if(StringUtils.isNotBlank(magicName)){
			if(magicName.equals(MagicConstants.MAGIC_CLOSE)){
				/**
				 * 沉默卡--关闭主题
				 */
				if(bbsForumMng.getModerators(siteId).contains(topic.getCreater().getUsername())){
					if (logger.isDebugEnabled()) {
						logger.debug("useMagic(HttpServletRequest, Integer, Integer, String, Integer, String, String, Integer) - end"); //$NON-NLS-1$
					}
					return MagicConstants.MAGIC_OPEN_ERROR_NOIN_MODERATORS;
				}
				topic.setAllayReply(false);
			}else if(magicName.equals(MagicConstants.MAGIC_OPEN)){
				/**
				 * 喧嚣卡---打开主题
				 */
				topic.setAllayReply(true);
			}else if(magicName.equals(MagicConstants.MAGIC_BUMP)){
				/**
				 * 提升卡
				 */
				topic.setTopLevel(MagicConstants.TOP_LEVEL_BUMP);
			}else if(magicName.equals(MagicConstants.MAGIC_JACK)){
				/**
				 * 千斤顶
				 */
				topic.setTopLevel(MagicConstants.TOP_LEVEL_JACK);
			}else if(magicName.equals(MagicConstants.MAGIC_STICK)){
				/**
				 * 置顶卡
				 */
				topic.setTopLevel(MagicConstants.TOP_LEVEL_STICK);
			}else if(magicName.equals(MagicConstants.MAGIC_SOFA)){
				/**
				 * 抢沙发(沙发的台词以后再设置道具那里再做更改由后台定制而来)
				 */
				if(topic.getHasSofeed()){
					if (logger.isDebugEnabled()) {
						logger.debug("useMagic(HttpServletRequest, Integer, Integer, String, Integer, String, String, Integer) - end"); //$NON-NLS-1$
					}
					return MagicConstants.MAGIC_SOFEED_ERROR;
				}else{
					String sofalines=magicConfigMng.findById(siteId).getMagicSofaLines();
					bbsPostMng.reply(userId, siteId, tid,topic.getPostType().getId(), sofalines,
							sofalines, ip, null, null);
					topic.setHasSofeed(true);
				}
			}else if(magicName.equals(MagicConstants.MAGIC_HIGHTLIGHT)){
				/**
				 * 变色卡--高亮显示
				 */
				highlightWithNoLog(new Integer[]{tid}, color, false, false,DateUtils.afterDate(new Date(), 1), "", user);
			}else if(magicName.equals(MagicConstants.MAGIC_NAMEPOST)){
				/**
				 * 照妖镜---查看匿名身份（tid传入pid）,返回传回帖子创建者的用户名，拼接了常量字符串在上层处理返回问题
				 */
				post=bbsPostMng.findById(tid);
				bbsUserMng.updatePoint(userId, null, null, magicName, 1, 1);
				String returnString = MagicConstants.MAGIC_NAMEPOST_SUCCESS + post.getCreater().getUsername();
				if (logger.isDebugEnabled()) {
					logger.debug("useMagic(HttpServletRequest, Integer, Integer, String, Integer, String, String, Integer) - end"); //$NON-NLS-1$
				}
				return returnString;
			}else if(magicName.equals(MagicConstants.MAGIC_ANONYMOUSPOST)){
				/**
				 * 匿名卡---隐藏自己信息
				 */
				post=bbsPostMng.findById(tid);
				post.setAnonymous(true);
			}else if(magicName.equals(MagicConstants.MAGIC_REPENT)){
				/**
				 * 悔悟卡--删除自己的主题
				 */
				deleteById(tid);
			}else if(magicName.equals(MagicConstants.MAGIC_SHOWIP)){
				/**
				 * 窥视卡---查看用户ip
				 */
				postCreater=bbsUserMng.findById(postCreaterId);
				if(postCreater!=null){
					//更新道具使用
					bbsUserMng.updatePoint(userId, null, null, magicName, 1, 1);
					String returnString = postCreater.getUsername() + MagicConstants.MAGIC_SHOWIP_SUCCESS + postCreater.getLastLoginIp();
					if (logger.isDebugEnabled()) {
						logger.debug("useMagic(HttpServletRequest, Integer, Integer, String, Integer, String, String, Integer) - end"); //$NON-NLS-1$
					}
					return returnString;
				}else{
					if (logger.isDebugEnabled()) {
						logger.debug("useMagic(HttpServletRequest, Integer, Integer, String, Integer, String, String, Integer) - end"); //$NON-NLS-1$
					}
					return MagicConstants.MAGIC_FIND_USER_ERROR;
				}
			}else if(magicName.equals(MagicConstants.MAGIC_CHECKONLINE)){
				/**
				 * 雷达卡---查看用户是否在线
				 */
				postCreater=bbsUserMng.findById(postCreaterId);
				if(postCreater!=null){
					bbsUserMng.updatePoint(userId, null, null, magicName, 1, 1);
					if(OnlineCountListener.isExistInSessions(postCreater.getUsername())){
						String returnString = MagicConstants.MAGIC_CHECKONLINE_ONLINE + postCreater.getUsername();
						if (logger.isDebugEnabled()) {
							logger.debug("useMagic(HttpServletRequest, Integer, Integer, String, Integer, String, String, Integer) - end"); //$NON-NLS-1$
						}
						return returnString;
					}else{
						String returnString = MagicConstants.MAGIC_CHECKONLINE_OFFLINE + postCreater.getUsername();
						if (logger.isDebugEnabled()) {
							logger.debug("useMagic(HttpServletRequest, Integer, Integer, String, Integer, String, String, Integer) - end"); //$NON-NLS-1$
						}
						return returnString;
					}
				}
				else{
					if (logger.isDebugEnabled()) {
						logger.debug("useMagic(HttpServletRequest, Integer, Integer, String, Integer, String, String, Integer) - end"); //$NON-NLS-1$
					}
					return MagicConstants.MAGIC_FIND_USER_ERROR;
				}
			}else if(magicName.equals(MagicConstants.MAGIC_MONEY)){
				/**
				 * 金钱卡
				 */
				BbsCommonMagic commomMagic=commomMagicMng.findByIdentifier(magicName);
				int price=commomMagic.getPrice();
				Integer money=generateRandom(1,price*2);
				String str="";
				MagicMessage magicMessage = MagicMessage.create(request);
				if(commomMagic.getCredit()==1){
					//积分
					str = magicMessage.getMessage("cmsUser.point");
					bbsUserMng.updatePoint(userId, money, null, magicName, 1, 1);
				}else if(commomMagic.getCredit()==2){
					//威望
					str = magicMessage.getMessage("cmsUser.prestige");
					bbsUserMng.updatePoint(userId, null, money, magicName, 1, 1);
				}
				String returnString = str + MagicConstants.MAGIC_MONEY_SUCCESS + money;
				if (logger.isDebugEnabled()) {
					logger.debug("useMagic(HttpServletRequest, Integer, Integer, String, Integer, String, String, Integer) - end"); //$NON-NLS-1$
				}
				return returnString;
			}
			//未返回的道具（减少道具数量）公用
			bbsUserMng.updatePoint(userId, null, null, magicName, 1, 1);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("useMagic(HttpServletRequest, Integer, Integer, String, Integer, String, String, Integer) - end"); //$NON-NLS-1$
		}
		return MagicConstants.MAGIC_OPEN_SUCCESS;
	}

	private BbsTopic createTopic(Integer category) {
		if (logger.isDebugEnabled()) {
			logger.debug("createTopic(Integer) - start"); //$NON-NLS-1$
		}

		if (category != null && category == TOPIC_VOTE) {
			BbsVoteTopic topic = new BbsVoteTopic();

			if (logger.isDebugEnabled()) {
				logger.debug("createTopic(Integer) - end"); //$NON-NLS-1$
			}
			return topic;
		}
		BbsTopic returnBbsTopic = new BbsTopic();
		if (logger.isDebugEnabled()) {
			logger.debug("createTopic(Integer) - end"); //$NON-NLS-1$
		}
		return returnBbsTopic;
	}

	private void handleVoteItem(BbsTopic topic, String[] name) {
		if (logger.isDebugEnabled()) {
			logger.debug("handleVoteItem(BbsTopic, String[]) - start"); //$NON-NLS-1$
		}

		if (name != null && topic.getCategory() == TOPIC_VOTE) {
			for(String s : name){
				BbsVoteItem bean = new BbsVoteItem();
				bean.init();
				bean.setName(s);
				bean.setTopic((BbsVoteTopic)topic);
				bbsVoteItemMng.save(bean);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("handleVoteItem(BbsTopic, String[]) - end"); //$NON-NLS-1$
		}
	}
	/**
	 *  获取某个范围内的随机数
	 * @param a
	 * @param b
	 * @return
	 */
	private  int generateRandom(int a, int b) {
		if (logger.isDebugEnabled()) {
			logger.debug("generateRandom(int, int) - start"); //$NON-NLS-1$
		}

	        int temp = 0;
	            if (a > b) {
	                temp = new Random().nextInt(a - b);
			int returnint = temp + b;
			if (logger.isDebugEnabled()) {
				logger.debug("generateRandom(int, int) - end"); //$NON-NLS-1$
			}
	                return returnint;
	            } else {
	                temp = new Random().nextInt(b - a);
			int returnint = temp + a;
			if (logger.isDebugEnabled()) {
				logger.debug("generateRandom(int, int) - end"); //$NON-NLS-1$
			}
	                return returnint;
	            }
	    }

	private BbsPostMng bbsPostMng;
	private BbsForumMng bbsForumMng;
	private BbsOperationMng bbsOperationMng;
	private CmsSiteMng siteMng;
	private BbsUserMng bbsUserMng;
	private BbsConfigEhCache bbsConfigEhCache;
	private BbsPostTypeMng bbsPostTypeMng;
	private BbsVoteItemMng bbsVoteItemMng;
	@Autowired
	private BbsMagicConfigMng magicConfigMng;
	@Autowired
	private BbsCommonMagicMng commomMagicMng;

	private BbsTopicDao dao;

	@Autowired
	public void setDao(BbsTopicDao dao) {
		this.dao = dao;
	}

	@Autowired
	public void setBbsPostMng(BbsPostMng bbsPostMng) {
		this.bbsPostMng = bbsPostMng;
	}

	@Autowired
	public void setBbsForumMng(BbsForumMng bbsForumMng) {
		this.bbsForumMng = bbsForumMng;
	}

	@Autowired
	public void setBbsOperationMng(BbsOperationMng bbsOperationMng) {
		this.bbsOperationMng = bbsOperationMng;
	}

	@Autowired
	public void setSiteMng(CmsSiteMng siteMng) {
		this.siteMng = siteMng;
	}

	@Autowired
	public void setBbsUserMng(BbsUserMng bbsUserMng) {
		this.bbsUserMng = bbsUserMng;
	}

	@Autowired
	public void setBbsConfigEhCache(BbsConfigEhCache bbsConfigEhCache) {
		this.bbsConfigEhCache = bbsConfigEhCache;
	}

	@Autowired
	public void setBbsPostTypeMng(BbsPostTypeMng bbsPostTypeMng) {
		this.bbsPostTypeMng = bbsPostTypeMng;
	}

	@Autowired
	public void setBbsVoteItemMng(BbsVoteItemMng bbsVoteItemMng) {
		this.bbsVoteItemMng = bbsVoteItemMng;
	}
}
