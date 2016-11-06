package com.nbdd0121.gfx.texture;

import com.nbdd0121.gfx.math.Vector3d;

public class PureColor extends Texture {

	private Vector3d color;
	
	public PureColor(Vector3d color) {
		this.color = color;
	}
	
	@Override
	public Vector3d getColor(double u, double v) {
		return color;
	}

}
