package com.jeecms.common.page;

import org.apache.log4j.Logger;

import java.util.List;

/**
 * 列表分页。包含list属性。
 * 
 * @author liufang
 * 
 */
@SuppressWarnings("serial")
public class Pagination extends SimplePage implements java.io.Serializable,
		Paginable {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Pagination.class);

	public Pagination() {
	}

	/**
	 * 构造器
	 * 
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页几条数据
	 * @param totalCount
	 *            总共几条数据
	 */
	public Pagination(int pageNo, int pageSize, int totalCount) {
		super(pageNo, pageSize, totalCount);
	}

	/**
	 * 构造器
	 * 
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页几条数据
	 * @param totalCount
	 *            总共几条数据
	 * @param list
	 *            分页内容
	 */
	public Pagination(int pageNo, int pageSize, int totalCount, List<?> list) {
		super(pageNo, pageSize, totalCount);
		this.list = list;
	}

	/**
	 * 第一条数据位置
	 * 
	 * @return
	 */
	public int getFirstResult() {
		if (logger.isDebugEnabled()) {
			logger.debug("getFirstResult() - start"); //$NON-NLS-1$
		}

		int returnint = (pageNo - 1) * pageSize;
		if (logger.isDebugEnabled()) {
			logger.debug("getFirstResult() - end"); //$NON-NLS-1$
		}
		return returnint;
	}

	/**
	 * 当前页的数据
	 */
	private List<?> list;

	/**
	 * 获得分页内容
	 * 
	 * @return
	 */
	public List<?> getList() {
		return list;
	}

	/**
	 * 设置分页内容
	 * 
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void setList(List list) {
		this.list = list;
	}
}
