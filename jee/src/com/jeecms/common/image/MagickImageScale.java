package com.jeecms.common.image;

import org.apache.log4j.Logger;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import magick.CompositeOperator;
import magick.DrawInfo;
import magick.ImageInfo;
import magick.MagickException;
import magick.MagickImage;
import magick.PixelPacket;

import org.apache.commons.io.FileUtils;

import com.jeecms.common.image.ImageUtils.Position;

public class MagickImageScale {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MagickImageScale.class);

	/**
	 * 缩小图片
	 * 
	 * @param srcFile
	 *            原图片
	 * @param destFile
	 *            目标图片
	 * @param boxWidth
	 *            缩略图最大宽度
	 * @param boxHeight
	 *            缩略图最大高度
	 * @throws IOException
	 * @throws MagickException
	 */
	public static void resizeFix(File srcFile, File destFile, int boxWidth,
			int boxHeight) throws IOException, MagickException {
		if (logger.isDebugEnabled()) {
			logger.debug("resizeFix(File, File, int, int) - start"); //$NON-NLS-1$
		}

		ImageInfo info = new ImageInfo(srcFile.getAbsolutePath());
		MagickImage image = new MagickImage(info);
		// 计算缩小宽高
		Dimension dim = image.getDimension();
		int width = (int) dim.getWidth();
		int height = (int) dim.getHeight();
		int zoomWidth;
		int zoomHeight;
		if ((float) width / height > (float) boxWidth / boxHeight) {
			zoomWidth = boxWidth;
			zoomHeight = Math.round((float) boxWidth * height / width);
		} else {
			zoomWidth = Math.round((float) boxHeight * width / height);
			zoomHeight = boxHeight;
		}
		// 缩小
		MagickImage scaled = image.scaleImage(zoomWidth, zoomHeight);
		// 输出
		scaled.setFileName(destFile.getAbsolutePath());
		scaled.writeImage(info);
		scaled.destroyImages();

		if (logger.isDebugEnabled()) {
			logger.debug("resizeFix(File, File, int, int) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 裁剪并缩小
	 * 
	 * @param srcFile
	 *            原文件
	 * @param destFile
	 *            目标文件
	 * @param boxWidth
	 *            缩略图最大宽度
	 * @param boxHeight
	 *            缩略图最大高度
	 * @param cutTop
	 *            裁剪TOP
	 * @param cutLeft
	 *            裁剪LEFT
	 * @param cutWidth
	 *            裁剪宽度
	 * @param catHeight
	 *            裁剪高度
	 * @throws IOException
	 * @throws MagickException
	 */
	public static void resizeFix(File srcFile, File destFile, int boxWidth,
			int boxHeight, int cutTop, int cutLeft, int cutWidth, int catHeight)
			throws IOException, MagickException {
		if (logger.isDebugEnabled()) {
			logger.debug("resizeFix(File, File, int, int, int, int, int, int) - start"); //$NON-NLS-1$
		}

		ImageInfo info = new ImageInfo(srcFile.getAbsolutePath());
		MagickImage image = new MagickImage(info);
		// 剪切
		Rectangle rect = new Rectangle(cutTop, cutLeft, cutWidth, catHeight);
		// 计算压缩宽高
		MagickImage cropped = image.cropImage(rect);
		Dimension dim = cropped.getDimension();
		int width = (int) dim.getWidth();
		int height = (int) dim.getHeight();
		int zoomWidth;
		int zoomHeight;
		if ((float) width / height > (float) boxWidth / boxHeight) {
			zoomWidth = boxWidth;
			zoomHeight = Math.round((float) boxWidth * height / width);
		} else {
			zoomWidth = Math.round((float) boxHeight * width / height);
			zoomHeight = boxHeight;
		}
		// 缩小
		MagickImage scaled = cropped.scaleImage(zoomWidth, zoomHeight);
		// 输出
		scaled.setFileName(destFile.getAbsolutePath());
		scaled.writeImage(info);
		scaled.destroyImages();

		if (logger.isDebugEnabled()) {
			logger.debug("resizeFix(File, File, int, int, int, int, int, int) - end"); //$NON-NLS-1$
		}
	}

	public static void imageMark(File srcFile, File destFile, int minWidth,
			int minHeight, int pos, int offsetX, int offsetY,
			String markContent, Color markColor, int markSize, int alpha)
			throws IOException, MagickException {
		if (logger.isDebugEnabled()) {
			logger.debug("imageMark(File, File, int, int, int, int, int, String, Color, int, int) - start"); //$NON-NLS-1$
		}

		ImageInfo info = new ImageInfo(srcFile.getAbsolutePath());
		MagickImage image = new MagickImage(info);

		Dimension dim = image.getDimension();
		int width = (int) dim.getWidth();
		int height = (int) dim.getHeight();
		if (width < minWidth || height < minHeight) {
			image.destroyImages();
			if (!srcFile.equals(destFile)) {
				FileUtils.copyFile(srcFile, destFile);
			}
		} else {
			imageMark(image, info, width, height, pos, offsetX, offsetY,
					markContent, markColor, markSize, alpha);
			image.setFileName(destFile.getAbsolutePath());
			image.writeImage(info);
			image.destroyImages();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("imageMark(File, File, int, int, int, int, int, String, Color, int, int) - end"); //$NON-NLS-1$
		}
	}

	public static void imageMark(File srcFile, File destFile, int minWidth,
			int minHeight, int pos, int offsetX, int offsetY, File markFile)
			throws IOException, MagickException {
		if (logger.isDebugEnabled()) {
			logger.debug("imageMark(File, File, int, int, int, int, int, File) - start"); //$NON-NLS-1$
		}

		ImageInfo info = new ImageInfo(srcFile.getAbsolutePath());
		MagickImage image = new MagickImage(info);

		Dimension dim = image.getDimension();
		int width = (int) dim.getWidth();
		int height = (int) dim.getHeight();
		if (width < minWidth || height < minHeight) {
			image.destroyImages();
			if (!srcFile.equals(destFile)) {
				FileUtils.copyFile(srcFile, destFile);
			}
		} else {
			imageMark(image, info, width, height, pos, offsetX, offsetY,
					markFile);
			image.setFileName(destFile.getAbsolutePath());
			image.writeImage(info);
			image.destroyImages();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("imageMark(File, File, int, int, int, int, int, File) - end"); //$NON-NLS-1$
		}
	}

	private static void imageMark(MagickImage image, ImageInfo info, int width,
			int height, int pos, int offsetX, int offsetY, String text,
			Color color, int size, int alpha) throws MagickException {
		if (logger.isDebugEnabled()) {
			logger.debug("imageMark(MagickImage, ImageInfo, int, int, int, int, int, String, Color, int, int) - start"); //$NON-NLS-1$
		}

		Position p = ImageUtils.markPosition(width, height, pos, offsetX,
				offsetY);
		DrawInfo draw = new DrawInfo(info);
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		draw.setFill(new PixelPacket(r * r, g * g, b * b,
				65535 - (alpha * 65535 / 100)));
		draw.setPointsize(size);
		draw.setTextAntialias(true);
		draw.setText(text);
		draw.setGeometry("+" + p.getX() + "+" + p.getY());
		image.annotateImage(draw);

		if (logger.isDebugEnabled()) {
			logger.debug("imageMark(MagickImage, ImageInfo, int, int, int, int, int, String, Color, int, int) - end"); //$NON-NLS-1$
		}
	}

	private static void imageMark(MagickImage image, ImageInfo info, int width,
			int height, int pos, int offsetX, int offsetY, File markFile)
			throws MagickException {
		if (logger.isDebugEnabled()) {
			logger.debug("imageMark(MagickImage, ImageInfo, int, int, int, int, int, File) - start"); //$NON-NLS-1$
		}

		Position p = ImageUtils.markPosition(width, height, pos, offsetX,
				offsetY);
		MagickImage mark = new MagickImage(new ImageInfo(markFile
				.getAbsolutePath()));
		image.compositeImage(CompositeOperator.AtopCompositeOp, mark, p.getX(),
				p.getY());
		mark.destroyImages();

		if (logger.isDebugEnabled()) {
			logger.debug("imageMark(MagickImage, ImageInfo, int, int, int, int, int, File) - end"); //$NON-NLS-1$
		}
	}

	public static void main(String[] args) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - start"); //$NON-NLS-1$
		}

		long time = System.currentTimeMillis();
		MagickImageScale.resizeFix(
				new File("test/com/jeecms/common/util/1.bmp"), new File(
						"test/com/jeecms/common/util/1-n-3.bmp"), 310, 310, 50,
				50, 320, 320);
		time = System.currentTimeMillis() - time;
		System.out.println("resize new img in " + time + "ms");

		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - end"); //$NON-NLS-1$
		}
	}
}
