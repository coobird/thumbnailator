/*
 * Thumbnailator - a thumbnail generation library
 *
 * Copyright (c) 2008-2020 Chris Kroells
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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

