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

package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.IOException;

import net.coobird.thumbnailator.ThumbnailParameter;

/**
 * An abstract class for {@link ImageSink}s.
 * 
 * @author coobird
 *
 */
public abstract class AbstractImageSink<T> implements ImageSink<T> {
	/**
	 * The name of the format to output the image as.
	 */
	protected String outputFormat;
	
	/**
	 * The parameters that should be used when storing the image.
	 */
	protected ThumbnailParameter param;
	
	/**
	 * Default constructor.
	 */
	protected AbstractImageSink() {}
	
	public void setOutputFormatName(String format) {
		outputFormat = format;
	}
	
	public void setThumbnailParameter(ThumbnailParameter param) {
		this.param = param;
	}

	public void write(BufferedImage img) throws IOException {
		if (img == null) {
			throw new NullPointerException("Cannot write a null image.");
		}
		
		if (ThumbnailParameter.DETERMINE_FORMAT.equals(outputFormat)) {
			outputFormat = preferredOutputFormatName();
		}
	}
	
	public String preferredOutputFormatName() {
		return ThumbnailParameter.ORIGINAL_FORMAT;
	}
}
