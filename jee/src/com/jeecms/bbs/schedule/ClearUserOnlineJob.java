package com.jeecms.bbs.schedule;

import org.apache.log4j.Logger;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;

import com.jeecms.bbs.entity.BbsUserOnline;
import com.jeecms.bbs.manager.BbsUserOnlineMng;

public class ClearUserOnlineJob {
	private static final Logger logger = Logger.getLogger(ClearUserOnlineJob.class);

	/**
	 * 日在线时长统计清零
	 */
	public void executeByDay() {
		if (logger.isDebugEnabled()) {
			logger.debug("executeByDay() - start"); //$NON-NLS-1$
		}

		List<BbsUserOnline> onlines = manager.getList();
		for (BbsUserOnline online : onlines) {
			online.setOnlineDay(0d);
			manager.update(online);
		}
		logger.info("clear userOnline by day BbsUserOnline success!");
	}
	/**
	 * 周在线时长统计清零
	 */
	public void executeByWeek() {
		if (logger.isDebugEnabled()) {
			logger.debug("executeByWeek() - start"); //$NON-NLS-1$
		}

		List<BbsUserOnline> onlines = manager.getList();
		for (BbsUserOnline online : onlines) {
			online.setOnlineWeek(0d);
			manager.update(online);
		}
		logger.info("clear userOnline by week BbsUserOnline success!");
	}
	/**
	 * 月在线时长统计清零
	 */
	public void executeByMonth() {
		if (logger.isDebugEnabled()) {
			logger.debug("executeByMonth() - start"); //$NON-NLS-1$
		}

		List<BbsUserOnline> onlines = manager.getList();
		for (BbsUserOnline online : onlines) {
			online.setOnlineMonth(0d);
			manager.update(online);
		}
		logger.info("clear userOnline by month BbsUserOnline success!");
	}
	/**
	 * 年在线时长统计清零
	 */
	public void executeByYear() {
		if (logger.isDebugEnabled()) {
			logger.debug("executeByYear() - start"); //$NON-NLS-1$
		}

		List<BbsUserOnline> onlines = manager.getList();
		for (BbsUserOnline online : onlines) {
			online.setOnlineYear(0d);
			manager.update(online);
		}
		logger.info("clear userOnline by year BbsUserOnline success!");
	}

	@Autowired
	private BbsUserOnlineMng manager;

}