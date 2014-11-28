package com.jeecms.bbs.manager;

import org.apache.log4j.Logger;

import com.jeecms.bbs.entity.BbsMagicConfig;

public interface BbsMagicConfigMng {

	public BbsMagicConfig findById(Integer id);

	public BbsMagicConfig update(BbsMagicConfig bean);

}