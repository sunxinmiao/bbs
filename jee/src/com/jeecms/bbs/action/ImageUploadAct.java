package com.jeecms.bbs.action;

import org.apache.log4j.Logger;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
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

import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.common.image.ImageScale;
import com.jeecms.common.image.ImageUtils;
import com.jeecms.common.upload.FileRepository;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.Ftp;
import com.jeecms.core.entity.MarkConfig;
import com.jeecms.core.manager.DbFileMng;
import com.jeecms.core.web.WebErrors;

@Controller
public class ImageUploadAct {
	private static final Logger logger = Logger.getLogger(ImageUploadAct.class);
	/**
	 * 结果页
	 */
	private static final String RESULT_PAGE = "/common/iframe_upload";
	/**
	 * 错误信息参数
	 */
	public static final String ERROR = "error";

	@RequestMapping("/common/o_upload_image.do")
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
		if (errors.hasErrors()) {
			model.addAttribute(ERROR, errors.getErrors().get(0));

			if (logger.isDebugEnabled()) {
				logger.debug("execute(String, Integer, Boolean, MultipartFile, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return RESULT_PAGE;
		}
		CmsSite site = CmsUtils.getSite(request);
		MarkConfig conf = site.getConfig().getMarkConfig();
		if (mark == null) {
			mark = conf.getOn();
		}

		String origName = file.getOriginalFilename();
		String ext = FilenameUtils.getExtension(origName).toLowerCase(
				Locale.ENGLISH);
		try {
			String fileUrl;
			if (site.getConfig().getUploadToDb()) {
				String dbFilePath = site.getConfig().getDbFileUri();
				if (!StringUtils.isBlank(filename)) {
					filename = filename.substring(dbFilePath.length());
					if (mark) {
						File tempFile = mark(file, conf);
						fileUrl = dbFileMng.storeByFilename(filename,
								new FileInputStream(tempFile));
						tempFile.delete();
					} else {
						fileUrl = dbFileMng.storeByFilename(filename, file
								.getInputStream());
					}
				} else {
					if (mark) {
						File tempFile = mark(file, conf);
						fileUrl = dbFileMng.storeByExt(site.getUploadPath(),
								ext, new FileInputStream(tempFile));
						tempFile.delete();
					} else {
						fileUrl = dbFileMng.storeByExt(site.getUploadPath(),
								ext, file.getInputStream());
					}
					// 加上访问地址
					fileUrl = request.getContextPath() + dbFilePath + fileUrl;
				}
			} else if (site.getUploadFtp() != null) {
				Ftp ftp = site.getUploadFtp();
				String ftpUrl = ftp.getUrl();
				if (!StringUtils.isBlank(filename)) {
					filename = filename.substring(ftpUrl.length());
					if (mark) {
						File tempFile = mark(file, conf);
						fileUrl = ftp.storeByFilename(filename,
								new FileInputStream(tempFile));
						tempFile.delete();
					} else {
						fileUrl = ftp.storeByFilename(filename, file
								.getInputStream());
					}
				} else {
					if (mark) {
						File tempFile = mark(file, conf);
						fileUrl = ftp.storeByExt(site.getUploadPath(), ext,
								new FileInputStream(tempFile));
						tempFile.delete();
					} else {
						fileUrl = ftp.storeByExt(site.getUploadPath(), ext,
								file.getInputStream());
					}
					// 加上url前缀
					fileUrl = ftpUrl + fileUrl;
				}
			} else {
				String ctx = request.getContextPath();
				if (!StringUtils.isBlank(filename)) {
					filename = filename.substring(ctx.length());
					if (mark) {
						File tempFile = mark(file, conf);
						fileUrl = fileRepository.storeByFilename(filename,
								tempFile);
						tempFile.delete();
					} else {
						fileUrl = fileRepository
								.storeByFilename(filename, file);
					}
				} else {
					if (mark) {
						File tempFile = mark(file, conf);
						fileUrl = fileRepository.storeByExt(site
								.getUploadPath(), ext, tempFile);
						tempFile.delete();
					} else {
						fileUrl = fileRepository.storeByExt(site
								.getUploadPath(), ext, file);
					}
					// 加上部署路径
					fileUrl = ctx + fileUrl;
				}
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

		if (logger.isDebugEnabled()) {
			logger.debug("execute(String, Integer, Boolean, MultipartFile, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return RESULT_PAGE;
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

	private File mark(MultipartFile file, MarkConfig conf) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("mark(MultipartFile, MarkConfig) - start"); //$NON-NLS-1$
		}

		String path = System.getProperty("java.io.tmpdir");
		File tempFile = new File(path, String.valueOf(System
				.currentTimeMillis()));
		file.transferTo(tempFile);
		boolean imgMark = !StringUtils.isBlank(conf.getImagePath());
		if (imgMark) {
			File markImg = new File(realPathResolver.get(conf.getImagePath()));
			imageScale.imageMark(tempFile, tempFile, conf.getMinWidth(), conf
					.getMinHeight(), conf.getPos(), conf.getOffsetX(), conf
					.getOffsetY(), markImg);
		} else {
			imageScale.imageMark(tempFile, tempFile, conf.getMinWidth(), conf
					.getMinHeight(), conf.getPos(), conf.getOffsetX(), conf
					.getOffsetY(), conf.getContent(), Color.decode(conf
					.getColor()), conf.getSize(), conf.getAlpha());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("mark(MultipartFile, MarkConfig) - end"); //$NON-NLS-1$
		}
		return tempFile;
	}

	private FileRepository fileRepository;
	private DbFileMng dbFileMng;
	private ImageScale imageScale;
	private RealPathResolver realPathResolver;

	@Autowired
	public void setFileRepository(FileRepository fileRepository) {
		this.fileRepository = fileRepository;
	}

	@Autowired
	public void setDbFileMng(DbFileMng dbFileMng) {
		this.dbFileMng = dbFileMng;
	}

	@Autowired
	public void setImageScale(ImageScale imageScale) {
		this.imageScale = imageScale;
	}

	@Autowired
	public void setRealPathResolver(RealPathResolver realPathResolver) {
		this.realPathResolver = realPathResolver;
	}
}