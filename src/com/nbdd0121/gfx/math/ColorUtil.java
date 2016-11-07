package com.nbdd0121.gfx.math;

public class ColorUtil {

	public static Vector3d toVector(int rgb) {
		int r = (rgb >> 16) & 0xFF;
		int g = (rgb >> 8) & 0xFF;
		int b = rgb & 0xFF;
		return new Vector3d(r / 255., g / 255., b / 255.);
	}

}
