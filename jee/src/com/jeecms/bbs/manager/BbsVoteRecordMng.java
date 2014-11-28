package com.jeecms.bbs.manager;

import org.apache.log4j.Logger;


import com.jeecms.bbs.entity.BbsVoteRecord;


public interface BbsVoteRecordMng {
	public BbsVoteRecord findRecord(Integer userId, Integer topicId);
	
	public BbsVoteRecord save(BbsVoteRecord bean);
}
