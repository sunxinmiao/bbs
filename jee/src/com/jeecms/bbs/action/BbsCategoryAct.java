package com.jeecms.bbs.action;

import org.apache.log4j.Logger;

import java.util.List;

import javax.servlet.http.HttpServletRequest;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.entity.BbsCategory;
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.manager.BbsCategoryMng;
import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.core.entity.CmsSite;

@Controller
public class BbsCategoryAct {
	private static final Logger logger = Logger.getLogger(BbsCategoryAct.class);

	@RequestMapping("/category/v_list.do")
	public String list(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("list(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		List<BbsCategory> list = bbsCategoryMng.getList(CmsUtils
				.getSiteId(request));
		model.put("list", list);

		if (logger.isDebugEnabled()) {
			logger.debug("list(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "category/list";
	}

	@RequestMapping("/category/v_add.do")
	public String add(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("add(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			logger.debug("add(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "category/add";
	}

	@RequestMapping("/category/v_edit.do")
	public String edit(Integer id, HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("edit(Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		BbsCategory category = bbsCategoryMng.findById(id);
		model.put("category", category);

		if (logger.isDebugEnabled()) {
			logger.debug("edit(Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "category/edit";
	}

	@RequestMapping("/category/o_save.do")
	public String save(BbsCategory category, HttpServletRequest request,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsCategory, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		category.setSite(site);
		bbsCategoryMng.save(category);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsCategory, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "redirect:v_list.do";
	}

	@RequestMapping("/category/o_update.do")
	public String update(BbsCategory category, HttpServletRequest request,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsCategory, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		bbsCategoryMng.update(category);

		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsCategory, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "redirect:v_list.do";
	}

	@RequestMapping("/category/o_delete.do")
	public String delete(Integer[] ids, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("delete(Integer[], Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		for (int i = 0; i < ids.length; i++) {
			List<BbsForum> listForum = bbsForumMng.getList(site.getId(), ids[i]);
			Integer[] forumIds = new Integer[listForum.size()];
			for (int ii = 0; ii < listForum.size(); ii++) {
				forumIds[ii]=listForum.get(ii).getId();
				List<BbsTopic> listTopic = bbsTopicMng.getListByForumId(listForum.get(ii).getId());
				for (int j = 0; j < listTopic.size(); j++) {
					BbsTopic topic = bbsTopicMng.deleteById(listTopic.get(j).getId());
					logger.info("delete BbsTopic id="+ topic.getId());
				}
			}
			BbsForum[] beans = bbsForumMng.deleteByIds(forumIds);
			for (BbsForum bean : beans) {
				logger.info("delete BbsForum id="+ bean.getId());
			}
		}
		BbsCategory[] beans = bbsCategoryMng.deleteByIds(ids);
		for (BbsCategory bean : beans) {
			logger.info("delete BbsCategory id="+ bean.getId());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("delete(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "redirect:v_list.do";
	}

	@RequestMapping("/category/o_priority.do")
	public String priorityUpdate(Integer[] wids, Integer[] prioritys,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("priorityUpdate(Integer[], Integer[], HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		if (wids == null || wids.length <= 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("priorityUpdate(Integer[], Integer[], HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return "redirect:v_list.do";
		}
		CmsSite site = CmsUtils.getSite(request);
		BbsCategory t;
		Integer id;
		Integer priority;
		for (int i = 0; i < wids.length; i++) {
			id = wids[i];
			priority = prioritys[i];
			if (id != null && priority != null) {
				t = bbsCategoryMng.findById(id);
				if (t != null && t.getSite().getId().equals(site.getId())) {
					t.setPriority(priority);
					bbsCategoryMng.update(t);
				}
			}
		}
		String returnString = list(request, model);
		if (logger.isDebugEnabled()) {
			logger.debug("priorityUpdate(Integer[], Integer[], HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@Autowired
	private BbsCategoryMng bbsCategoryMng;
	@Autowired
	private BbsForumMng bbsForumMng;
	@Autowired
	private BbsTopicMng bbsTopicMng;
}
