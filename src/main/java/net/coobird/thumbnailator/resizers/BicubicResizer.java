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

package net.coobird.thumbnailator.resizers;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Map;

/**
 * Image resizer class using bicubic interpolation for the resizing operation.
 * 
 * @author coobird
 *
 */
public class BicubicResizer extends AbstractResizer {
	/**
	 * Instantiates a {@link BicubicResizer} with default rendering hints.
	 */
	public BicubicResizer() {
		this(Collections.<RenderingHints.Key, Object>emptyMap());
	}
	
	/**
	 * Instantiates a {@link BicubicResizer} with the specified rendering hints.
	 * 
	 * @param hints		Additional rendering hints to apply.
	 */
	public BicubicResizer(Map<RenderingHints.Key, Object> hints) {
		super(RenderingHints.VALUE_INTERPOLATION_BICUBIC, hints);
	}
	
	/**
	 * <p>
	 * Resizes an image using bicubic interpolation.
	 * </p>
	 * <p>
	 * If the source and/or destination image is {@code null}, then a
	 * {@link NullPointerException} will be thrown.
	 * </p>
	 * 
	 * @param srcImage		The source image.
	 * @param destImage		The destination image.
	 * 
	 * @throws NullPointerException		When the source and/or the destination
	 * 									image is {@code null}.
	 */
	@Override
	public void resize(BufferedImage srcImage, BufferedImage destImage)
			throws NullPointerException {
		super.resize(srcImage, destImage);
	}
}
