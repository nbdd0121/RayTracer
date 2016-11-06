package com.nbdd0121.gfx;

import com.nbdd0121.gfx.math.Vector3d;

public class Ray {

    private Vector3d origin, direction;

    public Ray(Vector3d origin, Vector3d direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Vector3d getOrigin() {
        return origin;
    }

    public Vector3d getDirection() {
        return direction;
    }

    public Vector3d evaluateAt(double distance) {
        return origin.add(direction.scale(distance));
    }
}
