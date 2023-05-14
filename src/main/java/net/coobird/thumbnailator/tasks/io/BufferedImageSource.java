/*
 * Thumbnailator - a thumbnail generation library
 *
 * Copyright (c) 2008-2023 Chris Kroells
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

package net.coobird.thumbnailator.tasks.io;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import net.coobird.thumbnailator.geometry.Region;

/**
 * An {@link ImageSource} which uses a {@link BufferedImage} as the source
 * image.
 * 
 * @author coobird
 *
 */
public class BufferedImageSource extends AbstractImageSource<BufferedImage> {
	/**
	 * The image that should be used as the source for making a thumbnail.
	 */
	private final BufferedImage img;
	
	/**
	 * Instantiates a {@link BufferedImageSource} object with the
	 * {@link BufferedImage} that should be used as the source image for making
	 * thumbnails.
	 * 
	 * @param img		The source image.
	 * @throws NullPointerException		If the image is null.
	 */
	public BufferedImageSource(BufferedImage img) {
		super();
		
		if (img == null) {
			throw new NullPointerException("Image cannot be null.");
		}
		
		this.img = img;
	}

	public BufferedImage read() throws IOException {
		inputFormatName = null;
		
		if (param != null && param.getSourceRegion() != null) {
			Region region = param.getSourceRegion();
			Rectangle r = region.calculate(
					img.getWidth(), img.getHeight(), false, false, false
			);
			
			return finishedReading(img.getSubimage(r.x, r.y, r.width, r.height));
		} else {
			return finishedReading(img);
		}
	}

	public BufferedImage getSource() {
		return img;
	}
}
