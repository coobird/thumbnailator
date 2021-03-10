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

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.geometry.Position;

/**
 * This class applies a watermark to an image.
 * 
 * @author coobird
 *
 */
public class Watermark implements ImageFilter {
	/**
	 * The position of the watermark.
	 */
	private final Position position;
	
	/**
	 * The watermark image.
	 */
	private final BufferedImage watermarkImg;
	
	/**
	 * The opacity of the watermark.
	 */
	private final float opacity;

	/**
	 * The insets for the watermark.
	 */
	private final int insets;

	/**
	 * Instantiates a filter which applies a watermark to an image.
	 * 
	 * @param position			The position of the watermark.
	 * @param watermarkImg		The watermark image.
	 * @param opacity			The opacity of the watermark.
	 * 							<p>
	 * 							The value should be between {@code 0.0f} and
	 * 							{@code 1.0f}, where {@code 0.0f} is completely
	 * 							transparent, and {@code 1.0f} is completely
	 * 							opaque.
	 * @param insets			Inset size around the watermark.
	 * 							Cannot be negative.
	 */
	public Watermark(Position position, BufferedImage watermarkImg, float opacity, int insets) {
		if (position == null) {
			throw new NullPointerException("Position is null.");
		}
		if (watermarkImg == null) {
			throw new NullPointerException("Watermark image is null.");
		}
		if (opacity > 1.0f || opacity < 0.0f) {
			throw new IllegalArgumentException("Opacity is out of range of " +
					"between 0.0f and 1.0f.");
		}
		if (insets < 0) {
			throw new IllegalArgumentException("Insets cannot be negative.");
		}
		
		this.position = position;
		this.watermarkImg = watermarkImg;
		this.opacity = opacity;
		this.insets = insets;
	}

	/**
	 * Instantiates a filter which applies a watermark to an image.
	 *
	 * @param position			The position of the watermark.
	 * @param watermarkImg		The watermark image.
	 * @param opacity			The opacity of the watermark.
	 * 							<p>
	 * 							The value should be between {@code 0.0f} and
	 * 							{@code 1.0f}, where {@code 0.0f} is completely
	 * 							transparent, and {@code 1.0f} is completely
	 * 							opaque.
	 */
	public Watermark(Position position, BufferedImage watermarkImg, float opacity) {
		this(position, watermarkImg, opacity, 0);
	}

	public BufferedImage apply(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		int type = img.getType();

		BufferedImage imgWithWatermark =
			new BufferedImageBuilder(width, height, type).build();
		
		int watermarkWidth = watermarkImg.getWidth();
		int watermarkHeight = watermarkImg.getHeight();

		Point p = position.calculate(
				width, height, watermarkWidth, watermarkHeight,
				insets, insets, insets, insets
		);

		Graphics2D g = imgWithWatermark.createGraphics();
		
		// Draw the actual image.
		g.drawImage(img, 0, 0, null);
		
		// Draw the watermark on top.
		g.setComposite(
				AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity)
		);
		
		g.drawImage(watermarkImg, p.x, p.y, null);
		
		g.dispose();

		return imgWithWatermark;
	}
}
