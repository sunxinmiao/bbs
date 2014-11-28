package com.jeecms.bbs.action.template;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.jeecms.bbs.template.manager.CmsResourceMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.WebErrors;
import com.jeecms.common.file.FileWrap;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;

/**
 * JEEBBS资源的Action
 * 
 * @author liqiang
 * 
 */
// TODO 验证path必须以TPL_BASE开头，不能有..后退关键字
@Controller
public class ResourceAct {
	private static final Logger logger = Logger.getLogger(ResourceAct.class);

	@RequestMapping("/resource/v_left.do")
	public String left(String path, HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("left(String, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			logger.debug("left(String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "resource/left";
	}

	@RequestMapping(value = "/resource/v_tree.do")
	public String tree(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("tree(HttpServletRequest, HttpServletResponse, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		String root = RequestUtils.getQueryParam(request, "root");
		logger.debug("tree path="+root);
		// jquery treeview的根请求为root=source
		if (StringUtils.isBlank(root) || "source".equals(root)) {
			root = site.getResPath();
			model.addAttribute("isRoot", true);
		} else {
			model.addAttribute("isRoot", false);
		}
		WebErrors errors = validateTree(root, request);
		if (errors.hasErrors()) {
			logger.error(errors.getErrors().get(0));
			ResponseUtils.renderJson(response, "[]");

			if (logger.isDebugEnabled()) {
				logger.debug("tree(HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
			}
			return null;
		}
		List<? extends FileWrap> resList = resourceMng.listFile(root, true);
		model.addAttribute("resList", resList);
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/json;charset=UTF-8");

		if (logger.isDebugEnabled()) {
			logger.debug("tree(HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
		}
		return "resource/tree";
	}

	// 直接调用方法需要把root参数保存至model中
	@RequestMapping(value = "/resource/v_list.do")
	public String list(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("list(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		String root = (String) model.get("root");
		if (root == null) {
			root = RequestUtils.getQueryParam(request, "root");
		}
		logger.debug("list Resource root: "+root);
		if (StringUtils.isBlank(root)) {
			root = site.getResPath();
		}
		String rel = root.substring(site.getResPath().length());
		if (rel.length() == 0) {
			rel = "/";
		}
		model.addAttribute("root", root);
		model.addAttribute("rel", rel);
		model.addAttribute("list", resourceMng.listFile(root, false));

		if (logger.isDebugEnabled()) {
			logger.debug("list(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "resource/list";
	}

	@RequestMapping(value = "/resource/o_create_dir.do")
	public String createDir(String root, String dirName,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("createDir(String, String, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		// TODO 检查dirName是否存在
		resourceMng.createDir(root, dirName);
		model.addAttribute("root", root);
		String returnString = list(request, model);
		if (logger.isDebugEnabled()) {
			logger.debug("createDir(String, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping(value = "/resource/v_add.do")
	public String add(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("add(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		String root = RequestUtils.getQueryParam(request, "root");
		model.addAttribute("root", root);

		if (logger.isDebugEnabled()) {
			logger.debug("add(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "resource/add";
	}

	@RequestMapping("/resource/v_edit.do")
	public String edit(HttpServletRequest request, ModelMap model)
			throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("edit(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		String root = RequestUtils.getQueryParam(request, "root");
		String name = RequestUtils.getQueryParam(request, "name");
		WebErrors errors = validateEdit(root, request);
		if (errors.hasErrors()) {
			String returnString = errors.showErrorPage(model);
			if (logger.isDebugEnabled()) {
				logger.debug("edit(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		model.addAttribute("source", resourceMng.readFile(name));
		model.addAttribute("root", root);
		model.addAttribute("name", name);
		model.addAttribute("filename", name
				.substring(name.lastIndexOf('/') + 1));

		if (logger.isDebugEnabled()) {
			logger.debug("edit(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "resource/edit";
	}

	@RequestMapping("/resource/o_save.do")
	public String save(String root, String filename, String source,
			HttpServletRequest request, ModelMap model) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("save(String, String, String, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		WebErrors errors = validateSave(filename, source, request);
		if (errors.hasErrors()) {
			String returnString = errors.showErrorPage(model);
			if (logger.isDebugEnabled()) {
				logger.debug("save(String, String, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		resourceMng.createFile(root, filename, source);
		model.addAttribute("root", root);
		logger.info("save Resource name="+ filename);
		return "redirect:v_list.do";
	}

	// AJAX请求，不返回页面
	@RequestMapping("/resource/o_update.do")
	public void update(String root, String name, String source,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("update(String, String, String, HttpServletRequest, HttpServletResponse, ModelMap) - start"); //$NON-NLS-1$
		}

		WebErrors errors = validateUpdate(root, name, source, request);
		if (errors.hasErrors()) {
			ResponseUtils.renderJson(response, "{success:false,msg:'"
					+ errors.getErrors().get(0) + "'}");
		}
		resourceMng.updateFile(name, source);
		logger.info("update Resource name="+ name);
		model.addAttribute("root", root);
		ResponseUtils.renderJson(response, "{success:true}");

		if (logger.isDebugEnabled()) {
			logger.debug("update(String, String, String, HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
		}
	}

	@RequestMapping("/resource/o_delete.do")
	public String delete(String root, String[] names,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("delete(String, String[], HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		WebErrors errors = validateDelete(root, names, request);
		if (errors.hasErrors()) {
			String returnString = errors.showErrorPage(model);
			if (logger.isDebugEnabled()) {
				logger.debug("delete(String, String[], HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		int count = resourceMng.delete(names);
		logger.info("delete Resource count: "+ count);
		for (String name : names) {
			logger.info("delete Resource name="+ name);
		}
		model.addAttribute("root", root);
		String returnString = list(request, model);
		if (logger.isDebugEnabled()) {
			logger.debug("delete(String, String[], HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/resource/o_delete_single.do")
	public String deleteSingle(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteSingle(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		// TODO 输入验证
		String root = RequestUtils.getQueryParam(request, "root");
		String name = RequestUtils.getQueryParam(request, "name");
		int count = resourceMng.delete(new String[] { name });
		logger.info("delete Resource " + name+", count "+ count);
		model.addAttribute("root", root);
		String returnString = list(request, model);
		if (logger.isDebugEnabled()) {
			logger.debug("deleteSingle(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping(value = "/resource/v_rename.do")
	public String renameInput(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("renameInput(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		String root = RequestUtils.getQueryParam(request, "root");
		String name = RequestUtils.getQueryParam(request, "name");
		String origName = name.substring(site.getResPath().length());
		model.addAttribute("origName", origName);
		model.addAttribute("root", root);

		if (logger.isDebugEnabled()) {
			logger.debug("renameInput(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "resource/rename";
	}

	@RequestMapping(value = "/resource/o_rename.do", method = RequestMethod.POST)
	public String renameSubmit(String root, String origName, String distName,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("renameSubmit(String, String, String, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		String orig = site.getResPath() + origName;
		String dist = site.getResPath() + distName;
		resourceMng.rename(orig, dist);
		logger.info("name Resource from "+orig+" to "+ dist);
		model.addAttribute("root", root);
		String returnString = list(request, model);
		if (logger.isDebugEnabled()) {
			logger.debug("renameSubmit(String, String, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping(value = "/resource/v_upload.do")
	public String uploadInput(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("uploadInput(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		String root = RequestUtils.getQueryParam(request, "root");
		model.addAttribute("root", root);

		if (logger.isDebugEnabled()) {
			logger.debug("uploadInput(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "resource/upload";
	}

	@RequestMapping(value = "/resource/o_upload.do", method = RequestMethod.POST)
	public String uploadSubmit(String root, HttpServletRequest request,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("uploadSubmit(String, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		model.addAttribute("root", root);
		String returnString = list(request, model);
		if (logger.isDebugEnabled()) {
			logger.debug("uploadSubmit(String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping(value = "/resource/o_swfupload.do", method = RequestMethod.POST)
	public void swfUpload(
			String root,
			@RequestParam(value = "Filedata", required = false) MultipartFile file,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws IllegalStateException, IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("swfUpload(String, MultipartFile, HttpServletRequest, HttpServletResponse, ModelMap) - start"); //$NON-NLS-1$
		}

		resourceMng.saveFile(root, file);
		model.addAttribute("root", root);
		logger.info("file upload seccess: "+file
				.getOriginalFilename()+", size:"+ file.getSize());
		ResponseUtils.renderText(response, "");

		if (logger.isDebugEnabled()) {
			logger.debug("swfUpload(String, MultipartFile, HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
		}
	}

	private WebErrors validateTree(String path, HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateTree(String, HttpServletRequest) - start"); //$NON-NLS-1$
		}

		WebErrors errors = WebErrors.create(request);
		// if (errors.ifBlank(path, "path", 255)) {
		// return errors;
		// }

		if (logger.isDebugEnabled()) {
			logger.debug("validateTree(String, HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return errors;
	}

	private WebErrors validateSave(String name, String source,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateSave(String, String, HttpServletRequest) - start"); //$NON-NLS-1$
		}

		WebErrors errors = WebErrors.create(request);

		if (logger.isDebugEnabled()) {
			logger.debug("validateSave(String, String, HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return errors;
	}

	private WebErrors validateEdit(String id, HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateEdit(String, HttpServletRequest) - start"); //$NON-NLS-1$
		}

		WebErrors errors = WebErrors.create(request);
		if (vldExist(id, errors)) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateEdit(String, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return errors;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validateEdit(String, HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return errors;
	}

	private WebErrors validateUpdate(String root, String name, String source,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateUpdate(String, String, String, HttpServletRequest) - start"); //$NON-NLS-1$
		}

		WebErrors errors = WebErrors.create(request);
		if (vldExist(name, errors)) {
			if (logger.isDebugEnabled()) {
				logger.debug("validateUpdate(String, String, String, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return errors;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validateUpdate(String, String, String, HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return errors;
	}

	private WebErrors validateDelete(String root, String[] names,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateDelete(String, String[], HttpServletRequest) - start"); //$NON-NLS-1$
		}

		WebErrors errors = WebErrors.create(request);
		errors.ifEmpty(names, "names");
		for (String id : names) {
			vldExist(id, errors);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validateDelete(String, String[], HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return errors;
	}

	private boolean vldExist(String name, WebErrors errors) {
		if (logger.isDebugEnabled()) {
			logger.debug("vldExist(String, WebErrors) - start"); //$NON-NLS-1$
		}

		if (errors.ifNull(name, "name")) {
			if (logger.isDebugEnabled()) {
				logger.debug("vldExist(String, WebErrors) - end"); //$NON-NLS-1$
			}
			return true;
		}
		// Tpl entity = tplManager.get(name);
		// if (errors.ifNotExist(entity, Tpl.class, name)) {
		// return true;
		// }

		if (logger.isDebugEnabled()) {
			logger.debug("vldExist(String, WebErrors) - end"); //$NON-NLS-1$
		}
		return false;
	}

	private CmsResourceMng resourceMng;

	@Autowired
	public void setResourceMng(CmsResourceMng resourceMng) {
		this.resourceMng = resourceMng;
	}
}