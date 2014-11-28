package com.jeecms.bbs.manager;

import org.apache.log4j.Logger;

import com.jeecms.bbs.entity.BbsCreditExchange;

public interface BbsCreditExchangeMng {
	public BbsCreditExchange findById(Integer id);

	public BbsCreditExchange update(BbsCreditExchange bean);
}