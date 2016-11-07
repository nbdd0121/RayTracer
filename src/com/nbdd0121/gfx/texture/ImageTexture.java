package com.nbdd0121.gfx.texture;

import java.awt.image.BufferedImage;

import com.nbdd0121.gfx.math.ColorUtil;
import com.nbdd0121.gfx.math.MathUtil;
import com.nbdd0121.gfx.math.Vector3d;

public class ImageTexture extends Texture {

	BufferedImage img;
	int width;
	int height;

	public ImageTexture(BufferedImage img) {
		this.img = img;
		this.width = img.getWidth();
		this.height = img.getHeight();
	}

	@Override
	public Vector3d getColor(double u, double v) {
		// View image as grids with center from [0, width) x [0, height)
		double x = u * width;
		double y = (1 - v) * height;

		int xtl = (int) x;
		int ytl = (int) y;

		Vector3d ctl = ColorUtil
				.toVector(img.getRGB(MathUtil.positiveMod(xtl, width),
						MathUtil.positiveMod(ytl, height)));
		Vector3d ctr = ColorUtil
				.toVector(img.getRGB(MathUtil.positiveMod(xtl + 1, width),
						MathUtil.positiveMod(ytl, height)));
		Vector3d cbl = ColorUtil
				.toVector(img.getRGB(MathUtil.positiveMod(xtl, width),
						MathUtil.positiveMod(ytl + 1, height)));
		Vector3d cbr = ColorUtil
				.toVector(img.getRGB(MathUtil.positiveMod(xtl + 1, width),
						MathUtil.positiveMod(ytl + 1, height)));

		double xlen = 1 + xtl - x;
		double ylen = 1 + ytl - y;

		// Area averaging
		return ctl.scale(xlen * ylen).add(ctr.scale((1 - xlen) * ylen))
				.add(cbl.scale(xlen * (1 - ylen)))
				.add(cbr.scale((1 - xlen) * (1 - ylen)));
	}

}
