package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.jeecms.bbs.dao.BbsMessageDao;
import com.jeecms.bbs.entity.BbsMessage;
import com.jeecms.bbs.entity.BbsMessageReply;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsMessageMng;
import com.jeecms.bbs.manager.BbsMessageReplyMng;
import com.jeecms.common.hibernate3.Updater;
import com.jeecms.common.page.Pagination;

@Service
@Transactional
public class BbsMessageMngImpl implements BbsMessageMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsMessageMngImpl.class);

	@Transactional(readOnly = true)
	public BbsMessage findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsMessage entity = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsMessage save(BbsMessage bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsMessage) - start"); //$NON-NLS-1$
		}

		dao.save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsMessage) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsMessage update(BbsMessage bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsMessage) - start"); //$NON-NLS-1$
		}

		Updater<BbsMessage> updater = new Updater<BbsMessage>(bean);
		bean = dao.updateByUpdater(updater);

		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsMessage) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsMessage deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsMessage bean = dao.deleteById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsMessage[] deleteByIds(Integer[] ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - start"); //$NON-NLS-1$
		}

		BbsMessage[] beans = new BbsMessage[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}

	private BbsMessage getSendRelation(Integer userId, Integer senderId, Integer receiverId,Integer typeId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getSendRelation(Integer, Integer, Integer, Integer) - start"); //$NON-NLS-1$
		}

		BbsMessage returnBbsMessage = dao.getSendRelation(userId, senderId, receiverId, typeId);
		if (logger.isDebugEnabled()) {
			logger.debug("getSendRelation(Integer, Integer, Integer, Integer) - end"); //$NON-NLS-1$
		}
		return returnBbsMessage;
	}

	public void sendMsg(BbsUser sender, BbsUser receiver, BbsMessage sMsg) {
		if (logger.isDebugEnabled()) {
			logger.debug("sendMsg(BbsUser, BbsUser, BbsMessage) - start"); //$NON-NLS-1$
		}

		Integer senderId = sender.getId();
		Integer receiverId = receiver.getId();
		Integer typeId=sMsg.getMsgType();
		//处理发送端
		BbsMessage msg = getSendRelation(senderId, senderId, receiverId,typeId);
		BbsMessage rMsg = sMsg.putDataAndClone(sender, receiver);
		saveOrUpdate(msg, sMsg);
		//处理接收端
		msg = getSendRelation(receiverId, senderId, receiverId,typeId);
		saveOrUpdate(msg, rMsg);

		if (logger.isDebugEnabled()) {
			logger.debug("sendMsg(BbsUser, BbsUser, BbsMessage) - end"); //$NON-NLS-1$
		}
	}
	
	public Pagination getPageByUserId(Integer userId, Integer typeId,Integer pageNo, Integer pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPageByUserId(Integer, Integer, Integer, Integer) - start"); //$NON-NLS-1$
		}

		Pagination returnPagination = dao.getPageByUserId(userId, typeId, pageNo, pageSize);
		if (logger.isDebugEnabled()) {
			logger.debug("getPageByUserId(Integer, Integer, Integer, Integer) - end"); //$NON-NLS-1$
		}
		return returnPagination;
	}
	
	public boolean hasUnReadMessage(Integer userId, Integer typeId) {
		if (logger.isDebugEnabled()) {
			logger.debug("hasUnReadMessage(Integer, Integer) - start"); //$NON-NLS-1$
		}

		if(dao.getListByUserIdStatus(userId, typeId, false).size()>0){
			if (logger.isDebugEnabled()) {
				logger.debug("hasUnReadMessage(Integer, Integer) - end"); //$NON-NLS-1$
			}
			return true;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("hasUnReadMessage(Integer, Integer) - end"); //$NON-NLS-1$
		}
		return false;
	}
	public List getListUserIdStatus(Integer userId, Integer typeId,
			Boolean status) {
		if (logger.isDebugEnabled()) {
			logger.debug("getListUserIdStatus(Integer, Integer, Boolean) - start"); //$NON-NLS-1$
		}

		List returnList = dao.getListByUserIdStatus(userId, typeId, status);
		if (logger.isDebugEnabled()) {
			logger.debug("getListUserIdStatus(Integer, Integer, Boolean) - end"); //$NON-NLS-1$
		}
		return returnList;
	}
	
	private void saveOrUpdate(BbsMessage msg, BbsMessage bean){
		if (logger.isDebugEnabled()) {
			logger.debug("saveOrUpdate(BbsMessage, BbsMessage) - start"); //$NON-NLS-1$
		}

		//无论是更新还是新的留言或者回复都需要设置未读
		bean.setStatus(false);
		if (msg == null) {
			save(bean);
		}else{
			bean.setId(msg.getId());
			update(bean);
		}
		BbsMessageReply reply = bean.createReply();
		replyMng.save(reply);

		if (logger.isDebugEnabled()) {
			logger.debug("saveOrUpdate(BbsMessage, BbsMessage) - end"); //$NON-NLS-1$
		}
	}

	private BbsMessageDao dao;
	private BbsMessageReplyMng replyMng;

	@Autowired
	public void setDao(BbsMessageDao dao) {
		this.dao = dao;
	}
	@Autowired
	public void setReplyMng(BbsMessageReplyMng replyMng) {
		this.replyMng = replyMng;
	}
	
}