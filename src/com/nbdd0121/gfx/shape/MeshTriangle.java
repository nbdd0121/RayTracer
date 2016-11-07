package com.nbdd0121.gfx.shape;

import com.nbdd0121.gfx.math.Vector2d;
import com.nbdd0121.gfx.math.Vector3d;

public class MeshTriangle extends Triangle {

	private Vector3d aNormal, bNormal, cNormal;
	private Vector2d aTexture, bTexture, cTexture;

	public MeshTriangle(Vector3d a, Vector3d b, Vector3d c, Vector3d aNormal,
			Vector3d bNormal, Vector3d cNormal, Vector2d aTexture,
			Vector2d bTexture, Vector2d cTexture) {
		super(a, b, c);
		this.aNormal = aNormal;
		this.bNormal = bNormal;
		this.cNormal = cNormal;
		this.aTexture = aTexture;
		this.bTexture = bTexture;
		this.cTexture = cTexture;
	}

	@Override
	public Vector3d getNormalAt(Vector3d point) {
		if (aNormal == null)
			return normal;

		Vector3d coord = barycentric(point);
		return aNormal.scale(coord.x).add(bNormal.scale(coord.y))
				.add(cNormal.scale(coord.z));
	}

	@Override
	public Vector2d getUVMapping(Vector3d point) {
		if (aTexture == null)
			return super.getUVMapping(point);

		Vector3d coord = barycentric(point);
		return aTexture.scale(coord.x).add(bTexture.scale(coord.y))
				.add(cTexture.scale(coord.z));
	}

}
