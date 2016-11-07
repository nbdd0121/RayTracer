package com.nbdd0121.gfx.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.nbdd0121.gfx.math.Vector3d;
import com.nbdd0121.gfx.shape.MeshTriangle;
import com.nbdd0121.gfx.shape.Shape;
import com.nbdd0121.gfx.shape.ShapeGroup;
import com.nbdd0121.gfx.shape.Triangle;

public class ObjParser {

	private ArrayList<Vector3d> vertices;
	private ArrayList<Vector3d> normals;
	private ArrayList<Vector3d> textures;
	private Vector3d offset; 
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
		vertices.add(parseTuple(arg).add(offset));
	}

	private void processVn(String arg) {
		normals.add(parseTuple(arg));
	}

	private void processVt(String arg) {
		textures.add(parseTuple(arg));
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

		// Have texture but no normal, for now we ignore textures
		if (s0.length == 2) {
			faces.add(new Triangle(vertices.get(id0), vertices.get(id1),
					vertices.get(id2)));
			return;
		}

		// With normals, for now we ignore textures
		int n0 = Integer.parseInt(s0[2]);
		int n1 = Integer.parseInt(s1[2]);
		int n2 = Integer.parseInt(s2[2]);
		faces.add(new MeshTriangle(vertices.get(id0), vertices.get(id1),
				vertices.get(id2), normals.get(n0), normals.get(n1),
				normals.get(n2)));
	}

	public static Shape parse(String file, Vector3d offset) throws FileNotFoundException {
		ObjParser parser = new ObjParser();
		
		// TODO: Make me matrix transformation
		parser.offset = offset;

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
