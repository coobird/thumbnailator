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

package net.coobird.thumbnailator.geometry;

import java.awt.Dimension;

/**
 * Calculates the size of an enclosed object relative to the enclosing object.
 * 
 * @author coobird
 * @since	0.3.4
 *
 */
public class RelativeSize implements Size {
	/**
	 * The scaling factor to use for the enclosed object.
	 */
	private final double scalingFactor;
	
	/**
	 * Instantiates an object which calculates the size of an object, using
	 * the given scaling factor.
	 * 
	 * @param scalingFactor		The scaling factor to use to determine the
	 * 							size of the enclosing object.
	 * @throws IllegalArgumentException		When the scaling factor is not within
	 * 										the range of {@code 0.0d} and
	 * 										{@code 1.0d}, inclusive.
	 */
	public RelativeSize(double scalingFactor) {
		super();
		if (scalingFactor < 0.0d || scalingFactor > 1.0d) {
			throw new IllegalArgumentException(
					"The scaling factor must be between 0.0d and 1.0d, inclusive."
			);
		}
		this.scalingFactor = scalingFactor;
	}

	public Dimension calculate(int width, int height) {
		if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException(
					"Width and height must be greater than 0."
			);
		}
		int newWidth = (int)Math.round(width * scalingFactor);
		int newHeight = (int)Math.round(height * scalingFactor);
		return new Dimension(newWidth, newHeight);
	}

	/** 
	 * Returns a {@code String} representation of this object.
	 * 
	 * @return		{@code String} representation of this object.
	 */
	@Override
	public String toString() {
		return "RelativeSize [scalingFactor=" + scalingFactor + "]";
	}
}
