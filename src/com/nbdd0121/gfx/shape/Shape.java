package com.nbdd0121.gfx.shape;

import com.nbdd0121.gfx.BoundingBox;
import com.nbdd0121.gfx.Material;
import com.nbdd0121.gfx.Ray;
import com.nbdd0121.gfx.RaycastHit;
import com.nbdd0121.gfx.math.Vector2d;
import com.nbdd0121.gfx.math.Vector3d;

public abstract class Shape {
	protected Material material;

	public void setMaterial(Material mat) {
		material = mat;
	}

	public Material getMaterial() {
		return material;
	}

	public abstract Vector3d getNormalAt(Vector3d point);
	
	public abstract Vector2d getUVMapping(Vector3d point);
	
	public abstract BoundingBox getBoundingBox();

	public RaycastHit intersectionRay(Ray ray) {
		return intersectionWith(ray, Double.POSITIVE_INFINITY);
	}

	public abstract RaycastHit intersectionWith(Ray ray, double maxDistance);
}
