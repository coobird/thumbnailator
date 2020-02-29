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
 * An interface to be implemented by classes which stores the image resulting
 * from a thumbnail generation task.
 * 
 * @param <T> 		The destination class to which the thumbnail is stored
 * 					or written.
 *
 * @author coobird
 *
 */
public interface ImageSink<T> {
	/**
	 * Writes the resulting image to a destination.
	 * 
	 * @param img				The image to write or store.
	 * @throws IOException		When a problem occurs while writing or storing
	 * 							the image.
	 * @throws NullPointerException		If the image is {@code null}.
	 */
	public void write(BufferedImage img) throws IOException;
	
	/**
	 * Sets the output format of the resulting image.
	 * <p>
	 * For {@link ImageSink}s which stores raw images, the format name specified
	 * by this method may be ignored.
	 * 
	 * @param format			File format with which to store the image.
	 */
	public void setOutputFormatName(String format);
	
	/**
	 * Sets the {@link ThumbnailParameter} from which to retrieve parameters
	 * to use when storing the image.
	 * 
	 * @param param				The {@link ThumbnailParameter} with image
	 * 							writing parameters.
	 */
	public void setThumbnailParameter(ThumbnailParameter param);
	
	/**
	 * Returns the output format to use from information provided for the
	 * output image.
	 * <p>
	 * If the output format cannot be determined, then
	 * {@link ThumbnailParameter#ORIGINAL_FORMAT} should be returned.
	 */
	public String preferredOutputFormatName();
	
	/**
	 * Returns the destination to which the thumbnail will be stored or
	 * written.
	 * 
	 * @return					The destination for the thumbnail image.
	 */
	public T getSink();
}
