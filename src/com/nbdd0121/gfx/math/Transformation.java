package com.nbdd0121.gfx.math;

public class Transformation {

	public static Matrix4d translation(Vector3d offset) {
		return new Matrix4d(1, 0, 0, offset.x, 0, 1, 0, offset.y, 0, 0, 1,
				offset.z);
	}

	public static Matrix4d scale(Vector3d scale) {
		return new Matrix4d(scale.x, 0, 0, 0, scale.y, 0, 0, 0, scale.z);
	}

	public static Matrix4d scale(double scale) {
		return new Matrix4d(scale, 0, 0, 0, scale, 0, 0, 0, scale);
	}

	public static Matrix4d rotateY(double rad) {
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);
		return new Matrix4d(cos, 0, -sin, 0, 1, 0, sin, 0, cos);
	}

	public static Matrix4d rotateZ(double rad) {
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);
		return new Matrix4d(cos, -sin, 0, sin, cos, 0, 0, 0, 1);
	}

}
