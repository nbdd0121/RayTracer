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
	
	public static Shape parse(String file) throws FileNotFoundException {
		ArrayList<Vector3d> vertexes = new ArrayList<>();
		ArrayList<Vector3d> normals = new ArrayList<>();
		
		vertexes.add(null);
		normals.add(null);
		
		ShapeGroup faces = new ShapeGroup();
		Scanner scanner = new Scanner(new FileInputStream(file));

		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();

			// Comment
			if (line.startsWith("#")) {
				continue;
			}

			if (line.startsWith("v ")) {
				String[] vertex = line.substring(2).trim().split(" ");
				Vector3d v = new Vector3d(Double.parseDouble(vertex[0]),
						Double.parseDouble(vertex[1]),
						Double.parseDouble(vertex[2]));
				vertexes.add(v.add(new Vector3d(-0.2, -1, 5)));
			}else if (line.startsWith("vn ")) {
				String[] vertex = line.substring(2).trim().split(" ");
				Vector3d v = new Vector3d(Double.parseDouble(vertex[0]),
						Double.parseDouble(vertex[1]),
						Double.parseDouble(vertex[2]));
				normals.add(v.normalize());
			} else if (line.startsWith("f ")) {
				String[] vertex = line.substring(2).trim().split(" ");
				int id0 = Integer.parseInt(vertex[0].split("/")[0]);
				int id1 = Integer.parseInt(vertex[1].split("/")[0]);
				int id2 = Integer.parseInt(vertex[2].split("/")[0]);
				
				int n0 = Integer.parseInt(vertex[0].split("/")[2]);
				int n1 = Integer.parseInt(vertex[1].split("/")[2]);
				int n2 = Integer.parseInt(vertex[2].split("/")[2]);
				faces.add(new MeshTriangle(vertexes.get(id0), vertexes.get(id1),
						vertexes.get(id2),normals.get(n0), normals.get(n1),
						normals.get(n2)));
			} else {
				// Other directive are not supported yet!
			}
		}
		
		scanner.close();
		
		faces.build();
		return faces;
	}

}
