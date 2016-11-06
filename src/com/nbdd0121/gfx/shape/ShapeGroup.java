package com.nbdd0121.gfx.shape;

import java.util.ArrayList;
import java.util.List;

import com.nbdd0121.gfx.BoundingBox;
import com.nbdd0121.gfx.Ray;
import com.nbdd0121.gfx.RaycastHit;
import com.nbdd0121.gfx.math.Vector2d;
import com.nbdd0121.gfx.math.Vector3d;

public class ShapeGroup extends Shape {

	private List<Shape> shape;
	private BoundingBox box;

	public ShapeGroup() {
		shape = new ArrayList<>();
	}

	public void add(Shape shape) {
		this.shape.add(shape);
	}

	public void build() {
		// Find bounds first
		double xmin = Double.POSITIVE_INFINITY;
		double ymin = Double.POSITIVE_INFINITY;
		double zmin = Double.POSITIVE_INFINITY;
		double xmax = Double.NEGATIVE_INFINITY;
		double ymax = Double.NEGATIVE_INFINITY;
		double zmax = Double.NEGATIVE_INFINITY;
		for (Shape s : shape) {
			BoundingBox bbox = s.getBoundingBox();

			// Unable to build bounding box
			if (bbox == null) {
				return;
			}

			xmin = Math.min(xmin, bbox.getMinX());
			ymin = Math.min(ymin, bbox.getMinY());
			zmin = Math.min(zmin, bbox.getMinZ());
			xmax = Math.max(xmax, bbox.getMaxX());
			ymax = Math.max(ymax, bbox.getMaxY());
			zmax = Math.max(zmax, bbox.getMaxZ());
		}

		box = new BoundingBox(xmin, ymin, zmin, xmax, ymax, zmax);

		// If number of shapes is below threshold, then return
		if (shape.size() < 16) {
			return;
		}

		// Otherwise do a binary partition
		// First pick the axis
		char axis = 'z';
		double smin = zmin, smax = zmax;
		if (ymax - ymin > smax - smin) {
			axis = 'y';
			smax = ymax;
			smin = ymin;
		}
		if (xmax - xmin > smax - smin) {
			axis = 'x';
			smax = xmax;
			smin = xmin;
		}

		// Find the split
		double split = (smax + smin) / 2;

		// Now do the separation
		ShapeGroup l = new ShapeGroup();
		ShapeGroup r = new ShapeGroup();
		for (Shape s : shape) {
			BoundingBox bbox = s.getBoundingBox();

			double min, max;
			switch (axis) {
				case 'x':
					min = bbox.getMinX();
					max = bbox.getMaxX();
					break;
				case 'y':
					min = bbox.getMinY();
					max = bbox.getMaxY();
					break;
				case 'z':
				default:
					min = bbox.getMinZ();
					max = bbox.getMaxZ();
					break;
			}

			if (min < split) {
				l.add(s);
			}
			if (max > split) {
				r.add(s);
			}
		}

		// If we've successfully separate thing apart
		if (l.shape.size() != shape.size() && r.shape.size() != shape.size()) {
			l.build();
			r.build();
			shape.clear();
			shape.add(l);
			shape.add(r);
		}
	}

	@Override
	public RaycastHit intersectionWith(Ray ray, double maxDistance) {
		double d = box.intersects(ray);
		if (d == Double.POSITIVE_INFINITY || d >= maxDistance) {
			return null;
		}

		RaycastHit ret = null;
		for (Shape s : shape) {
			RaycastHit hit = s.intersectionWith(ray, maxDistance);
			if (hit != null) {
				ret = hit;
				maxDistance = hit.getDistance();
			}
		}
		if (ret == null)
			return null;
		return new RaycastHit(this, ret.getDistance(), ret.getLocation(),
				ret.getNormal());
	}

	@Override
	public Vector3d getNormalAt(Vector3d position) {
		throw new UnsupportedOperationException();
	}

	@Override
	public BoundingBox getBoundingBox() {
		return box;
	}

	@Override
	public Vector2d getUVMapping(Vector3d point) {
		return Vector2d.ZERO;
	}
}