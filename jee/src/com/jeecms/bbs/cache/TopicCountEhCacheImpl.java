package com.jeecms.bbs.cache;

import org.apache.log4j.Logger;

import java.util.List;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.manager.BbsTopicMng;

@Service
public class TopicCountEhCacheImpl implements TopicCountEhCache, DisposableBean {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TopicCountEhCacheImpl.class);

	public Long getViewCount(Integer topicId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getViewCount(Integer) - start"); //$NON-NLS-1$
		}

		Element e = cache.get(topicId);
		if (e != null) {
			Long returnLong = (Long) e.getValue();
			if (logger.isDebugEnabled()) {
				logger.debug("getViewCount(Integer) - end"); //$NON-NLS-1$
			}
			return returnLong;
		} else {
			BbsTopic topic = bbsTopicMng.findById(topicId);
			Long viewCount = 0L;
			if (topic.getViewCount() != null) {
				viewCount = topic.getViewCount();
			}
			cache.put(new Element(topicId, viewCount));

			if (logger.isDebugEnabled()) {
				logger.debug("getViewCount(Integer) - end"); //$NON-NLS-1$
			}
			return viewCount;
		}
	}

	public Long setViewCount(Integer topicId) {
		if (logger.isDebugEnabled()) {
			logger.debug("setViewCount(Integer) - start"); //$NON-NLS-1$
		}

		Long viewCount = 0L;
		Element e = cache.get(topicId);
		if (e != null) {
			viewCount = (Long) e.getValue() + 1;
		} else {
			BbsTopic topic = bbsTopicMng.findById(topicId);
			if (topic.getViewCount() == null) {
				viewCount = 1L;
			} else {
				viewCount = topic.getViewCount() + 1;
			}
		}
		cache.put(new Element(topicId, viewCount));
		refreshToDB(topicId);

		if (logger.isDebugEnabled()) {
			logger.debug("setViewCount(Integer) - end"); //$NON-NLS-1$
		}
		return viewCount;
	}

	private void refreshToDB(Integer topicId) {
		if (logger.isDebugEnabled()) {
			logger.debug("refreshToDB(Integer) - start"); //$NON-NLS-1$
		}

		long time = System.currentTimeMillis();
		if (time > refreshTime + interval) {
			refreshTime = time;
			BbsTopic topic = bbsTopicMng.findById(topicId);
			Element e = cache.get(topicId);
			Long viewCount = (Long) e.getValue();
			topic.setViewCount(viewCount);
			bbsTopicMng.update(topic);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("refreshToDB(Integer) - end"); //$NON-NLS-1$
		}
	}

	public boolean getLastReply(Integer userId, long time) {
		if (logger.isDebugEnabled()) {
			logger.debug("getLastReply(Integer, long) - start"); //$NON-NLS-1$
		}

		Element e = replycache.get(userId);
		if (e != null) {
			long reply = (Long) e.getValue();
			long times = System.currentTimeMillis() - reply;
			if (times / 1000 > time) {
				replycache.put(new Element(userId, System.currentTimeMillis()));

				if (logger.isDebugEnabled()) {
					logger.debug("getLastReply(Integer, long) - end"); //$NON-NLS-1$
				}
				return true;
			}
		} else {
			replycache.put(new Element(userId, System.currentTimeMillis()));

			if (logger.isDebugEnabled()) {
				logger.debug("getLastReply(Integer, long) - end"); //$NON-NLS-1$
			}
			return true;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getLastReply(Integer, long) - end"); //$NON-NLS-1$
		}
		return false;
	}

	/**
	 * 销毁BEAN时，缓存入库。
	 */
	@SuppressWarnings("unchecked")
	public void destroy() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("destroy() - start"); //$NON-NLS-1$
		}

		List<Integer> keys = cache.getKeys();
		for (Integer topicId : keys) {
			refreshToDB(topicId);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("destroy() - end"); //$NON-NLS-1$
		}
	}

	// 间隔时间
	private int interval = 60 * 1000; // 1个小时
	// 最后刷新时间
	private long refreshTime = System.currentTimeMillis();
	private BbsTopicMng bbsTopicMng;
	private Ehcache cache;
	private Ehcache replycache;

	@Autowired
	public void setCache(@Qualifier("topicCount") Ehcache cache) {
		this.cache = cache;
	}

	@Autowired
	public void setReplycache(@Qualifier("lastReply") Ehcache cache) {
		this.replycache = cache;
	}

	@Autowired
	public void setBbsTopicMng(BbsTopicMng bbsTopicMng) {
		this.bbsTopicMng = bbsTopicMng;
	}
}
