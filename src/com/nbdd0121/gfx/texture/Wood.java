package com.nbdd0121.gfx.texture;

import com.nbdd0121.gfx.math.Vector3d;
import com.nbdd0121.gfx.util.Noise;

public class Wood extends Texture {

	@Override
	public Vector3d getColor(double u, double v) {
		u *= 128;
		v *= 128;

		double xyPeriod = 12.0; // number of rings
		double turbPower = 0.1; // makes twists
		double turbSize = 32.0; // initial size of the turbulence

		double xValue = (u - 128 / 2) / (double) 128;
		double yValue = (v - 128 / 2) / (double) 128;
		double distValue = Math.sqrt(xValue * xValue + yValue * yValue)
				+ turbPower * Noise.turbulence(u, v, turbSize) / 256.0;
		double sineValue = 128.0
				* Math.abs(Math.sin(2 * xyPeriod * distValue * 3.14159));
		return new Vector3d((80 + sineValue) / 256, (30 + sineValue) / 256,
				30 / 256.);
	}

}
