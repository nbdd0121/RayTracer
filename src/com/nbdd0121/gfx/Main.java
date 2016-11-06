package com.nbdd0121.gfx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main {
	public static final String DEFAULT_INPUT = "basic_scene.xml";
	public static final String DEFAULT_OUTPUT = "output.png";

	// Width and height of the image to be rendered
	private static final int WIDTH_PX = 1280;
	private static final int HEIGHT_PX = 960;

	public static void usageError() {
		System.err.println("USAGE: <tick1> --input INPUT --output OUTPUT");
		System.exit(-1);
	}

	public static void main(String[] args) throws IOException {
		// We should have an even number of arguments
		if (args.length % 2 != 0)
			usageError();

		// Parse the input and output filenames from the arguments
		String input = DEFAULT_INPUT, output = DEFAULT_OUTPUT;
		for (int i = 0; i < args.length; i += 2) {
			switch (args[i]) {
				case "-i":
				case "--input":
					input = args[i + 1];
					break;
				case "-o":
				case "--output":
					output = args[i + 1];
					break;
				default:
					System.err.println("Unknown option: " + args[i]);
					usageError();
			}
		}

		Scene scene = new SceneLoader(input).getScene();
		long timeStart = System.currentTimeMillis();
		BufferedImage image;

		image = new Renderer(WIDTH_PX, HEIGHT_PX).render(scene);
		long timeEnd = System.currentTimeMillis();
		System.out.println((timeEnd - timeStart) + "ms");

		File save = new File(output);
		ImageIO.write(image, "png", save);
		JFrame jf = new JFrame("Render Result");
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jf.add(new JLabel(new ImageIcon(image)));
		jf.setVisible(true);
		jf.pack();
	}
}
