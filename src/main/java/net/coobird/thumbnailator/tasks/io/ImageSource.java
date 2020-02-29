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
 * An interface to be implemented by classes which read or retrieve images
 * from which a thumbnail should be produced.
 * 
 * @param <T> 		The source class from which the source image is retrieved
 * 					or read.
 * @author coobird
 *
 */
public interface ImageSource<T> {
	/**
	 * Retrieves the image from the source.
	 * 
	 * @return					The image.
	 * @throws IOException		When a problem occurs while reading or obtaining
	 * 							the image.
	 */
	public BufferedImage read() throws IOException;
	
	/**
	 * Returns the name of the image format.
	 * 
	 * @return							The image format name. If there is no
	 * 									image format information, then
	 * 									{@code null} will be returned.
	 * @throws IllegalStateException	If the source image has not been
	 * 									read yet.
	 */
	public String getInputFormatName();
	
	/**
	 * Sets the {@link ThumbnailParameter} from which to retrieve parameters
	 * to use when retrieving the image.
	 * 
	 * @param param				The {@link ThumbnailParameter} with image
	 * 							reading parameters.
	 */
	public void setThumbnailParameter(ThumbnailParameter param);	
	
	/**
	 * Returns the source from which the image is read or retrieved.
	 * 
	 * @return					The source of the image.
	 */
	public T getSource();
}
