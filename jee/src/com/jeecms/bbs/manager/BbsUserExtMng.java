package com.jeecms.bbs.manager;

import org.apache.log4j.Logger;

import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserExt;

public interface BbsUserExtMng {
	public BbsUserExt save(BbsUserExt ext, BbsUser user);

	public BbsUserExt update(BbsUserExt ext, BbsUser user);
}