package net.coobird.thumbnailator.test;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

public class BufferedImageAssert {
	
	/**
	 * Checks if the 3x3 pattern matches the given image.
	 * 
	 * @param img		The image to check.
	 * @param pattern	The pattern that should be present.
	 * @return			If the pattern is present in the image.
	 */
	public static void assertMatches(BufferedImage img, float[] pattern) {
		if (pattern.length != 9) {
			throw new IllegalArgumentException("Pattern must be of length 9.");
		}
		
		int width = img.getWidth();
		int height = img.getHeight();
		
		List<Point> points = Arrays.asList(
				new Point(0, 0),
				new Point(width / 2 - 1, 0),
				new Point(width - 1, 0),
				new Point(0, height / 2 - 1),
				new Point(width / 2 - 1, height / 2 - 1),
				new Point(width - 1, height / 2 - 1),
				new Point(0, height - 1),
				new Point(width / 2 - 1, height - 1),
				new Point(width - 1, height - 1)
		);
		
		for (int i = 0; i < 9; i++) {
			Point p = points.get(i);
			Color c = pattern[i] == 0 ? Color.white : Color.black;
			assertEquals(c.getRGB(), img.getRGB(p.x, p.y));
		}
	}
}

