package com.nbdd0121.gfx;

import java.awt.image.BufferedImage;

import com.nbdd0121.gfx.math.Vector2d;
import com.nbdd0121.gfx.math.Vector3d;
import com.nbdd0121.gfx.shape.Shape;

public class Renderer {

	private final Vector3d BACKGROUND_COLOUR = new Vector3d(0.1);
	private final double DELTA = 0.00001;

	private final double SUPERSAMPLING_QUALITY = 1 / 4.;
	// private final double SUPERSAMPLING_QUALITY = 2.;
	private final double SUPERSAMPLING_THRESHOLD = 0.001;

	// private final int MAX_REFLECTION_DEPTH = 0;
	private final int MAX_REFLECTION_DEPTH = 5;
	private final boolean SUPERSAMPLING_DEBUG = false;

	private int width, height;

	public Renderer(int width, int height) {
		this.width = width;
		this.height = height;
	}

	private Vector3d trace(Scene scene, Ray ray, int step,
			double currentRefractionIndex) {
		RaycastHit closestHit = scene.findClosestIntersection(ray);

		// If no object has been hit, return a background colour
		if (closestHit == null) {
			return BACKGROUND_COLOUR;
		}

		Shape object = closestHit.getObjectHit();
		Material mat = object.getMaterial();

		Vector3d P = closestHit.getLocation();
		Vector3d N = closestHit.getNormal();

		Vector2d uv = object.getUVMapping(P);
		Vector3d diffuseColor = mat.getTexture().getColor(uv.x, uv.y);

		Vector3d color = this.illuminate(scene, object, ray, P, N,
				diffuseColor);

		if (step > 0) {
			Vector3d incomingDirection = P.subtract(ray.getOrigin())
					.normalize();

			// Mirror reflection
			if (mat.reflectionIndex > 0) {
				Vector3d direction = incomingDirection.negate().reflectIn(N);
				Ray reflectedRay = new Ray(P.add(direction.scale(DELTA)),
						direction);
				color = color.add(trace(scene, reflectedRay, step - 1,
						currentRefractionIndex).scale(mat.reflectionIndex)
								.scale(diffuseColor));
			}

			if (mat.transparency > 0) {
				double cosI = -incomingDirection.dot(N);

				double newRefractionIndex;
				// Leave an object, back to air
				if (cosI < 0) {
					newRefractionIndex = 1;
				} else {
					newRefractionIndex = mat.refractionIndex;
				}

				// Snell's Law
				double refractionRatio = currentRefractionIndex
						/ newRefractionIndex;
				double sinISquared = 1 - cosI * cosI;
				double sinTSquared = refractionRatio * refractionRatio
						* sinISquared;

				// If we are not facing total internal reflection
				if (sinTSquared < 1) {
					double cosT = Math.sqrt(1 - sinTSquared);
					if (cosI < 0)
						cosT = -cosT;
					// Some math to get outgoing normalized light directly
					Vector3d refractedDirection = incomingDirection
							.scale(refractionRatio)
							.add(N.scale(refractionRatio * cosI - cosT));
					// Create an refracted ray. Offset a little bi
					Ray refractedRay = new Ray(
							P.add(refractedDirection.scale(DELTA)),
							refractedDirection);

					// System.out.println("Incoming Ray: " + incomingDirection);
					// System.out.println("Refracted Ray: " +
					// refractedDirection);

					RaycastHit nextHit = scene
							.findClosestIntersection(refractedRay);
					if (nextHit != null) {
						Vector3d refractedColor = trace(scene, refractedRay,
								step - 1, newRefractionIndex);
						// If we are re-entering air then no need for further
						// calculation
						if (newRefractionIndex != 1) {
							double distance = nextHit.getDistance();
							// If the object is diffusing color then
							// we also apply exponential decays (absorption)
							Vector3d dist = diffuseColor
									.scale(-0.15 * distance);
							Vector3d decay = new Vector3d(Math.exp(dist.x),
									Math.exp(dist.y), Math.exp(dist.y));
							refractedColor = refractedColor.scale(decay)
									.scale(mat.transparency);
						}
						color = color.add(refractedColor);
					}
				}
			}
		}

		return color;
	}

