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
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.geometry.Position;

/**
 * An {@link ImageFilter} which will enclose an image into a specified
 * enclosing image.
 * <p>
 * The intended use of this {@code ImageFilter} is to take an image and place
 * it inside of a larger image, creating a border around the original image.
 * This can be useful when the dimensions of a thumbnail must always be the
 * same dimensions, and the original images are of differing dimensions.
 * <p>
 * The fill color used for the enclosing image can be specified, along with
 * whether or not to crop an image if it is larger than the enclosing image.
 * 
 * @author coobird
 * @since 0.3.2
 *
 */
public class Canvas implements ImageFilter {
	/**
	 * The width of the enclosing image.
	 */
	private final int width;
	
	/**
	 * The width of the enclosing image.
	 */
	private final int height;
	
	/**
	 * The positioning of the enclosed image.
	 */
	private final Position position;
	
	/**
	 * The fill color for the background.
	 */
	private final Color fillColor;
	
	/**
	 * Whether or not to crop the enclosed image if the enclosing image is
	 * smaller than the enclosed image.
	 */
	private final boolean crop;
	
	/**
	 * Instantiates a {@code Canvas} filter.
	 * <p>
	 * No fill color will be applied to the filtered image. If the image to
	 * filter does not have a transparency channel, the image will be filled
	 * black.
	 * <p>
	 * Crops the enclosed image if the enclosing image is smaller.
	 * 
	 * @param width			The width of the filtered image.
	 * @param height		The height of the filtered image.
	 * @param position		The position to place the enclosed image.
	 */
	public Canvas(int width, int height, Position position) {
		this(width, height, position, true, null);
	}
	
	/**
	 * Instantiates a {@code Canvas} filter.
	 * <p>
	 * No fill color will be applied to the filtered image. If the image to
	 * filter does not have a transparency channel, the image will be filled
	 * black.
	 * 
	 * @param width			The width of the filtered image.
	 * @param height		The height of the filtered image.
	 * @param position		The position to place the enclosed image.
	 * @param crop			Whether or not to crop the enclosed image if the
	 * 						enclosed image has dimensions which are larger than
	 * 						the specified {@code width} and {@code height}.
	 */
	public Canvas(int width, int height, Position position, boolean crop) {
		this(width, height, position, crop, null);
	}
	
	/**
	 * Instantiates a {@code Canvas} filter.
	 * <p>
	 * Crops the enclosed image if the enclosing image is smaller.
	 * 
	 * @param width			The width of the filtered image.
	 * @param height		The height of the filtered image.
	 * @param position		The position to place the enclosed image.
	 * @param fillColor		The color to fill portions of the image which is
	 * 						not covered by the enclosed image. Portions of the
	 * 						image which is transparent will be filled with
	 * 						the specified color as well.
	 */
	public Canvas(int width, int height, Position position, Color fillColor) {
		this(width, height, position, true, fillColor);
	}
	
	/**
	 * Instantiates a {@code Canvas} filter.
	 * 
	 * @param width			The width of the filtered image.
	 * @param height		The height of the filtered image.
	 * @param position		The position to place the enclosed image.
	 * @param crop			Whether or not to crop the enclosed image if the
	 * 						enclosed image has dimensions which are larger than
	 * 						the specified {@code width} and {@code height}.
	 * @param fillColor		The color to fill portions of the image which is
	 * 						not covered by the enclosed image. Portions of the
	 * 						image which is transparent will be filled with
	 * 						the specified color as well.
	 */
	public Canvas(int width, int height, Position position, boolean crop, Color fillColor) {
		super();
		this.width = width;
		this.height = height;
		this.position = position;
		this.crop = crop;
		this.fillColor = fillColor;
	}

	public BufferedImage apply(BufferedImage img) {
		int widthToUse = width;
		int heightToUse = height;
		
		/*
		 * To prevent cropping when cropping is disabled, if the dimension of
		 * the enclosed image exceeds the dimension of the enclosing image,
		 * then the enclosing image will have its dimension enlarged.
		 * 
		 */
		if (!crop && img.getWidth() > width) {
			widthToUse = img.getWidth();
		}
		if (!crop && img.getHeight() > height) {
			heightToUse = img.getHeight();
		}
		
		Point p = position.calculate(
				widthToUse, heightToUse, img.getWidth(), img.getHeight(),
				0, 0, 0, 0
		);
		
		BufferedImage finalImage = new BufferedImageBuilder(
				widthToUse,
				heightToUse,
				img.getType()
		).build();
		
		Graphics g = finalImage.getGraphics();
		
		if (fillColor == null && !img.getColorModel().hasAlpha()) {
			/*
			 * Fulfills the specification to use a black fill color for images
			 * w/o alpha, if the fill color isn't specified.
			 */
			g.setColor(Color.black);
			g.fillRect(0, 0, width, height);

		} else if (fillColor != null) {
			g.setColor(fillColor);
			g.fillRect(0, 0, widthToUse, heightToUse);
		}
		
		g.drawImage(img, p.x, p.y, null);
		g.dispose();
		
		return finalImage;
	}
}
