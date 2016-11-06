package com.nbdd0121.gfx.shape;

import com.nbdd0121.gfx.BoundingBox;
import com.nbdd0121.gfx.Ray;
import com.nbdd0121.gfx.RaycastHit;
import com.nbdd0121.gfx.math.Vector2d;
import com.nbdd0121.gfx.math.Vector3d;

public class Sphere extends Shape {

	private Vector3d center;
	private double radius;

	public Sphere(Vector3d center, double radius) {
		this.center = center;
		this.radius = radius;
	}

	@Override
	public RaycastHit intersectionWith(Ray ray, double maxDistance) {
		Vector3d O = ray.getOrigin();
		Vector3d D = ray.getDirection();
		Vector3d C = center;
		double r = radius;

		Vector3d OsubC = O.subtract(C);
		double a = D.dot(D);
		double b = 2 * D.dot(OsubC);
		double c = OsubC.dot(OsubC) - Math.pow(r, 2);

		double d = Math.sqrt(b * b - 4 * a * c);

		if (d < 0) {
			return null;
		}

		double s1 = (d - b) / a / 2;
		double s2 = (-b - d) / a / 2;

		if (s1 > 0 && s1 < s2) {
			if (s1 >= maxDistance)
				return null;

			Vector3d point = ray.evaluateAt(s1);
			Vector3d normal = this.getNormalAt(point);
			return new RaycastHit(this, s1, point, normal);
		}

		if (s2 > 0 && s2 < s1) {
			if (s2 >= maxDistance)
				return null;

			Vector3d point = ray.evaluateAt(s2);
			Vector3d normal = this.getNormalAt(point);
			return new RaycastHit(this, s2, point, normal);
		}

		return null;
	}

	@Override
	public Vector3d getNormalAt(Vector3d position) {
		return position.subtract(this.center).normalize();
	}

	public Vector3d getCenter() {
		return center;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(center.x - radius, center.y - radius,
				center.z - radius, center.x + radius, center.y + radius,
				center.z + radius);
	}

	@Override
	public Vector2d getUVMapping(Vector3d point) {
		Vector3d d = center.subtract(point).normalize();
		double u = 0.5 + Math.atan2(d.z, d.x) / 2 / Math.PI;
		double v = 0.5 - Math.asin(d.y) / Math.PI;
		return new Vector2d(u, v);
	}

}