	private Vector3d illuminate(Scene scene, Shape object, Ray ray, Vector3d P,
			Vector3d N, Vector3d diffuseColor) {
		Material material = object.getMaterial();

		Vector3d color;

		if (material.ambientIntensity > 0) {
			// Calculate ambient lighting I * Ka * Ia
			Vector3d I_a = scene.getAmbientLighting();
			color = diffuseColor.scale(material.ambientIntensity).scale(I_a);
		} else {
			color = Vector3d.ZERO;
		}

		for (PointLight light : scene.getPointLights()) {
			// Create shadow ray
			Vector3d diffuse;
			Vector3d specular;

			Vector3d shadowRayVec = light.getPosition().subtract(P);
			Vector3d L = shadowRayVec.normalize();
			double distanceToLight = shadowRayVec.magnitude();
			Ray shadowRay = new Ray(P.add(L.scale(DELTA)), L);
			RaycastHit hit = scene.findClosestIntersection(shadowRay,
					distanceToLight);

			// In shadow
			if (hit != null) {
				continue;
			}

			// Get luminance
			Vector3d I = light.getIlluminationAt(distanceToLight);

			// Calculate diffuse color
			if (material.diffuseIndex > 0) {
				diffuse = diffuseColor.scale(material.diffuseIndex).scale(I)
						.scale(Math.max(0, N.dot(L)));
			} else {
				diffuse = Vector3d.ZERO;
			}

			if (material.specularIndex > 0) {
				// H = normalize(L + V), and use N.H instead of R.N
				// (Blinn-Phong)
				Vector3d V = ray.getOrigin().subtract(P).normalize();
				double NdotH = N.dot(L.add(V).normalize());
				specular = material.specularColor.scale(material.specularIndex)
						.scale(I).scale(Math.pow(Math.max(0, NdotH),
								material.shininess * 128));
			} else {
				specular = Vector3d.ZERO;
			}
			color = color.add(diffuse).add(specular);
		}
		return color;
	}

	private Vector3d singleSample(Scene scene, Camera camera, double x,
			double y) {
		Ray ray = camera.castRay(x, y);
		return trace(scene, ray, MAX_REFLECTION_DEPTH, 1);
	}

	private Vector3d subpixelSample(Scene scene, Camera camera, double x,
			double y, double size, Vector3d tl, Vector3d tr, Vector3d bl,
			Vector3d br) {
		double halfsize = size / 2;

		Vector3d pixel = tl.add(tr).add(bl).add(br).scale(0.25);
		Vector3d var = tl.elementwiseSquare().add(tr.elementwiseSquare())
				.add(bl.elementwiseSquare()).add(br.elementwiseSquare())
				.scale(0.25).subtract(pixel.elementwiseSquare());
		if (size > SUPERSAMPLING_QUALITY
				&& var.magnitude() > SUPERSAMPLING_THRESHOLD) {
			if (SUPERSAMPLING_DEBUG)
				return Vector3d.ONE;
			double quadsize = size / 4;

			Vector3d t = singleSample(scene, camera, x, y - halfsize);
			Vector3d b = singleSample(scene, camera, x, y + halfsize);
			Vector3d l = singleSample(scene, camera, x - halfsize, y);
			Vector3d r = singleSample(scene, camera, x + halfsize, y);
			Vector3d c = singleSample(scene, camera, x, y);

			Vector3d tlp = subpixelSample(scene, camera, x - quadsize,
					y - quadsize, halfsize, tl, t, l, c);
			Vector3d trp = subpixelSample(scene, camera, x + quadsize,
					y - quadsize, halfsize, t, tr, c, r);
			Vector3d blp = subpixelSample(scene, camera, x - quadsize,
					y + quadsize, halfsize, l, c, bl, b);
			Vector3d brp = subpixelSample(scene, camera, x + quadsize,
					y + quadsize, halfsize, c, r, b, br);
			return tlp.add(trp).add(blp).add(brp).scale(0.25);
		}
		if (SUPERSAMPLING_DEBUG)
			return Vector3d.ZERO;
		return pixel;
	}

	public BufferedImage render(Scene scene) {
		Vector3d[][] pixels = new Vector3d[height + 1][width + 1];
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Camera camera = new Camera(width, height);

		Thread[] threads = new Thread[4];
		for (int i = 0; i < 4; i++) {
			int start = i * height / 4 + (i == 0 ? 0 : 1);
			int end = (i + 1) * height / 4 + 1;
			int j = i;

			threads[i] = new Thread(() -> {
				for (int y = start; y < end; y++) {
					for (int x = 0; x <= width; x++) {
						pixels[y][x] = singleSample(scene, camera, x - 0.5,
								y - 0.5);
					}
					if (j == 3) {
						System.out
								.println("Rendering "
										+ String.format("%.2f",
												50. * (y - start)
														/ (end - start))
										+ "% complete");
					}
				}
			});
			threads[i].start();
		}

		for (int i = 0; i < 4; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			int start = i * height / 4;
			int end = (i + 1) * height / 4;
			int j = i;

			threads[i] = new Thread(() -> {
				for (int y = start; y < end; y++) {
					for (int x = 0; x < width; x++) {
						Vector3d pixel = subpixelSample(scene, camera, x, y, 1,
								pixels[y][x], pixels[y][x + 1],
								pixels[y + 1][x], pixels[y + 1][x + 1]);

						image.setRGB(x, y, pixel.toRGB());
					}
					if (j == 3) {
						System.out
								.println("Supersampling "
										+ String.format("%.2f",
												50 + 50. * (y - start)
														/ (end - start))
								+ "% complete");
					}
				}
			});
			threads[i].start();
		}

		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return image;
	}
}