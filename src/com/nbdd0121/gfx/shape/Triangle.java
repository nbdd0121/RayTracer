package com.nbdd0121.gfx.shape;

import com.nbdd0121.gfx.BoundingBox;
import com.nbdd0121.gfx.Ray;
import com.nbdd0121.gfx.RaycastHit;
import com.nbdd0121.gfx.math.MathUtil;
import com.nbdd0121.gfx.math.Vector2d;
import com.nbdd0121.gfx.math.Vector3d;

public class Triangle extends Shape {

	private Vector3d a, b, c;
	private Vector3d ab, ac;
	private Vector3d normal;
	private BoundingBox box;

	public Triangle(Vector3d a, Vector3d b, Vector3d c) {
		this.a = a;
		this.b = b;
		this.c = c;

		ab = b.subtract(a);
		ac = c.subtract(a);
		normal = ab.cross(ac).normalize();

		box = new BoundingBox(MathUtil.min(a.x, b.x, c.x),
				MathUtil.min(a.y, b.y, c.y), MathUtil.min(a.z, b.z, c.z),
				MathUtil.max(a.x, b.x, c.x), MathUtil.max(a.y, b.y, c.y),
				MathUtil.max(a.z, b.z, c.z));
	}

	@Override
	public Vector3d getNormalAt(Vector3d point) {
		return normal;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return box;
	}

	@Override
	public RaycastHit intersectionWith(Ray ray, double maxDistance) {
		// This implements Moller¨CTrumbore intersection algorithm

		// P = D x E2
		Vector3d P = ray.getDirection().cross(ac);

		// det = E1.P
		double det = ab.dot(P);

		// If determinant is near zero, ray lies in plane of triangle or ray is
		// parallel to plane of triangle
		if (Math.abs(det) < 0.00001) {
			return null;
		}

		double invDet = 1 / det;

		Vector3d T = ray.getOrigin().subtract(a);

		// Calculate u and make sure u <= 1
		double u = T.dot(P) * invDet;
		if (u < 0 || u > 1) {
			return null;
		}

		Vector3d Q = T.cross(ab);

		// Calculate v and make sure u + v <= 1
		double v = ray.getDirection().dot(Q) * invDet;
		if (v < 0 || u + v > 1) {
			return null;
		}

		// Calculate t, scale parameters, ray intersects triangle
		double t = ac.dot(Q) * invDet;

		if (t > 0.00001 && t < maxDistance) {
			return new RaycastHit(this, t, ray.evaluateAt(t),
					det > 0 ? normal : normal.negate());
		} else {
			return null;
		}
	}

	@Override
	public Vector2d getUVMapping(Vector3d point) {
		Vector3d pa = a.subtract(point);
		Vector3d pb = b.subtract(point);
		Vector3d pc = c.subtract(point);

		double invK = 1 / pa.subtract(pb).cross(pa.subtract(pc)).magnitude();
		double ka = pb.cross(pc).magnitude() * invK;
		double kb = pc.cross(pa).magnitude() * invK;
		double kc = pa.cross(pb).magnitude() * invK;

		return Vector2d.ZERO.scale(ka).add(Vector2d.ONE.scale(kc))
				.add(new Vector2d(0, 1).scale(kb));
	}

}
