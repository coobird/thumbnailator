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

import java.awt.Dimension;


/**
 * This class provides factory methods which provides suitable {@link Resizer}s
 * for a given situation.
 *
 * <dl>
 * <dt>{@code Resizer}s returned by this {@code ResizerFactory}:</dt>
 * <dd>
 * The {@link Resizer}s returned by this {@link ResizerFactory} depends upon
 * the size of the source and destination images. The conditions and the
 * {@link Resizer}s returned are as follows:
 * 
 * <ul>
 * <li>Default via {@link #getResizer()}
 * 	<ul><li>{@link ProgressiveBilinearResizer}</li></ul>
 * </li>
 * <li>Destination image has the same dimensions as the source image via
 * {@link #getResizer(Dimension, Dimension)}
 * 	<ul><li>{@link NullResizer}</li></ul>
 * </li>
 * <li>Both the width and height of the destination image is larger than the
 * source image via {@link #getResizer(Dimension, Dimension)}
 * 	<ul><li>{@link BicubicResizer}</li></ul>
 * </li>
 * <li>Both the width and height of the destination image is smaller in the
 * source image by a factor larger than 2,
 * via {@link #getResizer(Dimension, Dimension)}
 * 	<ul><li>{@link ProgressiveBilinearResizer}</li></ul>
 * </li>
 * <li>Both the width and height of the destination image is smaller in the
 * source image not by a factor larger than 2,
 * via {@link #getResizer(Dimension, Dimension)}
 * 	<ul><li>{@link BilinearResizer}</li></ul>
 * </li>
 * <li>Other conditions not described via
 * {@link #getResizer(Dimension, Dimension)}
 * 	<ul><li>{@link ProgressiveBilinearResizer}</li></ul>
 * </li>
 * </ul>
 * </dd>
 * </dl>
 * 
 * <DL>
 * <DT><B>Usage:</B></DT>
 * <DD>
 * The following example code demonstrates how to use {@link DefaultResizerFactory}
 * in order to obtain the optimal {@link Resizer}, and using that in order to
 * perform the resizing operation.
 * <p>
 * <pre>
BufferedImage sourceImage = new BufferedImageBuilder(400, 400).build();
BufferedImage destImage = new BufferedImageBuilder(200, 200).build();

Dimension sourceSize = new Dimension(sourceImage.getWidth(), sourceImage.getHeight());
Dimension destSize = new Dimension(destImage.getWidth(), destImage.getHeight());

// Obtain the optimal Resizer for this resizing operation.
Resizer resizer = DefaultResizerFactory.getInstance().getResizer(sourceSize, destSize);

// Perform the resizing using the Resizer obtained from the ResizerFactory.
resizer.resize(sourceImage, destImage);
 * </pre>
 * </DD>
 * </DL>
 * When a specific {@link Resizer} is required, the {@link Resizers}
 * {@code enum} is another way to obtain {@link Resizer}s.
 * <p>
 * 
 * @see Resizers
 *
 * @author coobird
 * @since	0.4.0
 *
 */
public class DefaultResizerFactory implements ResizerFactory {
	private static final DefaultResizerFactory INSTANCE = new DefaultResizerFactory();

	/**
	 * This class is not intended to be instantiated via the constructor.
	 */
	private DefaultResizerFactory() {}
	
	/**
	 * Returns an instance of this class.
	 * 
	 * @return		An instance of this class.
	 */
	public static ResizerFactory getInstance() {
		return INSTANCE;
	}
	
	public Resizer getResizer() {
		return Resizers.PROGRESSIVE;
	}
	
	public Resizer getResizer(Dimension originalSize, Dimension thumbnailSize) {
		int origWidth = originalSize.width;
		int origHeight = originalSize.height;
		int thumbWidth = thumbnailSize.width;
		int thumbHeight = thumbnailSize.height;
		
		if (thumbWidth < origWidth && thumbHeight < origHeight) {
			if (thumbWidth < (origWidth / 2) && thumbHeight < (origHeight / 2)) {
				return Resizers.PROGRESSIVE;
			} else {
				return Resizers.BILINEAR;
			}
		}
		else if (thumbWidth > origWidth && thumbHeight > origHeight) {
			return Resizers.BICUBIC;
		} else if (thumbWidth == origWidth && thumbHeight == origHeight) {
			return Resizers.NULL;
		} else {
			return getResizer();
		}
	}
}
