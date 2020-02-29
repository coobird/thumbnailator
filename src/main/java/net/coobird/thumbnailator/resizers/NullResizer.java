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

import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Map;

/**
 * A {@link Resizer} which does not actually resize the image.
 * <p>
 * The source image will be drawn at the origin of the destination image.
 * 
 * @author coobird
 * @since	0.4.0
 *
 */
public class NullResizer extends AbstractResizer {
	/**
	 * Instantiates the {@code NullResizer} which draws the source image at
	 * the origin of the destination image.
	 */
	public NullResizer() {
		this(
				RenderingHints.VALUE_INTERPOLATION_BILINEAR,
				Collections.<Key, Object>emptyMap()
		);
	}
	
	/**
	 * This constructor is {@code private} to prevent the rendering hints
	 * from being set, as this {@link Resizer} does not perform any resizing.
	 * 
	 * @param interpolationValue		Not used.
	 * @param hints						Not used.
	 */
	private NullResizer(Object interpolationValue, Map<Key, Object> hints) {
		super(interpolationValue, hints);
	}

	public void resize(BufferedImage srcImage, BufferedImage destImage) {
		super.performChecks(srcImage, destImage);
		
		Graphics g = destImage.getGraphics();
		g.drawImage(srcImage, 0, 0, null);
		g.dispose();
	}
}
