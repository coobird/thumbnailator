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

import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.util.BufferedImages;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * An image filter which will make an image transparent.
 * <p>
 * The resulting image will always have an image type of
 * {@link BufferedImage#TYPE_INT_ARGB}.
 * 
 * @author coobird
 *
 */
public class Transparency implements ImageFilter {
	/**
	 * The alpha composite to use when drawing the transparent image.
	 */
	private final AlphaComposite composite;

	/**
	 * Instantiates a {@link Transparency} filter with the specified opacity.
	 * 
	 * @param alpha		The opacity of the resulting image. The value should be
	 * 					between {@code 0.0f} (transparent) to {@code 1.0f}
	 * 					(opaque), inclusive.
	 * @throws IllegalArgumentException	If the specified opacity is outside of
	 * 									the range specified above.
	 */
	public Transparency(float alpha) {
		super();
		
		if (alpha < 0.0f || alpha > 1.0f) {
			throw new IllegalArgumentException(
					"The alpha must be between 0.0f and 1.0f, inclusive.");
		}
		
		this.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
	}
	
	/**
	 * Instantiates a {@link Transparency} filter with the specified opacity.
	 * <p>
	 * This is a convenience constructor for the
	 * {@link Transparency#Transparency(float)} constructor.
	 * 
	 * @param alpha		The opacity of the resulting image. The value should be
	 * 					between {@code 0.0f} (transparent) to {@code 1.0f}
	 * 					(opaque), inclusive.
	 * @throws IllegalArgumentException	If the specified opacity is outside of
	 * 									the range specified above.
	 */
	public Transparency(double alpha) {
		this((float)alpha);
	}

	public BufferedImage apply(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		
		BufferedImage finalImage = new BufferedImageBuilder(
				width,
				height,
				BufferedImage.TYPE_INT_ARGB
		).build();
		
		Graphics2D g = finalImage.createGraphics();
		g.setComposite(composite);
		g.drawImage(img, 0, 0, null);
		g.dispose();

		if (img.getType() != finalImage.getType()) {
			return BufferedImages.copy(finalImage, img.getType());
		}

		return finalImage;
	}
	
	/**
	 * Returns the opacity of this filter.
	 * 
	 * @return		The opacity in the range of {@code 0.0f} (transparent) to
	 * 				{@code 1.0f} (opaque).
	 */
	public float getAlpha() {
		return composite.getAlpha();
	}
}
