package com.nbdd0121.gfx.texture;

import java.awt.image.BufferedImage;

import com.nbdd0121.gfx.math.MathUtil;
import com.nbdd0121.gfx.math.Vector3d;

public class ImageTexture extends Texture {

	BufferedImage img;

	public ImageTexture(BufferedImage img) {
		this.img = img;
	}

	@Override
	public Vector3d getColor(double u, double v) {
		int x = MathUtil.clamp((int) (u * img.getWidth()), 0,
				img.getWidth() - 1);
		int y = MathUtil.clamp((int) ((1 - v) * img.getHeight()), 0,
				img.getHeight() - 1);
		int rgb = img.getRGB(x, y);
		int r = (rgb >> 16) & 0xFF;
		int g = (rgb >> 8) & 0xFF;
		int b = rgb & 0xFF;
		return new Vector3d(r / 255., g / 255., b / 255.);
	}

}
