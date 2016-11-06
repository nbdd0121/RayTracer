package com.nbdd0121.gfx;

import com.nbdd0121.gfx.math.Vector3d;
import com.nbdd0121.gfx.shape.Shape;

public class RaycastHit {

    // The distance the ray travelled before hitting an object
    private double distance;

    // The object that was hit by the ray
    private Shape objectHit;

    // The location that the ray hit the object
    private Vector3d location;

    // The normal of the object at the location hit by the ray
    private Vector3d normal;

    public RaycastHit(Shape objectHit, double distance, Vector3d location, Vector3d normal) {
        this.distance = distance;
        this.objectHit = objectHit;
        this.location = location;
        this.normal = normal;
    }

    public Shape getObjectHit() {
        return objectHit;
    }

    public Vector3d getLocation() {
        return location;
    }

    public Vector3d getNormal() {
        return normal;
    }

    public double getDistance() {
        return distance;
    }
}
