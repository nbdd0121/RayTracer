package com.nbdd0121.gfx.math;

public class MathUtil {

	public static double max(double a, double b, double c) {
		return Math.max(Math.max(a, b), c);
	}

	public static double min(double a, double b, double c) {
		return Math.min(Math.min(a, b), c);
	}

	public static int clamp(int v, int min, int max) {
		if (v < min)
			v = min;
		if (v > max)
			v = max;
		return v;
	}

}
