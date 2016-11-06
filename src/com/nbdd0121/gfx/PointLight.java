package com.nbdd0121.gfx;

import com.nbdd0121.gfx.math.Vector3d;

public class PointLight {

    private Vector3d position;
    private Vector3d colour;
    private double intensity;

    public PointLight(Vector3d position, Vector3d colour, double intensity) {
        this.position = position;
        this.colour = colour;
        this.intensity = intensity;
    }

    public Vector3d getPosition() {
        return position;
    }

    public Vector3d getColour() {
        return colour;
    }

    public double getIntensity() {
        return intensity;
    }

    public Vector3d getIlluminationAt(double distance) {
        return colour.scale(intensity / (Math.PI * 4 * Math.pow(distance, 2)));
    }
}
