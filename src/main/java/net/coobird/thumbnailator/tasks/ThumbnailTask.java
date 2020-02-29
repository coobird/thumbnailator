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

package net.coobird.thumbnailator.tasks;

import java.awt.image.BufferedImage;
import java.io.IOException;

import net.coobird.thumbnailator.ThumbnailParameter;

/**
 * This class is used by {@link ThumbnailTask} implementations which is used
 * when creating thumbnails from external sources and destinations.
 * <p>
 * If the image handled by a {@link ThumbnailTask} contains multiple images,
 * only the first image will be read by the {@link #read()} method. Any
 * subsequent images will be ignored.
 * 
 * @param <S>		The class from which the image is retrieved or read.
 * @param <D>		The class to which the thumbnail is stored or written.
 * 
 * @author coobird
 *
 */
public abstract class ThumbnailTask<S, D> {
	/**
	 * The parameters to use when creating a thumbnail.
	 */
	protected final ThumbnailParameter param;
	
	/**
	 * String indicating the image format of the input image.
	 * <p>
	 * To be used for situtions where the output image format should be the
	 * same as the input image format.
	 */
	protected String inputFormatName;
	
	/**
	 * Constant used to obtain the first image when reading an image file.
	 */
	protected static final int FIRST_IMAGE_INDEX = 0;
	
	/**
	 * Instantiates a {@link ThumbnailTask} with the parameters to use when
	 * creating thumbnails.
	 * 
	 * @param param			The parameters to use when creating thumbnails.
	 * @throws NullPointerException		If the parameter is {@code null}.
	 */
	protected ThumbnailTask(ThumbnailParameter param) {
		if (param == null) {
			throw new NullPointerException("The parameter is null.");
		}
		this.param = param;
	}
	
	/**
	 * Reads a source image.
	 * 
	 * @return					The image which was obtained from the source.
	 * @throws IOException		Thrown when an I/O problem occurs when reading
	 * 							from the image source.
	 */
	/*
	 * Future changes note: The public interface of this method may have to be
	 * changed to support reading images tile-by-tile. This change may be
	 * required in order to support large images.
	 */
	public abstract BufferedImage read() throws IOException;
	
	/**
	 * Writes the thumbnail to the destination.
	 * 
	 * @param img			The image to write.
	 * @throws UnsupportedFormatException	When an image file which is to be
	 * 										read or written is unsupported.
	 * @throws IOException	Thrown when an I/O problem occurs when writing the
	 * 						image.
	 */
	public abstract void write(BufferedImage img) throws IOException;
	
	/**
	 * Returns the {@link ThumbnailParameter} for this {@link ThumbnailTask},
	 * used when performing a thumbnail generation operation.
	 * 
	 * @return				The parameters to use when generating thumbnails.
	 */
	public ThumbnailParameter getParam() {
		return param;
	}
	
	/**
	 * Returns the source from which the source image is retrieved or read.
	 * 
	 * @return		The source.
	 */
	public abstract S getSource();
	
	/**
	 * Returns the destination to which the thumbnail is stored or written.
	 * 
	 * @return		The destination.
	 */
	public abstract D getDestination();
}
