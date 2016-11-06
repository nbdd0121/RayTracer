package com.nbdd0121.gfx.texture;

import java.awt.image.BufferedImage;

import com.nbdd0121.gfx.math.Vector3d;

public class ImageTexture extends Texture {

	BufferedImage img;

	public ImageTexture(BufferedImage img) {
		this.img = img;
	}

	@Override
	public Vector3d getColor(double u, double v) {
		int x = (int) (u * img.getWidth());
		if (x >= img.getWidth())
			x = img.getWidth() - 1;
		int y = (int) ((1 - v) * img.getHeight());
		if (y >= img.getHeight())
			y = img.getHeight() - 1;
		int rgb = img.getRGB(x, y);
		int r = (rgb >> 16) & 0xFF;
		int g = (rgb >> 8) & 0xFF;
		int b = rgb & 0xFF;
		return new Vector3d(r / 255., g / 255., b / 255.);
	}

}
