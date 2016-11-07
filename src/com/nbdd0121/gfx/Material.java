package com.nbdd0121.gfx;

import com.nbdd0121.gfx.math.Vector3d;
import com.nbdd0121.gfx.texture.Texture;
import com.nbdd0121.gfx.texture.Wood;

// Some material data comes from https://github.com/marczych/RayTracer
// Data members comes from https://github.com/scoffey/raytracer
public class Material {
	private static final Material air = new Material();
	private static final Material water = new Material();
	private static final Material glass = new Material();
	private static final Material china = new Material();
	private static final Material wood = new Material();
	private static final Material mirror = new Material();

	static {
		air.diffuseIndex = 0;
		air.specularIndex = 0;
		air.transparency = 1;
		air.refractionIndex = 1;
		
		water.specularIndex = 0;
		water.transparency = 1;
		water.refractionIndex = 1.33;
		
		glass.transparency = 1;
		glass.reflectionIndex = 0.2;
		glass.refractionIndex = 2;
		glass.shininess = 0.5;
		
		china.reflectionIndex = 0.2;
		china.shininess = 0.5;
		
		wood.specularIndex = 0;
		wood.texture = new Wood();
		
		mirror.diffuseIndex = 0.1;
		mirror.specularIndex = 0.2;
		mirror.reflectionIndex = 0.8;
	}

	public Vector3d specularColor = Vector3d.ONE;
	public double diffuseIndex = 0.8;
	public double specularIndex = 0.8;
	public double ambientIntensity = 0.8;
	public double transparency = 0;
	public double refractionIndex = 1;
	public double reflectionIndex = 0;
	public double shininess = 0.2;
	public Texture texture;

	public Material() {

	}

	public Material(Material mat) {
		this.specularColor = mat.specularColor;
		this.diffuseIndex = mat.diffuseIndex;
		this.specularIndex = mat.specularIndex;
		this.ambientIntensity = mat.ambientIntensity;
		this.transparency = mat.transparency;
		this.refractionIndex = mat.refractionIndex;
		this.reflectionIndex = mat.reflectionIndex;
		this.shininess = mat.shininess;
		this.texture = mat.texture;
	}

	public static Material preset(String name) {
		switch (name) {
			case "air":
				return new Material(air);
			case "water":
				return new Material(water);
			case "glass":
				return new Material(glass);
			case "wood":
				return new Material(wood);
			case "mirror":
				return new Material(mirror);
			default:
				return null;
		}
	}

	public Texture getTexture() {
		return texture;
	}
}
