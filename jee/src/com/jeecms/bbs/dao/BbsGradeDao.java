package com.jeecms.bbs.dao;

import org.apache.log4j.Logger;

import com.jeecms.bbs.entity.BbsGrade;
import com.jeecms.common.hibernate3.Updater;
import com.jeecms.common.page.Pagination;

public interface BbsGradeDao {
	public Pagination getPage(int pageNo, int pageSize);

	public BbsGrade findById(Integer id);

	public BbsGrade save(BbsGrade bean);

	public BbsGrade updateByUpdater(Updater<BbsGrade> updater);

	public BbsGrade deleteById(Integer id);
}