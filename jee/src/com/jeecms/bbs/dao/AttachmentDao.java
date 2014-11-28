package com.jeecms.bbs.dao;

import org.apache.log4j.Logger;

import com.jeecms.common.hibernate3.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.entity.Attachment;

public interface AttachmentDao {
	public Pagination getPage(int pageNo, int pageSize);

	public Attachment findById(Integer id);

	public Attachment save(Attachment bean);

	public Attachment updateByUpdater(Updater<Attachment> updater);

	public Attachment deleteById(Integer id);
}