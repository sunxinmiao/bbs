package com.jeecms.bbs.action.member;

import org.apache.log4j.Logger;

import static com.jeecms.bbs.Constants.TPLDIR_MEMBER;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.image.ImageUtils;
import com.jeecms.common.upload.FileRepository;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.WebErrors;

@Controller
public class ImageUploadAct {
	private static final Logger logger = Logger.getLogger(ImageUploadAct.class);
	/**
	 * 用户头像路径
	 */
	private static final String USER_IMG_PATH = "/user/images";
	/**
	 * 结果页
	 */
	private static final String RESULT_PAGE = "tpl.iframe_upload";
	/**
	 * 错误信息参数
	 */
	public static final String ERROR = "error";

	@RequestMapping("/member/o_upload_image.jspx")
	public String execute(
			String filename,
			Integer uploadNum,
			Boolean mark,
			@RequestParam(value = "uploadFile", required = false) MultipartFile file,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("execute(String, Integer, Boolean, MultipartFile, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		WebErrors errors = validate(filename, file, request);
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			String returnString = FrontUtils.showLogin(request, model, site);
			if (logger.isDebugEnabled()) {
				logger.debug("execute(String, Integer, Boolean, MultipartFile, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		if (errors.hasErrors()) {
			model.addAttribute(ERROR, errors.getErrors().get(0));
			String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, RESULT_PAGE);
			if (logger.isDebugEnabled()) {
				logger.debug("execute(String, Integer, Boolean, MultipartFile, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		String origName = file.getOriginalFilename();
		String ext = FilenameUtils.getExtension(origName).toLowerCase(
				Locale.ENGLISH);
		try {
			String fileUrl;
				String ctx = request.getContextPath();
				if (!StringUtils.isBlank(filename)) {
					filename = filename.substring(ctx.length());
						fileUrl = fileRepository
								.storeByFilename(filename, file);
				} else {
						fileUrl = fileRepository.storeByExt(USER_IMG_PATH, ext, file);
					// 加上部署路径
					fileUrl = ctx + fileUrl;
				}
			model.addAttribute("uploadPath", fileUrl);
			model.addAttribute("uploadNum", uploadNum);
		} catch (IllegalStateException e) {
			model.addAttribute(ERROR, e.getMessage());
			logger.error("upload file error!", e);
		} catch (IOException e) {
			model.addAttribute(ERROR, e.getMessage());
			logger.error("upload file error!", e);
		} catch (Exception e) {
			model.addAttribute(ERROR, e.getMessage());
			logger.error("upload file error!", e);
		}
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, RESULT_PAGE);
		if (logger.isDebugEnabled()) {
			logger.debug("execute(String, Integer, Boolean, MultipartFile, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	private WebErrors validate(String filename, MultipartFile file,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("validate(String, MultipartFile, HttpServletRequest) - start"); //$NON-NLS-1$
		}

		WebErrors errors = WebErrors.create(request);
		if (file == null) {
			errors.addErrorCode("imageupload.error.noFileToUpload");

			if (logger.isDebugEnabled()) {
				logger.debug("validate(String, MultipartFile, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return errors;
		}
		if (StringUtils.isBlank(filename)) {
			filename = file.getOriginalFilename();
		}
		String ext = FilenameUtils.getExtension(filename);
		if (!ImageUtils.isValidImageExt(ext)) {
			errors.addErrorCode("imageupload.error.notSupportExt", ext);

			if (logger.isDebugEnabled()) {
				logger.debug("validate(String, MultipartFile, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return errors;
		}
		try {
			if (!ImageUtils.isImage(file.getInputStream())) {
				errors.addErrorCode("imageupload.error.notImage", ext);

				if (logger.isDebugEnabled()) {
					logger.debug("validate(String, MultipartFile, HttpServletRequest) - end"); //$NON-NLS-1$
				}
				return errors;
			}
		} catch (IOException e) {
			logger.error("image upload error", e);
			errors.addErrorCode("imageupload.error.ioError", ext);

			if (logger.isDebugEnabled()) {
				logger.debug("validate(String, MultipartFile, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return errors;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validate(String, MultipartFile, HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return errors;
	}

	
	private FileRepository fileRepository;

	@Autowired
	public void setFileRepository(FileRepository fileRepository) {
		this.fileRepository = fileRepository;
	}


}