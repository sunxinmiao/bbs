package com.jeecms.bbs.action;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsPostType;
import com.jeecms.bbs.entity.BbsUserGroup;
import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.bbs.manager.BbsPostTypeMng;
import com.jeecms.bbs.manager.BbsUserGroupMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.WebErrors;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;

@Controller
public class BbsUserGroupAct {
	private static final Logger logger = Logger.getLogger(BbsUserGroupAct.class);

	@RequestMapping("/group/v_list.do")
	public String list(Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("list(Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		List<BbsUserGroup> list = manager.getList(site.getId());
		model.addAttribute("list", list);
		model.addAttribute("groupType", 1);

		if (logger.isDebugEnabled()) {
			logger.debug("list(Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "group/list";
	}

	@RequestMapping("/group/v_add.do")
	public String add(ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("add(ModelMap) - start"); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			logger.debug("add(ModelMap) - end"); //$NON-NLS-1$
		}
		return "group/add";
	}

	@RequestMapping("/group/v_edit.do")
	public String edit(Integer groupId, short groupType, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("edit(Integer, short, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		BbsUserGroup group = manager.findById(groupId);
		CmsSite site=CmsUtils.getSite(request);
		List<BbsPostType> postTypes = bbsPostTypeMng.getList(CmsUtils.getSiteId(request), null, null);
		List<BbsPostType> tempList;
		Map<String,List<BbsPostType>>childPostTypes=new HashMap<String,List<BbsPostType>>();
		for(BbsPostType parent:postTypes){
			tempList= bbsPostTypeMng.getList(site.getId(), null, parent.getId());
			childPostTypes.put(parent.getId().toString(),tempList);
		}
		model.put("childPostTypes", childPostTypes);
		model.put("postTypes", postTypes);
		model.put("bbsUserGroup", group);
		model.put("groupType", groupType);

		if (logger.isDebugEnabled()) {
			logger.debug("edit(Integer, short, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "group/edit";
	}
	@RequestMapping(value = "/group/v_tree.do")
	public String tree(String root,Integer groupId, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		logger.debug("tree path="+ root);
		boolean isRoot;
		// jquery treeview的根请求为root=source
		if (StringUtils.isBlank(root) || "source".equals(root)) {
			isRoot = true;
		} else {
			isRoot = false;
		}
		model.addAttribute("isRoot", isRoot);
		WebErrors errors = validateTree(root, request);
		if (errors.hasErrors()) {
			logger.error(errors.getErrors().get(0));
			ResponseUtils.renderJson(response, "[]");

			if (logger.isDebugEnabled()) {
				logger.debug("tree(String, Integer, HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
			}
			return null;
		}
		CmsSite site = CmsUtils.getSite(request);
		BbsUserGroup group = manager.findById(groupId);
		Integer[]postTypeIds=group.fetchIds(group.getPostTypes());
		if (isRoot) {
			List<BbsForum> list;
			list = bbsForumMng.getList(site.getId());
			model.addAttribute("list", list);
		} else {
			List<BbsPostType> list;
			Integer parentId;
			if(StringUtils.isNotBlank(root)&&root.contains(",")){
				parentId= Integer.valueOf(root.split(",")[0]);
				list=bbsPostTypeMng.getList(site.getId(), null, parentId);
			}else{
				parentId = Integer.valueOf(root);
				list = bbsPostTypeMng.getList(site.getId(), parentId,null);
			}
			model.addAttribute("list", list);
		}
		model.put("postTypeIds", postTypeIds);
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/json;charset=UTF-8");

		if (logger.isDebugEnabled()) {
			logger.debug("tree(String, Integer, HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
		}
		return "group/tree";
	}
	private WebErrors validateTree(String path, HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateTree(String, HttpServletRequest) - start"); //$NON-NLS-1$
		}

		WebErrors errors = WebErrors.create(request);

		if (logger.isDebugEnabled()) {
			logger.debug("validateTree(String, HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return errors;
	}

	@RequestMapping("/group/o_save.do")
	public String save(String[] name, String[] imgPath, Integer[] id,
			Long[] point, short groupType, HttpServletRequest request,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(String[], String[], Integer[], Long[], short, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		manager.saveOrUpdateGroups(site.getId(), groupType, name, imgPath, id,
				point);

		if (logger.isDebugEnabled()) {
			logger.debug("save(String[], String[], Integer[], Long[], short, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "redirect:v_list.do";
	}

	@RequestMapping("/group/o_update.do")
	public String update(BbsUserGroup bean, Integer[] postTypeIds,
			Integer pageNo, HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsUserGroup, Integer[], Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		// bean = manager.update(bean);
		bean = manager.update(bean, postTypeIds);
		logger.info("update BbsUserGroup id="+ bean.getId());
		return list(pageNo, request, model);
	}

	@RequestMapping("/group/o_delete.do")
	public String delete(Integer[] ids, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("delete(Integer[], Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		BbsUserGroup[] beans = manager.deleteByIds(ids);
		for (BbsUserGroup bean : beans) {
			logger.info("delete BbsUserGroup id="+ bean.getId());
		}
		String returnString = list(pageNo, request, model);
		if (logger.isDebugEnabled()) {
			logger.debug("delete(Integer[], Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@Autowired
	private BbsUserGroupMng manager;
	@Autowired
	private BbsPostTypeMng bbsPostTypeMng;
	@Autowired
	private BbsForumMng bbsForumMng;
}
