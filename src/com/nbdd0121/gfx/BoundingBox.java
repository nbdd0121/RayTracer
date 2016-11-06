package com.nbdd0121.gfx;

import com.nbdd0121.gfx.math.Vector3d;

public class BoundingBox {
	private double minX;
	private double minY;
	private double minZ;
	private double maxX;
	private double maxY;
	private double maxZ;

	public BoundingBox(double minX, double minY, double minZ, double maxX,
			double maxY, double maxZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	public double getMinX() {
		return minX;
	}

	public double getMinY() {
		return minY;
	}

	public double getMinZ() {
		return minZ;
	}

	public double getMaxX() {
		return maxX;
	}

	public double getMaxY() {
		return maxY;
	}

	public double getMaxZ() {
		return maxZ;
	}

	public double intersects(Ray ray) {
		Vector3d origin = ray.getOrigin();
		Vector3d direction = ray.getDirection();
		double x1 = (minX - origin.x) / direction.x;
		double x2 = (maxX - origin.x) / direction.x;
		double y1 = (minY - origin.y) / direction.y;
		double y2 = (maxY - origin.y) / direction.y;
		double z1 = (minZ - origin.z) / direction.z;
		double z2 = (maxZ - origin.z) / direction.z;

		double smin = Math.max(Math.max(Math.min(x1, x2), Math.min(y1, y2)),
				Math.min(z1, z2));
		double smax = Math.min(Math.min(Math.max(x1, x2), Math.max(y1, y2)),
				Math.max(z1, z2));

		if (smax < 0 || smin > smax) {
			return Double.POSITIVE_INFINITY;
		}

		return smin;
	}

}
