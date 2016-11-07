package com.nbdd0121.gfx.shape;

import com.nbdd0121.gfx.math.Vector3d;

public class MeshTriangle extends Triangle {
	
	private Vector3d aNormal, bNormal, cNormal;

	public MeshTriangle(Vector3d a, Vector3d b, Vector3d c, Vector3d aNormal, Vector3d bNormal, Vector3d cNormal) {
		super(a,b,c);
		
		this.aNormal = aNormal;
		this.bNormal = bNormal;
		this.cNormal = cNormal;
	}

	@Override
	public Vector3d getNormalAt(Vector3d point) {
		Vector3d pa = a.subtract(point);
		Vector3d pb = b.subtract(point);
		Vector3d pc = c.subtract(point);

		double invK = 1 / pa.subtract(pb).cross(pa.subtract(pc)).magnitude();
		double ka = pb.cross(pc).magnitude() * invK;
		double kb = pc.cross(pa).magnitude() * invK;
		double kc = pa.cross(pb).magnitude() * invK;
		
		return aNormal.scale(ka).add(bNormal.scale(kb))
				.add(cNormal.scale(kc));
	}

}
