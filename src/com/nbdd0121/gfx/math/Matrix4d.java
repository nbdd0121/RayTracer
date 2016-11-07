package com.nbdd0121.gfx.math;

public class Matrix4d {

	public final double m00, m01, m02, m03;
	public final double m10, m11, m12, m13;
	public final double m20, m21, m22, m23;
	public final double m30, m31, m32, m33;

	public Matrix4d(double m00, double m01, double m02, double m03, double m10,
			double m11, double m12, double m13, double m20, double m21,
			double m22, double m23, double m30, double m31, double m32,
			double m33) {
		this.m00 = m00;
		this.m01 = m01;
		this.m02 = m02;
		this.m03 = m03;
		this.m10 = m10;
		this.m11 = m11;
		this.m12 = m12;
		this.m13 = m13;
		this.m20 = m20;
		this.m21 = m21;
		this.m22 = m22;
		this.m23 = m23;
		this.m30 = m30;
		this.m31 = m31;
		this.m32 = m32;
		this.m33 = m33;
	}

	public Matrix4d(double m00, double m01, double m02, double m03, double m10,
			double m11, double m12, double m13, double m20, double m21,
			double m22, double m23) {
		this(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, 0, 0,
				0, 1);
	}

	public Matrix4d(double m00, double m01, double m02, double m10, double m11,
			double m12, double m20, double m21, double m22) {
		this(m00, m01, m02, 0, m10, m11, m12, 0, m20, m21, m22, 0, 0, 0, 0, 1);
	}

	public Vector4d multiply(Vector4d vec) {
		return new Vector4d(
				m00 * vec.x + m01 * vec.y + m02 * vec.z + m03 * vec.w,
				m10 * vec.x + m11 * vec.y + m12 * vec.z + m13 * vec.w,
				m20 * vec.x + m21 * vec.y + m22 * vec.z + m23 * vec.w,
				m30 * vec.x + m31 * vec.y + m32 * vec.z + m33 * vec.w);
	}

	public Vector4d multiply(Vector3d vec) {
		return new Vector4d(m00 * vec.x + m01 * vec.y + m02 * vec.z + m03,
				m10 * vec.x + m11 * vec.y + m12 * vec.z + m13,
				m20 * vec.x + m21 * vec.y + m22 * vec.z + m23,
				m30 * vec.x + m31 * vec.y + m32 * vec.z + m33);
	}

	public Matrix4d multiply(Matrix4d mat) {
		return new Matrix4d(
				m00 * mat.m00 + m01 * mat.m10 + m02 * mat.m20 + m03 * mat.m30,
				m00 * mat.m01 + m01 * mat.m11 + m02 * mat.m21 + m03 * mat.m31,
				m00 * mat.m02 + m01 * mat.m12 + m02 * mat.m22 + m03 * mat.m32,
				m00 * mat.m03 + m01 * mat.m13 + m02 * mat.m23 + m03 * mat.m33,

				m10 * mat.m00 + m11 * mat.m10 + m12 * mat.m20 + m13 * mat.m30,
				m10 * mat.m01 + m11 * mat.m11 + m12 * mat.m21 + m13 * mat.m31,
				m10 * mat.m02 + m11 * mat.m12 + m12 * mat.m22 + m13 * mat.m32,
				m10 * mat.m03 + m11 * mat.m13 + m12 * mat.m23 + m13 * mat.m33,

				m20 * mat.m00 + m21 * mat.m10 + m22 * mat.m20 + m23 * mat.m30,
				m20 * mat.m01 + m21 * mat.m11 + m22 * mat.m21 + m23 * mat.m31,
				m20 * mat.m02 + m21 * mat.m12 + m22 * mat.m22 + m23 * mat.m32,
				m20 * mat.m03 + m21 * mat.m13 + m22 * mat.m23 + m23 * mat.m33,

				m30 * mat.m00 + m31 * mat.m10 + m32 * mat.m20 + m33 * mat.m30,
				m30 * mat.m01 + m31 * mat.m11 + m32 * mat.m21 + m33 * mat.m31,
				m30 * mat.m02 + m31 * mat.m12 + m32 * mat.m22 + m33 * mat.m32,
				m30 * mat.m03 + m31 * mat.m13 + m32 * mat.m23 + m33 * mat.m33);
	}

