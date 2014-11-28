package com.jeecms.bbs.dao;

import org.apache.log4j.Logger;

import com.jeecms.bbs.entity.BbsLoginLog;
import com.jeecms.common.hibernate3.Updater;
import com.jeecms.common.page.Pagination;

public interface BbsLoginLogDao {
	
	public Pagination getPage(int pageNo, int pageSize);

	public BbsLoginLog findById(Integer id);

	public BbsLoginLog save(BbsLoginLog bean);

	public BbsLoginLog updateByUpdater(Updater<BbsLoginLog> bean);

	public BbsLoginLog deleteById(Integer id);
	
}