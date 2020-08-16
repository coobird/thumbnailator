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

import java.awt.image.BufferedImage;

/**
 * This enum can be used to select a specific {@link Resizer} in order
 * to perform a resizing operation.
 * <p>
 * The instance held by a value of this enum is a single instance. When using
 * specific implementations of {@link Resizer}s, it is preferable to obtain
 * an instance of a {@link Resizer} through this enum or the
 * {@link DefaultResizerFactory} class in order to prevent many instances of the
 * {@link Resizer} class implementations from being instantiated.
 * <p>
 * <DL>
 * <DT><B>Usage:</B></DT>
 * <DD>
 * The following example code demonstrates how to use the {@link Resizers} enum
 * in order to resize an image using bilinear interpolation:
 * <p>
 * <pre>
BufferedImage sourceImage = new BufferedImageBuilder(400, 400).build();
BufferedImage destImage = new BufferedImageBuilder(200, 200).build();

Resizers.BILINEAR.resize(sourceImage, destImage);
 * </pre>
 * </DD>
 * </DL>
 * 
 * @see DefaultResizerFactory
 * 
 * @author coobird
 *
 */
public enum Resizers implements Resizer {
	/**
	 * A {@link Resizer} which does not perform resizing operations. The source
	 * image will be drawn at the origin of the destination image.
	 */
	NULL(new NullResizer()),
	
	/**
	 * A {@link Resizer} which performs resizing operations using
	 * bilinear interpolation.
	 */
	BILINEAR(new BilinearResizer()),
	
	/**
	 * A {@link Resizer} which performs resizing operations using
	 * bicubic interpolation.
	 */
	BICUBIC(new BicubicResizer()),

	/**
	 * A {@link Resizer} which performs resizing operations using
	 * progressive bilinear scaling.
	 * <p>
	 * For details on this technique, refer to the documentation of the
	 * {@link ProgressiveBilinearResizer} class.
	 */
	PROGRESSIVE(new ProgressiveBilinearResizer())
	;
	
	private final Resizer resizer;
	
	private Resizers(Resizer resizer) {
		this.resizer = resizer;
	}

	public void resize(BufferedImage srcImage, BufferedImage destImage) {
		resizer.resize(srcImage, destImage);
	}
}
