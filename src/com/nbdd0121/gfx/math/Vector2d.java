package com.nbdd0121.gfx.math;

import java.util.Objects;

public class Vector2d {
	public static final Vector2d ZERO = new Vector2d(0);
	public static final Vector2d ONE = new Vector2d(1);

	public final double x, y;

	public Vector2d(double uniform) {
		this(uniform, uniform);
	}

	public Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector2d add(Vector2d other) {
		return new Vector2d(x + other.x, y + other.y);
	}

	public Vector2d subtract(Vector2d other) {
		return new Vector2d(x - other.x, y - other.y);
	}

	public Vector2d scale(double scalar) {
		return new Vector2d(scalar * x, scalar * y);
	}

	public Vector2d inverseScale(double scalar) {
		return this.scale(1 / scalar);
	}

	// Hadamard product, scales the vector in an element-wise fashion
	public Vector2d scale(Vector2d other) {
		return new Vector2d(x * other.x, y * other.y);
	}

	public double dot(Vector2d other) {
		return x * other.x + y * other.y;
	}

	public double magnitude() {
		return Math.sqrt(x * x + y * y);
	}

	public double magnitudeSquared() {
		return x * x + y * y;
	}

	public Vector2d elementwiseSquare() {
		return this.scale(this);
	}

	public Vector2d normalize() {
		double magnitude = this.magnitude();
		return new Vector2d(x / magnitude, y / magnitude);
	}

	public boolean equals(Vector2d other) {
		return x == other.x && y == other.y;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Vector2d) {
			return equals((Vector2d) other);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	public Vector2d negate() {
		return new Vector2d(-x, -y);
	}

	@Override
	public String toString() {
		return "Vector(" + x + ", " + y + ")";
	}
}
