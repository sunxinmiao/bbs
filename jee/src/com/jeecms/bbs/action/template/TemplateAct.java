package com.jeecms.bbs.action.template;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

import com.jeecms.bbs.Constants;
import com.jeecms.bbs.template.manager.CmsResourceMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.WebErrors;
import com.jeecms.common.util.Zipper;
import com.jeecms.common.util.Zipper.FileEntry;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsSiteMng;
import com.jeecms.core.tpl.Tpl;
import com.jeecms.core.tpl.TplManager;

/**
 * JEEBBS模板的Action
 * 
 * @author liqiang
 * 
 */
// TODO 验证path必须以TPL_BASE开头，不能有..后退关键字
@Controller
public class TemplateAct {
	private static final Logger logger = Logger.getLogger(TemplateAct.class);

	@RequestMapping("/template/v_left.do")
	public String left(String path, HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("left(String, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			logger.debug("left(String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "template/left";
	}

	@RequestMapping(value = "/template/v_tree.do", method = RequestMethod.GET)
	public String tree(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("tree(HttpServletRequest, HttpServletResponse, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		String root = RequestUtils.getQueryParam(request, "root");
		logger.debug("tree path="+ root);
		// jquery treeview的根请求为root=source
		if (StringUtils.isBlank(root) || "source".equals(root)) {
			root = site.getTplPath();
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
		List<? extends Tpl> tplList = tplManager.getChild(root);
		model.addAttribute("tplList", tplList);
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/json;charset=UTF-8");

		if (logger.isDebugEnabled()) {
			logger.debug("tree(HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
		}
		return "template/tree";
	}

	// 直接调用方法需要把root参数保存至model中
	@RequestMapping(value = "/template/v_list.do", method = RequestMethod.GET)
	public String list(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("list(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		String root = (String) model.get("root");
		if (root == null) {
			root = RequestUtils.getQueryParam(request, "root");
		}
		logger.debug("list Template root: "+ root);
		if (StringUtils.isBlank(root)) {
			root = site.getTplPath();
		}
		String rel = root.substring(site.getTplPath().length());
		if (rel.length() == 0) {
			rel = "/";
		}
		model.addAttribute("root", root);
		model.addAttribute("rel", rel);
		model.addAttribute("list", tplManager.getChild(root));

		if (logger.isDebugEnabled()) {
			logger.debug("list(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "template/list";
	}

	@RequestMapping(value = "/template/o_create_dir.do")
	public String createDir(String root, String dirName,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("createDir(String, String, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		// TODO 检查dirName是否存在
		tplManager.save(root + "/" + dirName, null, true);
		model.addAttribute("root", root);
		String returnString = list(request, model);
		if (logger.isDebugEnabled()) {
			logger.debug("createDir(String, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping(value = "/template/v_add.do", method = RequestMethod.GET)
	public String add(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("add(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		String root = RequestUtils.getQueryParam(request, "root");
		model.addAttribute("root", root);

		if (logger.isDebugEnabled()) {
			logger.debug("add(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "template/add";
	}

	@RequestMapping("/template/v_edit.do")
	public String edit(HttpServletRequest request, ModelMap model) {
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
		model.addAttribute("template", tplManager.get(name));
		model.addAttribute("root", root);

		if (logger.isDebugEnabled()) {
			logger.debug("edit(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "template/edit";
	}

	@RequestMapping("/template/o_save.do")
	public String save(String root, String filename, String source,
			HttpServletRequest request, ModelMap model) {
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
		String name = root + "/" + filename + Constants.TPL_SUFFIX;
		tplManager.save(name, source, false);
		model.addAttribute("root", root);
		logger.info("save Template name="+ filename);
		return "redirect:v_list.do";
	}

	// AJAX请求，不返回页面
	@RequestMapping("/template/o_update.do")
	public void update(String root, String name, String source,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(String, String, String, HttpServletRequest, HttpServletResponse, ModelMap) - start"); //$NON-NLS-1$
		}

		WebErrors errors = validateUpdate(root, name, source, request);
		if (errors.hasErrors()) {
			ResponseUtils.renderJson(response, "{success:false,msg:'"
					+ errors.getErrors().get(0) + "'}");
		}
		tplManager.update(name, source);
		logger.info("update Template name="+ name);
		model.addAttribute("root", root);
		ResponseUtils.renderJson(response, "{success:true}");

		if (logger.isDebugEnabled()) {
			logger.debug("update(String, String, String, HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
		}
	}

	@RequestMapping("/template/o_delete.do")
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
		int count = tplManager.delete(names);
		logger.info("delete Template count: {}"+ count);
		for (String name : names) {
			logger.info("delete Template name="+ name);
		}
		model.addAttribute("root", root);
		String returnString = list(request, model);
		if (logger.isDebugEnabled()) {
			logger.debug("delete(String, String[], HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/template/o_delete_single.do")
	public String deleteSingle(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteSingle(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		// TODO 输入验证
		String root = RequestUtils.getQueryParam(request, "root");
		String name = RequestUtils.getQueryParam(request, "name");
		int count = tplManager.delete(new String[] { name });
		logger.info("delete Template " +name + ", count " + count);
		model.addAttribute("root", root);
		String returnString = list(request, model);
		if (logger.isDebugEnabled()) {
			logger.debug("deleteSingle(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping(value = "/template/v_rename.do", method = RequestMethod.GET)
	public String renameInput(HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("renameInput(HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		String root = RequestUtils.getQueryParam(request, "root");
		String name = RequestUtils.getQueryParam(request, "name");
		String origName = name.substring(site.getTplPath().length());
		model.addAttribute("origName", origName);
		model.addAttribute("root", root);

		if (logger.isDebugEnabled()) {
			logger.debug("renameInput(HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return "template/rename";
	}

	@RequestMapping(value = "/template/o_rename.do", method = RequestMethod.POST)
	public String renameSubmit(String root, String origName, String distName,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("renameSubmit(String, String, String, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		String orig = site.getTplPath() + origName;
		String dist = site.getTplPath() + distName;
		tplManager.rename(orig, dist);
		logger.info("name Template from " + orig + " to "+ dist);
		model.addAttribute("root", root);
		String returnString = list(request, model);
		if (logger.isDebugEnabled()) {
			logger.debug("renameSubmit(String, String, String, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping(value = "/template/o_swfupload.do", method = RequestMethod.POST)
	public void swfUpload(
			String root,
			@RequestParam(value = "Filedata", required = false) MultipartFile file,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws IllegalStateException, IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("swfUpload(String, MultipartFile, HttpServletRequest, HttpServletResponse, ModelMap) - start"); //$NON-NLS-1$
		}

		tplManager.save(root, file);
		model.addAttribute("root", root);
		logger.info("file upload seccess: " + file
				.getOriginalFilename() + " , size:"+file.getSize());
		ResponseUtils.renderText(response, "");

		if (logger.isDebugEnabled()) {
			logger.debug("swfUpload(String, MultipartFile, HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
		}
	}

	@RequestMapping(value = "/template/v_setting.do")
	public String setting(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("setting(HttpServletRequest, HttpServletResponse, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		String[] solutions = resourceMng.getSolutions(site.getTplPath());
		model.addAttribute("solutions", solutions);
		model.addAttribute("defSolution", site.getTplSolution());

		if (logger.isDebugEnabled()) {
			logger.debug("setting(HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
		}
		return "template/setting";
	}

	@RequestMapping(value = "/template/o_def_template.do")
	public void defTempate(String solution, HttpServletRequest request,
			HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("defTempate(String, HttpServletRequest, HttpServletResponse) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		cmsSiteMng.updateTplSolution(site.getId(), solution);
		ResponseUtils.renderJson(response, "{'success':true}");

		if (logger.isDebugEnabled()) {
			logger.debug("defTempate(String, HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
		}
	}

	@RequestMapping(value = "/template/o_export.do")
	public void exportSubmit(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		if (logger.isDebugEnabled()) {
			logger.debug("exportSubmit(HttpServletRequest, HttpServletResponse) - start"); //$NON-NLS-1$
		}

		String solution = RequestUtils.getQueryParam(request, "solution");
		CmsSite site = CmsUtils.getSite(request);
		List<FileEntry> fileEntrys = resourceMng.export(site, solution);
		response.setContentType("application/x-download;charset=UTF-8");
		response.addHeader("Content-disposition", "filename=template-"
				+ solution + ".zip");
		try {
			// 模板一般都在windows下编辑，所以默认编码为GBK
			Zipper.zip(response.getOutputStream(), fileEntrys, "GBK");
		} catch (IOException e) {
			logger.error("export template error!", e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exportSubmit(HttpServletRequest, HttpServletResponse) - end"); //$NON-NLS-1$
		}
	}

	@RequestMapping(value = "/template/o_import.do")
	public String importSubmit(
			@RequestParam(value = "tplZip", required = false) MultipartFile file,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("importSubmit(MultipartFile, HttpServletRequest, HttpServletResponse, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		File tempFile = File.createTempFile("tplZip", "temp");
		file.transferTo(tempFile);
		resourceMng.imoport(tempFile, site);
		tempFile.delete();
		String returnString = setting(request, response, model);
		if (logger.isDebugEnabled()) {
			logger.debug("importSubmit(MultipartFile, HttpServletRequest, HttpServletResponse, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
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
		Tpl entity = tplManager.get(name);
		if (errors.ifNotExist(entity, Tpl.class, name)) {
			if (logger.isDebugEnabled()) {
				logger.debug("vldExist(String, WebErrors) - end"); //$NON-NLS-1$
			}
			return true;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("vldExist(String, WebErrors) - end"); //$NON-NLS-1$
		}
		return false;
	}

	private TplManager tplManager;
	private CmsResourceMng resourceMng;
	private CmsSiteMng cmsSiteMng;

	public void setTplManager(TplManager tplManager) {
		this.tplManager = tplManager;
	}

	@Autowired
	public void setResourceMng(CmsResourceMng resourceMng) {
		this.resourceMng = resourceMng;
	}

	@Autowired
	public void setCmsSiteMng(CmsSiteMng cmsSiteMng) {
		this.cmsSiteMng = cmsSiteMng;
	}
}