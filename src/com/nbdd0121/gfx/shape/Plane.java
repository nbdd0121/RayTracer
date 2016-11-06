package com.nbdd0121.gfx.shape;

import com.nbdd0121.gfx.BoundingBox;
import com.nbdd0121.gfx.Ray;
import com.nbdd0121.gfx.RaycastHit;
import com.nbdd0121.gfx.math.Vector2d;
import com.nbdd0121.gfx.math.Vector3d;

public class Plane extends Shape {

	private Vector3d normal;
	private double distance;

	public Plane(Vector3d normal, double distance) {
		this.normal = normal.normalize();
		this.distance = distance;

	}

	@Override
	public RaycastHit intersectionWith(Ray ray, double maxDistance) {
		double s = -(ray.getOrigin().dot(normal) + distance)
				/ (ray.getDirection().dot(normal));
		
		if (s <= 0 || s >= maxDistance)
			return null;
		Vector3d point = ray.evaluateAt(s);
		return new RaycastHit(this, s, point, normal);
	}

	@Override
	public Vector3d getNormalAt(Vector3d position) {
		return normal;
	}

	@Override
	public BoundingBox getBoundingBox() {
		// Not representable by bounding box
		return null;
	}

	@Override
	public Vector2d getUVMapping(Vector3d point) {
		return Vector2d.ZERO;
	}
}