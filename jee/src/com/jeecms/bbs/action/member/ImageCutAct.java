package com.jeecms.bbs.action.member;

import org.apache.log4j.Logger;

import static com.jeecms.bbs.Constants.TPLDIR_MEMBER;

import java.io.File;

import javax.servlet.http.HttpServletRequest;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.image.ImageScale;
import com.jeecms.common.upload.FileRepository;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.Ftp;
import com.jeecms.core.manager.DbFileMng;

@Controller
public class ImageCutAct {
	private static final Logger logger =Logger.getLogger(ImageCutAct.class);
	/**
	 * 图片选择页面
	 */
	public static final String IMAGE_SELECT_RESULT = "tpl.image_area_select";
	/**
	 * 图片裁剪完成页面
	 */
	public static final String IMAGE_CUTED = "tpl.image_cuted";
	/**
	 * 错误信息参数
	 */
	public static final String ERROR = "error";

	@RequestMapping("/member/v_image_area_select.jspx")
	public String imageAreaSelect(String uploadBase, String imgSrcPath,
			Integer zoomWidth, Integer zoomHeight, Integer uploadNum,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("imageAreaSelect(String, String, Integer, Integer, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		model.addAttribute("uploadBase", uploadBase);
		model.addAttribute("imgSrcPath", imgSrcPath);
		model.addAttribute("zoomWidth", zoomWidth);
		model.addAttribute("zoomHeight", zoomHeight);
		model.addAttribute("uploadNum", uploadNum);
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			String returnString = FrontUtils.showLogin(request, model, site);
			if (logger.isDebugEnabled()) {
				logger.debug("imageAreaSelect(String, String, Integer, Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, IMAGE_SELECT_RESULT);
		if (logger.isDebugEnabled()) {
			logger.debug("imageAreaSelect(String, String, Integer, Integer, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@RequestMapping("/member/o_image_cut.jspx")
	public String imageCut(String imgSrcPath, Integer imgTop, Integer imgLeft,
			Integer imgWidth, Integer imgHeight, Integer reMinWidth,
			Integer reMinHeight, Float imgScale, Integer uploadNum,
			HttpServletRequest request, ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("imageCut(String, Integer, Integer, Integer, Integer, Integer, Integer, Float, Integer, HttpServletRequest, ModelMap) - start"); //$NON-NLS-1$
		}

		CmsSite site = CmsUtils.getSite(request);
		try {
			if (imgWidth > 0) {
				if (site.getConfig().getUploadToDb()) {
					String dbFilePath = site.getConfig().getDbFileUri();
					imgSrcPath = imgSrcPath.substring(dbFilePath.length());
					File file = dbFileMng.retrieve(imgSrcPath);
					imageScale.resizeFix(file, file, reMinWidth, reMinHeight,
							getLen(imgTop, imgScale),
							getLen(imgLeft, imgScale), getLen(imgWidth,
									imgScale), getLen(imgHeight, imgScale));
					dbFileMng.restore(imgSrcPath, file);
				} else if (site.getUploadFtp() != null) {
					Ftp ftp = site.getUploadFtp();
					String ftpUrl = ftp.getUrl();
					imgSrcPath = imgSrcPath.substring(ftpUrl.length());
					File file = ftp.retrieve(imgSrcPath);
					imageScale.resizeFix(file, file, reMinWidth, reMinHeight,
							getLen(imgTop, imgScale),
							getLen(imgLeft, imgScale), getLen(imgWidth,
									imgScale), getLen(imgHeight, imgScale));
					ftp.restore(imgSrcPath, file);
				} else {
					String ctx = request.getContextPath();
					imgSrcPath = imgSrcPath.substring(ctx.length());
					File file = fileRepository.retrieve(imgSrcPath);
					imageScale.resizeFix(file, file, reMinWidth, reMinHeight,
							getLen(imgTop, imgScale),
							getLen(imgLeft, imgScale), getLen(imgWidth,
									imgScale), getLen(imgHeight, imgScale));
				}
			} else {
				if (site.getConfig().getUploadToDb()) {
					String dbFilePath = site.getConfig().getDbFileUri();
					imgSrcPath = imgSrcPath.substring(dbFilePath.length());
					File file = dbFileMng.retrieve(imgSrcPath);
					imageScale.resizeFix(file, file, reMinWidth, reMinHeight);
					dbFileMng.restore(imgSrcPath, file);
				} else if (site.getUploadFtp() != null) {
					Ftp ftp = site.getUploadFtp();
					String ftpUrl = ftp.getUrl();
					imgSrcPath = imgSrcPath.substring(ftpUrl.length());
					File file = ftp.retrieve(imgSrcPath);
					imageScale.resizeFix(file, file, reMinWidth, reMinHeight);
					ftp.restore(imgSrcPath, file);
				} else {
					String ctx = request.getContextPath();
					imgSrcPath = imgSrcPath.substring(ctx.length());
					File file = fileRepository.retrieve(imgSrcPath);
					imageScale.resizeFix(file, file, reMinWidth, reMinHeight);
				}
			}
			model.addAttribute("uploadNum", uploadNum);
		} catch (Exception e) {
			logger.error("cut image error", e);
			model.addAttribute(ERROR, e.getMessage());
		}
		String returnString = FrontUtils.getTplPath(request, site.getSolutionPath(), TPLDIR_MEMBER, IMAGE_CUTED);
		if (logger.isDebugEnabled()) {
			logger.debug("imageCut(String, Integer, Integer, Integer, Integer, Integer, Integer, Float, Integer, HttpServletRequest, ModelMap) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	private int getLen(int len, float imgScale) {
		if (logger.isDebugEnabled()) {
			logger.debug("getLen(int, float) - start"); //$NON-NLS-1$
		}

		int returnint = Math.round(len / imgScale);
		if (logger.isDebugEnabled()) {
			logger.debug("getLen(int, float) - end"); //$NON-NLS-1$
		}
		return returnint;
	}

	private ImageScale imageScale;

	private FileRepository fileRepository;
	private DbFileMng dbFileMng;

	@Autowired
	public void setImageScale(ImageScale imageScale) {
		this.imageScale = imageScale;
	}

	@Autowired
	public void setFileRepository(FileRepository fileRepository) {
		this.fileRepository = fileRepository;
	}

	@Autowired
	public void setDbFileMng(DbFileMng dbFileMng) {
		this.dbFileMng = dbFileMng;
	}

}
