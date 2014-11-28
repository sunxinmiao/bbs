package com.jeecms.common.page;

import org.apache.log4j.Logger;

/**
 * 简单分页类
 * 
 * @author liufang
 * 
 */
public class SimplePage implements Paginable {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SimplePage.class);

	private static final long serialVersionUID = 1L;
	public static final int DEF_COUNT = 20;

	/**
	 * 检查页码 checkPageNo
	 * 
	 * @param pageNo
	 * @return if pageNo==null or pageNo<1 then return 1 else return pageNo
	 */
	public static int cpn(Integer pageNo) {
		if (logger.isDebugEnabled()) {
			logger.debug("cpn(Integer) - start"); //$NON-NLS-1$
		}

		int returnint = (pageNo == null || pageNo < 1) ? 1 : pageNo;
		if (logger.isDebugEnabled()) {
			logger.debug("cpn(Integer) - end"); //$NON-NLS-1$
		}
		return returnint;
	}

	public SimplePage() {
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
	public SimplePage(int pageNo, int pageSize, int totalCount) {
		setTotalCount(totalCount);
		setPageSize(pageSize);
		setPageNo(pageNo);
		adjustPageNo();
	}

	/**
	 * 调整页码，使不超过最大页数
	 */
	public void adjustPageNo() {
		if (logger.isDebugEnabled()) {
			logger.debug("adjustPageNo() - start"); //$NON-NLS-1$
		}

		if (pageNo == 1) {
			if (logger.isDebugEnabled()) {
				logger.debug("adjustPageNo() - end"); //$NON-NLS-1$
			}
			return;
		}
		int tp = getTotalPage();
		if (pageNo > tp) {
			pageNo = tp;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("adjustPageNo() - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 获得页码
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * 每页几条数据
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 总共几条数据
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * 总共几页
	 */
	public int getTotalPage() {
		if (logger.isDebugEnabled()) {
			logger.debug("getTotalPage() - start"); //$NON-NLS-1$
		}

		int totalPage = totalCount / pageSize;
		if (totalPage == 0 || totalCount % pageSize != 0) {
			totalPage++;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getTotalPage() - end"); //$NON-NLS-1$
		}
		return totalPage;
	}

	/**
	 * 是否第一页
	 */
	public boolean isFirstPage() {
		if (logger.isDebugEnabled()) {
			logger.debug("isFirstPage() - start"); //$NON-NLS-1$
		}

		boolean returnboolean = pageNo <= 1;
		if (logger.isDebugEnabled()) {
			logger.debug("isFirstPage() - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/**
	 * 是否最后一页
	 */
	public boolean isLastPage() {
		if (logger.isDebugEnabled()) {
			logger.debug("isLastPage() - start"); //$NON-NLS-1$
		}

		boolean returnboolean = pageNo >= getTotalPage();
		if (logger.isDebugEnabled()) {
			logger.debug("isLastPage() - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/**
	 * 下一页页码
	 */
	public int getNextPage() {
		if (logger.isDebugEnabled()) {
			logger.debug("getNextPage() - start"); //$NON-NLS-1$
		}

		if (isLastPage()) {
			if (logger.isDebugEnabled()) {
				logger.debug("getNextPage() - end"); //$NON-NLS-1$
			}
			return pageNo;
		} else {
			int returnint = pageNo + 1;
			if (logger.isDebugEnabled()) {
				logger.debug("getNextPage() - end"); //$NON-NLS-1$
			}
			return returnint;
		}
	}

	/**
	 * 上一页页码
	 */
	public int getPrePage() {
		if (logger.isDebugEnabled()) {
			logger.debug("getPrePage() - start"); //$NON-NLS-1$
		}

		if (isFirstPage()) {
			if (logger.isDebugEnabled()) {
				logger.debug("getPrePage() - end"); //$NON-NLS-1$
			}
			return pageNo;
		} else {
			int returnint = pageNo - 1;
			if (logger.isDebugEnabled()) {
				logger.debug("getPrePage() - end"); //$NON-NLS-1$
			}
			return returnint;
		}
	}

	protected int totalCount = 0;
	protected int pageSize = 20;
	protected int pageNo = 1;

	/**
	 * if totalCount<0 then totalCount=0
	 * 
	 * @param totalCount
	 */
	public void setTotalCount(int totalCount) {
		if (logger.isDebugEnabled()) {
			logger.debug("setTotalCount(int) - start"); //$NON-NLS-1$
		}

		if (totalCount < 0) {
			this.totalCount = 0;
		} else {
			this.totalCount = totalCount;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("setTotalCount(int) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * if pageSize< 1 then pageSize=DEF_COUNT
	 * 
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("setPageSize(int) - start"); //$NON-NLS-1$
		}

		if (pageSize < 1) {
			this.pageSize = DEF_COUNT;
		} else {
			this.pageSize = pageSize;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("setPageSize(int) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * if pageNo < 1 then pageNo=1
	 * 
	 * @param pageNo
	 */
	public void setPageNo(int pageNo) {
		if (logger.isDebugEnabled()) {
			logger.debug("setPageNo(int) - start"); //$NON-NLS-1$
		}

		if (pageNo < 1) {
			this.pageNo = 1;
		} else {
			this.pageNo = pageNo;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("setPageNo(int) - end"); //$NON-NLS-1$
		}
	}
}
