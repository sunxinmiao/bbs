package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jeecms.bbs.cache.BbsConfigEhCache;
import com.jeecms.bbs.dao.BbsPostDao;
import com.jeecms.bbs.entity.Attachment;
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsPost;
import com.jeecms.bbs.entity.BbsPostText;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.AttachmentMng;
import com.jeecms.bbs.manager.BbsConfigMng;
import com.jeecms.bbs.manager.BbsOperationMng;
import com.jeecms.bbs.manager.BbsPostMng;
import com.jeecms.bbs.manager.BbsPostTypeMng;
import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.StrUtils;
import com.jeecms.common.hibernate3.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.upload.FileRepository;
import com.jeecms.core.manager.CmsSiteMng;

@Service
@Transactional
public class BbsPostMngImpl implements BbsPostMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsPostMngImpl.class);

	public BbsPost shield(Integer id, String reason, BbsUser operator,Short status) {
		if (logger.isDebugEnabled()) {
			logger.debug("shield(Integer, String, BbsUser, Short) - start"); //$NON-NLS-1$
		}

		BbsPost post = dao.findById(id);
		post.setStatus(status);
		if(status==-1){
			bbsOperationMng.saveOpt(post.getSite(), operator, "屏蔽", reason, post);
		}else if(status==0){
			bbsOperationMng.saveOpt(post.getSite(), operator, "取消屏蔽", reason, post);
		}
		

		if (logger.isDebugEnabled()) {
			logger.debug("shield(Integer, String, BbsUser, Short) - end"); //$NON-NLS-1$
		}
		return post;
	}

	public BbsPost updatePost(Integer id, String title, String content,
			BbsUser editor, String ip) {
		if (logger.isDebugEnabled()) {
			logger.debug("updatePost(Integer, String, String, BbsUser, String) - start"); //$NON-NLS-1$
		}

		// 修改BbsPost
		BbsPost post = dao.findById(id);
		post.setEditCount(post.getEditCount() + 1);
		post.setEditerIp(ip);
		post.setEditTime(new Timestamp(System.currentTimeMillis()));
		post.setEditer(editor);
		
		if (findHidden(content)) {
			post.setHidden(true);
		} else {
			post.setHidden(false);
		}
		if (post.isFirst()) {
			post.getTopic().setTitle(title);
		}
		// 修改BbsPostText
		BbsPostText text = post.getPostText();
		text.setTitle(title);
		text.setContent(content);
		
		// 写入操作日志
		bbsOperationMng.saveOpt(post.getSite(), editor, "编辑", null, post);

		if (logger.isDebugEnabled()) {
			logger.debug("updatePost(Integer, String, String, BbsUser, String) - end"); //$NON-NLS-1$
		}
		return post;
	}
	
	public BbsPost updatePost(Integer id, String title, String content,
			BbsUser editor, String ip, List<MultipartFile> file,
			List<String> code) {
		if (logger.isDebugEnabled()) {
			logger.debug("updatePost(Integer, String, String, BbsUser, String, List<MultipartFile>, List<String>) - start"); //$NON-NLS-1$
		}

		// 修改BbsPost
		BbsPost post = dao.findById(id);
		post.setEditCount(post.getEditCount() + 1);
		post.setEditerIp(ip);
		post.setEditTime(new Timestamp(System.currentTimeMillis()));
		post.setEditer(editor);
		if (file != null && file.size() > 0) {
			post.setAffix(true);
		}
		if (findHidden(content)) {
			post.setHidden(true);
		} else {
			post.setHidden(false);
		}
		if (post.isFirst()) {
			post.getTopic().setTitle(title);
		}
		
		BbsPostText text = post.getPostText();
		
		text.setTitle(title);
		
		if (file != null && file.size() > 0) {
			content = uploadImg(content, file, code, 1, post);
		}
		text.setContent(content);
		// 写入操作日志
		bbsOperationMng.saveOpt(post.getSite(), editor, "编辑", null, post);

		if (logger.isDebugEnabled()) {
			logger.debug("updatePost(Integer, String, String, BbsUser, String, List<MultipartFile>, List<String>) - end"); //$NON-NLS-1$
		}
		return post;
	}

	public BbsPost post(Integer userId, Integer siteId, Integer topicId,Integer postTypeId,
			String title, String content, String ip, List<MultipartFile> file,
			List<String> code) {
		if (logger.isDebugEnabled()) {
			logger.debug("post(Integer, Integer, Integer, Integer, String, String, String, List<MultipartFile>, List<String>) - start"); //$NON-NLS-1$
		}

		BbsPostText text = new BbsPostText();
		BbsPost post = new BbsPost();
		BbsTopic topic = bbsTopicMng.findById(topicId);
		post.setSite(siteMng.findById(siteId));
		post.setConfig(bbsConfigMng.findById(siteId));
		post.setTopic(topic);
		if(postTypeId!=null){
			post.setPostType(bbsPostTypeMng.findById(postTypeId));
		}
		post.setCreater(bbsUserMng.findById(userId));
		if (file != null && file.size() > 0) {
			post.setAffix(true);
		} else {
			post.setAffix(false);
		}
		if (findHidden(content)) {
			post.setHidden(true);
		} else {
			post.setHidden(false);
		}
		initBbsPost(post, ip);
		post = dao.save(post);
		if (file != null && file.size() > 0) {
			content = uploadImg(content, file, code, siteId, post);
		}
		text.setTitle(title);
		text.setContent(content);
		post.setPostText(text);
		post.setIndexCount(getIndexCount(topicId));
		if (post.getIndexCount() == 1) {
			topic.setHaveReply(",");
		} else {
			if (topic.getHaveReply() == null) {
				topic.setHaveReply("," + userId + ",");
			} else {
				if (topic.getHaveReply().indexOf("," + userId + ",") == -1) {
					topic.setHaveReply(topic.getHaveReply() + userId + ",");
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("post(Integer, Integer, Integer, Integer, String, String, String, List<MultipartFile>, List<String>) - end"); //$NON-NLS-1$
		}
		return post;
	}

	public List<BbsPost> getPostByTopic(Integer topicId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPostByTopic(Integer) - start"); //$NON-NLS-1$
		}

		List<BbsPost> returnList = dao.getPostByTopic(topicId);
		if (logger.isDebugEnabled()) {
			logger.debug("getPostByTopic(Integer) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	public BbsPost reply(Integer userId, Integer siteId, Integer topicId,Integer postTypeId,
			String title, String content, String ip, List<MultipartFile> file,
			List<String> code) {
		if (logger.isDebugEnabled()) {
			logger.debug("reply(Integer, Integer, Integer, Integer, String, String, String, List<MultipartFile>, List<String>) - start"); //$NON-NLS-1$
		}

		BbsPost post = post(userId, siteId, topicId,postTypeId, title, content, ip, file,
				code);
		if (bbsUserMng.findById(userId).getModerator()) {
			post.getTopic().setModeratorReply(true);
		}
		updatePostCount(post, bbsUserMng.findById(userId));
		bbsConfigEhCache.setBbsConfigCache(1, 0, 1, 0, null, siteId);

		if (logger.isDebugEnabled()) {
			logger.debug("reply(Integer, Integer, Integer, Integer, String, String, String, List<MultipartFile>, List<String>) - end"); //$NON-NLS-1$
		}
		return post;
	}

	public String uploadImg(String content, List<MultipartFile> file,
			List<String> code, Integer siteId, BbsPost post) {
		if (logger.isDebugEnabled()) {
			logger.debug("uploadImg(String, List<MultipartFile>, List<String>, Integer, BbsPost) - start"); //$NON-NLS-1$
		}

		List<String> list = findImgUrl(content);
		for (int i = 0; i < code.size(); i++) {
			if (list.contains(code.get(i))) {
				String origName = file.get(i).getOriginalFilename();
				String ext = FilenameUtils.getExtension(origName).toLowerCase(
						Locale.ENGLISH);
				String filepath;
				try {
					filepath = fileRepository.storeByExt(siteMng.findById(
							siteId).getUploadPath(), ext, file.get(i));
					Attachment att = addAttachment(origName, filepath, post,
							file.get(i).getSize());
					int j = i + 1;
					content = StrUtils.replace(content, "[localimg]" + j
							+ "[/localimg]", "[attachment]" + att.getId()
							+ "[/attachment]");
				} catch (IOException e) {
					logger.error("uploadImg(String, List<MultipartFile>, List<String>, Integer, BbsPost)", e); //$NON-NLS-1$

					e.printStackTrace();
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("uploadImg(String, List<MultipartFile>, List<String>, Integer, BbsPost) - end"); //$NON-NLS-1$
		}
		return content;
	}

	private List<String> findImgUrl(String content) {
		if (logger.isDebugEnabled()) {
			logger.debug("findImgUrl(String) - start"); //$NON-NLS-1$
		}

		String ems = "\\[localimg]([0-9]+)\\[/localimg]";
		Matcher matcher = Pattern.compile(ems).matcher(content);
		List<String> list = new ArrayList<String>();
		while (matcher.find()) {
			String url = matcher.group(1);
			list.add(url);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("findImgUrl(String) - end"); //$NON-NLS-1$
		}
		return list;
	}

	private boolean findHidden(String content) {
		if (logger.isDebugEnabled()) {
			logger.debug("findHidden(String) - start"); //$NON-NLS-1$
		}

		String ems = "\\[hide\\]([\\s\\S]*)\\[/hide\\]";
		Matcher matcher = Pattern.compile(ems).matcher(content);
		while (matcher.find()) {
			if (logger.isDebugEnabled()) {
				logger.debug("findHidden(String) - end"); //$NON-NLS-1$
			}
			return true;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("findHidden(String) - end"); //$NON-NLS-1$
		}
		return false;
	}

	@Transactional(readOnly = true)
	public Pagination getForTag(Integer siteId, Integer topicId,
			Integer userId, int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getForTag(Integer, Integer, Integer, int, int) - start"); //$NON-NLS-1$
		}

		Pagination returnPagination = dao.getForTag(siteId, topicId, userId, pageNo, pageSize);
		if (logger.isDebugEnabled()) {
			logger.debug("getForTag(Integer, Integer, Integer, int, int) - end"); //$NON-NLS-1$
		}
		return returnPagination;
	}

	public BbsPost getLastPost(Integer forumId, Integer topicId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getLastPost(Integer, Integer) - start"); //$NON-NLS-1$
		}

		BbsPost returnBbsPost = dao.getLastPost(forumId, topicId);
		if (logger.isDebugEnabled()) {
			logger.debug("getLastPost(Integer, Integer) - end"); //$NON-NLS-1$
		}
		return returnBbsPost;
	}

	@Transactional(readOnly = true)
	public Pagination getMemberReply(Integer webId, Integer memberId,
			int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getMemberReply(Integer, Integer, int, int) - start"); //$NON-NLS-1$
		}

		int count = dao.getMemberReplyCount(webId, memberId);
		if (pageSize == 0) {
			pageSize = 16;
		}
		if ((count / pageSize + 1) < pageNo) {
			pageNo = count / pageSize + 1;
		}
		if (pageNo < 1) {
			pageNo = 1;
		}
		List<Number> list = dao.getMemberReply(webId, memberId, (pageNo - 1)
				* pageSize, pageSize);
		List<BbsPost> l = new ArrayList<BbsPost>();
		Pagination p = new Pagination();
		for (Number b : list) {
			BbsPost bbspost = dao.findById(b.intValue());
			l.add(bbspost);
		}
		p.setPageNo(pageNo);
		p.setPageSize(pageSize);
		p.setTotalCount(count);
		p.setList(l);

		if (logger.isDebugEnabled()) {
			logger.debug("getMemberReply(Integer, Integer, int, int) - end"); //$NON-NLS-1$
		}
		return p;
	}

	@Transactional(readOnly = true)
	public int getMemberReplyCount(Integer webId, Integer memberId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getMemberReplyCount(Integer, Integer) - start"); //$NON-NLS-1$
		}

		int returnint = dao.getMemberReplyCount(webId, memberId);
		if (logger.isDebugEnabled()) {
			logger.debug("getMemberReplyCount(Integer, Integer) - end"); //$NON-NLS-1$
		}
		return returnint;
	}

	@Transactional(readOnly = true)
	public int getIndexCount(Integer topicId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getIndexCount(Integer) - start"); //$NON-NLS-1$
		}

		int returnint = dao.getIndexCount(topicId);
		if (logger.isDebugEnabled()) {
			logger.debug("getIndexCount(Integer) - end"); //$NON-NLS-1$
		}
		return returnint;
	}

	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPage(int, int) - start"); //$NON-NLS-1$
		}

		Pagination page = dao.getPage(pageNo, pageSize);

		if (logger.isDebugEnabled()) {
			logger.debug("getPage(int, int) - end"); //$NON-NLS-1$
		}
		return page;
	}

	@Transactional(readOnly = true)
	public BbsPost findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsPost entity = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsPost save(BbsPost bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsPost) - start"); //$NON-NLS-1$
		}

		dao.save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsPost) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsPost update(BbsPost bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsPost) - start"); //$NON-NLS-1$
		}

		Updater<BbsPost> updater = new Updater<BbsPost>(bean);
		bean = dao.updateByUpdater(updater);

		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsPost) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsPost deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsPost bean = dao.deleteById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsPost[] deleteByIds(Integer[] ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - start"); //$NON-NLS-1$
		}

		BbsPost[] beans = new BbsPost[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}

	private Attachment addAttachment(String filename, String filepath,
			BbsPost post, Long size) {
		if (logger.isDebugEnabled()) {
			logger.debug("addAttachment(String, String, BbsPost, Long) - start"); //$NON-NLS-1$
		}

		Attachment attach = new Attachment();
		attach.setName(filename);
		attach.setFileName(filename);
		attach.setFilePath(filepath);
		attach.setFileSize(size.intValue());
		attach.setPost(post);
		/*
		 * 判断图片还是附件
		 */
		// boolean tag = false;
		
		int indx = filename.indexOf('.');
		String type = "";
		if(indx!=-1){
			type = filename.substring(indx+1);
		}

		if("zip".equals(type.toLowerCase())||"rar".equals(type.toLowerCase())){
			attach.setPicture(false);
		}else{
			attach.setPicture(true);
		}
		attach.setCreateTime(new Timestamp(System.currentTimeMillis()));
		attachmentMng.save(attach);

		if (logger.isDebugEnabled()) {
			logger.debug("addAttachment(String, String, BbsPost, Long) - end"); //$NON-NLS-1$
		}
		return attach;
	}

	private void initBbsPost(BbsPost post, String ip) {
		if (logger.isDebugEnabled()) {
			logger.debug("initBbsPost(BbsPost, String) - start"); //$NON-NLS-1$
		}

		Date now = new Timestamp(System.currentTimeMillis());
		post.setCreateTime(now);
		post.setPosterIp(ip);
		post.setEditCount(0);
		post.setStatus(BbsPost.NORMAL);
		post.setAnonymous(false);
		if (post.getIndexCount() == null) {
			post.setIndexCount(0);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initBbsPost(BbsPost, String) - end"); //$NON-NLS-1$
		}
	}

	public void updatePostCount(BbsPost post, BbsUser user) {
		if (logger.isDebugEnabled()) {
			logger.debug("updatePostCount(BbsPost, BbsUser) - start"); //$NON-NLS-1$
		}

		BbsTopic topic = post.getTopic();
		BbsForum forum = topic.getForum();
		forum.setLastPost(post);
		forum.setLastReply(post.getCreater());
		forum.setLastTime(post.getCreateTime());
		forum.setPostToday(forum.getPostToday() + 1);
		forum.setPostTotal(forum.getPostTotal() + 1);
		topic.setLastPost(post);
		topic.setLastReply(post.getCreater());
		topic.setLastTime(post.getCreateTime());
		topic.setSortTime(post.getCreateTime());
		topic.setReplyCount(topic.getReplyCount() + 1);
		//是否启用积分
		if(forum.getPointAvailable()){
			user.setPoint(user.getPoint() + forum.getPointReply());
		}
		//是否启用威望
		if(forum.getPrestigeAvailable()){
			user.setPrestige(user.getPrestige()+forum.getPrestigeReply());
		}
		
		//存入金钱
		user.setMoney(user.getMoney() + forum.getMoneyReply());
		
		user.setPostToday(user.getPostToday() + 1);
		user.setReplyCount(user.getReplyCount() + 1);

		if (logger.isDebugEnabled()) {
			logger.debug("updatePostCount(BbsPost, BbsUser) - end"); //$NON-NLS-1$
		}
	}

	private BbsOperationMng bbsOperationMng;
	private BbsConfigMng bbsConfigMng;
	private CmsSiteMng siteMng;
	private BbsTopicMng bbsTopicMng;
	private BbsUserMng bbsUserMng;
	private BbsConfigEhCache bbsConfigEhCache;
	private AttachmentMng attachmentMng;
	private FileRepository fileRepository;
	private BbsPostTypeMng bbsPostTypeMng;
	private BbsPostDao dao;

	@Autowired
	public void setDao(BbsPostDao dao) {
		this.dao = dao;
	}

	@Autowired
	public void setBbsConfigMng(BbsConfigMng bbsConfigMng) {
		this.bbsConfigMng = bbsConfigMng;
	}

	@Autowired
	public void setSiteMng(CmsSiteMng siteMng) {
		this.siteMng = siteMng;
	}

	@Autowired
	public void setBbsUserMng(BbsUserMng bbsUserMng) {
		this.bbsUserMng = bbsUserMng;
	}

	@Autowired
	public void setBbsOperationMng(BbsOperationMng bbsOperationMng) {
		this.bbsOperationMng = bbsOperationMng;
	}

	@Autowired
	public void setBbsTopicMng(BbsTopicMng bbsTopicMng) {
		this.bbsTopicMng = bbsTopicMng;
	}

	@Autowired
	public void setBbsConfigEhCache(BbsConfigEhCache bbsConfigEhCache) {
		this.bbsConfigEhCache = bbsConfigEhCache;
	}

	@Autowired
	public void setAttachmentMng(AttachmentMng attachmentMng) {
		this.attachmentMng = attachmentMng;
	}

	@Autowired
	public void setFileRepository(FileRepository fileRepository) {
		this.fileRepository = fileRepository;
	}
	@Autowired
	public void setBbsPostTypeMng(BbsPostTypeMng bbsPostTypeMng) {
		this.bbsPostTypeMng = bbsPostTypeMng;
	}
	

	/* (non-Javadoc)
	 * @see com.jeecms.bbs.manager.BbsPostMng#clearS(com.jeecms.bbs.entity.BbsPost)
	 */
}
