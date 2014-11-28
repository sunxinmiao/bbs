package com.jeecms.bbs.action;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.entity.BbsPostType;
import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.bbs.manager.BbsPostTypeMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.common.web.WebErrors;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.core.entity.CmsSite;
import static com.jeecms.common.page.SimplePage.cpn;

@Controller
public class BbsPostTypeAct {
	private static final Logger logger = Logger.getLogger(BbsPostTypeAct.class);

	@RequestMapping("/posttype/v_list.do")
	public String list(Integer forumId,Integer parentId,Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("list(Integer, Integer, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		Pagination pagination = bbsPostTypeMng.getPage(CmsUtils
				.getSite(request).getId(), forumId,parentId,cpn(pageNo), CookieUtils
				.getPageSize(request));
		model.put("pagination", pagination);
		model.put("forumId", forumId);
		model.put("parentId", parentId);

		if (logger.isDebugEnabled()) {
			logger.debug("list(Integer, Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "posttype/list";
	}

	@RequestMapping("/posttype/v_add.do")
	public String add(Integer forumId,Integer parentId,HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("add(Integer, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		model.put("forumId", forumId);
		model.put("parentId", parentId);

		if (logger.isDebugEnabled()) {
			logger.debug("add(Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "posttype/add";
	}

	@RequestMapping("/posttype/v_edit.do")
	public String edit(Integer id, Integer forumId,Integer parentId,HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("edit(Integer, Integer, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		BbsPostType posttype = bbsPostTypeMng.findById(id);
		model.put("posttype", posttype);
		model.put("forumId", forumId);
		model.put("parentId", parentId);

		if (logger.isDebugEnabled()) {
			logger.debug("edit(Integer, Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "posttype/edit";
	}

	@RequestMapping("/posttype/o_save.do")
	public String save(BbsPostType posttype,Integer forumId,Integer parentId,Integer pageNo,HttpServletRequest request,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsPostType, Integer, Integer, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		if(forumId!=null){
			posttype.setForum(bbsForumMng.findById(forumId));
		}
		if(parentId!=null){
			posttype.setParent(bbsPostTypeMng.findById(parentId));
		}
		posttype.setSite(site);
		bbsPostTypeMng.save(posttype);
		String returnString = list(forumId, parentId, pageNo, request, model);
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsPostType, Integer, Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/posttype/o_update.do")
	public String update(BbsPostType posttype,Integer forumId,Integer parentId,Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsPostType, Integer, Integer, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		if(forumId!=null){
			posttype.setForum(bbsForumMng.findById(forumId));
		}
		if(parentId!=null){
			posttype.setParent(bbsPostTypeMng.findById(parentId));
		}
		bbsPostTypeMng.update(posttype);
		String returnString = list(forumId, parentId, pageNo, request, model);
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsPostType, Integer, Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/posttype/o_delete.do")
	public String delete(Integer[] ids,Integer forumId,Integer parentId, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("delete(Integer[], Integer, Integer, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		BbsPostType[] beans = bbsPostTypeMng.deleteByIds(ids);
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			String returnString = errors.showErrorPage(model);
			if (logger.isDebugEnabled()) {
				logger.debug("delete(Integer[], Integer, Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		for (BbsPostType bean : beans) {
			logger.info("delete BbsPostType id="+ bean.getId());
		}
		String returnString = list(forumId, parentId, pageNo, request, model);
		if (logger.isDebugEnabled()) {
			logger.debug("delete(Integer[], Integer, Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/posttype/o_priority.do")
	public String priorityUpdate(Integer forumId,Integer parentId,Integer[] wids, Integer[] prioritys,
			Integer pageNo, HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("priorityUpdate(Integer, Integer, Integer[], Integer[], Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		if (wids == null || wids.length <= 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("priorityUpdate(Integer, Integer, Integer[], Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return "redirect:v_list.do";
		}
		CmsSite site = CmsUtils.getSite(request);
		BbsPostType t;
		Integer id;
		Integer priority;
		for (int i = 0; i < wids.length; i++) {
			id = wids[i];
			priority = prioritys[i];
			if (id != null && priority != null) {
				t = bbsPostTypeMng.findById(id);
				if (t != null && t.getSite().getId().equals(site.getId())) {
					t.setPriority(priority);
					bbsPostTypeMng.update(t);
				}
			}
		}
		String returnString = list(forumId, parentId, cpn(pageNo), request, model);
		if (logger.isDebugEnabled()) {
			logger.debug("priorityUpdate(Integer, Integer, Integer[], Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	private WebErrors validateDelete(Integer[] ids, HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateDelete(Integer[], HttpServletRequest) - start"); //$NON-NLS-1$
		}

		WebErrors errors = WebErrors.create(request);
		errors.ifEmpty(ids, "ids");
		for (Integer id : ids) {
			vldExist(id, errors);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validateDelete(Integer[], HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return errors;
	}

	private boolean vldExist(Integer id, WebErrors errors) {
		if (logger.isDebugEnabled()) {
			logger.debug("vldExist(Integer, WebErrors) - start"); //$NON-NLS-1$
		}

		if (errors.ifNull(id, "id")) {
			if (logger.isDebugEnabled()) {
				logger.debug("vldExist(Integer, WebErrors) - end"); //$NON-NLS-1$
			}
			return true;
		}
		BbsPostType entity = bbsPostTypeMng.findById(id);
		if (errors.ifNotExist(entity, BbsPostType.class, id)) {
			if (logger.isDebugEnabled()) {
				logger.debug("vldExist(Integer, WebErrors) - end"); //$NON-NLS-1$
			}
			return true;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("vldExist(Integer, WebErrors) - end"); //$NON-NLS-1$
		}
		return false;
	}

	@Autowired
	private BbsPostTypeMng bbsPostTypeMng;
	@Autowired
	private BbsForumMng bbsForumMng;
}
