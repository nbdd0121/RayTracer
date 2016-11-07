package com.nbdd0121.gfx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.nbdd0121.gfx.math.Transformation;
import com.nbdd0121.gfx.math.Vector3d;
import com.nbdd0121.gfx.model.ObjParser;
import com.nbdd0121.gfx.shape.Plane;
import com.nbdd0121.gfx.shape.Shape;
import com.nbdd0121.gfx.shape.Sphere;
import com.nbdd0121.gfx.shape.Triangle;
import com.nbdd0121.gfx.texture.ImageTexture;
import com.nbdd0121.gfx.texture.PureColor;

public class SceneLoader {

	private Scene scene;

	public SceneLoader(String filename) {
		scene = new Scene();

		Element document = null;
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new File(filename)).getDocumentElement();
		} catch (ParserConfigurationException e) {
			assert false;
		} catch (IOException e) {
			throw new RuntimeException(
					"error reading file:\n" + e.getMessage());
		} catch (SAXException e) {
			throw new RuntimeException("error loading XML.");
		}

		if (document.getNodeName() != "scene")
			throw new RuntimeException(
					"scene file does not contain a scene element");

		NodeList elements = document.getElementsByTagName("*");
		for (int i = 0; i < elements.getLength(); ++i) {
			Element element = (Element) elements.item(i);
			switch (element.getNodeName()) {

				case "sphere": {
					Vector3d pos = getPosition(element);
					double radius = getDouble(element, "radius", 1);
					Sphere sphere = new Sphere(pos, radius);
					sphere.setMaterial(getMaterial(element));
					scene.addObject(sphere);
					break;
				}
				case "plane": {
					Vector3d pos = getPosition(element);
					double distance = getDouble(element, "distance", 1);
					Plane plane = new Plane(pos, distance);
					plane.setMaterial(getMaterial(element));
					scene.addObject(plane);
					break;
				}
				case "triangle": {
					Vector3d a = getVector(element, "a", null);
					Vector3d b = getVector(element, "b", null);
					Vector3d c = getVector(element, "c", null);
					Triangle tri = new Triangle(a, b, c);
					tri.setMaterial(getMaterial(element));
					scene.addObject(tri);
					break;
				}
				case "model": {
					Vector3d pos = getPosition(element);
					Vector3d scale = getVector(element, "scale", Vector3d.ONE);
					double rotate = getDouble(element, "rotate", 0);
					Shape s;
					try {
						s = ObjParser
								.parse(getString(element, "obj"),
										Transformation.translation(pos)
												.multiply(Transformation
														.scale(scale))
										.multiply(Transformation.rotateY(
												rotate / 180 * Math.PI)));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						s = null;
					}
					s.setMaterial(getMaterial(element));
					scene.addObject(s);
					break;
				}
				case "point-light":
					PointLight light = new PointLight(getPosition(element),
							getColor(element),
							getDouble(element, "intensity", 100));
					scene.addPointLight(light);
					break;

				case "ambient-light":
					scene.setAmbientLight(getColor(element));
					break;

				default:
					throw new RuntimeException(
							"unknown object tag: " + element.getNodeName());
			}
		}
	}

	public Scene getScene() {
		return scene;
	}

	private Material getMaterial(Element tag) {
		Material mat = Material.preset(getString(tag, "material"));
		if (mat == null)
			mat = new Material();
		if (tag.hasAttribute("texture")) {
			try {
				mat.texture = new ImageTexture(
						ImageIO.read(new File(tag.getAttribute("texture"))));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// Check if we have pure color
			Vector3d color = getColor(tag);
			if (color != null)
				mat.texture = new PureColor(color);
		}
		return mat;
	}

	private Vector3d getPosition(Element tag) {
		double x = getDouble(tag, "x", 0);
		double y = getDouble(tag, "y", 0);
		double z = getDouble(tag, "z", 0);
		return new Vector3d(x, y, z);
	}

	private Vector3d getVector(Element tag, String attribute,
			Vector3d fallback) {
		String[] comp = tag.getAttribute(attribute).split(",");
		if (comp.length != 1 && comp.length != 3)
			return fallback;
		try {
			double x = Double.parseDouble(comp[0]);
			if (comp.length == 1)
				return new Vector3d(x);
			double y = Double.parseDouble(comp[1]);
			double z = Double.parseDouble(comp[2]);
			return new Vector3d(x, y, z);
		} catch (NumberFormatException e) {
			return fallback;
		}
	}

	private Vector3d getColor(Element tag) {
		String hexString = tag.getAttribute("color");
		if (hexString.isEmpty()) {
			return null;
		}
		double red = Integer.parseInt(hexString.substring(1, 3), 16) / 255.0;
		double green = Integer.parseInt(hexString.substring(3, 5), 16) / 255.0;
		double blue = Integer.parseInt(hexString.substring(5, 7), 16) / 255.0;

		return new Vector3d(red, green, blue);
	}

	private double getDouble(Element tag, String attribute, double fallback) {
		try {
			return Double.parseDouble(tag.getAttribute(attribute));
		} catch (NumberFormatException e) {
			return fallback;
		}
	}

	private String getString(Element tag, String attribute) {
		return tag.getAttribute(attribute);
	}

}
