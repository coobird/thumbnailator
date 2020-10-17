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

package net.coobird.thumbnailator.builders;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 * A builder for creating {@link BufferedImage} with specified parameters.
 * 
 * @author coobird
 *
 */
public final class BufferedImageBuilder {
	/**
	 * The default image type of the {@link BufferedImage}s to be created
	 * by this builder.
	 */
	private static final int DEFAULT_TYPE = BufferedImage.TYPE_INT_ARGB;
	
	/**
	 * The image type to use for the {@link BufferedImage} that is to be
	 * created.
	 */
	private int imageType;
	
	/**
	 * The width to use for the {@link BufferedImage} that is to be created.
	 */
	private int width;
	
	/**
	 * The height to use for the {@link BufferedImage} that is to be created.
	 */
	private int height;
	
	/**
	 * Instantiates a {@code BufferedImageBuilder} with the specified size, and
	 * the default image type.
	 * 
	 * @param size			The size of the {@link BufferedImage} to build.
	 */
	public BufferedImageBuilder(Dimension size) {
		this(size.width, size.height);
	}
	
	/**
	 * Instantiates a {@code BufferedImageBuilder} with the specified size and
	 * image type.
	 * 
	 * @param size			The size of the {@link BufferedImage} to build.
	 * @param imageType		The image type of the {@link BufferedImage} to build.
	 */
	public BufferedImageBuilder(Dimension size, int imageType) {
		this(size.width, size.height, imageType);
	}
	
	/**
	 * Instantiates a {@code BufferedImageBuilder} with the specified size, and
	 * the default image type.
	 * 
	 * @param width			The width of the {@link BufferedImage} to build.
	 * @param height		The height of the {@link BufferedImage} to build.
	 */
	public BufferedImageBuilder(int width, int height) {
		this(width, height, DEFAULT_TYPE);
	}
	
	/**
	 * Instantiates a {@code BufferedImageBuilder} with the specified size and
	 * image type.
	 * 
	 * @param width			The width of the {@link BufferedImage} to build.
	 * @param height		The height of the {@link BufferedImage} to build.
	 * @param imageType		The image type of the {@link BufferedImage} to build.
	 */
	public BufferedImageBuilder(int width, int height, int imageType) {
		size(width, height);
		imageType(imageType);
	}
	
	/**
	 * Generates a new {@code BufferedImage}.
	 * 
	 * @return		Returns a newly created {@link BufferedImage} from the
	 * 				parameters set in the {@link BufferedImageBuilder}.
	 */
	public BufferedImage build() {
		return new BufferedImage(width, height, imageType);
	}

	/**
	 * Sets the type of the image of the {@link BufferedImage}.
	 * 
	 * @param imageType		The image type to use.
	 * 						If {@link BufferedImage#TYPE_CUSTOM} is used, it
	 * 						be substituted by {@link BufferedImage#TYPE_INT_ARGB}.
	 * @return				This {@link BufferedImageBuilder} instance.
	 */
	public BufferedImageBuilder imageType(int imageType) {
		if (imageType == BufferedImage.TYPE_CUSTOM) {
			imageType = DEFAULT_TYPE;
		}

		this.imageType = imageType;
		return this;
	}
	
	/**
	 * Sets the size for the {@code BufferedImage}.
	 * 
	 * @param width			The width of the image to create.
	 * @param height		The height of the image to create.
	 * @return				This {@link BufferedImageBuilder} instance.
	 */
	public BufferedImageBuilder size(int width, int height) {
		width(width);
		height(height);
		return this;
	}
	
	/**
	 * Sets the width for the {@link BufferedImage}.
	 * 
	 * @param width			The width of the image to create.
	 * @return				This {@link BufferedImageBuilder} instance.
	 */
	public BufferedImageBuilder width(int width) {
		if (width <= 0) {
			throw new IllegalArgumentException(
					"Width must be greater than 0."
			);
		}
		
		this.width = width;
		return this;
	}
	
	/**
	 * Sets the height for the {@link BufferedImage}.
	 * 
	 * @param height		The height of the image to create.
	 * @return				This {@link BufferedImageBuilder} instance.
	 */
	public BufferedImageBuilder height(int height) {
		if (height <= 0) {
			throw new IllegalArgumentException(
					"Height must be greater than 0."
			);
		}

		this.height = height;
		return this;
	}
}
