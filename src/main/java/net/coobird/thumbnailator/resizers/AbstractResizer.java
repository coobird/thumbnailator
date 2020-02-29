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

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A class which performs a resize operation on a source image and outputs the
 * result to a destination image.
 * 
 * @author coobird
 *
 */
public abstract class AbstractResizer implements Resizer {
	/**
	 * Rendering hints to use when resizing an image.
	 */
	protected final Map<RenderingHints.Key, Object> RENDERING_HINTS;
	
	/**
	 * Field used to hold the rendering hints in an unmodifiable form.
	 */
	protected final Map<RenderingHints.Key, Object> UNMODIFIABLE_RENDERING_HINTS;
	
	protected static final RenderingHints.Key KEY_INTERPOLATION =
		RenderingHints.KEY_INTERPOLATION;

	/**
	 * Initializes the {@link AbstractResizer}.
	 * 
	 * @param interpolationValue		The rendering hint value to use for the
	 * 									interpolation hint.
	 * @param hints						Other rendering hints to add.
	 */
	protected AbstractResizer(
			Object interpolationValue,
			Map<RenderingHints.Key, Object> hints
	) {
		RENDERING_HINTS = new HashMap<RenderingHints.Key, Object>();
		RENDERING_HINTS.put(KEY_INTERPOLATION, interpolationValue);
		
		if (
				hints.containsKey(KEY_INTERPOLATION)
				&& !interpolationValue.equals(hints.get(KEY_INTERPOLATION))
		) {
			throw new IllegalArgumentException("Cannot change the " +
					"RenderingHints.KEY_INTERPOLATION value.");
		}
		
		RENDERING_HINTS.putAll(hints);
		
		UNMODIFIABLE_RENDERING_HINTS = Collections.unmodifiableMap(RENDERING_HINTS);
	}
	
	/**
	 * <p>
	 * Performs a resize operation from a source image and outputs to a
	 * destination image.
	 * </p>
	 * <p>
	 * If the source or destination image is {@code null}, then a
	 * {@link NullPointerException} will be thrown.
	 * </p>
	 * 
	 * @param srcImage		The source image.
	 * @param destImage		The destination image.
	 * 
	 * @throws NullPointerException		When the source and/or the destination
	 * 									image is {@code null}.
	 */
	public void resize(BufferedImage srcImage, BufferedImage destImage) {
		performChecks(srcImage, destImage);
		
		int width = destImage.getWidth();
		int height = destImage.getHeight();
		
		Graphics2D g = createGraphics(destImage);
		g.drawImage(srcImage, 0, 0, width, height, null);
		g.dispose();
	}
	
	/**
	 * Returns a {@link Graphics2D} object with rendering hints pre-applied.
	 * @param img	{@link BufferedImage} for which the {@link Graphics2D}
	 * 				object should be generated for. 
	 * @return	{@link Graphics2D} object for the given {@link BufferedImage}.
	 */
	protected Graphics2D createGraphics(BufferedImage img) {
		Graphics2D g = img.createGraphics();
		g.setRenderingHints(RENDERING_HINTS);
		
		return g;
	}
	
	/**
	 * Performs checks on the source and destination image to see if they are
	 * images which can be processed.
	 * 
	 * @param srcImage		The source image.
	 * @param destImage		The destination image.
	 */
	protected void performChecks(BufferedImage srcImage, BufferedImage destImage) {
		if (srcImage == null || destImage == null) {
			throw new NullPointerException(
					"The source and/or destination image is null."
			);
		}
	}
	
	/**
	 * Returns the rendering hints that the resizer uses.
	 * <p>
	 * The keys and values used for the rendering hints are those defined in
	 * the {@link RenderingHints} class.
	 * 
	 * @see RenderingHints
	 * @return		Rendering hints used when resizing the image.
	 */
	public Map<RenderingHints.Key, Object> getRenderingHints() {
		return UNMODIFIABLE_RENDERING_HINTS;
	}
}
