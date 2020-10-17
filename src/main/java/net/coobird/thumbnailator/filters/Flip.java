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

package net.coobird.thumbnailator.filters;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.coobird.thumbnailator.builders.BufferedImageBuilder;

/**
 * A class containing flip transformation filters.
 * 
 * @author coobird
 *
 */
public class Flip {
	
	/**
	 * An image filter which performs a horizontal flip of the image.
	 */
	public static final ImageFilter HORIZONTAL = new ImageFilter() {
		public BufferedImage apply(BufferedImage img) {
			int width = img.getWidth();
			int height = img.getHeight();
			
			BufferedImage newImage =
					new BufferedImageBuilder(width, height, img.getType()).build();
			
			Graphics g = newImage.getGraphics();
			g.drawImage(img, width, 0, 0, height, 0, 0, width, height, null);
			g.dispose();
			
			return newImage;
		}
	};
	
	/**
	 * An image filter which performs a vertical flip of the image.
	 */
	public static final ImageFilter VERTICAL = new ImageFilter() {
		public BufferedImage apply(BufferedImage img) {
			int width = img.getWidth();
			int height = img.getHeight();
			
			BufferedImage newImage =
					new BufferedImageBuilder(width, height, img.getType()).build();
			
			Graphics g = newImage.getGraphics();
			g.drawImage(img, 0, height, width, 0, 0, 0, width, height, null);
			g.dispose();
			
			return newImage;
		}
	};
}
