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

import net.coobird.thumbnailator.ThumbnailParameter;

/**
 * An abstract class for {@link ImageSource}s.
 * 
 * @author coobird
 *
 */
public abstract class AbstractImageSource<T> implements ImageSource<T> {
	/**
	 * The image format of the input image.
	 */
	protected String inputFormatName;
	
	/**
	 * The parameters that should be used when retrieving the image.
	 */
	protected ThumbnailParameter param;
	
	/**
	 * Indicates whether the input has already been read.
	 */
	protected boolean hasReadInput = false;
	
	/**
	 * Default constructor.
	 */
	protected AbstractImageSource() {}
	
	/**
	 * Indicates that the {@link ImageSource} has completed reading the input
	 * file, and returns the value given in the argument.
	 * <p>
	 * This method should be used by implementation classes when returning
	 * the result of the {@link #read()} method, as shown in the following
	 * example code:
<pre>
return finishedReading(sourceImage);
</pre>
	 * 
	 * @param <V>			The return value type.
	 * @param returnValue	The return value of the {@link #read()} method.
	 * @return				The return value of the {@link #read()} method.
	 */
	protected <V> V finishedReading(V returnValue) {
		hasReadInput = true;
		return returnValue;
	}
	
	public void setThumbnailParameter(ThumbnailParameter param) {
		this.param = param;
	}
	
	public String getInputFormatName() {
		if (!hasReadInput) {
			throw new IllegalStateException("Input has not been read yet.");
		}
		return inputFormatName;
	}
}
