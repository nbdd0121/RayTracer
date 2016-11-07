package com.nbdd0121.gfx.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.nbdd0121.gfx.math.Matrix4d;
import com.nbdd0121.gfx.math.Vector2d;
import com.nbdd0121.gfx.math.Vector3d;
import com.nbdd0121.gfx.shape.MeshTriangle;
import com.nbdd0121.gfx.shape.Shape;
import com.nbdd0121.gfx.shape.ShapeGroup;
import com.nbdd0121.gfx.shape.Triangle;

public class ObjParser {

	private ArrayList<Vector3d> vertices;
	private ArrayList<Vector3d> normals;
	private ArrayList<Vector2d> textures;
	private Matrix4d transform;
	private Matrix4d normalTransform;
	private ShapeGroup faces = new ShapeGroup();

	private ObjParser() {
		vertices = new ArrayList<>();
		normals = new ArrayList<>();
		textures = new ArrayList<>();

		vertices.add(null);
		normals.add(null);
		textures.add(null);
	}

	private Vector3d parseTuple(String str) {
		String[] split = str.split(" ");
		return new Vector3d(Double.parseDouble(split[0]),
				Double.parseDouble(split[1]), Double.parseDouble(split[2]));
	}

	private void processV(String arg) {
		vertices.add(transform.multiply(parseTuple(arg)).asVector3d());
	}

	private void processVn(String arg) {
		normals.add(normalTransform.multiply(parseTuple(arg)).asVector3d());
	}

	private void processVt(String arg) {
		Vector3d vec = parseTuple(arg);
		textures.add(new Vector2d(vec.x, vec.y));
	}

	private void processF(String arg) {
		String[] split = arg.split(" ");

		if (split.length != 3) {
			throw new UnsupportedOperationException();
		}

		// TODO: Deal with more than 3 vertices
		String[] s0 = split[0].split("/");
		String[] s1 = split[1].split("/");
		String[] s2 = split[2].split("/");

		int id0 = Integer.parseInt(s0[0]);
		int id1 = Integer.parseInt(s1[0]);
		int id2 = Integer.parseInt(s2[0]);

		// For now assume all vertices have same format
		if (s0.length == 1) {
			faces.add(new Triangle(vertices.get(id0), vertices.get(id1),
					vertices.get(id2)));
			return;
		}

		// Get texture vertices
		int t0 = 0;
		int t1 = 0;
		int t2 = 0;
		if (!s0[1].isEmpty()) {
			t0 = Integer.parseInt(s0[1]);
			t1 = Integer.parseInt(s1[1]);
			t2 = Integer.parseInt(s2[1]);
		}

		// Get normal vertices
		int n0 = 0;
		int n1 = 0;
		int n2 = 0;
		if (s0.length >= 3) {
			n0 = Integer.parseInt(s0[2]);
			n1 = Integer.parseInt(s1[2]);
			n2 = Integer.parseInt(s2[2]);
		}

		faces.add(new MeshTriangle(vertices.get(id0), vertices.get(id1),
				vertices.get(id2), normals.get(n0), normals.get(n1),
				normals.get(n2), textures.get(t0), textures.get(t1),
				textures.get(t2)));
	}

	public static Shape parse(String file, Matrix4d transform)
			throws FileNotFoundException {
		ObjParser parser = new ObjParser();

		parser.transform = transform;
		parser.normalTransform = transform.inverse().transpose();

		Scanner scanner = new Scanner(new FileInputStream(file));

		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();

			// Comment
			if (line.startsWith("#")) {
				continue;
			}

			if (line.startsWith("v ")) {
				parser.processV(line.substring(2).trim());
			} else if (line.startsWith("vn ")) {
				parser.processVn(line.substring(3).trim());
			} else if (line.startsWith("vt ")) {
				parser.processVt(line.substring(3).trim());
			} else if (line.startsWith("f ")) {
				parser.processF(line.substring(2).trim());
			} else {
				// Other directive are not supported yet!
			}
		}

		scanner.close();

		parser.faces.build();
		return parser.faces;
	}

}
