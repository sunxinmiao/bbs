package com.jeecms.bbs.manager;

import org.apache.log4j.Logger;

import java.util.List;

import com.jeecms.bbs.entity.BbsUserOnline;
import com.jeecms.common.page.Pagination;

public interface BbsUserOnlineMng {

	public List<BbsUserOnline> getList();

	public Pagination getPage(int pageNo, int pageSize);

	public BbsUserOnline findById(Integer id);

	public BbsUserOnline save(BbsUserOnline bean);

	public BbsUserOnline update(BbsUserOnline bean);

	public BbsUserOnline deleteById(Integer id);

	public BbsUserOnline[] deleteByIds(Integer[] ids);
}