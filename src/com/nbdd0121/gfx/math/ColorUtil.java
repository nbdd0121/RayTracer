package com.nbdd0121.gfx.math;

public class ColorUtil {

	private static int convertToByte(double value) {
		return (int)Math.round(255 * MathUtil.clamp(value, 0, 1));
	}

	public static Vector3d toVector(int rgb) {
		int r = (rgb >> 16) & 0xFF;
		int g = (rgb >> 8) & 0xFF;
		int b = rgb & 0xFF;
		return new Vector3d(r / 255., g / 255., b / 255.);
	}

	public static int toRGB(Vector3d vec) {
		return convertToByte(vec.x) << 16 | convertToByte(vec.y) << 8
				| convertToByte(vec.z) << 0;
	}

}
