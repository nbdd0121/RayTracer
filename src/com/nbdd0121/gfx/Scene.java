package com.nbdd0121.gfx;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.nbdd0121.gfx.math.Vector3d;
import com.nbdd0121.gfx.shape.Shape;

public class Scene {

	private List<Shape> objects;
	private List<PointLight> pointLights;

	// The color of the ambient light in the scene
	private Vector3d ambientLight;

	public Scene() {
		objects = new LinkedList<Shape>();
		pointLights = new LinkedList<PointLight>();
		ambientLight = new Vector3d(1);
	}

	public void addObject(Shape sphere) {
		objects.add(sphere);
	}

	public RaycastHit findClosestIntersection(Ray ray) {
		return findClosestIntersection(ray, Double.POSITIVE_INFINITY);
	}

	public RaycastHit findClosestIntersection(Ray ray, double distance) {
		RaycastHit closestHit = null;

		for (Shape object : objects) {
			RaycastHit trialHit = object.intersectionWith(ray, distance);
			if (trialHit != null) {
				closestHit = trialHit;
				distance = trialHit.getDistance();
			}
		}

		return closestHit;
	}

	public Vector3d getAmbientLighting() {
		return ambientLight;
	}

	public void setAmbientLight(Vector3d ambientLight) {
		this.ambientLight = ambientLight;
	}

	public List<PointLight> getPointLights() {
		return Collections.unmodifiableList(pointLights);
	}

	public void addPointLight(PointLight pointLight) {
		pointLights.add(pointLight);
	}

}
