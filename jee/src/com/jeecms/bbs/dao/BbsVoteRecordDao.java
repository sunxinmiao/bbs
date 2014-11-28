package com.jeecms.bbs.dao;

import org.apache.log4j.Logger;

import com.jeecms.bbs.entity.BbsVoteRecord;




public interface BbsVoteRecordDao {
	public BbsVoteRecord findRecord(Integer userId, Integer topicId);
	
	public BbsVoteRecord save(BbsVoteRecord bean);
}
