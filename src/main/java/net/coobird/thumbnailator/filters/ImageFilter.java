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

package net.coobird.thumbnailator.filters;

import java.awt.image.BufferedImage;

/**
 * This interface is to be implemented by classes which performs an image
 * filtering operation on a {@link BufferedImage}.
 * <p>
 * The general contract for classes implementing {@link ImageFilter} is that
 * they should not change the contents of the {@link BufferedImage} which is
 * given as the argument for the {@link #apply(BufferedImage)} method.
 * <p>
 * The filter should make a copy of the given {@link BufferedImage}, and
 * perform the filtering operations on the copy, then return the copy.
 * 
 * @author coobird
 *
 */
public interface ImageFilter {
	/**
	 * Applies a image filtering operation on an image.
	 * 
	 * @param img		The image to apply the filtering on.
	 * @return			The resulting image after applying this filter.
	 */
	public BufferedImage apply(BufferedImage img);
}
