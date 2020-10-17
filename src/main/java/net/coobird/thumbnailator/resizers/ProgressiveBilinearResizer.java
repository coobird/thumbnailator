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

import net.coobird.thumbnailator.builders.BufferedImageBuilder;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Map;

/**
 * A {@link Resizer} which performs resizing operations by using
 * progressive bilinear scaling.
 * <p>
 * The resizing technique used in this class is based on the technique
 * discussed in <em>Chapter 4: Images</em> of
 * <a href="http://filthyrichclients.org">Filthy Rich Clients</a>
 * by Chet Haase and Romain Guy.
 * <p>
 * The actual implemenation of the technique is independent of the code which
 * is provided in the book.
 * 
 * @author coobird
 *
 */
public class ProgressiveBilinearResizer extends AbstractResizer {
	/**
	 * Instantiates a {@link ProgressiveBilinearResizer} with default
	 * rendering hints.
	 */
	public ProgressiveBilinearResizer() {
		this(Collections.<RenderingHints.Key, Object>emptyMap());
	}
	
	/**
	 * Instantiates a {@link ProgressiveBilinearResizer} with the specified
	 * rendering hints.
	 * 
	 * @param hints		Additional rendering hints to apply.
	 */
	public ProgressiveBilinearResizer(Map<RenderingHints.Key, Object> hints) {
		super(RenderingHints.VALUE_INTERPOLATION_BILINEAR, hints);
	}
	
	/**
	 * Resizes an image using the progressive bilinear scaling technique.
	 * <p>
	 * If the source and/or destination image is {@code null}, then a
	 * {@link NullPointerException} will be thrown.
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
		super.performChecks(srcImage, destImage);
		
		int currentWidth = srcImage.getWidth();
		int currentHeight = srcImage.getHeight();
		
		final int targetWidth = destImage.getWidth();
		final int targetHeight = destImage.getHeight();
		
		// If multi-step downscaling is not required, perform one-step.
		if ((targetWidth * 2 >= currentWidth) && (targetHeight * 2 >= currentHeight)) {
			Graphics2D g = createGraphics(destImage);
			g.drawImage(srcImage, 0, 0, targetWidth, targetHeight, null);
			g.dispose();
			return;
		}
		
		// Temporary image used for in-place resizing of image.
		BufferedImage tempImage = new BufferedImageBuilder(
				currentWidth,
				currentHeight,
				destImage.getType()
		).build();
		
		Graphics2D g = createGraphics(tempImage);
		g.setComposite(AlphaComposite.Src);
		
		/*
		 * Determine the size of the first resize step should be.
		 * 1) Beginning from the target size
		 * 2) Increase each dimension by 2
		 * 3) Until reaching the original size
		 */
		int startWidth = targetWidth;
		int startHeight = targetHeight;
		
		while (startWidth < currentWidth && startHeight < currentHeight) {
			startWidth *= 2;
			startHeight *= 2;
		}
		
		currentWidth = startWidth / 2;
		currentHeight = startHeight / 2;

		// Perform first resize step.
		g.drawImage(srcImage, 0, 0, currentWidth, currentHeight, null);
		
		// Perform an in-place progressive bilinear resize.
		while (	(currentWidth >= targetWidth * 2) && (currentHeight >= targetHeight * 2) ) {
			currentWidth /= 2;
			currentHeight /= 2;
			
			if (currentWidth < targetWidth) {
				currentWidth = targetWidth;
			}
			if (currentHeight < targetHeight) {
				currentHeight = targetHeight;
			}
			
			g.drawImage(
					tempImage,
					0, 0, currentWidth, currentHeight,
					0, 0, currentWidth * 2, currentHeight * 2,
					null
			);
		}
		
		g.dispose();
		
		// Draw the resized image onto the destination image.
		Graphics2D destg = createGraphics(destImage);
		destg.drawImage(tempImage, 0, 0, targetWidth, targetHeight, 0, 0, currentWidth, currentHeight, null);
		destg.dispose();
	}
}
