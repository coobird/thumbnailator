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
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.util.BufferedImages;

/**
 * An {@link ImageFilter} which will overlay a text caption to an image.
 * 
 * @author coobird
 *
 */
public class Caption implements ImageFilter {
	/**
	 * The text of the caption.
	 */
	private final String caption;
	
	/**
	 * The font of text to add.
	 */
	private final Font font;
	
	/**
	 * The color of the text to add.
	 */
	private final Color c;
	
	/**
	 * The opacity level of the text to add.
	 * <p>
	 * The value should be between {@code 0.0f} to {@code 1.0f}, where
	 * {@code 0.0f} is completely transparent, and {@code 1.0f} is completely
	 * opaque.
	 */
	private final float alpha;
	
	/**
	 * The position at which the text should be drawn.
	 */
	private final Position position;
	
	/** 
	 * The insets for the text to draw.
	 */
	private final int insets;
	
	/**
	 * Instantiates a filter which adds a text caption to an image.
	 * 
	 * @param caption	The text of the caption.
	 * @param font		The font of the caption.
	 * @param c			The color of the caption.
	 * @param alpha		The opacity level of caption.
	 * 					<p>
	 * 					The value should be between {@code 0.0f} and
	 * 					{@code 1.0f}, where {@code 0.0f} is completely
	 * 					transparent, and {@code 1.0f} is completely opaque.
	 * @param position	The position of the caption.
	 * @param insets	The inset size around the caption. Cannot be negative.
	 */
	public Caption(String caption, Font font, Color c, float alpha, Position position, int insets) {
		if (caption == null) {
			throw new NullPointerException("Caption is null.");
		}
		if (font == null) {
			throw new NullPointerException("Font is null.");
		}
		if (c == null) {
			throw new NullPointerException("Color is null.");
		}
		if (alpha > 1.0f || alpha < 0.0f) {
			throw new IllegalArgumentException("Opacity is out of range of " +
					"between 0.0f and 1.0f.");
		}
		if (position == null) {
			throw new NullPointerException("Position is null.");
		}
		if (insets < 0) {
			throw new IllegalArgumentException("Insets cannot be negative.");
		}

		this.caption = caption;
		this.font = font;
		this.c = c;
		this.alpha = alpha;
		this.position = position;
		this.insets = insets;
	}

	/**
	 * Instantiates a filter which adds a text caption to an image.
	 * <p>
	 * The opacity of the caption will be 100% opaque.
	 * 
	 * @param caption	The text of the caption.
	 * @param font		The font of the caption.
	 * @param c			The color of the caption.
	 * @param position	The position of the caption.
	 * @param insets	The inset size around the caption. Cannot be negative.
	 */
	public Caption(String caption, Font font, Color c, Position position, int insets) {
		this(caption, font, c, 1.0f, position, insets);
	}

	public BufferedImage apply(BufferedImage img) {
		BufferedImage newImage = BufferedImages.copy(img);
		
		Graphics2D g = newImage.createGraphics();
		g.setFont(font);
		g.setColor(c);
		g.setComposite(
				AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)
		);
		
		int imageWidth = img.getWidth();
		int imageHeight = img.getHeight();
		
		int captionWidth = g.getFontMetrics().stringWidth(caption);
		int captionHeight = g.getFontMetrics().getHeight() / 2;
		
		Point p = position.calculate(
				imageWidth,	imageHeight, captionWidth, 0,
				insets, insets, insets, insets
		);

		double yRatio = p.y / (double)img.getHeight();
		int yOffset = (int)((1.0 - yRatio) * captionHeight);
		
		g.drawString(caption, p.x, p.y + yOffset);
		
		g.dispose();
		
		return newImage;
	}
}
