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

package net.coobird.thumbnailator.util;

import net.coobird.thumbnailator.builders.BufferedImageBuilder;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * This class provides convenience methods for using {@link BufferedImage}s.
 * 
 * @author coobird
 *
 */
public final class BufferedImages {
	/**
	 * This class is not intended to be instantiated.
	 */
	private BufferedImages() {}
	
	/**
	 * Returns a {@link BufferedImage} which is a graphical copy of the
	 * specified image.
	 * 
	 * @param img		The image to copy.
	 * @return			A copy of the specified image.
	 */
	public static BufferedImage copy(BufferedImage img) {
		return copy(img, img.getType());
	}
	
	/**
	 * Returns a {@link BufferedImage} with the specified image type, where the
	 * graphical content is a copy of the specified image.
	 * 
	 * @param img		The image to copy.
	 * @param imageType	The image type for the image to return.
	 * @return			A copy of the specified image.
	 */
	public static BufferedImage copy(BufferedImage img, int imageType) {
		int width = img.getWidth();
		int height = img.getHeight();

		BufferedImage newImage = new BufferedImageBuilder(width, height, imageType).build();

		Graphics g = newImage.createGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();
		
		return newImage;
	}
}