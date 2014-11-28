package com.jeecms.common.image;

import org.apache.log4j.Logger;

import java.awt.Color;
import java.io.File;

import magick.Magick;




/**
 * 图片缩小类
 * 
 * 根据环境情况选择java图片缩小方式或专业的magick图片缩小方式
 * 
 * @author liufang
 * 
 */
public class ImageScaleImpl implements ImageScale {
	private static final Logger logger = Logger.getLogger(ImageScaleImpl.class);

	public void resizeFix(File srcFile, File destFile, int boxWidth,
			int boxHeight) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("resizeFix(File, File, int, int) - start"); //$NON-NLS-1$
		}

		if (isMagick) {
			MagickImageScale.resizeFix(srcFile, destFile, boxWidth, boxHeight);
		} else {
			AverageImageScale.resizeFix(srcFile, destFile, boxWidth, boxHeight);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("resizeFix(File, File, int, int) - end"); //$NON-NLS-1$
		}
	}

	public void resizeFix(File srcFile, File destFile, int boxWidth,
			int boxHeight, int cutTop, int cutLeft, int cutWidth, int catHeight)
			throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("resizeFix(File, File, int, int, int, int, int, int) - start"); //$NON-NLS-1$
		}

		if (isMagick) {
			MagickImageScale.resizeFix(srcFile, destFile, boxWidth, boxHeight,
					cutTop, cutLeft, cutWidth, catHeight);
		} else {
			AverageImageScale.resizeFix(srcFile, destFile, boxWidth, boxHeight,
					cutTop, cutLeft, cutWidth, catHeight);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("resizeFix(File, File, int, int, int, int, int, int) - end"); //$NON-NLS-1$
		}
	}

	public void imageMark(File srcFile, File destFile, int minWidth,
			int minHeight, int pos, int offsetX, int offsetY, String text,
			Color color, int size, int alpha) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("imageMark(File, File, int, int, int, int, int, String, Color, int, int) - start"); //$NON-NLS-1$
		}

		if (isMagick) {
			MagickImageScale.imageMark(srcFile, destFile, minWidth, minHeight,
					pos, offsetX, offsetY, text, color, size, alpha);
		} else {
			AverageImageScale.imageMark(srcFile, destFile, minWidth, minHeight,
					pos, offsetX, offsetY, text, color, size, alpha);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("imageMark(File, File, int, int, int, int, int, String, Color, int, int) - end"); //$NON-NLS-1$
		}
	}

	public void imageMark(File srcFile, File destFile, int minWidth,
			int minHeight, int pos, int offsetX, int offsetY, File markFile)
			throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("imageMark(File, File, int, int, int, int, int, File) - start"); //$NON-NLS-1$
		}

		if (isMagick) {
			MagickImageScale.imageMark(srcFile, destFile, minWidth, minHeight,
					pos, offsetX, offsetY, markFile);
		} else {
			AverageImageScale.imageMark(srcFile, destFile, minWidth, minHeight,
					pos, offsetX, offsetY, markFile);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("imageMark(File, File, int, int, int, int, int, File) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 检查是否安装magick
	 */
	public void init() {
		if (logger.isDebugEnabled()) {
			logger.debug("init() - start"); //$NON-NLS-1$
		}

		if (tryMagick) {
			try {
				System.setProperty("jmagick.systemclassloader", "no");
				new Magick();
				logger.info("using jmagick");
				isMagick = true;
			} catch (Throwable e) {
				logger.warn("load jmagick fail, use java image scale.",
						e);
				isMagick = false;
			}
		} else {
			logger.info("jmagick is disabled.");
			isMagick = false;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("init() - end"); //$NON-NLS-1$
		}
	}

	private boolean isMagick = false;
	private boolean tryMagick = true;

	public void setTryMagick(boolean tryMagick) {
		this.tryMagick = tryMagick;
	}
}
