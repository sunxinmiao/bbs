package com.jeecms.bbs.entity;

import org.apache.log4j.Logger;

import java.util.Date;

import com.jeecms.bbs.entity.base.BaseBbsMessage;



public class BbsMessage extends BaseBbsMessage implements Cloneable{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsMessage.class);

	private static final long serialVersionUID = 1L;
	
	public BbsMessage clone(){
		if (logger.isDebugEnabled()) {
			logger.debug("clone() - start"); //$NON-NLS-1$
		}

		BbsMessage clone;
		try {
			clone = (BbsMessage) super.clone();
		} catch (CloneNotSupportedException e) {
			logger.error("clone()", e); //$NON-NLS-1$

			throw new RuntimeException("Clone not support?");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("clone() - end"); //$NON-NLS-1$
		}
		return clone;
	}
	
	public BbsMessage putDataAndClone(BbsUser sender, BbsUser receiver){
		if (logger.isDebugEnabled()) {
			logger.debug("putDataAndClone(BbsUser, BbsUser) - start"); //$NON-NLS-1$
		}

		Date now = new Date();
		setUser(sender);
		setSender(sender);
		setReceiver(receiver);
		setCreateTime(now);
		init();
		BbsMessage clone = clone();
		clone.setUser(receiver);

		if (logger.isDebugEnabled()) {
			logger.debug("putDataAndClone(BbsUser, BbsUser) - end"); //$NON-NLS-1$
		}
		return clone;
	}
	
	public BbsMessageReply createReply(){
		if (logger.isDebugEnabled()) {
			logger.debug("createReply() - start"); //$NON-NLS-1$
		}

		BbsMessageReply bean = new BbsMessageReply();
		bean.setContent(getContent());
		bean.setCreateTime(getCreateTime());
		bean.setMessage(this);
		bean.setSender(getSender());
		bean.setReceiver(getReceiver());

		if (logger.isDebugEnabled()) {
			logger.debug("createReply() - end"); //$NON-NLS-1$
		}
		return bean;
	}
	
	public void init(){
		if (logger.isDebugEnabled()) {
			logger.debug("init() - start"); //$NON-NLS-1$
		}

		if(getSys()==null){
			setSys(false);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("init() - end"); //$NON-NLS-1$
		}
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsMessage () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsMessage (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsMessage (
		java.lang.Integer id,
		com.jeecms.bbs.entity.BbsUser user,
		com.jeecms.bbs.entity.BbsUser receiver,
		java.util.Date createTime,
		java.lang.Boolean sys) {

		super (
			id,
			user,
			receiver,
			createTime,
			sys);
	}

/*[CONSTRUCTOR MARKER END]*/


}