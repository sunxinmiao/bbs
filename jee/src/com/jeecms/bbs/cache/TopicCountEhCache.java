package com.jeecms.bbs.cache;

import org.apache.log4j.Logger;

public interface TopicCountEhCache {

	public Long getViewCount(Integer topicId);

	public Long setViewCount(Integer topicId);

	public boolean getLastReply(Integer userId, long time);

}
