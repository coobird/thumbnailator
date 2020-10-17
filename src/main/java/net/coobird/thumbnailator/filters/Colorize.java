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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.util.BufferedImages;

/**
 * An image filter which will add a color tint to an image.
 * 
 * @author coobird
 *
 */
public final class Colorize implements ImageFilter {
	/**
	 * The color to tint a target image with.
	 */
	private final Color c;
	
	/**
	 * Instantiates this filter with the color to use to tint the target image
	 * with.
	 * <p>
	 * Note: If the provided {@link Color} does not have an alpha channel
	 * (transparency channel), then the target image will be painted with an
	 * opaque color, resulting in an image with only the specified color.
	 * 
	 * @param c				Color to tint with.
	 */
	public Colorize(Color c) {
		this.c = c;
	}
	
	/**
	 * Instantiates this filter with the color to use to tint the target image
	 * with and the transparency level provided as a {@code float} ranging from
	 * {@code 0.0f} to {@code 1.0f}, where {@code 0.0f} indicates completely
	 * transparent, and {@code 1.0f} indicates completely opaque.
	 * 
	 * @param c				Color to tint with.
	 * @param alpha			The opacity of the tint.
	 */
	public Colorize(Color c, float alpha) {
		this(c, (int)(255 * alpha));
	}
	
	/**
	 * Instantiates this filter with the color to use to tint the target image
	 * with and the transparency level provided as a {@code int} ranging from
	 * {@code 0} to {@code 255}, where {@code 0} indicates completely
	 * transparent, and {@code 255} indicates completely opaque.
	 * 
	 * @param c				Color to tint with.
	 * @param alpha			The opacity of the tint.
	 */
	public Colorize(Color c, int alpha) {
		if (alpha > 255 || alpha < 0) {
			throw new IllegalArgumentException(
					"Specified alpha value is outside the range of allowed " +
					"values.");
		}
		
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		int a = alpha;
		
		this.c = new Color(r, g, b, a);
	}
	
	public BufferedImage apply(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		
		BufferedImage newImage = new BufferedImageBuilder(width, height).build();
		
		Graphics2D g = newImage.createGraphics();
		g.drawImage(img, 0, 0, null);
		g.setColor(c);
		g.fillRect(0, 0, width, height);
		g.dispose();

		if (img.getType() != newImage.getType()) {
			return BufferedImages.copy(newImage, img.getType());
		}

		return newImage;
	}
}
