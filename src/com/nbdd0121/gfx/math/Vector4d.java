package com.nbdd0121.gfx.math;

import java.util.Objects;

public class Vector4d {
	public static final Vector4d ZERO = new Vector4d(0, 0, 0, 0);
	public static final Vector4d W = new Vector4d(0, 0, 0, 1);
	public static final Vector4d ONE = new Vector4d(1, 1, 1, 1);

	public final double x, y, z, w;

	public Vector4d(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Vector4d(Vector3d vec, double w) {
		this(vec.x, vec.y, vec.z, w);
	}

	public Vector4d add(Vector4d other) {
		return new Vector4d(x + other.x, y + other.y, z + other.z, w + other.w);
	}

	public Vector4d subtract(Vector4d other) {
		return new Vector4d(x - other.x, y - other.y, z - other.z, w - other.w);
	}

	public Vector4d scale(double scalar) {
		return new Vector4d(scalar * x, scalar * y, scalar * z, scalar * w);
	}

	public Vector4d inverseScale(double scalar) {
		return this.scale(1 / scalar);
	}

	// Hadamard product, scales the vector in an element-wise fashion
	public Vector4d scale(Vector4d other) {
		return new Vector4d(x * other.x, y * other.y, z * other.z, w * other.w);
	}

	public double dot(Vector4d other) {
		return x * other.x + y * other.y + z * other.z + w * other.w;
	}

	public double magnitude() {
		return Math.sqrt(magnitudeSquared());
	}

	public double magnitudeSquared() {
		return dot(this);
	}

	public Vector4d elementwiseSquare() {
		return this.scale(this);
	}

	public Vector4d normalize() {
		return this.inverseScale(this.magnitude());
	}

	public boolean equals(Vector4d other) {
		return x == other.x && y == other.y && z == other.z && w == other.w;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Vector4d) {
			return equals((Vector4d) other);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z, w);
	}

	public Vector4d negate() {
		return new Vector4d(-x, -y, -z, -w);
	}

	@Override
	public String toString() {
		return "Vector(" + x + ", " + y + ", " + z + ", " + w + ")";
	}
}
