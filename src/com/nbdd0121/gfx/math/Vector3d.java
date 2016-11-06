package com.nbdd0121.gfx.math;

import java.util.Objects;

public class Vector3d {
	public static final Vector3d ZERO = new Vector3d(0);
	public static final Vector3d ONE = new Vector3d(1);

	public final double x, y, z;

	public Vector3d(double uniform) {
		this(uniform, uniform, uniform);
	}

	public Vector3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	private static int convertToByte(double value) {
		return (int) (255 * Math.max(0, Math.min(1, value)));
	}

	public Vector3d add(Vector3d other) {
		return new Vector3d(x + other.x, y + other.y, z + other.z);
	}

	public Vector3d subtract(Vector3d other) {
		return new Vector3d(x - other.x, y - other.y, z - other.z);
	}

	public Vector3d scale(double scalar) {
		return new Vector3d(scalar * x, scalar * y, scalar * z);
	}

	public Vector3d inverseScale(double scalar) {
		return this.scale(1 / scalar);
	}

	// Hadamard product, scales the vector in an element-wise fashion
	public Vector3d scale(Vector3d other) {
		return new Vector3d(x * other.x, y * other.y, z * other.z);
	}

	public double dot(Vector3d other) {
		return x * other.x + y * other.y + z * other.z;
	}

	public Vector3d cross(Vector3d other) {
		return new Vector3d(y * other.z - z * other.y,
				z * other.x - x * other.z, x * other.y - y * other.x);
	}

	public double magnitude() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public double magnitudeSquared() {
		return x * x + y * y + z * z;
	}

	public Vector3d elementwiseSquare() {
		return this.scale(this);
	}

	// Calculate mirror-like reflection
	public Vector3d reflectIn(Vector3d N) {
		return N.scale(2 * this.dot(N)).subtract(this);
	}

	public Vector3d normalize() {
		double magnitude = this.magnitude();
		return new Vector3d(x / magnitude, y / magnitude, z / magnitude);
	}

	public int toRGB() {
		return convertToByte(x) << 16 | convertToByte(y) << 8
				| convertToByte(z) << 0;
	}

	public boolean equals(Vector3d other) {
		return x == other.x && y == other.y && z == other.z;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Vector3d) {
			return equals((Vector3d) other);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}

	public Vector3d negate() {
		return new Vector3d(-x, -y, -z);
	}

	@Override
	public String toString() {
		return "Vector(" + x + ", " + y + ", " + z + ")";
	}
}