	public double determinant() {
		return (m00 * m11 - m01 * m10) * (m22 * m33 - m23 * m32)
				- (m00 * m12 - m02 * m10) * (m21 * m33 - m23 * m31)
				+ (m00 * m13 - m03 * m10) * (m21 * m32 - m22 * m31)
				+ (m01 * m12 - m02 * m11) * (m20 * m33 - m23 * m30)
				- (m01 * m13 - m03 * m11) * (m20 * m32 - m22 * m30)
				+ (m02 * m13 - m03 * m12) * (m20 * m31 - m21 * m30);
	}

	public Matrix4d inverse() {
		double det = determinant();
		if (det == 0) {
			throw new RuntimeException("Singular matrix");
		}
		double invDet = 1 / det;
		return new Matrix4d(
				m11 * (m22 * m33 - m23 * m32) + m12 * (m23 * m31 - m21 * m33)
						+ m13 * (m21 * m32 - m22 * m31) * invDet,
				m21 * (m02 * m33 - m03 * m32) + m22 * (m03 * m31 - m01 * m33)
						+ m23 * (m01 * m32 - m02 * m31) * invDet,
				m31 * (m02 * m13 - m03 * m12) + m32 * (m03 * m11 - m01 * m13)
						+ m33 * (m01 * m12 - m02 * m11) * invDet,
				m01 * (m13 * m22 - m12 * m23) + m02 * (m11 * m23 - m13 * m21)
						+ m03 * (m12 * m21 - m11 * m22) * invDet,

				m12 * (m20 * m33 - m23 * m30) + m13 * (m22 * m30 - m20 * m32)
						+ m10 * (m23 * m32 - m22 * m33) * invDet,
				m22 * (m00 * m33 - m03 * m30) + m23 * (m02 * m30 - m00 * m32)
						+ m20 * (m03 * m32 - m02 * m33) * invDet,
				m32 * (m00 * m13 - m03 * m10) + m33 * (m02 * m10 - m00 * m12)
						+ m30 * (m03 * m12 - m02 * m13) * invDet,
				m02 * (m13 * m20 - m10 * m23) + m03 * (m10 * m22 - m12 * m20)
						+ m00 * (m12 * m23 - m13 * m22) * invDet,

				m13 * (m20 * m31 - m21 * m30) + m10 * (m21 * m33 - m23 * m31)
						+ m11 * (m23 * m30 - m20 * m33) * invDet,
				m23 * (m00 * m31 - m01 * m30) + m20 * (m01 * m33 - m03 * m31)
						+ m21 * (m03 * m30 - m00 * m33) * invDet,
				m33 * (m00 * m11 - m01 * m10) + m30 * (m01 * m13 - m03 * m11)
						+ m31 * (m03 * m10 - m00 * m13) * invDet,
				m03 * (m11 * m20 - m10 * m21) + m00 * (m13 * m21 - m11 * m23)
						+ m01 * (m10 * m23 - m13 * m20) * invDet,

				m10 * (m22 * m31 - m21 * m32) + m11 * (m20 * m32 - m22 * m30)
						+ m12 * (m21 * m30 - m20 * m31) * invDet,
				m20 * (m02 * m31 - m01 * m32) + m21 * (m00 * m32 - m02 * m30)
						+ m22 * (m01 * m30 - m00 * m31) * invDet,
				m30 * (m02 * m11 - m01 * m12) + m31 * (m00 * m12 - m02 * m10)
						+ m32 * (m01 * m10 - m00 * m11) * invDet,
				m00 * (m11 * m22 - m12 * m21) + m01 * (m12 * m20 - m10 * m22)
						+ m02 * (m10 * m21 - m11 * m20) * invDet);
	}

	public Matrix4d transpose() {
		return new Matrix4d(m00, m10, m20, m30, m01, m11, m21, m31, m02, m12,
				m22, m32, m03, m13, m23, m33);
	}
}
